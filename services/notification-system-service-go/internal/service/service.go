package service

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"services/notification-system-service-go/internal/repository"
	"services/notification-system-service-go/pkg/models"
)

// Service handles business logic for notification system
type Service struct {
	repo   *repository.Repository
	logger *zap.Logger
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, logger *zap.Logger) *Service {
	return &Service{
		repo:   repo,
		logger: logger,
	}
}

// CreateNotification creates a new notification
func (s *Service) CreateNotification(ctx context.Context, req *models.CreateNotificationRequest) (*models.Notification, error) {
	if req.PlayerID == uuid.Nil {
		return nil, fmt.Errorf("player ID is required")
	}

	if req.Type == "" {
		return nil, fmt.Errorf("notification type is required")
	}

	if req.Title == "" {
		return nil, fmt.Errorf("notification title is required")
	}

	if req.Message == "" {
		return nil, fmt.Errorf("notification message is required")
	}

	// Validate notification type
	validTypes := map[string]bool{
		"system":     true,
		"achievement": true,
		"quest":      true,
		"social":     true,
		"combat":     true,
		"economy":    true,
		"event":      true,
	}

	if !validTypes[req.Type] {
		return nil, fmt.Errorf("invalid notification type: %s", req.Type)
	}

	// Validate priority
	validPriorities := map[string]bool{
		"low":    true,
		"normal": true,
		"high":   true,
		"urgent": true,
	}

	if req.Priority == "" {
		req.Priority = "normal"
	} else if !validPriorities[req.Priority] {
		return nil, fmt.Errorf("invalid notification priority: %s", req.Priority)
	}

	notification := &models.Notification{
		PlayerID:  req.PlayerID,
		Type:      req.Type,
		Title:     req.Title,
		Message:   req.Message,
		Data:      req.Data,
		ExpiresAt: req.ExpiresAt,
		Priority:  req.Priority,
	}

	err := s.repo.CreateNotification(ctx, notification)
	if err != nil {
		s.logger.Error("Failed to create notification", zap.Error(err), zap.String("type", req.Type))
		return nil, fmt.Errorf("failed to create notification: %w", err)
	}

	s.logger.Info("Notification created",
		zap.String("id", notification.ID.String()),
		zap.String("player_id", req.PlayerID.String()),
		zap.String("type", req.Type))

	return notification, nil
}

// GetPlayerNotifications retrieves paginated notifications for a player
func (s *Service) GetPlayerNotifications(ctx context.Context, playerID uuid.UUID, status, notificationType string, limit, offset int) (*models.NotificationListResponse, error) {
	if playerID == uuid.Nil {
		return nil, fmt.Errorf("player ID is required")
	}

	notifications, total, err := s.repo.GetPlayerNotifications(ctx, playerID, status, notificationType, limit, offset)
	if err != nil {
		s.logger.Error("Failed to get player notifications", zap.Error(err), zap.String("player_id", playerID.String()))
		return nil, fmt.Errorf("failed to get player notifications: %w", err)
	}

	unreadCount, err := s.repo.GetUnreadCount(ctx, playerID)
	if err != nil {
		s.logger.Error("Failed to get unread count", zap.Error(err), zap.String("player_id", playerID.String()))
		// Don't fail the request, just log the error
		unreadCount = 0
	}

	return &models.NotificationListResponse{
		Notifications: notifications,
		Total:         total,
		UnreadCount:   unreadCount,
		Offset:        offset,
		Limit:         limit,
	}, nil
}

// GetNotificationByID retrieves a specific notification
func (s *Service) GetNotificationByID(ctx context.Context, notificationID, playerID uuid.UUID) (*models.Notification, error) {
	if notificationID == uuid.Nil {
		return nil, fmt.Errorf("notification ID is required")
	}

	if playerID == uuid.Nil {
		return nil, fmt.Errorf("player ID is required")
	}

	notification, err := s.repo.GetNotificationByID(ctx, notificationID, playerID)
	if err != nil {
		s.logger.Error("Failed to get notification", zap.Error(err),
			zap.String("notification_id", notificationID.String()),
			zap.String("player_id", playerID.String()))
		return nil, err
	}

	return notification, nil
}

