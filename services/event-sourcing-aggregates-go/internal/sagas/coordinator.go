// Saga Coordinator for distributed transactions
// Issue: #2217
// Agent: Backend Agent
package sagas

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/event-sourcing-aggregates-go/internal/events"
)

// SagaStep represents a step in a saga
type SagaStep struct {
	ID          string                 `json:"id"`
	Name        string                 `json:"name"`
	Action      string                 `json:"action"`
	Compensation string                `json:"compensation"`
	Status      SagaStepStatus        `json:"status"`
	Data        map[string]interface{} `json:"data,omitempty"`
	Error       string                 `json:"error,omitempty"`
	ExecutedAt  *time.Time            `json:"executed_at,omitempty"`
}

// SagaStepStatus represents saga step execution status
type SagaStepStatus string

const (
	SagaStepPending    SagaStepStatus = "pending"
	SagaStepExecuting  SagaStepStatus = "executing"
	SagaStepCompleted  SagaStepStatus = "completed"
	SagaStepFailed     SagaStepStatus = "failed"
	SagaStepCompensated SagaStepStatus = "compensated"
)

// Saga represents a distributed transaction
type Saga struct {
	ID          uuid.UUID              `json:"id"`
	Name        string                 `json:"name"`
	Status      SagaStatus            `json:"status"`
	Steps       []*SagaStep           `json:"steps"`
	CreatedAt   time.Time             `json:"created_at"`
	UpdatedAt   time.Time             `json:"updated_at"`
	CompletedAt *time.Time            `json:"completed_at,omitempty"`
	Data        map[string]interface{} `json:"data,omitempty"`
}

// SagaStatus represents saga execution status
type SagaStatus string

const (
	SagaPending     SagaStatus = "pending"
	SagaExecuting   SagaStatus = "executing"
	SagaCompleted   SagaStatus = "completed"
	SagaFailed      SagaStatus = "failed"
	SagaCompensating SagaStatus = "compensating"
	SagaCompensated SagaStatus = "compensated"
)

// SagaDefinition defines a saga template
type SagaDefinition struct {
	Name  string       `json:"name"`
	Steps []*SagaStepDef `json:"steps"`
}

// SagaStepDef defines a saga step
type SagaStepDef struct {
	Name         string `json:"name"`
	Action       string `json:"action"`
	Compensation string `json:"compensation"`
}

// SagaHandler defines interface for saga handlers
type SagaHandler interface {
	ExecuteStep(ctx context.Context, saga *Saga, step *SagaStep) error
	CompensateStep(ctx context.Context, saga *Saga, step *SagaStep) error
}

// Coordinator manages saga execution
type Coordinator struct {
	sagas     map[uuid.UUID]*Saga
	handlers  map[string]SagaHandler
	definitions map[string]*SagaDefinition
	eventBus  *events.EventBus
	logger    *zap.Logger
	mu        sync.RWMutex
	running   bool
}

// NewCoordinator creates a new saga coordinator
func NewCoordinator(eventBus *events.EventBus, logger *zap.Logger) *Coordinator {
	return &Coordinator{
		sagas:       make(map[uuid.UUID]*Saga),
		handlers:    make(map[string]SagaHandler),
		definitions: make(map[string]*SagaDefinition),
		eventBus:    eventBus,
		logger:      logger,
	}
}

// RegisterSagaDefinition registers a saga definition
func (c *Coordinator) RegisterSagaDefinition(definition *SagaDefinition) {
	c.mu.Lock()
	defer c.mu.Unlock()

	c.definitions[definition.Name] = definition
	c.logger.Info("Registered saga definition",
		zap.String("saga_name", definition.Name),
		zap.Int("step_count", len(definition.Steps)))
}

// RegisterHandler registers a saga handler
func (c *Coordinator) RegisterHandler(action string, handler SagaHandler) {
	c.mu.Lock()
	defer c.mu.Unlock()

	c.handlers[action] = handler
	c.logger.Info("Registered saga handler",
		zap.String("action", action))
}

// StartSaga starts a new saga
func (c *Coordinator) StartSaga(ctx context.Context, sagaName string, data map[string]interface{}) (*Saga, error) {
	c.mu.Lock()
	defer c.mu.Unlock()

	definition, exists := c.definitions[sagaName]
	if !exists {
		return nil, fmt.Errorf("saga definition not found: %s", sagaName)
	}

	saga := &Saga{
		ID:        uuid.New(),
		Name:      sagaName,
		Status:    SagaPending,
		Steps:     make([]*SagaStep, len(definition.Steps)),
		CreatedAt: time.Now().UTC(),
		UpdatedAt: time.Now().UTC(),
		Data:      data,
	}

	// Initialize steps
	for i, stepDef := range definition.Steps {
		saga.Steps[i] = &SagaStep{
			ID:           fmt.Sprintf("%s-step-%d", saga.ID.String(), i),
			Name:         stepDef.Name,
			Action:       stepDef.Action,
			Compensation: stepDef.Compensation,
			Status:       SagaStepPending,
		}
	}

	c.sagas[saga.ID] = saga

	c.logger.Info("Started saga",
		zap.String("saga_id", saga.ID.String()),
		zap.String("saga_name", sagaName),
		zap.Int("step_count", len(saga.Steps)))

	return saga, nil
}

