// Issue: #1943 - 3-tier cache implementation for MMOFPS performance
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"github.com/redis/go-redis/v9"
)

// GuildCache implements 3-tier caching (Memory → Redis → Database)
type GuildCache struct {
	redisClient *redis.Client
	repo        *Repository
	memoryCache sync.Map // L1: In-memory cache
}

// OPTIMIZATION: Cache TTL constants for MMOFPS performance
const (
	GuildCacheTTL      = 5 * time.Minute  // Issue: #1943 - Guild data cache
	ListCacheTTL       = 2 * time.Minute  // Issue: #1943 - List data cache
	MemberCacheTTL     = 10 * time.Minute // Issue: #1943 - Member data cache
)

// NewGuildCache creates new 3-tier cache
func NewGuildCache(redisClient *redis.Client, repo *Repository) *GuildCache {
	return &GuildCache{
		redisClient: redisClient,
		repo:        repo,
	}
}

// GetGuild retrieves guild from cache (L1 → L2 → L3)
func (c *GuildCache) GetGuild(ctx context.Context, guildID string) (*GuildResponse, error) {
	// L1: Check memory cache
	if cached, ok := c.memoryCache.Load(guildID); ok {
		if response, ok := cached.(*GuildResponse); ok {
			return response, nil
		}
	}

	// L2: Check Redis cache
	redisKey := fmt.Sprintf("guild:%s", guildID)
	if c.redisClient != nil {
		if data, err := c.redisClient.Get(ctx, redisKey).Result(); err == nil {
			var response GuildResponse
			if err := json.Unmarshal([]byte(data), &response); err == nil {
				// Store in memory cache for faster future access
				c.memoryCache.Store(guildID, &response)
				return &response, nil
			}
		}
	}

	return nil, fmt.Errorf("guild not in cache")
}

// GetGuildList retrieves guild list from cache
func (c *GuildCache) GetGuildList(ctx context.Context, params *GetGuildsParams) ([]*GuildResponse, error) {
	cacheKey := fmt.Sprintf("guilds:list:page%d:limit%d", params.Page, params.Limit)

	// L1: Check memory cache
	if cached, ok := c.memoryCache.Load(cacheKey); ok {
		if responses, ok := cached.([]*GuildResponse); ok {
			return responses, nil
		}
	}

	// L2: Check Redis cache
	if c.redisClient != nil {
		if data, err := c.redisClient.Get(ctx, cacheKey).Result(); err == nil {
			var responses []*GuildResponse
			if err := json.Unmarshal([]byte(data), &responses); err == nil {
				// Store in memory cache
				c.memoryCache.Store(cacheKey, responses)
				return responses, nil
			}
		}
	}

	return nil, fmt.Errorf("guild list not in cache")
}

// Store methods for caching results
func (c *GuildCache) storeInRedisGuild(ctx context.Context, guildID string, response *GuildResponse) {
	if c.redisClient == nil {
		return
	}

	data, err := json.Marshal(response)
	if err != nil {
		return
	}

	redisKey := fmt.Sprintf("guild:%s", guildID)
	c.redisClient.Set(ctx, redisKey, data, GuildCacheTTL)
}

func (c *GuildCache) storeInMemoryGuild(guildID string, response *GuildResponse) {
	c.memoryCache.Store(guildID, response)
}

func (c *GuildCache) storeInRedisGuildList(ctx context.Context, params *GetGuildsParams, guilds []*GuildResponse, total int) {
	if c.redisClient == nil {
		return
	}

	data, err := json.Marshal(guilds)
	if err != nil {
		return
	}

	cacheKey := fmt.Sprintf("guilds:list:page%d:limit%d", params.Page, params.Limit)
	c.redisClient.Set(ctx, cacheKey, data, ListCacheTTL)
}

func (c *GuildCache) storeInMemoryGuildList(cacheKey string, guilds []*GuildResponse) {
	c.memoryCache.Store(cacheKey, guilds)
}

// Invalidation methods
func (c *GuildCache) InvalidateGuild(ctx context.Context, guildID string) {
	// Remove from memory cache
	c.memoryCache.Delete(guildID)

	// Remove from Redis cache
	if c.redisClient != nil {
		redisKey := fmt.Sprintf("guild:%s", guildID)
		c.redisClient.Del(ctx, redisKey)
	}
}

func (c *GuildCache) InvalidateGuildList(ctx context.Context) {
	// Clear all guild list caches from memory
	c.memoryCache.Range(func(key, value interface{}) bool {
		if keyStr, ok := key.(string); ok && len(keyStr) > 12 && keyStr[:12] == "guilds:list:" {
			c.memoryCache.Delete(key)
		}
		return true
	})

	// Clear Redis guild list caches
	if c.redisClient != nil {
		// Use Redis SCAN to find and delete list keys
		iter := c.redisClient.Scan(ctx, 0, "guilds:list:*", 100).Iterator()
		for iter.Next(ctx) {
			c.redisClient.Del(ctx, iter.Val())
		}
	}
}

func (c *GuildCache) InvalidatePlayerGuilds(ctx context.Context, playerID uuid.UUID) {
	playerKey := fmt.Sprintf("player:%s:guilds", playerID.String())

	// Remove from memory cache
	c.memoryCache.Delete(playerKey)

	// Remove from Redis cache
	if c.redisClient != nil {
		c.redisClient.Del(ctx, playerKey)
	}
}
