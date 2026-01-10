// Base aggregate implementation for Event Sourcing
// Issue: #2217
// Agent: Backend Agent
package aggregates

import (
	"time"

	"github.com/google/uuid"

	"necpgame/services/event-sourcing-aggregates-go/internal/events"
)

// Aggregate represents a domain aggregate root
type Aggregate interface {
	// GetID returns the aggregate ID
	GetID() uuid.UUID

	// GetType returns the aggregate type
	GetType() string

	// GetVersion returns the current version
	GetVersion() int

	// GetUncommittedEvents returns uncommitted domain events
	GetUncommittedEvents() []events.DomainEvent

	// MarkEventsAsCommitted marks all uncommitted events as committed
	MarkEventsAsCommitted()

	// LoadFromEvents loads aggregate state from domain events
	LoadFromEvents(events []events.DomainEvent) error

	// ApplyEvent applies a domain event to the aggregate
	ApplyEvent(event events.DomainEvent) error
}

// BaseAggregate provides common aggregate functionality
type BaseAggregate struct {
	ID               uuid.UUID              `json:"id"`
	Type             string                 `json:"type"`
	Version          int                    `json:"version"`
	CreatedAt        time.Time              `json:"created_at"`
	UpdatedAt        time.Time              `json:"updated_at"`
	uncommittedEvents []events.DomainEvent `json:"-"`
}

// NewBaseAggregate creates a new base aggregate
func NewBaseAggregate(id uuid.UUID, aggregateType string) *BaseAggregate {
	now := time.Now().UTC()
	return &BaseAggregate{
		ID:        id,
		Type:      aggregateType,
		Version:   0,
		CreatedAt: now,
		UpdatedAt: now,
		uncommittedEvents: make([]events.DomainEvent, 0),
	}
}

// GetID returns the aggregate ID
func (a *BaseAggregate) GetID() uuid.UUID {
	return a.ID
}

// GetType returns the aggregate type
func (a *BaseAggregate) GetType() string {
	return a.Type
}

// GetVersion returns the current version
func (a *BaseAggregate) GetVersion() int {
	return a.Version
}

// GetUncommittedEvents returns uncommitted domain events
func (a *BaseAggregate) GetUncommittedEvents() []events.DomainEvent {
	return a.uncommittedEvents
}

// MarkEventsAsCommitted marks all uncommitted events as committed
func (a *BaseAggregate) MarkEventsAsCommitted() {
	a.uncommittedEvents = make([]events.DomainEvent, 0)
}

// LoadFromEvents loads aggregate state from domain events
func (a *BaseAggregate) LoadFromEvents(domainEvents []events.DomainEvent) error {
	for _, event := range domainEvents {
		if err := a.ApplyEvent(event); err != nil {
			return err
		}
		a.Version = event.GetVersion()
	}
	return nil
}

// RaiseEvent adds a domain event to uncommitted events
func (a *BaseAggregate) RaiseEvent(event events.DomainEvent) {
	event.SetAggregateID(a.ID)
	event.SetAggregateType(a.Type)
	event.SetVersion(a.Version + 1)
	event.SetTimestamp(time.Now().UTC())

	a.uncommittedEvents = append(a.uncommittedEvents, event)
	a.UpdatedAt = time.Now().UTC()
}

// ApplyEvent applies a domain event (to be overridden by concrete aggregates)
func (a *BaseAggregate) ApplyEvent(event events.DomainEvent) error {
	// Base implementation - concrete aggregates should override
	return nil
}