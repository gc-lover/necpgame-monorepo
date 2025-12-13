// Combat Combos Loadouts Service Configuration
// Issue: #141890005

package server

import (
	"github.com/kelseyhightower/envconfig"
)

// Config holds all configuration for the service
type Config struct {
	// Server configuration
	ServerAddr     string `envconfig:"SERVER_ADDR" default:":8083"`
	ReadTimeout    int    `envconfig:"READ_TIMEOUT" default:"30"`
	WriteTimeout   int    `envconfig:"WRITE_TIMEOUT" default:"30"`
	IdleTimeout    int    `envconfig:"IDLE_TIMEOUT" default:"60"`
	MaxHeaderBytes int    `envconfig:"MAX_HEADER_BYTES" default:"1048576"`

	// Database configuration
	DatabaseURL     string `envconfig:"DATABASE_URL" default:"postgres://user:password@localhost/combat_combos_loadouts?sslmode=disable"`
	MaxOpenConns    int    `envconfig:"DB_MAX_OPEN_CONNS" default:"25"`
	MaxIdleConns    int    `envconfig:"DB_MAX_IDLE_CONNS" default:"25"`
	ConnMaxLifetime int    `envconfig:"DB_CONN_MAX_LIFETIME" default:"300"`

	// Redis configuration (for caching loadouts)
	RedisURL string `envconfig:"REDIS_URL" default:"redis://localhost:6379"`
	RedisTTL int    `envconfig:"REDIS_TTL" default:"300"`

	// Service configuration
	ServiceName    string `envconfig:"SERVICE_NAME" default:"combat-combos-loadouts-service"`
	ServiceVersion string `envconfig:"SERVICE_VERSION" default:"1.0.0"`
	Environment    string `envconfig:"ENVIRONMENT" default:"development"`

	// Security configuration
	JWTSecret string `envconfig:"JWT_SECRET" default:"your-secret-key"`
	JWTIssuer string `envconfig:"JWT_ISSUER" default:"combat-combos-loadouts-service"`

	// Performance configuration
	GoroutinePoolSize int `envconfig:"GOROUTINE_POOL_SIZE" default:"100"`
	RequestTimeout    int `envconfig:"REQUEST_TIMEOUT" default:"30"`

	// Monitoring configuration
	MetricsPort     string `envconfig:"METRICS_PORT" default:":9090"`
	HealthCheckPath string `envconfig:"HEALTH_CHECK_PATH" default:"/health"`

	// Logging configuration
	LogLevel  string `envconfig:"LOG_LEVEL" default:"info"`
	LogFormat string `envconfig:"LOG_FORMAT" default:"json"`
}

// LoadConfig loads configuration from environment variables
func LoadConfig() (*Config, error) {
	var cfg Config
	if err := envconfig.Process("", &cfg); err != nil {
		return nil, err
	}
	return &cfg, nil
}
