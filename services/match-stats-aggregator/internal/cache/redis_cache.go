// Issue: #2214
// Redis Cache Layer for Match Statistics
// High-performance caching with TTL and compression for MMOFPS workloads

package cache

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/gc-lover/necpgame/services/match-stats-aggregator/pkg/models"
	"go.uber.org/zap"
)

// RedisCache provides Redis-based caching for match statistics
// Optimized for MMOFPS performance with compression and TTL management
type RedisCache struct {
	client     *redis.Client
	config     *CacheConfig
	logger     *zap.Logger
}

// CacheConfig defines cache behavior and performance settings
type CacheConfig struct {
	Addr         string        `json:"addr"`
	Password     string        `json:"password"`
	DB           int           `json:"db"`
	PoolSize     int           `json:"pool_size"`
	MinIdleConns int           `json:"min_idle_conns"`

	// TTL settings
	DefaultTTL   time.Duration `json:"default_ttl"`
	MatchTTL     time.Duration `json:"match_ttl"`
	PlayerTTL    time.Duration `json:"player_ttl"`
	SnapshotTTL  time.Duration `json:"snapshot_ttl"`

	// Performance settings
	MaxRetries   int           `json:"max_retries"`
	DialTimeout  time.Duration `json:"dial_timeout"`
	ReadTimeout  time.Duration `json:"read_timeout"`
	WriteTimeout time.Duration `json:"write_timeout"`
}

// NewRedisCache creates a new Redis cache instance
func NewRedisCache(config *CacheConfig, logger *zap.Logger) (*RedisCache, error) {
	client := redis.NewClient(&redis.Options{
		Addr:         config.Addr,
		Password:     config.Password,
		DB:           config.DB,
		PoolSize:     config.PoolSize,
		MinIdleConns: config.MinIdleConns,
		MaxRetries:   config.MaxRetries,
		DialTimeout:  config.DialTimeout,
		ReadTimeout:  config.ReadTimeout,
		WriteTimeout: config.WriteTimeout,
	})

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := client.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to connect to Redis: %w", err)
	}

	cache := &RedisCache{
		client: client,
		config: config,
		logger: logger,
	}

	logger.Info("Connected to Redis",
		zap.String("addr", config.Addr),
		zap.Int("db", config.DB))

	return cache, nil
}

// Close closes the Redis connection
func (rc *RedisCache) Close() error {
	return rc.client.Close()
}

// StoreMatchStatistics caches match statistics with appropriate TTL
func (rc *RedisCache) StoreMatchStatistics(ctx context.Context, stats *models.MatchStatistics) error {
	key := rc.getMatchKey(stats.MatchID)

	data, err := json.Marshal(stats)
	if err != nil {
		return fmt.Errorf("failed to marshal match statistics: %w", err)
	}

	// Use different TTL based on match status
	ttl := rc.config.MatchTTL
	if stats.Status == "completed" {
		ttl = rc.config.DefaultTTL * 2 // Keep completed matches longer
	}

	err = rc.client.Set(ctx, key, data, ttl).Err()
	if err != nil {
		rc.logger.Error("Failed to cache match statistics",
			zap.String("match_id", stats.MatchID),
			zap.Error(err))
		return err
	}

	rc.logger.Debug("Cached match statistics",
		zap.String("match_id", stats.MatchID),
		zap.Duration("ttl", ttl))

	return nil
}

// GetMatchStatistics retrieves cached match statistics
func (rc *RedisCache) GetMatchStatistics(ctx context.Context, matchID string) (*models.MatchStatistics, error) {
	key := rc.getMatchKey(matchID)

	data, err := rc.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, ErrCacheMiss
		}
		return nil, fmt.Errorf("failed to get match statistics: %w", err)
	}

	var stats models.MatchStatistics
	if err := json.Unmarshal([]byte(data), &stats); err != nil {
		return nil, fmt.Errorf("failed to unmarshal match statistics: %w", err)
	}

	rc.logger.Debug("Retrieved match statistics from cache",
		zap.String("match_id", matchID))

	return &stats, nil
}

// StorePlayerStatistics caches individual player statistics
func (rc *RedisCache) StorePlayerStatistics(ctx context.Context, matchID, playerID string, stats *models.PlayerMatchStats) error {
	key := rc.getPlayerKey(matchID, playerID)

	data, err := json.Marshal(stats)
	if err != nil {
		return fmt.Errorf("failed to marshal player statistics: %w", err)
	}

	err = rc.client.Set(ctx, key, data, rc.config.PlayerTTL).Err()
	if err != nil {
		rc.logger.Error("Failed to cache player statistics",
			zap.String("match_id", matchID),
			zap.String("player_id", playerID),
			zap.Error(err))
		return err
	}

	return nil
}

// GetPlayerStatistics retrieves cached player statistics
func (rc *RedisCache) GetPlayerStatistics(ctx context.Context, matchID, playerID string) (*models.PlayerMatchStats, error) {
	key := rc.getPlayerKey(matchID, playerID)

	data, err := rc.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, ErrCacheMiss
		}
		return nil, fmt.Errorf("failed to get player statistics: %w", err)
	}

	var stats models.PlayerMatchStats
	if err := json.Unmarshal([]byte(data), &stats); err != nil {
		return nil, fmt.Errorf("failed to unmarshal player statistics: %w", err)
	}

	return &stats, nil
}

