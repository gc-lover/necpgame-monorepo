// Issue: #2217
// PERFORMANCE: Optimized PostgreSQL event store for high-throughput event sourcing
package store

import (
	"context"
	"encoding/json"
	"fmt"
	"os"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// PostgreSQLEventStore implements EventStore interface for PostgreSQL
type PostgreSQLEventStore struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// EventEnvelope wraps domain events with metadata for storage
type EventEnvelope struct {
	EventID     uuid.UUID       `json:"event_id"`
	AggregateID uuid.UUID       `json:"aggregate_id"`
	AggregateType string        `json:"aggregate_type"`
	EventType   string          `json:"event_type"`
	EventData   json.RawMessage `json:"event_data"`
	Version     int             `json:"version"`
	Timestamp   time.Time       `json:"timestamp"`
	Metadata    EventMetadata   `json:"metadata"`
}

// EventMetadata contains additional event metadata
type EventMetadata struct {
	UserID      *uuid.UUID `json:"user_id,omitempty"`
	SessionID   *uuid.UUID `json:"session_id,omitempty"`
	CorrelationID *uuid.UUID `json:"correlation_id,omitempty"`
	Source      string     `json:"source,omitempty"`
	Tags        []string   `json:"tags,omitempty"`
}

// EventStore defines the interface for event storage
type EventStore interface {
	// AppendEvents appends events to an aggregate's event stream
	AppendEvents(ctx context.Context, aggregateID uuid.UUID, aggregateType string, events []EventEnvelope, expectedVersion int) error

	// GetEvents retrieves events for an aggregate
	GetEvents(ctx context.Context, aggregateID uuid.UUID, fromVersion int) ([]EventEnvelope, error)

	// GetEventsByType retrieves events of a specific type across all aggregates
	GetEventsByType(ctx context.Context, eventType string, limit int, offset int) ([]EventEnvelope, error)

	// GetAggregateVersion returns the current version of an aggregate
	GetAggregateVersion(ctx context.Context, aggregateID uuid.UUID) (int, error)

	// Close closes the event store connection
	Close() error
}

// NewPostgreSQLEventStore creates a new PostgreSQL event store
func NewPostgreSQLEventStore(logger *zap.Logger) (*PostgreSQLEventStore, error) {
	// Get database connection from environment
	dbURL := getEnvOrDefault("DATABASE_URL", "postgres://user:password@localhost:5432/necp_game?sslmode=disable")

	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database URL: %w", err)
	}

	// Configure connection pool for event sourcing workloads
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = 30 * time.Minute
	config.MaxConnIdleTime = 5 * time.Minute

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, fmt.Errorf("failed to create connection pool: %w", err)
	}

	// Test connection
	if err := pool.Ping(context.Background()); err != nil {
		pool.Close()
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	store := &PostgreSQLEventStore{
		db:     pool,
		logger: logger,
	}

	// Ensure schema exists
	if err := store.ensureSchema(context.Background()); err != nil {
		pool.Close()
		return nil, fmt.Errorf("failed to ensure schema: %w", err)
	}

	logger.Info("PostgreSQL event store initialized",
		zap.String("database_url", dbURL),
		zap.Int("max_conns", int(config.MaxConns)),
		zap.Int("min_conns", int(config.MinConns)))

	return store, nil
}

