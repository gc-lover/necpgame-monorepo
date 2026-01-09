package models

import (
	"time"

	"github.com/google/uuid"
)

// TicketStatus represents the status of a support ticket
type TicketStatus string

const (
	TicketStatusOpen           TicketStatus = "open"
	TicketStatusAssigned       TicketStatus = "assigned"
	TicketStatusInProgress     TicketStatus = "in_progress"
	TicketStatusPendingCustomer TicketStatus = "pending_customer"
	TicketStatusResolved       TicketStatus = "resolved"
	TicketStatusClosed         TicketStatus = "closed"
	TicketStatusCancelled      TicketStatus = "cancelled"
)

// TicketPriority represents the priority level of a ticket
type TicketPriority string

const (
	TicketPriorityLow      TicketPriority = "low"
	TicketPriorityNormal   TicketPriority = "normal"
	TicketPriorityHigh     TicketPriority = "high"
	TicketPriorityUrgent   TicketPriority = "urgent"
	TicketPriorityCritical TicketPriority = "critical"
)

// TicketCategory represents the category of a support ticket
type TicketCategory string

const (
	TicketCategoryTechnicalIssue TicketCategory = "technical_issue"
	TicketCategoryBilling        TicketCategory = "billing"
	TicketCategoryGameplay       TicketCategory = "gameplay"
	TicketCategoryAccount        TicketCategory = "account"
	TicketCategoryBugReport      TicketCategory = "bug_report"
	TicketCategoryFeatureRequest TicketCategory = "feature_request"
	TicketCategoryOther          TicketCategory = "other"
)

// Ticket represents a support ticket
type Ticket struct {
	ID           uuid.UUID      `json:"id" db:"id"`
	PlayerID     uuid.UUID      `json:"player_id" db:"player_id"`
	Title        string         `json:"title" db:"title"`
	Description  string         `json:"description" db:"description"`
	Category     TicketCategory `json:"category" db:"category"`
	Priority     TicketPriority `json:"priority" db:"priority"`
	Status       TicketStatus   `json:"status" db:"status"`
	AgentID      *uuid.UUID     `json:"agent_id,omitempty" db:"agent_id"`
	CreatedAt    time.Time      `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time      `json:"updated_at" db:"updated_at"`
	ClosedAt     *time.Time     `json:"closed_at,omitempty" db:"closed_at"`
	Tags         []string       `json:"tags" db:"tags"`
	SLADueDate   *time.Time     `json:"sla_due_date,omitempty" db:"sla_due_date"`
	SLAStatus    SLAStatus      `json:"sla_status" db:"sla_status"`
	ResponseCount int           `json:"response_count" db:"response_count"`
}

// CreateTicketRequest represents a request to create a new ticket
type CreateTicketRequest struct {
	PlayerID    uuid.UUID      `json:"player_id"`
	Title       string         `json:"title"`
	Description string         `json:"description"`
	Category    TicketCategory `json:"category"`
	Priority    TicketPriority `json:"priority"`
	Tags        []string       `json:"tags,omitempty"`
}

// UpdateTicketRequest represents a request to update a ticket
type UpdateTicketRequest struct {
	Title       *string         `json:"title,omitempty"`
	Description *string         `json:"description,omitempty"`
	Category    *TicketCategory `json:"category,omitempty"`
	Priority    *TicketPriority `json:"priority,omitempty"`
	Tags        []string        `json:"tags,omitempty"`
}

// AssignAgentRequest represents a request to assign an agent to a ticket
type AssignAgentRequest struct {
	AgentID uuid.UUID `json:"agent_id"`
}

// UpdateStatusRequest represents a request to update ticket status
type UpdateStatusRequest struct {
	Status  TicketStatus `json:"status"`
	Comment *string      `json:"comment,omitempty"`
}

// UpdatePriorityRequest represents a request to update ticket priority
type UpdatePriorityRequest struct {
	Priority TicketPriority `json:"priority"`
	Reason   *string        `json:"reason,omitempty"`
}

// RateTicketRequest represents a request to rate ticket resolution
type RateTicketRequest struct {
	Rating  int     `json:"rating"`
	Comment *string `json:"comment,omitempty"`
}

// TicketFilter represents filters for ticket listing
type TicketFilter struct {
	Status   *TicketStatus   `json:"status,omitempty"`
	Priority *TicketPriority `json:"priority,omitempty"`
	Category *TicketCategory `json:"category,omitempty"`
	AgentID  *uuid.UUID      `json:"agent_id,omitempty"`
}

// TicketListResponse represents a paginated list of tickets
type TicketListResponse struct {
	Tickets    []Ticket `json:"tickets"`
	Pagination struct {
		Page       int `json:"page"`
		Limit      int `json:"limit"`
		Total      int `json:"total"`
		TotalPages int `json:"total_pages"`
	} `json:"pagination"`
}






