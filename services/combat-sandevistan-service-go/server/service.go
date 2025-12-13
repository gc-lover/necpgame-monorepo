// Issue: #39, #1607
package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/combat-sandevistan-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	PhasePreparation = "preparation"
	PhaseActive      = "active"
	PhaseRecovery    = "recovery"
	PhaseIdle        = "idle"

	PreparationDuration = 300 * time.Millisecond
	ActiveDuration      = 4 * time.Second
	RecoveryDuration    = 6 * time.Second

	MaxActionBudget     = 100
	MaxActionsPerTick   = 3
	MaxTemporalMarks    = 3
	MaxHeatStacks       = 4
	OverstressThreshold = 4
)

type SandevistanService interface {
	Activate(ctx context.Context, playerID uuid.UUID) (*api.SandevistanActivation, error)
	Deactivate(ctx context.Context, playerID uuid.UUID) error
	GetStatus(ctx context.Context, playerID uuid.UUID) (*api.SandevistanStatus, error)
	UseActionBudget(ctx context.Context, playerID uuid.UUID, actions []api.Action) (*api.ActionBudgetResult, error)
	SetTemporalMarks(ctx context.Context, playerID uuid.UUID, targetIDs []uuid.UUID) error
	GetTemporalMarks(ctx context.Context, playerID uuid.UUID) ([]api.TemporalMark, error)
	ApplyCooling(ctx context.Context, playerID uuid.UUID, cartridgeID uuid.UUID) (*api.CoolingResult, error)
	GetHeatStatus(ctx context.Context, playerID uuid.UUID) (*api.HeatStatus, error)
	ApplyCounterplay(ctx context.Context, playerID uuid.UUID, effectType string, sourcePlayerID uuid.UUID) (*api.CounterplayResult, error)
	ApplyTemporalMarks(ctx context.Context, playerID uuid.UUID) (*api.TemporalMarksApplied, error)
	GetBonuses(ctx context.Context, playerID uuid.UUID) (*api.SandevistanBonuses, error)
	PublishPerceptionDragEvent(ctx context.Context, playerID uuid.UUID, event *api.PerceptionDragEvent) error
}

type sandevistanService struct {
	repo   Repository
	logger *logrus.Logger

	// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
	activationPool sync.Pool
	statusPool sync.Pool
	actionBudgetResultPool sync.Pool
	coolingResultPool sync.Pool
	heatStatusPool sync.Pool
	counterplayResultPool sync.Pool
}

func NewSandevistanService(repo Repository, logger *logrus.Logger) SandevistanService {
	s := &sandevistanService{
		repo:   repo,
		logger: logger,
	}

	// Initialize memory pools (zero allocations target!)
	s.activationPool = sync.Pool{
		New: func() interface{} {
			return &api.SandevistanActivation{}
		},
	}
	s.statusPool = sync.Pool{
		New: func() interface{} {
			return &api.SandevistanStatus{}
		},
	}
	s.actionBudgetResultPool = sync.Pool{
		New: func() interface{} {
			return &api.ActionBudgetResult{}
		},
	}
	s.coolingResultPool = sync.Pool{
		New: func() interface{} {
			return &api.CoolingResult{}
		},
	}
	s.heatStatusPool = sync.Pool{
		New: func() interface{} {
			return &api.HeatStatus{}
		},
	}
	s.counterplayResultPool = sync.Pool{
		New: func() interface{} {
			return &api.CounterplayResult{}
		},
	}

	return s
}

