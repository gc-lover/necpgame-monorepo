// Issue: #2237
// PERFORMANCE: Optimized consumer manager for high-throughput event processing
package consumers

import (
	"context"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"github.com/IBM/sarama"
	"go.uber.org/zap"

	"kafka-event-driven-core/internal/config"
	"kafka-event-driven-core/internal/events"
	"kafka-event-driven-core/internal/metrics"
)

// Consumer defines the interface for domain-specific consumers
type Consumer interface {
	// ProcessEvent processes a single event
	ProcessEvent(ctx context.Context, event *events.BaseEvent) error

	// GetName returns the consumer name
	GetName() string

	// GetTopics returns the topics this consumer listens to
	GetTopics() []string

	// HealthCheck performs a health check
	HealthCheck() error

	// Close closes the consumer
	Close() error
}

// Manager manages all consumer groups and their lifecycle with enterprise features
type Manager struct {
	config         *config.Config
	eventRegistry  *events.Registry
	logger         *zap.Logger
	metrics        *metrics.Collector

	consumers      map[string]Consumer
	saramaConsumers map[string]sarama.ConsumerGroup
	handlers       map[string]*ConsumerGroupHandler

	// Enterprise features
	deadLetterQueue *DeadLetterQueue
	eventReplayer   *EventReplayer
	circuitBreaker  *CircuitBreaker
	workerPool      chan struct{}
	maxWorkers      int

	mu       sync.RWMutex
	started  bool
	closed   bool
}

// NewManager creates a new consumer manager with enterprise features
func NewManager(cfg *config.Config, eventRegistry *events.Registry, logger *zap.Logger, metrics *metrics.Collector) *Manager {
	maxWorkers := 100 // Configurable worker pool size
	workerPool := make(chan struct{}, maxWorkers)
	for i := 0; i < maxWorkers; i++ {
		workerPool <- struct{}{}
	}

	manager := &Manager{
		config:          cfg,
		eventRegistry:   eventRegistry,
		logger:          logger,
		metrics:         metrics,
		consumers:       make(map[string]Consumer),
		saramaConsumers: make(map[string]sarama.ConsumerGroup),
		handlers:        make(map[string]*ConsumerGroupHandler),
		maxWorkers:      maxWorkers,
		workerPool:      workerPool,
	}

	// Initialize enterprise features
	manager.initializeEnterpriseFeatures()

	return manager
}

// initializeEnterpriseFeatures sets up enterprise-grade features
func (m *Manager) initializeEnterpriseFeatures() {
	// Initialize dead letter queue
	dlq, err := NewDeadLetterQueue(m.config.Kafka.Brokers, "event-dlq", m.logger)
	if err != nil {
		m.logger.Error("Failed to initialize dead letter queue", zap.Error(err))
	} else {
		m.deadLetterQueue = dlq
		m.logger.Info("Initialized dead letter queue")
	}

	// Initialize event replayer
	replayer, err := NewEventReplayer(m.config.Kafka.Brokers, []string{"events"}, m.logger)
	if err != nil {
		m.logger.Error("Failed to initialize event replayer", zap.Error(err))
	} else {
		m.eventReplayer = replayer
		m.logger.Info("Initialized event replayer")
	}

	// Initialize circuit breaker
	m.circuitBreaker = NewCircuitBreaker(10, 30*time.Second)
	m.logger.Info("Initialized circuit breaker")
}

// RegisterConsumer registers a domain-specific consumer
func (m *Manager) RegisterConsumer(name string, consumer Consumer, topics []string) error {
	m.mu.Lock()
	defer m.mu.Unlock()

	if m.started {
		return fmt.Errorf("cannot register consumer after manager has started")
	}

	if _, exists := m.consumers[name]; exists {
		return fmt.Errorf("consumer %s already registered", name)
	}

	m.consumers[name] = consumer

	// Create Sarama consumer group
	saramaConfig := m.config.Kafka.SaramaConfig()
	saramaConfig.Consumer.Group.Rebalance.Strategy = sarama.BalanceStrategyRoundRobin
	saramaConfig.Consumer.Offsets.Initial = sarama.OffsetOldest

	group, err := sarama.NewConsumerGroup(m.config.Kafka.Brokers, name, saramaConfig)
	if err != nil {
		return fmt.Errorf("failed to create consumer group %s: %w", name, err)
	}

	m.saramaConsumers[name] = group

	// Create handler
	handler := NewConsumerGroupHandler(name, consumer, topics, m.eventRegistry, m.logger, m.metrics)
	m.handlers[name] = handler

	m.logger.Info("Registered consumer",
		zap.String("name", name),
		zap.Strings("topics", topics))

	return nil
}

