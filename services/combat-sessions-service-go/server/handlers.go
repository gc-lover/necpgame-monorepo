// Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sessions-service-go/pkg/api"
	"github.com/google/uuid"
)

const DBTimeout = 50 * time.Millisecond

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1588 - Resilience patterns (Load Shedding, Circuit Breaker)
// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
type Handlers struct {
	service          *CombatSessionService
	loadShedder      *LoadShedder
	sessionValidator *SessionValidator
}

// NewHandlers creates new handlers
func NewHandlers(service *CombatSessionService) *Handlers {
	// Issue: #1588 - Resilience patterns for hot path service
	loadShedder := NewLoadShedder(500) // Max 500 concurrent requests
	
	// Issue: #1587 - Anti-cheat validation
	sessionValidator := NewSessionValidator()
	
	return &Handlers{
		service:          service,
		loadShedder:      loadShedder,
		sessionValidator: sessionValidator,
	}
}

// ListCombatSessions - TYPED response!
func (h *Handlers) ListCombatSessions(ctx context.Context, params api.ListCombatSessionsParams) ([]api.CombatSession, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !h.loadShedder.Allow() {
		// Return empty list on overload (graceful degradation)
		return []api.CombatSession{}, nil
	}
	defer h.loadShedder.Done()

	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return []api.CombatSession{}, nil
	}

	sessions, err := h.service.ListSessions(ctx, params)
	if err != nil {
		return nil, err
	}

	return sessions, nil
}

// CreateCombatSession - TYPED response!
// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
func (h *Handlers) CreateCombatSession(ctx context.Context, req *api.CreateSessionRequest) (*api.CombatSession, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Issue: #1587 - Validate session creation (anti-cheat: prevent abuse)
	// TODO: Get participant count from request when field is available
	participantCount := 1 // Default: single player session
	if err := h.sessionValidator.ValidateSessionCreation(participantCount, string(req.SessionType)); err != nil {
		// Return validation error (will be handled by ogen)
		return nil, err
	}

	if h.service == nil {
		return nil, errors.New("service not initialized")
	}

	session, err := h.service.CreateSession(ctx, req)
	if err != nil {
		return nil, err
	}

	return session, nil
}

// GetCombatSession - TYPED response!
func (h *Handlers) GetCombatSession(ctx context.Context, params api.GetCombatSessionParams) (api.GetCombatSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.Error{
			Error:   "ServiceNotInitialized",
			Message: "Service not initialized",
			Code:    api.NewOptNilString("500"),
		}, nil
	}

	session, err := h.service.GetSession(ctx, params.SessionID.String())
	if err != nil {
		return &api.Error{
			Error:   "NotFound",
			Message: "Session not found",
			Code:    api.NewOptNilString("404"),
		}, nil
	}

	return session, nil
}

// EndCombatSession - TYPED response!
func (h *Handlers) EndCombatSession(ctx context.Context, params api.EndCombatSessionParams) (api.EndCombatSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.Error{
			Error:   "ServiceNotInitialized",
			Message: "Service not initialized",
			Code:    api.NewOptNilString("500"),
		}, nil
	}

	// TODO: Get playerID from context (from SecurityHandler)
	playerID := uuid.New().String()
	
	err := h.service.EndSession(ctx, params.SessionID.String(), playerID)
	if err != nil {
		return &api.Error{
			Error:   "NotFound",
			Message: "Session not found",
			Code:    api.NewOptNilString("404"),
		}, nil
	}

	return &api.EndCombatSessionOK{}, nil
}
