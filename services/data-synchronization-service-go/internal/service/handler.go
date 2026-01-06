package service

import (
	"encoding/json"
	"net/http"

	"go.uber.org/zap"
)

// Handler handles HTTP requests for the data synchronization service
type Handler struct {
	service *Service
	logger  *zap.Logger
}

// NewHandler creates a new HTTP handler
func NewHandler(svc *Service) *Handler {
	return &Handler{
		service: svc,
		logger:  svc.logger,
	}
}

// ServeHTTP implements http.Handler
func (h *Handler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	switch r.URL.Path {
	case "/health":
		h.handleHealth(w, r)
	default:
		http.NotFound(w, r)
	}
}

// handleHealth handles health check requests
func (h *Handler) handleHealth(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	health, err := h.service.Health(r.Context())
	if err != nil {
		h.logger.Error("health check failed", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	if health.Status != "healthy" {
		w.WriteHeader(http.StatusServiceUnavailable)
	}

	if err := json.NewEncoder(w).Encode(health); err != nil {
		h.logger.Error("failed to encode health response", zap.Error(err))
	}
}

// HealthResponse represents health check response
type HealthResponse struct {
	Status   string            `json:"status"`
	Services map[string]string `json:"services,omitempty"`
}
