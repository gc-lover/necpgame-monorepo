// Issue: #2262 - Cyberspace Easter Eggs Backend Integration
// Configuration management for Easter Eggs Service - Enterprise-grade config

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
	LogLevel     string
	MetricsPort  int
	Environment  string
}

// Load loads configuration from environment variables with defaults
func Load() (*Config, error) {
	config := &Config{
		Port:         getEnvAsInt("PORT", 8080),
		DatabaseURL:  getEnv("DATABASE_URL", "postgres://user:password@localhost/easter_eggs?sslmode=disable"),
		RedisURL:     getEnv("REDIS_URL", "redis://localhost:6379"),
		LogLevel:     getEnv("LOG_LEVEL", "info"),
		MetricsPort:  getEnvAsInt("METRICS_PORT", 9090),
		Environment:  getEnv("ENVIRONMENT", "development"),
	}

	return config, nil
}

// getEnv gets an environment variable with a default value
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

// getEnvAsInt gets an environment variable as integer with a default value
func getEnvAsInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}
