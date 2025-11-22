package models

import (
	"time"

	"github.com/google/uuid"
)

type MailType string

const (
	MailTypePlayer    MailType = "player"
	MailTypeSystem    MailType = "system"
	MailTypeQuest     MailType = "quest"
	MailTypeAuction   MailType = "auction"
	MailTypeTrade     MailType = "trade"
)

type MailStatus string

const (
	MailStatusUnread   MailStatus = "unread"
	MailStatusRead     MailStatus = "read"
	MailStatusClaimed  MailStatus = "claimed"
	MailStatusExpired  MailStatus = "expired"
	MailStatusReturned MailStatus = "returned"
)

type MailMessage struct {
	ID            uuid.UUID              `json:"id" db:"id"`
	SenderID      *uuid.UUID             `json:"sender_id,omitempty" db:"sender_id"`
	SenderName    string                 `json:"sender_name" db:"sender_name"`
	RecipientID   uuid.UUID              `json:"recipient_id" db:"recipient_id"`
	Type          MailType               `json:"type" db:"type"`
	Subject       string                 `json:"subject" db:"subject"`
	Content       string                 `json:"content" db:"content"`
	Attachments   map[string]interface{} `json:"attachments,omitempty" db:"attachments"`
	CODAmount     *int                   `json:"cod_amount,omitempty" db:"cod_amount"`
	Status        MailStatus             `json:"status" db:"status"`
	IsRead        bool                   `json:"is_read" db:"is_read"`
	IsClaimed     bool                   `json:"is_claimed" db:"is_claimed"`
	SentAt        time.Time              `json:"sent_at" db:"sent_at"`
	ReadAt        *time.Time             `json:"read_at,omitempty" db:"read_at"`
	ExpiresAt     *time.Time             `json:"expires_at,omitempty" db:"expires_at"`
	CreatedAt     time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time              `json:"updated_at" db:"updated_at"`
	DeletedAt     *time.Time             `json:"deleted_at,omitempty" db:"deleted_at"`
}

type CreateMailRequest struct {
	RecipientID uuid.UUID              `json:"recipient_id"`
	Subject     string                 `json:"subject"`
	Content     string                 `json:"content"`
	Attachments map[string]interface{} `json:"attachments,omitempty"`
	CODAmount   *int                   `json:"cod_amount,omitempty"`
	ExpiresIn   *int                   `json:"expires_in,omitempty"`
}

type MailListResponse struct {
	Messages []MailMessage `json:"messages"`
	Total    int           `json:"total"`
	Unread   int           `json:"unread"`
}

type ClaimAttachmentRequest struct {
	MailID uuid.UUID `json:"mail_id"`
}

type ClaimAttachmentResponse struct {
	Success   bool                   `json:"success"`
	Items     map[string]interface{} `json:"items,omitempty"`
	Currency  map[string]int          `json:"currency,omitempty"`
}

