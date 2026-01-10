package main

import (
	"context"
	"log"
	"net/http"
	_ "net/http/pprof" // PERFORMANCE: pprof endpoint for profiling (Level 3 optimization)
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

	// PERFORMANCE: GC tuning for real-time ability operations (Level 3 optimization)
	if gcPercent := os.Getenv("GOGC"); gcPercent == "" {
		// debug.SetGCPercent(50) // Uncomment for production tuning
	}

	// PERFORMANCE: Profiling endpoint for real-time performance monitoring
	profilingAddr := os.Getenv("PPROF_ADDR")
	if profilingAddr == "" {
		profilingAddr = ":6064" // Ability service profiling port
	}

	// Initialize database and Redis connections with enterprise-grade pool optimization
	ctx := context.Background()
	repo, err := repository.NewRepository(ctx, logger, cfg.Database.GetDSN(), cfg.Database, cfg.Redis)
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
		ReadTimeout:    15 * time.Second, // BACKEND NOTE: Increased for complex ability operations
		WriteTimeout:   15 * time.Second, // BACKEND NOTE: For ability activation responses
		IdleTimeout:    120 * time.Second, // BACKEND NOTE: Keep connections alive for performance
		ReadHeaderTimeout: 3 * time.Second, // BACKEND NOTE: Fast header processing for ability requests
		MaxHeaderBytes: 1 << 20, // BACKEND NOTE: 1MB max headers for security
	}

	// PERFORMANCE: Start pprof profiling server for real-time performance monitoring
	go func() {
		logger.Info("Starting pprof profiling server", zap.String("addr", profilingAddr))
		if err := http.ListenAndServe(profilingAddr, nil); err != nil {
			logger.Error("Pprof server failed", zap.Error(err))
		}
	}()

	// Start server in goroutine
	go func() {
		logger.Info("Starting ability service",
			zap.String("port", cfg.Server.Port),
			zap.String("pprof_addr", profilingAddr),
			zap.String("performance_target", "P99 <20ms"),
			zap.String("optimization_level", "Level 3 (Game Server)"))
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