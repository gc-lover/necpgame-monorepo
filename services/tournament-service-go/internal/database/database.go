package database

import (
	"context"
	"fmt"
	"time"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"necpgame/services/tournament-service-go/internal/config"
)

// Manager manages database connections and operations
type Manager struct {
	db     *pgxpool.Pool
	logger *zap.Logger
	config *config.DatabaseConfig
}

// NewManager creates a new database manager with connection pooling
func NewManager(cfg *config.DatabaseConfig, logger *zap.Logger) (*Manager, error) {
	dsn := cfg.GetDatabaseDSN()

	// Parse connection config for pgxpool
	config, err := pgxpool.ParseConfig(dsn)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database config: %w", err)
	}

	// Configure connection pool for high-performance tournament operations
	config.MaxConns = int32(cfg.MaxOpenConns)
	config.MinConns = int32(cfg.MaxIdleConns)
	config.MaxConnLifetime = cfg.MaxLifetime

	db, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, fmt.Errorf("failed to create database connection pool: %w", err)
	}

	// Test connection with context timeout
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := db.Ping(ctx); err != nil {
		db.Close()
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	logger.Info("Database connection established",
		zap.String("host", cfg.Host),
		zap.Int("port", cfg.Port),
		zap.String("database", cfg.Database),
		zap.Int32("maxConns", config.MaxConns),
		zap.Int32("minConns", config.MinConns),
		zap.Duration("maxLifetime", config.MaxConnLifetime))

	return &Manager{
		db:     db,
		logger: logger,
		config: cfg,
	}, nil
}

// Close closes the database connection
func (m *Manager) Close() error {
	if m.db != nil {
		m.db.Close()
	}
	return nil
}

// GetDB returns the underlying database connection
func (m *Manager) GetDB() *pgxpool.Pool {
	return m.db
}

// Ping tests the database connection with timeout
func (m *Manager) Ping(ctx context.Context) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	return m.db.Ping(ctx)
}

// Stats returns database connection pool statistics (simplified for pgxpool)
func (m *Manager) Stats() map[string]interface{} {
	// pgxpool doesn't expose detailed stats like sql.DB
	return map[string]interface{}{
		"pool_size": "unknown", // pgxpool doesn't expose this
		"open_connections": "unknown",
		"in_use": "unknown",
		"idle": "unknown",
		"wait_count": 0,
		"wait_duration": "0s",
	}
}

// ExecuteInTransaction executes a function within a database transaction
func (m *Manager) ExecuteInTransaction(ctx context.Context, fn func(pgx.Tx) error) error {
	tx, err := m.db.Begin(ctx)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}

	defer func() {
		if p := recover(); p != nil {
			tx.Rollback(ctx)
			panic(p)
		}
	}()

	if err := fn(tx); err != nil {
		if rbErr := tx.Rollback(ctx); rbErr != nil {
			m.logger.Error("Failed to rollback transaction",
				zap.Error(rbErr), zap.Error(err))
			return fmt.Errorf("transaction failed and rollback failed: %w (rollback: %v)", err, rbErr)
		}
		return err
	}

	if err := tx.Commit(ctx); err != nil {
		return fmt.Errorf("failed to commit transaction: %w", err)
	}

	return nil
}

// HealthCheck performs a comprehensive database health check
func (m *Manager) HealthCheck(ctx context.Context) error {
	// Test connection
	if err := m.Ping(ctx); err != nil {
		return fmt.Errorf("database ping failed: %w", err)
	}

	// Test simple query
	var result int
	err := m.db.QueryRow(ctx, "SELECT 1").Scan(&result)
	if err != nil {
		return fmt.Errorf("database query test failed: %w", err)
	}

	if result != 1 {
		return fmt.Errorf("database query test returned unexpected result: %d", result)
	}

	stats := m.Stats()
	m.logger.Debug("Database health check passed",
		zap.Any("connectionStats", stats))

	return nil
}