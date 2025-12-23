// Combat Stats Service - Enterprise-grade battle analytics and performance tracking
// Issue: #2245
package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-stats-service-go/server"
)

func main() {
	// Create server instance
	srv := server.NewServer()

	// Create HTTP server
	httpServer := &http.Server{
		Addr:    ":8080",
		Handler: srv.Handler(),
	}

	// Start server in goroutine
	go func() {
		log.Printf("Starting combat-stats-service-go on :8080")
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	log.Println("Server exited")
}
