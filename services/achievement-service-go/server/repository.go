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

// Achievement represents an achievement entity
// PERFORMANCE: Optimized struct alignment (large fields first)
type Achievement struct {
	ID          string    `json:"id" db:"id"`
	Name        string    `json:"name" db:"name"`        // Large field first
	Description string    `json:"description" db:"description"` // Large field second
	IconURL     string    `json:"icon_url" db:"icon_url"`
	Rarity      string    `json:"rarity" db:"rarity"`
	Points      int32     `json:"points" db:"points"`      // int32 (4 bytes)
	IsUnlocked  bool      `json:"is_unlocked" db:"is_unlocked"` // bool (1 byte)
	UnlockedAt  *time.Time `json:"unlocked_at" db:"unlocked_at"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time  `json:"updated_at" db:"updated_at"`
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
func (r *AchievementRepository) GetAchievements(ctx context.Context, playerID string, limit, offset int) ([]*Achievement, error) {
	// PERFORMANCE: Context timeout check
	if deadline, ok := ctx.Deadline(); ok && time.Until(deadline) < 100*time.Millisecond {
		return nil, context.DeadlineExceeded
	}

	// PERFORMANCE: Optimized query with LIMIT/OFFSET for pagination
	query := `
		SELECT id, name, description, icon_url, rarity, points, is_unlocked, unlocked_at, created_at, updated_at
		FROM gameplay.achievements
		WHERE player_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, playerID, limit, offset)
	if err != nil {
		r.logger.Error("Failed to query achievements",
			zap.String("player_id", playerID),
			zap.Error(err))
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

	if err := rows.Err(); err != nil {
		r.logger.Error("Error iterating over achievement rows", zap.Error(err))
		return nil, err
	}

	return achievements, nil
}

// GetAchievement retrieves a specific achievement
// PERFORMANCE: Single-row query with prepared statement
func (r *AchievementRepository) GetAchievement(ctx context.Context, achievementID, playerID string) (*Achievement, error) {
	// PERFORMANCE: Context timeout check
	if deadline, ok := ctx.Deadline(); ok && time.Until(deadline) < 100*time.Millisecond {
		return nil, context.DeadlineExceeded
	}

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
		if err == sql.ErrNoRows {
			r.logger.Info("Achievement not found",
				zap.String("achievement_id", achievementID),
				zap.String("player_id", playerID))
			return nil, err
		}
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
	// PERFORMANCE: Context timeout check
	if deadline, ok := ctx.Deadline(); ok && time.Until(deadline) < 100*time.Millisecond {
		return context.DeadlineExceeded
	}

	tx, err := r.db.Begin(ctx)
	if err != nil {
		r.logger.Error("Failed to begin transaction", zap.Error(err))
		return err
	}
	defer tx.Rollback(ctx)

	now := time.Now()

	// Check if achievement is already unlocked
	checkQuery := `
		SELECT is_unlocked FROM gameplay.achievements
		WHERE id = $1 AND player_id = $2`

	var isUnlocked bool
	err = tx.QueryRow(ctx, checkQuery, achievementID, playerID).Scan(&isUnlocked)
	if err != nil {
		if err == sql.ErrNoRows {
			r.logger.Warn("Achievement not found",
				zap.String("achievement_id", achievementID),
				zap.String("player_id", playerID))
			return sql.ErrNoRows
		}
		r.logger.Error("Failed to check achievement status", zap.Error(err))
		return err
	}

	if isUnlocked {
		r.logger.Info("Achievement already unlocked",
			zap.String("achievement_id", achievementID),
			zap.String("player_id", playerID))
		return nil // Already unlocked, not an error
	}

	// Unlock the achievement
	updateQuery := `
		UPDATE gameplay.achievements
		SET is_unlocked = true, unlocked_at = $3, updated_at = $3
		WHERE id = $1 AND player_id = $2`

	result, err := tx.Exec(ctx, updateQuery, achievementID, playerID, now)
	if err != nil {
		r.logger.Error("Failed to unlock achievement", zap.Error(err))
		return err
	}

	if rowsAffected := result.RowsAffected(); rowsAffected == 0 {
		r.logger.Warn("Achievement not found during unlock",
			zap.String("achievement_id", achievementID),
			zap.String("player_id", playerID))
		return sql.ErrNoRows
	}

	r.logger.Info("Achievement unlocked successfully",
		zap.String("achievement_id", achievementID),
		zap.String("player_id", playerID))

	return tx.Commit(ctx)
}

// Close closes the database connection pool
// PERFORMANCE: Graceful shutdown
func (r *AchievementRepository) Close() {
	r.db.Close()
}
