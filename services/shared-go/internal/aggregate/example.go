// Package aggregate provides example implementation of Event Sourcing Aggregate
// Demonstrates User aggregate with event sourcing capabilities
//
// Issue: #2217 - Event Sourcing Aggregate Implementation
// Agent: Backend Agent
package aggregate

import (
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
)

// UserAggregate demonstrates a concrete aggregate implementation
// Represents a User domain object with event sourcing
// Issue: #2217
type UserAggregate struct {
	*BaseAggregate
	userID   string
	email    string
	name     string
	isActive bool
	createdAt time.Time
	updatedAt time.Time
}

// NewUserAggregate creates a new user aggregate
// Issue: #2217
func NewUserAggregate(userID string) *UserAggregate {
	aggregate := &UserAggregate{
		BaseAggregate: NewBaseAggregate(userID, "User"),
		userID:        userID,
		isActive:      true,
		createdAt:     time.Now().UTC(),
	}
	return aggregate
}

// RaiseEvent overrides base RaiseEvent to ensure proper method dispatch
// Issue: #2217
func (u *UserAggregate) RaiseEvent(eventType string, eventData []byte) error {
	event := &BaseEvent{
		eventID:       uuid.New().String(),
		eventType:     eventType,
		aggregateID:   u.id,
		aggregateType: u.aggregateType,
		eventVersion:  u.version + 1,
		occurredAt:    time.Now().UTC(),
		eventData:     eventData,
	}

	if err := u.applyEvent(event); err != nil {
		return err
	}
	u.version++
	u.uncommittedEvents = append(u.uncommittedEvents, event)
	return nil
}

// LoadFromHistory overrides base LoadFromHistory to ensure proper method dispatch
// Issue: #2217
func (u *UserAggregate) LoadFromHistory(events []DomainEvent) error {
	for _, event := range events {
		if event.AggregateID() != u.id {
			return ErrAggregateIDMismatch
		}

		err := u.applyEvent(event)
		if err != nil {
			return err
		}

		u.version = event.EventVersion()
	}
	return nil
}

// UserCreatedEvent represents user creation event
// Issue: #2217
type UserCreatedEvent struct {
	UserID   string `json:"user_id"`
	Email    string `json:"email"`
	Name     string `json:"name"`
}

// UserUpdatedEvent represents user update event
// Issue: #2217
type UserUpdatedEvent struct {
	UserID   string `json:"user_id"`
	Email    string `json:"email,omitempty"`
	Name     string `json:"name,omitempty"`
}

// UserDeactivatedEvent represents user deactivation event
// Issue: #2217
type UserDeactivatedEvent struct {
	UserID string `json:"user_id"`
}

// CreateUser creates a new user and raises UserCreated event
// Issue: #2217
func (u *UserAggregate) CreateUser(email, name string) error {
	if u.userID == "" {
		return fmt.Errorf("user ID is required")
	}
	if email == "" {
		return fmt.Errorf("email is required")
	}
	if name == "" {
		return fmt.Errorf("name is required")
	}

	eventData := UserCreatedEvent{
		UserID: u.userID,
		Email:  email,
		Name:   name,
	}

	eventJSON, err := json.Marshal(eventData)
	if err != nil {
		return fmt.Errorf("failed to marshal event: %w", err)
	}

	return u.RaiseEvent("UserCreated", eventJSON)
}

// UpdateUser updates user information and raises UserUpdated event
// Issue: #2217
func (u *UserAggregate) UpdateUser(email, name string) error {
	eventData := UserUpdatedEvent{
		UserID: u.userID,
		Email:  email,
		Name:   name,
	}

	eventJSON, err := json.Marshal(eventData)
	if err != nil {
		return fmt.Errorf("failed to marshal event: %w", err)
	}

	return u.RaiseEvent("UserUpdated", eventJSON)
}

// DeactivateUser deactivates the user and raises UserDeactivated event
// Issue: #2217
func (u *UserAggregate) DeactivateUser() error {
	if !u.isActive {
		return fmt.Errorf("user is already deactivated")
	}

	eventData := UserDeactivatedEvent{
		UserID: u.userID,
	}

	eventJSON, err := json.Marshal(eventData)
	if err != nil {
		return fmt.Errorf("failed to marshal event: %w", err)
	}

	return u.RaiseEvent("UserDeactivated", eventJSON)
}

// applyEvent applies domain events to update aggregate state
// This is the core event sourcing mechanism - override this method in concrete aggregates
// Issue: #2217
func (u *UserAggregate) applyEvent(event DomainEvent) error {
	switch event.EventType() {
	case "UserCreated":
		var eventData UserCreatedEvent
		if err := json.Unmarshal(event.EventData(), &eventData); err != nil {
			return fmt.Errorf("failed to unmarshal UserCreated event: %w", err)
		}
		u.userID = eventData.UserID
		u.email = eventData.Email
		u.name = eventData.Name
		u.isActive = true
		u.createdAt = event.OccurredAt()
		u.updatedAt = event.OccurredAt()

	case "UserUpdated":
		var eventData UserUpdatedEvent
		if err := json.Unmarshal(event.EventData(), &eventData); err != nil {
			return fmt.Errorf("failed to unmarshal UserUpdated event: %w", err)
		}
		if eventData.Email != "" {
			u.email = eventData.Email
		}
		if eventData.Name != "" {
			u.name = eventData.Name
		}
		u.updatedAt = event.OccurredAt()

	case "UserDeactivated":
		var eventData UserDeactivatedEvent
		if err := json.Unmarshal(event.EventData(), &eventData); err != nil {
			return fmt.Errorf("failed to unmarshal UserDeactivated event: %w", err)
		}
		u.isActive = false
		u.updatedAt = event.OccurredAt()

	default:
		return fmt.Errorf("unknown event type: %s", event.EventType())
	}

	return nil
}

// Getters for aggregate state
// Issue: #2217
func (u *UserAggregate) UserID() string {
	return u.userID
}

func (u *UserAggregate) Email() string {
	return u.email
}

func (u *UserAggregate) Name() string {
	return u.name
}

func (u *UserAggregate) IsActive() bool {
	return u.isActive
}

func (u *UserAggregate) CreatedAt() time.Time {
	return u.createdAt
}

func (u *UserAggregate) UpdatedAt() time.Time {
	return u.updatedAt
}

// Example usage of the aggregate system
// Issue: #2217
func ExampleUserAggregate() {
	// Create new user aggregate
	user := NewUserAggregate("user-123")

	// Create user
	err := user.CreateUser("john@example.com", "John Doe")
	if err != nil {
		panic(err)
	}

	// Update user
	err = user.UpdateUser("john.doe@example.com", "John Doe Updated")
	if err != nil {
		panic(err)
	}

	// Deactivate user
	err = user.DeactivateUser()
	if err != nil {
		panic(err)
	}

	// Check final state
	fmt.Printf("User ID: %s\n", user.UserID())
	fmt.Printf("Email: %s\n", user.Email())
	fmt.Printf("Name: %s\n", user.Name())
	fmt.Printf("Is Active: %t\n", user.IsActive())
	fmt.Printf("Version: %d\n", user.Version())
	fmt.Printf("Uncommitted Events: %d\n", len(user.UncommittedEvents()))

	// Output:
	// User ID: user-123
	// Email: john.doe@example.com
	// Name: John Doe Updated
	// Is Active: false
	// Version: 3
	// Uncommitted Events: 3
}