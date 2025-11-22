package models

import (
	"time"

	"github.com/google/uuid"
)

type TradeStatus string

const (
	TradeStatusPending    TradeStatus = "pending"
	TradeStatusActive     TradeStatus = "active"
	TradeStatusConfirmed  TradeStatus = "confirmed"
	TradeStatusCompleted  TradeStatus = "completed"
	TradeStatusCancelled  TradeStatus = "cancelled"
	TradeStatusExpired    TradeStatus = "expired"
)

type TradeOffer struct {
	Items    []map[string]interface{} `json:"items"`
	Currency map[string]int           `json:"currency"`
}

type TradeSession struct {
	ID              uuid.UUID              `json:"id" db:"id"`
	InitiatorID     uuid.UUID              `json:"initiator_id" db:"initiator_id"`
	RecipientID     uuid.UUID              `json:"recipient_id" db:"recipient_id"`
	InitiatorOffer  TradeOffer              `json:"initiator_offer" db:"initiator_offer"`
	RecipientOffer  TradeOffer              `json:"recipient_offer" db:"recipient_offer"`
	InitiatorConfirmed bool                `json:"initiator_confirmed" db:"initiator_confirmed"`
	RecipientConfirmed  bool               `json:"recipient_confirmed" db:"recipient_confirmed"`
	Status          TradeStatus            `json:"status" db:"status"`
	ZoneID         *uuid.UUID              `json:"zone_id,omitempty" db:"zone_id"`
	CreatedAt      time.Time               `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time               `json:"updated_at" db:"updated_at"`
	ExpiresAt      time.Time               `json:"expires_at" db:"expires_at"`
	CompletedAt    *time.Time              `json:"completed_at,omitempty" db:"completed_at"`
}

type TradeHistory struct {
	ID              uuid.UUID              `json:"id" db:"id"`
	TradeSessionID  uuid.UUID              `json:"trade_session_id" db:"trade_session_id"`
	InitiatorID     uuid.UUID              `json:"initiator_id" db:"initiator_id"`
	RecipientID     uuid.UUID              `json:"recipient_id" db:"recipient_id"`
	InitiatorOffer  TradeOffer              `json:"initiator_offer" db:"initiator_offer"`
	RecipientOffer  TradeOffer              `json:"recipient_offer" db:"recipient_offer"`
	Status          TradeStatus            `json:"status" db:"status"`
	ZoneID         *uuid.UUID              `json:"zone_id,omitempty" db:"zone_id"`
	CreatedAt      time.Time               `json:"created_at" db:"created_at"`
	CompletedAt    time.Time               `json:"completed_at" db:"completed_at"`
}

type CreateTradeRequest struct {
	RecipientID    uuid.UUID   `json:"recipient_id"`
	ZoneID        *uuid.UUID  `json:"zone_id,omitempty"`
}

type UpdateTradeOfferRequest struct {
	Items    []map[string]interface{} `json:"items,omitempty"`
	Currency map[string]int           `json:"currency,omitempty"`
}

type TradeListResponse struct {
	Trades []TradeSession `json:"trades"`
	Total  int            `json:"total"`
}

type TradeHistoryListResponse struct {
	History []TradeHistory `json:"history"`
	Total   int            `json:"total"`
}

