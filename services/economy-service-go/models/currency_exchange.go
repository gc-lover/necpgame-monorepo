// Package models SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1443
// Currency Exchange models for economy service
package models

import (
	"time"

	"github.com/google/uuid"
)

// CurrencyExchangeRate represents a currency pair exchange rate
type CurrencyExchangeRate struct {
	Pair      string    `json:"pair" db:"pair"`
	Bid       float64   `json:"bid" db:"bid"`
	Ask       float64   `json:"ask" db:"ask"`
	Spread    float64   `json:"spread" db:"spread"`
	UpdatedAt time.Time `json:"updated_at" db:"updated_at"`
	IsActive  bool      `json:"is_active" db:"is_active"`
}

// CurrencyExchangeOrder represents a currency exchange order
type CurrencyExchangeOrder struct {
	ID           uuid.UUID  `json:"id" db:"id"`
	PlayerID     uuid.UUID  `json:"player_id" db:"player_id"`
	OrderType    string     `json:"order_type" db:"order_type"` // instant, limit
	FromCurrency string     `json:"from_currency" db:"from_currency"`
	ToCurrency   string     `json:"to_currency" db:"to_currency"`
	FromAmount   float64    `json:"from_amount" db:"from_amount"`
	ToAmount     float64    `json:"to_amount" db:"to_amount"`
	ExchangeRate float64    `json:"exchange_rate" db:"exchange_rate"`
	Fee          float64    `json:"fee" db:"fee"`
	Status       string     `json:"status" db:"status"` // pending, filled, cancelled, expired
	CreatedAt    time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time  `json:"updated_at" db:"updated_at"`
	FilledAt     *time.Time `json:"filled_at,omitempty" db:"filled_at"`
	ExpiresAt    *time.Time `json:"expires_at,omitempty" db:"expires_at"`
}

// CurrencyExchangeTrade represents a completed currency exchange trade
type CurrencyExchangeTrade struct {
	TradeID      uuid.UUID `json:"trade_id" db:"trade_id"`
	OrderID      uuid.UUID `json:"order_id" db:"order_id"`
	PlayerID     uuid.UUID `json:"player_id" db:"player_id"`
	FromCurrency string    `json:"from_currency" db:"from_currency"`
	ToCurrency   string    `json:"to_currency" db:"to_currency"`
	FromAmount   float64   `json:"from_amount" db:"from_amount"`
	ToAmount     float64   `json:"to_amount" db:"to_amount"`
	ExchangeRate float64   `json:"exchange_rate" db:"exchange_rate"`
	Fee          float64   `json:"fee" db:"fee"`
	ExecutedAt   time.Time `json:"executed_at" db:"executed_at"`
}

// CurrencyExchangeQuote represents a quote for currency exchange
type CurrencyExchangeQuote struct {
	FromCurrency string    `json:"from_currency"`
	ToCurrency   string    `json:"to_currency"`
	FromAmount   float64   `json:"from_amount"`
	ToAmount     float64   `json:"to_amount"`
	ExchangeRate float64   `json:"exchange_rate"`
	Fee          float64   `json:"fee"`
	ValidUntil   time.Time `json:"valid_until"`
}

// CreateInstantExchangeRequest represents request for instant exchange
type CreateInstantExchangeRequest struct {
	FromCurrency string  `json:"from_currency"`
	ToCurrency   string  `json:"to_currency"`
	FromAmount   float64 `json:"from_amount"`
}

// CreateLimitOrderRequest represents request for limit order
type CreateLimitOrderRequest struct {
	FromCurrency string     `json:"from_currency"`
	ToCurrency   string     `json:"to_currency"`
	FromAmount   float64    `json:"from_amount"`
	ExchangeRate float64    `json:"exchange_rate"`
	ExpiresAt    *time.Time `json:"expires_at,omitempty"`
}

// OrderFilter represents filter for listing orders
type OrderFilter struct {
	PlayerID     *uuid.UUID `json:"player_id,omitempty"`
	OrderType    *string    `json:"order_type,omitempty"`
	Status       *string    `json:"status,omitempty"`
	FromCurrency *string    `json:"from_currency,omitempty"`
	ToCurrency   *string    `json:"to_currency,omitempty"`
	Limit        int        `json:"limit"`
	Offset       int        `json:"offset"`
}

// TradeFilter represents filter for listing trades
type TradeFilter struct {
	PlayerID     *uuid.UUID `json:"player_id,omitempty"`
	FromCurrency *string    `json:"from_currency,omitempty"`
	ToCurrency   *string    `json:"to_currency,omitempty"`
	Limit        int        `json:"limit"`
	Offset       int        `json:"offset"`
}