// MarkAsRead marks a notification as read
func (s *Service) MarkAsRead(ctx context.Context, notificationID, playerID uuid.UUID) error {
	if notificationID == uuid.Nil {
		return fmt.Errorf("notification ID is required")
	}

	if playerID == uuid.Nil {
		return fmt.Errorf("player ID is required")
	}

	err := s.repo.MarkAsRead(ctx, notificationID, playerID)
	if err != nil {
		s.logger.Error("Failed to mark notification as read", zap.Error(err),
			zap.String("notification_id", notificationID.String()),
			zap.String("player_id", playerID.String()))
		return err
	}

	s.logger.Info("Notification marked as read",
		zap.String("notification_id", notificationID.String()),
		zap.String("player_id", playerID.String()))

	return nil
}

// MarkBulkAsRead marks multiple notifications as read
func (s *Service) MarkBulkAsRead(ctx context.Context, req *models.BulkReadRequest, playerID uuid.UUID) error {
	if len(req.NotificationIDs) == 0 {
		return fmt.Errorf("notification IDs are required")
	}

	if playerID == uuid.Nil {
		return fmt.Errorf("player ID is required")
	}

	err := s.repo.MarkBulkAsRead(ctx, req.NotificationIDs, playerID)
	if err != nil {
		s.logger.Error("Failed to mark bulk notifications as read", zap.Error(err),
			zap.String("player_id", playerID.String()),
			zap.Int("count", len(req.NotificationIDs)))
		return err
	}

	s.logger.Info("Bulk notifications marked as read",
		zap.String("player_id", playerID.String()),
		zap.Int("count", len(req.NotificationIDs)))

	return nil
}

// DeleteNotification deletes a notification
func (s *Service) DeleteNotification(ctx context.Context, notificationID, playerID uuid.UUID) error {
	if notificationID == uuid.Nil {
		return fmt.Errorf("notification ID is required")
	}

	if playerID == uuid.Nil {
		return fmt.Errorf("player ID is required")
	}

	err := s.repo.DeleteNotification(ctx, notificationID, playerID)
	if err != nil {
		s.logger.Error("Failed to delete notification", zap.Error(err),
			zap.String("notification_id", notificationID.String()),
			zap.String("player_id", playerID.String()))
		return err
	}

	s.logger.Info("Notification deleted",
		zap.String("notification_id", notificationID.String()),
		zap.String("player_id", playerID.String()))

	return nil
}

// DeleteBulkNotifications deletes multiple notifications
func (s *Service) DeleteBulkNotifications(ctx context.Context, req *models.BulkDeleteRequest, playerID uuid.UUID) error {
	if len(req.NotificationIDs) == 0 {
		return fmt.Errorf("notification IDs are required")
	}

	if playerID == uuid.Nil {
		return fmt.Errorf("player ID is required")
	}

	err := s.repo.DeleteBulkNotifications(ctx, req.NotificationIDs, playerID)
	if err != nil {
		s.logger.Error("Failed to delete bulk notifications", zap.Error(err),
			zap.String("player_id", playerID.String()),
			zap.Int("count", len(req.NotificationIDs)))
		return err
	}

	s.logger.Info("Bulk notifications deleted",
		zap.String("player_id", playerID.String()),
		zap.Int("count", len(req.NotificationIDs)))

	return nil
}

// GetUnreadCount gets the count of unread notifications for a player
func (s *Service) GetUnreadCount(ctx context.Context, playerID uuid.UUID) (*models.UnreadCountResponse, error) {
	if playerID == uuid.Nil {
		return nil, fmt.Errorf("player ID is required")
	}

	count, err := s.repo.GetUnreadCount(ctx, playerID)
	if err != nil {
		s.logger.Error("Failed to get unread count", zap.Error(err), zap.String("player_id", playerID.String()))
		return nil, fmt.Errorf("failed to get unread count: %w", err)
	}

	return &models.UnreadCountResponse{Count: count}, nil
}
