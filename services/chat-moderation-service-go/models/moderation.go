// Package models Issue: #1911
package models

import (
	"time"

	"github.com/google/uuid"
)

// RuleType represents different types of moderation rules
type RuleType string

const (
	RuleTypeWordFilter        RuleType = "word_filter"
	RuleTypeSpamPattern       RuleType = "spam_pattern"
	RuleTypeToxicityThreshold RuleType = "toxicity_threshold"
)

// ViolationType represents types of violations
type ViolationType string

const (
	ViolationTypeSpam     ViolationType = "spam"
	ViolationTypeToxicity ViolationType = "toxicity"

	ViolationTypeForbiddenWords    ViolationType = "forbidden_words"
	ViolationTypeRateLimitExceeded ViolationType = "rate_limit_exceeded"
)

// ViolationStatus represents violation processing status
type ViolationStatus string

const (
	ViolationStatusResolved  ViolationStatus = "resolved"
	ViolationStatusDismissed ViolationStatus = "dismissed"
)

// ActionType represents moderation actions
type ActionType string

const (
	ActionTypeMessageDelete ActionType = "message_delete"

	ActionTypeDismiss ActionType = "dismiss"
)

// SeverityLevel Severity level for violations
type SeverityLevel string

const (
	SeverityLow      SeverityLevel = "low"
	SeverityMedium   SeverityLevel = "medium"
	SeverityHigh     SeverityLevel = "high"
	SeverityCritical SeverityLevel = "critical"
)

// ChannelType represents chat channel types
type ChannelType string

const (
	ChannelTypeGlobal ChannelType = "global"
)

// ModerationRule represents a moderation rule
// OPTIMIZATION: Field alignment - large fields first
type ModerationRule struct {
	// Large fields first for memory alignment
	Metadata  map[string]interface{} `json:"metadata" db:"metadata"`     // 24 bytes (map header)
	Pattern   string                 `json:"pattern" db:"pattern"`       // 16 bytes (string header)
	Name      string                 `json:"name" db:"name"`             // 16 bytes (string header)
	ID        uuid.UUID              `json:"id" db:"id"`                 // 16 bytes (uuid.UUID)
	CreatedBy uuid.UUID              `json:"created_by" db:"created_by"` // 16 bytes (uuid.UUID)
	CreatedAt time.Time              `json:"created_at" db:"created_at"` // 24 bytes (time.Time)
	UpdatedAt time.Time              `json:"updated_at" db:"updated_at"` // 24 bytes (time.Time)
	RuleType  RuleType               `json:"rule_type" db:"rule_type"`   // 16 bytes (string)
	Severity  SeverityLevel          `json:"severity" db:"severity"`     // 16 bytes (string)
	Action    ActionType             `json:"action" db:"action"`         // 16 bytes (string)
	IsActive  bool                   `json:"is_active" db:"is_active"`   // 1 byte (bool)
}

// ModerationViolation represents a detected violation
// OPTIMIZATION: Field alignment - large fields first
type ModerationViolation struct {
	// Large fields first for memory alignment
	Metadata       map[string]interface{} `json:"metadata" db:"metadata"`                 // 24 bytes (map header)
	PlayerInfo     map[string]interface{} `json:"player_info" db:"player_info"`           // 24 bytes (map header)
	MessageContent string                 `json:"message_content" db:"message_content"`   // 16 bytes (string header)
	ID             uuid.UUID              `json:"id" db:"id"`                             // 16 bytes (uuid.UUID)
	PlayerID       uuid.UUID              `json:"player_id" db:"player_id"`               // 16 bytes (uuid.UUID)
	MessageID      uuid.UUID              `json:"message_id" db:"message_id"`             // 16 bytes (uuid.UUID)
	RuleTriggered  uuid.UUID              `json:"rule_triggered" db:"rule_triggered"`     // 16 bytes (uuid.UUID)
	DetectedAt     time.Time              `json:"detected_at" db:"detected_at"`           // 24 bytes (time.Time)
	ResolvedAt     *time.Time             `json:"resolved_at,omitempty" db:"resolved_at"` // 8 bytes (pointer)
	ViolationType  ViolationType          `json:"violation_type" db:"violation_type"`     // 16 bytes (string)
	Status         ViolationStatus        `json:"status" db:"status"`                     // 16 bytes (string)
	ChannelType    ChannelType            `json:"channel_type" db:"channel_type"`         // 16 bytes (string)
	SeverityScore  float64                `json:"severity_score" db:"severity_score"`     // 8 bytes (float64)
}

// ModerationAction represents an action taken on a violation
// OPTIMIZATION: Field alignment - large fields first
type ModerationAction struct {
	// Large fields first for memory alignment
	Metadata    map[string]interface{} `json:"metadata" db:"metadata"`               // 24 bytes (map header)
	Reason      string                 `json:"reason" db:"reason"`                   // 16 bytes (string header)
	ID          uuid.UUID              `json:"id" db:"id"`                           // 16 bytes (uuid.UUID)
	ViolationID uuid.UUID              `json:"violation_id" db:"violation_id"`       // 16 bytes (uuid.UUID)
	ModeratorID *uuid.UUID             `json:"moderator_id" db:"moderator_id"`       // 8 bytes (pointer)
	AppliedAt   time.Time              `json:"applied_at" db:"applied_at"`           // 24 bytes (time.Time)
	ExpiresAt   *time.Time             `json:"expires_at,omitempty" db:"expires_at"` // 8 bytes (pointer)
	ActionType  ActionType             `json:"action_type" db:"action_type"`         // 16 bytes (string)
	Duration    string                 `json:"duration" db:"duration"`               // 16 bytes (string header)
}

