package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jmoiron/sqlx"
	_ "github.com/lib/pq"
	"github.com/redis/go-redis/v9"

	"services/achievement-system-service-go/pkg/models"
)

// Repository handles data access for the Achievement System
type Repository struct {
	db    *sqlx.DB
	redis *redis.Client
}

// NewRepository creates a new repository instance
func NewRepository(databaseURL, redisURL string) (*Repository, error) {
	db, err := sqlx.Connect("postgres", databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to database: %w", err)
	}

	// Configure connection pool for MMOFPS performance
	db.SetMaxOpenConns(100)
	db.SetMaxIdleConns(10)
	db.SetConnMaxLifetime(time.Hour)

	// Parse Redis URL and create client
	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, fmt.Errorf("failed to parse Redis URL: %w", err)
	}

	rdb := redis.NewClient(opt)

	return &Repository{
		db:    db,
		redis: rdb,
	}, nil
}

// Close closes database and Redis connections
func (r *Repository) Close() error {
	if err := r.redis.Close(); err != nil {
		return err
	}
	return r.db.Close()
}

// Achievement CRUD operations

// CreateAchievement creates a new achievement
func (r *Repository) CreateAchievement(ctx context.Context, achievement *models.Achievement) error {
	query := `
		INSERT INTO achievements (id, name, description, category, icon_url, points, rarity, is_hidden, is_active, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`
	achievement.CreatedAt = time.Now()
	achievement.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		achievement.ID, achievement.Name, achievement.Description, achievement.Category,
		achievement.IconURL, achievement.Points, achievement.Rarity, achievement.IsHidden,
		achievement.IsActive, achievement.CreatedAt, achievement.UpdatedAt)

	if err != nil {
		return fmt.Errorf("failed to create achievement: %w", err)
	}

	// Cache achievement
	return r.cacheAchievement(ctx, achievement)
}

// GetAchievement retrieves an achievement by ID
func (r *Repository) GetAchievement(ctx context.Context, id uuid.UUID) (*models.Achievement, error) {
	// Try cache first
	if achievement := r.getCachedAchievement(ctx, id); achievement != nil {
		return achievement, nil
	}

	query := `SELECT * FROM achievements WHERE id = $1 AND is_active = true`
	var achievement models.Achievement
	err := r.db.GetContext(ctx, &achievement, query, id)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("achievement not found")
		}
		return nil, fmt.Errorf("failed to get achievement: %w", err)
	}

	// Cache achievement
	r.cacheAchievement(ctx, &achievement)

	return &achievement, nil
}

// ListAchievements retrieves all active achievements with pagination
func (r *Repository) ListAchievements(ctx context.Context, limit, offset int) ([]*models.Achievement, error) {
	query := `SELECT * FROM achievements WHERE is_active = true ORDER BY created_at DESC LIMIT $1 OFFSET $2`
	var achievements []*models.Achievement
	err := r.db.SelectContext(ctx, &achievements, query, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to list achievements: %w", err)
	}
	return achievements, nil
}

// UpdateAchievement updates an achievement
func (r *Repository) UpdateAchievement(ctx context.Context, achievement *models.Achievement) error {
	query := `
		UPDATE achievements
		SET name = $2, description = $3, category = $4, icon_url = $5, points = $6,
		    rarity = $7, is_hidden = $8, is_active = $9, updated_at = $10
		WHERE id = $1
	`
	achievement.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		achievement.ID, achievement.Name, achievement.Description, achievement.Category,
		achievement.IconURL, achievement.Points, achievement.Rarity, achievement.IsHidden,
		achievement.IsActive, achievement.UpdatedAt)

	if err != nil {
		return fmt.Errorf("failed to update achievement: %w", err)
	}

	// Invalidate cache
	r.invalidateAchievementCache(ctx, achievement.ID)

	return nil
}

