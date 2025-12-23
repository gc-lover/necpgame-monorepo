// Issue: #2237
// PERFORMANCE: Optimized event producer for high-throughput Kafka publishing
package producers

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/IBM/sarama"
	"go.uber.org/zap"

	"kafka-event-driven-core/internal/events"
	"kafka-event-driven-core/internal/metrics"
)

// EventProducer handles publishing events to Kafka topics
type EventProducer struct {
	producer   sarama.SyncProducer
	topic      string
	registry   *events.Registry
	logger     *zap.Logger
	metrics    *metrics.Collector
	mu         sync.RWMutex
	closed     bool
}

// NewEventProducer creates a new event producer
func NewEventProducer(brokers []string, topic string, registry *events.Registry, logger *zap.Logger, metrics *metrics.Collector) (*EventProducer, error) {
	config := sarama.NewConfig()
	config.Producer.RequiredAcks = sarama.WaitForAll
	config.Producer.Retry.Max = 5
	config.Producer.Retry.Backoff = 100 * time.Millisecond
	config.Producer.Return.Successes = true
	config.Producer.Compression = sarama.CompressionSnappy
	config.Producer.Flush.Frequency = 500 * time.Millisecond
	config.Producer.Flush.Messages = 100
	config.ClientID = "event-producer-" + topic

	producer, err := sarama.NewSyncProducer(brokers, config)
	if err != nil {
		return nil, fmt.Errorf("failed to create Kafka producer: %w", err)
	}

	ep := &EventProducer{
		producer: producer,
		topic:    topic,
		registry: registry,
		logger:   logger,
		metrics:  metrics,
	}

	logger.Info("Created event producer",
		zap.String("topic", topic),
		zap.Strings("brokers", brokers))

	return ep, nil
}

// PublishEvent publishes a single event to the Kafka topic
func (ep *EventProducer) PublishEvent(ctx context.Context, event *events.BaseEvent) error {
	ep.mu.RLock()
	if ep.closed {
		ep.mu.RUnlock()
		return fmt.Errorf("producer is closed")
	}
	ep.mu.RUnlock()

	startTime := time.Now()

	// Validate event before publishing
	data, err := ep.registry.SerializeEvent(event)
	if err != nil {
		ep.metrics.RecordEventError("serialization")
		return fmt.Errorf("failed to serialize event: %w", err)
	}

	if err := ep.registry.ValidateEvent(event.EventType, data); err != nil {
		ep.metrics.RecordEventError("validation")
		ep.logger.Error("Event validation failed",
			zap.String("event_type", event.EventType),
			zap.String("event_id", event.EventID.String()),
			zap.Error(err))
		return fmt.Errorf("event validation failed: %w", err)
	}

	// Create Kafka message
	msg := &sarama.ProducerMessage{
		Topic: ep.topic,
		Key:   sarama.StringEncoder(event.EventID.String()),
		Value: sarama.ByteEncoder(data),
		Timestamp: time.Now(),
		Headers: ep.createHeaders(event),
	}

	// Publish message
	partition, offset, err := ep.producer.SendMessage(msg)
	_ = partition // We don't use partition in this context
	if err != nil {
		ep.metrics.RecordEventError("publish")
		ep.logger.Error("Failed to publish event",
			zap.String("topic", ep.topic),
			zap.String("event_id", event.EventID.String()),
			zap.String("event_type", event.EventType),
			zap.Error(err))
		return fmt.Errorf("failed to publish event: %w", err)
	}

	// Record metrics
	duration := time.Since(startTime)
	ep.metrics.RecordEventPublished(ep.topic, duration, len(data))

	ep.logger.Debug("Event published successfully",
		zap.String("topic", ep.topic),
		zap.Int32("partition", partition),
		zap.Int64("offset", offset),
		zap.String("event_id", event.EventID.String()),
		zap.String("event_type", event.EventType),
		zap.Duration("duration", duration))

	return nil
}

