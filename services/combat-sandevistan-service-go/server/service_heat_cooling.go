// Package server Issue: #39, #1607 - Sandevistan heat and cooling operations
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sandevistan-service-go/pkg/api"
	"github.com/google/uuid"
)

// ApplyCooling applies cooling effect using a cartridge
func (s *sandevistanService) ApplyCooling(ctx context.Context, playerID uuid.UUID, _ uuid.UUID) (*api.CoolingResult, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	heatStatus, err := s.repo.GetHeatStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if heatStatus == nil || heatStatus.HeatStacks == 0 {
		return &api.CoolingResult{
			PlayerID:      playerID,
			Cooled:        false,
			RemainingHeat: 0,
			Message:       "no heat to cool",
		}, nil
	}

	// Apply cooling (remove one heat stack)
	if err := s.repo.RemoveHeatStack(ctx, playerID); err != nil {
		return nil, err
	}

	// Consume cartridge (would integrate with inventory service)
	// For now, just mark as successful

	return &api.CoolingResult{
		PlayerID:      playerID,
		Cooled:        true,
		RemainingHeat: heatStatus.HeatStacks - 1,
		Message:       "cooling applied successfully",
	}, nil
}

// GetHeatStatus retrieves current heat status for a player
func (s *sandevistanService) GetHeatStatus(ctx context.Context, playerID uuid.UUID) (*api.HeatStatus, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	heatStatus, err := s.repo.GetHeatStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if heatStatus == nil {
		return &api.HeatStatus{
			PlayerID:   playerID,
			HeatStacks: 0,
			MaxHeat:    MaxHeatStacks,
		}, nil
	}

	return &api.HeatStatus{
		PlayerID:    heatStatus.PlayerID,
		HeatStacks:  heatStatus.HeatStacks,
		MaxHeat:     MaxHeatStacks,
		LastUpdated: heatStatus.LastUpdated,
	}, nil
}