// DeleteAchievement soft deletes an achievement
func (r *Repository) DeleteAchievement(ctx context.Context, id uuid.UUID) error {
	query := `UPDATE achievements SET is_active = false, updated_at = $2 WHERE id = $1`
	_, err := r.db.ExecContext(ctx, query, id, time.Now())
	if err != nil {
		return fmt.Errorf("failed to delete achievement: %w", err)
	}

	// Invalidate cache
	r.invalidateAchievementCache(ctx, id)

	return nil
}

// Player Achievement operations

// GetPlayerAchievements retrieves all achievements for a player
func (r *Repository) GetPlayerAchievements(ctx context.Context, playerID uuid.UUID) ([]*models.PlayerAchievement, error) {
	query := `
		SELECT pa.*, a.name, a.description, a.category, a.icon_url, a.points, a.rarity
		FROM player_achievements pa
		JOIN achievements a ON pa.achievement_id = a.id
		WHERE pa.player_id = $1 AND a.is_active = true
		ORDER BY pa.unlocked_at DESC
	`

	rows, err := r.db.QueryContext(ctx, query, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get player achievements: %w", err)
	}
	defer rows.Close()

	var achievements []*models.PlayerAchievement
	for rows.Next() {
		var pa models.PlayerAchievement
		var rewardsJSON []byte
		err := rows.Scan(
			&pa.ID, &pa.PlayerID, &pa.AchievementID, &pa.UnlockedAt, &pa.PointsEarned, &rewardsJSON,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan player achievement: %w", err)
		}

		// Parse rewards JSON
		if len(rewardsJSON) > 0 {
			json.Unmarshal(rewardsJSON, &pa.Rewards)
		}

		achievements = append(achievements, &pa)
	}

	return achievements, nil
}

// UnlockAchievement unlocks an achievement for a player
func (r *Repository) UnlockAchievement(ctx context.Context, playerAchievement *models.PlayerAchievement) error {
	query := `
		INSERT INTO player_achievements (id, player_id, achievement_id, unlocked_at, points_earned, rewards)
		VALUES ($1, $2, $3, $4, $5, $6)
	`

	rewardsJSON, _ := json.Marshal(playerAchievement.Rewards)

	_, err := r.db.ExecContext(ctx, query,
		playerAchievement.ID, playerAchievement.PlayerID, playerAchievement.AchievementID,
		playerAchievement.UnlockedAt, playerAchievement.PointsEarned, rewardsJSON)

	if err != nil {
		return fmt.Errorf("failed to unlock achievement: %w", err)
	}

	// Invalidate player cache
	r.invalidatePlayerCache(ctx, playerAchievement.PlayerID)

	return nil
}

// GetPlayerAchievementProgress retrieves progress for a specific achievement
func (r *Repository) GetPlayerAchievementProgress(ctx context.Context, playerID, achievementID uuid.UUID) (*models.AchievementProgress, error) {
	query := `
		SELECT * FROM achievement_progress
		WHERE player_id = $1 AND achievement_id = $2
	`
	var progress models.AchievementProgress
	err := r.db.GetContext(ctx, &progress, query, playerID, achievementID)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("progress not found")
		}
		return nil, fmt.Errorf("failed to get achievement progress: %w", err)
	}
	return &progress, nil
}

// UpdateAchievementProgress updates player progress towards an achievement
func (r *Repository) UpdateAchievementProgress(ctx context.Context, progress *models.AchievementProgress) error {
	query := `
		INSERT INTO achievement_progress (id, player_id, achievement_id, progress, max_progress, is_completed, completed_at, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		ON CONFLICT (player_id, achievement_id)
		DO UPDATE SET
			progress = EXCLUDED.progress,
			is_completed = EXCLUDED.is_completed,
			completed_at = EXCLUDED.completed_at,
			updated_at = EXCLUDED.updated_at
	`

	progress.UpdatedAt = time.Now()
	if progress.CreatedAt.IsZero() {
		progress.CreatedAt = time.Now()
	}

	var completedAt *time.Time
	if progress.IsCompleted && progress.CompletedAt != nil {
		completedAt = progress.CompletedAt
	}

	_, err := r.db.ExecContext(ctx, query,
		progress.ID, progress.PlayerID, progress.AchievementID, progress.Progress,
		progress.MaxProgress, progress.IsCompleted, completedAt, progress.CreatedAt, progress.UpdatedAt)

	if err != nil {
		return fmt.Errorf("failed to update achievement progress: %w", err)
	}

	return nil
}

