// Package server Notification handlers implementation for ogen-generated API
// Issue: #140874394
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/notification-service-go/pkg/api"
)

// NotificationHandler implements the Handler interface from ogen
type NotificationHandler struct {
	service *NotificationService
	logger  *zap.Logger
}

// NewNotificationHandler creates a new notification handler
func NewNotificationHandler(service *NotificationService, logger *zap.Logger) *NotificationHandler {
	return &NotificationHandler{
		service: service,
		logger:  logger,
	}
}

// CreateNotification implements createNotification operation
func (h *NotificationHandler) CreateNotification(ctx context.Context, req *api.CreateNotificationRequest) (api.CreateNotificationRes, error) {
	// Get user ID from context
	userIDVal := ctx.Value("user_id")
	if userIDVal == nil {
		return &api.CreateNotificationUnauthorized{}, nil
	}

	userID, ok := userIDVal.(string)
	if !ok {
		return &api.CreateNotificationBadRequest{}, nil
	}

	h.logger.Info("Creating notification via API",
		zap.String("user_id", userID),
		zap.String("type", string(req.Type)),
		zap.String("title", req.Title))

	// Convert data - CreateNotificationRequestData is empty struct, so we use empty map
	data := make(map[string]interface{})

	// Convert priority
	priority := "normal"
	if req.Priority.IsSet() {
		priority = string(req.Priority.Value)
	}

	// Convert expires_at
	var expiresAt *time.Time
	if req.ExpiresAt.IsSet() {
		expiresAt = &req.ExpiresAt.Value
	}

	// Create notification using service
	notification, err := h.service.CreateNotification(ctx, userID, string(req.Type), req.Title, req.Message, data, priority, expiresAt)
	if err != nil {
		h.logger.Error("Failed to create notification", zap.Error(err))
		return &api.CreateNotificationInternalServerError{}, nil
	}

	// Convert to response format - NotificationData is empty struct, so we don't set Data
	response := &api.Notification{
		ID:        uuid.MustParse(notification.ID),
		UserID:    uuid.MustParse(userID),
		Type:      api.NotificationType(notification.Type),
		Title:     notification.Title,
		Priority:  api.NotificationPriority(notification.Priority),
		Status:    api.NotificationStatus(notification.Status),
		CreatedAt: notification.CreatedAt,
		UpdatedAt: notification.UpdatedAt,
	}

	if notification.ExpiresAt != nil {
		response.ExpiresAt.SetTo(*notification.ExpiresAt)
	}

	return response, nil
}

// GetNotifications implements getNotifications operation
func (h *NotificationHandler) GetNotifications(ctx context.Context, params api.GetNotificationsParams) (api.GetNotificationsRes, error) {
	// Get user ID from context
	userIDVal := ctx.Value("user_id")
	if userIDVal == nil {
		return &api.GetNotificationsUnauthorized{}, nil
	}

	userID, ok := userIDVal.(string)
	if !ok {
		return &api.GetNotificationsUnauthorized{}, nil
	}

	// Parse pagination parameters
	limit := 20 // default
	if params.Limit.IsSet() && params.Limit.Value > 0 && params.Limit.Value <= 100 {
		limit = params.Limit.Value
	}

	offset := 0 // default
	if params.Offset.IsSet() && params.Offset.Value >= 0 {
		offset = params.Offset.Value
	}

	statusFilter := "unread" // default
	if params.Status.IsSet() {
		statusFilter = string(params.Status.Value)
	}

	// Get notifications
	notifications, total, err := h.service.GetUserNotifications(ctx, userID, limit, offset, statusFilter)
	if err != nil {
		h.logger.Error("Failed to get notifications", zap.Error(err))
		return &api.GetNotificationsInternalServerError{}, nil
	}

	// Convert to response format
	responseNotifications := make([]api.Notification, len(notifications))
	for i, n := range notifications {
		responseNotifications[i] = api.Notification{
			ID:        uuid.MustParse(n.ID),
			UserID:    uuid.MustParse(userID),
			Type:      api.NotificationType(n.Type),
			Title:     n.Title,
			Priority:  api.NotificationPriority(n.Priority),
			Status:    api.NotificationStatus(n.Status),
			CreatedAt: n.CreatedAt,
			UpdatedAt: n.UpdatedAt,
		}
		if n.ExpiresAt != nil {
			responseNotifications[i].ExpiresAt.SetTo(*n.ExpiresAt)
		}
	}

	// Get unread count
	unreadCount, err := h.service.GetUnreadCount(ctx, userID)
	if err != nil {
		h.logger.Warn("Failed to get unread count", zap.Error(err))
		unreadCount = 0
	}

	return &api.GetNotificationsOK{
		Notifications: responseNotifications,
		Total:         api.OptInt{Value: total, Set: true},
		UnreadCount:   api.OptInt{Value: unreadCount, Set: true},
	}, nil
}

