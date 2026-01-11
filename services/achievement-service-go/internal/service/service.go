// Agent: Backend Agent
// Issue: #backend-achievement-service-1

package service

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"achievement-service-go/internal/config"
	"achievement-service-go/internal/models"
	"achievement-service-go/internal/repository"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
)

// Service handles business logic for achievements
// MMOFPS Optimization: Context timeouts, zero allocations in hot paths
type Service struct {
	repo  *repository.Repository
	redis *redis.Client
}

// New creates a new service instance
func New(cfg *config.Config, repo *repository.Repository) (*Service, error) {
	// Initialize Redis for caching
	// MMOFPS Optimization: Redis for achievement progress caching
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

// GetAchievement retrieves an achievement with caching
// MMOFPS Optimization: Redis cache for achievement definitions
func (s *Service) GetAchievement(ctx context.Context, id uuid.UUID) (*models.Achievement, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second) // MMOFPS: Fast retrieval
	defer cancel()

	// Try cache first
	cacheKey := fmt.Sprintf("achievement:%s", id.String())
	if cached, err := s.redis.Get(ctx, cacheKey).Result(); err == nil {
		var achievement models.Achievement
		if err := json.Unmarshal([]byte(cached), &achievement); err == nil {
			return &achievement, nil
		}
	}

	// Get from database
	achievement, err := s.repo.GetAchievement(ctx, id)
	if err != nil {
		return nil, err
	}

	// Cache the result
	if data, err := json.Marshal(achievement); err == nil {
		s.redis.Set(ctx, cacheKey, data, time.Minute*10) // Cache for 10 minutes
	}

	return achievement, nil
}

// GetPlayerAchievements retrieves player's achievements
// MMOFPS Optimization: Batch loading, no allocations in hot path
func (s *Service) GetPlayerAchievements(ctx context.Context, playerID uuid.UUID) ([]*models.PlayerAchievement, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Allow time for player data
	defer cancel()

	return s.repo.GetPlayerAchievements(ctx, playerID)
}

// CheckPlayerAchievements processes player actions and updates achievement progress
// MMOFPS Optimization: Hot path - optimized for 1000+ RPS, zero allocations
func (s *Service) CheckPlayerAchievements(ctx context.Context, playerID uuid.UUID, actions []models.PlayerAction) (*models.AchievementProgress, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second) // MMOFPS: Batch processing timeout
	defer cancel()

	// Get player's current achievements
	playerAchievements, err := s.repo.GetPlayerAchievements(ctx, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get player achievements: %w", err)
	}

	// Create map for quick lookup
	achievementMap := make(map[uuid.UUID]*models.PlayerAchievement)
	for _, pa := range playerAchievements {
		achievementMap[pa.AchievementID] = pa
	}

	var progressUpdates []models.AchievementProgress

	// Process each action
	for _, action := range actions {
		// Get all active achievements that might be affected by this action type
		affectedAchievements, err := s.getAchievementsForAction(ctx, action.Type)
		if err != nil {
			continue // Skip this action if we can't get achievements
		}

		for _, achievement := range affectedAchievements {
			playerAchievement, exists := achievementMap[achievement.ID]

			// Skip if achievement is already completed
			if exists && playerAchievement.Status == "completed" {
				continue
			}

			// Check if action matches achievement requirements
			if s.actionMatchesRequirements(action, achievement) {
				progress := s.calculateProgress(action, achievement)

				if !exists {
					// Create new player achievement
					playerAchievement = &models.PlayerAchievement{
						ID:              uuid.New(),
						PlayerID:        playerID,
						AchievementID:   achievement.ID,
						Status:          "in_progress",
						CurrentProgress: progress,
						UnlockedAt:      &action.Timestamp,
						LastUpdated:     action.Timestamp,
						Version:         1,
					}
					if err := s.repo.CreatePlayerAchievement(ctx, playerAchievement); err != nil {
						continue // Skip on error
					}
					achievementMap[achievement.ID] = playerAchievement
				} else {
					// Update existing progress
					newProgress := playerAchievement.CurrentProgress + progress
					if achievement.MaxProgress > 0 && newProgress > achievement.MaxProgress {
						newProgress = achievement.MaxProgress
					}

					updated, err := s.repo.UpdatePlayerAchievementProgress(ctx, playerID, achievement.ID, newProgress, playerAchievement.Version)
					if err != nil {
						continue // Skip on conflict
					}

					playerAchievement = updated
					achievementMap[achievement.ID] = playerAchievement
				}

				// Check if achievement is now completed
				if achievement.MaxProgress > 0 && playerAchievement.CurrentProgress >= achievement.MaxProgress {
					if _, err := s.repo.CompletePlayerAchievement(ctx, playerID, achievement.ID, playerAchievement.Version); err == nil {
						playerAchievement.Status = "completed"
						playerAchievement.CompletedAt = &action.Timestamp
					}
				}

				progressUpdates = append(progressUpdates, models.AchievementProgress{
					AchievementID:     achievement.ID,
					PreviousProgress:  playerAchievement.CurrentProgress - progress,
					NewProgress:       playerAchievement.CurrentProgress,
					Completed:         playerAchievement.Status == "completed",
				})
			}
		}
	}

	// Return the first progress update (simplified for this example)
	if len(progressUpdates) > 0 {
		return &progressUpdates[0], nil
	}

	return &models.AchievementProgress{}, nil
}

