// Integration Testing Utilities
// Issue: #2144
// PERFORMANCE: Integration test helpers for database, Redis, HTTP

package testing

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"
)

// IntegrationTestEnvironment provides test environment setup
type IntegrationTestEnvironment struct {
	DB      *pgxpool.Pool
	Redis   *redis.Client
	HTTP    *http.Client
	Logger  *zap.Logger
	Cleanup func()
}

// SetupIntegrationEnvironment sets up integration test environment
func SetupIntegrationEnvironment(ctx context.Context, dbURL, redisURL string, logger *zap.Logger) (*IntegrationTestEnvironment, error) {
	env := &IntegrationTestEnvironment{
		Logger: logger,
		HTTP: &http.Client{
			Timeout: 10 * time.Second,
		},
		Cleanup: func() {},
	}

	// Setup database
	if dbURL != "" {
		pool, err := pgxpool.New(ctx, dbURL)
		if err != nil {
			return nil, fmt.Errorf("failed to connect to test database: %w", err)
		}

		// Test connection
		if err := pool.Ping(ctx); err != nil {
			pool.Close()
			return nil, fmt.Errorf("failed to ping test database: %w", err)
		}

		env.DB = pool
		env.Cleanup = func() {
			pool.Close()
		}
	}

	// Setup Redis
	if redisURL != "" {
		opt, err := redis.ParseURL(redisURL)
		if err != nil {
			return nil, fmt.Errorf("failed to parse Redis URL: %w", err)
		}

		client := redis.NewClient(opt)
		if err := client.Ping(ctx).Err(); err != nil {
			client.Close()
			return nil, fmt.Errorf("failed to ping test Redis: %w", err)
		}

		env.Redis = client
		oldCleanup := env.Cleanup
		env.Cleanup = func() {
			client.Close()
			oldCleanup()
		}
	}

	return env, nil
}

// CleanupDatabase cleans up test data from database
func CleanupDatabase(ctx context.Context, pool *pgxpool.Pool, tables []string) error {
	for _, table := range tables {
		query := fmt.Sprintf("TRUNCATE TABLE %s CASCADE", table)
		if _, err := pool.Exec(ctx, query); err != nil {
			return fmt.Errorf("failed to truncate table %s: %w", table, err)
		}
	}
	return nil
}

// CleanupRedis cleans up test data from Redis
func CleanupRedis(ctx context.Context, client *redis.Client, patterns []string) error {
	for _, pattern := range patterns {
		iter := client.Scan(ctx, 0, pattern, 0).Iterator()
		for iter.Next(ctx) {
			if err := client.Del(ctx, iter.Val()).Err(); err != nil {
				return fmt.Errorf("failed to delete key %s: %w", iter.Val(), err)
			}
		}
		if err := iter.Err(); err != nil {
			return fmt.Errorf("failed to scan keys: %w", err)
		}
	}
	return nil
}

// WaitForService waits for a service to be available
func WaitForService(ctx context.Context, url string, timeout time.Duration) error {
	deadline := time.Now().Add(timeout)
	client := &http.Client{
		Timeout: 1 * time.Second,
	}

	for time.Now().Before(deadline) {
		req, err := http.NewRequestWithContext(ctx, "GET", url, nil)
		if err != nil {
			return err
		}

		resp, err := client.Do(req)
		if err == nil && resp.StatusCode == http.StatusOK {
			resp.Body.Close()
			return nil
		}

		time.Sleep(100 * time.Millisecond)
	}

	return fmt.Errorf("service not available: %s", url)
}
