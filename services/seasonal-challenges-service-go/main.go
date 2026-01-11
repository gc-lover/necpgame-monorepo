// Seasonal Challenges Service - Enterprise-grade MMOFPS seasonal event management
// Issue: #1506
package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"go.uber.org/zap"
	"necpgame/services/seasonal-challenges-service-go/server"
)

func main() {
	// Initialize structured logging for production MMOFPS monitoring
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	sugar := logger.Sugar()
	sugar.Info("Starting Seasonal Challenges Service")

	// Create HTTP server with enterprise-grade optimizations
	srv := server.NewServer(logger)

	// Graceful shutdown handling for zero-downtime deployments
	go func() {
		sugar.Info("Server starting on :8080")
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			sugar.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	sugar.Info("Shutting down server...")

	// Graceful shutdown with 30 second timeout (MMOFPS requirement)
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		sugar.Fatalf("Server forced to shutdown: %v", err)
	}

	sugar.Info("Server exited")
}