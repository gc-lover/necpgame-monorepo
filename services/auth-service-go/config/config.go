// Issue: #136
package config

import (
	"time"
)

// AuthServiceConfig содержит конфигурацию сервиса аутентификации
type AuthServiceConfig struct {
	// HTTP server settings
	HTTPAddr       string        `json:"http_addr"`       // HTTP server address
	HealthAddr     string        `json:"health_addr"`     // Health check address
	ReadTimeout    time.Duration `json:"read_timeout"`    // HTTP read timeout
	WriteTimeout   time.Duration `json:"write_timeout"`   // HTTP write timeout
	MaxHeaderBytes int           `json:"max_header_bytes"` // Max header size

	// JWT settings
	JWTSecret      string        `json:"jwt_secret"`       // JWT signing secret
	JWTExpiry      time.Duration `json:"jwt_expiry"`       // JWT token expiry
	RefreshExpiry  time.Duration `json:"refresh_expiry"`   // Refresh token expiry

	// Session settings
	SessionTimeout time.Duration `json:"session_timeout"`  // Session timeout

	// Security settings
	MaxLoginAttempts int `json:"max_login_attempts"` // Max failed login attempts

	// Database settings
	DatabaseURL string `json:"database_url"` // PostgreSQL connection string

	// Redis settings
	RedisURL string `json:"redis_url"` // Redis connection string

	// OAuth settings
	GoogleClientID     string `json:"google_client_id"`
	GoogleClientSecret string `json:"google_client_secret"`
	GoogleRedirectURI  string `json:"google_redirect_uri"`

	// Metrics settings
	MetricsAddr string `json:"metrics_addr"` // Prometheus metrics address
}
