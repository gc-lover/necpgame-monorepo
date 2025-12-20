// Package server Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-turns-service-go/pkg/api"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1588 - Resilience patterns (Load Shedding, Circuit Breaker)
type Handlers struct {
	service     *Service
	loadShedder *LoadShedder
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	// Issue: #1588 - Resilience patterns for hot path service
	loadShedder := NewLoadShedder(500) // Max 500 concurrent requests

	return &Handlers{
		service:     service,
		loadShedder: loadShedder,
	}
}

// GetCurrentTurn - TYPED response!
func (h *Handlers) GetCurrentTurn(ctx context.Context, params api.GetCurrentTurnParams) (api.GetCurrentTurnRes, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !h.loadShedder.Allow() {
		err := api.GetCurrentTurnInternalServerError(api.Error{
			Error:   "ServiceUnavailable",
			Message: "service overloaded, please try again later",
			Code:    api.NewOptNilString("503"),
		})
		return &err, nil
	}
	defer h.loadShedder.Done()

	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetCurrentTurn(params.SessionId)
	if err != nil {
		return &api.GetCurrentTurnInternalServerError{}, err
	}

	return result, nil
}

// GetTurnOrder - TYPED response!
func (h *Handlers) GetTurnOrder(ctx context.Context, params api.GetTurnOrderParams) (api.GetTurnOrderRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetTurnOrder(params.SessionId)
	if err != nil {
		return &api.GetTurnOrderInternalServerError{}, err
	}

	return result, nil
}

// NextTurn - TYPED response!
func (h *Handlers) NextTurn(ctx context.Context, params api.NextTurnParams) (api.NextTurnRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.NextTurn(params.SessionId)
	if err != nil {
		return &api.NextTurnInternalServerError{}, err
	}

	return result, nil
}

// SkipTurn - TYPED response!
func (h *Handlers) SkipTurn(ctx context.Context, _ *api.SkipTurnRequest, params api.SkipTurnParams) (api.SkipTurnRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.SkipTurn(params.SessionId)
	if err != nil {
		return &api.SkipTurnInternalServerError{}, err
	}

	return result, nil
}
