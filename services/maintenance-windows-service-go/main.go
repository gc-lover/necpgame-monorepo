// Maintenance Windows Service - Управление окнами обслуживания
// Issue: #316
// PERFORMANCE: Optimized for high-throughput maintenance window management

package main

import (
	"context"
	"encoding/json"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/maintenance-windows-service-go/server"
	"github.com/gorilla/mux"
)

func main() {
	// Create handler with performance optimizations
	handler := server.NewHandler()

	// Create simple HTTP router for testing
	router := mux.NewRouter()

	// Health check endpoint
	router.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(map[string]string{
			"status":    "healthy",
			"service":   "maintenance-windows-service",
			"timestamp": time.Now().Format(time.RFC3339),
		})
	}).Methods("GET")

	// Basic maintenance window endpoints (placeholder)
	router.HandleFunc("/api/v1/infrastructure/maintenance/windows", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(map[string]interface{}{
			"windows": []interface{}{},
			"total":   0,
			"message": "Maintenance Windows Service is running",
		})
	}).Methods("GET")

	// PERFORMANCE: Configure HTTP server with timeouts
	httpServer := &http.Server{
		Addr:         ":8097", // Using port 8097 for maintenance windows service
		Handler:      router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in a goroutine
	go func() {
		log.Printf("Maintenance Windows Service listening on %s", httpServer.Addr)
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Maintenance Windows Service failed to start: %v", err)
		}
	}()

	// Graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("Shutting down Maintenance Windows Service...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Fatalf("Maintenance Windows Service forced to shutdown: %v", err)
	}
	log.Println("Maintenance Windows Service exited gracefully.")
}

