// Package server Issue: #1856
package server

import (
	"context"
	"errors"
	"sync"

	"github.com/gc-lover/necpgame-monorepo/services/guild-territory-service-go/pkg/api"
	"github.com/google/uuid"
)

// Common errors
var (
	ErrTerritoryNotFound = errors.New("territory not found")
	ErrAlreadyOwned      = errors.New("territory already owned")
	_                    = errors.New("territory not owned by guild")
	ErrClaimInProgress   = errors.New("territory claim already in progress")
)

// Service implements business logic for guild territory service
// SOLID: Single Responsibility - business logic only
// Issue: #1856 - Memory pooling for hot path (Level 2 optimization)
type Service struct {
	repo *Repository

	// Memory pooling for hot path structs (zero allocations target!)
	territoryResponsePool sync.Pool
	claimResponsePool     sync.Pool
	bonusesResponsePool   sync.Pool
}

// NewService creates new service with dependency injection and memory pooling
func NewService(repo *Repository) *Service {
	s := &Service{repo: repo}

	// Initialize memory pools (zero allocations target!)
	s.territoryResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.GetTerritoryOK{}
		},
	}
	s.claimResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.ClaimTerritoryOK{}
		},
	}
	s.bonusesResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.GetTerritoryBonusesOK{}
		},
	}

	return s
}

// GetTerritories retrieves territories with optional filtering - BUSINESS LOGIC
func (s *Service) GetTerritories(ctx context.Context, params api.ListTerritoriesParams) (*api.ListTerritoriesOK, error) {
	// Convert API parameters to internal types
	var controlType *string
	if params.ControlType.IsSet() {
		controlTypeStr := string(params.ControlType.Value)
		controlType = &controlTypeStr
	}

	var limit *int
	if params.Limit.IsSet() {
		limitVal := int(params.Limit.Value)
		limit = &limitVal
	}

	// Call repository
	territories, err := s.repo.GetTerritories(ctx, controlType, limit)
	if err != nil {
		return nil, err
	}

	// Convert to API response
	response := &api.ListTerritoriesOK{}
	// TODO: Convert internal Territory models to API Territory models
	// This will be implemented when the API models are defined

	_ = territories // Remove when implemented
	return response, nil
}

// GetTerritory retrieves a single territory by ID - BUSINESS LOGIC
func (s *Service) GetTerritory(ctx context.Context, params api.GetTerritoryParams) (*api.GetTerritoryOK, error) {
	// Call repository
	territory, err := s.repo.GetTerritoryByID(ctx, params.TerritoryID)
	if err != nil {
		if err == ErrTerritoryNotFound {
			return nil, errors.New("territory not found")
		}
		return nil, err
	}

	// Convert to API response
	response := s.territoryResponsePool.Get().(*api.GetTerritoryOK)
	defer s.territoryResponsePool.Put(response)

	// TODO: Convert internal Territory model to API Territory model
	// This will be implemented when the API models are defined

	_ = territory // Remove when implemented
	return response, nil
}

// ClaimTerritory initiates territory claim - BUSINESS LOGIC
func (s *Service) ClaimTerritory(ctx context.Context, params api.ClaimTerritoryParams) (*api.ClaimTerritoryOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// TODO: Check if user is guild leader/officer
	// TODO: Check if territory is claimable (not already owned, not contested)
	// For now, allow any user

	// Get user's guild ID (this would come from user context or database)
	guildID := uuid.New() // Placeholder

	// Call repository
	claim, err := s.repo.ClaimTerritory(ctx, params.TerritoryID, guildID)
	if err != nil {
		return nil, err
	}

	// Return response
	response := s.claimResponsePool.Get().(*api.ClaimTerritoryOK)
	defer s.claimResponsePool.Put(response)

	response.Message.Set("Territory claim initiated")
	return response, nil
}

// GetTerritoryBonuses calculates territory bonuses - BUSINESS LOGIC
func (s *Service) GetTerritoryBonuses(ctx context.Context) (*api.GetTerritoryBonusesOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// TODO: Check if user is member of guild that owns the territory

	// Call repository
	bonuses, err := s.repo.CalculateTerritoryBonuses()
	if err != nil {
		return nil, err
	}

	// Convert to API response
	response := s.bonusesResponsePool.Get().(*api.GetTerritoryBonusesOK)
	defer s.bonusesResponsePool.Put(response)

	// TODO: Convert bonuses map to API Bonuses model
	// This will be implemented when the API models are defined

	_ = bonuses // Remove when implemented
	return response, nil
}

// GetGuildTerritories retrieves territories owned by user's guild - BUSINESS LOGIC
func (s *Service) GetGuildTerritories(ctx context.Context) (*api.ListGuildTerritoriesOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// TODO: Get user's guild ID from context
	guildID := uuid.New() // Placeholder

	// Call repository
	territories, err := s.repo.GetGuildTerritories(ctx, guildID)
	if err != nil {
		return nil, err
	}

	// Convert to API response
	response := &api.ListGuildTerritoriesOK{}
	// TODO: Convert internal Territory models to API Territory models
	// This will be implemented when the API models are defined

	_ = territories // Remove when implemented
	return response, nil
}

// GetTerritoryWars retrieves wars for a territory - BUSINESS LOGIC
func (s *Service) GetTerritoryWars(ctx context.Context, params api.GetTerritoryWarsParams) (*api.ListTerritoryWarsOK, error) {
	// Call repository
	wars, err := s.repo.GetTerritoryWars(ctx, params.TerritoryID)
	if err != nil {
		return nil, err
	}

	// Convert to API response
	response := &api.ListTerritoryWarsOK{}
	// TODO: Convert internal TerritoryWar models to API TerritoryWar models
	// This will be implemented when the API models are defined

	_ = wars // Remove when implemented
	return response, nil
}
