package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/stock-analytics-tools-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type ToolsHandlers struct {
	logger *logrus.Logger
}

func NewToolsHandlers() *ToolsHandlers {
	return &ToolsHandlers{
		logger: GetLogger(),
	}
}

func (h *ToolsHandlers) GetHeatmap(w http.ResponseWriter, r *http.Request, params api.GetHeatmapParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"period": params.Period,
	}).Info("GetHeatmap request")

	period := string(params.Period)
	sectors := []struct {
		ChangePercent *float32 `json:"change_percent,omitempty"`
		Sector        *string  `json:"sector,omitempty"`
		TickersCount  *int     `json:"tickers_count,omitempty"`
		Volume        *int     `json:"volume,omitempty"`
	}{}
	response := api.Heatmap{
		Period:  &period,
		Sectors: &sectors,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ToolsHandlers) GetOrderBook(w http.ResponseWriter, r *http.Request, ticker string, params api.GetOrderBookParams) {
	ctx := r.Context()
	_ = ctx

	depth := 10
	if params.Depth != nil {
		depth = *params.Depth
	}

	h.logger.WithFields(logrus.Fields{
		"ticker": ticker,
		"depth":  depth,
	}).Info("GetOrderBook request")

	bids := []api.Order{}
	asks := []api.Order{}
	spread := float32(0.0)
	response := api.OrderBook{
		Ticker: &ticker,
		Bids:   &bids,
		Asks:   &asks,
		Spread: &spread,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ToolsHandlers) CreateAlert(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.CreateAlertJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode CreateAlert request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"ticker":         req.Ticker,
		"condition_type": req.ConditionType,
		"threshold":      req.Threshold,
	}).Info("CreateAlert request")

	alertId := openapi_types.UUID{}
	isActive := true
	conditionTypeStr := string(req.ConditionType)
	threshold := req.Threshold
	response := api.Alert{
		Id:            &alertId,
		Ticker:        &req.Ticker,
		ConditionType: &conditionTypeStr,
		Threshold:     &threshold,
		IsActive:      &isActive,
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *ToolsHandlers) ListAlerts(w http.ResponseWriter, r *http.Request, params api.ListAlertsParams) {
	ctx := r.Context()
	_ = ctx

	activeOnly := true
	if params.ActiveOnly != nil {
		activeOnly = *params.ActiveOnly
	}

	h.logger.WithFields(logrus.Fields{
		"active_only": activeOnly,
		"limit":       params.Limit,
		"offset":      params.Offset,
	}).Info("ListAlerts request")

	alerts := []api.Alert{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type ListAlertsResponse struct {
		Alerts     []api.Alert              `json:"alerts"`
		Pagination *api.PaginationResponse `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := ListAlertsResponse{
		Alerts:     alerts,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ToolsHandlers) DeleteAlert(w http.ResponseWriter, r *http.Request, alertId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("alert_id", alertId).Info("DeleteAlert request")

	w.WriteHeader(http.StatusNoContent)
}

func (h *ToolsHandlers) GetPortfolioDashboard(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	h.logger.Info("GetPortfolioDashboard request")

	totalValue := float32(0.0)
	pnl := float32(0.0)
	pnlPercent := float32(0.0)
	assetDistribution := []map[string]interface{}{}
	topPositions := []map[string]interface{}{}
	riskMetrics := map[string]interface{}{}

	response := api.PortfolioDashboard{
		TotalValue:        &totalValue,
		Pnl:               &pnl,
		PnlPercent:        &pnlPercent,
		AssetDistribution: &assetDistribution,
		TopPositions:      &topPositions,
		RiskMetrics:       &riskMetrics,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ToolsHandlers) GetMarketDashboard(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	h.logger.Info("GetMarketDashboard request")

	topGainers := []map[string]interface{}{}
	topLosers := []map[string]interface{}{}
	marketEvents := []map[string]interface{}{}
	totalVolume := 0

	response := api.MarketDashboard{
		TopGainers:   &topGainers,
		TopLosers:    &topLosers,
		MarketEvents: &marketEvents,
		TotalVolume:  &totalVolume,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ToolsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *ToolsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	h.respondJSON(w, status, errorResponse)
}

