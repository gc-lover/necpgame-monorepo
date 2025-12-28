package repository

import (
	"time"

	"github.com/google/uuid"
)

// ReferralCode represents a referral code
type ReferralCode struct {
	ID          uuid.UUID  `db:"id" json:"id"`
	CharacterID uuid.UUID  `db:"character_id" json:"character_id"`
	Code        string     `db:"code" json:"code"`
	Prefix      string     `db:"prefix" json:"prefix"`
	CreatedAt   time.Time  `db:"created_at" json:"created_at"`
	ExpiresAt   *time.Time `db:"expires_at" json:"expires_at,omitempty"`
	IsActive    bool       `db:"is_active" json:"is_active"`
	UsageCount  int        `db:"usage_count" json:"usage_count"`
	MaxUsage    *int       `db:"max_usage" json:"max_usage,omitempty"`
}

// ReferralRegistration represents a referral registration
type ReferralRegistration struct {
	ID                   uuid.UUID  `db:"id" json:"id"`
	ReferrerID           uuid.UUID  `db:"referrer_id" json:"referrer_id"`
	ReferredID           uuid.UUID  `db:"referred_id" json:"referred_id"`
	ReferralCode         string     `db:"referral_code" json:"referral_code"`
	Status               string     `db:"status" json:"status"` // pending, active, milestone_reached, inactive
	RegisteredAt         time.Time  `db:"registered_at" json:"registered_at"`
	Level10ReachedAt     *time.Time `db:"level_10_reached_at" json:"level_10_reached_at,omitempty"`
	MilestoneReachedAt   *time.Time `db:"milestone_reached_at" json:"milestone_reached_at,omitempty"`
	WelcomeRewardClaimed bool       `db:"welcome_reward_claimed" json:"welcome_reward_claimed"`
	Level10RewardClaimed bool       `db:"level_10_reward_claimed" json:"level_10_reward_claimed"`
	MilestoneRewardClaimed bool     `db:"milestone_reward_claimed" json:"milestone_reward_claimed"`
	CreatedAt            time.Time  `db:"created_at" json:"created_at"`
	UpdatedAt            time.Time  `db:"updated_at" json:"updated_at"`
}

// ReferralStatistics represents referral statistics for a user
type ReferralStatistics struct {
	UserID            uuid.UUID `json:"user_id"`
	TotalReferrals    int       `json:"total_referrals"`
	ConvertedReferrals int      `json:"converted_referrals"`
	PendingReferrals  int       `json:"pending_referrals"`
	TotalEarnings     float64   `json:"total_earnings"`
}

// ReferralMilestone represents a referral milestone
type ReferralMilestone struct {
	ID                uuid.UUID  `db:"id" json:"id"`
	CharacterID       uuid.UUID  `db:"character_id" json:"character_id"`
	MilestoneLevel    int        `db:"milestone_level" json:"milestone_level"` // 5, 10, 25, 50, 100
	RequiredReferrals int        `db:"required_referrals" json:"required_referrals"`
	CurrentReferrals  int        `db:"current_referrals" json:"current_referrals"`
	RewardType        string     `db:"reward_type" json:"reward_type"`
	RewardAmount      int        `db:"reward_amount" json:"reward_amount"`
	BonusRewardType   *string    `db:"bonus_reward_type" json:"bonus_reward_type,omitempty"`
	BonusRewardAmount *int       `db:"bonus_reward_amount" json:"bonus_reward_amount,omitempty"`
	IsCompleted       bool       `db:"is_completed" json:"is_completed"`
	CompletedAt       *time.Time `db:"completed_at" json:"completed_at,omitempty"`
	IsRewardClaimed   bool       `db:"is_reward_claimed" json:"is_reward_claimed"`
	RewardClaimedAt   *time.Time `db:"reward_claimed_at" json:"reward_claimed_at,omitempty"`
	CreatedAt         time.Time  `db:"created_at" json:"created_at"`
	UpdatedAt         time.Time  `db:"updated_at" json:"updated_at"`
}

// ReferralReward represents a claimed referral reward
type ReferralReward struct {
	ID            uuid.UUID  `db:"id" json:"id"`
	CharacterID   uuid.UUID  `db:"character_id" json:"character_id"`
	ReferralID    *uuid.UUID `db:"referral_id" json:"referral_id,omitempty"`
	RewardType    string     `db:"reward_type" json:"reward_type"`
	RewardAmount  int        `db:"reward_amount" json:"reward_amount"`
	CurrencyType  string     `db:"currency_type" json:"currency_type"`
	ItemID        *uuid.UUID `db:"item_id" json:"item_id,omitempty"`
	Status        string     `db:"status" json:"status