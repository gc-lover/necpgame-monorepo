// Issue: #150 - Matchmaking Service Main Entry Point (ogen-based)
package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/server"
)

func main() {
	// Configuration from environment
	addr := getEnv("SERVER_ADDR", ":8090")
	dbConnStr := getEnv("DATABASE_URL", "postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	redisAddr := getEnv("REDIS_ADDR", "localhost:6379")

	log.Println("Starting Matchmaking Service...")
	log.Printf("Server Address: %s", addr)
	log.Printf("Database: %s", dbConnStr)
	log.Printf("Redis: %s", redisAddr)

	// Initialize repository (DB pool: 25-50 connections)
	repo, err := server.NewRepository(dbConnStr)
	if err != nil {
		log.Fatalf("Failed to initialize repository: %v", err)
	}
	defer repo.Close()
	log.Println("OK Database connected")

	// Initialize cache manager (Redis)
	cache := server.NewCacheManager(redisAddr)
	defer cache.Close()
	log.Println("OK Redis connected")

	// Initialize service (with memory pooling, skill buckets)
	service := server.NewService(repo, cache)
	log.Println("OK Service initialized")

	// Create HTTP server (ogen-based)
	httpServer := server.NewHTTPServer(addr, service)
	log.Println("OK HTTP server created")

	// Graceful shutdown
	stop := make(chan os.Signal, 1)
	signal.Notify(stop, os.Interrupt, syscall.SIGTERM)

	// Start server
	go func() {
		log.Printf("🚀 Matchmaking Service listening on %s", addr)
		log.Println("📊 Endpoints:")
		log.Println("  - POST   /api/v1/matchmaking/queue")
		log.Println("  - GET    /api/v1/matchmaking/queue/{queueId}")
		log.Println("  - DELETE /api/v1/matchmaking/queue/{queueId}")
		log.Println("  - GET    /api/v1/matchmaking/rating/{player_id}")
		log.Println("  - GET    /api/v1/matchmaking/leaderboard/{activityType}")
		log.Println("  - GET    /health")
		log.Println("  - GET    /metrics")
		
		if err := httpServer.Start(); err != nil {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	// Wait for shutdown signal
	<-stop
	log.Println("WARNING  Shutting down gracefully...")

	// Shutdown timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Printf("❌ Shutdown error: %v", err)
	}

	log.Println("OK Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
