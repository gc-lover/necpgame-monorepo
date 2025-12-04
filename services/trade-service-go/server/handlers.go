// Issue: #131, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/trade-service-go/pkg/api"
)

const DBTimeout = 50 * time.Millisecond

var (
	ErrNotFound = errors.New("not found")
)

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	service Service

	// Memory pooling for hot path structs (zero allocations target!)
	successResponsePool      sync.Pool
	tradeSessionResponsePool sync.Pool
	tradeHistoryResponsePool sync.Pool
}

// NewHandlers creates new handlers
func NewHandlers(service Service) *Handlers {
	h := &Handlers{service: service}

	// Initialize memory pools (zero allocations target!)
	h.successResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.SuccessResponse{}
		},
	}
	h.tradeSessionResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.TradeSessionResponse{}
		},
	}
	h.tradeHistoryResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.TradeHistoryResponse{}
		},
	}

	return h
}

// CreateTradeSession - TYPED response!
func (h *Handlers) CreateTradeSession(ctx context.Context, req *api.CreateTradeRequest) (api.CreateTradeSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.service.CreateTradeSession(ctx, req)
	if err != nil {
		return &api.CreateTradeSessionBadRequest{
			Error:   "BAD_REQUEST",
			Message: err.Error(),
		}, nil
	}

	return response, nil
}

// GetTradeSession - TYPED response!
func (h *Handlers) GetTradeSession(ctx context.Context, params api.GetTradeSessionParams) (api.GetTradeSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	sessionID := params.SessionID.String()
	response, err := h.service.GetTradeSession(ctx, sessionID)
	if err != nil {
		if err == ErrNotFound {
			return &api.Error{
				Error:   "NOT_FOUND",
				Message: "Trade session not found",
			}, nil
		}
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	return response, nil
}

// CancelTradeSession - TYPED response!
func (h *Handlers) CancelTradeSession(ctx context.Context, params api.CancelTradeSessionParams) (api.CancelTradeSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	sessionID := params.SessionID.String()
	err := h.service.CancelTradeSession(ctx, sessionID)
	if err != nil {
		return &api.CancelTradeSessionForbidden{
			Error:   "FORBIDDEN",
			Message: err.Error(),
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	result := h.successResponsePool.Get().(*api.SuccessResponse)
	// Note: Not returning to pool - struct is returned to caller

	result.Status = api.NewOptString("cancelled")
	return result, nil
}

// AddTradeItems - TYPED response!
func (h *Handlers) AddTradeItems(ctx context.Context, req *api.AddItemsRequest, params api.AddTradeItemsParams) (api.AddTradeItemsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	sessionID := params.SessionID.String()
	response, err := h.service.AddTradeItems(ctx, sessionID, req)
	if err != nil {
		return &api.AddTradeItemsBadRequest{
			Error:   "BAD_REQUEST",
			Message: err.Error(),
		}, nil
	}

	return response, nil
}

// AddTradeCurrency - TYPED response!
func (h *Handlers) AddTradeCurrency(ctx context.Context, req *api.AddCurrencyRequest, params api.AddTradeCurrencyParams) (api.AddTradeCurrencyRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	sessionID := params.SessionID.String()
	response, err := h.service.AddTradeCurrency(ctx, sessionID, req)
	if err != nil {
		return &api.Error{
			Error:   "BAD_REQUEST",
			Message: err.Error(),
		}, nil
	}

	return response, nil
}

// SetTradeReady - TYPED response!
func (h *Handlers) SetTradeReady(ctx context.Context, req *api.ReadyRequest, params api.SetTradeReadyParams) (*api.TradeSessionResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	sessionID := params.SessionID.String()
	response, err := h.service.SetTradeReady(ctx, sessionID, req)
	if err != nil {
		return nil, err
	}

	return response, nil
}

// CompleteTrade - TYPED response!
func (h *Handlers) CompleteTrade(ctx context.Context, params api.CompleteTradeParams) (api.CompleteTradeRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	sessionID := params.SessionID.String()
	response, err := h.service.CompleteTrade(ctx, sessionID)
	if err != nil {
		return &api.CompleteTradeBadRequest{
			Error:   "BAD_REQUEST",
			Message: err.Error(),
		}, nil
	}

	return response, nil
}

// GetTradeHistory - TYPED response!
func (h *Handlers) GetTradeHistory(ctx context.Context, params api.GetTradeHistoryParams) (*api.TradeHistoryResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID := params.PlayerID.String()
	response, err := h.service.GetTradeHistory(ctx, playerID, params)
	if err != nil {
		return nil, err
	}

	return response, nil
}
