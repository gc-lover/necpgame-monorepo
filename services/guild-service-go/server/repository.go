//go:align 64
// Issue: #2290

package server

import (
	"context"
	"database/sql"
	"sync"
	"time"

	"guild-service-go/pkg/api"

	_ "github.com/lib/pq" // PostgreSQL driver
)

// GuildRepository handles data persistence with PERFORMANCE optimizations
// PERFORMANCE: Struct aligned (pointers first)
type GuildRepository struct {
	db        *sql.DB
	prepared  map[string]*sql.Stmt
	pool      *sync.Pool
	maxConns  int

	// PERFORMANCE: Prepared statements cache
	preparedMu sync.RWMutex

	// Padding for struct alignment
	_pad [64]byte
}

// NewGuildRepository creates optimized repository instance
func NewGuildRepository(config *Config) *GuildRepository {
	return &GuildRepository{
		prepared: make(map[string]*sql.Stmt, 10), // Pre-allocate capacity
		pool: &sync.Pool{
			New: func() interface{} {
				return make([]interface{}, 0, 100) // Pre-allocate slice capacity
			},
		},
		maxConns: config.MaxWorkers, // Match worker pool size
	}
}

// InitDB initializes database connection with PERFORMANCE optimizations
func (r *GuildRepository) InitDB(dsn string) error {
	var err error
	r.db, err = sql.Open("postgres", dsn)
	if err != nil {
		return err
	}

	// PERFORMANCE: Optimize connection pool
	r.db.SetMaxOpenConns(r.maxConns)     // Limit concurrent connections
	r.db.SetMaxIdleConns(r.maxConns / 2) // Keep some idle connections
	r.db.SetConnMaxLifetime(time.Hour)   // Rotate connections

	// PERFORMANCE: Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	return r.db.PingContext(ctx)
}

// HealthCheck performs database health check with PERFORMANCE optimizations
func (r *GuildRepository) HealthCheck(ctx context.Context) error {
	if r.db == nil {
		return sql.ErrNoRows
	}

	// PERFORMANCE: Ping with context timeout
	pingCtx, cancel := context.WithTimeout(ctx, 1*time.Second)
	defer cancel()

	return r.db.PingContext(pingCtx)
}

// prepareStmt prepares and caches SQL statements
func (r *GuildRepository) prepareStmt(ctx context.Context, name, query string) (*sql.Stmt, error) {
	r.preparedMu.RLock()
	if stmt, exists := r.prepared[name]; exists {
		r.preparedMu.RUnlock()
		return stmt, nil
	}
	r.preparedMu.RUnlock()

	r.preparedMu.Lock()
	defer r.preparedMu.Unlock()

	// Double-check after acquiring write lock
	if stmt, exists := r.prepared[name]; exists {
		return stmt, nil
	}

	stmt, err := r.db.PrepareContext(ctx, query)
	if err != nil {
		return nil, err
	}

	r.prepared[name] = stmt
	return stmt, nil
}

// Close closes database connections and cleans up resources
func (r *GuildRepository) Close() error {
	// PERFORMANCE: Close prepared statements
	r.preparedMu.Lock()
	for _, stmt := range r.prepared {
		stmt.Close()
	}
	r.preparedMu.Unlock()

	if r.db != nil {
		return r.db.Close()
	}
	return nil
}