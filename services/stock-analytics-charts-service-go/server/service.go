package server

import (
	"context"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-analytics-charts-service-go/pkg/api"
)

type ChartsService struct {
	repository Repository
	logger     *logrus.Logger
}

func NewChartsService(repository Repository, logger *logrus.Logger) *ChartsService {
	return &ChartsService{
		repository: repository,
		logger:     logger,
	}
}

func (s *ChartsService) GetChart(ctx context.Context, ticker string, chartType api.ChartType, interval string, from, to time.Time) (*api.Chart, error) {
	s.logger.WithFields(map[string]interface{}{
		"ticker":    ticker,
		"type":      chartType,
		"interval":  interval,
		"from":      from,
		"to":        to,
	}).Info("Getting chart data")

	ohlcData, err := s.repository.GetOHLCData(ctx, ticker, interval, from, to)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get OHLC data")
		return nil, fmt.Errorf("failed to get OHLC data: %w", err)
	}

	chart := &api.Chart{
		Ticker:   &ticker,
		Type:     &chartType,
		Interval: &interval,
		Data:     &ohlcData,
	}

	return chart, nil
}

func (s *ChartsService) GetIndicators(ctx context.Context, ticker string, indicators []api.GetIndicatorsParamsIndicators, interval string, from, to time.Time) (*api.Indicators, error) {
	s.logger.WithFields(map[string]interface{}{
		"ticker":     ticker,
		"indicators": indicators,
		"interval":   interval,
		"from":       from,
		"to":         to,
	}).Info("Getting indicators data")

	indicatorData := make(map[string][]api.IndicatorValue)

	for _, indicator := range indicators {
		indicatorStr := string(indicator)
		data, err := s.repository.GetIndicatorData(ctx, ticker, indicatorStr, interval, from, to)
		if err != nil {
			s.logger.WithError(err).WithField("indicator", indicatorStr).Error("Failed to get indicator data")
			continue
		}
		indicatorData[indicatorStr] = data
	}

	result := &api.Indicators{
		Ticker:   &ticker,
		Interval: &interval,
		Data:     &indicatorData,
	}

	return result, nil
}

func (s *ChartsService) CompareCharts(ctx context.Context, tickers []string, interval string, from, to time.Time) ([]api.Chart, error) {
	if len(tickers) > 4 {
		return nil, fmt.Errorf("maximum 4 tickers allowed, got %d", len(tickers))
	}

	s.logger.WithFields(map[string]interface{}{
		"tickers":  tickers,
		"interval": interval,
		"from":     from,
		"to":       to,
	}).Info("Comparing charts")

	ohlcMap, err := s.repository.GetMultipleTickersOHLC(ctx, tickers, interval, from, to)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get multiple tickers OHLC data")
		return nil, fmt.Errorf("failed to get OHLC data: %w", err)
	}

	var charts []api.Chart
	chartType := api.ChartTypeLine

	for _, ticker := range tickers {
		data, exists := ohlcMap[ticker]
		if !exists {
			data = []api.OHLC{}
		}

		chart := api.Chart{
			Ticker:   &ticker,
			Type:     &chartType,
			Interval: &interval,
			Data:     &data,
		}
		charts = append(charts, chart)
	}

	return charts, nil
}

