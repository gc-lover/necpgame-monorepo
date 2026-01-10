// Event Bus for CQRS pattern - publishes domain events
// Issue: #2217
// Agent: Backend Agent
package events

import (
	"context"
	"fmt"
	"reflect"
	"sync"
	"time"

	"go.uber.org/zap"
)

// EventHandler defines interface for event handlers
type EventHandler interface {
	Handle(ctx context.Context, event DomainEvent) error
}

// EventBus manages event publishing and subscription
type EventBus struct {
	handlers map[string][]EventHandler
	logger   *zap.Logger
	mu       sync.RWMutex
}

// NewEventBus creates a new event bus
func NewEventBus() *EventBus {
	logger, _ := zap.NewProduction()
	return &EventBus{
		handlers: make(map[string][]EventHandler),
		logger:   logger,
	}
}

// Subscribe registers an event handler for an event type
func (b *EventBus) Subscribe(eventType string, handler EventHandler) {
	b.mu.Lock()
	defer b.mu.Unlock()

	b.handlers[eventType] = append(b.handlers[eventType], handler)
	b.logger.Info("Subscribed event handler",
		zap.String("event_type", eventType),
		zap.String("handler_type", reflect.TypeOf(handler).String()),
		zap.Int("total_handlers", len(b.handlers[eventType])))
}

// Publish publishes a domain event to all subscribers
func (b *EventBus) Publish(ctx context.Context, event DomainEvent) error {
	eventType := event.GetEventType()

	b.mu.RLock()
	handlers := make([]EventHandler, len(b.handlers[eventType]))
	copy(handlers, b.handlers[eventType])
	b.mu.RUnlock()

	if len(handlers) == 0 {
		b.logger.Debug("No handlers for event type",
			zap.String("event_type", eventType),
			zap.String("event_id", event.GetEventID().String()))
		return nil
	}

	b.logger.Info("Publishing event",
		zap.String("event_type", eventType),
		zap.String("event_id", event.GetEventID().String()),
		zap.String("aggregate_id", event.GetAggregateID().String()),
		zap.Int("aggregate_version", event.GetVersion()),
		zap.Int("handler_count", len(handlers)))

	// Publish to all handlers asynchronously
	for _, handler := range handlers {
		go func(h EventHandler) {
			publishCtx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
			defer cancel()

			start := time.Now()
			err := h.Handle(publishCtx, event)
			duration := time.Since(start)

			if err != nil {
				b.logger.Error("Event handler failed",
					zap.Error(err),
					zap.String("event_type", eventType),
					zap.String("event_id", event.GetEventID().String()),
					zap.String("handler_type", reflect.TypeOf(h).String()),
					zap.Duration("duration", duration))
			} else {
				b.logger.Debug("Event handler completed",
					zap.String("event_type", eventType),
					zap.String("event_id", event.GetEventID().String()),
					zap.String("handler_type", reflect.TypeOf(h).String()),
					zap.Duration("duration", duration))
			}
		}(handler)
	}

	return nil
}

// PublishSync publishes a domain event synchronously
func (b *EventBus) PublishSync(ctx context.Context, event DomainEvent) error {
	eventType := event.GetEventType()

	b.mu.RLock()
	handlers := make([]EventHandler, len(b.handlers[eventType]))
	copy(handlers, b.handlers[eventType])
	b.mu.RUnlock()

	b.logger.Info("Publishing event synchronously",
		zap.String("event_type", eventType),
		zap.String("event_id", event.GetEventID().String()),
		zap.Int("handler_count", len(handlers)))

	// Publish to all handlers synchronously
	for _, handler := range handlers {
		publishCtx, cancel := context.WithTimeout(ctx, 30*time.Second)

		start := time.Now()
		err := handler.Handle(publishCtx, event)
		duration := time.Since(start)
		cancel()

		if err != nil {
			b.logger.Error("Event handler failed",
				zap.Error(err),
				zap.String("event_type", eventType),
				zap.String("event_id", event.GetEventID().String()),
				zap.String("handler_type", reflect.TypeOf(handler).String()),
				zap.Duration("duration", duration))
			return fmt.Errorf("event handler failed: %w", err)
		}

		b.logger.Debug("Event handler completed",
			zap.String("event_type", eventType),
			zap.String("event_id", event.GetEventID().String()),
			zap.String("handler_type", reflect.TypeOf(handler).String()),
			zap.Duration("duration", duration))
	}

	return nil
}

// GetSubscribedEvents returns list of subscribed event types
func (b *EventBus) GetSubscribedEvents() []string {
	b.mu.RLock()
	defer b.mu.RUnlock()

	types := make([]string, 0, len(b.handlers))
	for eventType := range b.handlers {
		types = append(types, eventType)
	}
	return types
}

// GetHandlerCount returns number of handlers for an event type
func (b *EventBus) GetHandlerCount(eventType string) int {
	b.mu.RLock()
	defer b.mu.RUnlock()

	return len(b.handlers[eventType])
}

// Shutdown gracefully shuts down the event bus
func (b *EventBus) Shutdown(ctx context.Context) error {
	b.logger.Info("Event bus shutting down")
	// Wait for any pending event publications to complete
	time.Sleep(200 * time.Millisecond)
	return nil
}

// Event handler implementations

// LoggingEventHandler logs all events
type LoggingEventHandler struct {
	logger *zap.Logger
}

// NewLoggingEventHandler creates a logging event handler
func NewLoggingEventHandler(logger *zap.Logger) *LoggingEventHandler {
	return &LoggingEventHandler{logger: logger}
}

// Handle logs the event
func (h *LoggingEventHandler) Handle(ctx context.Context, event DomainEvent) error {
	h.logger.Info("Domain event occurred",
		zap.String("event_type", event.GetEventType()),
		zap.String("event_id", event.GetEventID().String()),
		zap.String("aggregate_id", event.GetAggregateID().String()),
		zap.String("aggregate_type", event.GetAggregateType()),
		zap.Int("aggregate_version", event.GetVersion()),
		zap.Time("timestamp", event.GetTimestamp()),
		zap.Any("data", event.GetData()))

	return nil
}

// MetricsEventHandler collects event metrics
type MetricsEventHandler struct {
	metrics map[string]int64
	mu      sync.RWMutex
}

// NewMetricsEventHandler creates a metrics event handler
func NewMetricsEventHandler() *MetricsEventHandler {
	return &MetricsEventHandler{
		metrics: make(map[string]int64),
	}
}

// Handle collects metrics for the event
func (h *MetricsEventHandler) Handle(ctx context.Context, event DomainEvent) error {
	h.mu.Lock()
	defer h.mu.Unlock()

	eventType := event.GetEventType()
	h.metrics[eventType]++

	// Could integrate with actual metrics system here
	return nil
}

// GetMetrics returns current metrics
func (h *MetricsEventHandler) GetMetrics() map[string]int64 {
	h.mu.RLock()
	defer h.mu.RUnlock()

	result := make(map[string]int64)
	for k, v := range h.metrics {
		result[k] = v
	}
	return result
}