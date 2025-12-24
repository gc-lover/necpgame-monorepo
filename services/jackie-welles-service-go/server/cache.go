// Redis-based caching for Jackie Welles NPC service
// Issue: #1905
// PERFORMANCE: In-memory caching with TTL for high-throughput NPC interactions

package server

import (
	"context"
	"encoding/json"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/jackie-welles-service-go/pkg/api"
)

// Cache handles caching operations for Jackie Welles data
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

// GetProfile retrieves Jackie profile from cache
func (c *Cache) GetProfile() (*api.JackieProfileResponse, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	if val, exists := c.data["jackie_profile"]; exists {
		if profile, ok := val.(*api.JackieProfileResponse); ok {
			return profile, true
		}
	}
	return nil, false
}

// SetProfile stores Jackie profile in cache
func (c *Cache) SetProfile(profile *api.JackieProfileResponse) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	c.data["jackie_profile"] = profile
	c.ttl["jackie_profile"] = time.Now().Add(30 * time.Minute) // Cache for 30 minutes
}

// GetRelationship retrieves relationship data from cache
func (c *Cache) GetRelationship(key string) (*api.JackieRelationshipResponse, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	if val, exists := c.data[key]; exists {
		if rel, ok := val.(*api.JackieRelationshipResponse); ok {
			return rel, true
		}
	}
	return nil, false
}

// SetRelationship stores relationship data in cache
func (c *Cache) SetRelationship(key string, rel *api.JackieRelationshipResponse) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	c.data[key] = rel
	c.ttl[key] = time.Now().Add(10 * time.Minute) // Shorter TTL for relationship data
}

// GetStatus retrieves Jackie status from cache
func (c *Cache) GetStatus() (*api.JackieStatusResponse, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	if val, exists := c.data["jackie_status"]; exists {
		if status, ok := val.(*api.JackieStatusResponse); ok {
			return status, true
		}
	}
	return nil, false
}

// SetStatus stores Jackie status in cache
func (c *Cache) SetStatus(status *api.JackieStatusResponse) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	c.data["jackie_status"] = status
	c.ttl["jackie_status"] = time.Now().Add(2 * time.Minute) // Very short TTL for status
}

// GetQuest retrieves quest data from cache
func (c *Cache) GetQuest(questID string) (*api.JackieQuest, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	key := "quest:" + questID
	if val, exists := c.data[key]; exists {
		if quest, ok := val.(*api.JackieQuest); ok {
			return quest, true
		}
	}
	return nil, false
}

// SetQuest stores quest data in cache
func (c *Cache) SetQuest(questID string, quest *api.JackieQuest) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	key := "quest:" + questID
	c.data[key] = quest
	c.ttl[key] = time.Now().Add(15 * time.Minute)
}

// InvalidateQuestCache removes quest from cache
func (c *Cache) InvalidateQuestCache(questID string) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	key := "quest:" + questID
	delete(c.data, key)
	delete(c.ttl, key)
}

// InvalidateRelationshipCache removes relationship data from cache
func (c *Cache) InvalidateRelationshipCache(key string) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	delete(c.data, key)
	delete(c.ttl, key)
}

// GetInventory retrieves inventory from cache
func (c *Cache) GetInventory() ([]api.JackieInventoryItem, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	if val, exists := c.data["jackie_inventory"]; exists {
		if items, ok := val.([]api.JackieInventoryItem); ok {
			return items, true
		}
	}
	return nil, false
}

// SetInventory stores inventory in cache
func (c *Cache) SetInventory(items []api.JackieInventoryItem) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	c.data["jackie_inventory"] = items
	c.ttl["jackie_inventory"] = time.Now().Add(5 * time.Minute)
}

// GetDialogue retrieves active dialogue from cache
func (c *Cache) GetDialogue(dialogueID string) (*api.StartJackieDialogueOK, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	key := "dialogue:" + dialogueID
	if val, exists := c.data[key]; exists {
		if dialogue, ok := val.(*api.StartJackieDialogueOK); ok {
			return dialogue, true
		}
	}
	return nil, false
}

// SetDialogue stores active dialogue in cache
func (c *Cache) SetDialogue(dialogueID string, dialogue *api.StartJackieDialogueOK) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	key := "dialogue:" + dialogueID
	c.data[key] = dialogue
	c.ttl[key] = time.Now().Add(30 * time.Minute) // Dialogues can be long-lived
}

// InvalidateDialogueCache removes dialogue from cache
func (c *Cache) InvalidateDialogueCache(dialogueID string) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	key := "dialogue:" + dialogueID
	delete(c.data, key)
	delete(c.ttl, key)
}

// GetJSON retrieves any cached data as JSON bytes
func (c *Cache) GetJSON(key string) ([]byte, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	if val, exists := c.data[key]; exists {
		if jsonData, err := json.Marshal(val); err == nil {
			return jsonData, true
		}
	}
	return nil, false
}

// SetJSON stores JSON data in cache
func (c *Cache) SetJSON(key string, data []byte, ttl time.Duration) error {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	var value interface{}
	if err := json.Unmarshal(data, &value); err != nil {
		return err
	}

	c.data[key] = value
	c.ttl[key] = time.Now().Add(ttl)
	return nil
}

// ClearAll removes all cache entries
func (c *Cache) ClearAll() {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	c.data = make(map[string]interface{})
	c.ttl = make(map[string]time.Time)
}

// Stats returns cache statistics
func (c *Cache) Stats() map[string]int {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	return map[string]int{
		"entries": len(c.data),
		"ttl_entries": len(c.ttl),
	}
}
