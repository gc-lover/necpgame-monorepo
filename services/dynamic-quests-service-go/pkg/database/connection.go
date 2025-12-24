// Database connection package with MMOFPS optimizations
// Issue: #2244
// Agent: Backend

package database

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	_ "github.com/lib/pq"
	"go.uber.org/zap"

	"necpgame/services/dynamic-quests-service-go/internal/config"
)

// NewConnection creates a new PostgreSQL database connection with optimized settings
func NewConnection(cfg config.DatabaseConfig) (*sql.DB, error) {
	// Build connection string
	connStr := fmt.Sprintf(
		"host=%s port=%d user=%s password=%s dbname=%s sslmode=%s",
		cfg.Host,
		cfg.Port,
		cfg.User,
		cfg.Password,
		cfg.Database,
		cfg.SSLMode,
	)

	// Open database connection
	db, err := sql.Open("postgres", connStr)
	if err != nil {
		return nil, fmt.Errorf("failed to open database connection: %w", err)
	}

	// Configure connection pool for MMOFPS performance
	// Max open connections: 25 (optimized for concurrent quest operations)
	db.SetMaxOpenConns(cfg.MaxOpenConns)

	// Max idle connections: 25 (keep connections ready for hot path operations)
	db.SetMaxIdleConns(cfg.MaxIdleConns)

	// Connection max lifetime: 5 minutes (prevent stale connections)
	db.SetConnMaxLifetime(cfg.ConnMaxLifetime)

	// Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		db.Close()
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return db, nil
}

// HealthCheck performs a database health check
func HealthCheck(db *sql.DB) error {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	// Simple query to verify database connectivity
	_, err := db.ExecContext(ctx, "SELECT 1")
	if err != nil {
		return fmt.Errorf("database health check failed: %w", err)
	}

	return nil
}

// Close closes the database connection
func Close(db *sql.DB, logger *zap.SugaredLogger) {
	if err := db.Close(); err != nil {
		logger.Errorf("Error closing database connection: %v", err)
	} else {
		logger.Info("Database connection closed successfully")
	}
}
