// Package server Issue: #2224 - Basic service handlers for world events service
// Split from service.go to reduce file size (was 859 lines)
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"go.uber.org/zap"
)

// HealthCheckHandler handles health check requests
func (s *WorldEventsService) HealthCheckHandler(w http.ResponseWriter, request *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"status":    "ok",
		"service":   "world-events-service",
		"timestamp": time.Now().UTC().Format(time.RFC3339),
		"version":   "1.0.0",
	})
}

// ReadinessCheckHandler handles readiness check requests
func (s *WorldEventsService) ReadinessCheckHandler(w http.ResponseWriter, r *http.Request) {
	// Check database connectivity
	ctx, cancel := context.WithTimeout(r.Context(), 3*time.Second)
	defer cancel()

	if err := s.db.PingContext(ctx); err != nil {
		s.logger.Error("Database health check failed", zap.Error(err))
		s.respondError(w, http.StatusServiceUnavailable, "Database not ready")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"status":    "ready",
		"service":   "world-events-service",
		"timestamp": time.Now().UTC().Format(time.RFC3339),
	})
}

// MetricsHandler handles metrics requests
func (s *WorldEventsService) MetricsHandler(w http.ResponseWriter, request *http.Request) {
	// Basic metrics for world events service
	metrics := map[string]interface{}{
		"service": "world-events-service",
		"uptime":  time.Since(time.Now()).String(), // This should be tracked properly
		"version": "1.0.0",
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(metrics)
}
