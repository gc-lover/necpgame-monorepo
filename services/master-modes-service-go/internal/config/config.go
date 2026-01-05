package config

import (
	"fmt"
	"os"
	"strconv"
	"time"

	"github.com/go-playground/validator/v10"
)

// Config содержит всю конфигурацию сервиса с оптимизациями для MMOFPS
type Config struct {
	Environment string        `validate:"required,oneof=development staging production"`
	Server      ServerConfig  `validate:"required"`
	Database    DatabaseConfig `validate:"required"`
	Redis       RedisConfig   `validate:"required"`
	Telemetry   TelemetryConfig `validate:"required"`
	Security    SecurityConfig `validate:"required"`
}

// ServerConfig конфигурация HTTP сервера
type ServerConfig struct {
	Port         int           `validate:"required,min=1,max=65535"`
	ReadTimeout  time.Duration `validate:"required"`
	WriteTimeout time.Duration `validate:"required"`
	IdleTimeout  time.Duration `validate:"required"`
	Host         string        `validate:"required"`
}

// DatabaseConfig конфигурация PostgreSQL с connection pooling для MMOFPS
type DatabaseConfig struct {
	Host            string        `validate:"required"`
	Port            int           `validate:"required,min=1,max=65535"`
	User            string        `validate:"required"`
	Password        string        `validate:"required"`
	Database        string        `validate:"required"`
	SSLMode         string        `validate:"required,oneof=disable allow prefer require verify-ca verify-full"`
	MaxConns        int           `validate:"required,min=1,max=1000"`
	MinConns        int           `validate:"required,min=0"`
	MaxConnLifetime time.Duration `validate:"required"`
	MaxConnIdleTime time.Duration `validate:"required"`
	HealthCheckPeriod time.Duration `validate:"required"`
}

// RedisConfig конфигурация Redis с оптимизациями для высокой производительности
type RedisConfig struct {
	Host         string        `validate:"required"`
	Port         int           `validate:"required,min=1,max=65535"`
	Password     string
	DB           int           `validate:"min=0,max=15"`
	PoolSize     int           `validate:"required,min=1,max=1000"`
	MinIdleConns int           `validate:"required,min=0"`
	MaxConnAge   time.Duration `validate:"required"`
	IdleTimeout  time.Duration `validate:"required"`
	ReadTimeout  time.Duration `validate:"required"`
	WriteTimeout time.Duration `validate:"required"`
}

// TelemetryConfig конфигурация мониторинга и tracing
type TelemetryConfig struct {
	ServiceName      string        `validate:"required"`
	ServiceVersion   string        `validate:"required"`
	JaegerEndpoint   string        `validate:"required"`
	MetricsInterval  time.Duration `validate:"required"`
	TracingEnabled   bool
	MetricsEnabled   bool
}

// SecurityConfig конфигурация безопасности
type SecurityConfig struct {
	JWTSecret           string        `validate:"required,min=32"`
	JWTExpiration       time.Duration `validate:"required"`
	RateLimitEnabled    bool
	RateLimitRequests   int           `validate:"required,min=1"`
	RateLimitWindow     time.Duration `validate:"required"`
	CORSAllowedOrigins  []string
	APIKeyRequired      bool
	APIKey              string
}

