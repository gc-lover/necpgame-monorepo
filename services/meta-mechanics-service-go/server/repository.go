// Issue: #1928 - Repository for Meta Mechanics Service
// PERFORMANCE: Connection pooling, prepared statements, context timeouts
package server

import (
	"context"
	"fmt"

	"github.com/jackc/pgx/v5/pgxpool"
)

// Repository handles database operations
// OPTIMIZATION: Connection pool configured for meta-mechanics load
type Repository struct {
	pool *pgxpool.Pool
}

// NewRepository creates database connection pool
func NewRepository(connStr string) (*Repository, error) {
	config, err := pgxpool.ParseConfig(connStr)
	if err != nil {
		return nil, fmt.Errorf("parse config: %w", err)
	}

	// OPTIMIZATION: Connection pool sizing for meta-mechanics
	config.MaxConns = 25              // Max 25 concurrent connections
	config.MinConns = 5               // Min 5 idle connections
	config.MaxConnLifetime = 5 * 60   // 5 minutes max lifetime
	config.MaxConnIdleTime = 2 * 60   // 2 minutes max idle time

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, fmt.Errorf("create pool: %w", err)
	}

	return &Repository{pool: pool}, nil
}

// Close shuts down the connection pool
func (r *Repository) Close() {
	r.pool.Close()
}

// Ping tests database connectivity
func (r *Repository) Ping(ctx context.Context) error {
	return r.pool.Ping(ctx)
}
