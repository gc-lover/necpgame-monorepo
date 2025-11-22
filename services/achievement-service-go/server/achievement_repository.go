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
		json.Unmarshal(conditionsJSON, &achievement.Conditions)
	}
	if len(rewardsJSON) > 0 {
		json.Unmarshal(rewardsJSON, &achievement.Rewards)
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
		json.Unmarshal(conditionsJSON, &achievement.Conditions)
	}
	if len(rewardsJSON) > 0 {
		json.Unmarshal(rewardsJSON, &achievement.Rewards)
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
		json.Unmarshal(progressDataJSON, &pa.ProgressData)
	}

	return &pa, nil
}

func (r *AchievementRepository) CreatePlayerAchievement(ctx context.Context, pa *models.PlayerAchievement) error {
	progressDataJSON, _ := json.Marshal(pa.ProgressData)

	query := `
		INSERT INTO mvp_core.player_achievements (
			id, player_id, achievement_id, status, progress, progress_max,
			progress_data, unlocked_at, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10
		)`

	_, err := r.db.Exec(ctx, query,
		pa.ID, pa.PlayerID, pa.AchievementID, pa.Status, pa.Progress,
		pa.ProgressMax, progressDataJSON, pa.UnlockedAt, pa.CreatedAt, pa.UpdatedAt,
	)

	return err
}

func (r *AchievementRepository) UpdatePlayerAchievement(ctx context.Context, pa *models.PlayerAchievement) error {
	progressDataJSON, _ := json.Marshal(pa.ProgressData)

	query := `
		UPDATE mvp_core.player_achievements
		SET status = $1, progress = $2, progress_max = $3, progress_data = $4,
			unlocked_at = $5, updated_at = $6
		WHERE id = $7`

	_, err := r.db.Exec(ctx, query,
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
			json.Unmarshal(progressDataJSON, &pa.ProgressData)
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
			json.Unmarshal(progressDataJSON, &pa.ProgressData)
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
			json.Unmarshal(progressDataJSON, &pa.ProgressData)
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

