// Issue: Implement integration-domain-service-go
// HTTP handlers for integration domain service
// Enterprise-grade handlers with performance optimizations and error handling

package handlers

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/integration-domain-service-go/internal/config"
	"github.com/gc-lover/necpgame-monorepo/services/integration-domain-service-go/internal/service"
	"github.com/gc-lover/necpgame-monorepo/services/integration-domain-service-go/pkg/models"
	"go.uber.org/zap"
)

// Handlers contains all HTTP handlers
type Handlers struct {
	service *service.IntegrationDomainService
	logger  *zap.Logger
	config  *config.Config
}

// NewHandlers creates new handlers
func NewHandlers(svc *service.IntegrationDomainService, logger *zap.Logger, cfg *config.Config) *Handlers {
	return &Handlers{
		service: svc,
		logger:  logger,
		config:  cfg,
	}
}

// HealthCheck handles basic health check
func (h *Handlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	startTime := time.Now()

	response := models.HealthResponse{
		Domain:    "integration-domain",
		Status:    "healthy",
		Timestamp: startTime,
	}

	h.writeJSONResponse(w, http.StatusOK, response)

	duration := time.Since(startTime)
	h.logger.Info("Health check handled",
		zap.Duration("duration", duration),
		zap.String("status", "healthy"))
}

// DomainHealthCheck handles integration domain specific health check
func (h *Handlers) DomainHealthCheck(w http.ResponseWriter, r *http.Request) {
	startTime := time.Now()

	ctx := r.Context()
	response := h.service.DomainHealthCheck(ctx)

	h.writeJSONResponse(w, http.StatusOK, response)

	duration := time.Since(startTime)
	h.logger.Info("Domain health check handled",
		zap.Duration("duration", duration),
		zap.String("status", response.Status))
}

// BatchHealthCheck handles batch health checks
func (h *Handlers) BatchHealthCheck(w http.ResponseWriter, r *http.Request) {
	startTime := time.Now()

	var req models.BatchHealthRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	// Validate request
	if len(req.Domains) == 0 {
		h.writeErrorResponse(w, http.StatusBadRequest, "Domains array cannot be empty", nil)
		return
	}

	if len(req.Domains) > 10 {
		h.writeErrorResponse(w, http.StatusBadRequest, "Too many domains requested (max 10)", nil)
		return
	}

	ctx := r.Context()
	response := h.service.BatchHealthCheck(ctx, req)

	h.writeJSONResponse(w, http.StatusOK, response)

	duration := time.Since(startTime)
	h.logger.Info("Batch health check handled",
		zap.Int("domain_count", len(req.Domains)),
		zap.Int("total_time_ms", response.TotalTimeMs),
		zap.Duration("duration", duration))
}

// HealthWebSocket handles WebSocket connections for real-time health monitoring
func (h *Handlers) HealthWebSocket(w http.ResponseWriter, r *http.Request) {
	if !h.config.EnableWebSocket {
		h.writeErrorResponse(w, http.StatusServiceUnavailable, "WebSocket support disabled", nil)
		return
	}

	h.service.GetWebSocketManager().HandleConnection(w, r)
}

// Metrics handles Prometheus metrics endpoint
func (h *Handlers) Metrics(w http.ResponseWriter, r *http.Request) {
	if !h.config.MetricsEnabled {
		h.writeErrorResponse(w, http.StatusServiceUnavailable, "Metrics disabled", nil)
		return
	}

	snapshot := h.service.GetMetrics()
	h.writeJSONResponse(w, http.StatusOK, snapshot)
}

// writeJSONResponse writes a JSON response
func (h *Handlers) writeJSONResponse(w http.ResponseWriter, statusCode int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("Cache-Control", "max-age=300, s-maxage=600")
	w.Header().Set("ETag", "\"-api-v1-integration-domain-health-v1.0\"")
	w.WriteHeader(statusCode)

	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
	}
}

// writeErrorResponse writes an error response
func (h *Handlers) writeErrorResponse(w http.ResponseWriter, statusCode int, message string, err error) {
	errorResp := models.Error{
		Message:   message,
		Domain:    "integration-domain",
		Timestamp: time.Now(),
		Code:      statusCode,
	}

	if err != nil {
		h.logger.Error("Request error",
			zap.Int("status_code", statusCode),
			zap.String("message", message),
			zap.Error(err))
	} else {
		h.logger.Warn("Request error",
			zap.Int("status_code", statusCode),
			zap.String("message", message))
	}

	h.writeJSONResponse(w, statusCode, errorResp)
}


