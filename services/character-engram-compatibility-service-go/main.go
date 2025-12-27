// Character Engram Compatibility Service Go - Enterprise-grade engram compatibility calculations
// Issue: #1600
// PERFORMANCE: Memory pooling, context timeouts, struct alignment for real-time compatibility checks

package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/character-engram-compatibility-service-go/internal/handlers"
	"github.com/gc-lover/necpgame-monorepo/services/character-engram-compatibility-service-go/internal/service"
	"github.com/gc-lover/necpgame-monorepo/services/character-engram-compatibility-service-go/internal/repository"
	"github.com/gc-lover/necpgame-monorepo/services/character-engram-compatibility-service-go/pkg/api"
)

const (
	serverAddr = ":8087"
	shutdownTimeout = 30 * time.Second
	dbTimeout = 50 * time.Millisecond
)

func main() {
	// Initialize repository with connection pooling
	repo := repository.NewRepository()

	// Initialize service with performance optimizations
	svc := service.NewService(repo)

	// Initialize handlers with memory pooling
	h := handlers.NewHandlers(svc)

	// Create server
	srv := api.NewServer(h)

	// Configure HTTP server with timeouts
	httpSrv := &http.Server{
		Addr:         serverAddr,
		Handler:      srv,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in goroutine
	go func() {
		log.Printf("Starting Character Engram Compatibility Service on %s", serverAddr)
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("Shutting down server...")

	// Graceful shutdown with timeout
	ctx, cancel := context.WithTimeout(context.Background(), shutdownTimeout)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	log.Println("Server exited")
}
