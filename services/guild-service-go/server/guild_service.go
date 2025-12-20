// Issue: #1943
package server

import (
	"fmt"
	"os"

	"github.com/redis/go-redis/v9"
)

// NewGuildService initializes the guild service with all dependencies
func NewGuildService() (*Service, error) {
	// Initialize repository
	repo := NewRepository()

	// Initialize Redis client
	redisURL := getEnv("REDIS_URL", "redis://localhost:6379/11")
	opts, err := redis.ParseURL(redisURL)
	if err != nil {
		GetLogger().WithError(err).Warn("Failed to parse Redis URL, continuing without Redis")
		opts = nil
	}

	var redisClient *redis.Client
	if opts != nil {
		redisClient = redis.NewClient(opts)

		// Test Redis connection
		if err := redisClient.Ping(ctx.Background()).Err(); err != nil {
			GetLogger().WithError(err).Warn("Failed to connect to Redis, continuing without cache")
			redisClient = nil
		}
	}

	// Initialize service with optimizations
	service := NewService(repo, redisClient)

	GetLogger().Info("Guild service initialized successfully")
	return service, nil
}

// getEnv gets environment variable or returns default value
func getEnv(key, defaultValue string) string {
	if value, exists := os.LookupEnv(key); exists {
		return value
	}
	return defaultValue
}
