package database

import (
	"context"
	"fmt"

	"github.com/go-faster/errors"
	"github.com/jackc/pgx/v5/pgxpool"

	"battle-pass-service-go/internal/config"
)

// DB представляет подключение к базе данных
type DB struct {
	pool *pgxpool.Pool
	cfg  *config.DatabaseConfig
}

// NewConnection создает новое подключение к PostgreSQL
func NewConnection(ctx context.Context, cfg *config.DatabaseConfig) (*DB, error) {
	dbConfig, err := pgxpool.ParseConfig(fmt.Sprintf(
		"postgres://%s:%s@%s:%d/%s?sslmode=%s",
		cfg.User,
		cfg.Password,
		cfg.Host,
		cfg.Port,
		cfg.Database,
		cfg.SSLMode,
	))
	if err != nil {
		return nil, errors.Wrap(err, "failed to parse database config")
	}

	// MMOFPS Optimization: Connection pool sizing for seasonal loads
	dbConfig.MaxConns = int32(cfg.MaxOpenConns)
	dbConfig.MinConns = int32(cfg.MaxIdleConns)
	dbConfig.MaxConnLifetime = cfg.MaxLifetime

	pool, err := pgxpool.NewWithConfig(ctx, dbConfig)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create connection pool")
	}

	db := &DB{
		pool: pool,
		cfg:  cfg,
	}

	// Проверка подключения
	if err := db.Ping(ctx); err != nil {
		pool.Close()
		return nil, errors.Wrap(err, "failed to ping database")
	}

	// MMOFPS: Connection pool configured for seasonal battle pass loads

	return db, nil
}

// Ping проверяет подключение к базе данных
func (db *DB) Ping(ctx context.Context) error {
	return db.pool.Ping(ctx)
}

// Close закрывает подключение к базе данных
func (db *DB) Close() {
	if db.pool != nil {
		db.pool.Close()
	}
}

// Pool возвращает пул соединений
func (db *DB) Pool() *pgxpool.Pool {
	return db.pool
}

// GetConfig возвращает конфигурацию базы данных
func (db *DB) GetConfig() *config.DatabaseConfig {
	return db.cfg
}