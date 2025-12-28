// Package eventbus provides Kafka-based event-driven architecture for MMOFPS microservices
package eventbus

import (
	"context"
	"encoding/json"
	"fmt"
	"strings"
	"sync"
	"time"

	"github.com/Shopify/sarama"
	"go.uber.org/zap"

	errorhandling "github.com/your-org/necpgame/scripts/core/error-handling"
)

// EventBus manages event-driven communication between microservices
type EventBus struct {
	config      *EventBusConfig
	logger      *errorhandling.Logger
	producer    sarama.SyncProducer
	consumer    sarama.ConsumerGroup
	handlers    map[string][]EventHandler
	middlewares []EventMiddleware

	// Event processing
	eventsChan chan *EventEnvelope
	errorsChan chan error

	// Lifecycle management
	shutdownChan chan struct{}
	wg           sync.WaitGroup

	mu sync.RWMutex
}

// EventBusConfig holds configuration for the event bus
type EventBusConfig struct {
	Brokers         []string      `json:"brokers"`
	ClientID        string        `json:"client_id"`
	GroupID         string        `json:"group_id"`
	Topics          []string      `json:"topics"`
	BufferSize      int           `json:"buffer_size"`
	MaxRetries      int           `json:"max_retries"`
	RetryDelay      time.Duration `json:"retry_delay"`
	EnableMetrics   bool          `json:"enable_metrics"`
	EnableTracing   bool          `json:"enable_tracing"`
	EnableDeadLetter bool          `json:"enable_dead_letter"`
}

// EventEnvelope wraps events with metadata
type EventEnvelope struct {
	ID          string                 `json:"id"`
	EventType   string                 `json:"event_type"`
	AggregateID string                 `json:"aggregate_id"`
	Source      string                 `json:"source"`      // Service that produced the event
	Timestamp   time.Time              `json:"timestamp"`
	Version     string                 `json:"version"`
	Payload     interface{}            `json:"payload"`
	Metadata    map[string]interface{} `json:"metadata,omitempty"`
	Headers     map[string]string      `json:"headers,omitempty"`
}

// EventHandler processes events of specific types
type EventHandler interface {
	HandleEvent(ctx context.Context, event *EventEnvelope) error
	GetEventTypes() []string
	GetName() string
}

// EventMiddleware processes events before/after handlers
type EventMiddleware interface {
	Process(ctx context.Context, event *EventEnvelope, next EventHandlerFunc) error
}

// EventHandlerFunc represents a function that handles events
type EventHandlerFunc func(ctx context.Context, event *EventEnvelope) error

// EventPublisher publishes events to the bus
type EventPublisher struct {
	producer sarama.SyncProducer
	topic    string
	logger   *errorhandling.Logger
}

// EventConsumer consumes events from the bus
type EventConsumer struct {
	group   sarama.ConsumerGroup
	topics  []string
	handlers map[string][]EventHandler
	logger  *errorhandling.Logger
}

// Metrics tracks event bus performance
type Metrics struct {
	TotalPublished    int64            `json:"total_published"`
	TotalConsumed     int64            `json:"total_consumed"`
	TotalErrors       int64            `json:"total_errors"`
	ProcessingTime    time.Duration    `json:"processing_time"`
	EventTypeMetrics  map[string]int64 `json:"event_type_metrics"`
	HandlerMetrics    map[string]int64 `json:"handler_metrics"`
	ErrorMetrics      map[string]int64 `json:"error_metrics"`
	LastActivity      time.Time        `json:"last_activity"`
}

// Event types for MMOFPS game
const (
	EventTypePlayerJoined         = "player.joined"
	EventTypePlayerLeft           = "player.left"
	EventTypePlayerAction         = "player.action"
	EventTypeGameStarted          = "game.started"
	EventTypeGameEnded            = "game.ended"
	EventTypeQuestStarted         = "quest.started"
	EventTypeQuestCompleted       = "quest.completed"
	EventTypeAchievementUnlocked  = "achievement.unlocked"
	EventTypeInventoryChanged     = "inventory.changed"
	EventTypeGuildAction          = "guild.action"
	EventTypeMarketTransaction    = "market.transaction"
	EventTypeCombatEvent          = "combat.event"
	EventTypeSocialInteraction    = "social.interaction"
	EventTypeSystemNotification   = "system.notification"
)