// ensureSchema creates the necessary database schema if it doesn't exist
func (s *PostgreSQLEventStore) ensureSchema(ctx context.Context) error {
	// Create events table with optimized indexing for event sourcing
	schema := `
	CREATE TABLE IF NOT EXISTS event_store (
		event_id UUID PRIMARY KEY,
		aggregate_id UUID NOT NULL,
		aggregate_type VARCHAR(255) NOT NULL,
		event_type VARCHAR(255) NOT NULL,
		event_data JSONB NOT NULL,
		version INTEGER NOT NULL,
		timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
		metadata JSONB,
		created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

		-- Optimized indexes for event sourcing queries
		CONSTRAINT event_store_aggregate_version UNIQUE (aggregate_id, version)
	);

	-- Index for aggregate queries (most frequent)
	CREATE INDEX IF NOT EXISTS idx_event_store_aggregate
	ON event_store (aggregate_id, version ASC);

	-- Index for event type queries
	CREATE INDEX IF NOT EXISTS idx_event_store_event_type
	ON event_store (event_type, timestamp DESC);

	-- Index for time-based queries
	CREATE INDEX IF NOT EXISTS idx_event_store_timestamp
	ON event_store (timestamp DESC);

	-- Partial index for recent events (optimization)
	CREATE INDEX IF NOT EXISTS idx_event_store_recent
	ON event_store (aggregate_id, version DESC)
	WHERE timestamp > NOW() - INTERVAL '30 days';

	-- Aggregate metadata table for optimistic concurrency
	CREATE TABLE IF NOT EXISTS aggregate_metadata (
		aggregate_id UUID PRIMARY KEY,
		aggregate_type VARCHAR(255) NOT NULL,
		current_version INTEGER NOT NULL DEFAULT 0,
		snapshot_version INTEGER NOT NULL DEFAULT 0,
		last_event_timestamp TIMESTAMP WITH TIME ZONE,
		created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
		updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
	);

	-- Index for aggregate metadata queries
	CREATE INDEX IF NOT EXISTS idx_aggregate_metadata_type
	ON aggregate_metadata (aggregate_type);
	`

	_, err := s.db.Exec(ctx, schema)
	if err != nil {
		return fmt.Errorf("failed to create schema: %w", err)
	}

	s.logger.Info("Event store schema ensured")
	return nil
}

// AppendEvents appends events to an aggregate's event stream with optimistic concurrency
func (s *PostgreSQLEventStore) AppendEvents(ctx context.Context, aggregateID uuid.UUID, aggregateType string, events []EventEnvelope, expectedVersion int) error {
	if len(events) == 0 {
		return nil
	}

	startTime := time.Now()

	// Begin transaction for atomic append
	tx, err := s.db.Begin(ctx)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback(ctx)

	// Check current version for optimistic concurrency
	var currentVersion int
	err = tx.QueryRow(ctx, `
		SELECT COALESCE(MAX(current_version), 0)
		FROM aggregate_metadata
		WHERE aggregate_id = $1
	`, aggregateID).Scan(&currentVersion)

	if err != nil && err != pgx.ErrNoRows {
		return fmt.Errorf("failed to get current version: %w", err)
	}

	// Optimistic concurrency check
	if expectedVersion != currentVersion {
		return fmt.Errorf("concurrency conflict: expected version %d, got %d", expectedVersion, currentVersion)
	}

	// Insert events in batch
	for i, event := range events {
		event.Version = expectedVersion + i + 1

		metadataJSON, err := json.Marshal(event.Metadata)
		if err != nil {
			return fmt.Errorf("failed to marshal metadata: %w", err)
		}

		_, err = tx.Exec(ctx, `
			INSERT INTO event_store (
				event_id, aggregate_id, aggregate_type, event_type,
				event_data, version, timestamp, metadata
			) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
		`, event.EventID, aggregateID, aggregateType, event.EventType,
		   event.EventData, event.Version, event.Timestamp, metadataJSON)

		if err != nil {
			return fmt.Errorf("failed to insert event %d: %w", i, err)
		}
	}

	// Update aggregate metadata
	newVersion := expectedVersion + len(events)
	_, err = tx.Exec(ctx, `
		INSERT INTO aggregate_metadata (
			aggregate_id, aggregate_type, current_version,
			last_event_timestamp, updated_at
		) VALUES ($1, $2, $3, $4, NOW())
		ON CONFLICT (aggregate_id) DO UPDATE SET
			current_version = EXCLUDED.current_version,
			last_event_timestamp = EXCLUDED.last_event_timestamp,
			updated_at = NOW()
	`, aggregateID, aggregateType, newVersion, events[len(events)-1].Timestamp)

	if err != nil {
		return fmt.Errorf("failed to update aggregate metadata: %w", err)
	}

	// Commit transaction
	if err := tx.Commit(ctx); err != nil {
		return fmt.Errorf("failed to commit transaction: %w", err)
	}

	duration := time.Since(startTime)
	s.logger.Info("Events appended successfully",
		zap.String("aggregate_id", aggregateID.String()),
		zap.String("aggregate_type", aggregateType),
		zap.Int("event_count", len(events)),
		zap.Int("new_version", newVersion),
		zap.Duration("duration", duration))

	return nil
}

