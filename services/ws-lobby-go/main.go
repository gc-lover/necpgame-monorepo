// Issue: #2218 - Backend: Добавить unit-тесты для ws-lobby-go
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
	"ws-lobby-go/server"
)

func main() {
	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		fmt.Printf("Failed to initialize logger: %v\n", err)
		os.Exit(1)
	}
	defer logger.Sync()

	// Get configuration from environment
	port := os.Getenv("PORT")
	if port == "" {
		port = "18081"
	}

	redisURL := os.Getenv("REDIS_URL")
	if redisURL == "" {
		redisURL = "redis://localhost:6379"
	}

	databaseURL := os.Getenv("DATABASE_URL")
	if databaseURL == "" {
		databaseURL = "postgres://user:password@localhost:5432/necpgame?sslmode=disable"
	}

	// Initialize service
	svc, err := server.NewLobbyService(logger, redisURL, databaseURL)
	if err != nil {
		logger.Fatal("Failed to initialize lobby service", zap.Error(err))
	}

	// Initialize HTTP server
	srv := &http.Server{
		Addr:    ":" + port,
		Handler: svc.Router(),
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting WebSocket Lobby service", zap.String("port", port))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
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
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}
