package models

import (
	"time"
)

// Season represents a Battle Pass season
type Season struct {
	ID          string    `json:"id" db:"id"`
	Name        string    `json:"name" db:"name"`
	Description string    `json:"description,omitempty" db:"description"`
	StartDate   time.Time `json:"startDate" db:"start_date"`
	EndDate     time.Time `json:"endDate" db:"end_date"`
	MaxLevel    int       `json:"maxLevel" db:"max_level"`
	Status      string    `json:"status" db:"status"` // active, upcoming, ended
	CreatedAt   time.Time `json:"createdAt" db:"created_at"`
	UpdatedAt   time.Time `json:"updatedAt" db:"updated_at"`
}

// SeasonReward represents rewards for a specific level in a season
type SeasonReward struct {
	SeasonID      string  `json:"seasonId" db:"season_id"`
	Level         int     `json:"level" db:"level"`
	FreeRewardID  string  `json:"freeRewardId" db:"free_reward_id"`
	PremiumRewardID *string `json:"premiumRewardId,omitempty" db:"premium_reward_id"`
	XpRequired    int     `json:"xpRequired" db:"xp_required"`
}

// Reward represents a reward that can be claimed
type Reward struct {
	ID          string                 `json:"id" db:"id"`
	Type        string                 `json:"type" db:"type"` // cosmetic, weapon, currency, boost, title, emote
	Name        string                 `json:"name" db:"name"`
	Description string                 `json:"description,omitempty" db:"description"`
	Rarity      string                 `json:"rarity,omitempty" db:"rarity"` // common, rare, epic, legendary
	Metadata    map[string]interface{} `json:"metadata,omitempty" db:"metadata"`
	CreatedAt   time.Time              `json:"createdAt" db:"created_at"`
}

// PlayerProgress represents a player's Battle Pass progress
type PlayerProgress struct {
	PlayerID       string                 `json:"playerId" db:"player_id"`
	SeasonID       string                 `json:"seasonId" db:"season_id"`
	CurrentLevel   int                    `json:"currentLevel" db:"current_level"`
	CurrentXP      int                    `json:"currentXp" db:"current_xp"`
	TotalXP        int                    `json:"totalXp" db:"total_xp"`
	XpToNextLevel  int                    `json:"xpToNextLevel" db:"xp_to_next_level"`
	HasPremium     bool                   `json:"hasPremium" db:"has_premium"`
	LastUpdated    time.Time              `json:"lastUpdated" db:"last_updated"`
}

// ClaimedReward represents a reward that has been claimed by a player
type ClaimedReward struct {
	PlayerID    string    `json:"playerId" db:"player_id"`
	SeasonID    string    `json:"seasonId" db:"season_id"`
	Level       int       `json:"level" db:"level"`
	Tier        string    `json:"tier" db:"tier"` // free, premium
	RewardID    string    `json:"rewardId" db:"reward_id"`
	ClaimedAt   time.Time `json:"claimedAt" db:"claimed_at"`
	InventoryID *string   `json:"inventoryId,omitempty" db:"inventory_id"`
}

// XPGrant represents an XP grant request
type XPGrant struct {
	Amount  int                    `json:"amount"`
	Reason  string                 `json:"reason"`
	Metadata map[string]interface{} `json:"metadata,omitempty"`
}

// XPGrantResult represents the result of an XP grant
type XPGrantResult struct {
	NewLevel     int `json:"newLevel"`
	XPGained     int `json:"xpGained"`
	RewardsUnlocked []Reward `json:"rewardsUnlocked"`
}

// ClaimRequest represents a reward claim request
type ClaimRequest struct {
	Level int    `json:"level"`
	Tier  string `json:"tier"`
}

// ClaimResult represents the result of a reward claim
type ClaimResult struct {
	Success    bool      `json:"success"`
	Reward     Reward    `json:"reward"`
	ClaimedAt  time.Time `json:"claimedAt"`
	InventoryID *string  `json:"inventoryId,omitempty"`
}

// AvailableReward represents a reward available for claiming
type AvailableReward struct {
	Level    int    `json:"level"`
	Tier     string `json:"tier"`
	Reward   Reward `json:"reward"`
	CanClaim bool   `json:"canClaim"`
	Reason   string `json:"reason,omitempty"`
}

// PlayerStatistics represents comprehensive player Battle Pass statistics
type PlayerStatistics struct {
	PlayerID                string               `json:"playerId"`
	SeasonsPlayed           int                  `json:"seasonsPlayed"`
	TotalXPEarned           int                  `json:"totalXpEarned"`
	HighestLevelReached     int                  `json:"highestLevelReached"`
	RewardsClaimed          int                  `json:"rewardsClaimed"`
	PremiumPassesPurchased  int                  `json:"premiumPassesPurchased"`
	FavoriteRewardType      string               `json:"favoriteRewardType"`
	SeasonsData             []SeasonData         `json:"seasonsData"`
}

// SeasonData represents per-season statistics
type SeasonData struct {
	SeasonID       string `json:"seasonId"`
	SeasonName     string `json:"seasonName"`
	FinalLevel     int    `json:"finalLevel"`
	XPEarned       int    `json:"xpEarned"`
	RewardsClaimed int    `json:"rewardsClaimed"`
	HadPremium     bool   `json:"hadPremium"`
}

// PremiumPurchase represents a premium pass purchase
type PremiumPurchase struct {
	PlayerID    string    `json:"playerId" db:"player_id"`
	SeasonID    string    `json:"seasonId" db:"season_id"`
	PurchasedAt time.Time `json:"purchasedAt" db:"purchased_at"`
	Price       int       `json:"price" db:"price"`
	Currency    string    `json:"currency" db:"currency"`
}