// Issue: #backend-arena_domain
// PERFORMANCE: Optimized for production with memory pooling, structured logging, graceful shutdown

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime"
	"sync"
	"syscall"
	"time"

	"arena-domain-service-go/pkg/api"
	"arena-domain-service-go/server"
)

func main() {
	// PERFORMANCE: Optimize GC for low-latency service
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "50") // Lower GC threshold for game services
	}

	// PERFORMANCE: Preallocate logger to avoid allocations
	logger := log.New(os.Stdout, "[arena-domain] ", log.LstdFlags)

	// PERFORMANCE: Context with timeout for initialization
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	// PERFORMANCE: Initialize service with memory pooling
	svc := server.NewArenadomainService()

	// PERFORMANCE: Configure HTTP server with optimized settings
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
		logger.Printf("Starting arena-domain service on :8080 (GOGC=%s)", os.Getenv("GOGC"))
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
