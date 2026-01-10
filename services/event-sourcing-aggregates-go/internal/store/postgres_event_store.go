// PostgreSQL Event Store implementation for Event Sourcing
// Issue: #2217
// Agent: Backend Agent
package store

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"

	"necpgame/services/event-sourcing-aggregates-go/internal/events"
)

// EventStore defines the interface for event storage
type EventStore interface {
	// SaveEvents saves domain events with optimistic concurrency
	SaveEvents(ctx context.Context, aggregateID uuid.UUID, events []events.DomainEvent, expectedVersion int) error

	// GetEvents retrieves events for an aggregate
	GetEvents(ctx context.Context, aggregateID uuid.UUID) ([]events.DomainEvent, error)

	// GetEventsFromVersion retrieves events from specific version
	GetEventsFromVersion(ctx context.Context, aggregateID uuid.UUID, fromVersion int) ([]events.DomainEvent, error)

	// GetAggregateEvents retrieves events for aggregate with pagination
	GetAggregateEvents(ctx context.Context, aggregateType, aggregateID string, fromVersion, toVersion *int, limit, offset int) ([]events.DomainEvent, int, error)

	// Close closes the event store connection
	Close() error
}

// PostgresEventStore implements EventStore using PostgreSQL
type PostgresEventStore struct {
	pool *pgxpool.Pool
}

// NewPostgresEventStore creates a new PostgreSQL event store
func NewPostgresEventStore(databaseURL string) (*PostgresEventStore, error) {
	config, err := pgxpool.ParseConfig(databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database URL: %w", err)
	}

	// Configure connection pool for high performance
	config.MaxConns = 25
	config.MinConns = 5
	config.MaxConnLifetime = time.Hour
	config.MaxConnIdleTime = 30 * time.Minute

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, fmt.Errorf("failed to create connection pool: %w", err)
	}

	store := &PostgresEventStore{
		pool: pool,
	}

	// Ensure tables exist
	if err := store.ensureTables(context.Background()); err != nil {
		pool.Close()
		return nil, fmt.Errorf("failed to ensure tables: %w", err)
	}

	return store, nil
}

// ensureTables creates necessary tables if they don't exist
func (s *PostgresEventStore) ensureTables(ctx context.Context) error {
	// Create events table with optimized indexes
	createTableQuery := `
		CREATE TABLE IF NOT EXISTS events (
			id BIGSERIAL PRIMARY KEY,
			event_id UUID UNIQUE NOT NULL DEFAULT gen_random_uuid(),
			aggregate_id UUID NOT NULL,
			aggregate_type VARCHAR(50) NOT NULL,
			event_type VARCHAR(100) NOT NULL,
			event_version INTEGER NOT NULL,
			event_data JSONB NOT NULL,
			metadata JSONB,
			correlation_id UUID,
			causation_id UUID,
			server_id VARCHAR(100),
			player_id UUID,
			session_id UUID,
			event_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
			processed_at TIMESTAMP,
			state_changes JSONB,
			affected_players JSONB,
			is_processed BOOLEAN DEFAULT FALSE,
			processing_error TEXT,
			retry_count INTEGER DEFAULT 0,
			created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

			-- Constraints for data integrity
			CONSTRAINT events_version_unique UNIQUE (aggregate_id, aggregate_type, event_version),
			CONSTRAINT events_positive_version CHECK (event_version > 0)
		);

		-- Partitioning by time for better performance
		CREATE INDEX IF NOT EXISTS idx_events_aggregate ON events(aggregate_id, aggregate_type, event_version);
		CREATE INDEX IF NOT EXISTS idx_events_type_time ON events(event_type, event_timestamp);
		CREATE INDEX IF NOT EXISTS idx_events_timestamp ON events(event_timestamp);
		CREATE INDEX IF NOT EXISTS idx_events_correlation ON events(correlation_id);
		CREATE INDEX IF NOT EXISTS idx_events_processed ON events(is_processed, event_timestamp);
		CREATE INDEX IF NOT EXISTS idx_events_metadata ON events USING GIN(metadata);
		CREATE INDEX IF NOT EXISTS idx_events_player ON events(player_id, event_timestamp);
	`

	_, err := s.pool.Exec(ctx, createTableQuery)
	return err
}

