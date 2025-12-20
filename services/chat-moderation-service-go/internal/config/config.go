// Issue: #1911
package config

import (
	"os"
	"strconv"
	"time"

	"go.uber.org/zap"
)

// Config holds all configuration for the chat moderation service
type Config struct {
	// Server configuration
	Port        int           `json:"port"`
	Host        string        `json:"host"`
	ReadTimeout time.Duration `json:"read_timeout"`

	// Database configuration
	DatabaseURL     string        `json:"database_url"`
	MaxConns        int32         `json:"max_conns"`
	MinConns        int32         `json:"min_conns"`
	MaxConnLifetime time.Duration `json:"max_conn_lifetime"`
	MaxConnIdleTime time.Duration `json:"max_conn_idle_time"`

	// Redis configuration
	RedisURL string `json:"redis_url"`

	// Service configuration
	ServiceName string `json:"service_name"`
	Environment string `json:"environment"`
	Version     string `json:"version"`

	// Moderation configuration
	MaxMessageLength   int           `json:"max_message_length"`
	ProcessingTimeout  time.Duration `json:"processing_timeout"`
	CacheTTL           time.Duration `json:"cache_ttl"`
	RateLimitPerSecond int           `json:"rate_limit_per_second"`

	// Monitoring
	EnablePprof         bool          `json:"enable_pprof"`
	PprofAddr           string        `json:"pprof_addr"`
	MetricsAddr         string        `json:"metrics_addr"`
	HealthCheckInterval time.Duration `json:"health_check_interval"`

	// Logger
	Logger *zap.Logger `json:"-"` // Not serialized
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	config := &Config{
		// Server defaults
		Port:        getEnvInt("PORT", 8084),
		Host:        getEnv("HOST", "0.0.0.0"),
		ReadTimeout: getEnvDuration("READ_TIMEOUT", 30*time.Second),

		// Database defaults (optimized for MMOFPS)
		DatabaseURL:     getEnv("DATABASE_URL", "postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable"),
		MaxConns:        getEnvInt32("DB_MAX_CONNS", 50),
		MinConns:        getEnvInt32("DB_MIN_CONNS", 10),
		MaxConnLifetime: getEnvDuration("DB_MAX_CONN_LIFETIME", 5*time.Minute),
		MaxConnIdleTime: getEnvDuration("DB_MAX_CONN_IDLE_TIME", 10*time.Minute),

		// Redis defaults
		RedisURL: getEnv("REDIS_URL", "redis://localhost:6379"),

		// Service defaults
		ServiceName: getEnv("SERVICE_NAME", "chat-moderation-service"),
		Environment: getEnv("ENVIRONMENT", "development"),
		Version:     getEnv("VERSION", "1.0.0"),

		// Moderation defaults (optimized for low latency)
		MaxMessageLength:   getEnvInt("MAX_MESSAGE_LENGTH", 500),
		ProcessingTimeout:  getEnvDuration("PROCESSING_TIMEOUT", 50*time.Millisecond),
		CacheTTL:           getEnvDuration("CACHE_TTL", 5*time.Minute),
		RateLimitPerSecond: getEnvInt("RATE_LIMIT_PER_SECOND", 10),

		// Monitoring defaults
		EnablePprof:         getEnvBool("ENABLE_PPROF", true),
		PprofAddr:           getEnv("PPROF_ADDR", "localhost:6061"),
		MetricsAddr:         getEnv("METRICS_ADDR", ":9093"),
		HealthCheckInterval: getEnvDuration("HEALTH_CHECK_INTERVAL", 30*time.Second),
	}

	// Initialize structured logger
	var err error
	if config.Environment == "production" {
		config.Logger, err = zap.NewProduction()
	} else {
		config.Logger, err = zap.NewDevelopment()
	}
	if err != nil {
		return nil, err
	}

	return config, nil
}

// Validate checks configuration validity
func (c *Config) Validate() error {
	if c.Port < 1 || c.Port > 65535 {
		return &ValidationError{Field: "Port", Message: "must be between 1 and 65535"}
	}
	if c.DatabaseURL == "" {
		return &ValidationError{Field: "DatabaseURL", Message: "cannot be empty"}
	}
	if c.MaxConns < c.MinConns {
		return &ValidationError{Field: "MaxConns", Message: "must be >= MinConns"}
	}
	if c.ProcessingTimeout > 100*time.Millisecond {
		return &ValidationError{Field: "ProcessingTimeout", Message: "must be <= 100ms for P99 requirements"}
	}
	return nil
}

// ValidationError represents configuration validation error
type ValidationError struct {
	Field   string
	Message string
}

func (e *ValidationError) Error() string {
	return "config validation failed: " + e.Field + " " + e.Message
}

// Helper functions for environment variable parsing

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if parsed, err := strconv.Atoi(value); err == nil {
			return parsed
		}
	}
	return defaultValue
}

func getEnvInt32(key string, defaultValue int32) int32 {
	if value := os.Getenv(key); value != "" {
		if parsed, err := strconv.ParseInt(value, 10, 32); err == nil {
			return int32(parsed)
		}
	}
	return defaultValue
}

func getEnvBool(key string, defaultValue bool) bool {
	if value := os.Getenv(key); value != "" {
		if parsed, err := strconv.ParseBool(value); err == nil {
			return parsed
		}
	}
	return defaultValue
}

func getEnvDuration(key string, defaultValue time.Duration) time.Duration {
	if value := os.Getenv(key); value != "" {
		if parsed, err := time.ParseDuration(value); err == nil {
			return parsed
		}
	}
	return defaultValue
}
