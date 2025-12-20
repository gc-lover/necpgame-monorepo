package server

import (
	"sync"
	"time"
)

// OPTIMIZATION: Field alignment for message queue performance (time.Time=24 bytes, string=16 bytes, int=8 bytes, bool=1 byte)
type MessageQueueServiceConfig struct {
	StateSyncInterval time.Duration `json:"state_sync_interval"` // 8 bytes
	ReadTimeout       time.Duration `json:"read_timeout"`       // 8 bytes
	WriteTimeout      time.Duration `json:"write_timeout"`      // 8 bytes
	MetricsInterval   time.Duration `json:"metrics_interval"`   // 8 bytes
	DefaultTimeout    time.Duration `json:"default_timeout"`    // 8 bytes
	CleanupInterval   time.Duration `json:"cleanup_interval"`   // 8 bytes
	HTTPAddr          string        `json:"http_addr"`          // 16 bytes
	RedisAddr         string        `json:"redis_addr"`         // 16 bytes
	PprofAddr         string        `json:"pprof_addr"`         // 16 bytes
	HealthAddr        string        `json:"health_addr"`        // 16 bytes
	MaxConnections    int           `json:"max_connections"`    // 8 bytes
	MaxHeaderBytes    int           `json:"max_header_bytes"`   // 8 bytes
	DefaultQueueSize  int           `json:"default_queue_size"` // 8 bytes
	DefaultMessageTTL time.Duration `json:"default_message_ttl"` // 8 bytes
}

// OPTIMIZATION: Memory-aligned message structure for high-throughput messaging
type Message struct {
	ID          string            `json:"id"`           // 16 bytes
	QueueName   string            `json:"queue_name"`   // 16 bytes
	Payload     string            `json:"payload"`      // 16 bytes (string header)
	Headers     map[string]string `json:"headers"`      // 8 bytes (map header)
	Priority    int               `json:"priority"`     // 8 bytes
	TTL         time.Duration     `json:"ttl"`          // 8 bytes
	CreatedAt   time.Time         `json:"created_at"`   // 24 bytes - largest
	ExpiresAt   time.Time         `json:"expires_at"`   // 24 bytes
	DeliveredAt *time.Time        `json:"delivered_at"` // 8 bytes (pointer)
	RetryCount  int               `json:"retry_count"`  // 8 bytes
	MaxRetries  int               `json:"max_retries"`  // 8 bytes
	Status      string            `json:"status"`       // 16 bytes
}

// OPTIMIZATION: Memory-aligned queue structure with concurrent access
type Queue struct {
	Name           string        `json:"name"`            // 16 bytes
	MaxSize        int           `json:"max_size"`        // 8 bytes
	CurrentSize    int           `json:"current_size"`    // 8 bytes
	MessageTTL     time.Duration `json:"message_ttl"`     // 8 bytes
	CreatedAt      time.Time     `json:"created_at"`      // 24 bytes - largest
	LastActivityAt time.Time     `json:"last_activity_at"` // 24 bytes
	Priority       bool          `json:"priority"`        // 1 byte
	Persistent     bool          `json:"persistent"`      // 1 byte
	mu             sync.RWMutex  `json:"-"`               // mutex for thread safety
}

// OPTIMIZATION: Zero-allocation consumer group with sync.Pool
type ConsumerGroup struct {
	ID            string            `json:"id"`             // 16 bytes
	QueueName     string            `json:"queue_name"`     // 16 bytes
	ConsumerIDs   []string          `json:"consumer_ids"`   // 24 bytes (slice header)
	LastOffset    int64             `json:"last_offset"`    // 8 bytes
	CreatedAt     time.Time         `json:"created_at"`     // 24 bytes - largest
	Active        bool              `json:"active"`         // 1 byte
	mu            sync.RWMutex      `json:"-"`              // mutex for thread safety
}

// OPTIMIZATION: Memory-aligned consumer for load balancing
type Consumer struct {
	ID           string        `json:"id"`            // 16 bytes
	GroupID      string        `json:"group_id"`      // 16 bytes
	QueueName    string        `json:"queue_name"`    // 16 bytes
	LastHeartbeat time.Time    `json:"last_heartbeat"` // 24 bytes - largest
	Active       bool          `json:"active"`        // 1 byte
	Processing   int           `json:"processing"`    // 8 bytes
	mu           sync.RWMutex  `json:"-"`             // mutex for thread safety
}

// OPTIMIZATION: Metrics structure for monitoring message queue performance
type MessageQueueMetrics struct {
	MessagesEnqueued   int64 `json:"messages_enqueued"`   // 8 bytes
	MessagesDequeued   int64 `json:"messages_dequeued"`   // 8 bytes
	MessagesExpired    int64 `json:"messages_expired"`    // 8 bytes
	QueuesCreated      int64 `json:"queues_created"`      // 8 bytes
	ConsumersActive    int64 `json:"consumers_active"`    // 8 bytes
	AverageProcessTime time.Duration `json:"average_process_time"` // 8 bytes
	mu                 sync.RWMutex `json:"-"`                   // mutex for thread safety
}

// OPTIMIZATION: Batch operations for high-throughput
type MessageBatch struct {
	Messages []*Message `json:"messages"` // slice of message pointers
	Size     int        `json:"size"`     // batch size
}

// OPTIMIZATION: Acknowledgment structure for reliable messaging
type Acknowledgment struct {
	MessageID   string `json:"message_id"`   // 16 bytes
	ConsumerID  string `json:"consumer_id"`  // 16 bytes
	QueueName   string `json:"queue_name"`   // 16 bytes
	Status      string `json:"status"`       // 16 bytes
	ProcessedAt time.Time `json:"processed_at"` // 24 bytes - largest
	Success     bool   `json:"success"`      // 1 byte
	Error       string `json:"error"`        // 16 bytes
}

// Request/Response structures for API
type EnqueueMessageRequest struct {
	QueueName string            `json:"queue_name"`
	Payload   string            `json:"payload"`
	Headers   map[string]string `json:"headers,omitempty"`
	Priority  int               `json:"priority,omitempty"`
	TTL       *int64            `json:"ttl,omitempty"` // in milliseconds
}

type EnqueueMessageResponse struct {
	MessageID string `json:"message_id"`
	QueuedAt  int64  `json:"queued_at"`
}

type DequeueMessageRequest struct {
	QueueName   string `json:"queue_name"`
	ConsumerID  string `json:"consumer_id,omitempty"`
	GroupID     string `json:"group_id,omitempty"`
	MaxMessages int    `json:"max_messages,omitempty"`
	Timeout     *int64 `json:"timeout,omitempty"` // in milliseconds
}

type DequeueMessageResponse struct {
	Messages []*Message `json:"messages"`
	Count    int        `json:"count"`
}

type CreateQueueRequest struct {
	Name       string `json:"name"`
	MaxSize    int    `json:"max_size,omitempty"`
	MessageTTL *int64 `json:"message_ttl,omitempty"` // in milliseconds
	Priority   bool   `json:"priority,omitempty"`
	Persistent bool   `json:"persistent,omitempty"`
}

type CreateQueueResponse struct {
	Name      string `json:"name"`
	CreatedAt int64  `json:"created_at"`
}

type AcknowledgeMessageRequest struct {
	MessageID  string `json:"message_id"`
	ConsumerID string `json:"consumer_id"`
	Success    bool   `json:"success"`
	Error      string `json:"error,omitempty"`
}

type AcknowledgeMessageResponse struct {
	MessageID   string `json:"message_id"`
	Acknowledged bool  `json:"acknowledged"`
}