// Issue: #event-processing-service - Enterprise Event Processing Service
// Models for Event Processing Service - High-performance event handling

package models

import (
	"time"
)

// Event represents a game event to be processed
type Event struct {
	ID          string                 `json:"id" db:"id"`
	EventType   string                 `json:"event_type" db:"event_type"`     // "player_action", "quest_complete", "combat_end", etc.
	PlayerID    string                 `json:"player_id" db:"player_id"`
	SessionID   string                 `json:"session_id" db:"session_id"`
	GameID      string                 `json:"game_id" db:"game_id"`           // For multiplayer games
	EventData   map[string]interface{} `json:"event_data" db:"event_data"`     // JSON event payload
	Timestamp   time.Time              `json:"timestamp" db:"timestamp"`
	Processed   bool                   `json:"processed" db:"processed"`
	ProcessedAt *time.Time             `json:"processed_at" db:"processed_at"`
	ProcessingTime int                 `json:"processing_time" db:"processing_time"` // milliseconds
	Status      string                 `json:"status" db:"status"`             // "pending", "processing", "completed", "failed"
	Error       string                 `json:"error" db:"error"`
	Priority    int                    `json:"priority" db:"priority"`         // 1-10, higher = more urgent
	Retries     int                    `json:"retries" db:"retries"`
	MaxRetries  int                    `json:"max_retries" db:"max_retries"`
	CreatedAt   time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time              `json:"updated_at" db:"updated_at"`
}

// EventHandler represents an event handler configuration
type EventHandler struct {
	ID          string `json:"id" db:"id"`
	EventType   string `json:"event_type" db:"event_type"`
	HandlerName string `json:"handler_name" db:"handler_name"`
	ServiceName string `json:"service_name" db:"service_name"` // Which service handles this
	Endpoint    string `json:"endpoint" db:"endpoint"`         // HTTP endpoint or queue name
	Method      string `json:"method" db:"method"`             // "http", "queue", "grpc"
	IsActive    bool   `json:"is_active" db:"is_active"`
	Priority    int    `json:"priority" db:"priority"`         // Processing priority
	Timeout     int    `json:"timeout" db:"timeout"`           // Timeout in seconds
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
}

// EventProcessingStats tracks event processing statistics
type EventProcessingStats struct {
	EventType         string    `json:"event_type" db:"event_type"`
	TotalEvents       int64     `json:"total_events" db:"total_events"`
	ProcessedEvents   int64     `json:"processed_events" db:"processed_events"`
	FailedEvents      int64     `json:"failed_events" db:"failed_events"`
	AvgProcessingTime float64   `json:"avg_processing_time" db:"avg_processing_time"` // milliseconds
	SuccessRate       float64   `json:"success_rate" db:"success_rate"`               // 0-1
	LastProcessedAt   *time.Time `json:"last_processed_at" db:"last_processed_at"`
	CreatedAt         time.Time `json:"created_at" db:"created_at"`
	UpdatedAt         time.Time `json:"updated_at" db:"updated_at"`
}

// EventBatch represents a batch of events for processing
type EventBatch struct {
	ID        string    `json:"id" db:"id"`
	EventType string    `json:"event_type" db:"event_type"`
	EventIDs  []string  `json:"event_ids" db:"event_ids"` // JSON array
	Status    string    `json:"status" db:"status"`       // "pending", "processing", "completed"
	CreatedAt time.Time `json:"created_at" db:"created_at"`
	UpdatedAt time.Time `json:"updated_at" db:"updated_at"`
}

// DeadLetterEvent represents failed events that need manual review
type DeadLetterEvent struct {
	ID          string                 `json:"id" db:"id"`
	EventID     string                 `json:"event_id" db:"event_id"`
	EventType   string                 `json:"event_type" db:"event_type"`
	EventData   map[string]interface{} `json:"event_data" db:"event_data"`
	Error       string                 `json:"error" db:"error"`
	ErrorCode   string                 `json:"error_code" db:"error_code"`
	FailedAt    time.Time              `json:"failed_at" db:"failed_at"`
	RetryCount  int                    `json:"retry_count" db:"retry_count"`
	Reviewed    bool                   `json:"reviewed" db:"reviewed"`
	ReviewedBy  string                 `json:"reviewed_by" db:"reviewed_by"`
	ReviewedAt  *time.Time             `json:"reviewed_at" db:"reviewed_at"`
	Action      string                 `json:"action" db:"action"` // "retry", "discard", "manual_fix"
	CreatedAt   time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time              `json:"updated_at" db:"updated_at"`
}

// EventFilter represents filtering criteria for event queries
type EventFilter struct {
	EventType   []string  `json:"event_type,omitempty"`
	PlayerID    string    `json:"player_id,omitempty"`
	Status      []string  `json:"status,omitempty"`
	TimeRange   *TimeRange `json:"time_range,omitempty"`
	Priority    *IntRange `json:"priority,omitempty"`
	Processed   *bool     `json:"processed,omitempty"`
}