// ModerationLog represents audit log entry
// OPTIMIZATION: Field alignment - large fields first
type ModerationLog struct {
	// Large fields first for memory alignment
	Details     map[string]interface{} `json:"details" db:"details"`           // 24 bytes (map header)
	Action      string                 `json:"action" db:"action"`             // 16 bytes (string header)
	UserAgent   string                 `json:"user_agent" db:"user_agent"`     // 16 bytes (string header)
	IPAddress   string                 `json:"ip_address" db:"ip_address"`     // 16 bytes (string header)
	ID          uuid.UUID              `json:"id" db:"id"`                     // 16 bytes (uuid.UUID)
	PlayerID    uuid.UUID              `json:"player_id" db:"player_id"`       // 16 bytes (uuid.UUID)
	ModeratorID *uuid.UUID             `json:"moderator_id" db:"moderator_id"` // 8 bytes (pointer)
	CreatedAt   time.Time              `json:"created_at" db:"created_at"`     // 24 bytes (time.Time)
	ActionType  ActionType             `json:"action_type" db:"action_type"`   // 16 bytes (string)
}

// Request/Response models for API

// CheckMessageRequest for message validation
type CheckMessageRequest struct {
	PlayerID    uuid.UUID              `json:"player_id"`
	Message     string                 `json:"message"`
	ChannelType ChannelType            `json:"channel_type"`
	Metadata    map[string]interface{} `json:"metadata,omitempty"`
}

// CheckMessageResponse for validation result
type CheckMessageResponse struct {
	Allowed           bool          `json:"allowed"`
	ViolationDetected bool          `json:"violation_detected"`
	ViolationType     ViolationType `json:"violation_type,omitempty"`
	SeverityScore     float64       `json:"severity_score"`
	ActionRequired    bool          `json:"action_required"`
	FilteredMessage   string        `json:"filtered_message,omitempty"`
	RuleTriggered     *uuid.UUID    `json:"rule_triggered,omitempty"`
	ProcessingTimeMs  float64       `json:"processing_time_ms"`
}

// CreateModerationRuleRequest for creating rules
type CreateModerationRuleRequest struct {
	RuleType RuleType               `json:"rule_type"`
	Name     string                 `json:"name"`
	Pattern  string                 `json:"pattern"`
	Severity SeverityLevel          `json:"severity"`
	Action   ActionType             `json:"action"`
	Metadata map[string]interface{} `json:"metadata,omitempty"`
}

// ApplyModerationActionRequest for applying actions
type ApplyModerationActionRequest struct {
	ActionType   ActionType `json:"action_type"`
	Duration     string     `json:"duration,omitempty"`
	Reason       string     `json:"reason"`
	NotifyPlayer bool       `json:"notify_player"`
	EscalateTo   *uuid.UUID `json:"escalate_to,omitempty"`
}

// HealthResponse for health check
type HealthResponse struct {
	Status               string    `json:"status"`
	Timestamp            time.Time `json:"timestamp"`
	Version              string    `json:"version"`
	UptimeSeconds        int64     `json:"uptime_seconds"`
	ActiveRules          int       `json:"active_rules"`
	PendingViolations    int       `json:"pending_violations"`
	TotalViolationsToday int       `json:"total_violations_today"`
}

// ModerationStatsResponse for statistics
type ModerationStatsResponse struct {
	Timeframe               string         `json:"timeframe"`
	TotalMessagesChecked    int64          `json:"total_messages_checked"`
	ViolationsDetected      int64          `json:"violations_detected"`
	ViolationsByType        map[string]int `json:"violations_by_type"`
	ActionsTaken            int64          `json:"actions_taken"`
	ActionsByType           map[string]int `json:"actions_by_type"`
	AverageProcessingTimeMs float64        `json:"average_processing_time_ms"`
	P99ProcessingTimeMs     float64        `json:"p99_processing_time_ms"`
	RuleHitCounts           map[string]int `json:"rule_hit_counts"`
	TopViolatingPlayers     []PlayerStats  `json:"top_violating_players"`
}

// PlayerStats for top violators
type PlayerStats struct {
	PlayerID       uuid.UUID `json:"player_id"`
	ViolationCount int       `json:"violation_count"`
	LastViolation  time.Time `json:"last_violation"`
}

// ModerationRulesResponse for rules list
type ModerationRulesResponse struct {
	Rules  []ModerationRule `json:"rules"`
	Total  int              `json:"total"`
	Limit  int              `json:"limit"`
	Offset int              `json:"offset"`
}

// ModerationViolationsResponse for violations list
type ModerationViolationsResponse struct {
	Violations []ModerationViolation `json:"violations"`
	Total      int                   `json:"total"`
	Limit      int                   `json:"limit"`
	Offset     int                   `json:"offset"`
}

// ModerationLogsResponse for logs list
type ModerationLogsResponse struct {
	Logs   []ModerationLog `json:"logs"`
	Total  int             `json:"total"`
	Limit  int             `json:"limit"`
	Offset int             `json:"offset"`
}
