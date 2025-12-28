// Issue: #1495 - Gameplay Affixes Service implementation
// PERFORMANCE: Affix service with optimized business logic and caching

package service

import (
	"context"
	"fmt"
	"math/rand"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"gameplay-affixes-service-go/internal/models"
	"gameplay-affixes-service-go/internal/repository"
)

// AffixService defines affix business logic operations
type AffixService interface {
	// Affix operations
	CreateAffix(ctx context.Context, affix *models.Affix) error
	GetAffix(ctx context.Context, id uuid.UUID) (*models.Affix, error)
	ListAffixes(ctx context.Context, limit, offset int) ([]models.Affix, error)
	UpdateAffix(ctx context.Context, affix *models.Affix) error
	DeleteAffix(ctx context.Context, id uuid.UUID) error

	// Active affixes operations
	GetActiveAffixes(ctx context.Context) (*models.ActiveAffixesResponse, error)
	RotateAffixes(ctx context.Context, customAffixes []uuid.UUID, force bool) error

	// Instance operations
	GetInstanceAffixes(ctx context.Context, instanceID uuid.UUID) (*models.InstanceAffixesResponse, error)
	GenerateInstanceAffixes(ctx context.Context, instanceID uuid.UUID) (*models.InstanceAffixesResponse, error)

	// Rotation operations
	GetAffixRotationHistory(ctx context.Context, weeksBack, limit, offset int) ([]models.AffixRotation, error)
}

// affixService implements AffixService
type affixService struct {
	repo   repository.AffixRepository
	logger *zap.Logger
}

// NewAffixService creates a new affix service
func NewAffixService(repo repository.AffixRepository, logger *zap.Logger) AffixService {
	return &affixService{
		repo:   repo,
		logger: logger,
	}
}

// CreateAffix creates a new affix with validation
func (s *affixService) CreateAffix(ctx context.Context, affix *models.Affix) error {
	if affix.Name == "" {
		return fmt.Errorf("affix name cannot be empty")
	}
	if affix.RewardModifier < 1.0 {
		return fmt.Errorf("reward modifier must be >= 1.0")
	}
	if affix.DifficultyModifier < 1.0 {
		return fmt.Errorf("difficulty modifier must be >= 1.0")
	}

	affix.ID = uuid.New()
	affix.CreatedAt = time.Now()

	return s.repo.CreateAffix(ctx, affix)
}

// GetAffix retrieves an affix by ID
func (s *affixService) GetAffix(ctx context.Context, id uuid.UUID) (*models.Affix, error) {
	return s.repo.GetAffix(ctx, id)
}

// ListAffixes retrieves a paginated list of affixes
func (s *affixService) ListAffixes(ctx context.Context, limit, offset int) ([]models.Affix, error) {
	if limit <= 0 || limit > 100 {
		limit = 20 // default limit
	}
	if offset < 0 {
		offset = 0
	}

	return s.repo.ListAffixes(ctx, limit, offset)
}

// UpdateAffix updates an existing affix
func (s *affixService) UpdateAffix(ctx context.Context, affix *models.Affix) error {
	if affix.Name == "" {
		return fmt.Errorf("affix name cannot be empty")
	}
	if affix.RewardModifier < 1.0 {
		return fmt.Errorf("reward modifier must be >= 1.0")
	}
	if affix.DifficultyModifier < 1.0 {
		return fmt.Errorf("difficulty modifier must be >= 1.0")
	}

	return s.repo.UpdateAffix(ctx, affix)
}

// DeleteAffix deletes an affix
func (s *affixService) DeleteAffix(ctx context.Context, id uuid.UUID) error {
	return s.repo.DeleteAffix(ctx, id)
}

// GetActiveAffixes retrieves the currently active affixes
func (s *affixService) GetActiveAffixes(ctx context.Context) (*models.ActiveAffixesResponse, error) {
	return s.repo.GetActiveAffixes(ctx)
}

