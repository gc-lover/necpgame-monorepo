// Social Service - Enterprise-grade social systems for MMOFPS RPG
// Issue: #140875791
// PERFORMANCE: Memory pooling, context timeouts, zero allocations for MMOFPS

package main

import (
	"context"
	"database/sql"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-redis/redis/v8"
	_ "github.com/lib/pq"

	"github.com/gc-lover/necpgame-monorepo/services/social-service-go/pkg/handlers"
	"github.com/gc-lover/necpgame-monorepo/services/social-service-go/pkg/repository"
	"github.com/gc-lover/necpgame-monorepo/services/social-service-go/pkg/cache"
	"github.com/gc-lover/necpgame-monorepo/services/social-service-go/server"
)

func main() {
	// Initialize database connection
	db, err := initDatabase()
	if err != nil {
		log.Fatalf("Failed to initialize database: %v", err)
	}
	defer db.Close()

	// Initialize Redis connection
	redisClient, err := initRedis()
	if err != nil {
		log.Fatalf("Failed to initialize Redis: %v", err)
	}
	defer redisClient.Close()

	// Initialize components
	repo := repository.NewRepository(db)
	cacheSvc := cache.NewCache(redisClient)
	handlerSvc := handlers.NewService(repo, cacheSvc)

	// Create server instance with dependencies
	srv := server.NewServer(handlerSvc)

	// Create HTTP server
	httpServer := &http.Server{
		Addr:    ":8080",
		Handler: srv.Handler(),
	}

	// Start server in goroutine
	go func() {
		log.Printf("Starting social-service-go on :8080")
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

// initDatabase initializes PostgreSQL database connection
func initDatabase() (*sql.DB, error) {
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://user:password@localhost/social?sslmode=disable"
	}

	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		return nil, err
	}

	// Test connection
	if err := db.Ping(); err != nil {
		return nil, err
	}

	// Configure connection pool
	db.SetMaxOpenConns(25)
	db.SetMaxIdleConns(5)
	db.SetConnMaxLifetime(5 * time.Minute)

	log.Println("Database connection established")
	return db, nil
}

// initRedis initializes Redis connection
func initRedis() (*redis.Client, error) {
	redisURL := os.Getenv("REDIS_URL")
	if redisURL == "" {
		redisURL = "localhost:6379"
	}

	client := redis.NewClient(&redis.Options{
		Addr:     redisURL,
		Password: os.Getenv("REDIS_PASSWORD"),
		DB:       0,
	})

	// Test connection
	if err := client.Ping(context.Background()).Err(); err != nil {
		return nil, err
	}

	log.Println("Redis connection established")
	return client, nil
}
