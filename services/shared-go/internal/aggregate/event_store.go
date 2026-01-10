// Package aggregate provides PostgreSQL-based Event Store implementation
// Optimized for high-performance event sourcing with connection pooling and indexing
//
// Issue: #2217 - Event Sourcing Aggregate Implementation
// Agent: Backend Agent
package aggregate

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// PostgreSQLEventStore implements EventStore interface using PostgreSQL
// Optimized for event sourcing with proper indexing and connection pooling
// Issue: #2217
type PostgreSQLEventStore struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewPostgreSQLEventStore creates a new PostgreSQL event store
// Issue: #2217
func NewPostgreSQLEventStore(db *pgxpool.Pool, logger *zap.Logger) *PostgreSQLEventStore {
	return &PostgreSQLEventStore{
		db:     db,
		logger: logger,
	}
}

// SaveEvents saves events for an aggregate with optimistic concurrency control
// Uses PostgreSQL transactions for atomicity and performance
// Issue: #2217
func (es *PostgreSQLEventStore) SaveEvents(ctx context.Context, aggregateID string, events []DomainEvent, expectedVersion int) error {
	if len(events) == 0 {
		return nil
	}

	// Start transaction
	tx, err := es.db.Begin(ctx)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback(ctx)

	// Check current version for optimistic locking
	var currentVersion int
	err = tx.QueryRow(ctx, `
		SELECT COALESCE(MAX(event_version), 0)
		FROM event_store
		WHERE aggregate_id = $1
	`, aggregateID).Scan(&currentVersion)

	if err != nil && err != sql.ErrNoRows {
		return fmt.Errorf("failed to get current version: %w", err)
	}

	// Optimistic concurrency check
	if currentVersion != expectedVersion {
		return ErrAggregateVersionConflict
	}

	// Insert events
	for _, event := range events {
		eventJSON, err := json.Marshal(event)
		if err != nil {
			return fmt.Errorf("failed to marshal event: %w", err)
		}

		_, err = tx.Exec(ctx, `
			INSERT INTO event_store (
				event_id, event_type, aggregate_id, aggregate_type,
				event_version, event_data, occurred_at, created_at
			) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
		`, event.EventID(), event.EventType(), event.AggregateID(),
		   event.AggregateType(), event.EventVersion(), eventJSON,
		   event.OccurredAt(), time.Now().UTC())

		if err != nil {
			return fmt.Errorf("failed to save event: %w", err)
		}
	}

	// Commit transaction
	err = tx.Commit(ctx)
	if err != nil {
		return fmt.Errorf("failed to commit transaction: %w", err)
	}

	es.logger.Info("Events saved successfully",
		zap.String("aggregate_id", aggregateID),
		zap.Int("event_count", len(events)),
		zap.Int("new_version", expectedVersion+len(events)))

	return nil
}

// LoadEvents loads all events for an aggregate
// Optimized with proper indexing on aggregate_id
// Issue: #2217
func (es *PostgreSQLEventStore) LoadEvents(ctx context.Context, aggregateID string) ([]DomainEvent, error) {
	rows, err := es.db.Query(ctx, `
		SELECT event_id, event_type, aggregate_id, aggregate_type,
		       event_version, event_data, occurred_at
		FROM event_store
		WHERE aggregate_id = $1
		ORDER BY event_version ASC
	`, aggregateID)

	if err != nil {
		return nil, fmt.Errorf("failed to load events: %w", err)
	}
	defer rows.Close()

	var events []DomainEvent
	for rows.Next() {
		var eventID, eventType, aggID, aggType string
		var eventVersion int
		var eventData []byte
		var occurredAt time.Time

		err := rows.Scan(&eventID, &eventType, &aggID, &aggType,
		                &eventVersion, &eventData, &occurredAt)
		if err != nil {
			return nil, fmt.Errorf("failed to scan event: %w", err)
		}

		// Deserialize event based on type
		event, err := es.deserializeEvent(eventType, eventData)
		if err != nil {
			return nil, fmt.Errorf("failed to deserialize event: %w", err)
		}

		// Set deserialized event fields
		if baseEvent, ok := event.(*BaseEvent); ok {
			baseEvent.eventID = eventID
			baseEvent.eventType = eventType
			baseEvent.aggregateID = aggID
			baseEvent.aggregateType = aggType
			baseEvent.eventVersion = eventVersion
			baseEvent.occurredAt = occurredAt
			baseEvent.eventData = eventData
		}

		events = append(events, event)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating events: %w", err)
	}

	es.logger.Debug("Events loaded successfully",
		zap.String("aggregate_id", aggregateID),
		zap.Int("event_count", len(events)))

	return events, nil
}

