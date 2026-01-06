package session

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"github.com/go-faster/errors"
	"github.com/gorilla/websocket"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/realtime-gateway-service-go/internal/service/protobuf"
)

// SessionConfig holds session configuration
type SessionConfig struct {
	ID              string
	Connection      *websocket.Conn
	ProtobufHandler *protobuf.Handler
	Logger          *zap.Logger
	Manager         *Manager
	OnClose         func(string)
}

// Session represents a WebSocket session
type Session struct {
	id              string
	conn            *websocket.Conn
	protobufHandler *protobuf.Handler
	logger          *zap.Logger
	manager         *Manager
	onClose         func(string)

	// Control channels
	sendChan        chan []byte
	closeChan       chan struct{}
	closeOnce       sync.Once

	// Session state
	isClosed        bool
	lastActivity    time.Time
	mu              sync.RWMutex
}

// NewSession creates a new WebSocket session
func NewSession(config SessionConfig) *Session {
	session := &Session{
		id:              config.ID,
		conn:            config.Connection,
		protobufHandler: config.ProtobufHandler,
		logger:          config.Logger,
		manager:         config.Manager,
		onClose:         config.OnClose,
		sendChan:        make(chan []byte, 256), // Buffered channel for outgoing messages
		closeChan:       make(chan struct{}),
		lastActivity:    time.Now(),
	}

	return session
}

// ID returns the session ID
func (s *Session) ID() string {
	return s.id
}

// Handle starts handling the WebSocket session
func (s *Session) Handle() error {
	defer s.Close()

	// Start goroutine for sending messages
	go s.sendLoop()

	// Main message handling loop
	for {
		select {
		case <-s.closeChan:
			return nil

		default:
			// Set read deadline
			if err := s.conn.SetReadDeadline(time.Now().Add(60 * time.Second)); err != nil {
				s.logger.Error("failed to set read deadline", zap.Error(err))
				return err
			}

			// Read message
			messageType, data, err := s.conn.ReadMessage()
			if err != nil {
				if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
					s.logger.Error("websocket error", zap.Error(err))
				}
				return err
			}

			// Update last activity
			s.mu.Lock()
			s.lastActivity = time.Now()
			s.mu.Unlock()

			// Handle message
			if err := s.handleMessage(messageType, data); err != nil {
				s.logger.Error("failed to handle message", zap.Error(err))
				continue
			}
		}
	}
}

// handleMessage processes incoming WebSocket messages
func (s *Session) handleMessage(messageType int, data []byte) error {
	switch messageType {
	case websocket.TextMessage:
		return s.handleTextMessage(data)
	case websocket.BinaryMessage:
		return s.handleBinaryMessage(data)
	case websocket.PingMessage:
		return s.handlePing()
	case websocket.PongMessage:
		return s.handlePong()
	default:
		s.logger.Warn("unknown message type", zap.Int("type", messageType))
		return nil
	}
}

// JSONMessage represents a JSON message structure
type JSONMessage struct {
	Type      string                 `json:"type"`
	Payload   map[string]interface{} `json:"payload"`
	Timestamp int64                  `json:"timestamp"`
	SessionID string                 `json:"session_id,omitempty"`
}

// handleTextMessage handles text messages (JSON)
func (s *Session) handleTextMessage(data []byte) error {
	s.logger.Debug("received text message", zap.String("session_id", s.id), zap.Int("size", len(data)))

	// Parse JSON message
	var msg JSONMessage
	if err := json.Unmarshal(data, &msg); err != nil {
		s.logger.Error("failed to parse JSON message", zap.Error(err), zap.String("session_id", s.id))
		return errors.Wrap(err, "invalid JSON message format")
	}

	// Set session ID if not provided
	if msg.SessionID == "" {
		msg.SessionID = s.id
	}

	// Route message based on type
	return s.routeJSONMessage(msg)
}

// routeJSONMessage routes JSON messages to appropriate handlers
func (s *Session) routeJSONMessage(msg JSONMessage) error {
	switch msg.Type {
	case "ping":
		return s.handlePingMessage(msg)
	case "subscribe":
		return s.handleSubscribeMessage(msg)
	case "unsubscribe":
		return s.handleUnsubscribeMessage(msg)
	case "broadcast":
		return s.handleBroadcastMessage(msg)
	case "private":
		return s.handlePrivateMessage(msg)
	default:
		s.logger.Warn("unknown message type", zap.String("type", msg.Type), zap.String("session_id", s.id))
		return s.protobufHandler.HandleMessage(s.id, []byte(fmt.Sprintf(`{"error": "unknown message type: %s"}`, msg.Type)))
	}
}

// handlePingMessage handles ping messages
func (s *Session) handlePingMessage(msg JSONMessage) error {
	// Send pong response
	pongMsg := JSONMessage{
		Type:      "pong",
		Timestamp: time.Now().Unix(),
		SessionID: s.id,
	}

	return s.sendJSONMessage(pongMsg)
}

// handleSubscribeMessage handles channel subscription
func (s *Session) handleSubscribeMessage(msg JSONMessage) error {
	channel, ok := msg.Payload["channel"].(string)
	if !ok {
		return errors.New("channel not specified in subscribe message")
	}

	if err := s.manager.SubscribeToChannel(s.id, channel); err != nil {
		return errors.Wrap(err, "failed to subscribe to channel")
	}

	return s.sendJSONMessage(JSONMessage{
		Type:      "subscribed",
		Payload:   map[string]interface{}{"channel": channel},
		Timestamp: time.Now().Unix(),
		SessionID: s.id,
	})
}

