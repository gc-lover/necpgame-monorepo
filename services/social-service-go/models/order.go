// Package models SQL queries use prepared statements with placeholders ($1, $2, ?) for safety
package models

import (
	"time"

	"github.com/google/uuid"
)

type OrderType string

const (
	OrderTypeCombat OrderType = "combat"
)

type OrderStatus string

const (
	OrderStatusOpen       OrderStatus = "open"
	OrderStatusAccepted   OrderStatus = "accepted"
	OrderStatusInProgress OrderStatus = "in_progress"
	OrderStatusCompleted  OrderStatus = "completed"
	OrderStatusCancelled  OrderStatus = "cancelled"
)

type PlayerOrder struct {
	ID           uuid.UUID              `json:"id" db:"id"`
	CustomerID   uuid.UUID              `json:"customer_id" db:"customer_id"`
	ExecutorID   *uuid.UUID             `json:"executor_id,omitempty" db:"executor_id"`
	OrderType    OrderType              `json:"order_type" db:"order_type"`
	Title        string                 `json:"title" db:"title"`
	Description  string                 `json:"description" db:"description"`
	Status       OrderStatus            `json:"status" db:"status"`
	Reward       map[string]interface{} `json:"reward,omitempty" db:"reward"`
	Requirements map[string]interface{} `json:"requirements,omitempty" db:"requirements"`
	Deadline     *time.Time             `json:"deadline,omitempty" db:"deadline"`
	CreatedAt    time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time              `json:"updated_at" db:"updated_at"`
	CompletedAt  *time.Time             `json:"completed_at,omitempty" db:"completed_at"`
}

type CreatePlayerOrderRequest struct {
	OrderType    OrderType              `json:"order_type"`
	Title        string                 `json:"title"`
	Description  string                 `json:"description"`
	Reward       map[string]interface{} `json:"reward"`
	Requirements map[string]interface{} `json:"requirements,omitempty"`
	Deadline     *time.Time             `json:"deadline,omitempty"`
}

type PlayerOrdersResponse struct {
	Orders []PlayerOrder `json:"orders"`
	Total  int           `json:"total"`
}

type CompletePlayerOrderRequest struct {
	Success  bool     `json:"success"`
	Evidence []string `json:"evidence,omitempty"`
}

type ReviewPlayerOrderRequest struct {
	Rating     int       `json:"rating"`
	Comment    string    `json:"comment,omitempty"`
	ExecutorID uuid.UUID `json:"executor_id"`
}

type PlayerOrderReview struct {
	ID         uuid.UUID `json:"id" db:"id"`
	OrderID    uuid.UUID `json:"order_id" db:"order_id"`
	ReviewerID uuid.UUID `json:"reviewer_id" db:"reviewer_id"`
	ExecutorID uuid.UUID `json:"executor_id" db:"executor_id"`
	Rating     int       `json:"rating" db:"rating"`
	Comment    string    `json:"comment" db:"comment"`
	CreatedAt  time.Time `json:"created_at" db:"created_at"`
}
