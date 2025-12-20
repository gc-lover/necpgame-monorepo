// Package server Issue: #1443
// Currency Exchange Repository - database operations for currency exchange
package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/lib/pq"
	"github.com/sirupsen/logrus"
)

// CurrencyExchangeRepositoryInterface defines methods for currency exchange database operations
type CurrencyExchangeRepositoryInterface interface {
	// GetExchangeRates Rates
	GetExchangeRates(ctx context.Context) ([]models.CurrencyExchangeRate, error)
	GetExchangeRate(ctx context.Context, pair string) (*models.CurrencyExchangeRate, error)
	GetExchangeRateHistory(ctx context.Context, pair string, limit int) ([]models.CurrencyExchangeRate, error)
	UpdateExchangeRate(ctx context.Context, rate *models.CurrencyExchangeRate) error

	// CreateOrder Orders
	CreateOrder(ctx context.Context, order *models.CurrencyExchangeOrder) (*models.CurrencyExchangeOrder, error)
	GetOrder(ctx context.Context, orderID uuid.UUID) (*models.CurrencyExchangeOrder, error)
	ListOrders(ctx context.Context, filter models.OrderFilter) ([]models.CurrencyExchangeOrder, error)
	UpdateOrderStatus(ctx context.Context, orderID uuid.UUID, status string) error
	CancelOrder(ctx context.Context, orderID uuid.UUID) error

	// CreateTrade Trades
	CreateTrade(ctx context.Context, trade *models.CurrencyExchangeTrade) (*models.CurrencyExchangeTrade, error)
	ListTrades(ctx context.Context, filter models.TradeFilter) ([]models.CurrencyExchangeTrade, error)
}

// CurrencyExchangeRepository implements CurrencyExchangeRepositoryInterface
type CurrencyExchangeRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

// NewCurrencyExchangeRepository creates a new currency exchange repository
func NewCurrencyExchangeRepository(db *pgxpool.Pool) *CurrencyExchangeRepository {
	return &CurrencyExchangeRepository{
		db:     db,
		logger: GetLogger(),
	}
}

// GetExchangeRates returns all active exchange rates
func (r *CurrencyExchangeRepository) GetExchangeRates(ctx context.Context) ([]models.CurrencyExchangeRate, error) {
	query := `
		SELECT pair, bid, ask, spread, updated_at, is_active
		FROM economy.currency_exchange_rates
		WHERE is_active = true
		ORDER BY pair
	`

	rows, err := r.db.Query(ctx, query)
	if err != nil {
		r.logger.WithError(err).Error("Failed to query exchange rates")
		return nil, fmt.Errorf("failed to query exchange rates: %w", err)
	}
	defer rows.Close()

	var rates []models.CurrencyExchangeRate
	for rows.Next() {
		var rate models.CurrencyExchangeRate
		err := rows.Scan(&rate.Pair, &rate.Bid, &rate.Ask, &rate.Spread, &rate.UpdatedAt, &rate.IsActive)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan exchange rate")
			return nil, fmt.Errorf("failed to scan exchange rate: %w", err)
		}
		rates = append(rates, rate)
	}

	if err = rows.Err(); err != nil {
		r.logger.WithError(err).Error("Error iterating exchange rates")
		return nil, fmt.Errorf("error iterating exchange rates: %w", err)
	}

	return rates, nil
}

// GetExchangeRate returns exchange rate for specific pair
func (r *CurrencyExchangeRepository) GetExchangeRate(ctx context.Context, pair string) (*models.CurrencyExchangeRate, error) {
	query := `
		SELECT pair, bid, ask, spread, updated_at, is_active
		FROM economy.currency_exchange_rates
		WHERE pair = $1 AND is_active = true
	`

	var rate models.CurrencyExchangeRate
	err := r.db.QueryRow(ctx, query, pair).Scan(
		&rate.Pair, &rate.Bid, &rate.Ask, &rate.Spread, &rate.UpdatedAt, &rate.IsActive,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil
		}
		r.logger.WithError(err).Error("Failed to query exchange rate")
		return nil, fmt.Errorf("failed to query exchange rate: %w", err)
	}

	return &rate, nil
}

// GetExchangeRateHistory returns historical exchange rates for pair
func (r *CurrencyExchangeRepository) GetExchangeRateHistory(ctx context.Context, pair string, limit int) ([]models.CurrencyExchangeRate, error) {
	query := `
		SELECT pair, bid, ask, spread, updated_at, is_active
		FROM economy.currency_exchange_rates
		WHERE pair = $1
		ORDER BY updated_at DESC
		LIMIT $2
	`

	rows, err := r.db.Query(ctx, query, pair, limit)
	if err != nil {
		r.logger.WithError(err).Error("Failed to query exchange rate history")
		return nil, fmt.Errorf("failed to query exchange rate history: %w", err)
	}
	defer rows.Close()

	var rates []models.CurrencyExchangeRate
	for rows.Next() {
		var rate models.CurrencyExchangeRate
		err := rows.Scan(&rate.Pair, &rate.Bid, &rate.Ask, &rate.Spread, &rate.UpdatedAt, &rate.IsActive)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan exchange rate history")
			return nil, fmt.Errorf("failed to scan exchange rate history: %w", err)
		}
		rates = append(rates, rate)
	}

	return rates, nil
}

