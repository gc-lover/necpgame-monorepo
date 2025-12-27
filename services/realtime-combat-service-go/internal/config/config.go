// Issue: #2232
package config

import (
	"os"
	"strconv"
)

// Config holds all configuration for the service
type Config struct {
	Port         int
	DatabaseURL  string
	RedisURL     string
	JWTSecret    string
	Environment  string
	LogLevel     string
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	port := 8085
	if p := os.Getenv("PORT"); p != "" {
		if parsed, err := strconv.Atoi(p); err == nil {
			port = parsed
		}
	}

	config := &Config{
		Port:         port,
		DatabaseURL:  getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/necpgame?sslmode=disable"),
		RedisURL:     getEnv("REDIS_URL", "redis://localhost:6379"),
		JWTSecret:    getEnv("JWT_SECRET", "your-secret-key"),
		Environment:  getEnv("ENVIRONMENT", "development"),
		LogLevel:     getEnv("LOG_LEVEL", "info"),
	}

	return config, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
