// Issue: #2217
// PERFORMANCE: Optimized aggregate repository with snapshotting and event replay
package aggregates

import (
	"context"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"event-sourcing-aggregates-go/internal/events"
	"event-sourcing-aggregates-go/internal/snapshots"
	"event-sourcing-aggregates-go/internal/store"
)

// Aggregate defines the interface for domain aggregates
type Aggregate interface {
	// GetID returns the aggregate ID
	GetID() uuid.UUID

	// GetType returns the aggregate type
	GetType() string

	// GetVersion returns the current version
	GetVersion() int

	// ApplyEvent applies an event to the aggregate
	ApplyEvent(event events.DomainEventInterface) error

	// GetUncommittedEvents returns uncommitted events
	GetUncommittedEvents() []events.DomainEventInterface

	// ClearUncommittedEvents clears uncommitted events
	ClearUncommittedEvents()

	// SetVersion sets the aggregate version
	SetVersion(version int)
}

// Repository manages aggregate lifecycle and persistence
type Repository struct {
	eventStore    store.EventStore
	snapshotStore snapshots.SnapshotStore
	logger        *zap.Logger

	// Cache for recently loaded aggregates
	cache    map[string]Aggregate
	cacheMu  sync.RWMutex
	cacheTTL time.Duration
}

// NewRepository creates a new aggregate repository
func NewRepository(eventStore store.EventStore, snapshotStore snapshots.SnapshotStore, logger *zap.Logger) *Repository {
	repo := &Repository{
		eventStore:    eventStore,
		snapshotStore: snapshotStore,
		logger:        logger,
		cache:         make(map[string]Aggregate),
		cacheTTL:      5 * time.Minute, // Cache aggregates for 5 minutes
	}

	// Start cache cleanup goroutine
	go repo.cleanupCache()

	return repo
}

// Load loads an aggregate from event store
func (r *Repository) Load(ctx context.Context, aggregateType string, aggregateID uuid.UUID) (Aggregate, error) {
	startTime := time.Now()

	// Check cache first
	cacheKey := fmt.Sprintf("%s:%s", aggregateType, aggregateID.String())
	r.cacheMu.RLock()
	if cached, exists := r.cache[cacheKey]; exists {
		r.cacheMu.RUnlock()
		r.logger.Debug("Aggregate loaded from cache",
			zap.String("aggregate_type", aggregateType),
			zap.String("aggregate_id", aggregateID.String()))
		return cached, nil
	}
	r.cacheMu.RUnlock()

	// Try to load from snapshot first
	aggregate, err := r.loadFromSnapshot(ctx, aggregateType, aggregateID)
	if err != nil {
		r.logger.Debug("Failed to load from snapshot, loading from events",
			zap.String("aggregate_type", aggregateType),
			zap.String("aggregate_id", aggregateID.String()),
			zap.Error(err))
	}

	// If no snapshot or snapshot loading failed, load from events
	if aggregate == nil {
		aggregate, err = r.loadFromEvents(ctx, aggregateType, aggregateID)
		if err != nil {
			return nil, fmt.Errorf("failed to load aggregate from events: %w", err)
		}
	}

	// Cache the aggregate
	r.cacheMu.Lock()
	r.cache[cacheKey] = aggregate
	r.cacheMu.Unlock()

	duration := time.Since(startTime)
	r.logger.Info("Aggregate loaded",
		zap.String("aggregate_type", aggregateType),
		zap.String("aggregate_id", aggregateID.String()),
		zap.Int("version", aggregate.GetVersion()),
		zap.Duration("duration", duration))

	return aggregate, nil
}

// loadFromSnapshot loads aggregate from snapshot store
func (r *Repository) loadFromSnapshot(ctx context.Context, aggregateType string, aggregateID uuid.UUID) (Aggregate, error) {
	snapshot, err := r.snapshotStore.GetSnapshot(ctx, aggregateID)
	if err != nil {
		return nil, fmt.Errorf("failed to get snapshot: %w", err)
	}

	if snapshot == nil {
		return nil, nil // No snapshot found
	}

	// Create aggregate instance
	aggregate := r.createAggregate(aggregateType, aggregateID)
	if aggregate == nil {
		return nil, fmt.Errorf("unknown aggregate type: %s", aggregateType)
	}

	// Deserialize snapshot data
	if err := json.Unmarshal(snapshot.Data, aggregate); err != nil {
		return nil, fmt.Errorf("failed to unmarshal snapshot: %w", err)
	}

	// Set version from snapshot
	aggregate.SetVersion(snapshot.Version)

	// Load events after snapshot version
	events, err := r.eventStore.GetEvents(ctx, aggregateID, snapshot.Version+1)
	if err != nil {
		return nil, fmt.Errorf("failed to get events after snapshot: %w", err)
	}

	// Apply events to bring aggregate up to date
	for _, eventEnvelope := range events {
		// For now, we'll skip event replay as it requires complex event deserialization
		// In a full implementation, we would use event registry to deserialize events
		r.logger.Warn("Event replay not implemented - skipping event application",
			zap.String("aggregate_id", aggregateID.String()),
			zap.Int("event_version", eventEnvelope.Version))
	}

	return aggregate, nil
}

