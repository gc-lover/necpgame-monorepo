// Issue: #1510, #1584
package main

import (
	"context"
	"log"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/combat-acrobatics-wall-run-service-go/server"
)

func main() {
	// Database connection
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://user:password@localhost:5432/necpgame?sslmode=disable"
	}

	connConfig, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		log.Fatalf("Failed to parse database URL: %v", err)
	}

	// Connection pool settings for performance (Issue #1605)
	connConfig.MaxConns = 25
	connConfig.MinConns = 5
	connConfig.MaxConnLifetime = 5 * time.Minute
	connConfig.MaxConnIdleTime = 10 * time.Minute

	db, err := pgxpool.NewWithConfig(context.Background(), connConfig)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	if err := db.Ping(context.Background()); err != nil {
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
		addr = ":8084" // Default port for combat-acrobatics-wall-run-service
	}

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6746") // Unique pprof port
		log.Printf("pprof server starting on %s", pprofAddr)
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			log.Printf("pprof server error: %v", err)
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	logger := log.New(os.Stdout, "[wall-run] ", log.LstdFlags)
	monitor := server.NewGoroutineMonitor(200, logger) // Max 200 goroutines for wall-run service
	go monitor.Start()
	defer monitor.Stop()
	logger.Println("OK Goroutine monitor started")

	srv := server.NewHTTPServer(addr, handlers)

	log.Printf("Starting combat-acrobatics-wall-run-service on %s", addr)
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
