package config

import (
	"os"
	"strconv"
)

type Config struct {
	Port        int
	DatabaseURL string
	RedisURL    string
	Environment string
	ServiceName string
}

func Load() (*Config, error) {
	port := 8083
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
		ServiceName: "interactive-objects-service",
	}, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
