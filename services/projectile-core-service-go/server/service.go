// Issue: #1560

package server

import (
	"context"
	"fmt"

	"github.com/gc-lover/necpgame-monorepo/services/projectile-core-service-go/pkg/api"
	"github.com/google/uuid"
)

// ProjectileService contains business logic
type ProjectileService struct {
	repo *ProjectileRepository
}

// NewProjectileService creates a new service
func NewProjectileService(repo *ProjectileRepository) *ProjectileService {
	return &ProjectileService{
		repo: repo,
	}
}

// GetForms returns list of projectile forms
func (s *ProjectileService) GetForms(ctx context.Context, params api.GetProjectileFormsParams) (interface{}, error) {
	forms, err := s.repo.GetForms(ctx, params)
	if err != nil {
		return nil, fmt.Errorf("failed to get forms: %w", err)
	}

	return map[string]interface{}{
		"forms": forms,
		"pagination": map[string]interface{}{
			"total":  len(forms),
			"limit":  params.Limit,
			"offset": params.Offset,
		},
	}, nil
}

// GetForm returns a single form by ID
func (s *ProjectileService) GetForm(ctx context.Context, formID string) (*api.ProjectileForm, error) {
	form, err := s.repo.GetFormByID(ctx, formID)
	if err != nil {
		return nil, fmt.Errorf("failed to get form: %w", err)
	}
	return form, nil
}

// SpawnProjectile creates a new projectile
func (s *ProjectileService) SpawnProjectile(ctx context.Context, req *api.SpawnProjectileRequest) (*api.SpawnProjectileResponse, error) {
	// Validate compatibility
	compatible, err := s.repo.CheckCompatibility(ctx, req.WeaponId, string(req.Form))
	if err != nil {
		return nil, fmt.Errorf("failed to check compatibility: %w", err)
	}

	if !compatible {
		return &api.SpawnProjectileResponse{
			Success: false,
		}, fmt.Errorf("form %s is not compatible with weapon %s", req.Form, req.WeaponId)
	}

	// Generate projectile ID
	projectileID := uuid.New().String()

	// TODO: Calculate trajectory
	// TODO: Spawn projectile in world

	return &api.SpawnProjectileResponse{
		ProjectileId: projectileID,
		Success:      true,
	}, nil
}

// ValidateCompatibility checks if form is compatible with weapon
func (s *ProjectileService) ValidateCompatibility(ctx context.Context, req *api.ValidateCompatibilityRequest) (*api.CompatibilityResponse, error) {
	compatible, err := s.repo.CheckCompatibilityByType(ctx, string(req.WeaponType), string(req.Form))
	if err != nil {
		return nil, fmt.Errorf("failed to validate: %w", err)
	}

	return &api.CompatibilityResponse{
		Compatible: compatible,
	}, nil
}

// GetCompatibilityMatrix returns compatibility matrix
func (s *ProjectileService) GetCompatibilityMatrix(ctx context.Context) (*api.CompatibilityMatrix, error) {
	matrix, err := s.repo.GetCompatibilityMatrix(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get matrix: %w", err)
	}

	return &api.CompatibilityMatrix{
		Matrix: matrix,
	}, nil
}

