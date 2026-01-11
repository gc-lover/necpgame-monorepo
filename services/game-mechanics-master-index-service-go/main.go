// Package main - Game Mechanics Master Index Service
// Issue: #2176 - Game Mechanics Systems Master Index
// PERFORMANCE: Optimized for MMOFPS with low latency and high concurrency
package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-faster/errors"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/exporters/prometheus"
	"go.opentelemetry.io/otel/metric"
	sdkmetric "go.opentelemetry.io/otel/sdk/metric"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/game-mechanics-master-index-service-go/internal/handlers"
	"github.com/gc-lover/necp-game/services/game-mechanics-master-index-service-go/internal/repository"
	"github.com/gc-lover/necp-game/services/game-mechanics-master-index-service-go/internal/service"
)

func main() {
	// Initialize structured logging
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting Game Mechanics Master Index Service",
		zap.String("version", "1.0.0"),
		zap.String("service", "game-mechanics-master-index"))

	// Initialize OpenTelemetry metrics
	meter, err := initMetrics()
	if err != nil {
		logger.Fatal("Failed to initialize metrics", zap.Error(err))
	}

	// Connect to PostgreSQL
	db, err := initDatabase(logger)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Connect to Redis (optional, for caching)
	redisClient := initRedis(logger)

	// Initialize repository
	repo := repository.NewPostgresRepository(db, logger)

	// Initialize service
	svc, err := service.NewService(service.Config{
		Repository:         repo,
		Logger:             logger,
		Redis:              redisClient,
		Meter:              meter,
		HealthCheckInterval: 30 * time.Second,
	})
	if err != nil {
		logger.Fatal("Failed to initialize service", zap.Error(err))
	}

	// Initialize HTTP handlers
	httpHandlers := handlers.NewHandlers(handlers.Config{
		Service: svc,
		Logger:  logger,
	})

	// Create HTTP server
	httpAddr := os.Getenv("HTTP_ADDR")
	if httpAddr == "" {
		httpAddr = ":8080"
	}

	srv := &http.Server{
		Addr:         httpAddr,
		Handler:      httpHandlers.Router(),
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start service
	go func() {
		if err := svc.Start(context.Background()); err != nil {
			logger.Fatal("Failed to start service", zap.Error(err))
		}
	}()

	// Start HTTP server
	go func() {
		logger.Info("Starting HTTP server", zap.String("addr", httpAddr))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start HTTP server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Stop service
	if err := svc.Stop(context.Background()); err != nil {
		logger.Error("Failed to stop service", zap.Error(err))
	}

	// Graceful shutdown HTTP server
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

// initDatabase initializes PostgreSQL connection pool
func initDatabase(logger *zap.Logger) (*pgxpool.Pool, error) {
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable"
	}

	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, errors.Wrap(err, "failed to parse database URL")
	}

	// Configure connection pool for MMOFPS performance
	config.MaxConns = 25
	config.MinConns = 5
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 1 * time.Minute

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create connection pool")
	}

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := pool.Ping(ctx); err != nil {
		pool.Close()
		return nil, errors.Wrap(err, "failed to ping database")
	}

	logger.Info("Database connection established",
		zap.Int("max_conns", int(config.MaxConns)),
		zap.Int("min_conns", int(config.MinConns)))

	return pool, nil
}

// initRedis initializes Redis client (optional)
func initRedis(logger *zap.Logger) *redis.Client {
	redisURL := os.Getenv("REDIS_URL")
	if redisURL == "" {
		redisURL = "redis://localhost:6379"
	}

	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		logger.Warn("Failed to parse Redis URL, Redis disabled", zap.Error(err))
		return nil
	}

	client := redis.NewClient(opt)

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := client.Ping(ctx).Err(); err != nil {
		logger.Warn("Failed to connect to Redis, Redis disabled", zap.Error(err))
		return nil
	}

	logger.Info("Redis connection established", zap.String("addr", opt.Addr))
	return client
}

// initMetrics initializes OpenTelemetry metrics
func initMetrics() (metric.Meter, error) {
	exporter, err := prometheus.New()
	if err != nil {
		return nil, errors.Wrap(err, "failed to create prometheus exporter")
	}

	provider := sdkmetric.NewMeterProvider(
		sdkmetric.WithReader(exporter),
	)

	otel.SetMeterProvider(provider)
	return provider.Meter("game-mechanics-master-index"), nil
}