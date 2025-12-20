// Package server Issue: #1600 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/stock-margin-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond // Performance: context timeout for DB ops
)

// Handlers implements api.Handler interface (ogen typed handlers)
type Handlers struct {
	logger  *logrus.Logger
	service MarginServiceInterface
}

// NewHandlers creates new handlers
func NewHandlers(service MarginServiceInterface) *Handlers {
	return &Handlers{
		logger:  GetLogger(),
		service: service,
	}
}

// GetMarginAccount - TYPED response!
func (h *Handlers) GetMarginAccount(ctx context.Context) (api.GetMarginAccountRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get accountID from context (from SecurityHandler)
	accountID := uuid.New()

	if h.service == nil {
		return &api.MarginAccount{
			AccountID: api.NewOptUUID(accountID),
			Balance:   api.NewOptFloat64(0.0),
			Equity:    api.NewOptFloat64(0.0),
			Leverage:  api.NewOptFloat64(1.0),
		}, nil
	}

	account, err := h.service.GetMarginAccount(ctx, accountID)
	if err != nil {
		h.logger.WithError(err).Error("GetMarginAccount: failed")
		// Return default account on error
		return &api.MarginAccount{
			AccountID: api.NewOptUUID(accountID),
			Balance:   api.NewOptFloat64(0.0),
			Equity:    api.NewOptFloat64(0.0),
			Leverage:  api.NewOptFloat64(1.0),
		}, nil
	}

	return account, nil
}

// BorrowMargin - TYPED response!
func (h *Handlers) BorrowMargin(ctx context.Context, req *api.BorrowMarginRequest) (api.BorrowMarginRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get accountID from context (from SecurityHandler)
	accountID := uuid.New()

	if h.service == nil {
		return &api.BorrowMarginResponse{
			BorrowedAmount:     api.NewOptFloat64(0.0),
			CollateralRequired: api.NewOptFloat64(0.0),
			InterestRate:       api.NewOptFloat64(0.0),
			Leverage:           api.NewOptFloat64(0.0),
		}, nil
	}

	response, err := h.service.BorrowMargin(ctx, accountID, req.Amount)
	if err != nil {
		h.logger.WithError(err).Error("BorrowMargin: failed")
		return &api.BorrowMarginBadRequest{
			Error:   "BadRequest",
			Message: "Failed to borrow margin",
		}, nil
	}

	return response, nil
}

// RepayMargin - TYPED response!
func (h *Handlers) RepayMargin(ctx context.Context, req *api.RepayMarginRequest) (api.RepayMarginRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get accountID from context (from SecurityHandler)
	accountID := uuid.New()

	if h.service == nil {
		return &api.RepayMarginOK{}, nil
	}

	err := h.service.RepayMargin(ctx, accountID, req.Amount)
	if err != nil {
		h.logger.WithError(err).Error("RepayMargin: failed")
		return &api.RepayMarginBadRequest{
			Error:   "BadRequest",
			Message: "Failed to repay margin",
		}, nil
	}

	return &api.RepayMarginOK{}, nil
}

// OpenMarginAccount - TYPED response!
func (h *Handlers) OpenMarginAccount(ctx context.Context, req *api.OpenMarginAccountRequest) (api.OpenMarginAccountRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.MarginAccount{
			AccountID: api.NewOptUUID(uuid.New()),
			Balance:   api.NewOptFloat64(req.InitialDeposit),
			Equity:    api.NewOptFloat64(req.InitialDeposit),
			Leverage:  api.NewOptFloat64(1.0),
		}, nil
	}

	account, err := h.service.OpenMarginAccount(ctx, req.InitialDeposit)
	if err != nil {
		h.logger.WithError(err).Error("OpenMarginAccount: failed")
		return &api.OpenMarginAccountBadRequest{
			Error:   "BadRequest",
			Message: "Failed to open margin account",
		}, nil
	}

	return account, nil
}

// GetMarginCallHistory - TYPED response!
func (h *Handlers) GetMarginCallHistory(ctx context.Context, params api.GetMarginCallHistoryParams) (api.GetMarginCallHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get accountID from context (from SecurityHandler)
	accountID := uuid.New()

	limit := 50
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}
	offset := 0
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	if h.service == nil {
		return &api.GetMarginCallHistoryOK{
			MarginCalls: []api.MarginCall{},
			Pagination:  api.OptPaginationResponse{},
		}, nil
	}

	calls, pagination, err := h.service.GetMarginCallHistory(ctx, accountID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("GetMarginCallHistory: failed")
		return &api.GetMarginCallHistoryOK{
			MarginCalls: []api.MarginCall{},
			Pagination:  api.OptPaginationResponse{},
		}, nil
	}

	return &api.GetMarginCallHistoryOK{
		MarginCalls: calls,
		Pagination:  api.NewOptPaginationResponse(*pagination),
	}, nil
}

