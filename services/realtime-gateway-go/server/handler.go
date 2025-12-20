// Package server Issue: #141889273
package server

import (
	"fmt"
	"sync"
	"time"

	"github.com/gorilla/websocket"
)

// GatewayHandler manages WebSocket connections and game state broadcasting
type GatewayHandler struct {
	tickRate               int
	gameStateMgr           *GameStateManager
	sessionMgr             SessionManagerInterface
	serverConn             *websocket.Conn
	serverConnMu           sync.RWMutex
	serverWriteMu          sync.Mutex
	clientConns            map[*websocket.Conn]*ClientConnection
	clientConnsMu          sync.RWMutex
	clientDeltaStates      map[*websocket.Conn]*ClientDeltaState
	deltaStatesMu          sync.RWMutex
	useDeltaCompression    bool
	sessionTokens          map[*websocket.Conn]string
	sessionTokensMu        sync.RWMutex
	banNotifier            *BanNotificationSubscriber
	notificationSubscriber *NotificationSubscriber
	compressor             *AdaptiveCompressor // Issue: #1612 - Adaptive compression
}

func NewGatewayHandler(tickRate int, sessionMgr SessionManagerInterface) *GatewayHandler {
	// Issue: #1612 - Initialize adaptive compressor
	compressor, err := NewAdaptiveCompressor()
	if err != nil {
		// Log error but continue without compression
		GetLogger().WithError(err).Warn("Failed to initialize compressor, continuing without compression")
		compressor = nil
	}

	handler := &GatewayHandler{
		tickRate:            tickRate,
		gameStateMgr:        NewGameStateManager(tickRate),
		sessionMgr:          sessionMgr,
		clientConns:         make(map[*websocket.Conn]*ClientConnection),
		clientDeltaStates:   make(map[*websocket.Conn]*ClientDeltaState),
		useDeltaCompression: true,
		sessionTokens:       make(map[*websocket.Conn]string),
		compressor:          compressor, // Issue: #1612
	}
	return handler
}

func (h *GatewayHandler) SetBanNotifier(notifier *BanNotificationSubscriber) {
	h.banNotifier = notifier
}

func (h *GatewayHandler) GetBanNotifier() *BanNotificationSubscriber {
	return h.banNotifier
}

func (h *GatewayHandler) SetNotificationSubscriber(subscriber *NotificationSubscriber) {
	h.notificationSubscriber = subscriber
}

func (h *GatewayHandler) GetNotificationSubscriber() *NotificationSubscriber {
	return h.notificationSubscriber
}

func (h *GatewayHandler) SetServerConnection(conn *websocket.Conn) {
	h.serverConnMu.Lock()
	defer h.serverConnMu.Unlock()
	h.serverConn = conn
	SetActiveServerConnection(conn != nil)
}

func (h *GatewayHandler) GetServerConnection() *websocket.Conn {
	h.serverConnMu.RLock()
	defer h.serverConnMu.RUnlock()
	return h.serverConn
}

func (h *GatewayHandler) SendToServer(data []byte) error {
	h.serverConnMu.RLock()
	conn := h.serverConn
	h.serverConnMu.RUnlock()

	if conn == nil {
		return fmt.Errorf("server connection not available")
	}

	h.serverWriteMu.Lock()
	defer h.serverWriteMu.Unlock()

	conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
	return conn.WriteMessage(websocket.BinaryMessage, data)
}
