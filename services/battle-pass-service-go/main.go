package main

import (
	"context"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"go.uber.org/zap"

	"battle-pass-service-go/internal/api"
	"battle-pass-service-go/internal/config"
	"battle-pass-service-go/internal/database"
	"battle-pass-service-go/internal/redis"
)

func main() {
	// Initialize logger
	logger, err := zap.NewProduction()
	if err != nil {
		panic(fmt.Sprintf("failed to create logger: %v", err))
	}
	defer logger.Sync()

	sugar := logger.Sugar()
	sugar.Info("Starting Battle Pass Service")

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		sugar.Fatalf("Failed to load config: %v", err)
	}

	// Initialize database
	db, err := database.NewConnection(cfg.Database)
	if err != nil {
		sugar.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Initialize Redis
	rdb, err := redis.NewClient(cfg.Redis)
	if err != nil {
		sugar.Fatalf("Failed to connect to Redis: %v", err)
	}
	defer rdb.Close()

	// Create API server
	server := api.NewServer(cfg, db, rdb, logger)

	// Start server in goroutine
	go func() {
		addr := fmt.Sprintf("%s:%d", cfg.Server.Host, cfg.Server.Port)
		sugar.Infof("Server starting on %s", addr)

		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			sugar.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	sugar.Info("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := server.Shutdown(ctx); err != nil {
		sugar.Fatalf("Server forced to shutdown: %v", err)
	}

	sugar.Info("Server exited")
}