// Issue: #2218 - Backend: Добавить unit-тесты для ws-lobby-go
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/websocket"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"
	"ws-lobby-go/server/internal/models"
)

// LobbyService handles WebSocket lobby connections and real-time messaging
type LobbyService struct {
	logger       *zap.Logger
	redis        *redis.Client
	connections  map[string]*models.Connection // connectionID -> connection
	rooms        map[string]*models.Room       // roomID -> room
	mu           sync.RWMutex
	upgrader     websocket.Upgrader
	messagePool  sync.Pool
}

// NewLobbyService creates a new lobby service instance
func NewLobbyService(logger *zap.Logger, redisURL, dbURL string) (*LobbyService, error) {
	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	rdb := redis.NewClient(opt)

	// Test Redis connection
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := rdb.Ping(ctx).Err(); err != nil {
		logger.Error("Failed to connect to Redis", zap.Error(err))
		return nil, err
	}

	service := &LobbyService{
		logger:      logger,
		redis:       rdb,
		connections: make(map[string]*models.Connection),
		rooms:       make(map[string]*models.Room),
		upgrader: websocket.Upgrader{
			ReadBufferSize:  1024,
			WriteBufferSize: 1024,
			CheckOrigin: func(r *http.Request) bool {
				// Allow connections from any origin in development
				// In production, implement proper CORS checking
				return true
			},
		},
	}

	// Initialize message pool for memory efficiency
	service.messagePool = sync.Pool{
		New: func() interface{} {
			return &models.LobbyMessage{}
		},
	}

	return service, nil
}

// HandleWebSocketConnection handles new WebSocket connections
func (s *LobbyService) HandleWebSocketConnection(w http.ResponseWriter, r *http.Request) {
	token := r.URL.Query().Get("token")
	if token == "" {
		http.Error(w, "Missing authentication token", http.StatusUnauthorized)
		return
	}

	// TODO: Validate JWT token
	userID := uuid.New() // Placeholder - should parse from JWT

	conn, err := s.upgrader.Upgrade(w, r, nil)
	if err != nil {
		s.logger.Error("Failed to upgrade connection", zap.Error(err))
		return
	}

	connectionID := uuid.New().String()
	connection := &models.Connection{
		ID:       connectionID,
		UserID:   userID,
		Conn:     conn,
		JoinedAt: time.Now(),
	}

	s.mu.Lock()
	s.connections[connectionID] = connection
	s.mu.Unlock()

	s.logger.Info("New WebSocket connection established",
		zap.String("connection_id", connectionID),
		zap.String("user_id", userID.String()))

	// Start connection handler
	go s.handleConnection(connection)
}

// handleConnection processes messages from a single connection
func (s *LobbyService) handleConnection(conn *models.Connection) {
	defer func() {
		s.mu.Lock()
		delete(s.connections, conn.ID)
		s.mu.Unlock()

		conn.Conn.Close()
		s.logger.Info("Connection closed", zap.String("connection_id", conn.ID))
	}()

	conn.Conn.SetReadDeadline(time.Now().Add(60 * time.Second))
	conn.Conn.SetPongHandler(func(string) error {
		conn.Conn.SetReadDeadline(time.Now().Add(60 * time.Second))
		return nil
	})

	for {
		messageType, data, err := conn.Conn.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
				s.logger.Error("WebSocket error", zap.Error(err), zap.String("connection_id", conn.ID))
			}
			break
		}

		if messageType != websocket.TextMessage {
			continue
		}

		var msg models.LobbyMessage
		if err := json.Unmarshal(data, &msg); err != nil {
			s.logger.Error("Failed to parse message", zap.Error(err), zap.String("connection_id", conn.ID))
			s.sendError(conn, "invalid_message_format", "Message format is invalid")
			continue
		}

		msg.SenderID = conn.UserID
		msg.Timestamp = time.Now()

		if err := s.processMessage(conn, &msg); err != nil {
			s.logger.Error("Failed to process message", zap.Error(err), zap.String("connection_id", conn.ID))
		}
	}
}

// processMessage handles different types of lobby messages
func (s *LobbyService) processMessage(conn *models.Connection, msg *models.LobbyMessage) error {
	switch msg.Type {
	case "chat_message":
		return s.handleChatMessage(conn, msg)
	case "room_create":
		return s.handleRoomCreate(conn, msg)
	case "room_join":
		return s.handleRoomJoin(conn, msg)
	case "room_leave":
		return s.handleRoomLeave(conn, msg)
	case "heartbeat":
		return s.handleHeartbeat(conn, msg)
	default:
		s.sendError(conn, "unknown_message_type", "Unknown message type: "+msg.Type)
		return nil
	}
}

// handleChatMessage processes chat messages
func (s *LobbyService) handleChatMessage(conn *models.Connection, msg *models.LobbyMessage) error {
	// Broadcast to room or globally
	if msg.RoomID != nil {
		return s.broadcastToRoom(*msg.RoomID, msg)
	}
	return s.broadcastToAll(msg)
}

