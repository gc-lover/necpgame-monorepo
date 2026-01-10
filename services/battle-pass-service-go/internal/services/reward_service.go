package services

import (
	"database/sql"
	"fmt"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"battle-pass-service-go/internal/clients"
	"battle-pass-service-go/internal/models"
)

// RewardService handles reward-related business logic
type RewardService struct {
	db              *sql.DB
	redis           *redis.Client
	logger          *zap.Logger
	inventoryClient *clients.InventoryClient
	economyClient   *clients.EconomyClient
	playerClient    *clients.PlayerClient
}

// NewRewardService creates a new RewardService instance
func NewRewardService(db *sql.DB, redis *redis.Client, logger *zap.Logger,
	inventoryClient *clients.InventoryClient, economyClient *clients.EconomyClient,
	playerClient *clients.PlayerClient) *RewardService {
	return &RewardService{
		db:              db,
		redis:           redis,
		logger:          logger,
		inventoryClient: inventoryClient,
		economyClient:   economyClient,
		playerClient:    playerClient,
	}
}

// ClaimReward claims a reward for a player
func (r *RewardService) ClaimReward(playerID string, request models.ClaimRequest) (*models.ClaimResult, error) {
	// Get current season
	currentSeason, err := r.getCurrentSeason()
	if err != nil {
		return nil, fmt.Errorf("failed to get current season: %w", err)
	}

	// Check if reward is available
	reward, tier, err := r.getRewardForLevel(currentSeason.ID, request.Level, request.Tier)
	if err != nil {
		return nil, fmt.Errorf("failed to get reward for level: %w", err)
	}

	// Check if player has reached the level
	progress, err := r.getPlayerProgress(playerID, currentSeason.ID)
	if err != nil {
		return nil, fmt.Errorf("failed to get player progress: %w", err)
	}

	// Check level requirement
	if progress.CurrentLevel < request.Level {
		return &models.ClaimResult{
			Success: false,
		}, fmt.Errorf("player level too low")
	}

	// Check premium requirement
	if request.Tier == "premium" && !progress.HasPremium {
		return &models.ClaimResult{
			Success: false,
		}, fmt.Errorf("premium pass required")
	}

	// Check if already claimed
	alreadyClaimed, err := r.isRewardClaimed(playerID, currentSeason.ID, request.Level, request.Tier)
	if err != nil {
		return nil, fmt.Errorf("failed to check claim status: %w", err)
	}

	if alreadyClaimed {
		return &models.ClaimResult{
			Success: false,
		}, fmt.Errorf("reward already claimed")
	}

	// Add to inventory (external service call would go here)
	inventoryID, err := r.addToInventory(playerID, reward)
	if err != nil {
		r.logger.Error("Failed to add reward to inventory",
			zap.String("playerID", playerID), zap.String("rewardID", reward.ID), zap.Error(err))
		return nil, fmt.Errorf("failed to add reward to inventory: %w", err)
	}

	// Record the claim
	err = r.recordClaim(playerID, currentSeason.ID, request.Level, request.Tier, reward.ID, inventoryID)
	if err != nil {
		r.logger.Error("Failed to record claim",
			zap.String("playerID", playerID), zap.Error(err))
		return nil, fmt.Errorf("failed to record claim: %w", err)
	}

	result := &models.ClaimResult{
		Success:     true,
		Reward:      *reward,
		InventoryID: &inventoryID,
	}

	r.logger.Info("Reward claimed successfully",
		zap.String("playerID", playerID),
		zap.Int("level", request.Level),
		zap.String("tier", request.Tier),
		zap.String("rewardID", reward.ID))

	return result, nil
}

// GetCurrentSeason returns the currently active season
func (r *RewardService) GetCurrentSeason() (*models.Season, error) {
	var season models.Season
	query := `
		SELECT id, name, description, start_date, end_date, max_level, status, created_at, updated_at
		FROM seasons
		WHERE status = 'active'
		ORDER BY start_date DESC
		LIMIT 1
	`

	err := r.db.QueryRow(query).Scan(
		&season.ID, &season.Name, &season.Description,
		&season.StartDate, &season.EndDate, &season.MaxLevel,
		&season.Status, &season.CreatedAt, &season.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("no active season found")
		}
		r.logger.Error("Failed to get current season", zap.Error(err))
		return nil, fmt.Errorf("failed to get current season: %w", err)
	}

	return &season, nil
}