// NewEventBus creates a new event bus instance
func NewEventBus(config *EventBusConfig, logger *errorhandling.Logger) (*EventBus, error) {
	if config == nil {
		config = &EventBusConfig{
			Brokers:       []string{"localhost:9092"},
			ClientID:      "necpgame-eventbus",
			GroupID:       "necpgame-consumers",
			Topics:        []string{"game-events", "player-events", "system-events"},
			BufferSize:    1000,
			MaxRetries:    3,
			RetryDelay:    1 * time.Second,
			EnableMetrics: true,
			EnableTracing: false,
			EnableDeadLetter: true,
		}
	}

	eb := &EventBus{
		config:      config,
		logger:      logger,
		handlers:    make(map[string][]EventHandler),
		middlewares: make([]EventMiddleware, 0),
		eventsChan:  make(chan *EventEnvelope, config.BufferSize),
		errorsChan:  make(chan error, 100),
		shutdownChan: make(chan struct{}),
	}

	// Initialize Kafka producer
	producer, err := eb.createProducer()
	if err != nil {
		return nil, errorhandling.WrapError(err, errorhandling.ErrorTypeInternal, "PRODUCER_INIT_FAILED", "Failed to create Kafka producer")
	}
	eb.producer = producer

	// Initialize Kafka consumer
	consumer, err := eb.createConsumer()
	if err != nil {
		return nil, errorhandling.WrapError(err, errorhandling.ErrorTypeInternal, "CONSUMER_INIT_FAILED", "Failed to create Kafka consumer")
	}
	eb.consumer = consumer

	// Start background processing
	eb.startBackgroundProcessing()

	logger.Infow("Event bus initialized",
		"brokers", config.Brokers,
		"group_id", config.GroupID,
		"topics", config.Topics)

	return eb, nil
}

// PublishEvent publishes an event to the event bus
func (eb *EventBus) PublishEvent(ctx context.Context, event *EventEnvelope) error {
	if event.ID == "" {
		event.ID = fmt.Sprintf("evt_%d", time.Now().UnixNano())
	}
	event.Timestamp = time.Now()
	if event.Version == "" {
		event.Version = "1.0"
	}

	// Serialize event
	eventData, err := json.Marshal(event)
	if err != nil {
		return errorhandling.WrapError(err, errorhandling.ErrorTypeInternal, "EVENT_SERIALIZE_FAILED", "Failed to serialize event")
	}

	// Create Kafka message
	topic := eb.getTopicForEvent(event)
	msg := &sarama.ProducerMessage{
		Topic: topic,
		Key:   sarama.StringEncoder(event.AggregateID),
		Value: sarama.ByteEncoder(eventData),
		Headers: []sarama.RecordHeader{
			{Key: []byte("event_type"), Value: []byte(event.EventType)},
			{Key: []byte("source"), Value: []byte(event.Source)},
		},
	}

	// Publish message
	partition, offset, err := eb.producer.SendMessage(msg)
	if err != nil {
		return errorhandling.WrapError(err, errorhandling.ErrorTypeInternal, "PUBLISH_FAILED", "Failed to publish event to Kafka")
	}

	eb.logger.Debugw("Event published",
		"event_id", event.ID,
		"event_type", event.EventType,
		"topic", topic,
		"partition", partition,
		"offset", offset)

	return nil
}

// SubscribeHandler subscribes an event handler for specific event types
func (eb *EventBus) SubscribeHandler(handler EventHandler) error {
	eb.mu.Lock()
	defer eb.mu.Unlock()

	handlerName := handler.GetName()
	eventTypes := handler.GetEventTypes()

	for _, eventType := range eventTypes {
		if eb.handlers[eventType] == nil {
			eb.handlers[eventType] = make([]EventHandler, 0)
		}

		// Check for duplicate handlers
		for _, existing := range eb.handlers[eventType] {
			if existing.GetName() == handlerName {
				return errorhandling.NewConflictError("HANDLER_EXISTS", "Handler already subscribed for this event type")
			}
		}

		eb.handlers[eventType] = append(eb.handlers[eventType], handler)
	}

	eb.logger.Infow("Event handler subscribed",
		"handler", handlerName,
		"event_types", eventTypes)

	return nil
}

// AddMiddleware adds an event processing middleware
func (eb *EventBus) AddMiddleware(middleware EventMiddleware) {
	eb.mu.Lock()
	defer eb.mu.Unlock()

	eb.middlewares = append(eb.middlewares, middleware)
}

