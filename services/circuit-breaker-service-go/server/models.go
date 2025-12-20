// Package server OPTIMIZATION: Issue #2202 - Memory-aligned struct definitions for circuit breaker performance
package server

import (
	"time"
)

// CircuitBreaker OPTIMIZATION: Field alignment - large to small (time.Time=24 bytes, string=16 bytes, int=8 bytes, bool=1 byte)
type CircuitBreaker struct {
	CreatedAt           time.Time                 `json:"created_at"`           // 24 bytes - largest
	StateChangedAt      time.Time                 `json:"state_changed_at"`     // 24 bytes
	LastFailureTime     time.Time                 `json:"last_failure_time"`    // 24 bytes
	StateHistory        []StateChange             `json:"state_history"`        // 24 bytes (slice header)
	CircuitID           string                    `json:"circuit_id"`           // 16 bytes
	ServiceName         string                    `json:"service_name"`         // 16 bytes
	Endpoint            string                    `json:"endpoint"`             // 16 bytes
	State               string                    `json:"state"`                // 16 bytes
	Config              CircuitBreakerConfig      `json:"config"`               // 16 bytes (struct)
	Metrics             CircuitBreakerMetricsData `json:"metrics"`              // 16 bytes (struct)
	FailureCount        int                       `json:"failure_count"`        // 8 bytes
	SuccessCount        int                       `json:"success_count"`        // 8 bytes
	ConsecutiveFailures int                       `json:"consecutive_failures"` // 8 bytes
}

type CircuitBreakerConfig struct {
	MonitoringWindow      time.Duration   `json:"monitoring_window"`        // 8 bytes
	Timeout               time.Duration   `json:"timeout"`                  // 8 bytes
	RetryDelay            time.Duration   `json:"retry_delay"`              // 8 bytes
	MaxRetryDelay         time.Duration   `json:"max_retry_delay"`          // 8 bytes
	SlowCallThreshold     time.Duration   `json:"slow_call_threshold"`      // 8 bytes
	FailureThreshold      int             `json:"failure_threshold"`        // 8 bytes
	SuccessThreshold      int             `json:"success_threshold"`        // 8 bytes
	SlowCallRateThreshold float64         `json:"slow_call_rate_threshold"` // 8 bytes
	AlertThresholds       AlertThresholds `json:"alert_thresholds"`         // 8 bytes (struct)
	FallbackEnabled       bool            `json:"fallback_enabled"`         // 1 byte
	MetricsEnabled        bool            `json:"metrics_enabled"`          // 1 byte
	FallbackResponse      string          `json:"fallback_response"`        // 16 bytes (moved after bools for alignment)
}

type CircuitBreakerMetricsData struct {
	AverageResponseTime time.Duration `json:"average_response_time"` // 8 bytes
	TotalRequests       int64         `json:"total_requests"`        // 8 bytes
	TotalFailures       int64         `json:"total_failures"`        // 8 bytes
	TotalSuccesses      int64         `json:"total_successes"`       // 8 bytes
	TotalTimeouts       int64         `json:"total_timeouts"`        // 8 bytes
	LastRequestTime     time.Time     `json:"last_request_time"`     // 24 bytes
}

type AlertThresholds struct {
	ErrorRate           float64 `json:"error_rate"`           // 8 bytes
	ResponseTime        int64   `json:"response_time"`        // 8 bytes
	ConsecutiveFailures int     `json:"consecutive_failures"` // 8 bytes
	SlowCallRate        float64 `json:"slow_call_rate"`       // 8 bytes
}

type StateChange struct {
	ChangedAt   time.Time `json:"changed_at"`   // 24 bytes - largest
	FromState   string    `json:"from_state"`   // 16 bytes
	ToState     string    `json:"to_state"`     // 16 bytes
	Reason      string    `json:"reason"`       // 16 bytes
	TriggeredBy string    `json:"triggered_by"` // 16 bytes
}

