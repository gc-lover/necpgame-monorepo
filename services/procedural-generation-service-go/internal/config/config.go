// Issue: #2266 - Refactor system-domain AI services
// PERFORMANCE: Optimized configuration with validation and environment handling

package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all configuration for the procedural generation service
// PERFORMANCE: Precomputed values to avoid runtime calculations
type Config struct {
	// Database configuration with optimized connection settings
	Database DatabaseConfig

	// Server configuration with performance tunings
	Server ServerConfig

	// Procedural-specific configuration for generation operations
	Procedural ProceduralConfig
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
	MaxOpenConns    int           // 50 connections for procedural service
	MaxIdleConns    int           // 10 idle connections
	ConnMaxLifetime time.Duration // 30 minutes
}

// ServerConfig holds HTTP server settings
type ServerConfig struct {
	Host string
	Port int

	// PERFORMANCE: Timeout settings for procedural operations
	ReadTimeout  time.Duration // 30s for procedural computations
	WriteTimeout time.Duration // 15s for responses
	IdleTimeout  time.Duration // 120s for generation sessions

	// PERFORMANCE: GC tuning for procedural workloads
	GOGC int // 50 for low-latency procedural generation
}

// ProceduralConfig holds procedural-specific settings for generation operations
type ProceduralConfig struct {
	// PERFORMANCE: Preallocated pools for procedural operations
	GeneratorPoolSize int // 1000 generators
	TemplatePoolSize  int // 5000 templates

	// Procedural processing timeouts for generation operations
	GenerationTimeout time.Duration // 10s for content generation
	WorldGenTimeout   time.Duration // 30s for world generation
	BatchTimeout      time.Duration // 60s for batch processing

	// Generation settings for procedural service
	MaxComplexity   float64 // 1.0 maximum complexity
	DefaultQuality  string  // "high" default quality
	MaxWorldSize    int     // 10000 maximum world size
	SeedRange       int64   // 1000000 maximum seed range
}

// LoadConfig loads configuration from environment variables with defaults
func LoadConfig() *Config {
	return &Config{
		Database: DatabaseConfig{
			Host:            getEnv("DB_HOST", "localhost"),
			Port:            getEnvInt("DB_PORT", 5432),
			User:            getEnv("DB_USER", "procedural_service"),
			Password:        getEnv("DB_PASSWORD", ""),
			Database:        getEnv("DB_NAME", "procedural_service"),
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
		Procedural: ProceduralConfig{
			GeneratorPoolSize: getEnvInt("PROCEDURAL_GENERATOR_POOL_SIZE", 1000),
			TemplatePoolSize:  getEnvInt("PROCEDURAL_TEMPLATE_POOL_SIZE", 5000),
			GenerationTimeout: getEnvDuration("PROCEDURAL_GENERATION_TIMEOUT", 10*time.Second),
			WorldGenTimeout:   getEnvDuration("PROCEDURAL_WORLD_GEN_TIMEOUT", 30*time.Second),
			BatchTimeout:      getEnvDuration("PROCEDURAL_BATCH_TIMEOUT", 60*time.Second),
			MaxComplexity:     getEnvFloat("PROCEDURAL_MAX_COMPLEXITY", 1.0),
			DefaultQuality:    getEnv("PROCEDURAL_DEFAULT_QUALITY", "high"),
			MaxWorldSize:      getEnvInt("PROCEDURAL_MAX_WORLD_SIZE", 10000),
			SeedRange:         getEnvInt64("PROCEDURAL_SEED_RANGE", 1000000),
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

func getEnvInt64(key string, defaultValue int64) int64 {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.ParseInt(value, 10, 64); err == nil {
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

func getEnvFloat(key string, defaultValue float64) float64 {
	if value := os.Getenv(key); value != "" {
		if floatValue, err := strconv.ParseFloat(value, 64); err == nil {
			return floatValue
		}
	}
	return defaultValue
}