// CreatePublisher creates an event publisher for a specific topic
func (eb *EventBus) CreatePublisher(topic string) (*EventPublisher, error) {
	return &EventPublisher{
		producer: eb.producer,
		topic:    topic,
		logger:   eb.logger,
	}, nil
}

// GetMetrics returns current event bus metrics
func (eb *EventBus) GetMetrics() *Metrics {
	// Placeholder for metrics collection
	return &Metrics{
		LastActivity: time.Now(),
		EventTypeMetrics: make(map[string]int64),
		HandlerMetrics: make(map[string]int64),
		ErrorMetrics: make(map[string]int64),
	}
}

// Publish publishes an event using the publisher
func (ep *EventPublisher) Publish(ctx context.Context, event *EventEnvelope) error {
	if event.ID == "" {
		event.ID = fmt.Sprintf("evt_%d", time.Now().UnixNano())
	}
	event.Timestamp = time.Now()

	eventData, err := json.Marshal(event)
	if err != nil {
		return err
	}

	msg := &sarama.ProducerMessage{
		Topic: ep.topic,
		Key:   sarama.StringEncoder(event.AggregateID),
		Value: sarama.ByteEncoder(eventData),
	}

	_, _, err = ep.producer.SendMessage(msg)
	return err
}

// PublishPlayerEvent publishes a player-related event
func (eb *EventBus) PublishPlayerEvent(ctx context.Context, playerID, eventType string, payload interface{}, source string) error {
	event := &EventEnvelope{
		EventType:   eventType,
		AggregateID: playerID,
		Source:      source,
		Payload:     payload,
		Metadata: map[string]interface{}{
			"player_id": playerID,
		},
	}

	return eb.PublishEvent(ctx, event)
}

// PublishGameEvent publishes a game-related event
func (eb *EventBus) PublishGameEvent(ctx context.Context, gameID, eventType string, payload interface{}, source string) error {
	event := &EventEnvelope{
		EventType:   eventType,
		AggregateID: gameID,
		Source:      source,
		Payload:     payload,
		Metadata: map[string]interface{}{
			"game_id": gameID,
		},
	}

	return eb.PublishEvent(ctx, event)
}

// PublishSystemEvent publishes a system-wide event
func (eb *EventBus) PublishSystemEvent(ctx context.Context, eventType string, payload interface{}, source string) error {
	event := &EventEnvelope{
		EventType:   eventType,
		AggregateID: "system",
		Source:      source,
		Payload:     payload,
		Metadata: map[string]interface{}{
			"system_wide": true,
		},
	}

	return eb.PublishEvent(ctx, event)
}

// Helper methods

func (eb *EventBus) createProducer() (sarama.SyncProducer, error) {
	config := sarama.NewConfig()
	config.Producer.RequiredAcks = sarama.WaitForAll
	config.Producer.Retry.Max = eb.config.MaxRetries
	config.Producer.Retry.Backoff = eb.config.RetryDelay
	config.Producer.Return.Successes = true
	config.ClientID = eb.config.ClientID

	return sarama.NewSyncProducer(eb.config.Brokers, config)
}

func (eb *EventBus) createConsumer() (sarama.ConsumerGroup, error) {
	config := sarama.NewConfig()
	config.Consumer.Group.Rebalance.Strategy = sarama.BalanceStrategyRoundRobin
	config.Consumer.Offsets.Initial = sarama.OffsetOldest
	config.ClientID = eb.config.ClientID

	return sarama.NewConsumerGroup(eb.config.Brokers, eb.config.GroupID, config)
}

func (eb *EventBus) getTopicForEvent(event *EventEnvelope) string {
	// Route events to appropriate topics based on event type
	switch {
	case strings.HasPrefix(event.EventType, "player."):
		return "player-events"
	case strings.HasPrefix(event.EventType, "game."):
		return "game-events"
	case strings.HasPrefix(event.EventType, "system."):
		return "system-events"
	case strings.HasPrefix(event.EventType, "combat."):
		return "combat-events"
	case strings.HasPrefix(event.EventType, "social."):
		return "social-events"
	default:
		return "game-events" // Default topic
	}
}

