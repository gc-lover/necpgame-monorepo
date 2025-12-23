// Issue: #2217
// PERFORMANCE: Optimized event bus for high-throughput event publishing
package events

import (
	"context"
	"fmt"
	"reflect"
	"sync"
	"time"

	"go.uber.org/zap"
)

// EventHandler defines the interface for event handlers
type EventHandler interface {
	Handle(ctx context.Context, event DomainEvent) error
}

// EventHandlerFunc is a function type for event handlers
type EventHandlerFunc func(ctx context.Context, event DomainEvent) error

// Handle implements EventHandler interface for functions
func (f EventHandlerFunc) Handle(ctx context.Context, event DomainEvent) error {
	return f(ctx, event)
}

// Bus manages event publishing and subscription
type Bus struct {
	handlers map[string][]EventHandler
	logger   *zap.Logger
	mu       sync.RWMutex
}

// NewBus creates a new event bus
func NewBus(logger *zap.Logger) *Bus {
	return &Bus{
		handlers: make(map[string][]EventHandler),
		logger:   logger,
	}
}

// Subscribe registers an event handler for an event type
func (b *Bus) Subscribe(eventType string, handler EventHandler) error {
	b.mu.Lock()
	defer b.mu.Unlock()

	b.handlers[eventType] = append(b.handlers[eventType], handler)

	b.logger.Info("Event handler subscribed",
		zap.String("event_type", eventType),
		zap.String("handler_type", reflect.TypeOf(handler).String()),
		zap.Int("total_handlers", len(b.handlers[eventType])))

	return nil
}

// Publish publishes an event to all subscribed handlers
func (b *Bus) Publish(ctx context.Context, event DomainEvent) error {
	eventType := event.EventType()

	b.mu.RLock()
	handlers, exists := b.handlers[eventType]
	b.mu.RUnlock()

	if !exists || len(handlers) == 0 {
		b.logger.Debug("No handlers subscribed for event type",
			zap.String("event_type", eventType))
		return nil
	}

	// Publish to all handlers concurrently
	errChan := make(chan error, len(handlers))
	var wg sync.WaitGroup

	startTime := time.Now()

	for _, handler := range handlers {
		wg.Add(1)
		go func(h EventHandler) {
			defer wg.Done()

			handlerStart := time.Now()
			err := h.Handle(ctx, event)
			handlerDuration := time.Since(handlerStart)

			if err != nil {
				b.logger.Error("Event handler failed",
					zap.String("event_type", eventType),
					zap.String("aggregate_id", event.AggregateID().String()),
					zap.Error(err),
					zap.Duration("duration", handlerDuration))
				errChan <- err
			} else {
				b.logger.Debug("Event handler completed",
					zap.String("event_type", eventType),
					zap.String("aggregate_id", event.AggregateID().String()),
					zap.Duration("duration", handlerDuration))
			}
		}(handler)
	}

	// Wait for all handlers to complete
	wg.Wait()
	close(errChan)

	// Collect errors
	var errors []error
	for err := range errChan {
		errors = append(errors, err)
	}

	duration := time.Since(startTime)

	if len(errors) > 0 {
		b.logger.Error("Event publishing completed with errors",
			zap.String("event_type", eventType),
			zap.String("aggregate_id", event.AggregateID().String()),
			zap.Int("handlers_count", len(handlers)),
			zap.Int("errors_count", len(errors)),
			zap.Duration("total_duration", duration))
		return fmt.Errorf("event publishing failed with %d errors", len(errors))
	}

	b.logger.Info("Event published successfully",
		zap.String("event_type", eventType),
		zap.String("aggregate_id", event.AggregateID().String()),
		zap.Int("handlers_count", len(handlers)),
		zap.Duration("total_duration", duration))

	return nil
}

// PublishAsync publishes an event asynchronously
func (b *Bus) PublishAsync(ctx context.Context, event DomainEvent) chan error {
	errChan := make(chan error, 1)

	go func() {
		defer close(errChan)
		errChan <- b.Publish(ctx, event)
	}()

	return errChan
}

// GetSubscribedEvents returns all subscribed event types
func (b *Bus) GetSubscribedEvents() map[string]int {
	b.mu.RLock()
	defer b.mu.RUnlock()

	result := make(map[string]int)
	for eventType, handlers := range b.handlers {
		result[eventType] = len(handlers)
	}

	return result
}

// Unsubscribe removes all handlers for an event type
func (b *Bus) Unsubscribe(eventType string) {
	b.mu.Lock()
	defer b.mu.Unlock()

	if handlers, exists := b.handlers[eventType]; exists {
		delete(b.handlers, eventType)
		b.logger.Info("Event handlers unsubscribed",
			zap.String("event_type", eventType),
			zap.Int("handlers_removed", len(handlers)))
	}
}

// UnsubscribeHandler removes a specific handler for an event type
func (b *Bus) UnsubscribeHandler(eventType string, handler EventHandler) {
	b.mu.Lock()
	defer b.mu.Unlock()

	handlers, exists := b.handlers[eventType]
	if !exists {
		return
	}

	// Find and remove the handler
	for i, h := range handlers {
		if reflect.ValueOf(h).Pointer() == reflect.ValueOf(handler).Pointer() {
			b.handlers[eventType] = append(handlers[:i], handlers[i+1:]...)
			b.logger.Info("Event handler unsubscribed",
				zap.String("event_type", eventType),
				zap.String("handler_type", reflect.TypeOf(handler).String()))
			break
		}
	}

	// Remove the event type if no handlers left
	if len(b.handlers[eventType]) == 0 {
		delete(b.handlers, eventType)
	}
}
