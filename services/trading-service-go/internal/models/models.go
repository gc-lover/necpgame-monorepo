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

// TradeOrder represents a trade order (buy/sell order)
// PERFORMANCE: Struct field alignment optimized for order book operations
type TradeOrder struct {
	// Large types first (16 bytes - UUID)
	ID        uuid.UUID `json:"id" db:"id"`
	PlayerID  uuid.UUID `json:"player_id" db:"player_id"`
	ItemID    uuid.UUID `json:"item_id" db:"item_id"`

	// Strings (24 bytes pointers)
	OrderType   string    `json:"order_type" db:"order_type"`     // "buy" or "sell"
	OrderMode   string    `json:"order_mode" db:"order_mode"`     // "limit", "market", "stop"
	ItemName    string    `json:"item_name" db:"item_name"`
	CurrencyType string    `json:"currency_type" db:"currency_type"`

	// Integers (8 bytes)
	Quantity     int32     `json:"quantity" db:"quantity"`
	Price        int64     `json:"price" db:"price"`              // Price per unit
	MinQuantity  int32     `json:"min_quantity" db:"min_quantity"`
	MaxQuantity  int32     `json:"max_quantity" db:"max_quantity"`
	FilledQuantity int32   `json:"filled_quantity" db:"filled_quantity"`

	// Time fields (24 bytes)
	CreatedAt    time.Time `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time `json:"updated_at" db:"updated_at"`
	ExpiresAt    time.Time `json:"expires_at" db:"expires_at"`

	// Booleans (1 byte)
	IsActive     bool      `json:"is_active" db:"is_active"`
	IsPartial    bool      `json:"is_partial" db:"is_partial"`
}

// TradeContract represents a long-term trading contract
// PERFORMANCE: Struct field alignment optimized for contract management
type TradeContract struct {
	// Large types first (16 bytes - UUID)
	ID          uuid.UUID `json:"id" db:"id"`
	SellerID    uuid.UUID `json:"seller_id" db:"seller_id"`
	BuyerID     uuid.UUID `json:"buyer_id" db:"buyer_id"`

	// Strings (24 bytes pointers)
	ContractType string    `json:"contract_type" db:"contract_type"` // "supply", "exclusive", "bulk"
	Status       string    `json:"status" db:"status"`
	ItemName     string    `json:"item_name" db:"item_name"`

	// Integers (8 bytes)
	TotalQuantity  int32     `json:"total_quantity" db:"total_quantity"`
	DeliveredQuantity int32  `json:"delivered_quantity" db:"delivered_quantity"`
	UnitPrice      int64     `json:"unit_price" db:"unit_price"`
	EscrowAmount   int64     `json:"escrow_amount" db:"escrow_amount"`

	// Time fields (24 bytes)
	CreatedAt      time.Time `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time `json:"updated_at" db:"updated_at"`
	DeliveryDeadline time.Time `json:"delivery_deadline" db:"delivery_deadline"`
	ExpiresAt      time.Time `json:"expires_at" db:"expires_at"`

	// Arrays (24 bytes)
	Deliveries     []ContractDelivery `json:"deliveries" db:"deliveries"`

	// Booleans (1 byte)
	IsEscrowActive bool      `json:"is_escrow_active" db:"is_escrow_active"`
	IsCompleted    bool      `json:"is_completed" db:"is_completed"`
}

// ContractDelivery represents a delivery within a contract
type ContractDelivery struct {
	ID            uuid.UUID `json:"id" db:"id"`
	ContractID    uuid.UUID `json:"contract_id" db:"contract_id"`
	Quantity      int32     `json:"quantity" db:"quantity"`
	DeliveredAt   time.Time `json:"delivered_at" db:"delivered_at"`
	Status        string    `json:"status" db:"status"` // "pending", "delivered", "accepted", "rejected"
}

// Auction represents an auction for rare items
// PERFORMANCE: Struct field alignment optimized for auction operations
type Auction struct {
	// Large types first (16 bytes - UUID)
	ID          uuid.UUID `json:"id" db:"id"`
	SellerID    uuid.UUID `json:"seller_id" db:"seller_id"`
	ItemID      uuid.UUID `json:"item_id" db:"item_id"`

	// Strings (24 bytes pointers)
	ItemName      string    `json:"item_name" db:"item_name"`
	AuctionType   string    `json:"auction_type" db:"auction_type"` // "english", "dutch", "sealed"
	Status        string    `json:"status" db:"status"`

	// Integers (8 bytes)
	StartingPrice int64     `json:"starting_price" db:"starting_price"`
	CurrentPrice  int64     `json:"current_price" db:"current_price"`
	ReservePrice  int64     `json:"reserve_price" db:"reserve_price"`
	Quantity      int32     `json:"quantity" db:"quantity"`

	// Time fields (24 bytes)
	CreatedAt     time.Time `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time `json:"updated_at" db:"updated_at"`
	EndsAt        time.Time `json:"ends_at" db:"ends_at"`

	// Arrays (24 bytes)
	Bids          []AuctionBid `json:"bids" db:"bids"`

	// Winner info
	WinnerID      uuid.UUID `json:"winner_id" db:"winner_id"`
	FinalPrice    int64     `json:"final_price" db:"final_price"`
}

// AuctionBid represents a bid in an auction
type AuctionBid struct {
	ID         uuid.UUID `json:"id" db:"id"`
	AuctionID  uuid.UUID `json:"auction_id" db:"auction_id"`
	BidderID   uuid.UUID `json:"bidder_id" db:"bidder_id"`
	Amount     int64     `json:"amount" db:"amount"`
	BidTime    time.Time `json:"bid_time" db:"bid_time"`
	IsWinning  bool      `json:"is_winning" db:"is_winning"`
}

// TradeOrderStatus represents the status of a trade order
type TradeOrderStatus string

const (
	OrderStatusActive    TradeOrderStatus = "active"
	OrderStatusFilled    TradeOrderStatus = "filled"
	OrderStatusCancelled TradeOrderStatus = "cancelled"
	OrderStatusExpired   TradeOrderStatus = "expired"
	OrderStatusPartial   TradeOrderStatus = "partial"
)

// TradeContractStatus represents the status of a trade contract
type TradeContractStatus string

const (
	ContractStatusActive     TradeContractStatus = "active"
	ContractStatusCompleted  TradeContractStatus = "completed"
	ContractStatusCancelled  TradeContractStatus = "cancelled"
	ContractStatusBreached   TradeContractStatus = "breached"
	ContractStatusDisputed   TradeContractStatus = "disputed"
)

// AuctionStatus represents the status of an auction
type AuctionStatus string

const (
	AuctionStatusActive     AuctionStatus = "active"
	AuctionStatusEnded      AuctionStatus = "ended"
	AuctionStatusCancelled  AuctionStatus = "cancelled"
	AuctionStatusSold       AuctionStatus = "sold"
	AuctionStatusNoBids     AuctionStatus = "no_bids"
)