// ExecuteSaga executes a saga
func (c *Coordinator) ExecuteSaga(ctx context.Context, sagaID uuid.UUID) error {
	c.mu.Lock()
	saga, exists := c.sagas[sagaID]
	c.mu.Unlock()

	if !exists {
		return fmt.Errorf("saga not found: %s", sagaID)
	}

	if saga.Status != SagaPending {
		return fmt.Errorf("saga already executed: %s", saga.Status)
	}

	saga.Status = SagaExecuting
	saga.UpdatedAt = time.Now().UTC()

	c.logger.Info("Executing saga",
		zap.String("saga_id", saga.ID.String()),
		zap.String("saga_name", saga.Name))

	// Execute steps sequentially
	for _, step := range saga.Steps {
		if err := c.executeStep(ctx, saga, step); err != nil {
			c.logger.Error("Saga step failed",
				zap.Error(err),
				zap.String("saga_id", saga.ID.String()),
				zap.String("step_name", step.Name))

			// Start compensation
			return c.compensateSaga(ctx, saga, step)
		}
	}

	// Saga completed successfully
	saga.Status = SagaCompleted
	now := time.Now().UTC()
	saga.CompletedAt = &now
	saga.UpdatedAt = now

	c.logger.Info("Saga completed successfully",
		zap.String("saga_id", saga.ID.String()),
		zap.String("saga_name", saga.Name))

	return nil
}

// executeStep executes a single saga step
func (c *Coordinator) executeStep(ctx context.Context, saga *Saga, step *SagaStep) error {
	step.Status = SagaStepExecuting

	handler, exists := c.handlers[step.Action]
	if !exists {
		step.Status = SagaStepFailed
		step.Error = fmt.Sprintf("handler not found for action: %s", step.Action)
		return fmt.Errorf("handler not found for action: %s", step.Action)
	}

	executeCtx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	start := time.Now()
	err := handler.ExecuteStep(executeCtx, saga, step)
	duration := time.Since(start)

	if err != nil {
		step.Status = SagaStepFailed
		step.Error = err.Error()
		return err
	}

	step.Status = SagaStepCompleted
	now := time.Now().UTC()
	step.ExecutedAt = &now

	c.logger.Info("Saga step completed",
		zap.String("saga_id", saga.ID.String()),
		zap.String("step_name", step.Name),
		zap.Duration("duration", duration))

	return nil
}

// compensateSaga compensates failed saga
func (c *Coordinator) compensateSaga(ctx context.Context, saga *Saga, failedStep *SagaStep) error {
	saga.Status = SagaCompensating
	saga.UpdatedAt = time.Now().UTC()

	c.logger.Info("Compensating saga",
		zap.String("saga_id", saga.ID.String()),
		zap.String("failed_step", failedStep.Name))

	// Find failed step index and compensate from there backwards
	failedIndex := -1
	for i, step := range saga.Steps {
		if step.ID == failedStep.ID {
			failedIndex = i
			break
		}
	}

	// Compensate completed steps in reverse order
	for i := failedIndex; i >= 0; i-- {
		step := saga.Steps[i]
		if step.Status == SagaStepCompleted {
			if err := c.compensateStep(ctx, saga, step); err != nil {
				c.logger.Error("Compensation failed",
					zap.Error(err),
					zap.String("saga_id", saga.ID.String()),
					zap.String("step_name", step.Name))
				// Continue with other compensations even if one fails
			}
		}
	}

	saga.Status = SagaCompensated
	saga.UpdatedAt = time.Now().UTC()

	c.logger.Info("Saga compensation completed",
		zap.String("saga_id", saga.ID.String()))

	return nil
}

// compensateStep compensates a single step
func (c *Coordinator) compensateStep(ctx context.Context, saga *Saga, step *SagaStep) error {
	if step.Compensation == "" {
		c.logger.Warn("No compensation defined for step",
			zap.String("saga_id", saga.ID.String()),
			zap.String("step_name", step.Name))
		step.Status = SagaStepCompensated
		return nil
	}

	handler, exists := c.handlers[step.Compensation]
	if !exists {
		c.logger.Error("Compensation handler not found",
			zap.String("saga_id", saga.ID.String()),
			zap.String("step_name", step.Name),
			zap.String("compensation", step.Compensation))
		return fmt.Errorf("compensation handler not found: %s", step.Compensation)
	}

	compensateCtx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	start := time.Now()
	err := handler.CompensateStep(compensateCtx, saga, step)
	duration := time.Since(start)

	if err != nil {
		c.logger.Error("Step compensation failed",
			zap.Error(err),
			zap.String("saga_id", saga.ID.String()),
			zap.String("step_name", step.Name),
			zap.Duration("duration", duration))
		return err
	}

	step.Status = SagaStepCompensated

	c.logger.Info("Step compensation completed",
		zap.String("saga_id", saga.ID.String()),
		zap.String("step_name", step.Name),
		zap.Duration("duration", duration))

	return nil
}

