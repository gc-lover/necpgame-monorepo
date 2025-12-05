// Issue: #1515
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

type AffixRepositoryInterface interface {
	GetAffix(ctx context.Context, id uuid.UUID) (*models.Affix, error)
	GetActiveRotation(ctx context.Context) (*models.AffixRotation, error)
	GetRotationHistory(ctx context.Context, weeksBack, limit, offset int) ([]models.AffixRotation, int, error)
	CreateRotation(ctx context.Context, rotation *models.AffixRotation) error
	GetInstanceAffixes(ctx context.Context, instanceID uuid.UUID) ([]models.AffixSummary, time.Time, error)
	SaveInstanceAffixes(ctx context.Context, instanceID uuid.UUID, affixes []uuid.UUID) error
}

type AffixRepository struct {
	db *pgxpool.Pool
}

func NewAffixRepository(db *pgxpool.Pool) *AffixRepository {
	return &AffixRepository{db: db}
}

func (r *AffixRepository) GetAffix(ctx context.Context, id uuid.UUID) (*models.Affix, error) {
	var affix models.Affix
	var mechanicsJSON, visualEffectsJSON []byte

	err := r.db.QueryRow(ctx,
		`SELECT id, name, category, description, mechanics, visual_effects, 
		 reward_modifier, difficulty_modifier, created_at
		 FROM gameplay.affixes WHERE id = $1`,
		id).Scan(
		&affix.ID, &affix.Name, &affix.Category, &affix.Description,
		&mechanicsJSON, &visualEffectsJSON, &affix.RewardModifier,
		&affix.DifficultyModifier, &affix.CreatedAt)

	if err != nil {
		return nil, errors.New("affix not found")
	}

	if len(mechanicsJSON) > 0 {
		if err := json.Unmarshal(mechanicsJSON, &affix.Mechanics); err != nil {
			return nil, err
		}
	}

	if len(visualEffectsJSON) > 0 {
		if err := json.Unmarshal(visualEffectsJSON, &affix.VisualEffects); err != nil {
			return nil, err
		}
	}

	return &affix, nil
}

func (r *AffixRepository) GetActiveRotation(ctx context.Context) (*models.AffixRotation, error) {
	var rotation models.AffixRotation
	var seasonalAffixID *uuid.UUID

	err := r.db.QueryRow(ctx,
		`SELECT id, week_start, week_end, seasonal_affix_id, created_at
		 FROM gameplay.affix_rotations
		 WHERE week_start <= NOW() AND week_end > NOW()
		 ORDER BY week_start DESC LIMIT 1`).Scan(
		&rotation.ID, &rotation.WeekStart, &rotation.WeekEnd,
		&seasonalAffixID, &rotation.CreatedAt)

	if err != nil {
		return nil, errors.New("no active rotation")
	}

	rows, err := r.db.Query(ctx,
		`SELECT a.id, a.name, a.category, a.description, a.reward_modifier, a.difficulty_modifier
		 FROM gameplay.affix_rotation_affixes ara
		 JOIN gameplay.affixes a ON ara.affix_id = a.id
		 WHERE ara.rotation_id = $1`,
		rotation.ID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var summary models.AffixSummary
		if err := rows.Scan(&summary.ID, &summary.Name, &summary.Category,
			&summary.Description, &summary.RewardModifier, &summary.DifficultyModifier); err != nil {
			return nil, err
		}
		rotation.ActiveAffixes = append(rotation.ActiveAffixes, summary)
	}

	if seasonalAffixID != nil {
		affix, err := r.GetAffix(ctx, *seasonalAffixID)
		if err == nil {
			rotation.SeasonalAffix = &models.AffixSummary{
				ID:                affix.ID,
				Name:              affix.Name,
				Category:          affix.Category,
				Description:       affix.Description,
				RewardModifier:    affix.RewardModifier,
				DifficultyModifier: affix.DifficultyModifier,
			}
		}
	}

	return &rotation, nil
}

