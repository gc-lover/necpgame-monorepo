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

// NOTE: This repository requires database migration for tables:
// - economy.currency_exchange_rates (pair, bid, ask, spread, updated_at, is_active)
// - economy.currency_exchange_orders (id, player_id, order_type, from_currency, to_currency, from_amount, to_amount, exchange_rate, fee, status, created_at, updated_at, filled_at, expires_at)
// - economy.currency_exchange_trades (trade_id, order_id, player_id, from_currency, to_currency, from_amount, to_amount, exchange_rate, fee, executed_at)

type CurrencyExchangeRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewCurrencyExchangeRepository(db *pgxpool.Pool) *CurrencyExchangeRepository {
	return &CurrencyExchangeRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *CurrencyExchangeRepository) GetExchangeRates(ctx context.Context) ([]models.ExchangeRate, error) {
	query := `
		SELECT pair, bid, ask, spread, updated_at
		FROM economy.currency_exchange_rates
		WHERE is_active = true
		ORDER BY pair ASC`

	rows, err := r.db.Query(ctx, query)
	if err != nil {
		return nil, fmt.Errorf("failed to get exchange rates: %w", err)
	}
	defer rows.Close()

	var rates []models.ExchangeRate
	for rows.Next() {
		var rate models.ExchangeRate
		var spread *float64

		err := rows.Scan(&rate.Pair, &rate.Bid, &rate.Ask, &spread, &rate.Timestamp)
		if err != nil {
			return nil, fmt.Errorf("failed to scan exchange rate: %w", err)
		}

		rate.Spread = spread
		rates = append(rates, rate)
	}

	return rates, nil
}

func (r *CurrencyExchangeRepository) GetExchangeRate(ctx context.Context, pair models.CurrencyPair) (*models.ExchangeRate, error) {
	var rate models.ExchangeRate
	var spread *float64

	query := `
		SELECT pair, bid, ask, spread, updated_at
		FROM economy.currency_exchange_rates
		WHERE pair = $1 AND is_active = true`

	err := r.db.QueryRow(ctx, query, pair).Scan(
		&rate.Pair, &rate.Bid, &rate.Ask, &spread, &rate.Timestamp,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get exchange rate: %w", err)
	}

	rate.Spread = spread
	return &rate, nil
}

func (r *CurrencyExchangeRepository) CreateOrder(ctx context.Context, order *models.ExchangeOrder) error {
	query := `
		INSERT INTO economy.currency_exchange_orders (
			id, player_id, order_type, from_currency, to_currency,
			from_amount, to_amount, exchange_rate, fee, status,
			created_at, updated_at, expires_at
		) VALUES (
			gen_random_uuid(), $1, $2, $3, $4, $5, $6, $7, $8, $9, NOW(), NOW(), $10
		) RETURNING id, created_at, updated_at`

	err := r.db.QueryRow(ctx, query,
		order.PlayerID, order.OrderType, order.FromCurrency, order.ToCurrency,
		order.FromAmount, order.ToAmount, order.ExchangeRate, order.Fee,
		order.Status, order.ExpiresAt,
	).Scan(&order.OrderID, &order.CreatedAt, &order.UpdatedAt)

	return err
}

