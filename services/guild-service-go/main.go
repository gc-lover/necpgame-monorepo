//go:align 64
// Issue: #2295

package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime"
	"runtime/debug"
	"syscall"
	"time"

	"guild-service-go/internal/service"
	"guild-service-go/internal/repository"
	"guild-service-go/internal/service"
	"guild-service-go/pkg/api"
)

func main() {
	// PERFORMANCE: Optimize GC for MMOFPS guild system
	if gcPercent := os.Getenv("GOGC"); gcPercent == "" {
		debug.SetGCPercent(50) // Reduce GC pressure for high-frequency guild operations
	}

	// PERFORMANCE: Pre-allocate worker pools for concurrent guild operations
	const maxGuildWorkers = 50
	guildWorkerPool := make(chan struct{}, maxGuildWorkers)

	logger := log.New(os.Stdout, "[guild-service] ", log.LstdFlags|log.Lmicroseconds)

	// Initialize repository
	repo := repository.NewRepository()

	// Initialize service
	svc := service.NewService(repo)

	// Initialize handlers
	handlerConfig := &handlers.Config{
		MaxWorkers: maxGuildWorkers,
		CacheTTL:   10 * time.Minute,
	}
	h := handlers.NewGuildHandler(handlerConfig, svc, repo)

	// Create server with security handler
	server, _ := api.NewServer(h, &handlers.SecurityHandler{})

	// PERFORMANCE: Configure HTTP server for low latency
	srv := &http.Server{
		Addr:         getEnv("SERVER_ADDR", ":8080"),
		Handler:      server,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
		MaxHeaderBytes: 1 << 16,
	}

	// Graceful shutdown handling
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// Start server in background
	go func() {
		logger.Printf("Starting Guild Service on %s (GOGC=%d, Workers=%d)",
			srv.Addr, debug.SetGCPercent(-1), maxGuildWorkers)

		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatalf("HTTP server error: %v", err)
		}
	}()

	// Wait for shutdown signal
	<-quit
	logger.Println("Shutting down Guild Service...")

	// PERFORMANCE: Graceful shutdown with timeout for active guild sessions
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Printf("Server forced to shutdown: %v", err)
	}

	logger.Println("Guild Service exited")
}

// getEnv gets environment variable with fallback
func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}