func (r *AffixRepository) GetRotationHistory(ctx context.Context, weeksBack, limit, offset int) ([]models.AffixRotation, int, error) {
	weekStart := time.Now().AddDate(0, 0, -7*weeksBack)

	var total int
	err := r.db.QueryRow(ctx,
		`SELECT COUNT(*) FROM gameplay.affix_rotations WHERE week_start >= $1`,
		weekStart).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	rows, err := r.db.Query(ctx,
		`SELECT id, week_start, week_end, seasonal_affix_id, created_at
		 FROM gameplay.affix_rotations
		 WHERE week_start >= $1
		 ORDER BY week_start DESC
		 LIMIT $2 OFFSET $3`,
		weekStart, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var rotations []models.AffixRotation
	for rows.Next() {
		var rotation models.AffixRotation
		var seasonalAffixID *uuid.UUID

		if err := rows.Scan(&rotation.ID, &rotation.WeekStart, &rotation.WeekEnd,
			&seasonalAffixID, &rotation.CreatedAt); err != nil {
			return nil, 0, err
		}

		affixRows, err := r.db.Query(ctx,
			`SELECT a.id, a.name, a.category, a.description, a.reward_modifier, a.difficulty_modifier
			 FROM gameplay.affix_rotation_affixes ara
			 JOIN gameplay.affixes a ON ara.affix_id = a.id
			 WHERE ara.rotation_id = $1`,
			rotation.ID)
		if err != nil {
			return nil, 0, err
		}

		for affixRows.Next() {
			var summary models.AffixSummary
			if err := affixRows.Scan(&summary.ID, &summary.Name, &summary.Category,
				&summary.Description, &summary.RewardModifier, &summary.DifficultyModifier); err != nil {
				affixRows.Close()
				return nil, 0, err
			}
			rotation.ActiveAffixes = append(rotation.ActiveAffixes, summary)
		}
		affixRows.Close()

		if seasonalAffixID != nil {
			affix, err := r.GetAffix(ctx, *seasonalAffixID)
			if err == nil {
				rotation.SeasonalAffix = &models.AffixSummary{
					ID:                affix.ID,
					Name:              affix.Name,
					Category:          affix.Category,
					Description:       affix.Description,
					RewardModifier:    affix.RewardModifier,
					DifficultyModifier: affix.DifficultyModifier,
				}
			}
		}

		rotations = append(rotations, rotation)
	}

	return rotations, total, nil
}

func (r *AffixRepository) CreateRotation(ctx context.Context, rotation *models.AffixRotation) error {
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return err
	}
	defer tx.Rollback(ctx)

	var seasonalAffixID *uuid.UUID
	if rotation.SeasonalAffix != nil {
		seasonalAffixID = &rotation.SeasonalAffix.ID
	}

	_, err = tx.Exec(ctx,
		`INSERT INTO gameplay.affix_rotations (id, week_start, week_end, seasonal_affix_id, created_at)
		 VALUES ($1, $2, $3, $4, $5)`,
		rotation.ID, rotation.WeekStart, rotation.WeekEnd, seasonalAffixID, rotation.CreatedAt)
	if err != nil {
		return err
	}

	for _, affix := range rotation.ActiveAffixes {
		_, err = tx.Exec(ctx,
			`INSERT INTO gameplay.affix_rotation_affixes (rotation_id, affix_id)
			 VALUES ($1, $2)`,
			rotation.ID, affix.ID)
		if err != nil {
			return err
		}
	}

	return tx.Commit(ctx)
}

func (r *AffixRepository) GetInstanceAffixes(ctx context.Context, instanceID uuid.UUID) ([]models.AffixSummary, time.Time, error) {
	var appliedAt time.Time

	err := r.db.QueryRow(ctx,
		`SELECT applied_at FROM gameplay.instance_affixes WHERE instance_id = $1 LIMIT 1`,
		instanceID).Scan(&appliedAt)
	if err != nil {
		return nil, time.Time{}, errors.New("instance not found")
	}

	rows, err := r.db.Query(ctx,
		`SELECT a.id, a.name, a.category, a.description, a.reward_modifier, a.difficulty_modifier
		 FROM gameplay.instance_affixes ia
		 JOIN gameplay.affixes a ON ia.affix_id = a.id
		 WHERE ia.instance_id = $1`,
		instanceID)
	if err != nil {
		return nil, time.Time{}, err
	}
	defer rows.Close()

	var affixes []models.AffixSummary
	for rows.Next() {
		var summary models.AffixSummary
		if err := rows.Scan(&summary.ID, &summary.Name, &summary.Category,
			&summary.Description, &summary.RewardModifier, &summary.DifficultyModifier); err != nil {
			return nil, time.Time{}, err
		}
		affixes = append(affixes, summary)
	}

	return affixes, appliedAt, nil
}

func (r *AffixRepository) SaveInstanceAffixes(ctx context.Context, instanceID uuid.UUID, affixes []uuid.UUID) error {
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return err
	}
	defer tx.Rollback(ctx)

	_, err = tx.Exec(ctx,
		`DELETE FROM gameplay.instance_affixes WHERE instance_id = $1`,
		instanceID)
	if err != nil {
		return err
	}

	appliedAt := time.Now()
	for _, affixID := range affixes {
		_, err = tx.Exec(ctx,
			`INSERT INTO gameplay.instance_affixes (instance_id, affix_id, applied_at)
			 VALUES ($1, $2, $3)`,
			instanceID, affixID, appliedAt)
		if err != nil {
			return err
		}
	}

	return tx.Commit(ctx)
}

