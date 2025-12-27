// Social Cache - Redis caching layer
// Issue: #140875791
// PERFORMANCE: Redis clustering, TTL management, cache hit rate >95%

package cache

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/google/uuid"

	"social-service-go/pkg/models"
)

// Cache provides Redis caching for social systems
type Cache struct {
	client *redis.Client
}

// NewCache creates a new cache instance
func NewCache(client *redis.Client) *Cache {
	return &Cache{client: client}
}

// RELATIONSHIP CACHE METHODS

// GetRelationship retrieves cached relationship
func (c *Cache) GetRelationship(ctx context.Context, sourceID, targetID uuid.UUID, sourceType, targetType models.EntityType) (*models.Relationship, bool) {
	key := fmt.Sprintf("social:relationship:%s:%s:%s:%s:%s", sourceType, sourceID, targetType, targetID, sourceType)

	val, err := c.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, false
		}
		fmt.Printf("Redis error: %v\n", err)
		return nil, false
	}

	var rel models.Relationship
	if err := json.Unmarshal([]byte(val), &rel); err != nil {
		fmt.Printf("JSON unmarshal error: %v\n", err)
		return nil, false
	}

	return &rel, true
}

// SetRelationship caches relationship
func (c *Cache) SetRelationship(ctx context.Context, rel *models.Relationship) {
	key := fmt.Sprintf("social:relationship:%s:%s:%s:%s", rel.SourceType, rel.SourceID, rel.TargetType, rel.TargetID)

	data, err := json.Marshal(rel)
	if err != nil {
		fmt.Printf("JSON marshal error: %v\n", err)
		return
	}

	if err := c.client.Set(ctx, key, data, 30*time.Minute).Err(); err != nil {
		fmt.Printf("Redis set error: %v\n", err)
	}
}

// GetSocialNetwork retrieves cached social network
func (c *Cache) GetSocialNetwork(ctx context.Context, entityID uuid.UUID, entityType models.EntityType) (*models.SocialNetwork, bool) {
	key := fmt.Sprintf("social:network:%s:%s", entityType, entityID)

	val, err := c.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, false
		}
		fmt.Printf("Redis error: %v\n", err)
		return nil, false
	}

	var network models.SocialNetwork
	if err := json.Unmarshal([]byte(val), &network); err != nil {
		fmt.Printf("JSON unmarshal error: %v\n", err)
		return nil, false
	}

	return &network, true
}

// SetSocialNetwork caches social network
func (c *Cache) SetSocialNetwork(ctx context.Context, network *models.SocialNetwork) {
	key := fmt.Sprintf("social:network:%s:%s", network.EntityType, network.EntityID)

	data, err := json.Marshal(network)
	if err != nil {
		fmt.Printf("JSON marshal error: %v\n", err)
		return
	}

	if err := c.client.Set(ctx, key, data, 15*time.Minute).Err(); err != nil {
		fmt.Printf("Redis set error: %v\n", err)
	}
}

// ORDER CACHE METHODS

// GetOrderBoard retrieves cached order board
func (c *Cache) GetOrderBoard(ctx context.Context, regionID string) (*models.OrderBoard, bool) {
	key := fmt.Sprintf("social:orderboard:%s", regionID)

	val, err := c.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, false
		}
		fmt.Printf("Redis error: %v\n", err)
		return nil, false
	}

	var board models.OrderBoard
	if err := json.Unmarshal([]byte(val), &board); err != nil {
		fmt.Printf("JSON unmarshal error: %v\n", err)
		return nil, false
	}

	return &board, true
}

// SetOrderBoard caches order board
func (c *Cache) SetOrderBoard(ctx context.Context, board *models.OrderBoard) {
	key := fmt.Sprintf("social:orderboard:%s", board.RegionID)

	data, err := json.Marshal(board)
	if err != nil {
		fmt.Printf("JSON marshal error: %v\n", err)
		return
	}

	if err := c.client.Set(ctx, key, data, 5*time.Minute).Err(); err != nil {
		fmt.Printf("Redis set error: %v\n", err)
	}
}

// InvalidateOrderBoard invalidates order board cache
func (c *Cache) InvalidateOrderBoard(ctx context.Context, regionID string) {
	key := fmt.Sprintf("social:orderboard:%s", regionID)

	if err := c.client.Del(ctx, key).Err(); err != nil {
		fmt.Printf("Redis delete error: %v\n", err)
	}
}

