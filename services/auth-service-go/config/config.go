// Issue: #136
package config

import (
	"time"
)

// HTTPServerConfig содержит настройки HTTP сервера
type HTTPServerConfig struct {
	HTTPAddr       string        `json:"http_addr"`
	HealthAddr     string        `json:"health_addr"`
	ReadTimeout    time.Duration `json:"read_timeout"`
	WriteTimeout   time.Duration `json:"write_timeout"`
	MaxHeaderBytes int           `json:"max_header_bytes"`
}

// JWTConfig содержит настройки JWT
type JWTConfig struct {
	JWTSecret     string        `json:"jwt_secret"`
	JWTExpiry     time.Duration `json:"jwt_expiry"`
	RefreshExpiry time.Duration `json:"refresh_expiry"`
}

// SecurityConfig содержит настройки безопасности
type SecurityConfig struct {
	SessionTimeout   time.Duration `json:"session_timeout"`
	MaxLoginAttempts int           `json:"max_login_attempts"`
}

// DatabaseConfig содержит настройки баз данных
type DatabaseConfig struct {
	DatabaseURL string `json:"database_url"`
	RedisURL    string `json:"redis_url"`
}

// OAuthConfig содержит настройки OAuth
type OAuthConfig struct {
	GoogleClientID     string `json:"google_client_id"`
	GoogleClientSecret string `json:"google_client_secret"`
	GoogleRedirectURI  string `json:"google_redirect_uri"`
}

// AuthServiceConfig содержит конфигурацию сервиса аутентификации
type AuthServiceConfig struct {
	HTTPServerConfig
	JWTConfig
	SecurityConfig
	DatabaseConfig
	OAuthConfig

	// Metrics settings
	MetricsAddr string `json:"metrics_addr"`
}
