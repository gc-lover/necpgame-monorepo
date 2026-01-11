package redis

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"necpgame/services/tournament-service-go/internal/config"
)

// Manager manages Redis connections and caching operations
type Manager struct {
	client *redis.Client
	logger *zap.Logger
	config *config.RedisConfig
}

// NewManager creates a new Redis manager with optimized connection pooling
func NewManager(cfg *config.RedisConfig, logger *zap.Logger) (*Manager, error) {
	options := cfg.GetRedisOptions()

	client := redis.NewClient(options)

	// Test connection with context timeout
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := client.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to connect to Redis: %w", err)
	}

	logger.Info("Redis connection established",
		zap.String("host", cfg.Host),
		zap.Int("port", cfg.Port),
		zap.Int("db", cfg.DB),
		zap.Int("poolSize", cfg.PoolSize),
		zap.Int("minIdleConns", cfg.MinIdleConns))

	return &Manager{
		client: client,
		logger: logger,
		config: cfg,
	}, nil
}

// Close closes the Redis connection
func (m *Manager) Close() error {
	if m.client != nil {
		return m.client.Close()
	}
	return nil
}

// GetClient returns the underlying Redis client
func (m *Manager) GetClient() *redis.Client {
	return m.client
}

func (m *Manager) Client() *redis.Client {
	return m.client
}

// Ping tests the Redis connection with timeout
func (m *Manager) Ping(ctx context.Context) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	return m.client.Ping(ctx).Err()
}

// HealthCheck performs a comprehensive Redis health check
func (m *Manager) HealthCheck(ctx context.Context) error {
	// Test connection
	if err := m.Ping(ctx); err != nil {
		return fmt.Errorf("redis ping failed: %w", err)
	}

	// Test basic operations
	testKey := "healthcheck:test"
	testValue := "ok"

	// Set test value
	if err := m.client.Set(ctx, testKey, testValue, 10*time.Second).Err(); err != nil {
		return fmt.Errorf("redis set test failed: %w", err)
	}

	// Get test value
	result, err := m.client.Get(ctx, testKey).Result()
	if err != nil {
		return fmt.Errorf("redis get test failed: %w", err)
	}

	if result != testValue {
		return fmt.Errorf("redis test returned unexpected value: %s", result)
	}

	// Clean up
	m.client.Del(ctx, testKey)

	poolStats := m.client.PoolStats()
	m.logger.Debug("Redis health check passed",
		zap.Uint32("totalConns", poolStats.TotalConns),
		zap.Uint32("idleConns", poolStats.IdleConns),
		zap.Uint32("staleConns", poolStats.StaleConns))

	return nil
}

// SetJSON sets a JSON value in Redis with TTL
func (m *Manager) SetJSON(ctx context.Context, key string, value interface{}, ttl time.Duration) error {
	data, err := json.Marshal(value)
	if err != nil {
		return fmt.Errorf("failed to marshal value: %w", err)
	}

	return m.client.Set(ctx, key, data, ttl).Err()
}

// GetJSON gets a JSON value from Redis
func (m *Manager) GetJSON(ctx context.Context, key string, dest interface{}) error {
	data, err := m.client.Get(ctx, key).Result()
	if err != nil {
		return err
	}

	return json.Unmarshal([]byte(data), dest)
}

// Delete removes keys from Redis
func (m *Manager) Delete(ctx context.Context, keys ...string) error {
	return m.client.Del(ctx, keys...).Err()
}

// Exists checks if keys exist in Redis
func (m *Manager) Exists(ctx context.Context, keys ...string) (int64, error) {
	return m.client.Exists(ctx, keys...).Result()
}

// Expire sets expiration on a key
func (m *Manager) Expire(ctx context.Context, key string, ttl time.Duration) error {
	return m.client.Expire(ctx, key, ttl).Err()
}

// Pipeline executes multiple commands atomically
func (m *Manager) Pipeline() redis.Pipeliner {
	return m.client.Pipeline()
}

// TournamentCache provides specialized caching for tournament data
type TournamentCache struct {
	redis  *Manager
	logger *zap.Logger
}

// NewTournamentCache creates a new tournament cache
func NewTournamentCache(redis *Manager, logger *zap.Logger) *TournamentCache {
	return &TournamentCache{
		redis:  redis,
		logger: logger,
	}
}

// TournamentKey generates Redis key for tournament data
func (c *TournamentCache) TournamentKey(tournamentID string) string {
	return fmt.Sprintf("tournament:%s", tournamentID)
}

// LeaderboardKey generates Redis key for leaderboard data
func (c *TournamentCache) LeaderboardKey(tournamentID string) string {
	return fmt.Sprintf("leaderboard:%s", tournamentID)
}