// GetRiskHealth - TYPED response!
func (h *Handlers) GetRiskHealth(ctx context.Context) (api.GetRiskHealthRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get accountID from context (from SecurityHandler)
	accountID := uuid.New()

	if h.service == nil {
		return &api.RiskHealth{
			MarginHealth:      api.NewOptFloat64(1.0),
			MaintenanceMargin: api.NewOptFloat64(0.0),
			LiquidationPrice:  api.NewOptFloat64(0.0),
		}, nil
	}

	health, err := h.service.GetRiskHealth(ctx, accountID)
	if err != nil {
		h.logger.WithError(err).Error("GetRiskHealth: failed")
		// Return default health on error
		return &api.RiskHealth{
			MarginHealth:      api.NewOptFloat64(1.0),
			MaintenanceMargin: api.NewOptFloat64(0.0),
			LiquidationPrice:  api.NewOptFloat64(0.0),
		}, nil
	}

	return health, nil
}

// OpenShortPosition - TYPED response!
func (h *Handlers) OpenShortPosition(ctx context.Context, req *api.ShortPositionRequest) (api.OpenShortPositionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get accountID from context (from SecurityHandler)
	accountID := uuid.New()

	if h.service == nil {
		return &api.ShortPosition{
			PositionID: api.NewOptUUID(uuid.New()),
			Ticker:     api.NewOptString(req.Ticker),
			Quantity:   api.NewOptInt(req.Quantity),
			EntryPrice: api.NewOptFloat64(0.0),
		}, nil
	}

	position, err := h.service.OpenShortPosition(ctx, accountID, req.Ticker, req.Quantity)
	if err != nil {
		h.logger.WithError(err).Error("OpenShortPosition: failed")
		return &api.OpenShortPositionBadRequest{
			Error:   "BadRequest",
			Message: "Failed to open short position",
		}, nil
	}

	return position, nil
}

// ListShortPositions - TYPED response!
func (h *Handlers) ListShortPositions(ctx context.Context, params api.ListShortPositionsParams) (api.ListShortPositionsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get accountID from context (from SecurityHandler)
	accountID := uuid.New()

	limit := 50
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}
	offset := 0
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	if h.service == nil {
		return &api.ListShortPositionsOK{
			Positions:  []api.ShortPosition{},
			Pagination: api.OptPaginationResponse{},
		}, nil
	}

	positions, pagination, err := h.service.ListShortPositions(ctx, accountID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("ListShortPositions: failed")
		return &api.ListShortPositionsOK{
			Positions:  []api.ShortPosition{},
			Pagination: api.OptPaginationResponse{},
		}, nil
	}

	return &api.ListShortPositionsOK{
		Positions:  positions,
		Pagination: api.NewOptPaginationResponse(*pagination),
	}, nil
}

// GetShortPosition - TYPED response!
func (h *Handlers) GetShortPosition(ctx context.Context, params api.GetShortPositionParams) (api.GetShortPositionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.ShortPosition{
			PositionID: api.NewOptUUID(params.PositionID),
		}, nil
	}

	position, err := h.service.GetShortPosition(ctx, params.PositionID)
	if err != nil {
		h.logger.WithError(err).Error("GetShortPosition: failed")
		return &api.GetShortPositionNotFound{
			Error:   "NotFound",
			Message: "Short position not found",
		}, nil
	}

	return position, nil
}

// CloseShortPosition - TYPED response!
func (h *Handlers) CloseShortPosition(ctx context.Context, params api.CloseShortPositionParams) (api.CloseShortPositionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.ClosePositionResponse{
			PositionID:  api.NewOptUUID(params.PositionID),
			RealizedPnl: api.NewOptFloat64(0.0),
			ClosedAt:    api.OptDateTime{},
		}, nil
	}

	response, err := h.service.CloseShortPosition(ctx, params.PositionID)
	if err != nil {
		h.logger.WithError(err).Error("CloseShortPosition: failed")
		return &api.CloseShortPositionNotFound{
			Error:   "NotFound",
			Message: "Short position not found",
		}, nil
	}

	return response, nil
}
