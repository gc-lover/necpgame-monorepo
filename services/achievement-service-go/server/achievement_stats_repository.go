package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/achievement-service-go/models"
)

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
		AchievementID:   achievementID,
		TotalUnlocks:    totalUnlocks,
		UnlockPercent:   unlockPercent,
		FirstUnlockedAt: firstUnlockedAt,
	}

	return stats, nil
}