// UpdateExchangeRate updates or inserts exchange rate
func (r *CurrencyExchangeRepository) UpdateExchangeRate(ctx context.Context, rate *models.CurrencyExchangeRate) error {
	query := `
		INSERT INTO economy.currency_exchange_rates (pair, bid, ask, spread, updated_at, is_active)
		VALUES ($1, $2, $3, $4, $5, $6)
		ON CONFLICT (pair) DO UPDATE SET
			bid = EXCLUDED.bid,
			ask = EXCLUDED.ask,
			spread = EXCLUDED.spread,
			updated_at = EXCLUDED.updated_at,
			is_active = EXCLUDED.is_active
	`

	_, err := r.db.Exec(ctx, query,
		rate.Pair, rate.Bid, rate.Ask, rate.Spread, rate.UpdatedAt, rate.IsActive)
	if err != nil {
		r.logger.WithError(err).Error("Failed to update exchange rate")
		return fmt.Errorf("failed to update exchange rate: %w", err)
	}

	return nil
}

// CreateOrder creates a new currency exchange order
func (r *CurrencyExchangeRepository) CreateOrder(ctx context.Context, order *models.CurrencyExchangeOrder) (*models.CurrencyExchangeOrder, error) {
	query := `
		INSERT INTO economy.currency_exchange_orders (
			player_id, order_type, from_currency, to_currency,
			from_amount, to_amount, exchange_rate, fee, status, expires_at
		)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
		RETURNING id, created_at, updated_at
	`

	var expiresAt interface{}
	if order.ExpiresAt != nil {
		expiresAt = *order.ExpiresAt
	} else {
		expiresAt = nil
	}

	err := r.db.QueryRow(ctx, query,
		order.PlayerID, order.OrderType, order.FromCurrency, order.ToCurrency,
		order.FromAmount, order.ToAmount, order.ExchangeRate, order.Fee, order.Status, expiresAt,
	).Scan(&order.ID, &order.CreatedAt, &order.UpdatedAt)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create exchange order")
		return nil, fmt.Errorf("failed to create exchange order: %w", err)
	}

	return order, nil
}

// GetOrder returns order by ID
func (r *CurrencyExchangeRepository) GetOrder(ctx context.Context, orderID uuid.UUID) (*models.CurrencyExchangeOrder, error) {
	query := `
		SELECT id, player_id, order_type, from_currency, to_currency,
			   from_amount, to_amount, exchange_rate, fee, status,
			   created_at, updated_at, filled_at, expires_at
		FROM economy.currency_exchange_orders
		WHERE id = $1
	`

	var order models.CurrencyExchangeOrder
	var filledAt, expiresAt pq.NullTime

	err := r.db.QueryRow(ctx, query, orderID).Scan(
		&order.ID, &order.PlayerID, &order.OrderType, &order.FromCurrency, &order.ToCurrency,
		&order.FromAmount, &order.ToAmount, &order.ExchangeRate, &order.Fee, &order.Status,
		&order.CreatedAt, &order.UpdatedAt, &filledAt, &expiresAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil
		}
		r.logger.WithError(err).Error("Failed to get exchange order")
		return nil, fmt.Errorf("failed to get exchange order: %w", err)
	}

	if filledAt.Valid {
		order.FilledAt = &filledAt.Time
	}
	if expiresAt.Valid {
		order.ExpiresAt = &expiresAt.Time
	}

	return &order, nil
}

