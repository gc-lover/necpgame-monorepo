// Database Connection Pooling Library
// Issue: #2145, #1979
// PERFORMANCE: Query optimization, indexes, partitioning, read replicas, connection pooling, query batching
// Enterprise-grade database connection pooling for all Go services

package database

import (
	"context"
	"fmt"
	"strings"
	"time"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgconn"
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

// BatchQuery executes multiple queries in a single batch for improved performance
// PERFORMANCE: Reduces round-trips to database by batching multiple operations
// Issue: #1979
func BatchQuery(ctx context.Context, pool *pgxpool.Pool, queries []BatchQueryItem) error {
	if len(queries) == 0 {
		return nil
	}

	conn, err := pool.Acquire(ctx)
	if err != nil {
		return fmt.Errorf("failed to acquire connection: %w", err)
	}
	defer conn.Release()

	batch := &pgx.Batch{}
	for _, q := range queries {
		batch.Queue(q.Query, q.Args...)
	}

	results := conn.SendBatch(ctx, batch)
	defer results.Close()

	// Process all results to ensure batch completes
	for i := 0; i < len(queries); i++ {
		_, err := results.Exec()
		if err != nil {
			return fmt.Errorf("batch query %d failed: %w", i, err)
		}
	}

	return nil
}

// BatchQueryItem represents a single query in a batch
type BatchQueryItem struct {
	Query string
	Args  []interface{}
}

// BatchInsert performs batch insert operation for multiple rows
// PERFORMANCE: 10-100x faster than individual inserts for large datasets
// Issue: #1979
func BatchInsert(ctx context.Context, pool *pgxpool.Pool, table string, columns []string, rows [][]interface{}) error {
	if len(rows) == 0 {
		return nil
	}

	// Build batch insert query
	query := fmt.Sprintf("INSERT INTO %s (%s) VALUES ", table, strings.Join(columns, ", "))
	placeholders := make([]string, len(rows))
	args := make([]interface{}, 0, len(rows)*len(columns))

	argIndex := 1
	for i, row := range rows {
		if len(row) != len(columns) {
			return fmt.Errorf("row %d has %d values, expected %d", i, len(row), len(columns))
		}

		rowPlaceholders := make([]string, len(row))
		for j := range row {
			rowPlaceholders[j] = fmt.Sprintf("$%d", argIndex)
			args = append(args, row[j])
			argIndex++
		}
		placeholders[i] = "(" + strings.Join(rowPlaceholders, ", ") + ")"
	}

	query += strings.Join(placeholders, ", ")

	_, err := pool.Exec(ctx, query, args...)
	return err
}

// ReadWriteSplit automatically routes queries to appropriate pool (read replica for SELECT, primary for writes)
// PERFORMANCE: Reduces load on primary database by routing reads to replicas
// Issue: #1979
type ReadWriteSplit struct {
	writePool *pgxpool.Pool
	readPool  *pgxpool.Pool
}

// NewReadWriteSplit creates a read/write splitter
func NewReadWriteSplit(writePool, readPool *pgxpool.Pool) *ReadWriteSplit {
	return &ReadWriteSplit{
		writePool: writePool,
		readPool:  readPool,
	}
}

// Query executes a SELECT query on read replica
func (rws *ReadWriteSplit) Query(ctx context.Context, query string, args ...interface{}) (pgx.Rows, error) {
	if rws.readPool != nil {
		return rws.readPool.Query(ctx, query, args...)
	}
	return rws.writePool.Query(ctx, query, args...)
}

// Exec executes a write query on primary database
func (rws *ReadWriteSplit) Exec(ctx context.Context, query string, args ...interface{}) (pgconn.CommandTag, error) {
	return rws.writePool.Exec(ctx, query, args...)
}

// QueryRow executes a SELECT query on read replica
func (rws *ReadWriteSplit) QueryRow(ctx context.Context, query string, args ...interface{}) pgx.Row {
	if rws.readPool != nil {
		return rws.readPool.QueryRow(ctx, query, args...)
	}
	return rws.writePool.QueryRow(ctx, query, args...)
}
