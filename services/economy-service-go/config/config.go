package config

import (
	"fmt"
	"os"
	"strconv"
	"time"
)

type Config struct {
	Server   ServerConfig
	Database DatabaseConfig
	JWT      JWTConfig
	Redis    RedisConfig
	Kafka    KafkaConfig // Added for event-driven architecture - Issue #2237
}

type ServerConfig struct {
	Port            string
	ReadTimeout     time.Duration
	WriteTimeout    time.Duration
	IdleTimeout     time.Duration
	ReadHeaderTimeout time.Duration // BACKEND NOTE: Fast header processing for economy requests
	MaxHeaderBytes  int
}

type DatabaseConfig struct {
	Host            string
	Port            string
	User            string
	Password        string
	DBName          string
	SSLMode         string
	MaxConns        int
	MinConns        int
	MaxConnLifetime time.Duration
	MaxConnIdleTime time.Duration
	HealthCheckPeriod time.Duration
}

type JWTConfig struct {
	Secret     string
	Expiration time.Duration
}

type RedisConfig struct {
	Host         string
	Port         string
	Password     string
	DB           int
	PoolSize     int // BACKEND NOTE: Redis connection pool size for MMOFPS
	MinIdleConns int // BACKEND NOTE: Minimum idle connections to maintain
}

// KafkaConfig holds Kafka consumer configuration for event-driven architecture
// Issue: #2237
type KafkaConfig struct {
	Brokers            []string      // Kafka broker addresses
	GroupID            string        // Consumer group ID
	SessionTimeout     time.Duration // Consumer session timeout
	HeartbeatInterval  time.Duration // Consumer heartbeat interval
	CommitInterval     time.Duration // Offset commit interval
	MaxProcessingTime  time.Duration // Maximum time to process a message
}

func (db DatabaseConfig) GetDSN() string {
	return fmt.Sprintf("postgres://%s:%s@%s:%s/%s?sslmode=%s",
		db.User, db.Password, db.Host, db.Port, db.DBName, db.SSLMode)
}

func Load() *Config {
	return &Config{
		Server: ServerConfig{
			Port:              getEnv("PORT", ":8083"),
			ReadTimeout:       15 * time.Second, // BACKEND NOTE: Increased for complex economy operations
			WriteTimeout:      15 * time.Second, // BACKEND NOTE: For economy transaction responses
			IdleTimeout:       120 * time.Second, // BACKEND NOTE: Keep connections alive for economy sessions
			ReadHeaderTimeout: 3 * time.Second, // BACKEND NOTE: Fast header processing for economy requests
			MaxHeaderBytes:    1 << 20, // BACKEND NOTE: 1MB max headers for security
		},
		Database: DatabaseConfig{
			Host:     getEnv("DB_HOST", "localhost"),
			Port:     getEnv("DB_PORT", "5432"),
			User:     getEnv("DB_USER", "economy_user"),
			Password: getEnv("DB_PASSWORD", "economy_password"),
			DBName:   getEnv("DB_NAME", "economy_db"),
			SSLMode:  getEnv("DB_SSLMODE", "disable"),
			// BACKEND NOTE: Enterprise-grade database pool optimization for MMOFPS economy operations
			MaxConns: 50, // BACKEND NOTE: High pool for economy transactions (50 max connections)
			MinConns: 10, // BACKEND NOTE: Keep minimum connections ready for instant economy access
			MaxConnLifetime: 30 * time.Minute, // BACKEND NOTE: Shorter lifetime for real-time economy ops
			MaxConnIdleTime: 5 * time.Minute,  // BACKEND NOTE: Quick cleanup for active economy sessions
			HealthCheckPeriod: 1 * time.Minute,
		},
		JWT: JWTConfig{
			Secret:     getEnv("JWT_SECRET", "economy-secret-key"),
			Expiration: time.Duration(getEnvInt("JWT_EXPIRATION_HOURS", 24)) * time.Hour,
		},
		Redis: RedisConfig{
			Host:         getEnv("REDIS_HOST", "localhost"),
			Port:         getEnv("REDIS_PORT", "6379"),
			Password:     getEnv("REDIS_PASSWORD", ""),
			DB:           getEnvInt("REDIS_DB", 1),
			// BACKEND NOTE: Enterprise-grade Redis pool for MMOFPS economy caching
			PoolSize:     getEnvInt("REDIS_POOL_SIZE", 25),     // BACKEND NOTE: High pool for economy session caching
			MinIdleConns: getEnvInt("REDIS_MIN_IDLE_CONNS", 8), // BACKEND NOTE: Keep connections ready for instant economy access
		},
		Kafka: KafkaConfig{ // Event-driven architecture configuration - Issue #2237
			Brokers:            []string{getEnv("KAFKA_BROKERS", "localhost:9092")},
			GroupID:            getEnv("KAFKA_GROUP_ID", "economy-service-tick-consumer"),
			SessionTimeout:     time.Duration(getEnvInt("KAFKA_SESSION_TIMEOUT_SEC", 30)) * time.Second,
			HeartbeatInterval:  time.Duration(getEnvInt("KAFKA_HEARTBEAT_INTERVAL_SEC", 3)) * time.Second,
			CommitInterval:     time.Duration(getEnvInt("KAFKA_COMMIT_INTERVAL_MS", 1000)) * time.Millisecond,
			MaxProcessingTime:  time.Duration(getEnvInt("KAFKA_MAX_PROCESSING_TIME_SEC", 30)) * time.Second,
		},
	}
}

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