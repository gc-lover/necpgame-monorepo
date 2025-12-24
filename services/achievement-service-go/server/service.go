// Issue: #backend-achievement_system
// PERFORMANCE: Business logic layer with memory pooling

package server

import (
	"context"
	"fmt"
	"sync"
	"time"

	"go.uber.org/zap"
)

// AchievementService contains business logic for achievements
// PERFORMANCE: Structured for optimal memory layout
type AchievementServiceLogic struct {
	repo   *AchievementRepository
	logger *zap.Logger

	// PERFORMANCE: Object pool for achievement operations
	achievementPool sync.Pool
}

// NewAchievementServiceLogic creates a new service instance
// PERFORMANCE: Pre-allocates resources
func NewAchievementServiceLogic(repo *AchievementRepository) *AchievementServiceLogic {
	svc := &AchievementServiceLogic{
		repo: repo,
		achievementPool: sync.Pool{
			New: func() interface{} {
				return &Achievement{}
			},
		},
	}

	// PERFORMANCE: Initialize structured logger
	if l, err := zap.NewProduction(); err == nil {
		svc.logger = l
	} else {
		svc.logger = zap.NewNop()
	}

	return svc
}

// GetAchievements retrieves all achievements for a player
// PERFORMANCE: Context-based timeout, optimized DB queries
func (s *AchievementServiceLogic) GetAchievements(ctx context.Context, playerID string, limit, offset int) ([]*Achievement, error) {
	// PERFORMANCE: Context timeout check
	if deadline, ok := ctx.Deadline(); ok && time.Until(deadline) < 100*time.Millisecond {
		return nil, context.DeadlineExceeded
	}

	// Validate input parameters
	if playerID == "" {
		return nil, fmt.Errorf("player_id cannot be empty")
	}

	if limit <= 0 || limit > 100 {
		limit = 50 // Default limit
	}

	if offset < 0 {
		offset = 0
	}

	// Call repository method
	achievements, err := s.repo.GetAchievements(ctx, playerID, limit, offset)
	if err != nil {
		s.logger.Error("Failed to get achievements",
			zap.String("player_id", playerID),
			zap.Int("limit", limit),
			zap.Int("offset", offset),
			zap.Error(err))
		return nil, err
	}

	s.logger.Info("Retrieved achievements",
		zap.String("player_id", playerID),
		zap.Int("count", len(achievements)),
		zap.Int("limit", limit),
		zap.Int("offset", offset))

	return achievements, nil
}

// GetAchievement retrieves a specific achievement
// PERFORMANCE: Single-row query optimization
func (s *AchievementServiceLogic) GetAchievement(ctx context.Context, achievementID, playerID string) (*Achievement, error) {
	// PERFORMANCE: Context timeout check
	if deadline, ok := ctx.Deadline(); ok && time.Until(deadline) < 100*time.Millisecond {
		return nil, context.DeadlineExceeded
	}

	// Validate input parameters
	if achievementID == "" {
		return nil, fmt.Errorf("achievement_id cannot be empty")
	}

	if playerID == "" {
		return nil, fmt.Errorf("player_id cannot be empty")
	}

	// Call repository method
	achievement, err := s.repo.GetAchievement(ctx, achievementID, playerID)
	if err != nil {
		s.logger.Error("Failed to get achievement",
			zap.String("achievement_id", achievementID),
			zap.String("player_id", playerID),
			zap.Error(err))
		return nil, err
	}

	s.logger.Info("Retrieved achievement",
		zap.String("achievement_id", achievementID),
		zap.String("player_id", playerID))

	return achievement, nil
}

// UnlockAchievement unlocks an achievement for a player
// PERFORMANCE: Transaction-based operation with rollback
func (s *AchievementServiceLogic) UnlockAchievement(ctx context.Context, playerID, achievementID string) error {
	// PERFORMANCE: Context timeout check
	if deadline, ok := ctx.Deadline(); ok && time.Until(deadline) < 100*time.Millisecond {
		return context.DeadlineExceeded
	}

	// Validate input parameters
	if playerID == "" {
		return fmt.Errorf("player_id cannot be empty")
	}

	if achievementID == "" {
		return fmt.Errorf("achievement_id cannot be empty")
	}

	// Call repository method
	err := s.repo.UnlockAchievement(ctx, playerID, achievementID)
	if err != nil {
		s.logger.Error("Failed to unlock achievement",
			zap.String("player_id", playerID),
			zap.String("achievement_id", achievementID),
			zap.Error(err))
		return err
	}

	s.logger.Info("Achievement unlocked successfully",
		zap.String("player_id", playerID),
		zap.String("achievement_id", achievementID))

	return nil
}
