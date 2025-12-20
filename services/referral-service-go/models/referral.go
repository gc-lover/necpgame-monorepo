package models

import (
	"time"

	"github.com/google/uuid"
)

type ReferralStatus string

type ReferralMilestoneType string

type ReferralRewardType string

type ReferralLeaderboardType string

type ReferralEventType string

type ReferralCode struct {
	ID        uuid.UUID `json:"id" db:"id"`
	PlayerID  uuid.UUID `json:"player_id" db:"player_id"`
	Code      string    `json:"code" db:"code"`
	IsActive  bool      `json:"is_active" db:"is_active"`
	CreatedAt time.Time `json:"created_at" db:"created_at"`
}

type Referral struct {
	ID                 uuid.UUID      `json:"id" db:"id"`
	ReferrerID         uuid.UUID      `json:"referrer_id" db:"referrer_id"`
	RefereeID          uuid.UUID      `json:"referee_id" db:"referee_id"`
	ReferralCodeID     uuid.UUID      `json:"referral_code_id" db:"referral_code_id"`
	RegisteredAt       time.Time      `json:"registered_at" db:"registered_at"`
	Status             ReferralStatus `json:"status" db:"status"`
	Level10Reached     bool           `json:"level_10_reached" db:"level_10_reached"`
	Level10ReachedAt   *time.Time     `json:"level_10_reached_at,omitempty" db:"level_10_reached_at"`
	WelcomeBonusGiven  bool           `json:"welcome_bonus_given" db:"welcome_bonus_given"`
	ReferrerBonusGiven bool           `json:"referrer_bonus_given" db:"referrer_bonus_given"`
	CreatedAt          time.Time      `json:"created_at" db:"created_at"`
	UpdatedAt          time.Time      `json:"updated_at" db:"updated_at"`
}

type ReferralMilestone struct {
	ID              uuid.UUID             `json:"id" db:"id"`
	PlayerID        uuid.UUID             `json:"player_id" db:"player_id"`
	MilestoneType   ReferralMilestoneType `json:"milestone_type" db:"milestone_type"`
	MilestoneValue  int                   `json:"milestone_value" db:"milestone_value"`
	AchievedAt      time.Time             `json:"achieved_at" db:"achieved_at"`
	RewardClaimed   bool                  `json:"reward_claimed" db:"reward_claimed"`
	RewardClaimedAt *time.Time            `json:"reward_claimed_at,omitempty" db:"reward_claimed_at"`
}

type ReferralReward struct {
	ID            uuid.UUID          `json:"id" db:"id"`
	PlayerID      uuid.UUID          `json:"player_id" db:"player_id"`
	ReferralID    *uuid.UUID         `json:"referral_id,omitempty" db:"referral_id"`
	RewardType    ReferralRewardType `json:"reward_type" db:"reward_type"`
	RewardAmount  int64              `json:"reward_amount" db:"reward_amount"`
	CurrencyType  string             `json:"currency_type" db:"currency_type"`
	DistributedAt time.Time          `json:"distributed_at" db:"distributed_at"`
}

type ReferralStats struct {
	PlayerID         uuid.UUID              `json:"player_id"`
	TotalReferrals   int                    `json:"total_referrals"`
	ActiveReferrals  int                    `json:"active_referrals"`
	Level10Referrals int                    `json:"level_10_referrals"`
	CurrentMilestone *ReferralMilestoneType `json:"current_milestone,omitempty"`
	TotalRewards     int64                  `json:"total_rewards"`
	LastUpdated      time.Time              `json:"last_updated"`
}

type ReferralLeaderboardEntry struct {
	Rank             int                    `json:"rank"`
	PlayerID         uuid.UUID              `json:"player_id"`
	PlayerName       string                 `json:"player_name"`
	TotalReferrals   int                    `json:"total_referrals"`
	ActiveReferrals  int                    `json:"active_referrals"`
	Level10Referrals int                    `json:"level_10_referrals"`
	CurrentMilestone *ReferralMilestoneType `json:"current_milestone,omitempty"`
	TotalRewards     int64                  `json:"total_rewards"`
}

type ReferralEvent struct {
	ID        uuid.UUID              `json:"id" db:"id"`
	PlayerID  uuid.UUID              `json:"player_id" db:"player_id"`
	EventType ReferralEventType      `json:"event_type" db:"event_type"`
	EventData map[string]interface{} `json:"event_data" db:"event_data"`
	CreatedAt time.Time              `json:"created_at" db:"created_at"`
}
