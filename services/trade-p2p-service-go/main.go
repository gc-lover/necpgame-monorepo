// Issue: #1637 - P2P Trade Service main
package main

import (
	"database/sql"
	"log"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/trade-p2p-service-go/server"
	_ "github.com/lib/pq"
)

func main() {
	// Database connection
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://user:password@localhost:5432/necpgame?sslmode=disable"
	}

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

	// Create repository
	repo := server.NewRepository(db)

	// Create service
	svc := server.NewService(repo)

	// Create handlers
	handlers := server.NewHandlers(svc)

	// Create HTTP server
	addr := os.Getenv("HTTP_ADDR")
	if addr == "" {
		addr = ":8086" // Different port from battle-pass
	}

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6748") // Different port from battle-pass
		log.Printf("pprof server starting on %s", pprofAddr)
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			log.Printf("pprof server error: %v", err)
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	logger := log.New(os.Stdout, "[trade-p2p] ", log.LstdFlags)
	monitor := server.NewGoroutineMonitor(150, logger) // Max 150 goroutines for trade-p2p service
	go monitor.Start()
	defer monitor.Stop()
	logger.Println("OK Goroutine monitor started")

	srv := server.NewHTTPServer(addr, handlers, svc)

	log.Printf("Starting trade-p2p-service on %s", addr)
	if err := srv.Start(); err != nil {
		log.Fatalf("Server failed: %v", err)
	}
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
