package redis

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"necpgame/services/matchmaking-service-go/internal/config"
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

// MatchmakingCache provides specialized caching for matchmaking data
type MatchmakingCache struct {
	redis  *Manager
	logger *zap.Logger
}

// NewMatchmakingCache creates a new matchmaking cache
func NewMatchmakingCache(redis *Manager, logger *zap.Logger) *MatchmakingCache {
	return &MatchmakingCache{
		redis:  redis,
		logger: logger,
	}
}

// QueueKey generates Redis key for queue data
func (c *MatchmakingCache) QueueKey(playerID string) string {
	return fmt.Sprintf("matchmaking:queue:%s", playerID)
}

// MatchKey generates Redis key for match data
func (c *MatchmakingCache) MatchKey(matchID string) string {
	return fmt.Sprintf("matchmaking:match:%s", matchID)
}

// SetQueuedPlayer caches queued player data with 10-minute TTL
func (c *MatchmakingCache) SetQueuedPlayer(ctx context.Context, playerID string, player interface{}) error {
	key := c.QueueKey(playerID)
	err := c.redis.SetJSON(ctx, key, player, 10*time.Minute)
	if err != nil {
		c.logger.Error("Failed to cache queued player",
			zap.String("playerID", playerID), zap.Error(err))
		return err
	}

	c.logger.Debug("Cached queued player",
		zap.String("playerID", playerID))
	return nil
}

// GetQueuedPlayer retrieves cached queued player data
func (c *MatchmakingCache) GetQueuedPlayer(ctx context.Context, playerID string, dest interface{}) error {
	key := c.QueueKey(playerID)
	err := c.redis.GetJSON(ctx, key, dest)
	if err != nil {
		if err == redis.Nil {
			c.logger.Debug("Queued player not found in cache",
				zap.String("playerID", playerID))
			return err
		}
		c.logger.Error("Failed to get queued player from cache",
			zap.String("playerID", playerID), zap.Error(err))
		return err
	}

	c.logger.Debug("Retrieved queued player from cache",
		zap.String("playerID", playerID))
	return nil
}

// DeleteQueuedPlayer removes queued player from cache
func (c *MatchmakingCache) DeleteQueuedPlayer(ctx context.Context, playerID string) error {
	key := c.QueueKey(playerID)
	err := c.redis.Delete(ctx, key)
	if err != nil {
		c.logger.Error("Failed to delete queued player from cache",
			zap.String("playerID", playerID), zap.Error(err))
		return err
	}

	c.logger.Debug("Deleted queued player from cache",
		zap.String("playerID", playerID))
	return nil
}

// SetMatch caches match data with 30-minute TTL
func (c *MatchmakingCache) SetMatch(ctx context.Context, matchID string, match interface{}) error {
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
func (c *MatchmakingCache) GetMatch(ctx context.Context, matchID string, dest interface{}) error {
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
func (c *MatchmakingCache) DeleteMatch(ctx context.Context, matchID string) error {
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

// GetQueueStats returns queue statistics using Redis
func (c *MatchmakingCache) GetQueueStats(ctx context.Context) (map[string]int, error) {
	// Use SCAN to get all queue keys (this is a simplified implementation)
	// In production, you might want to use Redis sets or sorted sets for better performance
	keys, err := c.redis.GetClient().Keys(ctx, "matchmaking:queue:*").Result()
	if err != nil {
		return nil, fmt.Errorf("failed to scan queue keys: %w", err)
	}

	stats := make(map[string]int)
	// For now, just count total queued players
	stats["totalQueued"] = len(keys)

	return stats, nil
}