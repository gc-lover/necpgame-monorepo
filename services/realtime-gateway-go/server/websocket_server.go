// Issue: #1580
// WebSocket Server for lobby/chat - complements UDP for non-real-time features
// Performance: WebSocket for chat/lobby, UDP for game state

package server

import (
	"context"
	"fmt"
	"net/http"
	"sync"
	"sync/atomic"
	"time"

	"github.com/gorilla/websocket"
	"go.uber.org/zap"
)

// WebSocketServer handles WebSocket connections for lobby/chat
type WebSocketServer struct {
	addr     string
	server   *http.Server
	upgrader websocket.Upgrader
	logger   *zap.Logger

	// Connection management
	connections sync.Map // map[string]*WebSocketConnection
	running     atomic.Bool

	// Metrics
	connectionsTotal   atomic.Uint64
	messagesReceived   atomic.Uint64
	messagesSent       atomic.Uint64
	activeConnections  atomic.Int32
}

// WebSocketConnection represents a WebSocket client connection
type WebSocketConnection struct {
	ID       string
	Conn     *websocket.Conn
	LastSeen time.Time
	UserID   string
	RoomID   string
	mu       sync.Mutex
}

// NewWebSocketServer creates a new WebSocket server
func NewWebSocketServer(addr string, logger *zap.Logger) (*WebSocketServer, error) {
	return &WebSocketServer{
		addr:   addr,
		logger: logger,
		upgrader: websocket.Upgrader{
			ReadBufferSize:  1024,
			WriteBufferSize: 1024,
			CheckOrigin: func(r *http.Request) bool {
				// Allow connections from any origin in development
				// In production, implement proper origin checking
				return true
			},
		},
	}, nil
}

// Start begins the WebSocket server
func (ws *WebSocketServer) Start(ctx context.Context) error {
	ws.running.Store(true)
	defer ws.running.Store(false)

	mux := http.NewServeMux()
	mux.HandleFunc("/ws", ws.handleWebSocket)
	mux.HandleFunc("/lobby", ws.handleWebSocket) // Alternative endpoint

	ws.server = &http.Server{
		Addr:    ws.addr,
		Handler: mux,
	}

	ws.logger.Info("WebSocket server starting", zap.String("addr", ws.addr))

	// Start cleanup routine
	go ws.cleanupRoutine(ctx)

	err := ws.server.ListenAndServe()
	if err != nil && err != http.ErrServerClosed {
		return fmt.Errorf("WebSocket server failed: %w", err)
	}

	return nil
}

// handleWebSocket handles WebSocket upgrade requests
func (ws *WebSocketServer) handleWebSocket(w http.ResponseWriter, r *http.Request) {
	conn, err := ws.upgrader.Upgrade(w, r, nil)
	if err != nil {
		ws.logger.Error("WebSocket upgrade failed", zap.Error(err))
		return
	}

	connection := &WebSocketConnection{
		ID:       fmt.Sprintf("ws_%d", time.Now().UnixNano()),
		Conn:     conn,
		LastSeen: time.Now(),
	}

	ws.connections.Store(connection.ID, connection)
	ws.connectionsTotal.Add(1)
	ws.activeConnections.Add(1)

	ws.logger.Info("WebSocket connection established",
		zap.String("connection_id", connection.ID),
		zap.String("remote_addr", r.RemoteAddr))

	// Handle connection in goroutine
	go ws.handleConnection(ctx.Background(), connection)
}

// handleConnection processes messages from a WebSocket connection
func (ws *WebSocketServer) handleConnection(ctx context.Context, conn *WebSocketConnection) {
	defer func() {
		conn.Conn.Close()
		ws.connections.Delete(conn.ID)
		ws.activeConnections.Add(-1)
		ws.logger.Info("WebSocket connection closed", zap.String("connection_id", conn.ID))
	}()

	// Set read deadline
	conn.Conn.SetReadDeadline(time.Now().Add(60 * time.Second))

	for {
		select {
		case <-ctx.Done():
			return
		default:
			messageType, data, err := conn.Conn.ReadMessage()
			if err != nil {
				if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
					ws.logger.Error("WebSocket read error", zap.Error(err), zap.String("connection_id", conn.ID))
				}
				return
			}

			conn.LastSeen = time.Now()
			ws.messagesReceived.Add(1)

			// Handle different message types
			switch messageType {
			case websocket.TextMessage:
				ws.handleTextMessage(conn, data)
			case websocket.BinaryMessage:
				ws.handleBinaryMessage(conn, data)
			case websocket.PingMessage:
				ws.handlePing(conn)
			case websocket.PongMessage:
				// Pong received, update last seen
			}
		}
	}
}

