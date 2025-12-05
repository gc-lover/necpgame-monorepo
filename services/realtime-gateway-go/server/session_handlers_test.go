package server

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/mock"
)

// mockSessionManager is defined in ban_notifications_test.go to avoid duplication

func TestHandleHeartbeat(t *testing.T) {
	mockMgr := new(mockSessionManager)

	characterID := uuid.New()
	session := &PlayerSession{
		ID:             uuid.New(),
		PlayerID:       "player123",
		Token:          uuid.New().String(),
		ReconnectToken: uuid.New().String(),
		Status:         SessionStatusActive,
		IPAddress:      "127.0.0.1",
		UserAgent:      "test-agent",
		CharacterID:    &characterID,
		CreatedAt:      time.Now(),
		LastHeartbeat:  time.Now(),
	}
	mockMgr.On("GetSessionByToken", mock.Anything, session.Token).Return(session, nil)
	mockMgr.On("UpdateHeartbeat", mock.Anything, session.Token).Return(nil)

	handler := &GatewayHandler{
		sessionMgr: mockMgr,
	}

	wsServer := NewWebSocketServer(":8080", handler)

	reqBody := map[string]string{
		"token": session.Token,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/session/heartbeat", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	wsServer.handleHeartbeat(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}

	var response map[string]string
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response["status"] != "ok" {
		t.Errorf("Expected status 'ok', got %s", response["status"])
	}
}

func TestHandleHeartbeatInvalidMethod(t *testing.T) {
	mockMgr := new(mockSessionManager)

	handler := &GatewayHandler{
		sessionMgr: mockMgr,
	}

	wsServer := NewWebSocketServer(":8080", handler)

	req := httptest.NewRequest("GET", "/session/heartbeat", nil)
	w := httptest.NewRecorder()

	wsServer.handleHeartbeat(w, req)

	if w.Code != http.StatusMethodNotAllowed {
		t.Errorf("Expected status %d, got %d", http.StatusMethodNotAllowed, w.Code)
	}
}

func TestHandleReconnect(t *testing.T) {
	mockMgr := new(mockSessionManager)

	characterID := uuid.New()
	session := &PlayerSession{
		ID:             uuid.New(),
		PlayerID:       "player123",
		Token:          uuid.New().String(),
		ReconnectToken: uuid.New().String(),
		Status:         SessionStatusDisconnected,
		IPAddress:      "127.0.0.1",
		UserAgent:      "test-agent",
		CharacterID:    &characterID,
		CreatedAt:      time.Now(),
		LastHeartbeat:  time.Now(),
	}
	reconnectedSession := *session
	reconnectedSession.Status = SessionStatusActive
	reconnectedSession.IPAddress = "192.168.1.1"
	reconnectedSession.UserAgent = "new-agent"
	mockMgr.On("ReconnectSession", mock.Anything, session.ReconnectToken, "192.168.1.1", "new-agent").Return(&reconnectedSession, nil)

	handler := &GatewayHandler{
		sessionMgr: mockMgr,
	}

	wsServer := NewWebSocketServer(":8080", handler)

	reqBody := map[string]string{
		"reconnect_token": session.ReconnectToken,
		"ip_address":      "192.168.1.1",
		"user_agent":      "new-agent",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/session/reconnect", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	wsServer.handleReconnect(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}

	var response map[string]interface{}
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response["status"] != "reconnected" {
		t.Errorf("Expected status 'reconnected', got %v", response["status"])
	}
}

func TestHandleReconnectNotFound(t *testing.T) {
	mockMgr := new(mockSessionManager)
	mockMgr.On("ReconnectSession", mock.Anything, mock.Anything, mock.Anything, mock.Anything).Return(nil, nil)

	handler := &GatewayHandler{
		sessionMgr: mockMgr,
	}

	wsServer := NewWebSocketServer(":8080", handler)

	reqBody := map[string]string{
		"reconnect_token": "invalid-token",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/session/reconnect", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	wsServer.handleReconnect(w, req)

	if w.Code != http.StatusNotFound {
		t.Errorf("Expected status %d, got %d", http.StatusNotFound, w.Code)
	}
}
