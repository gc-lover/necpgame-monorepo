package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type SocialService struct {
	notificationRepo *NotificationRepository
	friendRepo       *FriendRepository
	cache            *redis.Client
	logger           *logrus.Logger
}

func NewSocialService(dbURL, redisURL string) (*SocialService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	notificationRepo := NewNotificationRepository(dbPool)
	friendRepo := NewFriendRepository(dbPool)

	return &SocialService{
		notificationRepo: notificationRepo,
		friendRepo:       friendRepo,
		cache:            redisClient,
		logger:           GetLogger(),
	}, nil
}

func (s *SocialService) CreateNotification(ctx context.Context, req *models.CreateNotificationRequest) (*models.Notification, error) {
	notification, err := s.notificationRepo.Create(ctx, req)
	if err != nil {
		return nil, err
	}

	s.invalidateNotificationCache(ctx, req.AccountID)

	return notification, nil
}

func (s *SocialService) GetNotifications(ctx context.Context, accountID uuid.UUID, limit, offset int) (*models.NotificationListResponse, error) {
	cacheKey := "notifications:account:" + accountID.String() + ":limit:" + string(rune(limit)) + ":offset:" + string(rune(offset))

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.NotificationListResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
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

	SetNotificationsCount(accountID.String(), "total", float64(total))

	return response, nil
}

func (s *SocialService) GetNotification(ctx context.Context, notificationID uuid.UUID) (*models.Notification, error) {
	cacheKey := "notification:" + notificationID.String()

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var notification models.Notification
		if err := json.Unmarshal([]byte(cached), &notification); err == nil {
			return &notification, nil
		}
	}

	notification, err := s.notificationRepo.GetByID(ctx, notificationID)
	if err != nil {
		return nil, err
	}

	if notification != nil {
		notificationJSON, _ := json.Marshal(notification)
		s.cache.Set(ctx, cacheKey, notificationJSON, 5*time.Minute)
	}

	return notification, nil
}

func (s *SocialService) UpdateNotificationStatus(ctx context.Context, notificationID uuid.UUID, status models.NotificationStatus) (*models.Notification, error) {
	notification, err := s.notificationRepo.UpdateStatus(ctx, notificationID, status)
	if err != nil {
		return nil, err
	}

	if notification != nil {
		s.invalidateNotificationCache(ctx, notification.AccountID)
	}

	return notification, nil
}

func (s *SocialService) DeleteNotification(ctx context.Context, notificationID uuid.UUID) error {
	notification, err := s.notificationRepo.GetByID(ctx, notificationID)
	if err != nil {
		return err
	}

	err = s.notificationRepo.Delete(ctx, notificationID)
	if err != nil {
		return err
	}

	if notification != nil {
		s.invalidateNotificationCache(ctx, notification.AccountID)
	}

	return nil
}

func (s *SocialService) SendFriendRequest(ctx context.Context, fromCharacterID, toCharacterID uuid.UUID) (*models.Friendship, error) {
	friendship, err := s.friendRepo.CreateRequest(ctx, fromCharacterID, toCharacterID)
	if err != nil {
		return nil, err
	}

	s.invalidateFriendCache(ctx, fromCharacterID)
	s.invalidateFriendCache(ctx, toCharacterID)

	return friendship, nil
}

func (s *SocialService) AcceptFriendRequest(ctx context.Context, friendshipID uuid.UUID) (*models.Friendship, error) {
	friendship, err := s.friendRepo.AcceptRequest(ctx, friendshipID)
	if err != nil {
		return nil, err
	}

	if friendship != nil {
		s.invalidateFriendCache(ctx, friendship.CharacterAID)
		s.invalidateFriendCache(ctx, friendship.CharacterBID)
	}

	return friendship, nil
}

func (s *SocialService) RejectFriendRequest(ctx context.Context, friendshipID uuid.UUID) error {
	friendship, err := s.friendRepo.GetByID(ctx, friendshipID)
	if err != nil {
		return err
	}

	err = s.friendRepo.Delete(ctx, friendshipID)
	if err != nil {
		return err
	}

	if friendship != nil {
		s.invalidateFriendCache(ctx, friendship.CharacterAID)
		s.invalidateFriendCache(ctx, friendship.CharacterBID)
	}

	return nil
}

func (s *SocialService) GetFriends(ctx context.Context, characterID uuid.UUID) ([]models.Friendship, error) {
	cacheKey := "friends:character:" + characterID.String()

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var friendships []models.Friendship
		if err := json.Unmarshal([]byte(cached), &friendships); err == nil {
			return friendships, nil
		}
	}

	friendships, err := s.friendRepo.GetByCharacterID(ctx, characterID)
	if err != nil {
		return nil, err
	}

	friendshipsJSON, _ := json.Marshal(friendships)
	s.cache.Set(ctx, cacheKey, friendshipsJSON, 5*time.Minute)

	return friendships, nil
}

func (s *SocialService) RemoveFriend(ctx context.Context, friendshipID uuid.UUID) error {
	friendship, err := s.friendRepo.GetByID(ctx, friendshipID)
	if err != nil {
		return err
	}

	err = s.friendRepo.Delete(ctx, friendshipID)
	if err != nil {
		return err
	}

	if friendship != nil {
		s.invalidateFriendCache(ctx, friendship.CharacterAID)
		s.invalidateFriendCache(ctx, friendship.CharacterBID)
	}

	return nil
}

func (s *SocialService) BlockFriend(ctx context.Context, friendshipID uuid.UUID) (*models.Friendship, error) {
	friendship, err := s.friendRepo.Block(ctx, friendshipID)
	if err != nil {
		return nil, err
	}

	if friendship != nil {
		s.invalidateFriendCache(ctx, friendship.CharacterAID)
		s.invalidateFriendCache(ctx, friendship.CharacterBID)
	}

	return friendship, nil
}

func (s *SocialService) invalidateNotificationCache(ctx context.Context, accountID uuid.UUID) {
	pattern := "notifications:account:" + accountID.String() + ":*"
	keys, _ := s.cache.Keys(ctx, pattern).Result()
	if len(keys) > 0 {
		s.cache.Del(ctx, keys...)
	}
}

func (s *SocialService) invalidateFriendCache(ctx context.Context, characterID uuid.UUID) {
	cacheKey := "friends:character:" + characterID.String()
	s.cache.Del(ctx, cacheKey)
}

