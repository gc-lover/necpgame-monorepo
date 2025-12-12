package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/go-faster/jx"
)

// Combat session and gameplay handlers

// CreateCombatSession implements POST /gameplay/combat/sessions
// Issue: #1607
func (h *Handlers) CreateCombatSession(ctx context.Context, req *api.CreateSessionRequest) (api.CreateCombatSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.combatSessionService == nil {
		return &api.CreateCombatSessionBadRequest{}, nil
	}

	session, err := h.combatSessionService.CreateSession(ctx, req)
	if err != nil {
		return &api.CreateCombatSessionBadRequest{}, nil
	}

	return session, nil
}

// EndCombatSession implements DELETE /gameplay/combat/sessions/{sessionId}
// Issue: #1607
func (h *Handlers) EndCombatSession(ctx context.Context, params api.EndCombatSessionParams) (api.EndCombatSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.combatSessionService == nil {
		return &api.Error{}, nil
	}

	response, err := h.combatSessionService.EndSession(ctx, params.SessionId)
	if err != nil {
		return &api.Error{}, nil
	}

	return response, nil
}

// GetCombatSession implements GET /gameplay/combat/sessions/{sessionId}
// Issue: #1607
func (h *Handlers) GetCombatSession(ctx context.Context, params api.GetCombatSessionParams) (api.GetCombatSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.combatSessionService == nil {
		return &api.Error{}, nil
	}

	session, err := h.combatSessionService.GetSession(ctx, params.SessionId)
	if err != nil {
		return &api.Error{}, nil
	}

	return session, nil
}

// ListCombatSessions implements GET /gameplay/combat/sessions
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) ListCombatSessions(ctx context.Context, params api.ListCombatSessionsParams) (*api.SessionListResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.combatSessionService == nil {
		return &api.SessionListResponse{}, nil
	}

	limit := 20
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	offset := 0
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	var status *api.SessionStatus
	if params.Status.IsSet() {
		status = &params.Status.Value
	}

	var sessionType *api.SessionType
	if params.SessionType.IsSet() {
		sessionType = &params.SessionType.Value
	}

	return h.combatSessionService.ListSessions(ctx, status, sessionType, limit, offset)
}

// GetArenaSessions implements GET /gameplay/arena/sessions
func (h *Handlers) GetArenaSessions(ctx context.Context) (api.GetArenaSessionsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetArenaSessionsOK{Sessions: []jx.Raw{}}, nil
}

// GetExtractZones implements GET /gameplay/extract-zones
func (h *Handlers) GetExtractZones(ctx context.Context) (api.GetExtractZonesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetExtractZonesOK{Zones: []jx.Raw{}}, nil
}

// GetFreerunRoutes implements GET /gameplay/freerun/routes
func (h *Handlers) GetFreerunRoutes(ctx context.Context, params api.GetFreerunRoutesParams) (api.GetFreerunRoutesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetFreerunRoutesOK{Routes: []jx.Raw{}}, nil
}
