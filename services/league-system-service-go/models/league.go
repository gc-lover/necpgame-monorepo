// Package models Issue: #???
package models

import (
	"time"

	"github.com/google/uuid"
)

// League represents a seasonal league
type League struct {
	ID               uuid.UUID    `json:"id" db:"id"`
	Name             string       `json:"name" db:"name"`
	StartDate        time.Time    `json:"start_date" db:"start_date"`
	EndDate          time.Time    `json:"end_date" db:"end_date"`
	Status           LeagueStatus `json:"status" db:"status"`
	Phase            LeaguePhase  `json:"phase" db:"phase"`
	Seed             int64        `json:"seed" db:"seed"`
	TimeAcceleration float64      `json:"time_acceleration" db:"time_acceleration"`
	PlayerCount      int          `json:"player_count" db:"player_count"`
	CreatedAt        time.Time    `json:"created_at" db:"created_at"`
	UpdatedAt        time.Time    `json:"updated_at" db:"updated_at"`
}

// LeagueStatus represents the status of a league
type LeagueStatus string

const (
	LeagueStatusPlanned   LeagueStatus = "PLANNED"
	LeagueStatusActive    LeagueStatus = "ACTIVE"
	LeagueStatusFinishing LeagueStatus = "FINISHING"
	LeagueStatusCompleted LeagueStatus = "COMPLETED"
)

// LeaguePhase represents the current phase of a league
type LeaguePhase struct {
	Name             string    `json:"name"`
	StartDate        time.Time `json:"start_date"`
	EndDate          time.Time `json:"end_date"`
	Description      string    `json:"description"`
	TimeAcceleration float64   `json:"time_acceleration"`
}

// LeagueCountdown represents time remaining until league end
type LeagueCountdown struct {
	TotalSeconds    int    `json:"total_seconds"`
	Days            int    `json:"days"`
	Hours           int    `json:"hours"`
	Minutes         int    `json:"minutes"`
	Seconds         int    `json:"seconds"`
	PhaseEndSeconds int    `json:"phase_end_seconds,omitempty"`
	NextPhase       string `json:"next_phase,omitempty"`
}

// LeaguePhases represents all phases of a league
type LeaguePhases struct {
	CurrentPhase     LeaguePhase   `json:"current_phase"`
	Phases           []LeaguePhase `json:"phases"`
	TimeAcceleration float64       `json:"time_acceleration"`
}

// LeagueStatistics represents statistics for a completed league
type LeagueStatistics struct {
	LeagueID             uuid.UUID      `json:"league_id"`
	PlayerCount          int            `json:"player_count"`
	CompletionRate       float64        `json:"completion_rate"`
	AverageLevel         float64        `json:"average_level"`
	TotalQuestsCompleted int64          `json:"total_quests_completed"`
	TotalEconomyValue    int64          `json:"total_economy_value"`
	TopPlayers           []TopPlayer    `json:"top_players"`
	FactionDistribution  map[string]int `json:"faction_distribution"`
	EndingDistribution   map[string]int `json:"ending_distribution"`
}

// TopPlayer represents a top player in league statistics
type TopPlayer struct {
	PlayerID   uuid.UUID           `json:"player_id"`
	PlayerName string              `json:"player_name"`
	Score      float64             `json:"score"`
	Category   AchievementCategory `json:"category"`
	Rank       int                 `json:"rank"`
}

// AchievementCategory represents categories in Hall of Fame
type AchievementCategory string

const (
	CategoryStoryCompletion  AchievementCategory = "STORY_COMPLETION"
	CategoryEconomy          AchievementCategory = "ECONOMY"
	CategoryPVP              AchievementCategory = "PVP"
	CategoryAlternativeModes AchievementCategory = "ALTERNATIVE_MODES"
)

// HallOfFameCategory represents categories in Hall of Fame (alias for AchievementCategory)
type HallOfFameCategory = AchievementCategory

