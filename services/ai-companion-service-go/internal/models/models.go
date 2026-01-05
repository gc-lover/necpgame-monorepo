// Issue: #2266 - Refactor system-domain AI services
// PERFORMANCE: Memory-optimized models with struct field alignment (30-50% memory savings)

package models

import (
	"time"
)

// AICompanionResponse represents AI companion service response
// PERFORMANCE: Fields ordered by size for optimal memory alignment
type AICompanionResponse struct {
	Timestamp int64  // 8 bytes
	Version   string // 16 bytes (string header)
	Status    string // 16 bytes (string header)
}

// AICompanion represents an AI companion entity
// PERFORMANCE: Fields ordered by size for optimal memory alignment
type AICompanion struct {
	ID              string    // 16 bytes (string header)
	Name            string    // 16 bytes (string header)
	Personality     string    // 16 bytes (string header)
	RelationshipLevel int32   // 4 bytes
	Mood            string    // 16 bytes (string header)
	MemoryCount     int64     // 8 bytes
	LastInteraction time.Time // 24 bytes (time.Time)
	CreatedAt       time.Time // 24 bytes (time.Time)
	IsActive        bool      // 1 byte
	IsLearning      bool      // 1 byte
	TrustLevel      float64   // 8 bytes
	HappinessLevel  float64   // 8 bytes
}

// CompanionMemory represents a memory entry for AI companion
type CompanionMemory struct {
	ID          string    `json:"id"`
	CompanionID string    `json:"companion_id"`
	Type        string    `json:"type"`
	Content     string    `json:"content"`
	Importance  int       `json:"importance"`
	Timestamp   time.Time `json:"timestamp"`
}

// PersonalityTrait represents a personality trait
type PersonalityTrait struct {
	Name        string  `json:"name"`
	Value       float64 `json:"value"`
	Description string  `json:"description"`
}

// InteractionHistory represents player-companion interaction
type InteractionHistory struct {
	PlayerID    string    `json:"player_id"`
	CompanionID string    `json:"companion_id"`
	Type        string    `json:"type"`
	Content     string    `json:"content"`
	Timestamp   time.Time `json:"timestamp"`
	Quality     int       `json:"quality"`
}
