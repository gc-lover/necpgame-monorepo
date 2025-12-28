// Issue: Implement integration-domain-service-go
// WebSocket manager for real-time health monitoring
// Enterprise-grade WebSocket implementation with connection management and broadcasting

package service

import (
	"encoding/json"
	"fmt"
	"net/http"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/integration-domain-service-go/internal/config"
	"github.com/gc-lover/necpgame-monorepo/services/integration-domain-service-go/pkg/models"
	"github.com/gorilla/websocket"
	"go.uber.org/zap"
)

// WebSocketManager manages WebSocket connections for real-time health monitoring
type WebSocketManager struct {
	config      *config.Config
	logger      *zap.Logger
	connections map[string]*WebSocketConnection
	connMu      sync.RWMutex
	upgrader    websocket.Upgrader
	broadcast   chan models.WebSocketHealthMessage
	shutdown    chan struct{}
}

// WebSocketConnection represents a single WebSocket connection
type WebSocketConnection struct {
	ID            string
	Conn          *websocket.Conn
	ConnectedAt   time.Time
	LastHeartbeat time.Time
	RemoteAddr    string
	UserAgent     string
	sendChan      chan models.WebSocketHealthMessage
	closeChan     chan struct{}
}

// NewWebSocketManager creates a new WebSocket manager
func NewWebSocketManager(logger *zap.Logger, cfg *config.Config) *WebSocketManager {
	return &WebSocketManager{
		config:      cfg,
		logger:      logger,
		connections: make(map[string]*WebSocketConnection),
		upgrader: websocket.Upgrader{
			ReadBufferSize:    1024,
			WriteBufferSize:   1024,
			HandshakeTimeout:  10 * time.Second,
			CheckOrigin: func(r *http.Request) bool {
				// In production, implement proper origin checking
				return true
			},
		},
		broadcast: make(chan models.WebSocketHealthMessage, 100),
		shutdown:  make(chan struct{}),
	}
}

// Start starts the WebSocket manager
func (wm *WebSocketManager) Start() {
	go wm.broadcastHandler()
	go wm.cleanupHandler()
}

// HandleConnection handles a new WebSocket connection
func (wm *WebSocketManager) HandleConnection(w http.ResponseWriter, r *http.Request) {
	conn, err := wm.upgrader.Upgrade(w, r, nil)
	if err != nil {
		wm.logger.Error("Failed to upgrade connection to WebSocket", zap.Error(err))
		return
	}

	connection := &WebSocketConnection{
		ID:            generateConnectionID(),
		Conn:          conn,
		ConnectedAt:   time.Now(),
		LastHeartbeat: time.Now(),
		RemoteAddr:    r.RemoteAddr,
		UserAgent:     r.Header.Get("User-Agent"),
		sendChan:      make(chan models.WebSocketHealthMessage, 10),
		closeChan:     make(chan struct{}),
	}

	wm.addConnection(connection)

	wm.logger.Info("New WebSocket connection established",
		zap.String("id", connection.ID),
		zap.String("remote_addr", connection.RemoteAddr))

	// Start connection handlers
	go wm.readHandler(connection)
	go wm.writeHandler(connection)
}

// addConnection adds a connection to the manager
func (wm *WebSocketManager) addConnection(conn *WebSocketConnection) {
	wm.connMu.Lock()
	defer wm.connMu.Unlock()
	wm.connections[conn.ID] = conn
}

// removeConnection removes a connection from the manager
func (wm *WebSocketManager) removeConnection(id string) {
	wm.connMu.Lock()
	defer wm.connMu.Unlock()
	if conn, exists := wm.connections[id]; exists {
		close(conn.closeChan)
		delete(wm.connections, id)
	}
}

// GetActiveConnections returns the number of active connections
func (wm *WebSocketManager) GetActiveConnections() int {
	wm.connMu.RLock()
	defer wm.connMu.RUnlock()
	return len(wm.connections)
}

// Broadcast sends a message to all connected clients
func (wm *WebSocketManager) Broadcast(message models.WebSocketHealthMessage) {
	select {
	case wm.broadcast <- message:
		// Message queued for broadcast
	default:
		wm.logger.Warn("Broadcast channel full, dropping message")
	}
}

