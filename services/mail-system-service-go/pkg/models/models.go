package models

import (
	"time"

	"github.com/google/uuid"
)

// Mail represents a mail message
type Mail struct {
	ID          uuid.UUID  `json:"mail_id" db:"id"`
	SenderID    uuid.UUID  `json:"sender_id" db:"sender_id"`
	RecipientID uuid.UUID  `json:"recipient_id" db:"recipient_id"`
	SenderName  string     `json:"sender_name" db:"sender_name"`
	Subject     string     `json:"subject" db:"subject"`
	Category    string     `json:"category" db:"category"`
	Priority    string     `json:"priority" db:"priority"`
	SentAt      time.Time  `json:"sent_at" db:"sent_at"`
	ReadAt      *time.Time `json:"read_at,omitempty" db:"read_at"`
	ExpiresAt   *time.Time `json:"expires_at,omitempty" db:"expires_at"`
	Folder      string     `json:"folder" db:"folder"`
	IsArchived  bool       `json:"is_archived" db:"is_archived"`
	IsDeleted   bool       `json:"is_deleted" db:"is_deleted"`
	Content     MailContent `json:"content" db:"content"`
	Attachments []Attachment `json:"attachments" db:"attachments"`
}

// MailContent represents the content of a mail
type MailContent struct {
	Text   string `json:"text" db:"text"`
	HTML   string `json:"html,omitempty" db:"html"`
	Format string `json:"format" db:"format"`
}

// Attachment represents a mail attachment
type Attachment struct {
	ID           uuid.UUID `json:"attachment_id" db:"id"`
	MailID       uuid.UUID `json:"mail_id" db:"mail_id"`
	Filename     string    `json:"filename" db:"filename"`
	ContentType  string    `json:"content_type" db:"content_type"`
	SizeBytes    int64     `json:"size_bytes" db:"size_bytes"`
	Data         []byte    `json:"-" db:"data"`
	DownloadURL  string    `json:"download_url,omitempty" db:"-"`
}

// MailSummary represents a summary of a mail for listing
type MailSummary struct {
	ID             uuid.UUID `json:"mail_id" db:"id"`
	SenderName     string    `json:"sender_name" db:"sender_name"`
	Subject        string    `json:"subject" db:"subject"`
	Category       string    `json:"category" db:"category"`
	Priority       string    `json:"priority" db:"priority"`
	SentAt         time.Time `json:"sent_at" db:"sent_at"`
	ExpiresAt      *time.Time `json:"expires_at,omitempty" db:"expires_at"`
	HasAttachments bool      `json:"has_attachments" db:"has_attachments"`
	IsRead         bool      `json:"is_read" db:"is_read"`
}

// SendMailRequest represents a request to send mail
type SendMailRequest struct {
	Subject         string            `json:"subject"`
	Category        string            `json:"category"`
	Priority        string            `json:"priority"`
	Content         MailContent       `json:"content"`
	RecipientIDs    []uuid.UUID       `json:"recipient_ids"`
	Attachments     []AttachmentUpload `json:"attachments,omitempty"`
	ExpiresInHours  int               `json:"expires_in_hours"`
}

// SendMailResponse represents the response after sending mail
type SendMailResponse struct {
	MailID           uuid.UUID `json:"mail_id"`
	SentAt           time.Time `json:"sent_at"`
	DeliveryStatus   string    `json:"delivery_status"`
	RecipientCount   int       `json:"recipient_count"`
}

// AttachmentUpload represents an attachment being uploaded
type AttachmentUpload struct {
	Data        []byte `json:"data"`
	Filename    string `json:"filename"`
	ContentType string `json:"content_type"`
	SizeBytes   int64  `json:"size_bytes"`
}

// BulkMailRequest represents a request to send bulk mail
type BulkMailRequest struct {
	Subject           string            `json:"subject"`
	Category          string            `json:"category"`
	Priority          string            `json:"priority"`
	ScheduledSend     *time.Time        `json:"scheduled_send,omitempty"`
	Content           MailContent       `json:"content"`
	RecipientCriteria RecipientCriteria `json:"recipient_criteria"`
	MaxRecipients     int               `json:"max_recipients"`
}

// RecipientCriteria defines criteria for bulk mail recipients
type RecipientCriteria struct {
	Regions               []string    `json:"regions,omitempty"`
	SpecificPlayerIDs     []uuid.UUID `json:"specific_player_ids,omitempty"`
	VIPStatus             *bool       `json:"vip_status,omitempty"`
	GuildMembers          *bool       `json:"guild_members,omitempty"`
	PlayerLevelMin        *int        `json:"player_level_min,omitempty"`
	PlayerLevelMax        *int        `json:"player_level_max,omitempty"`
	LastActiveWithinDays  *int        `json:"last_active_within_days,omitempty"`
}

