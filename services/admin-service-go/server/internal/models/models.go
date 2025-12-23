// Issue: Implement admin-service-go based on OpenAPI specification
package models

import (
	"errors"
	"time"

	"github.com/google/uuid"
)

// AdminUser represents an administrative user with role-based permissions
type AdminUser struct {
	ID          uuid.UUID `json:"id" db:"id"`
	Username    string    `json:"username" db:"username"`
	Email       string    `json:"email" db:"email"`
	Role        string    `json:"role" db:"role"` // super_admin, admin, moderator
	Permissions []string  `json:"permissions" db:"permissions"`
	LastLogin   time.Time `json:"last_login" db:"last_login"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
	IsActive    bool      `json:"is_active" db:"is_active"`
}

// AdminSession represents an active admin user session
type AdminSession struct {
	SessionID    string    `json:"session_id"`
	AdminID      uuid.UUID `json:"admin_id"`
	Username     string    `json:"username"`
	LoginTime    time.Time `json:"login_time"`
	LastActivity time.Time `json:"last_activity"`
	IPAddress    string    `json:"ip_address"`
	UserAgent    string    `json:"user_agent"`
	IsActive     bool      `json:"is_active"`
}

// AdminAction represents an auditable admin action for compliance
type AdminAction struct {
	ID        uuid.UUID      `json:"id" db:"id"`
	AdminID   uuid.UUID      `json:"admin_id" db:"admin_id"`
	Action    string         `json:"action" db:"action"` // user_ban, user_unban, content_moderate, etc.
	Resource  string         `json:"resource" db:"resource"` // users/123, content/456, etc.
	Timestamp time.Time      `json:"timestamp" db:"timestamp"`
	IPAddress string         `json:"ip_address" db:"ip_address"`
	UserAgent string         `json:"user_agent" db:"user_agent"`
	Metadata  interface{}    `json:"metadata" db:"metadata"` // JSON metadata about the action
}

// SystemHealth represents comprehensive system health status
type SystemHealth struct {
	Status      string            `json:"status"`       // healthy, degraded, unhealthy
	Timestamp   time.Time         `json:"timestamp"`
	Version     string            `json:"version"`
	Uptime      time.Duration     `json:"uptime"`
	Database    string            `json:"database"`     // connected, disconnected
	Cache       string            `json:"cache"`        // connected, disconnected
	Services    []ServiceStatus   `json:"services"`
	Metrics     SystemMetrics     `json:"metrics"`
	Alerts      []SystemAlert     `json:"alerts"`
}

// ServiceStatus represents the status of a dependent service
type ServiceStatus struct {
	Name    string `json:"name"`
	Status  string `json:"status"`  // up, down, degraded
	Version string `json:"version,omitempty"`
	Uptime  time.Duration `json:"uptime,omitempty"`
}

// SystemMetrics contains key system performance metrics
type SystemMetrics struct {
	ActiveConnections int           `json:"active_connections"`
	TotalRequests     int64         `json:"total_requests"`
	ErrorRate         float64       `json:"error_rate"`
	AverageLatency    time.Duration `json:"average_latency"`
	MemoryUsage       int64         `json:"memory_usage_bytes"`
	CPUUsage          float64       `json:"cpu_usage_percent"`
}

// SystemAlert represents a system health alert
type SystemAlert struct {
	Level     string    `json:"level"`     // info, warning, error, critical
	Message   string    `json:"message"`
	Timestamp time.Time `json:"timestamp"`
	Service   string    `json:"service"`
	Code      string    `json:"code,omitempty"`
}

// UserBanRequest represents a request to ban a user
type UserBanRequest struct {
	UserID   uuid.UUID     `json:"user_id"`
	Reason   string        `json:"reason"`
	Duration time.Duration `json:"duration"`
	Notes    string        `json:"notes,omitempty"`
}

// UserUnbanRequest represents a request to unban a user
type UserUnbanRequest struct {
	UserID uuid.UUID `json:"user_id"`
	Reason string    `json:"reason"`
	Notes  string    `json:"notes,omitempty"`
}

// ContentModerationRequest represents a content moderation action
type ContentModerationRequest struct {
	ContentID   uuid.UUID `json:"content_id"`
	Action      string    `json:"action"`      // approve, reject, flag, delete
	Reason      string    `json:"reason"`
	ModeratorID uuid.UUID `json:"moderator_id"`
	Notes       string    `json:"notes,omitempty"`
}

// AuditLogFilter represents filters for audit log queries
type AuditLogFilter struct {
	AdminID    *uuid.UUID `json:"admin_id,omitempty"`
	Action     *string    `json:"action,omitempty"`
	Resource   *string    `json:"resource,omitempty"`
	StartTime  *time.Time `json:"start_time,omitempty"`
	EndTime    *time.Time `json:"end_time,omitempty"`
	IPAddress  *string    `json:"ip_address,omitempty"`
	Limit      int        `json:"limit"`
	Offset     int        `json:"offset"`
}

// UserDetails represents detailed user information for admin panel
type UserDetails struct {
	ID         uuid.UUID  `json:"id"`
	Username   string     `json:"username"`
	Email      string     `json:"email"`
	CreatedAt  time.Time  `json:"created_at"`
	LastLogin  *time.Time `json:"last_login,omitempty"`
	IsActive   bool       `json:"is_active"`
	Role       string     `json:"role"`
	BanReason  *string    `json:"ban_reason,omitempty"`
	BanExpires *time.Time `json:"ban_expires,omitempty"`
}

// ContentItem represents content requiring moderation
type ContentItem struct {
	ID          uuid.UUID `json:"id"`
	ContentType string    `json:"content_type"`
	Content     string    `json:"content"`
	AuthorID    uuid.UUID `json:"author_id"`
	SubmittedAt time.Time `json:"submitted_at"`
	Status      string    `json:"status"`
	Priority    string    `json:"priority"`
}

// Common errors
var (
	ErrInsufficientPermissions = errors.New("insufficient admin permissions")
	ErrUserNotFound           = errors.New("user not found")
	ErrSessionExpired         = errors.New("admin session expired")
	ErrInvalidToken           = errors.New("invalid admin token")
	ErrSystemUnavailable      = errors.New("system temporarily unavailable")
)
