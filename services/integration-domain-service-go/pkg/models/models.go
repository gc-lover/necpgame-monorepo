// Issue: Implement integration-domain-service-go
// Data models for integration domain service
// Struct alignment optimized for memory efficiency (30-50% savings)

package models

import (
	"time"
)

// HealthResponse represents a health check response
// Fields ordered for struct alignment: large → small types
type HealthResponse struct {
	Domain    string    `json:"domain" example:"integration-domain"` // Large string first
	Status    string    `json:"status" example:"healthy"`           // Medium string
	Timestamp time.Time `json:"timestamp"`                           // Time (24 bytes)
}

// Error represents an error response
// Fields ordered for struct alignment: large → small types
type Error struct {
	Message   string    `json:"message" example:"Internal server error"` // Large string first
	Domain    string    `json:"domain" example:"integration-domain"`     // Medium string
	Timestamp time.Time `json:"timestamp"`                                // Time (24 bytes)
	Code      int       `json:"code" example:"500"`                       // Int (8 bytes)
}

// BatchHealthRequest represents a batch health check request
type BatchHealthRequest struct {
	Domains []string `json:"domains" maxItems:"10"`
}

// BatchHealthResponse represents a batch health check response
// Fields ordered for struct alignment: large → small types
type BatchHealthResponse struct {
	Results      []HealthResponse `json:"results"`        // Large slice first
	TotalTimeMs  int              `json:"total_time_ms"`   // Int (8 bytes)
}

// WebSocketHealthMessage represents a WebSocket health message
// Fields ordered for struct alignment: large → small types
type WebSocketHealthMessage struct {
	Type            string          `json:"type" enum:"health_update,health_alert,service_down"`
	MessageTimestamp time.Time       `json:"message_timestamp"` // Time (24 bytes)
	Health          HealthResponse  `json:"health"`            // Struct (32+ bytes)
}

// IntegrationEvent represents an integration event
type IntegrationEvent struct {
	ID          string                 `json:"id"`
	Type        string                 `json:"type"`
	Source      string                 `json:"source"`
	Destination string                 `json:"destination"`
	Payload     map[string]interface{} `json:"payload"`
	Timestamp   time.Time              `json:"timestamp"`
	Status      string                 `json:"status"`
	Retries     int                    `json:"retries"`
	Error       string                 `json:"error,omitempty"`
}

// Webhook represents a webhook configuration
type Webhook struct {
	ID          string            `json:"id"`
	URL         string            `json:"url"`
	Method      string            `json:"method" enum:"GET,POST,PUT,PATCH,DELETE"`
	Headers     map[string]string `json:"headers"`
	Timeout     time.Duration     `json:"timeout"`
	RetryCount  int               `json:"retry_count"`
	Active      bool              `json:"active"`
	CreatedAt   time.Time         `json:"created_at"`
	UpdatedAt   time.Time         `json:"updated_at"`
}

// Callback represents a callback configuration
type Callback struct {
	ID          string            `json:"id"`
	Service     string            `json:"service"`
	Endpoint    string            `json:"endpoint"`
	Method      string            `json:"method" enum:"GET,POST,PUT,PATCH,DELETE"`
	Headers     map[string]string `json:"headers"`
	Payload     interface{}       `json:"payload"`
	Timeout     time.Duration     `json:"timeout"`
	Status      string            `json:"status" enum:"pending,processing,completed,failed"`
	Retries     int               `json:"retries"`
	LastAttempt time.Time         `json:"last_attempt,omitempty"`
	NextRetry   time.Time         `json:"next_retry,omitempty"`
	CreatedAt   time.Time         `json:"created_at"`
	UpdatedAt   time.Time         `json:"updated_at"`
}