// GetNotification implements getNotification operation
func (h *NotificationHandler) GetNotification(ctx context.Context, params api.GetNotificationParams) (api.GetNotificationRes, error) {
	// Get user ID from context
	userIDVal := ctx.Value("user_id")
	if userIDVal == nil {
		return &api.GetNotificationUnauthorized{}, nil
	}

	userID, ok := userIDVal.(string)
	if !ok {
		return &api.GetNotificationUnauthorized{}, nil
	}

	// Get notification by ID
	notification, err := h.service.GetNotificationByID(ctx, params.NotificationID.String(), userID)
	if err != nil {
		h.logger.Error("Failed to get notification",
			zap.String("notification_id", params.NotificationID.String()),
			zap.String("user_id", userID),
			zap.Error(err))
		return &api.GetNotificationNotFound{}, nil
	}

	// Convert to response format
	response := &api.Notification{
		ID:        uuid.MustParse(notification.ID),
		UserID:    uuid.MustParse(userID),
		Type:      api.NotificationType(notification.Type),
		Title:     notification.Title,
		Priority:  api.NotificationPriority(notification.Priority),
		Status:    api.NotificationStatus(notification.Status),
		CreatedAt: notification.CreatedAt,
		UpdatedAt: notification.UpdatedAt,
	}

	if notification.ExpiresAt != nil {
		response.ExpiresAt.SetTo(*notification.ExpiresAt)
	}

	return response, nil
}

// UpdateNotification implements updateNotification operation
func (h *NotificationHandler) UpdateNotification(ctx context.Context, req *api.UpdateNotificationRequest, params api.UpdateNotificationParams) (api.UpdateNotificationRes, error) {
	// Get user ID from context
	userIDVal := ctx.Value("user_id")
	if userIDVal == nil {
		return &api.UpdateNotificationUnauthorized{}, nil
	}

	userID, ok := userIDVal.(string)
	if !ok {
		return &api.UpdateNotificationUnauthorized{}, nil
	}

	// Convert request updates to map
	updates := make(map[string]interface{})
	if req.Title.IsSet() {
		updates["title"] = req.Title.Value
	}
	if req.Data != nil {
		// UpdateNotificationRequestData is empty, skip
	}
	if req.Priority.IsSet() {
		updates["priority"] = string(req.Priority.Value)
	}
	if req.Status.IsSet() {
		updates["status"] = string(req.Status.Value)
	}
	if req.ExpiresAt.IsSet() {
		updates["expires_at"] = req.ExpiresAt.Value
	}

	// Update notification
	notification, err := h.service.UpdateNotification(ctx, params.NotificationID.String(), userID, updates)
	if err != nil {
		h.logger.Error("Failed to update notification", zap.Error(err))
		return &api.UpdateNotificationNotFound{}, nil
	}

	// Convert to response format
	response := &api.Notification{
		ID:        uuid.MustParse(notification.ID),
		UserID:    uuid.MustParse(userID),
		Type:      api.NotificationType(notification.Type),
		Title:     notification.Title,
		Priority:  api.NotificationPriority(notification.Priority),
		Status:    api.NotificationStatus(notification.Status),
		CreatedAt: notification.CreatedAt,
		UpdatedAt: notification.UpdatedAt,
	}

	if notification.ExpiresAt != nil {
		response.ExpiresAt.SetTo(*notification.ExpiresAt)
	}

	return response, nil
}

// MarkAsRead implements markAsRead operation
func (h *NotificationHandler) MarkAsRead(ctx context.Context, params api.MarkAsReadParams) (api.MarkAsReadRes, error) {
	// Get user ID from context
	userIDVal := ctx.Value("user_id")
	if userIDVal == nil {
		return &api.MarkAsReadUnauthorized{}, nil
	}

	userID, ok := userIDVal.(string)
	if !ok {
		return &api.MarkAsReadUnauthorized{}, nil
	}

	// Mark notification as read
	if err := h.service.MarkAsRead(ctx, params.NotificationID.String(), userID); err != nil {
		h.logger.Error("Failed to mark notification as read",
			zap.String("notification_id", params.NotificationID.String()),
			zap.String("user_id", userID),
			zap.Error(err))
		return &api.MarkAsReadUnauthorized{}, nil
	}

	return &api.MarkAsReadOK{}, nil
}

// MarkBulkAsRead implements markBulkAsRead operation
func (h *NotificationHandler) MarkBulkAsRead(ctx context.Context, req *api.MarkBulkAsReadReq) (api.MarkBulkAsReadRes, error) {
	// Get user ID from context
	userIDVal := ctx.Value("user_id")
	if userIDVal == nil {
		return &api.MarkBulkAsReadUnauthorized{}, nil
	}

	userID, ok := userIDVal.(string)
	if !ok {
		return &api.MarkBulkAsReadUnauthorized{}, nil
	}

	// Validate request
	if len(req.NotificationIds) == 0 || len(req.NotificationIds) > 100 {
		return &api.MarkBulkAsReadBadRequest{}, nil
	}

	// Convert string IDs to string slice
	notificationIDs := make([]string, len(req.NotificationIds))
	for i, id := range req.NotificationIds {
		notificationIDs[i] = id.String()
	}

	// Mark bulk as read
	processedCount, err := h.service.MarkBulkAsRead(ctx, notificationIDs, userID)
	if err != nil {
		h.logger.Error("Failed to mark bulk notifications as read",
			zap.Strings("notification_ids", notificationIDs),
			zap.String("user_id", userID),
			zap.Error(err))
		return &api.MarkBulkAsReadUnauthorized{}, nil
	}

	return &api.MarkBulkAsReadOK{
		Success:        api.OptBool{Value: true, Set: true},
		ProcessedCount: api.OptInt{Value: processedCount, Set: true},
	}, nil
}

// NotificationWebSocket implements notificationWebSocket operation
func (h *NotificationHandler) NotificationWebSocket(_ context.Context, _ api.NotificationWebSocketParams) (api.NotificationWebSocketRes, error) {
	// WebSocket handling is done in the server package
	// This is just a placeholder for the ogen interface
	return &api.NotificationWebSocketSwitchingProtocols{}, nil
}
