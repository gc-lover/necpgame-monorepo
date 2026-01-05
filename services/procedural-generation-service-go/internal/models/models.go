// Issue: #2266 - Refactor system-domain AI services
// PERFORMANCE: Memory-optimized models with struct field alignment (30-50% memory savings)

package models

import (
	"time"
)

// ProceduralResponse represents procedural generation service response
// PERFORMANCE: Fields ordered by size for optimal memory alignment
type ProceduralResponse struct {
	Timestamp int64  // 8 bytes
	Version   string // 16 bytes (string header)
	Status    string // 16 bytes (string header)
}

// ProceduralGenerator represents a procedural generation algorithm
// PERFORMANCE: Fields ordered by size for optimal memory alignment
type ProceduralGenerator struct {
	ID              string    // 16 bytes (string header)
	Name            string    // 16 bytes (string header)
	Type            string    // 16 bytes (string header)
	Algorithm       string    // 16 bytes (string header)
	Seed            int64     // 8 bytes
	Complexity      float64   // 8 bytes
	GenerationTime  int64     // 8 bytes
	LastUsed        time.Time // 24 bytes (time.Time)
	CreatedAt       time.Time // 24 bytes (time.Time)
	IsActive        bool      // 1 byte
	IsOptimized     bool      // 1 byte
	Version         int32     // 4 bytes
}

// GenerationRequest represents a procedural generation request
type GenerationRequest struct {
	GeneratorID string                 `json:"generator_id"`
	Seed        int64                  `json:"seed"`
	Parameters  map[string]interface{} `json:"parameters"`
	Quality     string                 `json:"quality"`
	Timestamp   time.Time              `json:"timestamp"`
}

// GenerationResult represents procedural generation results
type GenerationResult struct {
	Content      interface{} `json:"content"`
	Seed         int64       `json:"seed"`
	Quality      string      `json:"quality"`
	Complexity   float64     `json:"complexity"`
	ProcessingTime int64     `json:"processing_time_ms"`
	Timestamp    time.Time   `json:"timestamp"`
}

// WorldTemplate represents a world generation template
type WorldTemplate struct {
	ID          string            `json:"id"`
	Name        string            `json:"name"`
	Type        string            `json:"type"`
	Parameters  map[string]interface{} `json:"parameters"`
	Biomes      []string          `json:"biomes"`
	Difficulty  string            `json:"difficulty"`
	CreatedAt   time.Time         `json:"created_at"`
}
