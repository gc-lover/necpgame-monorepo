// Issue: #39, #1607 - Sandevistan action budget operations
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sandevistan-service-go/pkg/api"
	"github.com/google/uuid"
)

// UseActionBudget consumes action budget for player actions
func (s *sandevistanService) UseActionBudget(ctx context.Context, playerID uuid.UUID, actions []api.Action) (*api.ActionBudgetResult, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	status, err := s.repo.GetSandevistanStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if status == nil || status.Phase != PhaseActive {
		return nil, errors.New("sandevistan not active")
	}

	// Calculate total cost
	totalCost := 0
	for _, action := range actions {
		totalCost += action.Cost
	}

	if totalCost > status.ActionBudget {
		return &api.ActionBudgetResult{
			PlayerID:         playerID,
			ActionsProcessed: 0,
			RemainingBudget:  status.ActionBudget,
			Success:          false,
			Error:            "insufficient action budget",
		}, nil
	}

	// Check per-tick limit
	if len(actions) > MaxActionsPerTick {
		return &api.ActionBudgetResult{
			PlayerID:         playerID,
			ActionsProcessed: 0,
			RemainingBudget:  status.ActionBudget,
			Success:          false,
			Error:            "too many actions per tick",
		}, nil
	}

	// Update budget
	newBudget := status.ActionBudget - totalCost
	if err := s.repo.UpdateActionBudget(ctx, playerID, newBudget); err != nil {
		return nil, err
	}

	return &api.ActionBudgetResult{
		PlayerID:         playerID,
		ActionsProcessed: len(actions),
		RemainingBudget:  newBudget,
		Success:          true,
	}, nil
}
