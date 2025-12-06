// Issue: #1579, #1584
package main

import (
	"context"
	"database/sql"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	_ "github.com/lib/pq"
	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-service-go/server"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Matchmaking Service (Go) starting...")

	// Database connection
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable"
	}

	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to connect to database")
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
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine, /debug/pprof/allocs
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(300, logger) // Max 300 goroutines for matchmaking service (hot path)
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	// HTTP Server
	addr := getEnv("HTTP_ADDR", ":8090")

	httpServer := server.NewHTTPServer(addr, service)

	// Graceful shutdown
	stop := make(chan os.Signal, 1)
	signal.Notify(stop, os.Interrupt, syscall.SIGTERM)

	go func() {
		logger.WithField("addr", addr).Info("Starting Matchmaking Service")
		if err := httpServer.Start(); err != nil {
			logger.WithError(err).Fatal("Failed to start server")
		}
	}()

	<-stop
	logger.Info("Shutting down gracefully...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("Shutdown error")
	}

	logger.Info("Server stopped")
}

// getEnv gets environment variable with fallback
func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}

