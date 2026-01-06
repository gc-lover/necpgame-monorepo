package service

import (
	"encoding/json"
	"net/http"

	"go.uber.org/zap"
)

// HTTPHandler handles HTTP requests for the realtime gateway service
type HTTPHandler struct {
	service *Service
	logger  *zap.Logger
}

// NewHTTPHandler creates a new HTTP handler
func NewHTTPHandler(svc *Service) *HTTPHandler {
	return &HTTPHandler{
		service: svc,
		logger:  svc.logger,
	}
}

// ServeHTTP implements http.Handler
func (h *HTTPHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	switch r.URL.Path {
	case "/health":
		h.handleHealth(w, r)
	default:
		http.NotFound(w, r)
	}
}

// handleHealth handles health check requests
func (h *HTTPHandler) handleHealth(w http.ResponseWriter, r *http.Request) {
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
