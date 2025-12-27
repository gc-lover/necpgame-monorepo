// Issue: #2262 - Cyberspace Easter Eggs Backend Integration
// Business logic service for Easter Eggs - Enterprise-grade implementation

package service

import (
	"context"
	"fmt"
	"time"

	"go.uber.org/zap"

	"cyberspace-easter-eggs-service-go/internal/metrics"
	"cyberspace-easter-eggs-service-go/pkg/models"
	"cyberspace-easter-eggs-service-go/pkg/repository"
)

// EasterEggsServiceInterface defines the service interface
type EasterEggsServiceInterface interface {
	// Easter egg operations
	GetEasterEgg(ctx context.Context, id string) (*models.EasterEgg, error)
	GetEasterEggsByCategory(ctx context.Context, category string, limit, offset int) ([]*models.EasterEgg, error)
	GetEasterEggsByDifficulty(ctx context.Context, difficulty string, limit, offset int) ([]*models.EasterEgg, error)
	CreateEasterEgg(ctx context.Context, egg *models.EasterEgg) error
	UpdateEasterEgg(ctx context.Context, egg *models.EasterEgg) error
	DeleteEasterEgg(ctx context.Context, id string) error

	// Player operations
	GetPlayerProgress(ctx context.Context, playerID, easterEggID string) (*models.PlayerEasterEggProgress, error)
	UpdatePlayerProgress(ctx context.Context, progress *models.PlayerEasterEggProgress) error
	GetPlayerProfile(ctx context.Context, playerID string) (*models.PlayerEasterEggProfile, error)
	DiscoverEasterEgg(ctx context.Context, playerID, easterEggID string, attemptData map[string]interface{}) (*models.PlayerEasterEggProgress, error)

	// Statistics operations
	GetEasterEggStatistics(ctx context.Context, easterEggID string) (*models.EasterEggStatistics, error)
	GetCategoryStatistics(ctx context.Context) ([]*models.EasterEggCategoryStats, error)

	// Hint operations
	GetHintsForEasterEgg(ctx context.Context, easterEggID string, maxLevel int) ([]*models.DiscoveryHint, error)

	// Challenge operations
	GetActiveChallenges(ctx context.Context) ([]*models.EasterEggChallenge, error)
	GetPlayerChallengeProgress(ctx context.Context, playerID, challengeID string) (int, error)

	// Health check
	HealthCheck(ctx context.Context) error
}

// EasterEggsService implements EasterEggsServiceInterface
type EasterEggsService struct {
	repo     repository.RepositoryInterface
	metrics  *metrics.Collector
	logger   *zap.SugaredLogger
}

// NewEasterEggsService creates a new easter eggs service
func NewEasterEggsService(repo repository.RepositoryInterface, metrics *metrics.Collector, logger *zap.SugaredLogger) EasterEggsServiceInterface {
	return &EasterEggsService{
		repo:    repo,
		metrics: metrics,
		logger:  logger,
	}
}

// GetEasterEgg retrieves a single easter egg
func (s *EasterEggsService) GetEasterEgg(ctx context.Context, id string) (*models.EasterEgg, error) {
	s.metrics.IncrementRequests("GetEasterEgg")
	defer s.metrics.ObserveRequestDuration("GetEasterEgg", time.Now())

	egg, err := s.repo.GetEasterEgg(ctx, id)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get easter egg %s: %v", id, err)
		return nil, fmt.Errorf("failed to get easter egg: %w", err)
	}

	s.logger.Debugf("Retrieved easter egg: %s", id)
	return egg, nil
}

// GetEasterEggsByCategory retrieves easter eggs by category
func (s *EasterEggsService) GetEasterEggsByCategory(ctx context.Context, category string, limit, offset int) ([]*models.EasterEgg, error) {
	s.metrics.IncrementRequests("GetEasterEggsByCategory")
	defer s.metrics.ObserveRequestDuration("GetEasterEggsByCategory", time.Now())

	eggs, err := s.repo.GetEasterEggsByCategory(ctx, category, limit, offset)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get easter eggs by category %s: %v", category, err)
		return nil, fmt.Errorf("failed to get easter eggs by category: %w", err)
	}

	s.logger.Debugf("Retrieved %d easter eggs for category: %s", len(eggs), category)
	return eggs, nil
}

// GetEasterEggsByDifficulty retrieves easter eggs by difficulty
func (s *EasterEggsService) GetEasterEggsByDifficulty(ctx context.Context, difficulty string, limit, offset int) ([]*models.EasterEgg, error) {
	s.metrics.IncrementRequests("GetEasterEggsByDifficulty")
	defer s.metrics.ObserveRequestDuration("GetEasterEggsByDifficulty", time.Now())

	eggs, err := s.repo.GetEasterEggsByDifficulty(ctx, difficulty, limit, offset)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get easter eggs by difficulty %s: %v", difficulty, err)
		return nil, fmt.Errorf("failed to get easter eggs by difficulty: %w", err)
	}

	s.logger.Debugf("Retrieved %d easter eggs for difficulty: %s", len(eggs), difficulty)
	return eggs, nil
}

