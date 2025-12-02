// Issue: #158
package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-go/server"
)

func main() {
	// Configuration
	addr := getEnv("SERVER_ADDR", ":8083")
	dbConnStr := getEnv("DATABASE_URL", "postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")

	// Initialize repository
	repo, err := server.NewRepository(dbConnStr)
	if err != nil {
		log.Fatalf("Failed to initialize repository: %v", err)
	}
	defer repo.Close()

	// Initialize service
	service := server.NewService(repo)

	// Create HTTP server
	httpServer := server.NewHTTPServer(addr, service)

	// Graceful shutdown
	stop := make(chan os.Signal, 1)
	signal.Notify(stop, os.Interrupt, syscall.SIGTERM)

	// Start server
	go func() {
		log.Printf("Starting Combat Combos Service on %s", addr)
		if err := httpServer.Start(); err != nil {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	// Wait for shutdown signal
	<-stop
	log.Println("Shutting down gracefully...")

	// Shutdown timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Printf("Shutdown error: %v", err)
	}

	log.Println("Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

