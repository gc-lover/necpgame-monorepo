// Issue: #1637 - ogen handlers for P2P Trade Service
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/trade-p2p-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

type Handlers struct {
	service *Service
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// InitiateTrade implements initiateTrade operation.
//
// Инициировать торговлю.
//
// POST /economy/trade/initiate
func (h *Handlers) InitiateTrade(ctx context.Context, req *api.InitiateTradeReq) (api.InitiateTradeRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	session, err := h.service.InitiateTrade(ctx, req)
	if err != nil {
		return &api.InitiateTradeBadRequest{
			Error:   "BadRequest",
			Message: err.Error(),
		}, nil
	}

	return session, nil
}

// GetTradeSession implements getTradeSession operation.
//
// Получить торговую сессию.
//
// GET /economy/trade/sessions/{sessionId}
func (h *Handlers) GetTradeSession(ctx context.Context, params api.GetTradeSessionParams) (api.GetTradeSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	session, err := h.service.GetTradeSession(ctx, params.SessionId.String())
	if err != nil {
		if err.Error() == "session not found" {
			return &api.Error{
				Error:   "NotFound",
				Message: "Trade session not found",
			}, nil
		}
		return &api.Error{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	return session, nil
}

// CancelTradeSession implements cancelTradeSession operation.
//
// Отменить торговлю.
//
// DELETE /economy/trade/sessions/{sessionId}
func (h *Handlers) CancelTradeSession(ctx context.Context, req api.OptCancelTradeSessionReq, params api.CancelTradeSessionParams) (*api.SuccessResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	reason := "Cancelled by user"
	if req.IsSet() && req.Value.Reason.IsSet() {
		reason = req.Value.Reason.Value
	}

	err := h.service.CancelTradeSession(ctx, params.SessionId.String(), reason)
	if err != nil {
		return nil, err
	}

	return &api.SuccessResponse{}, nil
}

// AddTradeOffer implements addTradeOffer operation.
//
// Добавить предложение.
//
// POST /economy/trade/sessions/{sessionId}/offer
func (h *Handlers) AddTradeOffer(ctx context.Context, req *api.TradeOfferRequest, params api.AddTradeOfferParams) (api.AddTradeOfferRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	session, err := h.service.AddTradeOffer(ctx, params.SessionId.String(), req)
	if err != nil {
		if err.Error() == "session not found" {
			return &api.AddTradeOfferBadRequest{
				Error:   "NotFound",
				Message: "Trade session not found",
			}, nil
		}
		if err.Error() == "not owner of items" {
			return &api.AddTradeOfferForbidden{
				Error:   "Forbidden",
				Message: "Not owner of items",
			}, nil
		}
		return &api.AddTradeOfferBadRequest{
			Error:   "BadRequest",
			Message: err.Error(),
		}, nil
	}

	return session, nil
}

// UpdateTradeOffer implements updateTradeOffer operation.
//
// Изменить предложение.
//
// PUT /economy/trade/sessions/{sessionId}/offer
func (h *Handlers) UpdateTradeOffer(ctx context.Context, req *api.TradeOfferRequest, params api.UpdateTradeOfferParams) (*api.TradeSessionResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	session, err := h.service.UpdateTradeOffer(ctx, params.SessionId.String(), req)
	if err != nil {
		return nil, err
	}

	return session, nil
}

// RemoveTradeOffer implements removeTradeOffer operation.
//
// Удалить предложение.
//
// DELETE /economy/trade/sessions/{sessionId}/offer
func (h *Handlers) RemoveTradeOffer(ctx context.Context, params api.RemoveTradeOfferParams) (*api.TradeSessionResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	session, err := h.service.RemoveTradeOffer(ctx, params.SessionId.String())
	if err != nil {
		return nil, err
	}

	return session, nil
}

// ConfirmTrade implements confirmTrade operation.
//
// Подтвердить предложение.
//
// POST /economy/trade/sessions/{sessionId}/confirm
func (h *Handlers) ConfirmTrade(ctx context.Context, params api.ConfirmTradeParams) (api.ConfirmTradeRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	session, err := h.service.ConfirmTrade(ctx, params.SessionId.String())
	if err != nil {
		return &api.Error{
			Error:   "BadRequest",
			Message: err.Error(),
		}, nil
	}

	return session, nil
}

// CompleteTrade implements completeTrade operation.
//
// Завершение сделки (требуется двойное подтверждение).
//
// POST /economy/trade/sessions/{sessionId}/complete
func (h *Handlers) CompleteTrade(ctx context.Context, params api.CompleteTradeParams) (api.CompleteTradeRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.CompleteTrade(ctx, params.SessionId.String())
	if err != nil {
		if err.Error() == "not confirmed" {
			return &api.CompleteTradeBadRequest{
				Error:   "BadRequest",
				Message: "Both parties must confirm before completing",
			}, nil
		}
		return &api.CompleteTradeInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	return result, nil
}

// GetTradeHistory implements getTradeHistory operation.
//
// История торговли.
//
// GET /economy/trade/history
func (h *Handlers) GetTradeHistory(ctx context.Context, params api.GetTradeHistoryParams) (*api.GetTradeHistoryOK, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	limit := 20
	if params.Limit.IsSet() && params.Limit.Value > 0 {
		limit = params.Limit.Value
	}
	if limit > 100 {
		limit = 100
	}

	offset := 0
	if params.Offset.IsSet() && params.Offset.Value >= 0 {
		offset = params.Offset.Value
	}

	history, err := h.service.GetTradeHistory(ctx, limit, offset)
	if err != nil {
		return nil, err
	}

	return history, nil
}
