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
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	port, err := strconv.Atoi(getEnv("PORT", "8086"))
	if err != nil {
		return nil, err
	}

	return &Config{
		Port:        port,
		DatabaseURL: getEnv("DATABASE_URL", "postgres://user:password@localhost/notification_system?sslmode=disable"),
		RedisURL:    getEnv("REDIS_URL", "redis://localhost:6379"),
		Environment: getEnv("ENVIRONMENT", "development"),
	}, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}


