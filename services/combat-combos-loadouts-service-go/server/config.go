// Combat Combos Loadouts Service Configuration
// Issue: #141890005

package server

import (
	"github.com/kelseyhightower/envconfig"
)

// Config ServerConfig holds server-specific configuration
type Config struct {
	Addr           string `envconfig:"SERVER_ADDR" default:":8083"`
	ReadTimeout    int    `envconfig:"READ_TIMEOUT" default:"30"`
	WriteTimeout   int    `envconfig:"WRITE_TIMEOUT" default:"30"`
	IdleTimeout    int    `envconfig:"IDLE_TIMEOUT" default:"60"`
	MaxHeaderBytes int    `envconfig:"MAX_HEADER_BYTES" default:"1048576"`
}

// DatabaseConfig holds database-specific configuration
type DatabaseConfig struct {
	URL             string `envconfig:"DATABASE_URL" default:"postgres://user:password@localhost/combat_combos_loadouts?sslmode=disable"`
	MaxOpenConns    int    `envconfig:"DB_MAX_OPEN_CONNS" default:"25"`
	MaxIdleConns    int    `envconfig:"DB_MAX_IDLE_CONNS" default:"25"`
	ConnMaxLifetime int    `envconfig:"DB_CONN_MAX_LIFETIME" default:"300"`
}

// RedisConfig holds Redis cache configuration
type RedisConfig struct {
	URL string `envconfig:"REDIS_URL" default:"redis://localhost:6379"`
	TTL int    `envconfig:"REDIS_TTL" default:"300"`
}

// ServiceConfig holds general service configuration
type ServiceConfig struct {
	Name        string `envconfig:"SERVICE_NAME" default:"combat-combos-loadouts-service"`
	Version     string `envconfig:"SERVICE_VERSION" default:"1.0.0"`
	Environment string `envconfig:"ENVIRONMENT" default:"development"`
}

// SecurityConfig holds security-related configuration
type SecurityConfig struct {
	JWTSecret string `envconfig:"JWT_SECRET" default:"your-secret-key"`
	JWTIssuer string `envconfig:"JWT_ISSUER" default:"combat-combos-loadouts-service"`
}

// PerformanceConfig holds performance tuning configuration
type PerformanceConfig struct {
	GoroutinePoolSize int `envconfig:"GOROUTINE_POOL_SIZE" default:"100"`
	RequestTimeout    int `envconfig:"REQUEST_TIMEOUT" default:"30"`
}

// MonitoringConfig holds monitoring and health check configuration
type MonitoringConfig struct {
	MetricsPort     string `envconfig:"METRICS_PORT" default:":9090"`
	HealthCheckPath string `envconfig:"HEALTH_CHECK_PATH" default:"/health"`
}

// LoggingConfig holds logging configuration
type LoggingConfig struct {
	Level  string `envconfig:"LOG_LEVEL" default:"info"`
	Format string `envconfig:"LOG_FORMAT" default:"json"`
}

// Config holds all configuration for the service (SOLID: Single Responsibility)
type Config struct {
	Server      Config            `json:"server"`
	Database    DatabaseConfig    `json:"database"`
	Redis       RedisConfig       `json:"redis"`
	Service     ServiceConfig     `json:"service"`
	Security    SecurityConfig    `json:"security"`
	Performance PerformanceConfig `json:"performance"`
	Monitoring  MonitoringConfig  `json:"monitoring"`
	Logging     LoggingConfig     `json:"logging"`
}

// LoadConfig loads configuration from environment variables
func LoadConfig() (*Config, error) {
	cfg := &Config{}
	if err := envconfig.Process("", cfg); err != nil {
		return nil, err
	}
	return cfg, nil
}
