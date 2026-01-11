// WebSocket server for real-time seasonal events
// Issue: #1506
package server

import (
	"encoding/json"
	"net/http"
	"sync"
	"time"

	"github.com/gorilla/websocket"
	"go.uber.org/zap"
)

// WebSocket message types for seasonal events
const (
	// Outgoing messages (server -> client)
	MsgProgressUpdate     = "progress_update"
	MsgChallengeUnlocked  = "challenge_unlocked"
	MsgSeasonStart        = "season_start"
	MsgSeasonEnd          = "season_end"
	MsgLeaderboardUpdate  = "leaderboard_update"
	MsgRewardAvailable    = "reward_available"
	MsgAchievementUnlock  = "achievement_unlock"
	MsgEventBroadcast     = "event_broadcast"

	// Incoming messages (client -> server)
	MsgSubscribeSeason    = "subscribe_season"
	MsgUnsubscribeSeason  = "unsubscribe_season"
	MsgPing               = "ping"
)

// WebSocket connection with metadata
type WSConnection struct {
	conn       *websocket.Conn
	playerID   string
	seasons    map[string]bool // subscribed seasons
	sendChan   chan []byte
	closeChan  chan struct{}
	lastPing   time.Time
}

// WebSocketServer manages WebSocket connections and broadcasting
type WebSocketServer struct {
	upgrader     websocket.Upgrader
	connections  map[*WSConnection]bool
	connectionsMu sync.RWMutex
	logger       *zap.Logger
	broadcast    chan *WSEvent
	shutdown     chan struct{}
}

// WSEvent represents a WebSocket event to broadcast
type WSEvent struct {
	Type         string      `json:"type"`
	SeasonID     string      `json:"season_id,omitempty"`
	PlayerID     string      `json:"player_id,omitempty"`
	ChallengeID  string      `json:"challenge_id,omitempty"`
	Data         interface{} `json:"data"`
	Timestamp    time.Time   `json:"timestamp"`
}

// WSMessage represents incoming WebSocket message
type WSMessage struct {
	Type    string                 `json:"type"`
	SeasonID string                 `json:"season_id,omitempty"`
	Data    map[string]interface{} `json:"data,omitempty"`
}

// NewWebSocketServer creates a new WebSocket server
func NewWebSocketServer(logger *zap.Logger) *WebSocketServer {
	return &WebSocketServer{
		upgrader: websocket.Upgrader{
			ReadBufferSize:  1024,
			WriteBufferSize: 1024,
			CheckOrigin: func(r *http.Request) bool {
				// TODO: Implement proper origin checking for production
				return true // Allow all origins for development
			},
			HandshakeTimeout: 10 * time.Second,
		},
		connections: make(map[*WSConnection]bool),
		logger:      logger,
		broadcast:   make(chan *WSEvent, 1000), // Buffered channel for performance
		shutdown:    make(chan struct{}),
	}
}

// HandleWebSocket handles WebSocket upgrade and connection management
func (ws *WebSocketServer) HandleWebSocket(w http.ResponseWriter, r *http.Request) {
	// Extract player ID from query parameters or headers
	playerID := r.URL.Query().Get("player_id")
	if playerID == "" {
		playerID = r.Header.Get("X-Player-ID")
	}
	if playerID == "" {
		http.Error(w, "Player ID required", http.StatusBadRequest)
		return
	}

	// Upgrade HTTP connection to WebSocket
	conn, err := ws.upgrader.Upgrade(w, r, nil)
	if err != nil {
		ws.logger.Error("Failed to upgrade connection", zap.Error(err))
		return
	}

	// Create connection wrapper
	connection := &WSConnection{
		conn:       conn,
		playerID:   playerID,
		seasons:    make(map[string]bool),
		sendChan:   make(chan []byte, 256),
		closeChan:  make(chan struct{}),
		lastPing:   time.Now(),
	}

	// Register connection
	ws.connectionsMu.Lock()
	ws.connections[connection] = true
	ws.connectionsMu.Unlock()

	ws.logger.Info("WebSocket connection established",
		zap.String("player_id", playerID),
		zap.String("remote_addr", r.RemoteAddr),
	)

	// Start connection handlers
	go ws.handleConnection(connection)
	go ws.writePump(connection)
}

