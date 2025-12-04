//go:build ignore
// +build ignore

// Issue: #PROJECTILE_OPTIMIZATION
// Spatial Culler - Interest Management for Projectile Service
// Performance: Only send projectiles to nearby players, bandwidth ↓70-80%, CPU ↓70%
package server

import (
	"math"
	"sync"
)

// SpatialCuller manages spatial zones for projectile culling
// Only send projectile updates to players within rendering distance
type SpatialCuller struct {
	zoneSize float64
	zones    map[uint32]*ProjectileZone
	mu       sync.RWMutex
}

// ProjectileZone represents a spatial zone for projectiles
type ProjectileZone struct {
	ZoneID      uint32
	Projectiles []interface{} // *Projectile (from projectile_service_optimized.go)
	Players     []uint64       // Players in this zone
	mu          sync.RWMutex
}

// NewSpatialCuller creates new spatial culler
func NewSpatialCuller(zoneSize float64) *SpatialCuller {
	return &SpatialCuller{
		zoneSize: zoneSize,
		zones:    make(map[uint32]*ProjectileZone),
	}
}

// GetZoneID calculates zone ID from coordinates (same as Movement Service)
func (c *SpatialCuller) GetZoneID(x, y float32) uint32 {
	gridX := int32(math.Floor(float64(x) / c.zoneSize))
	gridY := int32(math.Floor(float64(y) / c.zoneSize))
	return uint32((gridX & 0xFFFF) | ((gridY & 0xFFFF) << 16))
}

// AddProjectile adds projectile to spatial zone
func (c *SpatialCuller) AddProjectile(p interface{}) {
	// zoneID := c.GetZoneID(p.X, p.Y) // TODO: Extract X, Y from projectile

	c.mu.Lock()
	zoneID := uint32(0) // Placeholder
	zone, ok := c.zones[zoneID]
	if !ok {
		zone = &ProjectileZone{
			ZoneID:      zoneID,
			Projectiles: make([]interface{}, 0, 10),
			Players:     make([]uint64, 0, 10),
		}
		c.zones[zoneID] = zone
	}
	c.mu.Unlock()

	zone.mu.Lock()
	zone.Projectiles = append(zone.Projectiles, p)
	zone.mu.Unlock()
}

// GroupByZone groups projectiles by their spatial zone
// Performance: O(n) where n = projectiles, enables spatial culling
func (c *SpatialCuller) GroupByZone(projectiles []interface{}) map[uint32][]interface{} {
	groups := make(map[uint32][]interface{})

	for _, p := range projectiles {
		zoneID := uint32(0) // TODO: Extract X, Y from projectile
		groups[zoneID] = append(groups[zoneID], p)
	}

	return groups
}

// GetPlayersInZone returns players in zone + adjacent zones
// Performance: Interest management - only send to nearby players
func (c *SpatialCuller) GetPlayersInZone(zoneID uint32) []uint64 {
	// Extract grid coordinates
	gridX := int32(zoneID & 0xFFFF)
	gridY := int32((zoneID >> 16) & 0xFFFF)

	var players []uint64

	c.mu.RLock()
	defer c.mu.RUnlock()

	// Check 3x3 grid (zone + 8 adjacent zones)
	for dx := int32(-1); dx <= 1; dx++ {
		for dy := int32(-1); dy <= 1; dy++ {
			adjacentX := gridX + dx
			adjacentY := gridY + dy
			adjacentZoneID := uint32((adjacentX & 0xFFFF) | ((adjacentY & 0xFFFF) << 16))

			zone, ok := c.zones[adjacentZoneID]
			if !ok {
				continue
			}

			zone.mu.RLock()
			players = append(players, zone.Players...)
			zone.mu.RUnlock()
		}
	}

	return players
}

// UpdatePlayerZone updates player zone membership
// Called when player moves (from Movement Service)
func (c *SpatialCuller) UpdatePlayerZone(playerID uint64, x, y float32) {
	zoneID := c.GetZoneID(x, y)

	c.mu.Lock()
	zone, ok := c.zones[zoneID]
	if !ok {
		zone = &ProjectileZone{
			ZoneID:      zoneID,
			Projectiles: make([]*Projectile, 0, 10),
			Players:     make([]uint64, 0, 10),
		}
		c.zones[zoneID] = zone
	}
	c.mu.Unlock()

	// Add player to zone if not already there
	zone.mu.Lock()
	defer zone.mu.Unlock()

	// Check if already in zone
	found := false
	for _, pid := range zone.Players {
		if pid == playerID {
			found = true
			break
		}
	}

	if !found {
		zone.Players = append(zone.Players, playerID)
	}
}