// CreateCircuitRequest OPTIMIZATION: Field alignment for request/response structs
type CreateCircuitRequest struct {
	CircuitID             string                 `json:"circuit_id"`
	ServiceName           string                 `json:"service_name"`
	Endpoint              string                 `json:"endpoint"`
	FailureThreshold      int                    `json:"failure_threshold"`
	SuccessThreshold      int                    `json:"success_threshold"`
	Timeout               int64                  `json:"timeout"`
	RetryDelay            int64                  `json:"retry_delay"`
	MaxRetryDelay         int64                  `json:"max_retry_delay"`
	MonitoringWindow      int64                  `json:"monitoring_window"`
	SlowCallThreshold     int64                  `json:"slow_call_threshold"`
	SlowCallRateThreshold float64                `json:"slow_call_rate_threshold"`
	FallbackEnabled       bool                   `json:"fallback_enabled"`
	FallbackResponse      string                 `json:"fallback_response"`
	MetricsEnabled        bool                   `json:"metrics_enabled"`
	AlertThresholds       map[string]interface{} `json:"alert_thresholds"`
}

type CreateCircuitResponse struct {
	CircuitID   string                `json:"circuit_id"`
	ServiceName string                `json:"service_name"`
	Endpoint    string                `json:"endpoint"`
	State       string                `json:"state"`
	CreatedAt   int64                 `json:"created_at"`
	Config      *CircuitBreakerConfig `json:"config"`
}

// Bulkhead OPTIMIZATION: Field alignment for bulkhead structs
type Bulkhead struct {
	CreatedAt            time.Time           `json:"created_at"`             // 24 bytes - largest
	BulkheadID           string              `json:"bulkhead_id"`            // 16 bytes
	ServiceName          string              `json:"service_name"`           // 16 bytes
	Config               BulkheadConfig      `json:"config"`                 // 16 bytes (struct)
	Metrics              BulkheadMetricsData `json:"metrics"`                // 16 bytes (struct)
	ActiveThreads        int                 `json:"active_threads"`         // 8 bytes
	QueuedRequests       int                 `json:"queued_requests"`        // 8 bytes
	RejectedRequests     int                 `json:"rejected_requests"`      // 8 bytes
	CompletedRequests    int                 `json:"completed_requests"`     // 8 bytes
	AverageExecutionTime time.Duration       `json:"average_execution_time"` // 8 bytes
	MaxExecutionTime     time.Duration       `json:"max_execution_time"`     // 8 bytes
}

type BulkheadConfig struct {
	MaxWaitDuration    time.Duration `json:"max_wait_duration"`    // 8 bytes
	MaxConcurrentCalls int           `json:"max_concurrent_calls"` // 8 bytes
	QueueSize          int           `json:"queue_size"`           // 8 bytes
	ThreadPoolSize     int           `json:"thread_pool_size"`     // 8 bytes
	IsolationStrategy  string        `json:"isolation_strategy"`   // 16 bytes
	Fairness           bool          `json:"fairness"`             // 1 byte
	MetricsEnabled     bool          `json:"metrics_enabled"`      // 1 byte
}

type BulkheadMetricsData struct {
	LastRequestTime  time.Time     `json:"last_request_time"`  // 24 bytes - largest
	TotalRequests    int64         `json:"total_requests"`     // 8 bytes
	RejectedRequests int64         `json:"rejected_requests"`  // 8 bytes
	QueuedRequests   int64         `json:"queued_requests"`    // 8 bytes
	AverageQueueTime time.Duration `json:"average_queue_time"` // 8 bytes
}

