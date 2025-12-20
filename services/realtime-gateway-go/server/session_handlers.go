package server

import (
	"encoding/json"
	"net/http"
)

func (s *WebSocketServer) handleHeartbeat(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		w.WriteHeader(http.StatusMethodNotAllowed)
		return
	}

	var req struct {
		Token string `json:"token"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(map[string]string{"error": "invalid request body"})
		return
	}

	if handler, ok := s.handler.(*GatewayHandler); ok && handler.sessionMgr != nil {
		err := handler.sessionMgr.UpdateHeartbeat(r.Context(), req.Token)
		if err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			json.NewEncoder(w).Encode(map[string]string{"error": "failed to update heartbeat"})
			return
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(map[string]string{"status": "ok"})
	} else {
		w.WriteHeader(http.StatusServiceUnavailable)
		json.NewEncoder(w).Encode(map[string]string{"error": "session manager not available"})
	}
}

func (s *WebSocketServer) handleReconnect(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		w.WriteHeader(http.StatusMethodNotAllowed)
		return
	}

	var req struct {
		ReconnectToken string `json:"reconnect_token"`
		IPAddress      string `json:"ip_address,omitempty"`
		UserAgent      string `json:"user_agent,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(map[string]string{"error": "invalid request body"})
		return
	}

	if handler, ok := s.handler.(*GatewayHandler); ok && handler.sessionMgr != nil {
		ipAddress := req.IPAddress
		if ipAddress == "" {
			ipAddress = r.RemoteAddr
		}

		userAgent := req.UserAgent
		if userAgent == "" {
			userAgent = r.Header.Get("User-Agent")
		}

		session, err := handler.sessionMgr.ReconnectSession(r.Context(), req.ReconnectToken, ipAddress, userAgent)
		if err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			json.NewEncoder(w).Encode(map[string]string{"error": "failed to reconnect session"})
			return
		}

		if session == nil {
			w.WriteHeader(http.StatusNotFound)
			json.NewEncoder(w).Encode(map[string]string{"error": "session not found or expired"})
			return
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(map[string]interface{}{
			"status":          "reconnected",
			"token":           session.Token,
			"reconnect_token": session.ReconnectToken,
		})
	} else {
		w.WriteHeader(http.StatusServiceUnavailable)
		json.NewEncoder(w).Encode(map[string]string{"error": "session manager not available"})
	}
}