// GetOrder retrieves cached order
func (c *Cache) GetOrder(ctx context.Context, orderID uuid.UUID) (*models.Order, bool) {
	key := fmt.Sprintf("social:order:%s", orderID)

	val, err := c.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, false
		}
		fmt.Printf("Redis error: %v\n", err)
		return nil, false
	}

	var order models.Order
	if err := json.Unmarshal([]byte(val), &order); err != nil {
		fmt.Printf("JSON unmarshal error: %v\n", err)
		return nil, false
	}

	return &order, true
}

// SetOrder caches order
func (c *Cache) SetOrder(ctx context.Context, order *models.Order) {
	key := fmt.Sprintf("social:order:%s", order.ID)

	data, err := json.Marshal(order)
	if err != nil {
		fmt.Printf("JSON marshal error: %v\n", err)
		return
	}

	if err := c.client.Set(ctx, key, data, 10*time.Minute).Err(); err != nil {
		fmt.Printf("Redis set error: %v\n", err)
	}
}

// NPC HIRING CACHE METHODS

// GetAvailableNPCs retrieves cached available NPCs
func (c *Cache) GetAvailableNPCs(ctx context.Context, regionID string) ([]models.NPCAvailability, bool) {
	key := fmt.Sprintf("social:npcs:%s", regionID)

	val, err := c.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, false
		}
		fmt.Printf("Redis error: %v\n", err)
		return nil, false
	}

	var npcs []models.NPCAvailability
	if err := json.Unmarshal([]byte(val), &npcs); err != nil {
		fmt.Printf("JSON unmarshal error: %v\n", err)
		return nil, false
	}

	return npcs, true
}

// SetAvailableNPCs caches available NPCs
func (c *Cache) SetAvailableNPCs(ctx context.Context, regionID string, npcs []models.NPCAvailability) {
	key := fmt.Sprintf("social:npcs:%s", regionID)

	data, err := json.Marshal(npcs)
	if err != nil {
		fmt.Printf("JSON marshal error: %v\n", err)
		return
	}

	if err := c.client.Set(ctx, key, data, 15*time.Minute).Err(); err != nil {
		fmt.Printf("Redis set error: %v\n", err)
	}
}

// GetNPCHiring retrieves cached NPC hiring
func (c *Cache) GetNPCHiring(ctx context.Context, hiringID uuid.UUID) (*models.NPCHiring, bool) {
	key := fmt.Sprintf("social:npchiring:%s", hiringID)

	val, err := c.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, false
		}
		fmt.Printf("Redis error: %v\n", err)
		return nil, false
	}

	var hiring models.NPCHiring
	if err := json.Unmarshal([]byte(val), &hiring); err != nil {
		fmt.Printf("JSON unmarshal error: %v\n", err)
		return nil, false
	}

	return &hiring, true
}

// SetNPCHiring caches NPC hiring
func (c *Cache) SetNPCHiring(ctx context.Context, hiring *models.NPCHiring) {
	key := fmt.Sprintf("social:npchiring:%s", hiring.ID)

	data, err := json.Marshal(hiring)
	if err != nil {
		fmt.Printf("JSON marshal error: %v\n", err)
		return
	}

	if err := c.client.Set(ctx, key, data, 30*time.Minute).Err(); err != nil {
		fmt.Printf("Redis set error: %v\n", err)
	}
}

// GENERAL CACHE METHODS

// InvalidateByPattern invalidates cache keys matching a pattern
func (c *Cache) InvalidateByPattern(ctx context.Context, pattern string) error {
	keys, err := c.client.Keys(ctx, pattern).Result()
	if err != nil {
		return fmt.Errorf("failed to get keys for pattern %s: %w", pattern, err)
	}

	if len(keys) > 0 {
		if err := c.client.Del(ctx, keys...).Err(); err != nil {
			return fmt.Errorf("failed to delete keys: %w", err)
		}
	}

	return nil
}

// ClearAllSocialCache clears all social-related cache
func (c *Cache) ClearAllSocialCache(ctx context.Context) error {
	patterns := []string{
		"social:relationship:*",
		"social:network:*",
		"social:orderboard:*",
		"social:order:*",
		"social:npcs:*",
		"social:npchiring:*",
	}

	for _, pattern := range patterns {
		if err := c.InvalidateByPattern(ctx, pattern); err != nil {
			fmt.Printf("Failed to clear cache pattern %s: %v\n", pattern, err)
		}
	}

	return nil
}

// GetCacheStats returns cache statistics
func (c *Cache) GetCacheStats(ctx context.Context) map[string]interface{} {
	info := c.client.Info(ctx, "stats")
	if info.Err() != nil {
		return map[string]interface{}{"error": info.Err().Error()}
	}

	return map[string]interface{}{
		"status": "connected",
		"info":   info.Val(),
	}
}
