package server

import (
	"context"
	"encoding/json"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/necpgame/achievement-service-go/models"
)

func (r *AchievementRepository) GetPlayerAchievement(ctx context.Context, playerID, achievementID uuid.UUID) (*models.PlayerAchievement, error) {
	var pa models.PlayerAchievement
	var progressDataJSON []byte

	query := `
		SELECT id, player_id, achievement_id, status, progress, progress_max,
			progress_data, unlocked_at, created_at, updated_at
		FROM mvp_core.player_achievements
		WHERE player_id = $1 AND achievement_id = $2`

	err := r.db.QueryRow(ctx, query, playerID, achievementID).Scan(
		&pa.ID, &pa.PlayerID, &pa.AchievementID, &pa.Status, &pa.Progress,
		&pa.ProgressMax, &progressDataJSON, &pa.UnlockedAt, &pa.CreatedAt, &pa.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(progressDataJSON) > 0 {
		if err := json.Unmarshal(progressDataJSON, &pa.ProgressData); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal progress data JSON")
		}
	}

	return &pa, nil
}

func (r *AchievementRepository) CreatePlayerAchievement(ctx context.Context, pa *models.PlayerAchievement) error {
	progressDataJSON, err := json.Marshal(pa.ProgressData)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal progress data JSON")
		return err
	}

	query := `
		INSERT INTO mvp_core.player_achievements (
			id, player_id, achievement_id, status, progress, progress_max,
			progress_data, unlocked_at, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10
		)`

	_, err = r.db.Exec(ctx, query,
		pa.ID, pa.PlayerID, pa.AchievementID, pa.Status, pa.Progress,
		pa.ProgressMax, progressDataJSON, pa.UnlockedAt, pa.CreatedAt, pa.UpdatedAt,
	)

	return err
}

func (r *AchievementRepository) UpdatePlayerAchievement(ctx context.Context, pa *models.PlayerAchievement) error {
	progressDataJSON, err := json.Marshal(pa.ProgressData)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal progress data JSON")
		return err
	}

	query := `
		UPDATE mvp_core.player_achievements
		SET status = $1, progress = $2, progress_max = $3, progress_data = $4,
			unlocked_at = $5, updated_at = $6
		WHERE id = $7`

	_, err = r.db.Exec(ctx, query,
		pa.Status, pa.Progress, pa.ProgressMax, progressDataJSON,
		pa.UnlockedAt, pa.UpdatedAt, pa.ID,
	)

	return err
}

func (r *AchievementRepository) GetPlayerAchievements(ctx context.Context, playerID uuid.UUID, category *models.AchievementCategory, limit, offset int) ([]models.PlayerAchievement, error) {
	var query string
	var args []interface{}

	if category != nil {
		query = `
			SELECT pa.id, pa.player_id, pa.achievement_id, pa.status, pa.progress, pa.progress_max,
				pa.progress_data, pa.unlocked_at, pa.created_at, pa.updated_at
			FROM mvp_core.player_achievements pa
			JOIN mvp_core.achievements a ON pa.achievement_id = a.id
			WHERE pa.player_id = $1 AND a.category = $2
			ORDER BY pa.updated_at DESC
			LIMIT $3 OFFSET $4`
		args = []interface{}{playerID, *category, limit, offset}
	} else {
		query = `
			SELECT id, player_id, achievement_id, status, progress, progress_max,
				progress_data, unlocked_at, created_at, updated_at
			FROM mvp_core.player_achievements
			WHERE player_id = $1
			ORDER BY updated_at DESC
			LIMIT $2 OFFSET $3`
		args = []interface{}{playerID, limit, offset}
	}

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var playerAchievements []models.PlayerAchievement
	for rows.Next() {
		var pa models.PlayerAchievement
		var progressDataJSON []byte

		err := rows.Scan(
			&pa.ID, &pa.PlayerID, &pa.AchievementID, &pa.Status, &pa.Progress,
			&pa.ProgressMax, &progressDataJSON, &pa.UnlockedAt, &pa.CreatedAt, &pa.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}

		if len(progressDataJSON) > 0 {
			if err := json.Unmarshal(progressDataJSON, &pa.ProgressData); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal progress data JSON")
			}
		}

		playerAchievements = append(playerAchievements, pa)
	}

	return playerAchievements, nil
}

func (r *AchievementRepository) CountPlayerAchievements(ctx context.Context, playerID uuid.UUID) (int, int, error) {
	var total, unlocked int

	query := `
		SELECT 
			COUNT(*) as total,
			COUNT(CASE WHEN status = 'unlocked' THEN 1 END) as unlocked
		FROM mvp_core.player_achievements
		WHERE player_id = $1`

	err := r.db.QueryRow(ctx, query, playerID).Scan(&total, &unlocked)
	return total, unlocked, err
}

