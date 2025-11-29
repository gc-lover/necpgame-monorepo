package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/stock-analytics-charts-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type ChartsHandlers struct {
	logger *logrus.Logger
}

func NewChartsHandlers() *ChartsHandlers {
	return &ChartsHandlers{
		logger: GetLogger(),
	}
}

func (h *ChartsHandlers) GetChart(w http.ResponseWriter, r *http.Request, ticker string, params api.GetChartParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"ticker":   ticker,
		"type":     params.Type,
		"interval": params.Interval,
		"from":     params.From,
		"to":       params.To,
	}).Info("GetChart request")

	chartType := api.ChartType(params.Type)
	intervalStr := string(params.Interval)

	response := api.Chart{
		Ticker:   &ticker,
		Type:     &chartType,
		Interval: &intervalStr,
		Data:     &[]api.OHLC{},
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ChartsHandlers) CompareCharts(w http.ResponseWriter, r *http.Request, params api.CompareChartsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"tickers":  params.Tickers,
		"interval": params.Interval,
		"from":     params.From,
		"to":       params.To,
	}).Info("CompareCharts request")

	type CompareChartsResponse struct {
		Charts []api.Chart `json:"charts"`
	}

	response := CompareChartsResponse{
		Charts: []api.Chart{},
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ChartsHandlers) GetIndicators(w http.ResponseWriter, r *http.Request, ticker string, params api.GetIndicatorsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"ticker":     ticker,
		"indicators": params.Indicators,
		"interval":   params.Interval,
		"from":       params.From,
		"to":         params.To,
	}).Info("GetIndicators request")

	intervalStr := string(params.Interval)
	response := api.Indicators{
		Ticker:   &ticker,
		Interval: &intervalStr,
		Data:     &map[string][]api.IndicatorValue{},
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ChartsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *ChartsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

