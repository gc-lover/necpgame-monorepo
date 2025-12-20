// Issue: #1443
// Currency Exchange Service Tests
package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
)

// MockCurrencyExchangeRepository is a mock for CurrencyExchangeRepositoryInterface
type MockCurrencyExchangeRepository struct {
	GetExchangeRatesFunc       func(ctx context.Context) ([]models.CurrencyExchangeRate, error)
	GetExchangeRateFunc        func(ctx context.Context, pair string) (*models.CurrencyExchangeRate, error)
	GetExchangeRateHistoryFunc func(ctx context.Context, pair string, limit int) ([]models.CurrencyExchangeRate, error)
	UpdateExchangeRateFunc     func(ctx context.Context, rate *models.CurrencyExchangeRate) error
	CreateOrderFunc            func(ctx context.Context, order *models.CurrencyExchangeOrder) (*models.CurrencyExchangeOrder, error)
	GetOrderFunc               func(ctx context.Context, orderID uuid.UUID) (*models.CurrencyExchangeOrder, error)
	UpdateOrderStatusFunc      func(ctx context.Context, orderID uuid.UUID, status string) error
	CreateTradeFunc            func(ctx context.Context, trade *models.CurrencyExchangeTrade) (*models.CurrencyExchangeTrade, error)
}

func (m *MockCurrencyExchangeRepository) GetExchangeRates(ctx context.Context) ([]models.CurrencyExchangeRate, error) {
	if m.GetExchangeRatesFunc != nil {
		return m.GetExchangeRatesFunc(ctx)
	}
	return nil, errors.New("not implemented")
}

func (m *MockCurrencyExchangeRepository) GetExchangeRate(ctx context.Context, pair string) (*models.CurrencyExchangeRate, error) {
	if m.GetExchangeRateFunc != nil {
		return m.GetExchangeRateFunc(ctx, pair)
	}
	return nil, errors.New("not implemented")
}

func (m *MockCurrencyExchangeRepository) GetExchangeRateHistory(ctx context.Context, pair string, limit int) ([]models.CurrencyExchangeRate, error) {
	if m.GetExchangeRateHistoryFunc != nil {
		return m.GetExchangeRateHistoryFunc(ctx, pair, limit)
	}
	return nil, errors.New("not implemented")
}

func (m *MockCurrencyExchangeRepository) UpdateExchangeRate(ctx context.Context, rate *models.CurrencyExchangeRate) error {
	if m.UpdateExchangeRateFunc != nil {
		return m.UpdateExchangeRateFunc(ctx, rate)
	}
	return errors.New("not implemented")
}

func (m *MockCurrencyExchangeRepository) CreateOrder(ctx context.Context, order *models.CurrencyExchangeOrder) (*models.CurrencyExchangeOrder, error) {
	if m.CreateOrderFunc != nil {
		return m.CreateOrderFunc(ctx, order)
	}
	return nil, errors.New("not implemented")
}

func (m *MockCurrencyExchangeRepository) GetOrder(ctx context.Context, orderID uuid.UUID) (*models.CurrencyExchangeOrder, error) {
	if m.GetOrderFunc != nil {
		return m.GetOrderFunc(ctx, orderID)
	}
	return nil, errors.New("not implemented")
}

func (m *MockCurrencyExchangeRepository) UpdateOrderStatus(ctx context.Context, orderID uuid.UUID, status string) error {
	if m.UpdateOrderStatusFunc != nil {
		return m.UpdateOrderStatusFunc(ctx, orderID, status)
	}
	return errors.New("not implemented")
}

func (m *MockCurrencyExchangeRepository) CancelOrder(ctx context.Context, orderID uuid.UUID) error {
	return m.UpdateOrderStatus(ctx, orderID, "cancelled")
}

func (m *MockCurrencyExchangeRepository) ListOrders(_ context.Context, _ models.OrderFilter) ([]models.CurrencyExchangeOrder, error) {
	return nil, errors.New("not implemented")
}

func (m *MockCurrencyExchangeRepository) CreateTrade(ctx context.Context, trade *models.CurrencyExchangeTrade) (*models.CurrencyExchangeTrade, error) {
	if m.CreateTradeFunc != nil {
		return m.CreateTradeFunc(ctx, trade)
	}
	return nil, errors.New("not implemented")
}

func (m *MockCurrencyExchangeRepository) ListTrades(_ context.Context, _ models.TradeFilter) ([]models.CurrencyExchangeTrade, error) {
	return nil, errors.New("not implemented")
}

