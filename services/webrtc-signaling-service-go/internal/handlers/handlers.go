package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/webrtc-signaling-service-go/internal/service"
	"necpgame/services/webrtc-signaling-service-go/pkg/models"
)

// Handlers handles HTTP requests for WebRTC signaling service
type Handlers struct {
	service *service.Service
	logger  *zap.Logger
}

// NewHandlers creates a new handlers instance
func NewHandlers(svc *service.Service, logger *zap.Logger) *Handlers {
	return &Handlers{
		service: svc,
		logger:  logger,
	}
}

// SetupRoutes configures all HTTP routes
func (h *Handlers) SetupRoutes(r *chi.Mux) {
	// Health check endpoints
	r.Get("/health", h.HealthCheck)
	r.Post("/health/batch", h.BatchHealthCheck)
	r.Get("/health/ws", h.HealthWebSocket)

	// Metrics endpoint
	r.Handle("/metrics", h.MetricsHandler())

	// API routes
	r.Route("/api/v1", func(r chi.Router) {
		// Voice channels management
		r.Get("/voice-channels", h.ListVoiceChannels)
		r.Post("/voice-channels", h.CreateVoiceChannel)
		r.Get("/voice-channels/{channel_id}", h.GetVoiceChannel)
		r.Put("/voice-channels/{channel_id}", h.UpdateVoiceChannel)
		r.Delete("/voice-channels/{channel_id}", h.DeleteVoiceChannel)

		// Voice channel operations
		r.Post("/voice-channels/{channel_id}/join", h.JoinVoiceChannel)
		r.Post("/voice-channels/{channel_id}/signal", h.ExchangeSignalingMessage)
		r.Post("/voice-channels/{channel_id}/leave", h.LeaveVoiceChannel)

		// Guild voice channel management - INTEGRATION WITH GUILD SYSTEM
		r.Post("/guilds/{guild_id}/voice-channels", h.CreateGuildVoiceChannel)
		r.Get("/guilds/{guild_id}/voice-channels", h.ListGuildVoiceChannels)
		r.Put("/guilds/{guild_id}/voice-channels/{channel_id}", h.UpdateGuildVoiceChannel)
		r.Post("/guilds/{guild_id}/voice-channels/{channel_id}/join", h.JoinGuildVoiceChannel)

		// Voice quality monitoring
		r.Post("/voice-quality/{channel_id}/report", h.ReportVoiceQuality)
	})
}

// Health check handlers
func (h *Handlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := contextWithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	err := h.service.HealthCheck(ctx)
	if err != nil {
		h.logger.Error("Health check failed", zap.Error(err))
		h.respondError(w, http.StatusServiceUnavailable, "Service unhealthy")
		return
	}

	health := models.HealthStatus{
		Service:   "webrtc-signaling-service",
		Status:    "healthy",
		Version:   "1.0.0",
		Timestamp: time.Now(),
		Uptime:    "running", // TODO: Implement uptime tracking
		Database:  "connected",
		Redis:     "connected",
	}
	health.WebSocket.ActiveConnections = h.getActiveConnections()

	h.respondJSON(w, http.StatusOK, health)
}

func (h *Handlers) BatchHealthCheck(w http.ResponseWriter, r *http.Request) {
	// Simple batch health check - could be extended
	h.HealthCheck(w, r)
}

func (h *Handlers) HealthWebSocket(w http.ResponseWriter, r *http.Request) {
	// WebSocket health check endpoint
	h.HealthCheck(w, r)
}

// Metrics handler
func (h *Handlers) MetricsHandler() http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// TODO: Implement Prometheus metrics
		w.Header().Set("Content-Type", "text/plain")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("# WebRTC Signaling Service Metrics\n"))
		w.Write([]byte("webrtc_active_connections 0\n"))
		w.Write([]byte("webrtc_total_messages 0\n"))
	})
}

