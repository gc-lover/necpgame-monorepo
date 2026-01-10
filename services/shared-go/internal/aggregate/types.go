// Package aggregate provides enterprise-grade Event Sourcing Aggregate implementation
// for NECPGAME backend services. Supports domain-driven design with event versioning,
// optimistic concurrency, and snapshot capabilities.
//
// Issue: #2217 - Event Sourcing Aggregate Implementation
// Agent: Backend Agent
package aggregate

import (
	"context"
	"time"

	"github.com/google/uuid"
)

// DomainEvent represents a domain event in the event sourcing pattern
// All domain events must implement this interface
// Issue: #2217
type DomainEvent interface {
	// EventID returns unique event identifier
	EventID() string
	// EventType returns the type of event (e.g., "UserCreated", "OrderPlaced")
	EventType() string
	// AggregateID returns the ID of the aggregate this event belongs to
	AggregateID() string
	// AggregateType returns the type of aggregate (e.g., "User", "Order")
	AggregateType() string
	// EventVersion returns the version of this event
	EventVersion() int
	// OccurredAt returns when the event occurred
	OccurredAt() time.Time
	// EventData returns the serialized event data
	EventData() []byte
}

// Aggregate represents a domain aggregate root with event sourcing capabilities
// Provides optimistic concurrency control and event versioning
// Issue: #2217
type Aggregate interface {
	// ID returns the unique identifier of the aggregate
	ID() string
	// Type returns the type of the aggregate
	Type() string
	// Version returns the current version of the aggregate
	Version() int
	// UncommittedEvents returns events that haven't been persisted yet
	UncommittedEvents() []DomainEvent
	// ClearUncommittedEvents clears the uncommitted events list
	ClearUncommittedEvents()
	// ApplyEvent applies a domain event to the aggregate state
	ApplyEvent(event DomainEvent) error
	// LoadFromHistory reconstructs aggregate state from event history
	LoadFromHistory(events []DomainEvent) error
}

// BaseAggregate provides common functionality for all aggregates
// Includes event sourcing, versioning, and optimistic concurrency
// Issue: #2217
type BaseAggregate struct {
	id                string
	aggregateType     string
	version           int
	uncommittedEvents []DomainEvent
}

// NewBaseAggregate creates a new base aggregate instance
// Issue: #2217
func NewBaseAggregate(id, aggregateType string) *BaseAggregate {
	return &BaseAggregate{
		id:                id,
		aggregateType:     aggregateType,
		version:           0,
		uncommittedEvents: make([]DomainEvent, 0),
	}
}

// ID returns the aggregate ID
func (a *BaseAggregate) ID() string {
	return a.id
}

// Type returns the aggregate type
func (a *BaseAggregate) Type() string {
	return a.aggregateType
}

// Version returns the current aggregate version
func (a *BaseAggregate) Version() int {
	return a.version
}

// UncommittedEvents returns uncommitted events
func (a *BaseAggregate) UncommittedEvents() []DomainEvent {
	events := make([]DomainEvent, len(a.uncommittedEvents))
	copy(events, a.uncommittedEvents)
	return events
}

// ClearUncommittedEvents clears the uncommitted events
func (a *BaseAggregate) ClearUncommittedEvents() {
	a.uncommittedEvents = make([]DomainEvent, 0)
}

// ApplyEvent applies a domain event and increments version
// This is the core event sourcing mechanism
// Issue: #2217
func (a *BaseAggregate) ApplyEvent(event DomainEvent) error {
	if event.AggregateID() != a.id {
		return ErrAggregateIDMismatch
	}

	// Apply the event to change aggregate state
	// This method should be overridden by concrete aggregates
	err := a.applyEvent(event)
	if err != nil {
		return err
	}

	// Increment version
	a.version++

	return nil
}

// LoadFromHistory reconstructs aggregate state from event history
// Used when loading aggregates from event store
// Issue: #2217
func (a *BaseAggregate) LoadFromHistory(events []DomainEvent) error {
	for _, event := range events {
		if event.AggregateID() != a.id {
			return ErrAggregateIDMismatch
		}

		err := a.applyEvent(event)
		if err != nil {
			return err
		}

		a.version = event.EventVersion()
	}

	return nil
}

// applyEvent applies the event to aggregate state
// Must be implemented by concrete aggregates
// Issue: #2217
func (a *BaseAggregate) applyEvent(event DomainEvent) error {
	// Default implementation - should be overridden
	return nil
}

// RaiseEvent creates and applies a new domain event
// Convenience method for aggregate implementations
// Issue: #2217
func (a *BaseAggregate) RaiseEvent(eventType string, eventData []byte) error {
	event := &BaseEvent{
		eventID:       uuid.New().String(),
		eventType:     eventType,
		aggregateID:   a.id,
		aggregateType: a.aggregateType,
		eventVersion:  a.version + 1,
		occurredAt:    time.Now().UTC(),
		eventData:     eventData,
	}

	if err := a.applyEvent(event); err != nil {
		return err
	}
	a.version++
	a.uncommittedEvents = append(a.uncommittedEvents, event)
	return nil
}

