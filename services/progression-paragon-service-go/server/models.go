package server

import (
	"time"

	"github.com/google/uuid"
)

type ParagonAllocation struct {
	StatType       string `json:"stat_type"`
	PointsAllocated int   `json:"points_allocated"`
}

type ParagonLevels struct {
	CharacterID          uuid.UUID           `json:"character_id"`
	ParagonLevel         int                 `json:"paragon_level"`
	ParagonPointsTotal   int                 `json:"paragon_points_total"`
	ParagonPointsSpent   int                 `json:"paragon_points_spent"`
	ParagonPointsAvailable int               `json:"paragon_points_available"`
	ExperienceCurrent    int64               `json:"experience_current"`
	ExperienceRequired   int64               `json:"experience_required"`
	Allocations          []ParagonAllocation `json:"allocations"`
	UpdatedAt            time.Time           `json:"updated_at"`
}

type ParagonStats struct {
	CharacterID        uuid.UUID         `json:"character_id"`
	TotalParagonLevels int               `json:"total_paragon_levels"`
	TotalPointsEarned  int               `json:"total_points_earned"`
	TotalPointsSpent   int               `json:"total_points_spent"`
	PointsByStat       map[string]int    `json:"points_by_stat"`
	GlobalRank         int               `json:"global_rank"`
	Percentile         float64           `json:"percentile"`
}

