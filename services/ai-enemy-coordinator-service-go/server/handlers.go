package server

import (
	"context"
	"fmt"
	"log/slog"
	"net/http"
	"time"

	"necpgame/services/ai-enemy-coordinator-service-go/pkg/api"
)

// Handler implements the API handlers
type Handler struct {
	service Service
}

// NewHandler creates a new handler instance
func NewHandler(service Service) *Handler {
	return &Handler{
		service: service,
	}
}

// AiEnemyCoordinatorHealthCheck handles health check requests
func (h *Handler) AiEnemyCoordinatorHealthCheck(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	status := "healthy"
	timestamp := time.Now().UTC().Format(time.RFC3339)

	// TODO: Add actual health checks (DB connectivity, dependencies)

	resp := map[string]interface{}{
		"status":    status,
		"timestamp": timestamp,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	// Simple JSON encoding for health check
	if _, err := w.Write([]byte(`{"status":"healthy","timestamp":"` + timestamp + `"}`)); err != nil {
		slog.Error("Failed to write health check response", "error", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}

// SpawnAiEnemy handles AI enemy spawning requests
func (h *Handler) SpawnAiEnemy(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second) // Performance: Context timeouts
	defer cancel()

	// Decode request
	req, err := api.DecodeSpawnAiEnemyRequest(r)
	if err != nil {
		slog.Error("Failed to decode spawn request", "error", err)
		http.Error(w, "Bad request", http.StatusBadRequest)
		return
	}

	// Convert to service request type
	serviceReq := api.SpawnAiEnemyRequest(*req)

	// Call service
	resp, err := h.service.SpawnAiEnemy(ctx, serviceReq)
	if err != nil {
		slog.Error("Failed to spawn AI enemy", "error", err)
		http.Error(w, fmt.Sprintf("Spawn failed: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)

	if err := api.EncodeSpawnAiEnemyResponse(resp, w); err != nil {
		slog.Error("Failed to encode spawn response", "error", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}

// NewAiEnemyCoordinatorServer creates the API server with all handlers
func NewAiEnemyCoordinatorServer(service Service) *api.Server {
	handler := NewHandler(service)

	return &api.Server{
		AiEnemyCoordinatorHealthCheck: handler.AiEnemyCoordinatorHealthCheck,
		SpawnAiEnemy:                  handler.SpawnAiEnemy,
	}
}