package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"go.uber.org/zap"

	"necpgame/services/game-analytics-dashboard-service-go/pkg/models"
)

// Repository handles data persistence for game analytics
type Repository struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.Logger
}

// NewRepository creates a new repository instance
func NewRepository(db *sql.DB, redisClient *redis.Client, logger *zap.Logger) *Repository {
	return &Repository{
		db:     db,
		redis:  redisClient,
		logger: logger,
	}
}

// NewPostgresConnection creates a new PostgreSQL connection
func NewPostgresConnection(dsn string) (*sql.DB, error) {
	db, err := sql.Open("postgres", dsn)
	if err != nil {
		return nil, fmt.Errorf("failed to open database: %w", err)
	}

	// Configure connection pool for analytics workloads
	db.SetMaxOpenConns(50)  // Higher for analytics queries
	db.SetMaxIdleConns(25)
	db.SetConnMaxLifetime(time.Hour)

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return db, nil
}

// NewRedisConnection creates a new Redis connection
func NewRedisConnection(url string) (*redis.Client, error) {
	opt, err := redis.ParseURL(url)
	if err != nil {
		return nil, fmt.Errorf("failed to parse Redis URL: %w", err)
	}

	client := redis.NewClient(opt)

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := client.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to ping Redis: %w", err)
	}

	return client, nil
}

// PlayerAnalytics methods
func (r *Repository) GetPlayerAnalytics(ctx context.Context, playerID string, timeRange string) (*models.PlayerAnalytics, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("player_analytics:%s:%s", playerID, timeRange)
	if cached, err := r.redis.Get(ctx, cacheKey).Result(); err == nil {
		var analytics models.PlayerAnalytics
		if err := json.Unmarshal([]byte(cached), &analytics); err == nil {
			return &analytics, nil
		}
	}

	// Query database
	query := `
		SELECT
			player_id,
			username,
			total_play_time,
			sessions_count,
			last_seen,
			average_session_time,
			retention_rate,
			churn_risk,
			engagement_score
		FROM player_analytics
		WHERE player_id = $1 AND time_range = $2
	`

	var analytics models.PlayerAnalytics
	err := r.db.QueryRowContext(ctx, query, playerID, timeRange).Scan(
		&analytics.PlayerID,
		&analytics.Username,
		&analytics.TotalPlayTime,
		&analytics.SessionsCount,
		&analytics.LastSeen,
		&analytics.AverageSessionTime,
		&analytics.RetentionRate,
		&analytics.ChurnRisk,
		&analytics.EngagementScore,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("player analytics not found")
		}
		r.logger.Error("Failed to get player analytics", zap.Error(err))
		return nil, fmt.Errorf("failed to get player analytics: %w", err)
	}

	// Cache result
	if data, err := json.Marshal(analytics); err == nil {
		r.redis.Set(ctx, cacheKey, data, 5*time.Minute)
	}

	return &analytics, nil
}

func (r *Repository) GetGameMetrics(ctx context.Context, timeRange string) (*models.GameMetrics, error) {
	cacheKey := fmt.Sprintf("game_metrics:%s", timeRange)

	// Try cache first
	if cached, err := r.redis.Get(ctx, cacheKey).Result(); err == nil {
		var metrics models.GameMetrics
		if err := json.Unmarshal([]byte(cached), &metrics); err == nil {
			return &metrics, nil
		}
	}

	// Query database
	query := `
		SELECT
			total_players,
			active_players,
			new_registrations,
			concurrent_users,
			peak_concurrent,
			average_session_time,
			revenue,
			time_range,
			timestamp
		FROM game_metrics
		WHERE time_range = $1
		ORDER BY timestamp DESC
		LIMIT 1
	`

	var metrics models.GameMetrics
	err := r.db.QueryRowContext(ctx, query, timeRange).Scan(
		&metrics.TotalPlayers,
		&metrics.ActivePlayers,
		&metrics.NewRegistrations,
		&metrics.ConcurrentUsers,
		&metrics.PeakConcurrent,
		&metrics.AverageSessionTime,
		&metrics.Revenue,
		&metrics.TimeRange,
		&metrics.Timestamp,
	)

	if err != nil {
		r.logger.Error("Failed to get game metrics", zap.Error(err))
		return nil, fmt.Errorf("failed to get game metrics: %w", err)
	}

	// Cache result
	if data, err := json.Marshal(metrics); err == nil {
		r.redis.Set(ctx, cacheKey, data, 1*time.Minute) // Shorter TTL for real-time data
	}

	return &metrics, nil
}

