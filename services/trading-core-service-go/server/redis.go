// Issue: #2236
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/gc-lover/necpgame-monorepo/services/trading-core-service-go/pkg/api"
)

// RedisClient wraps Redis operations for trading service
type RedisClient struct {
	client *redis.Client
}

// NewRedisClient creates a new Redis client
func NewRedisClient(addr string) *RedisClient {
	rdb := redis.NewClient(&redis.Options{
		Addr: addr,
		// BACKEND NOTE: Redis connection pooling for MMOFPS performance
		PoolSize:     20,               // Connection pool size
		MinIdleConns: 5,                // Minimum idle connections
		PoolTimeout:  4 * time.Second,  // Pool timeout
		IdleTimeout:  5 * time.Minute,  // Idle connection timeout
	})

	return &RedisClient{
		client: rdb,
	}
}

// Ping tests Redis connectivity
func (r *RedisClient) Ping(ctx context.Context) error {
	return r.client.Ping(ctx).Err()
}

// GetListingsCache retrieves cached listings response
func (r *RedisClient) GetListingsCache(ctx context.Context, key string) (*api.ListingsResponse, error) {
	val, err := r.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, nil // Cache miss
		}
		return nil, fmt.Errorf("redis get error: %w", err)
	}

	var response api.ListingsResponse
	if err := json.Unmarshal([]byte(val), &response); err != nil {
		return nil, fmt.Errorf("json unmarshal error: %w", err)
	}

	return &response, nil
}

// SetListingsCache stores listings response in cache
func (r *RedisClient) SetListingsCache(ctx context.Context, key string, response *api.ListingsResponse, ttl time.Duration) error {
	data, err := json.Marshal(response)
	if err != nil {
		return fmt.Errorf("json marshal error: %w", err)
	}

	return r.client.Set(ctx, key, data, ttl).Err()
}

// GetListingCache retrieves cached individual listing
func (r *RedisClient) GetListingCache(ctx context.Context, listingID string) (*api.TradeListing, error) {
	key := fmt.Sprintf("listing:%s", listingID)
	val, err := r.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, nil // Cache miss
		}
		return nil, fmt.Errorf("redis get error: %w", err)
	}

	var listing api.TradeListing
	if err := json.Unmarshal([]byte(val), &listing); err != nil {
		return nil, fmt.Errorf("json unmarshal error: %w", err)
	}

	return &listing, nil
}

// SetListingCache stores individual listing in cache
func (r *RedisClient) SetListingCache(ctx context.Context, listing *api.TradeListing, ttl time.Duration) error {
	key := fmt.Sprintf("listing:%s", listing.ID)
	data, err := json.Marshal(listing)
	if err != nil {
		return fmt.Errorf("json marshal error: %w", err)
	}

	return r.client.Set(ctx, key, data, ttl).Err()
}

// Delete removes keys from cache
func (r *RedisClient) Delete(ctx context.Context, keys ...string) error {
	return r.client.Del(ctx, keys...).Err()
}

// PublishTradeEvent publishes trade events for real-time updates
func (r *RedisClient) PublishTradeEvent(ctx context.Context, event *TradeEvent) error {
	// BACKEND NOTE: Pub/Sub for real-time trading updates
	data, err := json.Marshal(event)
	if err != nil {
		return fmt.Errorf("json marshal error: %w", err)
	}

	return r.client.Publish(ctx, "trading:events", data).Err()
}

// SubscribeToTradeEvents subscribes to trade events
func (r *RedisClient) SubscribeToTradeEvents(ctx context.Context) *redis.PubSub {
	// BACKEND NOTE: Subscribe to real-time trading events
	return r.client.Subscribe(ctx, "trading:events")
}

// TradeEvent represents a trading event for pub/sub
type TradeEvent struct {
	Type       string    `json:"type"`
	ListingID  string    `json:"listing_id,omitempty"`
	TradeID    string    `json:"trade_id,omitempty"`
	SellerID   string    `json:"seller_id,omitempty"`
	BuyerID    string    `json:"buyer_id,omitempty"`
	ItemID     string    `json:"item_id,omitempty"`
	Quantity   int32     `json:"quantity,omitempty"`
	Price      int64     `json:"price,omitempty"`
	Timestamp  time.Time `json:"timestamp"`
}
