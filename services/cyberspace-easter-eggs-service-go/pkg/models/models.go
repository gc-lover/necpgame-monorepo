// Issue: #2262 - Cyberspace Easter Eggs Backend Integration
// Models for Cyberspace Easter Eggs Service - Enterprise-grade data structures

package models

import (
	"time"
)

// EasterEgg represents a single easter egg in the cyberspace
type EasterEgg struct {
	ID              string            `json:"id" db:"id"`
	Name            string            `json:"name" db:"name"`
	Category        string            `json:"category" db:"category"`               // "technological", "cultural", "historical", "humorous"
	Difficulty      string            `json:"difficulty" db:"difficulty"`           // "easy", "medium", "hard", "legendary"
	Description     string            `json:"description" db:"description"`
	Content         string            `json:"content" db:"content"`                 // Full content/story
	Location        EasterEggLocation `json:"location" db:"location"`               // JSON object
	DiscoveryMethod DiscoveryMethod   `json:"discovery_method" db:"discovery_method"` // JSON object
	Rewards         []EasterEggReward `json:"rewards" db:"rewards"`                 // JSON array
	LoreConnections []string          `json:"lore_connections" db:"lore_connections"` // JSON array
	Status          string            `json:"status" db:"status"`                   // "active", "disabled", "maintenance"
	CreatedAt       time.Time         `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time         `json:"updated_at" db:"updated_at"`
}

// EasterEggLocation represents where the easter egg can be found
type EasterEggLocation struct {
	NetworkType    string   `json:"network_type"`     // "corporate", "public", "underground", "educational"
	SpecificAreas  []string `json:"specific_areas"`   // ["universities", "corporate_rnd", "old_datacenters"]
	Coordinates    []int    `json:"coordinates"`      // [x, y, z] in cyberspace grid
	AccessLevel    string   `json:"access_level"`     // "public", "restricted", "secure", "fort_knox"
	TimeConditions []string `json:"time_conditions"`  // ["night_time", "system_crash", "data_storm"]
}

// DiscoveryMethod describes how to find the easter egg
type DiscoveryMethod struct {
	Type        string            `json:"type"`         // "scanning", "pattern", "puzzle", "event"
	Filters     map[string]string `json:"filters"`      // Filter requirements
	Commands    []string          `json:"commands"`     // Required commands
	Sequence    []string          `json:"sequence"`     // Action sequence
	Hints       []string          `json:"hints"`        // Discovery hints
	TimeLimit   int               `json:"time_limit"`   // Seconds to discover
}

// EasterEggReward represents rewards given for discovering an easter egg
type EasterEggReward struct {
	Type     string `json:"type"`     // "experience", "item", "achievement", "lore", "currency"
	Value    int    `json:"value"`    // Numeric value
	ItemID   string `json:"item_id"`  // For item rewards
	ItemName string `json:"item_name"` // Display name
	Rarity   string `json:"rarity"`   // "common", "rare", "epic", "legendary"
}

// PlayerEasterEggProgress tracks player progress with easter eggs
type PlayerEasterEggProgress struct {
	PlayerID     string    `json:"player_id" db:"player_id"`
	EasterEggID  string    `json:"easter_egg_id" db:"easter_egg_id"`
	Status       string    `json:"status" db:"status"`             // "undiscovered", "discovered", "completed", "revisited"
	DiscoveredAt *time.Time `json:"discovered_at" db:"discovered_at"`
	CompletedAt  *time.Time `json:"completed_at" db:"completed_at"`
	RewardsClaimed []string `json:"rewards_claimed" db:"rewards_claimed"` // JSON array of claimed reward IDs
	HintLevel    int       `json:"hint_level" db:"hint_level"`     // 0 = no hints, 1-3 = hint levels
	VisitCount   int       `json:"visit_count" db:"visit_count"`
	LastVisited  time.Time `json:"last_visited" db:"last_visited"`
}

// EasterEggDiscoveryAttempt tracks player attempts to discover easter eggs
type EasterEggDiscoveryAttempt struct {
	ID              string    `json:"id" db:"id"`
	PlayerID        string    `json:"player_id" db:"player_id"`
	EasterEggID     string    `json:"easter_egg_id" db:"easter_egg_id"`
	AttemptType     string    `json:"attempt_type" db:"attempt_type"`     // "scan", "command", "puzzle", "event"
	AttemptData     string    `json:"attempt_data" db:"attempt_data"`     // JSON data of the attempt
	Success         bool      `json:"success" db:"success"`
	AttemptedAt     time.Time `json:"attempted_at" db:"attempted_at"`
	ResponseTime    int       `json:"response_time" db:"response_time"`   // milliseconds
	IPAddress       string    `json:"ip_address" db:"ip_address"`
	UserAgent       string    `json:"user_agent" db:"user_agent"`
}

// EasterEggStatistics tracks global easter egg statistics
type EasterEggStatistics struct {
	EasterEggID         string    `json:"easter_egg_id" db:"easter_egg_id"`
	TotalDiscoveries    int       `json:"total_discoveries" db:"total_discoveries"`
	UniquePlayers       int       `json:"unique_players" db:"unique_players"`
	AverageDiscoveryTime int      `json:"average_discovery_time" db:"average_discovery_time"` // seconds
	SuccessRate         float64   `json:"success_rate" db:"success_rate"`                     // 0-1
	PopularDiscoveryMethod string `json:"popular_discovery_method" db:"popular_discovery_method"`
	LastUpdated         time.Time `json:"last_updated" db:"last_updated"`
}

// DiscoveryHint represents a hint for discovering easter eggs
type DiscoveryHint struct {
	ID           string   `json:"id" db:"id"`
	EasterEggID  string   `json:"easter_egg_id" db:"easter_egg_id"`
	HintLevel    int      `json:"hint_level" db:"hint_level"`     // 1-3 (increasing specificity)
	HintText     string   `json:"hint_text" db:"hint_text"`
	HintType     string   `json:"hint_type" db:"hint_type"`       // "direct", "indirect", "misleading"
	Cost         int      `json:"cost" db:"cost"`                 // Cost in some currency
	IsEnabled    bool     `json:"is_enabled" db:"is_enabled"`
	CreatedAt    time.Time `json:"created_at" db:"created_at"`
}

// EasterEggEvent represents events triggered by easter egg interactions
type EasterEggEvent struct {
	ID          string                 `json:"id" db:"id"`
	EventType   string                 `json:"event_type" db:"event_type"`     // "discovered", "completed", "revisited", "hint_used"
	PlayerID    string                 `json:"player_id" db:"player_id"`
	EasterEggID string                 `json:"easter_egg_id" db:"easter_egg_id"`
	EventData   map[string]interface{} `json:"event_data" db:"event_data"`     // JSON event data
	CreatedAt   time.Time              `json:"created_at" db:"created_at"`
	Processed   bool                   `json:"processed" db:"processed"`
}

// EasterEggCategoryStats provides statistics by category
type EasterEggCategoryStats struct {
	Category           string  `json:"category"`
	TotalEasterEggs    int     `json:"total_easter_eggs"`
	DiscoveredCount    int     `json:"discovered_count"`
	DiscoveryRate      float64 `json:"discovery_rate"`      // percentage
	AverageDifficulty  float64 `json:"average_difficulty"`  // 1-4 scale
	MostPopularEgg     string  `json:"most_popular_egg"`
	LeastPopularEgg    string  `json:"least_popular_egg"`
}

// PlayerEasterEggProfile tracks overall player engagement with easter eggs
type PlayerEasterEggProfile struct {
	PlayerID           string    `json:"player_id" db:"player_id"`
	TotalDiscovered    int       `json:"total_discovered" db:"total_discovered"`
	TotalCompleted     int       `json:"total_completed" db:"total_completed"`
	FavoriteCategory   string    `json:"favorite_category" db:"favorite_category"`
	AverageDifficulty  float64   `json:"average_difficulty" db:"average_difficulty"`
	TotalHintsUsed     int       `json:"total_hints_used" db:"total_hints_used"`
	CollectionProgress float64   `json:"collection_progress" db:"collection_progress"` // percentage 0-100
	AchievementLevel   string    `json:"achievement_level" db:"achievement_level"`     // "explorer", "hunter", "master", "legend"
	LastActivity       time.Time `json:"last_activity" db:"last_activity"`
	CreatedAt          time.Time `json:"created_at" db:"created_at"`
	UpdatedAt          time.Time `json:"updated_at" db:"updated_at"`
}

// EasterEggChallenge represents time-limited easter egg challenges
type EasterEggChallenge struct {
	ID          string    `json:"id" db:"id"`
	Title       string    `json:"title" db:"title"`
	Description string    `json:"description" db:"description"`
	EasterEggs  []string  `json:"easter_eggs" db:"easter_eggs"` // Easter egg IDs to discover
	Rewards     []EasterEggReward `json:"rewards" db:"rewards"` // Challenge completion rewards
	StartTime   time.Time `json:"start_time" db:"start_time"`
	EndTime     time.Time `json:"end_time" db:"end_time"`
	IsActive    bool      `json:"is_active" db:"is_active"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
}
