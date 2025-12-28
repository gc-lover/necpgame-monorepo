// Social Orders Creation Service
// Issue: #140894825
//
// This service provides advanced order creation with reputation integration,
// validation, optimization, and contractor suggestions for MMOFPS RPG.

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

	"github.com/gorilla/mux"
	"github.com/necp-game/social-orders-creation-service-go/internal/adapter"
	"github.com/necp-game/social-orders-creation-service-go/internal/service"
	"github.com/necp-game/social-orders-creation-service-go/pkg/api"
	"github.com/necp-game/social-orders-creation-service-go/pkg/orders-creation"
	"go.uber.org/zap"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to create logger:", err)
	}
	defer logger.Sync()

	logger.Info("Starting Social Orders Creation Service with ogen handlers")

	// Create existing orders creation service
	creationSvc, err := orderscreation.NewService(logger)
	if err != nil {
		logger.Fatal("Failed to create orders creation service", zap.Error(err))
	}

	// Create new service implementation that adapts the existing one
	socialOrdersSvc := service.NewSocialOrdersService(creationSvc, logger)

	// Create ogen server adapter
	ogenServer := adapter.NewOgenServerAdapter(socialOrdersSvc, logger)

	// Create ogen handler
	ogenHandler := api.NewHandler(ogenServer, logger)

	// Create HTTP router
	router := mux.NewRouter()

	// Add middleware
	router.Use(loggingMiddleware(logger))
	router.Use(recoveryMiddleware(logger))

	// Register ogen routes
	ogenHandler.RegisterRoutes(router)

	// Health check endpoint
	router.HandleFunc("/health", healthCheckHandler(logger)).Methods("GET")

	// Create HTTP server
	srv := &http.Server{
		Addr:         ":8080",
		Handler:      router,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Social Orders Creation Service with ogen handlers started on :8080")
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

// loggingMiddleware adds request logging
func loggingMiddleware(logger *zap.Logger) mux.MiddlewareFunc {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			logger.Info("Request started",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.String("remote_addr", r.RemoteAddr),
			)

			next.ServeHTTP(w, r)

			logger.Info("Request completed",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.Duration("duration", time.Since(start)),
			)
		})
	}
}

// recoveryMiddleware adds panic recovery
func recoveryMiddleware(logger *zap.Logger) mux.MiddlewareFunc {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			defer func() {
				if err := recover(); err != nil {
					logger.Error("Panic recovered", zap.Any("panic", err))
					http.Error(w, "Internal server error", http.StatusInternalServerError)
				}
			}()
			next.ServeHTTP(w, r)
		})
	}
}

// healthCheckHandler handles health check requests
func healthCheckHandler(logger *zap.Logger) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		response := map[string]interface{}{
			"status":    "healthy",
			"service":   "social-orders-creation",
			"timestamp": time.Now(),
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)

		if err := json.NewEncoder(w).Encode(response); err != nil {
			logger.Error("Failed to encode health check response", zap.Error(err))
		}
	}
}
