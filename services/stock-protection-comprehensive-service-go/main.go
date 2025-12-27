// Stock Protection Comprehensive Service
// Issue: #140894825
//
// This service provides comprehensive stock market protection including
// fraud detection, manipulation prevention, and regulatory compliance.

package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/necp-game/stock-protection-comprehensive-service-go/pkg/stock-protection"
	"github.com/necp-game/stock-protection-comprehensive-service-go/server"
	"go.uber.org/zap"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to create logger:", err)
	}
	defer logger.Sync()

	// Create stock protection service
	protectionSvc, err := stockprotection.NewService(logger)
	if err != nil {
		logger.Fatal("Failed to create stock protection service", zap.Error(err))
	}

	// Create HTTP server
	srv := server.NewServer(protectionSvc, logger)

	// Start server in goroutine
	go func() {
		logger.Info("Starting Stock Protection Comprehensive Service on :8080")
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
