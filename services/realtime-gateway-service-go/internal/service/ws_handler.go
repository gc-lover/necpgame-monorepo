package service

import (
	"net/http"

	"go.uber.org/zap"
)

// WebSocketHandler handles WebSocket connections for the realtime gateway service
type WebSocketHandler struct {
	service *Service
	logger  *zap.Logger
}

// NewWebSocketHandler creates a new WebSocket handler
func NewWebSocketHandler(svc *Service) *WebSocketHandler {
	return &WebSocketHandler{
		service: svc,
		logger:  svc.logger,
	}
}

// ServeHTTP implements http.Handler for WebSocket connections
func (h *WebSocketHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	// Upgrade HTTP connection to WebSocket
	conn, err := h.service.upgrader.Upgrade(w, r, nil)
	if err != nil {
		h.logger.Error("failed to upgrade connection", zap.Error(err))
		return
	}

	// Create new session for this connection
	session, err := h.service.sessionManager.CreateSession(r.Context(), conn)
	if err != nil {
		h.logger.Error("failed to create session", zap.Error(err))
		conn.Close()
		return
	}

	h.logger.Info("WebSocket connection established", zap.String("session_id", session.ID()))

	// Start session handling in goroutine
	go func() {
		defer conn.Close()
		if err := session.Handle(); err != nil {
			h.logger.Error("session handling failed", zap.Error(err), zap.String("session_id", session.ID()))
		}
	}()
}
