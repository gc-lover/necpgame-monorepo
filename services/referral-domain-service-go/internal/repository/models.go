package repository

import (
	"time"

	"github.com/google/uuid"
)

// ReferralCode represents a referral code
type ReferralCode struct {
	ID          uuid.UUID  `db:"id" json:"id"`
	Code        string     `db:"code" json:"code"`
	OwnerID     uuid.UUID  `db:"owner_id" json:"owner_id"`
	IsActive    bool       `db:"is_active" json:"is_active"`
	ExpiresAt   *time.Time `db:"expires_at" json:"expires_at,omitempty"`
	MaxUses     *int       `db:"max_uses" json:"max_uses,omitempty"`
	CurrentUses int        `db:"current_uses" json:"current_uses"`
	CreatedAt   time.Time  `db:"created_at" json:"created_at"`
	UpdatedAt   time.Time  `db:"updated_at" json:"updated_at"`
}

// ReferralRegistration represents a referral registration
type ReferralRegistration struct {
	ID              uuid.UUID  `db:"id" json:"id"`
	ReferrerID      uuid.UUID  `db:"referrer_id" json:"referrer_id"`
	RefereeID       uuid.UUID  `db:"referee_id" json:"referee_id"`
	ReferralCodeID  uuid.UUID  `db:"referral_code_id" json:"referral_code_id"`
	Status          string     `db:"status" json:"status"` // pending, converted, cancelled
	RegisteredAt    time.Time  `db:"registered_at" json:"registered_at"`
	ConvertedAt     *time.Time `db:"converted_at" json:"converted_at,omitempty"`
	CreatedAt       time.Time  `db:"created_at" json:"created_at"`
	UpdatedAt       time.Time  `db:"updated_at" json:"updated_at"`
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
	ID          uuid.UUID `db:"id" json:"id"`
	Name        string    `db:"name" json:"name"`
	Description string    `db:"description" json:"description"`
	Threshold   int       `db:"threshold" json:"threshold"` // number of referrals needed
	RewardType  string    `db:"reward_type" json:"reward_type"`
	RewardValue float64   `db:"reward_value" json:"reward_value"`
	IsActive    bool      `db:"is_active" json:"is_active"`
	CreatedAt   time.Time `db:"created_at" json:"created_at"`
	UpdatedAt   time.Time `db:"updated_at" json:"updated_at"`
}

// ReferralReward represents a claimed referral reward
type ReferralReward struct {
	ID            uuid.UUID  `db:"id" json:"id"`
	UserID        uuid.UUID  `db:"user_id" json:"user_id"`
	MilestoneID   uuid.UUID  `db:"milestone_id" json:"milestone_id"`
	Amount        float64    `db:"amount" json:"amount"`
	Status        string     `db:"status" json:"status"` // pending, claimed, expired
	ClaimedAt     *time.Time `db:"claimed_at" json:"claimed_at,omitempty"`
	CreatedAt     time.Time  `db:"created_at" json:"created_at"`
	UpdatedAt     time.Time  `db:"updated_at" json:"updated_at"`
}
