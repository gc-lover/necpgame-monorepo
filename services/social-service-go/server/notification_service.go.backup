// Issue: #141888033
package server

import (
	"context"
	"encoding/json"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
)

func (s *SocialService) CreateNotification(ctx context.Context, req *models.CreateNotificationRequest) (*models.Notification, error) {
	notification := &models.Notification{
		ID:        uuid.New(),
		AccountID: req.AccountID,
		Type:      req.Type,
		Priority:  req.Priority,
		Title:     req.Title,
		Content:   req.Content,
		Data:      req.Data,
		Status:    models.NotificationStatusUnread,
		Channels:  req.Channels,
		CreatedAt: time.Now(),
		ExpiresAt: req.ExpiresAt,
	}

	if len(notification.Channels) == 0 {
		notification.Channels = []models.DeliveryChannel{models.DeliveryChannelInGame}
	}

	notification, err := s.notificationRepo.Create(ctx, notification)
	if err != nil {
		return nil, err
	}

	cacheKey := "notifications:account:" + req.AccountID.String()
	s.cache.Del(ctx, cacheKey)

	return notification, nil
}

func (s *SocialService) GetNotifications(ctx context.Context, accountID uuid.UUID, limit, offset int) (*models.NotificationListResponse, error) {
	cacheKey := "notifications:account:" + accountID.String() + ":limit:" + strconv.Itoa(limit) + ":offset:" + strconv.Itoa(offset)
	
	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.NotificationListResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		} else if err != nil {
			s.logger.WithError(err).Error("Failed to unmarshal cached notifications JSON")
			// Continue without cache if unmarshal fails
		}
	}

	notifications, err := s.notificationRepo.GetByAccountID(ctx, accountID, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.notificationRepo.CountByAccountID(ctx, accountID)
	if err != nil {
		return nil, err
	}

	unread, err := s.notificationRepo.CountUnreadByAccountID(ctx, accountID)
	if err != nil {
		return nil, err
	}

	response := &models.NotificationListResponse{
		Notifications: notifications,
		Total:         total,
		Unread:        unread,
	}

	responseJSON, _ := json.Marshal(response)
	s.cache.Set(ctx, cacheKey, responseJSON, 1*time.Minute)

	return response, nil
}

func (s *SocialService) GetNotification(ctx context.Context, notificationID uuid.UUID) (*models.Notification, error) {
	return s.notificationRepo.GetByID(ctx, notificationID)
}

func (s *SocialService) UpdateNotificationStatus(ctx context.Context, notificationID uuid.UUID, status models.NotificationStatus) (*models.Notification, error) {
	notification, err := s.notificationRepo.UpdateStatus(ctx, notificationID, status)
	if err != nil {
		return nil, err
	}

	if notification != nil {
		cacheKey := "notifications:account:" + notification.AccountID.String()
		s.cache.Del(ctx, cacheKey)
	}

	return notification, nil
}

func (s *SocialService) GetNotificationPreferences(ctx context.Context, accountID uuid.UUID) (*models.NotificationPreferences, error) {
	return s.notificationPrefsRepo.GetByAccountID(ctx, accountID)
}

func (s *SocialService) UpdateNotificationPreferences(ctx context.Context, prefs *models.NotificationPreferences) error {
	return s.notificationPrefsRepo.Update(ctx, prefs)
}

