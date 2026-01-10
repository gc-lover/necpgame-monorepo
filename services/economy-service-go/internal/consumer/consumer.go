// Package consumer implements Kafka event-driven consumer for economy service
// Handles world.tick.hourly events and triggers market clearing operations
// Issue: #2237 - Kafka Event-Driven Architecture Implementation
// Agent: Backend Agent
package consumer

import (
	"context"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"
	"necpgame/services/economy-service-go/internal/simulation/bazaar"
)

// Service defines the interface for economy service operations
// Issue: #2237
type Service interface {
	// ClearMarkets triggers bazaar market clearing for all commodities
	// Returns market results and any errors encountered
	ClearMarkets(ctx context.Context, tickID string) ([]bazaar.MarketResult, error)

	// GetLogger returns the service logger
	GetLogger() *zap.Logger
}

// TickConsumer implements Kafka consumer for simulation tick events
// Processes world.tick.hourly events and coordinates market clearing
// Issue: #2237
type TickConsumer struct {
	reader     *kafka.Reader
	service    Service
	logger     *zap.Logger
	config     ConsumerConfig
	stopChan   chan struct{}
	wg         sync.WaitGroup
}

// NewTickConsumer creates a new tick event consumer
// Issue: #2237
func NewTickConsumer(service Service, config ConsumerConfig) *TickConsumer {
	// Create Kafka reader with enterprise configuration
	reader := kafka.NewReader(kafka.ReaderConfig{
		Brokers:                config.Brokers,
		GroupID:                config.GroupID,
		Topic:                  config.Topic,
		SessionTimeout:         config.SessionTimeout,
		HeartbeatInterval:      config.HeartbeatInterval,
		CommitInterval:         config.CommitInterval,
		MaxWait:                1 * time.Second,
		ReadBatchTimeout:       100 * time.Millisecond,
		StartOffset:            kafka.LastOffset, // Start from latest to avoid processing old events
		RetentionTime:          7 * 24 * time.Hour,
		ReadLagInterval:        -1, // Disable lag reporting for performance
		GroupBalancers:         []kafka.GroupBalancer{kafka.RoundRobinGroupBalancer{}},
		WatchPartitionChanges:  true,
		PartitionWatchInterval: 5 * time.Second,
	})

	logger := service.GetLogger().Named("tick-consumer")

	return &TickConsumer{
		reader:   reader,
		service:  service,
		logger:   logger,
		config:   config,
		stopChan: make(chan struct{}),
	}
}

// Start begins consuming tick events in a goroutine
// Runs until Stop() is called or context is cancelled
// Issue: #2237
func (tc *TickConsumer) Start(ctx context.Context) error {
	tc.logger.Info("Starting tick consumer",
		zap.Strings("brokers", tc.config.Brokers),
		zap.String("topic", tc.config.Topic),
		zap.String("group_id", tc.config.GroupID))

	tc.wg.Add(1)
	go tc.consumeLoop(ctx)

	tc.logger.Info("Tick consumer started successfully")
	return nil
}

// Stop gracefully shuts down the consumer
// Waits for current processing to complete
// Issue: #2237
func (tc *TickConsumer) Stop() error {
	tc.logger.Info("Stopping tick consumer")

	close(tc.stopChan)
	tc.wg.Wait()

	if err := tc.reader.Close(); err != nil {
		tc.logger.Error("Failed to close Kafka reader", zap.Error(err))
		return fmt.Errorf("failed to close Kafka reader: %w", err)
	}

	tc.logger.Info("Tick consumer stopped successfully")
	return nil
}

// consumeLoop runs the main consumption loop
// Processes messages until stopped or error occurs
// Issue: #2237
func (tc *TickConsumer) consumeLoop(ctx context.Context) {
	defer tc.wg.Done()

	tc.logger.Info("Tick consumer loop started")

	for {
		select {
		case <-ctx.Done():
			tc.logger.Info("Context cancelled, stopping consumer loop")
			return
		case <-tc.stopChan:
			tc.logger.Info("Stop signal received, stopping consumer loop")
			return
		default:
			tc.processNextMessage(ctx)
		}
	}
}

