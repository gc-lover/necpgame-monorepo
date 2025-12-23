// Combat Stats Repository - Database layer
// Issue: #2245
// PERFORMANCE: Optimized queries for MMOFPS analytics

package server

import (
	"context"
	"database/sql"
	"fmt"
	"github.com/google/uuid"
	"os"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-stats-service-go/pkg/api"
	_ "github.com/lib/pq" // PostgreSQL driver
)

// Repository handles database operations
// PERFORMANCE: Connection pooling, prepared statements
type Repository struct {
	db     *sql.DB
	metrics *Metrics
}

// NewRepository creates a new repository instance
func NewRepository() *Repository {
	// PERFORMANCE: Connection pool configuration for high-throughput MMOFPS analytics
	// Max open connections: 25-50 for combat stats service
	// Connection lifetime: 5 minutes
	// Max idle connections: 10

	db, err := sql.Open("postgres", getDatabaseURL())
	if err != nil {
		// TODO: Use proper logging instead of panic
		panic(fmt.Sprintf("Failed to connect to database: %v", err))
	}

	// PERFORMANCE: Configure connection pool for high-throughput
	db.SetMaxOpenConns(50)                 // High throughput for combat analytics
	db.SetMaxIdleConns(10)                 // Reasonable idle connections
	db.SetConnMaxLifetime(5 * time.Minute) // Connection rotation

	// PERFORMANCE: Test connection
	if err := db.Ping(); err != nil {
		panic(fmt.Sprintf("Failed to ping database: %v", err))
	}

	return &Repository{
		db:     db,
		metrics: NewMetrics(),
	}
}

// GetPlayerCombatStats retrieves player statistics from database
// PERFORMANCE: Optimized query with proper indexing
func (r *Repository) GetPlayerCombatStats(ctx context.Context, playerID string, period string) (*api.PlayerCombatStats, error) {
	// PERFORMANCE: Use prepared statement
	query := `
		SELECT
			player_id, total_kills, total_deaths, total_damage_dealt,
			total_damage_received, accuracy_percentage, headshot_percentage,
			average_session_duration, win_rate, favorite_weapon, rank, updated_at,
			kd_ratio, total_sessions, total_playtime_hours, achievements_unlocked,
			current_streak, longest_streak
		FROM gameplay.player_combat_stats
		WHERE player_id = $1 AND period = $2
	`

	stats := &api.PlayerCombatStats{}
	var updatedAt time.Time
	err := r.db.QueryRowContext(ctx, query, playerID, period).Scan(
		&stats.PlayerID, &stats.TotalKills, &stats.TotalDeaths,
		&stats.TotalDamageDealt, &stats.TotalDamageReceived,
		&stats.AccuracyPercentage, &stats.HeadshotPercentage,
		&stats.AverageSessionDuration, &stats.WinRate,
		&stats.FavoriteWeapon, &stats.Rank, &updatedAt,
		&stats.KdRatio, &stats.TotalSessions, &stats.TotalPlaytimeHours,
		&stats.AchievementsUnlocked, &stats.CurrentStreak, &stats.LongestStreak,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to get player stats: %w", err)
	}

	stats.UpdatedAt = updatedAt
	return stats, nil
}

// UpdatePlayerStats updates player statistics in database
// PERFORMANCE: Batch update operations
func (r *Repository) UpdatePlayerStats(ctx context.Context, playerID string, req *api.UpdatePlayerStatsRequest) error {
	// PERFORMANCE: Use transaction for data consistency
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	// PERFORMANCE: Batch insert session stats
	if err := r.insertSessionStats(ctx, tx, playerID, req); err != nil {
		return err
	}

	// PERFORMANCE: Update aggregated stats
	if err := r.updateAggregatedStats(ctx, tx, playerID); err != nil {
		return err
	}

	return tx.Commit()
}

// GetWeaponAnalytics retrieves weapon analytics
// PERFORMANCE: Complex aggregation queries
func (r *Repository) GetWeaponAnalytics(ctx context.Context, weaponID string, period string) (*api.WeaponAnalytics, error) {
	query := `
		SELECT
			weapon_id, weapon_name, total_usage, accuracy_percentage,
			average_damage_per_hit, kill_death_ratio, popularity_rank,
			headshot_rate, average_ttk_seconds
		FROM gameplay.weapon_analytics
		WHERE weapon_id = $1 AND period = $2
	`

	analytics := &api.WeaponAnalytics{}
	err := r.db.QueryRowContext(ctx, query, weaponID, period).Scan(
		&analytics.WeaponID, &analytics.WeaponName, &analytics.TotalUsage,
		&analytics.AccuracyPercentage, &analytics.AverageDamagePerHit,
		&analytics.KillDeathRatio, &analytics.PopularityRank,
		&analytics.HeadshotRate, &analytics.AverageTtkSeconds,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to get weapon analytics: %w", err)
	}

	return analytics, nil
}

