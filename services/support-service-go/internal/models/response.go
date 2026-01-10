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
	IsInternal bool       `json:"is_internal" db:"is_internal"`
	IsPublic   bool       `json:"is_public" db:"is_public"`
	CreatedAt  time.Time  `json:"created_at" db:"created_at"`
	Attachments []Attachment `json:"attachments,omitempty" db:"-"`
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
	Pagination PaginationInfo   `json:"pagination"`
}

// PaginationInfo represents pagination information
type PaginationInfo struct {
	Page       int `json:"page"`
	Limit      int `json:"limit"`
	Total      int `json:"total"`
	TotalPages int `json:"total_pages"`
}

// Attachment represents an attachment
type Attachment struct {
	ID          uuid.UUID `json:"id" db:"id"`
	Filename    string    `json:"filename" db:"filename"`
	ContentType string    `json:"content_type" db:"content_type"`
	Size        int64     `json:"size" db:"size"`
	Data        []byte    `json:"data" db:"data"`
	URL         string    `json:"url" db:"url"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
}

// QueueFilter represents filters for ticket queue
type QueueFilter struct {
	Priority *TicketPriority `json:"priority,omitempty"`
}

// QueueStats represents statistics for ticket queue
type QueueStats struct {
	TotalWaiting int `json:"total_waiting"`
	UrgentCount  int `json:"urgent_count"`
	HighCount    int `json:"high_count"`
	NormalCount  int `json:"normal_count"`
	LowCount     int `json:"low_count"`
}






