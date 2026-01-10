// Trading Service Data Models
// Issue: #2260 - Trading Service Implementation
// Agent: Backend Agent
package models

import (
	"time"

	"github.com/google/uuid"
)

// TradeSession represents a trading session between two players
// PERFORMANCE: Struct field alignment optimized (large → small types)
type TradeSession struct {
	// Large types first (16 bytes - UUID)
	ID             uuid.UUID `json:"id" db:"id"`
	InitiatorID    uuid.UUID `json:"initiator_id" db:"initiator_id"`
	ParticipantID  uuid.UUID `json:"participant_id" db:"participant_id"`

	// Strings (24 bytes pointers)
	Status         string    `json:"status" db:"status"`
	CurrencyType   string    `json:"currency_type" db:"currency_type"`

	// Integers (8 bytes)
	TotalValue     int64     `json:"total_value" db:"total_value"`
	CreatedAt      time.Time `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time `json:"updated_at" db:"updated_at"`
	ExpiresAt      time.Time `json:"expires_at" db:"expires_at"`

	// Arrays (24 bytes)
	InitiatorItems   []TradeItem `json:"initiator_items" db:"initiator_items"`
	ParticipantItems []TradeItem `json:"participant_items" db:"participant_items"`

	// Booleans (1 byte)
	IsActive       bool      `json:"is_active" db:"is_active"`
}

// TradeItem represents an item in a trade
type TradeItem struct {
	ID          uuid.UUID `json:"id" db:"id"`
	ItemID      uuid.UUID `json:"item_id" db:"item_id"`
	Quantity    int32     `json:"quantity" db:"quantity"`
	Value       int64     `json:"value" db:"value"`
	IsLocked    bool      `json:"is_locked" db:"is_locked"`
}

// TradeOffer represents an offer within a trade session
type TradeOffer struct {
	ID            uuid.UUID  `json:"id" db:"id"`
	SessionID     uuid.UUID  `json:"session_id" db:"session_id"`
	PlayerID      uuid.UUID  `json:"player_id" db:"player_id"`
	OfferType     string     `json:"offer_type" db:"offer_type"`
	Items         []TradeItem `json:"items" db:"items"`
	CurrencyAmount int64      `json:"currency_amount" db:"currency_amount"`
	Status        string     `json:"status" db:"status"`
	CreatedAt     time.Time  `json:"created_at" db:"created_at"`
	ExpiresAt     time.Time  `json:"expires_at" db:"expires_at"`
}

// TradeTransaction represents a completed trade transaction
type TradeTransaction struct {
	ID             uuid.UUID `json:"id" db:"id"`
	SessionID      uuid.UUID `json:"session_id" db:"session_id"`
	BuyerID        uuid.UUID `json:"buyer_id" db:"buyer_id"`
	SellerID       uuid.UUID `json:"seller_id" db:"seller_id"`
	ItemID         uuid.UUID `json:"item_id" db:"item_id"`
	Quantity       int32     `json:"quantity" db:"quantity"`
	TotalPrice     int64     `json:"total_price" db:"total_price"`
	CurrencyType   string    `json:"currency_type" db:"currency_type"`
	TransactionFee int64     `json:"transaction_fee" db:"transaction_fee"`
	Status         string    `json:"status" db:"status"`
	ExecutedAt     time.Time `json:"executed_at" db:"executed_at"`
}

// TradeSessionStatus represents the status of a trade session
type TradeSessionStatus string

const (
	StatusPending   TradeSessionStatus = "pending"
	StatusActive    TradeSessionStatus = "active"
	StatusCompleted TradeSessionStatus = "completed"
	StatusCancelled TradeSessionStatus = "cancelled"
	StatusExpired   TradeSessionStatus = "expired"
)

// TradeOfferStatus represents the status of a trade offer
type TradeOfferStatus string

const (
	OfferStatusPending   TradeOfferStatus = "pending"
	OfferStatusAccepted  TradeOfferStatus = "accepted"
	OfferStatusRejected  TradeOfferStatus = "rejected"
	OfferStatusExpired   TradeOfferStatus = "expired"
)

// TradeTransactionStatus represents the status of a trade transaction
type TradeTransactionStatus string

const (
	TxStatusPending   TradeTransactionStatus = "pending"
	TxStatusCompleted TradeTransactionStatus = "completed"
	TxStatusFailed    TradeTransactionStatus = "failed"
	TxStatusRefunded  TradeTransactionStatus = "refunded"
)