func (r *CurrencyExchangeRepository) GetOrder(ctx context.Context, orderID uuid.UUID) (*models.ExchangeOrder, error) {
	var order models.ExchangeOrder
	var exchangeRate *float64
	var filledAt, expiresAt *time.Time

	query := `
		SELECT id, player_id, order_type, from_currency, to_currency,
		       from_amount, to_amount, exchange_rate, fee, status,
		       created_at, updated_at, filled_at, expires_at
		FROM economy.currency_exchange_orders
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, orderID).Scan(
		&order.OrderID, &order.PlayerID, &order.OrderType,
		&order.FromCurrency, &order.ToCurrency,
		&order.FromAmount, &order.ToAmount, &exchangeRate,
		&order.Fee, &order.Status,
		&order.CreatedAt, &order.UpdatedAt, &filledAt, &expiresAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get order: %w", err)
	}

	order.ExchangeRate = exchangeRate
	order.FilledAt = filledAt
	order.ExpiresAt = expiresAt

	return &order, nil
}

func (r *CurrencyExchangeRepository) ListOrders(ctx context.Context, playerID uuid.UUID, status *models.OrderStatus, limit, offset int) ([]models.ExchangeOrder, error) {
	var args []interface{}
	baseQuery := `
		SELECT id, player_id, order_type, from_currency, to_currency,
		       from_amount, to_amount, exchange_rate, fee, status,
		       created_at, updated_at, filled_at, expires_at
		FROM economy.currency_exchange_orders
		WHERE player_id = $1`

	args = append(args, playerID)
	argIndex := 2

	if status != nil {
		baseQuery += fmt.Sprintf(" AND status = $%d", argIndex)
		args = append(args, *status)
		argIndex++
	}

	baseQuery += " ORDER BY created_at DESC"
	baseQuery += fmt.Sprintf(" LIMIT $%d OFFSET $%d", argIndex, argIndex+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, baseQuery, args...)
	if err != nil {
		return nil, fmt.Errorf("failed to list orders: %w", err)
	}
	defer rows.Close()

	var orders []models.ExchangeOrder
	for rows.Next() {
		var order models.ExchangeOrder
		var exchangeRate *float64
		var filledAt, expiresAt *time.Time

		err := rows.Scan(
			&order.OrderID, &order.PlayerID, &order.OrderType,
			&order.FromCurrency, &order.ToCurrency,
			&order.FromAmount, &order.ToAmount, &exchangeRate,
			&order.Fee, &order.Status,
			&order.CreatedAt, &order.UpdatedAt, &filledAt, &expiresAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan order: %w", err)
		}

		order.ExchangeRate = exchangeRate
		order.FilledAt = filledAt
		order.ExpiresAt = expiresAt
		orders = append(orders, order)
	}

	return orders, nil
}

func (r *CurrencyExchangeRepository) CountOrders(ctx context.Context, playerID uuid.UUID, status *models.OrderStatus) (int, error) {
	var args []interface{}
	baseQuery := `SELECT COUNT(*) FROM economy.currency_exchange_orders WHERE player_id = $1`
	args = append(args, playerID)

	if status != nil {
		baseQuery += " AND status = $2"
		args = append(args, *status)
	}

	var count int
	err := r.db.QueryRow(ctx, baseQuery, args...).Scan(&count)
	return count, err
}

func (r *CurrencyExchangeRepository) DeleteOrder(ctx context.Context, orderID uuid.UUID) error {
	query := `DELETE FROM economy.currency_exchange_orders WHERE id = $1`
	result, err := r.db.Exec(ctx, query, orderID)
	if err != nil {
		return fmt.Errorf("failed to delete order: %w", err)
	}

	if result.RowsAffected() == 0 {
		return pgx.ErrNoRows
	}

	return nil
}

func (r *CurrencyExchangeRepository) ListTrades(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]models.Trade, error) {
	query := `
		SELECT trade_id, order_id, player_id, from_currency, to_currency,
		       from_amount, to_amount, exchange_rate, fee, executed_at
		FROM economy.currency_exchange_trades
		WHERE player_id = $1
		ORDER BY executed_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, playerID, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to list trades: %w", err)
	}
	defer rows.Close()

	var trades []models.Trade
	for rows.Next() {
		var trade models.Trade

		err := rows.Scan(
			&trade.TradeID, &trade.OrderID, &trade.PlayerID,
			&trade.FromCurrency, &trade.ToCurrency,
			&trade.FromAmount, &trade.ToAmount,
			&trade.ExchangeRate, &trade.Fee, &trade.ExecutedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan trade: %w", err)
		}

		trades = append(trades, trade)
	}

	return trades, nil
}

func (r *CurrencyExchangeRepository) CountTrades(ctx context.Context, playerID uuid.UUID) (int, error) {
	var count int
	err := r.db.QueryRow(ctx,
		`SELECT COUNT(*) FROM economy.currency_exchange_trades WHERE player_id = $1`,
		playerID,
	).Scan(&count)
	return count, err
}