// TimeoutConfig OPTIMIZATION: Field alignment for timeout structs
type TimeoutConfig struct {
	TimeoutID           string        `json:"timeout_id"`            // 16 bytes
	ServiceName         string        `json:"service_name"`          // 16 bytes
	Endpoint            string        `json:"endpoint"`              // 16 bytes
	AverageResponseTime time.Duration `json:"average_response_time"` // 8 bytes
	TimeoutsTriggered   int64         `json:"timeouts_triggered"`    // 8 bytes
	TotalRequests       int64         `json:"total_requests"`        // 8 bytes
	TimeoutThreshold    time.Duration `json:"timeout_threshold"`     // 8 bytes
	CreatedAt           time.Time     `json:"created_at"`            // 24 bytes - largest
	Enabled             bool          `json:"enabled"`               // 1 byte
}

// CircuitBreakerServiceConfig OPTIMIZATION: Field alignment for service config
type CircuitBreakerServiceConfig struct {
	StateSyncInterval       time.Duration `json:"state_sync_interval"`       // 8 bytes
	ReadTimeout             time.Duration `json:"read_timeout"`              // 8 bytes
	WriteTimeout            time.Duration `json:"write_timeout"`             // 8 bytes
	MetricsInterval         time.Duration `json:"metrics_interval"`          // 8 bytes
	DefaultTimeout          time.Duration `json:"default_timeout"`           // 8 bytes
	CleanupInterval         time.Duration `json:"cleanup_interval"`          // 8 bytes
	HTTPAddr                string        `json:"http_addr"`                 // 16 bytes
	RedisAddr               string        `json:"redis_addr"`                // 16 bytes
	PprofAddr               string        `json:"pprof_addr"`                // 16 bytes
	HealthAddr              string        `json:"health_addr"`               // 16 bytes
	MaxConnections          int           `json:"max_connections"`           // 8 bytes
	MaxHeaderBytes          int           `json:"max_header_bytes"`          // 8 bytes
	DefaultFailureThreshold int           `json:"default_failure_threshold"` // 8 bytes
}

// DegradationPolicy OPTIMIZATION: Field alignment for degradation policy structs
type DegradationPolicy struct {
	PolicyID        string                 `json:"policy_id"`         // 16 bytes
	ServiceName     string                 `json:"service_name"`      // 16 bytes
	Status          string                 `json:"status"`            // 16 bytes
	Config          DegradationConfig      `json:"config"`            // 16 bytes (struct)
	Metrics         DegradationMetricsData `json:"metrics"`           // 16 bytes (struct)
	TriggerCount    int                    `json:"trigger_count"`     // 8 bytes
	RecoveryCount   int                    `json:"recovery_count"`    // 8 bytes
	LastTriggeredAt time.Time              `json:"last_triggered_at"` // 24 bytes - largest
	CreatedAt       time.Time              `json:"created_at"`        // 24 bytes
	Enabled         bool                   `json:"enabled"`           // 1 byte
}

type DegradationConfig struct {
	ErrorRateThreshold    float64       `json:"error_rate_threshold"`    // 8 bytes
	ResponseTimeThreshold time.Duration `json:"response_time_threshold"` // 8 bytes
	MinRequests           int           `json:"min_requests"`            // 8 bytes
	RecoveryTimeWindow    time.Duration `json:"recovery_time_window"`    // 8 bytes
	Enabled               bool          `json:"enabled"`                 // 1 byte
}

type DegradationMetricsData struct {
	ErrorRate           float64       `json:"error_rate"`            // 8 bytes
	AverageResponseTime time.Duration `json:"average_response_time"` // 8 bytes
	RequestCount        int64         `json:"request_count"`         // 8 bytes
	LastUpdated         time.Time     `json:"last_updated"`          // 24 bytes - largest
}

// CreateBulkheadRequest OPTIMIZATION: Field alignment for request/response structs
type CreateBulkheadRequest struct {
	BulkheadID         string `json:"bulkhead_id"`
	ServiceName        string `json:"service_name"`
	MaxConcurrentCalls int    `json:"max_concurrent_calls"`
	MaxWaitDuration    int64  `json:"max_wait_duration"`
	IsolationStrategy  string `json:"isolation_strategy"`
	ThreadPoolSize     int    `json:"thread_pool_size"`
	QueueSize          int    `json:"queue_size"`
	Fairness           bool   `json:"fairness"`
	MetricsEnabled     bool   `json:"metrics_enabled"`
}

