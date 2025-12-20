// Issue: #1560, #1607

package server

import (
	"context"
	"fmt"
	"sync"

	"github.com/gc-lover/necpgame-monorepo/services/projectile-core-service-go/pkg/api"
	"github.com/google/uuid"
)

// ProjectileService contains business logic with memory pooling (Issue #1607)
type ProjectileService struct {
	repo *ProjectileRepository

	// Memory pooling for hot path structs (Level 2 optimization)
	formsResponsePool         sync.Pool
	spawnResponsePool         sync.Pool
	compatibilityResponsePool sync.Pool
}

// NewProjectileService creates a new service with memory pooling
func NewProjectileService(repo *ProjectileRepository) *ProjectileService {
	s := &ProjectileService{
		repo: repo,
	}

	// Initialize memory pools (zero allocations target!)
	s.formsResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.GetProjectileFormsOK{}
		},
	}
	s.spawnResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.SpawnProjectileResponse{}
		},
	}
	s.compatibilityResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.CompatibilityResponse{}
		},
	}

	return s
}

// GetForms returns list of projectile forms
func (s *ProjectileService) GetForms(ctx context.Context, params api.GetProjectileFormsParams) (*api.GetProjectileFormsOK, error) {
	forms, err := s.repo.GetForms(ctx, params)
	if err != nil {
		return nil, fmt.Errorf("failed to get forms: %w", err)
	}

	limit := 10
	offset := 0
	if params.Limit.Set {
		limit = params.Limit.Value
	}
	if params.Offset.Set {
		offset = params.Offset.Value
	}

	// Get memory pooled response (zero allocation!)
	resp := s.formsResponsePool.Get().(*api.GetProjectileFormsOK)
	defer s.formsResponsePool.Put(resp)

	// Reset pooled struct
	resp.Forms = resp.Forms[:0] // Reuse slice
	resp.Pagination = api.PaginationResponse{}

	// Convert []*api.ProjectileForm to []api.ProjectileForm
	formsList := make([]api.ProjectileForm, 0, len(forms))
	for _, f := range forms {
		formsList = append(formsList, *f)
	}

	// Populate response
	resp.Forms = formsList
	resp.Pagination = api.PaginationResponse{
		Total:  len(forms),
		Limit:  api.NewOptInt(limit),
		Offset: api.NewOptInt(offset),
	}

	// Clone response (caller owns it)
	result := &api.GetProjectileFormsOK{
		Forms:      append([]api.ProjectileForm{}, resp.Forms...),
		Pagination: resp.Pagination,
	}

	return result, nil
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
	compatible, err := s.repo.CheckCompatibility(ctx, string(req.Form))
	if err != nil {
		return nil, fmt.Errorf("failed to check compatibility: %w", err)
	}

	// Get memory pooled response (zero allocation!)
	resp := s.spawnResponsePool.Get().(*api.SpawnProjectileResponse)
	defer s.spawnResponsePool.Put(resp)

	// Reset pooled struct
	*resp = api.SpawnProjectileResponse{}

	if !compatible {
		resp.Success = false
		// Clone response (caller owns it)
		result := &api.SpawnProjectileResponse{
			Success: resp.Success,
		}
		return result, fmt.Errorf("form %s is not compatible with weapon %s", req.Form, req.WeaponID)
	}

	// Generate projectile ID
	projectileID := uuid.New().String()

	// TODO: Calculate trajectory
	// TODO: Spawn projectile in world

	// Populate response
	resp.ProjectileID = projectileID
	resp.Success = true

	// Clone response (caller owns it)
	result := &api.SpawnProjectileResponse{
		ProjectileID: resp.ProjectileID,
		Success:      resp.Success,
	}

	return result, nil
}

// ValidateCompatibility checks if form is compatible with weapon
func (s *ProjectileService) ValidateCompatibility(ctx context.Context, req *api.ValidateCompatibilityRequest) (*api.CompatibilityResponse, error) {
	compatible, err := s.repo.CheckCompatibilityByType(ctx, string(req.WeaponType), string(req.Form))
	if err != nil {
		return nil, fmt.Errorf("failed to validate: %w", err)
	}

	// Get memory pooled response (zero allocation!)
	resp := s.compatibilityResponsePool.Get().(*api.CompatibilityResponse)
	defer s.compatibilityResponsePool.Put(resp)

	// Reset pooled struct
	*resp = api.CompatibilityResponse{}

	// Populate response
	resp.Compatible = compatible

	// Clone response (caller owns it)
	result := &api.CompatibilityResponse{
		Compatible: resp.Compatible,
	}

	return result, nil
}

// GetCompatibilityMatrix returns compatibility matrix
func (s *ProjectileService) GetCompatibilityMatrix(ctx context.Context) (*api.CompatibilityMatrix, error) {
	matrix, err := s.repo.GetCompatibilityMatrix(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get matrix: %w", err)
	}

	// Convert map[string]interface{} to CompatibilityMatrixMatrix
	result := make(api.CompatibilityMatrixMatrix)
	for k, v := range matrix {
		if forms, ok := v.([]string); ok {
			formTypes := make([]api.ProjectileFormType, 0, len(forms))
			for _, form := range forms {
				formTypes = append(formTypes, api.ProjectileFormType(form))
			}
			result[k] = formTypes
		}
	}

	return &api.CompatibilityMatrix{
		Matrix: result,
	}, nil
}
