// Package server Issue: #156 - Abilities loadout operations
package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
)

// GetLoadout retrieves the current ability loadout for a character
func (r *AbilityRepository) GetLoadout(ctx context.Context, characterID uuid.UUID) (*api.AbilityLoadout, error) {
	query := `SELECT character_id, primary_ability_id, secondary_ability_id,
			  tertiary_ability_id, quaternary_ability_id, updated_at
			  FROM gameplay.ability_loadouts WHERE character_id = $1`

	var loadout api.AbilityLoadout
	var primaryID, secondaryID, tertiaryID, quaternaryID uuid.UUID
	var updatedAt time.Time

	err := r.db.QueryRow(ctx, query, characterID).Scan(
		&loadout.CharacterID, &primaryID, &secondaryID, &tertiaryID, &quaternaryID, &updatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			// Return default loadout
			loadout.CharacterID = characterID
			loadout.UpdatedAt = time.Now()
			return &loadout, nil
		}
		return nil, err
	}

	loadout.PrimaryAbilityID = api.NewOptUUID(primaryID)
	loadout.SecondaryAbilityID = api.NewOptUUID(secondaryID)
	loadout.TertiaryAbilityID = api.NewOptUUID(tertiaryID)
	loadout.QuaternaryAbilityID = api.NewOptUUID(quaternaryID)
	loadout.UpdatedAt = updatedAt

	return &loadout, nil
}

// SaveLoadout saves the ability loadout for a character
func (r *AbilityRepository) SaveLoadout(ctx context.Context, loadout *api.AbilityLoadout) (*api.AbilityLoadout, error) {
	query := `INSERT INTO gameplay.ability_loadouts
			  (character_id, primary_ability_id, secondary_ability_id,
			   tertiary_ability_id, quaternary_ability_id, updated_at)
			  VALUES ($1, $2, $3, $4, $5, $6)
			  ON CONFLICT (character_id) DO UPDATE SET
			  	primary_ability_id = EXCLUDED.primary_ability_id,
			  	secondary_ability_id = EXCLUDED.secondary_ability_id,
			  	tertiary_ability_id = EXCLUDED.tertiary_ability_id,
			  	quaternary_ability_id = EXCLUDED.quaternary_ability_id,
			  	updated_at = EXCLUDED.updated_at`

	now := time.Now()
	loadout.UpdatedAt = now

	args := []interface{}{
		loadout.CharacterID,
		loadout.PrimaryAbilityID.Value, loadout.SecondaryAbilityID.Value,
		loadout.TertiaryAbilityID.Value, loadout.QuaternaryAbilityID.Value,
		now,
	}

	_, err := r.db.Exec(ctx, query, args...)
	if err != nil {
		return nil, err
	}

	return loadout, nil
}
