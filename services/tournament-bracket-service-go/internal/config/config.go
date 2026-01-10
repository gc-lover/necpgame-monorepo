// Tournament Bracket Service Configuration
// Issue: #2210
// Agent: Backend Agent
package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all service configuration
type Config struct {
	ServerAddr    string
	DatabaseURL   string
	RedisURL      string
	ReadTimeout   time.Duration
	WriteTimeout  time.Duration
	MaxConnPool   int
	MinConnPool   int
	ConnLifetime  time.Duration
	ConnIdleTime  time.Duration
}

// Load loads configuration from environment variables with defaults
func Load() (*Config, error) {
	config := &Config{
		ServerAddr:    getEnv("SERVER_ADDR", ":8080"),
		DatabaseURL:   getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/tournament?sslmode=disable"),
		RedisURL:      getEnv("REDIS_URL", "redis://localhost:6379"),
		ReadTimeout:   getEnvDuration("READ_TIMEOUT", 30*time.Second),
		WriteTimeout:  getEnvDuration("WRITE_TIMEOUT", 30*time.Second),
		MaxConnPool:   getEnvInt("MAX_CONN_POOL", 25),
		MinConnPool:   getEnvInt("MIN_CONN_POOL", 5),
		ConnLifetime:  getEnvDuration("CONN_LIFETIME", time.Hour),
		ConnIdleTime:  getEnvDuration("CONN_IDLE_TIME", 30*time.Minute),
	}

	return config, nil
}

// getEnv gets environment variable or returns default value
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

// getEnvInt gets environment variable as int or returns default value
func getEnvInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}

// getEnvDuration gets environment variable as duration or returns default value
func getEnvDuration(key string, defaultValue time.Duration) time.Duration {
	if value := os.Getenv(key); value != "" {
		if duration, err := time.ParseDuration(value); err == nil {
			return duration
		}
	}
	return defaultValue
}