// Start starts all consumer groups
func (m *Manager) Start(ctx context.Context) error {
	m.mu.Lock()
	defer m.mu.Unlock()

	if m.started {
		return fmt.Errorf("consumer manager already started")
	}

	m.started = true

	// Start each consumer group in a separate goroutine
	for name, consumer := range m.consumers {
		go m.runConsumerGroup(ctx, name, consumer)
	}

	m.logger.Info("Consumer manager started",
		zap.Int("consumers", len(m.consumers)))

	return nil
}

// runConsumerGroup runs a single consumer group
func (m *Manager) runConsumerGroup(ctx context.Context, name string, consumer Consumer) {
	defer func() {
		if r := recover(); r != nil {
			m.logger.Error("Consumer group panicked",
				zap.String("consumer", name),
				zap.Any("panic", r))
		}
	}()

	group := m.saramaConsumers[name]
	handler := m.handlers[name]
	topics := consumer.GetTopics()

	m.logger.Info("Starting consumer group",
		zap.String("consumer", name),
		zap.Strings("topics", topics))

	for {
		select {
		case <-ctx.Done():
			m.logger.Info("Stopping consumer group",
				zap.String("consumer", name))
			return
		default:
			// Consume messages
			err := group.Consume(ctx, topics, handler)
			if err != nil {
				m.logger.Error("Consumer group error",
					zap.String("consumer", name),
					zap.Error(err))
				time.Sleep(5 * time.Second) // Backoff before retry
			}

			// Check if context was cancelled
			if ctx.Err() != nil {
				return
			}
		}
	}
}

// Stop stops all consumer groups
func (m *Manager) Stop() error {
	m.mu.Lock()
	defer m.mu.Unlock()

	if m.closed {
		return nil
	}

	m.closed = true

	var errors []error

	// Close all consumer groups
	for name, group := range m.saramaConsumers {
		m.logger.Info("Closing consumer group", zap.String("consumer", name))
		if err := group.Close(); err != nil {
			m.logger.Error("Failed to close consumer group",
				zap.String("consumer", name),
				zap.Error(err))
			errors = append(errors, err)
		}
	}

	// Close all consumers
	for name, consumer := range m.consumers {
		if err := consumer.Close(); err != nil {
			m.logger.Error("Failed to close consumer",
				zap.String("consumer", name),
				zap.Error(err))
			errors = append(errors, err)
		}
	}

	if len(errors) > 0 {
		return fmt.Errorf("failed to close %d consumers", len(errors))
	}

	m.logger.Info("Consumer manager stopped")
	return nil
}

// GetConsumer returns a consumer by name
func (m *Manager) GetConsumer(name string) (Consumer, bool) {
	m.mu.RLock()
	defer m.mu.RUnlock()

	consumer, exists := m.consumers[name]
	return consumer, exists
}

// ListConsumers returns all registered consumer names
func (m *Manager) ListConsumers() []string {
	m.mu.RLock()
	defer m.mu.RUnlock()

	names := make([]string, 0, len(m.consumers))
	for name := range m.consumers {
		names = append(names, name)
	}

	return names
}

// HealthCheck performs health checks on all consumers
func (m *Manager) HealthCheck() error {
	m.mu.RLock()
	defer m.mu.RUnlock()

	for name, consumer := range m.consumers {
		if err := consumer.HealthCheck(); err != nil {
			return fmt.Errorf("consumer %s health check failed: %w", name, err)
		}
	}

	return nil
}

// IsStarted returns whether the manager has been started
func (m *Manager) IsStarted() bool {
	m.mu.RLock()
	defer m.mu.RUnlock()
	return m.started
}

// IsClosed returns whether the manager has been closed
func (m *Manager) IsClosed() bool {
	m.mu.RLock()
	defer m.mu.RUnlock()
	return m.closed
}

// EventRegistry returns the event registry
func (m *Manager) EventRegistry() *events.Registry {
	return m.eventRegistry
}

// Logger returns the logger
func (m *Manager) Logger() *zap.Logger {
	return m.logger
}

// Metrics returns the metrics collector
func (m *Manager) Metrics() *metrics.Collector {
	return m.metrics
}

// GetDeadLetterQueue returns the dead letter queue
func (m *Manager) GetDeadLetterQueue() *DeadLetterQueue {
	return m.deadLetterQueue
}

// GetEventReplayer returns the event replayer
func (m *Manager) GetEventReplayer() *EventReplayer {
	return m.eventReplayer
}

// GetCircuitBreaker returns the circuit breaker
func (m *Manager) GetCircuitBreaker() *CircuitBreaker {
	return m.circuitBreaker
}

