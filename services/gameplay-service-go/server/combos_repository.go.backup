// Issue: #1525
package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
)

type ComboRepositoryInterface interface {
	GetLoadout(ctx context.Context, characterID uuid.UUID) (*models.ComboLoadout, error)
	SaveLoadout(ctx context.Context, loadout *models.ComboLoadout) (*models.ComboLoadout, error)
	GetActivation(ctx context.Context, activationID uuid.UUID) (*models.ComboActivation, error)
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
		 FROM combo_loadouts WHERE character_id = $1`,
		characterID).Scan(
		&loadout.ID, &loadout.CharacterID, &loadout.ActiveCombos, &preferencesJSON,
		&loadout.CreatedAt, &loadout.UpdatedAt)

	if err != nil {
		return nil, errors.New("loadout not found")
	}

	return &loadout, nil
}

func (r *ComboRepository) SaveLoadout(ctx context.Context, loadout *models.ComboLoadout) (*models.ComboLoadout, error) {
	_, err := r.db.Exec(ctx,
		`INSERT INTO combo_loadouts (id, character_id, active_combos, preferences, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6)
		 ON CONFLICT (character_id) DO UPDATE SET
		 active_combos = $3, preferences = $4, updated_at = $6`,
		loadout.ID, loadout.CharacterID, loadout.ActiveCombos, loadout.Preferences,
		loadout.CreatedAt, loadout.UpdatedAt)

	if err != nil {
		return nil, err
	}

	return loadout, nil
}

func (r *ComboRepository) GetActivation(ctx context.Context, activationID uuid.UUID) (*models.ComboActivation, error) {
	var activation models.ComboActivation

	err := r.db.QueryRow(ctx,
		`SELECT id, combo_id, character_id, activated_at FROM combo_activations WHERE id = $1`,
		activationID).Scan(
		&activation.ID, &activation.ComboID, &activation.CharacterID, &activation.ActivatedAt)

	if err != nil {
		return nil, errors.New("activation not found")
	}

	return &activation, nil
}

func (r *ComboRepository) SaveScore(ctx context.Context, score *models.ComboScore) error {
	_, err := r.db.Exec(ctx,
		`INSERT INTO combo_scores (activation_id, execution_difficulty, damage_output, visual_impact, team_coordination, total_score, category, timestamp)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`,
		score.ActivationID, score.ExecutionDifficulty, score.DamageOutput,
		score.VisualImpact, score.TeamCoordination, score.TotalScore, score.Category, score.Timestamp)

	return err
}

func (r *ComboRepository) GetAnalytics(ctx context.Context, comboID, characterID *uuid.UUID, periodStart, periodEnd *time.Time, limit, offset int) ([]models.ComboAnalytics, error) {
	query := `SELECT combo_id, COUNT(*) as total_activations, 
			  AVG(CASE WHEN success THEN 1.0 ELSE 0.0 END) as success_rate,
			  AVG(total_score) as average_score,
			  MODE() WITHIN GROUP (ORDER BY category) as average_category,
			  0 as chain_combo_count
			  FROM combo_scores
			  WHERE timestamp BETWEEN $1 AND $2`

	args := []interface{}{periodStart, periodEnd}
	argIndex := 3

	if comboID != nil {
		query += ` AND combo_id = $` + string(rune(argIndex))
		args = append(args, *comboID)
		argIndex++
	}

	if characterID != nil {
		query += ` AND character_id = $` + string(rune(argIndex))
		args = append(args, *characterID)
		argIndex++
	}

	query += ` GROUP BY combo_id LIMIT $` + string(rune(argIndex)) + ` OFFSET $` + string(rune(argIndex+1))
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

