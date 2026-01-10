// Package consumer implements Kafka event-driven consumer for economy service
// Issue: #2237 - Kafka Event-Driven Architecture Implementation
// Agent: Backend Agent
package consumer

import (
	"time"
)

// TickEvent represents the complete tick event structure from simulation-ticker-service
// Follows proto/kafka/schemas/world/world-tick-events.json schema
// Issue: #2237
type TickEvent struct {
	// Base event fields (ordered for struct alignment: large → small)
	EventID      string    `json:"event_id"`
	EventType    string    `json:"event_type"`
	Timestamp    string    `json:"timestamp"` // RFC3339 format
	Version      string    `json:"version"`
	Source       string    `json:"source"`
	CorrelationID string   `json:"correlation_id,omitempty"`
	SessionID    string    `json:"session_id,omitempty"`
	PlayerID     string    `json:"player_id,omitempty"`
	GameID       string    `json:"game_id,omitempty"`
	Tags         []string  `json:"tags,omitempty"`
	TraceID      string    `json:"trace_id,omitempty"`

	// Tick-specific data
	Data         TickData  `json:"data"`
	Metadata     EventMetadata `json:"metadata,omitempty"`
}

// TickData represents tick-specific data payload
// Issue: #2237
type TickData struct {
	// Hourly tick fields
	TickID        string    `json:"tick_id"`
	TickType      string    `json:"tick_type"`
	GameHour      *int      `json:"game_hour,omitempty"` // 0-23
	GameDay       *int      `json:"game_day,omitempty"`  // Day number
	GameTime      string    `json:"game_time"`           // RFC3339
	TickTimestamp string    `json:"tick_timestamp"`      // RFC3339
	TriggeredBy   string    `json:"triggered_by"`
	Consumers     []string  `json:"consumers,omitempty"`
	TickMetadata  *TickMetadata `json:"metadata,omitempty"`
}

// TickMetadata contains additional simulation metadata
// Issue: #2237
type TickMetadata struct {
	SimulationDay    *int    `json:"simulation_day,omitempty"`
	SimulationWeek   *int    `json:"simulation_week,omitempty"`
	SimulationMonth  *int    `json:"simulation_month,omitempty"`
	SimulationYear   *int    `json:"simulation_year,omitempty"`
	Season           string  `json:"season,omitempty"`
}

// EventMetadata follows base-event.json schema
// Issue: #2237
type EventMetadata struct {
	Priority    string `json:"priority,omitempty"`
	TTL         string `json:"ttl,omitempty"`
	RetryCount  int    `json:"retry_count,omitempty"`
	Compression string `json:"compression,omitempty"`
	SizeBytes   int    `json:"size_bytes,omitempty"`
}

// TickConsumerConfig holds configuration for the tick consumer
// Issue: #2237
type TickConsumerConfig struct {
	// Will be implemented in consumer.go
}

// ConsumerConfig holds configuration for Kafka consumer
// Issue: #2237
type ConsumerConfig struct {
	Brokers         []string      `yaml:"brokers"`
	GroupID         string        `yaml:"group_id"`
	Topic           string        `yaml:"topic"`
	SessionTimeout  time.Duration `yaml:"session_timeout"`
	HeartbeatInterval time.Duration `yaml:"heartbeat_interval"`
	CommitInterval  time.Duration `yaml:"commit_interval"`
	MaxProcessingTime time.Duration `yaml:"max_processing_time"`
}