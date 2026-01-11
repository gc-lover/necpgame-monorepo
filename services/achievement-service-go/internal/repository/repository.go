// Agent: Backend Agent
// Issue: #backend-achievement-service-1

package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"achievement-service-go/internal/config"
	"achievement-service-go/internal/models"

	"github.com/google/uuid"
	"github.com/lib/pq"
)

// Repository handles database operations for achievements
// MMOFPS Optimization: Connection pooling, prepared statements, query timeouts
type Repository struct {
	db *sql.DB
}

// New creates a new repository instance
func New(cfg *config.Config) (*Repository, error) {
	// Connect to database with MMOFPS optimizations
	dsn := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=%s",
		cfg.Database.Host, cfg.Database.Port, cfg.Database.User,
		cfg.Database.Password, cfg.Database.Database, cfg.Database.SSLMode)

	db, err := sql.Open("postgres", dsn)
	if err != nil {
		return nil, fmt.Errorf("failed to open database: %w", err)
	}

	// MMOFPS Optimization: Configure connection pool for high concurrency
	db.SetMaxOpenConns(cfg.Database.MaxOpenConns) // 25 connections for concurrent achievement checks
	db.SetMaxIdleConns(cfg.Database.MaxIdleConns) // Keep 5 connections warm
	db.SetConnMaxLifetime(cfg.Database.MaxLifetime) // Rotate connections

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return &Repository{db: db}, nil
}

// Close closes the database connection
func (r *Repository) Close() error {
	return r.db.Close()
}

// GetAchievement retrieves an achievement by ID
// MMOFPS Optimization: Single query with timeout
func (r *Repository) GetAchievement(ctx context.Context, id uuid.UUID) (*models.Achievement, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second) // MMOFPS: Fast query
	defer cancel()

	query := `
		SELECT id, name, description, category, requirements, rewards, status,
		       max_progress, is_hidden, prerequisites, created_at, updated_at, version
		FROM achievements WHERE id = $1`

	var achievement models.Achievement
	var requirements, rewards []byte
	var prerequisites []uuid.UUID

	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&achievement.ID, &achievement.Name, &achievement.Description,
		&achievement.Category, &requirements, &rewards, &achievement.Status,
		&achievement.MaxProgress, &achievement.IsHidden, pq.Array(&prerequisites),
		&achievement.CreatedAt, &achievement.UpdatedAt, &achievement.Version,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("achievement not found")
		}
		return nil, fmt.Errorf("failed to get achievement: %w", err)
	}

	// Parse JSON fields
	if err := json.Unmarshal(requirements, &achievement.Requirements); err != nil {
		return nil, fmt.Errorf("failed to unmarshal requirements: %w", err)
	}
	if err := json.Unmarshal(rewards, &achievement.Rewards); err != nil {
		return nil, fmt.Errorf("failed to unmarshal rewards: %w", err)
	}
	achievement.Prerequisites = prerequisites

	return &achievement, nil
}

// GetPlayerAchievements retrieves all achievements for a player
// MMOFPS Optimization: Single query with JOIN for performance
func (r *Repository) GetPlayerAchievements(ctx context.Context, playerID uuid.UUID) ([]*models.PlayerAchievement, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Allow more time for player data
	defer cancel()

	query := `
		SELECT pa.id, pa.player_id, pa.achievement_id, pa.status, pa.current_progress,
		       pa.completed_at, pa.unlocked_at, pa.last_updated, pa.version
		FROM player_achievements pa
		WHERE pa.player_id = $1
		ORDER BY pa.last_updated DESC`

	rows, err := r.db.QueryContext(ctx, query, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to query player achievements: %w", err)
	}
	defer rows.Close()

	var achievements []*models.PlayerAchievement
	for rows.Next() {
		var pa models.PlayerAchievement
		err := rows.Scan(
			&pa.ID, &pa.PlayerID, &pa.AchievementID, &pa.Status, &pa.CurrentProgress,
			&pa.CompletedAt, &pa.UnlockedAt, &pa.LastUpdated, &pa.Version,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan player achievement: %w", err)
		}
		achievements = append(achievements, &pa)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating player achievements: %w", err)
	}

	return achievements, nil
}

