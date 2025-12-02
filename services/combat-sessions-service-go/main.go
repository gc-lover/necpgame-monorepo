// Issue: #130

package main

import (
	"context"
	"fmt"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/combat-sessions-service-go/server"
)

func main() {
	// Configuration
	port := getEnv("PORT", "8090")
	dbHost := getEnv("DB_HOST", "localhost")
	dbPort := getEnv("DB_PORT", "5432")
	dbName := getEnv("DB_NAME", "necpgame")
	dbUser := getEnv("DB_USER", "postgres")
	dbPass := getEnv("DB_PASSWORD", "postgres")
	redisAddr := getEnv("REDIS_ADDR", "localhost:6379")
	kafkaBrokers := getEnv("KAFKA_BROKERS", "localhost:9092")

	// Database connection string
	dbDSN := fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
		dbHost, dbPort, dbUser, dbPass, dbName)

	// Create repository
	repo, err := server.NewPostgresRepository(dbDSN)
	if err != nil {
		log.Fatalf("Failed to create repository: %v", err)
	}
	defer repo.Close()

	// Create service
	svc := server.NewCombatSessionService(repo, redisAddr, kafkaBrokers)

	// Create HTTP server
	httpServer := server.NewHTTPServer(":"+port, svc)

	// Start server in goroutine
	go func() {
		log.Printf("Combat Sessions Service starting on port %s", port)
		if err := httpServer.Start(); err != nil {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	// Graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	log.Println("Shutting down server...")
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	log.Println("Server exited")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

