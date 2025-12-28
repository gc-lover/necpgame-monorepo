package models

import (
	"time"

	"github.com/google/uuid"
)

// Achievement represents an achievement in the system
type Achievement struct {
	ID          uuid.UUID `json:"id" db:"id"`
	Name        string    `json:"name" db:"name"`
	Description string    `json:"description" db:"description"`
	Category    string    `json:"category" db:"category"`
	IconURL     string    `json:"icon_url" db:"icon_url"`
	Points      int       `json:"points" db:"points"`
	Rarity      string    `json:"rarity" db:"rarity"`
	IsHidden    bool      `json:"is_hidden" db:"is_hidden"`
	IsActive    bool      `json:"is_active" db:"is_active"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
}

// AchievementProgress tracks player progress towards achievements
type AchievementProgress struct {
	ID            uuid.UUID `json:"id" db:"id"`
	PlayerID      uuid.UUID `json:"player_id" db:"player_id"`
	AchievementID uuid.UUID `json:"achievement_id" db:"achievement_id"`
	Progress      int       `json:"progress" db:"progress"`
	MaxProgress   int       `json:"max_progress" db:"max_progress"`
	IsCompleted   bool      `json:"is_completed" db:"is_completed"`
	CompletedAt   *time.Time `json:"completed_at" db:"completed_at"`
	CreatedAt     time.Time `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time `json:"updated_at" db:"updated_at"`
}

// PlayerAchievement represents unlocked achievements for players
type PlayerAchievement struct {
	ID            uuid.UUID  `json:"id" db:"id"`
	PlayerID      uuid.UUID  `json:"player_id" db:"player_id"`
	AchievementID uuid.UUID  `json:"achievement_id" db:"achievement_id"`
	UnlockedAt    time.Time  `json:"unlocked_at" db:"unlocked_at"`
	PointsEarned  int        `json:"points_earned" db:"points_earned"`
	Rewards       []Reward   `json:"rewards" db:"rewards"`
}

// Reward represents rewards granted for achievements
type Reward struct {
	Type   string `json:"type"`   // "item", "currency", "title", "skin"
	ID     string `json:"id"`     // Item ID, currency type, etc.
	Amount int    `json:"amount"` // Quantity or value
}

// AchievementCriteria defines requirements for unlocking achievements
type AchievementCriteria struct {
	Type     string      `json:"type"`     // "stat", "action", "quest", "combat"
	Target   string      `json:"target"`   // Stat name, action type, etc.
	Value    interface{} `json:"value"`    // Required value
	Operator string      `json:"operator"` // "eq", "gt", "gte", "lt", "lte"
}

// AchievementStats holds achievement statistics
type AchievementStats struct {
	AchievementID       uuid.UUID `json:"achievement_id"`
	TotalPlayers        int       `json:"total_players"`
	CompletedPlayers    int       `json:"completed_players"`
	CompletionRate      float64   `json:"completion_rate"`
	AverageTimeToUnlock int64     `json:"average_time_to_unlock"` // in seconds
}

// PlayerAchievementProfile represents a player's achievement profile
type PlayerAchievementProfile struct {
	PlayerID               uuid.UUID `json:"player_id"`
	TotalAchievements      int       `json:"total_achievements"`
	CompletedAchievements  int       `json:"completed_achievements"`
	TotalPoints            int       `json:"total_points"`
	RarityBreakdown        map[string]int `json:"rarity_breakdown"`
	CategoryBreakdown      map[string]int `json:"category_breakdown"`
	RecentAchievements     []*PlayerAchievement `json:"recent_achievements"`
	AchievementStreak      int       `json:"achievement_streak"`
	LastAchievementDate    *time.Time `json:"last_achievement_date"`
}

// AchievementEvent represents events that can trigger achievement progress
type AchievementEvent struct {
	Type      string                 `json:"type"`      // "combat_win", "quest_complete", "level_up", etc.
	PlayerID  uuid.UUID              `json:"player_id"`
	Data      map[string]interface{} `json:"data"`      // Event-specific data
	Timestamp time.Time              `json:"timestamp"`
}

    // AchievementMilestone represents major milestones in the achievement system
    type AchievementMilestone struct {
    	ID          uuid.UUID `json:"id"`
    	Name        string    `json:"name"`
    	Description string    `json:"description"`
    	Threshold   int       `json:"threshold"`   // Number of achievements required
    	Rewards     []Reward  `json:"rewards"`
    	IsActive    bool      `json:"is_active"`
    }

    // AchievementImportRequest represents a request to import achievements
    type AchievementImportRequest struct {
    	Achievements []*Achievement `json:"achievements"`
    	DryRun       bool           `json:"dry_run,omitempty"` // If true, validate without importing
    }

    // AchievementImportResponse represents the response from an achievement import
    type AchievementImportResponse struct {
    	Total     int `json:"total"`
    	Imported  int `json:"imported"`
    	Failed    int `json:"failed"`
    	Validated bool `json:"validated,omitempty"`
    }