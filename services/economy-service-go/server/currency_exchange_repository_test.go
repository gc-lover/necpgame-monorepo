// Issue: #1443
// Currency Exchange Repository Tests
package server

import (
	"context"
	"database/sql"
	"testing"
	"time"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
)

func TestCurrencyExchangeRepository_GetExchangeRates(t *testing.T) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := NewCurrencyExchangeRepository(db)

	rows := sqlmock.NewRows([]string{"pair", "bid", "ask", "spread", "updated_at", "is_active"}).
		AddRow("USD/EUR", 0.85, 0.87, 0.02, time.Now(), true).
		AddRow("EUR/USD", 1.15, 1.17, 0.02, time.Now(), true)

	mock.ExpectQuery(`SELECT pair, bid, ask, spread, updated_at, is_active FROM economy.currency_exchange_rates WHERE is_active = true ORDER BY pair`).
		WillReturnRows(rows)

	rates, err := repo.GetExchangeRates(context.Background())

	assert.NoError(t, err)
	assert.Len(t, rates, 2)
	assert.Equal(t, "USD/EUR", rates[0].Pair)
	assert.Equal(t, 0.85, rates[0].Bid)
	assert.Equal(t, 0.87, rates[0].Ask)

	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestCurrencyExchangeRepository_GetExchangeRate(t *testing.T) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := NewCurrencyExchangeRepository(db)

	row := sqlmock.NewRows([]string{"pair", "bid", "ask", "spread", "updated_at", "is_active"}).
		AddRow("USD/EUR", 0.85, 0.87, 0.02, time.Now(), true)

	mock.ExpectQuery(`SELECT pair, bid, ask, spread, updated_at, is_active FROM economy.currency_exchange_rates WHERE pair = \$1 AND is_active = true`).
		WithArgs("USD/EUR").
		WillReturnRows(row)

	rate, err := repo.GetExchangeRate(context.Background(), "USD/EUR")

	assert.NoError(t, err)
	assert.NotNil(t, rate)
	assert.Equal(t, "USD/EUR", rate.Pair)
	assert.Equal(t, 0.85, rate.Bid)

	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestCurrencyExchangeRepository_GetExchangeRate_NotFound(t *testing.T) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := NewCurrencyExchangeRepository(db)

	mock.ExpectQuery(`SELECT pair, bid, ask, spread, updated_at, is_active FROM economy.currency_exchange_rates WHERE pair = \$1 AND is_active = true`).
		WithArgs("USD/EUR").
		WillReturnRows(sqlmock.NewRows([]string{"pair", "bid", "ask", "spread", "updated_at", "is_active"}))

	rate, err := repo.GetExchangeRate(context.Background(), "USD/EUR")

	assert.NoError(t, err)
	assert.Nil(t, rate)

	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestCurrencyExchangeRepository_CreateOrder(t *testing.T) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := NewCurrencyExchangeRepository(db)

	playerID := uuid.New()
	order := &models.CurrencyExchangeOrder{
		PlayerID:     playerID,
		OrderType:    "instant",
		FromCurrency: "USD",
		ToCurrency:   "EUR",
		FromAmount:   100.0,
		ToAmount:     85.0,
		ExchangeRate: 0.85,
		Fee:          0.85,
		Status:       "pending",
	}

	orderID := uuid.New()
	createdAt := time.Now()
	updatedAt := time.Now()

	mock.ExpectQuery(`INSERT INTO economy.currency_exchange_orders`).
		WithArgs(playerID, "instant", "USD", "EUR", 100.0, 85.0, 0.85, 0.85, "pending", nil).
		WillReturnRows(sqlmock.NewRows([]string{"id", "created_at", "updated_at"}).
			AddRow(orderID, createdAt, updatedAt))

	createdOrder, err := repo.CreateOrder(context.Background(), order)

	assert.NoError(t, err)
	assert.NotNil(t, createdOrder)
	assert.Equal(t, orderID, createdOrder.ID)
	assert.Equal(t, playerID, createdOrder.PlayerID)

	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestCurrencyExchangeRepository_GetOrder(t *testing.T) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := NewCurrencyExchangeRepository(db)

	orderID := uuid.New()
	playerID := uuid.New()
	createdAt := time.Now()
	updatedAt := time.Now()

	row := sqlmock.NewRows([]string{"id", "player_id", "order_type", "from_currency", "to_currency", "from_amount", "to_amount", "exchange_rate", "fee", "status", "created_at", "updated_at", "filled_at", "expires_at"}).
		AddRow(orderID, playerID, "instant", "USD", "EUR", 100.0, 85.0, 0.85, 0.85, "filled", createdAt, updatedAt, sql.NullTime{Valid: true, Time: updatedAt}, sql.NullTime{Valid: false})

	mock.ExpectQuery(`SELECT id, player_id, order_type, from_currency, to_currency, from_amount, to_amount, exchange_rate, fee, status, created_at, updated_at, filled_at, expires_at FROM economy.currency_exchange_orders WHERE id = \$1`).
		WithArgs(orderID).
		WillReturnRows(row)

	order, err := repo.GetOrder(context.Background(), orderID)

	assert.NoError(t, err)
	assert.NotNil(t, order)
	assert.Equal(t, orderID, order.ID)
	assert.Equal(t, playerID, order.PlayerID)
	assert.Equal(t, "instant", order.OrderType)
	assert.Equal(t, "USD", order.FromCurrency)
	assert.Equal(t, "EUR", order.ToCurrency)
	assert.Equal(t, 100.0, order.FromAmount)
	assert.Equal(t, 85.0, order.ToAmount)
	assert.Equal(t, 0.85, order.ExchangeRate)
	assert.Equal(t, 0.85, order.Fee)
	assert.Equal(t, "filled", order.Status)
	assert.NotNil(t, order.FilledAt)

	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestCurrencyExchangeRepository_UpdateOrderStatus(t *testing.T) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := NewCurrencyExchangeRepository(db)

	orderID := uuid.New()

	mock.ExpectExec(`UPDATE economy.currency_exchange_orders SET status = \$1, updated_at = \$2 WHERE id = \$3`).
		WithArgs("cancelled", sqlmock.AnyArg(), orderID).
		WillReturnResult(sqlmock.NewResult(1, 1))

	err = repo.UpdateOrderStatus(context.Background(), orderID, "cancelled")

	assert.NoError(t, err)

	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestCurrencyExchangeRepository_CreateTrade(t *testing.T) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := NewCurrencyExchangeRepository(db)

	trade := &models.CurrencyExchangeTrade{
		OrderID:      uuid.New(),
		PlayerID:     uuid.New(),
		FromCurrency: "USD",
		ToCurrency:   "EUR",
		FromAmount:   100.0,
		ToAmount:     85.0,
		ExchangeRate: 0.85,
		Fee:          0.85,
		ExecutedAt:   time.Now(),
	}

	tradeID := uuid.New()

	mock.ExpectQuery(`INSERT INTO economy.currency_exchange_trades`).
		WithArgs(trade.OrderID, trade.PlayerID, "USD", "EUR", 100.0, 85.0, 0.85, 0.85, sqlmock.AnyArg()).
		WillReturnRows(sqlmock.NewRows([]string{"trade_id"}).AddRow(tradeID))

	createdTrade, err := repo.CreateTrade(context.Background(), trade)

	assert.NoError(t, err)
	assert.NotNil(t, createdTrade)
	assert.Equal(t, tradeID, createdTrade.TradeID)

	assert.NoError(t, mock.ExpectationsWereMet())
}
