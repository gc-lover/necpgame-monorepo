// Issue: #1560

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/projectile-core-service-go/server"
)

func main() {
	// Configuration
	addr := getEnv("SERVER_ADDR", ":8091")
	dbConnStr := getEnv("DATABASE_URL", "postgres://necpg:necpg@localhost:5432/necpg?sslmode=disable")

	// Initialize server
	srv, err := server.NewHTTPServer(addr, dbConnStr)
	if err != nil {
		log.Fatalf("Failed to create server: %v", err)
	}

	// Start server
	go func() {
		log.Printf("🚀 Projectile Core Service starting on %s", addr)
		if err := srv.Start(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	// Graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	log.Println("🛑 Shutting down server...")

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	log.Println("OK Server stopped gracefully")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

