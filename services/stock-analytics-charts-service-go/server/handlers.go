package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-analytics-charts-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type Handlers struct {
	service *ChartsService
	logger  *logrus.Logger
}

func NewHandlers(service *ChartsService, logger *logrus.Logger) *Handlers {
	return &Handlers{
		service: service,
		logger:  logger,
	}
}

func (h *Handlers) GetChart(w http.ResponseWriter, r *http.Request, ticker string, params api.GetChartParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordChartRequestDuration(r.Method, "/api/v1/analytics/charts/{ticker}", duration)
	}()

	chartType := api.ChartType(params.Type)
	chart, err := h.service.GetChart(r.Context(), ticker, chartType, string(params.Interval), params.From, params.To)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get chart")
		RecordError("get_chart_error")
		RecordChartRequest(r.Method, "/api/v1/analytics/charts/{ticker}", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get chart", err.Error())
		return
	}

	RecordChartRequest(r.Method, "/api/v1/analytics/charts/{ticker}", "200")
	writeJSONResponse(w, http.StatusOK, chart)
}

func (h *Handlers) GetIndicators(w http.ResponseWriter, r *http.Request, ticker string, params api.GetIndicatorsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordChartRequestDuration(r.Method, "/api/v1/analytics/indicators/{ticker}", duration)
	}()

	indicators, err := h.service.GetIndicators(r.Context(), ticker, params.Indicators, string(params.Interval), params.From, params.To)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get indicators")
		RecordError("get_indicators_error")
		RecordIndicatorRequest(r.Method, "/api/v1/analytics/indicators/{ticker}", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get indicators", err.Error())
		return
	}

	RecordIndicatorRequest(r.Method, "/api/v1/analytics/indicators/{ticker}", "200")
	writeJSONResponse(w, http.StatusOK, indicators)
}

func (h *Handlers) CompareCharts(w http.ResponseWriter, r *http.Request, params api.CompareChartsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordChartRequestDuration(r.Method, "/api/v1/analytics/compare", duration)
	}()

	charts, err := h.service.CompareCharts(r.Context(), params.Tickers, string(params.Interval), params.From, params.To)
	if err != nil {
		h.logger.WithError(err).Error("Failed to compare charts")
		RecordError("compare_charts_error")
		RecordChartRequest(r.Method, "/api/v1/analytics/compare", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Failed to compare charts", err.Error())
		return
	}

	response := map[string]interface{}{
		"charts": charts,
	}

	RecordChartRequest(r.Method, "/api/v1/analytics/compare", "200")
	writeJSONResponse(w, http.StatusOK, response)
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

