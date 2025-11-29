package server

import (
	"time"

	"github.com/google/uuid"
)

type Mastery struct {
	MasteryType      string          `json:"mastery_type"`
	MasteryLevel     int             `json:"mastery_level"`
	ExperienceCurrent int64          `json:"experience_current"`
	ExperienceRequired int64          `json:"experience_required"`
	RewardsUnlocked  []MasteryReward `json:"rewards_unlocked"`
}

type MasteryLevels struct {
	CharacterID uuid.UUID `json:"character_id"`
	Masteries   []Mastery `json:"masteries"`
}

type MasteryProgress struct {
	CharacterID          uuid.UUID      `json:"character_id"`
	MasteryType          string         `json:"mastery_type"`
	MasteryLevel         int            `json:"mastery_level"`
	ExperienceCurrent    int64          `json:"experience_current"`
	ExperienceRequired   int64          `json:"experience_required"`
	ProgressPercent      float64        `json:"progress_percent"`
	TotalExperienceEarned int64         `json:"total_experience_earned"`
	CompletionsCount     int            `json:"completions_count"`
	RewardsUnlocked      []MasteryReward `json:"rewards_unlocked"`
}

type MasteryReward struct {
	Level      int       `json:"level"`
	RewardType string    `json:"reward_type"`
	RewardID   string    `json:"reward_id"`
	UnlockedAt *time.Time `json:"unlocked_at,omitempty"`
}

type MasteryRewards struct {
	CharacterID uuid.UUID           `json:"character_id"`
	Rewards     []MasteryRewardItem `json:"rewards"`
}

type MasteryRewardItem struct {
	MasteryType string     `json:"mastery_type"`
	Level       int        `json:"level"`
	RewardType  string     `json:"reward_type"`
	RewardID    string     `json:"reward_id"`
	RewardName  string     `json:"reward_name"`
	Unlocked    bool       `json:"unlocked"`
	UnlockedAt  *time.Time `json:"unlocked_at,omitempty"`
}

