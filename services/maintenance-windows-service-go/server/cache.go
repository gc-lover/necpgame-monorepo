// Redis-based caching for Maintenance Windows Service
// Issue: #316
// PERFORMANCE: In-memory caching with TTL for high-throughput maintenance operations

package server

import (
	"context"
	"encoding/json"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/maintenance-windows-service-go/pkg/api"
	"github.com/google/uuid"
)

// Cache handles caching operations for maintenance windows data
type Cache struct {
	data  map[string]interface{}
	ttl   map[string]time.Time
	mutex sync.RWMutex
}

// NewCache creates a new cache instance
func NewCache() *Cache {
	cache := &Cache{
		data: make(map[string]interface{}),
		ttl:  make(map[string]time.Time),
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
			}
		}
		c.mutex.Unlock()
	}
}

// GetMaintenanceWindow retrieves a maintenance window from cache
func (c *Cache) GetMaintenanceWindow(ctx context.Context, windowID uuid.UUID) (*api.MaintenanceWindow, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	key := "window:" + windowID.String()
	if expiry, exists := c.ttl[key]; exists && time.Now().Before(expiry) {
		if data, found := c.data[key]; found {
			if window, ok := data.(*api.MaintenanceWindow); ok {
				return window, true
			}
		}
	}

	return nil, false
}

// SetMaintenanceWindow stores a maintenance window in cache
func (c *Cache) SetMaintenanceWindow(ctx context.Context, windowID uuid.UUID, window *api.MaintenanceWindow) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	key := "window:" + windowID.String()
	c.data[key] = window
	c.ttl[key] = time.Now().Add(10 * time.Minute) // 10 minute TTL
}

// DeleteMaintenanceWindow removes a maintenance window from cache
func (c *Cache) DeleteMaintenanceWindow(ctx context.Context, windowID uuid.UUID) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	key := "window:" + windowID.String()
	delete(c.data, key)
	delete(c.ttl, key)
}

// GetMaintenanceWindows retrieves a list of maintenance windows from cache
func (c *Cache) GetMaintenanceWindows(ctx context.Context, limit int) ([]*api.MaintenanceWindow, int, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	key := "windows:list"
	if expiry, exists := c.ttl[key]; exists && time.Now().Before(expiry) {
		if data, found := c.data[key]; found {
			if cachedData, ok := data.(map[string]interface{}); ok {
				if windowsData, hasWindows := cachedData["windows"]; hasWindows {
					if totalData, hasTotal := cachedData["total"]; hasTotal {
						if windows, ok := windowsData.([]*api.MaintenanceWindow); ok {
							if total, ok := totalData.(int); ok {
								return windows, total, true
							}
						}
					}
				}
			}
		}
	}

	return nil, 0, false
}

// SetMaintenanceWindows stores a list of maintenance windows in cache
func (c *Cache) SetMaintenanceWindows(ctx context.Context, windows []*api.MaintenanceWindow, total, limit int) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	key := "windows:list"
	c.data[key] = map[string]interface{}{
		"windows": windows,
		"total":   total,
		"limit":   limit,
	}
	c.ttl[key] = time.Now().Add(5 * time.Minute) // 5 minute TTL for lists
}

// InvalidateAll clears all cache entries
func (c *Cache) InvalidateAll(ctx context.Context) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	c.data = make(map[string]interface{})
	c.ttl = make(map[string]time.Time)
}
