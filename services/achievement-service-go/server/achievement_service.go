package server

import (
	"context"
	"encoding/json"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type AchievementRepositoryInterface interface {
	Create(ctx context.Context, achievement *models.Achievement) error
	GetByID(ctx context.Context, id uuid.UUID) (*models.Achievement, error)
	GetByCode(ctx context.Context, code string) (*models.Achievement, error)
	List(ctx context.Context, category *models.AchievementCategory, limit, offset int) ([]models.Achievement, error)
	Count(ctx context.Context, category *models.AchievementCategory) (int, error)
	CountByCategory(ctx context.Context) (map[string]int, error)
	GetPlayerAchievement(ctx context.Context, playerID, achievementID uuid.UUID) (*models.PlayerAchievement, error)
	CreatePlayerAchievement(ctx context.Context, pa *models.PlayerAchievement) error
	UpdatePlayerAchievement(ctx context.Context, pa *models.PlayerAchievement) error
	GetPlayerAchievements(ctx context.Context, playerID uuid.UUID, category *models.AchievementCategory, limit, offset int) ([]models.PlayerAchievement, error)
	CountPlayerAchievements(ctx context.Context, playerID uuid.UUID) (int, int, error)
	GetNearCompletion(ctx context.Context, playerID uuid.UUID, threshold float64) ([]models.PlayerAchievement, error)
	GetRecentUnlocks(ctx context.Context, playerID uuid.UUID, limit int) ([]models.PlayerAchievement, error)
	GetLeaderboard(ctx context.Context, period string, limit int) ([]models.LeaderboardEntry, error)
	GetAchievementStats(ctx context.Context, achievementID uuid.UUID) (*models.AchievementStatsResponse, error)
}

type AchievementService struct {
	repo     AchievementRepositoryInterface
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

func NewAchievementService(dbURL, redisURL string) (*AchievementService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewAchievementRepository(dbPool)
	eventBus := NewRedisEventBus(redisClient)

	return &AchievementService{
		repo:     repo,
		cache:    redisClient,
		logger:   GetLogger(),
		eventBus: eventBus,
	}, nil
}

func (s *AchievementService) CreateAchievement(ctx context.Context, achievement *models.Achievement) error {
	achievement.ID = uuid.New()
	achievement.CreatedAt = time.Now()
	achievement.UpdatedAt = time.Now()

	err := s.repo.Create(ctx, achievement)
	if err != nil {
		return err
	}

	s.invalidateAchievementCache(ctx)
	return nil
}

func (s *AchievementService) GetAchievement(ctx context.Context, id uuid.UUID) (*models.Achievement, error) {
	cacheKey := "achievement:" + id.String()

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var achievement models.Achievement
		if err := json.Unmarshal([]byte(cached), &achievement); err == nil {
			return &achievement, nil
		}
	}

	achievement, err := s.repo.GetByID(ctx, id)
	if err != nil {
		return nil, err
	}

	if achievement != nil {
		achievementJSON, _ := json.Marshal(achievement)
		s.cache.Set(ctx, cacheKey, achievementJSON, 10*time.Minute)
	}

	return achievement, nil
}

func (s *AchievementService) GetAchievementByCode(ctx context.Context, code string) (*models.Achievement, error) {
	return s.repo.GetByCode(ctx, code)
}

func (s *AchievementService) ListAchievements(ctx context.Context, category *models.AchievementCategory, limit, offset int) (*models.AchievementListResponse, error) {
	cacheKey := "achievements:list:"
	if category != nil {
		cacheKey += string(*category) + ":"
	}
	cacheKey += "limit:" + strconv.Itoa(limit) + ":offset:" + strconv.Itoa(offset)

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.AchievementListResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		}
	}

	achievements, err := s.repo.List(ctx, category, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.Count(ctx, category)
	if err != nil {
		return nil, err
	}

	categories, err := s.repo.CountByCategory(ctx)
	if err != nil {
		return nil, err
	}

	response := &models.AchievementListResponse{
		Achievements: achievements,
		Total:        total,
		Categories:   categories,
	}

	responseJSON, _ := json.Marshal(response)
	s.cache.Set(ctx, cacheKey, responseJSON, 5*time.Minute)

	return response, nil
}

func (s *AchievementService) TrackProgress(ctx context.Context, playerID, achievementID uuid.UUID, progress int, progressData map[string]interface{}) error {
	achievement, err := s.repo.GetByID(ctx, achievementID)
	if err != nil {
		return err
	}
	if achievement == nil {
		return nil
	}

	pa, err := s.repo.GetPlayerAchievement(ctx, playerID, achievementID)
	if err != nil {
		return err
	}

	now := time.Now()

	if pa == nil {
		pa = &models.PlayerAchievement{
			ID:           uuid.New(),
			PlayerID:     playerID,
			AchievementID: achievementID,
			Status:       models.AchievementStatusProgress,
			Progress:     progress,
			ProgressMax:  0,
			ProgressData: progressData,
			CreatedAt:    now,
			UpdatedAt:    now,
		}

		if conditions, ok := achievement.Conditions["target"].(float64); ok {
			pa.ProgressMax = int(conditions)
		}

		err = s.repo.CreatePlayerAchievement(ctx, pa)
		if err != nil {
			return err
		}
	} else {
		pa.Progress = progress
		pa.ProgressData = progressData
		pa.UpdatedAt = now

		if conditions, ok := achievement.Conditions["target"].(float64); ok {
			pa.ProgressMax = int(conditions)
		}

		if pa.ProgressMax > 0 && pa.Progress >= pa.ProgressMax {
			pa.Status = models.AchievementStatusUnlocked
			pa.UnlockedAt = &now
			RecordAchievementUnlock(string(achievement.Category), string(achievement.Rarity))
		}

		err = s.repo.UpdatePlayerAchievement(ctx, pa)
		if err != nil {
			return err
		}
	}

	RecordAchievementProgress(string(achievement.Category))
	s.invalidatePlayerAchievementCache(ctx, playerID)

	return nil
}

