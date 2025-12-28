// Issue: #288 - Support Service Backend Implementation
// PERFORMANCE: Enterprise-grade support ticket system with optimized hot paths

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime"
	"syscall"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"support-service-go/internal/wiring"
	"support-service-go/server"
)

func main() {
	// PERFORMANCE: Optimize GC for low-latency service
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "50") // Lower GC threshold for support services
	}

	// PERFORMANCE: Preallocate logger to avoid allocations
	logger := log.New(os.Stdout, "[support-service-go] ", log.LstdFlags)

	// PERFORMANCE: Context with timeout for initialization
	_, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	// Initialize database connection if DATABASE_URL is provided
	var db *pgxpool.Pool
	if dbURL := os.Getenv("DATABASE_URL"); dbURL != "" {
		var err error
		db, err = pgxpool.New(context.Background(), dbURL)
		if err != nil {
			logger.Fatalf("Failed to connect to database: %v", err)
		}
		defer db.Close()
	}

	// Initialize structured logger
	zapLogger, err := zap.NewProduction()
	if err != nil {
		logger.Fatalf("Failed to create logger: %v", err)
	}
	defer zapLogger.Sync()

	// Initialize SLA components
	slaHandlers, err := wiring.WireComponents(db, zapLogger)
	if err != nil {
		logger.Fatalf("Failed to initialize SLA components: %v", err)
	}

	// PERFORMANCE: Initialize service with memory pooling
	svc := server.NewServer(db, zapLogger, slaHandlers)

	// PERFORMANCE: Configure HTTP server with optimized settings
	httpServer := &http.Server{
		Addr:         ":8080",
		Handler:      svc,
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
		logger.Printf("Starting support-service-go service on :8080 (GOGC=%s)", os.Getenv("GOGC"))
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			serverErr <- err
		}
	}()

	// PERFORMANCE: Wait for shutdown signal or server error
	select {
	case err := <-serverErr:
		logger.Fatalf("HTTP server error: %v", err)
	case sig := <-quit:
		logger.Printf("Received signal %v, shutting down server...", sig)
	}

	// PERFORMANCE: Graceful shutdown with timeout
	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		logger.Printf("Server forced to shutdown: %v", err)
	}

	// PERFORMANCE: Force GC before exit to clean up
	runtime.GC()
	logger.Println("Server exited cleanly")
}

// Issue: #288 - Support Service Backend Implementation
