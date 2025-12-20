package server

import (
	"fmt"
	"net/http"
	"time"

	"github.com/gorilla/websocket"
	"github.com/sirupsen/logrus"
)

// WebSocketHandler OPTIMIZATION: Issue #1978 - WebSocket connection handler with performance optimizations
func (s *NetworkService) WebSocketHandler(w http.ResponseWriter, r *http.Request) {
	token := r.URL.Query().Get("token")
	clientID := r.URL.Query().Get("client_id")

	if token == "" || clientID == "" {
		http.Error(w, "Missing authentication parameters", http.StatusUnauthorized)
		return
	}

	// TODO: Validate token and extract user ID
	userID := "user_" + token // Simplified for demo

	conn, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		s.logger.WithError(err).Error("failed to upgrade connection")
		return
	}

	connection := &WSConnection{
		ID:            generateConnectionID(),
		UserID:        userID,
		ClientID:      clientID,
		Conn:          conn,
		ConnectedAt:   time.Now(),
		LastHeartbeat: time.Now(),
		Status:        "CONNECTED",
		SendChan:      make(chan []byte, 256), // OPTIMIZATION: Buffered channel for performance
	}

	s.connections.Store(connection.ID, connection)
	s.metrics.WSConnections.Inc()

	s.logger.WithFields(logrus.Fields{
		"connection_id": connection.ID,
		"user_id":       userID,
		"client_id":     clientID,
	}).Info("WebSocket connection established")

	// Start goroutines for this connection
	go s.handleConnection(connection)
	go s.writePump(connection)
}

// OPTIMIZATION: Issue #1978 - Connection read handler with timeout management
func (s *NetworkService) handleConnection(conn *WSConnection) {
	defer func() {
		s.connections.Delete(conn.ID)
		s.metrics.WSConnections.Dec()
		conn.Conn.Close()
		close(conn.SendChan)

		// Broadcast disconnect event
		s.broadcastPresenceUpdate(conn.UserID, NetworkMessage{
			MessageID: generateMessageID(),
			Type:      "USER_DISCONNECTED",
			SenderID:  conn.UserID,
			Timestamp: time.Now().Unix(),
		})
	}()

	conn.Conn.SetReadDeadline(time.Now().Add(s.config.HeartbeatInterval * 2))

	for {
		_, message, err := conn.Conn.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
				s.logger.WithError(err).Error("WebSocket read error")
			}
			break
		}

		s.metrics.MessagesReceived.Inc()
		s.handleMessage(conn, message)
	}
}

// OPTIMIZATION: Issue #1978 - Write pump with heartbeat and flow control
func (s *NetworkService) writePump(conn *WSConnection) {
	ticker := time.NewTicker(s.config.HeartbeatInterval)
	defer ticker.Stop()

	for {
		select {
		case message, ok := <-conn.SendChan:
			if !ok {
				conn.Conn.WriteMessage(websocket.CloseMessage, []byte{})
				return
			}

			conn.Conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
			if err := conn.Conn.WriteMessage(websocket.TextMessage, message); err != nil {
				return
			}
			s.metrics.MessagesSent.Inc()

		case <-ticker.C:
			conn.Conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
			if err := conn.Conn.WriteMessage(websocket.PingMessage, nil); err != nil {
				return
			}
		}
	}
}

// OPTIMIZATION: Issue #1978 - Message processing with type-based routing
func (s *NetworkService) handleMessage(conn *WSConnection, message []byte) {
	var msg NetworkMessage
	if err := json.Unmarshal(message, &msg); err != nil {
		s.logger.WithError(err).Error("failed to parse message")
		return
	}

	switch msg.Type {
	case "HEARTBEAT":
		conn.LastHeartbeat = time.Now()
	case "SUBSCRIBE":
		s.handleSubscribe(conn, msg)
	case "UNSUBSCRIBE":
		s.handleUnsubscribe(conn, msg)
	case "MESSAGE":
		s.handleUserMessage(conn, msg)
	case "PRESENCE_UPDATE":
		s.handlePresenceUpdate(conn, msg)
	default:
		s.logger.WithField("type", msg.Type).Warn("unknown message type")
	}
}

// OPTIMIZATION: Issue #1978 - Subscription management for channels and events
func (s *NetworkService) handleSubscribe(conn *WSConnection, msg NetworkMessage) {
	channel := msg.Channel
	if channel == "" {
		return
	}

	conn.Subscriptions = append(conn.Subscriptions, channel)
	s.logger.WithFields(logrus.Fields{
		"connection_id": conn.ID,
		"channel":       channel,
	}).Info("client subscribed to channel")
}

func (s *NetworkService) handleUnsubscribe(conn *WSConnection, msg NetworkMessage) {
	channel := msg.Channel
	if channel == "" {
		return
	}

	for i, sub := range conn.Subscriptions {
		if sub == channel {
			conn.Subscriptions = append(conn.Subscriptions[:i], conn.Subscriptions[i+1:]...)
			break
		}
	}
	s.logger.WithFields(logrus.Fields{
		"connection_id": conn.ID,
		"channel":       channel,
	}).Info("client unsubscribed from channel")
}

func (s *NetworkService) handleUserMessage(conn *WSConnection, msg NetworkMessage) {
	// Handle user messages (chat, commands, etc.)
	s.logger.WithFields(logrus.Fields{
		"connection_id": conn.ID,
		"message_type":  msg.Type,
	}).Debug("user message received")
}

func (s *NetworkService) handlePresenceUpdate(conn *WSConnection, msg NetworkMessage) {
	// Handle presence updates from clients
	s.logger.WithFields(logrus.Fields{
		"connection_id": conn.ID,
		"presence_type": msg.Content,
	}).Debug("presence update received")
}

// OPTIMIZATION: Issue #1978 - Helper functions for ID generation
func generateConnectionID() string {
	return fmt.Sprintf("conn_%d", time.Now().UnixNano())
}

func generateMessageID() string {
	return fmt.Sprintf("msg_%d", time.Now().UnixNano())
}

// OPTIMIZATION: Issue #1978 - Send message to specific connection with error handling
func (s *NetworkService) sendToConnection(conn *WSConnection, message interface{}) {
	data, err := json.Marshal(message)
	if err != nil {
		s.logger.WithError(err).Error("failed to marshal message")
		return
	}

	select {
	case conn.SendChan <- data:
	default:
		// Channel full, close connection
		conn.Conn.Close()
	}
}
