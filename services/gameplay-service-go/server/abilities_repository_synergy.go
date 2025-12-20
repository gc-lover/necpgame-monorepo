// Package server Issue: #156 - Abilities synergy operations
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
)

// GetAvailableSynergies retrieves synergies available for a character
func (r *AbilityRepository) GetAvailableSynergies(ctx context.Context, characterID uuid.UUID, abilityID *uuid.UUID) ([]api.Synergy, error) {
	query := `SELECT s.id, s.name, s.description, s.ability_ids, s.bonuses, s.requirements, s.created_at
			  FROM gameplay.synergies s
			  WHERE s.id NOT IN (
			  	SELECT synergy_id FROM gameplay.character_synergies
			  	WHERE character_id = $1
			  )`

	args := []interface{}{characterID}

	if abilityID != nil {
		query += ` AND $2 = ANY(s.ability_ids)`
		args = append(args, *abilityID)
	}

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var synergies []api.Synergy
	for rows.Next() {
		var synergy api.Synergy
		var abilityIDs []uuid.UUID
		var bonuses, requirements []byte
		var createdAt time.Time

		err := rows.Scan(&synergy.ID, &synergy.Name, &synergy.Description,
			&abilityIDs, &bonuses, &requirements, &createdAt)
		if err != nil {
			return nil, err
		}

		if err := json.Unmarshal(bonuses, &synergy.Bonuses); err != nil {
			continue
		}
		if err := json.Unmarshal(requirements, &synergy.Requirements); err != nil {
			continue
		}

		synergies = append(synergies, synergy)
	}

	return synergies, nil
}

// GetSynergy retrieves a single synergy by ID
func (r *AbilityRepository) GetSynergy(ctx context.Context, synergyID uuid.UUID) (*api.Synergy, error) {
	query := `SELECT id, name, description, ability_ids, bonuses, requirements, created_at
			  FROM gameplay.synergies WHERE id = $1`

	var synergy api.Synergy
	var abilityIDs []uuid.UUID
	var bonuses, requirements []byte
	var createdAt time.Time

	err := r.db.QueryRow(ctx, query, synergyID).Scan(
		&synergy.ID, &synergy.Name, &synergy.Description,
		&abilityIDs, &bonuses, &requirements, &createdAt,
	)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, nil
		}
		return nil, err
	}

	if err := json.Unmarshal(bonuses, &synergy.Bonuses); err != nil {
		return nil, err
	}
	if err := json.Unmarshal(requirements, &synergy.Requirements); err != nil {
		return nil, err
	}

	return &synergy, nil
}

// CheckSynergyRequirements checks if a character meets synergy requirements
func (r *AbilityRepository) CheckSynergyRequirements(ctx context.Context, characterID uuid.UUID, synergy *api.Synergy) (bool, error) {
	// Check if character has all required abilities
	query := `SELECT COUNT(*) FROM gameplay.ability_loadouts al
			  WHERE al.character_id = $1
			  AND al.primary_ability_id = ANY($2)
			  AND al.secondary_ability_id = ANY($2)
			  AND al.tertiary_ability_id = ANY($2)
			  AND al.quaternary_ability_id = ANY($2)`

	var count int
	err := r.db.QueryRow(ctx, query, characterID, synergy.Requirements.AbilityIDs).Scan(&count)
	if err != nil {
		return false, err
	}

	// Additional requirement checks would go here
	return count > 0, nil
}

// ApplySynergy applies a synergy to a character
func (r *AbilityRepository) ApplySynergy(ctx context.Context, characterID, synergyID uuid.UUID, _ *api.Synergy) error {
	query := `INSERT INTO gameplay.character_synergies
			  (character_id, synergy_id, applied_at)
			  VALUES ($1, $2, $3)
			  ON CONFLICT (character_id, synergy_id) DO NOTHING`

	_, err := r.db.Exec(ctx, query, characterID, synergyID, time.Now())
	return err
}