func (s *sandevistanService) Activate(ctx context.Context, playerID uuid.UUID) (*api.SandevistanActivation, error) {
	s.logger.WithField("player_id", playerID).Info("Activating Sandevistan")

	existing, err := s.repo.GetActivation(ctx, playerID)
	if err == nil && existing != nil && existing.IsActive {
		return nil, errors.New("sandevistan already active")
	}

	activationID := uuid.New()
	startedAt := time.Now()
	expiresAt := startedAt.Add(PreparationDuration + ActiveDuration + RecoveryDuration)
	phase := api.SandevistanActivationPhase(PhasePreparation)
	actionBudgetRemaining := MaxActionBudget

	activation := &Activation{
		ID:                   activationID,
		PlayerID:            playerID,
		Phase:                string(phase),
		StartedAt:            startedAt,
		ActivePhaseStartedAt: nil,
		RecoveryPhaseStartedAt: nil,
		EndedAt:              nil,
		ActionBudgetRemaining: actionBudgetRemaining,
		ActionBudgetMax:      MaxActionBudget,
		HeatStacks:           0,
		IsActive:             true,
	}

	if err := s.repo.SaveActivation(ctx, activation); err != nil {
		return nil, err
	}

	// Publish Perception Drag event for activation
	go s.publishActivationEvent(ctx, activation)

	go s.handlePhaseTransitions(ctx, activation)

	// Issue: #1607 - Use memory pooling
	response := s.activationPool.Get().(*api.SandevistanActivation)
	// Note: Not returning to pool - struct is returned to caller

	response.ActivationID = activationID
	response.StartedAt = startedAt
	response.ExpiresAt = expiresAt
	response.Phase = phase
	response.ActionBudgetRemaining = api.NewOptInt(actionBudgetRemaining)

	return response, nil
}

func (s *sandevistanService) handlePhaseTransitions(ctx context.Context, activation *Activation) {
	preparationTimer := time.NewTimer(PreparationDuration)
	<-preparationTimer.C

	activation.Phase = PhaseActive
	now := time.Now()
	activation.ActivePhaseStartedAt = &now
	s.repo.SaveActivation(ctx, activation)

	// Publish phase change to active
	s.publishPhaseChangeEvent(ctx, activation, api.PerceptionDragEventPhaseActive)

	activeTimer := time.NewTimer(ActiveDuration)
	<-activeTimer.C

	// Apply temporal marks effects before transitioning to recovery
	s.ApplyTemporalMarks(ctx, activation.PlayerID)

	activation.Phase = PhaseRecovery
	now = time.Now()
	activation.RecoveryPhaseStartedAt = &now
	s.repo.SaveActivation(ctx, activation)

	// Publish phase change to recovery
	s.publishPhaseChangeEvent(ctx, activation, api.PerceptionDragEventPhaseRecovery)

	recoveryTimer := time.NewTimer(RecoveryDuration)
	<-recoveryTimer.C

	activation.Phase = PhaseIdle
	activation.IsActive = false
	now = time.Now()
	activation.EndedAt = &now
	s.repo.SaveActivation(ctx, activation)

	// Publish deactivation event
	s.publishDeactivationEvent(ctx, activation)
}

func (s *sandevistanService) Deactivate(ctx context.Context, playerID uuid.UUID) error {
	s.logger.WithField("player_id", playerID).Info("Deactivating Sandevistan")

	activation, err := s.repo.GetActivation(ctx, playerID)
	if err != nil {
		return err
	}

	if activation == nil || !activation.IsActive {
		return errors.New("sandevistan not active")
	}

	activation.IsActive = false
	now := time.Now()
	activation.EndedAt = &now
	activation.Phase = PhaseIdle

	return s.repo.SaveActivation(ctx, activation)
}

