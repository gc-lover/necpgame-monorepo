package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/google/uuid"
)

type mockSessionManager struct {
	sessions map[string]*PlayerSession
}

func (m *mockSessionManager) CreateSession(ctx context.Context, playerID, ipAddress, userAgent string, characterID *uuid.UUID) (*PlayerSession, error) {
	session := &PlayerSession{
		ID:              uuid.New(),
		PlayerID:        playerID,
		Token:           uuid.New().String(),
		ReconnectToken:  uuid.New().String(),
		Status:          SessionStatusActive,
		IPAddress:       ipAddress,
		UserAgent:       userAgent,
		CharacterID:     characterID,
	}
	m.sessions[session.Token] = session
	return session, nil
}

func (m *mockSessionManager) GetSessionByToken(ctx context.Context, token string) (*PlayerSession, error) {
	return m.sessions[token], nil
}

func (m *mockSessionManager) GetSessionByPlayerID(ctx context.Context, playerID string) (*PlayerSession, error) {
	for _, s := range m.sessions {
		if s.PlayerID == playerID {
			return s, nil
		}
	}
	return nil, nil
}

func (m *mockSessionManager) UpdateHeartbeat(ctx context.Context, token string) error {
	session := m.sessions[token]
	if session == nil {
		return nil
	}
	return nil
}

func (m *mockSessionManager) ReconnectSession(ctx context.Context, reconnectToken, ipAddress, userAgent string) (*PlayerSession, error) {
	for _, s := range m.sessions {
		if s.ReconnectToken == reconnectToken {
			if s.Status == SessionStatusDisconnected {
				s.Status = SessionStatusActive
				s.IPAddress = ipAddress
				s.UserAgent = userAgent
				s.DisconnectCount++
				return s, nil
			}
		}
	}
	return nil, nil
}

func (m *mockSessionManager) CloseSession(ctx context.Context, token string) error {
	delete(m.sessions, token)
	return nil
}

func (m *mockSessionManager) DisconnectSession(ctx context.Context, token string) error {
	session := m.sessions[token]
	if session != nil {
		session.Status = SessionStatusDisconnected
	}
	return nil
}

func (m *mockSessionManager) GetActiveSessionsCount(ctx context.Context) (int, error) {
	count := 0
	for _, s := range m.sessions {
		if s.Status == SessionStatusActive {
			count++
		}
	}
	return count, nil
}

func (m *mockSessionManager) CleanupExpiredSessions(ctx context.Context) error {
	return nil
}

func (m *mockSessionManager) SaveSession(ctx context.Context, session *PlayerSession) error {
	m.sessions[session.Token] = session
	return nil
}

func TestHandleHeartbeat(t *testing.T) {
	mockMgr := &mockSessionManager{
		sessions: make(map[string]*PlayerSession),
	}

	characterID := uuid.New()
	session, _ := mockMgr.CreateSession(context.Background(), "player123", "127.0.0.1", "test-agent", &characterID)

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
	mockMgr := &mockSessionManager{
		sessions: make(map[string]*PlayerSession),
	}

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
	mockMgr := &mockSessionManager{
		sessions: make(map[string]*PlayerSession),
	}

	characterID := uuid.New()
	session, _ := mockMgr.CreateSession(context.Background(), "player123", "127.0.0.1", "test-agent", &characterID)
	mockMgr.DisconnectSession(context.Background(), session.Token)

	handler := &GatewayHandler{
		sessionMgr: mockMgr,
	}

	wsServer := NewWebSocketServer(":8080", handler)

	reqBody := map[string]string{
		"reconnect_token": session.ReconnectToken,
		"ip_address":      "192.168.1.1",
		"user_agent":       "new-agent",
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
	mockMgr := &mockSessionManager{
		sessions: make(map[string]*PlayerSession),
	}

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

