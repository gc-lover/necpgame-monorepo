package server

import (
	"context"
	"fmt"
	"log/slog"
	"sync"
	"time"

	"github.com/google/uuid"
)

// Service defines the business logic interface for AI Behavior Engine
type Service interface {
	ExecuteBehavior(ctx context.Context, req ExecuteBehaviorRequest) (*BehaviorResult, error)
	GetBehaviorState(ctx context.Context, enemyID uuid.UUID) (*BehaviorState, error)
	UpdateBehaviorTree(ctx context.Context, req UpdateBehaviorTreeRequest) error
	// TODO: Add other business methods
}

// AiBehaviorEngineService implements the business logic for AI behavior management
type AiBehaviorEngineService struct {
	repo     Repository
	metrics  *ServiceMetrics
	mu       sync.RWMutex
	behaviors map[string]*BehaviorTree // Performance: Memory pooling for behavior trees
}

// BehaviorTree represents an AI behavior tree structure
type BehaviorTree struct {
	ID          string
	Name        string
	RootNode    *BehaviorNode
	LastUpdated time.Time
}

// BehaviorNode represents a node in the behavior tree
type BehaviorNode struct {
	ID       string
	Type     string // sequence, selector, action, condition
	Children []*BehaviorNode
	Status   string // running, success, failure
}

// ExecuteBehaviorRequest represents a request to execute AI behavior
type ExecuteBehaviorRequest struct {
	EnemyID      uuid.UUID
	BehaviorType string
	ContextData  map[string]interface{}
	Priority     int
}

// BehaviorResult represents the result of behavior execution
type BehaviorResult struct {
	EnemyID      uuid.UUID
	Action       string
	Parameters   map[string]interface{}
	Success      bool
	ExecutionTime time.Duration
}

// BehaviorState represents current AI behavior state
type BehaviorState struct {
	EnemyID        uuid.UUID
	CurrentBehavior string
	Priority       int
	Status         string
	LastUpdate     time.Time
}

// UpdateBehaviorTreeRequest represents a request to update behavior tree
type UpdateBehaviorTreeRequest struct {
	TreeID   string
	NewTree  *BehaviorTree
	Version  int
}

// PerformanceMetrics tracks service performance
type PerformanceMetrics struct {
	CPUUsagePercent       float64
	MemoryUsageMB         int
	BehaviorExecutionTime time.Duration
	DecisionLatency       time.Duration
}

// ServiceMetrics collects service-wide metrics
type ServiceMetrics struct {
	TotalActiveBehaviors int64
	AverageLatency       time.Duration
	ErrorRate            float64
	SuccessRate          float64
}

// NewAiBehaviorEngineService creates a new service instance
func NewAiBehaviorEngineService(repo Repository) *AiBehaviorEngineService {
	return &AiBehaviorEngineService{
		repo:      repo,
		metrics:   &ServiceMetrics{},
		behaviors: make(map[string]*BehaviorTree),
	}
}

// ExecuteBehavior implements AI behavior execution business logic
func (s *AiBehaviorEngineService) ExecuteBehavior(ctx context.Context, req ExecuteBehaviorRequest) (*BehaviorResult, error) {
	start := time.Now()
	defer func() {
		duration := time.Since(start)
		slog.Info("ExecuteBehavior completed", "duration_ms", duration.Milliseconds())
	}()

	// Validate request
	if err := s.validateExecuteRequest(req); err != nil {
		return nil, fmt.Errorf("invalid execute request: %w", err)
	}

	// Get behavior tree for this enemy type
	behaviorTree, exists := s.behaviors[req.BehaviorType]
	if !exists {
		return nil, fmt.Errorf("behavior tree not found for type: %s", req.BehaviorType)
	}

	// Execute behavior tree (simplified implementation)
	result := &BehaviorResult{
		EnemyID:      req.EnemyID,
		Action:       "patrol", // Default action
		Parameters:   req.ContextData,
		Success:      true,
		ExecutionTime: time.Since(start),
	}

	// Update metrics
	s.mu.Lock()
	s.metrics.TotalActiveBehaviors++
	s.mu.Unlock()

	slog.Info("Behavior executed successfully",
		"enemy_id", req.EnemyID,
		"behavior_type", req.BehaviorType,
		"action", result.Action,
	)

	return result, nil
}

// GetBehaviorState implements behavior state retrieval
func (s *AiBehaviorEngineService) GetBehaviorState(ctx context.Context, enemyID uuid.UUID) (*BehaviorState, error) {
	// TODO: Implement behavior state retrieval from repository
	return &BehaviorState{
		EnemyID:        enemyID,
		CurrentBehavior: "idle",
		Priority:       1,
		Status:         "active",
		LastUpdate:     time.Now().UTC(),
	}, nil
}

// UpdateBehaviorTree implements behavior tree updates
func (s *AiBehaviorEngineService) UpdateBehaviorTree(ctx context.Context, req UpdateBehaviorTreeRequest) error {
	// TODO: Implement behavior tree update with versioning
	s.mu.Lock()
	defer s.mu.Unlock()

	s.behaviors[req.TreeID] = req.NewTree
	slog.Info("Behavior tree updated", "tree_id", req.TreeID)
	return nil
}

// validateExecuteRequest validates behavior execution request
func (s *AiBehaviorEngineService) validateExecuteRequest(req ExecuteBehaviorRequest) error {
	if req.EnemyID == uuid.Nil {
		return fmt.Errorf("enemy ID is required")
	}
	if req.BehaviorType == "" {
		return fmt.Errorf("behavior type is required")
	}
	if req.Priority < 1 || req.Priority > 10 {
		return fmt.Errorf("priority must be between 1 and 10")
	}
	return nil
}