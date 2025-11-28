package server

import (
	"context"
	"encoding/json"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/sirupsen/logrus"
)

type AchievementRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewAchievementRepository(db *pgxpool.Pool) *AchievementRepository {
	return &AchievementRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *AchievementRepository) Create(ctx context.Context, achievement *models.Achievement) error {
	conditionsJSON, err := json.Marshal(achievement.Conditions)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal conditions JSON")
		return err
	}
	rewardsJSON, err := json.Marshal(achievement.Rewards)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal rewards JSON")
		return err
	}

	query := `
		INSERT INTO mvp_core.achievements (
			id, code, type, category, rarity, title, description, points,
			conditions, rewards, is_hidden, is_seasonal, season_id, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15
		)`

	_, err = r.db.Exec(ctx, query,
		achievement.ID, achievement.Code, achievement.Type, achievement.Category,
		achievement.Rarity, achievement.Title, achievement.Description, achievement.Points,
		conditionsJSON, rewardsJSON, achievement.IsHidden, achievement.IsSeasonal,
		achievement.SeasonID, achievement.CreatedAt, achievement.UpdatedAt,
	)

	return err
}

func (r *AchievementRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Achievement, error) {
	var achievement models.Achievement
	var conditionsJSON []byte
	var rewardsJSON []byte
	var seasonID *uuid.UUID

	query := `
		SELECT id, code, type, category, rarity, title, description, points,
			conditions, rewards, is_hidden, is_seasonal, season_id, created_at, updated_at
		FROM mvp_core.achievements
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, id).Scan(
		&achievement.ID, &achievement.Code, &achievement.Type, &achievement.Category,
		&achievement.Rarity, &achievement.Title, &achievement.Description, &achievement.Points,
		&conditionsJSON, &rewardsJSON, &achievement.IsHidden, &achievement.IsSeasonal,
		&seasonID, &achievement.CreatedAt, &achievement.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	achievement.SeasonID = seasonID
	if len(conditionsJSON) > 0 {
		if err := json.Unmarshal(conditionsJSON, &achievement.Conditions); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal conditions JSON")
		}
	}
	if len(rewardsJSON) > 0 {
		if err := json.Unmarshal(rewardsJSON, &achievement.Rewards); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal rewards JSON")
		}
	}

	return &achievement, nil
}

func (r *AchievementRepository) GetByCode(ctx context.Context, code string) (*models.Achievement, error) {
	var achievement models.Achievement
	var conditionsJSON []byte
	var rewardsJSON []byte
	var seasonID *uuid.UUID

	query := `
		SELECT id, code, type, category, rarity, title, description, points,
			conditions, rewards, is_hidden, is_seasonal, season_id, created_at, updated_at
		FROM mvp_core.achievements
		WHERE code = $1`

	err := r.db.QueryRow(ctx, query, code).Scan(
		&achievement.ID, &achievement.Code, &achievement.Type, &achievement.Category,
		&achievement.Rarity, &achievement.Title, &achievement.Description, &achievement.Points,
		&conditionsJSON, &rewardsJSON, &achievement.IsHidden, &achievement.IsSeasonal,
		&seasonID, &achievement.CreatedAt, &achievement.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	achievement.SeasonID = seasonID
	if len(conditionsJSON) > 0 {
		if err := json.Unmarshal(conditionsJSON, &achievement.Conditions); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal conditions JSON")
		}
	}
	if len(rewardsJSON) > 0 {
		if err := json.Unmarshal(rewardsJSON, &achievement.Rewards); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal rewards JSON")
		}
	}

	return &achievement, nil
}

func (r *AchievementRepository) List(ctx context.Context, category *models.AchievementCategory, limit, offset int) ([]models.Achievement, error) {
	var query string
	var args []interface{}

	if category != nil {
		query = `
			SELECT id, code, type, category, rarity, title, description, points,
				conditions, rewards, is_hidden, is_seasonal, season_id, created_at, updated_at
			FROM mvp_core.achievements
			WHERE category = $1
			ORDER BY points DESC, created_at DESC
			LIMIT $2 OFFSET $3`
		args = []interface{}{*category, limit, offset}
	} else {
		query = `
			SELECT id, code, type, category, rarity, title, description, points,
				conditions, rewards, is_hidden, is_seasonal, season_id, created_at, updated_at
			FROM mvp_core.achievements
			ORDER BY points DESC, created_at DESC
			LIMIT $1 OFFSET $2`
		args = []interface{}{limit, offset}
	}

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var achievements []models.Achievement
	for rows.Next() {
		var achievement models.Achievement
		var conditionsJSON []byte
		var rewardsJSON []byte
		var seasonID *uuid.UUID

		err := rows.Scan(
			&achievement.ID, &achievement.Code, &achievement.Type, &achievement.Category,
			&achievement.Rarity, &achievement.Title, &achievement.Description, &achievement.Points,
			&conditionsJSON, &rewardsJSON, &achievement.IsHidden, &achievement.IsSeasonal,
			&seasonID, &achievement.CreatedAt, &achievement.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}

		achievement.SeasonID = seasonID
		if len(conditionsJSON) > 0 {
			if err := json.Unmarshal(conditionsJSON, &achievement.Conditions); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal conditions JSON")
			}
		}
		if len(rewardsJSON) > 0 {
			if err := json.Unmarshal(rewardsJSON, &achievement.Rewards); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal rewards JSON")
			}
		}

		achievements = append(achievements, achievement)
	}

	return achievements, nil
}

func (r *AchievementRepository) Count(ctx context.Context, category *models.AchievementCategory) (int, error) {
	var count int
	var err error

	if category != nil {
		query := `SELECT COUNT(*) FROM mvp_core.achievements WHERE category = $1`
		err = r.db.QueryRow(ctx, query, *category).Scan(&count)
	} else {
		query := `SELECT COUNT(*) FROM mvp_core.achievements`
		err = r.db.QueryRow(ctx, query).Scan(&count)
	}

	return count, err
}

func (r *AchievementRepository) CountByCategory(ctx context.Context) (map[string]int, error) {
	query := `
		SELECT category, COUNT(*)
		FROM mvp_core.achievements
		GROUP BY category`

	rows, err := r.db.Query(ctx, query)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	categories := make(map[string]int)
	for rows.Next() {
		var category string
		var count int
		if err := rows.Scan(&category, &count); err != nil {
			return nil, err
		}
		categories[category] = count
	}

	return categories, nil
}
