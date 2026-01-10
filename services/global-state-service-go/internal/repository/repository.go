package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
)

// Repository handles database operations for global state management
type Repository struct {
	db *pgxpool.Pool
}

// NewRepository creates a new repository instance
func NewRepository(db *pgxpool.Pool) *Repository {
	return &Repository{db: db}
}

// AggregateState represents the current state of an aggregate
type AggregateState struct {
	AggregateType string                 `json:"aggregate_type"`
	AggregateID   string                 `json:"aggregate_id"`
	Version       int64                  `json:"version"`
	Data          map[string]interface{} `json:"data"`
	LastModified  time.Time              `json:"last_modified"`
	Checksum      string                 `json:"checksum"`
}

// GameEvent represents an event in the event store
type GameEvent struct {
	EventID        string                 `json:"event_id"`
	EventType      string                 `json:"event_type"`
	AggregateType  string                 `json:"aggregate_type"`
	AggregateID    string                 `json:"aggregate_id"`
	EventVersion   int64                  `json:"event_version"`
	CorrelationID  *string                `json:"correlation_id,omitempty"`
	CausationID    *string                `json:"causation_id,omitempty"`
	EventData      map[string]interface{} `json:"event_data"`
	Metadata       map[string]interface{} `json:"metadata,omitempty"`
	ServerID       string                 `json:"server_id"`
	PlayerID       *string                `json:"player_id,omitempty"`
	SessionID      *string                `json:"session_id,omitempty"`
	Timestamp      time.Time              `json:"timestamp"`
	ProcessedAt    *time.Time             `json:"processed_at,omitempty"`
	StateChanges   map[string]interface{} `json:"state_changes,omitempty"`
}

// GetAggregateState retrieves current state for an aggregate
func (r *Repository) GetAggregateState(ctx context.Context, aggregateType, aggregateID string, version *int64) (*AggregateState, error) {
	query := `
		SELECT aggregate_type, aggregate_id, version, data, last_modified, checksum
		FROM global_state.global_state
		WHERE aggregate_type = $1 AND aggregate_id = $2
	`

	args := []interface{}{aggregateType, aggregateID}
	if version != nil {
		query += " AND version <= $3 ORDER BY version DESC LIMIT 1"
		args = append(args, *version)
	} else {
		query += " ORDER BY version DESC LIMIT 1"
	}

	var state AggregateState
	var dataBytes []byte

	err := r.db.QueryRow(ctx, query, args...).Scan(
		&state.AggregateType, &state.AggregateID, &state.Version,
		&dataBytes, &state.LastModified, &state.Checksum,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to get aggregate state: %w", err)
	}

	if err := json.Unmarshal(dataBytes, &state.Data); err != nil {
		return nil, fmt.Errorf("failed to unmarshal state data: %w", err)
	}

	return &state, nil
}

// UpdateAggregateState updates state for an aggregate with optimistic locking
func (r *Repository) UpdateAggregateState(ctx context.Context, state *AggregateState, expectedVersion int64) error {
	dataBytes, err := json.Marshal(state.Data)
	if err != nil {
		return fmt.Errorf("failed to marshal state data: %w", err)
	}

	query := `
		INSERT INTO global_state.global_state (
			aggregate_type, aggregate_id, version, data, last_modified, checksum
		) VALUES ($1, $2, $3, $4, $5, $6)
		ON CONFLICT (aggregate_type, aggregate_id)
		DO UPDATE SET
			version = EXCLUDED.version,
			data = EXCLUDED.data,
			last_modified = EXCLUDED.last_modified,
			checksum = EXCLUDED.checksum
		WHERE global_state.version = $7
	`

	result, err := r.db.Exec(ctx, query,
		state.AggregateType, state.AggregateID, state.Version,
		dataBytes, time.Now(), state.Checksum, expectedVersion,
	)
	if err != nil {
		return fmt.Errorf("failed to update aggregate state: %w", err)
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("optimistic locking failed: version conflict")
	}

	return nil
}

