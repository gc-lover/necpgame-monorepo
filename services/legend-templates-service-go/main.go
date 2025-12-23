// Legend Templates Service - Enterprise-grade urban legend management
// Issue: #2241
// PERFORMANCE: Memory pooling, context timeouts, zero allocations for MMOFPS legend generation

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/legend-templates-service-go/pkg/api"
	"github.com/gc-lover/necpgame-monorepo/services/legend-templates-service-go/server"
)

func main() {
	// PERFORMANCE: Fast startup with minimal allocations
	log.Println("Starting Legend Templates Service...")

	// Create handler with performance optimizations
	handler := server.NewHandler()

	// PERFORMANCE: Configure HTTP server with timeouts
	httpHandler, err := api.NewServer(handler, handler)
	if err != nil {
		log.Fatalf("Failed to create HTTP server: %v", err)
	}

	srv := &http.Server{
		Addr:         ":8080",
		Handler:      httpHandler, // Handler implements both Handler and SecurityHandler
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// PERFORMANCE: Graceful shutdown with context cancellation
	go func() {
		log.Printf("Server starting on %s", srv.Addr)
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed to start: %v", err)
		}
	}()

	// PERFORMANCE: Clean shutdown handling
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("Shutting down server...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	log.Println("Server exited")
}