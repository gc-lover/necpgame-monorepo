package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all configuration for the Game Analytics Dashboard Service
type Config struct {
	// Server configuration
	ServerAddr string
	ServerPort int

	// Database configuration
	DatabaseURL string

	// Redis configuration
	RedisURL string

	// Analytics configuration
	Analytics struct {
		DataRetentionDays    int
		CacheTTL             time.Duration
		BatchSize            int
		WorkerPoolSize       int
		MaxConcurrentQueries int
	}

	// Dashboard configuration
	Dashboard struct {
		RealTimeUpdateInterval time.Duration
		DefaultTimeRange       string
		MaxWidgetsPerDashboard int
	}

	// Monitoring configuration
	Metrics struct {
		Enabled bool
		Path    string
	}

	// Security configuration
	Security struct {
		JWTSecret string
		CORSOrigins []string
		APIKey     string
	}

	// External services configuration
	Services struct {
		CombatStatsURL   string
		EconomyServiceURL string
		SocialServiceURL  string
		EventBusURL      string
	}

	// Logging configuration
	LogLevel string
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	cfg := &Config{}

	// Server configuration
	cfg.ServerAddr = getEnv("SERVER_ADDR", ":8080")
	cfg.ServerPort = getEnvAsInt("SERVER_PORT", 8080)

	// Database configuration
	cfg.DatabaseURL = getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/game_analytics?sslmode=disable")

	// Redis configuration
	cfg.RedisURL = getEnv("REDIS_URL", "redis://localhost:6379")

	// Analytics configuration
	cfg.Analytics.DataRetentionDays = getEnvAsInt("DATA_RETENTION_DAYS", 90)
	cfg.Analytics.CacheTTL = getEnvAsDuration("CACHE_TTL", 5*time.Minute)
	cfg.Analytics.BatchSize = getEnvAsInt("BATCH_SIZE", 1000)
	cfg.Analytics.WorkerPoolSize = getEnvAsInt("WORKER_POOL_SIZE", 10)
	cfg.Analytics.MaxConcurrentQueries = getEnvAsInt("MAX_CONCURRENT_QUERIES", 50)

	// Dashboard configuration
	cfg.Dashboard.RealTimeUpdateInterval = getEnvAsDuration("REALTIME_UPDATE_INTERVAL", 30*time.Second)
	cfg.Dashboard.DefaultTimeRange = getEnv("DEFAULT_TIME_RANGE", "24h")
	cfg.Dashboard.MaxWidgetsPerDashboard = getEnvAsInt("MAX_WIDGETS_PER_DASHBOARD", 20)

	// Monitoring configuration
	cfg.Metrics.Enabled = getEnvAsBool("METRICS_ENABLED", true)
	cfg.Metrics.Path = getEnv("METRICS_PATH", "/metrics")

	// Security configuration
	cfg.Security.JWTSecret = getEnv("JWT_SECRET", "your-secret-key")
	cfg.Security.CORSOrigins = []string{getEnv("CORS_ORIGINS", "*")}
	cfg.Security.APIKey = getEnv("API_KEY", "your-api-key")

	// External services configuration
	cfg.Services.CombatStatsURL = getEnv("COMBAT_STATS_URL", "http://combat-stats-service:8080")
	cfg.Services.EconomyServiceURL = getEnv("ECONOMY_SERVICE_URL", "http://economy-service:8080")
	cfg.Services.SocialServiceURL = getEnv("SOCIAL_SERVICE_URL", "http://social-service:8080")
	cfg.Services.EventBusURL = getEnv("EVENT_BUS_URL", "http://event-bus:8080")

	// Logging configuration
	cfg.LogLevel = getEnv("LOG_LEVEL", "info")

	return cfg, nil
}

// Helper functions for environment variable parsing
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvAsInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}

func getEnvAsBool(key string, defaultValue bool) bool {
	if value := os.Getenv(key); value != "" {
		if boolValue, err := strconv.ParseBool(value); err == nil {
			return boolValue
		}
	}
	return defaultValue
}

func getEnvAsDuration(key string, defaultValue time.Duration) time.Duration {
	if value := os.Getenv(key); value != "" {
		if duration, err := time.ParseDuration(value); err == nil {
			return duration
		}
	}
	return defaultValue
}
