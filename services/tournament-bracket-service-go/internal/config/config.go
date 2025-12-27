// Issue: #2210
package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all configuration for the service
type Config struct {
	Port         int
	DatabaseURL  string
	RedisURL     string
	JWTSecret    string
	Environment  string
	LogLevel     string
	CacheTTL     time.Duration
	MaxBracketSize int
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	port := 8081
	if p := os.Getenv("PORT"); p != "" {
		if parsed, err := strconv.Atoi(p); err == nil {
			port = parsed
		}
	}

	cacheTTL := 30 * time.Minute
	if cttl := os.Getenv("CACHE_TTL"); cttl != "" {
		if parsed, err := time.ParseDuration(cttl); err == nil {
			cacheTTL = parsed
		}
	}

	maxBracketSize := 128
	if mbs := os.Getenv("MAX_BRACKET_SIZE"); mbs != "" {
		if parsed, err := strconv.Atoi(mbs); err == nil && parsed > 0 {
			maxBracketSize = parsed
		}
	}

	config := &Config{
		Port:           port,
		DatabaseURL:    getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/necpgame?sslmode=disable"),
		RedisURL:       getEnv("REDIS_URL", "redis://localhost:6379"),
		JWTSecret:      getEnv("JWT_SECRET", "your-secret-key"),
		Environment:    getEnv("ENVIRONMENT", "development"),
		LogLevel:       getEnv("LOG_LEVEL", "info"),
		CacheTTL:       cacheTTL,
		MaxBracketSize: maxBracketSize,
	}

	return config, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
