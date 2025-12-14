// Issue: #1856
package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/guild-core-service-go/server"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Guild Core Service starting...")

	// Configuration
	addr := getEnv("SERVER_ADDR", ":8084")
	dbConnStr := getEnv("DATABASE_URL", "postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")

	// Initialize repository
	repo, err := server.NewRepository(dbConnStr)
	if err != nil {
		logger.WithError(err).Fatal("Failed to initialize repository")
	}
	defer repo.Close()

	// Initialize service
	service := server.NewService(repo)

	// Create HTTP server
	httpServer := server.NewHTTPServer(addr, service)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6824")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(200, logger) // Max 200 goroutines for guild-core service
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	// Graceful shutdown
	stop := make(chan os.Signal, 1)
	signal.Notify(stop, os.Interrupt, syscall.SIGTERM)

	// Start server
	go func() {
		logger.WithField("addr", addr).Info("Starting Guild Core Service")
		if err := httpServer.Start(); err != nil {
			logger.WithError(err).Fatal("Server failed")
		}
	}()

	// Wait for shutdown signal
	<-stop
	logger.Info("Shutting down gracefully...")

	// Shutdown timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("Shutdown error")
	}

	logger.Info("Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}