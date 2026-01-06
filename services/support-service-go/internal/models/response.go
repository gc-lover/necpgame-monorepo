package models

import (
	"time"

	"github.com/google/uuid"
)

// AuthorType represents the type of response author
type AuthorType string

const (
	AuthorTypeCustomer AuthorType = "customer"
	AuthorTypeAgent    AuthorType = "agent"
	AuthorTypeSystem   AuthorType = "system"
)

// TicketResponse represents a response to a support ticket
type TicketResponse struct {
	ID         uuid.UUID  `json:"id" db:"id"`
	TicketID   uuid.UUID  `json:"ticket_id" db:"ticket_id"`
	AuthorID   uuid.UUID  `json:"author_id" db:"author_id"`
	AuthorType AuthorType `json:"author_type" db:"author_type"`
	Content    string     `json:"content" db:"content"`
	IsPublic   bool       `json:"is_public" db:"is_public"`
	CreatedAt  time.Time  `json:"created_at" db:"created_at"`
}

// AddResponseRequest represents a request to add a response to a ticket
type AddResponseRequest struct {
	Content     string                    `json:"content"`
	IsInternal  bool                      `json:"is_internal"`
	Attachments []ResponseAttachment      `json:"attachments,omitempty"`
}

// ResponseAttachment represents an attachment to a response
type ResponseAttachment struct {
	ID          uuid.UUID `json:"id" db:"id"`
	ResponseID  uuid.UUID `json:"response_id" db:"response_id"`
	Filename    string    `json:"filename" db:"filename"`
	ContentType string    `json:"content_type" db:"content_type"`
	Size        int64     `json:"size" db:"size"`
	Data        []byte    `json:"data" db:"data"`
	URL         string    `json:"url" db:"url"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
}

// TicketResponseListResponse represents a paginated list of ticket responses
type TicketResponseListResponse struct {
	Responses  []TicketResponse `json:"responses"`
	Pagination struct {
		Page       int `json:"page"`
		Limit      int `json:"limit"`
		Total      int `json:"total"`
		TotalPages int `json:"total_pages"`
	} `json:"pagination"`
}


