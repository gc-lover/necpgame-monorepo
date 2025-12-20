// Package server Issue: #158
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-go/models"
)

// CombatCombosRepository handles database operations for combat combos
type CombatCombosRepository struct {
	db     *sql.DB
	logger *zap.Logger
}

// NewCombatCombosRepository creates a new repository instance
func NewCombatCombosRepository(db *sql.DB, logger *zap.Logger) *CombatCombosRepository {
	return &CombatCombosRepository{
		db:     db,
		logger: logger,
	}
}

// GetComboCatalog retrieves combos from the catalog with optional filtering
func (r *CombatCombosRepository) GetComboCatalog(ctx context.Context, comboType *models.ComboType, complexity *models.ComboComplexity, limit, offset int) ([]models.ComboCatalog, int, error) {
	baseQuery := `
		SELECT id, name, description, combo_type, complexity, requirements, sequence, bonuses, cooldown, chain_compatible, created_at
		FROM combo_catalog
		WHERE 1=1`
	var args []interface{}

	if comboType != nil {
		baseQuery += fmt.Sprintf(" AND combo_type = $%d", len(args)+1)
		args = append(args, *comboType)
	}

	if complexity != nil {
		baseQuery += fmt.Sprintf(" AND complexity = $%d", len(args)+1)
		args = append(args, *complexity)
	}

	// Safe parameterized query construction
	query := baseQuery + fmt.Sprintf(" ORDER BY created_at DESC LIMIT $%d OFFSET $%d", len(args)+1, len(args)+2)
	args = append(args, limit, offset)

	// Get total count
	countQuery := `
		SELECT COUNT(*)
		FROM combo_catalog
		WHERE 1=1`
	countArgs := args[:len(args)-2] // Remove limit and offset for count query

	var total int
	err := r.db.QueryRowContext(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		r.logger.Error("Failed to get combo catalog count", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to get combo catalog count: %w", err)
	}

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to query combo catalog", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to query combo catalog: %w", err)
	}
	defer rows.Close()

	var combos []models.ComboCatalog
	for rows.Next() {
		var combo models.ComboCatalog
		var requirementsJSON, sequenceJSON, bonusesJSON []byte

		err := rows.Scan(
			&combo.ID, &combo.Name, &combo.Description, &combo.ComboType,
			&combo.Complexity, &requirementsJSON, &sequenceJSON, &bonusesJSON,
			&combo.Cooldown, &combo.ChainCompatible, &combo.CreatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan combo catalog row", zap.Error(err))
			continue
		}

		// Unmarshal JSON fields
		if err := json.Unmarshal(requirementsJSON, &combo.Requirements); err != nil {
			r.logger.Error("Failed to unmarshal requirements", zap.Error(err))
			continue
		}
		if err := json.Unmarshal(sequenceJSON, &combo.Sequence); err != nil {
			r.logger.Error("Failed to unmarshal sequence", zap.Error(err))
			continue
		}
		if err := json.Unmarshal(bonusesJSON, &combo.Bonuses); err != nil {
			r.logger.Error("Failed to unmarshal bonuses", zap.Error(err))
			continue
		}

		combos = append(combos, combo)
	}

	return combos, total, nil
}

