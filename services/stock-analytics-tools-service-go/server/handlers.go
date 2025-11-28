package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-analytics-tools-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Handlers struct {
	service *ToolsService
	logger  *logrus.Logger
}

func NewHandlers(service *ToolsService, logger *logrus.Logger) *Handlers {
	return &Handlers{
		service: service,
		logger:  logger,
	}
}

func (h *Handlers) GetHeatmap(w http.ResponseWriter, r *http.Request, params api.GetHeatmapParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordToolRequestDuration(r.Method, "/api/v1/analytics/heatmap", duration)
	}()

	heatmap, err := h.service.GetHeatmap(r.Context(), string(params.Period))
	if err != nil {
		h.logger.WithError(err).Error("Failed to get heatmap")
		RecordError("get_heatmap_error")
		RecordToolRequest(r.Method, "/api/v1/analytics/heatmap", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get heatmap", err.Error())
		return
	}

	RecordToolRequest(r.Method, "/api/v1/analytics/heatmap", "200")
	writeJSONResponse(w, http.StatusOK, heatmap)
}

func (h *Handlers) GetOrderBook(w http.ResponseWriter, r *http.Request, ticker string, params api.GetOrderBookParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordToolRequestDuration(r.Method, "/api/v1/analytics/orderbook/{ticker}", duration)
	}()

	depth := 10
	if params.Depth != nil {
		depth = *params.Depth
	}

	orderBook, err := h.service.GetOrderBook(r.Context(), ticker, depth)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get order book")
		RecordError("get_orderbook_error")
		RecordToolRequest(r.Method, "/api/v1/analytics/orderbook/{ticker}", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get order book", err.Error())
		return
	}

	RecordToolRequest(r.Method, "/api/v1/analytics/orderbook/{ticker}", "200")
	writeJSONResponse(w, http.StatusOK, orderBook)
}

func (h *Handlers) CreateAlert(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordToolRequestDuration(r.Method, "/api/v1/analytics/alerts", duration)
	}()

	var req api.CreateAlertRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordAlertRequest(r.Method, "/api/v1/analytics/alerts", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	playerID := uuid.New()
	alert, err := h.service.CreateAlert(r.Context(), playerID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create alert")
		RecordError("create_alert_error")
		RecordAlertRequest(r.Method, "/api/v1/analytics/alerts", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to create alert", err.Error())
		return
	}

	RecordAlertRequest(r.Method, "/api/v1/analytics/alerts", "201")
	writeJSONResponse(w, http.StatusCreated, alert)
}

func (h *Handlers) ListAlerts(w http.ResponseWriter, r *http.Request, params api.ListAlertsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordToolRequestDuration(r.Method, "/api/v1/analytics/alerts", duration)
	}()

	activeOnly := true
	if params.ActiveOnly != nil {
		activeOnly = *params.ActiveOnly
	}

	limit := 10
	if params.Limit != nil {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	playerID := uuid.New()
	alerts, total, err := h.service.ListAlerts(r.Context(), playerID, activeOnly, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list alerts")
		RecordError("list_alerts_error")
		RecordAlertRequest(r.Method, "/api/v1/analytics/alerts", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to list alerts", err.Error())
		return
	}

	response := map[string]interface{}{
		"alerts": alerts,
		"pagination": map[string]interface{}{
			"total":  total,
			"limit":  limit,
			"offset": offset,
		},
	}

	RecordAlertRequest(r.Method, "/api/v1/analytics/alerts", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) DeleteAlert(w http.ResponseWriter, r *http.Request, alertId openapi_types.UUID) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordToolRequestDuration(r.Method, "/api/v1/analytics/alerts/{alert_id}", duration)
	}()

	alertUUID := uuid.UUID(alertId)
	if err := h.service.DeleteAlert(r.Context(), alertUUID); err != nil {
		h.logger.WithError(err).Error("Failed to delete alert")
		RecordError("delete_alert_error")
		RecordAlertRequest(r.Method, "/api/v1/analytics/alerts/{alert_id}", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to delete alert", err.Error())
		return
	}

	RecordAlertRequest(r.Method, "/api/v1/analytics/alerts/{alert_id}", "204")
	w.WriteHeader(http.StatusNoContent)
}

func (h *Handlers) GetPortfolioDashboard(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordToolRequestDuration(r.Method, "/api/v1/analytics/dashboards/portfolio", duration)
	}()

	playerID := uuid.New()
	dashboard, err := h.service.GetPortfolioDashboard(r.Context(), playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get portfolio dashboard")
		RecordError("get_portfolio_dashboard_error")
		RecordToolRequest(r.Method, "/api/v1/analytics/dashboards/portfolio", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get portfolio dashboard", err.Error())
		return
	}

	RecordToolRequest(r.Method, "/api/v1/analytics/dashboards/portfolio", "200")
	writeJSONResponse(w, http.StatusOK, dashboard)
}

func (h *Handlers) GetMarketDashboard(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordToolRequestDuration(r.Method, "/api/v1/analytics/dashboards/market", duration)
	}()

	dashboard, err := h.service.GetMarketDashboard(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get market dashboard")
		RecordError("get_market_dashboard_error")
		RecordToolRequest(r.Method, "/api/v1/analytics/dashboards/market", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get market dashboard", err.Error())
		return
	}

	RecordToolRequest(r.Method, "/api/v1/analytics/dashboards/market", "200")
	writeJSONResponse(w, http.StatusOK, dashboard)
}

func writeJSONResponse(w http.ResponseWriter, statusCode int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		http.Error(w, "Failed to encode response", http.StatusInternalServerError)
	}
}

func writeErrorResponse(w http.ResponseWriter, statusCode int, message, details string) {
	errorResponse := api.Error{
		Error:   http.StatusText(statusCode),
		Message: message,
		Details: &map[string]interface{}{
			"details": details,
		},
	}
	writeJSONResponse(w, statusCode, errorResponse)
}

