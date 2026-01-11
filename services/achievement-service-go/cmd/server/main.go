// Agent: Backend Agent
// Issue: #backend-achievement-service-1

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"achievement-service-go/internal/config"
	"achievement-service-go/internal/handlers"
	"achievement-service-go/server"
)

func main() {
	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		log.Fatalf("Failed to load config: %v", err)
	}

	// Initialize handlers (returns api.Handler interface)
	h, err := handlers.New(cfg)
	if err != nil {
		log.Fatalf("Failed to initialize handlers: %v", err)
	}

	// Create server
	srv, err := server.New(cfg, h)
	if err != nil {
		log.Fatalf("Failed to create server: %v", err)
	}

	// Wrap in HTTP server for graceful shutdown
	httpSrv := &http.Server{
		Addr:    cfg.Server.Address,
		Handler: srv,
	}

	// Start server
	go func() {
		log.Printf("Starting achievement service on %s", cfg.Server.Address)
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("Shutting down server...")

	// Create context with timeout for graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	// Shutdown server
	if err := httpSrv.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	log.Println("Server exited")
}