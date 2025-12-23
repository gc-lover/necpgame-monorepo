// Issue: #2217
// PERFORMANCE: Optimized saga coordinator for distributed transactions
package sagas

import (
	"context"
	"fmt"
	"sync"
	"time"

	"go.uber.org/zap"

	"event-sourcing-aggregates-go/internal/store"
)

// Saga defines the interface for distributed transaction sagas
type Saga interface {
	// Name returns the saga name
	Name() string

	// Start starts the saga
	Start(ctx context.Context, correlationID string, data map[string]interface{}) error

	// HandleEvent handles an event in the saga
	HandleEvent(ctx context.Context, event store.EventEnvelope) error

	// GetState returns the current saga state
	GetState() SagaState

	// Compensate performs compensation for failed saga
	Compensate(ctx context.Context) error
}

// SagaState represents the state of a saga
type SagaState struct {
	ID            string                 `json:"id"`
	Name          string                 `json:"name"`
	State         string                 `json:"state"`
	CorrelationID string                 `json:"correlation_id"`
	Data          map[string]interface{} `json:"data"`
	StartedAt     time.Time              `json:"started_at"`
	UpdatedAt     time.Time              `json:"updated_at"`
	CompletedAt   *time.Time             `json:"completed_at,omitempty"`
}

// Coordinator manages saga lifecycle and coordination
type Coordinator struct {
	sagas      map[string]Saga
	eventStore *store.PostgreSQLEventStore
	logger     *zap.Logger
	mu         sync.RWMutex
	running    bool
	stopChan   chan struct{}
}

// NewCoordinator creates a new saga coordinator
func NewCoordinator(eventStore *store.PostgreSQLEventStore, logger *zap.Logger) *Coordinator {
	return &Coordinator{
		sagas:      make(map[string]Saga),
		eventStore: eventStore,
		logger:     logger,
		stopChan:   make(chan struct{}),
	}
}

// RegisterSaga registers a saga
func (c *Coordinator) RegisterSaga(name string, saga Saga) error {
	c.mu.Lock()
	defer c.mu.Unlock()

	if c.running {
		return fmt.Errorf("cannot register saga while coordinator is running")
	}

	if _, exists := c.sagas[name]; exists {
		return fmt.Errorf("saga %s already registered", name)
	}

	c.sagas[name] = saga
	c.logger.Info("Saga registered",
		zap.String("saga_name", name),
		zap.String("saga_type", fmt.Sprintf("%T", saga)))

	return nil
}

// Start starts the saga coordinator
func (c *Coordinator) Start(ctx context.Context) error {
	c.mu.Lock()
	defer c.mu.Unlock()

	if c.running {
		return fmt.Errorf("saga coordinator already running")
	}

	c.running = true

	// Start saga monitoring
	go c.monitorSagas(ctx)

	c.logger.Info("Saga coordinator started",
		zap.Int("sagas_count", len(c.sagas)))

	return nil
}

// Stop stops the saga coordinator
func (c *Coordinator) Stop() error {
	c.mu.Lock()
	defer c.mu.Unlock()

	if !c.running {
		return nil
	}

	c.running = false
	close(c.stopChan)

	c.logger.Info("Saga coordinator stopped")
	return nil
}

// StartSaga starts a new saga instance
func (c *Coordinator) StartSaga(ctx context.Context, sagaName, correlationID string, data map[string]interface{}) error {
	c.mu.RLock()
	saga, exists := c.sagas[sagaName]
	c.mu.RUnlock()

	if !exists {
		return fmt.Errorf("saga %s not found", sagaName)
	}

	c.logger.Info("Starting saga",
		zap.String("saga_name", sagaName),
		zap.String("correlation_id", correlationID))

	if err := saga.Start(ctx, correlationID, data); err != nil {
		c.logger.Error("Failed to start saga",
			zap.String("saga_name", sagaName),
			zap.String("correlation_id", correlationID),
			zap.Error(err))
		return err
	}

	return nil
}

// PublishEventToSagas publishes an event to all sagas
func (c *Coordinator) PublishEventToSagas(ctx context.Context, event store.EventEnvelope) error {
	c.mu.RLock()
	defer c.mu.RUnlock()

	var errors []error

	for name, saga := range c.sagas {
		if err := saga.HandleEvent(ctx, event); err != nil {
			c.logger.Error("Saga failed to handle event",
				zap.String("saga_name", name),
				zap.String("event_type", event.EventType),
				zap.Error(err))
			errors = append(errors, err)
		}
	}

	if len(errors) > 0 {
		return fmt.Errorf("saga event handling failed with %d errors", len(errors))
	}

	return nil
}

// GetSaga returns a saga by name
func (c *Coordinator) GetSaga(name string) (Saga, bool) {
	c.mu.RLock()
	defer c.mu.RUnlock()

	saga, exists := c.sagas[name]
	return saga, exists
}

// ListSagas returns all registered saga names
func (c *Coordinator) ListSagas() []string {
	c.mu.RLock()
	defer c.mu.RUnlock()

	names := make([]string, 0, len(c.sagas))
	for name := range c.sagas {
		names = append(names, name)
	}

	return names
}

// monitorSagas monitors saga states and handles timeouts
func (c *Coordinator) monitorSagas(ctx context.Context) {
	ticker := time.NewTicker(30 * time.Second) // Check interval
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-c.stopChan:
			return
		case <-ticker.C:
			c.checkSagaTimeouts(ctx)
		}
	}
}

// checkSagaTimeouts checks for timed out sagas and triggers compensation
func (c *Coordinator) checkSagaTimeouts(ctx context.Context) {
	c.mu.RLock()
	defer c.mu.RUnlock()

	for name, saga := range c.sagas {
		state := saga.GetState()

		// Check if saga has been running too long (example: 10 minutes timeout)
		if state.State == "running" && time.Since(state.StartedAt) > 10*time.Minute {
			c.logger.Warn("Saga timeout detected, triggering compensation",
				zap.String("saga_name", name),
				zap.String("saga_id", state.ID),
				zap.Duration("duration", time.Since(state.StartedAt)))

			// Trigger compensation in a goroutine
			go func(s Saga, sagaName string) {
				ctx, cancel := context.WithTimeout(context.Background(), 5*time.Minute)
				defer cancel()

				if err := s.Compensate(ctx); err != nil {
					c.logger.Error("Saga compensation failed",
						zap.String("saga_name", sagaName),
						zap.Error(err))
				}
			}(saga, name)
		}
	}
}

// GetSagaStats returns statistics for all sagas
func (c *Coordinator) GetSagaStats() map[string]SagaStats {
	c.mu.RLock()
	defer c.mu.RUnlock()

	stats := make(map[string]SagaStats)
	for name, saga := range c.sagas {
		state := saga.GetState()
		stats[name] = SagaStats{
			Name:       name,
			State:      state.State,
			ActiveTime: time.Since(state.StartedAt),
			// Add more stats as needed
		}
	}

	return stats
}

// SagaStats represents saga statistics
type SagaStats struct {
	Name       string        `json:"name"`
	State      string        `json:"state"`
	ActiveTime time.Duration `json:"active_time"`
	// Add more fields as needed
}
