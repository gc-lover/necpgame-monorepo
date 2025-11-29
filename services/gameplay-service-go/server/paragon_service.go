package server

import (
	"context"
	"encoding/json"
	"fmt"
	"math"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type ParagonServiceInterface interface {
	GetParagonLevels(ctx context.Context, characterID uuid.UUID) (*models.ParagonLevels, error)
	DistributeParagonPoints(ctx context.Context, characterID uuid.UUID, req *models.DistributeParagonPointsRequest) (*models.ParagonLevels, error)
	GetParagonStats(ctx context.Context, characterID uuid.UUID) (*models.ParagonStats, error)
	AddParagonExperience(ctx context.Context, characterID uuid.UUID, amount int64) error
}

type ParagonService struct {
	repo     ParagonRepositoryInterface
	db       *pgxpool.Pool
	cache    *redis.Client
	logger   *logrus.Logger
	eventBus EventBus
}

func NewParagonService(dbURL, redisURL string) (*ParagonService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to database: %w", err)
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, fmt.Errorf("failed to parse Redis URL: %w", err)
	}

	redisClient := redis.NewClient(redisOpts)
	repo := NewParagonRepository(dbPool)
	eventBus := NewRedisEventBus(redisClient)

	return &ParagonService{
		repo:     repo,
		db:       dbPool,
		cache:    redisClient,
		logger:   GetLogger(),
		eventBus: eventBus,
	}, nil
}

func (s *ParagonService) GetParagonLevels(ctx context.Context, characterID uuid.UUID) (*models.ParagonLevels, error) {
	cacheKey := fmt.Sprintf("paragon:%s", characterID.String())

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil {
		var paragon models.ParagonLevels
		if err := json.Unmarshal([]byte(cached), &paragon); err == nil {
			return &paragon, nil
		} else {
			s.logger.WithError(err).Warn("Failed to unmarshal cached paragon JSON, fetching from DB")
		}
	}

	paragon, err := s.repo.GetParagonLevels(ctx, characterID)
	if err != nil {
		return nil, err
	}

	if paragon == nil {
		paragon = &models.ParagonLevels{
			CharacterID:          characterID,
			ParagonLevel:         0,
			ParagonPointsTotal:   0,
			ParagonPointsSpent:    0,
			ParagonPointsAvailable: 0,
			ExperienceCurrent:    0,
			ExperienceRequired:   s.calculateParagonExperienceForLevel(1),
			Allocations:          []models.ParagonAllocation{},
			UpdatedAt:            time.Now(),
		}
		err = s.repo.CreateParagonLevels(ctx, paragon)
		if err != nil {
			return nil, err
		}
	}

	data, err := json.Marshal(paragon)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal paragon JSON for caching")
	} else {
		s.cache.Set(ctx, cacheKey, data, 5*time.Minute)
	}

	return paragon, nil
}

func (s *ParagonService) DistributeParagonPoints(ctx context.Context, characterID uuid.UUID, req *models.DistributeParagonPointsRequest) (*models.ParagonLevels, error) {
	paragon, err := s.GetParagonLevels(ctx, characterID)
	if err != nil {
		return nil, err
	}

	if paragon == nil {
		return nil, fmt.Errorf("paragon levels not found for character %s", characterID)
	}

	totalPointsToAllocate := 0
	for _, alloc := range req.Allocations {
		if alloc.Points < 1 {
			return nil, fmt.Errorf("points must be at least 1 for stat_type %s", alloc.StatType)
		}
		totalPointsToAllocate += alloc.Points
	}

	if totalPointsToAllocate > paragon.ParagonPointsAvailable {
		return nil, fmt.Errorf("not enough paragon points available: have %d, need %d", paragon.ParagonPointsAvailable, totalPointsToAllocate)
	}

	allocationsMap := make(map[string]int)
	for _, alloc := range paragon.Allocations {
		allocationsMap[alloc.StatType] = alloc.PointsAllocated
	}

	for _, alloc := range req.Allocations {
		currentPoints := allocationsMap[alloc.StatType]
		newPoints := currentPoints + alloc.Points

		if newPoints > 100 {
			return nil, fmt.Errorf("stat_type %s cannot exceed 100 points (current: %d, trying to add: %d)", alloc.StatType, currentPoints, alloc.Points)
		}

		allocationsMap[alloc.StatType] = newPoints
	}

	paragon.Allocations = make([]models.ParagonAllocation, 0, len(allocationsMap))
	for statType, points := range allocationsMap {
		if points > 0 {
			paragon.Allocations = append(paragon.Allocations, models.ParagonAllocation{
				StatType:       statType,
				PointsAllocated: points,
			})
		}
	}

	paragon.ParagonPointsSpent += totalPointsToAllocate
	paragon.ParagonPointsAvailable -= totalPointsToAllocate
	paragon.UpdatedAt = time.Now()

	err = s.repo.UpdateParagonLevels(ctx, paragon)
	if err != nil {
		return nil, err
	}

	cacheKey := fmt.Sprintf("paragon:%s", characterID.String())
	s.cache.Del(ctx, cacheKey)

	err = s.publishParagonPointsDistributedEvent(ctx, characterID, req.Allocations)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish paragon points distributed event")
	}

	return paragon, nil
}

