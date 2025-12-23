// Issue: #2236
package server

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/trading-core-service-go/pkg/api"
)

// TradingCoreService implements the trading core business logic
type TradingCoreService struct {
	db     *sql.DB
	repo   *TradingRepository
	redis  *RedisClient // For caching and session management

	// Performance optimizations
	mu     sync.RWMutex
	pool   *sync.Pool // Memory pool for trade objects

	// Metrics and monitoring
	metrics *MetricsCollector
}

// NewTradingCoreService creates a new trading core service instance
func NewTradingCoreService() (*TradingCoreService, error) {
	// Initialize database connection with connection pooling
	db, err := initDatabase()
	if err != nil {
		return nil, fmt.Errorf("failed to initialize database: %w", err)
	}

	// Initialize Redis for caching
	redis, err := initRedis()
	if err != nil {
		return nil, fmt.Errorf("failed to initialize Redis: %w", err)
	}

	// Initialize metrics
	metrics := NewMetricsCollector()

	// Create memory pool for trade objects
	pool := &sync.Pool{
		New: func() interface{} {
			return &api.TradeResult{}
		},
	}

	repo := NewTradingRepository(db, redis)

	return &TradingCoreService{
		db:      db,
		repo:    repo,
		redis:   redis,
		pool:    pool,
		metrics: metrics,
	}, nil
}

// initDatabase initializes PostgreSQL connection with optimized settings
func initDatabase() (*sql.DB, error) {
	// BACKEND NOTE: Database connection pooling for MMOFPS performance
	// Pool size: 25-50 connections based on load testing
	db, err := sql.Open("postgres", "postgres://user:password@localhost/trading_db?sslmode=disable")
	if err != nil {
		return nil, err
	}

	// Performance optimizations
	db.SetMaxOpenConns(50)        // Maximum open connections
	db.SetMaxIdleConns(25)        // Maximum idle connections
	db.SetConnMaxLifetime(time.Hour) // Connection lifetime

	// Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return db, nil
}

// initRedis initializes Redis connection for caching
func initRedis() (*RedisClient, error) {
	// BACKEND NOTE: Redis for hot path caching
	// TTL: 5 minutes for market data, 30 minutes for user sessions
	client := NewRedisClient("localhost:6379")
	if err := client.Ping(context.Background()); err != nil {
		return nil, err
	}
	return client, nil
}

// Health check endpoint implementation
func (s *TradingCoreService) HealthCheck(ctx context.Context) (api.TradingCoreHealthCheckRes, error) {
	// BACKEND NOTE: Health check with database connectivity test
	now := time.Now()

	// Test database connectivity
	if err := s.db.PingContext(ctx); err != nil {
		// For unhealthy status, return nil to indicate error
		return nil, fmt.Errorf("service unhealthy: database connection failed")
	}

	// Test Redis connectivity
	if err := s.redis.Ping(ctx); err != nil {
		log.Printf("Redis health check failed: %v", err)
	}

	// Set version
	var version api.OptString
	version.SetTo("1.0.0")

	return &api.HealthResponse{
		Service:   "trading-core-service",
		Version:   version,
		Status:    api.HealthResponseStatusHealthy,
		Timestamp: now,
	}, nil
}