// Guild Voice Channel handlers
func (h *Handlers) CreateGuildVoiceChannel(w http.ResponseWriter, r *http.Request) {
	var req models.GuildVoiceChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get user ID from JWT token
	ownerID := uuid.New() // Placeholder

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	response, err := h.service.CreateGuildVoiceChannel(ctx, &req, ownerID)
	if err != nil {
		h.logger.Error("Failed to create guild voice channel", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to create guild voice channel")
		return
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *Handlers) ListGuildVoiceChannels(w http.ResponseWriter, r *http.Request) {
	guildIDStr := r.URL.Query().Get("guild_id")
	if guildIDStr == "" {
		h.respondError(w, http.StatusBadRequest, "Missing guild_id parameter")
		return
	}

	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid guild_id format")
		return
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	channels, err := h.service.GetGuildVoiceChannels(ctx, guildID)
	if err != nil {
		h.logger.Error("Failed to list guild voice channels", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to list guild voice channels")
		return
	}

	h.respondJSON(w, http.StatusOK, channels)
}

func (h *Handlers) UpdateGuildVoiceChannel(w http.ResponseWriter, r *http.Request) {
	channelIDStr := chi.URLParam(r, "channel_id")
	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid channel_id format")
		return
	}

	var req models.GuildVoiceChannelUpdateRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get user ID from JWT token
	userID := uuid.New() // Placeholder

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	response, err := h.service.UpdateGuildVoiceChannel(ctx, channelID, &req, userID)
	if err != nil {
		h.logger.Error("Failed to update guild voice channel", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to update guild voice channel")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) JoinGuildVoiceChannel(w http.ResponseWriter, r *http.Request) {
	channelIDStr := chi.URLParam(r, "channel_id")
	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid channel_id format")
		return
	}

	var req models.JoinVoiceChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	userID, err := uuid.Parse(req.UserID)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid user_id format")
		return
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	response, err := h.service.JoinGuildVoiceChannel(ctx, channelID, userID)
	if err != nil {
		h.logger.Error("Failed to join guild voice channel", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

// Voice channel handlers
func (h *Handlers) ListVoiceChannels(w http.ResponseWriter, r *http.Request) {
	guildIDStr := r.URL.Query().Get("guild_id")
	var guildID *uuid.UUID

	if guildIDStr != "" {
		parsed, err := uuid.Parse(guildIDStr)
		if err != nil {
			h.respondError(w, http.StatusBadRequest, "Invalid guild_id format")
			return
		}
		guildID = &parsed
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	channels, err := h.service.ListVoiceChannels(ctx, guildID)
	if err != nil {
		h.logger.Error("Failed to list voice channels", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to list voice channels")
		return
	}

	// Convert to response format
	responses := make([]models.VoiceChannelResponse, len(channels))
	for i, channel := range channels {
		responses[i] = models.VoiceChannelResponse{
			ID:           channel.ID.String(),
			Name:         channel.Name,
			Type:         channel.Type,
			OwnerID:      channel.OwnerID.String(),
			MaxUsers:     channel.MaxUsers,
			CurrentUsers: channel.CurrentUsers,
			IsActive:     channel.IsActive,
			CreatedAt:    channel.CreatedAt,
		}
		if channel.GuildID != nil {
			guildIDStr := channel.GuildID.String()
			responses[i].GuildID = &guildIDStr
		}
	}

	h.respondJSON(w, http.StatusOK, responses)
}

func (h *Handlers) CreateVoiceChannel(w http.ResponseWriter, r *http.Request) {
	var req models.VoiceChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get user ID from JWT token
	ownerID := uuid.New() // Placeholder

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	response, err := h.service.CreateVoiceChannel(ctx, &req, ownerID)
	if err != nil {
		h.logger.Error("Failed to create voice channel", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to create voice channel")
		return
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *Handlers) GetVoiceChannel(w http.ResponseWriter, r *http.Request) {
	channelIDStr := chi.URLParam(r, "channel_id")
	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid channel_id format")
		return
	}

	ctx, cancel := contextWithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	response, err := h.service.GetVoiceChannel(ctx, channelID)
	if err != nil {
		h.logger.Error("Failed to get voice channel", zap.Error(err))
		h.respondError(w, http.StatusNotFound, "Voice channel not found")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) UpdateVoiceChannel(w http.ResponseWriter, r *http.Request) {
	channelIDStr := chi.URLParam(r, "channel_id")
	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid channel_id format")
		return
	}

	var req models.VoiceChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	response, err := h.service.UpdateVoiceChannel(ctx, channelID, &req)
	if err != nil {
		h.logger.Error("Failed to update voice channel", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to update voice channel")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) DeleteVoiceChannel(w http.ResponseWriter, r *http.Request) {
	channelIDStr := chi.URLParam(r, "channel_id")
	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid channel_id format")
		return
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	err = h.service.DeleteVoiceChannel(ctx, channelID)
	if err != nil {
		h.logger.Error("Failed to delete voice channel", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to delete voice channel")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "deleted"})
}

// Voice channel operation handlers
func (h *Handlers) JoinVoiceChannel(w http.ResponseWriter, r *http.Request) {
	channelIDStr := chi.URLParam(r, "channel_id")
	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid channel_id format")
		return
	}

	var req models.JoinVoiceChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	userID, err := uuid.Parse(req.UserID)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid user_id format")
		return
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	response, err := h.service.JoinVoiceChannel(ctx, channelID, userID)
	if err != nil {
		h.logger.Error("Failed to join voice channel", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) ExchangeSignalingMessage(w http.ResponseWriter, r *http.Request) {
	channelIDStr := chi.URLParam(r, "channel_id")
	_, err := uuid.Parse(channelIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid channel_id format")
		return
	}

	var req models.SignalingRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	ctx, cancel := contextWithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	response, err := h.service.ExchangeSignalingMessage(ctx, &req)
	if err != nil {
		h.logger.Error("Failed to exchange signaling message", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to exchange signaling message")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) LeaveVoiceChannel(w http.ResponseWriter, r *http.Request) {
	channelIDStr := chi.URLParam(r, "channel_id")
	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid channel_id format")
		return
	}

	userIDStr := r.URL.Query().Get("user_id")
	if userIDStr == "" {
		h.respondError(w, http.StatusBadRequest, "Missing user_id parameter")
		return
	}

	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid user_id format")
		return
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	err = h.service.LeaveVoiceChannel(ctx, channelID, userID)
	if err != nil {
		h.logger.Error("Failed to leave voice channel", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to leave voice channel")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "left"})
}

// Voice quality handler
func (h *Handlers) ReportVoiceQuality(w http.ResponseWriter, r *http.Request) {
	channelIDStr := chi.URLParam(r, "channel_id")
	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid channel_id format")
		return
	}

	var req models.VoiceQualityReportRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get user ID from JWT token
	userID := uuid.New() // Placeholder

	ctx, cancel := contextWithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	err = h.service.ReportVoiceQuality(ctx, channelID, userID, &req)
	if err != nil {
		h.logger.Error("Failed to report voice quality", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to report voice quality")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "reported"})
}

// Helper methods
func (h *Handlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *Handlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

func (h *Handlers) getActiveConnections() int {
	// TODO: Implement active connections tracking
	return 0
}

// contextWithTimeout creates a context with timeout
func contextWithTimeout(parent context.Context, timeout time.Duration) (context.Context, context.CancelFunc) {
	return context.WithTimeout(parent, timeout)
}

// PERFORMANCE: Handlers use context timeouts for all operations
// Error responses include appropriate HTTP status codes
// JSON responses are properly formatted for API clients