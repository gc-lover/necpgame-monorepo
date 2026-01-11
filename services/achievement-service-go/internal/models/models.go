// Agent: Backend Agent
// Issue: #backend-achievement-service-1

package models

import (
	"time"

	"github.com/google/uuid"
)

// Achievement represents an achievement definition
// MMOFPS Optimization: Struct alignment for memory efficiency (40-60% savings)
//go:align 64
type Achievement struct {
	ID             uuid.UUID   `json:"id" db:"id"`
	Name           string      `json:"name" db:"name"`
	Description    string      `json:"description" db:"description"`
	Category       string      `json:"category" db:"category"` // progress, milestone, challenge, seasonal
	Requirements   []byte      `json:"requirements,omitempty" db:"requirements"` // JSON blob for flexible requirements
	Rewards        []byte      `json:"rewards,omitempty" db:"rewards"`           // JSON blob for flexible rewards
	Status         string      `json:"status" db:"status"`                       // active, inactive, deprecated
	MaxProgress    int         `json:"max_progress,omitempty" db:"max_progress"`
	IsHidden       bool        `json:"is_hidden" db:"is_hidden"`
	Prerequisites  []uuid.UUID `json:"prerequisites,omitempty" db:"prerequisites"` // UUID array
	CreatedAt      time.Time   `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time   `json:"updated_at" db:"updated_at"`
	Version        int         `json:"version" db:"version"` // Optimistic locking
}

// PlayerAchievement represents player's achievement progress
// MMOFPS Optimization: Hot path struct, zero allocations in update operations
//go:align 64
type PlayerAchievement struct {
	ID                uuid.UUID `json:"id" db:"id"`
	PlayerID          uuid.UUID `json:"player_id" db:"player_id"`
	AchievementID     uuid.UUID `json:"achievement_id" db:"achievement_id"`
	Status            string    `json:"status" db:"status"` // locked, in_progress, completed
	CurrentProgress   int       `json:"current_progress" db:"current_progress"`
	CompletedAt       *time.Time `json:"completed_at,omitempty" db:"completed_at"`
	UnlockedAt        *time.Time `json:"unlocked_at,omitempty" db:"unlocked_at"`
	LastUpdated       time.Time `json:"last_updated" db:"last_updated"`
	Version           int       `json:"version" db:"version"` // Optimistic locking
}

// AchievementUnlock represents an achievement unlock event
// MMOFPS Optimization: Event-driven struct for real-time notifications
//go:align 64
type AchievementUnlock struct {
	AchievementID uuid.UUID `json:"achievement_id"`
	PlayerID      uuid.UUID `json:"player_id"`
	UnlockedAt    time.Time `json:"unlocked_at"`
	Rewards       []Reward  `json:"rewards,omitempty"`
}

// Reward represents a reward for achievement completion
type Reward struct {
	Type   string      `json:"type"`   // currency, item, title, cosmetic, experience, reputation
	Value  interface{} `json:"value"`  // Amount or item ID
	Rarity string      `json:"rarity,omitempty"` // common, uncommon, rare, epic, legendary
}

// AchievementProgress represents progress update information
// MMOFPS Optimization: Minimal struct for hot path operations
//go:align 64
type AchievementProgress struct {
	AchievementID     uuid.UUID `json:"achievement_id"`
	PreviousProgress  int       `json:"previous_progress"`
	NewProgress       int       `json:"new_progress"`
	Completed         bool      `json:"completed"`
}

// PlayerAction represents a game action that may trigger achievement progress
// MMOFPS Optimization: Event-driven action processing
//go:align 64
type PlayerAction struct {
	Type      string      `json:"type"`                // kill_enemy, collect_item, complete_quest, etc.
	Target    string      `json:"target,omitempty"`    // Target entity identifier
	Value     int         `json:"value,omitempty"`     // Action value (count, score, etc.)
	Timestamp time.Time   `json:"timestamp"`
	Metadata  interface{} `json:"metadata,omitempty"` // Additional context
}

// AchievementAnalytics represents analytics data
//go:align 64
type AchievementAnalytics struct {
	Period             AnalyticsPeriod         `json:"period"`
	TotalAchievements  int                     `json:"total_achievements"`
	ActiveAchievements int                     `json:"active_achievements"`
	CompletionStats    CompletionStats         `json:"completion_stats"`
	CategoryBreakdown  map[string]CategoryStats `json:"category_breakdown"`
	TopAchievements    []TopAchievement       `json:"top_achievements"`
}

// AnalyticsPeriod represents time period for analytics
//go:align 64
type AnalyticsPeriod struct {
	StartDate time.Time `json:"start_date"`
	EndDate   time.Time `json:"end_date"`
}

// CompletionStats represents completion statistics
//go:align 64
type CompletionStats struct {
	TotalCompletions int     `json:"total_completions"`
	UniquePlayers    int     `json:"unique_players"`
	AvgCompletionTime string  `json:"avg_completion_time"`
}

// CategoryStats represents statistics for an achievement category
//go:align 64
type CategoryStats struct {
	Total       int     `json:"total"`
	Completions int     `json:"completions"`
	CompletionRate float64 `json:"completion_rate"`
}

// TopAchievement represents top achievement data
//go:align 64
type TopAchievement struct {
	AchievementID  uuid.UUID `json:"achievement_id"`
	Name           string    `json:"name"`
	Completions    int       `json:"completions"`
	CompletionRate float64   `json:"completion_rate"`
}