// TimeRange represents a time range filter
type TimeRange struct {
	Start time.Time `json:"start"`
	End   time.Time `json:"end"`
}

// IntRange represents an integer range filter
type IntRange struct {
	Min int `json:"min"`
	Max int `json:"max"`
}

// EventProcessingResult represents the result of processing an event
type EventProcessingResult struct {
	EventID      string `json:"event_id"`
	Success      bool   `json:"success"`
	Error        string `json:"error,omitempty"`
	ProcessingTime int    `json:"processing_time"` // milliseconds
	Response     map[string]interface{} `json:"response,omitempty"`
}

// BulkEventProcessingRequest represents a request to process multiple events
type BulkEventProcessingRequest struct {
	EventIDs []string               `json:"event_ids"`
	Options  BulkProcessingOptions `json:"options,omitempty"`
}

// BulkProcessingOptions represents options for bulk processing
type BulkProcessingOptions struct {
	ParallelProcessing bool `json:"parallel_processing"` // Process in parallel
	MaxConcurrency     int  `json:"max_concurrency"`     // Max concurrent handlers
	Timeout            int  `json:"timeout"`             // Overall timeout in seconds
	ContinueOnError    bool `json:"continue_on_error"`   // Continue processing if one fails
}

// EventMetrics represents real-time event processing metrics
type EventMetrics struct {
	TotalEventsQueued    int64   `json:"total_events_queued"`
	EventsProcessedPerSec float64 `json:"events_processed_per_sec"`
	AvgQueueWaitTime     float64 `json:"avg_queue_wait_time"`     // milliseconds
	ActiveHandlers       int     `json:"active_handlers"`
	FailedEventsToday    int64   `json:"failed_events_today"`
	SuccessRate          float64 `json:"success_rate"`            // 0-1
	Timestamp            time.Time `json:"timestamp"`
}

// EventProcessingConfig represents configuration for event processing
type EventProcessingConfig struct {
	MaxRetries            int           `json:"max_retries"`
	DefaultTimeout        int           `json:"default_timeout"`         // seconds
	BatchSize             int           `json:"batch_size"`
	MaxConcurrency        int           `json:"max_concurrency"`
	QueueSize             int           `json:"queue_size"`
	DeadLetterThreshold   int           `json:"dead_letter_threshold"`   // After this many retries
	ProcessingInterval    time.Duration `json:"processing_interval"`     // How often to check for new events
	CleanupInterval       time.Duration `json:"cleanup_interval"`        // How often to cleanup old events
	StatsUpdateInterval   time.Duration `json:"stats_update_interval"`   // How often to update stats
}

// EventType represents predefined event types
type EventType string

const (
	EventTypePlayerLogin         EventType = "player_login"
	EventTypePlayerLogout        EventType = "player_logout"
	EventTypeQuestStarted        EventType = "quest_started"
	EventTypeQuestCompleted      EventType = "quest_completed"
	EventTypeQuestFailed         EventType = "quest_failed"
	EventTypeCombatStarted       EventType = "combat_started"
	EventTypeCombatEnded         EventType = "combat_ended"
	EventTypeItemAcquired        EventType = "item_acquired"
	EventTypeItemUsed            EventType = "item_used"
	EventTypeAchievementUnlocked EventType = "achievement_unlocked"
	EventTypeLevelUp             EventType = "level_up"
	EventTypeCurrencyEarned      EventType = "currency_earned"
	EventTypeCurrencySpent       EventType = "currency_spent"
	EventTypeSocialInteraction   EventType = "social_interaction"
	EventTypeGuildJoined         EventType = "guild_joined"
	EventTypeGuildLeft           EventType = "guild_left"
	EventTypeEasterEggDiscovered EventType = "easter_egg_discovered"
	EventTypeSystemError         EventType = "system_error"
	EventTypePerformanceAlert    EventType = "performance_alert"
)

// ProcessingStatus represents event processing status
type ProcessingStatus string

const (
	ProcessingStatusPending    ProcessingStatus = "pending"
	ProcessingStatusProcessing ProcessingStatus = "processing"
	ProcessingStatusCompleted  ProcessingStatus = "completed"
	ProcessingStatusFailed     ProcessingStatus = "failed"
	ProcessingStatusRetrying   ProcessingStatus = "retrying"
)

// HandlerMethod represents the method to use for event handling
type HandlerMethod string

const (
	HandlerMethodHTTP HandlerMethod = "http"
	HandlerMethodGRPC HandlerMethod = "grpc"
	HandlerMethodQueue HandlerMethod = "queue"
	HandlerMethodDirect HandlerMethod = "direct"
)
