package service

import (
	"context"
	"fmt"
	"time"

	"combat-implants-stats-service-go/internal/repository"
)

// Service handles business logic for combat implants stats
type Service struct {
	repo *repository.Repository
}

// NewService creates a new service instance
func NewService(repo *repository.Repository) *Service {
	return &Service{repo: repo}
}

// ImplantUsageRequest represents a request to record implant usage
type ImplantUsageRequest struct {
	ImplantID   string  `json:"implant_id"`
	PlayerID    string  `json:"player_id"`
	Success     bool    `json:"success"`
	Duration    float64 `json:"duration"` // in seconds
}

// RecordImplantUsage records implant usage and updates statistics
func (s *Service) RecordImplantUsage(ctx context.Context, req *ImplantUsageRequest) error {
	// Get current stats
	stats, err := s.repo.GetImplantStats(ctx, req.ImplantID)
	if err != nil {
		// If no stats exist, create new ones
		stats = &repository.ImplantStats{
			ImplantID:   req.ImplantID,
			PlayerID:    req.PlayerID,
			UsageCount:  0,
			SuccessRate: 0,
			AvgDuration: 0,
			LastUsed:    time.Now(),
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		}
	}

	// Update statistics
	stats.UsageCount++
	if req.Success {
		// Calculate new success rate using rolling average
		stats.SuccessRate = (stats.SuccessRate*float64(stats.UsageCount-1) + 1) / float64(stats.UsageCount)
	} else {
		// Failed usage
		stats.SuccessRate = (stats.SuccessRate * float64(stats.UsageCount-1)) / float64(stats.UsageCount)
	}

	// Update average duration
	if stats.UsageCount == 1 {
		stats.AvgDuration = req.Duration
	} else {
		stats.AvgDuration = (stats.AvgDuration*float64(stats.UsageCount-1) + req.Duration) / float64(stats.UsageCount)
	}

	stats.LastUsed = time.Now()
	stats.UpdatedAt = time.Now()

	return s.repo.UpdateImplantStats(ctx, stats)
}

// GetImplantPerformance retrieves performance statistics for an implant
func (s *Service) GetImplantPerformance(ctx context.Context, implantID string) (*repository.ImplantStats, error) {
	return s.repo.GetImplantStats(ctx, implantID)
}

// GetPlayerImplantAnalytics retrieves analytics for player's implant usage
func (s *Service) GetPlayerImplantAnalytics(ctx context.Context, playerID string) ([]*repository.ImplantStats, error) {
	return s.repo.GetPlayerImplantAnalytics(ctx, playerID)
}

// CalculateImplantEfficiency calculates efficiency metrics for an implant
func (s *Service) CalculateImplantEfficiency(ctx context.Context, implantID string) (map[string]interface{}, error) {
	stats, err := s.repo.GetImplantStats(ctx, implantID)
	if err != nil {
		return nil, fmt.Errorf("failed to get implant stats: %w", err)
	}

	efficiency := map[string]interface{}{
		"implant_id":       stats.ImplantID,
		"total_usage":      stats.UsageCount,
		"success_rate":     stats.SuccessRate,
		"average_duration": stats.AvgDuration,
		"efficiency_score": stats.SuccessRate * (1.0 / (stats.AvgDuration + 1)), // Higher score = better efficiency
		"last_used":        stats.LastUsed,
	}

	return efficiency, nil
}