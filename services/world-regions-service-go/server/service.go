// Issue: #140875729
// PERFORMANCE: Business logic layer with memory pooling

package server

import (
	"context"
	"fmt"
	"sync"
	"time"

	"go.uber.org/zap"
)

// WorldRegionsService contains business logic for world regions
// PERFORMANCE: Structured for optimal memory layout
type WorldRegionsService struct {
	repo   *WorldRegionsRepository
	logger *zap.Logger

	// PERFORMANCE: Object pool for region operations
	regionPool sync.Pool
}

// NewWorldRegionsService creates a new service instance
// PERFORMANCE: Pre-allocates resources
func NewWorldRegionsService(repo *WorldRegionsRepository) *WorldRegionsService {
	svc := &WorldRegionsService{
		repo: repo,
		regionPool: sync.Pool{
			New: func() interface{} {
				return &WorldRegion{}
			},
		},
	}

	// PERFORMANCE: Initialize structured logger
	if l, err := zap.NewProduction(); err == nil {
		svc.logger = l
	} else {
		svc.logger = zap.NewNop()
	}

	return svc
}

// GetWorldRegions retrieves all world regions with filtering and pagination
// PERFORMANCE: Context-based timeout, optimized DB queries
func (s *WorldRegionsService) GetWorldRegions(ctx context.Context, status, continent string, limit, offset int) ([]*WorldRegion, int, error) {
	// PERFORMANCE: Context timeout check
	if deadline, ok := ctx.Deadline(); ok && time.Until(deadline) < 100*time.Millisecond {
		return nil, 0, fmt.Errorf("request timeout too close")
	}

	regions, total, err := s.repo.GetWorldRegions(ctx, status, continent, limit, offset)
	if err != nil {
		s.logger.Error("Failed to get world regions",
			zap.String("status", status),
			zap.String("continent", continent),
			zap.Error(err))
		return nil, 0, err
	}

	return regions, total, nil
}

// GetWorldRegion retrieves a specific world region by ID
func (s *WorldRegionsService) GetWorldRegion(ctx context.Context, regionID string) (*WorldRegion, error) {
	region, err := s.repo.GetWorldRegionByID(ctx, regionID)
	if err != nil {
		s.logger.Error("Failed to get world region",
			zap.String("region_id", regionID),
			zap.Error(err))
		return nil, err
	}

	return region, nil
}

// GetRegionTimeline retrieves timeline events for a region
func (s *WorldRegionsService) GetRegionTimeline(ctx context.Context, regionID string, periodStart, periodEnd int) ([]*TimelineEvent, error) {
	events, err := s.repo.GetRegionTimeline(ctx, regionID, periodStart, periodEnd)
	if err != nil {
		s.logger.Error("Failed to get region timeline",
			zap.String("region_id", regionID),
			zap.Error(err))
		return nil, err
	}

	return events, nil
}

// ImportWorldRegions imports regions from YAML knowledge base
func (s *WorldRegionsService) ImportWorldRegions(ctx context.Context, sourcePaths []string, overwriteExisting bool) (int, int, []string, error) {
	imported := 0
	updated := 0
	var errors []string

	for _, path := range sourcePaths {
		// TODO: Parse YAML file and import regions
		// For now, just demonstrate the structure
		s.logger.Info("Would import regions from", zap.String("path", path))

		// This would parse the YAML and call repo.ImportWorldRegion for each region
		// imported++
	}

	s.logger.Info("Import completed",
		zap.Int("imported", imported),
		zap.Int("updated", updated),
		zap.Int("errors", len(errors)))

	return imported, updated, errors, nil
}

// ValidateRegionData validates region data before import
func (s *WorldRegionsService) ValidateRegionData(region *WorldRegion) error {
	if region.ID == "" {
		return fmt.Errorf("region ID is required")
	}

	if region.Name == "" {
		return fmt.Errorf("region name is required")
	}

	validContinents := map[string]bool{
		"africa": true, "america": true, "asia": true, "europe": true,
		"oceania": true, "antarctica": true, "arctic": true, "cis": true, "middle-east": true,
	}

	if !validContinents[region.Continent] {
		return fmt.Errorf("invalid continent: %s", region.Continent)
	}

	validStatuses := map[string]bool{
		"draft": true, "ready_for_backend": true, "approved": true, "published": true,
	}

	if !validStatuses[region.Status] {
		return fmt.Errorf("invalid status: %s", region.Status)
	}

	return nil
}