func (s *sandevistanService) GetStatus(ctx context.Context, playerID uuid.UUID) (*api.SandevistanStatus, error) {
	activation, err := s.repo.GetActivation(ctx, playerID)
	if err != nil {
		return nil, err
	}

	isActive := false
	phase := api.SandevistanStatusPhase(PhaseIdle)
	cooldownRemaining := 0
	actionBudgetRemaining := 0
	heatStacks := 0
	temporalMarksCount := 0

	if activation != nil {
		isActive = activation.IsActive
		phase = api.SandevistanStatusPhase(activation.Phase)
		actionBudgetRemaining = activation.ActionBudgetRemaining
		heatStacks = activation.HeatStacks

		marks, _ := s.repo.GetTemporalMarks(ctx, playerID)
		temporalMarksCount = len(marks)
	}

	// Issue: #1607 - Use memory pooling
	status := s.statusPool.Get().(*api.SandevistanStatus)
	// Note: Not returning to pool - struct is returned to caller

	status.IsActive = isActive
	status.Phase = phase
	status.CooldownRemaining = cooldownRemaining
	status.ActionBudgetRemaining = api.NewOptInt(actionBudgetRemaining)
	status.HeatStacks = api.NewOptInt(heatStacks)
	status.TemporalMarksCount = api.NewOptInt(temporalMarksCount)

	return status, nil
}

