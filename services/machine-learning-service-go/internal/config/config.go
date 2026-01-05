// Issue: #2266 - Refactor system-domain AI services
// PERFORMANCE: Optimized configuration with validation and environment handling

package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all configuration for the machine learning service
// PERFORMANCE: Precomputed values to avoid runtime calculations
type Config struct {
	// Database configuration with optimized connection settings
	Database DatabaseConfig

	// Server configuration with performance tunings
	Server ServerConfig

	// ML-specific configuration for model operations
	ML MLConfig
}

// DatabaseConfig holds database connection settings
type DatabaseConfig struct {
	Host         string
	Port         int
	User         string
	Password     string
	Database     string
	SSLMode      string

	// PERFORMANCE: Optimized connection pool settings (BLOCKER requirement)
	MaxOpenConns    int           // 50 connections for ML service
	MaxIdleConns    int           // 10 idle connections
	ConnMaxLifetime time.Duration // 30 minutes
}

// ServerConfig holds HTTP server settings
type ServerConfig struct {
	Host string
	Port int

	// PERFORMANCE: Timeout settings for ML operations
	ReadTimeout  time.Duration // 30s for ML computations
	WriteTimeout time.Duration // 15s for responses
	IdleTimeout  time.Duration // 120s for ML sessions

	// PERFORMANCE: GC tuning for ML workloads
	GOGC int // 50 for low-latency ML inferences
}

// MLConfig holds ML-specific settings for model operations
type MLConfig struct {
	// PERFORMANCE: Preallocated pools for ML operations
	ModelPoolSize    int // 1000 models
	PredictionPoolSize int // 5000 predictions

	// ML processing timeouts for model operations
	InferenceTimeout time.Duration // 5s for real-time predictions
	TrainingTimeout  time.Duration // 30m for model training
	BatchTimeout     time.Duration // 10s for batch processing

	// Model settings for ML service
	BatchSize       int // 128 for batch processing
	MaxModels       int // 1000 active models
	MaxFeatures     int // 1000 input features
	DefaultAccuracy float64 // 0.95 minimum accuracy
}

// LoadConfig loads configuration from environment variables with defaults
func LoadConfig() *Config {
	return &Config{
		Database: DatabaseConfig{
			Host:            getEnv("DB_HOST", "localhost"),
			Port:            getEnvInt("DB_PORT", 5432),
			User:            getEnv("DB_USER", "ml_service"),
			Password:        getEnv("DB_PASSWORD", ""),
			Database:        getEnv("DB_NAME", "ml_service"),
			SSLMode:         getEnv("DB_SSLMODE", "disable"),
			MaxOpenConns:    getEnvInt("DB_MAX_OPEN_CONNS", 50),
			MaxIdleConns:    getEnvInt("DB_MAX_IDLE_CONNS", 10),
			ConnMaxLifetime: getEnvDuration("DB_CONN_MAX_LIFETIME", 30*time.Minute),
		},
		Server: ServerConfig{
			Host:         getEnv("SERVER_HOST", "0.0.0.0"),
			Port:         getEnvInt("SERVER_PORT", 8080),
			ReadTimeout:  getEnvDuration("SERVER_READ_TIMEOUT", 30*time.Second),
			WriteTimeout: getEnvDuration("SERVER_WRITE_TIMEOUT", 15*time.Second),
			IdleTimeout:  getEnvDuration("SERVER_IDLE_TIMEOUT", 120*time.Second),
			GOGC:         getEnvInt("GOGC", 50),
		},
		ML: MLConfig{
			ModelPoolSize:      getEnvInt("ML_MODEL_POOL_SIZE", 1000),
			PredictionPoolSize: getEnvInt("ML_PREDICTION_POOL_SIZE", 5000),
			InferenceTimeout:   getEnvDuration("ML_INFERENCE_TIMEOUT", 5*time.Second),
			TrainingTimeout:    getEnvDuration("ML_TRAINING_TIMEOUT", 30*time.Minute),
			BatchTimeout:       getEnvDuration("ML_BATCH_TIMEOUT", 10*time.Second),
			BatchSize:          getEnvInt("ML_BATCH_SIZE", 128),
			MaxModels:          getEnvInt("ML_MAX_MODELS", 1000),
			MaxFeatures:        getEnvInt("ML_MAX_FEATURES", 1000),
			DefaultAccuracy:    getEnvFloat("ML_DEFAULT_ACCURACY", 0.95),
		},
	}
}

// PERFORMANCE: Helper functions for environment variable parsing
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}

func getEnvDuration(key string, defaultValue time.Duration) time.Duration {
	if value := os.Getenv(key); value != "" {
		if duration, err := time.ParseDuration(value); err == nil {
			return duration
		}
	}
	return defaultValue
}

func getEnvFloat(key string, defaultValue float64) float64 {
	if value := os.Getenv(key); value != "" {
		if floatValue, err := strconv.ParseFloat(value, 64); err == nil {
			return floatValue
		}
	}
	return defaultValue
}
