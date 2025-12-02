// Issue: #141888033
package server

import (
	"context"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type SocialService struct {
	notificationRepo         NotificationRepositoryInterface
	notificationPrefsRepo    NotificationPreferencesRepositoryInterface
	friendRepo               FriendRepositoryInterface
	chatRepo                 ChatRepositoryInterface
	mailRepo                 MailRepositoryInterface
	guildRepo                GuildRepositoryInterface
	orderRepo                OrderRepositoryInterface
	moderationRepo            ModerationRepositoryInterface
	moderationService         ModerationServiceInterface
	cache                     *redis.Client
	logger                    *logrus.Logger
	notificationSubscriber    *NotificationSubscriber
	eventBus                  EventBus
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
	notificationPrefsRepo := NewNotificationPreferencesRepository(dbPool)
	friendRepo := NewFriendRepository(dbPool)
	chatRepo := NewChatRepository(dbPool)
	mailRepo := NewMailRepository(dbPool)
	guildRepo := NewGuildRepository(dbPool)
	orderRepo := NewOrderRepository(dbPool)
	moderationRepo := NewModerationRepository(dbPool)
	moderationService := NewModerationService(moderationRepo, redisClient)
	notificationSubscriber := NewNotificationSubscriber(notificationRepo, redisClient)
	notificationSubscriber.SetPreferencesRepository(notificationPrefsRepo)
	eventBus := NewRedisEventBus(redisClient)

	service := &SocialService{
		notificationRepo:      notificationRepo,
		notificationPrefsRepo: notificationPrefsRepo,
		friendRepo:            friendRepo,
		chatRepo:              chatRepo,
		mailRepo:              mailRepo,
		guildRepo:             guildRepo,
		orderRepo:             orderRepo,
		moderationRepo:        moderationRepo,
		moderationService:     moderationService,
		cache:                 redisClient,
		logger:                GetLogger(),
		notificationSubscriber: notificationSubscriber,
		eventBus:              eventBus,
	}

	mailRewardSubscriber := NewMailRewardSubscriber(mailRepo, notificationRepo, redisClient)
	if err := mailRewardSubscriber.Start(); err != nil {
		GetLogger().WithError(err).Warn("Failed to start mail reward subscriber")
	} else {
		GetLogger().Info("Mail reward subscriber started")
	}

	guildProgressionSubscriber := NewGuildProgressionSubscriber(guildRepo, eventBus, redisClient)
	if err := guildProgressionSubscriber.Start(); err != nil {
		GetLogger().WithError(err).Warn("Failed to start guild progression subscriber")
	} else {
		GetLogger().Info("Guild progression subscriber started")
	}

	return service, nil
}

func (s *SocialService) GetNotificationSubscriber() *NotificationSubscriber {
	return s.notificationSubscriber
}

