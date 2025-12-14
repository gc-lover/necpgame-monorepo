// Issue: #141889273
package server

import (
	"sync"
	"sync/atomic"
	"time"

	"github.com/gorilla/websocket"
)

// getWriteDeadline calculates write deadline based on tick rate
func (h *GatewayHandler) getWriteDeadline() time.Time {
	if h.tickRate > 0 {
		return time.Now().Add(time.Duration(1000/h.tickRate) * time.Millisecond)
	}
	return time.Now().Add(16 * time.Millisecond)
}

// BroadcastToClients broadcasts data to all connected clients
// Uses delta compression if enabled, otherwise falls back to parallel broadcast
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

// BroadcastGameStateWithDelta broadcasts game state with delta compression
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

				// Issue: #1612 - Adaptive compression (LZ4 для real-time)
				if h.compressor != nil {
					compressed, err := h.compressor.Compress(data, true) // true = real-time data
					if err == nil && len(compressed) < len(data) {
						data = compressed
					}
				}

				atomic.AddInt64(&totalDeltaSize, int64(len(data)))

				ci.clientConn.mu.Lock()
				ci.conn.SetWriteDeadline(deadline)
				if err := ci.conn.WriteMessage(websocket.BinaryMessage, data); err != nil {
					logger.WithError(err).Error("Failed to broadcast delta to client")
					ci.clientConn.mu.Unlock()
					h.RemoveClientConnection(ci.conn)
					continue
				}
				atomic.AddInt64(&successCount, 1)
				deltaState.SetLastState(CopyGameStateData(newState))
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

// BroadcastToClientsParallel broadcasts data to all clients in parallel without delta compression
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
			cc.conn.SetWriteDeadline(deadline)
			if err := cc.conn.WriteMessage(websocket.BinaryMessage, data); err != nil {
				logger.WithError(err).Error("Failed to broadcast to client")
				cc.mu.Unlock()
				h.RemoveClientConnection(cc.conn)
				return
			}
			mu.Lock()
			successCount++
			mu.Unlock()
			cc.mu.Unlock()
		}(clientConn)
	}
	
	wg.Wait()
	
	duration := time.Since(startTime).Seconds()
	RecordGameStateBroadcastDuration(duration)
	RecordGameStateBroadcasted()
	
	logger.WithField("success_count", successCount).WithField("total_clients", clientCount).WithField("duration_ms", duration*1000).Info("Broadcasted GameState to clients")
}
























