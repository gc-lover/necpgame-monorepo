package server

import (
	"sync"
	"time"

	"github.com/gorilla/websocket"
	"unicode"
)

func isValidPlayerID(id string) bool {
	if len(id) == 0 || len(id) > 20 {
		return false
	}
	if len(id) >= 2 && id[0] == 'p' {
		for i := 1; i < len(id); i++ {
			if !((id[i] >= '0' && id[i] <= '9') || (id[i] >= 'a' && id[i] <= 'f') || (id[i] >= 'A' && id[i] <= 'F')) {
				return false
			}
		}
		return true
	}
	for _, r := range id {
		if !unicode.IsPrint(r) && r != '\n' && r != '\r' && r != '\t' {
			return false
		}
	}
	return true
}

type ClientConnection struct {
	conn *websocket.Conn
	mu   sync.Mutex
}

type clientInfo struct {
	conn       *websocket.Conn
	clientConn *ClientConnection
}

type GatewayHandler struct {
	tickRate         int
	gameStateMgr     *GameStateManager
	sessionMgr       SessionManagerInterface
	serverConn       *websocket.Conn
	serverConnMu     sync.RWMutex
	serverWriteMu    sync.Mutex
	clientConns      map[*websocket.Conn]*ClientConnection
	clientConnsMu    sync.RWMutex
	clientDeltaStates map[*websocket.Conn]*ClientDeltaState
	deltaStatesMu    sync.RWMutex
	useDeltaCompression bool
	sessionTokens    map[*websocket.Conn]string
	sessionTokensMu  sync.RWMutex
	banNotifier      *BanNotificationSubscriber
	notificationSubscriber *NotificationSubscriber
}

func NewGatewayHandler(tickRate int, sessionMgr SessionManagerInterface) *GatewayHandler {
	handler := &GatewayHandler{
		tickRate:            tickRate,
		gameStateMgr:        NewGameStateManager(tickRate),
		sessionMgr:          sessionMgr,
		clientConns:         make(map[*websocket.Conn]*ClientConnection),
		clientDeltaStates:   make(map[*websocket.Conn]*ClientDeltaState),
		useDeltaCompression: true,
		sessionTokens:       make(map[*websocket.Conn]string),
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

func (h *GatewayHandler) AddClientConnection(conn *websocket.Conn) {
	h.clientConnsMu.Lock()
	h.clientConns[conn] = &ClientConnection{conn: conn}
	h.clientConnsMu.Unlock()
	
	h.deltaStatesMu.Lock()
	h.clientDeltaStates[conn] = NewClientDeltaState()
	h.deltaStatesMu.Unlock()
	
	SetActiveClients(float64(len(h.clientConns)))
}

func (h *GatewayHandler) RemoveClientConnection(conn *websocket.Conn) {
	h.clientConnsMu.Lock()
	delete(h.clientConns, conn)
	h.clientConnsMu.Unlock()
	
	h.deltaStatesMu.Lock()
	delete(h.clientDeltaStates, conn)
	h.deltaStatesMu.Unlock()
	
	SetActiveClients(float64(len(h.clientConns)))
}

func (h *GatewayHandler) getWriteDeadline() time.Time {
	if h.tickRate > 0 {
		return time.Now().Add(time.Duration(1000/h.tickRate) * time.Millisecond)
	}
	return time.Now().Add(16 * time.Millisecond)
}