// GetSaga returns saga by ID
func (c *Coordinator) GetSaga(sagaID uuid.UUID) (*Saga, bool) {
	c.mu.RLock()
	defer c.mu.RUnlock()

	saga, exists := c.sagas[sagaID]
	return saga, exists
}

// GetActiveSagas returns all active sagas
func (c *Coordinator) GetActiveSagas() []*Saga {
	c.mu.RLock()
	defer c.mu.RUnlock()

	active := make([]*Saga, 0)
	for _, saga := range c.sagas {
		if saga.Status == SagaPending || saga.Status == SagaExecuting || saga.Status == SagaCompensating {
			active = append(active, saga)
		}
	}

	return active
}

// Start starts the saga coordinator
func (c *Coordinator) Start(ctx context.Context) error {
	c.mu.Lock()
	defer c.mu.Unlock()

	if c.running {
		return fmt.Errorf("saga coordinator already running")
	}

	c.running = true
	c.logger.Info("Saga coordinator started")

	// Subscribe to events that might trigger saga compensations
	eventHandler := &sagaEventHandler{coordinator: c}
	c.eventBus.Subscribe("SagaCompensationRequested", eventHandler)

	return nil
}

// Stop stops the saga coordinator
func (c *Coordinator) Stop() {
	c.mu.Lock()
	defer c.mu.Unlock()

	c.running = false
	c.logger.Info("Saga coordinator stopped")
}

// sagaEventHandler handles events related to sagas
type sagaEventHandler struct {
	coordinator *Coordinator
}

// Handle processes saga-related events
func (h *sagaEventHandler) Handle(ctx context.Context, event events.DomainEvent) error {
	// Handle saga compensation requests
	// Implementation would depend on specific event types
	h.coordinator.logger.Debug("Received saga event",
		zap.String("event_type", event.GetEventType()),
		zap.String("event_id", event.GetEventID().String()))

	return nil
}

// PlayerCreationSagaHandler implements saga handler for player creation
type PlayerCreationSagaHandler struct {
	logger *zap.Logger
}

// NewPlayerCreationSagaHandler creates a new player creation saga handler
func NewPlayerCreationSagaHandler(logger *zap.Logger) *PlayerCreationSagaHandler {
	return &PlayerCreationSagaHandler{logger: logger}
}

// ExecuteStep executes a player creation step
func (h *PlayerCreationSagaHandler) ExecuteStep(ctx context.Context, saga *Saga, step *SagaStep) error {
	h.logger.Info("Executing player creation step",
		zap.String("saga_id", saga.ID.String()),
		zap.String("step_name", step.Name),
		zap.String("action", step.Action))

	// Simulate step execution
	switch step.Action {
	case "validate_player_data":
		// Validate player data
		return h.validatePlayerData(saga.Data)
	case "create_player_account":
		// Create player account
		return h.createPlayerAccount(saga.Data)
	case "send_welcome_email":
		// Send welcome email
		return h.sendWelcomeEmail(saga.Data)
	default:
		return fmt.Errorf("unknown action: %s", step.Action)
	}
}

// CompensateStep compensates a player creation step
func (h *PlayerCreationSagaHandler) CompensateStep(ctx context.Context, saga *Saga, step *SagaStep) error {
	h.logger.Info("Compensating player creation step",
		zap.String("saga_id", saga.ID.String()),
		zap.String("step_name", step.Name),
		zap.String("compensation", step.Compensation))

	// Simulate compensation
	switch step.Compensation {
	case "delete_player_account":
		return h.deletePlayerAccount(saga.Data)
	case "cancel_welcome_email":
		return h.cancelWelcomeEmail(saga.Data)
	default:
		return fmt.Errorf("unknown compensation: %s", step.Compensation)
	}
}

// Placeholder implementations
func (h *PlayerCreationSagaHandler) validatePlayerData(data map[string]interface{}) error {
	username, ok := data["username"].(string)
	if !ok || username == "" {
		return fmt.Errorf("invalid username")
	}
	return nil
}

func (h *PlayerCreationSagaHandler) createPlayerAccount(data map[string]interface{}) error {
	// Simulate account creation
	time.Sleep(100 * time.Millisecond)
	return nil
}

func (h *PlayerCreationSagaHandler) sendWelcomeEmail(data map[string]interface{}) error {
	// Simulate email sending
	time.Sleep(50 * time.Millisecond)
	return nil
}

func (h *PlayerCreationSagaHandler) deletePlayerAccount(data map[string]interface{}) error {
	// Simulate account deletion
	time.Sleep(100 * time.Millisecond)
	return nil
}

func (h *PlayerCreationSagaHandler) cancelWelcomeEmail(data map[string]interface{}) error {
	// Simulate email cancellation
	time.Sleep(50 * time.Millisecond)
	return nil
}