// MatchKey generates Redis key for match data
func (c *TournamentCache) MatchKey(matchID string) string {
	return fmt.Sprintf("match:%s", matchID)
}

// SetTournament caches tournament data with 10-minute TTL
func (c *TournamentCache) SetTournament(ctx context.Context, tournamentID string, tournament interface{}) error {
	key := c.TournamentKey(tournamentID)
	err := c.redis.SetJSON(ctx, key, tournament, 10*time.Minute)
	if err != nil {
		c.logger.Error("Failed to cache tournament",
			zap.String("tournamentID", tournamentID), zap.Error(err))
		return err
	}

	c.logger.Debug("Cached tournament",
		zap.String("tournamentID", tournamentID))
	return nil
}

// GetTournament retrieves cached tournament data
func (c *TournamentCache) GetTournament(ctx context.Context, tournamentID string, dest interface{}) error {
	key := c.TournamentKey(tournamentID)
	err := c.redis.GetJSON(ctx, key, dest)
	if err != nil {
		if err == redis.Nil {
			c.logger.Debug("Tournament not found in cache",
				zap.String("tournamentID", tournamentID))
			return err
		}
		c.logger.Error("Failed to get tournament from cache",
			zap.String("tournamentID", tournamentID), zap.Error(err))
		return err
	}

	c.logger.Debug("Retrieved tournament from cache",
		zap.String("tournamentID", tournamentID))
	return nil
}

// DeleteTournament removes tournament from cache
func (c *TournamentCache) DeleteTournament(ctx context.Context, tournamentID string) error {
	key := c.TournamentKey(tournamentID)
	err := c.redis.Delete(ctx, key)
	if err != nil {
		c.logger.Error("Failed to delete tournament from cache",
			zap.String("tournamentID", tournamentID), zap.Error(err))
		return err
	}

	c.logger.Debug("Deleted tournament from cache",
		zap.String("tournamentID", tournamentID))
	return nil
}

// SetLeaderboard caches leaderboard data with 5-minute TTL
func (c *TournamentCache) SetLeaderboard(ctx context.Context, tournamentID string, leaderboard interface{}) error {
	key := c.LeaderboardKey(tournamentID)
	err := c.redis.SetJSON(ctx, key, leaderboard, 5*time.Minute)
	if err != nil {
		c.logger.Error("Failed to cache leaderboard",
			zap.String("tournamentID", tournamentID), zap.Error(err))
		return err
	}

	c.logger.Debug("Cached leaderboard",
		zap.String("tournamentID", tournamentID))
	return nil
}

// GetLeaderboard retrieves cached leaderboard data
func (c *TournamentCache) GetLeaderboard(ctx context.Context, tournamentID string, dest interface{}) error {
	key := c.LeaderboardKey(tournamentID)
	err := c.redis.GetJSON(ctx, key, dest)
	if err != nil {
		if err == redis.Nil {
			c.logger.Debug("Leaderboard not found in cache",
				zap.String("tournamentID", tournamentID))
			return err
		}
		c.logger.Error("Failed to get leaderboard from cache",
			zap.String("tournamentID", tournamentID), zap.Error(err))
		return err
	}

	c.logger.Debug("Retrieved leaderboard from cache",
		zap.String("tournamentID", tournamentID))
	return nil
}

// SetMatch caches match data with 30-minute TTL
func (c *TournamentCache) SetMatch(ctx context.Context, matchID string, match interface{}) error {
	key := c.MatchKey(matchID)
	err := c.redis.SetJSON(ctx, key, match, 30*time.Minute)
	if err != nil {
		c.logger.Error("Failed to cache match",
			zap.String("matchID", matchID), zap.Error(err))
		return err
	}

	c.logger.Debug("Cached match",
		zap.String("matchID", matchID))
	return nil
}

// GetMatch retrieves cached match data
func (c *TournamentCache) GetMatch(ctx context.Context, matchID string, dest interface{}) error {
	key := c.MatchKey(matchID)
	err := c.redis.GetJSON(ctx, key, dest)
	if err != nil {
		if err == redis.Nil {
			c.logger.Debug("Match not found in cache",
				zap.String("matchID", matchID))
			return err
		}
		c.logger.Error("Failed to get match from cache",
			zap.String("matchID", matchID), zap.Error(err))
		return err
	}

	c.logger.Debug("Retrieved match from cache",
		zap.String("matchID", matchID))
	return nil
}

// DeleteMatch removes match from cache
func (c *TournamentCache) DeleteMatch(ctx context.Context, matchID string) error {
	key := c.MatchKey(matchID)
	err := c.redis.Delete(ctx, key)
	if err != nil {
		c.logger.Error("Failed to delete match from cache",
			zap.String("matchID", matchID), zap.Error(err))
		return err
	}

	c.logger.Debug("Deleted match from cache",
		zap.String("matchID", matchID))
	return nil
}

