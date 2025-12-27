// Social NPC Hiring World Impact Service
// Issue: #140894831
//
// This service tracks and calculates the effects of NPC hiring on the game world,
// including economic impacts, social changes, political shifts, and city development.

package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/necp-game/social-npc-hiring-world-impact-service-go/pkg/npc-hiring-impact"
	"github.com/necp-game/social-npc-hiring-world-impact-service-go/server"
	"go.uber.org/zap"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to create logger:", err)
	}
	defer logger.Sync()

	// Create NPC hiring world impact service
	impactSvc, err := npchiringimpact.NewService(logger)
	if err != nil {
		logger.Fatal("Failed to create NPC hiring world impact service", zap.Error(err))
	}

	// Create HTTP server
	srv := server.NewServer(impactSvc, logger)

	// Start server in goroutine
	go func() {
		logger.Info("Starting Social NPC Hiring World Impact Service on :8080")
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
