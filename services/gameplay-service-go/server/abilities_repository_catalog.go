// Issue: #156 - Abilities catalog operations
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
)

// GetCatalog retrieves abilities from the catalog with filtering
func (r *AbilityRepository) GetCatalog(ctx context.Context, abilityType *api.AbilityType, slot *api.AbilitySlot, source *api.AbilitySource, limit, offset int) ([]api.Ability, int, error) {
	query := `SELECT id, name, description, ability_type, slot, source, rank,
			  energy_cost, health_cost, cooldown_base, cyberpsychosis_impact,
			  requirements, modifiers, created_at
			  FROM gameplay.abilities_catalog WHERE 1=1`

	args := []interface{}{}
	argIndex := 1

	if abilityType != nil {
		query += ` AND ability_type = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *abilityType)
		argIndex++
	}

	if slot != nil {
		query += ` AND slot = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *slot)
		argIndex++
	}

	if source != nil {
		query += ` AND source = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *source)
		argIndex++
	}

	query += ` ORDER BY rank ASC, name ASC`
	if limit > 0 {
		query += ` LIMIT $` + fmt.Sprintf("%d", argIndex)
		args = append(args, limit)
		argIndex++
		if offset > 0 {
			query += ` OFFSET $` + fmt.Sprintf("%d", argIndex)
			args = append(args, offset)
		}
	}

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var abilities []api.Ability
	for rows.Next() {
		var ability api.Ability
		var requirements, modifiers []byte
		var createdAt time.Time

		err := rows.Scan(
			&ability.ID, &ability.Name, &ability.Description, &ability.AbilityType,
			&ability.Slot, &ability.Source, &ability.Rank, &ability.EnergyCost,
			&ability.HealthCost, &ability.CooldownBase, &ability.CyberpsychosisImpact,
			&requirements, &modifiers, &createdAt,
		)
		if err != nil {
			return nil, 0, err
		}

		// Parse JSON fields
		if err := json.Unmarshal(requirements, &ability.Requirements); err != nil {
			continue
		}
		if err := json.Unmarshal(modifiers, &ability.Modifiers); err != nil {
			continue
		}

		abilities = append(abilities, ability)
	}

	// Get total count
	countQuery := `SELECT COUNT(*) FROM gameplay.abilities_catalog WHERE 1=1`
	countArgs := []interface{}{}
	countArgIndex := 1

	if abilityType != nil {
		countQuery += ` AND ability_type = $` + fmt.Sprintf("%d", countArgIndex)
		countArgs = append(countArgs, *abilityType)
		countArgIndex++
	}

	if slot != nil {
		countQuery += ` AND slot = $` + fmt.Sprintf("%d", countArgIndex)
		countArgs = append(countArgs, *slot)
		countArgIndex++
	}

	if source != nil {
		countQuery += ` AND source = $` + fmt.Sprintf("%d", countArgIndex)
		countArgs = append(countArgs, *source)
	}

	var total int
	err = r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	return abilities, total, nil
}

// GetAbility retrieves a single ability by ID
func (r *AbilityRepository) GetAbility(ctx context.Context, abilityID uuid.UUID) (*api.Ability, error) {
	query := `SELECT id, name, description, ability_type, slot, source, rank,
			  energy_cost, health_cost, cooldown_base, cyberpsychosis_impact,
			  requirements, modifiers, created_at
			  FROM gameplay.abilities_catalog WHERE id = $1`

	var ability api.Ability
	var requirements, modifiers []byte
	var createdAt time.Time

	err := r.db.QueryRow(ctx, query, abilityID).Scan(
		&ability.ID, &ability.Name, &ability.Description, &ability.AbilityType,
		&ability.Slot, &ability.Source, &ability.Rank, &ability.EnergyCost,
		&ability.HealthCost, &ability.CooldownBase, &ability.CyberpsychosisImpact,
		&requirements, &modifiers, &createdAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil
		}
		return nil, err
	}

	// Parse JSON fields
	if err := json.Unmarshal(requirements, &ability.Requirements); err != nil {
		return nil, err
	}
	if err := json.Unmarshal(modifiers, &ability.Modifiers); err != nil {
		return nil, err
	}

	return &ability, nil
}