// handleTextMessage processes text messages (JSON)
func (ws *WebSocketServer) handleTextMessage(conn *WebSocketConnection, data []byte) {
	// Parse JSON message
	// This would contain lobby chat, room management, etc.
	ws.logger.Debug("Received text message",
		zap.String("connection_id", conn.ID),
		zap.String("message", string(data)))

	// Echo message back for now (replace with proper message handling)
	ws.sendMessage(conn, websocket.TextMessage, data)
}

// handleBinaryMessage processes binary messages (protobuf)
func (ws *WebSocketServer) handleBinaryMessage(conn *WebSocketConnection, data []byte) {
	// Parse protobuf message
	// This would contain structured lobby/game messages
	ws.logger.Debug("Received binary message",
		zap.String("connection_id", conn.ID),
		zap.Int("size", len(data)))

	// Process protobuf message here
	// For now, just acknowledge
	ack := []byte("ACK")
	ws.sendMessage(conn, websocket.BinaryMessage, ack)
}

// handlePing responds to ping messages
func (ws *WebSocketServer) handlePing(conn *WebSocketConnection) {
	conn.mu.Lock()
	defer conn.mu.Unlock()

	if err := conn.Conn.WriteMessage(websocket.PongMessage, []byte{}); err != nil {
		ws.logger.Error("Failed to send pong", zap.Error(err), zap.String("connection_id", conn.ID))
	}
}

// sendMessage sends a message to a WebSocket connection
func (ws *WebSocketServer) sendMessage(conn *WebSocketConnection, messageType int, data []byte) {
	conn.mu.Lock()
	defer conn.mu.Unlock()

	if err := conn.Conn.WriteMessage(messageType, data); err != nil {
		ws.logger.Error("Failed to send WebSocket message",
			zap.Error(err),
			zap.String("connection_id", conn.ID))
		return
	}

	ws.messagesSent.Add(1)
}

// BroadcastMessage sends a message to all connections in a room
func (ws *WebSocketServer) BroadcastMessage(roomID string, messageType int, data []byte) {
	ws.connections.Range(func(key, value interface{}) bool {
		conn := value.(*WebSocketConnection)
		if conn.RoomID == roomID {
			ws.sendMessage(conn, messageType, data)
		}
		return true
	})
}

// SendToUser sends a message to a specific user
func (ws *WebSocketServer) SendToUser(userID string, messageType int, data []byte) {
	ws.connections.Range(func(key, value interface{}) bool {
		conn := value.(*WebSocketConnection)
		if conn.UserID == userID {
			ws.sendMessage(conn, messageType, data)
			return false // Stop iteration after finding user
		}
		return true
	})
}

// cleanupRoutine removes inactive connections
func (ws *WebSocketServer) cleanupRoutine(ctx context.Context) {
	ticker := time.NewTicker(30 * time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
			ws.cleanupInactiveConnections()
		}
	}
}

// cleanupInactiveConnections closes connections that haven't been seen for 5 minutes
func (ws *WebSocketServer) cleanupInactiveConnections() {
	cutoff := time.Now().Add(-5 * time.Minute)
	removed := 0

	ws.connections.Range(func(key, value interface{}) bool {
		conn := value.(*WebSocketConnection)
		if conn.LastSeen.Before(cutoff) {
			conn.Conn.Close()
			ws.connections.Delete(key)
			removed++
		}
		return true
	})

	if removed > 0 {
		ws.activeConnections.Add(-int32(removed))
		ws.logger.Info("Cleaned up inactive WebSocket connections", zap.Int("removed", removed))
	}
}

// GetStats returns server statistics
func (ws *WebSocketServer) GetStats() map[string]interface{} {
	return map[string]interface{}{
		"connections_total":    ws.connectionsTotal.Load(),
		"messages_received":    ws.messagesReceived.Load(),
		"messages_sent":        ws.messagesSent.Load(),
		"active_connections":   ws.activeConnections.Load(),
	}
}
