// Issue: #39, #1607 - Sandevistan status operations
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sandevistan-service-go/pkg/api"
	"github.com/google/uuid"
)

// GetStatus retrieves the current Sandevistan status for a player
func (s *sandevistanService) GetStatus(ctx context.Context, playerID uuid.UUID) (*api.SandevistanStatus, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	status, err := s.repo.GetSandevistanStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if status == nil {
		// Return default idle status
		now := time.Now()
		return &api.SandevistanStatus{
			PlayerID:     playerID,
			Phase:        PhaseIdle,
			ActionBudget: MaxActionBudget,
			ActivatedAt:  now,
		}, nil
	}

	return &api.SandevistanStatus{
		PlayerID:          status.PlayerID,
		Phase:             status.Phase,
		ActionBudget:      status.ActionBudget,
		ActivatedAt:       status.ActivatedAt,
		PreparationEndsAt: status.PreparationEndsAt,
		ActiveEndsAt:      status.ActiveEndsAt,
		RecoveryEndsAt:    status.RecoveryEndsAt,
	}, nil
}

// GetBonuses retrieves current Sandevistan bonuses for a player
func (s *sandevistanService) GetBonuses(ctx context.Context, playerID uuid.UUID) (*api.SandevistanBonuses, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	status, err := s.repo.GetSandevistanStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	bonuses := &api.SandevistanBonuses{
		PlayerID:          playerID,
		SpeedMultiplier:   1.0,
		ActionBonus:       0,
		CooldownReduction: 0,
	}

	if status != nil && status.Phase == PhaseActive {
		bonuses.SpeedMultiplier = 2.5
		bonuses.ActionBonus = 2
		bonuses.CooldownReduction = 0.5
	}

	return bonuses, nil
}
