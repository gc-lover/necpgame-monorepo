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

// Contract-related types for trade contracts and orders system extension
type ContractType string

const (
	ContractTypeExchange  ContractType = "exchange"  // Обмен предметами
	ContractTypeDelivery  ContractType = "delivery"  // Доставка грузов
	ContractTypeCrafting  ContractType = "crafting"  // Крафтовые заказы
	ContractTypeService   ContractType = "service"   // Сервисные услуги
)

type ContractStatus string

const (
	ContractStatusDraft         ContractStatus = "draft"
	ContractStatusNegotiation   ContractStatus = "negotiation"
	ContractStatusEscrowPending ContractStatus = "escrow_pending"
	ContractStatusActive        ContractStatus = "active"
	ContractStatusCompleted     ContractStatus = "completed"
	ContractStatusCancelled     ContractStatus = "cancelled"
	ContractStatusDisputed      ContractStatus = "disputed"
	ContractStatusArbitrated    ContractStatus = "arbitrated"
)

type EscrowDeposit struct {
	DepositorID uuid.UUID         `json:"depositor_id" db:"depositor_id"`
	Items       []map[string]interface{} `json:"items,omitempty" db:"items"`
	Currency    map[string]int           `json:"currency,omitempty" db:"currency"`
	DepositedAt time.Time               `json:"deposited_at" db:"deposited_at"`
}

type ContractEscrow struct {
	ContractID      uuid.UUID         `json:"contract_id" db:"contract_id"`
	BuyerDeposit    *EscrowDeposit    `json:"buyer_deposit,omitempty" db:"buyer_deposit"`
	SellerDeposit   *EscrowDeposit    `json:"seller_deposit,omitempty" db:"seller_deposit"`
	Collateral      map[string]int    `json:"collateral,omitempty" db:"collateral"`
	TotalValue      int               `json:"total_value" db:"total_value"`
	ReleasedAt      *time.Time        `json:"released_at,omitempty" db:"released_at"`
}

type ContractDispute struct {
	ContractID     uuid.UUID    `json:"contract_id" db:"contract_id"`
	InitiatorID    uuid.UUID    `json:"initiator_id" db:"initiator_id"`
	Reason         string       `json:"reason" db:"reason"`
	Evidence       []string     `json:"evidence" db:"evidence"`
	ArbitratorID   *uuid.UUID   `json:"arbitrator_id,omitempty" db:"arbitrator_id"`
	Decision       string       `json:"decision,omitempty" db:"decision"`
	Penalty        map[string]int `json:"penalty,omitempty" db:"penalty"`
	ResolvedAt     *time.Time   `json:"resolved_at,omitempty" db:"resolved_at"`
}

type TradeContract struct {
	ID              uuid.UUID          `json:"id" db:"id"`
	Type            ContractType       `json:"type" db:"type"`
	BuyerID         uuid.UUID          `json:"buyer_id" db:"buyer_id"`
	SellerID        uuid.UUID          `json:"seller_id" db:"seller_id"`
	Title           string             `json:"title" db:"title"`
	Description     string             `json:"description" db:"description"`
	Terms           map[string]interface{} `json:"terms" db:"terms"`
	Status          ContractStatus     `json:"status" db:"status"`
	Escrow          *ContractEscrow    `json:"escrow,omitempty" db:"escrow"`
	Dispute         *ContractDispute   `json:"dispute,omitempty" db:"dispute"`
	ZoneID          *uuid.UUID         `json:"zone_id,omitempty" db:"zone_id"`
	CreatedAt       time.Time          `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time          `json:"updated_at" db:"updated_at"`
	Deadline        *time.Time         `json:"deadline,omitempty" db:"deadline"`
	CompletedAt     *time.Time         `json:"completed_at,omitempty" db:"completed_at"`
}

type ContractOffer struct {
	ContractID uuid.UUID `json:"contract_id"`
	Items      []map[string]interface{} `json:"items,omitempty"`
	Currency   map[string]int           `json:"currency,omitempty"`
	Services   map[string]interface{}   `json:"services,omitempty"`
}

type ContractNegotiation struct {
	ID          uuid.UUID     `json:"id" db:"id"`
	ContractID  uuid.UUID     `json:"contract_id" db:"contract_id"`
	OfferorID   uuid.UUID     `json:"offeror_id" db:"offeror_id"`
	Offer       ContractOffer `json:"offer" db:"offer"`
	Status      string        `json:"status" db:"status"` // "pending", "accepted", "rejected"
	CreatedAt   time.Time     `json:"created_at" db:"created_at"`
}

type CreateContractRequest struct {
	Type        ContractType `json:"type"`
	SellerID    uuid.UUID    `json:"seller_id"`
	Title       string       `json:"title"`
	Description string       `json:"description"`
	Terms       map[string]interface{} `json:"terms"`
	Deadline    *time.Time   `json:"deadline,omitempty"`
	ZoneID      *uuid.UUID   `json:"zone_id,omitempty"`
}

type UpdateContractTermsRequest struct {
	Terms    map[string]interface{} `json:"terms,omitempty"`
	Deadline *time.Time             `json:"deadline,omitempty"`
}

type DepositEscrowRequest struct {
	Items    []map[string]interface{} `json:"items,omitempty"`
	Currency map[string]int           `json:"currency,omitempty"`
}

type ContractDisputeRequest struct {
	Reason   string   `json:"reason"`
	Evidence []string `json:"evidence,omitempty"`
}

type ContractListResponse struct {
	Contracts []TradeContract `json:"contracts"`
	Total     int             `json:"total"`
}

type ContractHistoryResponse struct {
	ContractID uuid.UUID      `json:"contract_id"`
	Events     []ContractEvent `json:"events"`
}

type ContractEvent struct {
	ID         uuid.UUID              `json:"id" db:"id"`
	ContractID uuid.UUID              `json:"contract_id" db:"contract_id"`
	Type       string                 `json:"type" db:"type"`
	ActorID    uuid.UUID              `json:"actor_id" db:"actor_id"`
	Data       map[string]interface{} `json:"data" db:"data"`
	CreatedAt  time.Time              `json:"created_at" db:"created_at"`
}

