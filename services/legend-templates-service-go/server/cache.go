// Issue: #2241
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/legend-templates-service-go/pkg/api"
)

// TemplateCache provides fast access to active templates
type TemplateCache struct {
	mu        sync.RWMutex
	templates map[string][]api.ActiveTemplate // Keyed by event type
	lastUpdate time.Time
	ttl       time.Duration
	repo      *LegendRepository
}

// NewTemplateCache creates a new template cache
func NewTemplateCache() *TemplateCache {
	return &TemplateCache{
		templates: make(map[string][]api.ActiveTemplate),
		ttl:       10 * time.Minute, // Cache templates for 10 minutes
	}
}

// SetRepository sets the repository for cache refresh
func (c *TemplateCache) SetRepository(repo *LegendRepository) {
	c.repo = repo
}

// GetActiveTemplates retrieves active templates for an event type
func (c *TemplateCache) GetActiveTemplates(ctx context.Context, eventType string) ([]api.ActiveTemplate, error) {
	// BACKEND NOTE: HOT PATH cache access (<100μs target)
	c.mu.RLock()
	if c.isCacheValid() {
		templates := c.templates[eventType]
		c.mu.RUnlock()
		return templates, nil
	}
	c.mu.RUnlock()

	// Cache expired, refresh
	return c.refreshCache(ctx, eventType)
}

// isCacheValid checks if cache is still valid
func (c *TemplateCache) isCacheValid() bool {
	return time.Since(c.lastUpdate) < c.ttl
}

// refreshCache refreshes the cache from database
func (c *TemplateCache) refreshCache(ctx context.Context, requestedType string) ([]api.ActiveTemplate, error) {
	c.mu.Lock()
	defer c.mu.Unlock()

	// Check again under lock
	if c.isCacheValid() {
		return c.templates[requestedType], nil
	}

	// Refresh all active templates
	eventTypes := []string{"combat", "social", "economic", "exploration"}

	for _, eventType := range eventTypes {
		templates, err := c.repo.GetActiveTemplatesForCache(ctx, eventType)
		if err != nil {
			return nil, fmt.Errorf("failed to refresh cache for %s: %w", eventType, err)
		}
		c.templates[eventType] = templates
	}

	c.lastUpdate = time.Now()

	return c.templates[requestedType], nil
}

// InvalidateCache forces cache refresh
func (c *TemplateCache) InvalidateCache() {
	c.mu.Lock()
	defer c.mu.Unlock()

	c.templates = make(map[string][]api.ActiveTemplate)
	c.lastUpdate = time.Time{}
}

// GetCacheStats returns cache statistics
func (c *TemplateCache) GetCacheStats() map[string]interface{} {
	c.mu.RLock()
	defer c.mu.RUnlock()

	stats := make(map[string]interface{})
	stats["last_update"] = c.lastUpdate
	stats["ttl_seconds"] = c.ttl.Seconds()
	stats["is_valid"] = c.isCacheValid()

	templateCounts := make(map[string]int)
	for eventType, templates := range c.templates {
		templateCounts[eventType] = len(templates)
	}
	stats["template_counts"] = templateCounts

	return stats
}

// RedisClient provides Redis operations for templates
type RedisClient struct {
	// Mock Redis client - in production would use go-redis
	cache map[string]string
	mu    sync.RWMutex
}

// NewRedisClient creates a new Redis client
func NewRedisClient(addr string) *RedisClient {
	return &RedisClient{
		cache: make(map[string]string),
	}
}

// Ping tests Redis connectivity
func (r *RedisClient) Ping(ctx context.Context) error {
	// Mock ping - always successful
	return nil
}

// Get retrieves a value from Redis
func (r *RedisClient) Get(ctx context.Context, key string) (string, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	if val, exists := r.cache[key]; exists {
		return val, nil
	}
	return "", fmt.Errorf("key not found")
}

// Set sets a value in Redis
func (r *RedisClient) Set(ctx context.Context, key, value string, ttl time.Duration) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	r.cache[key] = value
	return nil
}

// MarshalJSON marshals data to JSON for Redis storage
func (r *RedisClient) MarshalJSON(data interface{}) (string, error) {
	bytes, err := json.Marshal(data)
	if err != nil {
		return "", err
	}
	return string(bytes), nil
}

// UnmarshalJSON unmarshals data from JSON for Redis retrieval
func (r *RedisClient) UnmarshalJSON(data string, v interface{}) error {
	return json.Unmarshal([]byte(data), v)
}
