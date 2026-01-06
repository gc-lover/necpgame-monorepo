package config

import (
	"github.com/kelseyhightower/envconfig"
)

// Config holds all configuration for the support service
type Config struct {
	Server   ServerConfig   `envconfig:"SERVER"`
	Database DatabaseConfig `envconfig:"DATABASE"`
	Logging  LoggingConfig  `envconfig:"LOGGING"`
	SLA      SLAConfig      `envconfig:"SLA"`
}

// ServerConfig holds server-related configuration
type ServerConfig struct {
	Port         int    `envconfig:"PORT" default:"8095"`
	Host         string `envconfig:"HOST" default:"0.0.0.0"`
	ReadTimeout  int    `envconfig:"READ_TIMEOUT" default:"15"`
	WriteTimeout int    `envconfig:"WRITE_TIMEOUT" default:"15"`
	IdleTimeout  int    `envconfig:"IDLE_TIMEOUT" default:"60"`
}

// DatabaseConfig holds database-related configuration
type DatabaseConfig struct {
	Host     string `envconfig:"HOST" default:"localhost"`
	Port     int    `envconfig:"PORT" default:"5432"`
	User     string `envconfig:"USER" required:"true"`
	Password string `envconfig:"PASSWORD" required:"true"`
	DBName   string `envconfig:"DBNAME" default:"support_db"`
	SSLMode  string `envconfig:"SSL_MODE" default:"disable"`
	MaxConns int    `envconfig:"MAX_CONNS" default:"10"`
}

// LoggingConfig holds logging-related configuration
type LoggingConfig struct {
	Level  string `envconfig:"LEVEL" default:"info"`
	Format string `envconfig:"FORMAT" default:"json"`
}

// SLAConfig holds SLA-related configuration
type SLAConfig struct {
	ResponseTimeLow    string `envconfig:"RESPONSE_TIME_LOW" default:"24h"`
	ResponseTimeNormal string `envconfig:"RESPONSE_TIME_NORMAL" default:"12h"`
	ResponseTimeHigh   string `envconfig:"RESPONSE_TIME_HIGH" default:"4h"`
	ResponseTimeUrgent string `envconfig:"RESPONSE_TIME_URGENT" default:"1h"`

	ResolutionTimeLow    string `envconfig:"RESOLUTION_TIME_LOW" default:"168h"`    // 7 days
	ResolutionTimeNormal string `envconfig:"RESOLUTION_TIME_NORMAL" default:"72h"`   // 3 days
	ResolutionTimeHigh   string `envconfig:"RESOLUTION_TIME_HIGH" default:"24h"`     // 1 day
	ResolutionTimeUrgent string `envconfig:"RESOLUTION_TIME_URGENT" default:"4h"`     // 4 hours
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	var cfg Config
	err := envconfig.Process("SUPPORT", &cfg)
	if err != nil {
		return nil, err
	}
	return &cfg, nil
}


