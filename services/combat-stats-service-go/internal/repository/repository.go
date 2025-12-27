// Issue: #2250
package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"go.uber.org/zap"
)

// CombatStats represents aggregated combat statistics
type CombatStats struct {
	PlayerID          string                 `json:"player_id" db:"player_id"`
	TotalKills        int64                  `json:"total_kills" db:"total_kills"`
	TotalDeaths       int64                  `json:"total_deaths" db:"total_deaths"`
	TotalScore        int64                  `json:"total_score" db:"total_score"`
	TotalPlaytime     int64                  `json:"total_playtime" db:"total_playtime"` // seconds
	WeaponStats       map[string]interface{} `json:"weapon_stats" db:"weapon_stats"`
	MatchHistory      []string               `json:"match_history" db:"match_history"`
	LastUpdated       time.Time              `json:"last_updated" db:"last_updated"`
	Accuracy          float64                `json:"accuracy" db:"accuracy"`
	HeadshotRate      float64                `json:"headshot_rate" db:"headshot_rate"`
	AvgDamagePerKill  float64                `json:"avg_damage_per_kill" db:"avg_damage_per_kill"`
}

// WeaponStats represents weapon-specific statistics
type WeaponStats struct {
	WeaponID       string    `json:"weapon_id" db:"weapon_id"`
	TotalKills     int64     `json:"total_kills" db:"total_kills"`
	TotalShots     int64     `json:"total_shots" db:"total_shots"`
	TotalHits      int64     `json:"total_hits" db:"total_hits"`
	Accuracy       float64   `json:"accuracy" db:"accuracy"`
	AvgDamage      float64   `json:"avg_damage" db:"avg_damage"`
	LastUsed       time.Time `json:"last_used" db:"last_used"`
}

// MatchStats represents statistics for a specific match
type MatchStats struct {
	MatchID     string                 `json:"match_id" db:"match_id"`
	PlayerID    string                 `json:"player_id" db:"player_id"`
	Kills       int                    `json:"kills" db:"kills"`
	Deaths      int                    `json:"deaths" db:"deaths"`
	Score       int                    `json:"score" db:"score"`
	Playtime    int                    `json:"playtime" db:"playtime"` // seconds
	WeaponUsage map[string]int         `json:"weapon_usage" db:"weapon_usage"`
	DamageDealt int                    `json:"damage_dealt" db:"damage_dealt"`
	StartTime   time.Time              `json:"start_time" db:"start_time"`
	EndTime     time.Time              `json:"end_time" db:"end_time"`
	Metadata    map[string]interface{} `json:"metadata" db:"metadata"`
}

// CombatEvent represents a combat event for real-time processing
type CombatEvent struct {
	EventID     string                 `json:"event_id"`
	EventType   string                 `json:"event_type"` // kill, death, damage, etc.
	PlayerID    string                 `json:"player_id"`
	TargetID    string                 `json:"target_id,omitempty"`
	WeaponID    string                 `json:"weapon_id,omitempty"`
	Damage      int                    `json:"damage,omitempty"`
	Timestamp   time.Time              `json:"timestamp"`
	MatchID     string                 `json:"match_id,omitempty"`
	Position    map[string]float64     `json:"position,omitempty"`
	Metadata    map[string]interface{} `json:"metadata,omitempty"`
}

// CombatStatsRepository handles database operations
type CombatStatsRepository struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.SugaredLogger
}

// NewConnection creates a new database connection
func NewConnection(databaseURL string) (*sql.DB, error) {
	db, err := sql.Open("postgres", databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to open database: %w", err)
	}

	// Test connection
	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	// Configure connection pool
	db.SetMaxOpenConns(100) // Higher for stats service
	db.SetMaxIdleConns(20)
	db.SetConnMaxLifetime(time.Hour)

	return db, nil
}

// NewRedisClient creates a new Redis client
func NewRedisClient(redisURL string) (*redis.Client, error) {
	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	client := redis.NewClient(opt)

	// Test connection
	ctx := context.Background()
	if err := client.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to ping Redis: %w", err)
	}

	return client, nil
}

// NewCombatStatsRepository creates a new combat stats repository
func NewCombatStatsRepository(db *sql.DB, redis *redis.Client, logger *zap.SugaredLogger) *CombatStatsRepository {
	return &CombatStatsRepository{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// GetPlayerStats retrieves player combat statistics
func (r *CombatStatsRepository) GetPlayerStats(ctx context.Context, playerID string) (*CombatStats, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("combat:stats:player:%s", playerID)
	cached, err := r.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var stats CombatStats
		if err := json.Unmarshal([]byte(cached), &stats); err == nil {
			return &stats, nil
		}
	}

	// Fallback to database
	query := `
		SELECT player_id, total_kills, total_deaths, total_score, total_playtime,
			   weapon_stats, match_history, last_updated, accuracy, headshot_rate, avg_damage_per_kill
		FROM combat.player_stats
		WHERE player_id = $1
	`

	var stats CombatStats
	var weaponStatsJSON, matchHistoryJSON []byte

	err = r.db.QueryRowContext(ctx, query, playerID).Scan(
		&stats.PlayerID, &stats.TotalKills, &stats.TotalDeaths, &stats.TotalScore,
		&stats.TotalPlaytime, &weaponStatsJSON, &matchHistoryJSON, &stats.LastUpdated,
		&stats.Accuracy, &stats.HeadshotRate, &stats.AvgDamagePerKill,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			// Return empty stats for new players
			return &CombatStats{PlayerID: playerID, LastUpdated: time.Now()}, nil
		}
		r.logger.Errorf("Failed to get player stats: %v", err)
		return nil, fmt.Errorf("failed to get player stats: %w", err)
	}

	// Parse JSON fields
	json.Unmarshal(weaponStatsJSON, &stats.WeaponStats)
	json.Unmarshal(matchHistoryJSON, &stats.MatchHistory)

	// Cache result
	statsJSON, _ := json.Marshal(stats)
	r.redis.Set(ctx, cacheKey, statsJSON, 30*time.Minute)

	return &stats, nil
}