// Load загружает конфигурацию из переменных окружения с валидацией
func Load() (*Config, error) {
	cfg := &Config{
		Environment: getEnvString("ENV", "development"),
		Server: ServerConfig{
			Port:         getEnvInt("SERVER_PORT", 8080),
			ReadTimeout:  getEnvDuration("SERVER_READ_TIMEOUT", 15*time.Second),
			WriteTimeout: getEnvDuration("SERVER_WRITE_TIMEOUT", 15*time.Second),
			IdleTimeout:  getEnvDuration("SERVER_IDLE_TIMEOUT", 60*time.Second),
			Host:         getEnvString("SERVER_HOST", "0.0.0.0"),
		},
		Database: DatabaseConfig{
			Host:              getEnvString("DB_HOST", "localhost"),
			Port:              getEnvInt("DB_PORT", 5432),
			User:              getEnvString("DB_USER", "postgres"),
			Password:          getEnvString("DB_PASSWORD", ""),
			Database:          getEnvString("DB_NAME", "master_modes"),
			SSLMode:           getEnvString("DB_SSL_MODE", "disable"),
			MaxConns:          getEnvInt("DB_MAX_CONNS", 50),          // Оптимизировано для MMOFPS
			MinConns:          getEnvInt("DB_MIN_CONNS", 5),
			MaxConnLifetime:   getEnvDuration("DB_MAX_CONN_LIFETIME", 30*time.Minute),
			MaxConnIdleTime:   getEnvDuration("DB_MAX_CONN_IDLE_TIME", 10*time.Minute),
			HealthCheckPeriod: getEnvDuration("DB_HEALTH_CHECK_PERIOD", 30*time.Second),
		},
		Redis: RedisConfig{
			Host:         getEnvString("REDIS_HOST", "localhost"),
			Port:         getEnvInt("REDIS_PORT", 6379),
			Password:     getEnvString("REDIS_PASSWORD", ""),
			DB:           getEnvInt("REDIS_DB", 0),
			PoolSize:     getEnvInt("REDIS_POOL_SIZE", 50),         // Оптимизировано для MMOFPS
			MinIdleConns: getEnvInt("REDIS_MIN_IDLE_CONNS", 10),
			MaxConnAge:   getEnvDuration("REDIS_MAX_CONN_AGE", 30*time.Minute),
			IdleTimeout:  getEnvDuration("REDIS_IDLE_TIMEOUT", 10*time.Minute),
			ReadTimeout:  getEnvDuration("REDIS_READ_TIMEOUT", 3*time.Second),
			WriteTimeout: getEnvDuration("REDIS_WRITE_TIMEOUT", 3*time.Second),
		},
		Telemetry: TelemetryConfig{
			ServiceName:     getEnvString("SERVICE_NAME", "master-modes-service"),
			ServiceVersion:  getEnvString("SERVICE_VERSION", "1.0.0"),
			JaegerEndpoint:  getEnvString("JAEGER_ENDPOINT", "http://jaeger:14268/api/traces"),
			MetricsInterval: getEnvDuration("METRICS_INTERVAL", 30*time.Second),
			TracingEnabled:  getEnvBool("TRACING_ENABLED", true),
			MetricsEnabled:  getEnvBool("METRICS_ENABLED", true),
		},
		Security: SecurityConfig{
			JWTSecret:          getEnvString("JWT_SECRET", "your-super-secret-jwt-key-at-least-32-chars-long"),
			JWTExpiration:      getEnvDuration("JWT_EXPIRY", 24*time.Hour),
			RateLimitEnabled:   getEnvBool("RATE_LIMIT_ENABLED", true),
			RateLimitRequests:  getEnvInt("RATE_LIMIT_REQUESTS", 1000),
			RateLimitWindow:    getEnvDuration("RATE_LIMIT_WINDOW", time.Minute),
			CORSAllowedOrigins: getEnvStringSlice("CORS_ALLOWED_ORIGINS", []string{"*"}),
			APIKeyRequired:     getEnvBool("API_KEY_REQUIRED", false),
			APIKey:             getEnvString("API_KEY", ""),
		},
	}

	// Валидация конфигурации
	validate := validator.New()
	if err := validate.Struct(cfg); err != nil {
		return nil, fmt.Errorf("config validation failed: %w", err)
	}

	return cfg, nil
}

// getEnvString получает строковую переменную окружения с дефолтным значением
func getEnvString(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

// getEnvInt получает целочисленную переменную окружения с дефолтным значением
func getEnvInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}

// getEnvBool получает булеву переменную окружения с дефолтным значением
func getEnvBool(key string, defaultValue bool) bool {
	if value := os.Getenv(key); value != "" {
		if boolValue, err := strconv.ParseBool(value); err == nil {
			return boolValue
		}
	}
	return defaultValue
}

// getEnvDuration получает переменную окружения с продолжительностью с дефолтным значением
func getEnvDuration(key string, defaultValue time.Duration) time.Duration {
	if value := os.Getenv(key); value != "" {
		if duration, err := time.ParseDuration(value); err == nil {
			return duration
		}
	}
	return defaultValue
}

// getEnvStringSlice получает переменную окружения как слайс строк
func getEnvStringSlice(key string, defaultValue []string) []string {
	if value := os.Getenv(key); value != "" {
		// Простая реализация, можно улучшить для поддержки кавычек
		return []string{value}
	}
	return defaultValue
}

