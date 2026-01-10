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

	"necpgame/services/adaptive-system-service-go/config"
	"necpgame/services/adaptive-system-service-go/internal/repository"
	"necpgame/services/adaptive-system-service-go/internal/service"
	api "necpgame/services/adaptive-system-service-go"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to create logger", err)
	}
	defer logger.Sync()

	// Load enterprise-grade configuration
	cfg := config.Load()

	// Initialize database connection with optimized pool
	ctx := context.Background()
	repo, err := repository.NewRepository(ctx, logger, cfg.Database.GetDSN(), cfg.Database)
	if err != nil {
		logger.Fatal("Failed to initialize repository", zap.Error(err))
	}
	defer repo.Close()

	// Initialize adaptive service with machine learning capabilities
	svc := service.NewService(logger, repo, cfg)

	// Create API handlers with generated interfaces
	handler := service.NewHandler(logger, svc)

	// Create security handler
	securityHandler := service.NewSecurityHandler(svc)

	// Create ogen server with enterprise-grade API handling
	apiServer, err := api.NewServer(handler, securityHandler)
	if err != nil {
		logger.Fatal("Failed to create API server", zap.Error(err))
	}

	// Create HTTP server with enterprise-grade optimizations
	srv := &http.Server{
		Addr:           cfg.Server.Port,
		Handler:        apiServer,
		ReadTimeout:    cfg.Server.ReadTimeout,
		WriteTimeout:   cfg.Server.WriteTimeout,
		IdleTimeout:    120 * time.Second, // Keep connections alive for performance
		MaxHeaderBytes: 1 << 20, // 1MB security limit
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting adaptive system service",
			zap.String("port", cfg.Server.Port),
			zap.Float64("learning_rate", cfg.Adaptive.LearningRate),
			zap.Duration("adaptation_window", cfg.Adaptive.AdaptationWindow))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down adaptive system service...")

	// Graceful shutdown with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Adaptive system service exited")
}