// Issue: #156 - Abilities cooldown operations
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
)

// GetCooldowns retrieves active cooldowns for a character
func (r *AbilityRepository) GetCooldowns(ctx context.Context, characterID uuid.UUID) ([]api.CooldownStatus, error) {
	query := `SELECT ability_id, expires_at, created_at
			  FROM gameplay.ability_cooldowns
			  WHERE character_id = $1 AND expires_at > $2
			  ORDER BY expires_at ASC`

	rows, err := r.db.Query(ctx, query, characterID, time.Now())
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var cooldowns []api.CooldownStatus
	for rows.Next() {
		var cooldown api.CooldownStatus
		var abilityID uuid.UUID
		var expiresAt, createdAt time.Time

		err := rows.Scan(&abilityID, &expiresAt, &createdAt)
		if err != nil {
			return nil, err
		}

		cooldown.AbilityID = abilityID
		cooldown.ExpiresAt = expiresAt
		cooldown.CreatedAt = createdAt
		cooldowns = append(cooldowns, cooldown)
	}

	return cooldowns, nil
}

// StartCooldown starts a cooldown for an ability
func (r *AbilityRepository) StartCooldown(ctx context.Context, characterID, abilityID uuid.UUID, duration time.Duration) error {
	expiresAt := time.Now().Add(duration)

	query := `INSERT INTO gameplay.ability_cooldowns
			  (character_id, ability_id, expires_at, created_at)
			  VALUES ($1, $2, $3, $4)
			  ON CONFLICT (character_id, ability_id) DO UPDATE SET
			  	expires_at = GREATEST(EXCLUDED.expires_at, gameplay.ability_cooldowns.expires_at),
			  	created_at = EXCLUDED.created_at`

	_, err := r.db.Exec(ctx, query, characterID, abilityID, expiresAt, time.Now())
	return err
}

// RecordActivation records an ability activation
func (r *AbilityRepository) RecordActivation(ctx context.Context, characterID, abilityID uuid.UUID, targetID *uuid.UUID) error {
	query := `INSERT INTO gameplay.ability_activations
			  (character_id, ability_id, target_id, activated_at)
			  VALUES ($1, $2, $3, $4)`

	_, err := r.db.Exec(ctx, query, characterID, abilityID, targetID, time.Now())
	return err
}
