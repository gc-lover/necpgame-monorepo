// Issue: #1525
package server

import (
	"context"
	"encoding/json"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/models"
)

type ComboRepositoryInterface interface {
	GetLoadout(ctx context.Context, characterID uuid.UUID) (*models.ComboLoadout, error)
	SaveLoadout(ctx context.Context, loadout *models.ComboLoadout) (*models.ComboLoadout, error)
	GetActivation(ctx context.Context, activationID uuid.UUID) (*models.ComboActivation, error)
	SaveActivation(ctx context.Context, activation *models.ComboActivation) (*models.ComboActivation, error)
	SaveScore(ctx context.Context, score *models.ComboScore) error
	GetAnalytics(ctx context.Context, comboID, characterID *uuid.UUID, periodStart, periodEnd *time.Time, limit, offset int) ([]models.ComboAnalytics, error)
}

type ComboRepository struct {
	db *pgxpool.Pool
}

func NewComboRepository(db *pgxpool.Pool) *ComboRepository {
	return &ComboRepository{db: db}
}

func (r *ComboRepository) GetLoadout(ctx context.Context, characterID uuid.UUID) (*models.ComboLoadout, error) {
	var loadout models.ComboLoadout
	var preferencesJSON []byte

	err := r.db.QueryRow(ctx,
		`SELECT id, character_id, active_combos, preferences, created_at, updated_at
		 FROM gameplay.combo_loadouts WHERE character_id = $1`,
		characterID).Scan(
		&loadout.ID, &loadout.CharacterID, &loadout.ActiveCombos, &preferencesJSON,
		&loadout.CreatedAt, &loadout.UpdatedAt)

	if err != nil {
		return nil, errors.New("loadout not found")
	}

	if len(preferencesJSON) > 0 {
		if err := json.Unmarshal(preferencesJSON, &loadout.Preferences); err != nil {
			return nil, err
		}
	}

	return &loadout, nil
}

func (r *ComboRepository) SaveLoadout(ctx context.Context, loadout *models.ComboLoadout) (*models.ComboLoadout, error) {
	var preferencesJSON []byte
	var err error
	if loadout.Preferences != nil {
		preferencesJSON, err = json.Marshal(loadout.Preferences)
		if err != nil {
			return nil, err
		}
	}

	_, err = r.db.Exec(ctx,
		`INSERT INTO gameplay.combo_loadouts (id, character_id, active_combos, preferences, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6)
		 ON CONFLICT (character_id) DO UPDATE SET
		 active_combos = $3, preferences = $4, updated_at = $6`,
		loadout.ID, loadout.CharacterID, loadout.ActiveCombos, preferencesJSON,
		loadout.CreatedAt, loadout.UpdatedAt)

	if err != nil {
		return nil, err
	}

	return loadout, nil
}

func (r *ComboRepository) GetActivation(ctx context.Context, activationID uuid.UUID) (*models.ComboActivation, error) {
	var activation models.ComboActivation

	err := r.db.QueryRow(ctx,
		`SELECT id, combo_id, character_id, activated_at FROM gameplay.combo_activations WHERE id = $1`,
		activationID).Scan(
		&activation.ID, &activation.ComboID, &activation.CharacterID, &activation.ActivatedAt)

	if err != nil {
		return nil, errors.New("activation not found")
	}

	return &activation, nil
}

func (r *ComboRepository) SaveActivation(ctx context.Context, activation *models.ComboActivation) (*models.ComboActivation, error) {
	_, err := r.db.Exec(ctx,
		`INSERT INTO gameplay.combo_activations (id, combo_id, character_id, activated_at)
		 VALUES ($1, $2, $3, $4)`,
		activation.ID, activation.ComboID, activation.CharacterID, activation.ActivatedAt)

	if err != nil {
		return nil, err
	}

	return activation, nil
}

func (r *ComboRepository) SaveScore(ctx context.Context, score *models.ComboScore) error {
	_, err := r.db.Exec(ctx,
		`INSERT INTO gameplay.combo_scores (activation_id, execution_difficulty, damage_output, visual_impact, team_coordination, total_score, category, timestamp)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`,
		score.ActivationID, score.ExecutionDifficulty, score.DamageOutput,
		score.VisualImpact, score.TeamCoordination, score.TotalScore, score.Category, score.Timestamp)

	return err
}

func (r *ComboRepository) GetAnalytics(ctx context.Context, comboID, characterID *uuid.UUID, periodStart, periodEnd *time.Time, limit, offset int) ([]models.ComboAnalytics, error) {
	query := `SELECT ca.combo_id, COUNT(*) as total_activations, 
			  AVG(CASE WHEN cs.total_score > 0 THEN 1.0 ELSE 0.0 END) as success_rate,
			  AVG(cs.total_score) as average_score,
			  MODE() WITHIN GROUP (ORDER BY cs.category) as average_category,
			  0 as chain_combo_count
			  FROM gameplay.combo_scores cs
			  JOIN gameplay.combo_activations ca ON cs.activation_id = ca.id
			  WHERE cs.timestamp BETWEEN $1 AND $2`

	args := []interface{}{periodStart, periodEnd}
	argIndex := 3

	if comboID != nil {
		query += ` AND ca.combo_id = $` + string(rune('0'+argIndex))
		args = append(args, *comboID)
		argIndex++
	}

	if characterID != nil {
		query += ` AND ca.character_id = $` + string(rune('0'+argIndex))
		args = append(args, *characterID)
		argIndex++
	}

	query += ` GROUP BY ca.combo_id LIMIT $` + string(rune('0'+argIndex)) + ` OFFSET $` + string(rune('0'+argIndex+1))
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var analytics []models.ComboAnalytics
	for rows.Next() {
		var a models.ComboAnalytics
		err := rows.Scan(&a.ComboID, &a.TotalActivations, &a.SuccessRate, &a.AverageScore, &a.AverageCategory, &a.ChainComboCount)
		if err != nil {
			return nil, err
		}
		analytics = append(analytics, a)
	}

	return analytics, nil
}

