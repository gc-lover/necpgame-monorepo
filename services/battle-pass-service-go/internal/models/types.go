package models

import (
	"time"

	"github.com/google/uuid"
)

// SeasonStatus представляет статус сезона Battle Pass
type SeasonStatus string

const (
	SeasonStatusUpcoming SeasonStatus = "upcoming"
	SeasonStatusActive   SeasonStatus = "active"
	SeasonStatusEnded    SeasonStatus = "ended"
)

// RewardTier представляет уровень награды (free/premium)
type RewardTier string

const (
	RewardTierFree    RewardTier = "free"
	RewardTierPremium RewardTier = "premium"
)

// Season представляет сезон Battle Pass
type Season struct {
	ID          uuid.UUID    `json:"id" db:"id"`
	Name        string       `json:"name" db:"name"`
	Description string       `json:"description" db:"description"`
	StartDate   time.Time    `json:"startDate" db:"start_date"`
	EndDate     time.Time    `json:"endDate" db:"end_date"`
	MaxLevel    int          `json:"maxLevel" db:"max_level"`
	Status      SeasonStatus `json:"status" db:"status"`
	IsActive    bool         `json:"isActive" db:"is_active"`
	CreatedAt   time.Time    `json:"createdAt" db:"created_at"`
	UpdatedAt   time.Time    `json:"updatedAt" db:"updated_at"`
}

// SeasonReward представляет награду сезона на определенном уровне
type SeasonReward struct {
	ID         uuid.UUID   `json:"id" db:"id"`
	SeasonID   uuid.UUID   `json:"seasonId" db:"season_id"`
	Level      int         `json:"level" db:"level"`
	Tier       RewardTier  `json:"tier" db:"tier"`
	RewardID   uuid.UUID   `json:"rewardId" db:"reward_id"`
	Reward     Reward      `json:"reward" db:"reward"`
	IsClaimed  bool        `json:"isClaimed" db:"is_claimed"`
	CreatedAt  time.Time   `json:"createdAt" db:"created_at"`
	UpdatedAt  time.Time   `json:"updatedAt" db:"updated_at"`
}

// Reward представляет награду Battle Pass
type Reward struct {
	ID          uuid.UUID `json:"id" db:"id"`
	Name        string    `json:"name" db:"name"`
	Description string    `json:"description" db:"description"`
	Type        string    `json:"type" db:"type"`        // "item", "currency", "cosmetic", etc.
	ItemID      *uuid.UUID `json:"itemId,omitempty" db:"item_id"`
	Amount      int       `json:"amount" db:"amount"`
	Rarity      string    `json:"rarity" db:"rarity"`
	IsActive    bool      `json:"isActive" db:"is_active"`
	CreatedAt   time.Time `json:"createdAt" db:"created_at"`
	UpdatedAt   time.Time `json:"updatedAt" db:"updated_at"`
}

// PlayerProgress представляет прогресс игрока в Battle Pass
type PlayerProgress struct {
	ID               uuid.UUID `json:"id" db:"id"`
	PlayerID         uuid.UUID `json:"playerId" db:"player_id"`
	SeasonID         uuid.UUID `json:"seasonId" db:"season_id"`
	CurrentLevel     int       `json:"currentLevel" db:"current_level"`
	CurrentXP        int       `json:"currentXp" db:"current_xp"`
	TotalXP          int       `json:"totalXp" db:"total_xp"`
	PremiumPurchased bool      `json:"premiumPurchased" db:"premium_purchased"`
	LastActivity     time.Time `json:"lastActivity" db:"last_activity"`
	CreatedAt        time.Time `json:"createdAt" db:"created_at"`
	UpdatedAt        time.Time `json:"updatedAt" db:"updated_at"`
}

// AvailableReward представляет доступную награду для игрока
type AvailableReward struct {
	Level      int         `json:"level"`
	Tier       RewardTier  `json:"tier"`
	Reward     Reward      `json:"reward"`
	IsClaimed  bool        `json:"isClaimed"`
	CanClaim   bool        `json:"canClaim"`
	ClaimedAt  *time.Time  `json:"claimedAt,omitempty"`
}

// ClaimResult представляет результат попытки получения награды
type ClaimResult struct {
	Success      bool        `json:"success"`
	Reward       *Reward     `json:"reward,omitempty"`
	ErrorMessage string      `json:"errorMessage,omitempty"`
	ClaimedAt    *time.Time  `json:"claimedAt,omitempty"`
}

// PlayerStatistics представляет статистику игрока по Battle Pass
type PlayerStatistics struct {
	PlayerID            uuid.UUID `json:"playerId"`
	TotalXP             int       `json:"totalXp"`
	SeasonsCompleted    int       `json:"seasonsCompleted"`
	RewardsClaimed      int       `json:"rewardsClaimed"`
	PremiumSeasons      int       `json:"premiumSeasons"`
	AverageCompletion   float64   `json:"averageCompletion"`
	LongestStreak       int       `json:"longestStreak"`
	FavoriteRewardType  string    `json:"favoriteRewardType"`
	FirstSeasonDate     *time.Time `json:"firstSeasonDate,omitempty"`
	LastSeasonDate      *time.Time `json:"lastSeasonDate,omitempty"`
}

// XPGrant представляет начисление XP игроку
type XPGrant struct {
	PlayerID uuid.UUID `json:"playerId"`
	SeasonID uuid.UUID `json:"seasonId"`
	Amount   int       `json:"amount"`
	Reason   string    `json:"reason"`
	Source   string    `json:"source"`
	Metadata map[string]interface{} `json:"metadata,omitempty"`
}