package models

import (
	"time"

	"github.com/google/uuid"
)

type ParagonAllocation struct {
	StatType        string `json:"stat_type" db:"stat_type"`
	PointsAllocated int    `json:"points_allocated" db:"points_allocated"`
}

type ParagonLevels struct {
	CharacterID            uuid.UUID           `json:"character_id" db:"character_id"`
	ParagonLevel           int                 `json:"paragon_level" db:"paragon_level"`
	ParagonPointsTotal     int                 `json:"paragon_points_total" db:"paragon_points_total"`
	ParagonPointsSpent     int                 `json:"paragon_points_spent" db:"paragon_points_spent"`
	ParagonPointsAvailable int                 `json:"paragon_points_available" db:"paragon_points_available"`
	ExperienceCurrent      int64               `json:"experience_current" db:"experience_current"`
	ExperienceRequired     int64               `json:"experience_required" db:"experience_required"`
	Allocations            []ParagonAllocation `json:"allocations" db:"allocations"`
	UpdatedAt              time.Time           `json:"updated_at" db:"updated_at"`
}

type DistributeParagonPointsRequest struct {
	Allocations []ParagonAllocationRequest `json:"allocations"`
}

type ParagonAllocationRequest struct {
	StatType string `json:"stat_type"`
	Points   int    `json:"points"`
}

type ParagonStats struct {
	CharacterID        uuid.UUID      `json:"character_id" db:"character_id"`
	TotalParagonLevels int            `json:"total_paragon_levels" db:"total_paragon_levels"`
	TotalPointsEarned  int            `json:"total_points_earned" db:"total_points_earned"`
	TotalPointsSpent   int            `json:"total_points_spent" db:"total_points_spent"`
	PointsByStat       map[string]int `json:"points_by_stat" db:"points_by_stat"`
	GlobalRank         int            `json:"global_rank" db:"global_rank"`
	Percentile         float64        `json:"percentile" db:"percentile"`
}
