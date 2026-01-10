// Package aggregate provides comprehensive tests for Event Sourcing Aggregate implementation
// Issue: #2217 - Event Sourcing Aggregate Implementation
// Agent: Backend Agent
package aggregate

import (
	"encoding/json"
	"testing"
	"time"

	"github.com/google/uuid"
)

func TestBaseAggregate(t *testing.T) {
	// Test aggregate creation
	aggregate := NewBaseAggregate("test-agg", "TestAggregate")
	if aggregate.ID() != "test-agg" {
		t.Errorf("Expected ID 'test-agg', got %s", aggregate.ID())
	}
	if aggregate.Type() != "TestAggregate" {
		t.Errorf("Expected Type 'TestAggregate', got %s", aggregate.Type())
	}
	if aggregate.Version() != 0 {
		t.Errorf("Expected initial version 0, got %d", aggregate.Version())
	}
}

func TestBaseEvent(t *testing.T) {
	event := NewBaseEvent("TestEvent", "test-agg", "TestAggregate", []byte("test data"))

	if event.EventType() != "TestEvent" {
		t.Errorf("Expected event type 'TestEvent', got %s", event.EventType())
	}
	if event.AggregateID() != "test-agg" {
		t.Errorf("Expected aggregate ID 'test-agg', got %s", event.AggregateID())
	}
	if event.AggregateType() != "TestAggregate" {
		t.Errorf("Expected aggregate type 'TestAggregate', got %s", event.AggregateType())
	}
	if event.EventVersion() != 1 {
		t.Errorf("Expected event version 1, got %d", event.EventVersion())
	}
	if len(event.EventData()) == 0 {
		t.Error("Expected non-empty event data")
	}
}

func TestUserAggregate(t *testing.T) {
	// Create user aggregate
	user := NewUserAggregate("user-123")

	// Test initial state
	if user.UserID() != "user-123" {
		t.Errorf("Expected UserID 'user-123', got %s", user.UserID())
	}
	if user.Version() != 0 {
		t.Errorf("Expected initial version 0, got %d", user.Version())
	}

	// Create user
	err := user.CreateUser("john@example.com", "John Doe")
	if err != nil {
		t.Fatalf("Failed to create user: %v", err)
	}

	// Check state after creation
	if user.Email() != "john@example.com" {
		t.Errorf("Expected email 'john@example.com', got %s", user.Email())
	}
	if user.Name() != "John Doe" {
		t.Errorf("Expected name 'John Doe', got %s", user.Name())
	}
	if !user.IsActive() {
		t.Error("Expected user to be active")
	}
	if user.Version() != 1 {
		t.Errorf("Expected version 1, got %d", user.Version())
	}

	// Update user
	err = user.UpdateUser("john.doe@example.com", "John Doe Updated")
	if err != nil {
		t.Fatalf("Failed to update user: %v", err)
	}

	// Check state after update
	if user.Email() != "john.doe@example.com" {
		t.Errorf("Expected updated email 'john.doe@example.com', got %s", user.Email())
	}
	if user.Name() != "John Doe Updated" {
		t.Errorf("Expected updated name 'John Doe Updated', got %s", user.Name())
	}
	if user.Version() != 2 {
		t.Errorf("Expected version 2, got %d", user.Version())
	}

	// Deactivate user
	err = user.DeactivateUser()
	if err != nil {
		t.Fatalf("Failed to deactivate user: %v", err)
	}

	// Check state after deactivation
	if user.IsActive() {
		t.Error("Expected user to be inactive")
	}
	if user.Version() != 3 {
		t.Errorf("Expected version 3, got %d", user.Version())
	}

	// Check uncommitted events
	events := user.UncommittedEvents()
	if len(events) != 3 {
		t.Errorf("Expected 3 uncommitted events, got %d", len(events))
	}

	// Test event types
	expectedTypes := []string{"UserCreated", "UserUpdated", "UserDeactivated"}
	for i, event := range events {
		if event.EventType() != expectedTypes[i] {
			t.Errorf("Expected event type %s, got %s", expectedTypes[i], event.EventType())
		}
		if event.AggregateID() != "user-123" {
			t.Errorf("Expected aggregate ID 'user-123', got %s", event.AggregateID())
		}
		if event.EventVersion() != i+1 {
			t.Errorf("Expected event version %d, got %d", i+1, event.EventVersion())
		}
	}
}

func TestAggregateLoadFromHistory(t *testing.T) {
	// Create initial user
	user := NewUserAggregate("user-456")

	// Simulate event history
	events := []DomainEvent{
		createUserCreatedEvent("user-456", "jane@example.com", "Jane Smith"),
		createUserUpdatedEvent("user-456", "jane.smith@example.com", "Jane Smith Updated"),
		createUserDeactivatedEvent("user-456"),
	}

	// Load from history
	err := user.LoadFromHistory(events)
	if err != nil {
		t.Fatalf("Failed to load from history: %v", err)
	}

	// Check final state
	if user.Email() != "jane.smith@example.com" {
		t.Errorf("Expected email 'jane.smith@example.com', got %s", user.Email())
	}
	if user.Name() != "Jane Smith Updated" {
		t.Errorf("Expected name 'Jane Smith Updated', got %s", user.Name())
	}
	if user.IsActive() {
		t.Error("Expected user to be inactive")
	}
	if user.Version() != 3 {
		t.Errorf("Expected version 3, got %d", user.Version())
	}
}

func TestAggregateOptimisticConcurrency(t *testing.T) {
	user := NewUserAggregate("user-789")

	// Apply first event
	err := user.CreateUser("test@example.com", "Test User")
	if err != nil {
		t.Fatalf("Failed to create user: %v", err)
	}

	// Clear events (simulate save)
	user.ClearUncommittedEvents()

	// Apply second event
	err = user.UpdateUser("updated@example.com", "Updated User")
	if err != nil {
		t.Fatalf("Failed to update user: %v", err)
	}

	// Check version progression
	if user.Version() != 2 {
		t.Errorf("Expected version 2, got %d", user.Version())
	}
}

// Helper functions for creating test events
func createUserCreatedEvent(userID, email, name string) DomainEvent {
	eventData := UserCreatedEvent{
		UserID: userID,
		Email:  email,
		Name:   name,
	}

	eventJSON, _ := json.Marshal(eventData)
	return &BaseEvent{
		eventID:       uuid.New().String(),
		eventType:     "UserCreated",
		aggregateID:   userID,
		aggregateType: "User",
		eventVersion:  1,
		occurredAt:    time.Now().UTC(),
		eventData:     eventJSON,
	}
}

func createUserUpdatedEvent(userID, email, name string) DomainEvent {
	eventData := UserUpdatedEvent{
		UserID: userID,
		Email:  email,
		Name:   name,
	}

	eventJSON, _ := json.Marshal(eventData)
	return &BaseEvent{
		eventID:       uuid.New().String(),
		eventType:     "UserUpdated",
		aggregateID:   userID,
		aggregateType: "User",
		eventVersion:  2,
		occurredAt:    time.Now().UTC(),
		eventData:     eventJSON,
	}
}

func createUserDeactivatedEvent(userID string) DomainEvent {
	eventData := UserDeactivatedEvent{
		UserID: userID,
	}

	eventJSON, _ := json.Marshal(eventData)
	return &BaseEvent{
		eventID:       uuid.New().String(),
		eventType:     "UserDeactivated",
		aggregateID:   userID,
		aggregateType: "User",
		eventVersion:  3,
		occurredAt:    time.Now().UTC(),
		eventData:     eventJSON,
	}
}