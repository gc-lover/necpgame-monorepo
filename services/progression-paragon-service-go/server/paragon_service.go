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

type ParagonServiceInterface interface {
	GetParagonLevels(ctx context.Context, characterID uuid.UUID) (*ParagonLevels, error)
	DistributeParagonPoints(ctx context.Context, characterID uuid.UUID, allocations []ParagonAllocation) (*ParagonLevels, error)
	GetParagonStats(ctx context.Context, characterID uuid.UUID) (*ParagonStats, error)
	AddParagonExperience(ctx context.Context, characterID uuid.UUID, amount int64) error
}

type ParagonService struct {
	repo   ParagonRepositoryInterface
	db     *pgxpool.Pool
	cache  *redis.Client
	logger *logrus.Logger
}

func NewParagonService(dbURL, redisURL string) (*ParagonService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewParagonRepository(dbPool)

	return &ParagonService{
		repo:   repo,
		db:     dbPool,
		cache:  redisClient,
		logger: GetLogger(),
	}, nil
}

func (s *ParagonService) GetParagonLevels(ctx context.Context, characterID uuid.UUID) (*ParagonLevels, error) {
	cacheKey := fmt.Sprintf("paragon:levels:%s", characterID.String())

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil {
		var levels ParagonLevels
		if json.Unmarshal([]byte(cached), &levels) == nil {
			return &levels, nil
		}
	}

	levels, err := s.repo.GetParagonLevels(ctx, characterID)
	if err != nil {
		return nil, err
	}

	if levels.ParagonLevel == 0 && levels.ParagonPointsTotal == 0 {
		err = s.initializeParagonLevels(ctx, characterID)
		if err != nil {
			return nil, err
		}
		levels, err = s.repo.GetParagonLevels(ctx, characterID)
		if err != nil {
			return nil, err
		}
	}

	data, _ := json.Marshal(levels)
	s.cache.Set(ctx, cacheKey, data, 5*time.Minute)

	return levels, nil
}

func (s *ParagonService) DistributeParagonPoints(ctx context.Context, characterID uuid.UUID, allocations []ParagonAllocation) (*ParagonLevels, error) {
	levels, err := s.repo.DistributeParagonPoints(ctx, characterID, allocations)
	if err != nil {
		return nil, err
	}

	cacheKey := fmt.Sprintf("paragon:levels:%s", characterID.String())
	s.cache.Del(ctx, cacheKey)

	return levels, nil
}

func (s *ParagonService) GetParagonStats(ctx context.Context, characterID uuid.UUID) (*ParagonStats, error) {
	return s.repo.GetParagonStats(ctx, characterID)
}

func (s *ParagonService) AddParagonExperience(ctx context.Context, characterID uuid.UUID, amount int64) error {
	levels, err := s.GetParagonLevels(ctx, characterID)
	if err != nil {
		return err
	}

	levels.ExperienceCurrent += amount

	for levels.ExperienceCurrent >= levels.ExperienceRequired {
		levels.ParagonLevel++
		levels.ParagonPointsTotal++
		levels.ParagonPointsAvailable++
		levels.ExperienceCurrent -= levels.ExperienceRequired
		levels.ExperienceRequired = s.calculateExperienceForParagonLevel(levels.ParagonLevel + 1)
	}

	err = s.updateParagonLevels(ctx, levels)
	if err != nil {
		return err
	}

	cacheKey := fmt.Sprintf("paragon:levels:%s", characterID.String())
	s.cache.Del(ctx, cacheKey)

	return nil
}

func (s *ParagonService) calculateExperienceForParagonLevel(level int) int64 {
	baseExp := int64(150000)
	multiplier := 1.0 + float64(level-1)*0.1
	return int64(float64(baseExp) * multiplier)
}

func (s *ParagonService) initializeParagonLevels(ctx context.Context, characterID uuid.UUID) error {
	_, err := s.db.Exec(ctx,
		`INSERT INTO progression.paragon_levels 
		 (character_id, paragon_level, paragon_points_total, paragon_points_spent, 
		  paragon_points_available, experience_current, experience_required, updated_at)
		 VALUES ($1, 0, 0, 0, 0, 0, 150000, $2)
		 ON CONFLICT (character_id) DO NOTHING`,
		characterID, time.Now(),
	)
	return err
}

func (s *ParagonService) updateParagonLevels(ctx context.Context, levels *ParagonLevels) error {
	_, err := s.db.Exec(ctx,
		`UPDATE progression.paragon_levels
		 SET paragon_level = $1, paragon_points_total = $2, paragon_points_available = $3,
		     experience_current = $4, experience_required = $5, updated_at = $6
		 WHERE character_id = $7`,
		levels.ParagonLevel, levels.ParagonPointsTotal, levels.ParagonPointsAvailable,
		levels.ExperienceCurrent, levels.ExperienceRequired, time.Now(), levels.CharacterID,
	)
	return err
}

