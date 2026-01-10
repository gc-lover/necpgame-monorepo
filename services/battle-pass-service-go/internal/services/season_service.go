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