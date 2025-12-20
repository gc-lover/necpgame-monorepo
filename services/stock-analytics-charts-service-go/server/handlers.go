// Package server Issue: #1601 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/necpgame/stock-analytics-charts-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond // Performance: context timeout for DB ops
)

// ChartsHandlers implements api.Handler interface (ogen typed handlers)
type ChartsHandlers struct {
	logger  *logrus.Logger
	service ChartsServiceInterface
}

// NewChartsHandlers creates new handlers
func NewChartsHandlers(service ChartsServiceInterface) *ChartsHandlers {
	return &ChartsHandlers{
		logger:  GetLogger(),
		service: service,
	}
}

// CompareCharts implements compareCharts operation.
func (h *ChartsHandlers) CompareCharts(ctx context.Context, params api.CompareChartsParams) (api.CompareChartsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.CompareChartsOK{
			Charts: []api.Chart{},
		}, nil
	}

	result, err := h.service.CompareCharts(ctx, params.Tickers, string(params.Interval), params.From, params.To)
	if err != nil {
		h.logger.WithError(err).Error("CompareCharts: failed")
		return &api.CompareChartsOK{
			Charts: []api.Chart{},
		}, nil
	}

	return result, nil
}

// GetChart implements getChart operation.
func (h *ChartsHandlers) GetChart(ctx context.Context, params api.GetChartParams) (api.GetChartRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.Chart{}, nil
	}

	chart, err := h.service.GetChart(ctx, params.Ticker, string(params.Type), string(params.Interval), params.From, params.To)
	if err != nil {
		h.logger.WithError(err).Error("GetChart: failed")
		return &api.Chart{}, nil
	}

	return chart, nil
}

// GetIndicators implements getIndicators operation.
func (h *ChartsHandlers) GetIndicators(ctx context.Context, params api.GetIndicatorsParams) (api.GetIndicatorsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.Indicators{}, nil
	}

	indicators, err := h.service.GetIndicators(ctx, params.Ticker)
	if err != nil {
		h.logger.WithError(err).Error("GetIndicators: failed")
		return &api.Indicators{}, nil
	}

	return indicators, nil
}