// GetComboByID retrieves a specific combo by ID
func (r *CombatCombosRepository) GetComboByID(ctx context.Context, comboID string) (*models.ComboCatalog, error) {
	query := `
		SELECT id, name, description, combo_type, complexity, requirements, sequence, bonuses, cooldown, chain_compatible, created_at
		FROM combo_catalog
		WHERE id = $1`

	var combo models.ComboCatalog
	var requirementsJSON, sequenceJSON, bonusesJSON []byte

	err := r.db.QueryRowContext(ctx, query, comboID).Scan(
		&combo.ID, &combo.Name, &combo.Description, &combo.ComboType,
		&combo.Complexity, &requirementsJSON, &sequenceJSON, &bonusesJSON,
		&combo.Cooldown, &combo.ChainCompatible, &combo.CreatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil
		}
		r.logger.Error("Failed to get combo by ID", zap.String("comboID", comboID), zap.Error(err))
		return nil, fmt.Errorf("failed to get combo by ID: %w", err)
	}

	// Unmarshal JSON fields
	if err := json.Unmarshal(requirementsJSON, &combo.Requirements); err != nil {
		r.logger.Error("Failed to unmarshal requirements", zap.Error(err))
		return nil, fmt.Errorf("failed to unmarshal requirements: %w", err)
	}
	if err := json.Unmarshal(sequenceJSON, &combo.Sequence); err != nil {
		r.logger.Error("Failed to unmarshal sequence", zap.Error(err))
		return nil, fmt.Errorf("failed to unmarshal sequence: %w", err)
	}
	if err := json.Unmarshal(bonusesJSON, &combo.Bonuses); err != nil {
		r.logger.Error("Failed to unmarshal bonuses", zap.Error(err))
		return nil, fmt.Errorf("failed to unmarshal bonuses: %w", err)
	}

	return &combo, nil
}

// ActivateCombo records a combo activation
func (r *CombatCombosRepository) ActivateCombo(ctx context.Context, activation *models.ComboActivation) error {
	query := `
		INSERT INTO combo_activations (id, combo_id, character_id, participants, context, success, score, activated_at, duration)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`

	activation.ID = uuid.New().String()
	activation.ActivatedAt = time.Now()

	participantsJSON, err := json.Marshal(activation.Participants)
	if err != nil {
		return fmt.Errorf("failed to marshal participants: %w", err)
	}

	contextJSON, err := json.Marshal(activation.Context)
	if err != nil {
		return fmt.Errorf("failed to marshal context: %w", err)
	}

	_, err = r.db.ExecContext(ctx, query,
		activation.ID, activation.ComboID, activation.CharacterID,
		participantsJSON, contextJSON, activation.Success,
		activation.Score, activation.ActivatedAt, activation.Duration,
	)
	if err != nil {
		r.logger.Error("Failed to activate combo", zap.Error(err))
		return fmt.Errorf("failed to activate combo: %w", err)
	}

	r.logger.Info("Combo activated",
		zap.String("activationID", activation.ID),
		zap.String("comboID", activation.ComboID),
		zap.String("characterID", activation.CharacterID))

	return nil
}

// GetComboLoadout retrieves a character's combo loadout
func (r *CombatCombosRepository) GetComboLoadout(ctx context.Context, characterID string) (*models.ComboLoadout, error) {
	query := `
		SELECT id, character_id, active_combos, preferences, auto_activate, created_at, updated_at
		FROM combo_loadouts
		WHERE character_id = $1`

	var loadout models.ComboLoadout
	var activeCombosJSON, preferencesJSON []byte

	err := r.db.QueryRowContext(ctx, query, characterID).Scan(
		&loadout.ID, &loadout.CharacterID, &activeCombosJSON,
		&preferencesJSON, &loadout.AutoActivate, &loadout.CreatedAt, &loadout.UpdatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			// Return default loadout
			return &models.ComboLoadout{
				ID:           uuid.New().String(),
				CharacterID:  characterID,
				ActiveCombos: []string{},
				Preferences:  make(map[string]interface{}),
				AutoActivate: false,
				CreatedAt:    time.Now(),
				UpdatedAt:    time.Now(),
			}, nil
		}
		r.logger.Error("Failed to get combo loadout", zap.String("characterID", characterID), zap.Error(err))
		return nil, fmt.Errorf("failed to get combo loadout: %w", err)
	}

	// Unmarshal JSON fields
	if err := json.Unmarshal(activeCombosJSON, &loadout.ActiveCombos); err != nil {
		r.logger.Error("Failed to unmarshal active combos", zap.Error(err))
		return nil, fmt.Errorf("failed to unmarshal active combos: %w", err)
	}
	if err := json.Unmarshal(preferencesJSON, &loadout.Preferences); err != nil {
		r.logger.Error("Failed to unmarshal preferences", zap.Error(err))
		return nil, fmt.Errorf("failed to unmarshal preferences: %w", err)
	}

	return &loadout, nil
}

