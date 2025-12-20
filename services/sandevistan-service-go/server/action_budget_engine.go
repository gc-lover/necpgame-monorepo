// Package server Issue: #140875766 - Action Priority Budget Engine
// Extracted from service.go to follow Single Responsibility Principle
package server

import (
	"context"
	"fmt"
	"sort"
	"time"

	"go.uber.org/zap"
)

// ActionPriorityBudgetEngine manages action budgets during Sandevistan activation
type ActionPriorityBudgetEngine struct {
	repo   *SandevistanRepository
	logger *zap.Logger
}

// ActionBudget represents the current action budget for a user
type ActionBudget struct {
	UserID         string    `json:"user_id"`
	RemainingSlots int       `json:"remaining_slots"`
	MaxSlots       int       `json:"max_slots"`
	ResetTime      time.Time `json:"reset_time"`
	LastUpdated    time.Time `json:"last_updated"`
}

// Action represents a queued action during Sandevistan
type Action struct {
	Type       string                 `json:"type"`
	TargetID   string                 `json:"target_id"`
	Priority   int                    `json:"priority"`
	QueuedAt   time.Time              `json:"queued_at"`
	Parameters map[string]interface{} `json:"parameters,omitempty"`
}

// MicroTickWindow represents a micro-tick processing window
type MicroTickWindow struct {
	UserID    string    `json:"user_id"`
	Actions   []Action  `json:"actions"`
	StartTime time.Time `json:"start_time"`
	EndTime   time.Time `json:"end_time"`
	Processed bool      `json:"processed"`
}

// NewActionPriorityBudgetEngine creates a new action budget engine
func NewActionPriorityBudgetEngine(repo *SandevistanRepository, logger *zap.Logger) *ActionPriorityBudgetEngine {
	return &ActionPriorityBudgetEngine{
		repo:   repo,
		logger: logger,
	}
}

// CheckBudget checks the current action budget for a user
func (e *ActionPriorityBudgetEngine) CheckBudget(ctx context.Context, userID string) (*ActionBudget, error) {
	return e.repo.GetActionBudget(ctx, userID)
}

// ConsumeAction consumes an action slot and executes the action
func (e *ActionPriorityBudgetEngine) ConsumeAction(ctx context.Context, userID, actionType, targetID string) error {
	budget, err := e.repo.GetActionBudget(ctx, userID)
	if err != nil {
		return fmt.Errorf("failed to get action budget: %w", err)
	}

	if budget.RemainingSlots <= 0 {
		return fmt.Errorf("no action slots remaining")
	}

	// Create action
	action := Action{
		Type:     actionType,
		TargetID: targetID,
		Priority: e.calculatePriority(actionType),
		QueuedAt: time.Now(),
	}

	// Execute action
	if err := e.executeAction(action); err != nil {
		return fmt.Errorf("failed to execute action: %w", err)
	}

	// Update budget
	budget.RemainingSlots--
	budget.LastUpdated = time.Now()

	return e.repo.UpdateActionBudget(ctx, budget)
}

// ProcessMicroTickWindow processes a batch of actions in a micro-tick window
func (e *ActionPriorityBudgetEngine) ProcessMicroTickWindow(window *MicroTickWindow) error {
	if window.Processed {
		return fmt.Errorf("window already processed")
	}

	// Sort actions by priority
	sortedActions := e.sortActionsByPriority(window.Actions)

	e.logger.Debug("Processing micro-tick window",
		zap.String("user_id", window.UserID),
		zap.Int("action_count", len(sortedActions)),
		zap.Time("start_time", window.StartTime),
		zap.Time("end_time", window.EndTime))

	// Execute actions in priority order
	for _, action := range sortedActions {
		if err := e.executeAction(action); err != nil {
			e.logger.Error("Failed to execute action in micro-tick window",
				zap.String("action_type", action.Type),
				zap.String("target_id", action.TargetID),
				zap.Error(err))
			// Continue with other actions
		}
	}

	// Mark window as processed
	window.Processed = true
	return e.repo.MarkMicroTickWindowProcessed()
}

// sortActionsByPriority sorts actions by priority (highest first)
func (e *ActionPriorityBudgetEngine) sortActionsByPriority(actions []Action) []Action {
	sorted := make([]Action, len(actions))
	copy(sorted, actions)

	sort.Slice(sorted, func(i, j int) bool {
		return sorted[i].Priority > sorted[j].Priority
	})

	return sorted
}

// executeAction executes a single action
func (e *ActionPriorityBudgetEngine) executeAction(action Action) error {
	e.logger.Debug("Executing action",
		zap.String("type", action.Type),
		zap.String("target_id", action.TargetID),
		zap.Int("priority", action.Priority))

	// Implementation would depend on action type
	// This is a placeholder for actual action execution
	switch action.Type {
	case "attack":
		return e.executeAttackAction()
	case "defend":
		return e.executeDefendAction()
	case "ability":
		return e.executeAbilityAction()
	default:
		return fmt.Errorf("unknown action type: %s", action.Type)
	}
}

// executeAttackAction executes an attack action
func (e *ActionPriorityBudgetEngine) executeAttackAction() error {
	// Placeholder implementation
	return nil
}

// executeDefendAction executes a defend action
func (e *ActionPriorityBudgetEngine) executeDefendAction() error {
	// Placeholder implementation
	return nil
}

// executeAbilityAction executes an ability action
func (e *ActionPriorityBudgetEngine) executeAbilityAction() error {
	// Placeholder implementation
	return nil
}

// calculatePriority calculates action priority based on type
func (e *ActionPriorityBudgetEngine) calculatePriority(actionType string) int {
	switch actionType {
	case "emergency_defend":
		return 100
	case "counter_attack":
		return 90
	case "ability":
		return 80
	case "attack":
		return 70
	case "defend":
		return 60
	default:
		return 50
	}
}

// ResetBudget resets the action budget for a user
func (e *ActionPriorityBudgetEngine) ResetBudget(ctx context.Context, userID string) error {
	budget := &ActionBudget{
		UserID:         userID,
		RemainingSlots: 10, // Default max slots
		MaxSlots:       10,
		ResetTime:      time.Now().Add(24 * time.Hour), // Reset in 24 hours
		LastUpdated:    time.Now(),
	}

	return e.repo.SaveActionBudget(ctx, budget)
}