// handleConnection processes incoming messages from a WebSocket connection
func (ws *WebSocketServer) handleConnection(conn *WSConnection) {
	defer func() {
		ws.removeConnection(conn)
	}()

	// Set read deadline for connection health
	conn.conn.SetReadDeadline(time.Now().Add(60 * time.Second))

	for {
		select {
		case <-conn.closeChan:
			return
		default:
			var msg WSMessage
			err := conn.conn.ReadJSON(&msg)
			if err != nil {
				if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
					ws.logger.Error("WebSocket read error", zap.Error(err))
				}
				return
			}

			ws.handleMessage(conn, &msg)
		}
	}
}

// writePump handles outgoing messages to a WebSocket connection
func (ws *WebSocketServer) writePump(conn *WSConnection) {
	ticker := time.NewTicker(54 * time.Second) // Send pings slightly before read deadline
	defer ticker.Stop()

	for {
		select {
		case <-conn.closeChan:
			return
		case message, ok := <-conn.sendChan:
			conn.conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
			if !ok {
				conn.conn.WriteMessage(websocket.CloseMessage, []byte{})
				return
			}

			if err := conn.conn.WriteMessage(websocket.TextMessage, message); err != nil {
				ws.logger.Error("WebSocket write error", zap.Error(err))
				return
			}
		case <-ticker.C:
			conn.conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
			if err := conn.conn.WriteMessage(websocket.PingMessage, nil); err != nil {
				return
			}
		}
	}
}

// handleMessage processes incoming WebSocket messages
func (ws *WebSocketServer) handleMessage(conn *WSConnection, msg *WSMessage) {
	switch msg.Type {
	case MsgSubscribeSeason:
		if msg.SeasonID != "" {
			conn.seasons[msg.SeasonID] = true
			ws.logger.Debug("Player subscribed to season",
				zap.String("player_id", conn.playerID),
				zap.String("season_id", msg.SeasonID),
			)
		}

	case MsgUnsubscribeSeason:
		if msg.SeasonID != "" {
			delete(conn.seasons, msg.SeasonID)
			ws.logger.Debug("Player unsubscribed from season",
				zap.String("player_id", conn.playerID),
				zap.String("season_id", msg.SeasonID),
			)
		}

	case MsgPing:
		conn.lastPing = time.Now()
		// Send pong response
		pongMsg := map[string]interface{}{
			"type":      "pong",
			"timestamp": time.Now().Unix(),
		}
		if data, err := json.Marshal(pongMsg); err == nil {
			select {
			case conn.sendChan <- data:
			default:
				ws.logger.Warn("Send channel full, dropping pong message")
			}
		}

	default:
		ws.logger.Warn("Unknown message type received",
			zap.String("type", msg.Type),
			zap.String("player_id", conn.playerID),
		)
	}
}

// BroadcastEvent broadcasts an event to all subscribed connections
func (ws *WebSocketServer) BroadcastEvent(event *WSEvent) {
	select {
	case ws.broadcast <- event:
	default:
		ws.logger.Warn("Broadcast channel full, dropping event",
			zap.String("event_type", event.Type))
	}
}

// BroadcastToSeason broadcasts an event to players subscribed to a specific season
func (ws *WebSocketServer) BroadcastToSeason(seasonID string, event *WSEvent) {
	event.SeasonID = seasonID
	event.Timestamp = time.Now()

	ws.connectionsMu.RLock()
	defer ws.connectionsMu.RUnlock()

	eventData, err := json.Marshal(event)
	if err != nil {
		ws.logger.Error("Failed to marshal event", zap.Error(err))
		return
	}

	broadcastCount := 0
	for conn := range ws.connections {
		if conn.seasons[seasonID] {
			select {
			case conn.sendChan <- eventData:
				broadcastCount++
			default:
				// Connection send channel full, remove connection
				close(conn.closeChan)
			}
		}
	}

	ws.logger.Debug("Event broadcast completed",
		zap.String("event_type", event.Type),
		zap.String("season_id", seasonID),
		zap.Int("connections_reached", broadcastCount),
	)
}

