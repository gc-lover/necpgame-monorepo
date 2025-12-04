// Issue: #1595
package server

import (
	"database/sql"
	"time"

	_ "github.com/lib/pq"
)

// Repository handles database operations
type Repository struct {
	db *sql.DB
}

// NewRepository creates new repository
func NewRepository(connStr string) (*Repository, error) {
	db, err := sql.Open("postgres", connStr)
	if err != nil {
		return nil, err
	}

	// Connection pool settings for performance (Issue #1605)
	db.SetMaxOpenConns(25)
	db.SetMaxIdleConns(25)
	db.SetConnMaxLifetime(5 * time.Minute)
	db.SetConnMaxIdleTime(10 * time.Minute)

	if err := db.Ping(); err != nil {
		return nil, err
	}

	return &Repository{db: db}, nil
}

// Close closes database connection
func (r *Repository) Close() error {
	return r.db.Close()
}

// TODO: Add database methods as needed

