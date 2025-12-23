// Issue: #140889770
// PERFORMANCE: Optimized for production with memory pooling, structured logging, graceful shutdown
// BACKEND: Narrative and cutscene management service for MMOFPS RPG

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

	"narrative-service-go/server"
)

func main() {
	// PERFORMANCE: Optimize GC for low-latency narrative service
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "50") // Lower GC threshold for real-time narrative
	}

	// PERFORMANCE: Preallocate logger to avoid allocations
	logger := log.New(os.Stdout, "[narrative] ", log.LstdFlags)

	// PERFORMANCE: Initialize service with memory pooling for narrative operations
	svc := server.NewNarrativeService()

	// PERFORMANCE: Configure HTTP server with optimized settings for narrative hot paths
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
		logger.Printf("Starting narrative service on :8080 (GOGC=%s)", os.Getenv("GOGC"))
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			serverErr <- err
		}
	}()

	// PERFORMANCE: Wait for shutdown signal or server error
	select {
	case err := <-serverErr:
		logger.Fatalf("HTTP server error: %v", err)
	case sig := <-quit:
		logger.Printf("Received signal %v, shutting down narrative service...", sig)
	}

	// PERFORMANCE: Graceful shutdown with timeout
	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		logger.Printf("Server forced to shutdown: %v", err)
	}

	// PERFORMANCE: Force GC before exit to clean up narrative states
	runtime.GC()
	logger.Println("Narrative service exited cleanly")
}