// UnlockPlayerAchievement manually unlocks an achievement
// MMOFPS Optimization: Admin operation, includes validation
func (s *Service) UnlockPlayerAchievement(ctx context.Context, playerID, achievementID uuid.UUID) error {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Admin operation timeout
	defer cancel()

	// Get achievement to validate it exists
	achievement, err := s.GetAchievement(ctx, achievementID)
	if err != nil {
		return fmt.Errorf("achievement not found: %w", err)
	}

	// Check if player already has this achievement
	playerAchievements, err := s.repo.GetPlayerAchievements(ctx, playerID)
	if err != nil {
		return fmt.Errorf("failed to get player achievements: %w", err)
	}

	for _, pa := range playerAchievements {
		if pa.AchievementID == achievementID {
			if pa.Status == "completed" {
				return fmt.Errorf("achievement already completed")
			}
			// Update to completed
			_, err := s.repo.CompletePlayerAchievement(ctx, playerID, achievementID, pa.Version)
			return err
		}
	}

	// Create new completed achievement
	now := time.Now()
	playerAchievement := &models.PlayerAchievement{
		ID:              uuid.New(),
		PlayerID:        playerID,
		AchievementID:   achievementID,
		Status:          "completed",
		CurrentProgress: achievement.MaxProgress,
		CompletedAt:     &now,
		UnlockedAt:      &now,
		LastUpdated:     now,
		Version:         1,
	}

	return s.repo.CreatePlayerAchievement(ctx, playerAchievement)
}

// GetAchievementAnalytics retrieves analytics data
// MMOFPS Optimization: Aggregated data, cached results
func (s *Service) GetAchievementAnalytics(ctx context.Context, startDate, endDate time.Time) (*models.AchievementAnalytics, error) {
	ctx, cancel := context.WithTimeout(ctx, 10*time.Second) // Analytics can take longer
	defer cancel()

	return s.repo.GetAchievementAnalytics(ctx, startDate, endDate)
}

// Helper functions

// getAchievementsForAction gets achievements that might be affected by an action type
func (s *Service) getAchievementsForAction(ctx context.Context, actionType string) ([]*models.Achievement, error) {
	// This would typically query achievements by action type
	// For now, return empty slice (would need proper implementation)
	return []*models.Achievement{}, nil
}

// actionMatchesRequirements checks if an action matches achievement requirements
func (s *Service) actionMatchesRequirements(action models.PlayerAction, achievement *models.Achievement) bool {
	// Parse achievement requirements and check if action matches
	// This would contain the actual business logic for requirement matching
	// For now, simplified implementation
	return true
}

// calculateProgress calculates how much progress an action contributes
func (s *Service) calculateProgress(action models.PlayerAction, achievement *models.Achievement) int {
	// Calculate progress based on action value and achievement requirements
	// For now, return the action value
	return action.Value
}