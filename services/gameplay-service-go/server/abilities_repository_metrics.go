// Package server Issue: #156 - Abilities metrics operations
package server

import (
	"context"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
)

// GetAbilityMetrics retrieves usage metrics for abilities
func (r *AbilityRepository) GetAbilityMetrics(ctx context.Context, characterID uuid.UUID, abilityID api.OptUUID, periodStart api.OptDateTime, periodEnd api.OptDateTime) (*api.AbilityMetrics, error) {
	var metrics api.AbilityMetrics
	metrics.CharacterID = characterID

	// Build base query
	baseQuery := `FROM gameplay.ability_activations WHERE character_id = $1`
	args := []interface{}{characterID}
	argIndex := 2

	if abilityID.Set {
		baseQuery += ` AND ability_id = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, abilityID.Value)
		argIndex++
	}

	if periodStart.Set {
		baseQuery += ` AND activated_at >= $` + fmt.Sprintf("%d", argIndex)
		args = append(args, periodStart.Value)
		argIndex++
	}

	if periodEnd.Set {
		baseQuery += ` AND activated_at <= $` + fmt.Sprintf("%d", argIndex)
		args = append(args, periodEnd.Value)
		argIndex++
	}

	// Get total activations
	countQuery := `SELECT COUNT(*) ` + baseQuery
	err := r.db.QueryRow(ctx, countQuery, args...).Scan(&metrics.TotalActivations)
	if err != nil {
		return nil, err
	}

	// Get unique abilities used
	uniqueQuery := `SELECT COUNT(DISTINCT ability_id) ` + baseQuery
	err = r.db.QueryRow(ctx, uniqueQuery, args...).Scan(&metrics.UniqueAbilitiesUsed)
	if err != nil {
		return nil, err
	}

	// Get most used ability
	mostUsedQuery := `SELECT ability_id, COUNT(*) as count ` + baseQuery +
		` GROUP BY ability_id ORDER BY count DESC LIMIT 1`
	var mostUsedID uuid.UUID
	var mostUsedCount int
	err = r.db.QueryRow(ctx, mostUsedQuery, args...).Scan(&mostUsedID, &mostUsedCount)
	if err == nil {
		metrics.MostUsedAbilityID = api.NewOptUUID(mostUsedID)
		metrics.MostUsedAbilityCount = mostUsedCount
	}

	// Get first activation
	firstQuery := `SELECT MIN(activated_at) ` + baseQuery
	var firstActivation time.Time
	err = r.db.QueryRow(ctx, firstQuery, args...).Scan(&firstActivation)
	if err == nil {
		metrics.FirstActivation = firstActivation
	}

	// Get last activation
	lastQuery := `SELECT MAX(activated_at) ` + baseQuery
	var lastActivation time.Time
	err = r.db.QueryRow(ctx, lastQuery, args...).Scan(&lastActivation)
	if err == nil {
		metrics.LastActivation = lastActivation
	}

	return &metrics, nil
}