func TestCurrencyExchangeService_GetExchangeRates(t *testing.T) {
	mockRepo := &MockCurrencyExchangeRepository{
		GetExchangeRatesFunc: func(ctx context.Context) ([]models.CurrencyExchangeRate, error) {
			return []models.CurrencyExchangeRate{
				{Pair: "USD/EUR", Bid: 0.85, Ask: 0.87, Spread: 0.02, IsActive: true},
			}, nil
		},
	}

	service := NewCurrencyExchangeService(mockRepo)

	rates, err := service.GetExchangeRates(context.Background())

	assert.NoError(t, err)
	assert.Len(t, rates, 1)
	assert.Equal(t, "USD/EUR", rates[0].Pair)
	assert.Equal(t, 0.85, rates[0].Bid)
}

func TestCurrencyExchangeService_GetExchangeRate(t *testing.T) {
	mockRepo := &MockCurrencyExchangeRepository{
		GetExchangeRateFunc: func(ctx context.Context, pair string) (*models.CurrencyExchangeRate, error) {
			if pair == "USD/EUR" {
				return &models.CurrencyExchangeRate{
					Pair: "USD/EUR", Bid: 0.85, Ask: 0.87, Spread: 0.02, IsActive: true,
				}, nil
			}
			return nil, nil
		},
	}

	service := NewCurrencyExchangeService(mockRepo)

	rate, err := service.GetExchangeRate(context.Background(), "USD/EUR")

	assert.NoError(t, err)
	assert.NotNil(t, rate)
	assert.Equal(t, "USD/EUR", rate.Pair)
	assert.Equal(t, 0.85, rate.Bid)
}

func TestCurrencyExchangeService_CreateQuote(t *testing.T) {
	mockRepo := &MockCurrencyExchangeRepository{
		GetExchangeRateFunc: func(ctx context.Context, pair string) (*models.CurrencyExchangeRate, error) {
			if pair == "USD/EUR" {
				return &models.CurrencyExchangeRate{
					Pair: "USD/EUR", Bid: 0.85, Ask: 0.87, Spread: 0.02, IsActive: true,
				}, nil
			}
			return nil, nil
		},
	}

	service := NewCurrencyExchangeService(mockRepo)

	quote, err := service.CreateQuote(context.Background(), "USD", "EUR", 100.0)

	assert.NoError(t, err)
	assert.NotNil(t, quote)
	assert.Equal(t, "USD", quote.FromCurrency)
	assert.Equal(t, "EUR", quote.ToCurrency)
	assert.Equal(t, 100.0, quote.FromAmount)
	assert.Equal(t, 84.15, quote.ToAmount) // 100 * 0.85 - 0.85 fee
	assert.Equal(t, 0.85, quote.ExchangeRate)
	assert.Equal(t, 0.85, quote.Fee)
	assert.True(t, quote.ValidUntil.After(time.Now()))
}

func TestCurrencyExchangeService_CreateQuote_InvalidAmount(t *testing.T) {
	mockRepo := &MockCurrencyExchangeRepository{}
	service := NewCurrencyExchangeService(mockRepo)

	quote, err := service.CreateQuote(context.Background(), "USD", "EUR", 0)

	assert.Error(t, err)
	assert.Nil(t, quote)
	assert.Contains(t, err.Error(), "invalid amount")
}

func TestCurrencyExchangeService_CreateQuote_RateNotFound(t *testing.T) {
	mockRepo := &MockCurrencyExchangeRepository{
		GetExchangeRateFunc: func(ctx context.Context, pair string) (*models.CurrencyExchangeRate, error) {
			return nil, nil // Rate not found
		},
	}

	service := NewCurrencyExchangeService(mockRepo)

	quote, err := service.CreateQuote(context.Background(), "USD", "EUR", 100.0)

	assert.Error(t, err)
	assert.Nil(t, quote)
	assert.Contains(t, err.Error(), "exchange rate not found")
}