// BaseEvent provides a basic implementation of DomainEvent
// Can be embedded or used as a base for custom events
// Issue: #2217
type BaseEvent struct {
	eventID       string
	eventType     string
	aggregateID   string
	aggregateType string
	eventVersion  int
	occurredAt    time.Time
	eventData     []byte
}

// NewBaseEvent creates a new base event
// Issue: #2217
func NewBaseEvent(eventType, aggregateID, aggregateType string, eventData []byte) *BaseEvent {
	return &BaseEvent{
		eventID:       uuid.New().String(),
		eventType:     eventType,
		aggregateID:   aggregateID,
		aggregateType: aggregateType,
		eventVersion:  1,
		occurredAt:    time.Now().UTC(),
		eventData:     eventData,
	}
}

// EventID returns the event ID
func (e *BaseEvent) EventID() string {
	return e.eventID
}

// EventType returns the event type
func (e *BaseEvent) EventType() string {
	return e.eventType
}

// AggregateID returns the aggregate ID
func (e *BaseEvent) AggregateID() string {
	return e.aggregateID
}

// AggregateType returns the aggregate type
func (e *BaseEvent) AggregateType() string {
	return e.aggregateType
}

// EventVersion returns the event version
func (e *BaseEvent) EventVersion() int {
	return e.eventVersion
}

// OccurredAt returns when the event occurred
func (e *BaseEvent) OccurredAt() time.Time {
	return e.occurredAt
}

// EventData returns the event data
func (e *BaseEvent) EventData() []byte {
	return e.eventData
}

// EventStore defines the interface for event persistence
// Supports both event sourcing and CQRS patterns
// Issue: #2217
type EventStore interface {
	// SaveEvents saves events for an aggregate with optimistic concurrency
	SaveEvents(ctx context.Context, aggregateID string, events []DomainEvent, expectedVersion int) error

	// LoadEvents loads all events for an aggregate
	LoadEvents(ctx context.Context, aggregateID string) ([]DomainEvent, error)

	// LoadEventsFromVersion loads events starting from a specific version
	LoadEventsFromVersion(ctx context.Context, aggregateID string, fromVersion int) ([]DomainEvent, error)

	// GetAggregateVersion returns the current version of an aggregate
	GetAggregateVersion(ctx context.Context, aggregateID string) (int, error)

	// SaveSnapshot saves a snapshot of aggregate state
	SaveSnapshot(ctx context.Context, aggregateID string, snapshot AggregateSnapshot) error

	// LoadSnapshot loads the latest snapshot for an aggregate
	LoadSnapshot(ctx context.Context, aggregateID string) (AggregateSnapshot, error)
}

// AggregateSnapshot represents a snapshot of aggregate state
// Used for performance optimization in event sourcing
// Issue: #2217
type AggregateSnapshot interface {
	// AggregateID returns the aggregate ID
	AggregateID() string
	// Version returns the aggregate version at snapshot time
	Version() int
	// SnapshotData returns the serialized snapshot data
	SnapshotData() []byte
	// CreatedAt returns when the snapshot was created
	CreatedAt() time.Time
}

// AggregateRepository provides high-level aggregate operations
// Handles loading, saving, and snapshot management
// Issue: #2217
type AggregateRepository struct {
	eventStore EventStore
}

// NewAggregateRepository creates a new aggregate repository
// Issue: #2217
func NewAggregateRepository(eventStore EventStore) *AggregateRepository {
	return &AggregateRepository{
		eventStore: eventStore,
	}
}

// Save saves an aggregate with optimistic concurrency control
// Issue: #2217
func (r *AggregateRepository) Save(ctx context.Context, aggregate Aggregate) error {
	events := aggregate.UncommittedEvents()
	if len(events) == 0 {
		return nil
	}

	// Save events with expected version for optimistic locking
	expectedVersion := aggregate.Version() - len(events)
	err := r.eventStore.SaveEvents(ctx, aggregate.ID(), events, expectedVersion)
	if err != nil {
		return err
	}

	// Clear uncommitted events after successful save
	aggregate.ClearUncommittedEvents()

	return nil
}

// Load loads an aggregate from event store
// Reconstructs state from event history
// Issue: #2217
func (r *AggregateRepository) Load(ctx context.Context, aggregateID string, aggregate Aggregate) error {
	// Try to load from snapshot first for performance
	snapshot, err := r.eventStore.LoadSnapshot(ctx, aggregateID)
	if err == nil {
		// Load events from snapshot version onwards
		events, err := r.eventStore.LoadEventsFromVersion(ctx, aggregateID, snapshot.Version())
		if err != nil {
			return err
		}

		// Apply snapshot and remaining events
		err = r.loadFromSnapshot(snapshot, aggregate)
		if err != nil {
			return err
		}

		return aggregate.LoadFromHistory(events)
	}

	// Fallback to loading all events
	events, err := r.eventStore.LoadEvents(ctx, aggregateID)
	if err != nil {
		return err
	}

	return aggregate.LoadFromHistory(events)
}

// loadFromSnapshot loads aggregate state from snapshot
// Must be implemented by concrete repositories for each aggregate type
// Issue: #2217
func (r *AggregateRepository) loadFromSnapshot(snapshot AggregateSnapshot, aggregate Aggregate) error {
	// Default implementation - should be overridden for snapshot support
	return ErrSnapshotNotSupported
}