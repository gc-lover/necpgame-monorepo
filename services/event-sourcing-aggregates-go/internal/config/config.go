// Issue: #2217
package config

import (
	"os"
	"strconv"
	"strings"
	"time"
)

// Config holds all configuration for the service
type Config struct {
	Port             int
	DatabaseURL      string
	RedisURL         string
	JWTSecret        string
	Environment      string
	LogLevel         string
	KafkaBrokers     []string
	ConsumerGroup    string
	EventStoreSchema string
	SnapshotInterval int
	ProcessingWorkers int
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	port := 8082
	if p := os.Getenv("PORT"); p != "" {
		if parsed, err := strconv.Atoi(p); err == nil {
			port = parsed
		}
	}

	kafkaBrokers := []string{"localhost:9092"}
	if kb := os.Getenv("KAFKA_BROKERS"); kb != "" {
		kafkaBrokers = strings.Split(kb, ",")
	}

	consumerGroup := "event-sourcing-aggregates"
	if cg := os.Getenv("CONSUMER_GROUP"); cg != "" {
		consumerGroup = cg
	}

	eventStoreSchema := "event_store"
	if es := os.Getenv("EVENT_STORE_SCHEMA"); es != "" {
		eventStoreSchema = es
	}

	snapshotInterval := 100
	if si := os.Getenv("SNAPSHOT_INTERVAL"); si != "" {
		if parsed, err := strconv.Atoi(si); err == nil {
			snapshotInterval = parsed
		}
	}

	processingWorkers := 10
	if pw := os.Getenv("PROCESSING_WORKERS"); pw != "" {
		if parsed, err := strconv.Atoi(pw); err == nil {
			processingWorkers = parsed
		}
	}

	config := &Config{
		Port:             port,
		DatabaseURL:      getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/necpgame?sslmode=disable"),
		RedisURL:         getEnv("REDIS_URL", "redis://localhost:6379"),
		JWTSecret:        getEnv("JWT_SECRET", "your-secret-key"),
		Environment:      getEnv("ENVIRONMENT", "development"),
		LogLevel:         getEnv("LOG_LEVEL", "info"),
		KafkaBrokers:     kafkaBrokers,
		ConsumerGroup:    consumerGroup,
		EventStoreSchema: eventStoreSchema,
		SnapshotInterval: snapshotInterval,
		ProcessingWorkers: processingWorkers,
	}

	return config, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
