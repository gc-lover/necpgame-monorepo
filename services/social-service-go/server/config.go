// Issue: JWT Implementation
// Configuration for social-service
package server

import (
	"os"
	"time"
)

// Config holds service configuration
type Config struct {
	JWTSecret     string
	JWTExpiryHour time.Duration
}

// NewConfig creates new configuration from environment variables
func NewConfig() *Config {
	return &Config{
		JWTSecret:     getEnv("JWT_SECRET", "your-jwt-secret-change-in-production"),
		JWTExpiryHour: time.Hour, // 1 hour default
	}
}

// getEnv gets environment variable or returns default value
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
