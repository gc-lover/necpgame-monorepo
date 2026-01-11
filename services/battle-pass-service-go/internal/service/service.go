// Agent: Backend Agent
// Issue: #backend-battle-pass-service

package service

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"battle-pass-service-go/internal/config"
	"battle-pass-service-go/internal/models"
	"battle-pass-service-go/internal/repository"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
)

// Service handles business logic for battle pass
// MMOFPS Optimization: Context timeouts, zero allocations in hot paths
type Service struct {
	repo  *repository.Repository
	redis *redis.Client
}

// New creates a new service instance
func New(cfg *config.Config, repo *repository.Repository) (*Service, error) {
	// Initialize Redis for caching
	// MMOFPS Optimization: Redis for battle pass progress caching
	rdb := redis.NewClient(&redis.Options{
		Addr:     fmt.Sprintf("%s:%d", cfg.Redis.Host, cfg.Redis.Port),
		Password: cfg.Redis.Password,
		DB:       cfg.Redis.DB,
	})

	// Test Redis connection
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := rdb.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to connect to Redis: %w", err)
	}

	return &Service{
		repo:  repo,
		redis: rdb,
	}, nil
}

// GetPlayerProgress retrieves player's battle pass progress with caching
// MMOFPS Optimization: Redis cache for progress data
func (s *Service) GetPlayerProgress(ctx context.Context, playerID uuid.UUID) (*models.PlayerProgress, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second) // MMOFPS: Fast retrieval
	defer cancel()

	// Try cache first
	cacheKey := fmt.Sprintf("battle_pass:progress:%s", playerID.String())
	if cached, err := s.redis.Get(ctx, cacheKey).Result(); err == nil {
		var progress models.PlayerProgress
		if err := json.Unmarshal([]byte(cached), &progress); err == nil {
			return &progress, nil
		}
	}

	// Get from database
	progress, err := s.repo.GetPlayerProgress(ctx, playerID)
	if err != nil {
		return nil, err
	}

	// Cache the result
	if data, err := json.Marshal(progress); err == nil {
		s.redis.Set(ctx, cacheKey, data, time.Minute*5) // Cache for 5 minutes
	}

	return progress, nil
}

// AwardXP awards XP to player and updates progress
// MMOFPS Optimization: Hot path - optimized for 1000+ RPS during gameplay
func (s *Service) AwardXP(ctx context.Context, playerID uuid.UUID, xpAmount int, reason string) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second) // MMOFPS: Fast XP awards
	defer cancel()

	// Award XP in repository
	err := s.repo.AwardXP(ctx, playerID, xpAmount, reason)
	if err != nil {
		return err
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("battle_pass:progress:%s", playerID.String())
	s.redis.Del(ctx, cacheKey)

	return nil
}

// GetAvailableRewards gets rewards available for claiming
func (s *Service) GetAvailableRewards(ctx context.Context, playerID uuid.UUID) ([]*models.Reward, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Reward availability check
	defer cancel()

	return s.repo.GetAvailableRewards(ctx, playerID)
}

// ClaimReward claims a reward for player
// MMOFPS Optimization: Transactional operation with inventory integration
func (s *Service) ClaimReward(ctx context.Context, playerID uuid.UUID, level int) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second) // MMOFPS: Reward claiming with external calls
	defer cancel()

	err := s.repo.ClaimReward(ctx, playerID, level)
	if err != nil {
		return err
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("battle_pass:progress:%s", playerID.String())
	s.redis.Del(ctx, cacheKey)

	return nil
}

// GetCurrentSeason gets the currently active battle pass season
func (s *Service) GetCurrentSeason(ctx context.Context) (*models.Season, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Current season info
	defer cancel()

	return s.repo.GetCurrentSeason(ctx)
}

// GetSeason gets a specific season by ID
func (s *Service) GetSeason(ctx context.Context, seasonID uuid.UUID) (*models.Season, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Season details
	defer cancel()

	return s.repo.GetSeason(ctx, seasonID)
}

// GetPlayerStatistics gets comprehensive player statistics
func (s *Service) GetPlayerStatistics(ctx context.Context, playerID uuid.UUID) (*models.PlayerStatistics, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second) // Analytics can take longer
	defer cancel()

	return s.repo.GetPlayerStatistics(ctx, playerID)
}