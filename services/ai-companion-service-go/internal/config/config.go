// Issue: #2266 - Refactor system-domain AI services
// PERFORMANCE: Optimized configuration with validation and environment handling

package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all configuration for the AI companion service
// PERFORMANCE: Precomputed values to avoid runtime calculations
type Config struct {
	// Database configuration with optimized connection settings
	Database DatabaseConfig

	// Server configuration with performance tunings
	Server ServerConfig

	// AI-specific configuration for companion interactions
	AI AIConfig
}

// DatabaseConfig holds database connection settings
type DatabaseConfig struct {
	Host         string
	Port         int
	User         string
	Password     string
	Database     string
	SSLMode      string

	// PERFORMANCE: Optimized connection pool settings (BLOCKER requirement)
	MaxOpenConns    int           // 50 connections for AI companion service
	MaxIdleConns    int           // 10 idle connections
	ConnMaxLifetime time.Duration // 30 minutes
}

// ServerConfig holds HTTP server settings
type ServerConfig struct {
	Host string
	Port int

	// PERFORMANCE: Timeout settings for AI operations
	ReadTimeout  time.Duration // 30s for AI computations
	WriteTimeout time.Duration // 15s for responses
	IdleTimeout  time.Duration // 120s for companion sessions

	// PERFORMANCE: GC tuning for AI workloads
	GOGC int // 50 for low-latency companion interactions
}

// AIConfig holds AI-specific settings for companion system
type AIConfig struct {
	// PERFORMANCE: Preallocated pools for companion objects
	CompanionPoolSize int // 1000 companions
	MemoryPoolSize    int // 5000 memory entries

	// AI processing timeouts for companion interactions
	InteractionTimeout time.Duration // 3s for real-time responses
	LearningTimeout    time.Duration // 10s for personality updates
	MemoryTimeout      time.Duration // 5s for memory operations

	// Model settings for companion AI
	BatchSize      int // 64 for batch companion processing
	MaxMemories    int // 1000 memories per companion
	PersonalityDim int // 128 personality vector dimension
}

// LoadConfig loads configuration from environment variables with defaults
func LoadConfig() *Config {
	return &Config{
		Database: DatabaseConfig{
			Host:            getEnv("DB_HOST", "localhost"),
			Port:            getEnvInt("DB_PORT", 5432),
			User:            getEnv("DB_USER", "ai_companion"),
			Password:        getEnv("DB_PASSWORD", ""),
			Database:        getEnv("DB_NAME", "ai_companion"),
			SSLMode:         getEnv("DB_SSLMODE", "disable"),
			MaxOpenConns:    getEnvInt("DB_MAX_OPEN_CONNS", 50),
			MaxIdleConns:    getEnvInt("DB_MAX_IDLE_CONNS", 10),
			ConnMaxLifetime: getEnvDuration("DB_CONN_MAX_LIFETIME", 30*time.Minute),
		},
		Server: ServerConfig{
			Host:         getEnv("SERVER_HOST", "0.0.0.0"),
			Port:         getEnvInt("SERVER_PORT", 8080),
			ReadTimeout:  getEnvDuration("SERVER_READ_TIMEOUT", 30*time.Second),
			WriteTimeout: getEnvDuration("SERVER_WRITE_TIMEOUT", 15*time.Second),
			IdleTimeout:  getEnvDuration("SERVER_IDLE_TIMEOUT", 120*time.Second),
			GOGC:         getEnvInt("GOGC", 50),
		},
		AI: AIConfig{
			CompanionPoolSize:  getEnvInt("AI_COMPANION_POOL_SIZE", 1000),
			MemoryPoolSize:     getEnvInt("AI_MEMORY_POOL_SIZE", 5000),
			InteractionTimeout: getEnvDuration("AI_INTERACTION_TIMEOUT", 3*time.Second),
			LearningTimeout:    getEnvDuration("AI_LEARNING_TIMEOUT", 10*time.Second),
			MemoryTimeout:      getEnvDuration("AI_MEMORY_TIMEOUT", 5*time.Second),
			BatchSize:          getEnvInt("AI_BATCH_SIZE", 64),
			MaxMemories:        getEnvInt("AI_MAX_MEMORIES", 1000),
			PersonalityDim:     getEnvInt("AI_PERSONALITY_DIM", 128),
		},
	}
}

// PERFORMANCE: Helper functions for environment variable parsing
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}

func getEnvDuration(key string, defaultValue time.Duration) time.Duration {
	if value := os.Getenv(key); value != "" {
		if duration, err := time.ParseDuration(value); err == nil {
			return duration
		}
	}
	return defaultValue
}