// SendToDeadLetterQueue sends a failed event to DLQ
func (m *Manager) SendToDeadLetterQueue(event *events.BaseEvent, err error, consumerName string) {
	if m.deadLetterQueue != nil {
		if dlqErr := m.deadLetterQueue.SendToDLQ(event, err, consumerName); dlqErr != nil {
			m.logger.Error("Failed to send event to dead letter queue",
				zap.String("event_id", event.EventID.String()),
				zap.String("consumer", consumerName),
				zap.Error(dlqErr))
		}
	}
}

// ReplayEvents replays events from a time range
func (m *Manager) ReplayEvents(ctx context.Context, startTime, endTime time.Time, eventTypes []string, callback func(*events.BaseEvent) error) error {
	if m.eventReplayer == nil {
		return fmt.Errorf("event replayer not initialized")
	}

	return m.eventReplayer.ReplayEvents(ctx, startTime, endTime, eventTypes, callback)
}

// ProcessEventWithCircuitBreaker processes an event with circuit breaker protection
func (m *Manager) ProcessEventWithCircuitBreaker(consumerName string, event *events.BaseEvent, processFn func() error) error {
	if m.circuitBreaker == nil {
		return processFn()
	}

	return m.circuitBreaker.Call(func() error {
		err := processFn()
		if err != nil {
			m.SendToDeadLetterQueue(event, err, consumerName)
			return err
		}
		return nil
	})
}

// GetWorkerPoolStatus returns status of the worker pool
func (m *Manager) GetWorkerPoolStatus() (active int, max int) {
	return len(m.workerPool), m.maxWorkers
}

// AcquireWorker acquires a worker from the pool
func (m *Manager) AcquireWorker(ctx context.Context) error {
	select {
	case <-m.workerPool:
		return nil
	case <-ctx.Done():
		return ctx.Err()
	case <-time.After(5 * time.Second):
		return fmt.Errorf("no workers available")
	}
}

// ReleaseWorker releases a worker back to the pool
func (m *Manager) ReleaseWorker() {
	select {
	case m.workerPool <- struct{}{}:
	default:
		m.logger.Warn("Failed to release worker - pool may be full")
	}
}

// HealthCheckWithContext performs comprehensive health check with context
func (m *Manager) HealthCheckWithContext(ctx context.Context) error {
	m.mu.RLock()
	defer m.mu.RUnlock()

	if m.closed {
		return fmt.Errorf("manager is closed")
	}

	// Check all consumers
	for name, consumer := range m.consumers {
		if err := consumer.HealthCheck(); err != nil {
			return fmt.Errorf("consumer %s health check failed: %w", name, err)
		}
	}

	// Check circuit breaker
	if m.circuitBreaker != nil && m.circuitBreaker.GetState() == "open" {
		return fmt.Errorf("circuit breaker is open")
	}

	// Check worker pool
	if len(m.workerPool) == 0 {
		return fmt.Errorf("no workers available")
	}

	return nil
}

// ENTERPRISE FEATURES BELOW

// DeadLetterQueue handles failed event processing
type DeadLetterQueue struct {
	topic    string
	producer sarama.SyncProducer
	logger   *zap.Logger
	mu       sync.Mutex
}

// NewDeadLetterQueue creates a new dead letter queue
func NewDeadLetterQueue(brokers []string, topic string, logger *zap.Logger) (*DeadLetterQueue, error) {
	config := sarama.NewConfig()
	config.Producer.RequiredAcks = sarama.WaitForAll
	config.Producer.Retry.Max = 3

	producer, err := sarama.NewSyncProducer(brokers, config)
	if err != nil {
		return nil, fmt.Errorf("failed to create DLQ producer: %w", err)
	}

	return &DeadLetterQueue{
		topic:    topic,
		producer: producer,
		logger:   logger,
	}, nil
}

// SendToDLQ sends a failed event to the dead letter queue
func (dlq *DeadLetterQueue) SendToDLQ(event *events.BaseEvent, error error, consumerName string) error {
	dlq.mu.Lock()
	defer dlq.mu.Unlock()

	dlqEvent := struct {
		OriginalEvent *events.BaseEvent `json:"original_event"`
		Error         string           `json:"error"`
		ConsumerName  string           `json:"consumer_name"`
		FailedAt      string           `json:"failed_at"`
		RetryCount    int              `json:"retry_count"`
	}{
		OriginalEvent: event,
		Error:         error.Error(),
		ConsumerName:  consumerName,
		FailedAt:      time.Now().Format(time.RFC3339),
		RetryCount:    event.Metadata.RetryCount,
	}

	data, err := json.Marshal(dlqEvent)
	if err != nil {
		return fmt.Errorf("failed to marshal DLQ event: %w", err)
	}

	message := &sarama.ProducerMessage{
		Topic: dlq.topic,
		Key:   sarama.StringEncoder(event.EventID.String()),
		Value: sarama.ByteEncoder(data),
	}

	_, _, err = dlq.producer.SendMessage(message)
	if err != nil {
		dlq.logger.Error("Failed to send event to DLQ",
			zap.String("event_id", event.EventID.String()),
			zap.Error(err))
		return err
	}

	dlq.logger.Info("Event sent to dead letter queue",
		zap.String("event_id", event.EventID.String()),
		zap.String("consumer", consumerName))

	return nil
}