// RotateAffixes rotates affixes for the new week
func (s *affixService) RotateAffixes(ctx context.Context, customAffixes []uuid.UUID, force bool) error {
	// Check if rotation already exists for current week (unless forced)
	if !force {
		currentRotation, err := s.repo.GetCurrentRotation(ctx)
		if err == nil && currentRotation != nil {
			return fmt.Errorf("rotation already exists for current week")
		}
	}

	var affixesToSet []uuid.UUID

	if len(customAffixes) > 0 {
		// Use custom affixes
		if len(customAffixes) < 8 || len(customAffixes) > 10 {
			return fmt.Errorf("custom affixes must be between 8 and 10")
		}
		affixesToSet = customAffixes
	} else {
		// Generate random affixes (8 active + optional seasonal)
		randomAffixes, err := s.repo.GetRandomAffixes(ctx, 9, []uuid.UUID{}) // 8 + 1 for seasonal
		if err != nil {
			return fmt.Errorf("failed to get random affixes: %w", err)
		}

		affixesToSet = randomAffixes[:8] // First 8 are active

		// 10% chance for seasonal affix
		if rand.Float64() < 0.1 && len(randomAffixes) > 8 {
			seasonalAffix := randomAffixes[8]
			return s.repo.SetActiveAffixes(ctx, affixesToSet, &seasonalAffix)
		}
	}

	return s.repo.SetActiveAffixes(ctx, affixesToSet, nil)
}

// GetInstanceAffixes retrieves affixes for a specific instance
func (s *affixService) GetInstanceAffixes(ctx context.Context, instanceID uuid.UUID) (*models.InstanceAffixesResponse, error) {
	return s.repo.GetInstanceAffixes(ctx, instanceID)
}

// GenerateInstanceAffixes generates and applies random affixes to an instance
func (s *affixService) GenerateInstanceAffixes(ctx context.Context, instanceID uuid.UUID) (*models.InstanceAffixesResponse, error) {
	// Get active affixes
	activeAffixes, err := s.repo.GetActiveAffixes(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get active affixes: %w", err)
	}

	if len(activeAffixes.ActiveAffixes) == 0 {
		return nil, fmt.Errorf("no active affixes available")
	}

	// Select 2-4 random affixes from active ones
	affixCount := 2 + rand.Intn(3) // 2-4 affixes
	selectedAffixes := make([]uuid.UUID, 0, affixCount)

	// Shuffle active affixes
	activeIDs := make([]uuid.UUID, len(activeAffixes.ActiveAffixes))
	for i, affix := range activeAffixes.ActiveAffixes {
		activeIDs[i] = affix.ID
	}

	// Fisher-Yates shuffle
	for i := len(activeIDs) - 1; i > 0; i-- {
		j := rand.Intn(i + 1)
		activeIDs[i], activeIDs[j] = activeIDs[j], activeIDs[i]
	}

	// Take first N affixes
	for i := 0; i < affixCount && i < len(activeIDs); i++ {
		selectedAffixes = append(selectedAffixes, activeIDs[i])
	}

	// Apply to instance
	err = s.repo.SetInstanceAffixes(ctx, instanceID, selectedAffixes)
	if err != nil {
		return nil, fmt.Errorf("failed to set instance affixes: %w", err)
	}

	// Return the result
	return s.repo.GetInstanceAffixes(ctx, instanceID)
}

// GetAffixRotationHistory retrieves the history of affix rotations
func (s *affixService) GetAffixRotationHistory(ctx context.Context, weeksBack, limit, offset int) ([]models.AffixRotation, error) {
	if weeksBack <= 0 || weeksBack > 52 {
		weeksBack = 4 // default 4 weeks
	}
	if limit <= 0 || limit > 100 {
		limit = 20 // default limit
	}
	if offset < 0 {
		offset = 0
	}

	return s.repo.GetAffixRotationHistory(ctx, weeksBack, limit, offset)
}
