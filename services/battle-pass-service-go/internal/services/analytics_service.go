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

	// Store analytics event in Redis for real-time processing
	err := a.redis.HSet(a.redis.Context(), eventKey, eventData).Err()
	if err != nil {
		a.logger.Error("Failed to store analytics event in Redis",
			zap.String("eventKey", eventKey),
			zap.Error(err))
		// Continue with database storage even if Redis fails
	} else {
		// Set expiration for analytics data (24 hours)
		a.redis.Expire(a.redis.Context(), eventKey, 24*time.Hour)
	}

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

// GetSeasonAnalytics returns comprehensive analytics for a specific season
func (a *AnalyticsService) GetSeasonAnalytics(seasonID string) (map[string]interface{}, error) {
	analytics := make(map[string]interface{})

	// Basic season stats
	err := a.getSeasonBasicStats(seasonID, analytics)
	if err != nil {
		return nil, fmt.Errorf("failed to get season basic stats: %w", err)
	}

	// Player engagement metrics
	engagement, err := a.getPlayerEngagementMetrics(seasonID)
	if err != nil {
		a.logger.Warn("Failed to get engagement metrics", zap.Error(err))
	} else {
		analytics["engagement"] = engagement
	}

	// Premium conversion rates
	premiumStats, err := a.getPremiumConversionStats(seasonID)
	if err != nil {
		a.logger.Warn("Failed to get premium stats", zap.Error(err))
	} else {
		analytics["premium"] = premiumStats
	}

	// Reward claim patterns
	rewardPatterns, err := a.getRewardClaimPatterns(seasonID)
	if err != nil {
		a.logger.Warn("Failed to get reward patterns", zap.Error(err))
	} else {
		analytics["reward_patterns"] = rewardPatterns
	}

	// Level progression analytics
	levelProgression, err := a.getLevelProgressionAnalytics(seasonID)
	if err != nil {
		a.logger.Warn("Failed to get level progression analytics", zap.Error(err))
	} else {
		analytics["level_progression"] = levelProgression
	}

	return analytics, nil
}

// getSeasonBasicStats gets basic statistics for a season
func (a *AnalyticsService) getSeasonBasicStats(seasonID string, analytics map[string]interface{}) error {
	query := `
		SELECT
			COUNT(DISTINCT pp.player_id) as total_players,
			COUNT(DISTINCT CASE WHEN pp.has_premium THEN pp.player_id END) as premium_players,
			AVG(pp.current_level) as avg_level,
			MAX(pp.current_level) as max_level,
			SUM(pp.total_xp) as total_xp_granted,
			COUNT(cr.reward_id) as total_rewards_claimed
		FROM player_progress pp
		LEFT JOIN claimed_rewards cr ON cr.season_id = pp.season_id AND cr.player_id = pp.player_id
		WHERE pp.season_id = $1
	`

	row := a.db.QueryRow(query, seasonID)

	var totalPlayers, premiumPlayers, maxLevel, totalXPGranted, totalRewardsClaimed int
	var avgLevel float64

	err := row.Scan(&totalPlayers, &premiumPlayers, &avgLevel, &maxLevel, &totalXPGranted, &totalRewardsClaimed)
	if err != nil {
		return fmt.Errorf("failed to scan season stats: %w", err)
	}

	analytics["season_id"] = seasonID
	analytics["total_players"] = totalPlayers
	analytics["premium_players"] = premiumPlayers
	analytics["average_level"] = avgLevel
	analytics["max_level"] = maxLevel
	analytics["total_xp_granted"] = totalXPGranted
	analytics["total_rewards_claimed"] = totalRewardsClaimed

	if totalPlayers > 0 {
		analytics["premium_conversion_rate"] = float64(premiumPlayers) / float64(totalPlayers) * 100
	}

	return nil
}

// getPlayerEngagementMetrics calculates player engagement statistics
func (a *AnalyticsService) getPlayerEngagementMetrics(seasonID string) (map[string]interface{}, error) {
	metrics := make(map[string]interface{})

	// Active players by level ranges
	levelRanges := map[string]string{
		"early_game":  "current_level <= 10",
		"mid_game":    "current_level > 10 AND current_level <= 25",
		"late_game":   "current_level > 25",
		"max_level":   "current_level >= 50", // Assuming max level is 50
	}

	for rangeName, condition := range levelRanges {
		query := fmt.Sprintf("SELECT COUNT(*) FROM player_progress WHERE season_id = $1 AND %s", condition)
		var count int
		err := a.db.QueryRow(query, seasonID).Scan(&count)
		if err != nil {
			continue
		}
		metrics[rangeName+"_players"] = count
	}

	// XP earned distribution
	xpDistribution, err := a.getXPDistribution(seasonID)
	if err != nil {
		return nil, err
	}
	metrics["xp_distribution"] = xpDistribution

	return metrics, nil
}

