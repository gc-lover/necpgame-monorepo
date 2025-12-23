// Issue: #2237
// PERFORMANCE: Optimized consumer manager for high-throughput event processing
package consumers

import (
	"context"
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

// Manager manages all consumer groups and their lifecycle
type Manager struct {
	config         *config.Config
	eventRegistry  *events.Registry
	logger         *zap.Logger
	metrics        *metrics.Collector

	consumers      map[string]Consumer
	saramaConsumers map[string]sarama.ConsumerGroup
	handlers       map[string]*ConsumerGroupHandler

	mu       sync.RWMutex
	started  bool
	closed   bool
}

// NewManager creates a new consumer manager
func NewManager(cfg *config.Config, eventRegistry *events.Registry, logger *zap.Logger, metrics *metrics.Collector) *Manager {
	return &Manager{
		config:          cfg,
		eventRegistry:   eventRegistry,
		logger:          logger,
		metrics:         metrics,
		consumers:       make(map[string]Consumer),
		saramaConsumers: make(map[string]sarama.ConsumerGroup),
		handlers:        make(map[string]*ConsumerGroupHandler),
	}
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
