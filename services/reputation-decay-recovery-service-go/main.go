// Package main - Reputation Decay & Recovery Service
// Issue: #2174 - Reputation Decay & Recovery mechanics
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

	"github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go/internal/handlers"
	"github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go/internal/repository"
	"github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go/internal/service"
)

func main() {
	// Initialize structured logging
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting Reputation Decay & Recovery Service",
		zap.String("version", "1.0.0"),
		zap.String("service", "reputation-decay-recovery"))

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

	// Connect to Redis
	redisClient, err := initRedis(logger)
	if err != nil {
		logger.Fatal("Failed to connect to Redis", zap.Error(err))
	}
	defer redisClient.Close()

	// Initialize repository
	repo := repository.NewRepository(db, logger)

	// Initialize service
	svc := service.NewService(service.Config{
		Repository: repo,
		Logger:     logger,
		Redis:      redisClient,
		Meter:      meter,
	})

	// Initialize handlers
	h := handlers.NewHandlers(handlers.Config{
		Service: svc,
		Logger:  logger,
	})

	// Start background workers for reputation decay
	go startDecayWorker(svc, logger)

	// Start server
	srv := &http.Server{
		Addr:         ":8085", // Different port from other services
		Handler:      h.Router(),
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Server starting", zap.String("addr", srv.Addr))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

// initMetrics initializes OpenTelemetry metrics
func initMetrics() (metric.Meter, error) {
	exporter, err := prometheus.New()
	if err != nil {
		return nil, errors.Wrap(err, "failed to create prometheus exporter")
	}

	provider := sdkmetric.NewMeterProvider(sdkmetric.WithReader(exporter))
	otel.SetMeterProvider(provider)

	meter := otel.Meter("reputation-decay-recovery-service")
	return meter, nil
}

// initDatabase initializes PostgreSQL connection
func initDatabase(logger *zap.Logger) (*pgxpool.Pool, error) {
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://user:password@localhost:5432/necp_game?sslmode=disable"
	}

	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, errors.Wrap(err, "failed to parse database config")
	}

	// Configure connection pool for MMOFPS performance
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = 30 * time.Minute
	config.MaxConnIdleTime = 10 * time.Minute

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create database pool")
	}

	// Test connection
	if err := pool.Ping(context.Background()); err != nil {
		return nil, errors.Wrap(err, "failed to ping database")
	}

	logger.Info("Database connection established")
	return pool, nil
}

// initRedis initializes Redis connection
func initRedis(logger *zap.Logger) (*redis.Client, error) {
	opt, err := redis.ParseURL(os.Getenv("REDIS_URL"))
	if err != nil {
		opt = &redis.Options{
			Addr:     "localhost:6379",
			Password: "",
			DB:       0,
		}
	}

	client := redis.NewClient(opt)

	// Test connection
	if err := client.Ping(context.Background()).Err(); err != nil {
		return nil, errors.Wrap(err, "failed to ping redis")
	}

	logger.Info("Redis connection established", zap.String("addr", opt.Addr))
	return client, nil
}

// startDecayWorker starts background worker for reputation decay processing
func startDecayWorker(svc *service.Service, logger *zap.Logger) {
	ticker := time.NewTicker(60 * time.Second) // Run every minute for MMOFPS responsiveness
	defer ticker.Stop()

	logger.Info("Starting reputation decay worker")

	for {
		select {
		case <-ticker.C:
			if err := svc.ProcessReputationDecay(context.Background()); err != nil {
				logger.Error("Failed to process reputation decay", zap.Error(err))
			}
		}
	}
}