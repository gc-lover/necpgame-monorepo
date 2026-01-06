package repository

import (
	"context"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// Repository handles database operations.
type Repository struct {
	pool   *pgxpool.Pool
	logger *zap.Logger
}

// NewRepository creates a new repository.
func NewRepository(ctx context.Context, logger *zap.Logger, dsn string) (*Repository, error) {
	config, err := pgxpool.ParseConfig(dsn)
	if err != nil {
		return nil, err
	}

	// PERFORMANCE: Optimized DB pool settings
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = time.Hour
	config.MaxConnIdleTime = 30 * time.Minute

	pool, err := pgxpool.NewWithConfig(ctx, config)
	if err != nil {
		return nil, err
	}

	return &Repository{
		pool:   pool,
		logger: logger,
	}, nil
}

// HealthCheck checks database health.
func (r *Repository) HealthCheck(ctx context.Context) error {
	// PERFORMANCE: Use context timeout
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	return r.pool.Ping(ctx)
}

// Close closes the connection pool.
func (r *Repository) Close() {
	r.pool.Close()
}
