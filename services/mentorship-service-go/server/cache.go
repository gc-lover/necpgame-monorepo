// Redis-based caching for Mentorship Service
// Issue: #140890865
// PERFORMANCE: In-memory caching with TTL for high-throughput status checks

package server

import (
	"context"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/mentorship-service-go/pkg/api"
	"go.uber.org/zap"
)

// Cache handles caching operations for Mentorship data
type Cache struct {
	data  map[string]interface{}
	ttl   map[string]time.Time
	mutex sync.RWMutex
	logger *zap.Logger
}

// NewCache creates a new cache instance
func NewCache(logger *zap.Logger) *Cache {
	cache := &Cache{
		data: make(map[string]interface{}),
		ttl:  make(map[string]time.Time),
		logger: logger,
	}

	// PERFORMANCE: Start cleanup goroutine for expired entries
	go cache.cleanup()

	return cache
}

// cleanup removes expired cache entries
func (c *Cache) cleanup() {
	ticker := time.NewTicker(5 * time.Minute)
	defer ticker.Stop()

	for range ticker.C {
		c.mutex.Lock()
		for key, expiry := range c.ttl {
			if time.Now().After(expiry) {
				delete(c.data, key)
				delete(c.ttl, key)
				c.logger.Debug("Cache entry expired and removed", zap.String("key", key))
			}
		}
		c.mutex.Unlock()
	}
}

// Set stores an item in the cache with a given TTL
func (c *Cache) Set(ctx context.Context, key string, value interface{}, ttl time.Duration) {
	c.mutex.Lock()
	defer c.mutex.Unlock()
	c.data[key] = value
	c.ttl[key] = time.Now().Add(ttl)
	c.logger.Debug("Cache set", zap.String("key", key), zap.Duration("ttl", ttl))
}

// Get retrieves an item from the cache
func (c *Cache) Get(ctx context.Context, key string) (interface{}, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	if value, found := c.data[key]; found {
		if expiry, ok := c.ttl[key]; ok && time.Now().Before(expiry) {
			c.logger.Debug("Cache hit", zap.String("key", key))
			return value, true
		}
		// Expired, will be cleaned up by goroutine
		c.logger.Debug("Cache entry found but expired", zap.String("key", key))
	}
	c.logger.Debug("Cache miss", zap.String("key", key))
	return nil, false
}

// Delete removes an item from the cache
func (c *Cache) Delete(ctx context.Context, key string) {
	c.mutex.Lock()
	defer c.mutex.Unlock()
	delete(c.data, key)
	delete(c.ttl, key)
	c.logger.Debug("Cache deleted", zap.String("key", key))
}



