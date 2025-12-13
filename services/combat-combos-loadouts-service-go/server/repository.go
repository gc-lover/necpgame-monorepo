// Combat Combos Loadouts Service Repository
// Issue: #141890005

package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
)

// Repository handles database operations for combo loadouts
type Repository struct {
	db *pgxpool.Pool
}

// NewRepository creates a new repository instance
func NewRepository(db *pgxpool.Pool) *Repository {
	return &Repository{db: db}
}

// GetComboLoadout retrieves a character's combo loadout
func (r *Repository) GetComboLoadout(ctx context.Context, characterID uuid.UUID) (*ComboLoadout, error) {
	query := `
		SELECT id, character_id, active_combos, preferences, created_at, updated_at
		FROM gameplay.combo_loadouts
		WHERE character_id = $1
	`

	var loadout ComboLoadout
	var activeCombosJSON []byte
	var preferencesJSON []byte

	err := r.db.QueryRow(ctx, query, characterID).Scan(
		&loadout.ID,
		&loadout.CharacterID,
		&activeCombosJSON,
		&preferencesJSON,
		&loadout.CreatedAt,
		&loadout.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get combo loadout: %w", err)
	}

	// Unmarshal JSON arrays/objects
	if err := json.Unmarshal(activeCombosJSON, &loadout.ActiveCombos); err != nil {
		return nil, fmt.Errorf("failed to unmarshal active_combos: %w", err)
	}

	if err := json.Unmarshal(preferencesJSON, &loadout.Preferences); err != nil {
		return nil, fmt.Errorf("failed to unmarshal preferences: %w", err)
	}

	return &loadout, nil
}

// UpdateComboLoadout updates or creates a character's combo loadout
func (r *Repository) UpdateComboLoadout(ctx context.Context, req *UpdateLoadoutRequest) (*ComboLoadout, error) {
	// Marshal JSON data
	activeCombosJSON, err := json.Marshal(req.ActiveCombos)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal active_combos: %w", err)
	}

	preferencesJSON, err := json.Marshal(req.Preferences)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal preferences: %w", err)
	}

	query := `
		INSERT INTO gameplay.combo_loadouts (
			id, character_id, active_combos, preferences, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6
		) ON CONFLICT (character_id) DO UPDATE SET
			active_combos = EXCLUDED.active_combos,
			preferences = EXCLUDED.preferences,
			updated_at = EXCLUDED.updated_at
		RETURNING id, character_id, active_combos, preferences, created_at, updated_at
	`

	now := time.Now()
	loadoutID := uuid.New()

	var loadout ComboLoadout
	var activeCombosResult []byte
	var preferencesResult []byte

	err = r.db.QueryRow(ctx, query,
		loadoutID,
		req.CharacterID,
		activeCombosJSON,
		preferencesJSON,
		now,
		now,
	).Scan(
		&loadout.ID,
		&loadout.CharacterID,
		&activeCombosResult,
		&preferencesResult,
		&loadout.CreatedAt,
		&loadout.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to update combo loadout: %w", err)
	}

	// Unmarshal result JSON
	if err := json.Unmarshal(activeCombosResult, &loadout.ActiveCombos); err != nil {
		return nil, fmt.Errorf("failed to unmarshal result active_combos: %w", err)
	}

	if err := json.Unmarshal(preferencesResult, &loadout.Preferences); err != nil {
		return nil, fmt.Errorf("failed to unmarshal result preferences: %w", err)
	}

	return &loadout, nil
}

// DeleteComboLoadout removes a character's combo loadout
func (r *Repository) DeleteComboLoadout(ctx context.Context, characterID uuid.UUID) error {
	query := `
		DELETE FROM gameplay.combo_loadouts
		WHERE character_id = $1
	`

	result, err := r.db.Exec(ctx, query, characterID)
	if err != nil {
		return fmt.Errorf("failed to delete combo loadout: %w", err)
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("combo loadout not found for character %s", characterID)
	}

	return nil
}

// ListComboLoadouts retrieves paginated list of combo loadouts (admin function)
func (r *Repository) ListComboLoadouts(ctx context.Context, limit, offset int) ([]ComboLoadout, int, error) {
	// Get total count
	countQuery := `SELECT COUNT(*) FROM gameplay.combo_loadouts`
	var total int
	if err := r.db.QueryRow(ctx, countQuery).Scan(&total); err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	// Get paginated results
	query := `
		SELECT id, character_id, active_combos, preferences, created_at, updated_at
		FROM gameplay.combo_loadouts
		ORDER BY updated_at DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list combo loadouts: %w", err)
	}
	defer rows.Close()

	var loadouts []ComboLoadout
	for rows.Next() {
		var loadout ComboLoadout
		var activeCombosJSON []byte
		var preferencesJSON []byte

		err := rows.Scan(
			&loadout.ID,
			&loadout.CharacterID,
			&activeCombosJSON,
			&preferencesJSON,
			&loadout.CreatedAt,
			&loadout.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan combo loadout: %w", err)
		}

		// Unmarshal JSON data
		if err := json.Unmarshal(activeCombosJSON, &loadout.ActiveCombos); err != nil {
			return nil, 0, fmt.Errorf("failed to unmarshal active_combos: %w", err)
		}

		if err := json.Unmarshal(preferencesJSON, &loadout.Preferences); err != nil {
			return nil, 0, fmt.Errorf("failed to unmarshal preferences: %w", err)
		}

		loadouts = append(loadouts, loadout)
	}

	if err := rows.Err(); err != nil {
		return nil, 0, fmt.Errorf("error iterating combo loadouts: %w", err)
	}

	return loadouts, total, nil
}