// BroadcastToPlayer broadcasts an event to a specific player
func (ws *WebSocketServer) BroadcastToPlayer(playerID string, event *WSEvent) {
	event.PlayerID = playerID
	event.Timestamp = time.Now()

	ws.connectionsMu.RLock()
	defer ws.connectionsMu.RUnlock()

	eventData, err := json.Marshal(event)
	if err != nil {
		ws.logger.Error("Failed to marshal event", zap.Error(err))
		return
	}

	for conn := range ws.connections {
		if conn.playerID == playerID {
			select {
			case conn.sendChan <- eventData:
				return // Found and sent to player
			default:
				// Connection send channel full, remove connection
				close(conn.closeChan)
			}
		}
	}
}

// broadcastLoop processes broadcast events
func (ws *WebSocketServer) broadcastLoop() {
	for {
		select {
		case <-ws.shutdown:
			return
		case event := <-ws.broadcast:
			if event.SeasonID != "" {
				ws.BroadcastToSeason(event.SeasonID, event)
			} else if event.PlayerID != "" {
				ws.BroadcastToPlayer(event.PlayerID, event)
			} else {
				// Broadcast to all connections
				ws.broadcastToAll(event)
			}
		}
	}
}

// broadcastToAll broadcasts an event to all connected clients
func (ws *WebSocketServer) broadcastToAll(event *WSEvent) {
	event.Timestamp = time.Now()

	ws.connectionsMu.RLock()
	defer ws.connectionsMu.RUnlock()

	eventData, err := json.Marshal(event)
	if err != nil {
		ws.logger.Error("Failed to marshal broadcast event", zap.Error(err))
		return
	}

	for conn := range ws.connections {
		select {
		case conn.sendChan <- eventData:
		default:
			close(conn.closeChan)
		}
	}
}

// removeConnection cleans up a disconnected WebSocket connection
func (ws *WebSocketServer) removeConnection(conn *WSConnection) {
	ws.connectionsMu.Lock()
	delete(ws.connections, conn)
	ws.connectionsMu.Unlock()

	close(conn.closeChan)
	conn.conn.Close()

	ws.logger.Info("WebSocket connection removed",
		zap.String("player_id", conn.playerID),
		zap.Int("remaining_connections", len(ws.connections)),
	)
}

// StartBroadcastLoop starts the broadcast processing goroutine
func (ws *WebSocketServer) StartBroadcastLoop() {
	go ws.broadcastLoop()
	go ws.cleanupLoop()
}

// cleanupLoop periodically cleans up stale connections
func (ws *WebSocketServer) cleanupLoop() {
	ticker := time.NewTicker(30 * time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-ws.shutdown:
			return
		case <-ticker.C:
			ws.connectionsMu.Lock()
			now := time.Now()
			for conn := range ws.connections {
				// Remove connections with no recent ping (5 minutes)
				if now.Sub(conn.lastPing) > 5*time.Minute {
					close(conn.closeChan)
					delete(ws.connections, conn)
					conn.conn.Close()
				}
			}
			ws.connectionsMu.Unlock()
		}
	}
}

// Shutdown gracefully shuts down the WebSocket server
func (ws *WebSocketServer) Shutdown() {
	close(ws.shutdown)

	ws.connectionsMu.Lock()
	for conn := range ws.connections {
		close(conn.closeChan)
		conn.conn.Close()
	}
	ws.connectionsMu.Unlock()
}

// GetConnectionCount returns the current number of active connections
func (ws *WebSocketServer) GetConnectionCount() int {
	ws.connectionsMu.RLock()
	defer ws.connectionsMu.RUnlock()
	return len(ws.connections)
}

// GetSeasonSubscriptions returns connection count per season
func (ws *WebSocketServer) GetSeasonSubscriptions() map[string]int {
	ws.connectionsMu.RLock()
	defer ws.connectionsMu.RUnlock()

	subscriptions := make(map[string]int)
	for conn := range ws.connections {
		for seasonID := range conn.seasons {
			subscriptions[seasonID]++
		}
	}

	return subscriptions
}