// Issue: #141889273
package server

import (
	"context"
	"fmt"
	"strings"
	"time"

	"github.com/gorilla/websocket"
)

// HandleConnection handles a WebSocket connection from a client
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
						logger := GetLogger()
						logger.WithError(err).Error("Failed to write WebSocket ping message")
						h.RemoveClientConnection(conn)
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

