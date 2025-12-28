package internal

import (
	"fmt"
	"os"
	"strconv"
	"time"
)

// Config holds all configuration for the Global State Manager
type Config struct {
	// Server configuration
	ServerPort string
	ReadTimeout time.Duration
	WriteTimeout time.Duration

	// Database configuration
	PostgresURL string
	PostgresMaxConns int
	PostgresMinConns int

	// Redis configuration
	RedisAddrs []string
	RedisPassword string
	RedisDB int

	// Kafka configuration
	KafkaBrokers []string
	KafkaTopic string

	// Performance tuning
	MemoryPoolSize int
	CacheTTL time.Duration
	BatchSize int

	// Circuit breaker
	CircuitBreakerMaxRequests uint32
	CircuitBreakerTimeout time.Duration
}

// LoadConfig loads configuration from environment variables
func LoadConfig() (*Config, error) {
	config := &Config{
		// Server defaults
		ServerPort:   getEnv("SERVER_PORT", "8080"),
		ReadTimeout:  getDurationEnv("READ_TIMEOUT", 30*time.Second),
		WriteTimeout: getDurationEnv("WRITE_TIMEOUT", 30*time.Second),

		// Database defaults
		PostgresURL:      getEnv("POSTGRES_URL", "postgresql://postgres:postgres@localhost:5432/necpgame"),
		PostgresMaxConns: getIntEnv("POSTGRES_MAX_CONNS", 50),
		PostgresMinConns: getIntEnv("POSTGRES_MIN_CONNS", 5),

		// Redis defaults
		RedisAddrs:    []string{getEnv("REDIS_ADDR", "localhost:6379")},
		RedisPassword: getEnv("REDIS_PASSWORD", ""),
		RedisDB:       getIntEnv("REDIS_DB", 0),

		// Kafka defaults
		KafkaBrokers: []string{getEnv("KAFKA_BROKERS", "localhost:9092")},
		KafkaTopic:   getEnv("KAFKA_TOPIC", "global.state.events"),

		// Performance defaults
		MemoryPoolSize: getIntEnv("MEMORY_POOL_SIZE", 1000),
		CacheTTL:       getDurationEnv("CACHE_TTL", 5*time.Minute),
		BatchSize:      getIntEnv("BATCH_SIZE", 100),

		// Circuit breaker defaults
		CircuitBreakerMaxRequests: getUint32Env("CIRCUIT_BREAKER_MAX_REQUESTS", 100),
		CircuitBreakerTimeout:     getDurationEnv("CIRCUIT_BREAKER_TIMEOUT", 10*time.Second),
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

func getIntEnv(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}

func getUint32Env(key string, defaultValue uint32) uint32 {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return uint32(intValue)
		}
	}
	return defaultValue
}

func getDurationEnv(key string, defaultValue time.Duration) time.Duration {
	if value := os.Getenv(key); value != "" {
		if duration, err := time.ParseDuration(value); err == nil {
			return duration
		}
	}
	return defaultValue
}

// Validate checks if the configuration is valid
func (c *Config) Validate() error {
	if c.ServerPort == "" {
		return fmt.Errorf("server port cannot be empty")
	}

	if c.PostgresURL == "" {
		return fmt.Errorf("postgres URL cannot be empty")
	}

	if len(c.RedisAddrs) == 0 {
		return fmt.Errorf("redis addresses cannot be empty")
	}

	if len(c.KafkaBrokers) == 0 {
		return fmt.Errorf("kafka brokers cannot be empty")
	}

	if c.MemoryPoolSize <= 0 {
		return fmt.Errorf("memory pool size must be positive")
	}

	if c.CacheTTL <= 0 {
		return fmt.Errorf("cache TTL must be positive")
	}

	if c.BatchSize <= 0 {
		return fmt.Errorf("batch size must be positive")
	}

	return nil
}
