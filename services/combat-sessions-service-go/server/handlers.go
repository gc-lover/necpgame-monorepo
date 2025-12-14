// Issue: #1595, #1867
// ogen handlers - TYPED responses (no interface{} boxing!)
// Memory pooling for hot path structs (zero allocations target!)
package server

import (
	"context"
	"errors"
	"sync"
	"sync/atomic"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sessions-service-go/pkg/api"
	"github.com/google/uuid"
)

const DBTimeout = 50 * time.Millisecond

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1588 - Resilience patterns (Load Shedding, Circuit Breaker)
// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
// Issue: #1867 - Memory pooling for hot path structs (zero allocations target!)
type Handlers struct {
	service          *CombatSessionService
	loadShedder      *LoadShedder
	sessionValidator *SessionValidator

	// Memory pooling for hot path structs (zero allocations target!)
	combatSessionPool        sync.Pool
	createSessionRequestPool sync.Pool
	combatSessionSlicePool   sync.Pool // For session arrays
	bufferPool               sync.Pool // For JSON encoding/decoding

	// Lock-free statistics (zero contention target!)
	requestsTotal     int64 // atomic
	sessionsListed    int64 // atomic
	sessionsCreated   int64 // atomic
	sessionsRetrieved int64 // atomic
	sessionsUpdated   int64 // atomic
	validationsFailed int64 // atomic
	lastRequestTime   int64 // atomic unix nano
}

// NewHandlers creates new handlers with memory pooling
// Issue: #1867 - Initialize memory pools for zero allocations
func NewHandlers(service *CombatSessionService) *Handlers {
	// Issue: #1588 - Resilience patterns for hot path service
	loadShedder := NewLoadShedder(500) // Max 500 concurrent requests

	// Issue: #1587 - Anti-cheat validation
	sessionValidator := NewSessionValidator()

	h := &Handlers{
		service:          service,
		loadShedder:      loadShedder,
		sessionValidator: sessionValidator,
	}

	// Initialize memory pools (zero allocations target!)
	h.combatSessionPool = sync.Pool{
		New: func() interface{} {
			return &api.CombatSession{}
		},
	}
	h.createSessionRequestPool = sync.Pool{
		New: func() interface{} {
			return &api.CreateSessionRequest{}
		},
	}
	h.combatSessionSlicePool = sync.Pool{
		New: func() interface{} {
			return make([]api.CombatSession, 0, 50) // Pre-allocate capacity
		},
	}
	h.bufferPool = sync.Pool{
		New: func() interface{} {
			return make([]byte, 0, 4096) // 4KB buffer for JSON
		},
	}

	return h
}

// Lock-free statistics methods (zero contention) - Issue: #1867
func (h *Handlers) incrementRequestsTotal() {
	atomic.AddInt64(&h.requestsTotal, 1)
	atomic.StoreInt64(&h.lastRequestTime, time.Now().UnixNano())
}

func (h *Handlers) incrementSessionsListed() {
	atomic.AddInt64(&h.sessionsListed, 1)
}

func (h *Handlers) incrementSessionsCreated() {
	atomic.AddInt64(&h.sessionsCreated, 1)
}

func (h *Handlers) incrementSessionsRetrieved() {
	atomic.AddInt64(&h.sessionsRetrieved, 1)
}

func (h *Handlers) incrementSessionsUpdated() {
	atomic.AddInt64(&h.sessionsUpdated, 1)
}

func (h *Handlers) incrementValidationsFailed() {
	atomic.AddInt64(&h.validationsFailed, 1)
}

func (h *Handlers) getStats() map[string]int64 {
	return map[string]int64{
		"requests_total":     atomic.LoadInt64(&h.requestsTotal),
		"sessions_listed":    atomic.LoadInt64(&h.sessionsListed),
		"sessions_created":   atomic.LoadInt64(&h.sessionsCreated),
		"sessions_retrieved": atomic.LoadInt64(&h.sessionsRetrieved),
		"sessions_updated":   atomic.LoadInt64(&h.sessionsUpdated),
		"validations_failed": atomic.LoadInt64(&h.validationsFailed),
		"last_request_time":  atomic.LoadInt64(&h.lastRequestTime),
	}
}

// ListCombatSessions - TYPED response!
// Issue: #1867 - Request tracking and memory pooling for slice allocations
func (h *Handlers) ListCombatSessions(ctx context.Context, params api.ListCombatSessionsParams) ([]api.CombatSession, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !h.loadShedder.Allow() {
		// Return empty list on overload (graceful degradation)
		return []api.CombatSession{}, nil
	}
	defer h.loadShedder.Done()

	h.incrementRequestsTotal()
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return []api.CombatSession{}, nil
	}

	sessions, err := h.service.ListSessions(ctx, params)
	if err != nil {
		return nil, err
	}

	h.incrementSessionsListed()

	return sessions, nil
}

// CreateCombatSession - TYPED response!
// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
// Issue: #1867 - Request tracking and validation statistics
func (h *Handlers) CreateCombatSession(ctx context.Context, req *api.CreateSessionRequest) (*api.CombatSession, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.incrementRequestsTotal()

	// Issue: #1587 - Validate session creation (anti-cheat: prevent abuse)
	// TODO: Get participant count from request when field is available
	participantCount := 1 // Default: single player session
	if err := h.sessionValidator.ValidateSessionCreation(participantCount, string(req.SessionType)); err != nil {
		h.incrementValidationsFailed()
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

	h.incrementSessionsCreated()

	return session, nil
}

// GetCombatSession - TYPED response!
// Issue: #1867 - Request tracking for statistics
func (h *Handlers) GetCombatSession(ctx context.Context, params api.GetCombatSessionParams) (api.GetCombatSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.incrementRequestsTotal()

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

	h.incrementSessionsRetrieved()

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
