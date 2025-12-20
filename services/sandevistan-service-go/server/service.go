// Package server Issue: #140875766 - Main Sandevistan Service
// Refactored to follow Single Responsibility Principle
package server

import (
	"context"
	"database/sql"
	"fmt"
	"sync"
	"time"

	"go.uber.org/zap"

	"necpgame/services/sandevistan-service-go/pkg/api"
)

// CooldownError represents a cooldown period error
type CooldownError struct {
	Message           string
	CooldownRemaining float64
}

func (e *CooldownError) Error() string {
	return e.Message
}

// SandevistanService contains the main business logic for Sandevistan system
type SandevistanService struct {
	repo                 *SandevistanRepository
	logger               *zap.Logger
	calculator           *SandevistanCalculator
	temporalMarksTracker *TemporalMarksTracker
	counterplayHandler   *CounterplayHandler
	actionBudgetEngine   *ActionPriorityBudgetEngine

	// Memory pools for optimization
	statePool  *sync.Pool
	statsPool  *sync.Pool
	actionPool *sync.Pool
}

// NewSandevistanService creates a new Sandevistan service
func NewSandevistanService(db *sql.DB, logger *zap.Logger) *SandevistanService {
	repo := NewSandevistanRepository(db, logger)
	service := &SandevistanService{
		repo:       repo,
		logger:     logger,
		calculator: NewSandevistanCalculator(),
	}

	// Initialize components
	service.temporalMarksTracker = NewTemporalMarksTracker(repo, logger)
	service.counterplayHandler = NewCounterplayHandler(repo, logger, service)
	service.actionBudgetEngine = NewActionPriorityBudgetEngine(repo, logger)

	// Initialize memory pools for optimization
	service.statePool = &sync.Pool{
		New: func() interface{} {
			return &api.SandevistanState{}
		},
	}
	service.statsPool = &sync.Pool{
		New: func() interface{} {
			return &api.SandevistanStats{}
		},
	}
	service.actionPool = &sync.Pool{
		New: func() interface{} {
			return &Action{}
		},
	}

	return service
}

// Memory pool methods for optimization

// getStateFromPool gets a state object from the pool
func (s *SandevistanService) getStateFromPool() *api.SandevistanState {
	return s.statePool.Get().(*api.SandevistanState)
}

// putStateToPool returns a state object to the pool
func (s *SandevistanService) putStateToPool(state *api.SandevistanState) {
	// Reset fields for reuse
	state.IsActive = false
	state.ActivationTime = api.OptNilDateTime{}
	state.RemainingTime = 0
	state.TimeDilation = 1.0
	state.CooldownRemaining = 0
	state.CyberpsychosisLevel = 0
	state.HeatLevel = 0
	s.statePool.Put(state)
}

// getStatsFromPool gets a stats object from the pool
func (s *SandevistanService) getStatsFromPool() *api.SandevistanStats {
	return s.statsPool.Get().(*api.SandevistanStats)
}

// putStatsToPool returns a stats object to the pool
func (s *SandevistanService) putStatsToPool(stats *api.SandevistanStats) {
	// Reset fields for reuse
	stats.Level = 0
	stats.MaxDuration = 0
	stats.CooldownTime = 0
	stats.TimeDilationBase = 0
	stats.CyberpsychosisResistance = 0
	stats.HeatDissipationRate = 0
	stats.TotalActivations = 0
	stats.TotalDuration = 0
	stats.BestStreak = 0
	stats.AverageDuration = 0
	s.statsPool.Put(stats)
}

// ActivateSandevistan activates Sandevistan for a player
func (s *SandevistanService) ActivateSandevistan(ctx context.Context, userID string, durationOverride *float64) (*api.SandevistanState, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	s.logger.Info("Activating Sandevistan", zap.String("user_id", userID))

	// Get current state
	state, err := s.repo.GetSandevistanState(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get current state: %w", err)
	}

	// Check if already active
	if state.IsActive {
		return nil, fmt.Errorf("sandevistan already active")
	}

	// Check cooldown
	if state.CooldownRemaining > 0 {
		return nil, fmt.Errorf("sandevistan on cooldown: %.1f seconds remaining", state.CooldownRemaining)
	}

	// Get stats for calculations
	stats, err := s.repo.GetSandevistanStats(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get stats: %w", err)
	}

	// Calculate duration
	duration := s.calculator.calculateDuration(stats.Level, durationOverride)

	// Calculate cooldown
	cooldown := s.calculator.calculateCooldown(stats.Level)

	// Calculate time dilation
	timeDilation := s.calculator.calculateTimeDilation(stats.Level)

	// Create new state
	now := time.Now()
	newState := s.getStateFromPool()
	newState.IsActive = true
	newState.ActivationTime = api.NewOptNilDateTime(now)
	newState.RemainingTime = api.NewOptFloat32(duration)
	newState.TimeDilation = timeDilation
	newState.CooldownRemaining = 0
	newState.CyberpsychosisLevel = 0
	newState.HeatLevel = 0

	// Save state
	if err := s.repo.SaveSandevistanState(ctx, userID, newState); err != nil {
		s.putStateToPool(newState)
		return nil, fmt.Errorf("failed to save state: %w", err)
	}

	// Update stats
	stats.TotalActivations++
	if err := s.repo.SaveSandevistanStats(ctx, userID, stats); err != nil {
		s.logger.Warn("Failed to update stats", zap.Error(err))
	}

	s.logger.Info("Sandevistan activated",
		zap.String("user_id", userID),
		zap.Float64("duration", duration),
		zap.Float64("time_dilation", timeDilation))

	return newState, nil
}

