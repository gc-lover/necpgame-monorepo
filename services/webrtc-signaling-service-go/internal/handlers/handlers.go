package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"go.uber.org/zap"

	"necpgame/services/webrtc-signaling-service-go/internal/service"
	"necpgame/services/webrtc-signaling-service-go/pkg/models"
)

// Handlers handles HTTP requests
type Handlers struct {
	service *service.Service
	logger  *zap.Logger
}

// NewHandlers creates new handlers instance
func NewHandlers(svc *service.Service, logger *zap.Logger) *Handlers {
	return &Handlers{
		service: svc,
		logger:  logger,
	}
}

// Metrics
var (
	requestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "webrtc_signaling_requests_total",
			Help: "Total number of requests processed",
		},
		[]string{"method", "endpoint", "status"},
	)

	requestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "webrtc_signaling_request_duration_seconds",
			Help:    "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "endpoint"},
	)

	activeConnections = promauto.NewGauge(
		prometheus.GaugeOpts{
			Name: "webrtc_signaling_active_connections",
			Help: "Number of active voice connections",
		},
	)
)

// HealthCheck handles health check requests
func (h *Handlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	start := time.Now()

	response := models.HealthResponse{
		Service:   "webrtc-signaling",
		Status:    "healthy",
		Timestamp: time.Now(),
		Version:   "1.0.0",
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(response)

	duration := time.Since(start)
	requestDuration.WithLabelValues("GET", "/health").Observe(duration.Seconds())
	requestsTotal.WithLabelValues("GET", "/health", "200").Inc()
}

// BatchHealthCheck handles batch health checks
func (h *Handlers) BatchHealthCheck(w http.ResponseWriter, r *http.Request) {
	start := time.Now()

	var req struct {
		Services []string `json:"services"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, "Invalid request", http.StatusBadRequest, err)
		return
	}

	results := make([]models.HealthResponse, len(req.Services))
	for i, svc := range req.Services {
		results[i] = models.HealthResponse{
			Service:   svc,
			Status:    "healthy", // Simplified - in production, check actual service health
			Timestamp: time.Now(),
			Version:   "1.0.0",
		}
	}

	response := map[string]interface{}{
		"results":     results,
		"total_time_ms": time.Since(start).Milliseconds(),
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(response)

	duration := time.Since(start)
	requestDuration.WithLabelValues("POST", "/health/batch").Observe(duration.Seconds())
	requestsTotal.WithLabelValues("POST", "/health/batch", "200").Inc()
}

// HealthWebSocket handles WebSocket health connections (placeholder)
func (h *Handlers) HealthWebSocket(w http.ResponseWriter, r *http.Request) {
	// Simplified WebSocket health check
	response := models.WebSocketHealthMessage{
		Type:      "health_update",
		Timestamp: time.Now(),
		Health: models.HealthResponse{
			Service:   "webrtc-signaling",
			Status:    "healthy",
			Timestamp: time.Now(),
			Version:   "1.0.0",
		},
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(response)

	requestsTotal.WithLabelValues("GET", "/health/ws", "200").Inc()
}

// ListVoiceChannels handles listing voice channels
func (h *Handlers) ListVoiceChannels(w http.ResponseWriter, r *http.Request) {
	start := time.Now()

	// Parse query parameters
	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")
	channelType := r.URL.Query().Get("type")
	status := r.URL.Query().Get("status")

	limit := 20 // default
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0 // default
	if offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := h.service.ListVoiceChannels(r.Context(), limit, offset, channelType, status)
	if err != nil {
		h.respondError(w, "Failed to list voice channels", http.StatusInternalServerError, err)
		return
	}

	h.respondJSON(w, response, http.StatusOK)

	duration := time.Since(start)
	requestDuration.WithLabelValues("GET", "/voice-channels").Observe(duration.Seconds())
	requestsTotal.WithLabelValues("GET", "/voice-channels", "200").Inc()
}

// CreateVoiceChannel handles creating a new voice channel
func (h *Handlers) CreateVoiceChannel(w http.ResponseWriter, r *http.Request) {
	start := time.Now()

	var req models.CreateVoiceChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, "Invalid request body", http.StatusBadRequest, err)
		return
	}

	channel, err := h.service.CreateVoiceChannel(r.Context(), req)
	if err != nil {
		h.respondError(w, "Failed to create voice channel", http.StatusInternalServerError, err)
		return
	}

	response := models.VoiceChannelResponse{Channel: *channel}
	h.respondJSON(w, response, http.StatusCreated)

	duration := time.Since(start)
	requestDuration.WithLabelValues("POST", "/voice-channels").Observe(duration.Seconds())
	requestsTotal.WithLabelValues("POST", "/voice-channels", "201").Inc()
}

// GetVoiceChannel handles retrieving a specific voice channel
func (h *Handlers) GetVoiceChannel(w http.ResponseWriter, r *http.Request) {
	start := time.Now()

	channelID := chi.URLParam(r, "channel_id")
	if channelID == "" {
		h.respondError(w, "Channel ID is required", http.StatusBadRequest, nil)
		return
	}

	channel, err := h.service.GetVoiceChannel(r.Context(), channelID)
	if err != nil {
		h.respondError(w, "Failed to get voice channel", http.StatusNotFound, err)
		return
	}

	response := models.VoiceChannelResponse{Channel: *channel}
	h.respondJSON(w, response, http.StatusOK)

	duration := time.Since(start)
	requestDuration.WithLabelValues("GET", "/voice-channels/{id}").Observe(duration.Seconds())
	requestsTotal.WithLabelValues("GET", "/voice-channels/{id}", "200").Inc()
}

// UpdateVoiceChannel handles updating a voice channel
func (h *Handlers) UpdateVoiceChannel(w http.ResponseWriter, r *http.Request) {
	start := time.Now()

	channelID := chi.URLParam(r, "channel_id")
	if channelID == "" {
		h.respondError(w, "Channel ID is required", http.StatusBadRequest, nil)
		return
	}

	var req models.UpdateVoiceChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, "Invalid request body", http.StatusBadRequest, err)
		return
	}

	channel, err := h.service.UpdateVoiceChannel(r.Context(), channelID, req)
	if err != nil {
		h.respondError(w, "Failed to update voice channel", http.StatusInternalServerError, err)
		return
	}

	response := models.VoiceChannelResponse{Channel: *channel}
	h.respondJSON(w, response, http.StatusOK)

	duration := time.Since(start)
	requestDuration.WithLabelValues("PUT", "/voice-channels/{id}").Observe(duration.Seconds())
	requestsTotal.WithLabelValues("PUT", "/voice-channels/{id}", "200").Inc()
}

// DeleteVoiceChannel handles deleting a voice channel
func (h *Handlers) DeleteVoiceChannel(w http.ResponseWriter, r *http.Request) {
	start := time.Now()

	channelID := chi.URLParam(r, "channel_id")
	if channelID == "" {
		h.respondError(w, "Channel ID is required", http.StatusBadRequest, nil)
		return
	}

	err := h.service.DeleteVoiceChannel(r.Context(), channelID)
	if err != nil {
		h.respondError(w, "Failed to delete voice channel", http.StatusInternalServerError, err)
		return
	}

	w.WriteHeader(http.StatusNoContent)

	duration := time.Since(start)
	requestDuration.WithLabelValues("DELETE", "/voice-channels/{id}").Observe(duration.Seconds())
	requestsTotal.WithLabelValues("DELETE", "/voice-channels/{id}", "204").Inc()
}

// JoinVoiceChannel handles joining a voice channel
func (h *Handlers) JoinVoiceChannel(w http.ResponseWriter, r *http.Request) {
	start := time.Now()

	channelID := chi.URLParam(r, "channel_id")
	if channelID == "" {
		h.respondError(w, "Channel ID is required", http.StatusBadRequest, nil)
		return
	}

	var req models.JoinChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, "Invalid request body", http.StatusBadRequest, err)
		return
	}

	response, err := h.service.JoinVoiceChannel(r.Context(), channelID, req)
	if err != nil {
		status := http.StatusInternalServerError
		if err.Error() == "voice channel is full" {
			status = http.StatusConflict
		}
		h.respondError(w, "Failed to join voice channel", status, err)
		return
	}

	h.respondJSON(w, response, http.StatusOK)
	activeConnections.Inc()

	duration := time.Since(start)
	requestDuration.WithLabelValues("POST", "/voice-channels/{id}/join").Observe(duration.Seconds())
	requestsTotal.WithLabelValues("POST", "/voice-channels/{id}/join", "200").Inc()
}

// ExchangeSignalingMessage handles WebRTC signaling
func (h *Handlers) ExchangeSignalingMessage(w http.ResponseWriter, r *http.Request) {
	start := time.Now()

	channelID := chi.URLParam(r, "channel_id")
	if channelID == "" {
		h.respondError(w, "Channel ID is required", http.StatusBadRequest, nil)
		return
	}

	var msg models.SignalingMessage
	if err := json.NewDecoder(r.Body).Decode(&msg); err != nil {
		h.respondError(w, "Invalid signaling message", http.StatusBadRequest, err)
		return
	}

	response, err := h.service.ExchangeSignalingMessage(r.Context(), channelID, msg)
	if err != nil {
		h.respondError(w, "Failed to process signaling message", http.StatusBadRequest, err)
		return
	}

	h.respondJSON(w, response, http.StatusOK)

	duration := time.Since(start)
	requestDuration.WithLabelValues("POST", "/voice-channels/{id}/signal").Observe(duration.Seconds())
	requestsTotal.WithLabelValues("POST", "/voice-channels/{id}/signal", "200").Inc()
}

// LeaveVoiceChannel handles leaving a voice channel
func (h *Handlers) LeaveVoiceChannel(w http.ResponseWriter, r *http.Request) {
	start := time.Now()

	channelID := chi.URLParam(r, "channel_id")
	if channelID == "" {
		h.respondError(w, "Channel ID is required", http.StatusBadRequest, nil)
		return
	}

	// Extract user ID from context (simplified - in production, from JWT)
	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		userID = "anonymous" // For demo purposes
	}

	response, err := h.service.LeaveVoiceChannel(r.Context(), channelID, userID)
	if err != nil {
		h.respondError(w, "Failed to leave voice channel", http.StatusInternalServerError, err)
		return
	}

	h.respondJSON(w, response, http.StatusOK)
	activeConnections.Dec()

	duration := time.Since(start)
	requestDuration.WithLabelValues("POST", "/voice-channels/{id}/leave").Observe(duration.Seconds())
	requestsTotal.WithLabelValues("POST", "/voice-channels/{id}/leave", "200").Inc()
}

// ReportVoiceQuality handles voice quality reports
func (h *Handlers) ReportVoiceQuality(w http.ResponseWriter, r *http.Request) {
	start := time.Now()

	channelID := chi.URLParam(r, "channel_id")
	if channelID == "" {
		h.respondError(w, "Channel ID is required", http.StatusBadRequest, nil)
		return
	}

	var report models.VoiceQualityReport
	if err := json.NewDecoder(r.Body).Decode(&report); err != nil {
		h.respondError(w, "Invalid quality report", http.StatusBadRequest, err)
		return
	}

	response, err := h.service.ReportVoiceQuality(r.Context(), channelID, report)
	if err != nil {
		h.respondError(w, "Failed to process quality report", http.StatusInternalServerError, err)
		return
	}

	h.respondJSON(w, response, http.StatusOK)

	duration := time.Since(start)
	requestDuration.WithLabelValues("POST", "/voice-quality/{id}/report").Observe(duration.Seconds())
	requestsTotal.WithLabelValues("POST", "/voice-quality/{id}/report", "200").Inc()
}

// Helper methods

func (h *Handlers) respondJSON(w http.ResponseWriter, data interface{}, status int) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
	}
}

func (h *Handlers) respondError(w http.ResponseWriter, message string, status int, err error) {
	response := models.Error{
		Code:    status,
		Message: message,
	}

	if err != nil {
		h.logger.Error(message, zap.Error(err))
		response.Details = err.Error()
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(response)

	requestsTotal.WithLabelValues("UNKNOWN", "UNKNOWN", strconv.Itoa(status)).Inc()
}
