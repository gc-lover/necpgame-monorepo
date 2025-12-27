// Order models for player orders system
// Issue: #140875791

package models

import (
	"time"

	"github.com/google/uuid"
)

// Order represents a player-created order/contract
type Order struct {
	ID            uuid.UUID    `json:"id" db:"id"`
	CreatorID     uuid.UUID    `json:"creator_id" db:"creator_id"`
	Title         string       `json:"title" db:"title"`
	Description   string       `json:"description" db:"description"`
	OrderType     OrderType    `json:"order_type" db:"order_type"`
	Status        OrderStatus  `json:"status" db:"status"`
	Reward        OrderReward  `json:"reward" db:"reward"`
	Requirements  OrderRequirements `json:"requirements" db:"requirements"`
	RegionID      string       `json:"region_id" db:"region_id"`
	ExpiresAt     *time.Time   `json:"expires_at" db:"expires_at"`
	AcceptedBy    *uuid.UUID   `json:"accepted_by" db:"accepted_by"`
	CompletedAt   *time.Time   `json:"completed_at" db:"completed_at"`
	CreatedAt     time.Time    `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time    `json:"updated_at" db:"updated_at"`
}

// OrderType defines the type of order
type OrderType string

const (
	OrderTypeAssassination OrderType = "assassination"
	OrderTypeProtection    OrderType = "protection"
	OrderTypeEspionage     OrderType = "espionage"
	OrderTypeTransport     OrderType = "transport"
	OrderTypeCrafting      OrderType = "crafting"
	OrderTypeInvestigation OrderType = "investigation"
	OrderTypeCollection    OrderType = "collection"
	OrderTypeOther         OrderType = "other"
)

// OrderStatus represents the current status of an order
type OrderStatus string

const (
	OrderStatusOpen       OrderStatus = "open"
	OrderStatusAccepted   OrderStatus = "accepted"
	OrderStatusInProgress OrderStatus = "in_progress"
	OrderStatusCompleted  OrderStatus = "completed"
	OrderStatusExpired    OrderStatus = "expired"
	OrderStatusCancelled  OrderStatus = "cancelled"
)

// OrderReward represents the reward for completing an order
type OrderReward struct {
	Currency  int                    `json:"currency"`
	Items     []RewardItem           `json:"items"`
	Reputation map[string]int        `json:"reputation"` // faction -> reputation change
	Bonuses   map[string]interface{} `json:"bonuses"`    // additional bonuses
}

// RewardItem represents an item reward
type RewardItem struct {
	ItemID   uuid.UUID `json:"item_id"`
	Name     string    `json:"name"`
	Quantity int       `json:"quantity"`
	Rarity   string    `json:"rarity"`
}

// OrderRequirements represents the requirements to accept/complete an order
type OrderRequirements struct {
	MinLevel      int              `json:"min_level"`
	MaxLevel      int              `json:"max_level"`
	RequiredSkills []string        `json:"required_skills"`
	RequiredItems  []RequiredItem  `json:"required_items"`
	RequiredReputation map[string]int `json:"required_reputation"` // faction -> min reputation
	TimeLimit     *time.Duration   `json:"time_limit"`
}

// RequiredItem represents an item required for an order
type RequiredItem struct {
	ItemID   uuid.UUID `json:"item_id"`
	Name     string    `json:"name"`
	Quantity int       `json:"quantity"`
}

// OrderApplication represents an application to accept an order
type OrderApplication struct {
	ID         uuid.UUID         `json:"id" db:"id"`
	OrderID    uuid.UUID         `json:"order_id" db:"order_id"`
	ApplicantID uuid.UUID        `json:"applicant_id" db:"applicant_id"`
	Message    string            `json:"message" db:"message"`
	Bid        *OrderReward      `json:"bid" db:"bid"` // Counter-offer
	Status     ApplicationStatus `json:"status" db:"status"`
	AppliedAt  time.Time         `json:"applied_at" db:"applied_at"`
}

// ApplicationStatus represents the status of an order application
type ApplicationStatus string

const (
	ApplicationStatusPending   ApplicationStatus = "pending"
	ApplicationStatusAccepted  ApplicationStatus = "accepted"
	ApplicationStatusRejected  ApplicationStatus = "rejected"
	ApplicationStatusWithdrawn ApplicationStatus = "withdrawn"
)

// OrderBoard represents a collection of orders for a region
type OrderBoard struct {
	RegionID     string  `json:"region_id"`
	Orders       []Order `json:"orders"`
	TotalOrders  int     `json:"total_orders"`
	ActiveOrders int     `json:"active_orders"`
	LastUpdated  time.Time `json:"last_updated"`
}

// OrderStats represents statistics for orders
type OrderStats struct {
	RegionID         string    `json:"region_id"`
	TotalOrders      int       `json:"total_orders"`
	CompletedOrders  int       `json:"completed_orders"`
	AverageReward    float64   `json:"average_reward"`
	PopularOrderTypes []OrderTypeCount `json:"popular_order_types"`
	LastCalculated   time.Time `json:"last_calculated"`
}

// OrderTypeCount represents count of orders by type
type OrderTypeCount struct {
	OrderType OrderType `json:"order_type"`
	Count     int       `json:"count"`
}
