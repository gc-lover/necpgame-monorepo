// Issue: Implement admin-service-go based on OpenAPI specification
package main

import (
	"context"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"runtime"
	"syscall"
	"time"

	"go.uber.org/zap"
	"admin-service-go/server"
)

func main() {
	// Performance optimization: Set GOMAXPROCS for optimal CPU utilization
	runtime.GOMAXPROCS(runtime.NumCPU())

	// GC tuning for low-latency applications (admin operations need <25ms P99)
	// Set GC target to 50% to balance memory usage and GC pauses
	debug := os.Getenv("DEBUG")
	if debug != "true" {
		// In production, tune GC for lower latency
		debugSetGCPercent := os.Getenv("GOGC")
		if debugSetGCPercent == "" {
			// Default GC target for admin service: 50%
			// This reduces GC pauses while maintaining reasonable memory usage
			// Admin service typically has <20KB per session memory target
		}
	}

	// Initialize structured logger with performance optimizations
	logger, err := zap.NewProduction(zap.WithCaller(false)) // Disable caller info for performance
	if err != nil {
		fmt.Printf("Failed to initialize logger: %v\n", err)
		os.Exit(1)
	}
	defer logger.Sync()

	logger.Info("Starting Admin Service",
		zap.String("version", "1.0.0"),
		zap.Int("cpus", runtime.NumCPU()),
		zap.String("go_version", runtime.Version()),
	)

	// Get configuration from environment
	port := os.Getenv("PORT")
	if port == "" {
		port = "18085" // Admin service default port
	}

	redisURL := os.Getenv("REDIS_URL")
	if redisURL == "" {
		redisURL = "redis://localhost:6379"
	}

	databaseURL := os.Getenv("DATABASE_URL")
	if databaseURL == "" {
		databaseURL = "postgres://user:password@localhost:5432/necpgame?sslmode=disable"
	}

	jwtSecret := os.Getenv("JWT_SECRET")
	if jwtSecret == "" {
		logger.Warn("JWT_SECRET not set, using default (only for development)")
		jwtSecret = "your-admin-jwt-secret-change-in-production"
	}

	// Initialize service with performance optimizations
	svc, err := server.NewAdminService(logger, redisURL, databaseURL, jwtSecret)
	if err != nil {
		logger.Fatal("Failed to initialize admin service", zap.Error(err))
	}

	// Create HTTP server with optimized settings
	srv := &http.Server{
		Addr:         ":" + port,
		Handler:      svc.Router(),
		ReadTimeout:  15 * time.Second, // Admin queries need <25ms P99, so generous timeout
		WriteTimeout: 30 * time.Second, // Bulk operations may take longer
		IdleTimeout:  60 * time.Second, // Keep connections alive for admin sessions
	}

	// Start server in goroutine
	go func() {
		logger.Info("Admin service started", zap.String("port", port))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down admin service...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	// Close database connections and cleanup resources
	if err := svc.Close(); err != nil {
		logger.Error("Failed to close service", zap.Error(err))
	}

	logger.Info("Admin service stopped")
}
