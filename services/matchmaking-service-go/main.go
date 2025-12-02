// Issue: #1579 - ogen migration + DB pool + skill buckets
package main

import (
	"database/sql"
	"log"
	"os"
	"time"

	_ "github.com/lib/pq"
	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-service-go/server"
)

func main() {
	// Database connection
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable"
	}

	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// CRITICAL: Configure connection pool (hot path service - 5k RPS)
	db.SetMaxOpenConns(50)                      // Higher for matchmaking
	db.SetMaxIdleConns(50)                      // Match MaxOpenConns
	db.SetConnMaxLifetime(5 * time.Minute)      // Prevent stale connections
	db.SetConnMaxIdleTime(10 * time.Minute)     // Reuse idle connections

	// Repository
	repository := server.NewPostgresRepository(db)

	// Service
	service := server.NewMatchmakingService(repository)

	// HTTP Server
	addr := os.Getenv("HTTP_ADDR")
	if addr == "" {
		addr = ":8090"
	}

	httpServer := server.NewHTTPServer(addr, service)

	log.Printf("Starting Matchmaking Service on %s", addr)
	if err := httpServer.Start(); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}


