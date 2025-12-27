// Issue: #2250
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
	StatsTTL     time.Duration
	CacheSize    int
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	port := 8080
	if p := os.Getenv("PORT"); p != "" {
		if parsed, err := strconv.Atoi(p); err == nil {
			port = parsed
		}
	}

	cacheSize := 10000
	if cs := os.Getenv("CACHE_SIZE"); cs != "" {
		if parsed, err := strconv.Atoi(cs); err == nil {
			cacheSize = parsed
		}
	}

	statsTTL := 24 * time.Hour
	if sttl := os.Getenv("STATS_TTL"); sttl != "" {
		if parsed, err := time.ParseDuration(sttl); err == nil {
			statsTTL = parsed
		}
	}

	config := &Config{
		Port:         port,
		DatabaseURL:  getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/necpgame?sslmode=disable"),
		RedisURL:     getEnv("REDIS_URL", "redis://localhost:6379"),
		JWTSecret:    getEnv("JWT_SECRET", "your-secret-key"),
		Environment:  getEnv("ENVIRONMENT", "development"),
		LogLevel:     getEnv("LOG_LEVEL", "info"),
		StatsTTL:     statsTTL,
		CacheSize:    cacheSize,
	}

	return config, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
