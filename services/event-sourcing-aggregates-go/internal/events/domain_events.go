// Domain events for Event Sourcing aggregates
// Issue: #2217
// Agent: Backend Agent
package events

import (
	"time"

	"github.com/google/uuid"
)

// DomainEvent represents a domain event in the event sourcing pattern
type DomainEvent interface {
	// GetEventID returns the unique event ID
	GetEventID() uuid.UUID

	// GetEventType returns the event type name
	GetEventType() string

	// GetAggregateID returns the aggregate ID
	GetAggregateID() uuid.UUID

	// GetAggregateType returns the aggregate type
	GetAggregateType() string

	// GetVersion returns the event version
	GetVersion() int

	// GetTimestamp returns when the event occurred
	GetTimestamp() time.Time

	// GetData returns the event data
	GetData() interface{}

	// GetMetadata returns event metadata
	GetMetadata() map[string]interface{}

	// SetEventID sets the event ID
	SetEventID(id uuid.UUID)

	// SetAggregateID sets the aggregate ID
	SetAggregateID(id uuid.UUID)

	// SetAggregateType sets the aggregate type
	SetAggregateType(aggregateType string)

	// SetVersion sets the event version
	SetVersion(version int)

	// SetTimestamp sets the event timestamp
	SetTimestamp(timestamp time.Time)
}

// BaseDomainEvent provides common domain event functionality
type BaseDomainEvent struct {
	EventID       uuid.UUID              `json:"event_id"`
	EventType     string                 `json:"event_type"`
	AggregateID   uuid.UUID              `json:"aggregate_id"`
	AggregateType string                 `json:"aggregate_type"`
	Version       int                    `json:"version"`
	Timestamp     time.Time              `json:"timestamp"`
	Data          interface{}            `json:"data"`
	Metadata      map[string]interface{} `json:"metadata,omitempty"`
}

// NewBaseDomainEvent creates a new base domain event
func NewBaseDomainEvent(eventType string, data interface{}) *BaseDomainEvent {
	return &BaseDomainEvent{
		EventID:   uuid.New(),
		EventType: eventType,
		Timestamp: time.Now().UTC(),
		Data:      data,
		Metadata:  make(map[string]interface{}),
	}
}

// GetEventID returns the unique event ID
func (e *BaseDomainEvent) GetEventID() uuid.UUID {
	return e.EventID
}

// GetEventType returns the event type name
func (e *BaseDomainEvent) GetEventType() string {
	return e.EventType
}

// GetAggregateID returns the aggregate ID
func (e *BaseDomainEvent) GetAggregateID() uuid.UUID {
	return e.AggregateID
}

// GetAggregateType returns the aggregate type
func (e *BaseDomainEvent) GetAggregateType() string {
	return e.AggregateType
}

// GetVersion returns the event version
func (e *BaseDomainEvent) GetVersion() int {
	return e.Version
}

// GetTimestamp returns when the event occurred
func (e *BaseDomainEvent) GetTimestamp() time.Time {
	return e.Timestamp
}

// GetData returns the event data
func (e *BaseDomainEvent) GetData() interface{} {
	return e.Data
}

// GetMetadata returns event metadata
func (e *BaseDomainEvent) GetMetadata() map[string]interface{} {
	return e.Metadata
}

// SetEventID sets the event ID
func (e *BaseDomainEvent) SetEventID(id uuid.UUID) {
	e.EventID = id
}

// SetAggregateID sets the aggregate ID
func (e *BaseDomainEvent) SetAggregateID(id uuid.UUID) {
	e.AggregateID = id
}

// SetAggregateType sets the aggregate type
func (e *BaseDomainEvent) SetAggregateType(aggregateType string) {
	e.AggregateType = aggregateType
}

// SetVersion sets the event version
func (e *BaseDomainEvent) SetVersion(version int) {
	e.Version = version
}

// SetTimestamp sets the event timestamp
func (e *BaseDomainEvent) SetTimestamp(timestamp time.Time) {
	e.Timestamp = timestamp
}

// AddMetadata adds metadata to the event
func (e *BaseDomainEvent) AddMetadata(key string, value interface{}) {
	if e.Metadata == nil {
		e.Metadata = make(map[string]interface{})
	}
	e.Metadata[key] = value
}

// Player-specific domain events

// PlayerCreatedEvent represents player creation
type PlayerCreatedEvent struct {
	*BaseDomainEvent
	PlayerID   uuid.UUID `json:"player_id"`
	Username   string    `json:"username"`
	Email      string    `json:"email"`
	CreatedAt  time.Time `json:"created_at"`
}

// NewPlayerCreatedEvent creates a new player created event
func NewPlayerCreatedEvent(playerID uuid.UUID, username, email string) *PlayerCreatedEvent {
	data := map[string]interface{}{
		"player_id":  playerID,
		"username":   username,
		"email":      email,
		"created_at": time.Now().UTC(),
	}

	event := &PlayerCreatedEvent{
		BaseDomainEvent: NewBaseDomainEvent("PlayerCreated", data),
		PlayerID:        playerID,
		Username:        username,
		Email:           email,
		CreatedAt:       time.Now().UTC(),
	}

	return event
}

// PlayerLevelGainedEvent represents level gain
type PlayerLevelGainedEvent struct {
	*BaseDomainEvent
	PlayerID    uuid.UUID `json:"player_id"`
	NewLevel    int       `json:"new_level"`
	OldLevel    int       `json:"old_level"`
	Experience  int64     `json:"experience"`
	GainedAt    time.Time `json:"gained_at"`
}

// NewPlayerLevelGainedEvent creates a new level gained event
func NewPlayerLevelGainedEvent(playerID uuid.UUID, newLevel, oldLevel int, experience int64) *PlayerLevelGainedEvent {
	data := map[string]interface{}{
		"player_id":   playerID,
		"new_level":   newLevel,
		"old_level":   oldLevel,
		"experience":  experience,
		"gained_at":   time.Now().UTC(),
	}

	event := &PlayerLevelGainedEvent{
		BaseDomainEvent: NewBaseDomainEvent("PlayerLevelGained", data),
		PlayerID:        playerID,
		NewLevel:        newLevel,
		OldLevel:        oldLevel,
		Experience:      experience,
		GainedAt:        time.Now().UTC(),
	}

	return event
}

// PlayerItemEquippedEvent represents item equipping
type PlayerItemEquippedEvent struct {
	*BaseDomainEvent
	PlayerID uuid.UUID `json:"player_id"`
	ItemID   uuid.UUID `json:"item_id"`
	Slot     string    `json:"slot"`
	EquippedAt time.Time `json:"equipped_at"`
}

// NewPlayerItemEquippedEvent creates a new item equipped event
func NewPlayerItemEquippedEvent(playerID, itemID uuid.UUID, slot string) *PlayerItemEquippedEvent {
	data := map[string]interface{}{
		"player_id":   playerID,
		"item_id":     itemID,
		"slot":        slot,
		"equipped_at": time.Now().UTC(),
	}

	event := &PlayerItemEquippedEvent{
		BaseDomainEvent: NewBaseDomainEvent("PlayerItemEquipped", data),
		PlayerID:        playerID,
		ItemID:          itemID,
		Slot:            slot,
		EquippedAt:      time.Now().UTC(),
	}

	return event
}