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
	Port         string
	ReadTimeout  time.Duration
	WriteTimeout time.Duration
	IdleTimeout  time.Duration
	MaxHeaderBytes int
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
	Host     string
	Port     string
	Password string
	DB       int
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
			Port:         getEnv("PORT", ":8083"),
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  120 * time.Second,
			MaxHeaderBytes: 1 << 20, // 1MB
		},
		Database: DatabaseConfig{
			Host:     getEnv("DB_HOST", "localhost"),
			Port:     getEnv("DB_PORT", "5432"),
			User:     getEnv("DB_USER", "economy_user"),
			Password: getEnv("DB_PASSWORD", "economy_password"),
			DBName:   getEnv("DB_NAME", "economy_db"),
			SSLMode:  getEnv("DB_SSLMODE", "disable"),
			MaxConns: 25,
			MinConns: 5,
			MaxConnLifetime: 1 * time.Hour,
			MaxConnIdleTime: 30 * time.Minute,
			HealthCheckPeriod: 1 * time.Minute,
		},
		JWT: JWTConfig{
			Secret:     getEnv("JWT_SECRET", "economy-secret-key"),
			Expiration: time.Duration(getEnvInt("JWT_EXPIRATION_HOURS", 24)) * time.Hour,
		},
		Redis: RedisConfig{
			Host:     getEnv("REDIS_HOST", "localhost"),
			Port:     getEnv("REDIS_PORT", "6379"),
			Password: getEnv("REDIS_PASSWORD", ""),
			DB:       getEnvInt("REDIS_DB", 1),
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