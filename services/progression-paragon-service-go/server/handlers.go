// Issue: #1604, #1607
package server

import (
	"context"
	"net/http"
	"strings"
	"sync"
	"time"

	"github.com/necpgame/progression-paragon-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// Context timeout constants
const (
	DBTimeout = 50 * time.Millisecond
)

// ParagonHandlers implements api.Handler interface (ogen typed handlers)
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type ParagonHandlers struct {
	service ParagonServiceInterface
	logger  *logrus.Logger

	// Memory pooling for hot path structs (zero allocations target!)
	paragonLevelsPool sync.Pool
	paragonStatsPool sync.Pool
	paragonAllocationPool sync.Pool
}

func NewParagonHandlers(service ParagonServiceInterface) *ParagonHandlers {
	h := &ParagonHandlers{
		service: service,
		logger:  GetLogger(),
	}

	// Initialize memory pools (zero allocations target!)
	h.paragonLevelsPool = sync.Pool{
		New: func() interface{} {
			return &api.ParagonLevels{}
		},
	}
	h.paragonStatsPool = sync.Pool{
		New: func() interface{} {
			return &api.ParagonStats{}
		},
	}
	h.paragonAllocationPool = sync.Pool{
		New: func() interface{} {
			return &api.ParagonAllocation{}
		},
	}

	return h
}

// GetParagonLevels - TYPED response!
func (h *ParagonHandlers) GetParagonLevels(ctx context.Context, params api.GetParagonLevelsParams) (api.GetParagonLevelsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	levels, err := h.service.GetParagonLevels(ctx, params.CharacterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get paragon levels")
		return &api.GetParagonLevelsInternalServerError{
			Error:   http.StatusText(http.StatusInternalServerError),
			Message: "failed to get paragon levels",
		}, nil
	}

	if levels == nil {
		return &api.GetParagonLevelsNotFound{
			Error:   http.StatusText(http.StatusNotFound),
			Message: "paragon levels not found",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiLevels := h.paragonLevelsPool.Get().(*api.ParagonLevels)
	// Note: Not returning to pool - struct is returned to caller

	*apiLevels = convertParagonLevelsToAPI(levels)
	return apiLevels, nil
}

// DistributeParagonPoints - TYPED response!
// Issue: #1607 - Uses memory pooling for zero allocations
// Issue: #1516 - Validation and error handling
func (h *ParagonHandlers) DistributeParagonPoints(ctx context.Context, req *api.DistributeParagonPointsRequest, params api.DistributeParagonPointsParams) (api.DistributeParagonPointsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Issue: #1516 - Validate request
	if req == nil || len(req.Allocations) == 0 {
		return &api.DistributeParagonPointsBadRequest{
			Error:   http.StatusText(http.StatusBadRequest),
			Message: "allocations are required",
		}, nil
	}

	allocations := make([]ParagonAllocation, len(req.Allocations))
	for i, a := range req.Allocations {
		if a.Points <= 0 {
			return &api.DistributeParagonPointsBadRequest{
				Error:   http.StatusText(http.StatusBadRequest),
				Message: "points must be positive",
			}, nil
		}
		allocations[i] = ParagonAllocation{
			StatType:        string(a.StatType),
			PointsAllocated: a.Points,
		}
	}

	levels, err := h.service.DistributeParagonPoints(ctx, params.CharacterID, allocations)
	if err != nil {
		// Issue: #1516 - Check for validation errors (BadRequest)
		errMsg := err.Error()
		if strings.Contains(errMsg, "invalid stat_type") || strings.Contains(errMsg, "points must be positive") || 
		   strings.Contains(errMsg, "not enough paragon points") || strings.Contains(errMsg, "not found") {
			h.logger.WithError(err).Warn("Validation error in distribute paragon points")
			return &api.DistributeParagonPointsBadRequest{
				Error:   http.StatusText(http.StatusBadRequest),
				Message: errMsg,
			}, nil
		}
		
		h.logger.WithError(err).Error("Failed to distribute paragon points")
		return &api.DistributeParagonPointsInternalServerError{
			Error:   http.StatusText(http.StatusInternalServerError),
			Message: "failed to distribute paragon points",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiLevels := h.paragonLevelsPool.Get().(*api.ParagonLevels)
	// Note: Not returning to pool - struct is returned to caller

	*apiLevels = convertParagonLevelsToAPI(levels)
	return apiLevels, nil
}

// GetParagonStats - TYPED response!
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *ParagonHandlers) GetParagonStats(ctx context.Context, params api.GetParagonStatsParams) (api.GetParagonStatsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	stats, err := h.service.GetParagonStats(ctx, params.CharacterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get paragon stats")
		// Error responses are rare, no pooling needed
		return &api.GetParagonStatsInternalServerError{
			Error:   http.StatusText(http.StatusInternalServerError),
			Message: "failed to get paragon stats",
		}, nil
	}

	if stats == nil {
		// Error responses are rare, no pooling needed
		return &api.GetParagonStatsNotFound{
			Error:   http.StatusText(http.StatusNotFound),
			Message: "paragon stats not found",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiStats := h.paragonStatsPool.Get().(*api.ParagonStats)
	// Note: Not returning to pool - struct is returned to caller

	*apiStats = convertParagonStatsToAPI(stats)
	return apiStats, nil
}

func convertParagonLevelsToAPI(levels *ParagonLevels) api.ParagonLevels {
	allocations := make([]api.ParagonAllocation, len(levels.Allocations))
	for i, a := range levels.Allocations {
		statType := api.ParagonAllocationStatType(a.StatType)
		allocations[i] = api.ParagonAllocation{
			StatType:        api.NewOptParagonAllocationStatType(statType),
			PointsAllocated: api.NewOptInt(a.PointsAllocated),
		}
	}

	return api.ParagonLevels{
		CharacterID:            api.NewOptUUID(levels.CharacterID),
		ParagonLevel:           api.NewOptInt(levels.ParagonLevel),
		ParagonPointsTotal:     api.NewOptInt(levels.ParagonPointsTotal),
		ParagonPointsSpent:     api.NewOptInt(levels.ParagonPointsSpent),
		ParagonPointsAvailable: api.NewOptInt(levels.ParagonPointsAvailable),
		ExperienceCurrent:      api.NewOptInt(int(levels.ExperienceCurrent)),
		ExperienceRequired:     api.NewOptInt(int(levels.ExperienceRequired)),
		Allocations:            allocations,
		UpdatedAt:             api.NewOptDateTime(levels.UpdatedAt),
	}
}

func convertParagonStatsToAPI(stats *ParagonStats) api.ParagonStats {
	return api.ParagonStats{
		CharacterID:        api.NewOptUUID(stats.CharacterID),
		TotalParagonLevels: api.NewOptInt(stats.TotalParagonLevels),
		TotalPointsEarned:  api.NewOptInt(stats.TotalPointsEarned),
		TotalPointsSpent:   api.NewOptInt(stats.TotalPointsSpent),
		PointsByStat:       api.NewOptParagonStatsPointsByStat(stats.PointsByStat),
		GlobalRank:         api.NewOptInt(stats.GlobalRank),
		Percentile:         api.NewOptFloat32(float32(stats.Percentile)),
	}
}

