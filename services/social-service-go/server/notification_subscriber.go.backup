package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type NotificationSubscriber struct {
	notificationRepo      NotificationRepositoryInterface
	preferencesRepo       NotificationPreferencesRepositoryInterface
	cache                 *redis.Client
	logger                *logrus.Logger
	pubsub                *redis.PubSub
	ctx                   context.Context
	cancel                context.CancelFunc
}

func NewNotificationSubscriber(notificationRepo NotificationRepositoryInterface, cache *redis.Client) *NotificationSubscriber {
	ctx, cancel := context.WithCancel(context.Background())
	return &NotificationSubscriber{
		notificationRepo: notificationRepo,
		cache:            cache,
		logger:           GetLogger(),
		ctx:              ctx,
		cancel:           cancel,
	}
}

func (ns *NotificationSubscriber) Start() error {
	ns.logger.Info("Starting notification subscriber")

	channels := []string{
		"events:friend:*",
		"events:guild:*",
		"events:trade:*",
		"events:achievement:*",
		"events:quest:*",
		"events:combat:*",
	}

	ns.pubsub = ns.cache.PSubscribe(ns.ctx, channels...)

	go ns.listen()
	return nil
}

func (ns *NotificationSubscriber) Stop() error {
	ns.logger.Info("Stopping notification subscriber")
	ns.cancel()
	if ns.pubsub != nil {
		return ns.pubsub.Close()
	}
	return nil
}

func (ns *NotificationSubscriber) listen() {
	ch := ns.pubsub.Channel()
	
	for {
		select {
		case <-ns.ctx.Done():
			ns.logger.Info("Notification subscriber context cancelled")
			return
		case msg := <-ch:
			if msg == nil {
				continue
			}
			
			ns.handleEvent(msg.Channel, []byte(msg.Payload))
		}
	}
}

