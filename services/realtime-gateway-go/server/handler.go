package server

import (
	"context"
	"fmt"
	"strings"
	"sync"
	"time"
	"unicode"

	"github.com/gorilla/websocket"
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

type GatewayHandler struct {
	tickRate      int
	gameStateMgr  *GameStateManager
	serverConn    *websocket.Conn
	serverConnMu  sync.RWMutex
	clientConns   map[*websocket.Conn]bool
	clientConnsMu sync.RWMutex
}

func NewGatewayHandler(tickRate int) *GatewayHandler {
	return &GatewayHandler{
		tickRate:     tickRate,
		gameStateMgr: NewGameStateManager(tickRate),
		clientConns:  make(map[*websocket.Conn]bool),
	}
}

func (h *GatewayHandler) SetServerConnection(conn *websocket.Conn) {
	h.serverConnMu.Lock()
	defer h.serverConnMu.Unlock()
	h.serverConn = conn
}

func (h *GatewayHandler) GetServerConnection() *websocket.Conn {
	h.serverConnMu.RLock()
	defer h.serverConnMu.RUnlock()
	return h.serverConn
}

func (h *GatewayHandler) AddClientConnection(conn *websocket.Conn) {
	h.clientConnsMu.Lock()
	defer h.clientConnsMu.Unlock()
	h.clientConns[conn] = true
}

func (h *GatewayHandler) RemoveClientConnection(conn *websocket.Conn) {
	h.clientConnsMu.Lock()
	defer h.clientConnsMu.Unlock()
	delete(h.clientConns, conn)
}

func (h *GatewayHandler) BroadcastToClients(data []byte) {
	h.clientConnsMu.RLock()
	clientCount := len(h.clientConns)
	h.clientConnsMu.RUnlock()
	
	logger := GetLogger()
	logger.WithField("client_count", clientCount).WithField("data_len", len(data)).Info("Broadcasting GameState to clients")
	
	if clientCount == 0 {
		logger.Warn("No clients connected, GameState not broadcasted")
		return
	}
	
	h.clientConnsMu.RLock()
	defer h.clientConnsMu.RUnlock()
	
	successCount := 0
	for conn := range h.clientConns {
		conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
		if err := conn.WriteMessage(websocket.BinaryMessage, data); err != nil {
			logger.WithError(err).Warn("Failed to broadcast to client")
		} else {
			successCount++
		}
	}
	
	logger.WithField("success_count", successCount).WithField("total_clients", clientCount).Info("Broadcasted GameState to clients")
}

func (h *GatewayHandler) SendToServer(data []byte) error {
	h.serverConnMu.RLock()
	defer h.serverConnMu.RUnlock()
	
	if h.serverConn == nil {
		return fmt.Errorf("server connection not available")
	}
	
	h.serverConn.SetWriteDeadline(time.Now().Add(10 * time.Second))
	return h.serverConn.WriteMessage(websocket.BinaryMessage, data)
}

func (h *GatewayHandler) HandleConnection(ctx context.Context, conn *websocket.Conn) error {
	var playerID string
	h.AddClientConnection(conn)
	defer func() {
		logger := GetLogger()
		if playerID != "" {
			h.gameStateMgr.RemovePlayer(playerID)
		}
		h.RemoveClientConnection(conn)
		logger.Info("Closing WebSocket connection")
		conn.Close()
	}()

	logger := GetLogger()
	logger.WithField("remote_addr", conn.RemoteAddr().String()).Info("New WebSocket client connection")
	
	h.clientConnsMu.RLock()
	clientCount := len(h.clientConns)
	h.clientConnsMu.RUnlock()
	logger.WithField("total_clients", clientCount).Info("Client connected, total clients")

	conn.SetReadDeadline(time.Now().Add(60 * time.Second))
	conn.SetPongHandler(func(string) error {
		conn.SetReadDeadline(time.Now().Add(60 * time.Second))
		return nil
	})
	
	for {
		select {
		case <-ctx.Done():
			logger.Info("Context cancelled, closing connection")
			return nil
		default:
		}

		startTime := time.Now()
		conn.SetReadDeadline(time.Now().Add(60 * time.Second))
		messageType, data, err := conn.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
				logger.WithError(err).Debug("WebSocket connection closed by client")
				return nil
			}
			if websocket.IsCloseError(err, websocket.CloseNormalClosure, websocket.CloseGoingAway) {
				logger.Debug("WebSocket closed normally")
				return nil
			}
			logger.WithError(err).Debug("WebSocket read error")
			return nil
		}

		if messageType != websocket.BinaryMessage && messageType != websocket.TextMessage {
			continue
		}

		latency := time.Since(startTime).Seconds()
		RecordMessageLatency("read", latency)

		if messageType == websocket.BinaryMessage {
			logger.WithFields(map[string]interface{}{
				"data_len":    len(data),
				"source":      "client_connection",
			}).Debug("HandleConnection: Received binary message from client, attempting to parse as PlayerInput")
			
			playerInput, err := ParseClientMessage(data)
			if err != nil {
				if err.Error() != "no PlayerInput found in message" {
					hexLen := 50
					if len(data) < hexLen {
						hexLen = len(data)
					}
					remoteAddr := conn.RemoteAddr().String()
					
					if len(data) > 100 && !strings.Contains(err.Error(), "PlayerID string length") {
						logger.WithFields(map[string]interface{}{
							"data_len":    len(data),
							"remote_addr": remoteAddr,
							"error_msg":   err.Error(),
							"source":      "client_connection",
						}).Warn("HandleConnection: Large message that doesn't parse as PlayerInput - might be GameState from server that should be handled by handleServerWebSocket")
					}
					
					logger.WithError(err).WithFields(map[string]interface{}{
						"data_len":    len(data),
						"data_hex":     fmt.Sprintf("%x", data[:hexLen]),
						"remote_addr":  remoteAddr,
						"error_msg":    err.Error(),
						"source":       "client_connection",
					}).Error("HandleConnection: Failed to parse PlayerInput from client - TRACING SOURCE")
					
					if strings.Contains(err.Error(), "PlayerID string length") {
						fullHexLen := 200
						if len(data) < fullHexLen {
							fullHexLen = len(data)
						}
						logger.WithFields(map[string]interface{}{
							"remote_addr": remoteAddr,
							"data_len":    len(data),
							"full_hex":     fmt.Sprintf("%x", data[:fullHexLen]),
							"source":       "client_connection",
						}).Error("HandleConnection: LONG PlayerID DETECTED in client message - SOURCE TRACE")
					}
				} else {
					if len(data) > 100 {
						logger.WithField("data_len", len(data)).Warn("HandleConnection: Large message without PlayerInput - might be GameState from server that should be handled by handleServerWebSocket")
					} else {
						logger.WithField("data_len", len(data)).Debug("HandleConnection: Message does not contain PlayerInput")
					}
				}
			} else if playerInput != nil {
				if playerID == "" {
					playerID = playerInput.PlayerID
				}
				
				if len(playerInput.PlayerID) > 20 || !isValidPlayerID(playerInput.PlayerID) {
					hexLen := 50
					if len(data) < hexLen {
						hexLen = len(data)
					}
					logger.WithFields(map[string]interface{}{
						"player_id":      playerInput.PlayerID,
						"player_id_len":  len(playerInput.PlayerID),
						"data_len":       len(data),
						"data_hex":       fmt.Sprintf("%x", data[:hexLen]),
						"tick":           playerInput.Tick,
						"move_x":         playerInput.MoveX,
						"move_y":         playerInput.MoveY,
					}).Warn("Received PlayerInput with suspicious player_id")
				} else {
					logger.WithFields(map[string]interface{}{
						"player_id": playerInput.PlayerID,
						"tick":      playerInput.Tick,
						"move_x":    playerInput.MoveX,
						"move_y":    playerInput.MoveY,
						"shoot":     playerInput.Shoot,
						"aim_x":     playerInput.AimX,
						"aim_y":     playerInput.AimY,
					}).Info("Received PlayerInput")
				}

				h.gameStateMgr.UpdatePlayerInput(playerInput)

				if err := h.SendToServer(data); err != nil {
					logger.WithError(err).Debug("Failed to forward PlayerInput to server")
				} else {
					logger.WithField("player_id", playerInput.PlayerID).Debug("Forwarded PlayerInput to server")
				}
			} else {
				logger.WithField("data_len", len(data)).Warn("ParseClientMessage returned nil without error")
				response := []byte(fmt.Sprintf("Echo: %s", string(data)))
				conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
				if err := conn.WriteMessage(messageType, response); err != nil {
					logger.WithError(err).Error("Failed to write response")
					RecordError("websocket_write")
					return err
				}
			}
		} else {
			logger.WithFields(map[string]interface{}{
				"message_type": messageType,
				"bytes":        len(data),
			}).Debug("Received non-binary message")

			response := []byte(fmt.Sprintf("Echo: %s", string(data)))
			conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
			if err := conn.WriteMessage(messageType, response); err != nil {
				logger.WithError(err).Error("Failed to write response")
				RecordError("websocket_write")
				return err
			}
		}

		conn.SetReadDeadline(time.Now().Add(60 * time.Second))
	}
}

