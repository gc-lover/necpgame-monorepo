// Issue: #140875800
package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"tournament-spectator-service-go/internal/models"
	"tournament-spectator-service-go/internal/service"
)

// BACKEND NOTE: Tournament Spectator Handlers - Enterprise-grade HTTP API
// Performance: Optimized JSON responses, proper error handling, request validation
// Architecture: REST API with chi router, middleware integration

// Handler handles HTTP requests for spectator service
type Handler struct {
	service *service.Service
	logger  *zap.Logger
}

// NewHandler creates new handler instance
func NewHandler(svc *service.Service, logger *zap.Logger) *Handler {
	return &Handler{
		service: svc,
		logger:  logger,
	}
}

// JoinSpectatorSession handles POST /sessions
func (h *Handler) JoinSpectatorSession(w http.ResponseWriter, r *http.Request) {
	var req models.JoinSpectatorRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request payload")
		return
	}

	// Get spectator ID from context (would be set by auth middleware)
	spectatorID := uuid.New() // Placeholder - should come from JWT

	ctx := r.Context()
	session, err := h.service.JoinSpectatorSession(ctx, &req, spectatorID, r.RemoteAddr, r.UserAgent())
	if err != nil {
		h.logger.Error("Failed to join spectator session", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to join spectator session")
		return
	}

	h.respondJSON(w, http.StatusCreated, session)
}

// GetSpectatorSession handles GET /sessions/{session_id}
func (h *Handler) GetSpectatorSession(w http.ResponseWriter, r *http.Request) {
	sessionIDStr := chi.URLParam(r, "session_id")
	sessionID, err := uuid.Parse(sessionIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid session ID")
		return
	}

	ctx := r.Context()
	session, err := h.service.GetSpectatorSession(ctx, sessionID)
	if err != nil {
		if err.Error() == "spectator session not found" {
			h.respondError(w, http.StatusNotFound, "Spectator session not found")
			return
		}
		h.logger.Error("Failed to get spectator session", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get spectator session")
		return
	}

	h.respondJSON(w, http.StatusOK, session)
}

// LeaveSpectatorSession handles DELETE /sessions/{session_id}
func (h *Handler) LeaveSpectatorSession(w http.ResponseWriter, r *http.Request) {
	sessionIDStr := chi.URLParam(r, "session_id")
	sessionID, err := uuid.Parse(sessionIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid session ID")
		return
	}

	ctx := r.Context()
	if err := h.service.LeaveSpectatorSession(ctx, sessionID); err != nil {
		if err.Error() == "spectator session not found" {
			h.respondError(w, http.StatusNotFound, "Spectator session not found")
			return
		}
		h.logger.Error("Failed to leave spectator session", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to leave spectator session")
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// ListSpectatorSessions handles GET /sessions
func (h *Handler) ListSpectatorSessions(w http.ResponseWriter, r *http.Request) {
	// Parse query parameters
	tournamentIDStr := r.URL.Query().Get("tournament_id")
	var tournamentID *uuid.UUID
	if tournamentIDStr != "" {
		if id, err := uuid.Parse(tournamentIDStr); err == nil {
			tournamentID = &id
		}
	}

	statusStr := r.URL.Query().Get("status")
	var status *models.SpectatorStatus
	if statusStr != "" {
		s := models.SpectatorStatus(statusStr)
		status = &s
	}

	limit := 20 // default
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0 // default
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	ctx := r.Context()
	sessionList, err := h.service.ListSpectatorSessions(ctx, tournamentID, status, limit, offset)
	if err != nil {
		h.logger.Error("Failed to list spectator sessions", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to list spectator sessions")
		return
	}

	h.respondJSON(w, http.StatusOK, sessionList)
}

// UpdateCameraSettings handles PUT /sessions/{session_id}/camera
func (h *Handler) UpdateCameraSettings(w http.ResponseWriter, r *http.Request) {
	sessionIDStr := chi.URLParam(r, "session_id")
	sessionID, err := uuid.Parse(sessionIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid session ID")
		return
	}

	var settings models.CameraSettings
	if err := json.NewDecoder(r.Body).Decode(&settings); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request payload")
		return
	}

	ctx := r.Context()
	if err := h.service.UpdateCameraSettings(ctx, sessionID, &settings); err != nil {
		if err.Error() == "spectator session not found" {
			h.respondError(w, http.StatusNotFound, "Spectator session not found")
			return
		}
		h.logger.Error("Failed to update camera settings", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to update camera settings")
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// SendChatMessage handles POST /sessions/{session_id}/chat
func (h *Handler) SendChatMessage(w http.ResponseWriter, r *http.Request) {
	sessionIDStr := chi.URLParam(r, "session_id")
	sessionID, err := uuid.Parse(sessionIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid session ID")
		return
	}

	var req struct {
		Content     string               `json:"content"`
		MessageType models.MessageType   `json:"message_type,omitempty"`
		ReplyTo     *uuid.UUID           `json:"reply_to,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request payload")
		return
	}

	if req.MessageType == "" {
		req.MessageType = models.TypeText
	}

	// Get sender info from context (would be set by auth middleware)
	senderID := uuid.New() // Placeholder
	senderName := "Anonymous" // Placeholder

	ctx := r.Context()
	message, err := h.service.SendChatMessage(ctx, sessionID, senderID, senderName, req.Content, req.MessageType, req.ReplyTo)
	if err != nil {
		h.logger.Error("Failed to send chat message", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to send chat message")
		return
	}

	h.respondJSON(w, http.StatusCreated, message)
}

// GetChatMessages handles GET /sessions/{session_id}/chat
func (h *Handler) GetChatMessages(w http.ResponseWriter, r *http.Request) {
	sessionIDStr := chi.URLParam(r, "session_id")
	sessionID, err := uuid.Parse(sessionIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid session ID")
		return
	}

	limit := 50 // default for chat
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0 // default
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	ctx := r.Context()
	messageList, err := h.service.GetChatMessages(ctx, sessionID, limit, offset)
	if err != nil {
		h.logger.Error("Failed to get chat messages", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get chat messages")
		return
	}

	h.respondJSON(w, http.StatusOK, messageList)
}

// GetTournamentStats handles GET /tournaments/{tournament_id}/stats
func (h *Handler) GetTournamentStats(w http.ResponseWriter, r *http.Request) {
	tournamentIDStr := chi.URLParam(r, "tournament_id")
	tournamentID, err := uuid.Parse(tournamentIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid tournament ID")
		return
	}

	ctx := r.Context()
	stats, err := h.service.GetTournamentStats(ctx, tournamentID)
	if err != nil {
		h.logger.Error("Failed to get tournament stats", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get tournament stats")
		return
	}

	h.respondJSON(w, http.StatusOK, stats)
}

// HealthCheck handles GET /health
func (h *Handler) HealthCheck(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	health := h.service.HealthCheck(ctx)
	h.respondJSON(w, http.StatusOK, health)
}

// Helper methods

func (h *Handler) respondJSON(w http.ResponseWriter, status int, payload interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(payload); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
	}
}

func (h *Handler) respondError(w http.ResponseWriter, status int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	errorResp := models.ErrorResponse{
		Message:   message,
		Domain:    "tournament-spectator",
		Timestamp: time.Now(),
		Code:      status,
	}

	if err := json.NewEncoder(w).Encode(errorResp); err != nil {
		h.logger.Error("Failed to encode error response", zap.Error(err))
	}
}
