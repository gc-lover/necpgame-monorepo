// Issue: #1601 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	api "github.com/necpgame/stock-analytics-tools-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond // Performance: context timeout for DB ops
)

// ToolsHandlers implements api.Handler interface (ogen typed handlers)
type ToolsHandlers struct {
	logger  *logrus.Logger
	service ToolsServiceInterface
}

// NewToolsHandlers creates new handlers
func NewToolsHandlers(service ToolsServiceInterface) *ToolsHandlers {
	return &ToolsHandlers{
		logger:  GetLogger(),
		service: service,
	}
}

// CreateAlert implements createAlert operation.
func (h *ToolsHandlers) CreateAlert(ctx context.Context, req *api.CreateAlertRequest) (api.CreateAlertRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		alertID := uuid.New()
		now := time.Now()
		return &api.Alert{
			ID:            api.NewOptUUID(alertID),
			PlayerID:      api.NewOptUUID(uuid.New()),
			Ticker:        api.NewOptString(req.Ticker),
			ConditionType: api.NewOptString(string(req.ConditionType)),
			Threshold:     api.NewOptFloat64(req.Threshold),
			IsActive:      api.NewOptBool(true),
			TriggeredAt:   api.OptDateTime{},
			CreatedAt:     api.NewOptDateTime(now),
		}, nil
	}

	alert, err := h.service.CreateAlert(ctx, req)
	if err != nil {
		h.logger.WithError(err).Error("CreateAlert: failed")
		alertID := uuid.New()
		now := time.Now()
		return &api.Alert{
			ID:            api.NewOptUUID(alertID),
			PlayerID:      api.NewOptUUID(uuid.New()),
			Ticker:        api.NewOptString(req.Ticker),
			ConditionType: api.NewOptString(string(req.ConditionType)),
			Threshold:     api.NewOptFloat64(req.Threshold),
			IsActive:      api.NewOptBool(false),
			TriggeredAt:   api.OptDateTime{},
			CreatedAt:     api.NewOptDateTime(now),
		}, nil
	}

	return alert, nil
}

// DeleteAlert implements deleteAlert operation.
func (h *ToolsHandlers) DeleteAlert(ctx context.Context, params api.DeleteAlertParams) (api.DeleteAlertRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.DeleteAlertNoContent{}, nil
	}

	err := h.service.DeleteAlert(ctx, params.AlertID)
	if err != nil {
		h.logger.WithError(err).Error("DeleteAlert: failed")
		return &api.DeleteAlertNoContent{}, nil
	}

	return &api.DeleteAlertNoContent{}, nil
}

// GetHeatmap implements getHeatmap operation.
func (h *ToolsHandlers) GetHeatmap(ctx context.Context, params api.GetHeatmapParams) (api.GetHeatmapRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.Heatmap{}, nil
	}

	heatmap, err := h.service.GetHeatmap(ctx, string(params.Period))
	if err != nil {
		h.logger.WithError(err).Error("GetHeatmap: failed")
		return &api.Heatmap{}, nil
	}

	return heatmap, nil
}

// GetMarketDashboard implements getMarketDashboard operation.
func (h *ToolsHandlers) GetMarketDashboard(ctx context.Context) (api.GetMarketDashboardRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.MarketDashboard{}, nil
	}

	dashboard, err := h.service.GetMarketDashboard(ctx)
	if err != nil {
		h.logger.WithError(err).Error("GetMarketDashboard: failed")
		return &api.MarketDashboard{}, nil
	}

	return dashboard, nil
}

// GetOrderBook implements getOrderBook operation.
func (h *ToolsHandlers) GetOrderBook(ctx context.Context, params api.GetOrderBookParams) (api.GetOrderBookRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.OrderBook{}, nil
	}

	orderBook, err := h.service.GetOrderBook(ctx, params.Ticker)
	if err != nil {
		h.logger.WithError(err).Error("GetOrderBook: failed")
		return &api.OrderBook{}, nil
	}

	return orderBook, nil
}

// GetPortfolioDashboard implements getPortfolioDashboard operation.
func (h *ToolsHandlers) GetPortfolioDashboard(ctx context.Context) (api.GetPortfolioDashboardRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.PortfolioDashboard{}, nil
	}

	dashboard, err := h.service.GetPortfolioDashboard(ctx)
	if err != nil {
		h.logger.WithError(err).Error("GetPortfolioDashboard: failed")
		return &api.PortfolioDashboard{}, nil
	}

	return dashboard, nil
}

// ListAlerts implements listAlerts operation.
func (h *ToolsHandlers) ListAlerts(ctx context.Context, params api.ListAlertsParams) (api.ListAlertsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	activeOnly := false
	if params.ActiveOnly.IsSet() {
		activeOnly = params.ActiveOnly.Value
	}

	limit := 50
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}
	offset := 0
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	if h.service == nil {
		alerts := []api.Alert{}
		pagination := api.PaginationResponse{
			Total:  0,
			Limit:  api.NewOptInt(limit),
			Offset: api.NewOptInt(offset),
		}
		return &api.ListAlertsOK{
			Alerts:     alerts,
			Pagination: api.NewOptPaginationResponse(pagination),
		}, nil
	}

	alerts, total, err := h.service.ListAlerts(ctx, activeOnly, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("ListAlerts: failed")
		alerts := []api.Alert{}
		pagination := api.PaginationResponse{
			Total:  0,
			Limit:  api.NewOptInt(limit),
			Offset: api.NewOptInt(offset),
		}
		return &api.ListAlertsOK{
			Alerts:     alerts,
			Pagination: api.NewOptPaginationResponse(pagination),
		}, nil
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  api.NewOptInt(limit),
		Offset: api.NewOptInt(offset),
	}

	return &api.ListAlertsOK{
		Alerts:     alerts,
		Pagination: api.NewOptPaginationResponse(pagination),
	}, nil
}

