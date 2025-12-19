// Issue: #1856
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/guild-war-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// ListGuildWars returns list of guild wars - TYPED response!
func (h *Handlers) ListGuildWars(ctx context.Context, params api.ListGuildWarsParams) (api.ListGuildWarsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	wars, err := h.service.GetGuildWars(ctx, params)
	if err != nil {
		return &api.ListGuildWarsInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return wars, nil
}

// GetGuildWar returns guild war details - TYPED response!
func (h *Handlers) GetGuildWar(ctx context.Context, params api.GetGuildWarParams) (api.GetGuildWarRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	war, err := h.service.GetGuildWar(ctx, params)
	if err != nil {
		// Check for specific error types
		if err.Error() == "guild war not found" {
			return &api.GetGuildWarNotFound{}, nil
		}
		return &api.GetGuildWarInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return war, nil
}

// DeclareWar creates a new guild war - TYPED response!
func (h *Handlers) DeclareWar(ctx context.Context, params api.DeclareWarParams, req api.WarDeclarationRequest) (api.DeclareWarRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.DeclareWar(ctx, params, &req)
	if err != nil {
		// Check for validation errors
		if err.Error() == "war duration must be 1-24 hours" {
			return &api.DeclareWarBadRequest{}, nil
		}
		if err.Error() == "user not authenticated" {
			return &api.DeclareWarUnauthorized{}, nil
		}
		return &api.DeclareWarInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return result, nil
}

// JoinWar allows joining a war - TYPED response!
func (h *Handlers) JoinWar(ctx context.Context, params api.JoinWarParams) (api.JoinWarRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.JoinWar(ctx, params)
	if err != nil {
		// Check for specific error types
		if err == ErrAlreadyInWar {
			return &api.JoinWarConflict{}, nil
		}
		if err == ErrWarFull {
			return &api.JoinWarBadRequest{}, nil
		}
		if err.Error() == "user not authenticated" {
			return &api.JoinWarUnauthorized{}, nil
		}
		return &api.JoinWarInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return result, nil
}

// GetWarLeaderboard returns war leaderboard - TYPED response!
func (h *Handlers) GetWarLeaderboard(ctx context.Context, params api.GetWarLeaderboardParams) (api.GetWarLeaderboardRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	leaderboard, err := h.service.GetWarLeaderboard(ctx, params)
	if err != nil {
		return &api.GetWarLeaderboardInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return leaderboard, nil
}

// UpdateWarScore updates participant score - TYPED response!
func (h *Handlers) UpdateWarScore(ctx context.Context, params api.UpdateWarScoreParams, req api.ScoreUpdateRequest) (api.UpdateWarScoreRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.UpdateWarScore(ctx, params, &req)
	if err != nil {
		// Check for authorization errors
		if err == ErrAccessDenied {
			return &api.UpdateWarScoreForbidden{}, nil
		}
		if err.Error() == "user not authenticated" {
			return &api.UpdateWarScoreUnauthorized{}, nil
		}
		return &api.UpdateWarScoreInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return result, nil
}
