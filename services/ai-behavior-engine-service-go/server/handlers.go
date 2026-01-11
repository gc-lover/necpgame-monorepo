package server

import (
	"context"
	"encoding/json"
	"log/slog"
	"net/http"
	"time"

	"github.com/google/uuid"
)

// Handler implements the API handlers for AI Behavior Engine
type Handler struct {
	service Service
}

// NewHandler creates a new handler instance
func NewHandler(service Service) *Handler {
	return &Handler{
		service: service,
	}
}

// AiBehaviorEngineHealthCheck handles health check requests
func (h *Handler) AiBehaviorEngineHealthCheck(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	status := "healthy"
	timestamp := time.Now().UTC().Format(time.RFC3339)

	// TODO: Add actual health checks (DB connectivity, dependencies)

	resp := map[string]interface{}{
		"status":    status,
		"timestamp": timestamp,
		"service":   "ai-behavior-engine",
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	if err := json.NewEncoder(w).Encode(resp); err != nil {
		slog.Error("Failed to write health check response", "error", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}

// ExecuteBehavior handles behavior execution requests
func (h *Handler) ExecuteBehavior(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()

	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req ExecuteBehaviorRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		slog.Error("Failed to decode execute behavior request", "error", err)
		http.Error(w, "Bad request", http.StatusBadRequest)
		return
	}

	result, err := h.service.ExecuteBehavior(ctx, req)
	if err != nil {
		slog.Error("Failed to execute behavior", "error", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	if err := json.NewEncoder(w).Encode(result); err != nil {
		slog.Error("Failed to encode behavior result", "error", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}

// GetBehaviorState handles behavior state retrieval requests
func (h *Handler) GetBehaviorState(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extract enemy ID from URL path
	// TODO: Implement proper URL parsing
	enemyIDStr := r.URL.Query().Get("enemy_id")
	if enemyIDStr == "" {
		http.Error(w, "Missing enemy_id parameter", http.StatusBadRequest)
		return
	}

	enemyID, err := uuid.Parse(enemyIDStr)
	if err != nil {
		slog.Error("Invalid enemy ID format", "enemy_id", enemyIDStr, "error", err)
		http.Error(w, "Invalid enemy ID", http.StatusBadRequest)
		return
	}

	state, err := h.service.GetBehaviorState(ctx, enemyID)
	if err != nil {
		slog.Error("Failed to get behavior state", "error", err, "enemy_id", enemyID)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	if err := json.NewEncoder(w).Encode(state); err != nil {
		slog.Error("Failed to encode behavior state", "error", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}

// UpdateBehaviorTree handles behavior tree update requests
func (h *Handler) UpdateBehaviorTree(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()

	if r.Method != http.MethodPut {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req UpdateBehaviorTreeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		slog.Error("Failed to decode update behavior tree request", "error", err)
		http.Error(w, "Bad request", http.StatusBadRequest)
		return
	}

	err := h.service.UpdateBehaviorTree(ctx, req)
	if err != nil {
		slog.Error("Failed to update behavior tree", "error", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	resp := map[string]string{"status": "updated"}
	if err := json.NewEncoder(w).Encode(resp); err != nil {
		slog.Error("Failed to encode update response", "error", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}