// Package server Issue: #156 - Abilities cyberpsychosis operations
package server

import (
	"context"
	"database/sql"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
)

// UpdateCyberpsychosis updates cyberpsychosis level for a character
func (r *AbilityRepository) UpdateCyberpsychosis(ctx context.Context, characterID uuid.UUID, impact float32) error {
	query := `INSERT INTO gameplay.cyberpsychosis_levels
			  (character_id, current_level, last_updated)
			  VALUES ($1, $2, $3)
			  ON CONFLICT (character_id) DO UPDATE SET
			  	current_level = gameplay.cyberpsychosis_levels.current_level + EXCLUDED.current_level,
			  	last_updated = EXCLUDED.last_updated`

	_, err := r.db.Exec(ctx, query, characterID, impact, time.Now())
	return err
}

// GetCyberpsychosisState retrieves cyberpsychosis state for a character
func (r *AbilityRepository) GetCyberpsychosisState(ctx context.Context, characterID uuid.UUID) (*api.CyberpsychosisState, error) {
	query := `SELECT current_level, last_updated, created_at
			  FROM gameplay.cyberpsychosis_levels
			  WHERE character_id = $1`

	var state api.CyberpsychosisState
	var lastUpdated, createdAt time.Time

	err := r.db.QueryRow(ctx, query, characterID).Scan(
		&state.CurrentLevel, &lastUpdated, &state.CreatedAt,
	)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			// Return default state
			state.CharacterID = characterID
			state.CurrentLevel = 0
			state.CreatedAt = time.Now()
			return &state, nil
		}
		return nil, err
	}

	state.CharacterID = characterID
	state.LastUpdated = lastUpdated
	state.CreatedAt = createdAt

	return &state, nil
}
