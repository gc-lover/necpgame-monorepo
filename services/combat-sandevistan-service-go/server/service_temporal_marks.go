// Issue: #39, #1607 - Sandevistan temporal marks operations
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sandevistan-service-go/pkg/api"
	"github.com/google/uuid"
)

// SetTemporalMarks sets temporal marks on target players
func (s *sandevistanService) SetTemporalMarks(ctx context.Context, playerID uuid.UUID, targetIDs []uuid.UUID) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	if len(targetIDs) > MaxTemporalMarks {
		return errors.New("too many temporal marks")
	}

	// Check if player has active sandevistan
	status, err := s.repo.GetSandevistanStatus(ctx, playerID)
	if err != nil {
		return err
	}

	if status == nil || status.Phase != PhaseActive {
		return errors.New("sandevistan not active")
	}

	// Clear existing marks
	if err := s.repo.ClearTemporalMarks(ctx, playerID); err != nil {
		return err
	}

	// Set new marks
	for _, targetID := range targetIDs {
		mark := &api.TemporalMark{
			TargetPlayerID: targetID,
			SetAt:          time.Now(),
			ExpiresAt:      time.Now().Add(ActiveDuration),
		}

		if err := s.repo.SetTemporalMark(ctx, playerID, targetID, mark.SetAt, mark.ExpiresAt); err != nil {
			return err
		}
	}

	return nil
}

// GetTemporalMarks retrieves temporal marks for a player
func (s *sandevistanService) GetTemporalMarks(ctx context.Context, playerID uuid.UUID) ([]api.TemporalMark, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	marks, err := s.repo.GetTemporalMarks(ctx, playerID)
	if err != nil {
		return nil, err
	}

	var result []api.TemporalMark
	for _, mark := range marks {
		result = append(result, api.TemporalMark{
			TargetPlayerID: mark.TargetPlayerID,
			SetAt:          mark.SetAt,
			ExpiresAt:      mark.ExpiresAt,
		})
	}

	return result, nil
}

// ApplyTemporalMarks applies effects to marked targets
func (s *sandevistanService) ApplyTemporalMarks(ctx context.Context, playerID uuid.UUID) (*api.TemporalMarksApplied, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	marks, err := s.repo.GetTemporalMarks(ctx, playerID)
	if err != nil {
		return nil, err
	}

	result := &api.TemporalMarksApplied{
		PlayerID:        playerID,
		MarksApplied:    len(marks),
		TargetsAffected: make([]uuid.UUID, 0, len(marks)),
	}

	for _, mark := range marks {
		if time.Now().Before(mark.ExpiresAt) {
			result.TargetsAffected = append(result.TargetsAffected, mark.TargetPlayerID)
		}
	}

	return result, nil
}
