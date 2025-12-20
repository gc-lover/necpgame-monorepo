// Package server SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1443
// Currency Exchange Service - business logic for currency exchange operations
package server

import (
	"context"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// CurrencyExchangeServiceInterface defines business logic methods for currency exchange
type CurrencyExchangeServiceInterface interface {
	// GetExchangeRates Rates
	GetExchangeRates(ctx context.Context) ([]models.CurrencyExchangeRate, error)
	GetExchangeRate(ctx context.Context, pair string) (*models.CurrencyExchangeRate, error)
	GetExchangeRateHistory(ctx context.Context, pair string, limit int) ([]models.CurrencyExchangeRate, error)

	// CreateQuote Quotes
	CreateQuote(ctx context.Context, fromCurrency, toCurrency string, fromAmount float64) (*models.CurrencyExchangeQuote, error)

	// CreateInstantExchange Orders
	CreateInstantExchange(ctx context.Context, playerID uuid.UUID, req models.CreateInstantExchangeRequest) (*models.CurrencyExchangeOrder, error)
	CreateLimitOrder(ctx context.Context, playerID uuid.UUID, req models.CreateLimitOrderRequest) (*models.CurrencyExchangeOrder, error)
	GetOrder(ctx context.Context, orderID uuid.UUID) (*models.CurrencyExchangeOrder, error)
	ListOrders(ctx context.Context, filter models.OrderFilter) ([]models.CurrencyExchangeOrder, error)
	CancelOrder(ctx context.Context, orderID uuid.UUID) error

	// ListTrades Trades
	ListTrades(ctx context.Context, filter models.TradeFilter) ([]models.CurrencyExchangeTrade, error)

	// ProcessExpiredOrders Background processing
	ProcessExpiredOrders(ctx context.Context) error
	ProcessLimitOrders(ctx context.Context) error
}

// CurrencyExchangeService implements CurrencyExchangeServiceInterface
type CurrencyExchangeService struct {
	repo   CurrencyExchangeRepositoryInterface
	logger *logrus.Logger
}

// NewCurrencyExchangeService creates a new currency exchange service
func NewCurrencyExchangeService(repo CurrencyExchangeRepositoryInterface) *CurrencyExchangeService {
	return &CurrencyExchangeService{
		repo:   repo,
		logger: GetLogger(),
	}
}

// GetExchangeRates returns all active exchange rates
func (s *CurrencyExchangeService) GetExchangeRates(ctx context.Context) ([]models.CurrencyExchangeRate, error) {
	rates, err := s.repo.GetExchangeRates(ctx)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get exchange rates")
		return nil, fmt.Errorf("failed to get exchange rates: %w", err)
	}
	return rates, nil
}

// GetExchangeRate returns exchange rate for specific pair
func (s *CurrencyExchangeService) GetExchangeRate(ctx context.Context, pair string) (*models.CurrencyExchangeRate, error) {
	rate, err := s.repo.GetExchangeRate(ctx, pair)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get exchange rate")
		return nil, fmt.Errorf("failed to get exchange rate: %w", err)
	}
	return rate, nil
}

// GetExchangeRateHistory returns historical exchange rates
func (s *CurrencyExchangeService) GetExchangeRateHistory(ctx context.Context, pair string, limit int) ([]models.CurrencyExchangeRate, error) {
	if limit <= 0 || limit > 100 {
		limit = 50 // default limit
	}

	rates, err := s.repo.GetExchangeRateHistory(ctx, pair, limit)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get exchange rate history")
		return nil, fmt.Errorf("failed to get exchange rate history: %w", err)
	}
	return rates, nil
}

// CreateQuote creates a quote for currency exchange
func (s *CurrencyExchangeService) CreateQuote(ctx context.Context, fromCurrency, toCurrency string, fromAmount float64) (*models.CurrencyExchangeQuote, error) {
	if fromAmount <= 0 {
		return nil, fmt.Errorf("invalid amount: must be positive")
	}

	pair := fmt.Sprintf("%s/%s", fromCurrency, toCurrency)
	rate, err := s.repo.GetExchangeRate(ctx, pair)
	if err != nil {
		return nil, fmt.Errorf("failed to get exchange rate for pair %s: %w", pair, err)
	}
	if rate == nil {
		return nil, fmt.Errorf("exchange rate not found for pair %s", pair)
	}

	// Calculate exchange amount using bid rate (what buyer pays)
	exchangeRate := rate.Bid
	toAmount := fromAmount * exchangeRate

	// Calculate fee (0.1% for instant exchange)
	feeRate := 0.001
	fee := toAmount * feeRate

	// Valid for 30 seconds
	validUntil := time.Now().Add(30 * time.Second)

	quote := &models.CurrencyExchangeQuote{
		FromCurrency: fromCurrency,
		ToCurrency:   toCurrency,
		FromAmount:   fromAmount,
		ToAmount:     toAmount - fee, // Fee deducted from received amount
		ExchangeRate: exchangeRate,
		Fee:          fee,
		ValidUntil:   validUntil,
	}

	return quote, nil
}

