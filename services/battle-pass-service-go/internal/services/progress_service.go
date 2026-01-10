package services

import (
	"database/sql"
	"fmt"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"battle-pass-service-go/internal/models"
)

// ProgressService handles player progress business logic
type ProgressService struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.Logger
}

// NewProgressService creates a new ProgressService instance
func NewProgressService(db *sql.DB, redis *redis.Client, logger *zap.Logger) *ProgressService {
	return &ProgressService{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// GetPlayerProgress returns player progress for a season
func (p *ProgressService) GetPlayerProgress(playerID, seasonID string) (*models.PlayerProgress, error) {
	// Try to get from cache first
	cacheKey := fmt.Sprintf("progress:%s:%s", playerID, seasonID)
	cached, err := p.redis.HGetAll(p.redis.Context(), cacheKey).Result()
	if err == nil && len(cached) > 0 {
		// Parse cached data
		progress := &models.PlayerProgress{
			PlayerID: playerID,
			SeasonID: seasonID,
		}
		// TODO: Parse cached fields
		return progress, nil
	}

	// Get from database
	var progress models.PlayerProgress
	query := `
		SELECT player_id, season_id, current_level, current_xp, total_xp, xp_to_next_level, has_premium, last_updated
		FROM player_progress
		WHERE player_id = $1 AND season_id = $2
	`

	err = p.db.QueryRow(query, playerID, seasonID).Scan(
		&progress.PlayerID, &progress.SeasonID, &progress.CurrentLevel,
		&progress.CurrentXP, &progress.TotalXP, &progress.XpToNextLevel,
		&progress.HasPremium, &progress.LastUpdated,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			// Initialize progress for new players
			return p.initializePlayerProgress(playerID, seasonID)
		}
		p.logger.Error("Failed to get player progress",
			zap.String("playerID", playerID), zap.String("seasonID", seasonID), zap.Error(err))
		return nil, fmt.Errorf("failed to get player progress: %w", err)
	}

	// Cache the result
	// TODO: Cache progress data

	return &progress, nil
}

// GrantXP grants XP to a player and handles level ups
func (p *ProgressService) GrantXP(playerID string, grant models.XPGrant) (*models.XPGrantResult, error) {
	// Get current season
	currentSeason, err := p.getCurrentSeason()
	if err != nil {
		return nil, fmt.Errorf("failed to get current season: %w", err)
	}

	// Get current progress
	progress, err := p.GetPlayerProgress(playerID, currentSeason.ID)
	if err != nil {
		return nil, fmt.Errorf("failed to get player progress: %w", err)
	}

	// Calculate new XP
	newTotalXP := progress.TotalXP + grant.Amount
	newCurrentXP := progress.CurrentXP + grant.Amount

	// Calculate level progression
	levelUps := 0
	xpForNextLevel := progress.XpToNextLevel
	currentLevel := progress.CurrentLevel

	for newCurrentXP >= xpForNextLevel && currentLevel < currentSeason.MaxLevel {
		levelUps++
		newCurrentXP -= xpForNextLevel
		currentLevel++

		// Calculate XP required for next level (increasing by 10% each level)
		xpForNextLevel = int(float64(xpForNextLevel) * 1.1)
	}

	// Update progress in database
	query := `
		UPDATE player_progress
		SET current_level = $1, current_xp = $2, total_xp = $3, xp_to_next_level = $4, last_updated = NOW()
		WHERE player_id = $5 AND season_id = $6
	`

	_, err = p.db.Exec(query, currentLevel, newCurrentXP, newTotalXP, xpForNextLevel, playerID, currentSeason.ID)
	if err != nil {
		p.logger.Error("Failed to update player progress",
			zap.String("playerID", playerID), zap.Error(err))
		return nil, fmt.Errorf("failed to update player progress: %w", err)
	}

	// Check for unlocked rewards
	var rewardsUnlocked []models.Reward
	if levelUps > 0 {
		rewards, err := p.getUnlockedRewards(playerID, currentSeason.ID, progress.CurrentLevel, currentLevel)
		if err != nil {
			p.logger.Warn("Failed to get unlocked rewards", zap.Error(err))
		} else {
			rewardsUnlocked = rewards
		}
	}

	result := &models.XPGrantResult{
		NewLevel:        currentLevel,
		XPGained:        grant.Amount,
		RewardsUnlocked: rewardsUnlocked,
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("progress:%s:%s", playerID, currentSeason.ID)
	p.redis.Del(p.redis.Context(), cacheKey)

	p.logger.Info("XP granted to player",
		zap.String("playerID", playerID),
		zap.Int("xpGranted", grant.Amount),
		zap.Int("newLevel", currentLevel),
		zap.Int("rewardsUnlocked", len(rewardsUnlocked)))

	return result, nil
}

// initializePlayerProgress creates initial progress for a new player
func (p *ProgressService) initializePlayerProgress(playerID, seasonID string) (*models.PlayerProgress, error) {
	progress := &models.PlayerProgress{
		PlayerID:      playerID,
		SeasonID:      seasonID,
		CurrentLevel:  1,
		CurrentXP:     0,
		TotalXP:       0,
		XpToNextLevel: 100, // Starting XP requirement
		HasPremium:    false,
	}

	query := `
		INSERT INTO player_progress (player_id, season_id, current_level, current_xp, total_xp, xp_to_next_level, has_premium)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
		ON CONFLICT (player_id, season_id) DO NOTHING
	`

	_, err := p.db.Exec(query, progress.PlayerID, progress.SeasonID, progress.CurrentLevel,
		progress.CurrentXP, progress.TotalXP, progress.XpToNextLevel, progress.HasPremium)

	if err != nil {
		p.logger.Error("Failed to initialize player progress",
			zap.String("playerID", playerID), zap.String("seasonID", seasonID), zap.Error(err))
		return nil, fmt.Errorf("failed to initialize player progress: %w", err)
	}

	return progress, nil
}

// getCurrentSeason gets the current active season
func (p *ProgressService) getCurrentSeason() (*models.Season, error) {
	var season models.Season
	query := `
		SELECT id, name, description, start_date, end_date, max_level, status, created_at, updated_at
		FROM seasons
		WHERE status = 'active'
		ORDER BY start_date DESC
		LIMIT 1
	`

	err := p.db.QueryRow(query).Scan(
		&season.ID, &season.Name, &season.Description,
		&season.StartDate, &season.EndDate, &season.MaxLevel,
		&season.Status, &season.CreatedAt, &season.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get current season: %w", err)
	}

	return &season, nil
}

// getUnlockedRewards returns rewards unlocked between old and new levels
func (p *ProgressService) getUnlockedRewards(playerID, seasonID string, oldLevel, newLevel int) ([]models.Reward, error) {
	query := `
		SELECT r.id, r.type, r.name, r.description, r.rarity, r.metadata, r.created_at
		FROM season_rewards sr
		JOIN rewards r ON (sr.free_reward_id = r.id OR sr.premium_reward_id = r.id)
		LEFT JOIN claimed_rewards cr ON cr.player_id = $1 AND cr.season_id = $2 AND cr.level = sr.level AND cr.reward_id = r.id
		WHERE sr.season_id = $2
		AND sr.level > $3 AND sr.level <= $4
		AND cr.reward_id IS NULL
	`

	rows, err := p.db.Query(query, playerID, seasonID, oldLevel, newLevel)
	if err != nil {
		return nil, fmt.Errorf("failed to get unlocked rewards: %w", err)
	}
	defer rows.Close()

	var rewards []models.Reward
	for rows.Next() {
		var reward models.Reward
		err := rows.Scan(&reward.ID, &reward.Type, &reward.Name, &reward.Description,
			&reward.Rarity, &reward.Metadata, &reward.CreatedAt)
		if err != nil {
			continue
		}
		rewards = append(rewards, reward)
	}

	return rewards, nil
}