// CreateEasterEgg creates a new easter egg
func (s *EasterEggsService) CreateEasterEgg(ctx context.Context, egg *models.EasterEgg) error {
	s.metrics.IncrementRequests("CreateEasterEgg")
	defer s.metrics.ObserveRequestDuration("CreateEasterEgg", time.Now())

	if err := s.repo.CreateEasterEgg(ctx, egg); err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to create easter egg: %v", err)
		return fmt.Errorf("failed to create easter egg: %w", err)
	}

	s.logger.Infof("Created easter egg: %s", egg.ID)
	return nil
}

// UpdateEasterEgg updates an existing easter egg
func (s *EasterEggsService) UpdateEasterEgg(ctx context.Context, egg *models.EasterEgg) error {
	s.metrics.IncrementRequests("UpdateEasterEgg")
	defer s.metrics.ObserveRequestDuration("UpdateEasterEgg", time.Now())

	if err := s.repo.UpdateEasterEgg(ctx, egg); err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to update easter egg %s: %v", egg.ID, err)
		return fmt.Errorf("failed to update easter egg: %w", err)
	}

	s.logger.Infof("Updated easter egg: %s", egg.ID)
	return nil
}

// DeleteEasterEgg deletes an easter egg
func (s *EasterEggsService) DeleteEasterEgg(ctx context.Context, id string) error {
	s.metrics.IncrementRequests("DeleteEasterEgg")
	defer s.metrics.ObserveRequestDuration("DeleteEasterEgg", time.Now())

	if err := s.repo.DeleteEasterEgg(ctx, id); err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to delete easter egg %s: %v", id, err)
		return fmt.Errorf("failed to delete easter egg: %w", err)
	}

	s.logger.Infof("Deleted easter egg: %s", id)
	return nil
}

// GetPlayerProgress gets player progress for a specific easter egg
func (s *EasterEggsService) GetPlayerProgress(ctx context.Context, playerID, easterEggID string) (*models.PlayerEasterEggProgress, error) {
	s.metrics.IncrementRequests("GetPlayerProgress")
	defer s.metrics.ObserveRequestDuration("GetPlayerProgress", time.Now())

	progress, err := s.repo.GetPlayerProgress(ctx, playerID, easterEggID)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get player progress for %s:%s: %v", playerID, easterEggID, err)
		return nil, fmt.Errorf("failed to get player progress: %w", err)
	}

	return progress, nil
}

// UpdatePlayerProgress updates player progress
func (s *EasterEggsService) UpdatePlayerProgress(ctx context.Context, progress *models.PlayerEasterEggProgress) error {
	s.metrics.IncrementRequests("UpdatePlayerProgress")
	defer s.metrics.ObserveRequestDuration("UpdatePlayerProgress", time.Now())

	if err := s.repo.UpdatePlayerProgress(ctx, progress); err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to update player progress for %s:%s: %v", progress.PlayerID, progress.EasterEggID, err)
		return fmt.Errorf("failed to update player progress: %w", err)
	}

	s.logger.Debugf("Updated progress for player %s on egg %s", progress.PlayerID, progress.EasterEggID)
	return nil
}

// GetPlayerProfile gets complete player profile
func (s *EasterEggsService) GetPlayerProfile(ctx context.Context, playerID string) (*models.PlayerEasterEggProfile, error) {
	s.metrics.IncrementRequests("GetPlayerProfile")
	defer s.metrics.ObserveRequestDuration("GetPlayerProfile", time.Now())

	profile, err := s.repo.GetPlayerProfile(ctx, playerID)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get player profile for %s: %v", playerID, err)
		return nil, fmt.Errorf("failed to get player profile: %w", err)
	}

	return profile, nil
}

