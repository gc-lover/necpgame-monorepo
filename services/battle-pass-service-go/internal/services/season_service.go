package services

import (
	"database/sql"
	"fmt"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"battle-pass-service-go/internal/models"
)

// SeasonService handles season-related business logic
type SeasonService struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.Logger
}

// NewSeasonService creates a new SeasonService instance
func NewSeasonService(db *sql.DB, redis *redis.Client, logger *zap.Logger) *SeasonService {
	return &SeasonService{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// GetCurrentSeason returns the currently active season
func (s *SeasonService) GetCurrentSeason() (*models.Season, error) {
	var season models.Season
	query := `
		SELECT id, name, description, start_date, end_date, max_level, status, created_at, updated_at
		FROM seasons
		WHERE status = 'active'
		ORDER BY start_date DESC
		LIMIT 1
	`

	err := s.db.QueryRow(query).Scan(
		&season.ID, &season.Name, &season.Description,
		&season.StartDate, &season.EndDate, &season.MaxLevel,
		&season.Status, &season.CreatedAt, &season.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("no active season found")
		}
		s.logger.Error("Failed to get current season", zap.Error(err))
		return nil, fmt.Errorf("failed to get current season: %w", err)
	}

	return &season, nil
}

// GetSeason returns a season by ID
func (s *SeasonService) GetSeason(seasonID string) (*models.Season, error) {
	var season models.Season
	query := `
		SELECT id, name, description, start_date, end_date, max_level, status, created_at, updated_at
		FROM seasons
		WHERE id = $1
	`

	err := s.db.QueryRow(query, seasonID).Scan(
		&season.ID, &season.Name, &season.Description,
		&season.StartDate, &season.EndDate, &season.MaxLevel,
		&season.Status, &season.CreatedAt, &season.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("season not found: %s", seasonID)
		}
		s.logger.Error("Failed to get season", zap.String("seasonID", seasonID), zap.Error(err))
		return nil, fmt.Errorf("failed to get season: %w", err)
	}

	return &season, nil
}

