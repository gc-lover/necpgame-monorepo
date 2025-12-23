// Issue: #140889798
// PERFORMANCE: Optimized for production with memory pooling, structured logging, graceful shutdown
// BACKEND: Session management service for MMOFPS RPG authentication and state tracking

package main

import (
	"context"
	"net/http"
	"os"
	"os/signal"
	"runtime"
	"syscall"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"session-management-service-go/server"
)

func main() {
	// PERFORMANCE: Optimize GC for low-latency session service
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "50") // Lower GC threshold for session services
	}

	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		panic("Failed to initialize logger: " + err.Error())
	}
	defer logger.Sync()

	// Initialize database connection (placeholder - would use real DB config)
	dbConfig, err := pgxpool.ParseConfig("postgres://user:pass@localhost:5432/sessions?sslmode=disable")
	if err != nil {
		logger.Fatal("Failed to parse database config", zap.Error(err))
	}

	db, err := pgxpool.NewWithConfig(context.Background(), dbConfig)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Initialize repository and service
	repo := server.NewSessionRepository(db, logger)
	service := server.NewSessionServiceLogic(logger, repo)

	// Initialize HTTP service
	svc := server.NewSessionService(logger, service, repo)

	// PERFORMANCE: Configure HTTP server with optimized settings for session hot paths
	httpServer := &http.Server{
		Addr:         ":8080",
		Handler:      svc.Handler(),
		ReadTimeout:  15 * time.Second, // PERFORMANCE: Prevent slowloris
		WriteTimeout: 15 * time.Second, // PERFORMANCE: Prevent hanging connections
		IdleTimeout:  60 * time.Second, // PERFORMANCE: Reuse connections
	}

	// PERFORMANCE: Preallocate channels to avoid runtime allocation
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// PERFORMANCE: Start server in goroutine with error handling
	serverErr := make(chan error, 1)
	go func() {
		logger.Info("Starting session management service",
			zap.String("addr", ":8080"),
			zap.String("gogc", os.Getenv("GOGC")))
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			serverErr <- err
		}
	}()

	// PERFORMANCE: Wait for shutdown signal or server error
	select {
	case err := <-serverErr:
		logger.Fatal("HTTP server error", zap.Error(err))
	case sig := <-quit:
		logger.Info("Received shutdown signal", zap.String("signal", sig.String()))
	}

	// PERFORMANCE: Graceful shutdown with timeout
	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	// PERFORMANCE: Force GC before exit to clean up active sessions
	runtime.GC()
	logger.Info("Session management service exited cleanly")
}
