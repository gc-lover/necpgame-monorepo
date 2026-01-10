// Aggregate Repository for managing aggregate lifecycle
// Issue: #2217
// Agent: Backend Agent
package aggregates

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/event-sourcing-aggregates-go/internal/events"
	"necpgame/services/event-sourcing-aggregates-go/internal/snapshots"
	"necpgame/services/event-sourcing-aggregates-go/internal/store"
)

// Repository manages aggregate persistence and retrieval
type Repository struct {
	eventStore   store.EventStore
	snapshotStore snapshots.SnapshotStore
	logger       *zap.Logger
}

// NewRepository creates a new aggregate repository
func NewRepository(eventStore store.EventStore, snapshotStore snapshots.SnapshotStore, logger *zap.Logger) *Repository {
	return &Repository{
		eventStore:   eventStore,
		snapshotStore: snapshotStore,
		logger:       logger,
	}
}

// Save saves an aggregate with optimistic concurrency
func (r *Repository) Save(ctx context.Context, aggregate Aggregate) error {
	events := aggregate.GetUncommittedEvents()
	if len(events) == 0 {
		return nil // Nothing to save
	}

	r.logger.Info("Saving aggregate",
		zap.String("aggregate_id", aggregate.GetID().String()),
		zap.String("aggregate_type", aggregate.GetType()),
		zap.Int("event_count", len(events)),
		zap.Int("expected_version", aggregate.GetVersion()-len(events)))

	// Save events with concurrency control
	expectedVersion := aggregate.GetVersion() - len(events)
	err := r.eventStore.SaveEvents(ctx, aggregate.GetID(), events, expectedVersion)
	if err != nil {
		r.logger.Error("Failed to save events",
			zap.Error(err),
			zap.String("aggregate_id", aggregate.GetID().String()))
		return fmt.Errorf("failed to save events: %w", err)
	}

	// Mark events as committed
	aggregate.MarkEventsAsCommitted()

	// Create snapshot if needed (every 50 events for performance)
	if aggregate.GetVersion()%50 == 0 {
		if err := r.createSnapshot(ctx, aggregate); err != nil {
			r.logger.Warn("Failed to create snapshot",
				zap.Error(err),
				zap.String("aggregate_id", aggregate.GetID().String()))
			// Don't fail the save operation if snapshot fails
		}
	}

	r.logger.Info("Aggregate saved successfully",
		zap.String("aggregate_id", aggregate.GetID().String()),
		zap.Int("new_version", aggregate.GetVersion()))

	return nil
}

// Load loads an aggregate by ID with snapshot optimization
func (r *Repository) Load(ctx context.Context, aggregateID uuid.UUID, aggregateType string) (Aggregate, error) {
	r.logger.Info("Loading aggregate",
		zap.String("aggregate_id", aggregateID.String()),
		zap.String("aggregate_type", aggregateType))

	var aggregate Aggregate
	var events []events.DomainEvent
	var snapshotVersion int

	// Try to load from snapshot first (performance optimization)
	if r.snapshotStore != nil {
		snapshot, err := r.snapshotStore.GetLatestSnapshot(ctx, aggregateID, aggregateType)
		if err != nil {
			r.logger.Warn("Failed to load snapshot",
				zap.Error(err),
				zap.String("aggregate_id", aggregateID.String()))
		} else if snapshot != nil {
			// Create aggregate from snapshot
			aggregate = r.createAggregateFromSnapshot(snapshot)
			snapshotVersion = snapshot.Version

			r.logger.Info("Loaded aggregate from snapshot",
				zap.String("aggregate_id", aggregateID.String()),
				zap.Int("snapshot_version", snapshotVersion))

			// Load events after snapshot
			recentEvents, err := r.eventStore.GetEventsFromVersion(ctx, aggregateID, snapshotVersion+1)
			if err != nil {
				return nil, fmt.Errorf("failed to load recent events: %w", err)
			}
			events = recentEvents
		}
	}

	// If no snapshot, load all events
	if aggregate == nil {
		allEvents, err := r.eventStore.GetEvents(ctx, aggregateID)
		if err != nil {
			return nil, fmt.Errorf("failed to load events: %w", err)
		}
		events = allEvents

		// Create new aggregate
		aggregate = r.createAggregate(aggregateID, aggregateType)
	}

	// Apply events to aggregate
	if err := aggregate.LoadFromEvents(events); err != nil {
		return nil, fmt.Errorf("failed to apply events: %w", err)
	}

	r.logger.Info("Aggregate loaded successfully",
		zap.String("aggregate_id", aggregateID.String()),
		zap.Int("final_version", aggregate.GetVersion()),
		zap.Int("events_applied", len(events)))

	return aggregate, nil
}