// CreateInstantExchange creates an instant currency exchange order
func (s *CurrencyExchangeService) CreateInstantExchange(ctx context.Context, playerID uuid.UUID, req models.CreateInstantExchangeRequest) (*models.CurrencyExchangeOrder, error) {
	// Create quote first
	quote, err := s.CreateQuote(ctx, req.FromCurrency, req.ToCurrency, req.FromAmount)
	if err != nil {
		return nil, fmt.Errorf("failed to create quote: %w", err)
	}

	// Check if quote is still valid
	if time.Now().After(quote.ValidUntil) {
		return nil, fmt.Errorf("quote expired")
	}

	// Create order
	order := &models.CurrencyExchangeOrder{
		PlayerID:     playerID,
		OrderType:    "instant",
		FromCurrency: req.FromCurrency,
		ToCurrency:   req.ToCurrency,
		FromAmount:   req.FromAmount,
		ToAmount:     quote.ToAmount,
		ExchangeRate: quote.ExchangeRate,
		Fee:          quote.Fee,
		Status:       "pending",
	}

	createdOrder, err := s.repo.CreateOrder(ctx, order)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create instant exchange order")
		return nil, fmt.Errorf("failed to create instant exchange order: %w", err)
	}

	// Execute the trade immediately
	trade := &models.CurrencyExchangeTrade{
		OrderID:      createdOrder.ID,
		PlayerID:     playerID,
		FromCurrency: req.FromCurrency,
		ToCurrency:   req.ToCurrency,
		FromAmount:   req.FromAmount,
		ToAmount:     quote.ToAmount,
		ExchangeRate: quote.ExchangeRate,
		Fee:          quote.Fee,
		ExecutedAt:   time.Now(),
	}

	_, err = s.repo.CreateTrade(ctx, trade)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create instant exchange trade")
		// Try to cancel the order
		s.repo.UpdateOrderStatus(ctx, createdOrder.ID, "failed")
		return nil, fmt.Errorf("failed to create instant exchange trade: %w", err)
	}

	// Mark order as filled
	err = s.repo.UpdateOrderStatus(ctx, createdOrder.ID, "filled")
	if err != nil {
		s.logger.WithError(err).Error("Failed to update order status to filled")
		// This is not critical, trade was created successfully
	}

	return createdOrder, nil
}

// CreateLimitOrder creates a limit order for currency exchange
func (s *CurrencyExchangeService) CreateLimitOrder(ctx context.Context, playerID uuid.UUID, req models.CreateLimitOrderRequest) (*models.CurrencyExchangeOrder, error) {
	// Validate exchange rate
	if req.ExchangeRate <= 0 {
		return nil, fmt.Errorf("invalid exchange rate: must be positive")
	}

	// Calculate amounts
	toAmount := req.FromAmount * req.ExchangeRate

	// Calculate fee (0.05% for limit orders)
	feeRate := 0.0005
	fee := toAmount * feeRate

	// Set expiration if not provided (24 hours default)
	expiresAt := req.ExpiresAt
	if expiresAt == nil {
		defaultExpiry := time.Now().Add(24 * time.Hour)
		expiresAt = &defaultExpiry
	}

	order := &models.CurrencyExchangeOrder{
		PlayerID:     playerID,
		OrderType:    "limit",
		FromCurrency: req.FromCurrency,
		ToCurrency:   req.ToCurrency,
		FromAmount:   req.FromAmount,
		ToAmount:     toAmount - fee,
		ExchangeRate: req.ExchangeRate,
		Fee:          fee,
		Status:       "pending",
		ExpiresAt:    expiresAt,
	}

	createdOrder, err := s.repo.CreateOrder(ctx, order)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create limit order")
		return nil, fmt.Errorf("failed to create limit order: %w", err)
	}

	return createdOrder, nil
}

// GetOrder returns order by ID
func (s *CurrencyExchangeService) GetOrder(ctx context.Context, orderID uuid.UUID) (*models.CurrencyExchangeOrder, error) {
	order, err := s.repo.GetOrder(ctx, orderID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get order")
		return nil, fmt.Errorf("failed to get order: %w", err)
	}
	return order, nil
}

// ListOrders returns orders with filtering
func (s *CurrencyExchangeService) ListOrders(ctx context.Context, filter models.OrderFilter) ([]models.CurrencyExchangeOrder, error) {
	orders, err := s.repo.ListOrders(ctx, filter)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list orders")
		return nil, fmt.Errorf("failed to list orders: %w", err)
	}
	return orders, nil
}

// CancelOrder cancels an order
func (s *CurrencyExchangeService) CancelOrder(ctx context.Context, orderID uuid.UUID) error {
	// Check if order exists and can be cancelled
	order, err := s.repo.GetOrder(ctx, orderID)
	if err != nil {
		return fmt.Errorf("failed to get order: %w", err)
	}
	if order == nil {
		return fmt.Errorf("order not found")
	}

	if order.Status != "pending" {
		return fmt.Errorf("cannot cancel order with status: %s", order.Status)
	}

	err = s.repo.CancelOrder(ctx, orderID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to cancel order")
		return fmt.Errorf("failed to cancel order: %w", err)
	}

	return nil
}

// ListTrades returns trades with filtering
func (s *CurrencyExchangeService) ListTrades(ctx context.Context, filter models.TradeFilter) ([]models.CurrencyExchangeTrade, error) {
	trades, err := s.repo.ListTrades(ctx, filter)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list trades")
		return nil, fmt.Errorf("failed to list trades: %w", err)
	}
	return trades, nil
}

// ProcessExpiredOrders processes expired limit orders
func (s *CurrencyExchangeService) ProcessExpiredOrders(_ context.Context) error {
	// This would be called by a background job
	// For now, just log that it's not implemented
	s.logger.Info("ProcessExpiredOrders called - not implemented yet")
	return nil
}

// ProcessLimitOrders processes limit orders that can be filled
func (s *CurrencyExchangeService) ProcessLimitOrders(_ context.Context) error {
	// This would be called by a background job
	// For now, just log that it's not implemented yet
	s.logger.Info("ProcessLimitOrders called - not implemented yet")
	return nil
}