// ResetType represents types of global resets
type ResetType string

// PlayerLegacyProgressItem represents an item in player's legacy progress (alias for LegacyItem)
type PlayerLegacyProgressItem = LegacyItem

// PlayerLegacyProgress represents a player's meta-progression
type PlayerLegacyProgress struct {
	PlayerID      uuid.UUID       `json:"player_id"`
	LegacyPoints  int             `json:"legacy_points"`
	Titles        []Title         `json:"titles"`
	Cosmetics     []Cosmetic      `json:"cosmetics"`
	LegacyItems   []LegacyItem    `json:"legacy_items"`
	GlobalRating  float64         `json:"global_rating"`
	RatingHistory []RatingHistory `json:"rating_history"`
	Achievements  []string        `json:"achievements"`
}

// RatingHistory represents rating change over time
type RatingHistory struct {
	LeagueID uuid.UUID `json:"league_id"`
	Rating   float64   `json:"rating"`
	Date     time.Time `json:"date"`
}

// Title represents an unlocked title
type Title struct {
	ID          uuid.UUID   `json:"id"`
	Name        string      `json:"name"`
	Description string      `json:"description"`
	Rarity      RarityLevel `json:"rarity"`
	UnlockedAt  time.Time   `json:"unlocked_at"`
	IsActive    bool        `json:"is_active"`
}

// RarityLevel represents rarity levels
type RarityLevel string

const (
	RarityLegendary RarityLevel = "LEGENDARY"
)

// Cosmetic represents cosmetic items
type Cosmetic struct {
	ID         uuid.UUID    `json:"id"`
	Name       string       `json:"name"`
	Type       CosmeticType `json:"type"`
	Rarity     RarityLevel  `json:"rarity"`
	UnlockedAt time.Time    `json:"unlocked_at"`
	IsEquipped bool         `json:"is_equipped"`
}

// CosmeticType represents types of cosmetics
type CosmeticType string

// LegacyItem represents powerful items for league start
type LegacyItem struct {
	ID         uuid.UUID          `json:"id"`
	Name       string             `json:"name"`
	Type       LegacyItemType     `json:"type"`
	PowerLevel float64            `json:"power_level"` // 0.0 to 1.0, decreases over league
	Effects    []LegacyItemEffect `json:"effects"`
	AcquiredAt time.Time          `json:"acquired_at"`
	ExpiresAt  time.Time          `json:"expires_at"`
}

// LegacyItemType represents types of legacy items
type LegacyItemType string

// LegacyItemEffect represents an effect of a legacy item
type LegacyItemEffect struct {
	Type  string  `json:"type"`
	Value float64 `json:"value"`
}

// HallOfFame represents Hall of Fame for a league
type HallOfFame struct {
	LeagueID   uuid.UUID         `json:"league_id"`
	LeagueName string            `json:"league_name"`
	Entries    []HallOfFameEntry `json:"entries"`
}

// HallOfFameEntry represents an entry in Hall of Fame
type HallOfFameEntry struct {
	PlayerID       uuid.UUID           `json:"player_id"`
	PlayerName     string              `json:"player_name"`
	Category       AchievementCategory `json:"category"`
	Achievement    string              `json:"achievement"`
	Date           time.Time           `json:"date"`
	Rank           int                 `json:"rank"`
	RewardCosmetic string              `json:"reward_cosmetic,omitempty"`
}

// LegacyShopItem represents an item available in Legacy Shop
type LegacyShopItem struct {
	ID          uuid.UUID  `json:"id"`
	Name        string     `json:"name"`
	Description string     `json:"description"`
	Type        string     `json:"type"` // TITLE, COSMETIC, LEGACY_ITEM
	Cost        int        `json:"cost"`
	Available   bool       `json:"available"`
	LimitedTime bool       `json:"limited_time"`
	ExpiresAt   *time.Time `json:"expires_at,omitempty"`
}