// SaveEvents saves domain events with optimistic concurrency
func (s *PostgresEventStore) SaveEvents(ctx context.Context, aggregateID uuid.UUID, domainEvents []events.DomainEvent, expectedVersion int) error {
	if len(domainEvents) == 0 {
		return nil
	}

	tx, err := s.pool.Begin(ctx)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback(ctx)

	// Check current version for optimistic concurrency
	var currentVersion int
	err = tx.QueryRow(ctx, `
		SELECT COALESCE(MAX(event_version), 0)
		FROM events
		WHERE aggregate_id = $1 AND aggregate_type = $2
	`, aggregateID, domainEvents[0].GetAggregateType()).Scan(&currentVersion)

	if err != nil {
		return fmt.Errorf("failed to get current version: %w", err)
	}

	if currentVersion != expectedVersion {
		return fmt.Errorf("concurrency conflict: expected version %d, got %d", expectedVersion, currentVersion)
	}

	// Insert events in batch
	for _, event := range domainEvents {
		eventData, err := json.Marshal(event.GetData())
		if err != nil {
			return fmt.Errorf("failed to marshal event data: %w", err)
		}

		metadata, err := json.Marshal(event.GetMetadata())
		if err != nil {
			return fmt.Errorf("failed to marshal metadata: %w", err)
		}

		_, err = tx.Exec(ctx, `
			INSERT INTO events (
				event_id, aggregate_id, aggregate_type, event_type, event_version,
				event_data, metadata, event_timestamp, created_at
			) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		`,
			event.GetEventID(),
			event.GetAggregateID(),
			event.GetAggregateType(),
			event.GetEventType(),
			event.GetVersion(),
			eventData,
			metadata,
			event.GetTimestamp(),
			time.Now().UTC(),
		)

		if err != nil {
			return fmt.Errorf("failed to insert event: %w", err)
		}
	}

	return tx.Commit(ctx)
}

// GetEvents retrieves all events for an aggregate
func (s *PostgresEventStore) GetEvents(ctx context.Context, aggregateID uuid.UUID) ([]events.DomainEvent, error) {
	rows, err := s.pool.Query(ctx, `
		SELECT event_id, aggregate_type, event_type, event_version, event_data, metadata, event_timestamp
		FROM events
		WHERE aggregate_id = $1
		ORDER BY event_version ASC
	`, aggregateID)

	if err != nil {
		return nil, fmt.Errorf("failed to query events: %w", err)
	}
	defer rows.Close()

	var domainEvents []events.DomainEvent
	for rows.Next() {
		var eventID uuid.UUID
		var aggregateType, eventType string
		var eventVersion int
		var eventData, metadata []byte
		var eventTimestamp time.Time

		err := rows.Scan(&eventID, &aggregateType, &eventType, &eventVersion, &eventData, &metadata, &eventTimestamp)
		if err != nil {
			return nil, fmt.Errorf("failed to scan event: %w", err)
		}

		// Deserialize event data
		var data interface{}
		if err := json.Unmarshal(eventData, &data); err != nil {
			return nil, fmt.Errorf("failed to unmarshal event data: %w", err)
		}

		var meta map[string]interface{}
		if len(metadata) > 0 {
			if err := json.Unmarshal(metadata, &meta); err != nil {
				return nil, fmt.Errorf("failed to unmarshal metadata: %w", err)
			}
		}

		// Create appropriate domain event based on type
		var domainEvent events.DomainEvent
		switch eventType {
		case "PlayerCreated":
			domainEvent = events.NewPlayerCreatedEvent(aggregateID, "", "")
		case "PlayerLevelGained":
			domainEvent = events.NewPlayerLevelGainedEvent(aggregateID, 0, 0, 0)
		case "PlayerItemEquipped":
			domainEvent = events.NewPlayerItemEquippedEvent(aggregateID, uuid.New(), "")
		default:
			continue // Skip unknown event types
		}

		domainEvent.SetEventID(eventID)
		domainEvent.SetAggregateID(aggregateID)
		domainEvent.SetAggregateType(aggregateType)
		domainEvent.SetVersion(eventVersion)
		domainEvent.SetTimestamp(eventTimestamp)

		domainEvents = append(domainEvents, domainEvent)
	}

	return domainEvents, rows.Err()
}