// getXPDistribution returns XP earned distribution across players
func (a *AnalyticsService) getXPDistribution(seasonID string) (map[string]int, error) {
	query := `
		SELECT
			CASE
				WHEN total_xp < 1000 THEN 'low'
				WHEN total_xp < 5000 THEN 'medium'
				WHEN total_xp < 15000 THEN 'high'
				ELSE 'very_high'
			END as xp_range,
			COUNT(*) as player_count
		FROM player_progress
		WHERE season_id = $1
		GROUP BY xp_range
	`

	rows, err := a.db.Query(query, seasonID)
	if err != nil {
		return nil, fmt.Errorf("failed to get XP distribution: %w", err)
	}
	defer rows.Close()

	distribution := make(map[string]int)
	for rows.Next() {
		var xpRange string
		var count int
		err := rows.Scan(&xpRange, &count)
		if err != nil {
			continue
		}
		distribution[xpRange] = count
	}

	return distribution, nil
}

// getPremiumConversionStats returns premium pass conversion statistics
func (a *AnalyticsService) getPremiumConversionStats(seasonID string) (map[string]interface{}, error) {
	stats := make(map[string]interface{})

	query := `
		SELECT
			COUNT(*) as total_purchases,
			AVG(price) as avg_price,
			SUM(price) as total_revenue
		FROM premium_purchases
		WHERE season_id = $1
	`

	row := a.db.QueryRow(query, seasonID)

	var totalPurchases int
	var avgPrice, totalRevenue float64

	err := row.Scan(&totalPurchases, &avgPrice, &totalRevenue)
	if err != nil {
		return nil, fmt.Errorf("failed to scan premium stats: %w", err)
	}

	stats["total_purchases"] = totalPurchases
	stats["average_price"] = avgPrice
	stats["total_revenue"] = totalRevenue

	return stats, nil
}

// getRewardClaimPatterns analyzes reward claiming patterns
func (a *AnalyticsService) getRewardClaimPatterns(seasonID string) (map[string]interface{}, error) {
	patterns := make(map[string]interface{})

	// Most claimed rewards
	query := `
		SELECT r.name, r.type, COUNT(*) as claim_count
		FROM claimed_rewards cr
		JOIN rewards r ON r.id = cr.reward_id
		WHERE cr.season_id = $1
		GROUP BY r.id, r.name, r.type
		ORDER BY claim_count DESC
		LIMIT 10
	`

	rows, err := a.db.Query(query, seasonID)
	if err != nil {
		return nil, fmt.Errorf("failed to get reward patterns: %w", err)
	}
	defer rows.Close()

	var topRewards []map[string]interface{}
	for rows.Next() {
		var name, rewardType string
		var count int
		err := rows.Scan(&name, &rewardType, &count)
		if err != nil {
			continue
		}

		topRewards = append(topRewards, map[string]interface{}{
			"name":  name,
			"type":  rewardType,
			"count": count,
		})
	}

	patterns["top_claimed_rewards"] = topRewards

	// Claim rate by level
	levelClaims, err := a.getLevelClaimRates(seasonID)
	if err != nil {
		a.logger.Warn("Failed to get level claim rates", zap.Error(err))
	} else {
		patterns["level_claim_rates"] = levelClaims
	}

	return patterns, nil
}

// getLevelClaimRates returns claim rates for each level
func (a *AnalyticsService) getLevelClaimRates(seasonID string) ([]map[string]interface{}, error) {
	query := `
		SELECT
			cr.level,
			COUNT(DISTINCT cr.player_id) as players_claimed,
			COUNT(cr.reward_id) as total_claims
		FROM claimed_rewards cr
		WHERE cr.season_id = $1
		GROUP BY cr.level
		ORDER BY cr.level
	`

	rows, err := a.db.Query(query, seasonID)
	if err != nil {
		return nil, fmt.Errorf("failed to get level claim rates: %w", err)
	}
	defer rows.Close()

	var rates []map[string]interface{}
	for rows.Next() {
		var level, playersClaimed, totalClaims int
		err := rows.Scan(&level, &playersClaimed, &totalClaims)
		if err != nil {
			continue
		}

		rates = append(rates, map[string]interface{}{
			"level":           level,
			"players_claimed": playersClaimed,
			"total_claims":    totalClaims,
		})
	}

	return rates, nil
}

// getLevelProgressionAnalytics returns level progression statistics
func (a *AnalyticsService) getLevelProgressionAnalytics(seasonID string) (map[string]interface{}, error) {
	analytics := make(map[string]interface{})

	// Level distribution
	query := `
		SELECT current_level, COUNT(*) as player_count
		FROM player_progress
		WHERE season_id = $1
		GROUP BY current_level
		ORDER BY current_level
	`

	rows, err := a.db.Query(query, seasonID)
	if err != nil {
		return nil, fmt.Errorf("failed to get level distribution: %w", err)
	}
	defer rows.Close()

	levelDistribution := make(map[int]int)
	for rows.Next() {
		var level, count int
		err := rows.Scan(&level, &count)
		if err != nil {
			continue
		}
		levelDistribution[level] = count
	}

	analytics["level_distribution"] = levelDistribution

	// Average time to reach levels (would need timestamp data)
	// This is a placeholder for more complex analytics

	return analytics, nil
}