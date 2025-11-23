package server

import (
	"context"
	"fmt"
	"strings"
	"sync"
	"sync/atomic"
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
	var totalDeltaSize int64

	h.deltaStatesMu.RLock()
	deltaStatesSnapshot := make(map[*websocket.Conn]*ClientDeltaState, len(h.clientDeltaStates))
	for conn, deltaState := range h.clientDeltaStates {
		deltaStatesSnapshot[conn] = deltaState
	}
	h.deltaStatesMu.RUnlock()

	const maxWorkers = 50
	workerCount := clientCount
	if workerCount > maxWorkers {
		workerCount = maxWorkers
	}

	clientChan := make(chan clientInfo, clientCount)
	for _, ci := range clients {
		clientChan <- ci
	}
	close(clientChan)

	for w := 0; w < workerCount; w++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for ci := range clientChan {
				deltaState, exists := deltaStatesSnapshot[ci.conn]
				if !exists {
					continue
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
						continue
					}
				} else if delta != nil {
					data, err = BuildGameStateMessage(delta)
					if err != nil {
						logger.WithError(err).Debug("Failed to build empty delta message")
						deltaState.SetLastState(CopyGameStateData(newState))
						continue
					}
				} else {
					deltaState.SetLastState(CopyGameStateData(newState))
					continue
				}

				atomic.AddInt64(&totalDeltaSize, int64(len(data)))

				ci.clientConn.mu.Lock()
				ci.conn.SetWriteDeadline(deadline)
				if err := ci.conn.WriteMessage(websocket.BinaryMessage, data); err != nil {
					logger.WithError(err).Debug("Failed to broadcast delta to client")
				} else {
					atomic.AddInt64(&successCount, 1)
					deltaState.SetLastState(CopyGameStateData(newState))
				}
				ci.clientConn.mu.Unlock()
			}
		}()
	}

	wg.Wait()

	duration := time.Since(startTime).Seconds()
	RecordGameStateBroadcastDuration(duration)
	RecordGameStateBroadcasted()

	avgDeltaSize := float64(0)
	finalSuccessCount := atomic.LoadInt64(&successCount)
	finalTotalDeltaSize := atomic.LoadInt64(&totalDeltaSize)
	if finalSuccessCount > 0 {
		avgDeltaSize = float64(finalTotalDeltaSize) / float64(finalSuccessCount)
	}

	logger.WithFields(map[string]interface{}{
		"success_count": finalSuccessCount,
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

func (h *GatewayHandler) HandleConnection(ctx context.Context, conn *websocket.Conn) error {
	var playerID string
	var sessionToken string
	
	h.AddClientConnection(conn)
	defer func() {
		logger := GetLogger()
		if playerID != "" {
			h.gameStateMgr.RemovePlayer(playerID)
		}
		if sessionToken != "" && h.sessionMgr != nil {
			h.sessionMgr.DisconnectSession(ctx, sessionToken)
		}
		h.RemoveClientConnection(conn)
		h.sessionTokensMu.Lock()
		delete(h.sessionTokens, conn)
		h.sessionTokensMu.Unlock()
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
		if sessionToken != "" && h.sessionMgr != nil {
			h.sessionMgr.UpdateHeartbeat(ctx, sessionToken)
		}
		return nil
	})

	heartbeatTicker := time.NewTicker(30 * time.Second)
	defer heartbeatTicker.Stop()

	go func() {
		for {
			select {
			case <-ctx.Done():
				return
			case <-heartbeatTicker.C:
				if conn != nil {
					conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
					if err := conn.WriteMessage(websocket.PingMessage, nil); err != nil {
						return
					}
				}
			}
		}
	}()
	
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
					
					if h.sessionMgr != nil {
						ipAddress := conn.RemoteAddr().String()
						userAgent := ""
						
						existingSession, _ := h.sessionMgr.GetSessionByPlayerID(ctx, playerID)
						if existingSession != nil && existingSession.Status == SessionStatusActive {
							h.sessionMgr.DisconnectSession(ctx, existingSession.Token)
						}
						
						newSession, err := h.sessionMgr.CreateSession(ctx, playerID, ipAddress, userAgent, nil)
						if err == nil && newSession != nil {
							sessionToken = newSession.Token
							h.sessionTokensMu.Lock()
							h.sessionTokens[conn] = sessionToken
							h.sessionTokensMu.Unlock()
						}
					}
				}
				
				if sessionToken != "" && h.sessionMgr != nil {
					h.sessionMgr.UpdateHeartbeat(ctx, sessionToken)
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