// GetEvents retrieves events for an aggregate from a specific version
func (s *PostgreSQLEventStore) GetEvents(ctx context.Context, aggregateID uuid.UUID, fromVersion int) ([]EventEnvelope, error) {
	startTime := time.Now()

	rows, err := s.db.Query(ctx, `
		SELECT event_id, aggregate_id, aggregate_type, event_type,
			   event_data, version, timestamp, metadata
		FROM event_store
		WHERE aggregate_id = $1 AND version >= $2
		ORDER BY version ASC
	`, aggregateID, fromVersion)

	if err != nil {
		return nil, fmt.Errorf("failed to query events: %w", err)
	}
	defer rows.Close()

	var events []EventEnvelope
	for rows.Next() {
		var event EventEnvelope
		var metadataJSON []byte

		err := rows.Scan(
			&event.EventID, &event.AggregateID, &event.AggregateType,
			&event.EventType, &event.EventData, &event.Version,
			&event.Timestamp, &metadataJSON,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan event: %w", err)
		}

		if len(metadataJSON) > 0 {
			if err := json.Unmarshal(metadataJSON, &event.Metadata); err != nil {
				return nil, fmt.Errorf("failed to unmarshal metadata: %w", err)
			}
		}

		events = append(events, event)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating events: %w", err)
	}

	duration := time.Since(startTime)
	s.logger.Debug("Events retrieved",
		zap.String("aggregate_id", aggregateID.String()),
		zap.Int("from_version", fromVersion),
		zap.Int("event_count", len(events)),
		zap.Duration("duration", duration))

	return events, nil
}

// GetEventsByType retrieves events of a specific type across all aggregates
func (s *PostgreSQLEventStore) GetEventsByType(ctx context.Context, eventType string, limit int, offset int) ([]EventEnvelope, error) {
	startTime := time.Now()

	if limit <= 0 {
		limit = 100
	}
	if limit > 1000 {
		limit = 1000 // Safety limit
	}

	rows, err := s.db.Query(ctx, `
		SELECT event_id, aggregate_id, aggregate_type, event_type,
			   event_data, version, timestamp, metadata
		FROM event_store
		WHERE event_type = $1
		ORDER BY timestamp DESC
		LIMIT $2 OFFSET $3
	`, eventType, limit, offset)

	if err != nil {
		return nil, fmt.Errorf("failed to query events by type: %w", err)
	}
	defer rows.Close()

	var events []EventEnvelope
	for rows.Next() {
		var event EventEnvelope
		var metadataJSON []byte

		err := rows.Scan(
			&event.EventID, &event.AggregateID, &event.AggregateType,
			&event.EventType, &event.EventData, &event.Version,
			&event.Timestamp, &metadataJSON,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan event: %w", err)
		}

		if len(metadataJSON) > 0 {
			if err := json.Unmarshal(metadataJSON, &event.Metadata); err != nil {
				return nil, fmt.Errorf("failed to unmarshal metadata: %w", err)
			}
		}

		events = append(events, event)
	}

	duration := time.Since(startTime)
	s.logger.Debug("Events retrieved by type",
		zap.String("event_type", eventType),
		zap.Int("limit", limit),
		zap.Int("offset", offset),
		zap.Int("event_count", len(events)),
		zap.Duration("duration", duration))

	return events, nil
}

// GetAggregateVersion returns the current version of an aggregate
func (s *PostgreSQLEventStore) GetAggregateVersion(ctx context.Context, aggregateID uuid.UUID) (int, error) {
	var version int
	err := s.db.QueryRow(ctx, `
		SELECT COALESCE(current_version, 0)
		FROM aggregate_metadata
		WHERE aggregate_id = $1
	`, aggregateID).Scan(&version)

	if err != nil && err != pgx.ErrNoRows {
		return 0, fmt.Errorf("failed to get aggregate version: %w", err)
	}

	return version, nil
}

// Close closes the database connection pool
func (s *PostgreSQLEventStore) Close() error {
	s.db.Close()
	s.logger.Info("PostgreSQL event store closed")
	return nil
}

// getEnvOrDefault returns environment variable value or default
func getEnvOrDefault(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
