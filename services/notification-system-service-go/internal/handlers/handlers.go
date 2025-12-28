package handlers

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"services/notification-system-service-go/internal/service"
	"services/notification-system-service-go/pkg/models"
)

// Handler handles HTTP requests for notification system
type Handler struct {
	service *service.Service
	logger  *zap.Logger
}

// NewHandler creates a new handler instance
func NewHandler(svc *service.Service, logger *zap.Logger) *Handler {
	return &Handler{
		service: svc,
		logger:  logger,
	}
}

// HealthCheck handles health check requests
func (h *Handler) HealthCheck(w http.ResponseWriter, r *http.Request) {
	response := models.HealthResponse{
		Status:    "healthy",
		Timestamp: time.Now(),
		Version:   "1.0.0",
		Uptime:    0, // Would be calculated from start time in production
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetPlayerNotifications handles GET /api/v1/notifications
func (h *Handler) GetPlayerNotifications(w http.ResponseWriter, r *http.Request) {
	playerID, err := h.getPlayerIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	// Parse query parameters
	status := r.URL.Query().Get("status")
	if status == "" {
		status = "unread"
	}

	notificationType := r.URL.Query().Get("type")

	limitStr := r.URL.Query().Get("limit")
	limit := 20
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offsetStr := r.URL.Query().Get("offset")
	offset := 0
	if offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := h.service.GetPlayerNotifications(r.Context(), playerID, status, notificationType, limit, offset)
	if err != nil {
		h.logger.Error("Failed to get player notifications", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get notifications")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

// CreateNotification handles POST /api/v1/notifications
func (h *Handler) CreateNotification(w http.ResponseWriter, r *http.Request) {
	var req models.CreateNotificationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
		return
	}

	notification, err := h.service.CreateNotification(r.Context(), &req)
	if err != nil {
		h.logger.Error("Failed to create notification", zap.Error(err))
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusCreated, notification)
}

// GetNotificationByID handles GET /api/v1/notifications/{notificationId}
func (h *Handler) GetNotificationByID(w http.ResponseWriter, r *http.Request) {
	playerID, err := h.getPlayerIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	notificationIDStr := chi.URLParam(r, "notificationId")
	notificationID, err := uuid.Parse(notificationIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid notification ID")
		return
	}

	notification, err := h.service.GetNotificationByID(r.Context(), notificationID, playerID)
	if err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondError(w, http.StatusNotFound, "Notification not found")
		} else {
			h.logger.Error("Failed to get notification", zap.Error(err))
			h.respondError(w, http.StatusInternalServerError, "Failed to get notification")
		}
		return
	}

	h.respondJSON(w, http.StatusOK, notification)
}

// MarkNotificationAsRead handles PUT /api/v1/notifications/{notificationId}/read
func (h *Handler) MarkNotificationAsRead(w http.ResponseWriter, r *http.Request) {
	playerID, err := h.getPlayerIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	notificationIDStr := chi.URLParam(r, "notificationId")
	notificationID, err := uuid.Parse(notificationIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid notification ID")
		return
	}

	err = h.service.MarkAsRead(r.Context(), notificationID, playerID)
	if err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondError(w, http.StatusNotFound, "Notification not found")
		} else {
			h.logger.Error("Failed to mark notification as read", zap.Error(err))
			h.respondError(w, http.StatusInternalServerError, "Failed to mark notification as read")
		}
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "marked as read"})
}

// DeleteNotification handles DELETE /api/v1/notifications/{notificationId}
func (h *Handler) DeleteNotification(w http.ResponseWriter, r *http.Request) {
	playerID, err := h.getPlayerIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	notificationIDStr := chi.URLParam(r, "notificationId")
	notificationID, err := uuid.Parse(notificationIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid notification ID")
		return
	}

	err = h.service.DeleteNotification(r.Context(), notificationID, playerID)
	if err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondError(w, http.StatusNotFound, "Notification not found")
		} else {
			h.logger.Error("Failed to delete notification", zap.Error(err))
			h.respondError(w, http.StatusInternalServerError, "Failed to delete notification")
		}
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "deleted"})
}

// GetUnreadCount handles GET /api/v1/notifications/unread/count
func (h *Handler) GetUnreadCount(w http.ResponseWriter, r *http.Request) {
	playerID, err := h.getPlayerIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	response, err := h.service.GetUnreadCount(r.Context(), playerID)
	if err != nil {
		h.logger.Error("Failed to get unread count", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get unread count")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

// MarkBulkAsRead handles PUT /api/v1/notifications/bulk/read
func (h *Handler) MarkBulkAsRead(w http.ResponseWriter, r *http.Request) {
	playerID, err := h.getPlayerIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	var req models.BulkReadRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
		return
	}

	if len(req.NotificationIDs) == 0 {
		h.respondError(w, http.StatusBadRequest, "Notification IDs are required")
		return
	}

	err = h.service.MarkBulkAsRead(r.Context(), &req, playerID)
	if err != nil {
		h.logger.Error("Failed to mark bulk as read", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to mark notifications as read")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "bulk marked as read"})
}

// DeleteBulkNotifications handles DELETE /api/v1/notifications/bulk
func (h *Handler) DeleteBulkNotifications(w http.ResponseWriter, r *http.Request) {
	playerID, err := h.getPlayerIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	var req models.BulkDeleteRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
		return
	}

	if len(req.NotificationIDs) == 0 {
		h.respondError(w, http.StatusBadRequest, "Notification IDs are required")
		return
	}

	err = h.service.DeleteBulkNotifications(r.Context(), &req, playerID)
	if err != nil {
		h.logger.Error("Failed to delete bulk notifications", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to delete notifications")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "bulk deleted"})
}

// getPlayerIDFromContext extracts player ID from request context (would be set by auth middleware)
func (h *Handler) getPlayerIDFromContext(r *http.Request) (uuid.UUID, error) {
	// In production, this would extract from JWT token or session
	// For now, we'll get it from a header for testing
	playerIDStr := r.Header.Get("X-Player-ID")
	if playerIDStr == "" {
		return uuid.Nil, fmt.Errorf("player ID not found in context")
	}

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		return uuid.Nil, fmt.Errorf("invalid player ID format")
	}

	return playerID, nil
}

// respondJSON sends a JSON response
func (h *Handler) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
	}
}

// respondError sends an error response
func (h *Handler) respondError(w http.ResponseWriter, status int, message string) {
	response := models.ErrorResponse{
		Error:   http.StatusText(status),
		Code:    strconv.Itoa(status),
		Message: message,
	}
	h.respondJSON(w, status, response)
}


