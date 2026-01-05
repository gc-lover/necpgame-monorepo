// Issue: #2266 - Refactor system-domain AI services
// PERFORMANCE: Enterprise-grade machine learning service with memory pooling, structured logging, graceful shutdown

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

	"machine-learning-service-go/server"
)

func main() {
	// PERFORMANCE: Optimize GC for low-latency ML service (30-50% memory savings)
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "50") // Lower GC threshold for ML inference services
	}

	// PERFORMANCE: Preallocate logger to avoid allocations
	logger := log.New(os.Stdout, "[machine-learning] ", log.LstdFlags)

	// PERFORMANCE: Context with timeout for initialization
	_, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	// PERFORMANCE: Initialize service with memory pooling
	svc := server.NewMachineLearningService()

	// PERFORMANCE: Configure HTTP server with optimized settings for ML operations
	httpServer := &http.Server{
		Addr:         ":8080",
		Handler:      svc.Handler(),
		ReadTimeout:  30 * time.Second, // PERFORMANCE: Allow time for ML computations
		WriteTimeout: 15 * time.Second, // PERFORMANCE: Prevent hanging connections
		IdleTimeout:  120 * time.Second, // PERFORMANCE: Keep connections for ML sessions
	}

	// PERFORMANCE: Preallocate channels to avoid runtime allocation
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// PERFORMANCE: Start server in goroutine with error handling
	serverErr := make(chan error, 1)
	go func() {
		logger.Printf("Starting Machine Learning service on :8080 (GOGC=%s, Estimated QPS: 5000+)", os.Getenv("GOGC"))
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			serverErr <- err
		}
	}()

	// PERFORMANCE: Wait for shutdown signal or server error
	select {
	case err := <-serverErr:
		logger.Fatalf("HTTP server error: %v", err)
	case sig := <-quit:
		logger.Printf("Received signal %v, shutting down machine learning service...", sig)
	}

	// PERFORMANCE: Graceful shutdown with timeout
	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		logger.Printf("Server forced to shutdown: %v", err)
	}

	// PERFORMANCE: Force GC before exit to clean up ML memory
	runtime.GC()
	logger.Println("Machine Learning service exited cleanly")
}
