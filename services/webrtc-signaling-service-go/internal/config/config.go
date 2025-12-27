package config

import (
	"os"
)

// Config holds all configuration for the WebRTC Signaling Service
type Config struct {
	ServerAddr  string
	DatabaseURL string
	RedisURL    string
	JWTSecret   string
	Environment string
}

// Load loads configuration from environment variables with defaults
func Load() (*Config, error) {
	config := &Config{
		ServerAddr:  getEnv("SERVER_ADDR", ":8080"),
		DatabaseURL: getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/webrtc_signaling?sslmode=disable"),
		RedisURL:    getEnv("REDIS_URL", "redis://localhost:6379"),
		JWTSecret:   getEnv("JWT_SECRET", "your-secret-key-change-in-production"),
		Environment: getEnv("ENVIRONMENT", "development"),
	}

	return config, nil
}

// getEnv gets an environment variable with a fallback value
func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}
