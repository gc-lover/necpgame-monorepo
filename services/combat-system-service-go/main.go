//go:align 64
// Issue: #2293

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

	"github.com/NECPGAME/combat-system-service-go/internal/handlers"
	"github.com/NECPGAME/combat-system-service-go/internal/repository"
	"github.com/NECPGAME/combat-system-service-go/internal/service"
)

func main() {
	// PERFORMANCE: Optimize GC for MMOFPS combat system
	if gcPercent := os.Getenv("GOGC"); gcPercent == "" {
		debug.SetGCPercent(50) // Reduce GC pressure for high-frequency combat calculations
	}

	// PERFORMANCE: Pre-allocate worker pools for concurrent combat calculations
	const maxCombatWorkers = 100
	combatWorkerPool := make(chan struct{}, maxCombatWorkers)

	logger := log.New(os.Stdout, "[combat-system] ", log.LstdFlags|log.Lmicroseconds)

	// Initialize repository (simplified for now)
	repo := repository.NewRepository()

	// Initialize service
	svc := service.NewService(repo)

	// Initialize handlers with WebSocket and UDP support
	handlerConfig := &handlers.Config{
		MaxWorkers: maxCombatWorkers,
		CacheTTL:   5 * time.Minute,

		// WebSocket configuration for real-time combat
		WebSocketHost:     getEnv("WEBSOCKET_HOST", "0.0.0.0"),
		WebSocketPort:     8081,
		WebSocketPath:     "/ws/combat",
		WebSocketReadTimeout:  60 * time.Second,
		WebSocketWriteTimeout: 10 * time.Second,

		// UDP configuration for high-frequency updates
		UDPHost:         getEnv("UDP_HOST", "0.0.0.0"),
		UDPPort:         8082,
		UDPReadTimeout:  5 * time.Second,
		UDPWriteTimeout: 1 * time.Second,
		UDPBufferSize:   4096,

		Logger: logger,
	}
	h := handlers.NewCombatHandler(handlerConfig, svc, repo)

	// Create custom HTTP mux
	mux := http.NewServeMux()

	// Add WebSocket endpoint for real-time combat
	mux.HandleFunc("/ws/combat", func(w http.ResponseWriter, r *http.Request) {
		h.HandleWebSocketCombat(w, r)
	})

	// Add health check endpoint
	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	// PERFORMANCE: Configure HTTP server for low latency
	srv := &http.Server{
		Addr:         getEnv("SERVER_ADDR", ":8080"),
		Handler:      mux,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
		MaxHeaderBytes: 1 << 16,
	}

	// Graceful shutdown handling
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// Start HTTP server in background
	go func() {
		logger.Printf("Starting Combat System HTTP Service on %s (GOGC=%d, Workers=%d)",
			srv.Addr, debug.SetGCPercent(-1), maxCombatWorkers)

		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatalf("HTTP server error: %v", err)
		}
	}()

	// Start UDP server for high-frequency position updates
	go func() {
		logger.Printf("Starting Combat System UDP Service on %s:%d",
			handlerConfig.UDPHost, handlerConfig.UDPPort)
		h.HandleUDPCombat()
	}()

	// Wait for shutdown signal
	<-quit
	logger.Println("Shutting down Combat System Service...")

	// PERFORMANCE: Graceful shutdown with timeout for active combat sessions
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Printf("Server forced to shutdown: %v", err)
	}

	logger.Println("Combat System Service exited")
}

// getEnv gets environment variable with fallback
func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}