func (s *AchievementService) UnlockAchievement(ctx context.Context, playerID, achievementID uuid.UUID) error {
	achievement, err := s.repo.GetByID(ctx, achievementID)
	if err != nil {
		return err
	}
	if achievement == nil {
		return nil
	}

	pa, err := s.repo.GetPlayerAchievement(ctx, playerID, achievementID)
	if err != nil {
		return err
	}

	now := time.Now()

	if pa == nil {
		pa = &models.PlayerAchievement{
			ID:            uuid.New(),
			PlayerID:      playerID,
			AchievementID: achievementID,
			Status:        models.AchievementStatusUnlocked,
			Progress:      0,
			ProgressMax:   0,
			UnlockedAt:    &now,
			CreatedAt:     now,
			UpdatedAt:     now,
		}

		err = s.repo.CreatePlayerAchievement(ctx, pa)
		if err != nil {
			return err
		}
	} else {
		pa.Status = models.AchievementStatusUnlocked
		pa.UnlockedAt = &now
		pa.UpdatedAt = now

		err = s.repo.UpdatePlayerAchievement(ctx, pa)
		if err != nil {
			return err
		}
	}

	RecordAchievementUnlock(string(achievement.Category), string(achievement.Rarity))
	s.invalidatePlayerAchievementCache(ctx, playerID)

	err = s.publishAchievementUnlockedEvent(ctx, playerID, achievementID, achievement)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish achievement unlocked event")
	}

	return nil
}

func (s *AchievementService) publishAchievementUnlockedEvent(ctx context.Context, playerID, achievementID uuid.UUID, achievement *models.Achievement) error {
	payload := map[string]interface{}{
		"player_id":     playerID.String(),
		"achievement_id": achievementID.String(),
		"achievement_code": achievement.Code,
		"category":       string(achievement.Category),
		"rarity":         string(achievement.Rarity),
		"rewards":        achievement.Rewards,
		"timestamp":      time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "achievement:unlocked", payload)
}

func (s *AchievementService) GetPlayerAchievements(ctx context.Context, playerID uuid.UUID, category *models.AchievementCategory, limit, offset int) (*models.PlayerAchievementResponse, error) {
	cacheKey := "player_achievements:" + playerID.String() + ":"
	if category != nil {
		cacheKey += string(*category) + ":"
	}
	cacheKey += "limit:" + strconv.Itoa(limit) + ":offset:" + strconv.Itoa(offset)

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.PlayerAchievementResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		}
	}

	playerAchievements, err := s.repo.GetPlayerAchievements(ctx, playerID, category, limit, offset)
	if err != nil {
		return nil, err
	}

	total, unlocked, err := s.repo.CountPlayerAchievements(ctx, playerID)
	if err != nil {
		return nil, err
	}

	nearCompletion, _ := s.repo.GetNearCompletion(ctx, playerID, 0.8)
	recentUnlocks, _ := s.repo.GetRecentUnlocks(ctx, playerID, 10)

	response := &models.PlayerAchievementResponse{
		Achievements:   playerAchievements,
		Total:          total,
		Unlocked:       unlocked,
		NearCompletion: nearCompletion,
		RecentUnlocks:  recentUnlocks,
	}

	responseJSON, _ := json.Marshal(response)
	s.cache.Set(ctx, cacheKey, responseJSON, 2*time.Minute)

	return response, nil
}

func (s *AchievementService) GetLeaderboard(ctx context.Context, period string, limit int) (*models.LeaderboardResponse, error) {
	cacheKey := "leaderboard:" + period + ":limit:" + strconv.Itoa(limit)

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.LeaderboardResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		}
	}

	entries, err := s.repo.GetLeaderboard(ctx, period, limit)
	if err != nil {
		return nil, err
	}

	response := &models.LeaderboardResponse{
		Entries: entries,
		Total:   len(entries),
		Period:  period,
	}

	responseJSON, _ := json.Marshal(response)
	s.cache.Set(ctx, cacheKey, responseJSON, 1*time.Minute)

	return response, nil
}

func (s *AchievementService) GetAchievementStats(ctx context.Context, achievementID uuid.UUID) (*models.AchievementStatsResponse, error) {
	return s.repo.GetAchievementStats(ctx, achievementID)
}

func (s *AchievementService) invalidateAchievementCache(ctx context.Context) {
	pattern := "achievements:*"
	keys, _ := s.cache.Keys(ctx, pattern).Result()
	if len(keys) > 0 {
		s.cache.Del(ctx, keys...)
	}
}

func (s *AchievementService) invalidatePlayerAchievementCache(ctx context.Context, playerID uuid.UUID) {
	pattern := "player_achievements:" + playerID.String() + ":*"
	keys, _ := s.cache.Keys(ctx, pattern).Result()
	if len(keys) > 0 {
		s.cache.Del(ctx, keys...)
	}
}

