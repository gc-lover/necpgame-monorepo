// Issue: #2237
// PERFORMANCE: Optimized consumer group handler for high-throughput event processing
package consumers

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

// ConsumerGroupHandler implements sarama.ConsumerGroupHandler
type ConsumerGroupHandler struct {
	name          string
	consumer      Consumer
	topics        []string
	eventRegistry *events.Registry
	logger        *zap.Logger
	metrics       *metrics.Collector

	workers       int
	bufferSize    int
	workerPool    chan *sarama.ConsumerMessage
	workerWg      sync.WaitGroup

	mu       sync.RWMutex
	closed   bool
}

// NewConsumerGroupHandler creates a new consumer group handler
func NewConsumerGroupHandler(name string, consumer Consumer, topics []string, eventRegistry *events.Registry, logger *zap.Logger, metrics *metrics.Collector) *ConsumerGroupHandler {
	// Default worker configuration - can be made configurable
	workers := 10
	bufferSize := 1000

	handler := &ConsumerGroupHandler{
		name:          name,
		consumer:      consumer,
		topics:        topics,
		eventRegistry: eventRegistry,
		logger:        logger,
		metrics:       metrics,
		workers:       workers,
		bufferSize:    bufferSize,
		workerPool:    make(chan *sarama.ConsumerMessage, bufferSize),
	}

	// Start worker pool
	handler.startWorkers()

	return handler
}

// Setup is called at the beginning of a new session
func (h *ConsumerGroupHandler) Setup(sess sarama.ConsumerGroupSession) error {
	h.logger.Info("Consumer group session setup",
		zap.String("consumer", h.name),
		zap.Strings("topics", h.topics),
		zap.Int32("generation_id", sess.GenerationID()),
		zap.Strings("member_id", []string{sess.MemberID()}))

	// Mark all partitions as ready
	for topic, partitions := range sess.Claims() {
		h.metrics.RecordConsumerPartitions(h.name, topic, len(partitions))
		h.logger.Info("Claimed partitions",
			zap.String("consumer", h.name),
			zap.String("topic", topic),
			zap.Int("partitions", len(partitions)),
			zap.Int32s("partition_ids", partitions))
	}

	h.metrics.RecordConsumerWorkers(h.name, h.topics[0], h.workers)

	return nil
}

// Cleanup is called at the end of a session
func (h *ConsumerGroupHandler) Cleanup(sess sarama.ConsumerGroupSession) error {
	h.logger.Info("Consumer group session cleanup",
		zap.String("consumer", h.name),
		zap.Strings("topics", h.topics))

	// Reset metrics
	for _, topic := range h.topics {
		h.metrics.RecordConsumerWorkers(h.name, topic, 0)
	}

	return nil
}

// ConsumeClaim processes messages from a single partition
func (h *ConsumerGroupHandler) ConsumeClaim(sess sarama.ConsumerGroupSession, claim sarama.ConsumerGroupClaim) error {
	h.logger.Info("Starting to consume partition",
		zap.String("consumer", h.name),
		zap.String("topic", claim.Topic()),
		zap.Int32("partition", claim.Partition()))

	// Process messages
	for {
		select {
		case message := <-claim.Messages():
			if message == nil {
				continue
			}

			// Send message to worker pool
			select {
			case h.workerPool <- message:
				// Message sent to worker
			case <-sess.Context().Done():
				h.logger.Info("Consumer claim stopped",
					zap.String("consumer", h.name),
					zap.String("topic", claim.Topic()),
					zap.Int32("partition", claim.Partition()))
				return nil
			}

		case <-sess.Context().Done():
			h.logger.Info("Consumer claim stopped",
				zap.String("consumer", h.name),
				zap.String("topic", claim.Topic()),
				zap.Int32("partition", claim.Partition()))
			return nil
		}
	}
}

// startWorkers starts the worker pool for processing messages
func (h *ConsumerGroupHandler) startWorkers() {
	h.workerWg.Add(h.workers)

	for i := 0; i < h.workers; i++ {
		go h.worker(i)
	}

	h.logger.Info("Started worker pool",
		zap.String("consumer", h.name),
		zap.Int("workers", h.workers))
}

