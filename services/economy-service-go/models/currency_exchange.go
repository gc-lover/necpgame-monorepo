package models

import (
	"time"

	"github.com/google/uuid"
)

type CurrencyPair string

type ExchangeRate struct {
	Pair      CurrencyPair `json:"pair" db:"pair"`
	Bid       float64      `json:"bid" db:"bid"`
	Ask       float64      `json:"ask" db:"ask"`
	Spread    *float64     `json:"spread,omitempty" db:"spread"`
	Timestamp time.Time    `json:"timestamp" db:"timestamp"`
}

type ExchangeRatesResponse struct {
	Rates    []ExchangeRate `json:"rates"`
	UpdatedAt *time.Time    `json:"updated_at,omitempty"`
}

type QuoteRequest struct {
	FromCurrency string  `json:"from_currency"`
	ToCurrency   string  `json:"to_currency"`
	Amount       float64 `json:"amount"`
}

type Quote struct {
	FromCurrency string    `json:"from_currency"`
	ToCurrency   string    `json:"to_currency"`
	FromAmount   float64   `json:"from_amount"`
	ToAmount     float64   `json:"to_amount"`
	ExchangeRate float64   `json:"exchange_rate"`
	Fee          float64   `json:"fee"`
	ExpiresAt    time.Time `json:"expires_at"`
}

type OrderStatus string

const (
	OrderStatusPending   OrderStatus = "pending"
	OrderStatusActive    OrderStatus = "active"
	OrderStatusFilled    OrderStatus = "filled"
	OrderStatusCancelled OrderStatus = "cancelled"
	OrderStatusExpired   OrderStatus = "expired"
)

type ExchangeOrder struct {
	OrderID      uuid.UUID   `json:"order_id" db:"order_id"`
	PlayerID     uuid.UUID   `json:"player_id" db:"player_id"`
	OrderType    string      `json:"order_type" db:"order_type"`
	FromCurrency string      `json:"from_currency" db:"from_currency"`
	ToCurrency   string      `json:"to_currency" db:"to_currency"`
	FromAmount   float64     `json:"from_amount" db:"from_amount"`
	ToAmount     float64     `json:"to_amount" db:"to_amount"`
	ExchangeRate *float64    `json:"exchange_rate,omitempty" db:"exchange_rate"`
	Fee          float64     `json:"fee" db:"fee"`
	Status       OrderStatus `json:"status" db:"status"`
	CreatedAt    time.Time   `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time   `json:"updated_at" db:"updated_at"`
	FilledAt     *time.Time  `json:"filled_at,omitempty" db:"filled_at"`
	ExpiresAt    *time.Time  `json:"expires_at,omitempty" db:"expires_at"`
}

type InstantExchangeRequest struct {
	FromCurrency string  `json:"from_currency"`
	ToCurrency   string  `json:"to_currency"`
	Amount       float64 `json:"amount"`
}

type LimitOrderRequest struct {
	FromCurrency string     `json:"from_currency"`
	ToCurrency   string     `json:"to_currency"`
	FromAmount   float64    `json:"from_amount"`
	TargetRate   float64    `json:"target_rate"`
	ExpiresAt    *time.Time `json:"expires_at,omitempty"`
}

type OrderListResponse struct {
	Orders    []ExchangeOrder `json:"orders"`
	Total     int             `json:"total"`
	Limit     int             `json:"limit"`
	Offset    int             `json:"offset"`
}

type Trade struct {
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

type CurrencyExchangeTradeListResponse struct {
	Trades []Trade `json:"trades"`
	Total  int     `json:"total"`
	Limit  int     `json:"limit"`
	Offset int     `json:"offset"`
}

