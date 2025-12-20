// Package server Issue: #140899117, #141889261
package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/websocket"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type BanNotification struct {
	BanID       string  `json:"ban_id"`
	CharacterID string  `json:"character_id"`
	Reason      string  `json:"reason"`
	ExpiresAt   *string `json:"expires_at,omitempty"`
	ChannelID   *string `json:"channel_id,omitempty"`
	Type        *string `json:"type,omitempty"`
	Timestamp   string  `json:"timestamp"`
}

type BanNotificationSubscriber struct {
	redis   *redis.Client
	handler *GatewayHandler
	logger  *logrus.Logger
	pubsub  *redis.PubSub
	ctx     context.Context
	cancel  context.CancelFunc
}

func NewBanNotificationSubscriber(redisClient *redis.Client, handler *GatewayHandler) *BanNotificationSubscriber {
	ctx, cancel := context.WithCancel(context.Background())
	return &BanNotificationSubscriber{
		redis:   redisClient,
		handler: handler,
		logger:  GetLogger(),
		ctx:     ctx,
		cancel:  cancel,
	}
}

func (bns *BanNotificationSubscriber) Start() error {
	bns.logger.Info("Starting ban notification subscriber")

	channels := []string{
		"events:chat:ban:created",
		"events:chat:ban:auto:spam",
		"events:chat:ban:auto:severe",
		"events:chat:ban:removed",
	}

	bns.pubsub = bns.redis.Subscribe(bns.ctx, channels...)

	go bns.listen()
	return nil
}

func (bns *BanNotificationSubscriber) Stop() error {
	bns.logger.Info("Stopping ban notification subscriber")
	bns.cancel()
	if bns.pubsub != nil {
		return bns.pubsub.Close()
	}
	return nil
}

func (bns *BanNotificationSubscriber) listen() {
	ch := bns.pubsub.Channel()

	for {
		select {
		case <-bns.ctx.Done():
			bns.logger.Info("Ban notification subscriber context cancelled")
			return
		case msg := <-ch:
			if msg == nil {
				continue
			}

			bns.handleBanEvent(msg.Channel, []byte(msg.Payload))
		}
	}
}

func (bns *BanNotificationSubscriber) handleBanEvent(channel string, data []byte) {
	var notification BanNotification
	if err := json.Unmarshal(data, &notification); err != nil {
		bns.logger.WithError(err).Error("Failed to unmarshal ban notification")
		return
	}

	bns.logger.WithFields(logrus.Fields{
		"channel":      channel,
		"character_id": notification.CharacterID,
		"ban_id":       notification.BanID,
	}).Info("Received ban notification event")

	// Determine notification type based on channel
	isRemoved := channel == "events:chat:ban:removed"
	bns.sendBanNotification(notification, isRemoved)
}

func (bns *BanNotificationSubscriber) sendBanNotification(notification BanNotification, isRemoved bool) {
	characterID, err := uuid.Parse(notification.CharacterID)
	if err != nil {
		bns.logger.WithError(err).WithField("character_id", notification.CharacterID).Error("Invalid character_id in ban notification")
		return
	}

	bns.handler.clientConnsMu.RLock()
	defer bns.handler.clientConnsMu.RUnlock()

	found := false
	for conn, clientConn := range bns.handler.clientConns {
		bns.handler.sessionTokensMu.RLock()
		sessionToken, hasToken := bns.handler.sessionTokens[conn]
		bns.handler.sessionTokensMu.RUnlock()

		if !hasToken {
			continue
		}

		if bns.handler.sessionMgr == nil {
			continue
		}

		session, err := bns.handler.sessionMgr.GetSessionByToken(context.Background(), sessionToken)
		if err != nil || session == nil {
			continue
		}

		match := false
		if session.CharacterID != nil && *session.CharacterID == characterID {
			match = true
		}

		if !match && session.PlayerID != "" {
			if session.PlayerID == notification.CharacterID {
				match = true
			} else {
				playerIDAsUUID, err := uuid.Parse(session.PlayerID)
				if err == nil && playerIDAsUUID == characterID {
					match = true
				}
			}
		}

		if match {
			notificationMessage := bns.buildNotificationMessage(notification, isRemoved)
			if notificationMessage == nil {
				bns.logger.WithField("character_id", notification.CharacterID).Error("Failed to build ban notification message, skipping send")
				break
			}

			clientConn.mu.Lock()
			conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
			if err := conn.WriteMessage(websocket.TextMessage, notificationMessage); err != nil {
				bns.logger.WithError(err).WithField("character_id", notification.CharacterID).Error("Failed to send ban notification")
				clientConn.mu.Unlock()
				bns.handler.RemoveClientConnection(conn)
				continue
			} else {
				action := "ban notification"
				if isRemoved {
					action = "ban removal notification"
				}
				bns.logger.WithFields(logrus.Fields{
					"character_id": notification.CharacterID,
					"ban_id":       notification.BanID,
				}).Info("Sent " + action + " to player")
				found = true
			}
			clientConn.mu.Unlock()
			break
		}
	}

	if !found {
		bns.logger.WithField("character_id", notification.CharacterID).Debug("Player not connected, ban notification not sent")
	}
}

// Issue: #1407 - Error handling: returns nil on marshal error, caller checks and skips send
func (bns *BanNotificationSubscriber) buildNotificationMessage(notification BanNotification, isRemoved bool) []byte {
	notificationType := "ban_notification"
	if isRemoved {
		notificationType = "ban_removed"
	}

	response := map[string]interface{}{
		"type":      notificationType,
		"ban_id":    notification.BanID,
		"timestamp": notification.Timestamp,
	}

	// Only include reason for ban notifications, not removals
	if !isRemoved && notification.Reason != "" {
		response["reason"] = notification.Reason
	}

	if notification.ExpiresAt != nil {
		response["expires_at"] = *notification.ExpiresAt
	}

	if notification.ChannelID != nil {
		response["channel_id"] = *notification.ChannelID
	}

	if notification.Type != nil {
		response["ban_type"] = *notification.Type
	}

	message, err := json.Marshal(response)
	if err != nil {
		bns.logger.WithError(err).Error("Failed to marshal ban notification message")
		return nil
	}
	return message
}
