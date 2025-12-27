// Guild Service Configuration - Enterprise-grade config management
// Issue: #2247

package config

import (
	"os"
	"strconv"
)

// Config holds all service configuration
type Config struct {
	Server   ServerConfig   `json:"server"`
	Database DatabaseConfig `json:"database"`
	Redis    RedisConfig    `json:"redis"`
}

// ServerConfig holds HTTP server configuration
type ServerConfig struct {
	Host string `json:"host"`
	Port int    `json:"port"`
}

// GetAddr returns the server address
func (s ServerConfig) GetAddr() string {
	return s.Host + ":" + strconv.Itoa(s.Port)
}

// DatabaseConfig holds PostgreSQL configuration
type DatabaseConfig struct {
	Host     string `json:"host"`
	Port     int    `json:"port"`
	User     string `json:"user"`
	Password string `json:"password"`
	Database string `json:"database"`
	SSLMode  string `json:"ssl_mode"`
	MaxConns int    `json:"max_conns"`
}

// GetDSN returns the database connection string
func (d DatabaseConfig) GetDSN() string {
	return "host=" + d.Host +
		" port=" + strconv.Itoa(d.Port) +
		" user=" + d.User +
		" password=" + d.Password +
		" dbname=" + d.Database +
		" sslmode=" + d.SSLMode
}

// RedisConfig holds Redis configuration
type RedisConfig struct {
	Host     string `json:"host"`
	Port     int    `json:"port"`
	Password string `json:"password"`
	DB       int    `json:"db"`
}

// GetAddr returns the Redis address
func (r RedisConfig) GetAddr() string {
	return r.Host + ":" + strconv.Itoa(r.Port)
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	cfg := &Config{
		Server: ServerConfig{
			Host: getEnv("SERVER_HOST", "0.0.0.0"),
			Port: getEnvInt("SERVER_PORT", 8080),
		},
		Database: DatabaseConfig{
			Host:     getEnv("DB_HOST", "localhost"),
			Port:     getEnvInt("DB_PORT", 5432),
			User:     getEnv("DB_USER", "guild_service"),
			Password: getEnv("DB_PASSWORD", "password"),
			Database: getEnv("DB_NAME", "guild_service"),
			SSLMode:  getEnv("DB_SSL_MODE", "disable"),
			MaxConns: getEnvInt("DB_MAX_CONNS", 50),
		},
		Redis: RedisConfig{
			Host:     getEnv("REDIS_HOST", "localhost"),
			Port:     getEnvInt("REDIS_PORT", 6379),
			Password: getEnv("REDIS_PASSWORD", ""),
			DB:       getEnvInt("REDIS_DB", 0),
		},
	}

	return cfg, nil
}

// getEnv gets an environment variable with a fallback
func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}

// getEnvInt gets an environment variable as int with a fallback
func getEnvInt(key string, fallback int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return fallback
}
