// Issue: #39
package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/combat-sandevistan-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
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
}

type sandevistanService struct {
	repo   Repository
	logger *logrus.Logger
}

func NewSandevistanService(repo Repository, logger *logrus.Logger) SandevistanService {
	return &sandevistanService{
		repo:   repo,
		logger: logger,
	}
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

	go s.handlePhaseTransitions(ctx, activation)

	response := &api.SandevistanActivation{
		ActivationId:          activationID,
		StartedAt:             startedAt,
		ExpiresAt:             expiresAt,
		Phase:                 phase,
		ActionBudgetRemaining: &actionBudgetRemaining,
	}

	return response, nil
}

func (s *sandevistanService) handlePhaseTransitions(ctx context.Context, activation *Activation) {
	preparationTimer := time.NewTimer(PreparationDuration)
	<-preparationTimer.C

	activation.Phase = PhaseActive
	now := time.Now()
	activation.ActivePhaseStartedAt = &now
	s.repo.SaveActivation(ctx, activation)

	activeTimer := time.NewTimer(ActiveDuration)
	<-activeTimer.C

	activation.Phase = PhaseRecovery
	now = time.Now()
	activation.RecoveryPhaseStartedAt = &now
	s.repo.SaveActivation(ctx, activation)

	recoveryTimer := time.NewTimer(RecoveryDuration)
	<-recoveryTimer.C

	activation.Phase = PhaseIdle
	activation.IsActive = false
	now = time.Now()
	activation.EndedAt = &now
	s.repo.SaveActivation(ctx, activation)
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

	status := &api.SandevistanStatus{
		IsActive:              isActive,
		Phase:                 phase,
		CooldownRemaining:     cooldownRemaining,
		ActionBudgetRemaining: &actionBudgetRemaining,
		HeatStacks:            &heatStacks,
		TemporalMarksCount:    &temporalMarksCount,
	}

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

	executedActions := make([]struct {
		ActionType *string    `json:"action_type,omitempty"`
		Success    *bool      `json:"success,omitempty"`
		Timestamp  *time.Time `json:"timestamp,omitempty"`
	}, len(actions))
	for i, action := range actions {
		success := true
		now := time.Now()
		actionTypeStr := string(action.Type)
		executedActions[i] = struct {
			ActionType *string    `json:"action_type,omitempty"`
			Success    *bool      `json:"success,omitempty"`
			Timestamp  *time.Time `json:"timestamp,omitempty"`
		}{
			ActionType: &actionTypeStr,
			Success:    &success,
			Timestamp:  &now,
		}
	}

	result := &api.ActionBudgetResult{
		BudgetRemaining:  activation.ActionBudgetRemaining,
		ExecutedActions: executedActions,
	}

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
			MarkId:    openapi_types.UUID(mark.ID),
			TargetId:  openapi_types.UUID(mark.TargetID),
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

	newHeatLevel := activation.HeatStacks
	cooldownReduced := 5
	cyberpsychosisRisk := float32(activation.HeatStacks) * 0.1

	if err := s.repo.SaveActivation(ctx, activation); err != nil {
		return nil, err
	}

	result := &api.CoolingResult{
		HeatStacksRemoved:  heatStacksRemoved,
		NewHeatLevel:       newHeatLevel,
		CooldownReduced:    &cooldownReduced,
		CyberpsychosisRisk: &cyberpsychosisRisk,
	}

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

	isOverstress := currentStacks >= OverstressThreshold
	cyberpsychosisRisk := float32(currentStacks) * 0.1

	status := &api.HeatStatus{
		CurrentStacks:     currentStacks,
		MaxStacks:         MaxHeatStacks,
		IsOverstress:      isOverstress,
		CyberpsychosisRisk: &cyberpsychosisRisk,
	}

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

	result := &api.CounterplayResult{
		SandevistanInterrupted: sandevistanInterrupted,
		EffectApplied:          effectApplied,
		PhaseEnded:             &phaseEnded,
		ActionBudgetReduced:    &actionBudgetReduced,
	}

	return result, nil
}