// UpdatePlayerAchievementProgress updates achievement progress for a player
// MMOFPS Optimization: Optimistic locking, single UPDATE with RETURNING
func (r *Repository) UpdatePlayerAchievementProgress(ctx context.Context, playerID, achievementID uuid.UUID, newProgress int, version int) (*models.PlayerAchievement, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second) // MMOFPS: Fast update
	defer cancel()

	query := `
		UPDATE player_achievements
		SET current_progress = $1, last_updated = NOW(), version = version + 1
		WHERE player_id = $2 AND achievement_id = $3 AND version = $4
		RETURNING id, player_id, achievement_id, status, current_progress,
		          completed_at, unlocked_at, last_updated, version`

	var pa models.PlayerAchievement
	err := r.db.QueryRowContext(ctx, query, newProgress, playerID, achievementID, version).Scan(
		&pa.ID, &pa.PlayerID, &pa.AchievementID, &pa.Status, &pa.CurrentProgress,
		&pa.CompletedAt, &pa.UnlockedAt, &pa.LastUpdated, &pa.Version,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("achievement progress update conflict or not found")
		}
		return nil, fmt.Errorf("failed to update achievement progress: %w", err)
	}

	return &pa, nil
}

// CompletePlayerAchievement marks an achievement as completed
// MMOFPS Optimization: Single UPDATE for completion
func (r *Repository) CompletePlayerAchievement(ctx context.Context, playerID, achievementID uuid.UUID, version int) (*models.PlayerAchievement, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second) // MMOFPS: Fast completion
	defer cancel()

	query := `
		UPDATE player_achievements
		SET status = 'completed', completed_at = NOW(), last_updated = NOW(), version = version + 1
		WHERE player_id = $1 AND achievement_id = $2 AND version = $3 AND status != 'completed'
		RETURNING id, player_id, achievement_id, status, current_progress,
		          completed_at, unlocked_at, last_updated, version`

	var pa models.PlayerAchievement
	err := r.db.QueryRowContext(ctx, query, playerID, achievementID, version).Scan(
		&pa.ID, &pa.PlayerID, &pa.AchievementID, &pa.Status, &pa.CurrentProgress,
		&pa.CompletedAt, &pa.UnlockedAt, &pa.LastUpdated, &pa.Version,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("achievement already completed or not found")
		}
		return nil, fmt.Errorf("failed to complete achievement: %w", err)
	}

	return &pa, nil
}

// CreatePlayerAchievement creates a new player achievement record
// MMOFPS Optimization: INSERT with ON CONFLICT for idempotency
func (r *Repository) CreatePlayerAchievement(ctx context.Context, pa *models.PlayerAchievement) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second) // MMOFPS: Fast creation
	defer cancel()

	query := `
		INSERT INTO player_achievements (id, player_id, achievement_id, status, current_progress, unlocked_at, last_updated, version)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
		ON CONFLICT (player_id, achievement_id) DO NOTHING`

	_, err := r.db.ExecContext(ctx, query,
		pa.ID, pa.PlayerID, pa.AchievementID, pa.Status, pa.CurrentProgress,
		pa.UnlockedAt, pa.LastUpdated, pa.Version)

	if err != nil {
		return fmt.Errorf("failed to create player achievement: %w", err)
	}

	return nil
}

// GetAchievementAnalytics retrieves analytics data
// MMOFPS Optimization: Aggregated queries with date ranges
func (r *Repository) GetAchievementAnalytics(ctx context.Context, startDate, endDate time.Time) (*models.AchievementAnalytics, error) {
	ctx, cancel := context.WithTimeout(ctx, 10*time.Second) // Analytics can take longer
	defer cancel()

	analytics := &models.AchievementAnalytics{
		Period: models.AnalyticsPeriod{
			StartDate: startDate,
			EndDate:   endDate,
		},
	}

	// Get basic stats
	err := r.db.QueryRowContext(ctx, `
		SELECT
			COUNT(*) as total_achievements,
			COUNT(CASE WHEN status = 'active' THEN 1 END) as active_achievements
		FROM achievements
	`).Scan(&analytics.TotalAchievements, &analytics.ActiveAchievements)

	if err != nil {
		return nil, fmt.Errorf("failed to get basic analytics: %w", err)
	}

	// Get completion stats
	err = r.db.QueryRowContext(ctx, `
		SELECT
			COUNT(*) as total_completions,
			COUNT(DISTINCT player_id) as unique_players
		FROM player_achievements
		WHERE completed_at BETWEEN $1 AND $2
	`, startDate, endDate).Scan(
		&analytics.CompletionStats.TotalCompletions,
		&analytics.CompletionStats.UniquePlayers,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get completion stats: %w", err)
	}

	return analytics, nil
}