// PublishEvents publishes multiple events in batch
func (ep *EventProducer) PublishEvents(ctx context.Context, events []*events.BaseEvent) error {
	ep.mu.RLock()
	if ep.closed {
		ep.mu.RUnlock()
		return fmt.Errorf("producer is closed")
	}
	ep.mu.RUnlock()

	if len(events) == 0 {
		return nil
	}

	startTime := time.Now()
	totalSize := 0

	// Prepare messages
	messages := make([]*sarama.ProducerMessage, len(events))
	for i, event := range events {
		// Validate and serialize each event
		data, err := ep.registry.SerializeEvent(event)
		if err != nil {
			ep.metrics.RecordEventError("serialization")
			return fmt.Errorf("failed to serialize event %d: %w", i, err)
		}

		if err := ep.registry.ValidateEvent(event.EventType, data); err != nil {
			ep.metrics.RecordEventError("validation")
			return fmt.Errorf("event %d validation failed: %w", i, err)
		}

		messages[i] = &sarama.ProducerMessage{
			Topic:     ep.topic,
			Key:       sarama.StringEncoder(event.EventID.String()),
			Value:     sarama.ByteEncoder(data),
			Timestamp: time.Now(),
			Headers:   ep.createHeaders(event),
		}

		totalSize += len(data)
	}

	// Publish batch
	err := ep.producer.SendMessages(messages)
	if err != nil {
		ep.metrics.RecordEventError("batch_publish")
		ep.logger.Error("Failed to publish event batch",
			zap.String("topic", ep.topic),
			zap.Int("batch_size", len(events)),
			zap.Error(err))
		return fmt.Errorf("failed to publish event batch: %w", err)
	}

	// Record metrics
	duration := time.Since(startTime)
	ep.metrics.RecordBatchPublished(ep.topic, len(events), duration, totalSize)

	ep.logger.Info("Event batch published successfully",
		zap.String("topic", ep.topic),
		zap.Int("batch_size", len(events)),
		zap.Int("total_bytes", totalSize),
		zap.Duration("duration", duration))

	return nil
}

// createHeaders creates Kafka message headers from event metadata
func (ep *EventProducer) createHeaders(event *events.BaseEvent) []sarama.RecordHeader {
	headers := []sarama.RecordHeader{
		{
			Key:   []byte("event_type"),
			Value: []byte(event.EventType),
		},
		{
			Key:   []byte("source"),
			Value: []byte(event.Source),
		},
		{
			Key:   []byte("version"),
			Value: []byte(event.Version),
		},
	}

	if event.CorrelationID != nil {
		headers = append(headers, sarama.RecordHeader{
			Key:   []byte("correlation_id"),
			Value: []byte(event.CorrelationID.String()),
		})
	}

	if event.SessionID != nil {
		headers = append(headers, sarama.RecordHeader{
			Key:   []byte("session_id"),
			Value: []byte(event.SessionID.String()),
		})
	}

	if event.PlayerID != nil {
		headers = append(headers, sarama.RecordHeader{
			Key:   []byte("player_id"),
			Value: []byte(event.PlayerID.String()),
		})
	}

	if event.Metadata != nil {
		headers = append(headers, sarama.RecordHeader{
			Key:   []byte("priority"),
			Value: []byte(event.Metadata.Priority),
		})

		if event.Metadata.TTL != "" {
			headers = append(headers, sarama.RecordHeader{
				Key:   []byte("ttl"),
				Value: []byte(event.Metadata.TTL),
			})
		}
	}

	return headers
}

// HealthCheck performs a health check on the producer
func (ep *EventProducer) HealthCheck() error {
	ep.mu.RLock()
	defer ep.mu.RUnlock()

	if ep.closed {
		return fmt.Errorf("producer is closed")
	}

	// Simple health check - try to get broker metadata
	_, _, err := ep.producer.SendMessage(&sarama.ProducerMessage{
		Topic: ep.topic,
		Key:   sarama.StringEncoder("health-check"),
		Value: sarama.StringEncoder("ping"),
	})

	return err
}

// Close closes the producer
func (ep *EventProducer) Close() error {
	ep.mu.Lock()
	defer ep.mu.Unlock()

	if ep.closed {
		return nil
	}

	ep.closed = true

	if err := ep.producer.Close(); err != nil {
		ep.logger.Error("Failed to close producer", zap.Error(err))
		return err
	}

	ep.logger.Info("Event producer closed", zap.String("topic", ep.topic))
	return nil
}

// IsClosed returns whether the producer is closed
func (ep *EventProducer) IsClosed() bool {
	ep.mu.RLock()
	defer ep.mu.RUnlock()
	return ep.closed
}

// GetTopic returns the topic name
func (ep *EventProducer) GetTopic() string {
	return ep.topic
}
