// Issue: #39, #1607 - Sandevistan counterplay operations
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sandevistan-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// ApplyCounterplay applies counterplay effects to disrupt Sandevistan
func (s *sandevistanService) ApplyCounterplay(ctx context.Context, playerID uuid.UUID, effectType string, sourcePlayerID uuid.UUID) (*api.CounterplayResult, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	status, err := s.repo.GetSandevistanStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	result := &api.CounterplayResult{
		PlayerID:       playerID,
		SourcePlayerID: sourcePlayerID,
		EffectType:     effectType,
		Success:        false,
	}

	if status == nil || status.Phase == PhaseIdle {
		result.Message = "sandevistan not active, counterplay ineffective"
		return result, nil
	}

	// Apply different counterplay effects
	switch effectType {
	case "overstress":
		// Force early deactivation
		if err := s.Deactivate(ctx, playerID); err != nil {
			return nil, err
		}
		result.Success = true
		result.Message = "overstress applied, sandevistan deactivated"

	case "temporal_disruption":
		// Reduce action budget
		newBudget := status.ActionBudget / 2
		if err := s.repo.UpdateActionBudget(ctx, playerID, newBudget); err != nil {
			return nil, err
		}
		result.Success = true
		result.Message = "temporal disruption applied, action budget reduced"

	default:
		result.Message = "unknown counterplay effect"
	}

	return result, nil
}

// PublishPerceptionDragEvent publishes a perception drag event
func (s *sandevistanService) PublishPerceptionDragEvent(ctx context.Context, playerID uuid.UUID, event *api.PerceptionDragEvent) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// This would publish to a message queue or event system
	// For now, just log the event
	s.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"event":     event,
	}).Info("Perception drag event published")

	return nil
}
