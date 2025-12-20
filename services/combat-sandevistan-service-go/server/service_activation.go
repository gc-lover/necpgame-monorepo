// Package server Issue: #39, #1607 - Sandevistan activation operations
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sandevistan-service-go/pkg/api"
	"github.com/google/uuid"
)

// Activate activates Sandevistan for a player
func (s *sandevistanService) Activate(ctx context.Context, playerID uuid.UUID) (*api.SandevistanActivation, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Check if already active
	status, err := s.repo.GetSandevistanStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if status != nil && status.Phase != PhaseIdle {
		return nil, errors.New("sandevistan already active")
	}

	// Check heat status
	heatStatus, err := s.repo.GetHeatStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if heatStatus != nil && heatStatus.HeatStacks >= MaxHeatStacks {
		return nil, errors.New("heat stacks too high, cannot activate")
	}

	// Create activation
	activation := &api.SandevistanActivation{
		PlayerID:          playerID,
		ActivatedAt:       time.Now(),
		PreparationEndsAt: time.Now().Add(PreparationDuration),
		ActiveEndsAt:      time.Now().Add(PreparationDuration + ActiveDuration),
		RecoveryEndsAt:    time.Now().Add(PreparationDuration + ActiveDuration + RecoveryDuration),
		ActionBudget:      MaxActionBudget,
		Phase:             PhasePreparation,
	}

	// Save to repository
	if err := s.repo.SaveSandevistanStatus(ctx, playerID, activation.Phase, activation.ActionBudget,
		activation.ActivatedAt, activation.PreparationEndsAt, activation.ActiveEndsAt, activation.RecoveryEndsAt); err != nil {
		return nil, err
	}

	// Add heat stack
	if err := s.repo.AddHeatStack(ctx, playerID); err != nil {
		s.logger.WithError(err).Error("Failed to add heat stack on activation")
	}

	return activation, nil
}

// Deactivate deactivates Sandevistan for a player
func (s *sandevistanService) Deactivate(ctx context.Context, playerID uuid.UUID) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	status, err := s.repo.GetSandevistanStatus(ctx, playerID)
	if err != nil {
		return err
	}

	if status == nil || status.Phase == PhaseIdle {
		return errors.New("sandevistan not active")
	}

	// Update to idle phase
	now := time.Now()
	if err := s.repo.SaveSandevistanStatus(ctx, playerID, PhaseIdle, 0, now, now, now, now); err != nil {
		return err
	}

	// Clear temporal marks
	if err := s.repo.ClearTemporalMarks(ctx, playerID); err != nil {
		s.logger.WithError(err).Error("Failed to clear temporal marks on deactivation")
	}

	return nil
}
