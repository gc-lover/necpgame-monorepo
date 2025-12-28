package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all configuration for the Referral Domain Service
type Config struct {
	ServerAddr       string
	DatabaseURL      string
	RedisURL         string
	JWTSecret        string
	Environment      string

	// MMOFPS RPG Optimizations
	DBMaxOpenConns   int
	DBMaxIdleConns   int
	DBConnMaxLifetime time.Duration
	RedisPoolSize    int
	RequestTimeout   time.Duration
	ShutdownTimeout  time.Duration
}

// Load loads configuration from environment variables with MMOFPS RPG optimizations
func Load() (*Config, error) {
	// Parse MMOFPS-optimized database settings
	dbMaxOpenConns, _ := strconv.Atoi(getEnv("DB_MAX_OPEN_CONNS", "100"))
	dbMaxIdleConns, _ := strconv.Atoi(getEnv("DB_MAX_IDLE_CONNS", "25"))
	redisPoolSize, _ := strconv.Atoi(getEnv("REDIS_POOL_SIZE", "50"))

	// Parse timeouts (optimized for MMOFPS real-time requirements)
	requestTimeout, _ := time.ParseDuration(getEnv("REQUEST_TIMEOUT", "5s"))
	shutdownTimeout, _ := time.ParseDuration(getEnv("SHUTDOWN_TIMEOUT", "30s"))
	connMaxLifetime, _ := time.ParseDuration(getEnv("DB_CONN_MAX_LIFETIME", "5m"))

	config := &Config{
		ServerAddr:       getEnv("SERVER_ADDR", ":8080"),
		DatabaseURL:      getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/referral_system?sslmode=disable"),
		RedisURL:         getEnv("REDIS_URL", "redis://localhost:6379"),
		JWTSecret:        getEnv("JWT_SECRET", "your-secret-key-change-in-production"),
		Environment:      getEnv("ENVIRONMENT", "development"),

		// MMOFPS RPG Database Optimizations
		DBMaxOpenConns:   dbMaxOpenConns,
		DBMaxIdleConns:   dbMaxIdleConns,
		DBConnMaxLifetime: connMaxLifetime,
		RedisPoolSize:    redisPoolSize,
		RequestTimeout:   requestTimeout,
		ShutdownTimeout:  shutdownTimeout,
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