// GetAvailableRewards returns all rewards available for claiming by a player
func (r *RewardService) GetAvailableRewards(playerID, seasonID string) ([]models.AvailableReward, error) {
	// Get player progress
	progress, err := r.getPlayerProgress(playerID, seasonID)
	if err != nil {
		return nil, fmt.Errorf("failed to get player progress: %w", err)
	}

	// Get all season rewards
	seasonRewards, err := r.getSeasonRewards(seasonID)
	if err != nil {
		return nil, fmt.Errorf("failed to get season rewards: %w", err)
	}

	var availableRewards []models.AvailableReward

	for _, sr := range seasonRewards {
		// Check free reward
		if sr.FreeRewardID != "" {
			canClaim, reason := r.canClaimReward(playerID, seasonID, sr.Level, "free", progress)
			reward, _ := r.getReward(sr.FreeRewardID)

			availableRewards = append(availableRewards, models.AvailableReward{
				Level:    sr.Level,
				Tier:     "free",
				Reward:   *reward,
				CanClaim: canClaim,
				Reason:   reason,
			})
		}

		// Check premium reward
		if sr.PremiumRewardID != nil && *sr.PremiumRewardID != "" {
			canClaim, reason := r.canClaimReward(playerID, seasonID, sr.Level, "premium", progress)
			reward, _ := r.getReward(*sr.PremiumRewardID)

			availableRewards = append(availableRewards, models.AvailableReward{
				Level:    sr.Level,
				Tier:     "premium",
				Reward:   *reward,
				CanClaim: canClaim,
				Reason:   reason,
			})
		}
	}

	return availableRewards, nil
}

// getRewardForLevel returns the reward for a specific level and tier
func (r *RewardService) getRewardForLevel(seasonID string, level int, tier string) (*models.Reward, string, error) {
	var rewardID string
	query := `
		SELECT CASE WHEN $3 = 'free' THEN free_reward_id ELSE premium_reward_id END
		FROM season_rewards
		WHERE season_id = $1 AND level = $2
	`

	err := r.db.QueryRow(query, seasonID, level, tier).Scan(&rewardID)
	if err != nil {
		return nil, "", fmt.Errorf("failed to get reward ID: %w", err)
	}

	if rewardID == "" {
		return nil, "", fmt.Errorf("no reward found for level %d tier %s", level, tier)
	}

	reward, err := r.getReward(rewardID)
	return reward, tier, err
}

// getReward returns a reward by ID
func (r *RewardService) getReward(rewardID string) (*models.Reward, error) {
	var reward models.Reward
	query := `
		SELECT id, type, name, description, rarity, metadata, created_at
		FROM rewards
		WHERE id = $1
	`

	err := r.db.QueryRow(query, rewardID).Scan(
		&reward.ID, &reward.Type, &reward.Name, &reward.Description,
		&reward.Rarity, &reward.Metadata, &reward.CreatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get reward: %w", err)
	}

	return &reward, nil
}

// canClaimReward checks if a player can claim a specific reward
func (r *RewardService) canClaimReward(playerID, seasonID string, level int, tier string, progress *models.PlayerProgress) (bool, string) {
	// Check level requirement
	if progress.CurrentLevel < level {
		return false, "Insufficient level"
	}

	// Check premium requirement
	if tier == "premium" && !progress.HasPremium {
		return false, "Premium pass required"
	}

	// Check if already claimed
	claimed, err := r.isRewardClaimed(playerID, seasonID, level, tier)
	if err != nil {
		r.logger.Warn("Failed to check claim status", zap.Error(err))
		return false, "Error checking claim status"
	}

	if claimed {
		return false, "Already claimed"
	}

	return true, ""
}

// isRewardClaimed checks if a reward has already been claimed
func (r *RewardService) isRewardClaimed(playerID, seasonID string, level int, tier string) (bool, error) {
	var count int
	query := `
		SELECT COUNT(*)
		FROM claimed_rewards
		WHERE player_id = $1 AND season_id = $2 AND level = $3 AND tier = $4
	`

	err := r.db.QueryRow(query, playerID, seasonID, level, tier).Scan(&count)
	if err != nil {
		return false, fmt.Errorf("failed to check claim status: %w", err)
	}

	return count > 0, nil
}

// getPlayerProgress gets player progress (simplified version)
func (r *RewardService) getPlayerProgress(playerID, seasonID string) (*models.PlayerProgress, error) {
	var progress models.PlayerProgress
	query := `
		SELECT player_id, season_id, current_level, current_xp, total_xp, xp_to_next_level, has_premium, last_updated
		FROM player_progress
		WHERE player_id = $1 AND season_id = $2
	`

	err := r.db.QueryRow(query, playerID, seasonID).Scan(
		&progress.PlayerID, &progress.SeasonID, &progress.CurrentLevel,
		&progress.CurrentXP, &progress.TotalXP, &progress.XpToNextLevel,
		&progress.HasPremium, &progress.LastUpdated,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get player progress: %w", err)
	}

	return &progress, nil
}