// Close closes the dead letter queue
func (dlq *DeadLetterQueue) Close() error {
	return dlq.producer.Close()
}

// EventReplayer handles event replay functionality
type EventReplayer struct {
	consumer sarama.Consumer
	topics   []string
	logger   *zap.Logger
	mu       sync.Mutex
}

// NewEventReplayer creates a new event replayer
func NewEventReplayer(brokers []string, topics []string, logger *zap.Logger) (*EventReplayer, error) {
	config := sarama.NewConfig()
	config.Consumer.Offsets.Initial = sarama.OffsetOldest

	consumer, err := sarama.NewConsumer(brokers, config)
	if err != nil {
		return nil, fmt.Errorf("failed to create replayer consumer: %w", err)
	}

	return &EventReplayer{
		consumer: consumer,
		topics:   topics,
		logger:   logger,
	}, nil
}

// ReplayEvents replays events from specified time range
func (er *EventReplayer) ReplayEvents(ctx context.Context, startTime, endTime time.Time, eventTypes []string, callback func(*events.BaseEvent) error) error {
	er.mu.Lock()
	defer er.mu.Unlock()

	for _, topic := range er.topics {
		partitions, err := er.consumer.Partitions(topic)
		if err != nil {
			er.logger.Error("Failed to get partitions for topic",
				zap.String("topic", topic), zap.Error(err))
			continue
		}

		for _, partition := range partitions {
			pc, err := er.consumer.ConsumePartition(topic, partition, sarama.OffsetOldest)
			if err != nil {
				er.logger.Error("Failed to consume partition",
					zap.String("topic", topic), zap.Int32("partition", partition), zap.Error(err))
				continue
			}

			go func(pc sarama.PartitionConsumer) {
				defer pc.Close()

				for {
					select {
					case <-ctx.Done():
						return
					case msg := <-pc.Messages():
						eventTime, err := time.Parse(time.RFC3339, string(msg.Key))
						if err != nil {
							continue
						}

						if eventTime.Before(startTime) || eventTime.After(endTime) {
							continue
						}

						var baseEvent events.BaseEvent
						if err := json.Unmarshal(msg.Value, &baseEvent); err != nil {
							continue
						}

						// Filter by event types if specified
						if len(eventTypes) > 0 {
							found := false
							for _, et := range eventTypes {
								if baseEvent.EventType == et {
									found = true
									break
								}
							}
							if !found {
								continue
							}
						}

						if err := callback(&baseEvent); err != nil {
							er.logger.Error("Error in replay callback",
								zap.String("event_id", baseEvent.EventID.String()), zap.Error(err))
						}
					}
				}
			}(pc)
		}
	}

	return nil
}

// Close closes the event replayer
func (er *EventReplayer) Close() error {
	return er.consumer.Close()
}

// CircuitBreaker implements circuit breaker pattern for consumer processing
type CircuitBreaker struct {
	failures     int
	lastFailTime time.Time
	state        string // "closed", "open", "half-open"
	maxFailures  int
	timeout      time.Duration
	mu           sync.Mutex
}

// NewCircuitBreaker creates a new circuit breaker
func NewCircuitBreaker(maxFailures int, timeout time.Duration) *CircuitBreaker {
	return &CircuitBreaker{
		state:       "closed",
		maxFailures: maxFailures,
		timeout:     timeout,
	}
}

// Call executes a function with circuit breaker protection
func (cb *CircuitBreaker) Call(fn func() error) error {
	cb.mu.Lock()
	defer cb.mu.Unlock()

	if cb.state == "open" {
		if time.Since(cb.lastFailTime) > cb.timeout {
			cb.state = "half-open"
		} else {
			return fmt.Errorf("circuit breaker is open")
		}
	}

	err := fn()
	if err != nil {
		cb.failures++
		cb.lastFailTime = time.Now()
		if cb.failures >= cb.maxFailures {
			cb.state = "open"
		}
		return err
	}

	if cb.state == "half-open" {
		cb.state = "closed"
		cb.failures = 0
	}

	return nil
}

// GetState returns the current circuit breaker state
func (cb *CircuitBreaker) GetState() string {
	cb.mu.Lock()
	defer cb.mu.Unlock()
	return cb.state
}

// Reset resets the circuit breaker to closed state
func (cb *CircuitBreaker) Reset() {
	cb.mu.Lock()
	defer cb.mu.Unlock()
	cb.s