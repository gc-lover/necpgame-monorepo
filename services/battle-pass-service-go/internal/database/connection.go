package database

import (
	"context"
	"fmt"
	"time"

	"github.com/go-faster/errors"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame/services/battle-pass-service-go/internal/config"
)

// DB представляет подключение к базе данных
type DB struct {
	pool   *pgxpool.Pool
	logger *zap.Logger
	cfg    *config.DatabaseConfig
}

// NewConnection создает новое подключение к PostgreSQL
func NewConnection(ctx context.Context, cfg *config.DatabaseConfig, logger *zap.Logger) (*DB, error) {
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

	// Оптимизации для MMOFPS
	dbConfig.MaxConns = int32(cfg.MaxConns)
	dbConfig.MinConns = int32(cfg.MinConns)

	if cfg.MaxConnLifetime != "" {
		if duration, err := time.ParseDuration(cfg.MaxConnLifetime); err == nil {
			dbConfig.MaxConnLifetime = duration
		}
	}

	if cfg.MaxConnIdleTime != "" {
		if duration, err := time.ParseDuration(cfg.MaxConnIdleTime); err == nil {
			dbConfig.MaxConnIdleTime = duration
		}
	}

	if cfg.HealthCheckPeriod != "" {
		if duration, err := time.ParseDuration(cfg.HealthCheckPeriod); err == nil {
			dbConfig.HealthCheckPeriod = duration
		}
	}

	pool, err := pgxpool.NewWithConfig(ctx, dbConfig)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create connection pool")
	}

	db := &DB{
		pool:   pool,
		logger: logger,
		cfg:    cfg,
	}

	// Проверка подключения
	if err := db.Ping(ctx); err != nil {
		pool.Close()
		return nil, errors.Wrap(err, "failed to ping database")
	}

	logger.Info("Database connection established",
		zap.String("host", cfg.Host),
		zap.Int("port", cfg.Port),
		zap.String("database", cfg.Database),
		zap.Int("max_conns", cfg.MaxConns),
		zap.Int("min_conns", cfg.MinConns))

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
		db.logger.Info("Database connection closed")
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