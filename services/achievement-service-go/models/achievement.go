package models

import (
	"time"

	"github.com/google/uuid"
)

type AchievementRarity string

type AchievementType string

type AchievementCategory string

type AchievementStatus string

type Achievement struct {
	ID          uuid.UUID              `json:"id" db:"id"`
	CreatedAt   time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time              `json:"updated_at" db:"updated_at"`
	Code        string                 `json:"code" db:"code"`
	Title       string                 `json:"title" db:"title"`
	Description string                 `json:"description" db:"description"`
	Type        AchievementType        `json:"type" db:"type"`
	Category    AchievementCategory    `json:"category" db:"category"`
	Rarity      AchievementRarity      `json:"rarity" db:"rarity"`
	Points      int                    `json:"points" db:"points"`
	IsHidden    bool                   `json:"is_hidden" db:"is_hidden"`
	IsSeasonal  bool                   `json:"is_seasonal" db:"is_seasonal"`
	Conditions  map[string]interface{} `json:"conditions" db:"conditions"`
	Rewards     map[string]interface{} `json:"rewards" db:"rewards"`
	SeasonID    *uuid.UUID             `json:"season_id,omitempty" db:"season_id"`
}

type PlayerAchievement struct {
	// Pointers first (8 bytes alignment)
	ProgressData map[string]interface{} `json:"progress_data,omitempty" db:"progress_data"`
	UnlockedAt   *time.Time             `json:"unlocked_at,omitempty" db:"unlocked_at"`

	// UUIDs (16 bytes alignment)
	ID            uuid.UUID `json:"id" db:"id"`
	PlayerID      uuid.UUID `json:"player_id" db:"player_id"`
	AchievementID uuid.UUID `json:"achievement_id" db:"achievement_id"`

	// Time fields (8 bytes alignment)
	CreatedAt time.Time `json:"created_at" db:"created_at"`
	UpdatedAt time.Time `json:"updated_at" db:"updated_at"`

	// String enum (8 bytes alignment)
	Status AchievementStatus `json:"status" db:"status"`

	// Ints (8 bytes alignment)
	Progress    int `json:"progress" db:"progress"`
	ProgressMax int `json:"progress_max" db:"progress_max"`
}

type AchievementListResponse struct {
	// Pointers first (8 bytes alignment)
	Categories map[string]int `json:"categories"`

	// Slice (24 bytes alignment)
	Achievements []Achievement `json:"achievements"`

	// Int (8 bytes alignment)
	Total int `json:"total"`
}

type PlayerAchievementResponse struct {
	// Slices (24 bytes alignment)
	Achievements   []PlayerAchievement `json:"achievements"`
	NearCompletion []PlayerAchievement `json:"near_completion"`
	RecentUnlocks  []PlayerAchievement `json:"recent_unlocks"`

	// Ints (8 bytes alignment)
	Total    int `json:"total"`
	Unlocked int `json:"unlocked"`
}

type LeaderboardEntry struct {
	// UUID (16 bytes alignment)
	PlayerID uuid.UUID `json:"player_id"`

	// String (8 bytes alignment)
	PlayerName string `json:"player_name"`

	// Ints (8 bytes alignment)
	Rank     int `json:"rank"`
	Points   int `json:"points"`
	Unlocked int `json:"unlocked"`
}

type LeaderboardResponse struct {
	// Slice (24 bytes alignment)
	Entries []LeaderboardEntry `json:"entries"`

	// String (8 bytes alignment)
	Period string `json:"period"`

	// Int (8 bytes alignment)
	Total int `json:"total"`
}

type AchievementStatsResponse struct {
	// Pointers first (8 bytes alignment)
	AverageTime     *float64   `json:"average_time,omitempty"`
	FirstPlayerID   *uuid.UUID `json:"first_player_id,omitempty"`
	FirstPlayerName *string    `json:"first_player_name,omitempty"`
	FirstUnlockedAt *time.Time `json:"first_unlocked_at,omitempty"`

	// UUID (16 bytes alignment)
	AchievementID uuid.UUID `json:"achievement_id"`

	// Float (8 bytes alignment)
	UnlockPercent float64 `json:"unlock_percent"`

	// Int (8 bytes alignment)
	TotalUnlocks int `json:"total_unlocks"`
}
