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

	"necpgame/services/ability-service-go/config"
	"necpgame/services/ability-service-go/internal/repository"
	"necpgame/services/ability-service-go/internal/service"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to create logger", err)
	}
	defer logger.Sync()

	// Load configuration
	cfg := config.Load()

	// Initialize database connection with enterprise-grade pool optimization
	ctx := context.Background()
	repo, err := repository.NewRepository(ctx, logger, cfg.Database.GetDSN(), cfg.Database)
	if err != nil {
		logger.Fatal("Failed to initialize repository", zap.Error(err))
	}
	defer repo.Close()

	// Initialize service
	svc := service.NewService(logger, repo, cfg)

	// Create enterprise-grade HTTP server with MMOFPS optimizations
	srv := &http.Server{
		Addr:           cfg.Server.Port,
		Handler:        svc,
		ReadTimeout:    15 * time.Second, // Increased for complex ability operations
		WriteTimeout:   15 * time.Second, // For ability activation responses
		IdleTimeout:    120 * time.Second, // Keep connections alive for performance
		MaxHeaderBytes: 1 << 20, // 1MB max headers for security
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting ability service", zap.String("port", cfg.Server.Port))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Graceful shutdown with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}