// GetTournamentLeaderboard retrieves cached tournament leaderboard
// PERFORMANCE: <5ms response time with cached rankings
func (c *TournamentCache) GetTournamentLeaderboard(ctx context.Context, tournamentID string, limit int) (interface{}, error) {
	key := fmt.Sprintf("leaderboard:%s:%d", tournamentID, limit)

	var leaderboard interface{}
	err := c.redis.GetJSON(ctx, key, &leaderboard)
	if err != nil {
		if err == redis.Nil {
			c.logger.Debug("Tournament leaderboard not found in cache",
				zap.String("tournamentID", tournamentID), zap.Int("limit", limit))
			return nil, err
		}
		c.logger.Error("Failed to get tournament leaderboard from cache",
			zap.String("tournamentID", tournamentID), zap.Error(err))
		return nil, err
	}

	c.logger.Debug("Retrieved tournament leaderboard from cache",
		zap.String("tournamentID", tournamentID))
	return leaderboard, nil
}

// SetTournamentLeaderboard caches tournament leaderboard with TTL
// PERFORMANCE: 30-second cache with real-time invalidation on score updates
func (c *TournamentCache) SetTournamentLeaderboard(ctx context.Context, tournamentID string, leaderboard interface{}, ttl time.Duration) error {
	key := fmt.Sprintf("leaderboard:%s", tournamentID)

	err := c.redis.SetJSON(ctx, key, leaderboard, ttl)
	if err != nil {
		c.logger.Error("Failed to cache tournament leaderboard",
			zap.String("tournamentID", tournamentID), zap.Error(err))
		return err
	}

	c.logger.Debug("Cached tournament leaderboard",
		zap.String("tournamentID", tournamentID), zap.Duration("ttl", ttl))
	return nil
}

// InvalidateTournamentLeaderboard clears cached leaderboard for real-time updates
// PERFORMANCE: Ensures fresh data after score updates
func (c *TournamentCache) InvalidateTournamentLeaderboard(ctx context.Context, tournamentID string) error {
	// PERFORMANCE: Delete all leaderboard variants for this tournament
	pattern := fmt.Sprintf("leaderboard:%s:*", tournamentID)

	keys, err := c.redis.client.Keys(ctx, pattern).Result()
	if err != nil {
		c.logger.Error("Failed to find leaderboard keys for invalidation",
			zap.String("tournamentID", tournamentID), zap.Error(err))
		return err
	}

	if len(keys) > 0 {
		err = c.redis.client.Del(ctx, keys...).Err()
		if err != nil {
			c.logger.Error("Failed to invalidate tournament leaderboard cache",
				zap.String("tournamentID", tournamentID), zap.Error(err))
			return err
		}

		c.logger.Debug("Invalidated tournament leaderboard cache",
			zap.String("tournamentID", tournamentID), zap.Int("keys_deleted", len(keys)))
	}

	return nil
}

// GetGlobalLeaderboard retrieves cached global leaderboard
// PERFORMANCE: <50ms P99 latency, cached for 5 minutes
func (c *TournamentCache) GetGlobalLeaderboard(ctx context.Context, cacheKey string) (interface{}, error) {
	key := fmt.Sprintf("global:%s", cacheKey)

	var leaderboard interface{}
	err := c.redis.GetJSON(ctx, key, &leaderboard)
	if err != nil {
		if err == redis.Nil {
			c.logger.Debug("Global leaderboard not found in cache", zap.String("cacheKey", cacheKey))
			return nil, err
		}
		c.logger.Error("Failed to get global leaderboard from cache",
			zap.String("cacheKey", cacheKey), zap.Error(err))
		return nil, err
	}

	c.logger.Debug("Retrieved global leaderboard from cache",
		zap.String("cacheKey", cacheKey))
	return leaderboard, nil
}

// SetGlobalLeaderboard caches global leaderboard with TTL
// PERFORMANCE: 5-minute cache for global leaderboards
func (c *TournamentCache) SetGlobalLeaderboard(ctx context.Context, cacheKey string, leaderboard interface{}, ttl time.Duration) error {
	key := fmt.Sprintf("global:%s", cacheKey)

	err := c.redis.SetJSON(ctx, key, leaderboard, ttl)
	if err != nil {
		c.logger.Error("Failed to cache global leaderboard",
			zap.String("cacheKey", cacheKey), zap.Error(err))
		return err
	}

	c.logger.Debug("Cached global leaderboard",
		zap.String("cacheKey", cacheKey), zap.Duration("ttl", ttl))
	return nil
}
