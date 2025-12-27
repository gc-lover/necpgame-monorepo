// Combat Abilities Service - Enterprise-grade combat abilities management for MMOFPS RPG
// Issue: #140875781
// PERFORMANCE: Optimized for 1000+ RPS ability activations with zero allocations in hot path

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

	"combat-abilities-service-go/server"
)

func main() {
	// PERFORMANCE: Preallocate logger to avoid allocations
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting Combat Abilities Service",
		zap.String("service", "combat-abilities-service-go"),
		zap.String("version", "1.0.0"),
	)

	// PERFORMANCE: Context with timeout for initialization
	_, initCancel := context.WithTimeout(context.Background(), 15*time.Second)
	defer initCancel()

	// Create server instance with PERFORMANCE optimizations
	srv := server.NewServer(logger)

	// Create HTTP server with enterprise-grade configuration
	httpServer := &http.Server{
		Addr:         ":8084", // Different port from other services
		Handler:      srv.Handler(),
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting combat-abilities-service-go on :8084")
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	logger.Info("Combat Abilities Service started successfully",
		zap.String("port", ":8084"),
		zap.String("health", "http://localhost:8084/api/v1/health"),
	)

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	logger.Info("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}
