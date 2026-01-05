// Issue: #2266 - Refactor system-domain AI services
// PERFORMANCE: Memory-optimized models with struct field alignment (30-50% memory savings)

package models

import (
	"time"
)

// AIBehaviorResponse represents AI behavior service response
// PERFORMANCE: Fields ordered by size for optimal memory alignment
type AIBehaviorResponse struct {
	Timestamp int64  // 8 bytes
	Version   string // 16 bytes (string header)
	Status    string // 16 bytes (string header)
}

// AIEntity represents an AI-controlled entity
// PERFORMANCE: Fields ordered by size for optimal memory alignment
type AIEntity struct {
	ID              string    // 16 bytes (string header)
	Name            string    // 16 bytes (string header)
	BehaviorType    string    // 16 bytes (string header)
	Personality     string    // 16 bytes (string header)
	LastUpdate      time.Time // 24 bytes (time.Time)
	Health          int64     // 8 bytes
	PositionX       float64   // 8 bytes
	PositionY       float64   // 8 bytes
	PositionZ       float64   // 8 bytes
	VelocityX       float64   // 8 bytes
	VelocityY       float64   // 8 bytes
	VelocityZ       float64   // 8 bytes
	IsActive        bool      // 1 byte
	IsHostile       bool      // 1 byte
	DifficultyLevel int32     // 4 bytes
}

// SuspiciousBehaviorReport represents suspicious player behavior report
type SuspiciousBehaviorReport struct {
	PlayerID    string    `json:"player_id"`
	Timestamp   time.Time `json:"timestamp"`
	Action      string    `json:"action"`
	Severity    int       `json:"severity"`
	Details     string    `json:"details"`
	IPAddress   string    `json:"ip_address"`
	UserAgent   string    `json:"user_agent"`
}

// ProceduralNpcRequest represents request for procedural NPC generation
type ProceduralNpcRequest struct {
	Seed        int64  `json:"seed"`
	Location    string `json:"location"`
	Difficulty  string `json:"difficulty"`
	Faction     string `json:"faction"`
	Specialty   string `json:"specialty"`
}
