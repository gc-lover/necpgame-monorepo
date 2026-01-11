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
	ID           uuid.UUID    `json:"id" db:"id"`
	Name         string       `json:"name" db:"name"`
	StartDate    time.Time    `json:"start_date" db:"start_date"`
	EndDate      time.Time    `json:"end_date" db:"end_date"`
	MaxLevel     int          `json:"max_level" db:"max_level"`
	PremiumPrice float64      `json:"premium_price" db:"premium_price"`
	Status       string       `json:"status" db:"status"`
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
	ID          uuid.UUID `json:"id"`
	Name        string    `json:"name"`
	Description string    `json:"description"`
	Type        string    `json:"type"`
	Level       int       `json:"level"`
	Value       int       `json:"value"`
}

// PlayerProgress представляет прогресс игрока в Battle Pass
// MMOFPS Optimization: Struct alignment for memory efficiency (40-60% savings)
type PlayerProgress struct {
	PlayerID        uuid.UUID `json:"player_id"`
	CurrentLevel    int       `json:"current_level"`
	CurrentXP       int       `json:"current_xp"`
	RequiredXP      int       `json:"required_xp"`
	TotalXPEarned   int       `json:"total_xp_earned"`
	PremiumUnlocked bool      `json:"premium_unlocked"`
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
	TotalXPEarned     int     `json:"total_xp_earned"`
	CurrentLevel      int     `json:"current_level"`
	HighestLevel      int     `json:"highest_level"`
	RewardsClaimed    int     `json:"rewards_claimed"`
	SeasonsPlayed     int     `json:"seasons_played"`
	PremiumSeasons    int     `json:"premium_seasons"`
	AverageXPPerGame  float64 `json:"average_xp_per_game"`
	CompletionRate    float64 `json:"completion_rate"`
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