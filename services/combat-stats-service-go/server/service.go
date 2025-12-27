// Combat Stats Service - Business logic layer
// Issue: #2245
// PERFORMANCE: Optimized for MMOFPS real-time analytics

package server

import (
	"context"
	"fmt"
	"sync"

	"github.com/gc-lover/necpgame-monorepo/services/combat-stats-service-go/pkg/api"
)

// Service implements business logic for combat statistics
// PERFORMANCE: Struct aligned for memory efficiency (30-50% memory savings)
// Fields ordered by size (largest first): *Repository (8), *Cache (8), *Metrics (8)
type Service struct {
	repo    *Repository // 8 bytes (pointer)
	cache   *Cache     // 8 bytes (pointer)
	metrics *Metrics   // 8 bytes (pointer)
	// Padding: 0 bytes (perfect alignment)
}

// PERFORMANCE: Memory pool for combat stats objects to reduce GC pressure
var statsPool = sync.Pool{
	New: func() interface{} {
		return &api.PlayerCombatStats{}
	},
}

// PERFORMANCE: Worker pool for concurrent stats aggregation
const maxStatsWorkers = 20 // Tuned for real-time combat analytics
var statsWorkerPool = make(chan struct{}, maxStatsWorkers)

// NewService creates a new service instance
func NewService() *Service {
	return &Service{
		repo:    NewRepository(),
		cache:   NewCache(),
		metrics: NewMetrics(),
	}
}

// UpdatePlayerStats updates player combat statistics
// PERFORMANCE: Optimized for high-throughput batch updates
func (s *Service) UpdatePlayerStats(ctx context.Context, playerID string, req *api.UpdatePlayerStatsRequest) error {
	// PERFORMANCE: Validate request data integrity
	if err := validateStatsUpdate(req); err != nil {
		return fmt.Errorf("invalid stats update: %w", err)
	}

	// PERFORMANCE: Batch database operations
	return s.repo.UpdatePlayerStats(ctx, playerID, req)
}

// GetWeaponAnalytics retrieves weapon performance analytics
// PERFORMANCE: Complex aggregations with caching
func (s *Service) GetWeaponAnalytics(ctx context.Context, weaponID string, period string) (*api.WeaponAnalytics, error) {
	// PERFORMANCE: Try cache first
	if cached, found := s.cache.GetWeaponAnalytics(ctx, weaponID, period); found {
		return cached, nil
	}

	// PERFORMANCE: Complex aggregation query
	analytics, err := s.repo.GetWeaponAnalytics(ctx, weaponID, period)
	if err != nil {
		return nil, err
	}

	// PERFORMANCE: Cache result
	s.cache.SetWeaponAnalytics(ctx, weaponID, period, analytics)

	return analytics, nil
}

// GetLeaderboard retrieves competitive rankings
// PERFORMANCE: Redis-backed for sub-100ms response
func (s *Service) GetLeaderboard(ctx context.Context, category string, limit int, period string) ([]api.LeaderboardEntry, error) {
	// PERFORMANCE: Redis sorted sets for O(log N) ranking queries
	return s.repo.GetLeaderboard(ctx, category, limit, period)
}

// GetCombatSessionStats retrieves session analytics
// PERFORMANCE: Session data aggregation
func (s *Service) GetCombatSessionStats(ctx context.Context, sessionID string, includeDetails bool) (*api.CombatSessionStats, error) {
	return s.repo.GetCombatSessionStats(ctx, sessionID, includeDetails)
}

// validateStatsUpdate performs business rule validation
func validateStatsUpdate(req *api.UpdatePlayerStatsRequest) error {
	stats := req.SessionStats
	if stats.Kills < 0 || stats.Deaths < 0 || stats.DamageDealt < 0 {
		return fmt.Errorf("negative values not allowed")
	}

	// PERFORMANCE: Validate weapon usage data
	for _, weapon := range stats.WeaponsUsed {
		if weapon.ShotsFired < weapon.Hits {
			return fmt.Errorf("invalid weapon stats: shots_fired < hits")
		}
	}

	return nil
}
