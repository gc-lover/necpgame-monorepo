package database

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	_ "github.com/lib/pq"
	"go.uber.org/zap"

	"necpgame/services/tournament-service-go/internal/config"
)

// Manager manages database connections and operations
type Manager struct {
	db     *sql.DB
	logger *zap.Logger
	config *config.DatabaseConfig
}

// NewManager creates a new database manager with connection pooling
func NewManager(cfg *config.DatabaseConfig, logger *zap.Logger) (*Manager, error) {
	dsn := cfg.GetDatabaseDSN()

	db, err := sql.Open("postgres", dsn)
	if err != nil {
		return nil, fmt.Errorf("failed to open database connection: %w", err)
	}

	// Configure connection pooling
	db.SetMaxOpenConns(cfg.MaxOpenConns)
	db.SetMaxIdleConns(cfg.MaxIdleConns)
	db.SetConnMaxLifetime(cfg.MaxLifetime)

	// Test connection with context timeout
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		db.Close()
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	logger.Info("Database connection established",
		zap.String("host", cfg.Host),
		zap.Int("port", cfg.Port),
		zap.String("database", cfg.Database),
		zap.Int("maxOpenConns", cfg.MaxOpenConns),
		zap.Int("maxIdleConns", cfg.MaxIdleConns),
		zap.Duration("maxLifetime", cfg.MaxLifetime))

	return &Manager{
		db:     db,
		logger: logger,
		config: cfg,
	}, nil
}

// Close closes the database connection
func (m *Manager) Close() error {
	if m.db != nil {
		return m.db.Close()
	}
	return nil
}

// GetDB returns the underlying database connection
func (m *Manager) GetDB() *sql.DB {
	return m.db
}

// Ping tests the database connection with timeout
func (m *Manager) Ping(ctx context.Context) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	return m.db.PingContext(ctx)
}

// Stats returns database connection pool statistics
func (m *Manager) Stats() sql.DBStats {
	return m.db.Stats()
}

// ExecuteInTransaction executes a function within a database transaction
func (m *Manager) ExecuteInTransaction(ctx context.Context, fn func(*sql.Tx) error) error {
	tx, err := m.db.BeginTx(ctx, nil)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}

	defer func() {
		if p := recover(); p != nil {
			tx.Rollback()
			panic(p)
		}
	}()

	if err := fn(tx); err != nil {
		if rbErr := tx.Rollback(); rbErr != nil {
			m.logger.Error("Failed to rollback transaction",
				zap.Error(rbErr), zap.Error(err))
			return fmt.Errorf("transaction failed and rollback failed: %w (rollback: %v)", err, rbErr)
		}
		return err
	}

	if err := tx.Commit(); err != nil {
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
	err := m.db.QueryRowContext(ctx, "SELECT 1").Scan(&result)
	if err != nil {
		return fmt.Errorf("database query test failed: %w", err)
	}

	if result != 1 {
		return fmt.Errorf("database query test returned unexpected result: %d", result)
	}

	stats := m.Stats()
	m.logger.Debug("Database health check passed",
		zap.Int("openConnections", stats.OpenConnections),
		zap.Int("inUse", stats.InUse),
		zap.Int("idle", stats.Idle),
		zap.Int64("waitCount", stats.WaitCount),
		zap.Duration("waitDuration", stats.WaitDuration))

	return nil
}