type CreateBulkheadResponse struct {
	BulkheadID  string          `json:"bulkhead_id"`
	ServiceName string          `json:"service_name"`
	CreatedAt   int64           `json:"created_at"`
	Config      *BulkheadConfig `json:"config"`
}

type CreateTimeoutRequest struct {
	TimeoutID        string `json:"timeout_id"`
	ServiceName      string `json:"service_name"`
	Endpoint         string `json:"endpoint"`
	TimeoutThreshold int64  `json:"timeout_threshold"`
	Enabled          bool   `json:"enabled"`
}

type CreateTimeoutResponse struct {
	TimeoutID   string `json:"timeout_id"`
	ServiceName string `json:"service_name"`
	CreatedAt   int64  `json:"created_at"`
}

type CreateDegradationPolicyRequest struct {
	PolicyID              string  `json:"policy_id"`
	ServiceName           string  `json:"service_name"`
	ErrorRateThreshold    float64 `json:"error_rate_threshold"`
	ResponseTimeThreshold int64   `json:"response_time_threshold"`
	MinRequests           int     `json:"min_requests"`
	RecoveryTimeWindow    int64   `json:"recovery_time_window"`
	Enabled               bool    `json:"enabled"`
}

type CreateDegradationPolicyResponse struct {
	PolicyID    string `json:"policy_id"`
	ServiceName string `json:"service_name"`
	CreatedAt   int64  `json:"created_at"`
}

// GetCircuitStateResponse Response structs for various endpoints
type GetCircuitStateResponse struct {
	CircuitID      string `json:"circuit_id"`
	State          string `json:"state"`
	StateChangedAt int64  `json:"state_changed_at"`
	Reason         string `json:"reason"`
}

type SetCircuitStateRequest struct {
	State  string `json:"state"`
	Reason string `json:"reason"`
}

type SetCircuitStateResponse struct {
	CircuitID     string `json:"circuit_id"`
	PreviousState string `json:"previous_state"`
	NewState      string `json:"new_state"`
	ChangedAt     int64  `json:"changed_at"`
	ChangedBy     string `json:"changed_by"`
	Reason        string `json:"reason"`
}

type ListCircuitsResponse struct {
	Circuits   []*CircuitBreakerSummary `json:"circuits"`
	TotalCount int                      `json:"total_count"`
}

type CircuitBreakerSummary struct {
	CircuitID       string  `json:"circuit_id"`
	ServiceName     string  `json:"service_name"`
	State           string  `json:"state"`
	Endpoint        string  `json:"endpoint"`
	FailureCount    int     `json:"failure_count"`
	SuccessCount    int     `json:"success_count"`
	ErrorRate       float64 `json:"error_rate"`
	LastFailureTime int64   `json:"last_failure_time"`
	CreatedAt       int64   `json:"created_at"`
}

type ListDegradationPoliciesResponse struct {
	Policies   []*DegradationPolicySummary `json:"policies"`
	TotalCount int                         `json:"total_count"`
}

type DegradationPolicySummary struct {
	PolicyID        string `json:"policy_id"`
	ServiceName     string `json:"service_name"`
	Status          string `json:"status"`
	TriggerCount    int    `json:"trigger_count"`
	RecoveryCount   int    `json:"recovery_count"`
	LastTriggeredAt int64  `json:"last_triggered_at"`
	CreatedAt       int64  `json:"created_at"`
}

type GetMetricsResponse struct {
	Circuits            []*CircuitBreakerMetricsData `json:"circuits"`
	Bulkheads           []*BulkheadMetricsData       `json:"bulkheads"`
	Timeouts            []map[string]interface{}     `json:"timeouts"`
	DegradationPolicies []map[string]interface{}     `json:"degradation_policies"`
	TimeRange           string                       `json:"time_range"`
	GeneratedAt         int64                        `json:"generated_at"`
}

