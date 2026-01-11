// Redis Cluster Configuration Library
// Issue: #2153
// PERFORMANCE: Redis cluster setup, connection pooling, high availability

package memory

import (
	"context"
	"fmt"
	"time"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"
)

// RedisClusterConfig holds Redis cluster configuration
type RedisClusterConfig struct {
	// Cluster addresses
	Addrs []string

	// Authentication
	Password string
	Username string

	// Connection pool settings
	PoolSize     int
	MinIdleConns int
	MaxRetries   int

	// Timeouts
	DialTimeout  time.Duration
	ReadTimeout  time.Duration
	WriteTimeout time.Duration

	// Connection settings
	MaxConnAge        time.Duration
	PoolTimeout       time.Duration
	IdleTimeout       time.Duration
	IdleCheckFrequency time.Duration

	// Cluster settings
	EnableRedirects bool
	ReadOnly        bool
	RouteByLatency  bool
	RouteRandomly   bool
}

// DefaultRedisClusterConfig returns default Redis cluster configuration
func DefaultRedisClusterConfig() RedisClusterConfig {
	return RedisClusterConfig{
		Addrs:             []string{"localhost:6379"},
		PoolSize:          25,
		MinIdleConns:      8,
		MaxRetries:        3,
		DialTimeout:       5 * time.Second,
		ReadTimeout:       3 * time.Second,
		WriteTimeout:      3 * time.Second,
		MaxConnAge:        30 * time.Minute,
		PoolTimeout:       4 * time.Second,
		IdleTimeout:       5 * time.Minute,
		IdleCheckFrequency: 1 * time.Minute,
		EnableRedirects:  true,
		ReadOnly:         false,
		RouteByLatency:   false,
		RouteRandomly:    false,
	}
}

// NewRedisClusterClient creates a new Redis cluster client with optimized settings
func NewRedisClusterClient(config RedisClusterConfig, logger *zap.Logger) (*redis.ClusterClient, error) {
	clusterClient := redis.NewClusterClient(&redis.ClusterOptions{
		Addrs:    config.Addrs,
		Password: config.Password,
		Username: config.Username,

		// Connection pool optimization
		PoolSize:     config.PoolSize,
		MinIdleConns: config.MinIdleConns,
		MaxRetries:   config.MaxRetries,

		// Timeouts
		DialTimeout:  config.DialTimeout,
		ReadTimeout:  config.ReadTimeout,
		WriteTimeout: config.WriteTimeout,

		// Connection lifecycle
		MaxConnAge:        config.MaxConnAge,
		PoolTimeout:       config.PoolTimeout,
		IdleTimeout:       config.IdleTimeout,
		IdleCheckFrequency: config.IdleCheckFrequency,

		// Cluster-specific settings
		EnableRedirects: config.EnableRedirects,
		ReadOnly:        config.ReadOnly,
		RouteByLatency:  config.RouteByLatency,
		RouteRandomly:   config.RouteRandomly,
	})

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := clusterClient.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to connect to Redis cluster: %w", err)
	}

	logger.Info("Redis cluster client created",
		zap.Strings("addrs", config.Addrs),
		zap.Int("pool_size", config.PoolSize),
		zap.Int("min_idle_conns", config.MinIdleConns))

	return clusterClient, nil
}

// RedisClientConfig holds single Redis instance configuration
type RedisClientConfig struct {
	Addr     string
	Password string
	DB       int

	// Connection pool settings
	PoolSize     int
	MinIdleConns int
	MaxRetries   int

	// Timeouts
	DialTimeout  time.Duration
	ReadTimeout  time.Duration
	WriteTimeout time.Duration

	// Connection settings
	MaxConnAge        time.Duration
	PoolTimeout       time.Duration
	IdleTimeout       time.Duration
	IdleCheckFrequency time.Duration
}

// DefaultRedisClientConfig returns default Redis client configuration
func DefaultRedisClientConfig() RedisClientConfig {
	return RedisClientConfig{
		Addr:              "localhost:6379",
		PoolSize:          25,
		MinIdleConns:      8,
		MaxRetries:        3,
		DialTimeout:       5 * time.Second,
		ReadTimeout:       3 * time.Second,
		WriteTimeout:      3 * time.Second,
		MaxConnAge:        30 * time.Minute,
		PoolTimeout:       4 * time.Second,
		IdleTimeout:       5 * time.Minute,
		IdleCheckFrequency: 1 * time.Minute,
	}
}

// NewRedisClient creates a new Redis client with optimized settings
func NewRedisClient(config RedisClientConfig, logger *zap.Logger) (*redis.Client, error) {
	client := redis.NewClient(&redis.Options{
		Addr:     config.Addr,
		Password: config.Password,
		DB:       config.DB,

		// Connection pool optimization
		PoolSize:     config.PoolSize,
		MinIdleConns: config.MinIdleConns,
		MaxRetries:   config.MaxRetries,

		// Timeouts
		DialTimeout:  config.DialTimeout,
		ReadTimeout:  config.ReadTimeout,
		WriteTimeout: config.WriteTimeout,

		// Connection lifecycle
		MaxConnAge:        config.MaxConnAge,
		PoolTimeout:       config.PoolTimeout,
		IdleTimeout:       config.IdleTimeout,
		IdleCheckFrequency: config.IdleCheckFrequency,
	})

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := client.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to connect to Redis: %w", err)
	}

	logger.Info("Redis client created",
		zap.String("addr", config.Addr),
		zap.Int("pool_size", config.PoolSize),
		zap.Int("min_idle_conns", config.MinIdleConns))

	return client, nil
}