// UpdatePlayerStats updates player combat statistics
func (r *CombatStatsRepository) UpdatePlayerStats(ctx context.Context, stats *CombatStats) error {
	weaponStatsJSON, _ := json.Marshal(stats.WeaponStats)
	matchHistoryJSON, _ := json.Marshal(stats.MatchHistory)

	query := `
		INSERT INTO combat.player_stats (
			player_id, total_kills, total_deaths, total_score, total_playtime,
			weapon_stats, match_history, last_updated, accuracy, headshot_rate, avg_damage_per_kill
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
		ON CONFLICT (player_id) DO UPDATE SET
			total_kills = EXCLUDED.total_kills,
			total_deaths = EXCLUDED.total_deaths,
			total_score = EXCLUDED.total_score,
			total_playtime = EXCLUDED.total_playtime,
			weapon_stats = EXCLUDED.weapon_stats,
			match_history = EXCLUDED.match_history,
			last_updated = EXCLUDED.last_updated,
			accuracy = EXCLUDED.accuracy,
			headshot_rate = EXCLUDED.headshot_rate,
			avg_damage_per_kill = EXCLUDED.avg_damage_per_kill
	`

	_, err := r.db.ExecContext(ctx, query,
		stats.PlayerID, stats.TotalKills, stats.TotalDeaths, stats.TotalScore, stats.TotalPlaytime,
		weaponStatsJSON, matchHistoryJSON, stats.LastUpdated, stats.Accuracy,
		stats.HeadshotRate, stats.AvgDamagePerKill)

	if err != nil {
		r.logger.Errorf("Failed to update player stats: %v", err)
		return fmt.Errorf("failed to update player stats: %w", err)
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("combat:stats:player:%s", stats.PlayerID)
	r.redis.Del(ctx, cacheKey)

	return nil
}

// RecordCombatEvent records a combat event for real-time processing
func (r *CombatStatsRepository) RecordCombatEvent(ctx context.Context, event *CombatEvent) error {
	eventJSON, _ := json.Marshal(event)

	// Store in Redis for real-time processing
	eventKey := fmt.Sprintf("combat:event:%s", event.EventID)
	r.redis.Set(ctx, eventKey, eventJSON, 1*time.Hour)

	// Add to processing queue
	r.redis.LPush(ctx, "combat:events:queue", eventJSON)

	r.logger.Infof("Recorded combat event: %s for player %s", event.EventType, event.PlayerID)
	return nil
}

// GetWeaponStats retrieves weapon-specific statistics
func (r *CombatStatsRepository) GetWeaponStats(ctx context.Context, weaponID string) (*WeaponStats, error) {
	cacheKey := fmt.Sprintf("combat:stats:weapon:%s", weaponID)
	cached, err := r.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var stats WeaponStats
		if err := json.Unmarshal([]byte(cached), &stats); err == nil {
			return &stats, nil
		}
	}

	query := `
		SELECT weapon_id, total_kills, total_shots, total_hits, accuracy, avg_damage, last_used
		FROM combat.weapon_stats
		WHERE weapon_id = $1
	`

	var stats WeaponStats
	err = r.db.QueryRowContext(ctx, query, weaponID).Scan(
		&stats.WeaponID, &stats.TotalKills, &stats.TotalShots, &stats.TotalHits,
		&stats.Accuracy, &stats.AvgDamage, &stats.LastUsed,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return &WeaponStats{WeaponID: weaponID}, nil
		}
		r.logger.Errorf("Failed to get weapon stats: %v", err)
		return nil, fmt.Errorf("failed to get weapon stats: %w", err)
	}

	// Cache result
	statsJSON, _ := json.Marshal(stats)
	r.redis.Set(ctx, cacheKey, statsJSON, 15*time.Minute)

	return &stats, nil
}

// GetMatchStats retrieves statistics for a specific match
func (r *CombatStatsRepository) GetMatchStats(ctx context.Context, matchID string) ([]*MatchStats, error) {
	query := `
		SELECT match_id, player_id, kills, deaths, score, playtime, weapon_usage,
			   damage_dealt, start_time, end_time, metadata
		FROM combat.match_stats
		WHERE match_id = $1
		ORDER BY score DESC
	`

	rows, err := r.db.QueryContext(ctx, query, matchID)
	if err != nil {
		r.logger.Errorf("Failed to get match stats: %v", err)
		return nil, fmt.Errorf("failed to get match stats: %w", err)
	}
	defer rows.Close()

	var matchStats []*MatchStats
	for rows.Next() {
		var stats MatchStats
		var weaponUsageJSON, metadataJSON []byte

		err := rows.Scan(
			&stats.MatchID, &stats.PlayerID, &stats.Kills, &stats.Deaths, &stats.Score,
			&stats.Playtime, &weaponUsageJSON, &stats.DamageDealt, &stats.StartTime,
			&stats.EndTime, &metadataJSON,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan match stats: %v", err)
			continue
		}

		json.Unmarshal(weaponUsageJSON, &stats.WeaponUsage)
		json.Unmarshal(metadataJSON, &stats.Metadata)

		matchStats = append(matchStats, &stats)
	}

	return matchStats, nil
}
