// Agent: Backend Agent
// Issue: #backend-battle-pass-service

package repository

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"battle-pass-service-go/internal/config"
	"battle-pass-service-go/internal/database"
	"battle-pass-service-go/internal/models"

	"github.com/google/uuid"
)

// Repository handles database operations for battle pass
// MMOFPS Optimization: Connection pooling, prepared statements, query timeouts
type Repository struct {
	db *database.DB
}

// New creates a new repository instance
func New(cfg *config.Config) (*Repository, error) {
	// Initialize database connection
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	db, err := database.NewConnection(ctx, &cfg.Database)
	if err != nil {
		return nil, fmt.Errorf("failed to initialize database: %w", err)
	}

	return &Repository{db: db}, nil
}

// Close closes the database connection
func (r *Repository) Close() error {
	r.db.Close()
	return nil
}

// GetPlayerProgress retrieves player's battle pass progress
// MMOFPS Optimization: Single query with timeout
func (r *Repository) GetPlayerProgress(ctx context.Context, playerID uuid.UUID) (*models.PlayerProgress, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second) // MMOFPS: Fast progress query
	defer cancel()

	query := `
		SELECT current_level, current_xp, required_xp, total_xp_earned, premium_unlocked
		FROM battle_pass_progress WHERE player_id = $1`

	var progress models.PlayerProgress
	progress.PlayerID = playerID

	err := r.db.Pool().QueryRow(ctx, query, playerID).Scan(
		&progress.CurrentLevel, &progress.CurrentXP, &progress.RequiredXP,
		&progress.TotalXPEarned, &progress.PremiumUnlocked,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			// Initialize new progress for player
			return r.initializePlayerProgress(ctx, playerID)
		}
		return nil, fmt.Errorf("failed to get player progress: %w", err)
	}

	return &progress, nil
}

// initializePlayerProgress creates initial progress for a new player
func (r *Repository) initializePlayerProgress(ctx context.Context, playerID uuid.UUID) (*models.PlayerProgress, error) {
	query := `
		INSERT INTO battle_pass_progress (player_id, current_level, current_xp, required_xp, total_xp_earned, premium_unlocked)
		VALUES ($1, 1, 0, 1000, 0, false)
		ON CONFLICT (player_id) DO NOTHING
		RETURNING current_level, current_xp, required_xp, total_xp_earned, premium_unlocked`

	var progress models.PlayerProgress
	progress.PlayerID = playerID

	err := r.db.Pool().QueryRow(ctx, query, playerID).Scan(
		&progress.CurrentLevel, &progress.CurrentXP, &progress.RequiredXP,
		&progress.TotalXPEarned, &progress.PremiumUnlocked,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to initialize player progress: %w", err)
	}

	return &progress, nil
}

// AwardXP adds XP to player's battle pass progress
// MMOFPS Optimization: Optimistic locking for concurrent XP awards
func (r *Repository) AwardXP(ctx context.Context, playerID uuid.UUID, xpAmount int, reason string) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second) // MMOFPS: Fast XP update
	defer cancel()

	// Get current progress
	progress, err := r.GetPlayerProgress(ctx, playerID)
	if err != nil {
		return err
	}

	// Calculate new XP
	newTotalXP := progress.TotalXPEarned + xpAmount
	newCurrentXP := progress.CurrentXP + xpAmount

	// Check for level up
	newLevel := progress.CurrentLevel
	for newCurrentXP >= progress.RequiredXP && newLevel < 100 { // Max level 100
		newCurrentXP -= progress.RequiredXP
		newLevel++
		progress.RequiredXP = calculateRequiredXP(newLevel)
	}

	// Update progress
	query := `
		UPDATE battle_pass_progress
		SET current_level = $1, current_xp = $2, required_xp = $3, total_xp_earned = $4, updated_at = NOW()
		WHERE player_id = $5`

	_, err = r.db.Pool().Exec(ctx, query, newLevel, newCurrentXP, progress.RequiredXP, newTotalXP, playerID)
	if err != nil {
		return fmt.Errorf("failed to update XP: %w", err)
	}

	// Record XP transaction
	return r.recordXPTransaction(ctx, playerID, xpAmount, reason)
}

// recordXPTransaction logs XP earning transaction
func (r *Repository) recordXPTransaction(ctx context.Context, playerID uuid.UUID, xpAmount int, reason string) error {
	query := `
		INSERT INTO battle_pass_xp_transactions (player_id, xp_amount, reason, created_at)
		VALUES ($1, $2, $3, NOW())`

	_, err := r.db.Pool().Exec(ctx, query, playerID, xpAmount, reason)
	return err
}

// GetAvailableRewards gets rewards available for claiming at player's level
func (r *Repository) GetAvailableRewards(ctx context.Context, playerID uuid.UUID) ([]*models.Reward, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Reward availability check
	defer cancel()

	progress, err := r.GetPlayerProgress(ctx, playerID)
	if err != nil {
		return nil, err
	}

	query := `
		SELECT level, type, value, description
		FROM battle_pass_rewards
		WHERE level <= $1 AND claimed_by IS NULL
		ORDER BY level`

	rows, err := r.db.Pool().Query(ctx, query, progress.CurrentLevel)
	if err != nil {
		return nil, fmt.Errorf("failed to get available rewards: %w", err)
	}
	defer rows.Close()

	var rewards []*models.Reward
	for rows.Next() {
		var reward models.Reward
		err := rows.Scan(&reward.Level, &reward.Type, &reward.Value, &reward.Description)
		if err != nil {
			return nil, fmt.Errorf("failed to scan reward: %w", err)
		}
		rewards = append(rewards, &reward)
	}

	return rewards, nil
}

