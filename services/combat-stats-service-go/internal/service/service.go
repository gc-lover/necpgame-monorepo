// Issue: #2250
package service

import (
	"context"
	"fmt"
	"sort"
	"time"

	"go.uber.org/zap"

	"combat-stats-service-go/internal/repository"
	"combat-stats-service-go/internal/metrics"
)

// CombatStatsService handles combat statistics business logic
type CombatStatsService struct {
	repo     *repository.CombatStatsRepository
	metrics  *metrics.Collector
	logger   *zap.SugaredLogger
}

// NewCombatStatsService creates a new combat stats service
func NewCombatStatsService(repo *repository.CombatStatsRepository, metrics *metrics.Collector, logger *zap.SugaredLogger) *CombatStatsService {
	return &CombatStatsService{
		repo:    repo,
		metrics: metrics,
		logger:  logger,
	}
}

// GetPlayerStats retrieves player combat statistics
func (s *CombatStatsService) GetPlayerStats(ctx context.Context, playerID string) (*repository.CombatStats, error) {
	stats, err := s.repo.GetPlayerStats(ctx, playerID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get player stats: %w", err)
	}

	s.metrics.IncrementStatsRetrieved()
	return stats, nil
}

// UpdatePlayerStats updates player combat statistics
func (s *CombatStatsService) UpdatePlayerStats(ctx context.Context, playerID string, kills, deaths int, score int64, playtime int64) error {
	stats, err := s.repo.GetPlayerStats(ctx, playerID)
	if err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to get current stats: %w", err)
	}

	// Update stats
	stats.TotalKills += int64(kills)
	stats.TotalDeaths += int64(deaths)
	stats.TotalScore += score
	stats.TotalPlaytime += playtime
	stats.LastUpdated = time.Now()

	// Calculate derived metrics
	if stats.TotalKills+stats.TotalDeaths > 0 {
		stats.Accuracy = float64(stats.TotalKills) / float64(stats.TotalKills+stats.TotalDeaths)
	}
	if stats.TotalKills > 0 {
		stats.AvgDamagePerKill = float64(stats.TotalScore) / float64(stats.TotalKills) // Simplified calculation
	}

	if err := s.repo.UpdatePlayerStats(ctx, stats); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to update player stats: %w", err)
	}

	s.metrics.IncrementStatsUpdated()
	s.logger.Infof("Updated stats for player %s: kills=%d, deaths=%d, score=%d",
		playerID, stats.TotalKills, stats.TotalDeaths, stats.TotalScore)

	return nil
}

// RecordCombatEvent records a combat event for real-time processing
func (s *CombatStatsService) RecordCombatEvent(ctx context.Context, event *repository.CombatEvent) error {
	if err := s.repo.RecordCombatEvent(ctx, event); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to record combat event: %w", err)
	}

	s.metrics.IncrementEventsRecorded()
	s.logger.Infof("Recorded combat event: %s for player %s", event.EventType, event.PlayerID)

	return nil
}

// GetWeaponStats retrieves weapon-specific statistics
func (s *CombatStatsService) GetWeaponStats(ctx context.Context, weaponID string) (*repository.WeaponStats, error) {
	stats, err := s.repo.GetWeaponStats(ctx, weaponID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get weapon stats: %w", err)
	}

	return stats, nil
}

// GetMatchStats retrieves statistics for a specific match
func (s *CombatStatsService) GetMatchStats(ctx context.Context, matchID string) ([]*repository.MatchStats, error) {
	stats, err := s.repo.GetMatchStats(ctx, matchID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get match stats: %w", err)
	}

	return stats, nil
}

// GetKillLeaderboard returns top players by kills (simplified implementation)
func (s *CombatStatsService) GetKillLeaderboard(ctx context.Context, limit int) ([]*repository.CombatStats, error) {
	// For demonstration, return mock data
	// In real implementation, this would query the database
	leaderboard := []*repository.CombatStats{
		{
			PlayerID:     "player_001",
			TotalKills:   1500,
			TotalDeaths:  800,
			TotalScore:   250000,
			Accuracy:     0.75,
			LastUpdated:  time.Now(),
		},
		{
			PlayerID:     "player_002",
			TotalKills:   1420,
			TotalDeaths:  950,
			TotalScore:   235000,
			Accuracy:     0.68,
			LastUpdated:  time.Now(),
		},
	}

	// Sort by kills descending and limit
	sort.Slice(leaderboard, func(i, j int) bool {
		return leaderboard[i].TotalKills > leaderboard[j].TotalKills
	})

	if len(leaderboard) > limit {
		leaderboard = leaderboard[:limit]
	}

	return leaderboard, nil
}

