// Package config Issue: #1911
package config

import (
	"time"

	"go.uber.org/zap"
)

// ServerConfig contains server-related configuration
type ServerConfig struct {
	Port        int           `json:"port"`
	Host        string        `json:"host"`
	ReadTimeout time.Duration `json:"read_timeout"`
}

// DatabaseConfig contains database-related configuration
type DatabaseConfig struct {
	DatabaseURL     string        `json:"database_url"`
	MaxConns        int32         `json:"max_conns"`
	MinConns        int32         `json:"min_conns"`
	MaxConnLifetime time.Duration `json:"max_conn_lifetime"`
	MaxConnIdleTime time.Duration `json:"max_conn_idle_time"`
}

// ServiceConfig contains general service configuration
type ServiceConfig struct {
	ServiceName string `json:"service_name"`
	Environment string `json:"environment"`
	Version     string `json:"version"`
	RedisURL    string `json:"redis_url"`
}

// ModerationConfig contains moderation-specific configuration
type ModerationConfig struct {
	MaxMessageLength   int           `json:"max_message_length"`
	ProcessingTimeout  time.Duration `json:"processing_timeout"`
	CacheTTL           time.Duration `json:"cache_ttl"`
	RateLimitPerSecond int           `json:"rate_limit_per_second"`
}

// MonitoringConfig contains monitoring configuration
type MonitoringConfig struct {
	EnablePprof         bool          `json:"enable_pprof"`
	PprofAddr           string        `json:"pprof_addr"`
	MetricsAddr         string        `json:"metrics_addr"`
	HealthCheckInterval time.Duration `json:"health_check_interval"`
}

// Config holds all configuration for the chat moderation service
type Config struct {
	ServerConfig
	DatabaseConfig
	ServiceConfig
	ModerationConfig
	MonitoringConfig

	// Logger
	Logger *zap.Logger `json:"-"` // Not serialized
}

// Load loads configuration from environment variables

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