// ListOrders returns orders with filtering
func (r *CurrencyExchangeRepository) ListOrders(ctx context.Context, filter models.OrderFilter) ([]models.CurrencyExchangeOrder, error) {
	baseQuery := `
		SELECT id, player_id, order_type, from_currency, to_currency,
			   from_amount, to_amount, exchange_rate, fee, status,
			   created_at, updated_at, filled_at, expires_at
		FROM economy.currency_exchange_orders
		WHERE 1=1
	`
	var args []interface{}

	if filter.PlayerID != nil {
		baseQuery += fmt.Sprintf(" AND player_id = $%d", len(args)+1)
		args = append(args, *filter.PlayerID)
	}

	if filter.OrderType != nil {
		baseQuery += fmt.Sprintf(" AND order_type = $%d", len(args)+1)
		args = append(args, *filter.OrderType)
	}

	if filter.Status != nil {
		baseQuery += fmt.Sprintf(" AND status = $%d", len(args)+1)
		args = append(args, *filter.Status)
	}

	if filter.FromCurrency != nil {
		baseQuery += fmt.Sprintf(" AND from_currency = $%d", len(args)+1)
		args = append(args, *filter.FromCurrency)
	}

	if filter.ToCurrency != nil {
		baseQuery += fmt.Sprintf(" AND to_currency = $%d", len(args)+1)
		args = append(args, *filter.ToCurrency)
	}

	query := baseQuery + " ORDER BY created_at DESC" + fmt.Sprintf(" LIMIT $%d OFFSET $%d", len(args)+1, len(args)+2)
	args = append(args, filter.Limit, filter.Offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		r.logger.WithError(err).Error("Failed to list exchange orders")
		return nil, fmt.Errorf("failed to list exchange orders: %w", err)
	}
	defer rows.Close()

	var orders []models.CurrencyExchangeOrder
	for rows.Next() {
		var order models.CurrencyExchangeOrder
		var filledAt, expiresAt pq.NullTime

		err := rows.Scan(
			&order.ID, &order.PlayerID, &order.OrderType, &order.FromCurrency, &order.ToCurrency,
			&order.FromAmount, &order.ToAmount, &order.ExchangeRate, &order.Fee, &order.Status,
			&order.CreatedAt, &order.UpdatedAt, &filledAt, &expiresAt,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan exchange order")
			return nil, fmt.Errorf("failed to scan exchange order: %w", err)
		}

		if filledAt.Valid {
			order.FilledAt = &filledAt.Time
		}
		if expiresAt.Valid {
			order.ExpiresAt = &expiresAt.Time
		}

		orders = append(orders, order)
	}

	return orders, nil
}

// UpdateOrderStatus updates order status
func (r *CurrencyExchangeRepository) UpdateOrderStatus(ctx context.Context, orderID uuid.UUID, status string) error {
	query := `
		UPDATE economy.currency_exchange_orders
		SET status = $1, updated_at = $2
		WHERE id = $3
	`

	_, err := r.db.Exec(ctx, query, status, time.Now(), orderID)
	if err != nil {
		r.logger.WithError(err).Error("Failed to update order status")
		return fmt.Errorf("failed to update order status: %w", err)
	}

	return nil
}

// CancelOrder cancels an order
func (r *CurrencyExchangeRepository) CancelOrder(ctx context.Context, orderID uuid.UUID) error {
	return r.UpdateOrderStatus(ctx, orderID, "cancelled")
}

// CreateTrade creates a new trade record
func (r *CurrencyExchangeRepository) CreateTrade(ctx context.Context, trade *models.CurrencyExchangeTrade) (*models.CurrencyExchangeTrade, error) {
	query := `
		INSERT INTO economy.currency_exchange_trades (
			order_id, player_id, from_currency, to_currency,
			from_amount, to_amount, exchange_rate, fee, executed_at
		)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		RETURNING trade_id
	`

	err := r.db.QueryRow(ctx, query,
		trade.OrderID, trade.PlayerID, trade.FromCurrency, trade.ToCurrency,
		trade.FromAmount, trade.ToAmount, trade.ExchangeRate, trade.Fee, trade.ExecutedAt,
	).Scan(&trade.TradeID)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create exchange trade")
		return nil, fmt.Errorf("failed to create exchange trade: %w", err)
	}

	return trade, nil
}

// ListTrades returns trades with filtering
func (r *CurrencyExchangeRepository) ListTrades(ctx context.Context, filter models.TradeFilter) ([]models.CurrencyExchangeTrade, error) {
	baseQuery := `
		SELECT trade_id, order_id, player_id, from_currency, to_currency,
			   from_amount, to_amount, exchange_rate, fee, executed_at
		FROM economy.currency_exchange_trades
		WHERE 1=1
	`
	var args []interface{}

	if filter.PlayerID != nil {
		baseQuery += fmt.Sprintf(" AND player_id = $%d", len(args)+1)
		args = append(args, *filter.PlayerID)
	}

	if filter.FromCurrency != nil {
		baseQuery += fmt.Sprintf(" AND from_currency = $%d", len(args)+1)
		args = append(args, *filter.FromCurrency)
	}

	if filter.ToCurrency != nil {
		baseQuery += fmt.Sprintf(" AND to_currency = $%d", len(args)+1)
		args = append(args, *filter.ToCurrency)
	}

	query := baseQuery + " ORDER BY executed_at DESC" + fmt.Sprintf(" LIMIT $%d OFFSET $%d", len(args)+1, len(args)+2)
	args = append(args, filter.Limit, filter.Offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		r.logger.WithError(err).Error("Failed to list exchange trades")
		return nil, fmt.Errorf("failed to list exchange trades: %w", err)
	}
	defer rows.Close()

	var trades []models.CurrencyExchangeTrade
	for rows.Next() {
		var trade models.CurrencyExchangeTrade
		err := rows.Scan(
			&trade.TradeID, &trade.OrderID, &trade.PlayerID, &trade.FromCurrency, &trade.ToCurrency,
			&trade.FromAmount, &trade.ToAmount, &trade.ExchangeRate, &trade.Fee, &trade.ExecutedAt,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan exchange trade")
			return nil, fmt.Errorf("failed to scan exchange trade: %w", err)
		}
		trades = append(trades, trade)
	}

	return trades, nil
}
