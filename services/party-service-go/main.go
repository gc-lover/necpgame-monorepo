// Issue: #139
package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame/services/party-service-go/server"
)

func main() {
	// Initialize repository
	repo := server.NewPartyRepository()

	// Initialize service
	service := server.NewPartyService(repo)

	// Initialize HTTP server
	addr := getEnv("SERVER_ADDR", ":8080")
	httpServer := server.NewHTTPServer(addr, service)

	// Start server
	go func() {
		log.Printf("Starting Party Service on %s", addr)
		if err := httpServer.Start(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	log.Println("Shutting down server...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	log.Println("Server exited")
}

func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}
