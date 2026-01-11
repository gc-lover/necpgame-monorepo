//go:align 64
package main

import (
	"context"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-faster/errors"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"github.com/NECPGAME/combat-system-service-go/internal/handlers"
	"github.com/NECPGAME/combat-system-service-go/internal/repository"
	"github.com/NECPGAME/combat-system-service-go/internal/service"
	"github.com/NECPGAME/combat-system-service-go/pkg/api"
)

//go:align 64
func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		panic(fmt.Sprintf("failed to create logger: %v", err))
	}
	defer logger.Sync()

	logger.Info("Starting combat-system-service-go")

	// Database configuration
	dbURL := getEnvOrDefault("DATABASE_URL", "postgres://user:password@localhost:5432/necpgame?sslmode=disable")
	dbPool, err := initDatabase(dbURL, logger)
	if err != nil {
		logger.Fatal("Failed to initialize database", zap.Error(err))
	}
	defer dbPool.Close()

	// Redis configuration
	redisAddr := getEnvOrDefault("REDIS_URL", "localhost:6379")
	redisClient := initRedis(redisAddr, logger)
	defer redisClient.Close()

	// Repository
	repo := repository.NewPostgresRepository(dbPool, logger)

	// Service configuration
	serviceConfig := service.Config{
		MaxAbilityNameLength:      100,
		MaxAbilityDescription:     1000,
		DefaultCooldownMs:         1000,
		DefaultCastTimeMs:         0,
		DamageCalculationTimeout:  5 * time.Second,
		MaxConcurrentCalculations: 1000,
	}

	svc, err := service.NewService(repo, logger, serviceConfig)
	if err != nil {
		logger.Fatal("Failed to create service", zap.Error(err))
	}

	// Handlers
	handlerConfig := handlers.Config{
		Service: svc,
		Logger:  logger,
	}
	h := handlers.NewHandlers(handlerConfig)

	// HTTP Server
	server, err := api.NewServer(h,
		api.WithPathPrefix("/api/v1/combat-system"),
		api.WithMiddleware(func(req *http.Request, next http.Handler) http.Handler {
			return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
				start := time.Now()

				// Add CORS headers
				w.Header().Set("Access-Control-Allow-Origin", "*")
				w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
				w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

				// Handle preflight requests
				if r.Method == "OPTIONS" {
					w.WriteHeader(http.StatusOK)
					return
				}

				// Add request logging
				logger.Info("HTTP Request",
					zap.String("method", r.Method),
					zap.String("path", r.URL.Path),
					zap.String("remote_addr", r.RemoteAddr))

				next.ServeHTTP(w, r)

				logger.Info("HTTP Response",
					zap.String("method", r.Method),
					zap.String("path", r.URL.Path),
					zap.Duration("duration", time.Since(start)),
					zap.Int("status", 200)) // Would need proper status capture
			})
		}),
	)
	if err != nil {
		logger.Fatal("Failed to create HTTP server", zap.Error(err))
	}

	// Server configuration
	port := getEnvOrDefault("PORT", "8080")
	addr := fmt.Sprintf(":%s", port)

	// Add Prometheus metrics endpoint
	http.Handle("/metrics", promhttp.Handler())

	httpServer := &http.Server{
		Addr:         addr,
		Handler:      server,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting HTTP server", zap.String("addr", addr))
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start HTTP server", zap.Error(err))
		}
	}()

	// Graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

//go:align 64
func initDatabase(dbURL string, logger *zap.Logger) (*pgxpool.Pool, error) {
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, errors.Wrap(err, "failed to parse database URL")
	}

	// Connection pool settings optimized for MMOFPS combat calculations
	config.MaxConns = 100
	config.MinConns = 20
	config.MaxConnLifetime = 30 * time.Minute
	config.MaxConnIdleTime = 5 * time.Minute

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create database pool")
	}

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := pool.Ping(ctx); err != nil {
		pool.Close()
		return nil, errors.Wrap(err, "failed to ping database")
	}

	logger.Info("Database connection established")
	return pool, nil
}

//go:align 64
func initRedis(addr string, logger *zap.Logger) *redis.Client {
	client := redis.NewClient(&redis.Options{
		Addr:         addr,
		Password:     "",
		DB:           0,
		DialTimeout:  5 * time.Second,
		ReadTimeout:  3 * time.Second,
		WriteTimeout: 3 * time.Second,
		PoolSize:     50,
		MinIdleConns: 10,
	})

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if _, err := client.Ping(ctx).Result(); err != nil {
		logger.Error("Failed to connect to Redis", zap.Error(err))
		return nil
	}

	logger.Info("Redis connection established")
	return client
}

//go:align 64
func getEnvOrDefault(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}