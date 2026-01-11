package config

import (
	"github.com/kelseyhightower/envconfig"
)

// Config holds all configuration for the stock dividends service
type Config struct {
	Server   ServerConfig   `envconfig:"SERVER"`
	Database DatabaseConfig `envconfig:"DATABASE"`
	Logging  LoggingConfig  `envconfig:"LOGGING"`
	JWT      JWTConfig      `envconfig:"JWT"`
	Dividends DividendsConfig `envconfig:"DIVIDENDS"`
}

// ServerConfig holds server-related configuration
type ServerConfig struct {
	Port         int    `envconfig:"PORT" default:"8096"`
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
	DBName   string `envconfig:"DBNAME" default:"stock_dividends_db"`
	SSLMode  string `envconfig:"SSL_MODE" default:"disable"`
	MaxConns int    `envconfig:"MAX_CONNS" default:"10"`
}

// LoggingConfig holds logging-related configuration
type LoggingConfig struct {
	Level  string `envconfig:"LEVEL" default:"info"`
	Format string `envconfig:"FORMAT" default:"json"`
}

// JWTConfig holds JWT-related configuration
type JWTConfig struct {
	Secret string `envconfig:"SECRET" required:"true"`
}

// DividendsConfig holds dividends-specific configuration
type DividendsConfig struct {
	TaxRate            float64 `envconfig:"TAX_RATE" default:"0.15"`
	DRIPMinThreshold   float64 `envconfig:"DRIP_MIN_THRESHOLD" default:"10.0"`
	ProcessingInterval string  `envconfig:"PROCESSING_INTERVAL" default:"24h"`
	WalletServiceURL   string  `envconfig:"WALLET_SERVICE_URL" default:"http://wallet-service:8080"`
	TaxServiceURL      string  `envconfig:"TAX_SERVICE_URL" default:"http://tax-service:8080"`
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	var cfg Config
	err := envconfig.Process("STOCK_DIVIDENDS", &cfg)
	if err != nil {
		return nil, err
	}
	return &cfg, nil
}