// PublishEvent publishes an event to the event store
func (r *Repository) PublishEvent(ctx context.Context, event *GameEvent) error {
	eventDataBytes, err := json.Marshal(event.EventData)
	if err != nil {
		return fmt.Errorf("failed to marshal event data: %w", err)
	}

	var metadataBytes []byte
	if event.Metadata != nil {
		metadataBytes, err = json.Marshal(event.Metadata)
		if err != nil {
			return fmt.Errorf("failed to marshal metadata: %w", err)
		}
	}

	var stateChangesBytes []byte
	if event.StateChanges != nil {
		stateChangesBytes, err = json.Marshal(event.StateChanges)
		if err != nil {
			return fmt.Errorf("failed to marshal state changes: %w", err)
		}
	}

	query := `
		INSERT INTO global_state.game_events (
			event_id, event_type, aggregate_type, aggregate_id, event_version,
			correlation_id, causation_id, event_data, metadata, server_id,
			player_id, session_id, event_timestamp, processed_at, state_changes
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15)
	`

	_, err = r.db.Exec(ctx, query,
		event.EventID, event.EventType, event.AggregateType, event.AggregateID, event.EventVersion,
		event.CorrelationID, event.CausationID, eventDataBytes, metadataBytes, event.ServerID,
		event.PlayerID, event.SessionID, event.Timestamp, event.ProcessedAt, stateChangesBytes,
	)
	if err != nil {
		return fmt.Errorf("failed to publish event: %w", err)
	}

	return nil
}

// GetAggregateEvents retrieves event history for an aggregate
func (r *Repository) GetAggregateEvents(ctx context.Context, aggregateType, aggregateID string, fromVersion, toVersion *int64, limit int) ([]*GameEvent, error) {
	query := `
		SELECT event_id, event_type, aggregate_type, aggregate_id, event_version,
			   correlation_id, causation_id, event_data, metadata, server_id,
			   player_id, session_id, event_timestamp, processed_at, state_changes
		FROM global_state.game_events
		WHERE aggregate_type = $1 AND aggregate_id = $2
	`

	args := []interface{}{aggregateType, aggregateID}
	paramCount := 2

	if fromVersion != nil {
		paramCount++
		query += fmt.Sprintf(" AND event_version >= $%d", paramCount)
		args = append(args, *fromVersion)
	}

	if toVersion != nil {
		paramCount++
		query += fmt.Sprintf(" AND event_version <= $%d", paramCount)
		args = append(args, *toVersion)
	}

	query += " ORDER BY event_version ASC"
	if limit > 0 {
		paramCount++
		query += fmt.Sprintf(" LIMIT $%d", paramCount)
		args = append(args, limit)
	}

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, fmt.Errorf("failed to query events: %w", err)
	}
	defer rows.Close()

	var events []*GameEvent
	for rows.Next() {
		var event GameEvent
		var eventDataBytes, metadataBytes, stateChangesBytes []byte

		err := rows.Scan(
			&event.EventID, &event.EventType, &event.AggregateType, &event.AggregateID, &event.EventVersion,
			&event.CorrelationID, &event.CausationID, &eventDataBytes, &metadataBytes, &event.ServerID,
			&event.PlayerID, &event.SessionID, &event.Timestamp, &event.ProcessedAt, &stateChangesBytes,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan event: %w", err)
		}

		if err := json.Unmarshal(eventDataBytes, &event.EventData); err != nil {
			return nil, fmt.Errorf("failed to unmarshal event data: %w", err)
		}

		if len(metadataBytes) > 0 {
			event.Metadata = make(map[string]interface{})
			if err := json.Unmarshal(metadataBytes, &event.Metadata); err != nil {
				return nil, fmt.Errorf("failed to unmarshal metadata: %w", err)
			}
		}

		if len(stateChangesBytes) > 0 {
			event.StateChanges = make(map[string]interface{})
			if err := json.Unmarshal(stateChangesBytes, &event.StateChanges); err != nil {
				return nil, fmt.Errorf("failed to unmarshal state changes: %w", err)
			}
		}

		events = append(events, &event)
	}

	return events, nil
}

// GetStateAnalytics retrieves analytics about state changes
func (r *Repository) GetStateAnalytics(ctx context.Context, aggregateType *string, timeRange string, groupBy string) (map[string]interface{}, error) {
	// This would contain complex analytics queries
	// For now, return a placeholder
	return map[string]interface{}{
		"time_range": timeRange,
		"group_by":   groupBy,
		"metrics":    []map[string]interface{}{},
	}, nil
}