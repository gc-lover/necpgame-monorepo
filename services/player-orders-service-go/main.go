// Player Orders World Impact Service
// Issue: #140894810
//
// This service tracks and calculates the effects of player orders on the game world,
// including economic impacts, social changes, political shifts, and city development.

package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/necp-game/player-orders-service-go/pkg/world-impact"
	"github.com/necp-game/player-orders-service-go/server"
	"go.uber.org/zap"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to create logger:", err)
	}
	defer logger.Sync()

	// Create world impact service
	impactSvc, err := worldimpact.NewService(logger)
	if err != nil {
		logger.Fatal("Failed to create world impact service", zap.Error(err))
	}

	// Create HTTP server
	srv := server.NewServer(impactSvc, logger)

	// Start server in goroutine
	go func() {
		logger.Info("Starting Player Orders World Impact Service on :8080")
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


