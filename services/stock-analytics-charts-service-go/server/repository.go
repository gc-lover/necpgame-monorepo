package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-analytics-charts-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type Repository interface {
	GetOHLCData(ctx context.Context, ticker string, interval string, from, to time.Time) ([]api.OHLC, error)
	GetIndicatorData(ctx context.Context, ticker string, indicator string, interval string, from, to time.Time) ([]api.IndicatorValue, error)
	GetMultipleTickersOHLC(ctx context.Context, tickers []string, interval string, from, to time.Time) (map[string][]api.OHLC, error)
}

type InMemoryRepository struct {
	logger *logrus.Logger
	ohlcData map[string][]api.OHLC
	indicatorData map[string][]api.IndicatorValue
}

func NewInMemoryRepository(logger *logrus.Logger) *InMemoryRepository {
	return &InMemoryRepository{
		logger: logger,
		ohlcData: make(map[string][]api.OHLC),
		indicatorData: make(map[string][]api.IndicatorValue),
	}
}

func (r *InMemoryRepository) GetOHLCData(ctx context.Context, ticker string, interval string, from, to time.Time) ([]api.OHLC, error) {
	key := ticker + ":" + interval
	data, exists := r.ohlcData[key]
	if !exists {
		return []api.OHLC{}, nil
	}

	var result []api.OHLC
	for _, item := range data {
		if item.Timestamp != nil && 
			item.Timestamp.After(from) && 
			item.Timestamp.Before(to) {
			result = append(result, item)
		}
	}

	return result, nil
}

func (r *InMemoryRepository) GetIndicatorData(ctx context.Context, ticker string, indicator string, interval string, from, to time.Time) ([]api.IndicatorValue, error) {
	key := ticker + ":" + indicator + ":" + interval
	data, exists := r.indicatorData[key]
	if !exists {
		return []api.IndicatorValue{}, nil
	}

	var result []api.IndicatorValue
	for _, item := range data {
		if item.Timestamp != nil && 
			item.Timestamp.After(from) && 
			item.Timestamp.Before(to) {
			result = append(result, item)
		}
	}

	return result, nil
}

func (r *InMemoryRepository) GetMultipleTickersOHLC(ctx context.Context, tickers []string, interval string, from, to time.Time) (map[string][]api.OHLC, error) {
	result := make(map[string][]api.OHLC)
	
	for _, ticker := range tickers {
		data, err := r.GetOHLCData(ctx, ticker, interval, from, to)
		if err != nil {
			return nil, err
		}
		result[ticker] = data
	}

	return result, nil
}

