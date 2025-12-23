// Issue: #2218 - Backend: Добавить unit-тесты для ws-lobby-go
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"
)

// Router creates and configures the HTTP router
func (s *LobbyService) Router() http.Handler {
	r := chi.NewRouter()

	// CORS middleware
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure for production
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300,
	}))

	// Standard middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(60 * time.Second))

	// Health check endpoint
	r.Get("/health", s.handleHealth)
	r.Get("/ready", s.handleReady)

	// Metrics endpoint
	r.Handle("/metrics", promhttp.Handler())

	// WebSocket endpoint
	r.Get("/ws/lobby", s.HandleWebSocketConnection)

	return r
}

// handleHealth returns service health status
func (s *LobbyService) handleHealth(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "healthy", "service": "ws-lobby-go"}`))
}

// handleReady returns service readiness status
func (s *LobbyService) handleReady(w http.ResponseWriter, r *http.Request) {
	// Check Redis connectivity
	ctx, cancel := context.WithTimeout(r.Context(), 2*time.Second)
	defer cancel()

	if err := s.redis.Ping(ctx).Err(); err != nil {
		s.logger.Error("Redis health check failed", zap.Error(err))
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusServiceUnavailable)
		w.Write([]byte(`{"status": "unhealthy", "service": "ws-lobby-go", "error": "redis_unavailable"}`))
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "ready", "service": "ws-lobby-go"}`))
}