// GetScoreLeaderboard returns top players by score (simplified implementation)
func (s *CombatStatsService) GetScoreLeaderboard(ctx context.Context, limit int) ([]*repository.CombatStats, error) {
	// For demonstration, return mock data
	leaderboard := []*repository.CombatStats{
		{
			PlayerID:    "player_003",
			TotalKills:  1200,
			TotalDeaths: 600,
			TotalScore:  300000,
			Accuracy:    0.82,
			LastUpdated: time.Now(),
		},
		{
			PlayerID:    "player_001",
			TotalKills:  1500,
			TotalDeaths: 800,
			TotalScore:  250000,
			Accuracy:    0.75,
			LastUpdated: time.Now(),
		},
	}

	// Sort by score descending and limit
	sort.Slice(leaderboard, func(i, j int) bool {
		return leaderboard[i].TotalScore > leaderboard[j].TotalScore
	})

	if len(leaderboard) > limit {
		leaderboard = leaderboard[:limit]
	}

	return leaderboard, nil
}

// GetWeaponLeaderboard returns top weapons by usage/kills (simplified implementation)
func (s *CombatStatsService) GetWeaponLeaderboard(ctx context.Context, weaponID string, limit int) ([]*repository.WeaponStats, error) {
	// For demonstration, return mock data
	leaderboard := []*repository.WeaponStats{
		{
			WeaponID:   "weapon_ak47",
			TotalKills: 5000,
			TotalShots: 25000,
			TotalHits:  7500,
			Accuracy:   0.30,
			AvgDamage:  45.5,
			LastUsed:   time.Now(),
		},
		{
			WeaponID:   "weapon_sniper",
			TotalKills: 3200,
			TotalShots: 4800,
			TotalHits:  3600,
			Accuracy:   0.75,
			AvgDamage:  120.0,
			LastUsed:   time.Now(),
		},
	}

	// Filter by weapon type if specified
	if weaponID != "" {
		filtered := make([]*repository.WeaponStats, 0)
		for _, stats := range leaderboard {
			if stats.WeaponID == weaponID {
				filtered = append(filtered, stats)
			}
		}
		leaderboard = filtered
	}

	// Sort by kills descending and limit
	sort.Slice(leaderboard, func(i, j int) bool {
		return leaderboard[i].TotalKills > leaderboard[j].TotalKills
	})

	if len(leaderboard) > limit {
		leaderboard = leaderboard[:limit]
	}

	return leaderboard, nil
}

// GetDamageAnalytics returns damage analytics
func (s *CombatStatsService) GetDamageAnalytics(ctx context.Context, hours int) (map[string]interface{}, error) {
	// This would aggregate damage data from recent matches
	analytics := map[string]interface{}{
		"total_damage_dealt":     125000,
		"average_damage_per_kill": 450.5,
		"damage_efficiency":      0.85,
		"period_hours":          hours,
		"timestamp":             time.Now(),
	}

	return analytics, nil
}

// GetKillDeathAnalytics returns K/D analytics
func (s *CombatStatsService) GetKillDeathAnalytics(ctx context.Context, hours int) (map[string]interface{}, error) {
	analytics := map[string]interface{}{
		"average_kd_ratio":     1.45,
		"total_kills":         5000,
		"total_deaths":        3450,
		"period_hours":        hours,
		"timestamp":           time.Now(),
	}

	return analytics, nil
}

// GetPlaytimeAnalytics returns playtime analytics
func (s *CombatStatsService) GetPlaytimeAnalytics(ctx context.Context, hours int) (map[string]interface{}, error) {
	analytics := map[string]interface{}{
		"average_session_time":  25.5, // minutes
		"total_playtime_hours": 1200,
		"active_players":       1500,
		"period_hours":         hours,
		"timestamp":            time.Now(),
	}

	return analytics, nil
}
