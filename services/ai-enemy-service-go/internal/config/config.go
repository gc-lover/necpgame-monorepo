package config

import (
	"os"
	"strconv"
)

// Config holds all configuration for the service
type Config struct {
	Port        int
	DatabaseURL string
	RedisURL    string
	Environment string
	ServiceName string
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	port := 8081
	if p := os.Getenv("PORT"); p != "" {
		if parsed, err := strconv.Atoi(p); err == nil {
			port = parsed
		}
	}

	return &Config{
		Port:        port,
		DatabaseURL: getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/necpgame"),
		RedisURL:    getEnv("REDIS_URL", "redis://localhost:6379"),
		Environment: getEnv("ENVIRONMENT", "development"),
		ServiceName: "ai-enemy-service",
	}, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

// Issue: #1861
