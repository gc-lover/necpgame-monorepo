package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type MasteryServiceInterface interface {
	GetMasteryLevels(ctx context.Context, characterID uuid.UUID) (*MasteryLevels, error)
	GetMasteryProgress(ctx context.Context, characterID uuid.UUID, masteryType string) (*MasteryProgress, error)
	GetMasteryRewards(ctx context.Context, characterID uuid.UUID, masteryType *string) (*MasteryRewards, error)
	AddMasteryExperience(ctx context.Context, characterID uuid.UUID, masteryType string, experience int64) error
}

type MasteryService struct {
	repo   MasteryRepositoryInterface
	db     *pgxpool.Pool
	cache  *redis.Client
	logger *logrus.Logger
}

func NewMasteryService(dbURL, redisURL string) (*MasteryService, error) {
	// Issue: #1605 - DB Connection Pool configuration
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, err
	}
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 1 * time.Minute
	
	dbPool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewMasteryRepository(dbPool)

	return &MasteryService{
		repo:   repo,
		db:     dbPool,
		cache:  redisClient,
		logger: GetLogger(),
	}, nil
}

func (s *MasteryService) GetMasteryLevels(ctx context.Context, characterID uuid.UUID) (*MasteryLevels, error) {
	cacheKey := fmt.Sprintf("mastery:levels:%s", characterID.String())

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil {
		var levels MasteryLevels
		if json.Unmarshal([]byte(cached), &levels) == nil {
			return &levels, nil
		}
	}

	levels, err := s.repo.GetMasteryLevels(ctx, characterID)
	if err != nil {
		return nil, err
	}

	data, _ := json.Marshal(levels)
	s.cache.Set(ctx, cacheKey, data, 5*time.Minute)

	return levels, nil
}

func (s *MasteryService) GetMasteryProgress(ctx context.Context, characterID uuid.UUID, masteryType string) (*MasteryProgress, error) {
	if !s.isValidMasteryType(masteryType) {
		return nil, fmt.Errorf("invalid mastery type: %s", masteryType)
	}

	cacheKey := fmt.Sprintf("mastery:progress:%s:%s", characterID.String(), masteryType)

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil {
		var progress MasteryProgress
		if json.Unmarshal([]byte(cached), &progress) == nil {
			return &progress, nil
		}
	}

	progress, err := s.repo.GetMasteryProgress(ctx, characterID, masteryType)
	if err != nil {
		return nil, err
	}

	data, _ := json.Marshal(progress)
	s.cache.Set(ctx, cacheKey, data, 5*time.Minute)

	return progress, nil
}

func (s *MasteryService) GetMasteryRewards(ctx context.Context, characterID uuid.UUID, masteryType *string) (*MasteryRewards, error) {
	if masteryType != nil && !s.isValidMasteryType(*masteryType) {
		return nil, fmt.Errorf("invalid mastery type: %s", *masteryType)
	}

	cacheKey := fmt.Sprintf("mastery:rewards:%s", characterID.String())
	if masteryType != nil {
		cacheKey += ":" + *masteryType
	}

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil {
		var rewards MasteryRewards
		if json.Unmarshal([]byte(cached), &rewards) == nil {
			return &rewards, nil
		}
	}

	rewards, err := s.repo.GetMasteryRewards(ctx, characterID, masteryType)
	if err != nil {
		return nil, err
	}

	data, _ := json.Marshal(rewards)
	s.cache.Set(ctx, cacheKey, data, 5*time.Minute)

	return rewards, nil
}

func (s *MasteryService) AddMasteryExperience(ctx context.Context, characterID uuid.UUID, masteryType string, experience int64) error {
	if !s.isValidMasteryType(masteryType) {
		return fmt.Errorf("invalid mastery type: %s", masteryType)
	}

	progress, err := s.repo.GetMasteryProgress(ctx, characterID, masteryType)
	if err != nil {
		return err
	}

	newExp := progress.ExperienceCurrent + experience
	newTotalExp := progress.TotalExperienceEarned + experience
	newLevel := progress.MasteryLevel
	newExpRequired := progress.ExperienceRequired

	for newExp >= newExpRequired && newLevel < 100 {
		newExp -= newExpRequired
		newLevel++
		newExpRequired = s.calculateExperienceRequired(newLevel)
	}

	if newLevel > progress.MasteryLevel {
		s.checkAndUnlockRewards(ctx, characterID, masteryType, progress.MasteryLevel+1, newLevel)
	}

	err = s.repo.CreateOrUpdateMasteryLevel(ctx, characterID, masteryType, newLevel, newExp, newExpRequired, newTotalExp, progress.CompletionsCount+1)
	if err != nil {
		return err
	}

	cacheKey := fmt.Sprintf("mastery:levels:%s", characterID.String())
	s.cache.Del(ctx, cacheKey)
	cacheKey = fmt.Sprintf("mastery:progress:%s:%s", characterID.String(), masteryType)
	s.cache.Del(ctx, cacheKey)
	cacheKey = fmt.Sprintf("mastery:rewards:%s", characterID.String())
	s.cache.Del(ctx, cacheKey)

	return nil
}

func (s *MasteryService) isValidMasteryType(masteryType string) bool {
	validTypes := []string{"raid", "dungeon", "world_boss", "pvp", "exploration"}
	for _, vt := range validTypes {
		if vt == masteryType {
			return true
		}
	}
	return false
}

func (s *MasteryService) calculateExperienceRequired(level int) int64 {
	baseExp := int64(1000)
	multiplier := 1.15
	exp := float64(baseExp)
	for i := 1; i < level; i++ {
		exp *= multiplier
	}
	return int64(exp)
}

func (s *MasteryService) checkAndUnlockRewards(ctx context.Context, characterID uuid.UUID, masteryType string, fromLevel, toLevel int) {
	rewardLevels := []int{25, 50, 75, 100}
	for _, rewardLevel := range rewardLevels {
		if rewardLevel > fromLevel && rewardLevel <= toLevel {
			rewardID := fmt.Sprintf("%s_master_%d", masteryType, rewardLevel)
			s.repo.AddMasteryReward(ctx, characterID, masteryType, rewardLevel, "title", rewardID)
			if rewardLevel >= 50 {
				cosmeticID := fmt.Sprintf("%s_master_cosmetic_%d", masteryType, rewardLevel)
				s.repo.AddMasteryReward(ctx, characterID, masteryType, rewardLevel, "cosmetic", cosmeticID)
			}
		}
	}
}