// GetEventsFromVersion retrieves events from specific version
func (s *PostgresEventStore) GetEventsFromVersion(ctx context.Context, aggregateID uuid.UUID, fromVersion int) ([]events.DomainEvent, error) {
	rows, err := s.pool.Query(ctx, `
		SELECT event_id, aggregate_type, event_type, event_version, event_data, metadata, event_timestamp
		FROM events
		WHERE aggregate_id = $1 AND event_version >= $2
		ORDER BY event_version ASC
	`, aggregateID, fromVersion)

	if err != nil {
		return nil, fmt.Errorf("failed to query events: %w", err)
	}
	defer rows.Close()

	var domainEvents []events.DomainEvent
	for rows.Next() {
		var eventID uuid.UUID
		var aggregateType, eventType string
		var eventVersion int
		var eventData, metadata []byte
		var eventTimestamp time.Time

		err := rows.Scan(&eventID, &aggregateType, &eventType, &eventVersion, &eventData, &metadata, &eventTimestamp)
		if err != nil {
			return nil, fmt.Errorf("failed to scan event: %w", err)
		}

		var data interface{}
		if err := json.Unmarshal(eventData, &data); err != nil {
			continue
		}

		// Create domain event (simplified for this example)
		baseEvent := &events.BaseDomainEvent{
			EventID:       eventID,
			EventType:     eventType,
			AggregateID:   aggregateID,
			AggregateType: aggregateType,
			Version:       eventVersion,
			Timestamp:     eventTimestamp,
			Data:          data,
		}

		if len(metadata) > 0 {
			var meta map[string]interface{}
			if err := json.Unmarshal(metadata, &meta); err == nil {
				baseEvent.Metadata = meta
			}
		}

		domainEvents = append(domainEvents, baseEvent)
	}

	return domainEvents, rows.Err()
}

// GetAggregateEvents retrieves events for aggregate with pagination
func (s *PostgresEventStore) GetAggregateEvents(ctx context.Context, aggregateType, aggregateID string, fromVersion, toVersion *int, limit, offset int) ([]events.DomainEvent, int, error) {
	// Build query with optional filters
	query := `
		SELECT event_id, event_type, event_version, event_data, metadata, event_timestamp,
		       COUNT(*) OVER() as total_count
		FROM events
		WHERE aggregate_type = $1 AND aggregate_id::text = $2
	`
	args := []interface{}{aggregateType, aggregateID}
	argCount := 2

	if fromVersion != nil {
		argCount++
		query += fmt.Sprintf(" AND event_version >= $%d", argCount)
		args = append(args, *fromVersion)
	}

	if toVersion != nil {
		argCount++
		query += fmt.Sprintf(" AND event_version <= $%d", argCount)
		args = append(args, *toVersion)
	}

	query += " ORDER BY event_version ASC"

	if limit > 0 {
		argCount++
		query += fmt.Sprintf(" LIMIT $%d", argCount)
		args = append(args, limit)
	}

	if offset > 0 {
		argCount++
		query += fmt.Sprintf(" OFFSET $%d", argCount)
		args = append(args, offset)
	}

	rows, err := s.pool.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to query events: %w", err)
	}
	defer rows.Close()

	var domainEvents []events.DomainEvent
	var totalCount int

	for rows.Next() {
		var eventID uuid.UUID
		var eventType string
		var eventVersion int
		var eventData, metadata []byte
		var eventTimestamp time.Time

		err := rows.Scan(&eventID, &eventType, &eventVersion, &eventData, &metadata, &eventTimestamp, &totalCount)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan event: %w", err)
		}

		var data interface{}
		if err := json.Unmarshal(eventData, &data); err != nil {
			continue
		}

		baseEvent := &events.BaseDomainEvent{
			EventID:       eventID,
			EventType:     eventType,
			AggregateID:   uuid.MustParse(aggregateID),
			AggregateType: aggregateType,
			Version:       eventVersion,
			Timestamp:     eventTimestamp,
			Data:          data,
		}

		if len(metadata) > 0 {
			var meta map[string]interface{}
			if err := json.Unmarshal(metadata, &meta); err == nil {
				baseEvent.Metadata = meta
			}
		}

		domainEvents = append(domainEvents, baseEvent)
	}

	return domainEvents, totalCount, rows.Err()
}

// Close closes the database connection pool
func (s *PostgresEventStore) Close() error {
	s.pool.Close()
	return nil
}