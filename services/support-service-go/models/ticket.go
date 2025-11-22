package models

import (
	"time"

	"github.com/google/uuid"
)

type TicketStatus string

const (
	TicketStatusOpen       TicketStatus = "open"
	TicketStatusAssigned   TicketStatus = "assigned"
	TicketStatusInProgress TicketStatus = "in_progress"
	TicketStatusWaiting    TicketStatus = "waiting"
	TicketStatusResolved   TicketStatus = "resolved"
	TicketStatusClosed     TicketStatus = "closed"
)

type TicketPriority string

const (
	TicketPriorityLow      TicketPriority = "low"
	TicketPriorityNormal   TicketPriority = "normal"
	TicketPriorityHigh     TicketPriority = "high"
	TicketPriorityCritical TicketPriority = "critical"
)

type TicketCategory string

const (
	TicketCategoryTechnical  TicketCategory = "technical"
	TicketCategoryBilling    TicketCategory = "billing"
	TicketCategoryGameplay   TicketCategory = "gameplay"
	TicketCategoryAccount    TicketCategory = "account"
	TicketCategoryBug        TicketCategory = "bug"
	TicketCategorySuggestion TicketCategory = "suggestion"
	TicketCategoryOther      TicketCategory = "other"
)

type TicketVisibility string

const (
	TicketVisibilityPublic  TicketVisibility = "public"
	TicketVisibilityPrivate TicketVisibility = "private"
)

type SupportTicket struct {
	ID              uuid.UUID        `json:"id" db:"id"`
	Number          string           `json:"number" db:"number"`
	PlayerID        uuid.UUID        `json:"player_id" db:"player_id"`
	Category        TicketCategory    `json:"category" db:"category"`
	Priority        TicketPriority    `json:"priority" db:"priority"`
	Status          TicketStatus      `json:"status" db:"status"`
	Subject         string           `json:"subject" db:"subject"`
	Description     string           `json:"description" db:"description"`
	AssignedAgentID *uuid.UUID       `json:"assigned_agent_id,omitempty" db:"assigned_agent_id"`
	CreatedAt       time.Time        `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time        `json:"updated_at" db:"updated_at"`
	ResolvedAt      *time.Time       `json:"resolved_at,omitempty" db:"resolved_at"`
	ClosedAt        *time.Time       `json:"closed_at,omitempty" db:"closed_at"`
	FirstResponseAt *time.Time       `json:"first_response_at,omitempty" db:"first_response_at"`
	SatisfactionRating *int          `json:"satisfaction_rating,omitempty" db:"satisfaction_rating"`
}

type TicketResponse struct {
	ID          uuid.UUID         `json:"id" db:"id"`
	TicketID    uuid.UUID         `json:"ticket_id" db:"ticket_id"`
	AuthorID    uuid.UUID         `json:"author_id" db:"author_id"`
	IsAgent     bool              `json:"is_agent" db:"is_agent"`
	Message     string            `json:"message" db:"message"`
	Attachments []map[string]interface{} `json:"attachments" db:"attachments"`
	Visibility  TicketVisibility  `json:"visibility" db:"visibility"`
	CreatedAt   time.Time         `json:"created_at" db:"created_at"`
}

type CreateTicketRequest struct {
	Category    TicketCategory `json:"category"`
	Subject     string         `json:"subject"`
	Description string         `json:"description"`
	Priority    *TicketPriority `json:"priority,omitempty"`
}

type UpdateTicketRequest struct {
	Category *TicketCategory `json:"category,omitempty"`
	Priority *TicketPriority `json:"priority,omitempty"`
	Status   *TicketStatus   `json:"status,omitempty"`
	Subject  *string         `json:"subject,omitempty"`
}

type AssignTicketRequest struct {
	AgentID uuid.UUID `json:"agent_id"`
}

type AddResponseRequest struct {
	Message     string                    `json:"message"`
	Attachments []map[string]interface{}  `json:"attachments,omitempty"`
	Visibility  TicketVisibility          `json:"visibility,omitempty"`
}

type RateTicketRequest struct {
	Rating int `json:"rating"`
}

type TicketListResponse struct {
	Tickets []SupportTicket `json:"tickets"`
	Total   int             `json:"total"`
}

type TicketDetailResponse struct {
	Ticket    SupportTicket   `json:"ticket"`
	Responses []TicketResponse `json:"responses"`
}

