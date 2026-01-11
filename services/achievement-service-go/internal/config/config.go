// Agent: Backend Agent
// Issue: #backend-achievement-service-1

package config

import (
	"fmt"
	"time"

	"github.com/kelseyhightower/envconfig"
)

// Config holds all configuration for the achievement service
type Config struct {
	Server   ServerConfig
	Database DatabaseConfig
	Redis    RedisConfig
	Metrics  MetricsConfig
}

// ServerConfig holds HTTP server configuration
type ServerConfig struct {
	Address         string        `envconfig:"SERVER_ADDRESS" default:":8080"`
	ReadTimeout     time.Duration `envconfig:"SERVER_READ_TIMEOUT" default:"10s"`
	WriteTimeout    time.Duration `envconfig:"SERVER_WRITE_TIMEOUT" default:"10s"`
	MaxHeaderBytes  int           `envconfig:"SERVER_MAX_HEADER_BYTES" default:"1048576"`
	ShutdownTimeout time.Duration `envconfig:"SERVER_SHUTDOWN_TIMEOUT" default:"30s"`
}

// DatabaseConfig holds database connection configuration
// MMOFPS Optimization: Connection pool sized for high concurrency
type DatabaseConfig struct {
	Host         string        `envconfig:"DB_HOST" default:"localhost"`
	Port         int           `envconfig:"DB_PORT" default:"5432"`
	User         string        `envconfig:"DB_USER" required:"true"`
	Password     string        `envconfig:"DB_PASSWORD" required:"true"`
	Database     string        `envconfig:"DB_NAME" default:"necpgame"`
	SSLMode      string        `envconfig:"DB_SSL_MODE" default:"disable"`
	MaxOpenConns int           `envconfig:"DB_MAX_OPEN_CONNS" default:"25"`     // MMOFPS: Pool for concurrent achievement checks
	MaxIdleConns int           `envconfig:"DB_MAX_IDLE_CONNS" default:"5"`      // MMOFPS: Keep some connections warm
	MaxLifetime  time.Duration `envconfig:"DB_MAX_LIFETIME" default:"5m"`       // MMOFPS: Rotate connections to prevent stale connections
	QueryTimeout time.Duration `envconfig:"DB_QUERY_TIMEOUT" default:"5s"`     // MMOFPS: Fast queries for real-time updates
}

// RedisConfig holds Redis configuration for caching
// MMOFPS Optimization: Redis for achievement progress caching
type RedisConfig struct {
	Host     string        `envconfig:"REDIS_HOST" default:"localhost"`
	Port     int           `envconfig:"REDIS_PORT" default:"6379"`
	Password string        `envconfig:"REDIS_PASSWORD"`
	DB       int           `envconfig:"REDIS_DB" default:"0"`
	TTL      time.Duration `envconfig:"REDIS_TTL" default:"10m"` // MMOFPS: Cache achievement data briefly
}

// MetricsConfig holds metrics and monitoring configuration
type MetricsConfig struct {
	Enabled  bool   `envconfig:"METRICS_ENABLED" default:"true"`
	Address  string `envconfig:"METRICS_ADDRESS" default:":9090"`
	Path     string `envconfig:"METRICS_PATH" default:"/metrics"`
	Interval time.Duration `envconfig:"METRICS_INTERVAL" default:"30s"`
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	var cfg Config
	if err := envconfig.Process("", &cfg); err != nil {
		return nil, fmt.Errorf("failed to load config: %w", err)
	}
	return &cfg, nil
}