// ListSeasons returns all seasons with pagination
func (s *SeasonService) ListSeasons(limit, offset int) ([]models.Season, error) {
	query := `
		SELECT id, name, description, start_date, end_date, max_level, status, created_at, updated_at
		FROM seasons
		ORDER BY start_date DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := s.db.Query(query, limit, offset)
	if err != nil {
		s.logger.Error("Failed to list seasons", zap.Error(err))
		return nil, fmt.Errorf("failed to list seasons: %w", err)
	}
	defer rows.Close()

	var seasons []models.Season
	for rows.Next() {
		var season models.Season
		err := rows.Scan(
			&season.ID, &season.Name, &season.Description,
			&season.StartDate, &season.EndDate, &season.MaxLevel,
			&season.Status, &season.CreatedAt, &season.UpdatedAt,
		)
		if err != nil {
			s.logger.Error("Failed to scan season", zap.Error(err))
			continue
		}
		seasons = append(seasons, season)
	}

	return seasons, nil
}

// GetSeasonRewards returns all rewards for a season
func (s *SeasonService) GetSeasonRewards(seasonID string) ([]models.SeasonReward, error) {
	query := `
		SELECT season_id, level, free_reward_id, premium_reward_id, xp_required
		FROM season_rewards
		WHERE season_id = $1
		ORDER BY level ASC
	`

	rows, err := s.db.Query(query, seasonID)
	if err != nil {
		s.logger.Error("Failed to get season rewards", zap.String("seasonID", seasonID), zap.Error(err))
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
			s.logger.Error("Failed to scan season reward", zap.Error(err))
			continue
		}
		rewards = append(rewards, reward)
	}

	return rewards, nil
}

// CreateSeason creates a new season
func (s *SeasonService) CreateSeason(season *models.Season) error {
	// Validate season data
	if season.Name == "" {
		return fmt.Errorf("season name is required")
	}
	if season.StartDate.IsZero() || season.EndDate.IsZero() {
		return fmt.Errorf("season start and end dates are required")
	}
	if season.StartDate.After(season.EndDate) {
		return fmt.Errorf("season start date cannot be after end date")
	}
	if season.MaxLevel <= 0 {
		return fmt.Errorf("season max level must be positive")
	}

	query := `
		INSERT INTO seasons (id, name, description, start_date, end_date, max_level, status, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, NOW(), NOW())
	`

	_, err := s.db.Exec(query, season.ID, season.Name, season.Description,
		season.StartDate, season.EndDate, season.MaxLevel, season.Status)

	if err != nil {
		s.logger.Error("Failed to create season", zap.String("seasonID", season.ID), zap.Error(err))
		return fmt.Errorf("failed to create season: %w", err)
	}

	s.logger.Info("Season created successfully", zap.String("seasonID", season.ID))
	return nil
}

// UpdateSeason updates an existing season
func (s *SeasonService) UpdateSeason(seasonID string, updates map[string]interface{}) error {
	if len(updates) == 0 {
		return fmt.Errorf("no updates provided")
	}

	// Build dynamic update query
	query := "UPDATE seasons SET updated_at = NOW()"
	args := []interface{}{}
	argCount := 1

	for field, value := range updates {
		query += fmt.Sprintf(", %s = $%d", field, argCount)
		args = append(args, value)
		argCount++
	}

	query += fmt.Sprintf(" WHERE id = $%d", argCount)
	args = append(args, seasonID)

	_, err := s.db.Exec(query, args...)
	if err != nil {
		s.logger.Error("Failed to update season", zap.String("seasonID", seasonID), zap.Error(err))
		return fmt.Errorf("failed to update season: %w", err)
	}

	s.logger.Info("Season updated successfully", zap.String("seasonID", seasonID))
	return nil
}

// ActivateSeason sets a season as active and deactivates others
func (s *SeasonService) ActivateSeason(seasonID string) error {
	// Start transaction
	tx, err := s.db.Begin()
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	// Deactivate all seasons
	_, err = tx.Exec("UPDATE seasons SET status = 'inactive', updated_at = NOW() WHERE status = 'active'")
	if err != nil {
		s.logger.Error("Failed to deactivate current season", zap.Error(err))
		return fmt.Errorf("failed to deactivate current season: %w", err)
	}

	// Activate the specified season
	_, err = tx.Exec("UPDATE seasons SET status = 'active', updated_at = NOW() WHERE id = $1", seasonID)
	if err != nil {
		s.logger.Error("Failed to activate season", zap.String("seasonID", seasonID), zap.Error(err))
		return fmt.Errorf("failed to activate season: %w", err)
	}

	// Commit transaction
	if err = tx.Commit(); err != nil {
		s.logger.Error("Failed to commit season activation", zap.Error(err))
		return fmt.Errorf("failed to commit season activation: %w", err)
	}

	// Invalidate cache
	cacheKey := "current_season"
	s.redis.Del(s.redis.Context(), cacheKey)

	s.logger.Info("Season activated successfully", zap.String("seasonID", seasonID))
	return nil
}

// EndSeason marks a season as ended
func (s *SeasonService) EndSeason(seasonID string) error {
	query := "UPDATE seasons SET status = 'ended', updated_at = NOW() WHERE id = $1 AND status = 'active'"

	result, err := s.db.Exec(query, seasonID)
	if err != nil {
		s.logger.Error("Failed to end season", zap.String("seasonID", seasonID), zap.Error(err))
		return fmt.Errorf("failed to end season: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("season not found or not active")
	}

	s.logger.Info("Season ended successfully", zap.String("seasonID", seasonID))
	return nil
}

// SetSeasonRewards sets rewards for a season
func (s *SeasonService) SetSeasonRewards(seasonID string, rewards []models.SeasonReward) error {
	// Start transaction
	tx, err := s.db.Begin()
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	// Delete existing rewards
	_, err = tx.Exec("DELETE FROM season_rewards WHERE season_id = $1", seasonID)
	if err != nil {
		s.logger.Error("Failed to delete existing season rewards", zap.String("seasonID", seasonID), zap.Error(err))
		return fmt.Errorf("failed to delete existing season rewards: %w", err)
	}

	// Insert new rewards
	for _, reward := range rewards {
		_, err = tx.Exec(`
			INSERT INTO season_rewards (season_id, level, free_reward_id, premium_reward_id, xp_required)
			VALUES ($1, $2, $3, $4, $5)
		`, seasonID, reward.Level, reward.FreeRewardID, reward.PremiumRewardID, reward.XpRequired)

		if err != nil {
			s.logger.Error("Failed to insert season reward",
				zap.String("seasonID", seasonID), zap.Int("level", reward.Level), zap.Error(err))
			return fmt.Errorf("failed to insert season reward: %w", err)
		}
	}

	// Commit transaction
	if err = tx.Commit(); err != nil {
		s.logger.Error("Failed to commit season rewards", zap.Error(err))
		return fmt.Errorf("failed to commit season rewards: %w", err)
	}

	s.logger.Info("Season rewards set successfully", zap.String("seasonID", seasonID), zap.Int("rewardCount", len(rewards)))
	return nil
}