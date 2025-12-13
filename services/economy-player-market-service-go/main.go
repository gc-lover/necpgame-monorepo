// Issue: #1594 - economy-player-market ogen migration + optimizations
package main

import (
	"context"
	"net/http"
	_ "net/http/pprof"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/server"
	"github.com/jackc/pgx/v5/pgxpool"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Economy Player Market Service (Go) starting...")

	// Database connection
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable"
	}

	ctx := context.Background()

	// Configure DB pool (standard service - 25 connections)
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		logger.WithError(err).Fatal("Unable to parse DATABASE_URL")
	}
	config.MaxConns = 25
	config.MinConns = 5
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 10 * time.Minute

	dbpool, err := pgxpool.NewWithConfig(ctx, config)
	if err != nil {
		logger.WithError(err).Fatal("Unable to connect to database")
	}
	defer dbpool.Close()

	// Ping database
	if err := dbpool.Ping(ctx); err != nil {
		logger.WithError(err).Fatal("Unable to ping database")
	}

	logger.Info("OK Connected to database successfully (pool: 25 connections)")

	// pprof profiling endpoint
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6513")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(200, logger) // Max 200 goroutines for economy-player-market service
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	// Initialize service layers with ogen
	httpServer := server.NewHTTPServerOgen(dbpool, ":8094")

	// Start server
	go func() {
		logger.WithField("addr", ":8094").Info("Starting Player Market Service")
		if err := httpServer.Start(); err != nil {
			logger.WithError(err).Fatal("Server failed")
		}
	}()

	// Graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")
	shutdownCtx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		logger.WithError(err).Error("Server forced to shutdown")
	}

	logger.Info("Server exited")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
