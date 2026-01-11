// Reputation Service - Dynamic Reputation System
// Issue: #2174 - Reputation Decay & Recovery mechanics
package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gorilla/mux"
	"go.uber.org/zap"

	"necpgame/services/reputation-service-go/internal/models"
	"necpgame/services/reputation-service-go/internal/service"
)

func main() {
	// Initialize logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	// Initialize service components
	registry := models.NewReputationRegistry()

	// Create service instance
	reputationService := &service.Service{
		Registry: registry,
		Logger:   logger,
	}

	// Initialize HTTP server
	router := mux.NewRouter()

	// Health check endpoint
	router.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"healthy","service":"reputation"}`))
	}).Methods("GET")

	// API routes
	api := router.PathPrefix("/api/v1").Subrouter()

	// Reputation management
	api.HandleFunc("/players/{playerId}/reputation", reputationService.GetPlayerReputation).Methods("GET")
	api.HandleFunc("/players/{playerId}/reputation", reputationService.UpdatePlayerReputation).Methods("PUT")
	api.HandleFunc("/players/{playerId}/reputation/history", reputationService.GetReputationHistory).Methods("GET")

	// Decay & recovery
	api.HandleFunc("/players/{playerId}/reputation/decay", reputationService.TriggerDecay).Methods("POST")
	api.HandleFunc("/players/{playerId}/reputation/recovery", reputationService.TriggerRecovery).Methods("POST")

	// Configuration
	api.HandleFunc("/config/decay-rules", reputationService.GetDecayConfiguration).Methods("GET")
	api.HandleFunc("/config/decay-rules", reputationService.UpdateDecayConfiguration).Methods("PUT")

	// Analytics
	api.HandleFunc("/analytics/reputation-stats", reputationService.GetReputationStatistics).Methods("GET")

	// Server configuration
	port := os.Getenv("REPUTATION_PORT")
	if port == "" {
		port = "8080"
	}

	srv := &http.Server{
		Addr:         ":" + port,
		Handler:      router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting Reputation Service",
			zap.String("port", port),
			zap.Time("started_at", time.Now()))

		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down Reputation Service...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Reputation Service stopped")
}