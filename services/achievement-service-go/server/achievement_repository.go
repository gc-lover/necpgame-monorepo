// Issue: #141888300
package server

import (
	"context"
	"encoding/json"
	"time"

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
	conditionsJSON, _ := json.Marshal(achievement.Conditions)
	rewardsJSON, _ := json.Marshal(achievement.Rewards)

	query := `
		INSERT INTO mvp_core.achievements (
			id, code, type, category, rarity, title, description, points,
			conditions, rewards, is_hidden, is_seasonal, season_id, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15
		)`

	_, err := r.db.Exec(ctx, query,
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
			achievement.Conditions = make(map[string]interface{})
		}
	}
	if len(rewardsJSON) > 0 {
		if err := json.Unmarshal(rewardsJSON, &achievement.Rewards); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal rewards JSON")
			achievement.Rewards = make(map[string]interface{})
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
			achievement.Conditions = make(map[string]interface{})
		}
	}
	if len(rewardsJSON) > 0 {
		if err := json.Unmarshal(rewardsJSON, &achievement.Rewards); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal rewards JSON")
			achievement.Rewards = make(map[string]interface{})
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
			json.Unmarshal(conditionsJSON, &achievement.Conditions)
		}
		if len(rewardsJSON) > 0 {
			json.Unmarshal(rewardsJSON, &achievement.Rewards)
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

func (r *AchievementRepository) GetNearCompletion(ctx context.Context, playerID uuid.UUID, threshold float64) ([]models.PlayerAchievement, error) {
	query := `
		SELECT id, player_id, achievement_id, status, progress, progress_max,
			progress_data, unlocked_at, created_at, updated_at
		FROM mvp_core.player_achievements
		WHERE player_id = $1 
			AND status = 'progress'
			AND progress_max > 0
			AND (progress::float / progress_max::float) >= $2
		ORDER BY (progress::float / progress_max::float) DESC
		LIMIT 10`

	rows, err := r.db.Query(ctx, query, playerID, threshold)
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
				pa.ProgressData = make(map[string]interface{})
			}
		}

		playerAchievements = append(playerAchievements, pa)
	}

	return playerAchievements, nil
}

func (r *AchievementRepository) GetRecentUnlocks(ctx context.Context, playerID uuid.UUID, limit int) ([]models.PlayerAchievement, error) {
	query := `
		SELECT id, player_id, achievement_id, status, progress, progress_max,
			progress_data, unlocked_at, created_at, updated_at
		FROM mvp_core.player_achievements
		WHERE player_id = $1 AND status = 'unlocked' AND unlocked_at IS NOT NULL
		ORDER BY unlocked_at DESC
		LIMIT $2`

	rows, err := r.db.Query(ctx, query, playerID, limit)
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
				pa.ProgressData = make(map[string]interface{})
			}
		}

		playerAchievements = append(playerAchievements, pa)
	}

	return playerAchievements, nil
}

func (r *AchievementRepository) GetLeaderboard(ctx context.Context, period string, limit int) ([]models.LeaderboardEntry, error) {
	var query string

	switch period {
	case "daily":
		query = `
			SELECT 
				ROW_NUMBER() OVER (ORDER BY SUM(a.points) DESC) as rank,
				pa.player_id,
				COUNT(CASE WHEN pa.status = 'unlocked' THEN 1 END) as unlocked,
				COALESCE(SUM(a.points), 0) as points
			FROM mvp_core.player_achievements pa
			JOIN mvp_core.achievements a ON pa.achievement_id = a.id
			WHERE pa.unlocked_at >= CURRENT_DATE
			GROUP BY pa.player_id
			ORDER BY points DESC
			LIMIT $1`
	case "weekly":
		query = `
			SELECT 
				ROW_NUMBER() OVER (ORDER BY SUM(a.points) DESC) as rank,
				pa.player_id,
				COUNT(CASE WHEN pa.status = 'unlocked' THEN 1 END) as unlocked,
				COALESCE(SUM(a.points), 0) as points
			FROM mvp_core.player_achievements pa
			JOIN mvp_core.achievements a ON pa.achievement_id = a.id
			WHERE pa.unlocked_at >= CURRENT_DATE - INTERVAL '7 days'
			GROUP BY pa.player_id
			ORDER BY points DESC
			LIMIT $1`
	default:
		query = `
			SELECT 
				ROW_NUMBER() OVER (ORDER BY SUM(a.points) DESC) as rank,
				pa.player_id,
				COUNT(CASE WHEN pa.status = 'unlocked' THEN 1 END) as unlocked,
				COALESCE(SUM(a.points), 0) as points
			FROM mvp_core.player_achievements pa
			JOIN mvp_core.achievements a ON pa.achievement_id = a.id
			WHERE pa.status = 'unlocked'
			GROUP BY pa.player_id
			ORDER BY points DESC
			LIMIT $1`
	}

	rows, err := r.db.Query(ctx, query, limit)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var entries []models.LeaderboardEntry
	for rows.Next() {
		var entry models.LeaderboardEntry
		err := rows.Scan(&entry.Rank, &entry.PlayerID, &entry.Unlocked, &entry.Points)
		if err != nil {
			return nil, err
		}
		entries = append(entries, entry)
	}

	return entries, nil
}

func (r *AchievementRepository) GetAchievementStats(ctx context.Context, achievementID uuid.UUID) (*models.AchievementStatsResponse, error) {
	query := `
		SELECT 
			COUNT(CASE WHEN pa.status = 'unlocked' THEN 1 END) as total_unlocks,
			COUNT(*) as total_players,
			MIN(pa.unlocked_at) as first_unlocked_at
		FROM mvp_core.player_achievements pa
		WHERE pa.achievement_id = $1`

	var totalUnlocks, totalPlayers int
	var firstUnlockedAt *time.Time

	err := r.db.QueryRow(ctx, query, achievementID).Scan(&totalUnlocks, &totalPlayers, &firstUnlockedAt)
	if err != nil {
		return nil, err
	}

	unlockPercent := 0.0
	if totalPlayers > 0 {
		unlockPercent = float64(totalUnlocks) / float64(totalPlayers) * 100.0
	}

	stats := &models.AchievementStatsResponse{
		AchievementID: achievementID,
		TotalUnlocks:  totalUnlocks,
		UnlockPercent:  unlockPercent,
		FirstUnlockedAt: firstUnlockedAt,
	}

	return stats, nil
}