// broadcastHandler handles broadcasting messages to all connections
func (wm *WebSocketManager) broadcastHandler() {
	for {
		select {
		case message := <-wm.broadcast:
			wm.connMu.RLock()
			connections := make([]*WebSocketConnection, 0, len(wm.connections))
			for _, conn := range wm.connections {
				connections = append(connections, conn)
			}
			wm.connMu.RUnlock()

			for _, conn := range connections {
				select {
				case conn.sendChan <- message:
					// Message queued for connection
				default:
					wm.logger.Warn("Connection send channel full",
						zap.String("connection_id", conn.ID))
				}
			}

		case <-wm.shutdown:
			return
		}
	}
}

// readHandler handles reading from a WebSocket connection
func (wm *WebSocketManager) readHandler(conn *WebSocketConnection) {
	defer wm.removeConnection(conn.ID)

	conn.Conn.SetReadDeadline(time.Now().Add(wm.config.WebSocketReadTimeout))
	conn.Conn.SetPongHandler(func(string) error {
		conn.LastHeartbeat = time.Now()
		conn.Conn.SetReadDeadline(time.Now().Add(wm.config.WebSocketReadTimeout))
		return nil
	})

	for {
		select {
		case <-conn.closeChan:
			return
		default:
			messageType, _, err := conn.Conn.ReadMessage()
			if err != nil {
				if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
					wm.logger.Error("WebSocket read error",
						zap.String("connection_id", conn.ID),
						zap.Error(err))
				}
				return
			}

			// Handle ping messages
			if messageType == websocket.PingMessage {
				conn.LastHeartbeat = time.Now()
			}
		}
	}
}

// writeHandler handles writing to a WebSocket connection
func (wm *WebSocketManager) writeHandler(conn *WebSocketConnection) {
	ticker := time.NewTicker(wm.config.WebSocketPingInterval)
	defer ticker.Stop()

	for {
		select {
		case message := <-conn.sendChan:
			conn.Conn.SetWriteDeadline(time.Now().Add(wm.config.WebSocketWriteTimeout))

			data, err := json.Marshal(message)
			if err != nil {
				wm.logger.Error("Failed to marshal WebSocket message",
					zap.String("connection_id", conn.ID),
					zap.Error(err))
				continue
			}

			if err := conn.Conn.WriteMessage(websocket.TextMessage, data); err != nil {
				wm.logger.Error("Failed to write WebSocket message",
					zap.String("connection_id", conn.ID),
					zap.Error(err))
				return
			}

		case <-ticker.C:
			conn.Conn.SetWriteDeadline(time.Now().Add(wm.config.WebSocketWriteTimeout))
			if err := conn.Conn.WriteMessage(websocket.PingMessage, nil); err != nil {
				wm.logger.Error("Failed to send ping",
					zap.String("connection_id", conn.ID),
					zap.Error(err))
				return
			}

		case <-conn.closeChan:
			conn.Conn.SetWriteDeadline(time.Now().Add(time.Second))
			conn.Conn.WriteMessage(websocket.CloseMessage, []byte{})
			conn.Conn.Close()
			return
		}
	}
}

// cleanupHandler periodically cleans up dead connections
func (wm *WebSocketManager) cleanupHandler() {
	ticker := time.NewTicker(time.Minute)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			wm.connMu.Lock()
			now := time.Now()
			for id, conn := range wm.connections {
				if now.Sub(conn.LastHeartbeat) > 5*time.Minute {
					wm.logger.Info("Cleaning up dead connection",
						zap.String("connection_id", id),
						zap.Duration("inactive_duration", now.Sub(conn.LastHeartbeat)))
					close(conn.closeChan)
					delete(wm.connections, id)
				}
			}
			wm.connMu.Unlock()

		case <-wm.shutdown:
			return
		}
	}
}

// Shutdown gracefully shuts down the WebSocket manager
func (wm *WebSocketManager) Shutdown() {
	close(wm.shutdown)

	wm.connMu.Lock()
	for _, conn := range wm.connections {
		close(conn.closeChan)
	}
	wm.connections = nil
	wm.connMu.Unlock()
}

// generateConnectionID generates a unique connection ID
func generateConnectionID() string {
	return fmt.Sprintf("ws_%d_%d", time.Now().Unix(), time.Now().UnixNano()%1000)
}


