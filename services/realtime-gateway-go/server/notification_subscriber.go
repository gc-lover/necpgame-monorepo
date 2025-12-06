// Issue: #141889261
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

type NotificationEvent struct {
	NotificationID string                 `json:"notification_id"`
	AccountID      string                 `json:"account_id"`
	Type           string                 `json:"type"`
	Priority       string                 `json:"priority"`
	Title          string                 `json:"title"`
	Content        string                 `json:"content"`
	Data           map[string]interface{} `json:"data"`
	Timestamp      string                 `json:"timestamp"`
}

type NotificationSubscriber struct {
	redis        *redis.Client
	handler      *GatewayHandler
	logger       *logrus.Logger
	pubsub       *redis.PubSub
	ctx          context.Context
	cancel       context.CancelFunc
}

func NewNotificationSubscriber(redisClient *redis.Client, handler *GatewayHandler) *NotificationSubscriber {
	ctx, cancel := context.WithCancel(context.Background())
	return &NotificationSubscriber{
		redis:   redisClient,
		handler: handler,
		logger:  GetLogger(),
		ctx:     ctx,
		cancel:  cancel,
	}
}

func (ns *NotificationSubscriber) Start() error {
	ns.logger.Info("Starting notification subscriber")

	channels := []string{
		"events:notification:websocket:*",
	}

	ns.pubsub = ns.redis.PSubscribe(ns.ctx, channels...)

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
			
			ns.handleNotificationEvent(msg.Channel, []byte(msg.Payload))
		}
	}
}

func (ns *NotificationSubscriber) handleNotificationEvent(channel string, data []byte) {
	var notification NotificationEvent
	if err := json.Unmarshal(data, &notification); err != nil {
		ns.logger.WithError(err).Error("Failed to unmarshal notification event")
		return
	}

	ns.logger.WithFields(logrus.Fields{
		"channel":         channel,
		"account_id":     notification.AccountID,
		"notification_id": notification.NotificationID,
	}).Info("Received notification event")

	ns.sendNotification(notification)
}

func (ns *NotificationSubscriber) sendNotification(notification NotificationEvent) {
	accountID, err := uuid.Parse(notification.AccountID)
	if err != nil {
		ns.logger.WithError(err).WithField("account_id", notification.AccountID).Error("Invalid account_id in notification")
		return
	}

	ns.handler.clientConnsMu.RLock()
	defer ns.handler.clientConnsMu.RUnlock()

	found := false
	for conn, clientConn := range ns.handler.clientConns {
		ns.handler.sessionTokensMu.RLock()
		sessionToken, hasToken := ns.handler.sessionTokens[conn]
		ns.handler.sessionTokensMu.RUnlock()

		if !hasToken {
			continue
		}

		if ns.handler.sessionMgr == nil {
			continue
		}

		session, err := ns.handler.sessionMgr.GetSessionByToken(context.Background(), sessionToken)
		if err != nil || session == nil {
			continue
		}

		match := false
		if session.CharacterID != nil && *session.CharacterID == accountID {
			match = true
		}

		if !match && session.PlayerID != "" {
			if session.PlayerID == notification.AccountID {
				match = true
			} else {
				playerIDAsUUID, err := uuid.Parse(session.PlayerID)
				if err == nil && playerIDAsUUID == accountID {
					match = true
				}
			}
		}

		if match {
			notificationMessage := ns.buildNotificationMessage(notification)
			if notificationMessage == nil {
				ns.logger.WithField("account_id", notification.AccountID).Error("Failed to build notification message, skipping send")
				break
			}
			
			clientConn.mu.Lock()
			conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
			if err := conn.WriteMessage(websocket.TextMessage, notificationMessage); err != nil {
				ns.logger.WithError(err).WithField("account_id", notification.AccountID).Error("Failed to send notification")
				clientConn.mu.Unlock()
				ns.handler.RemoveClientConnection(conn)
				continue
			} else {
				ns.logger.WithFields(logrus.Fields{
					"account_id":      notification.AccountID,
					"notification_id": notification.NotificationID,
				}).Info("Sent notification to player")
				found = true
			}
			clientConn.mu.Unlock()
			break
		}
	}

	if !found {
		ns.logger.WithField("account_id", notification.AccountID).Debug("Player not connected, notification not sent")
	}
}

// Issue: #1407 - Error handling: returns nil on marshal error, caller checks and skips send
func (ns *NotificationSubscriber) buildNotificationMessage(notification NotificationEvent) []byte {
	response := map[string]interface{}{
		"type":            "notification",
		"notification_id": notification.NotificationID,
		"account_id":      notification.AccountID,
		"notification_type": notification.Type,
		"priority":        notification.Priority,
		"title":           notification.Title,
		"content":         notification.Content,
		"timestamp":       notification.Timestamp,
	}

	if notification.Data != nil {
		response["data"] = notification.Data
	}

	message, err := json.Marshal(response)
	if err != nil {
		ns.logger.WithError(err).Error("Failed to marshal notification message")
		return nil
	}
	return message
}

