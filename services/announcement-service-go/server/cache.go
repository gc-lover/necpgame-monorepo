// Redis-based caching for Announcement Service
// Issue: #323
// PERFORMANCE: In-memory caching with TTL for high-throughput announcement lookups

package server

import (
	"context"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/announcement-service-go/pkg/api"
	"go.uber.org/zap"
)

// Cache handles caching operations for Announcement data
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
		now := time.Now()
		for key, expiry := range c.ttl {
			if now.After(expiry) {
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

// GetAnnouncement retrieves an announcement from cache
func (c *Cache) GetAnnouncement(ctx context.Context, id string) (*api.Announcement, bool) {
	if value, found := c.Get(ctx, "announcement:"+id); found {
		if ann, ok := value.(*api.Announcement); ok {
			return ann, true
		}
	}
	return nil, false
}

// SetAnnouncement stores an announcement in cache
func (c *Cache) SetAnnouncement(ctx context.Context, id string, announcement *api.Announcement, ttl time.Duration) {
	c.Set(ctx, "announcement:"+id, announcement, ttl)
}

// GetAnnouncementsList retrieves announcements list from cache
func (c *Cache) GetAnnouncementsList(ctx context.Context, key string) ([]*api.Announcement, bool) {
	if value, found := c.Get(ctx, "announcements:"+key); found {
		if list, ok := value.([]*api.Announcement); ok {
			return list, true
		}
	}
	return nil, false
}

// SetAnnouncementsList stores announcements list in cache
func (c *Cache) SetAnnouncementsList(ctx context.Context, key string, announcements []*api.Announcement, ttl time.Duration) {
	c.Set(ctx, "announcements:"+key, announcements, ttl)
}