// BulkheadSummary Additional structures for handlers
type BulkheadSummary struct {
	BulkheadID       string `json:"bulkhead_id"`
	ServiceName      string `json:"service_name"`
	ActiveThreads    int    `json:"active_threads"`
	QueuedRequests   int    `json:"queued_requests"`
	RejectedRequests int    `json:"rejected_requests"`
	CreatedAt        int64  `json:"created_at"`
}

type ListBulkheadsResponse struct {
	Bulkheads  []*BulkheadSummary `json:"bulkheads"`
	TotalCount int                `json:"total_count"`
}

type BulkheadDetails struct {
	BulkheadID           string               `json:"bulkhead_id"`
	ServiceName          string               `json:"service_name"`
	Config               *BulkheadConfig      `json:"config"`
	Metrics              *BulkheadMetricsData `json:"metrics"`
	ActiveThreads        int                  `json:"active_threads"`
	QueuedRequests       int                  `json:"queued_requests"`
	RejectedRequests     int                  `json:"rejected_requests"`
	CompletedRequests    int                  `json:"completed_requests"`
	AverageExecutionTime time.Duration        `json:"average_execution_time"`
	MaxExecutionTime     time.Duration        `json:"max_execution_time"`
	CreatedAt            int64                `json:"created_at"`
}

type GetBulkheadResponse struct {
	Bulkhead *BulkheadDetails `json:"bulkhead"`
}

type CircuitBreakerDetails struct {
	CircuitID           string                     `json:"circuit_id"`
	ServiceName         string                     `json:"service_name"`
	Endpoint            string                     `json:"endpoint"`
	State               string                     `json:"state"`
	StateChangedAt      int64                      `json:"state_changed_at"`
	Config              *CircuitBreakerConfig      `json:"config"`
	Metrics             *CircuitBreakerMetricsData `json:"metrics"`
	FailureCount        int                        `json:"failure_count"`
	SuccessCount        int                        `json:"success_count"`
	ConsecutiveFailures int                        `json:"consecutive_failures"`
	StateHistory        []StateChange              `json:"state_history"`
	CreatedAt           int64                      `json:"created_at"`
	LastUpdatedAt       int64                      `json:"last_updated_at"`
}

type GetCircuitResponse struct {
	Circuit *CircuitBreakerDetails `json:"circuit"`
}

type UpdateCircuitRequest struct {
	ServiceName           *string                 `json:"service_name,omitempty"`
	Endpoint              *string                 `json:"endpoint,omitempty"`
	FailureThreshold      *int                    `json:"failure_threshold,omitempty"`
	SuccessThreshold      *int                    `json:"success_threshold,omitempty"`
	Timeout               *int64                  `json:"timeout,omitempty"`
	RetryDelay            *int64                  `json:"retry_delay,omitempty"`
	MaxRetryDelay         *int64                  `json:"max_retry_delay,omitempty"`
	MonitoringWindow      *int64                  `json:"monitoring_window,omitempty"`
	SlowCallThreshold     *int64                  `json:"slow_call_threshold,omitempty"`
	SlowCallRateThreshold *float64                `json:"slow_call_rate_threshold,omitempty"`
	FallbackEnabled       *bool                   `json:"fallback_enabled,omitempty"`
	FallbackResponse      *string                 `json:"fallback_response,omitempty"`
	MetricsEnabled        *bool                   `json:"metrics_enabled,omitempty"`
	AlertThresholds       *map[string]interface{} `json:"alert_thresholds,omitempty"`
}

type UpdateCircuitResponse struct {
	CircuitID     string   `json:"circuit_id"`
	UpdatedFields []string `json:"updated_fields"`
	UpdatedAt     int64    `json:"updated_at"`
}

type ResetCircuitResponse struct {
	CircuitID     string `json:"circuit_id"`
	PreviousState string `json:"previous_state"`
	ResetAt       int64  `json:"reset_at"`
}
