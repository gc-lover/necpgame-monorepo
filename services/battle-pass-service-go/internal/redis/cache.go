package redis

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/redis/go-redis/v9"
)

// Cache provides high-performance caching utilities
type Cache struct {
	client *redis.Client
}

// NewCache creates a new cache instance
func NewCache(client *redis.Client) *Cache {
	return &Cache{client: client}
}

// SetJSON sets a JSON value in cache with TTL
func (c *Cache) SetJSON(ctx context.Context, key string, value interface{}, ttl time.Duration) error {
	data, err := json.Marshal(value)
	if err != nil {
		return fmt.Errorf("failed to marshal value: %w", err)
	}

	return c.client.Set(ctx, key, data, ttl).Err()
}

// GetJSON gets a JSON value from cache
func (c *Cache) GetJSON(ctx context.Context, key string, dest interface{}) error {
	data, err := c.client.Get(ctx, key).Result()
	if err != nil {
		return err
	}

	return json.Unmarshal([]byte(data), dest)
}

// SetNX sets value only if key doesn't exist (useful for locks)
func (c *Cache) SetNX(ctx context.Context, key string, value interface{}, ttl time.Duration) (bool, error) {
	data, err := json.Marshal(value)
	if err != nil {
		return false, fmt.Errorf("failed to marshal value: %w", err)
	}

	return c.client.SetNX(ctx, key, data, ttl).Result()
}

// Delete removes a key from cache
func (c *Cache) Delete(ctx context.Context, keys ...string) error {
	return c.client.Del(ctx, keys...).Err()
}

// Exists checks if a key exists
func (c *Cache) Exists(ctx context.Context, key string) (bool, error) {
	count, err := c.client.Exists(ctx, key).Result()
	return count > 0, err
}

// Expire sets expiration on a key
func (c *Cache) Expire(ctx context.Context, key string, ttl time.Duration) error {
	return c.client.Expire(ctx, key, ttl).Err()
}

// Pipeline executes multiple commands atomically
func (c *Cache) Pipeline() redis.Pipeliner {
	return c.client.Pipeline()
}

// PlayerProgressCache provides specialized caching for player progress
type PlayerProgressCache struct {
	cache *Cache
}

// NewPlayerProgressCache creates a new player progress cache
func NewPlayerProgressCache(cache *Cache) *PlayerProgressCache {
	return &PlayerProgressCache{cache: cache}
}

// CacheKey generates cache key for player progress
func (pc *PlayerProgressCache) CacheKey(playerID, seasonID string) string {
	return fmt.Sprintf("progress:%s:%s", playerID, seasonID)
}

// Set caches player progress
func (pc *PlayerProgressCache) Set(ctx context.Context, playerID, seasonID string, progress interface{}) error {
	key := pc.CacheKey(playerID, seasonID)
	return pc.cache.SetJSON(ctx, key, progress, 10*time.Minute) // 10 minute TTL
}

// Get retrieves cached player progress
func (pc *PlayerProgressCache) Get(ctx context.Context, playerID, seasonID string, dest interface{}) error {
	key := pc.CacheKey(playerID, seasonID)
	return pc.cache.GetJSON(ctx, key, dest)
}

// Invalidate removes player progress from cache
func (pc *PlayerProgressCache) Invalidate(ctx context.Context, playerID, seasonID string) error {
	key := pc.CacheKey(playerID, seasonID)
	return pc.cache.Delete(ctx, key)
}

// LeaderboardCache provides optimized leaderboard caching using Redis sorted sets
type LeaderboardCache struct {
	cache *Cache
}

// NewLeaderboardCache creates a new leaderboard cache
func NewLeaderboardCache(cache *Cache) *LeaderboardCache {
	return &LeaderboardCache{cache: cache}
}

// CacheKey generates cache key for leaderboard
func (lc *LeaderboardCache) CacheKey(seasonID string) string {
	return fmt.Sprintf("leaderboard:%s", seasonID)
}

// UpdateScore updates a player's score in the leaderboard
func (lc *LeaderboardCache) UpdateScore(ctx context.Context, seasonID, playerID string, score float64) error {
	key := lc.CacheKey(seasonID)
	return lc.cache.client.ZAdd(ctx, key, redis.Z{
		Score:  score,
		Member: playerID,
	}).Err()
}

// GetTopPlayers gets top N players from leaderboard
func (lc *LeaderboardCache) GetTopPlayers(ctx context.Context, seasonID string, limit int) ([]redis.Z, error) {
	key := lc.CacheKey(seasonID)
	return lc.cache.client.ZRevRangeWithScores(ctx, key, 0, int64(limit-1)).Result()
}

// GetPlayerRank gets a player's rank in the leaderboard
func (lc *LeaderboardCache) GetPlayerRank(ctx context.Context, seasonID, playerID string) (int64, error) {
	key := lc.CacheKey(seasonID)
	rank, err := lc.cache.client.ZRevRank(ctx, key, playerID).Result()
	if err != nil {
		return 0, err
	}
	return rank + 1, nil // Redis ranks are 0-based
}

// GetPlayerScore gets a player's score
func (lc *LeaderboardCache) GetPlayerScore(ctx context.Context, seasonID, playerID string) (float64, error) {
	key := lc.CacheKey(seasonID)
	return lc.cache.client.ZScore(ctx, key, playerID).Result()
}