//go:generate go run github.com/ogen-go/ogen/cmd/ogen@latest --target . --package interactive_objects --clean ../../proto/openapi/interactive-objects-service.yaml

package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	_ "net/http/pprof" // Import for pprof profiling
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/jackc/pgx/v4/pgxpool"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"necpgame/services/interactive-objects-service-go/internal/config"
	"necpgame/services/interactive-objects-service-go/internal/handler"
	"necpgame/services/interactive-objects-service-go/internal/repository"
	"necpgame/services/interactive-objects-service-go/internal/service"
)

func main() {
	ctx := context.Background()

	cfg, err := config.Load()
	if err != nil {
		log.Fatalf("Failed to load config: %v", err)
	}
	defer cfg.Logger.Sync() // Flush logs on exit

	cfg.Logger.Info("Starting Interactive Objects Service",
		zap.String("service", cfg.ServiceName),
		zap.String("environment", cfg.Environment),
		zap.Int("port", cfg.Port))

	db, err := initDatabase(ctx, cfg.DatabaseURL, cfg.Logger)
	if err != nil {
		cfg.Logger.Fatal("Failed to initialize database", zap.Error(err))
	}
	defer db.Close()

	rdb := initRedis(cfg.RedisURL, cfg.Logger)

	interactiveService := service.NewInteractiveService(repository.NewRepository(db, rdb))
	h := handler.NewHandler(interactiveService)

	srv := &http.Server{
		Addr:         fmt.Sprintf(":%d", cfg.Port),
		Handler:      h.Router(),
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start metrics server
	go func() {
		metricsSrv := &http.Server{
			Addr:    ":9092",
			Handler: promhttp.Handler(),
		}
		cfg.Logger.Info("Starting metrics server", zap.String("addr", ":9092"))
		if err := metricsSrv.ListenAndServe(); err != nil {
			cfg.Logger.Error("Metrics server error", zap.Error(err))
		}
	}()

	// Start profiling server
	go func() {
		profilingSrv := &http.Server{
			Addr:    ":6060",
			Handler: nil, // Default mux with pprof handlers
		}
		cfg.Logger.Info("Starting profiling server", zap.String("addr", ":6060"))
		if err := profilingSrv.ListenAndServe(); err != nil {
			cfg.Logger.Error("Profiling server error", zap.Error(err))
		}
	}()

	go func() {
		cfg.Logger.Info("Starting Interactive Objects Service",
			zap.Int("port", cfg.Port))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			cfg.Logger.Fatal("Server error", zap.Error(err))
		}
	}()

	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	cfg.Logger.Info("Shutting down server...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		cfg.Logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	cfg.Logger.Info("Server exited")
}

func initDatabase(ctx context.Context, url string, logger *zap.Logger) (*pgxpool.Pool, error) {
	config, err := pgxpool.ParseConfig(url)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database config: %w", err)
	}

	// Optimized DB pool config for MMOFPS performance
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = time.Hour
	config.MaxConnIdleTime = 30 * time.Minute

	logger.Info("Initializing database connection pool",
		zap.Int32("max_conns", config.MaxConns),
		zap.Int32("min_conns", config.MinConns))

	pool, err := pgxpool.ConnectConfig(ctx, config)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to database: %w", err)
	}

	if err := pool.Ping(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	logger.Info("Database connection established")
	return pool, nil
}

func initRedis(url string, logger *zap.Logger) *redis.Client {
	opt, err := redis.ParseURL(url)
	if err != nil {
		logger.Fatal("Failed to parse Redis URL", zap.Error(err))
	}

	rdb := redis.NewClient(opt)

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := rdb.Ping(ctx).Err(); err != nil {
		logger.Fatal("Failed to connect to Redis", zap.Error(err))
	}

	logger.Info("Redis connection established")
	return rdb
}