// DiscoverEasterEgg handles the easter egg discovery process
func (s *EasterEggsService) DiscoverEasterEgg(ctx context.Context, playerID, easterEggID string, attemptData map[string]interface{}) (*models.PlayerEasterEggProgress, error) {
	s.metrics.IncrementRequests("DiscoverEasterEgg")
	defer s.metrics.ObserveRequestDuration("DiscoverEasterEgg", time.Now())

	// Record the discovery attempt
	attempt := &models.EasterEggDiscoveryAttempt{
		ID:          fmt.Sprintf("%s-%s-%d", playerID, easterEggID, time.Now().Unix()),
		PlayerID:    playerID,
		EasterEggID: easterEggID,
		AttemptType: "api_discovery",
		Success:     true,
		AttemptedAt: time.Now(),
		ResponseTime: 100, // Placeholder
	}

	if err := s.repo.CreateDiscoveryAttempt(ctx, attempt); err != nil {
		s.logger.Warnf("Failed to record discovery attempt: %v", err)
	}

	// Get or create player progress
	progress, err := s.repo.GetPlayerProgress(ctx, playerID, easterEggID)
	if err != nil {
		// Create new progress if doesn't exist
		progress = &models.PlayerEasterEggProgress{
			PlayerID:     playerID,
			EasterEggID:  easterEggID,
			Status:       "discovered",
			DiscoveredAt: &time.Time{},
			VisitCount:   1,
			LastVisited:  time.Now(),
		}
		*progress.DiscoveredAt = time.Now()
	} else {
		// Update existing progress
		progress.Status = "discovered"
		if progress.DiscoveredAt == nil {
			progress.DiscoveredAt = &time.Time{}
			*progress.DiscoveredAt = time.Now()
		}
		progress.VisitCount++
		progress.LastVisited = time.Now()
	}

	if err := s.repo.UpdatePlayerProgress(ctx, progress); err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to update player progress: %v", err)
		return nil, fmt.Errorf("failed to update player progress: %w", err)
	}

	// Record successful discovery
	if err := s.repo.RecordSuccessfulDiscovery(ctx, playerID, easterEggID); err != nil {
		s.logger.Warnf("Failed to record successful discovery: %v", err)
	}

	// Update easter egg statistics
	if err := s.repo.UpdateEasterEggStats(ctx, easterEggID); err != nil {
		s.logger.Warnf("Failed to update easter egg stats: %v", err)
	}

	// Create discovery event
	event := &models.EasterEggEvent{
		ID:          fmt.Sprintf("event-%s-%s-%d", playerID, easterEggID, time.Now().Unix()),
		EventType:   "discovered",
		PlayerID:    playerID,
		EasterEggID: easterEggID,
		EventData:   attemptData,
		CreatedAt:   time.Now(),
		Processed:   false,
	}

	if err := s.repo.CreateEasterEggEvent(ctx, event); err != nil {
		s.logger.Warnf("Failed to create discovery event: %v", err)
	}

	s.logger.Infof("Player %s discovered easter egg %s", playerID, easterEggID)
	return progress, nil
}

// GetEasterEggStatistics gets statistics for an easter egg
func (s *EasterEggsService) GetEasterEggStatistics(ctx context.Context, easterEggID string) (*models.EasterEggStatistics, error) {
	s.metrics.IncrementRequests("GetEasterEggStatistics")
	defer s.metrics.ObserveRequestDuration("GetEasterEggStatistics", time.Now())

	stats, err := s.repo.GetEasterEggStatistics(ctx, easterEggID)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get easter egg statistics for %s: %v", easterEggID, err)
		return nil, fmt.Errorf("failed to get easter egg statistics: %w", err)
	}

	return stats, nil
}

// GetCategoryStatistics gets statistics by category
func (s *EasterEggsService) GetCategoryStatistics(ctx context.Context) ([]*models.EasterEggCategoryStats, error) {
	s.metrics.IncrementRequests("GetCategoryStatistics")
	defer s.metrics.ObserveRequestDuration("GetCategoryStatistics", time.Now())

	stats, err := s.repo.GetCategoryStatistics(ctx)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get category statistics: %v", err)
		return nil, fmt.Errorf("failed to get category statistics: %w", err)
	}

	return stats, nil
}

// GetHintsForEasterEgg gets hints for easter egg discovery
func (s *EasterEggsService) GetHintsForEasterEgg(ctx context.Context, easterEggID string, maxLevel int) ([]*models.DiscoveryHint, error) {
	s.metrics.IncrementRequests("GetHintsForEasterEgg")
	defer s.metrics.ObserveRequestDuration("GetHintsForEasterEgg", time.Now())

	hints, err := s.repo.GetHintsForEasterEgg(ctx, easterEggID, maxLevel)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get hints for easter egg %s: %v", easterEggID, err)
		return nil, fmt.Errorf("failed to get hints: %w", err)
	}

	return hints, nil
}

// GetActiveChallenges gets currently active challenges
func (s *EasterEggsService) GetActiveChallenges(ctx context.Context) ([]*models.EasterEggChallenge, error) {
	s.metrics.IncrementRequests("GetActiveChallenges")
	defer s.metrics.ObserveRequestDuration("GetActiveChallenges", time.Now())

	challenges, err := s.repo.GetActiveChallenges(ctx)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get active challenges: %v", err)
		return nil, fmt.Errorf("failed to get active challenges: %w", err)
	}

	return challenges, nil
}

// GetPlayerChallengeProgress gets player progress on a challenge
func (s *EasterEggsService) GetPlayerChallengeProgress(ctx context.Context, playerID, challengeID string) (int, error) {
	s.metrics.IncrementRequests("GetPlayerChallengeProgress")
	defer s.metrics.ObserveRequestDuration("GetPlayerChallengeProgress", time.Now())

	progress, err := s.repo.GetPlayerChallengeProgress(ctx, playerID, challengeID)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get player challenge progress for %s:%s: %v", playerID, challengeID, err)
		return 0, fmt.Errorf("failed to get player challenge progress: %w", err)
	}

	return progress, nil
}

// HealthCheck performs a health check
func (s *EasterEggsService) HealthCheck(ctx context.Context) error {
	return s.repo.HealthCheck(ctx)
}
