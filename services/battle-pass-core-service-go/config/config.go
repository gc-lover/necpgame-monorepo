// Issue: #1636
package config

import (
	"github.com/kelseyhightower/envconfig"
)

// Config holds service configuration
type Config struct {
	// Server
	Port        int    `envconfig:"PORT" default:"8093"`
	Environment string `envconfig:"ENVIRONMENT" default:"development"`

	// Database
	DatabaseURL string `envconfig:"DATABASE_URL" required:"true"`

	// Auth
	JWTSecret string `envconfig:"JWT_SECRET" required:"true"`

	// Redis (for caching)
	RedisURL string `envconfig:"REDIS_URL" default:"redis://localhost:6379"`

	// Observability
	TracingEnabled bool   `envconfig:"TRACING_ENABLED" default:"false"`
	TracingURL     string `envconfig:"TRACING_URL" default:"http://localhost:14268/api/traces"`

	// Performance
	EnableProfiling bool `envconfig:"ENABLE_PROFILING" default:"false"`
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	var cfg Config
	if err := envconfig.Process("", &cfg); err != nil {
		return nil, err
	}
	return &cfg, nil
}