func (r *Repository) GetCombatAnalytics(ctx context.Context, timeRange string) (*models.CombatAnalytics, error) {
	cacheKey := fmt.Sprintf("combat_analytics:%s", timeRange)

	// Try cache first
	if cached, err := r.redis.Get(ctx, cacheKey).Result(); err == nil {
		var analytics models.CombatAnalytics
		if err := json.Unmarshal([]byte(cached), &analytics); err == nil {
			return &analytics, nil
		}
	}

	// Query database
	query := `
		SELECT
			total_matches,
			average_match_time,
			win_rate,
			popular_weapons,
			kill_death_ratio,
			headshot_rate,
			time_range,
			timestamp
		FROM combat_analytics
		WHERE time_range = $1
		ORDER BY timestamp DESC
		LIMIT 1
	`

	var analytics models.CombatAnalytics
	var weaponsJSON string

	err := r.db.QueryRowContext(ctx, query, timeRange).Scan(
		&analytics.TotalMatches,
		&analytics.AverageMatchTime,
		&analytics.WinRate,
		&weaponsJSON,
		&analytics.KillDeathRatio,
		&analytics.HeadshotRate,
		&analytics.TimeRange,
		&analytics.Timestamp,
	)

	if err != nil {
		r.logger.Error("Failed to get combat analytics", zap.Error(err))
		return nil, fmt.Errorf("failed to get combat analytics: %w", err)
	}

	// Parse weapons JSON
	if err := json.Unmarshal([]byte(weaponsJSON), &analytics.PopularWeapons); err != nil {
		r.logger.Warn("Failed to parse popular weapons JSON", zap.Error(err))
	}

	// Cache result
	if data, err := json.Marshal(analytics); err == nil {
		r.redis.Set(ctx, cacheKey, data, 2*time.Minute)
	}

	return &analytics, nil
}

func (r *Repository) GetEconomicAnalytics(ctx context.Context, timeRange string) (*models.EconomicAnalytics, error) {
	cacheKey := fmt.Sprintf("economic_analytics:%s", timeRange)

	// Try cache first
	if cached, err := r.redis.Get(ctx, cacheKey).Result(); err == nil {
		var analytics models.EconomicAnalytics
		if err := json.Unmarshal([]byte(cached), &analytics); err == nil {
			return &analytics, nil
		}
	}

	// Query database
	query := `
		SELECT
			total_transactions,
			total_revenue,
			average_transaction,
			popular_items,
			currency_circulation,
			trade_volume,
			time_range,
			timestamp
		FROM economic_analytics
		WHERE time_range = $1
		ORDER BY timestamp DESC
		LIMIT 1
	`

	var analytics models.EconomicAnalytics
	var itemsJSON string

	err := r.db.QueryRowContext(ctx, query, timeRange).Scan(
		&analytics.TotalTransactions,
		&analytics.TotalRevenue,
		&analytics.AverageTransaction,
		&itemsJSON,
		&analytics.CurrencyCirculation,
		&analytics.TradeVolume,
		&analytics.TimeRange,
		&analytics.Timestamp,
	)

	if err != nil {
		r.logger.Error("Failed to get economic analytics", zap.Error(err))
		return nil, fmt.Errorf("failed to get economic analytics: %w", err)
	}

	// Parse items JSON
	if err := json.Unmarshal([]byte(itemsJSON), &analytics.PopularItems); err != nil {
		r.logger.Warn("Failed to parse popular items JSON", zap.Error(err))
	}

	// Cache result
	if data, err := json.Marshal(analytics); err == nil {
		r.redis.Set(ctx, cacheKey, data, 3*time.Minute)
	}

	return &analytics, nil
}

