package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all configuration for the service
type Config struct {
	Port        int
	DatabaseURL string
	RedisURL    string
	JWTSecret   string
	Environment string

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
	port, err := strconv.Atoi(getEnv("PORT", "8084"))
	if err != nil {
		return nil, err
	}

	// Parse MMOFPS-optimized database settings
	dbMaxOpenConns, _ := strconv.Atoi(getEnv("DB_MAX_OPEN_CONNS", "100"))
	dbMaxIdleConns, _ := strconv.Atoi(getEnv("DB_MAX_IDLE_CONNS", "25"))
	redisPoolSize, _ := strconv.Atoi(getEnv("REDIS_POOL_SIZE", "50"))

	// Parse timeouts (optimized for MMOFPS real-time requirements)
	requestTimeout, _ := time.ParseDuration(getEnv("REQUEST_TIMEOUT", "5s"))
	shutdownTimeout, _ := time.ParseDuration(getEnv("SHUTDOWN_TIMEOUT", "30s"))
	connMaxLifetime, _ := time.ParseDuration(getEnv("DB_CONN_MAX_LIFETIME", "5m"))

	return &Config{
		Port:        port,
		DatabaseURL: getEnv("DATABASE_URL", "postgres://user:password@localhost/mail_system?sslmode=disable"),
		RedisURL:    getEnv("REDIS_URL", "redis://localhost:6379"),
		JWTSecret:   getEnv("JWT_SECRET", "your-secret-key"),
		Environment: getEnv("ENVIRONMENT", "development"),

		// MMOFPS RPG Database Optimizations
		DBMaxOpenConns:   dbMaxOpenConns,
		DBMaxIdleConns:   dbMaxIdleConns,
		DBConnMaxLifetime: connMaxLifetime,
		RedisPoolSize:    redisPoolSize,
		RequestTimeout:   requestTimeout,
		ShutdownTimeout:  shutdownTimeout,
	}, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

