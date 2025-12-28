package models

import (
	"time"

	"github.com/google/uuid"
)

// Notification represents a notification message
type Notification struct {
	ID          uuid.UUID  `json:"id" db:"id"`
	PlayerID    uuid.UUID  `json:"player_id" db:"player_id"`
	Type        string     `json:"type" db:"type"`        // system, achievement, quest, social, combat, economy, event
	Title       string     `json:"title" db:"title"`
	Message     string     `json:"message" db:"message"`
	Data        string     `json:"data,omitempty" db:"data"` // JSON string for additional data
	IsRead      bool       `json:"is_read" db:"is_read"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
	ReadAt      *time.Time `json:"read_at,omitempty" db:"read_at"`
	ExpiresAt   *time.Time `json:"expires_at,omitempty" db:"expires_at"`
	Priority    string     `json:"priority" db:"priority"` // low, normal, high, urgent
}

// NotificationListResponse represents paginated notification list
type NotificationListResponse struct {
	Notifications []Notification `json:"notifications"`
	Total         int            `json:"total"`
	UnreadCount   int            `json:"unread_count"`
	Offset        int            `json:"offset"`
	Limit         int            `json:"limit"`
}

// CreateNotificationRequest represents request to create notification
type CreateNotificationRequest struct {
	PlayerID  uuid.UUID `json:"player_id"`
	Type      string    `json:"type"`
	Title     string    `json:"title"`
	Message   string    `json:"message"`
	Data      string    `json:"data,omitempty"`
	ExpiresAt *time.Time `json:"expires_at,omitempty"`
	Priority  string    `json:"priority"`
}

// MarkReadRequest represents request to mark notification as read
type MarkReadRequest struct {
	NotificationID uuid.UUID `json:"notification_id"`
}

// BulkReadRequest represents request to mark multiple notifications as read
type BulkReadRequest struct {
	NotificationIDs []uuid.UUID `json:"notification_ids"`
}

// BulkDeleteRequest represents request to delete multiple notifications
type BulkDeleteRequest struct {
	NotificationIDs []uuid.UUID `json:"notification_ids"`
}

// UnreadCountResponse represents unread notifications count
type UnreadCountResponse struct {
	Count int `json:"count"`
}

// HealthResponse represents health check response
type HealthResponse struct {
	Status    string    `json:"status"`
	Timestamp time.Time `json:"timestamp"`
	Version   string    `json:"version"`
	Uptime    int64     `json:"uptime"`
}

// ErrorResponse represents error response
type ErrorResponse struct {
	Error   string `json:"error"`
	Code    string `json:"code"`
	Message string `json:"message"`
}

