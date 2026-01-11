//go:align 64
// Issue: #2286

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime/debug"
	"syscall"
	"time"

	"necpgame/services/crafting-network-service-go/server"
)

func main() {
	// PERFORMANCE: Optimize GC for MMOFPS crafting network
	if gcPercent := os.Getenv("GOGC"); gcPercent == "" {
		debug.SetGCPercent(50) // Reduce GC pressure for high-frequency crafting updates
	}

	// PERFORMANCE: Pre-allocate worker pools for concurrent crafting sessions
	const maxCraftingWorkers = 200
	craftingWorkerPool := make(chan struct{}, maxCraftingWorkers)

	logger := log.New(os.Stdout, "[crafting-network] ", log.LstdFlags|log.Lmicroseconds)

	// Initialize server with enterprise-grade optimizations
	srv := server.NewCraftingNetworkServer(&server.Config{
		MaxWorkers:    maxCraftingWorkers,
		WorkerPool:    craftingWorkerPool,
		CacheTTL:      10 * time.Minute,
		ReadTimeout:   15 * time.Second,
		WriteTimeout:  15 * time.Second,
		IdleTimeout:   60 * time.Second,
		MaxHeaderBytes: 1 << 16,
	})

	// PERFORMANCE: Configure HTTP server for low latency real-time crafting
	httpSrv := &http.Server{
		Addr:           getEnv("SERVER_ADDR", ":8080"),
		Handler:        srv.Handler(),
		ReadTimeout:    srv.Config().ReadTimeout,
		WriteTimeout:   srv.Config().WriteTimeout,
		IdleTimeout:    srv.Config().IdleTimeout,
		MaxHeaderBytes: srv.Config().MaxHeaderBytes,
	}

	// Graceful shutdown handling
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// Start server in background
	go func() {
		logger.Printf("Starting Crafting Network Service on %s (GOGC=%d, Workers=%d)",
			httpSrv.Addr, debug.SetGCPercent(-1), maxCraftingWorkers)

		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatalf("HTTP server error: %v", err)
		}
	}()

	// Wait for shutdown signal
	<-quit
	logger.Println("Shutting down Crafting Network Service...")

	// PERFORMANCE: Graceful shutdown with timeout for active crafting sessions
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.Printf("Server forced to shutdown: %v", err)
	}

	logger.Println("Crafting Network Service exited")
}

// getEnv gets environment variable with fallback
func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}