// getCurrentSeason gets the current active season
func (r *RewardService) getCurrentSeason() (*models.Season, error) {
	var season models.Season
	query := `
		SELECT id, name, description, start_date, end_date, max_level, status, created_at, updated_at
		FROM seasons
		WHERE status = 'active'
		ORDER BY start_date DESC
		LIMIT 1
	`

	err := r.db.QueryRow(query).Scan(
		&season.ID, &season.Name, &season.Description,
		&season.StartDate, &season.EndDate, &season.MaxLevel,
		&season.Status, &season.CreatedAt, &season.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get current season: %w", err)
	}

	return &season, nil
}

// getSeasonRewards gets all rewards for a season
func (r *RewardService) getSeasonRewards(seasonID string) ([]models.SeasonReward, error) {
	query := `
		SELECT season_id, level, free_reward_id, premium_reward_id, xp_required
		FROM season_rewards
		WHERE season_id = $1
		ORDER BY level ASC
	`

	rows, err := r.db.Query(query, seasonID)
	if err != nil {
		return nil, fmt.Errorf("failed to get season rewards: %w", err)
	}
	defer rows.Close()

	var rewards []models.SeasonReward
	for rows.Next() {
		var reward models.SeasonReward
		err := rows.Scan(
			&reward.SeasonID, &reward.Level, &reward.FreeRewardID,
			&reward.PremiumRewardID, &reward.XpRequired,
		)
		if err != nil {
			continue
		}
		rewards = append(rewards, reward)
	}

	return rewards, nil
}

// addToInventory adds a reward to player inventory via Inventory Service
func (r *RewardService) addToInventory(playerID string, reward *models.Reward) (string, error) {
	if r.inventoryClient == nil {
		// Fallback for testing or when service is unavailable
		r.logger.Warn("Inventory client not available, using fallback")
		inventoryID := fmt.Sprintf("inv_%s_%s", playerID, reward.ID)
		return inventoryID, nil
	}

	metadata := make(map[string]interface{})
	if reward.Metadata != nil {
		metadata = reward.Metadata
	}
	metadata["reward_type"] = reward.Type
	metadata["rarity"] = reward.Rarity

	inventoryID, err := r.inventoryClient.GrantReward(playerID, reward.Type, reward.ID, metadata)
	if err != nil {
		r.logger.Error("Failed to add reward to inventory",
			zap.String("playerID", playerID),
			zap.String("rewardID", reward.ID),
			zap.Error(err))
		return "", fmt.Errorf("failed to add reward to inventory: %w", err)
	}

	return inventoryID, nil
}

// recordClaim records a reward claim in the database
func (r *RewardService) recordClaim(playerID, seasonID string, level int, tier, rewardID, inventoryID string) error {
	query := `
		INSERT INTO claimed_rewards (player_id, season_id, level, tier, reward_id, inventory_id, claimed_at)
		VALUES ($1, $2, $3, $4, $5, $6, NOW())
	`

	_, err := r.db.Exec(query, playerID, seasonID, level, tier, rewardID, inventoryID)
	if err != nil {
		return fmt.Errorf("failed to record claim: %w", err)
	}

	return nil
}

// CreateReward creates a new reward
func (r *RewardService) CreateReward(reward *models.Reward) error {
	// Validate reward data
	if reward.ID == "" {
		return fmt.Errorf("reward ID is required")
	}
	if reward.Type == "" {
		return fmt.Errorf("reward type is required")
	}
	if reward.Name == "" {
		return fmt.Errorf("reward name is required")
	}

	query := `
		INSERT INTO rewards (id, type, name, description, rarity, metadata, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, NOW())
	`

	_, err := r.db.Exec(query, reward.ID, reward.Type, reward.Name, reward.Description, reward.Rarity, reward.Metadata)
	if err != nil {
		r.logger.Error("Failed to create reward", zap.String("rewardID", reward.ID), zap.Error(err))
		return fmt.Errorf("failed to create reward: %w", err)
	}

	r.logger.Info("Reward created successfully", zap.String("rewardID", reward.ID))
	return nil
}

// UpdateReward updates an existing reward
func (r *RewardService) UpdateReward(rewardID string, updates map[string]interface{}) error {
	if len(updates) == 0 {
		return fmt.Errorf("no updates provided")
	}

	// Build dynamic update query
	query := "UPDATE rewards SET"
	args := []interface{}{}
	argCount := 1

	for field, value := range updates {
		if argCount > 1 {
			query += ","
		}
		query += fmt.Sprintf(" %s = $%d", field, argCount)
		args = append(args, value)
		argCount++
	}

	query += fmt.Sprintf(" WHERE id = $%d", argCount)
	args = append(args, rewardID)

	_, err := r.db.Exec(query, args...)
	if err != nil {
		r.logger.Error("Failed to update reward", zap.String("rewardID", rewardID), zap.Error(err))
		return fmt.Errorf("failed to update reward: %w", err)
	}

	r.logger.Info("Reward updated successfully", zap.String("rewardID", rewardID))
	return nil
}

