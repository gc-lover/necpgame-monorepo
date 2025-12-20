package models

import (
	"time"

	"github.com/google/uuid"
)

type BattlePassTrack string

type RewardType string

type XPSource string

type BattlePassSeason struct {
	ID        uuid.UUID `json:"id" db:"id"`
	Name      string    `json:"name" db:"name"`
	StartDate time.Time `json:"start_date" db:"start_date"`
	EndDate   time.Time `json:"end_date" db:"end_date"`
	MaxLevel  int       `json:"max_level" db:"max_level"`
	IsActive  bool      `json:"is_active" db:"is_active"`
	CreatedAt time.Time `json:"created_at" db:"created_at"`
	UpdatedAt time.Time `json:"updated_at" db:"updated_at"`
}

type BattlePassReward struct {
	ID         uuid.UUID              `json:"id" db:"id"`
	SeasonID   uuid.UUID              `json:"season_id" db:"season_id"`
	Level      int                    `json:"level" db:"level"`
	Track      BattlePassTrack        `json:"track" db:"track"`
	RewardType RewardType             `json:"reward_type" db:"reward_type"`
	RewardData map[string]interface{} `json:"reward_data" db:"reward_data"`
	CreatedAt  time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt  time.Time              `json:"updated_at" db:"updated_at"`
}

type PlayerBattlePassProgress struct {
	ID                 uuid.UUID  `json:"id" db:"id"`
	CharacterID        uuid.UUID  `json:"character_id" db:"character_id"`
	SeasonID           uuid.UUID  `json:"season_id" db:"season_id"`
	Level              int        `json:"level" db:"level"`
	XP                 int        `json:"xp" db:"xp"`
	XPToNextLevel      int        `json:"xp_to_next_level" db:"xp_to_next_level"`
	HasPremium         bool       `json:"has_premium" db:"has_premium"`
	PremiumPurchasedAt *time.Time `json:"premium_purchased_at,omitempty" db:"premium_purchased_at"`
	CreatedAt          time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt          time.Time  `json:"updated_at" db:"updated_at"`
}

type WeeklyChallenge struct {
	ID          uuid.UUID `json:"id" db:"id"`
	SeasonID    uuid.UUID `json:"season_id" db:"season_id"`
	Title       string    `json:"title" db:"title"`
	Description string    `json:"description" db:"description"`
	XPReward    int       `json:"xp_reward" db:"xp_reward"`
	Progress    int       `json:"progress"`
	Target      int       `json:"target" db:"target"`
	IsCompleted bool      `json:"is_completed"`
	WeekNumber  int       `json:"week_number" db:"week_number"`
	IsActive    bool      `json:"is_active" db:"is_active"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
}

type PlayerChallengeProgress struct {
	ID          uuid.UUID  `json:"id" db:"id"`
	CharacterID uuid.UUID  `json:"character_id" db:"character_id"`
	ChallengeID uuid.UUID  `json:"challenge_id" db:"challenge_id"`
	Progress    int        `json:"progress" db:"progress"`
	IsCompleted bool       `json:"is_completed" db:"is_completed"`
	CompletedAt *time.Time `json:"completed_at,omitempty" db:"completed_at"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time  `json:"updated_at" db:"updated_at"`
}

type ClaimedReward struct {
	ID          uuid.UUID `json:"id" db:"id"`
	CharacterID uuid.UUID `json:"character_id" db:"character_id"`
	RewardID    uuid.UUID `json:"reward_id" db:"reward_id"`
	ClaimedAt   time.Time `json:"claimed_at" db:"claimed_at"`
}

type LevelRequirements struct {
	Level        int `json:"level"`
	XPRequired   int `json:"xp_required"`
	CumulativeXP int `json:"cumulative_xp"`
}