// handleUnsubscribeMessage handles channel unsubscription
func (s *Session) handleUnsubscribeMessage(msg JSONMessage) error {
	channel, ok := msg.Payload["channel"].(string)
	if !ok {
		return errors.New("channel not specified in unsubscribe message")
	}

	if err := s.manager.UnsubscribeFromChannel(s.id, channel); err != nil {
		return errors.Wrap(err, "failed to unsubscribe from channel")
	}

	return s.sendJSONMessage(JSONMessage{
		Type:      "unsubscribed",
		Payload:   map[string]interface{}{"channel": channel},
		Timestamp: time.Now().Unix(),
		SessionID: s.id,
	})
}

// handleBroadcastMessage handles broadcast messages
func (s *Session) handleBroadcastMessage(msg JSONMessage) error {
	channel, ok := msg.Payload["channel"].(string)
	if !ok {
		return errors.New("channel not specified in broadcast message")
	}

	// Broadcast the message to all channel subscribers
	messageData, err := json.Marshal(msg)
	if err != nil {
		return errors.Wrap(err, "failed to marshal broadcast message")
	}

	if err := s.manager.BroadcastToChannel(context.Background(), channel, messageData); err != nil {
		return errors.Wrap(err, "failed to broadcast message")
	}

	s.logger.Info("broadcasted message to channel", zap.String("channel", channel), zap.String("session_id", s.id))
	return nil
}

// handlePrivateMessage handles private messages
func (s *Session) handlePrivateMessage(msg JSONMessage) error {
	targetID, ok := msg.Payload["target_id"].(string)
	if !ok {
		return errors.New("target_id not specified in private message")
	}

	// Send private message to target session
	messageData, err := json.Marshal(msg)
	if err != nil {
		return errors.Wrap(err, "failed to marshal private message")
	}

	if err := s.manager.SendPrivateMessage(targetID, messageData); err != nil {
		return errors.Wrap(err, "failed to send private message")
	}

	s.logger.Info("sent private message", zap.String("target_id", targetID), zap.String("session_id", s.id))
	return nil
}

// sendJSONMessage sends a JSON message to the client
func (s *Session) sendJSONMessage(msg JSONMessage) error {
	data, err := json.Marshal(msg)
	if err != nil {
		return errors.Wrap(err, "failed to marshal JSON message")
	}

	return s.sendMessage(data)
}

// handleBinaryMessage handles binary messages (Protobuf)
func (s *Session) handleBinaryMessage(data []byte) error {
	s.logger.Debug("received binary message", zap.String("session_id", s.id), zap.Int("size", len(data)))

	// Delegate to protobuf handler
	return s.protobufHandler.HandleMessage(s.id, data)
}

// handlePing handles ping messages
func (s *Session) handlePing() error {
	// Send pong automatically handled by gorilla/websocket
	s.logger.Debug("received ping", zap.String("session_id", s.id))
	return nil
}

// handlePong handles pong messages
func (s *Session) handlePong() error {
	s.logger.Debug("received pong", zap.String("session_id", s.id))
	return nil
}

// Send sends a message to the client
func (s *Session) Send(data []byte) error {
	s.mu.RLock()
	if s.isClosed {
		s.mu.RUnlock()
		return errors.New("session is closed")
	}
	s.mu.RUnlock()

	select {
	case s.sendChan <- data:
		return nil
	case <-time.After(5 * time.Second):
		return errors.New("send timeout")
	}
}

// sendLoop runs in a goroutine to send messages to the client
func (s *Session) sendLoop() {
	ticker := time.NewTicker(30 * time.Second) // Ping every 30 seconds
	defer ticker.Stop()

	for {
		select {
		case <-s.closeChan:
			return

		case message := <-s.sendChan:
			if err := s.conn.WriteMessage(websocket.BinaryMessage, message); err != nil {
				s.logger.Error("failed to send message", zap.Error(err))
				s.Close()
				return
			}

		case <-ticker.C:
			// Send ping to keep connection alive
			if err := s.conn.WriteMessage(websocket.PingMessage, nil); err != nil {
				s.logger.Error("failed to send ping", zap.Error(err))
				s.Close()
				return
			}
		}
	}
}

// Close closes the session
func (s *Session) Close() error {
	var err error
	s.closeOnce.Do(func() {
		s.mu.Lock()
		s.isClosed = true
		s.mu.Unlock()

		close(s.closeChan)
		close(s.sendChan)

		// Close WebSocket connection
		err = s.conn.Close()

		// Notify session manager
		if s.onClose != nil {
			s.onClose(s.id)
		}

		s.logger.Info("session closed", zap.String("session_id", s.id))
	})
	return err
}

// IsClosed returns whether the session is closed
func (s *Session) IsClosed() bool {
	s.mu.RLock()
	defer s.mu.RUnlock()
	return s.isClosed
}

// GetLastActivity returns the last activity timestamp
func (s *Session) GetLastActivity() time.Time {
	s.mu.RLock()
	defer s.mu.RUnlock()
	return s.lastActivity
}

// generateSessionID generates a unique session ID
func generateSessionID() string {
	bytes := make([]byte, 16)
	if _, err := rand.Read(bytes); err != nil {
		// Fallback to timestamp-based ID if random fails
		return hex.EncodeToString([]byte(time.Now().String()[:16]))
	}
	return hex.EncodeToString(bytes)
}