// DeleteReward deletes a reward (admin function)
func (r *RewardService) DeleteReward(rewardID string) error {
	// Check if reward is being used in any season
	var count int
	checkQuery := "SELECT COUNT(*) FROM season_rewards WHERE free_reward_id = $1 OR premium_reward_id = $1"
	err := r.db.QueryRow(checkQuery, rewardID).Scan(&count)
	if err != nil {
		return fmt.Errorf("failed to check reward usage: %w", err)
	}

	if count > 0 {
		return fmt.Errorf("cannot delete reward that is used in seasons")
	}

	// Delete the reward
	query := "DELETE FROM rewards WHERE id = $1"
	result, err := r.db.Exec(query, rewardID)
	if err != nil {
		r.logger.Error("Failed to delete reward", zap.String("rewardID", rewardID), zap.Error(err))
		return fmt.Errorf("failed to delete reward: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("reward not found")
	}

	r.logger.Info("Reward deleted successfully", zap.String("rewardID", rewardID))
	return nil
}

// ListRewards returns all rewards with pagination
func (r *RewardService) ListRewards(limit, offset int, rewardType string) ([]models.Reward, error) {
	query := `
		SELECT id, type, name, description, rarity, metadata, created_at
		FROM rewards
		WHERE ($1 = '' OR type = $1)
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.Query(query, rewardType, limit, offset)
	if err != nil {
		r.logger.Error("Failed to list rewards", zap.Error(err))
		return nil, fmt.Errorf("failed to list rewards: %w", err)
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

// GetClaimHistory returns claim history for a player
func (r *RewardService) GetClaimHistory(playerID, seasonID string, limit, offset int) ([]models.ClaimedReward, error) {
	query := `
		SELECT player_id, season_id, level, tier, reward_id, claimed_at, inventory_id
		FROM claimed_rewards
		WHERE player_id = $1 AND ($2 = '' OR season_id = $2)
		ORDER BY claimed_at DESC
		LIMIT $3 OFFSET $4
	`

	rows, err := r.db.Query(query, playerID, seasonID, limit, offset)
	if err != nil {
		r.logger.Error("Failed to get claim history",
			zap.String("playerID", playerID), zap.String("seasonID", seasonID), zap.Error(err))
		return nil, fmt.Errorf("failed to get claim history: %w", err)
	}
	defer rows.Close()

	var claims []models.ClaimedReward
	for rows.Next() {
		var claim models.ClaimedReward
		err := rows.Scan(&claim.PlayerID, &claim.SeasonID, &claim.Level, &claim.Tier,
			&claim.RewardID, &claim.ClaimedAt, &claim.InventoryID)
		if err != nil {
			continue
		}
		claims = append(claims, claim)
	}

	return claims, nil
}

// ValidateRewardClaim validates if a reward claim is valid (business logic check)
func (r *RewardService) ValidateRewardClaim(playerID string, request models.ClaimRequest) error {
	// Get current season
	currentSeason, err := r.getCurrentSeason()
	if err != nil {
		return fmt.Errorf("failed to get current season: %w", err)
	}

	// Check if reward exists at this level
	reward, tier, err := r.getRewardForLevel(currentSeason.ID, request.Level, request.Tier)
	if err != nil {
		return fmt.Errorf("invalid reward level/tier combination: %w", err)
	}
	_ = reward // reward is not used in validation but could be for additional checks
	_ = tier

	// Check player progress
	progress, err := r.getPlayerProgress(playerID, currentSeason.ID)
	if err != nil {
		return fmt.Errorf("failed to get player progress: %w", err)
	}

	// Validate level requirement
	if progress.CurrentLevel < request.Level {
		return fmt.Errorf("insufficient level: player level %d, required level %d",
			progress.CurrentLevel, request.Level)
	}

	// Validate premium requirement
	if request.Tier == "premium" && !progress.HasPremium {
		return fmt.Errorf("premium pass required")
	}

	// Check if already claimed
	alreadyClaimed, err := r.isRewardClaimed(playerID, currentSeason.ID, request.Level, request.Tier)
	if err != nil {
		return fmt.Errorf("failed to check claim status: %w", err)
	}

	if alreadyClaimed {
		return fmt.Errorf("reward already claimed")
	}

	return nil
}