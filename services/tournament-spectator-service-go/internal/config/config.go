// Issue: #2213 - Tournament Spectator Mode Implementation
// Configuration management for Tournament Spectator Service - Enterprise-grade config

package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all service configuration
type Config struct {
	ServerPort     int
	DatabaseURL    string
	RedisURL       string
	LogLevel       string
	SpectatorMode  SpectatorConfig
	Performance    PerformanceConfig
}

// SpectatorConfig holds spectator-specific configuration
type SpectatorConfig struct {
	MaxConcurrentSpectators int
	SpectatorTimeout        time.Duration
	UpdateInterval          time.Duration
	BufferSize              int
	EnableRecording         bool
}

// PerformanceConfig holds performance tuning configuration
type PerformanceConfig struct {
	GOGC           int
	GOMAXPROCS     int
	MaxConnections int
	ReadTimeout    time.Duration
	WriteTimeout   time.Duration
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	cfg := &Config{
		ServerPort:  getEnvAsInt("SERVER_PORT", 8087),
		DatabaseURL: getEnv("DATABASE_URL", "postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable"),
		RedisURL:    getEnv("REDIS_URL", "redis://localhost:6379"),
		LogLevel:    getEnv("LOG_LEVEL", "info"),
		SpectatorMode: SpectatorConfig{
			MaxConcurrentSpectators: getEnvAsInt("MAX_CONCURRENT_SPECTATORS", 1000),
			SpectatorTimeout:        getEnvAsDuration("SPECTATOR_TIMEOUT", 30*time.Minute),
			UpdateInterval:          getEnvAsDuration("SPECTATOR_UPDATE_INTERVAL", 1*time.Second),
			BufferSize:              getEnvAsInt("SPECTATOR_BUFFER_SIZE", 100),
			EnableRecording:         getEnvAsBool("ENABLE_SPECTATOR_RECORDING", true),
		},
		Performance: PerformanceConfig{
			GOGC:           getEnvAsInt("GOGC", 75),
			GOMAXPROCS:     getEnvAsInt("GOMAXPROCS", 4),
			MaxConnections: getEnvAsInt("MAX_CONNECTIONS", 10000),
			ReadTimeout:    getEnvAsDuration("READ_TIMEOUT", 30*time.Second),
			WriteTimeout:   getEnvAsDuration("WRITE_TIMEOUT", 30*time.Second),
		},
	}

	return cfg, nil
}

// Helper functions for environment variable parsing
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvAsInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}

func getEnvAsBool(key string, defaultValue bool) bool {
	if value := os.Getenv(key); value != "" {
		if boolValue, err := strconv.ParseBool(value); err == nil {
			return boolValue
		}
	}
	return defaultValue
}

func getEnvAsDuration(key string, defaultValue time.Duration) time.Duration {
	if value := os.Getenv(key); value != "" {
		if duration, err := time.ParseDuration(value); err == nil {
			return duration
		}
	}
	return defaultValue
}
