// Social Orders Creation Service
// Issue: #140894825
//
// This service provides advanced order creation with reputation integration,
// validation, optimization, and contractor suggestions for MMOFPS RPG.

package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/necp-game/social-orders-creation-service-go/pkg/orders-creation"
	"github.com/necp-game/social-orders-creation-service-go/server"
	"go.uber.org/zap"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to create logger:", err)
	}
	defer logger.Sync()

	// Create orders creation service
	creationSvc, err := orderscreation.NewService(logger)
	if err != nil {
		logger.Fatal("Failed to create orders creation service", zap.Error(err))
	}

	// Create HTTP server
	srv := server.NewServer(creationSvc, logger)

	// Start server in goroutine
	go func() {
		logger.Info("Starting Social Orders Creation Service on :8080")
		if err := srv.Start(":8080"); err != nil {
			logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}
