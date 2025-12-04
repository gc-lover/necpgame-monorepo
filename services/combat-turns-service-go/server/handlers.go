// Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-turns-service-go/pkg/api"
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

// GetCurrentTurn - TYPED response!
func (h *Handlers) GetCurrentTurn(ctx context.Context, params api.GetCurrentTurnParams) (api.GetCurrentTurnRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetCurrentTurn(ctx, params.SessionId)
	if err != nil {
		return &api.GetCurrentTurnInternalServerError{}, err
	}

	return result, nil
}

// GetTurnOrder - TYPED response!
func (h *Handlers) GetTurnOrder(ctx context.Context, params api.GetTurnOrderParams) (api.GetTurnOrderRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetTurnOrder(ctx, params.SessionId)
	if err != nil {
		return &api.GetTurnOrderInternalServerError{}, err
	}

	return result, nil
}

// NextTurn - TYPED response!
func (h *Handlers) NextTurn(ctx context.Context, params api.NextTurnParams) (api.NextTurnRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.NextTurn(ctx, params.SessionId)
	if err != nil {
		return &api.NextTurnInternalServerError{}, err
	}

	return result, nil
}

// SkipTurn - TYPED response!
func (h *Handlers) SkipTurn(ctx context.Context, req *api.SkipTurnRequest, params api.SkipTurnParams) (api.SkipTurnRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.SkipTurn(ctx, params.SessionId, req)
	if err != nil {
		return &api.SkipTurnInternalServerError{}, err
	}

	return result, nil
}
