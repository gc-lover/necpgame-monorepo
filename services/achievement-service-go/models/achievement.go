package models

import (
	"time"

	"github.com/google/uuid"
)

type AchievementRarity string

const (
	RarityCommon    AchievementRarity = "common"
	RarityUncommon  AchievementRarity = "uncommon"
	RarityRare      AchievementRarity = "rare"
	RarityEpic      AchievementRarity = "epic"
	RarityLegendary AchievementRarity = "legendary"
)

type AchievementType string

const (
	AchievementTypeOneTime    AchievementType = "one_time"
	AchievementTypeProgressive AchievementType = "progressive"
	AchievementTypeHidden     AchievementType = "hidden"
	AchievementTypeSeasonal   AchievementType = "seasonal"
	AchievementTypeMeta       AchievementType = "meta"
)

type AchievementCategory string

const (
	CategoryCombat      AchievementCategory = "combat"
	CategoryQuest       AchievementCategory = "quest"
	CategorySocial      AchievementCategory = "social"
	CategoryEconomy     AchievementCategory = "economy"
	CategoryExploration AchievementCategory = "exploration"
	CategorySkills      AchievementCategory = "skills"
	CategoryCollections AchievementCategory = "collections"
	CategorySpecial     AchievementCategory = "special"
)

type AchievementStatus string

const (
	AchievementStatusLocked   AchievementStatus = "locked"
	AchievementStatusProgress AchievementStatus = "progress"
	AchievementStatusUnlocked AchievementStatus = "unlocked"
)

type Achievement struct {
	ID          uuid.UUID          `json:"id" db:"id"`
	Code        string             `json:"code" db:"code"`
	Type        AchievementType    `json:"type" db:"type"`
	Category    AchievementCategory `json:"category" db:"category"`
	Rarity      AchievementRarity  `json:"rarity" db:"rarity"`
	Title       string             `json:"title" db:"title"`
	Description string             `json:"description" db:"description"`
	Points      int                `json:"points" db:"points"`
	Conditions  map[string]interface{} `json:"conditions" db:"conditions"`
	Rewards     map[string]interface{} `json:"rewards" db:"rewards"`
	IsHidden    bool               `json:"is_hidden" db:"is_hidden"`
	IsSeasonal  bool               `json:"is_seasonal" db:"is_seasonal"`
	SeasonID    *uuid.UUID         `json:"season_id,omitempty" db:"season_id"`
	CreatedAt   time.Time          `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time          `json:"updated_at" db:"updated_at"`
}

type PlayerAchievement struct {
	ID            uuid.UUID          `json:"id" db:"id"`
	PlayerID      uuid.UUID          `json:"player_id" db:"player_id"`
	AchievementID uuid.UUID          `json:"achievement_id" db:"achievement_id"`
	Status        AchievementStatus  `json:"status" db:"status"`
	Progress      int                `json:"progress" db:"progress"`
	ProgressMax   int                `json:"progress_max" db:"progress_max"`
	ProgressData  map[string]interface{} `json:"progress_data,omitempty" db:"progress_data"`
	UnlockedAt    *time.Time         `json:"unlocked_at,omitempty" db:"unlocked_at"`
	CreatedAt     time.Time          `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time          `json:"updated_at" db:"updated_at"`
}

type AchievementListResponse struct {
	Achievements []Achievement `json:"achievements"`
	Total        int           `json:"total"`
	Categories   map[string]int `json:"categories"`
}

type PlayerAchievementResponse struct {
	Achievements    []PlayerAchievement `json:"achievements"`
	Total           int                 `json:"total"`
	Unlocked        int                 `json:"unlocked"`
	NearCompletion  []PlayerAchievement `json:"near_completion"`
	RecentUnlocks   []PlayerAchievement `json:"recent_unlocks"`
}

type LeaderboardEntry struct {
	Rank      int       `json:"rank"`
	PlayerID  uuid.UUID `json:"player_id"`
	PlayerName string   `json:"player_name"`
	Points    int       `json:"points"`
	Unlocked  int       `json:"unlocked"`
}

type LeaderboardResponse struct {
	Entries    []LeaderboardEntry `json:"entries"`
	Total      int                `json:"total"`
	Period     string             `json:"period"`
}

type AchievementStatsResponse struct {
	AchievementID   uuid.UUID `json:"achievement_id"`
	TotalUnlocks    int      `json:"total_unlocks"`
	UnlockPercent   float64  `json:"unlock_percent"`
	AverageTime     *float64 `json:"average_time,omitempty"`
	FirstPlayerID   *uuid.UUID `json:"first_player_id,omitempty"`
	FirstPlayerName *string   `json:"first_player_name,omitempty"`
	FirstUnlockedAt *time.Time `json:"first_unlocked_at,omitempty"`
}

