// Issue: #1579, #1584
package main

import (
	"database/sql"
	"log"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
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

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6191")
		log.Printf("Starting pprof server on %s", pprofAddr)
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine, /debug/pprof/allocs
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			log.Printf("pprof server error: %v", err)
		}
	}()

	// HTTP Server
	addr := getEnv("HTTP_ADDR", ":8090")

	httpServer := server.NewHTTPServer(addr, service)

	log.Printf("Starting Matchmaking Service on %s", addr)
	if err := httpServer.Start(); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}

// getEnv gets environment variable with fallback
func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}