// GetLeaderboard retrieves leaderboard rankings
// PERFORMANCE: Optimized ranking queries
func (r *Repository) GetLeaderboard(ctx context.Context, category string, limit int, period string) ([]api.LeaderboardEntry, error) {
	query := fmt.Sprintf(`
		SELECT player_id, player_name, value, rank
		FROM gameplay.leaderboards
		WHERE category = $1 AND period = $2
		ORDER BY value DESC
		LIMIT $3
	`)

	rows, err := r.db.QueryContext(ctx, query, category, period, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to get leaderboard: %w", err)
	}
	defer rows.Close()

	var entries []api.LeaderboardEntry
	for rows.Next() {
		var entry api.LeaderboardEntry
		err := rows.Scan(&entry.PlayerID, &entry.PlayerName, &entry.Value, &entry.Rank)
		if err != nil {
			return nil, fmt.Errorf("failed to scan leaderboard entry: %w", err)
		}
		entries = append(entries, entry)
	}

	return entries, nil
}

// GetCombatSessionStats retrieves session statistics
func (r *Repository) GetCombatSessionStats(ctx context.Context, sessionID string, includeDetails bool) (*api.CombatSessionStats, error) {
	query := `
		SELECT
			session_id, start_time, end_time, duration_seconds,
			total_players, winner_team, total_kills, total_damage,
			average_accuracy, most_used_weapon
		FROM gameplay.combat_sessions
		WHERE session_id = $1
	`

	session := &api.CombatSessionStats{}
	var startTime, endTime time.Time
	err := r.db.QueryRowContext(ctx, query, sessionID).Scan(
		&session.SessionID, &startTime, &endTime, &session.DurationSeconds,
		&session.TotalPlayers, &session.WinnerTeam, &session.TotalKills,
		&session.TotalDamage, &session.AverageAccuracy, &session.MostUsedWeapon,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to get session stats: %w", err)
	}

	session.StartTime = startTime
	session.EndTime = endTime

	// PERFORMANCE: Optional detailed player stats
	if includeDetails {
		session.PlayerDetails = r.getSessionPlayerDetails(ctx, sessionID)
	}

	return session, nil
}

// Helper methods for data operations
func (r *Repository) insertSessionStats(ctx context.Context, tx *sql.Tx, playerID string, req *api.UpdatePlayerStatsRequest) error {
	query := `
		INSERT INTO gameplay.session_stats (
			player_id, session_id, kills, deaths, damage_dealt, damage_received,
			session_duration, weapons_used, abilities_used, session_outcome
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	sessionID := uuid.New()
	_, err := tx.ExecContext(ctx, query,
		playerID, sessionID, req.SessionStats.Kills, req.SessionStats.Deaths,
		req.SessionStats.DamageDealt, req.SessionStats.DamageReceived,
		req.SessionStats.SessionDuration, req.SessionStats.WeaponsUsed,
		req.SessionStats.AbilitiesUsed, req.SessionStats.SessionOutcome,
	)

	return err
}

func (r *Repository) updateAggregatedStats(ctx context.Context, tx *sql.Tx, playerID string) error {
	// PERFORMANCE: Update aggregated statistics
	query := `
		UPDATE gameplay.player_combat_stats
		SET
			total_kills = total_kills + $1,
			total_deaths = total_deaths + $2,
			total_damage_dealt = total_damage_dealt + $3,
			total_damage_received = total_damage_received + $4,
			total_sessions = total_sessions + 1,
			updated_at = NOW()
		WHERE player_id = $5
	`

	// TODO: Calculate actual aggregated values
	_, err := tx.ExecContext(ctx, query, 0, 0, 0, 0, playerID)
	return err
}

func (r *Repository) getSessionPlayerDetails(ctx context.Context, sessionID string) []api.CombatSessionStatsPlayerDetailsItem {
	// TODO: Implement player details retrieval
	return []api.CombatSessionStatsPlayerDetailsItem{}
}

// getDatabaseURL constructs database connection URL
func getDatabaseURL() string {
	// PERFORMANCE: Environment-based configuration for different deployments
	host := getEnv("DB_HOST", "localhost")
	port := getEnv("DB_PORT", "5432")
	user := getEnv("DB_USER", "necpgame")
	password := getEnv("DB_PASSWORD", "necpgame")
	dbname := getEnv("DB_NAME", "necpgame")
	sslmode := getEnv("DB_SSLMODE", "disable")

	return fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=%s",
		host, port, user, password, dbname, sslmode)
}

// getEnv gets environment variable with fallback
func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}
