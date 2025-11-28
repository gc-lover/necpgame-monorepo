package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/economy-service-go/models"
	"github.com/sirupsen/logrus"
)

type CurrencyExchangeRepositoryInterface interface {
	GetExchangeRates(ctx context.Context) ([]models.ExchangeRate, error)
	GetExchangeRate(ctx context.Context, pair models.CurrencyPair) (*models.ExchangeRate, error)
	CreateOrder(ctx context.Context, order *models.ExchangeOrder) error
	GetOrder(ctx context.Context, orderID uuid.UUID) (*models.ExchangeOrder, error)
	ListOrders(ctx context.Context, playerID uuid.UUID, status *models.OrderStatus, limit, offset int) ([]models.ExchangeOrder, error)
	CountOrders(ctx context.Context, playerID uuid.UUID, status *models.OrderStatus) (int, error)
	DeleteOrder(ctx context.Context, orderID uuid.UUID) error
	ListTrades(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]models.Trade, error)
	CountTrades(ctx context.Context, playerID uuid.UUID) (int, error)
}

type CurrencyExchangeService struct {
	repo   CurrencyExchangeRepositoryInterface
	logger *logrus.Logger
}

func NewCurrencyExchangeService(repo CurrencyExchangeRepositoryInterface) *CurrencyExchangeService {
	return &CurrencyExchangeService{
		repo:   repo,
		logger: GetLogger(),
	}
}

func NewCurrencyExchangeServiceFromDB(dbURL string) (*CurrencyExchangeService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	repo := NewCurrencyExchangeRepository(dbPool)
	return NewCurrencyExchangeService(repo), nil
}

func (s *CurrencyExchangeService) GetExchangeRates(ctx context.Context) (*models.ExchangeRatesResponse, error) {
	rates, err := s.repo.GetExchangeRates(ctx)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get exchange rates")
		return nil, err
	}

	now := time.Now()
	return &models.ExchangeRatesResponse{
		Rates:     rates,
		UpdatedAt: &now,
	}, nil
}

func (s *CurrencyExchangeService) GetExchangeRate(ctx context.Context, pair models.CurrencyPair) (*models.ExchangeRate, error) {
	rate, err := s.repo.GetExchangeRate(ctx, pair)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get exchange rate")
		return nil, err
	}

	if rate == nil {
		return nil, pgx.ErrNoRows
	}

	return rate, nil
}

func (s *CurrencyExchangeService) CalculateQuote(ctx context.Context, req *models.QuoteRequest) (*models.Quote, error) {
	pair := models.CurrencyPair(fmt.Sprintf("%s_%s", req.FromCurrency, req.ToCurrency))
	rate, err := s.repo.GetExchangeRate(ctx, pair)
	if err != nil {
		return nil, err
	}

	if rate == nil {
		return nil, fmt.Errorf("exchange rate not found for pair %s", pair)
	}

	fee := req.Amount * 0.01
	toAmount := (req.Amount - fee) * rate.Bid

	return &models.Quote{
		FromCurrency: req.FromCurrency,
		ToCurrency:   req.ToCurrency,
		FromAmount:   req.Amount,
		ToAmount:     toAmount,
		ExchangeRate: rate.Bid,
		Fee:          fee,
		ExpiresAt:    time.Now().Add(30 * time.Second),
	}, nil
}

func (s *CurrencyExchangeService) InstantExchange(ctx context.Context, playerID uuid.UUID, req *models.InstantExchangeRequest) (*models.ExchangeOrder, error) {
	quote, err := s.CalculateQuote(ctx, &models.QuoteRequest{
		FromCurrency: req.FromCurrency,
		ToCurrency:   req.ToCurrency,
		Amount:       req.Amount,
	})
	if err != nil {
		return nil, err
	}

	filledAt := time.Now()
	order := &models.ExchangeOrder{
		PlayerID:     playerID,
		OrderType:    "instant",
		FromCurrency: req.FromCurrency,
		ToCurrency:   req.ToCurrency,
		FromAmount:   req.Amount,
		ToAmount:     quote.ToAmount,
		ExchangeRate: &quote.ExchangeRate,
		Fee:          quote.Fee,
		Status:       models.OrderStatusFilled,
		FilledAt:     &filledAt,
	}

	err = s.repo.CreateOrder(ctx, order)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create instant exchange order")
		return nil, err
	}

	return order, nil
}

func (s *CurrencyExchangeService) CreateLimitOrder(ctx context.Context, playerID uuid.UUID, req *models.LimitOrderRequest) (*models.ExchangeOrder, error) {
	rate := req.TargetRate
	order := &models.ExchangeOrder{
		PlayerID:     playerID,
		OrderType:    "limit",
		FromCurrency: req.FromCurrency,
		ToCurrency:   req.ToCurrency,
		FromAmount:   req.FromAmount,
		ExchangeRate: &rate,
		ToAmount:     req.FromAmount * rate,
		Fee:          req.FromAmount * 0.01,
		Status:       models.OrderStatusPending,
		ExpiresAt:    req.ExpiresAt,
	}

	err := s.repo.CreateOrder(ctx, order)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create limit order")
		return nil, err
	}

	return order, nil
}

func (s *CurrencyExchangeService) GetOrder(ctx context.Context, orderID uuid.UUID) (*models.ExchangeOrder, error) {
	order, err := s.repo.GetOrder(ctx, orderID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get order")
		return nil, err
	}

	if order == nil {
		return nil, pgx.ErrNoRows
	}

	return order, nil
}

func (s *CurrencyExchangeService) ListOrders(ctx context.Context, playerID uuid.UUID, status *models.OrderStatus, limit, offset int) (*models.OrderListResponse, error) {
	orders, err := s.repo.ListOrders(ctx, playerID, status, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list orders")
		return nil, err
	}

	total, err := s.repo.CountOrders(ctx, playerID, status)
	if err != nil {
		s.logger.WithError(err).Warn("Failed to count orders")
		total = len(orders)
	}

	return &models.OrderListResponse{
		Orders: orders,
		Total:  total,
		Limit:  limit,
		Offset: offset,
	}, nil
}

func (s *CurrencyExchangeService) DeleteOrder(ctx context.Context, orderID uuid.UUID) error {
	order, err := s.repo.GetOrder(ctx, orderID)
	if err != nil {
		return err
	}

	if order == nil {
		return pgx.ErrNoRows
	}

	if order.Status == models.OrderStatusFilled {
		return fmt.Errorf("cannot delete filled order")
	}

	err = s.repo.DeleteOrder(ctx, orderID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to delete order")
		return err
	}

	return nil
}

func (s *CurrencyExchangeService) ListTrades(ctx context.Context, playerID uuid.UUID, limit, offset int) (*models.CurrencyExchangeTradeListResponse, error) {
	trades, err := s.repo.ListTrades(ctx, playerID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list trades")
		return nil, err
	}

	total, err := s.repo.CountTrades(ctx, playerID)
	if err != nil {
		s.logger.WithError(err).Warn("Failed to count trades")
		total = len(trades)
	}

	return &models.CurrencyExchangeTradeListResponse{
		Trades: trades,
		Total:  total,
		Limit:  limit,
		Offset: offset,
	}, nil
}

