// World Regions Service Go - Enterprise-Grade World Regions Management
// Issue: #140875729
// PERFORMANCE: Optimized for MMOFPS with struct alignment and memory pooling

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

	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/world-regions-service-go/server"
)

func main() {
	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting World Regions Service",
		zap.String("version", "1.0.0"),
		zap.Time("started_at", time.Now()))

	// Get database URL from environment
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		// Default for development
		dbURL = "postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable"
		logger.Warn("Using default DATABASE_URL", zap.String("url", dbURL))
	}

	// Initialize repository
	repo, err := server.NewWorldRegionsRepository(dbURL)
	if err != nil {
		logger.Fatal("Failed to initialize repository", zap.Error(err))
	}

	// Initialize service
	service := server.NewWorldRegionsService(repo)

	logger.Info("World Regions Service initialized",
		zap.String("status", "ready"),
		zap.Any("repository", repo != nil),
		zap.Any("service", service != nil))

	// Initialize HTTP server
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}

	srv := &http.Server{
		Addr: ":" + port,
		Handler: setupRoutes(service, logger),
	}

	// Start server in goroutine
	go func() {
		logger.Info("HTTP server starting", zap.String("port", port))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("HTTP server failed", zap.Error(err))
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Graceful shutdown
	shutdownCtx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(shutdownCtx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")

	// TODO: Close database connections
	// TODO: Stop HTTP server

	logger.Info("World Regions Service stopped")
}

// setupRoutes configures HTTP routes for the service
func setupRoutes(service *server.WorldRegionsService, logger *zap.Logger) http.Handler {
	mux := http.NewServeMux()

	// Health check endpoint
	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"healthy","service":"world-regions","version":"1.0.0"}`))
	})

	// API endpoints
	mux.HandleFunc("/api/v1/world-regions", func(w http.ResponseWriter, r *http.Request) {
		ctx := context.Background()

		regions, total, err := service.GetWorldRegions(ctx, "", "", 10, 0)
		if err != nil {
			logger.Error("Failed to get regions", zap.Error(err))
			http.Error(w, "Internal server error", http.StatusInternalServerError)
			return
		}

		response := map[string]interface{}{
			"regions": regions,
			"total":   total,
			"limit":   10,
			"offset":  0,
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		if err := json.NewEncoder(w).Encode(response); err != nil {
			logger.Error("Failed to encode response", zap.Error(err))
		}
	})

	return mux
}