// worker processes messages from the worker pool
func (h *ConsumerGroupHandler) worker(workerID int) {
	defer h.workerWg.Done()

	h.logger.Debug("Worker started",
		zap.String("consumer", h.name),
		zap.Int("worker_id", workerID))

	for {
		select {
		case message, ok := <-h.workerPool:
			if !ok {
				h.logger.Debug("Worker stopping",
					zap.String("consumer", h.name),
					zap.Int("worker_id", workerID))
				return
			}

			h.processMessage(message, workerID)
		}
	}
}

// processMessage processes a single Kafka message
func (h *ConsumerGroupHandler) processMessage(message *sarama.ConsumerMessage, workerID int) {
	startTime := time.Now()

	// Parse event from message
	event, err := h.parseEvent(message)
	if err != nil {
		h.metrics.RecordEventError("parse")
		h.logger.Error("Failed to parse event",
			zap.String("consumer", h.name),
			zap.String("topic", message.Topic),
			zap.Int32("partition", message.Partition),
			zap.Int64("offset", message.Offset),
			zap.Error(err))
		h.handleProcessingError(message, err)
		return
	}

	// Validate event
	if err := h.eventRegistry.ValidateEvent(event.EventType, message.Value); err != nil {
		h.metrics.RecordEventError("validation")
		h.logger.Error("Event validation failed",
			zap.String("consumer", h.name),
			zap.String("event_type", event.EventType),
			zap.String("event_id", event.EventID.String()),
			zap.Error(err))
		h.handleProcessingError(message, err)
		return
	}

	// Process event
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := h.consumer.ProcessEvent(ctx, event); err != nil {
		h.metrics.RecordEventError("processing")
		h.logger.Error("Event processing failed",
			zap.String("consumer", h.name),
			zap.String("event_type", event.EventType),
			zap.String("event_id", event.EventID.String()),
			zap.Error(err))
		h.handleProcessingError(message, err)
		return
	}

	// Record success metrics
	duration := time.Since(startTime)
	h.metrics.RecordEventConsumed(h.name, message.Topic, event.EventType)

	h.logger.Debug("Event processed successfully",
		zap.String("consumer", h.name),
		zap.String("event_type", event.EventType),
		zap.String("event_id", event.EventID.String()),
		zap.String("topic", message.Topic),
		zap.Int32("partition", message.Partition),
		zap.Int64("offset", message.Offset),
		zap.Duration("duration", duration),
		zap.Int("worker_id", workerID))
}

// parseEvent parses a Kafka message into a BaseEvent
func (h *ConsumerGroupHandler) parseEvent(message *sarama.ConsumerMessage) (*events.BaseEvent, error) {
	event, err := h.eventRegistry.DeserializeEvent(message.Value)
	if err != nil {
		return nil, fmt.Errorf("failed to deserialize event: %w", err)
	}

	// Set size in metadata if not present
	if event.Metadata != nil && event.Metadata.SizeBytes == 0 {
		event.Metadata.SizeBytes = len(message.Value)
	}

	return event, nil
}

// handleProcessingError handles errors that occur during message processing
func (h *ConsumerGroupHandler) handleProcessingError(message *sarama.ConsumerMessage, err error) {
	// TODO: Implement dead letter queue logic
	// For now, just log the error

	h.logger.Error("Message processing error - would send to DLQ",
		zap.String("consumer", h.name),
		zap.String("topic", message.Topic),
		zap.Int32("partition", message.Partition),
		zap.Int64("offset", message.Offset),
		zap.Time("timestamp", message.Timestamp),
		zap.Error(err))
}

// Close closes the consumer group handler
func (h *ConsumerGroupHandler) Close() error {
	h.mu.Lock()
	defer h.mu.Unlock()

	if h.closed {
		return nil
	}

	h.closed = true

	// Close worker pool
	close(h.workerPool)

	// Wait for workers to finish
	done := make(chan struct{})
	go func() {
		h.workerWg.Wait()
		close(done)
	}()

	select {
	case <-done:
		h.logger.Info("Consumer group handler closed", zap.String("consumer", h.name))
	case <-time.After(30 * time.Second):
		h.logger.Warn("Consumer group handler close timed out", zap.String("consumer", h.name))
	}

	return nil
}

// IsClosed returns whether the handler is closed
func (h *ConsumerGroupHandler) IsClosed() bool {
	h.mu.RLock()
	defer h.mu.RUnlock()
	return h.closed
}
