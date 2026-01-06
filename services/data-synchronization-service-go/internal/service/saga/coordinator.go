package saga

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-faster/errors"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/data-synchronization-service-go/internal/service/sync"
)

// Config holds saga coordinator configuration
type Config struct {
	DB          *pgxpool.Pool
	Redis       *redis.Client
	SyncManager *sync.Manager
	Logger      *zap.Logger
	Meter       metric.Meter
}

// Coordinator manages distributed synchronization sagas
type Coordinator struct {
	config      Config
	logger      *zap.Logger
	db          *pgxpool.Pool
	redis       *redis.Client
	syncManager *sync.Manager
	meter       metric.Meter
}

// NewCoordinator creates a new saga coordinator
func NewCoordinator(config Config) *Coordinator {
	return &Coordinator{
		config:      config,
		logger:      config.Logger,
		db:          config.DB,
		redis:       config.Redis,
		syncManager: config.SyncManager,
		meter:       config.Meter,
	}
}

// Start starts the saga coordinator
func (c *Coordinator) Start(ctx context.Context) error {
	c.logger.Info("saga coordinator started")
	return nil
}

// Stop stops the saga coordinator
func (c *Coordinator) Stop(ctx context.Context) error {
	c.logger.Info("saga coordinator stopped")
	return nil
}

// StartSaga starts a new synchronization saga
func (c *Coordinator) StartSaga(ctx context.Context, sagaType string, steps []SagaStep) (*Saga, error) {
	// Generate unique saga ID
	sagaID := c.generateSagaID()

	// Validate saga steps
	if err := c.validateSagaSteps(steps); err != nil {
		return nil, err
	}

	// Create saga instance
	saga := &Saga{
		ID:          sagaID,
		Type:        sagaType,
		Status:      "pending",
		Steps:       steps,
		CurrentStep: -1,
	}

	// Create saga execution record
	execution := &SagaExecution{
		SagaID:      sagaID,
		Status:      "pending",
		CurrentStep: -1,
		StepResults: make(map[int]StepResult),
		CreatedAt:   time.Now().Unix(),
		UpdatedAt:   time.Now().Unix(),
		Metadata:    map[string]interface{}{"type": sagaType},
	}

	// Persist saga to database
	if err := c.persistSaga(ctx, saga, execution); err != nil {
		return nil, err
	}

	// Start saga execution asynchronously
	go c.executeSaga(ctx, saga, execution)

	c.logger.Info("saga created and started",
		zap.String("saga_id", sagaID),
		zap.String("type", sagaType),
		zap.Int("steps", len(steps)))

	return saga, nil
}

// generateSagaID generates a unique saga ID
func (c *Coordinator) generateSagaID() string {
	bytes := make([]byte, 16)
	rand.Read(bytes)
	return "saga-" + hex.EncodeToString(bytes)[:16]
}

// validateSagaSteps validates saga steps configuration
func (c *Coordinator) validateSagaSteps(steps []SagaStep) error {
	if len(steps) == 0 {
		return errors.New("saga must have at least one step")
	}

	for i, step := range steps {
		if step.Service == "" {
			return fmt.Errorf("step %d: service name is required", i)
		}
		if step.Operation == "" {
			return fmt.Errorf("step %d: operation is required", i)
		}
		if step.Timeout <= 0 {
			step.Timeout = 30 // Default timeout
		}
		if step.MaxRetries < 0 {
			step.MaxRetries = 3 // Default max retries
		}
	}

	return nil
}

// persistSaga persists saga and execution data to database
func (c *Coordinator) persistSaga(ctx context.Context, saga *Saga, execution *SagaExecution) error {
	// Convert saga steps to JSON
	stepsJSON, err := json.Marshal(saga.Steps)
	if err != nil {
		return errors.Wrap(err, "failed to marshal saga steps")
	}

	// Convert execution metadata to JSON
	metadataJSON, err := json.Marshal(execution.Metadata)
	if err != nil {
		return errors.Wrap(err, "failed to marshal execution metadata")
	}

	// Convert step results to JSON
	resultsJSON, err := json.Marshal(execution.StepResults)
	if err != nil {
		return errors.Wrap(err, "failed to marshal step results")
	}

	// Insert saga record
	sagaQuery := `
		INSERT INTO sagas.sagas (id, type, status, steps, current_step, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`

	now := time.Now()
	_, err = c.db.Exec(ctx, sagaQuery,
		saga.ID, saga.Type, saga.Status, string(stepsJSON),
		saga.CurrentStep, now, now)
	if err != nil {
		return errors.Wrap(err, "failed to persist saga")
	}

	// Insert execution record
	execQuery := `
		INSERT INTO sagas.executions (saga_id, status, current_step, step_results, metadata, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`

	_, err = c.db.Exec(ctx, execQuery,
		execution.SagaID, execution.Status, execution.CurrentStep,
		string(resultsJSON), string(metadataJSON),
		time.Unix(execution.CreatedAt, 0), time.Unix(execution.UpdatedAt, 0))

	return errors.Wrap(err, "failed to persist saga execution")
}

