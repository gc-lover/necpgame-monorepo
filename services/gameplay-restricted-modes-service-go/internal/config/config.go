// Issue: #1499
package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all configuration for the gameplay restricted modes service
type Config struct {
	Environment string
	Server      ServerConfig
	Database    DatabaseConfig
	Redis       RedisConfig
	JWT         JWTConfig
	CORS        CORSConfig
	Metrics     MetricsConfig
	Profiling   ProfilingConfig
	Logging     LoggingConfig
}

// ServerConfig holds HTTP server configuration
type ServerConfig struct {
	Port     int
	BaseURL  string
	TLS      TLSConfig
}

// TLSConfig holds TLS configuration
type TLSConfig struct {
	Enabled  bool
	CertFile string
	KeyFile  string
}

// DatabaseConfig holds PostgreSQL configuration
type DatabaseConfig struct {
	URL string
}

// RedisConfig holds Redis configuration
type RedisConfig struct {
	Addr     string
	Password string
	DB       int
}

// JWTConfig holds JWT configuration
type JWTConfig struct {
	Secret           string
	AccessTokenTTL   time.Duration
	RefreshTokenTTL  time.Duration
	Issuer           string
}

// CORSConfig holds CORS configuration
type CORSConfig struct {
	AllowedOrigins []string
}

// MetricsConfig holds metrics configuration
type MetricsConfig struct {
	Username string
	Password string
}

// ProfilingConfig holds profiling configuration
type ProfilingConfig struct {
	Enabled bool
	Port    int
}

// LoggingConfig holds logging configuration
type LoggingConfig struct {
	Level string
}

// Load loads configuration from environment variables with sensible defaults
func Load() (*Config, error) {
	config := &Config{
		Environment: getEnv("ENVIRONMENT", "development"),
		Server: ServerConfig{
			Port:    getEnvAsInt("SERVER_PORT", 8080),
			BaseURL: getEnv("SERVER_BASE_URL", "http://localhost:8080"),
			TLS: TLSConfig{
				Enabled:  getEnvAsBool("SERVER_TLS_ENABLED", false),
				CertFile: getEnv("SERVER_TLS_CERT_FILE", ""),
				KeyFile:  getEnv("SERVER_TLS_KEY_FILE", ""),
			},
		},
		Database: DatabaseConfig{
			URL: getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/gameplay_db?sslmode=disable"),
		},
		Redis: RedisConfig{
			Addr:     getEnv("REDIS_ADDR", "localhost:6379"),
			Password: getEnv("REDIS_PASSWORD", ""),
			DB:       getEnvAsInt("REDIS_DB", 0),
		},
		JWT: JWTConfig{
			Secret:          getEnv("JWT_SECRET", "your-super-secret-jwt-key-change-in-production"),
			AccessTokenTTL:  getEnvAsDuration("JWT_ACCESS_TOKEN_TTL", 15*time.Minute),
			RefreshTokenTTL: getEnvAsDuration("JWT_REFRESH_TOKEN_TTL", 24*time.Hour*7),
			Issuer:          getEnv("JWT_ISSUER", "gameplay-restricted-modes-service"),
		},
		CORS: CORSConfig{
			AllowedOrigins: getEnvAsSlice("CORS_ALLOWED_ORIGINS", []string{"http://localhost:3000", "http://localhost:8080"}),
		},
		Metrics: MetricsConfig{
			Username: getEnv("METRICS_USERNAME", "metrics"),
			Password: getEnv("METRICS_PASSWORD", "secure-metrics-password"),
		},
		Profiling: ProfilingConfig{
			Enabled: getEnvAsBool("PROFILING_ENABLED", true),
			Port:    getEnvAsInt("PROFILING_PORT", 6555),
		},
		Logging: LoggingConfig{
			Level: getEnv("LOG_LEVEL", "info"),
		},
	}

	return config, nil
}

// Helper functions for environment variable parsing
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvAsInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}

func getEnvAsBool(key string, defaultValue bool) bool {
	if value := os.Getenv(key); value != "" {
		if boolValue, err := strconv.ParseBool(value); err == nil {
			return boolValue
		}
	}
	return defaultValue
}

func getEnvAsDuration(key string, defaultValue time.Duration) time.Duration {
	if value := os.Getenv(key); value != "" {
		if duration, err := time.ParseDuration(value); err == nil {
			return duration
		}
	}
	return defaultValue
}

func getEnvAsSlice(key string, defaultValue []string) []string {
	if value := os.Getenv(key); value != "" {
		// Simple comma-separated parsing
		// In production, consider using a more robust parsing library
		return []string{value}
	}
	return defaultValue
}

