// Issue: #1574, #1584
package main

import (
	"database/sql"
	"log"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"time"

	_ "github.com/lib/pq"
	"github.com/gc-lover/necpgame-monorepo/services/weapon-resource-service-go/server"
)

func main() {
	// Get configuration from environment
	addr := getEnv("HTTP_ADDR", ":8081")
	dbURL := getEnv("DATABASE_URL", "postgres://localhost/necpgame?sslmode=disable")

	// Connect to database
	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Connection pool settings for performance (Issue #1605)
	db.SetMaxOpenConns(25)
	db.SetMaxIdleConns(25)
	db.SetConnMaxLifetime(5 * time.Minute)
	db.SetConnMaxIdleTime(10 * time.Minute)

	if err := db.Ping(); err != nil {
		log.Fatalf("Failed to ping database: %v", err)
	}

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6349")
		log.Printf("Starting pprof server on %s", pprofAddr)
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			log.Printf("pprof server error: %v", err)
		}
	}()

	// Create and start HTTP server
	srv := server.NewHTTPServer(addr, db)
	log.Fatal(srv.Start())
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}









