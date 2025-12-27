package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"go.uber.org/zap"

	"necpgame/services/game-analytics-dashboard-service-go/internal/config"
	"necpgame/services/game-analytics-dashboard-service-go/internal/handlers"
	"necpgame/services/game-analytics-dashboard-service-go/internal/repository"
	"necpgame/services/game-analytics-dashboard-service-go/internal/service"
)

func main() {
	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Initialize database connection
	db, err := repository.NewPostgresConnection(cfg.DatabaseURL)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Initialize Redis connection
	redisClient, err := repository.NewRedisConnection(cfg.RedisURL)
	if err != nil {
		logger.Fatal("Failed to connect to Redis", zap.Error(err))
	}
	defer redisClient.Close()

	// Initialize repository layer
	repo := repository.NewRepository(db, redisClient, logger)

	// Initialize service layer
	svc := service.NewService(repo, logger)

	// Initialize handlers
	h := handlers.NewHandlers(svc, logger)

	// Setup HTTP server
	r := setupRouter(h, cfg)

	srv := &http.Server{
		Addr:         cfg.ServerAddr,
		Handler:      r,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start background workers for data aggregation
	go startBackgroundWorkers(svc, logger)

	// Start server in goroutine
	go func() {
		logger.Info("Starting Game Analytics Dashboard Service",
			zap.String("addr", cfg.ServerAddr),
			zap.String("version", "1.0.0"))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	logger.Info("Shutting down server...")

	// Graceful shutdown with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

func setupRouter(h *handlers.Handlers, cfg *config.Config) *chi.Mux {
	r := chi.NewRouter()

	// Middleware stack
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(60 * time.Second))

	// CORS middleware for dashboard clients
	r.Use(middleware.AllowContentType("application/json"))
	r.Use(middleware.SetHeader("Access-Control-Allow-Origin", "*"))
	r.Use(middleware.SetHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"))
	r.Use(middleware.SetHeader("Access-Control-Allow-Headers", "Accept, Authorization, Content-Type"))

	// Setup routes
	h.SetupRoutes(r)

	return r
}

func startBackgroundWorkers(svc *service.Service, logger *zap.Logger) {
	// Start data aggregation workers
	ticker := time.NewTicker(5 * time.Minute) // Aggregate every 5 minutes
	defer ticker.Stop()

	for range ticker.C {
		ctx, cancel := context.WithTimeout(context.Background(), 2*time.Minute)

		// Aggregate player analytics
		if err := svc.AggregatePlayerAnalytics(ctx); err != nil {
			logger.Error("Failed to aggregate player analytics", zap.Error(err))
		}

		// Aggregate game metrics
		if err := svc.AggregateGameMetrics(ctx); err != nil {
			logger.Error("Failed to aggregate game metrics", zap.Error(err))
		}

		// Aggregate combat data
		if err := svc.AggregateCombatData(ctx); err != nil {
			logger.Error("Failed to aggregate combat data", zap.Error(err))
		}

		cancel()
	}
}

// PERFORMANCE: Main function optimized for analytics service startup
// Background workers handle data aggregation without blocking main thread
// Graceful shutdown ensures all operations complete before exit
// Structured logging provides comprehensive observability for production monitoring