// LoadEventsFromVersion loads events starting from a specific version
// Used for snapshot optimization
// Issue: #2217
func (es *PostgreSQLEventStore) LoadEventsFromVersion(ctx context.Context, aggregateID string, fromVersion int) ([]DomainEvent, error) {
	rows, err := es.db.Query(ctx, `
		SELECT event_id, event_type, aggregate_id, aggregate_type,
		       event_version, event_data, occurred_at
		FROM event_store
		WHERE aggregate_id = $1 AND event_version > $2
		ORDER BY event_version ASC
	`, aggregateID, fromVersion)

	if err != nil {
		return nil, fmt.Errorf("failed to load events from version: %w", err)
	}
	defer rows.Close()

	var events []DomainEvent
	for rows.Next() {
		var eventID, eventType, aggID, aggType string
		var eventVersion int
		var eventData []byte
		var occurredAt time.Time

		err := rows.Scan(&eventID, &eventType, &aggID, &aggType,
		                &eventVersion, &eventData, &occurredAt)
		if err != nil {
			return nil, fmt.Errorf("failed to scan event: %w", err)
		}

		event, err := es.deserializeEvent(eventType, eventData)
		if err != nil {
			return nil, fmt.Errorf("failed to deserialize event: %w", err)
		}

		if baseEvent, ok := event.(*BaseEvent); ok {
			baseEvent.eventID = eventID
			baseEvent.eventType = eventType
			baseEvent.aggregateID = aggID
			baseEvent.aggregateType = aggType
			baseEvent.eventVersion = eventVersion
			baseEvent.occurredAt = occurredAt
			baseEvent.eventData = eventData
		}

		events = append(events, event)
	}

	return events, rows.Err()
}

// GetAggregateVersion returns the current version of an aggregate
// Issue: #2217
func (es *PostgreSQLEventStore) GetAggregateVersion(ctx context.Context, aggregateID string) (int, error) {
	var version int
	err := es.db.QueryRow(ctx, `
		SELECT COALESCE(MAX(event_version), 0)
		FROM event_store
		WHERE aggregate_id = $1
	`, aggregateID).Scan(&version)

	if err != nil {
		return 0, fmt.Errorf("failed to get aggregate version: %w", err)
	}

	return version, nil
}

// SaveSnapshot saves a snapshot of aggregate state
// Issue: #2217
func (es *PostgreSQLEventStore) SaveSnapshot(ctx context.Context, aggregateID string, snapshot AggregateSnapshot) error {
	snapshotJSON, err := json.Marshal(snapshot)
	if err != nil {
		return fmt.Errorf("failed to marshal snapshot: %w", err)
	}

	_, err = es.db.Exec(ctx, `
		INSERT INTO aggregate_snapshots (
			aggregate_id, version, snapshot_data, created_at
		) VALUES ($1, $2, $3, $4)
		ON CONFLICT (aggregate_id)
		DO UPDATE SET
			version = EXCLUDED.version,
			snapshot_data = EXCLUDED.snapshot_data,
			created_at = EXCLUDED.created_at
	`, aggregateID, snapshot.Version(), snapshotJSON, snapshot.CreatedAt())

	if err != nil {
		return fmt.Errorf("failed to save snapshot: %w", err)
	}

	es.logger.Info("Snapshot saved successfully",
		zap.String("aggregate_id", aggregateID),
		zap.Int("version", snapshot.Version()))

	return nil
}

// LoadSnapshot loads the latest snapshot for an aggregate
// Issue: #2217
func (es *PostgreSQLEventStore) LoadSnapshot(ctx context.Context, aggregateID string) (AggregateSnapshot, error) {
	var snapshotData []byte
	var version int
	var createdAt time.Time

	err := es.db.QueryRow(ctx, `
		SELECT snapshot_data, version, created_at
		FROM aggregate_snapshots
		WHERE aggregate_id = $1
		ORDER BY version DESC
		LIMIT 1
	`, aggregateID).Scan(&snapshotData, &version, &createdAt)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrAggregateNotFound
		}
		return nil, fmt.Errorf("failed to load snapshot: %w", err)
	}

	// Deserialize snapshot - this is a basic implementation
	// In practice, you'd have specific snapshot types per aggregate
	var snapshot map[string]interface{}
	err = json.Unmarshal(snapshotData, &snapshot)
	if err != nil {
		return nil, fmt.Errorf("failed to unmarshal snapshot: %w", err)
	}

	// Return basic snapshot implementation
	return &BaseSnapshot{
		aggregateID:  aggregateID,
		version:      version,
		snapshotData: snapshotData,
		createdAt:    createdAt,
	}, nil
}

// deserializeEvent deserializes event data based on event type
// This is a basic implementation - in practice you'd have a registry
// Issue: #2217
func (es *PostgreSQLEventStore) deserializeEvent(eventType string, eventData []byte) (DomainEvent, error) {
	// For now, return a basic event
	// In practice, you'd have event type registry and proper deserialization
	return &BaseEvent{
		eventData: eventData,
	}, nil
}

// BaseSnapshot provides a basic snapshot implementation
// Issue: #2217
type BaseSnapshot struct {
	aggregateID  string
	version      int
	snapshotData []byte
	createdAt    time.Time
}

func (s *BaseSnapshot) AggregateID() string {
	return s.aggregateID
}

func (s *BaseSnapshot) Version() int {
	return s.version
}

func (s *BaseSnapshot) SnapshotData() []byte {
	return s.snapshotData
}

func (s *BaseSnapshot) CreatedAt() time.Time {
	return s.createdAt
}