// LoadPlayer loads a player aggregate specifically
func (r *Repository) LoadPlayer(ctx context.Context, playerID uuid.UUID) (*PlayerAggregate, error) {
	aggregate, err := r.Load(ctx, playerID, "player")
	if err != nil {
		return nil, err
	}

	playerAggregate, ok := aggregate.(*PlayerAggregate)
	if !ok {
		return nil, fmt.Errorf("aggregate is not a player aggregate")
	}

	return playerAggregate, nil
}

// createAggregate creates a new aggregate instance based on type
func (r *Repository) createAggregate(aggregateID uuid.UUID, aggregateType string) Aggregate {
	switch aggregateType {
	case "player":
		return NewPlayerAggregate(aggregateID)
	default:
		r.logger.Error("Unknown aggregate type",
			zap.String("aggregate_type", aggregateType))
		return nil
	}
}

// createAggregateFromSnapshot creates aggregate from snapshot data
func (r *Repository) createAggregateFromSnapshot(snapshot *snapshots.Snapshot) Aggregate {
	switch snapshot.AggregateType {
	case "player":
		playerAggregate := NewPlayerAggregate(snapshot.AggregateID)

		// Deserialize state from snapshot
		if stateData, ok := snapshot.State.(map[string]interface{}); ok {
			if stateJSON, err := json.Marshal(stateData); err == nil {
				var state PlayerState
				if err := json.Unmarshal(stateJSON, &state); err == nil {
					playerAggregate.State = state
					// Set version from snapshot
					playerAggregate.Version = snapshot.Version
					playerAggregate.UpdatedAt = snapshot.CreatedAt
				}
			}
		}

		return playerAggregate
	default:
		r.logger.Error("Unknown aggregate type in snapshot",
			zap.String("aggregate_type", snapshot.AggregateType))
		return nil
	}
}

// createSnapshot creates a snapshot of the current aggregate state
func (r *Repository) createSnapshot(ctx context.Context, aggregate Aggregate) error {
	if r.snapshotStore == nil {
		return nil // No snapshot store configured
	}

	var state interface{}

	switch a := aggregate.(type) {
	case *PlayerAggregate:
		state = a.GetState()
	default:
		return fmt.Errorf("unsupported aggregate type for snapshot: %T", aggregate)
	}

	snapshot := &snapshots.Snapshot{
		AggregateID:   aggregate.GetID(),
		AggregateType: aggregate.GetType(),
		Version:       aggregate.GetVersion(),
		State:         state,
		CreatedAt:     time.Now().UTC(),
		EventCount:    aggregate.GetVersion(),
	}

	// Calculate size (approximate)
	if stateJSON, err := json.Marshal(state); err == nil {
		snapshot.Size = len(stateJSON)
	}

	r.logger.Info("Creating snapshot",
		zap.String("aggregate_id", aggregate.GetID().String()),
		zap.String("aggregate_type", aggregate.GetType()),
		zap.Int("version", aggregate.GetVersion()),
		zap.Int("size_bytes", snapshot.Size))

	return r.snapshotStore.SaveSnapshot(ctx, snapshot)
}

// Exists checks if an aggregate exists
func (r *Repository) Exists(ctx context.Context, aggregateID uuid.UUID, aggregateType string) (bool, error) {
	events, err := r.eventStore.GetEvents(ctx, aggregateID)
	if err != nil {
		return false, fmt.Errorf("failed to check aggregate existence: %w", err)
	}
	return len(events) > 0, nil
}

// GetAggregateStats returns statistics about an aggregate
func (r *Repository) GetAggregateStats(ctx context.Context, aggregateID uuid.UUID, aggregateType string) (map[string]interface{}, error) {
	stats := make(map[string]interface{})

	// Get event count
	events, err := r.eventStore.GetEvents(ctx, aggregateID)
	if err != nil {
		return nil, fmt.Errorf("failed to get events for stats: %w", err)
	}
	stats["event_count"] = len(events)

	// Get snapshot stats if available
	if r.snapshotStore != nil {
		snapshotStats, err := r.snapshotStore.GetSnapshotStats(ctx, aggregateID, aggregateType)
		if err == nil {
			for k, v := range snapshotStats {
				stats["snapshot_"+k] = v
			}
		}
	}

	// Calculate aggregate age
	if len(events) > 0 {
		firstEvent := events[0]
		lastEvent := events[len(events)-1]
		stats["created_at"] = firstEvent.GetTimestamp()
		stats["last_modified"] = lastEvent.GetTimestamp()
		stats["age_seconds"] = time.Since(firstEvent.GetTimestamp()).Seconds()
	}

	return stats, nil
}