// handleRoomCreate creates a new lobby room
func (s *LobbyService) handleRoomCreate(conn *models.Connection, msg *models.LobbyMessage) error {
	roomID := uuid.New().String()

	room := &models.Room{
		ID:          roomID,
		Name:        "New Room", // Should come from payload
		MaxPlayers:  10,
		CreatedBy:   conn.UserID,
		CreatedAt:   time.Now(),
		PlayerIDs:   []uuid.UUID{conn.UserID},
	}

	s.mu.Lock()
	s.rooms[roomID] = room
	s.mu.Unlock()

	// Notify creator
	response := &models.LobbyMessage{
		Type:      "room_created",
		Payload:   map[string]interface{}{"room_id": roomID, "room": room},
		Timestamp: time.Now(),
	}

	return s.sendToConnection(conn, response)
}

// handleRoomJoin adds player to existing room
func (s *LobbyService) handleRoomJoin(conn *models.Connection, msg *models.LobbyMessage) error {
	// Implementation would validate room exists, check capacity, etc.
	// For now, just acknowledge
	response := &models.LobbyMessage{
		Type:      "room_joined",
		Payload:   map[string]interface{}{"status": "success"},
		Timestamp: time.Now(),
	}

	return s.sendToConnection(conn, response)
}

// handleRoomLeave removes player from room
func (s *LobbyService) handleRoomLeave(conn *models.Connection, msg *models.LobbyMessage) error {
	response := &models.LobbyMessage{
		Type:      "room_left",
		Payload:   map[string]interface{}{"status": "success"},
		Timestamp: time.Now(),
	}

	return s.sendToConnection(conn, response)
}

// handleHeartbeat responds to heartbeat messages
func (s *LobbyService) handleHeartbeat(conn *models.Connection, msg *models.LobbyMessage) error {
	response := &models.LobbyMessage{
		Type:      "heartbeat_ack",
		Timestamp: time.Now(),
	}

	return s.sendToConnection(conn, response)
}

// broadcastToRoom sends message to all players in a room
func (s *LobbyService) broadcastToRoom(roomID string, msg *models.LobbyMessage) error {
	s.mu.RLock()
	room, exists := s.rooms[roomID]
	s.mu.RUnlock()

	if !exists {
		return nil // Room doesn't exist
	}

	for _, playerID := range room.PlayerIDs {
		if conn := s.findConnectionByUserID(playerID); conn != nil {
			if err := s.sendToConnection(conn, msg); err != nil {
				s.logger.Error("Failed to send message to player",
					zap.Error(err),
					zap.String("user_id", playerID.String()))
			}
		}
	}

	return nil
}

// broadcastToAll sends message to all connected players
func (s *LobbyService) broadcastToAll(msg *models.LobbyMessage) error {
	s.mu.RLock()
	connections := make([]*models.Connection, 0, len(s.connections))
	for _, conn := range s.connections {
		connections = append(connections, conn)
	}
	s.mu.RUnlock()

	for _, conn := range connections {
		if err := s.sendToConnection(conn, msg); err != nil {
			s.logger.Error("Failed to broadcast message",
				zap.Error(err),
				zap.String("connection_id", conn.ID))
		}
	}

	return nil
}

// sendToConnection sends a message to a specific connection
func (s *LobbyService) sendToConnection(conn *models.Connection, msg *models.LobbyMessage) error {
	if conn.Conn == nil {
		// Skip sending for tests or when connection is not initialized
		return nil
	}

	data, err := json.Marshal(msg)
	if err != nil {
		return err
	}

	conn.Conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
	return conn.Conn.WriteMessage(websocket.TextMessage, data)
}

// sendError sends an error message to a connection
func (s *LobbyService) sendError(conn *models.Connection, code, message string) {
	errorMsg := &models.LobbyMessage{
		Type: "error",
		Payload: map[string]interface{}{
			"code":    code,
			"message": message,
		},
		Timestamp: time.Now(),
	}

	if err := s.sendToConnection(conn, errorMsg); err != nil {
		s.logger.Error("Failed to send error message", zap.Error(err))
	}
}

// findConnectionByUserID finds a connection by user ID
func (s *LobbyService) findConnectionByUserID(userID uuid.UUID) *models.Connection {
	s.mu.RLock()
	defer s.mu.RUnlock()

	for _, conn := range s.connections {
		if conn.UserID == userID {
			return conn
		}
	}

	return nil
}

// Shutdown gracefully shuts down the service
func (s *LobbyService) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down lobby service")

	// Close all connections
	s.mu.Lock()
	for _, conn := range s.connections {
		conn.Conn.Close()
	}
	s.connections = nil
	s.mu.Unlock()

	// Close Redis connection
	return s.redis.Close()
}
