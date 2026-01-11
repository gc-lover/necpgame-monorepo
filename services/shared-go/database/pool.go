// Database Connection Pooling Library
// Issue: #2145
// PERFORMANCE: Query optimization, indexes, partitioning, read replicas, connection pooling
// Enterprise-grade database connection pooling for all Go services

package database

import (
	"context"
	"fmt"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// PoolConfig holds database connection pool configuration
type PoolConfig struct {
	// Connection settings
	Host     string
	Port     int
	User     string
	Password string
	Database string
	SSLMode  string

	// Connection pool settings
	MaxConns        int
	MinConns        int
	MaxConnLifetime time.Duration
	MaxConnIdleTime time.Duration
	HealthCheckPeriod time.Duration

	// Query settings
	StatementCacheCapacity int
	PreparedStatementCacheEnabled bool

	// Read replica settings (for read/write splitting)
	ReadReplicaHost     string
	ReadReplicaPort     int
	ReadReplicaEnabled  bool
}

// DefaultPoolConfig returns default database pool configuration
func DefaultPoolConfig() PoolConfig {
	return PoolConfig{
		Host:     "localhost",
		Port:     5432,
		User:     "postgres",
		Password: "",
		Database: "necpgame",
		SSLMode:  "disable",

		// Optimized for MMOFPS (100k+ concurrent users)
		MaxConns:        50,
		MinConns:        10,
		MaxConnLifetime: 30 * time.Minute,
		MaxConnIdleTime: 10 * time.Minute,
		HealthCheckPeriod: 30 * time.Second,

		// Query optimization
		StatementCacheCapacity:       250,
		PreparedStatementCacheEnabled: true,

		// Read replicas disabled by default
		ReadReplicaEnabled: false,
	}
}

// NewPool creates a new database connection pool with optimized settings
func NewPool(ctx context.Context, config PoolConfig, logger *zap.Logger) (*pgxpool.Pool, error) {
	dsn := fmt.Sprintf(
		"host=%s port=%d user=%s password=%s dbname=%s sslmode=%s",
		config.Host, config.Port, config.User, config.Password, config.Database, config.SSLMode,
	)

	poolConfig, err := pgxpool.ParseConfig(dsn)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database config: %w", err)
	}

	// Connection pool optimization
	poolConfig.MaxConns = int32(config.MaxConns)
	poolConfig.MinConns = int32(config.MinConns)
	poolConfig.MaxConnLifetime = config.MaxConnLifetime
	poolConfig.MaxConnIdleTime = config.MaxConnIdleTime
	poolConfig.HealthCheckPeriod = config.HealthCheckPeriod

	// Query optimization
	poolConfig.ConnConfig.DefaultQueryExecMode = pgxpool.QueryExecModeExec
	if config.PreparedStatementCacheEnabled {
		poolConfig.ConnConfig.DefaultQueryExecMode = pgxpool.QueryExecModeCacheStatement
	}

	// Create pool
	pool, err := pgxpool.NewWithConfig(ctx, poolConfig)
	if err != nil {
		return nil, fmt.Errorf("failed to create connection pool: %w", err)
	}

	// Test connection
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	if err := pool.Ping(ctx); err != nil {
		pool.Close()
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	logger.Info("Database connection pool created",
		zap.String("host", config.Host),
		zap.Int("port", config.Port),
		zap.String("database", config.Database),
		zap.Int("max_conns", config.MaxConns),
		zap.Int("min_conns", config.MinConns))

	return pool, nil
}

// ReadReplicaPool creates a read replica connection pool
func NewReadReplicaPool(ctx context.Context, config PoolConfig, logger *zap.Logger) (*pgxpool.Pool, error) {
	if !config.ReadReplicaEnabled {
		return nil, fmt.Errorf("read replica not enabled")
	}

	readConfig := config
	readConfig.Host = config.ReadReplicaHost
	readConfig.Port = config.ReadReplicaPort

	return NewPool(ctx, readConfig, logger)
}

// PoolStats provides connection pool statistics
type PoolStats struct {
	MaxConns        int32
	AcquiredConns   int32
	IdleConns       int32
	ConstructingConns int32
	TotalConns      int32
}

// GetPoolStats returns current pool statistics
func GetPoolStats(pool *pgxpool.Pool) PoolStats {
	stats := pool.Stat()
	return PoolStats{
		MaxConns:        stats.MaxConns(),
		AcquiredConns:   stats.AcquiredConns(),
		IdleConns:       stats.IdleConns(),
		ConstructingConns: stats.ConstructingConns(),
		TotalConns:      stats.TotalConns(),
	}
}