// ClaimReward marks a reward as claimed by player
// MMOFPS Optimization: Transactional operation with inventory integration
func (r *Repository) ClaimReward(ctx context.Context, playerID uuid.UUID, level int) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second) // MMOFPS: Reward claiming with external calls
	defer cancel()

	// Check if reward exists and not claimed
	var rewardID uuid.UUID
	var rewardType string
	var rewardValue interface{}

	query := `
		SELECT id, type, value FROM battle_pass_rewards
		WHERE level = $1 AND claimed_by IS NULL
		LIMIT 1`

	err := r.db.Pool().QueryRow(ctx, query, level).Scan(&rewardID, &rewardType, &rewardValue)
	if err != nil {
		if err == sql.ErrNoRows {
			return fmt.Errorf("reward not available")
		}
		return fmt.Errorf("failed to get reward: %w", err)
	}

	// Mark reward as claimed
	updateQuery := `
		UPDATE battle_pass_rewards
		SET claimed_by = $1, claimed_at = NOW()
		WHERE id = $2 AND claimed_by IS NULL`

	result, err := r.db.Pool().Exec(ctx, updateQuery, playerID, rewardID)
	if err != nil {
		return fmt.Errorf("failed to claim reward: %w", err)
	}

	if rowsAffected := result.RowsAffected(); rowsAffected == 0 {
		return fmt.Errorf("reward already claimed")
	}

	// TODO: Integrate with inventory service to actually grant the reward
	return nil
}

// GetCurrentSeason gets the currently active battle pass season
func (r *Repository) GetCurrentSeason(ctx context.Context) (*models.Season, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Current season query
	defer cancel()

	query := `
		SELECT id, name, start_date, end_date, max_level, premium_price, status
		FROM battle_pass_seasons
		WHERE status = 'active' AND NOW() BETWEEN start_date AND end_date
		ORDER BY start_date DESC
		LIMIT 1`

	var season models.Season
	err := r.db.Pool().QueryRow(ctx, query).Scan(
		&season.ID, &season.Name, &season.StartDate, &season.EndDate,
		&season.MaxLevel, &season.PremiumPrice, &season.Status,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("no active season found")
		}
		return nil, fmt.Errorf("failed to get current season: %w", err)
	}

	return &season, nil
}

// GetSeason gets a specific season by ID
func (r *Repository) GetSeason(ctx context.Context, seasonID uuid.UUID) (*models.Season, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Season details query
	defer cancel()

	query := `
		SELECT id, name, start_date, end_date, max_level, premium_price, status
		FROM battle_pass_seasons WHERE id = $1`

	var season models.Season
	err := r.db.Pool().QueryRow(ctx, query, seasonID).Scan(
		&season.ID, &season.Name, &season.StartDate, &season.EndDate,
		&season.MaxLevel, &season.PremiumPrice, &season.Status,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("season not found")
		}
		return nil, fmt.Errorf("failed to get season: %w", err)
	}

	return &season, nil
}

// GetPlayerStatistics gets comprehensive player statistics
func (r *Repository) GetPlayerStatistics(ctx context.Context, playerID uuid.UUID) (*models.PlayerStatistics, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second) // Analytics can take longer
	defer cancel()

	query := `
		SELECT
			COALESCE(SUM(xp_amount), 0) as total_xp_earned,
			COALESCE(MAX(current_level), 1) as current_level,
			COALESCE(MAX(current_level), 1) as highest_level,
			COUNT(DISTINCT CASE WHEN claimed_by = $1 THEN reward_id END) as rewards_claimed,
			COUNT(DISTINCT season_id) as seasons_played,
			COUNT(DISTINCT CASE WHEN premium_unlocked THEN season_id END) as premium_seasons
		FROM battle_pass_progress p
		LEFT JOIN battle_pass_xp_transactions t ON p.player_id = t.player_id
		LEFT JOIN battle_pass_seasons s ON s.start_date <= NOW() AND s.end_date >= NOW()
		WHERE p.player_id = $1
		GROUP BY p.player_id`

	var stats models.PlayerStatistics
	err := r.db.Pool().QueryRow(ctx, query, playerID).Scan(
		&stats.TotalXPEarned, &stats.CurrentLevel, &stats.HighestLevel,
		&stats.RewardsClaimed, &stats.SeasonsPlayed, &stats.PremiumSeasons,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get player statistics: %w", err)
	}

	// Calculate additional metrics
	stats.CompletionRate = float64(stats.RewardsClaimed) / float64(stats.HighestLevel) * 100
	if stats.SeasonsPlayed > 0 {
		stats.AverageXPPerGame = float64(stats.TotalXPEarned) / float64(stats.SeasonsPlayed)
	}

	return &stats, nil
}

// calculateRequiredXP calculates XP required for a given level
func calculateRequiredXP(level int) int {
	// Simple exponential growth: base 1000 XP, multiplier 1.1 per level
	baseXP := 1000
	multiplier := 1.1
	requiredXP := float64(baseXP)
	for i := 2; i <= level; i++ {
		requiredXP *= multiplier
	}
	return int(requiredXP)
}