// executeSaga executes saga steps sequentially
func (c *Coordinator) executeSaga(ctx context.Context, saga *Saga, execution *SagaExecution) {
	defer c.handleSagaCompletion(saga, execution)

	saga.Status = "running"
	execution.Status = "running"
	c.updateSagaStatus(ctx, saga, execution)

	for i, step := range saga.Steps {
		saga.CurrentStep = i
		execution.CurrentStep = i

		stepCtx, cancel := context.WithTimeout(ctx, time.Duration(step.Timeout)*time.Second)

		startTime := time.Now()
		result := c.executeStep(stepCtx, step)
		duration := time.Since(startTime)

		cancel()

		// Record step result
		stepResult := StepResult{
			StepIndex:  i,
			Status:     result.Status,
			Result:     result.Data,
			Error:      result.Error,
			ExecutedAt: time.Now().Unix(),
			DurationMs: duration.Milliseconds(),
		}

		execution.StepResults[i] = stepResult
		c.updateSagaStatus(ctx, saga, execution)

		if result.Status == "failed" {
			// Execute compensation for all completed steps
			c.executeCompensation(ctx, saga, execution, i)
			saga.Status = "failed"
			execution.Status = "failed"
			break
		}
	}

	if saga.Status == "running" {
		saga.Status = "completed"
		execution.Status = "completed"
	}
}

// executeStep executes a single saga step
func (c *Coordinator) executeStep(ctx context.Context, step SagaStep) StepExecutionResult {
	// Implement step execution based on service and operation
	// This is a simplified implementation - in reality, this would route to specific services

	result := StepExecutionResult{Status: "pending"}

	switch step.Service {
	case "user-service":
		result = c.executeUserServiceOperation(ctx, step)
	case "inventory-service":
		result = c.executeInventoryServiceOperation(ctx, step)
	case "gameplay-service":
		result = c.executeGameplayServiceOperation(ctx, step)
	default:
		result = StepExecutionResult{
			Status: "failed",
			Error:  fmt.Sprintf("unknown service: %s", step.Service),
		}
	}

	return result
}

// StepExecutionResult represents the result of executing a saga step
type StepExecutionResult struct {
	Status string
	Data   interface{}
	Error  string
}

// executeUserServiceOperation executes user service operations
func (c *Coordinator) executeUserServiceOperation(ctx context.Context, step SagaStep) StepExecutionResult {
	switch step.Operation {
	case "create_user":
		return c.syncManager.CreateUser(ctx, step.Payload)
	case "update_user":
		return c.syncManager.UpdateUser(ctx, step.Payload)
	case "delete_user":
		return c.syncManager.DeleteUser(ctx, step.Payload)
	default:
		return StepExecutionResult{
			Status: "failed",
			Error:  fmt.Sprintf("unknown user operation: %s", step.Operation),
		}
	}
}

// executeInventoryServiceOperation executes inventory service operations
func (c *Coordinator) executeInventoryServiceOperation(ctx context.Context, step SagaStep) StepExecutionResult {
	switch step.Operation {
	case "add_item":
		return c.syncManager.AddInventoryItem(ctx, step.Payload)
	case "remove_item":
		return c.syncManager.RemoveInventoryItem(ctx, step.Payload)
	case "update_quantity":
		return c.syncManager.UpdateItemQuantity(ctx, step.Payload)
	default:
		return StepExecutionResult{
			Status: "failed",
			Error:  fmt.Sprintf("unknown inventory operation: %s", step.Operation),
		}
	}
}

// executeGameplayServiceOperation executes gameplay service operations
func (c *Coordinator) executeGameplayServiceOperation(ctx context.Context, step SagaStep) StepExecutionResult {
	switch step.Operation {
	case "update_player_stats":
		return c.syncManager.UpdatePlayerStats(ctx, step.Payload)
	case "sync_achievements":
		return c.syncManager.SyncAchievements(ctx, step.Payload)
	case "update_quest_progress":
		return c.syncManager.UpdateQuestProgress(ctx, step.Payload)
	default:
		return StepExecutionResult{
			Status: "failed",
			Error:  fmt.Sprintf("unknown gameplay operation: %s", step.Operation),
		}
	}
}

// executeCompensation executes compensation for failed saga steps
func (c *Coordinator) executeCompensation(ctx context.Context, saga *Saga, execution *SagaExecution, failedStep int) {
	c.logger.Info("executing compensation for failed saga",
		zap.String("saga_id", saga.ID),
		zap.Int("failed_step", failedStep))

	// Execute compensation in reverse order
	for i := failedStep; i >= 0; i-- {
		step := saga.Steps[i]
		if step.Compensate != nil {
			c.logger.Info("executing compensation",
				zap.String("saga_id", saga.ID),
				zap.Int("step", i))

			// Execute compensation step
			compStep := SagaStep{
				Service:   step.Service,
				Operation: fmt.Sprintf("compensate_%s", step.Operation),
				Payload:   step.Compensate,
				Timeout:   step.Timeout,
			}

			result := c.executeStep(ctx, compStep)

			// Record compensation result
			compResult := StepResult{
				StepIndex:  i,
				Status:     result.Status,
				Result:     result.Data,
				Error:      result.Error,
				ExecutedAt: time.Now().Unix(),
				DurationMs: 0, // Compensation timing not tracked
			}

			execution.StepResults[i+len(saga.Steps)] = compResult
		}
	}
}