func (ns *NotificationSubscriber) handleEvent(channel string, data []byte) {
	var eventData map[string]interface{}
	if err := json.Unmarshal(data, &eventData); err != nil {
		ns.logger.WithError(err).Error("Failed to unmarshal event data")
		return
	}

	ns.logger.WithFields(logrus.Fields{
		"channel": channel,
	}).Debug("Received event for notification")

	notification := ns.createNotificationFromEvent(channel, eventData)
	if notification == nil {
		return
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	_, err := ns.notificationRepo.Create(ctx, notification)
	if err != nil {
		ns.logger.WithError(err).WithField("channel", channel).Error("Failed to create notification from event")
		return
	}

	ns.logger.WithFields(logrus.Fields{
		"channel":      channel,
		"account_id":  notification.AccountID,
		"type":        notification.Type,
	}).Info("Created notification from event")

	ns.publishWebSocketNotification(ctx, notification)
}

func (ns *NotificationSubscriber) createNotificationFromEvent(channel string, eventData map[string]interface{}) *models.Notification {
	var accountIDStr string
	var ok bool

	if accountIDStr, ok = eventData["account_id"].(string); !ok {
		if characterIDStr, ok := eventData["character_id"].(string); ok {
			accountIDStr = characterIDStr
		} else {
			ns.logger.WithField("channel", channel).Warn("Event missing account_id or character_id")
			return nil
		}
	}

	accountID, err := uuid.Parse(accountIDStr)
	if err != nil {
		ns.logger.WithError(err).WithField("account_id", accountIDStr).Error("Invalid account_id in event")
		return nil
	}

	notificationType := ns.getNotificationTypeFromChannel(channel)
	
	if !ns.shouldCreateNotification(accountID, notificationType) {
		ns.logger.WithFields(logrus.Fields{
			"account_id": accountID,
			"type":       notificationType,
		}).Debug("Notification skipped due to user preferences")
		return nil
	}

	priority := ns.getPriorityFromEvent(eventData)
	title, content := ns.getTitleAndContentFromEvent(channel, eventData)

	channels := ns.getDeliveryChannels(accountID, priority)

	notification := &models.Notification{
		ID:        uuid.New(),
		AccountID: accountID,
		Type:      notificationType,
		Priority:  priority,
		Title:     title,
		Content:   content,
		Data:      eventData,
		Status:    models.NotificationStatusUnread,
		Channels:  channels,
		CreatedAt: time.Now(),
	}

	return notification
}

func (ns *NotificationSubscriber) shouldCreateNotification(accountID uuid.UUID, notificationType models.NotificationType) bool {
	prefsRepo := ns.getPreferencesRepository()
	if prefsRepo == nil {
		return true
	}

	ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
	defer cancel()

	prefs, err := prefsRepo.GetByAccountID(ctx, accountID)
	if err != nil || prefs == nil {
		return true
	}

	switch notificationType {
	case models.NotificationTypeQuest:
		return prefs.QuestEnabled
	case models.NotificationTypeMessage:
		return prefs.MessageEnabled
	case models.NotificationTypeAchievement:
		return prefs.AchievementEnabled
	case models.NotificationTypeSystem:
		return prefs.SystemEnabled
	case models.NotificationTypeFriend:
		return prefs.FriendEnabled
	case models.NotificationTypeGuild:
		return prefs.GuildEnabled
	case models.NotificationTypeTrade:
		return prefs.TradeEnabled
	case models.NotificationTypeCombat:
		return prefs.CombatEnabled
	default:
		return true
	}
}

func (ns *NotificationSubscriber) getDeliveryChannels(accountID uuid.UUID, priority models.NotificationPriority) []models.DeliveryChannel {
	prefsRepo := ns.getPreferencesRepository()
	if prefsRepo == nil {
		channels := []models.DeliveryChannel{models.DeliveryChannelInGame}
		if priority == models.NotificationPriorityHigh || priority == models.NotificationPriorityCritical {
			channels = append(channels, models.DeliveryChannelWebSocket)
		}
		return channels
	}

	ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
	defer cancel()

	prefs, err := prefsRepo.GetByAccountID(ctx, accountID)
	if err != nil || prefs == nil || len(prefs.PreferredChannels) == 0 {
		channels := []models.DeliveryChannel{models.DeliveryChannelInGame}
		if priority == models.NotificationPriorityHigh || priority == models.NotificationPriorityCritical {
			channels = append(channels, models.DeliveryChannelWebSocket)
		}
		return channels
	}

	channels := prefs.PreferredChannels
	if priority == models.NotificationPriorityHigh || priority == models.NotificationPriorityCritical {
		hasWebSocket := false
		for _, ch := range channels {
			if ch == models.DeliveryChannelWebSocket {
				hasWebSocket = true
				break
			}
		}
		if !hasWebSocket {
			channels = append(channels, models.DeliveryChannelWebSocket)
		}
	}

	return channels
}

func (ns *NotificationSubscriber) SetPreferencesRepository(repo NotificationPreferencesRepositoryInterface) {
	ns.preferencesRepo = repo
}

func (ns *NotificationSubscriber) getPreferencesRepository() NotificationPreferencesRepositoryInterface {
	return ns.preferencesRepo
}

func (ns *NotificationSubscriber) getNotificationTypeFromChannel(channel string) models.NotificationType {
	if contains(channel, "friend") {
		return models.NotificationTypeFriend
	}
	if contains(channel, "guild") {
		return models.NotificationTypeGuild
	}
	if contains(channel, "trade") {
		return models.NotificationTypeTrade
	}
	if contains(channel, "achievement") {
		return models.NotificationTypeAchievement
	}
	if contains(channel, "quest") {
		return models.NotificationTypeQuest
	}
	if contains(channel, "combat") {
		return models.NotificationTypeCombat
	}
	return models.NotificationTypeSystem
}

func (ns *NotificationSubscriber) getPriorityFromEvent(eventData map[string]interface{}) models.NotificationPriority {
	if priorityStr, ok := eventData["priority"].(string); ok {
		switch priorityStr {
		case "critical":
			return models.NotificationPriorityCritical
		case "high":
			return models.NotificationPriorityHigh
		case "medium":
			return models.NotificationPriorityMedium
		case "low":
			return models.NotificationPriorityLow
		}
	}
	return models.NotificationPriorityMedium
}

func (ns *NotificationSubscriber) getTitleAndContentFromEvent(channel string, eventData map[string]interface{}) (string, string) {
	if title, ok := eventData["title"].(string); ok {
		content := ""
		if contentStr, ok := eventData["content"].(string); ok {
			content = contentStr
		} else if message, ok := eventData["message"].(string); ok {
			content = message
		}
		return title, content
	}

	title := ns.getDefaultTitleFromChannel(channel)
	content := ""
	if message, ok := eventData["message"].(string); ok {
		content = message
	} else if description, ok := eventData["description"].(string); ok {
		content = description
	}

	return title, content
}

func (ns *NotificationSubscriber) getDefaultTitleFromChannel(channel string) string {
	if contains(channel, "friend") {
		return "Friend Update"
	}
	if contains(channel, "guild") {
		return "Guild Update"
	}
	if contains(channel, "trade") {
		return "Trade Update"
	}
	if contains(channel, "achievement") {
		return "Achievement Unlocked"
	}
	if contains(channel, "quest") {
		return "Quest Update"
	}
	if contains(channel, "combat") {
		return "Combat Update"
	}
	return "System Notification"
}

func (ns *NotificationSubscriber) publishWebSocketNotification(ctx context.Context, notification *models.Notification) {
	for _, channel := range notification.Channels {
		if channel == models.DeliveryChannelWebSocket {
			payload := map[string]interface{}{
				"notification_id": notification.ID.String(),
				"account_id":      notification.AccountID.String(),
				"type":            string(notification.Type),
				"priority":        string(notification.Priority),
				"title":           notification.Title,
				"content":         notification.Content,
				"data":            notification.Data,
				"timestamp":       notification.CreatedAt.Format(time.RFC3339),
			}

			eventData, err := json.Marshal(payload)
			if err != nil {
				ns.logger.WithError(err).Error("Failed to marshal WebSocket notification")
				continue
			}

			wsChannel := "events:notification:websocket:" + notification.AccountID.String()
			err = ns.cache.Publish(ctx, wsChannel, eventData).Err()
			if err != nil {
				ns.logger.WithError(err).Error("Failed to publish WebSocket notification")
			} else {
				ns.logger.WithField("account_id", notification.AccountID).Debug("Published WebSocket notification")
			}
		}
	}
}

func contains(s, substr string) bool {
	return len(s) >= len(substr) && (s == substr || (len(s) > len(substr) && containsMiddle(s, substr)))
}

func containsMiddle(s, substr string) bool {
	for i := 0; i <= len(s)-len(substr); i++ {
		if s[i:i+len(substr)] == substr {
			return true
		}
	}
	return false
}

