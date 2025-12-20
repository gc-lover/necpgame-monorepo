// Package server Issue: #1856
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/guild-territory-service-go/pkg/api"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// ListTerritories returns list of territories - TYPED response!
func (h *Handlers) ListTerritories(ctx context.Context, params api.ListTerritoriesParams) (api.ListTerritoriesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	territories, err := h.service.GetTerritories(ctx, params)
	if err != nil {
		return &api.ListTerritoriesInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return territories, nil
}

// GetTerritory returns territory details - TYPED response!
func (h *Handlers) GetTerritory(ctx context.Context, params api.GetTerritoryParams) (api.GetTerritoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	territory, err := h.service.GetTerritory(ctx, params)
	if err != nil {
		// Check for specific error types
		if err.Error() == "territory not found" {
			return &api.GetTerritoryNotFound{}, nil
		}
		return &api.GetTerritoryInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return territory, nil
}

// ClaimTerritory initiates territory claim - TYPED response!
func (h *Handlers) ClaimTerritory(ctx context.Context, params api.ClaimTerritoryParams) (api.ClaimTerritoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ClaimTerritory(ctx, params)
	if err != nil {
		// Check for validation errors
		if err == ErrAlreadyOwned {
			return &api.ClaimTerritoryConflict{}, nil
		}
		if err == ErrClaimInProgress {
			return &api.ClaimTerritoryConflict{}, nil
		}
		if err.Error() == "user not authenticated" {
			return &api.ClaimTerritoryUnauthorized{}, nil
		}
		return &api.ClaimTerritoryInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return result, nil
}

// GetTerritoryBonuses returns territory bonuses - TYPED response!
func (h *Handlers) GetTerritoryBonuses(ctx context.Context, params api.GetTerritoryBonusesParams) (api.GetTerritoryBonusesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	bonuses, err := h.service.GetTerritoryBonuses(ctx)
	if err != nil {
		return &api.GetTerritoryBonusesInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return bonuses, nil
}

// ListGuildTerritories returns guild territories - TYPED response!
func (h *Handlers) ListGuildTerritories(ctx context.Context) (api.ListGuildTerritoriesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	territories, err := h.service.GetGuildTerritories(ctx)
	if err != nil {
		return &api.ListGuildTerritoriesInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return territories, nil
}

// GetTerritoryWars returns territory wars - TYPED response!
func (h *Handlers) GetTerritoryWars(ctx context.Context, params api.GetTerritoryWarsParams) (api.GetTerritoryWarsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	wars, err := h.service.GetTerritoryWars(ctx, params)
	if err != nil {
		return &api.GetTerritoryWarsInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return wars, nil
}