func (s *sandevistanService) UseActionBudget(ctx context.Context, playerID uuid.UUID, actions []api.Action) (*api.ActionBudgetResult, error) {
	if len(actions) > MaxActionsPerTick {
		return nil, errors.New("too many actions in batch")
	}

	activation, err := s.repo.GetActivation(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if activation == nil || !activation.IsActive || activation.Phase != PhaseActive {
		return nil, errors.New("sandevistan not in active phase")
	}

	if activation.ActionBudgetRemaining < len(actions) {
		return nil, errors.New("insufficient action budget")
	}

	activation.ActionBudgetRemaining -= len(actions)
	if err := s.repo.SaveActivation(ctx, activation); err != nil {
		return nil, err
	}

	executedActions := make([]api.ActionBudgetResultExecutedActionsItem, len(actions))
	for i, action := range actions {
		success := true
		now := time.Now()
		actionTypeStr := string(action.Type)
		executedActions[i] = api.ActionBudgetResultExecutedActionsItem{
			ActionType: api.NewOptString(actionTypeStr),
			Success:    api.NewOptBool(success),
			Timestamp:  api.NewOptDateTime(now),
		}
	}

	// Issue: #1607 - Use memory pooling
	result := s.actionBudgetResultPool.Get().(*api.ActionBudgetResult)
	// Note: Not returning to pool - struct is returned to caller

	result.BudgetRemaining = activation.ActionBudgetRemaining
	result.ExecutedActions = executedActions

	return result, nil
}

func (s *sandevistanService) SetTemporalMarks(ctx context.Context, playerID uuid.UUID, targetIDs []uuid.UUID) error {
	if len(targetIDs) > MaxTemporalMarks {
		return errors.New("too many temporal marks")
	}

	activation, err := s.repo.GetActivation(ctx, playerID)
	if err != nil {
		return err
	}

	if activation == nil || !activation.IsActive {
		return errors.New("sandevistan not active")
	}

	marks := make([]*TemporalMark, len(targetIDs))
	for i, targetID := range targetIDs {
		marks[i] = &TemporalMark{
			ID:          uuid.New(),
			ActivationID: activation.ID,
			PlayerID:    playerID,
			TargetID:    targetID,
			MarkedAt:    time.Now(),
		}
	}

	return s.repo.SaveTemporalMarks(ctx, marks)
}

func (s *sandevistanService) GetTemporalMarks(ctx context.Context, playerID uuid.UUID) ([]api.TemporalMark, error) {
	marks, err := s.repo.GetTemporalMarks(ctx, playerID)
	if err != nil {
		return nil, err
	}

	result := make([]api.TemporalMark, len(marks))
	for i, mark := range marks {
		expiresAt := mark.MarkedAt.Add(5 * time.Second)
		result[i] = api.TemporalMark{
			MarkID:    mark.ID,
			TargetID:  mark.TargetID,
			MarkedAt:  mark.MarkedAt,
			ExpiresAt: expiresAt,
		}
	}

	return result, nil
}

func (s *sandevistanService) ApplyCooling(ctx context.Context, playerID uuid.UUID, cartridgeID uuid.UUID) (*api.CoolingResult, error) {
	activation, err := s.repo.GetActivation(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if activation == nil {
		return nil, errors.New("no active sandevistan activation")
	}

	heatStacksRemoved := 2
	if activation.HeatStacks > 0 {
		if activation.HeatStacks < heatStacksRemoved {
			heatStacksRemoved = activation.HeatStacks
		}
		activation.HeatStacks -= heatStacksRemoved
	}

	// Check and apply overstress effect if heat stacks were at threshold
	wasOverstress := activation.IsOverstress
	activation.IsOverstress = activation.HeatStacks >= OverstressThreshold

	newHeatLevel := activation.HeatStacks
	cooldownReduced := 5
	cyberpsychosisRisk := float32(activation.HeatStacks) * 0.1

	// Apply overstress effects if entering overstress state
	if !wasOverstress && activation.IsOverstress {
		s.applyOverstressEffects(ctx, activation)
	}

	if err := s.repo.SaveActivation(ctx, activation); err != nil {
		return nil, err
	}

	// Issue: #1607 - Use memory pooling
	result := s.coolingResultPool.Get().(*api.CoolingResult)
	// Note: Not returning to pool - struct is returned to caller

	result.HeatStacksRemoved = heatStacksRemoved
	result.NewHeatLevel = newHeatLevel
	result.CooldownReduced = api.NewOptInt(cooldownReduced)
	result.CyberpsychosisRisk = api.NewOptFloat32(cyberpsychosisRisk)

	return result, nil
}

func (s *sandevistanService) GetHeatStatus(ctx context.Context, playerID uuid.UUID) (*api.HeatStatus, error) {
	activation, err := s.repo.GetActivation(ctx, playerID)
	if err != nil {
		return nil, err
	}

	currentStacks := 0
	if activation != nil {
		currentStacks = activation.HeatStacks
	}

	cyberpsychosisRisk := float32(currentStacks) * 0.1

	// Check if currently in overstress from activation
	isCurrentlyOverstress := false
	if activation != nil {
		isCurrentlyOverstress = activation.IsOverstress
	}

	// Issue: #1607 - Use memory pooling
	status := s.heatStatusPool.Get().(*api.HeatStatus)
	// Note: Not returning to pool - struct is returned to caller

	status.CurrentStacks = currentStacks
	status.MaxStacks = MaxHeatStacks
	status.IsOverstress = isCurrentlyOverstress
	status.CyberpsychosisRisk = api.NewOptFloat32(cyberpsychosisRisk)

	return status, nil
}

func (s *sandevistanService) ApplyCounterplay(ctx context.Context, playerID uuid.UUID, effectType string, sourcePlayerID uuid.UUID) (*api.CounterplayResult, error) {
	activation, err := s.repo.GetActivation(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if activation == nil || !activation.IsActive {
		return nil, errors.New("sandevistan not active")
	}

	sandevistanInterrupted := false
	effectApplied := api.CounterplayResultEffectApplied("none")
	phaseEnded := false
	actionBudgetReduced := 0

	switch effectType {
		case "chrono_jammer":
		actionBudgetReduced = 50
		if activation.ActionBudgetRemaining > actionBudgetReduced {
			activation.ActionBudgetRemaining -= actionBudgetReduced
		} else {
			activation.ActionBudgetRemaining = 0
		}
		effectApplied = api.CounterplayResultEffectAppliedChronoJammer
	case "emp":
		sandevistanInterrupted = true
		activation.IsActive = false
		now := time.Now()
		activation.EndedAt = &now
		effectApplied = api.CounterplayResultEffectAppliedEmp
	case "dilation_break":
		sandevistanInterrupted = true
		phaseEnded = true
		activation.Phase = PhaseIdle
		activation.IsActive = false
		now := time.Now()
		activation.EndedAt = &now
		effectApplied = api.CounterplayResultEffectAppliedDilationBreak
	}

	if err := s.repo.SaveActivation(ctx, activation); err != nil {
		return nil, err
	}

	// Issue: #1607 - Use memory pooling
	result := s.counterplayResultPool.Get().(*api.CounterplayResult)
	// Note: Not returning to pool - struct is returned to caller

	result.SandevistanInterrupted = sandevistanInterrupted
	result.EffectApplied = effectApplied
	result.PhaseEnded = api.NewOptBool(phaseEnded)
	result.ActionBudgetReduced = api.NewOptInt(actionBudgetReduced)

	return result, nil
}

func (s *sandevistanService) ApplyTemporalMarks(ctx context.Context, playerID uuid.UUID) (*api.TemporalMarksApplied, error) {
	marks, err := s.repo.GetTemporalMarks(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if len(marks) == 0 {
		return &api.TemporalMarksApplied{
			MarksApplied: 0,
			DamageDealt:  0,
			TargetsHit:   []uuid.UUID{},
		}, nil
	}

	var appliedMarks []*TemporalMark
	var targetsHit []uuid.UUID
	totalDamage := 0

	for _, mark := range marks {
		// Check if mark is still valid (not expired)
		if time.Since(mark.MarkedAt) > 5*time.Second {
			continue
		}

		// Apply delayed burst effect
		// In PvP: 60% base damage, in PvE: full damage + neuroschock
		damage := 100 // Base damage, would be calculated based on game mechanics
		totalDamage += damage

		// Mark as applied
		now := time.Now()
		mark.AppliedAt = &now
		appliedMarks = append(appliedMarks, mark)
		targetsHit = append(targetsHit, mark.TargetID)

		s.logger.WithFields(logrus.Fields{
			"player_id": playerID,
			"target_id": mark.TargetID,
			"damage":    damage,
		}).Info("Applied temporal mark damage")
	}

	// Save applied marks
	if len(appliedMarks) > 0 {
		if err := s.repo.UpdateTemporalMarks(ctx, appliedMarks); err != nil {
			s.logger.WithError(err).Error("Failed to update temporal marks")
		}
	}

	return &api.TemporalMarksApplied{
		MarksApplied: len(appliedMarks),
		DamageDealt:  totalDamage,
		TargetsHit:   targetsHit,
	}, nil
}

func (s *sandevistanService) GetBonuses(ctx context.Context, playerID uuid.UUID) (*api.SandevistanBonuses, error) {
	activation, err := s.repo.GetActivation(ctx, playerID)
	if err != nil {
		return nil, err
	}

	isActive := false
	phase := api.SandevistanBonusesPhaseIdle
	var movementBoost, fireRateBoost, dashDistance float32
	autoTargetEnabled := false

	if activation != nil && activation.IsActive {
		isActive = true
		switch activation.Phase {
		case PhasePreparation:
			phase = api.SandevistanBonusesPhasePreparation
		case PhaseActive:
			phase = api.SandevistanBonusesPhaseActive
			// Active phase bonuses according to spec
			movementBoost = 1.4  // +40%
			fireRateBoost = 1.25 // +25%
			dashDistance = 15    // 15 meters
			autoTargetEnabled = true
		case PhaseRecovery:
			phase = api.SandevistanBonusesPhaseRecovery
			// Recovery phase penalties
			movementBoost = 0.8  // -20%
			fireRateBoost = 0.8  // -20%
		}
	}

	return &api.SandevistanBonuses{
		IsActive:          isActive,
		Phase:             phase,
		MovementBoost:     movementBoost,
		FireRateBoost:     fireRateBoost,
		DashDistance:      dashDistance,
		AutoTargetEnabled: autoTargetEnabled,
	}, nil
}

func (s *sandevistanService) PublishPerceptionDragEvent(ctx context.Context, playerID uuid.UUID, event *api.PerceptionDragEvent) error {
	// Validate event belongs to player
	if event.PlayerID != playerID {
		return errors.New("event player_id does not match request player_id")
	}

	// Here we would publish to realtime-service via message queue
	// For now, just log the event
	s.logger.WithFields(logrus.Fields{
		"event_type":       event.EventType,
		"player_id":        event.PlayerID,
		"phase":            event.Phase,
		"affected_players": len(event.AffectedPlayers),
		"drag_duration_ms": event.DragDurationMs,
		"drag_intensity":   event.DragIntensity,
	}).Info("Published Perception Drag event")

	// TODO: Integrate with realtime-service for actual event publishing
	// This would involve sending the event to a message queue that realtime-service subscribes to

	return nil
}

func (s *sandevistanService) publishActivationEvent(ctx context.Context, activation *Activation) {
	// Get nearby players (simplified - in real implementation would query world service)
	affectedPlayers := []uuid.UUID{
		// This would be populated with actual nearby players
		uuid.New(), // placeholder
	}

	event := &api.PerceptionDragEvent{
		EventType:       api.PerceptionDragEventEventTypeSandevistanActivated,
		PlayerID:        activation.PlayerID,
		ActivationID:    activation.ID,
		Phase:           api.PerceptionDragEventPhasePreparation,
		Timestamp:       activation.StartedAt,
		AffectedPlayers: affectedPlayers,
		DragDurationMs:  300, // Preparation phase duration
		DragIntensity:   0.3, // Low intensity for preparation
	}

	_ = s.PublishPerceptionDragEvent(ctx, activation.PlayerID, event)
}

func (s *sandevistanService) publishPhaseChangeEvent(ctx context.Context, activation *Activation, phase api.PerceptionDragEventPhase) {
	affectedPlayers := []uuid.UUID{uuid.New()} // placeholder

	var dragDuration int
	var dragIntensity float32

	switch phase {
	case api.PerceptionDragEventPhaseActive:
		dragDuration = 4000 // Active phase duration
		dragIntensity = 1.0 // Full intensity
	case api.PerceptionDragEventPhaseRecovery:
		dragDuration = 6000 // Recovery phase duration
		dragIntensity = 0.5 // Medium intensity
	}

	event := &api.PerceptionDragEvent{
		EventType:       api.PerceptionDragEventEventTypePhaseChanged,
		PlayerID:        activation.PlayerID,
		ActivationID:    activation.ID,
		Phase:           phase,
		Timestamp:       time.Now(),
		AffectedPlayers: affectedPlayers,
		DragDurationMs:  dragDuration,
		DragIntensity:   dragIntensity,
	}

	_ = s.PublishPerceptionDragEvent(ctx, activation.PlayerID, event)
}

func (s *sandevistanService) publishDeactivationEvent(ctx context.Context, activation *Activation) {
	affectedPlayers := []uuid.UUID{uuid.New()} // placeholder

	event := &api.PerceptionDragEvent{
		EventType:       api.PerceptionDragEventEventTypeSandevistanDeactivated,
		PlayerID:        activation.PlayerID,
		ActivationID:    activation.ID,
		Phase:           api.PerceptionDragEventPhaseIdle,
		Timestamp:       time.Now(),
		AffectedPlayers: affectedPlayers,
		DragDurationMs:  0,
		DragIntensity:   0,
	}

	_ = s.PublishPerceptionDragEvent(ctx, activation.PlayerID, event)
}

func (s *sandevistanService) applyOverstressEffects(ctx context.Context, activation *Activation) {
	// Apply overstress effects: 30% HP loss and uncontrolled dash
	// In a real implementation, this would integrate with combat-service/health-system

	s.logger.WithFields(logrus.Fields{
		"player_id": activation.PlayerID,
		"activation_id": activation.ID,
	}).Warn("Overstress effect triggered: 30% HP loss and uncontrolled dash")

	// TODO: Integrate with combat-service to apply:
	// - 30% HP damage
	// - Force an uncontrolled dash movement
	// - Apply stun/knockback effects

	// For now, just log the effect
	// In production, this would call combat-service APIs
}

