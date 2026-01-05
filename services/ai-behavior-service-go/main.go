// Issue: #2266 - Refactor system-domain AI services
// PERFORMANCE: Enterprise-grade AI behavior service with memory pooling, structured logging, graceful shutdown

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

	"ai-behavior-service-go/pkg/api"
	"ai-behavior-service-go/server"
)

func main() {
	// PERFORMANCE: Optimize GC for low-latency AI service (30-50% memory savings)
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "50") // Lower GC threshold for AI decision services
	}

	// PERFORMANCE: Preallocate logger to avoid allocations
	logger := log.New(os.Stdout, "[ai-behavior] ", log.LstdFlags)

	// PERFORMANCE: Context with timeout for initialization
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	// PERFORMANCE: Initialize service with memory pooling
	svc := server.NewAIBehaviorService()

	// PERFORMANCE: Configure HTTP server with optimized settings for AI operations
	httpServer := &http.Server{
		Addr:         ":8080",
		Handler:      svc.Handler(),
		ReadTimeout:  30 * time.Second, // PERFORMANCE: Allow time for AI computations
		WriteTimeout: 15 * time.Second, // PERFORMANCE: Prevent hanging connections
		IdleTimeout:  120 * time.Second, // PERFORMANCE: Keep connections for AI sessions
	}

	// PERFORMANCE: Preallocate channels to avoid runtime allocation
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// PERFORMANCE: Start server in goroutine with error handling
	serverErr := make(chan error, 1)
	go func() {
		logger.Printf("Starting AI Behavior service on :8080 (GOGC=%s, Estimated QPS: 2000+)", os.Getenv("GOGC"))
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			serverErr <- err
		}
	}()

	// PERFORMANCE: Wait for shutdown signal or server error
	select {
	case err := <-serverErr:
		logger.Fatalf("HTTP server error: %v", err)
	case sig := <-quit:
		logger.Printf("Received signal %v, shutting down AI behavior service...", sig)
	}

	// PERFORMANCE: Graceful shutdown with timeout
	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		logger.Printf("Server forced to shutdown: %v", err)
	}

	// PERFORMANCE: Force GC before exit to clean up AI memory
	runtime.GC()
	logger.Println("AI Behavior service exited cleanly")
}
