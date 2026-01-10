package services

import (
	"database/sql"
	"fmt"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"battle-pass-service-go/internal/models"
)

// AnalyticsService handles analytics and statistics business logic
type AnalyticsService struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.Logger
}

// NewAnalyticsService creates a new AnalyticsService instance
func NewAnalyticsService(db *sql.DB, redis *redis.Client, logger *zap.Logger) *AnalyticsService {
	return &AnalyticsService{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// GetPlayerStatistics returns comprehensive statistics for a player
func (a *AnalyticsService) GetPlayerStatistics(playerID string) (*models.PlayerStatistics, error) {
	stats := &models.PlayerStatistics{
		PlayerID: playerID,
	}

	// Get basic stats
	err := a.getBasicStats(playerID, stats)
	if err != nil {
		return nil, fmt.Errorf("failed to get basic stats: %w", err)
	}

	// Get per-season data
	seasonsData, err := a.getSeasonsData(playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get seasons data: %w", err)
	}
	stats.SeasonsData = seasonsData

	// Get favorite reward type
	favoriteType, err := a.getFavoriteRewardType(playerID)
	if err != nil {
		a.logger.Warn("Failed to get favorite reward type", zap.Error(err))
		favoriteType = "cosmetic" // default
	}
	stats.FavoriteRewardType = favoriteType

	return stats, nil
}

// getBasicStats gets basic player statistics
func (a *AnalyticsService) getBasicStats(playerID string, stats *models.PlayerStatistics) error {
	query := `
		SELECT
			COUNT(DISTINCT pp.season_id) as seasons_played,
			COALESCE(SUM(pp.total_xp), 0) as total_xp_earned,
			COALESCE(MAX(pp.current_level), 0) as highest_level_reached,
			COALESCE(SUM(cr.rewards_count), 0) as rewards_claimed,
			COALESCE(SUM(pp.premium_count), 0) as premium_passes_purchased
		FROM player_progress pp
		LEFT JOIN (
			SELECT player_id, season_id, COUNT(*) as rewards_count
			FROM claimed_rewards
			WHERE player_id = $1
			GROUP BY player_id, season_id
		) cr ON cr.player_id = pp.player_id AND cr.season_id = pp.season_id
		LEFT JOIN (
			SELECT player_id, season_id, COUNT(*) as premium_count
			FROM premium_purchases
			WHERE player_id = $1
			GROUP BY player_id, season_id
		) pr ON pr.player_id = pp.player_id AND pr.season_id = pp.season_id
		WHERE pp.player_id = $1
	`

	row := a.db.QueryRow(query, playerID)

	err := row.Scan(
		&stats.SeasonsPlayed,
		&stats.TotalXPEarned,
		&stats.HighestLevelReached,
		&stats.RewardsClaimed,
		&stats.PremiumPassesPurchased,
	)

	if err != nil {
		return fmt.Errorf("failed to scan basic stats: %w", err)
	}

	return nil
}

// getSeasonsData gets detailed data for each season the player participated in
func (a *AnalyticsService) getSeasonsData(playerID string) ([]models.SeasonData, error) {
	query := `
		SELECT
			s.id, s.name,
			COALESCE(pp.current_level, 0) as final_level,
			COALESCE(pp.total_xp, 0) as xp_earned,
			COALESCE(cr.rewards_count, 0) as rewards_claimed,
			COALESCE(pp.has_premium, false) as had_premium
		FROM seasons s
		LEFT JOIN player_progress pp ON pp.season_id = s.id AND pp.player_id = $1
		LEFT JOIN (
			SELECT season_id, COUNT(*) as rewards_count
			FROM claimed_rewards
			WHERE player_id = $1
			GROUP BY season_id
		) cr ON cr.season_id = s.id
		WHERE pp.player_id IS NOT NULL OR cr.season_id IS NOT NULL
		ORDER BY s.start_date DESC
	`

	rows, err := a.db.Query(query, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get seasons data: %w", err)
	}
	defer rows.Close()

	var seasonsData []models.SeasonData
	for rows.Next() {
		var data models.SeasonData
		err := rows.Scan(
			&data.SeasonID, &data.SeasonName, &data.FinalLevel,
			&data.XPEarned, &data.RewardsClaimed, &data.HadPremium,
		)
		if err != nil {
			continue
		}
		seasonsData = append(seasonsData, data)
	}

	return seasonsData, nil
}

// getFavoriteRewardType determines the player's favorite reward category
func (a *AnalyticsService) getFavoriteRewardType(playerID string) (string, error) {
	query := `
		SELECT r.type, COUNT(*) as count
		FROM claimed_rewards cr
		JOIN rewards r ON r.id = cr.reward_id
		WHERE cr.player_id = $1
		GROUP BY r.type
		ORDER BY count DESC
		LIMIT 1
	`

	var rewardType string
	var count int

	err := a.db.QueryRow(query, playerID).Scan(&rewardType, &count)
	if err != nil {
		if err == sql.ErrNoRows {
			return "cosmetic", nil // default if no rewards claimed
		}
		return "", fmt.Errorf("failed to get favorite reward type: %w", err)
	}

	return rewardType, nil
}

// RecordXPEvent records an XP grant event for analytics
func (a *AnalyticsService) RecordXPEvent(playerID string, amount int, reason string, metadata map[string]interface{}) error {
	// Store in Redis for real-time analytics
	eventKey := fmt.Sprintf("analytics:xp_events:%s", playerID)
	eventData := map[string]interface{}{
		"amount":    amount,
		"reason":    reason,
		"timestamp": "NOW()", // In real implementation, use actual timestamp
		"metadata":  metadata,
	}

	// TODO: Implement proper Redis storage for analytics
	_ = eventKey
	_ = eventData

	// Also store in database for long-term analytics
	query := `
		INSERT INTO xp_events (player_id, amount, reason, metadata, created_at)
		VALUES ($1, $2, $3, $4, NOW())
	`

	_, err := a.db.Exec(query, playerID, amount, reason, metadata)
	if err != nil {
		a.logger.Error("Failed to record XP event",
			zap.String("playerID", playerID), zap.Error(err))
		return fmt.Errorf("failed to record XP event: %w", err)
	}

	return nil
}

// RecordRewardClaim records a reward claim event for analytics
func (a *AnalyticsService) RecordRewardClaim(playerID, rewardID string, level int, tier string) error {
	// Store in Redis for real-time analytics
	eventKey := fmt.Sprintf("analytics:claim_events:%s", playerID)
	eventData := map[string]interface{}{
		"reward_id": rewardID,
		"level":     level,
		"tier":      tier,
		"timestamp": "NOW()",
	}

	_ = eventKey
	_ = eventData

	// Also store in database for long-term analytics
	query := `
		INSERT INTO claim_events (player_id, reward_id, level, tier, created_at)
		VALUES ($1, $2, $3, $4, NOW())
	`

	_, err := a.db.Exec(query, playerID, rewardID, level, tier)
	if err != nil {
		a.logger.Error("Failed to record reward claim event",
			zap.String("playerID", playerID), zap.String("rewardID", rewardID), zap.Error(err))
		return fmt.Errorf("failed to record reward claim event: %w", err)
	}

	return nil
}

// GetGlobalStats returns global Battle Pass statistics
func (a *AnalyticsService) GetGlobalStats() (map[string]interface{}, error) {
	stats := make(map[string]interface{})

	// Total players
	var totalPlayers int
	err := a.db.QueryRow("SELECT COUNT(DISTINCT player_id) FROM player_progress").Scan(&totalPlayers)
	if err != nil {
		return nil, fmt.Errorf("failed to get total players: %w", err)
	}
	stats["total_players"] = totalPlayers

	// Total XP granted
	var totalXP int
	err = a.db.QueryRow("SELECT COALESCE(SUM(amount), 0) FROM xp_events").Scan(&totalXP)
	if err != nil {
		return nil, fmt.Errorf("failed to get total XP: %w", err)
	}
	stats["total_xp_granted"] = totalXP

	// Total rewards claimed
	var totalRewards int
	err = a.db.QueryRow("SELECT COUNT(*) FROM claimed_rewards").Scan(&totalRewards)
	if err != nil {
		return nil, fmt.Errorf("failed to get total rewards: %w", err)
	}
	stats["total_rewards_claimed"] = totalRewards

	// Most popular reward types
	popularRewards, err := a.getPopularRewardTypes()
	if err != nil {
		a.logger.Warn("Failed to get popular reward types", zap.Error(err))
	} else {
		stats["popular_reward_types"] = popularRewards
	}

	return stats, nil
}

// getPopularRewardTypes returns the most claimed reward types globally
func (a *AnalyticsService) getPopularRewardTypes() ([]map[string]interface{}, error) {
	query := `
		SELECT r.type, COUNT(*) as count
		FROM claimed_rewards cr
		JOIN rewards r ON r.id = cr.reward_id
		GROUP BY r.type
		ORDER BY count DESC
		LIMIT 5
	`

	rows, err := a.db.Query(query)
	if err != nil {
		return nil, fmt.Errorf("failed to get popular reward types: %w", err)
	}
	defer rows.Close()

	var results []map[string]interface{}
	for rows.Next() {
		var rewardType string
		var count int
		err := rows.Scan(&rewardType, &count)
		if err != nil {
			continue
		}

		results = append(results, map[string]interface{}{
			"type":  rewardType,
			"count": count,
		})
	}

	return results, nil
}