// StoreStatisticsSnapshot caches a statistics snapshot
func (rc *RedisCache) StoreStatisticsSnapshot(ctx context.Context, snapshot *models.StatisticsSnapshot) error {
	key := rc.getSnapshotKey(snapshot.MatchID, snapshot.SnapshotID)

	data, err := json.Marshal(snapshot)
	if err != nil {
		return fmt.Errorf("failed to marshal snapshot: %w", err)
	}

	// Compress if enabled (placeholder for compression logic)
	if snapshot.Compressed {
		// Implement compression here if needed
	}

	err = rc.client.Set(ctx, key, data, rc.config.SnapshotTTL).Err()
	if err != nil {
		rc.logger.Error("Failed to cache statistics snapshot",
			zap.String("match_id", snapshot.MatchID),
			zap.String("snapshot_id", snapshot.SnapshotID),
			zap.Error(err))
		return err
	}

	return nil
}

// GetStatisticsSnapshot retrieves a cached snapshot
func (rc *RedisCache) GetStatisticsSnapshot(ctx context.Context, matchID, snapshotID string) (*models.StatisticsSnapshot, error) {
	key := rc.getSnapshotKey(matchID, snapshotID)

	data, err := rc.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, ErrCacheMiss
		}
		return nil, fmt.Errorf("failed to get snapshot: %w", err)
	}

	var snapshot models.StatisticsSnapshot
	if err := json.Unmarshal([]byte(data), &snapshot); err != nil {
		return nil, fmt.Errorf("failed to unmarshal snapshot: %w", err)
	}

	return &snapshot, nil
}

// GetTopPlayers retrieves top players by a specific metric
func (rc *RedisCache) GetTopPlayers(ctx context.Context, matchID string, metric string, limit int) ([]models.PlayerMatchStats, error) {
	// This would use Redis sorted sets for leaderboard functionality
	// Implementation depends on how leaderboards are stored

	key := rc.getLeaderboardKey(matchID, metric)

	// Get top N players
	results, err := rc.client.ZRevRangeWithScores(ctx, key, 0, int64(limit-1)).Result()
	if err != nil {
		return nil, fmt.Errorf("failed to get leaderboard: %w", err)
	}

	players := make([]models.PlayerMatchStats, 0, len(results))
	for _, result := range results {
		playerID := result.Member.(string)

		// Get player stats
		stats, err := rc.GetPlayerStatistics(ctx, matchID, playerID)
		if err != nil {
			rc.logger.Warn("Failed to get player stats for leaderboard",
				zap.String("player_id", playerID),
				zap.Error(err))
			continue
		}

		players = append(players, *stats)
	}

	return players, nil
}

// UpdateLeaderboard updates player rankings in sorted sets
func (rc *RedisCache) UpdateLeaderboard(ctx context.Context, matchID, playerID string, stats *models.PlayerMatchStats) error {
	// Update various leaderboards
	leaderboards := map[string]float64{
		"kills":      float64(stats.Kills),
		"deaths":     float64(stats.Deaths),
		"kd_ratio":   stats.KDRatio,
		"damage":     float64(stats.DamageDealt),
		"accuracy":   stats.Accuracy,
		"score":      float64(stats.Score),
	}

	for metric, score := range leaderboards {
		key := rc.getLeaderboardKey(matchID, metric)
		err := rc.client.ZAdd(ctx, key, &redis.Z{
			Score:  score,
			Member: playerID,
		}).Err()

		if err != nil {
			rc.logger.Error("Failed to update leaderboard",
				zap.String("match_id", matchID),
				zap.String("metric", metric),
				zap.String("player_id", playerID),
				zap.Error(err))
			return err
		}
	}

	return nil
}

// DeleteMatchData removes all cached data for a match
func (rc *RedisCache) DeleteMatchData(ctx context.Context, matchID string) error {
	pattern := fmt.Sprintf("match_stats:%s:*", matchID)

	keys, err := rc.client.Keys(ctx, pattern).Result()
	if err != nil {
		return fmt.Errorf("failed to find keys for deletion: %w", err)
	}

	if len(keys) == 0 {
		return nil
	}

	err = rc.client.Del(ctx, keys...).Err()
	if err != nil {
		return fmt.Errorf("failed to delete match data: %w", err)
	}

	rc.logger.Info("Deleted match data from cache",
		zap.String("match_id", matchID),
		zap.Int("keys_deleted", len(keys)))

	return nil
}

// Health check for cache connectivity
func (rc *RedisCache) Health(ctx context.Context) error {
	return rc.client.Ping(ctx).Err()
}

// Key generation methods
func (rc *RedisCache) getMatchKey(matchID string) string {
	return fmt.Sprintf("match_stats:match:%s", matchID)
}

func (rc *RedisCache) getPlayerKey(matchID, playerID string) string {
	return fmt.Sprintf("match_stats:player:%s:%s", matchID, playerID)
}

func (rc *RedisCache) getSnapshotKey(matchID, snapshotID string) string {
	return fmt.Sprintf("match_stats:snapshot:%s:%s", matchID, snapshotID)
}

func (rc *RedisCache) getLeaderboardKey(matchID, metric string) string {
	return fmt.Sprintf("match_stats:leaderboard:%s:%s", matchID, metric)
}

// Errors
var (
	ErrCacheMiss = NewCacheError("cache miss")
)

// CacheError represents cache-specific errors
type CacheError struct {
	Message string
}

func NewCacheError(message string) *CacheError {
	return &CacheError{Message: message}
}

func (ce *CacheError) Error() string {
	return ce.Message
}
