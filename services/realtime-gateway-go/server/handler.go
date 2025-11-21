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
	serverConn       *websocket.Conn
	serverConnMu     sync.RWMutex
	clientConns      map[*websocket.Conn]*ClientConnection
	clientConnsMu    sync.RWMutex
	clientDeltaStates map[*websocket.Conn]*ClientDeltaState
	deltaStatesMu    sync.RWMutex
	useDeltaCompression bool
}

func NewGatewayHandler(tickRate int) *GatewayHandler {
	return &GatewayHandler{
		tickRate:            tickRate,
		gameStateMgr:        NewGameStateManager(tickRate),
		clientConns:         make(map[*websocket.Conn]*ClientConnection),
		clientDeltaStates:   make(map[*websocket.Conn]*ClientDeltaState),
		useDeltaCompression: true,
	}
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

func (h *GatewayHandler) BroadcastToClients(data []byte) {
	if h.useDeltaCompression {
		gameState, err := ParseGameStateMessage(data)
		if err != nil {
			logger := GetLogger()
			logger.WithError(err).Warn("Failed to parse GameState for delta compression, falling back to full broadcast")
			h.BroadcastToClientsParallel(data)
			return
		}
		h.BroadcastGameStateWithDelta(gameState)
	} else {
		h.BroadcastToClientsParallel(data)
	}
}

func (h *GatewayHandler) BroadcastGameStateWithDelta(newState *GameStateData) {
	startTime := time.Now()
	h.clientConnsMu.RLock()
	clientCount := len(h.clientConns)
	if clientCount == 0 {
		h.clientConnsMu.RUnlock()
		logger := GetLogger()
		logger.Warn("No clients connected, GameState not broadcasted")
		return
	}

	clients := make([]clientInfo, 0, clientCount)
	for conn, clientConn := range h.clientConns {
		clients = append(clients, clientInfo{conn: conn, clientConn: clientConn})
	}
	h.clientConnsMu.RUnlock()

	logger := GetLogger()
	logger.WithField("client_count", clientCount).WithField("tick", newState.Tick).Info("Broadcasting GameState with delta compression")

	deadline := h.getWriteDeadline()
	var wg sync.WaitGroup
	var successCount int64
	var mu sync.Mutex
	totalDeltaSize := int64(0)

	for i := range clients {
		wg.Add(1)
		go func(idx int) {
			ci := clients[idx]
			defer wg.Done()

			h.deltaStatesMu.RLock()
			deltaState, exists := h.clientDeltaStates[ci.conn]
			h.deltaStatesMu.RUnlock()

			if !exists {
				return
			}

			oldState := deltaState.GetLastState()
			delta := CalculateDelta(oldState, newState)
			defer func() {
				if delta != nil {
					PutGameStateToPool(delta)
				}
			}()

			var data []byte
			var err error
			if delta != nil && len(delta.Entities) > 0 {
				data, err = BuildGameStateMessage(delta)
				if err != nil {
					logger.WithError(err).Debug("Failed to build delta message")
					return
				}
			} else if delta != nil {
				// Дельта пустая, но Tick изменился - отправляем минимальное обновление
				// Это важно для синхронизации времени и предотвращения рассинхронизации
				data, err = BuildGameStateMessage(delta)
				if err != nil {
					logger.WithError(err).Debug("Failed to build empty delta message")
					deltaState.SetLastState(CopyGameStateData(newState))
					return
				}
			} else {
				// Дельта nil - это означает, что Tick не изменился, пропускаем
				deltaState.SetLastState(CopyGameStateData(newState))
				return
			}

			mu.Lock()
			totalDeltaSize += int64(len(data))
			mu.Unlock()

			ci.clientConn.mu.Lock()
			ci.conn.SetWriteDeadline(deadline)
			if err := ci.conn.WriteMessage(websocket.BinaryMessage, data); err != nil {
				logger.WithError(err).Debug("Failed to broadcast delta to client")
			} else {
				mu.Lock()
				successCount++
				mu.Unlock()

				deltaState.SetLastState(CopyGameStateData(newState))
			}
			ci.clientConn.mu.Unlock()
		}(i)
	}

	wg.Wait()

	duration := time.Since(startTime).Seconds()
	RecordGameStateBroadcastDuration(duration)
	RecordGameStateBroadcasted()

	avgDeltaSize := float64(0)
	if successCount > 0 {
		avgDeltaSize = float64(totalDeltaSize) / float64(successCount)
	}

	logger.WithFields(map[string]interface{}{
		"success_count": successCount,
		"total_clients": clientCount,
		"duration_ms":   duration * 1000,
		"avg_delta_size": avgDeltaSize,
	}).Info("Broadcasted GameState with delta compression to clients")
}

func (h *GatewayHandler) BroadcastToClientsParallel(data []byte) {
	startTime := time.Now()
	h.clientConnsMu.RLock()
	clientCount := len(h.clientConns)
	if clientCount == 0 {
		h.clientConnsMu.RUnlock()
		logger := GetLogger()
		logger.Warn("No clients connected, GameState not broadcasted")
		return
	}
	
	clients := make([]*ClientConnection, 0, clientCount)
	for _, clientConn := range h.clientConns {
		clients = append(clients, clientConn)
	}
	h.clientConnsMu.RUnlock()
	
	logger := GetLogger()
	logger.WithField("client_count", clientCount).WithField("data_len", len(data)).Info("Broadcasting GameState to clients")
	
	RecordMessageSize("gamestate", len(data))
	
	deadline := h.getWriteDeadline()
	var wg sync.WaitGroup
	var successCount int64
	var mu sync.Mutex
	
	for _, clientConn := range clients {
		wg.Add(1)
		go func(cc *ClientConnection) {
			defer wg.Done()
			cc.mu.Lock()
			defer cc.mu.Unlock()
			cc.conn.SetWriteDeadline(deadline)
			if err := cc.conn.WriteMessage(websocket.BinaryMessage, data); err != nil {
				logger.WithError(err).Debug("Failed to broadcast to client")
			} else {
				mu.Lock()
				successCount++
				mu.Unlock()
			}
		}(clientConn)
	}
	
	wg.Wait()
	
	duration := time.Since(startTime).Seconds()
	RecordGameStateBroadcastDuration(duration)
	RecordGameStateBroadcasted()
	
	logger.WithField("success_count", successCount).WithField("total_clients", clientCount).WithField("duration_ms", duration*1000).Info("Broadcasted GameState to clients")
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
				RecordPlayerInputReceived()
				RecordMessageSize("player_input", len(data))
				
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
					RecordPlayerInputForwarded()
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