// GetPlayerProfile retrieves a player's achievement profile
func (r *Repository) GetPlayerProfile(ctx context.Context, playerID uuid.UUID) (*models.PlayerAchievementProfile, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("player_profile:%s", playerID)
	if cached, err := r.redis.Get(ctx, cacheKey).Result(); err == nil {
		var profile models.PlayerAchievementProfile
		if json.Unmarshal([]byte(cached), &profile) == nil {
			return &profile, nil
		}
	}

	query := `
		SELECT
			COUNT(DISTINCT pa.achievement_id) as completed_achievements,
			COALESCE(SUM(pa.points_earned), 0) as total_points,
			COUNT(DISTINCT ap.achievement_id) as total_achievements
		FROM players p
		LEFT JOIN player_achievements pa ON p.id = pa.player_id
		LEFT JOIN achievement_progress ap ON p.id = ap.player_id
		WHERE p.id = $1
	`

	var profile models.PlayerAchievementProfile
	profile.PlayerID = playerID
	err := r.db.QueryRowContext(ctx, query, playerID).Scan(
		&profile.CompletedAchievements, &profile.TotalPoints, &profile.TotalAchievements)
	if err != nil {
		return nil, fmt.Errorf("failed to get player profile: %w", err)
	}

	// Get recent achievements
	recentQuery := `
		SELECT pa.* FROM player_achievements pa
		WHERE pa.player_id = $1
		ORDER BY pa.unlocked_at DESC LIMIT 10
	`
	var recent []*models.PlayerAchievement
	err = r.db.SelectContext(ctx, &recent, recentQuery, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get recent achievements: %w", err)
	}
	profile.RecentAchievements = recent

	// Cache profile for 5 minutes
	profileJSON, _ := json.Marshal(profile)
	r.redis.Set(ctx, cacheKey, profileJSON, 5*time.Minute)

	return &profile, nil
}

// RecordAchievementEvent records an event that might trigger achievement progress
func (r *Repository) RecordAchievementEvent(ctx context.Context, event *models.AchievementEvent) error {
	query := `
		INSERT INTO achievement_events (id, type, player_id, data, timestamp)
		VALUES ($1, $2, $3, $4, $5)
	`

	eventDataJSON, _ := json.Marshal(event.Data)

	_, err := r.db.ExecContext(ctx, query,
		uuid.New(), event.Type, event.PlayerID, eventDataJSON, event.Timestamp)

	return err
}

// Cache helper methods
func (r *Repository) cacheAchievement(ctx context.Context, achievement *models.Achievement) error {
	cacheKey := fmt.Sprintf("achievement:%s", achievement.ID)
	data, err := json.Marshal(achievement)
	if err != nil {
		return err
	}
	return r.redis.Set(ctx, cacheKey, data, time.Hour).Err()
}

func (r *Repository) getCachedAchievement(ctx context.Context, id uuid.UUID) *models.Achievement {
	cacheKey := fmt.Sprintf("achievement:%s", id)
	data, err := r.redis.Get(ctx, cacheKey).Result()
	if err != nil {
		return nil
	}

	var achievement models.Achievement
	if json.Unmarshal([]byte(data), &achievement) == nil {
		return &achievement
	}
	return nil
}

func (r *Repository) invalidateAchievementCache(ctx context.Context, id uuid.UUID) {
	cacheKey := fmt.Sprintf("achievement:%s", id)
	r.redis.Del(ctx, cacheKey)
}

func (r *Repository) invalidatePlayerCache(ctx context.Context, playerID uuid.UUID) {
	cacheKey := fmt.Sprintf("player_profile:%s", playerID)
	r.redis.Del(ctx, cacheKey)
}