// DeactivateSandevistan deactivates Sandevistan for a player
func (s *SandevistanService) DeactivateSandevistan(ctx context.Context, userID string) (*api.SandevistanState, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	s.logger.Info("Deactivating Sandevistan", zap.String("user_id", userID))

	// Get current state
	state, err := s.repo.GetSandevistanState(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get current state: %w", err)
	}

	if !state.IsActive {
		return nil, fmt.Errorf("sandevistan not active")
	}

	// Get stats for cooldown calculation
	stats, err := s.repo.GetSandevistanStats(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get stats: %w", err)
	}

	// Calculate cooldown
	cooldown := s.calculator.calculateCooldown(stats.Level)

	// Apply delayed burst if temporal marks exist
	if err := s.temporalMarksTracker.ApplyDelayedBurst(ctx, userID); err != nil {
		s.logger.Warn("Failed to apply delayed burst", zap.Error(err))
	}

	// Update state
	now := time.Now()
	state.IsActive = false
	state.CooldownRemaining = cooldown
	state.LastActivation = &now

	// Save updated state
	if err := s.repo.SaveSandevistanState(ctx, userID, state); err != nil {
		return nil, fmt.Errorf("failed to save updated state: %w", err)
	}

	s.logger.Info("Sandevistan deactivated",
		zap.String("user_id", userID),
		zap.Float64("cooldown", cooldown))

	return state, nil
}

// GetSandevistanState gets the current Sandevistan state for a player
func (s *SandevistanService) GetSandevistanState(ctx context.Context, userID string) (*api.SandevistanState, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	return s.repo.GetSandevistanState(ctx, userID)
}

// GetSandevistanStats gets the Sandevistan stats for a player
func (s *SandevistanService) GetSandevistanStats(ctx context.Context, userID string) (*api.SandevistanStats, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	return s.repo.GetSandevistanStats(ctx, userID)
}

// UpgradeSandevistan upgrades Sandevistan for a player
func (s *SandevistanService) UpgradeSandevistan(ctx context.Context, userID, upgradeType string, level int) (*api.SandevistanStats, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	s.logger.Info("Upgrading Sandevistan",
		zap.String("user_id", userID),
		zap.String("upgrade_type", upgradeType),
		zap.Int("level", level))

	// Get current stats
	stats, err := s.repo.GetSandevistanStats(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get stats: %w", err)
	}

	// Calculate upgrade cost
	cost := s.calculator.calculateUpgradeCost(upgradeType, level)
	// TODO: Check if player has enough resources

	// Apply upgrade based on type
	switch upgradeType {
	case "duration":
		stats.MaxDuration = s.calculator.calculateDuration(level, nil)
	case "cooldown":
		stats.CooldownTime = s.calculator.calculateCooldown(level)
	case "efficiency":
		stats.TimeDilationBase = s.calculator.calculateTimeDilation(level)
	case "resistance":
		stats.CyberpsychosisResistance = s.calculator.calculateResistance(level)
	case "capacity":
		stats.HeatDissipationRate = s.calculator.calculateDissipationRate(level)
	default:
		return nil, fmt.Errorf("unknown upgrade type: %s", upgradeType)
	}

	// Save updated stats
	if err := s.repo.SaveSandevistanStats(ctx, userID, stats); err != nil {
		return nil, fmt.Errorf("failed to save upgraded stats: %w", err)
	}

	s.logger.Info("Sandevistan upgraded successfully",
		zap.String("user_id", userID),
		zap.String("upgrade_type", upgradeType),
		zap.Int("new_level", level))

	return stats, nil
}

// ApplyCounterplay applies counterplay effects to a Sandevistan user
func (s *SandevistanService) ApplyCounterplay(ctx context.Context, userID, counterplayType string) error {
	return s.counterplayHandler.ApplyCounterplay(ctx, userID, counterplayType)
}

// TrackTarget adds a target for temporal marks tracking
func (s *SandevistanService) TrackTarget(userID, targetID, targetType string) error {
	// Get current activation ID (simplified)
	activationID := "current_activation_" + userID
	return s.temporalMarksTracker.TrackTarget(activationID, targetID, targetType)
}

// GetTrackedTargets gets the list of tracked targets
func (s *SandevistanService) GetTrackedTargets(ctx context.Context, userID string) ([]*TemporalMark, error) {
	activationID := "current_activation_" + userID
	return s.temporalMarksTracker.GetTrackedTargets(ctx, activationID)
}

// CheckActionBudget checks the current action budget for a user
func (s *SandevistanService) CheckActionBudget(ctx context.Context, userID string) (*ActionBudget, error) {
	return s.actionBudgetEngine.CheckBudget(ctx, userID)
}

// ConsumeAction consumes an action from the budget
func (s *SandevistanService) ConsumeAction(ctx context.Context, userID, actionType, targetID string) error {
	return s.actionBudgetEngine.ConsumeAction(ctx, userID, actionType, targetID)
}

// ProcessActionBatch processes a batch of actions
func (s *SandevistanService) ProcessActionBatch(ctx context.Context, userID string, actions []Action) error {
	window := &MicroTickWindow{
		UserID:    userID,
		Actions:   actions,
		StartTime: time.Now(),
		EndTime:   time.Now().Add(100 * time.Millisecond), // 100ms window
		Processed: false,
	}

	return s.actionBudgetEngine.ProcessMicroTickWindow(window)
}
