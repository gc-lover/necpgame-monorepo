package models

import (
	"time"

	"github.com/google/uuid"
)

type MailType string

type MailStatus string

type MailMessage struct {
	ID          uuid.UUID              `json:"id" db:"id"`
	SenderID    *uuid.UUID             `json:"sender_id,omitempty" db:"sender_id"`
	SenderName  string                 `json:"sender_name" db:"sender_name"`
	RecipientID uuid.UUID              `json:"recipient_id" db:"recipient_id"`
	Type        MailType               `json:"type" db:"type"`
	Subject     string                 `json:"subject" db:"subject"`
	Content     string                 `json:"content" db:"content"`
	Attachments map[string]interface{} `json:"attachments,omitempty" db:"attachments"`
	CODAmount   *int                   `json:"cod_amount,omitempty" db:"cod_amount"`
	Status      MailStatus             `json:"status" db:"status"`
	IsRead      bool                   `json:"is_read" db:"is_read"`
	IsClaimed   bool                   `json:"is_claimed" db:"is_claimed"`
	SentAt      time.Time              `json:"sent_at" db:"sent_at"`
	ReadAt      *time.Time             `json:"read_at,omitempty" db:"read_at"`
	ExpiresAt   *time.Time             `json:"expires_at,omitempty" db:"expires_at"`
	CreatedAt   time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time              `json:"updated_at" db:"updated_at"`
	DeletedAt   *time.Time             `json:"deleted_at,omitempty" db:"deleted_at"`
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
	Success  bool                   `json:"success"`
	Items    map[string]interface{} `json:"items,omitempty"`
	Currency map[string]int         `json:"currency,omitempty"`
}

type UnreadMailCountResponse struct {
	UnreadCount int `json:"unread_count"`
	TotalCount  int `json:"total_count"`
}

type MailAttachment struct {
	Type     string                 `json:"type"`
	ItemID   *string                `json:"item_id,omitempty"`
	Quantity *int                   `json:"quantity,omitempty"`
	Currency map[string]int         `json:"currency,omitempty"`
	Data     map[string]interface{} `json:"data,omitempty"`
}

type MailAttachmentsResponse struct {
	MailID      uuid.UUID        `json:"mail_id"`
	Attachments []MailAttachment `json:"attachments"`
	HasCOD      bool             `json:"has_cod"`
	CODAmount   *int             `json:"cod_amount,omitempty"`
}

type ExtendMailRequest struct {
	Days int `json:"days"`
}

type SendSystemMailRequest struct {
	RecipientID uuid.UUID              `json:"recipient_id"`
	Type        MailType               `json:"type"`
	Title       string                 `json:"title"`
	Content     string                 `json:"content"`
	Attachments map[string]interface{} `json:"attachments,omitempty"`
	ExpiresIn   *int                   `json:"expires_in,omitempty"`
}

type BroadcastSystemMailRequest struct {
	Type         MailType               `json:"type"`
	Title        string                 `json:"title"`
	Content      string                 `json:"content"`
	Attachments  map[string]interface{} `json:"attachments,omitempty"`
	RecipientIDs []uuid.UUID            `json:"recipient_ids,omitempty"`
	Filter       map[string]interface{} `json:"filter,omitempty"`
	ExpiresIn    *int                   `json:"expires_in,omitempty"`
}

type BroadcastResult struct {
	TotalSent        int      `json:"total_sent"`
	TotalFailed      int      `json:"total_failed"`
	FailedRecipients []string `json:"failed_recipients,omitempty"`
}