// processNextMessage reads and processes the next Kafka message
// Handles parsing, validation, and market clearing coordination
// Issue: #2237
func (tc *TickConsumer) processNextMessage(ctx context.Context) {
	// Set read deadline for controlled timeout
	readCtx, cancel := context.WithTimeout(ctx, tc.config.MaxProcessingTime)
	defer cancel()

	// Read message with timeout
	msg, err := tc.reader.ReadMessage(readCtx)
	if err != nil {
		if err != context.DeadlineExceeded && err != context.Canceled {
			tc.logger.Error("Failed to read Kafka message", zap.Error(err))
		}
		return
	}

	// Parse tick event
	tickEvent, err := tc.parseTickEvent(msg.Value)
	if err != nil {
		tc.logger.Error("Failed to parse tick event",
			zap.Error(err),
			zap.String("message_key", string(msg.Key)),
			zap.Int64("offset", msg.Offset))
		tc.commitMessage(ctx, msg) // Commit invalid messages to avoid reprocessing
		return
	}

	// Process tick event
	if err := tc.processTickEvent(ctx, tickEvent, msg); err != nil {
		tc.logger.Error("Failed to process tick event",
			zap.Error(err),
			zap.String("tick_id", tickEvent.Data.TickID),
			zap.String("event_id", tickEvent.EventID))
		// Don't commit failed messages - allow retry
		return
	}

	// Commit successful processing
	tc.commitMessage(ctx, msg)
}

// parseTickEvent deserializes JSON message into TickEvent struct
// Issue: #2237
func (tc *TickConsumer) parseTickEvent(data []byte) (*TickEvent, error) {
	var event TickEvent
	if err := json.Unmarshal(data, &event); err != nil {
		return nil, fmt.Errorf("failed to unmarshal tick event: %w", err)
	}

	// Validate required fields
	if event.EventID == "" {
		return nil, fmt.Errorf("missing required field: event_id")
	}
	if event.Data.TickID == "" {
		return nil, fmt.Errorf("missing required field: data.tick_id")
	}
	if event.Data.TickType != "hourly" {
		return nil, fmt.Errorf("unsupported tick type: %s (only 'hourly' supported)", event.Data.TickType)
	}

	return &event, nil
}

// processTickEvent handles the business logic for tick events
// Triggers market clearing when hourly tick is received
// Issue: #2237
func (tc *TickConsumer) processTickEvent(ctx context.Context, event *TickEvent, msg kafka.Message) error {
	startTime := time.Now()
	tickID := event.Data.TickID

	tc.logger.Info("Processing tick event",
		zap.String("tick_id", tickID),
		zap.String("tick_type", event.Data.TickType),
		zap.Int("game_hour", func() int { if event.Data.GameHour != nil { return *event.Data.GameHour } else { return -1 } }()),
		zap.String("event_id", event.EventID),
		zap.Int64("kafka_offset", msg.Offset))

	// Execute market clearing
	results, err := tc.service.ClearMarkets(ctx, tickID)
	if err != nil {
		return fmt.Errorf("market clearing failed: %w", err)
	}

	// Log results
	processingTime := time.Since(startTime)
	totalVolume := 0
	totalEfficiency := 0.0

	for i, result := range results {
		totalVolume += result.TotalVolume
		totalEfficiency += result.MarketEfficiency

		tc.logger.Info("Market clearing result",
			zap.Int("market_index", i),
			zap.Int("trades_cleared", len(result.ClearedTrades)),
			zap.Int("total_volume", result.TotalVolume),
			zap.Float64("market_efficiency", result.MarketEfficiency))
	}

	avgEfficiency := totalEfficiency / float64(len(results))
	if len(results) == 0 {
		avgEfficiency = 0.0
	}

	tc.logger.Info("Tick processing completed",
		zap.String("tick_id", tickID),
		zap.Int("markets_processed", len(results)),
		zap.Int("total_volume", totalVolume),
		zap.Float64("average_efficiency", avgEfficiency),
		zap.Duration("processing_time", processingTime),
		zap.Int64("kafka_offset", msg.Offset))

	return nil
}

// commitMessage commits the message offset to Kafka
// Ensures at-least-once delivery semantics
// Issue: #2237
func (tc *TickConsumer) commitMessage(ctx context.Context, msg kafka.Message) {
	if err := tc.reader.CommitMessages(ctx, msg); err != nil {
		tc.logger.Error("Failed to commit message",
			zap.Error(err),
			zap.Int64("offset", msg.Offset),
			zap.String("topic", msg.Topic))
	}
}

// HealthCheck returns consumer health status
// Used for monitoring and health checks
// Issue: #2237
func (tc *TickConsumer) HealthCheck() error {
	if tc.reader == nil {
		return fmt.Errorf("Kafka reader not initialized")
	}

	// Check if reader is still connected by attempting a stats call
	stats := tc.reader.Stats()
	if stats.DialTime <= 0 {
		return fmt.Errorf("Kafka reader not connected")
	}

	return nil
}