func (s *ParagonService) GetParagonStats(ctx context.Context, characterID uuid.UUID) (*models.ParagonStats, error) {
	return s.repo.GetParagonStats(ctx, characterID)
}

func (s *ParagonService) AddParagonExperience(ctx context.Context, characterID uuid.UUID, amount int64) error {
	paragon, err := s.GetParagonLevels(ctx, characterID)
	if err != nil {
		return err
	}

	if paragon == nil {
		paragon = &models.ParagonLevels{
			CharacterID:          characterID,
			ParagonLevel:         0,
			ParagonPointsTotal:   0,
			ParagonPointsSpent:    0,
			ParagonPointsAvailable: 0,
			ExperienceCurrent:    0,
			ExperienceRequired:   s.calculateParagonExperienceForLevel(1),
			Allocations:          []models.ParagonAllocation{},
			UpdatedAt:            time.Now(),
		}
		err = s.repo.CreateParagonLevels(ctx, paragon)
		if err != nil {
			return err
		}
	}

	paragon.ExperienceCurrent += amount

	for paragon.ExperienceCurrent >= paragon.ExperienceRequired {
		paragon.ParagonLevel++
		paragon.ParagonPointsTotal += 5
		paragon.ParagonPointsAvailable += 5
		paragon.ExperienceCurrent -= paragon.ExperienceRequired
		paragon.ExperienceRequired = s.calculateParagonExperienceForLevel(paragon.ParagonLevel + 1)

		err = s.publishParagonLevelUpEvent(ctx, characterID, paragon.ParagonLevel)
		if err != nil {
			s.logger.WithError(err).Error("Failed to publish paragon level up event")
		}
	}

	paragon.UpdatedAt = time.Now()
	err = s.repo.UpdateParagonLevels(ctx, paragon)
	if err != nil {
		return err
	}

	cacheKey := fmt.Sprintf("paragon:%s", characterID.String())
	s.cache.Del(ctx, cacheKey)

	return nil
}

func (s *ParagonService) calculateParagonExperienceForLevel(level int) int64 {
	baseExp := 1000.0
	multiplier := 1.2
	return int64(baseExp * math.Pow(float64(level), multiplier))
}

func (s *ParagonService) publishParagonLevelUpEvent(ctx context.Context, characterID uuid.UUID, level int) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"paragon_level": level,
		"timestamp":     time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "character:paragon-level-up", payload)
}

func (s *ParagonService) publishParagonPointsDistributedEvent(ctx context.Context, characterID uuid.UUID, allocations []models.ParagonAllocationRequest) error {
	allocationsData := make([]map[string]interface{}, 0, len(allocations))
	for _, alloc := range allocations {
		allocationsData = append(allocationsData, map[string]interface{}{
			"stat_type": alloc.StatType,
			"points":    alloc.Points,
		})
	}

	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"allocations":  allocationsData,
		"timestamp":    time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "character:paragon-points-distributed", payload)
}

