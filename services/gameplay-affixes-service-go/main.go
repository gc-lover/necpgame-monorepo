// Issue: #1495 - Gameplay Affixes Service implementation
// PERFORMANCE: Optimized Go service with efficient database connections and context timeouts

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
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"gameplay-affixes-service-go/internal/wiring"
)

func main() {
	// Initialize logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting Gameplay Affixes Service")

	// Get database URL from environment
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://user:password@localhost:5432/gameplay_affixes?sslmode=disable"
	}

	// Connect to database
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	dbPool, err := pgxpool.New(ctx, dbURL)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer dbPool.Close()

	if err := dbPool.Ping(ctx); err != nil {
		logger.Fatal("Failed to ping database", zap.Error(err))
	}

	logger.Info("Connected to database successfully")

	// Wire dependencies
	affixesHandlers, err := wiring.WireComponents(dbPool, logger)
	if err != nil {
		logger.Fatal("Failed to wire components", zap.Error(err))
	}

	// Setup HTTP router
	router := mux.NewRouter()

	// Health check
	router.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	// Affixes endpoints
	router.HandleFunc("/api/v1/gameplay/affixes/active", affixesHandlers.GetActiveAffixes).Methods("GET")
	router.HandleFunc("/api/v1/gameplay/affixes/{id}", affixesHandlers.GetAffix).Methods("GET")
	router.HandleFunc("/api/v1/gameplay/affixes", affixesHandlers.ListAffixes).Methods("GET")
	router.HandleFunc("/api/v1/gameplay/affixes", affixesHandlers.CreateAffix).Methods("POST")
	router.HandleFunc("/api/v1/gameplay/affixes/{id}", affixesHandlers.UpdateAffix).Methods("PUT")
	router.HandleFunc("/api/v1/gameplay/affixes/{id}", affixesHandlers.DeleteAffix).Methods("DELETE")

	// Instance affixes endpoints
	router.HandleFunc("/api/v1/gameplay/instances/{instance_id}/affixes", affixesHandlers.GetInstanceAffixes).Methods("GET")
	router.HandleFunc("/api/v1/gameplay/instances/{instance_id}/affixes/generate", affixesHandlers.GenerateInstanceAffixes).Methods("POST")

	// Rotation endpoints
	router.HandleFunc("/api/v1/gameplay/affixes/rotation/history", affixesHandlers.GetAffixRotationHistory).Methods("GET")
	router.HandleFunc("/api/v1/gameplay/affixes/rotation/trigger", affixesHandlers.TriggerAffixRotation).Methods("POST")

	// Add logging middleware
	router.Use(func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			logger.Info("Request started",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.String("remote_addr", r.RemoteAddr),
			)

			// Call the next handler
			next.ServeHTTP(w, r)

			logger.Info("Request completed",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.Duration("duration", time.Since(start)),
			)
		})
	})

	// Create server
	server := &http.Server{
		Addr:         ":8083",
		Handler:      router,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Server starting on :8083")
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Graceful shutdown
	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := server.Shutdown(shutdownCtx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server stopped")
}