func TestCurrencyExchangeService_CreateInstantExchange(t *testing.T) {
	playerID := uuid.New()
	orderID := uuid.New()

	mockRepo := &MockCurrencyExchangeRepository{
		GetExchangeRateFunc: func(ctx context.Context, pair string) (*models.CurrencyExchangeRate, error) {
			if pair == "USD/EUR" {
				return &models.CurrencyExchangeRate{
					Pair: "USD/EUR", Bid: 0.85, Ask: 0.87, Spread: 0.02, IsActive: true,
				}, nil
			}
			return nil, nil
		},
		CreateOrderFunc: func(ctx context.Context, order *models.CurrencyExchangeOrder) (*models.CurrencyExchangeOrder, error) {
			order.ID = orderID
			order.CreatedAt = time.Now()
			order.UpdatedAt = time.Now()
			return order, nil
		},
		CreateTradeFunc: func(ctx context.Context, trade *models.CurrencyExchangeTrade) (*models.CurrencyExchangeTrade, error) {
			trade.TradeID = uuid.New()
			return trade, nil
		},
		UpdateOrderStatusFunc: func(ctx context.Context, orderID uuid.UUID, status string) error {
			return nil
		},
	}

	service := NewCurrencyExchangeService(mockRepo)

	req := models.CreateInstantExchangeRequest{
		FromCurrency: "USD",
		ToCurrency:   "EUR",
		FromAmount:   100.0,
	}

	order, err := service.CreateInstantExchange(context.Background(), playerID, req)

	assert.NoError(t, err)
	assert.NotNil(t, order)
	assert.Equal(t, orderID, order.ID)
	assert.Equal(t, playerID, order.PlayerID)
	assert.Equal(t, "instant", order.OrderType)
	assert.Equal(t, "USD", order.FromCurrency)
	assert.Equal(t, "EUR", order.ToCurrency)
	assert.Equal(t, 100.0, order.FromAmount)
	assert.Equal(t, 84.15, order.ToAmount) // 100 * 0.85 - 0.85 fee
	assert.Equal(t, 0.85, order.ExchangeRate)
	assert.Equal(t, 0.85, order.Fee)
	assert.Equal(t, "pending", order.Status)
}

func TestCurrencyExchangeService_GetOrder(t *testing.T) {
	orderID := uuid.New()
	playerID := uuid.New()

	mockRepo := &MockCurrencyExchangeRepository{
		GetOrderFunc: func(ctx context.Context, orderID uuid.UUID) (*models.CurrencyExchangeOrder, error) {
			return &models.CurrencyExchangeOrder{
				ID:           orderID,
				PlayerID:     playerID,
				OrderType:    "instant",
				FromCurrency: "USD",
				ToCurrency:   "EUR",
				FromAmount:   100.0,
				ToAmount:     85.0,
				ExchangeRate: 0.85,
				Fee:          0.85,
				Status:       "filled",
				CreatedAt:    time.Now(),
				UpdatedAt:    time.Now(),
				FilledAt:     &time.Time{},
			}, nil
		},
	}

	service := NewCurrencyExchangeService(mockRepo)

	order, err := service.GetOrder(context.Background(), orderID)

	assert.NoError(t, err)
	assert.NotNil(t, order)
	assert.Equal(t, orderID, order.ID)
	assert.Equal(t, playerID, order.PlayerID)
	assert.Equal(t, "filled", order.Status)
}

func TestCurrencyExchangeService_CancelOrder(t *testing.T) {
	orderID := uuid.New()

	mockRepo := &MockCurrencyExchangeRepository{
		GetOrderFunc: func(ctx context.Context, orderID uuid.UUID) (*models.CurrencyExchangeOrder, error) {
			return &models.CurrencyExchangeOrder{
				ID:     orderID,
				Status: "pending",
			}, nil
		},
		UpdateOrderStatusFunc: func(ctx context.Context, orderID uuid.UUID, status string) error {
			assert.Equal(t, "cancelled", status)
			return nil
		},
	}

	service := NewCurrencyExchangeService(mockRepo)

	err := service.CancelOrder(context.Background(), orderID)

	assert.NoError(t, err)
}

func TestCurrencyExchangeService_CancelOrder_NotFound(t *testing.T) {
	orderID := uuid.New()

	mockRepo := &MockCurrencyExchangeRepository{
		GetOrderFunc: func(ctx context.Context, orderID uuid.UUID) (*models.CurrencyExchangeOrder, error) {
			return nil, nil // Order not found
		},
	}

	service := NewCurrencyExchangeService(mockRepo)

	err := service.CancelOrder(context.Background(), orderID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "order not found")
}

func TestCurrencyExchangeService_CancelOrder_InvalidStatus(t *testing.T) {
	orderID := uuid.New()

	mockRepo := &MockCurrencyExchangeRepository{
		GetOrderFunc: func(ctx context.Context, orderID uuid.UUID) (*models.CurrencyExchangeOrder, error) {
			return &models.CurrencyExchangeOrder{
				ID:     orderID,
				Status: "filled", // Already filled, cannot cancel
			}, nil
		},
	}

	service := NewCurrencyExchangeService(mockRepo)

	err := service.CancelOrder(context.Background(), orderID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "cannot cancel order")
}
