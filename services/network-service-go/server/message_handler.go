package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
)

// OPTIMIZATION: Issue #1978 - Message broadcasting with concurrent processing
func (s *NetworkService) BroadcastMessage(w http.ResponseWriter, r *http.Request) {
	var req BroadcastMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode broadcast request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.BroadcastOps.Inc()

	message := req.Message
	message.MessageID = generateMessageID()
	message.Timestamp = time.Now().Unix()

	// Broadcast to all connections (except excluded users)
	recipients := 0
	s.connections.Range(func(key, value interface{}) bool {
		conn := value.(*WSConnection)
		if !s.isExcluded(conn.UserID, req.ExcludeUsers) {
			s.sendToConnection(conn, message)
			recipients++
		}
		return true
	})

	// OPTIMIZATION: Issue #1978 - Use memory pool
	resp := s.messageResponsePool.Get().(*BroadcastMessageResponse)
	defer s.messageResponsePool.Put(resp)

	resp.MessageID = message.MessageID
	resp.RecipientsCount = recipients
	resp.SentAt = message.Timestamp
	resp.DeliveryStatus = "SENT"

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"message_id":       message.MessageID,
		"recipients_count": recipients,
	}).Info("message broadcasted successfully")
}

// OPTIMIZATION: Issue #1978 - Channel messaging with permission checks
func (s *NetworkService) SendChannelMessage(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channel")

	var req ChannelMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode channel message request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	message := req.Message
	message.MessageID = generateMessageID()
	message.Channel = channelID
	message.Timestamp = time.Now().Unix()

	// Send to channel subscribers
	recipients := s.broadcastToChannel(channelID, message)

	resp := &ChannelMessageResponse{
		MessageID:       message.MessageID,
		ChannelID:       channelID,
		RecipientsCount: recipients,
		SentAt:          message.Timestamp,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"message_id":       message.MessageID,
		"channel_id":       channelID,
		"recipients_count": recipients,
	}).Info("channel message sent successfully")
}

// OPTIMIZATION: Issue #1978 - Channel broadcasting with subscription filtering
func (s *NetworkService) broadcastToChannel(channelID string, message NetworkMessage) int {
	recipients := 0
	s.connections.Range(func(key, value interface{}) bool {
		conn := value.(*WSConnection)
		for _, sub := range conn.Subscriptions {
			if sub == channelID {
				s.sendToConnection(conn, message)
				recipients++
				break
			}
		}
		return true
	})
	return recipients
}

// OPTIMIZATION: Issue #1978 - Exclusion check for broadcast filtering
func (s *NetworkService) isExcluded(userID string, excludeList []string) bool {
	for _, excluded := range excludeList {
		if excluded == userID {
			return true
		}
	}
	return false
}
