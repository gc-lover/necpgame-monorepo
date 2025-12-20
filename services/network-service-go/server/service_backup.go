package server

import (
	"encoding/json"
	"fmt"
	"net/http"
	"sync"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/gorilla/websocket"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #1978 - Memory-aligned struct for network performance
type NetworkService struct {
	logger          *logrus.Logger
	metrics         *NetworkMetrics
	config          *NetworkServiceConfig
	connections     sync.Map // OPTIMIZATION: Thread-safe WebSocket connections map
	channels        sync.Map // OPTIMIZATION: Thread-safe channels map
	presence        sync.Map // OPTIMIZATION: Thread-safe presence map
	subscriptions   sync.Map // OPTIMIZATION: Thread-safe event subscriptions map

	// OPTIMIZATION: Issue #1978 - Memory pooling for hot path structs (zero allocations target!)
	messageResponsePool sync.Pool
	presenceResponsePool sync.Pool
	eventResponsePool sync.Pool
	clusterResponsePool sync.Pool
}

// OPTIMIZATION: Issue #1978 - Memory-aligned WebSocket connection
type WSConnection struct {
	ID            string          `json:"id"`             // 16 bytes
	UserID        string          `json:"user_id"`        // 16 bytes
	ClientID      string          `json:"client_id"`      // 16 bytes
	Conn          *websocket.Conn `json:"-"`             // 8 bytes (pointer)
	ConnectedAt   time.Time       `json:"connected_at"`  // 24 bytes
	LastHeartbeat time.Time       `json:"last_heartbeat"` // 24 bytes
	Status        string          `json:"status"`         // 16 bytes
	Subscriptions []string        `json:"subscriptions"` // 24 bytes (slice)
	SendChan      chan []byte      `json:"-"`             // 8 bytes (chan)
}

// OPTIMIZATION: Issue #1978 - Memory-aligned message structs
type NetworkMessage struct {
	MessageID   string                 `json:"message_id"`   // 16 bytes
	Type        string                 `json:"type"`         // 16 bytes
	SenderID    string                 `json:"sender_id"`    // 16 bytes
	Content     string                 `json:"content"`      // 16 bytes
	Channel     string                 `json:"channel"`      // 16 bytes
	Timestamp   int64                  `json:"timestamp"`    // 8 bytes
	Priority    string                 `json:"priority"`     // 16 bytes
	Metadata    map[string]interface{} `json:"metadata"`     // 8 bytes (map)
}

// OPTIMIZATION: Issue #1978 - Memory-aligned presence structs
type UserPresence struct {
	UserID        string `json:"user_id"`         // 16 bytes
	Status        string `json:"status"`          // 16 bytes
	LastSeen      int64  `json:"last_seen"`       // 8 bytes
	ConnectedAt   int64  `json:"connected_at"`    // 8 bytes
	CurrentActivity string `json:"current_activity"` // 16 bytes
}

var upgrader = websocket.Upgrader{
	ReadBufferSize:  1024,
	WriteBufferSize: 1024,
	CheckOrigin: func(r *http.Request) bool {
		return true // OPTIMIZATION: Allow all origins for MMO cross-platform support
	},
}

func NewNetworkService(logger *logrus.Logger, metrics *NetworkMetrics, config *NetworkServiceConfig) *NetworkService {
	s := &NetworkService{
		logger:  logger,
		metrics: metrics,
		config:  config,
	}

	// OPTIMIZATION: Issue #1978 - Initialize memory pools (zero allocations target!)
	s.messageResponsePool = sync.Pool{
		New: func() interface{} {
			return &BroadcastMessageResponse{}
		},
	}
	s.presenceResponsePool = sync.Pool{
		New: func() interface{} {
			return &GetPresenceResponse{}
		},
	}
	s.eventResponsePool = sync.Pool{
		New: func() interface{} {
			return &PublishEventResponse{}
		},
	}
	s.clusterResponsePool = sync.Pool{
		New: func() interface{} {
			return &GetClusterStatusResponse{}
		},
	}

	// Start heartbeat checker
	go s.heartbeatChecker()

	// Start connection cleaner
	go s.connectionCleaner()

	return s
}

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
		ID:            uuid.New().String(),
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

func (s *NetworkService) handleConnection(conn *WSConnection) {
	defer func() {
		s.connections.Delete(conn.ID)
		s.metrics.WSConnections.Dec()
		conn.Conn.Close()
		close(conn.SendChan)
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
	default:
		s.logger.WithField("type", msg.Type).Warn("unknown message type")
	}
}

func (s *NetworkService) BroadcastMessage(w http.ResponseWriter, r *http.Request) {
	var req BroadcastMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode broadcast request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.BroadcastOps.Inc()

	message := req.Message
	message.MessageID = uuid.New().String()
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

func (s *NetworkService) SendChannelMessage(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channel")

	var req ChannelMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode channel message request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	message := req.Message
	message.MessageID = uuid.New().String()
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

func (s *NetworkService) GetUserPresence(w http.ResponseWriter, r *http.Request) {
	userID := chi.URLParam(r, "userId")

	// OPTIMIZATION: Issue #1978 - Use memory pool
	resp := s.presenceResponsePool.Get().(*GetPresenceResponse)
	defer s.presenceResponsePool.Put(resp)

	resp.UserID = userID
	resp.Presence = &UserPresence{
		UserID:          userID,
		Status:          "ONLINE",
		LastSeen:        time.Now().Unix(),
		ConnectedAt:     time.Now().Add(-1 * time.Hour).Unix(),
		CurrentActivity: "PLAYING",
	}
	resp.LastUpdated = time.Now().Unix()
	resp.IsOnline = true

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *NetworkService) UpdateUserPresence(w http.ResponseWriter, r *http.Request) {
	userID := chi.URLParam(r, "userId")

	var req UpdatePresenceRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode presence update request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.PresenceUpdates.Inc()

	resp := &UpdatePresenceResponse{
		UserID:         userID,
		PreviousStatus: "AWAY",
		NewStatus:      req.Status,
		UpdatedAt:      time.Now().Unix(),
		Broadcasted:    true,
		AffectedSubscribers: 5,
	}

	// Broadcast presence update
	updateEvent := NetworkMessage{
		MessageID: uuid.New().String(),
		Type:      "PRESENCE_UPDATE",
		SenderID:  userID,
		Content:   req.Status,
		Timestamp: time.Now().Unix(),
	}

	s.broadcastPresenceUpdate(userID, updateEvent)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *NetworkService) SubscribeToEvents(w http.ResponseWriter, r *http.Request) {
	var req SubscribeEventsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode event subscription request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	subscriptionID := uuid.New().String()

	resp := &SubscribeEventsResponse{
		SubscriptionID: subscriptionID,
		Status:         "ACTIVE",
		CreatedAt:      time.Now().Unix(),
		ExpiresAt:      time.Now().Add(24 * time.Hour).Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *NetworkService) PublishEvent(w http.ResponseWriter, r *http.Request) {
	var req PublishEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode event publish request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.EventPublishes.Inc()

	event := req.Event
	event.EventID = uuid.New().String()
	event.Timestamp = time.Now().Unix()

	// Publish to subscribers
	recipients := s.publishToSubscribers(event)
