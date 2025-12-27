// Social Orders Reputation Integration Service
// Issue: #140894823
//
// This service integrates player orders with reputation system in MMOFPS RPG.
// It calculates order costs based on reputation, validates reputation requirements,
// and applies reputation changes after order completion.

package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/necp-game/social-orders-reputation-integration-service-go/pkg/orders-reputation"
	"github.com/necp-game/social-orders-reputation-integration-service-go/server"
	"go.uber.org/zap"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to create logger:", err)
	}
	defer logger.Sync()

	// Create orders reputation integration service
	integrationSvc, err := ordersreputation.NewService(logger)
	if err != nil {
		logger.Fatal("Failed to create orders reputation integration service", zap.Error(err))
	}

	// Create HTTP server
	srv := server.NewServer(integrationSvc, logger)

	// Start server in goroutine
	go func() {
		logger.Info("Starting Social Orders Reputation Integration Service on :8080")
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
