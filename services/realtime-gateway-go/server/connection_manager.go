// Issue: #141889273
package server

import (
	"sync"

	"github.com/gorilla/websocket"
)

// ClientConnection represents a client WebSocket connection with mutex protection
type ClientConnection struct {
	conn *websocket.Conn
	mu   sync.Mutex
}

type clientInfo struct {
	conn       *websocket.Conn
	clientConn *ClientConnection
}

// AddClientConnection adds a new client connection to the handler
func (h *GatewayHandler) AddClientConnection(conn *websocket.Conn) {
	h.clientConnsMu.Lock()
	h.clientConns[conn] = &ClientConnection{conn: conn}
	h.clientConnsMu.Unlock()
	
	h.deltaStatesMu.Lock()
	h.clientDeltaStates[conn] = NewClientDeltaState()
	h.deltaStatesMu.Unlock()
	
	SetActiveClients(float64(len(h.clientConns)))
}

// RemoveClientConnection removes a client connection from the handler
// Issue: #1410
func (h *GatewayHandler) RemoveClientConnection(conn *websocket.Conn) {
	h.clientConnsMu.Lock()
	clientConn, exists := h.clientConns[conn]
	delete(h.clientConns, conn)
	h.clientConnsMu.Unlock()
	
	h.deltaStatesMu.Lock()
	delete(h.clientDeltaStates, conn)
	h.deltaStatesMu.Unlock()
	
	// Закрываем соединение, если оно существует
	if exists && clientConn != nil {
		clientConn.mu.Lock()
		if conn != nil {
			// Отправляем close message перед закрытием
			conn.WriteMessage(websocket.CloseMessage, websocket.FormatCloseMessage(websocket.CloseGoingAway, "Connection closed"))
			conn.Close()
		}
		clientConn.mu.Unlock()
	} else if conn != nil {
		// Если clientConn не найден, все равно закрываем соединение
		conn.WriteMessage(websocket.CloseMessage, websocket.FormatCloseMessage(websocket.CloseGoingAway, "Connection closed"))
		conn.Close()
	}
	
	SetActiveClients(float64(len(h.clientConns)))
}




























