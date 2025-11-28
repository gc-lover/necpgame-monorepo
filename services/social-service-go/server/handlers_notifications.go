package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/necpgame/social-service-go/pkg/api/notifications"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type NotificationsServiceInterface interface {
	GetNotifications(ctx context.Context, params notifications.GetNotificationsParams) (*notifications.NotificationListResponse, error)
	CreateNotification(ctx context.Context, req *notifications.CreateNotificationRequest) (*notifications.Notification, error)
	GetNotification(ctx context.Context, notificationID openapi_types.UUID) (*notifications.Notification, error)
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

func (h *NotificationsHandlers) GetNotifications(w http.ResponseWriter, r *http.Request, params notifications.GetNotificationsParams) {
	ctx := r.Context()
	
	response, err := h.service.GetNotifications(ctx, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get notifications")
		h.respondError(w, http.StatusInternalServerError, "failed to get notifications")
		return
	}
	
	h.respondJSON(w, http.StatusOK, response)
}

func (h *NotificationsHandlers) CreateNotification(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	
	var req notifications.CreateNotificationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	
	notification, err := h.service.CreateNotification(ctx, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create notification")
		h.respondError(w, http.StatusInternalServerError, "failed to create notification")
		return
	}
	
	h.respondJSON(w, http.StatusCreated, notification)
}

func (h *NotificationsHandlers) GetNotification(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	ctx := r.Context()
	
	notification, err := h.service.GetNotification(ctx, id)
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

func (h *NotificationsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *NotificationsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := notifications.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}