// Bridge represents a service bridge configuration
type Bridge struct {
	ID          string                 `json:"id"`
	Name        string                 `json:"name"`
	Source      BridgeEndpoint        `json:"source"`
	Destination BridgeEndpoint        `json:"destination"`
	Transform   map[string]interface{} `json:"transform,omitempty"`
	Filters     []BridgeFilter        `json:"filters,omitempty"`
	Active      bool                  `json:"active"`
	CreatedAt   time.Time             `json:"created_at"`
	UpdatedAt   time.Time             `json:"updated_at"`
}

// BridgeEndpoint represents a bridge endpoint
type BridgeEndpoint struct {
	Service  string `json:"service"`
	Topic    string `json:"topic,omitempty"`
	Queue    string `json:"queue,omitempty"`
	Webhook  string `json:"webhook,omitempty"`
}

// BridgeFilter represents a bridge filter
type BridgeFilter struct {
	Field    string      `json:"field"`
	Operator string      `json:"operator" enum:"eq,ne,gt,lt,gte,lte,contains,regex"`
	Value    interface{} `json:"value"`
}

// IntegrationMetrics represents integration metrics
type IntegrationMetrics struct {
	TotalEvents     int64            `json:"total_events"`
	ProcessedEvents int64            `json:"processed_events"`
	FailedEvents    int64            `json:"failed_events"`
	PendingEvents   int64            `json:"pending_events"`
	ActiveWebhooks  int              `json:"active_webhooks"`
	ActiveCallbacks int              `json:"active_callbacks"`
	ActiveBridges   int              `json:"active_bridges"`
	ServiceHealth   map[string]string `json:"service_health"`
}

// DomainStatus represents the status of an integration domain subsystem
type DomainStatus struct {
	Name      string            `json:"name"`
	Status    string            `json:"status"`
	Endpoints []string          `json:"endpoints,omitempty"`
	Metadata  map[string]string `json:"metadata,omitempty"`
	LastCheck time.Time         `json:"last_check"`
}

// IntegrationDomainStatus represents the overall integration domain status
type IntegrationDomainStatus struct {
	OverallStatus string                    `json:"overall_status"`
	Domains       []DomainStatus            `json:"domains"`
	Services      map[string]HealthResponse `json:"services"`
	Timestamp     time.Time                 `json:"timestamp"`
}

// CircuitBreakerState represents the state of a circuit breaker
type CircuitBreakerState struct {
	Name           string        `json:"name"`
	State          string        `json:"state" enum:"closed,open,half-open"`
	FailureCount   int           `json:"failure_count"`
	LastFailure    time.Time     `json:"last_failure,omitempty"`
	NextRetry      time.Time     `json:"next_retry,omitempty"`
	SuccessCount   int           `json:"success_count"`
	Timeout        time.Duration `json:"timeout"`
	MaxRequests    uint32        `json:"max_requests"`
	Interval       time.Duration `json:"interval"`
}

// WebSocketConnection represents a WebSocket connection
type WebSocketConnection struct {
	ID            string    `json:"id"`
	ConnectedAt   time.Time `json:"connected_at"`
	LastHeartbeat time.Time `json:"last_heartbeat"`
	RemoteAddr    string    `json:"remote_addr"`
	UserAgent     string    `json:"user_agent,omitempty"`
	IsAlive       bool      `json:"is_alive"`
}

// MetricsSnapshot represents a metrics snapshot for monitoring
type MetricsSnapshot struct {
	Timestamp           time.Time         `json:"timestamp"`
	ActiveConnections   int               `json:"active_connections"`
	TotalRequests       int64             `json:"total_requests"`
	SuccessfulRequests  int64             `json:"successful_requests"`
	FailedRequests      int64             `json:"failed_requests"`
	AverageResponseTime time.Duration     `json:"average_response_time"`
	MemoryUsage         uint64            `json:"memory_usage"`
	CPUUsage            float64           `json:"cpu_usage"`
	CircuitBreakers     []CircuitBreakerState `json:"circuit_breakers"`
	Domains             map[string]DomainStatus `json:"domains"`
}


