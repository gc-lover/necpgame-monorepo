// Issue: #2266 - Refactor system-domain AI services
// PERFORMANCE: Optimized configuration with validation and environment handling

package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all configuration for the AI behavior service
// PERFORMANCE: Precomputed values to avoid runtime calculations
type Config struct {
	// Database configuration with optimized connection settings
	Database DatabaseConfig

	// Server configuration with performance tunings
	Server ServerConfig

	// AI-specific configuration
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
	MaxOpenConns    int           // 50 connections for AI service
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
	IdleTimeout  time.Duration // 120s for AI sessions

	// PERFORMANCE: GC tuning for AI workloads
	GOGC int // 50 for low-latency AI decisions
}

// AIConfig holds AI-specific settings
type AIConfig struct {
	// PERFORMANCE: Preallocated pools
	ResponsePoolSize int // 1000 responses
	EntityPoolSize   int // 500 entities

	// AI processing timeouts
	DecisionTimeout time.Duration // 5s for behavior decisions
	AnalysisTimeout time.Duration // 10s for pattern analysis
	GenerationTimeout time.Duration // 15s for procedural generation

	// Model settings
	BatchSize int // 32 for batch processing
}

// LoadConfig loads configuration from environment variables with defaults
func LoadConfig() *Config {
	return &Config{
		Database: DatabaseConfig{
			Host:            getEnv("DB_HOST", "localhost"),
			Port:            getEnvInt("DB_PORT", 5432),
			User:            getEnv("DB_USER", "ai_behavior"),
			Password:        getEnv("DB_PASSWORD", ""),
			Database:        getEnv("DB_NAME", "ai_behavior"),
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
			ResponsePoolSize:   getEnvInt("AI_RESPONSE_POOL_SIZE", 1000),
			EntityPoolSize:     getEnvInt("AI_ENTITY_POOL_SIZE", 500),
			DecisionTimeout:    getEnvDuration("AI_DECISION_TIMEOUT", 5*time.Second),
			AnalysisTimeout:    getEnvDuration("AI_ANALYSIS_TIMEOUT", 10*time.Second),
			GenerationTimeout:  getEnvDuration("AI_GENERATION_TIMEOUT", 15*time.Second),
			BatchSize:          getEnvInt("AI_BATCH_SIZE", 32),
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