// loadFromEvents loads aggregate by replaying all events
func (r *Repository) loadFromEvents(ctx context.Context, aggregateType string, aggregateID uuid.UUID) (Aggregate, error) {
	// Create aggregate instance
	aggregate := r.createAggregate(aggregateType, aggregateID)
	if aggregate == nil {
		return nil, fmt.Errorf("unknown aggregate type: %s", aggregateType)
	}

	// Load all events for this aggregate
	eventEnvelopes, err := r.eventStore.GetEvents(ctx, aggregateID, 1)
	if err != nil {
		return nil, fmt.Errorf("failed to get events: %w", err)
	}

	// Apply all events
	for _, eventEnvelope := range eventEnvelopes {
		var domainEvent events.DomainEvent
		if err := json.Unmarshal(eventEnvelope.EventData, &domainEvent); err != nil {
			return nil, fmt.Errorf("failed to unmarshal event: %w", err)
		}

		if err := aggregate.ApplyEvent(domainEvent); err != nil {
			return nil, fmt.Errorf("failed to apply event: %w", err)
		}
	}

	return aggregate, nil
}

// Save saves an aggregate to event store
func (r *Repository) Save(ctx context.Context, aggregate Aggregate) error {
	startTime := time.Now()

	// Get uncommitted events
	events := aggregate.GetUncommittedEvents()
	if len(events) == 0 {
		return nil // Nothing to save
	}

	// Convert domain events to event envelopes
	var envelopes []store.EventEnvelope
	currentVersion := aggregate.GetVersion()

	for i, event := range events {
		eventData, err := json.Marshal(event)
		if err != nil {
			return fmt.Errorf("failed to marshal event: %w", err)
		}

		envelope := store.EventEnvelope{
			EventID:       uuid.New(),
			AggregateID:   aggregate.GetID(),
			AggregateType: aggregate.GetType(),
			EventType:     event.EventType(),
			EventData:     eventData,
			Version:       currentVersion + i + 1,
			Timestamp:     time.Now(),
			Metadata:      store.EventMetadata{}, // TODO: Add proper metadata
		}
		envelopes = append(envelopes, envelope)
	}

	// Append events to event store
	if err := r.eventStore.AppendEvents(ctx, aggregate.GetID(), aggregate.GetType(), envelopes, currentVersion); err != nil {
		return fmt.Errorf("failed to append events: %w", err)
	}

	// Clear uncommitted events
	aggregate.ClearUncommittedEvents()

	// Update aggregate version
	aggregate.SetVersion(currentVersion + len(events))

	// Update cache
	cacheKey := fmt.Sprintf("%s:%s", aggregate.GetType(), aggregate.GetID().String())
	r.cacheMu.Lock()
	r.cache[cacheKey] = aggregate
	r.cacheMu.Unlock()

	// Create snapshot if needed
	if r.shouldCreateSnapshot(aggregate) {
		if err := r.createSnapshot(ctx, aggregate); err != nil {
			r.logger.Warn("Failed to create snapshot",
				zap.String("aggregate_id", aggregate.GetID().String()),
				zap.Error(err))
			// Don't fail the save operation if snapshot fails
		}
	}

	duration := time.Since(startTime)
	r.logger.Info("Aggregate saved",
		zap.String("aggregate_type", aggregate.GetType()),
		zap.String("aggregate_id", aggregate.GetID().String()),
		zap.Int("event_count", len(events)),
		zap.Int("new_version", aggregate.GetVersion()),
		zap.Duration("duration", duration))

	return nil
}

// createAggregate creates an aggregate instance based on type
func (r *Repository) createAggregate(aggregateType string, aggregateID uuid.UUID) Aggregate {
	switch aggregateType {
	case "player":
		return NewPlayerAggregateWithID(aggregateID)
	default:
		return nil
	}
}

// shouldCreateSnapshot determines if a snapshot should be created
func (r *Repository) shouldCreateSnapshot(aggregate Aggregate) bool {
	version := aggregate.GetVersion()
	// Create snapshot every 50 events
	return version%50 == 0
}

// createSnapshot creates a snapshot of the aggregate
func (r *Repository) createSnapshot(ctx context.Context, aggregate Aggregate) error {
	data, err := json.Marshal(aggregate)
	if err != nil {
		return fmt.Errorf("failed to marshal aggregate: %w", err)
	}

	snapshot := &snapshots.Snapshot{
		AggregateID:   aggregate.GetID(),
		AggregateType: aggregate.GetType(),
		Version:       aggregate.GetVersion(),
		Data:          data,
		Timestamp:     time.Now(),
	}

	return r.snapshotStore.SaveSnapshot(ctx, snapshot)
}

// cleanupCache periodically cleans up expired cache entries
func (r *Repository) cleanupCache() {
	ticker := time.NewTicker(10 * time.Minute)
	defer ticker.Stop()

	for range ticker.C {
		r.cacheMu.Lock()
		// In a real implementation, we would check TTL and remove expired entries
		// For now, just clear the entire cache periodically
		r.cache = make(map[string]Aggregate)
		r.cacheMu.Unlock()

		r.logger.Debug("Aggregate cache cleaned up")
	}
}

// Exists checks if an aggregate exists
func (r *Repository) Exists(ctx context.Context, aggregateID uuid.UUID) (bool, error) {
	version, err := r.eventStore.GetAggregateVersion(ctx, aggregateID)
	if err != nil {
		return false, err
	}
	return version > 0, nil
}

// GetVersion returns the current version of an aggregate
func (r *Repository) GetVersion(ctx context.Context, aggregateID uuid.UUID) (int, error) {
	return r.eventStore.GetAggregateVersion(ctx, aggregateID)
}