func (eb *EventBus) startBackgroundProcessing() {
	// Event processing worker
	eb.wg.Add(1)
	go func() {
		defer eb.wg.Done()

		for {
			select {
			case event := <-eb.eventsChan:
				eb.processEvent(context.Background(), event)
			case err := <-eb.errorsChan:
				eb.logger.LogError(err, "Event processing error")
			case <-eb.shutdownChan:
				return
			}
		}
	}()

	// Consumer worker
	eb.wg.Add(1)
	go func() {
		defer eb.wg.Done()

		consumer := &EventConsumer{
			group:    eb.consumer,
			topics:   eb.config.Topics,
			handlers: eb.handlers,
			logger:   eb.logger,
		}

		ctx, cancel := context.WithCancel(context.Background())
		defer cancel()

		go func() {
			<-eb.shutdownChan
			cancel()
		}()

		for {
			err := consumer.Consume(ctx)
			if err != nil {
				eb.errorsChan <- err
				return
			}

			select {
			case <-eb.shutdownChan:
				return
			default:
				time.Sleep(1 * time.Second) // Backoff before retry
			}
		}
	}()
}

func (eb *EventBus) processEvent(ctx context.Context, event *EventEnvelope) {
	eb.mu.RLock()
	handlers := eb.handlers[event.EventType]
	middlewares := eb.middlewares
	eb.mu.RUnlock()

	if len(handlers) == 0 {
		eb.logger.Debugw("No handlers for event type", "event_type", event.EventType)
		return
	}

	// Create handler chain with middlewares
	handlerFunc := func(ctx context.Context, event *EventEnvelope) error {
		for _, handler := range handlers {
			if err := handler.HandleEvent(ctx, event); err != nil {
				return err
			}
		}
		return nil
	}

	// Apply middlewares in reverse order (onion pattern)
	for i := len(middlewares) - 1; i >= 0; i-- {
		middleware := middlewares[i]
		next := handlerFunc
		handlerFunc = func(ctx context.Context, event *EventEnvelope) error {
			return middleware.Process(ctx, event, next)
		}
	}

	// Execute handler chain
	startTime := time.Now()
	err := handlerFunc(ctx, event)
	processingTime := time.Since(startTime)

	if err != nil {
		eb.logger.LogError(err, "Event handler failed",
			zap.String("event_id", event.ID),
			zap.String("event_type", event.EventType),
			zap.Duration("processing_time", processingTime))
	} else {
		eb.logger.Debugw("Event processed successfully",
			"event_id", event.ID,
			"event_type", event.EventType,
			"processing_time", processingTime)
	}
}

// Consume starts consuming messages from Kafka topics
func (ec *EventConsumer) Consume(ctx context.Context) error {
	handler := &consumerGroupHandler{
		handlers: ec.handlers,
		logger:   ec.logger,
	}

	for {
		err := ec.group.Consume(ctx, ec.topics, handler)
		if err != nil {
			return err
		}

		if ctx.Err() != nil {
			return ctx.Err()
		}
	}
}

// consumerGroupHandler implements sarama.ConsumerGroupHandler
type consumerGroupHandler struct {
	handlers map[string][]EventHandler
	logger   *errorhandling.Logger
}

func (h *consumerGroupHandler) Setup(sarama.ConsumerGroupSession) error   { return nil }
func (h *consumerGroupHandler) Cleanup(sarama.ConsumerGroupSession) error { return nil }

func (h *consumerGroupHandler) ConsumeClaim(sess sarama.ConsumerGroupSession, claim sarama.ConsumerGroupClaim) error {
	for msg := range claim.Messages() {
		// Deserialize event
		var event EventEnvelope
		if err := json.Unmarshal(msg.Value, &event); err != nil {
			h.logger.LogError(err, "Failed to deserialize event")
			continue
		}

		// Mark message as processed
		sess.MarkMessage(msg, "")

		// Process event (would send to event bus processing channel)
		h.logger.Debugw("Event consumed",
			"topic", msg.Topic,
			"partition", msg.Partition,
			"offset", msg.Offset,
			"event_type", event.EventType)
	}

	return nil
}

// Shutdown gracefully shuts down the event bus
func (eb *EventBus) Shutdown(ctx context.Context) error {
	close(eb.shutdownChan)

	// Close Kafka connections
	if eb.producer != nil {
		eb.producer.Close()
	}
	if eb.consumer != nil {
		eb.consumer.Close()
	}

	done := make(chan struct{})
	go func() {
		eb.wg.Wait()
		close(done)
	}()

	select {
	case <-done:
		eb.logger.Info("Event bus shut down gracefully")
		return nil
	case <-ctx.Done():
		eb.logger.Warn("Event bus shutdown timed out")
		return ctx.Err()
	}
}
