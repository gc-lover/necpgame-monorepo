// Issue: #2217
// PERFORMANCE: Optimized base aggregate with memory-efficient event handling
package aggregates

import (
	"fmt"
	"time"

	"github.com/google/uuid"

	"event-sourcing-aggregates-go/internal/events"
)

// BaseAggregate provides common aggregate functionality
type BaseAggregate struct {
	ID               uuid.UUID
	Type             string
	Version          int
	UncommittedEvents []events.DomainEventInterface
}

// NewBaseAggregate creates a new base aggregate
func NewBaseAggregate(aggregateType string, aggregateID uuid.UUID) BaseAggregate {
	return BaseAggregate{
		ID:               aggregateID,
		Type:             aggregateType,
		Version:          0,
		UncommittedEvents: make([]events.DomainEventInterface, 0),
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

// SetVersion sets the aggregate version
func (a *BaseAggregate) SetVersion(version int) {
	a.Version = version
}

// GetUncommittedEvents returns uncommitted events
func (a *BaseAggregate) GetUncommittedEvents() []events.DomainEventInterface {
	return a.UncommittedEvents
}

// ClearUncommittedEvents clears uncommitted events
func (a *BaseAggregate) ClearUncommittedEvents() {
	a.UncommittedEvents = a.UncommittedEvents[:0]
}

// AddEvent adds an event to uncommitted events
func (a *BaseAggregate) AddEvent(event events.DomainEventInterface) {
	event.SetAggregateID(a.ID)
	event.SetTimestamp(time.Now())
	a.UncommittedEvents = append(a.UncommittedEvents, event)
	a.Version++
}

// ApplyEvent applies an event to the aggregate (must be overridden)
func (a *BaseAggregate) ApplyEvent(event events.DomainEventInterface) error {
	panic("ApplyEvent() must be implemented by concrete aggregate")
}

// ValidateState validates aggregate state
func (a *BaseAggregate) ValidateState() error {
	// Base validation - can be overridden
	if a.ID == uuid.Nil {
		return fmt.Errorf("aggregate ID cannot be nil")
	}
	if a.Version < 0 {
		return fmt.Errorf("aggregate version cannot be negative")
	}
	return nil
}