// updateSagaStatus updates saga and execution status in database
func (c *Coordinator) updateSagaStatus(ctx context.Context, saga *Saga, execution *SagaExecution) {
	// Update saga
	sagaQuery := `UPDATE sagas.sagas SET status = $1, current_step = $2, updated_at = $3 WHERE id = $4`
	c.db.Exec(ctx, sagaQuery, saga.Status, saga.CurrentStep, time.Now(), saga.ID)

	// Update execution
	resultsJSON, _ := json.Marshal(execution.StepResults)
	execQuery := `UPDATE sagas.executions SET status = $1, current_step = $2, step_results = $3, updated_at = $4 WHERE saga_id = $5`
	c.db.Exec(ctx, execQuery, execution.Status, execution.CurrentStep, string(resultsJSON), time.Now(), saga.ID)
}

// handleSagaCompletion handles saga completion logic
func (c *Coordinator) handleSagaCompletion(saga *Saga, execution *SagaExecution) {
	c.logger.Info("saga completed",
		zap.String("saga_id", saga.ID),
		zap.String("status", saga.Status))

	// Update final status
	ctx := context.Background()
	c.updateSagaStatus(ctx, saga, execution)

	// Publish completion event
	event := map[string]interface{}{
		"type":      "saga_completed",
		"saga_id":   saga.ID,
		"saga_type": saga.Type,
		"status":    saga.Status,
		"steps":     len(saga.Steps),
		"duration":  time.Now().Unix() - execution.CreatedAt,
	}

	eventJSON, _ := json.Marshal(event)
	c.redis.Publish(ctx, "saga-events", eventJSON)
}

// GetSagaStatus retrieves the current status of a saga
func (c *Coordinator) GetSagaStatus(ctx context.Context, sagaID string) (*SagaExecution, error) {
	query := `SELECT status, current_step, step_results, metadata, created_at, updated_at FROM sagas.executions WHERE saga_id = $1`

	var status, resultsJSON, metadataJSON string
	var currentStep int
	var createdAt, updatedAt time.Time

	err := c.db.QueryRow(ctx, query, sagaID).Scan(&status, &currentStep, &resultsJSON, &metadataJSON, &createdAt, &updatedAt)
	if err != nil {
		return nil, errors.Wrap(err, "failed to get saga status")
	}

	var stepResults map[int]StepResult
	json.Unmarshal([]byte(resultsJSON), &stepResults)

	var metadata map[string]interface{}
	json.Unmarshal([]byte(metadataJSON), &metadata)

	return &SagaExecution{
		SagaID:      sagaID,
		Status:      status,
		CurrentStep: currentStep,
		StepResults: stepResults,
		CreatedAt:   createdAt.Unix(),
		UpdatedAt:   updatedAt.Unix(),
		Metadata:    metadata,
	}, nil
}

// Saga represents a distributed synchronization saga
type Saga struct {
	ID       string     `json:"id"`
	Type     string     `json:"type"`
	Status   string     `json:"status"`
	Steps    []SagaStep `json:"steps"`
	CurrentStep int     `json:"current_step"`
}

// SagaStep represents a single step in a saga
type SagaStep struct {
	Service     string      `json:"service"`
	Operation   string      `json:"operation"`
	Payload     interface{} `json:"payload"`
	Compensate  interface{} `json:"compensate,omitempty"`  // Compensation payload
	Timeout     int         `json:"timeout"`                // Timeout in seconds
	RetryCount  int         `json:"retry_count"`
	MaxRetries  int         `json:"max_retries"`
}

// SagaExecution represents the execution state of a saga
type SagaExecution struct {
	SagaID      string                 `json:"saga_id"`
	Status      string                 `json:"status"`
	CurrentStep int                    `json:"current_step"`
	StepResults map[int]StepResult     `json:"step_results"`
	CreatedAt   int64                  `json:"created_at"`
	UpdatedAt   int64                  `json:"updated_at"`
	Metadata    map[string]interface{} `json:"metadata"`
}

// StepResult represents the result of executing a saga step
type StepResult struct {
	StepIndex   int         `json:"step_index"`
	Status      string      `json:"status"`
	Result      interface{} `json:"result,omitempty"`
	Error       string      `json:"error,omitempty"`
	ExecutedAt  int64       `json:"executed_at"`
	DurationMs  int64       `json:"duration_ms"`
}