// UpdateComboLoadout updates or creates a character's combo loadout
func (r *CombatCombosRepository) UpdateComboLoadout(ctx context.Context, loadout *models.ComboLoadout) error {
	loadout.UpdatedAt = time.Now()

	activeCombosJSON, err := json.Marshal(loadout.ActiveCombos)
	if err != nil {
		return fmt.Errorf("failed to marshal active combos: %w", err)
	}

	preferencesJSON, err := json.Marshal(loadout.Preferences)
	if err != nil {
		return fmt.Errorf("failed to marshal preferences: %w", err)
	}

	query := `
		INSERT INTO combo_loadouts (id, character_id, active_combos, preferences, auto_activate, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
		ON CONFLICT (character_id) DO UPDATE SET
			active_combos = EXCLUDED.active_combos,
			preferences = EXCLUDED.preferences,
			auto_activate = EXCLUDED.auto_activate,
			updated_at = EXCLUDED.updated_at`

	loadout.ID = uuid.New().String()
	loadout.CreatedAt = time.Now()

	_, err = r.db.ExecContext(ctx, query,
		loadout.ID, loadout.CharacterID, activeCombosJSON, preferencesJSON,
		loadout.AutoActivate, loadout.CreatedAt, loadout.UpdatedAt,
	)
	if err != nil {
		r.logger.Error("Failed to update combo loadout", zap.Error(err))
		return fmt.Errorf("failed to update combo loadout: %w", err)
	}

	return nil
}

// SaveComboScoring saves scoring metrics for a combo activation
func (r *CombatCombosRepository) SaveComboScoring(ctx context.Context, scoring *models.ComboScoring) error {
	query := `
		INSERT INTO combo_scoring_history (
			id, activation_id, execution_difficulty, damage_output, visual_impact,
			team_coordination, total_score, category, timestamp
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`

	scoring.ID = uuid.New().String()
	scoring.Timestamp = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		scoring.ID, scoring.ActivationID, scoring.ExecutionDifficulty,
		scoring.DamageOutput, scoring.VisualImpact, scoring.TeamCoordination,
		scoring.TotalScore, scoring.Category, scoring.Timestamp,
	)
	if err != nil {
		r.logger.Error("Failed to save combo scoring", zap.Error(err))
		return fmt.Errorf("failed to save combo scoring: %w", err)
	}

	return nil
}

// GetComboAnalytics retrieves analytics data
func (r *CombatCombosRepository) GetComboAnalytics(ctx context.Context, days int) (*models.ComboAnalyticsResponse, error) {
	// This is a simplified implementation - in production you'd have more complex queries
	query := `
		SELECT COUNT(*) as total_activations,
			   AVG(CASE WHEN success THEN 1 ELSE 0 END) as success_rate
		FROM combo_activations
		WHERE activated_at >= NOW() - INTERVAL '%d days'`

	query = fmt.Sprintf(query, days)

	var totalActivations int
	var successRate float64

	err := r.db.QueryRowContext(ctx, query).Scan(&totalActivations, &successRate)
	if err != nil {
		r.logger.Error("Failed to get combo analytics", zap.Error(err))
		return nil, fmt.Errorf("failed to get combo analytics: %w", err)
	}

	return &models.ComboAnalyticsResponse{
		TotalActivations: totalActivations,
		SuccessRate:      successRate,
		PopularCombos:    []models.ComboPopularity{}, // Simplified
		ScoringTrends:    []models.ScoringTrend{},    // Simplified
	}, nil
}
