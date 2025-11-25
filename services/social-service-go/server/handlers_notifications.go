package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/pkg/api/notifications"
	"github.com/sirupsen/logrus"
)

type NotificationsServiceInterface interface {
	GetNotification(ctx context.Context, characterID, notificationID uuid.UUID) (*notifications.Notification, error)
	ListNotifications(ctx context.Context, characterID uuid.UUID, params notifications.ListNotificationsParams) ([]notifications.Notification, error)
	MarkNotificationAsRead(ctx context.Context, characterID, notificationID uuid.UUID) error
	MarkAllNotificationsAsRead(ctx context.Context, characterID uuid.UUID) error
	DeleteNotification(ctx context.Context, characterID, notificationID uuid.UUID) error
	GetNotificationPreferences(ctx context.Context, characterID uuid.UUID) (*notifications.NotificationPreferences, error)
	UpdateNotificationPreferences(ctx context.Context, characterID uuid.UUID, req *notifications.UpdateNotificationPreferencesRequest) (*notifications.NotificationPreferences, error)
}

type NotificationsHandlers struct {
	service NotificationsServiceInterface
	logger  *logrus.Logger
}

func NewNotificationsHandlers(service NotificationsServiceInterface) *NotificationsHandlers {
	return &NotificationsHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *NotificationsHandlers) GetNotification(w http.ResponseWriter, r *http.Request, characterId notifications.CharacterId, notificationId notifications.NotificationId) {
	ctx := r.Context()
	
	notification, err := h.service.GetNotification(ctx, characterId, notificationId)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get notification")
		h.respondError(w, http.StatusInternalServerError, "failed to get notification")
		return
	}
	
	if notification == nil {
		h.respondError(w, http.StatusNotFound, "notification not found")
		return
	}
	
	h.respondJSON(w, http.StatusOK, notification)
}

func (h *NotificationsHandlers) ListNotifications(w http.ResponseWriter, r *http.Request, characterId notifications.CharacterId, params notifications.ListNotificationsParams) {
	ctx := r.Context()
	
	notificationsList, err := h.service.ListNotifications(ctx, characterId, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list notifications")
		h.respondError(w, http.StatusInternalServerError, "failed to list notifications")
		return
	}
	
	response := notifications.NotificationListResponse{
		Notifications: &notificationsList,
		Total:         new(int),
	}
	*response.Total = len(notificationsList)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *NotificationsHandlers) MarkNotificationAsRead(w http.ResponseWriter, r *http.Request, characterId notifications.CharacterId, notificationId notifications.NotificationId) {
	ctx := r.Context()
	
	if err := h.service.MarkNotificationAsRead(ctx, characterId, notificationId); err != nil {
		h.logger.WithError(err).Error("Failed to mark notification as read")
		h.respondError(w, http.StatusInternalServerError, "failed to mark notification as read")
		return
	}
	
	h.respondJSON(w, http.StatusOK, notifications.StatusResponse{Status: "success"})
}

func (h *NotificationsHandlers) MarkAllNotificationsAsRead(w http.ResponseWriter, r *http.Request, characterId notifications.CharacterId) {
	ctx := r.Context()
	
	if err := h.service.MarkAllNotificationsAsRead(ctx, characterId); err != nil {
		h.logger.WithError(err).Error("Failed to mark all notifications as read")
		h.respondError(w, http.StatusInternalServerError, "failed to mark all notifications as read")
		return
	}
	
	h.respondJSON(w, http.StatusOK, notifications.StatusResponse{Status: "success"})
}

func (h *NotificationsHandlers) DeleteNotification(w http.ResponseWriter, r *http.Request, characterId notifications.CharacterId, notificationId notifications.NotificationId) {
	ctx := r.Context()
	
	if err := h.service.DeleteNotification(ctx, characterId, notificationId); err != nil {
		h.logger.WithError(err).Error("Failed to delete notification")
		h.respondError(w, http.StatusInternalServerError, "failed to delete notification")
		return
	}
	
	w.WriteHeader(http.StatusNoContent)
}

func (h *NotificationsHandlers) GetNotificationPreferences(w http.ResponseWriter, r *http.Request, characterId notifications.CharacterId) {
	ctx := r.Context()
	
	prefs, err := h.service.GetNotificationPreferences(ctx, characterId)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get notification preferences")
		h.respondError(w, http.StatusInternalServerError, "failed to get notification preferences")
		return
	}
	
	h.respondJSON(w, http.StatusOK, prefs)
}

func (h *NotificationsHandlers) UpdateNotificationPreferences(w http.ResponseWriter, r *http.Request, characterId notifications.CharacterId) {
	ctx := r.Context()
	
	var req notifications.UpdateNotificationPreferencesRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	
	prefs, err := h.service.UpdateNotificationPreferences(ctx, characterId, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update notification preferences")
		h.respondError(w, http.StatusInternalServerError, "failed to update notification preferences")
		return
	}
	
	h.respondJSON(w, http.StatusOK, prefs)
}

func (h *NotificationsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *NotificationsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := notifications.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