func (r *Repository) GetRealTimeDashboard(ctx context.Context) (*models.RealTimeDashboard, error) {
	cacheKey := "realtime_dashboard"

	// Try cache first (very short TTL for real-time data)
	if cached, err := r.redis.Get(ctx, cacheKey).Result(); err == nil {
		var dashboard models.RealTimeDashboard
		if err := json.Unmarshal([]byte(cached), &dashboard); err == nil {
			return &dashboard, nil
		}
	}

	// Query real-time data
	dashboard := &models.RealTimeDashboard{
		Timestamp: time.Now(),
	}

	// Get online players
	if err := r.db.QueryRowContext(ctx, "SELECT COUNT(*) FROM active_sessions").Scan(&dashboard.OnlinePlayers); err != nil {
		r.logger.Warn("Failed to get online players count", zap.Error(err))
	}

	// Get active matches
	if err := r.db.QueryRowContext(ctx, "SELECT COUNT(*) FROM active_matches").Scan(&dashboard.ActiveMatches); err != nil {
		r.logger.Warn("Failed to get active matches count", zap.Error(err))
	}

	// Get server load (placeholder)
	dashboard.ServerLoad = []models.ServerLoad{
		{
			ServerID:   "server-1",
			ServerName: "EU-West-1",
			Region:     "EU",
			Load:       65.5,
			Players:    1250,
			Status:     "healthy",
		},
	}

	// Get recent events
	dashboard.RecentEvents = []models.GameEvent{} // Would query recent events

	// Get top players
	dashboard.TopPlayers = []models.PlayerRank{} // Would query leaderboard

	// Get today's revenue
	if err := r.db.QueryRowContext(ctx, "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE DATE(created_at) = CURRENT_DATE").Scan(&dashboard.RevenueToday); err != nil {
		r.logger.Warn("Failed to get today's revenue", zap.Error(err))
	}

	// Get new players today
	if err := r.db.QueryRowContext(ctx, "SELECT COUNT(*) FROM players WHERE DATE(created_at) = CURRENT_DATE").Scan(&dashboard.NewPlayersToday); err != nil {
		r.logger.Warn("Failed to get new players today", zap.Error(err))
	}

	// Cache result with very short TTL
	if data, err := json.Marshal(dashboard); err == nil {
		r.redis.Set(ctx, cacheKey, data, 30*time.Second)
	}

	return dashboard, nil
}

func (r *Repository) StoreAnalyticsEvent(ctx context.Context, eventType string, data map[string]interface{}) error {
	// Store in Redis for real-time processing
	eventKey := fmt.Sprintf("analytics_event:%s:%d", eventType, time.Now().Unix())

	eventData := map[string]interface{}{
		"type":      eventType,
		"data":      data,
		"timestamp": time.Now(),
	}

	if jsonData, err := json.Marshal(eventData); err == nil {
		// Store with 24 hour TTL
		r.redis.Set(ctx, eventKey, jsonData, 24*time.Hour)
	}

	// Also add to event stream for processing
	streamKey := "analytics_events"
	r.redis.XAdd(ctx, &redis.XAddArgs{
		Stream: streamKey,
		Values: map[string]interface{}{
			"type":      eventType,
			"data":      data,
			"timestamp": time.Now().Unix(),
		},
	})

	return nil
}

// Health check
func (r *Repository) HealthCheck(ctx context.Context) error {
	// Test database connection
	if err := r.db.PingContext(ctx); err != nil {
		r.logger.Error("Database health check failed", zap.Error(err))
		return fmt.Errorf("database health check failed: %w", err)
	}

	// Test Redis connection
	if err := r.redis.Ping(ctx).Err(); err != nil {
		r.logger.Error("Redis health check failed", zap.Error(err))
		return fmt.Errorf("redis health check failed: %w", err)
	}

	return nil
}

// Close closes database and Redis connections
func (r *Repository) Close() error {
	if r.db != nil {
		if err := r.db.Close(); err != nil {
			r.logger.Error("Failed to close database connection", zap.Error(err))
		}
	}

	if r.redis != nil {
		if err := r.redis.Close(); err != nil {
			r.logger.Error("Failed to close Redis connection", zap.Error(err))
		}
	}

	return nil
}

// PERFORMANCE: Repository optimized for analytics workloads
// Multi-level caching (Redis) reduces database load
// Connection pooling configured for high-throughput analytics queries
// Structured logging for performance monitoring
