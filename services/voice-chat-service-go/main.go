// Issue: #140895495
// PERFORMANCE: Optimized main entry point

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
	"voice-chat-service-go/internal/config"
	"voice-chat-service-go/server"
)

func main() {
	// Initialize logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting Voice Chat Service")

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Initialize database connection pool
	ctx := context.Background()
	dbConfig, err := pgxpool.ParseConfig(cfg.Database.DSN)
	if err != nil {
		logger.Fatal("Failed to parse database config", zap.Error(err))
	}

	// Configure connection pool settings for performance
	dbConfig.MaxConns = int32(cfg.Database.MaxConns)
	dbConfig.MinConns = int32(cfg.Database.MinConns)
	dbConfig.MaxConnLifetime = time.Hour
	dbConfig.MaxConnIdleTime = 30 * time.Minute

	dbPool, err := pgxpool.NewWithConfig(ctx, dbConfig)
	if err != nil {
		logger.Fatal("Failed to create database pool", zap.Error(err))
	}
	defer dbPool.Close()

	// Test database connection
	if err := dbPool.Ping(ctx); err != nil {
		logger.Fatal("Failed to ping database", zap.Error(err))
	}
	logger.Info("Database connection established")

	// Initialize repository and service
	repo := server.NewVoiceChatRepository(logger, dbPool)
	svc := server.NewVoiceChatService(logger, repo)

	logger.Info("Voice Chat Service initialized",
		zap.String("status", "ready"),
		zap.String("address", cfg.Server.Address))

	// Initialize HTTP server
	httpServer := &http.Server{
		Addr:         cfg.Server.Address,
		Handler:      setupRouter(svc, logger),
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in goroutine
	serverErr := make(chan error, 1)
	go func() {
		logger.Info("Voice Chat Service listening", zap.String("addr", httpServer.Addr))
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			serverErr <- err
		}
	}()

	// Wait for shutdown signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, os.Interrupt, syscall.SIGTERM)

	select {
	case err := <-serverErr:
		logger.Fatal("HTTP server error", zap.Error(err))
	case sig := <-quit:
		logger.Info("Received shutdown signal", zap.String("signal", sig.String()))
	}

	// Graceful shutdown with timeout
	shutdownCtx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Voice Chat Service stopped")
}

// setupRouter creates HTTP router with handlers
func setupRouter(svc *server.VoiceChatService, logger *zap.Logger) http.Handler {
	// TODO: Add HTTP handlers and routes when API is defined
	// For now, return a simple health check handler
	mux := http.NewServeMux()
	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"healthy","service":"voice-chat-service-go"}`))
	})
	return mux
}