// BulkMailResponse represents the response for bulk mail operations
type BulkMailResponse struct {
	OperationID        uuid.UUID `json:"operation_id"`
	EstimatedCompletion time.Time `json:"estimated_completion"`
	Status             string    `json:"status"`
	TotalRecipients    int       `json:"total_recipients"`
}

// SystemAnnouncementRequest represents a system-wide announcement
type SystemAnnouncementRequest struct {
	Subject          string      `json:"subject"`
	TargetAudience   string      `json:"target_audience"`
	Priority         string      `json:"priority"`
	ScheduledDelivery *time.Time `json:"scheduled_delivery,omitempty"`
	Content          MailContent `json:"content"`
	ExpiresInHours   int         `json:"expires_in_hours"`
}

// SystemAnnouncementResponse represents the response for system announcements
type SystemAnnouncementResponse struct {
	AnnouncementID     uuid.UUID `json:"announcement_id"`
	ScheduledDelivery  *time.Time `json:"scheduled_delivery,omitempty"`
	Priority           string    `json:"priority"`
	RecipientCount     int       `json:"recipient_count"`
}

// ReportRequest represents a mail moderation report
type ReportRequest struct {
	Reason      string `json:"reason"`
	Description string `json:"description,omitempty"`
	Severity    string `json:"severity"`
}

// Report represents a mail moderation report
type Report struct {
	ID        uuid.UUID `json:"report_id" db:"id"`
	MailID    uuid.UUID `json:"mail_id" db:"mail_id"`
	ReporterID uuid.UUID `json:"reporter_id" db:"reporter_id"`
	Reason    string    `json:"reason" db:"reason"`
	Description string  `json:"description" db:"description"`
	Severity  string    `json:"severity" db:"severity"`
	SubmittedAt time.Time `json:"submitted_at" db:"submitted_at"`
	Status    string    `json:"status" db:"status"`
}

// MailboxResponse represents a user's mailbox contents
type MailboxResponse struct {
	Pagination  PaginationInfo `json:"pagination"`
	Mails       []MailSummary  `json:"mails"`
	TotalCount  int            `json:"total_count"`
	UnreadCount int            `json:"unread_count"`
}

// PaginationInfo represents pagination information
type PaginationInfo struct {
	HasMore     bool `json:"has_more"`
	Offset      int  `json:"offset"`
	Limit       int  `json:"limit"`
	TotalItems  int  `json:"total_items"`
}

// AnalyticsResponse represents mail analytics data
type AnalyticsResponse struct {
	Timeframe            string             `json:"timeframe"`
	CategoryBreakdown    map[string]int     `json:"category_breakdown"`
	AverageResponseTime  float64            `json:"average_response_time"`
	DeliverySuccessRate  float64            `json:"delivery_success_rate"`
	TotalMailsSent       int                `json:"total_mails_sent"`
	TotalMailsReceived   int                `json:"total_mails_received"`
	SpamReports          int                `json:"spam_reports"`
}

// HealthResponse represents service health status
type HealthResponse struct {
	Status    string    `json:"status"`
	Timestamp time.Time `json:"timestamp"`
	Version   string    `json:"version"`
	Uptime    int64     `json:"uptime"`
}

// MarkReadResponse represents response for mark as read operation
type MarkReadResponse struct {
	MailID       uuid.UUID `json:"mail_id"`
	ReadAt       time.Time `json:"read_at"`
	PreviousStatus string  `json:"previous_status"`
}

// ArchiveResponse represents response for archive operation
type ArchiveResponse struct {
	MailID     uuid.UUID `json:"mail_id"`
	ArchivedAt time.Time `json:"archived_at"`
	Folder     string    `json:"folder"`
}

// ReportResponse represents response for mail reporting
type ReportResponse struct {
	ReportID    uuid.UUID `json:"report_id"`
	MailID      uuid.UUID `json:"mail_id"`
	SubmittedAt time.Time `json:"submitted_at"`
	Status      string    `json:"status"`
}

// Error represents an error response
type Error struct {
	MailID    *uuid.UUID `json:"mail_id,omitempty"`
	Error     string     `json:"error"`
	Code      string     `json:"code"`
	Message   string     `json:"message"`
	Timestamp time.Time  `json:"timestamp"`
}

// MailEvent represents an event related to mail operations
type MailEvent struct {
	ID          uuid.UUID              `json:"id"`
	PlayerID    uuid.UUID              `json:"player_id"`
	Type        string                 `json:"type"`
	Data        map[string]interface{} `json:"data"`
	Timestamp   time.Time              `json:"timestamp"`
}
