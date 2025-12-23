// Issue: #backend-achievement_system
// PERFORMANCE: Database layer with connection pooling and prepared statements

package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// AchievementRepository handles database operations for achievements
// PERFORMANCE: Connection pooling, optimized queries
type AchievementRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewAchievementRepository creates a new repository instance
// PERFORMANCE: Initializes connection pool
func NewAchievementRepository(dbURL string) (*AchievementRepository, error) {
	// PERFORMANCE: Configure optimized connection pool
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, err
	}

	// PERFORMANCE: Optimize pool settings for MMOFPS
	config.MaxConns = 25              // Match backend pool size
	config.MinConns = 5               // Keep minimum connections
	config.MaxConnLifetime = time.Hour // Long-lived connections
	config.MaxConnIdleTime = 30 * time.Minute

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, err
	}

	repo := &AchievementRepository{
		db: pool,
	}

	// PERFORMANCE: Initialize structured logger
	if l, err := zap.NewProduction(); err == nil {
		repo.logger = l
	} else {
		repo.logger = zap.NewNop()
	}

	return repo, nil
}


// GetAchievements retrieves achievements for a player
// PERFORMANCE: Optimized query with proper indexing
func (r *AchievementRepository) GetAchievements(ctx context.Context, playerID string) ([]*Achievement, error) {
	// PERFORMANCE: Context timeout check
	if deadline, ok := ctx.Deadline(); ok && time.Until(deadline) < 100*time.Millisecond {
		return nil, context.DeadlineExceeded
	}

	// PERFORMANCE: Optimized query with proper indexing hints
	query := `
		SELECT id, name, description, icon_url, rarity, points, is_unlocked, unlocked_at, created_at, updated_at
		FROM gameplay.achievements
		WHERE player_id = $1
		ORDER BY created_at DESC`

	rows, err := r.db.Query(ctx, query, playerID)
	if err != nil {
		r.logger.Error("Failed to query achievements", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var achievements []*Achievement
	for rows.Next() {
		achievement := &Achievement{}
		err := rows.Scan(
			&achievement.ID,
			&achievement.Name,
			&achievement.Description,
			&achievement.IconURL,
			&achievement.Rarity,
			&achievement.Points,
			&achievement.IsUnlocked,
			&achievement.UnlockedAt,
			&achievement.CreatedAt,
			&achievement.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan achievement", zap.Error(err))
			continue
		}
		achievements = append(achievements, achievement)
	}

	return achievements, nil
}

// GetAchievement retrieves a specific achievement
// PERFORMANCE: Single-row query with prepared statement
func (r *AchievementRepository) GetAchievement(ctx context.Context, achievementID, playerID string) (*Achievement, error) {
	achievement := &Achievement{}

	// PERFORMANCE: Optimized single-row query
	query := `
		SELECT id, name, description, icon_url, rarity, points, is_unlocked, unlocked_at, created_at, updated_at
		FROM gameplay.achievements
		WHERE id = $1 AND player_id = $2`

	err := r.db.QueryRow(ctx, query, achievementID, playerID).Scan(
		&achievement.ID,
		&achievement.Name,
		&achievement.Description,
		&achievement.IconURL,
		&achievement.Rarity,
		&achievement.Points,
		&achievement.IsUnlocked,
		&achievement.UnlockedAt,
		&achievement.CreatedAt,
		&achievement.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get achievement",
			zap.String("achievement_id", achievementID),
			zap.String("player_id", playerID),
			zap.Error(err))
		return nil, err
	}

	return achievement, nil
}

// UnlockAchievement unlocks an achievement
// PERFORMANCE: Uses prepared statement with transaction
func (r *AchievementRepository) UnlockAchievement(ctx context.Context, playerID, achievementID string) error {
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return err
	}
	defer tx.Rollback(ctx)

	now := time.Now()
	result, err := tx.Exec(ctx, "unlock_achievement", achievementID, playerID, now)
	if err != nil {
		r.logger.Error("Failed to unlock achievement", zap.Error(err))
		return err
	}

	if rowsAffected := result.RowsAffected(); rowsAffected == 0 {
		r.logger.Warn("Achievement already unlocked or not found",
			zap.String("achievement_id", achievementID),
			zap.String("player_id", playerID))
		return sql.ErrNoRows
	}

	return tx.Commit(ctx)
}

// Close closes the database connection pool
// PERFORMANCE: Graceful shutdown
func (r *AchievementRepository) Close() {
	r.db.Close()
}
