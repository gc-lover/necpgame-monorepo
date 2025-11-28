package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/stock-analytics-tools-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

type Repository interface {
	GetHeatmapData(ctx context.Context, period string) (*api.Heatmap, error)
	GetOrderBookData(ctx context.Context, ticker string, depth int) (*api.OrderBook, error)
	CreateAlert(ctx context.Context, alert *api.Alert) error
	GetAlerts(ctx context.Context, playerID uuid.UUID, activeOnly bool, limit, offset int) ([]api.Alert, int, error)
	DeleteAlert(ctx context.Context, alertID uuid.UUID) error
	GetPortfolioDashboard(ctx context.Context, playerID uuid.UUID) (*api.PortfolioDashboard, error)
	GetMarketDashboard(ctx context.Context) (*api.MarketDashboard, error)
}

type InMemoryRepository struct {
	logger      *logrus.Logger
	heatmapData map[string]*api.Heatmap
	orderBookData map[string]*api.OrderBook
	alerts      map[uuid.UUID]*api.Alert
	portfolios  map[uuid.UUID]*api.PortfolioDashboard
	marketDashboard *api.MarketDashboard
}

func NewInMemoryRepository(logger *logrus.Logger) *InMemoryRepository {
	return &InMemoryRepository{
		logger:        logger,
		heatmapData:   make(map[string]*api.Heatmap),
		orderBookData: make(map[string]*api.OrderBook),
		alerts:        make(map[uuid.UUID]*api.Alert),
		portfolios:    make(map[uuid.UUID]*api.PortfolioDashboard),
	}
}

func (r *InMemoryRepository) GetHeatmapData(ctx context.Context, period string) (*api.Heatmap, error) {
	data, exists := r.heatmapData[period]
	if !exists {
		emptySectors := []struct {
			ChangePercent *float32 `json:"change_percent,omitempty"`
			Sector        *string  `json:"sector,omitempty"`
			TickersCount  *int     `json:"tickers_count,omitempty"`
			Volume        *int     `json:"volume,omitempty"`
		}{}
		return &api.Heatmap{
			Period:  &period,
			Sectors: &emptySectors,
		}, nil
	}
	return data, nil
}

func (r *InMemoryRepository) GetOrderBookData(ctx context.Context, ticker string, depth int) (*api.OrderBook, error) {
	data, exists := r.orderBookData[ticker]
	if !exists {
		return &api.OrderBook{
			Ticker: &ticker,
			Bids:   &[]api.Order{},
			Asks:   &[]api.Order{},
			Spread: func() *float32 { v := float32(0); return &v }(),
		}, nil
	}
	return data, nil
}

func (r *InMemoryRepository) CreateAlert(ctx context.Context, alert *api.Alert) error {
	if alert.Id == nil {
		id := uuid.New()
		alert.Id = &id
	}
	r.alerts[*alert.Id] = alert
	return nil
}

func (r *InMemoryRepository) GetAlerts(ctx context.Context, playerID uuid.UUID, activeOnly bool, limit, offset int) ([]api.Alert, int, error) {
	var result []api.Alert
	count := 0

	for _, alert := range r.alerts {
		if alert.PlayerId != nil && *alert.PlayerId == playerID {
			if activeOnly && alert.IsActive != nil && !*alert.IsActive {
				continue
			}
			if count >= offset && len(result) < limit {
				result = append(result, *alert)
			}
			count++
		}
	}

	return result, count, nil
}

func (r *InMemoryRepository) DeleteAlert(ctx context.Context, alertID uuid.UUID) error {
	delete(r.alerts, alertID)
	return nil
}

func (r *InMemoryRepository) GetPortfolioDashboard(ctx context.Context, playerID uuid.UUID) (*api.PortfolioDashboard, error) {
	data, exists := r.portfolios[playerID]
	if !exists {
		return &api.PortfolioDashboard{}, nil
	}
	return data, nil
}

func (r *InMemoryRepository) GetMarketDashboard(ctx context.Context) (*api.MarketDashboard, error) {
	if r.marketDashboard == nil {
		return &api.MarketDashboard{}, nil
	}
	return r.marketDashboard, nil
}

