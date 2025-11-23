package server

import (
	"context"
	"encoding/json"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/battle-pass-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type BattlePassServiceInterface interface {
	GetCurrentSeason(ctx context.Context) (*models.BattlePassSeason, error)
	GetSeasonByID(ctx context.Context, id uuid.UUID) (*models.BattlePassSeason, error)
	CreateSeason(ctx context.Context, season *models.BattlePassSeason) error
	GetProgress(ctx context.Context, characterID, seasonID uuid.UUID) (*models.PlayerBattlePassProgress, error)
	AwardXP(ctx context.Context, characterID uuid.UUID, xpAmount int, source models.XPSource) (*models.PlayerBattlePassProgress, error)
	PurchasePremium(ctx context.Context, characterID uuid.UUID) (*models.PlayerBattlePassProgress, error)
	GetRewards(ctx context.Context, seasonID uuid.UUID) ([]models.BattlePassReward, error)
	ClaimReward(ctx context.Context, characterID, rewardID uuid.UUID) error
	GetWeeklyChallenges(ctx context.Context, characterID uuid.UUID) ([]models.WeeklyChallenge, error)
	CompleteChallenge(ctx context.Context, characterID, challengeID uuid.UUID) error
	GetLevelRequirements(ctx context.Context, level int) (*models.LevelRequirements, error)
}

type BattlePassService struct {
	repo     BattlePassRepositoryInterface
	cache    *redis.Client
	logger   *logrus.Logger
	eventBus EventBus
}

type EventBus interface {
	PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error
}

type RedisEventBus struct {
	client *redis.Client
	logger *logrus.Logger
}

func NewRedisEventBus(redisClient *redis.Client) *RedisEventBus {
	return &RedisEventBus{
		client: redisClient,
		logger: GetLogger(),
	}
}

func (b *RedisEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	eventData, err := json.Marshal(payload)
	if err != nil {
		return err
	}

	channel := "events:" + eventType
	return b.client.Publish(ctx, channel, eventData).Err()
}

func NewBattlePassService(dbURL, redisURL string) (*BattlePassService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewBattlePassRepository(dbPool)
	eventBus := NewRedisEventBus(redisClient)

	return &BattlePassService{
		repo:     repo,
		cache:    redisClient,
		logger:   GetLogger(),
		eventBus: eventBus,
	}, nil
}

func (s *BattlePassService) GetCurrentSeason(ctx context.Context) (*models.BattlePassSeason, error) {
	cacheKey := "battle_pass:current_season"

	if s.cache != nil {
		cached, err := s.cache.Get(ctx, cacheKey).Result()
		if err == nil && cached != "" {
			var season models.BattlePassSeason
			if err := json.Unmarshal([]byte(cached), &season); err == nil {
				return &season, nil
			}
		}
	}

	season, err := s.repo.GetCurrentSeason(ctx)
	if err != nil {
		return nil, err
	}

	if season != nil && s.cache != nil {
		seasonJSON, _ := json.Marshal(season)
		s.cache.Set(ctx, cacheKey, seasonJSON, 5*time.Minute)
	}

	return season, nil
}

func (s *BattlePassService) GetSeasonByID(ctx context.Context, id uuid.UUID) (*models.BattlePassSeason, error) {
	return s.repo.GetSeasonByID(ctx, id)
}

func (s *BattlePassService) CreateSeason(ctx context.Context, season *models.BattlePassSeason) error {
	season.ID = uuid.New()
	season.CreatedAt = time.Now()
	season.UpdatedAt = time.Now()

	err := s.repo.CreateSeason(ctx, season)
	if err != nil {
		return err
	}

	s.cache.Del(ctx, "battle_pass:current_season")
	return nil
}

func (s *BattlePassService) GetProgress(ctx context.Context, characterID, seasonID uuid.UUID) (*models.PlayerBattlePassProgress, error) {
	progress, err := s.repo.GetProgress(ctx, characterID, seasonID)
	if err != nil {
		return nil, err
	}

	if progress == nil {
		season, err := s.repo.GetSeasonByID(ctx, seasonID)
		if err != nil {
			return nil, err
		}
		if season == nil {
			return nil, errors.New("season not found")
		}

		levelReqs, err := s.repo.GetLevelRequirements(ctx, 1)
		if err != nil {
			return nil, err
		}

		progress = &models.PlayerBattlePassProgress{
			ID:            uuid.New(),
			CharacterID:  characterID,
			SeasonID:     seasonID,
			Level:        1,
			XP:           0,
			XPToNextLevel: levelReqs.XPRequired,
			HasPremium:   false,
			CreatedAt:    time.Now(),
			UpdatedAt:    time.Now(),
		}

		err = s.repo.CreateProgress(ctx, progress)
		if err != nil {
			return nil, err
		}
	}

	return progress, nil
}

func (s *BattlePassService) AwardXP(ctx context.Context, characterID uuid.UUID, xpAmount int, source models.XPSource) (*models.PlayerBattlePassProgress, error) {
	season, err := s.repo.GetCurrentSeason(ctx)
	if err != nil {
		return nil, err
	}
	if season == nil {
		return nil, errors.New("no active season")
	}

	progress, err := s.GetProgress(ctx, characterID, season.ID)
	if err != nil {
		return nil, err
	}

	oldLevel := progress.Level
	progress.XP += xpAmount

	for progress.XP >= progress.XPToNextLevel && progress.Level < season.MaxLevel {
		progress.XP -= progress.XPToNextLevel
		progress.Level++

		levelReqs, err := s.repo.GetLevelRequirements(ctx, progress.Level+1)
		if err != nil {
			return nil, err
		}
		progress.XPToNextLevel = levelReqs.XPRequired

		if progress.Level > oldLevel {
			s.publishLevelUpEvent(ctx, characterID, season.ID, progress.Level)
			RecordLevelUp(season.ID.String())
		}
	}

	progress.UpdatedAt = time.Now()
	err = s.repo.UpdateProgress(ctx, progress)
	if err != nil {
		return nil, err
	}

	return progress, nil
}

func (s *BattlePassService) PurchasePremium(ctx context.Context, characterID uuid.UUID) (*models.PlayerBattlePassProgress, error) {
	season, err := s.repo.GetCurrentSeason(ctx)
	if err != nil {
		return nil, err
	}
	if season == nil {
		return nil, errors.New("no active season")
	}

	progress, err := s.GetProgress(ctx, characterID, season.ID)
	if err != nil {
		return nil, err
	}

	if progress.HasPremium {
		return progress, nil
	}

	now := time.Now()
	progress.HasPremium = true
	progress.PremiumPurchasedAt = &now
	progress.UpdatedAt = now

	err = s.repo.UpdateProgress(ctx, progress)
	if err != nil {
		return nil, err
	}

	s.publishPremiumPurchasedEvent(ctx, characterID, season.ID)
	RecordPremiumPurchased(season.ID.String())

	return progress, nil
}

func (s *BattlePassService) GetRewards(ctx context.Context, seasonID uuid.UUID) ([]models.BattlePassReward, error) {
	return s.repo.GetRewardsBySeason(ctx, seasonID)
}

func (s *BattlePassService) ClaimReward(ctx context.Context, characterID, rewardID uuid.UUID) error {
	reward, err := s.repo.GetRewardByID(ctx, rewardID)
	if err != nil {
		return err
	}
	if reward == nil {
		return errors.New("reward not found")
	}

	progress, err := s.GetProgress(ctx, characterID, reward.SeasonID)
	if err != nil {
		return err
	}

	if progress.Level < reward.Level {
		return errors.New("insufficient level")
	}

	if reward.Track == models.TrackPremium && !progress.HasPremium {
		return errors.New("premium required")
	}

	claimedRewards, err := s.repo.GetClaimedRewards(ctx, characterID, reward.SeasonID)
	if err != nil {
		return err
	}

	for _, claimedID := range claimedRewards {
		if claimedID == rewardID {
			return errors.New("reward already claimed")
		}
	}

	err = s.repo.ClaimReward(ctx, characterID, rewardID)
	if err != nil {
		return err
	}

	s.publishRewardClaimedEvent(ctx, characterID, reward.SeasonID, rewardID, reward.Level, reward.Track)
	RecordRewardClaimed(reward.SeasonID.String(), string(reward.Track))

	return nil
}

func (s *BattlePassService) GetWeeklyChallenges(ctx context.Context, characterID uuid.UUID) ([]models.WeeklyChallenge, error) {
	season, err := s.repo.GetCurrentSeason(ctx)
	if err != nil {
		return nil, err
	}
	if season == nil {
		return nil, errors.New("no active season")
	}

	challenges, err := s.repo.GetWeeklyChallenges(ctx, season.ID, nil)
	if err != nil {
		return nil, err
	}

	result := make([]models.WeeklyChallenge, len(challenges))
	for i := range challenges {
		result[i] = challenges[i]
		progress, err := s.repo.GetChallengeProgress(ctx, characterID, challenges[i].ID)
		if err == nil && progress != nil {
			result[i].Progress = progress.Progress
			result[i].IsCompleted = progress.IsCompleted
		}
	}

	return result, nil
}

func (s *BattlePassService) CompleteChallenge(ctx context.Context, characterID, challengeID uuid.UUID) error {
	season, err := s.repo.GetCurrentSeason(ctx)
	if err != nil {
		return err
	}
	if season == nil {
		return errors.New("no active season")
	}

	challenges, err := s.repo.GetWeeklyChallenges(ctx, season.ID, nil)
	if err != nil {
		return err
	}

	var targetChallenge *models.WeeklyChallenge
	for i := range challenges {
		if challenges[i].ID == challengeID {
			targetChallenge = &challenges[i]
			break
		}
	}

	if targetChallenge == nil {
		return errors.New("challenge not found")
	}

	progress, err := s.repo.GetChallengeProgress(ctx, characterID, challengeID)
	if err != nil {
		return err
	}

	if progress == nil {
		progress = &models.PlayerChallengeProgress{
			ID:          uuid.New(),
			CharacterID: characterID,
			ChallengeID: challengeID,
			Progress:    0,
			IsCompleted: false,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		}
		err = s.repo.CreateChallengeProgress(ctx, progress)
		if err != nil {
			return err
		}
	}

	if progress.IsCompleted {
		return errors.New("challenge already completed")
	}

	if progress.Progress < targetChallenge.Target {
		return errors.New("challenge not completed")
	}

	now := time.Now()
	progress.IsCompleted = true
	progress.CompletedAt = &now
	progress.UpdatedAt = now

	err = s.repo.UpdateChallengeProgress(ctx, progress)
	if err != nil {
		return err
	}

	_, err = s.AwardXP(ctx, characterID, targetChallenge.XPReward, models.XPSourceChallenge)
	if err != nil {
		return err
	}

	return nil
}

func (s *BattlePassService) GetLevelRequirements(ctx context.Context, level int) (*models.LevelRequirements, error) {
	return s.repo.GetLevelRequirements(ctx, level)
}

func (s *BattlePassService) publishLevelUpEvent(ctx context.Context, characterID, seasonID uuid.UUID, level int) {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"season_id":    seasonID.String(),
		"level":        level,
		"timestamp":    time.Now().Unix(),
	}

	if err := s.eventBus.PublishEvent(ctx, "battle-pass:level-up", payload); err != nil {
		s.logger.WithError(err).Error("Failed to publish level-up event")
	}
}

func (s *BattlePassService) publishRewardClaimedEvent(ctx context.Context, characterID, seasonID, rewardID uuid.UUID, level int, track models.BattlePassTrack) {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"season_id":    seasonID.String(),
		"reward_id":    rewardID.String(),
		"level":        level,
		"track":        string(track),
		"timestamp":    time.Now().Unix(),
	}

	if err := s.eventBus.PublishEvent(ctx, "battle-pass:reward-claimed", payload); err != nil {
		s.logger.WithError(err).Error("Failed to publish reward-claimed event")
	}
}

func (s *BattlePassService) publishPremiumPurchasedEvent(ctx context.Context, characterID, seasonID uuid.UUID) {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"season_id":    seasonID.String(),
		"timestamp":    time.Now().Unix(),
	}

	if err := s.eventBus.PublishEvent(ctx, "battle-pass:premium-purchased", payload); err != nil {
		s.logger.WithError(err).Error("Failed to publish premium-purchased event")
	}
}

