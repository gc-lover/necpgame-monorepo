package server

import (
	"context"
	"encoding/json"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type MailRewardSubscriber struct {
	mailRepo         MailRepositoryInterface
	notificationRepo NotificationRepositoryInterface
	cache            *redis.Client
	logger           *logrus.Logger
	pubsub           *redis.PubSub
	ctx              context.Context
	cancel           context.CancelFunc
}

func NewMailRewardSubscriber(mailRepo MailRepositoryInterface, notificationRepo NotificationRepositoryInterface, cache *redis.Client) *MailRewardSubscriber {
	ctx, cancel := context.WithCancel(context.Background())
	return &MailRewardSubscriber{
		mailRepo:         mailRepo,
		notificationRepo: notificationRepo,
		cache:            cache,
		logger:           GetLogger(),
		ctx:              ctx,
		cancel:           cancel,
	}
}

func (mrs *MailRewardSubscriber) Start() error {
	mrs.logger.Info("Starting mail reward subscriber")

	channels := []string{
		"events:quest:rewards:*",
		"events:auction:won:*",
		"events:achievement:reward:*",
	}

	mrs.pubsub = mrs.cache.PSubscribe(mrs.ctx, channels...)

	go mrs.listen()
	return nil
}

func (mrs *MailRewardSubscriber) Stop() error {
	mrs.logger.Info("Stopping mail reward subscriber")
	mrs.cancel()
	if mrs.pubsub != nil {
		return mrs.pubsub.Close()
	}
	return nil
}

func (mrs *MailRewardSubscriber) listen() {
	ch := mrs.pubsub.Channel()
	
	for {
		select {
		case <-mrs.ctx.Done():
			mrs.logger.Info("Mail reward subscriber context cancelled")
			return
		case msg := <-ch:
			if msg == nil {
				continue
			}
			
			mrs.handleRewardEvent(msg.Channel, []byte(msg.Payload))
		}
	}
}

func (mrs *MailRewardSubscriber) handleRewardEvent(channel string, data []byte) {
	var eventData map[string]interface{}
	if err := json.Unmarshal(data, &eventData); err != nil {
		mrs.logger.WithError(err).Error("Failed to unmarshal reward event data")
		return
	}

	mrs.logger.WithFields(logrus.Fields{
		"channel": channel,
	}).Debug("Received reward event for mail")

	recipientIDStr, ok := eventData["character_id"].(string)
	if !ok {
		if accountIDStr, ok := eventData["account_id"].(string); ok {
			recipientIDStr = accountIDStr
		} else {
			mrs.logger.WithField("channel", channel).Warn("Event missing character_id or account_id")
			return
		}
	}

	recipientID, err := uuid.Parse(recipientIDStr)
	if err != nil {
		mrs.logger.WithError(err).WithField("recipient_id", recipientIDStr).Error("Invalid recipient_id in event")
		return
	}

	mail := mrs.createMailFromRewardEvent(channel, eventData, recipientID)
	if mail == nil {
		return
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	err = mrs.mailRepo.Create(ctx, mail)
	if err != nil {
		mrs.logger.WithError(err).WithField("channel", channel).Error("Failed to create mail from reward event")
		return
	}

	mrs.logger.WithFields(logrus.Fields{
		"channel":      channel,
		"recipient_id": recipientID,
		"mail_id":      mail.ID,
	}).Info("Created mail from reward event")

	if mrs.notificationRepo != nil {
		notification := &models.Notification{
			ID:        uuid.New(),
			AccountID: recipientID,
			Type:      models.NotificationTypeSystem,
			Priority:  models.NotificationPriorityMedium,
			Title:     "New Mail",
			Content:   "You have received a new mail with rewards",
			Data: map[string]interface{}{
				"mail_id": mail.ID.String(),
				"type":    mail.Type,
			},
			Status:    models.NotificationStatusUnread,
			Channels:  []models.DeliveryChannel{models.DeliveryChannelInGame, models.DeliveryChannelWebSocket},
			CreatedAt: time.Now(),
		}
		mrs.notificationRepo.Create(ctx, notification)
	}
}

func (mrs *MailRewardSubscriber) createMailFromRewardEvent(channel string, eventData map[string]interface{}, recipientID uuid.UUID) *models.MailMessage {
	subject := "Reward"
	content := "You have received a reward"
	attachments := make(map[string]interface{})
	mailType := models.MailTypeSystem

	if strings.Contains(channel, "quest") {
		subject = "Quest Reward"
		content = "Quest completed! Here are your rewards."
		if rewards, ok := eventData["rewards"].(map[string]interface{}); ok {
			attachments = rewards
		} else if items, ok := eventData["items"].([]interface{}); ok {
			attachments["items"] = items
		} else if currency, ok := eventData["currency"].(map[string]interface{}); ok {
			attachments["currency"] = currency
		}
	}

	if strings.Contains(channel, "auction") {
		subject = "Auction Win"
		content = "Congratulations! You won an auction."
		if item, ok := eventData["item"].(map[string]interface{}); ok {
			attachments["items"] = []interface{}{item}
		}
	}

	if strings.Contains(channel, "achievement") {
		subject = "Achievement Reward"
		content = "Achievement unlocked! Here is your reward."
		if rewards, ok := eventData["rewards"].(map[string]interface{}); ok {
			attachments = rewards
		}
	}

	now := time.Now()
	expiresAt := now.Add(30 * 24 * time.Hour)

	mail := &models.MailMessage{
		ID:          uuid.New(),
		SenderID:    nil,
		SenderName:  "System",
		RecipientID: recipientID,
		Type:        mailType,
		Subject:     subject,
		Content:     content,
		Attachments: attachments,
		CODAmount:   nil,
		Status:      models.MailStatusUnread,
		IsRead:      false,
		IsClaimed:   false,
		SentAt:      now,
		CreatedAt:   now,
		UpdatedAt:   now,
		ExpiresAt:   &expiresAt,
	}

	return mail
}


