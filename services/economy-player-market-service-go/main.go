// Issue: #1594 - economy-player-market ogen migration + optimizations
package main

import (
	"context"
	"log"
	_ "net/http/pprof"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/server"
	"github.com/jackc/pgx/v5/pgxpool"
)

func main() {
	// Database connection
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable"
	}

	ctx := context.Background()
	
	// Configure DB pool (standard service - 25 connections)
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		log.Fatalf("Unable to parse DATABASE_URL: %v\n", err)
	}
	config.MaxConns = 25
	config.MinConns = 5
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 10 * time.Minute
	
	dbpool, err := pgxpool.NewWithConfig(ctx, config)
	if err != nil {
		log.Fatalf("Unable to connect to database: %v\n", err)
	}
	defer dbpool.Close()

	// Ping database
	if err := dbpool.Ping(ctx); err != nil {
		log.Fatalf("Unable to ping database: %v\n", err)
	}

	log.Println("OK Connected to database successfully (pool: 25 connections)")

	// pprof profiling endpoint
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6094")
		log.Printf("🔧 pprof profiling on http://%s/debug/pprof", pprofAddr)
		http.ListenAndServe(pprofAddr, nil)
	}()

	// Initialize service layers with ogen
	httpServer := server.NewHTTPServerOgen(":8094")

	// Start server
	go func() {
		log.Printf("Starting Player Market Service on :8094")
		if err := httpServer.Start(); err != nil {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	// Graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	log.Println("Shutting down server...")
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
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

