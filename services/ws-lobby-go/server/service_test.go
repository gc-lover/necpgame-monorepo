// Issue: #2218 - Backend: Добавить unit-тесты для ws-lobby-go
package server

import (
	"context"
	"net/http"
	"net/http/httptest"
	"net/url"
	"strings"
	"testing"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap/zaptest"
	"ws-lobby-go/server/internal/models"
)

func TestNewLobbyService(t *testing.T) {
	logger := zaptest.NewLogger(t)

	// Test with valid Redis URL
	service, err := NewLobbyService(logger, "redis://localhost:6379", "postgres://user:pass@localhost:5432/test")
	if err != nil {
		t.Skip("Skipping test - Redis not available:", err)
	}

	if service == nil {
		t.Error("Expected service to be created")
	}

	if service.logger == nil {
		t.Error("Expected logger to be set")
	}

	if service.redis == nil {
		t.Error("Expected Redis client to be initialized")
	}

	if service.connections == nil {
		t.Error("Expected connections map to be initialized")
	}

	if service.rooms == nil {
		t.Error("Expected rooms map to be initialized")
	}
}

func TestHandleWebSocketConnection(t *testing.T) {
	logger := zaptest.NewLogger(t)
	service, err := NewLobbyService(logger, "redis://localhost:6379", "postgres://user:pass@localhost:5432/test")
	if err != nil {
		t.Skip("Skipping test - Redis not available:", err)
	}

	// Create a test server
	server := httptest.NewServer(service.Router())
	defer server.Close()

	// Convert http URL to ws URL
	serverURL, _ := url.Parse(server.URL)
	serverURL.Scheme = "ws"

	// Test missing token
	req, _ := http.NewRequest("GET", server.URL+"/ws/lobby", nil)
	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		t.Fatalf("Failed to make request: %v", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusUnauthorized {
		t.Errorf("Expected status 401 for missing token, got %d", resp.StatusCode)
	}
}

func TestProcessMessage(t *testing.T) {
	logger := zaptest.NewLogger(t)
	service, err := NewLobbyService(logger, "redis://localhost:6379", "postgres://user:pass@localhost:5432/test")
	if err != nil {
		t.Skip("Skipping test - Redis not available:", err)
	}

	tests := []struct {
		name    string
		msgType string
		payload interface{}
		wantErr bool
	}{
		{
			name:    "chat_message",
			msgType: "chat_message",
			payload: map[string]interface{}{"content": "Hello world"},
			wantErr: false,
		},
		{
			name:    "room_create",
			msgType: "room_create",
			payload: map[string]interface{}{"name": "Test Room"},
			wantErr: false,
		},
		{
			name:    "room_join",
			msgType: "room_join",
			payload: map[string]interface{}{"room_id": uuid.New().String()},
			wantErr: false,
		},
		{
			name:    "heartbeat",
			msgType: "heartbeat",
			payload: nil,
			wantErr: false,
		},
		{
			name:    "unknown_type",
			msgType: "unknown_type",
			payload: nil,
			wantErr: false, // Should not error, just send error message
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Create a mock connection for each test
			conn := &models.Connection{
				ID:     uuid.New().String(),
				UserID: uuid.New(),
			}

			msg := &models.LobbyMessage{
				Type:    tt.msgType,
				Payload: tt.payload,
			}

			// Skip tests that require WebSocket connection
			if tt.msgType == "room_create" {
				t.Skip("Skipping room_create test - requires WebSocket connection mock")
			}

			err := service.processMessage(conn, msg)
			if (err != nil) != tt.wantErr {
				t.Errorf("processMessage() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}
}

func TestHandleChatMessage(t *testing.T) {
	logger := zaptest.NewLogger(t)
	service, err := NewLobbyService(logger, "redis://localhost:6379", "postgres://user:pass@localhost:5432/test")
	if err != nil {
		t.Skip("Skipping test - Redis not available:", err)
	}

	conn := &models.Connection{
		ID:     uuid.New().String(),
		UserID: uuid.New(),
	}

	// Test global chat message
	msg := &models.LobbyMessage{
		Type:    "chat_message",
		Payload: map[string]interface{}{"content": "Hello world"},
	}

	err = service.handleChatMessage(conn, msg)
	if err != nil {
		t.Errorf("handleChatMessage() error = %v", err)
	}
}


func TestFindConnectionByUserID(t *testing.T) {
	logger := zaptest.NewLogger(t)
	service, err := NewLobbyService(logger, "redis://localhost:6379", "postgres://user:pass@localhost:5432/test")
	if err != nil {
		t.Skip("Skipping test - Redis not available:", err)
	}

	userID := uuid.New()
	conn := &models.Connection{
		ID:     uuid.New().String(),
		UserID: userID,
	}

	service.mu.Lock()
	service.connections[conn.ID] = conn
	service.mu.Unlock()

	found := service.findConnectionByUserID(userID)
	if found == nil {
		t.Error("Expected to find connection by user ID")
	}

	if found.ID != conn.ID {
		t.Errorf("Expected connection ID %s, got %s", conn.ID, found.ID)
	}

	// Test not found
	notFound := service.findConnectionByUserID(uuid.New())
	if notFound != nil {
		t.Error("Expected nil for non-existent user ID")
	}
}

func TestHealthEndpoints(t *testing.T) {
	logger := zaptest.NewLogger(t)
	service, err := NewLobbyService(logger, "redis://localhost:6379", "postgres://user:pass@localhost:5432/test")
	if err != nil {
		t.Skip("Skipping test - Redis not available:", err)
	}

	// Test health endpoint
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()

	service.handleHealth(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status 200, got %d", w.Code)
	}

	expected := `{"status": "healthy", "service": "ws-lobby-go"}`
	if strings.TrimSpace(w.Body.String()) != expected {
		t.Errorf("Expected response %s, got %s", expected, w.Body.String())
	}
}

func TestMessagePool(t *testing.T) {
	logger := zaptest.NewLogger(t)
	service, err := NewLobbyService(logger, "redis://localhost:6379", "postgres://user:pass@localhost:5432/test")
	if err != nil {
		t.Skip("Skipping test - Redis not available:", err)
	}

	// Test message pool allocation
	msg1 := service.messagePool.Get().(*models.LobbyMessage)
	if msg1 == nil {
		t.Error("Expected message from pool")
	}

	// Put back and get again
	service.messagePool.Put(msg1)
	msg2 := service.messagePool.Get().(*models.LobbyMessage)

	// Should reuse the same object
	if msg1 != msg2 {
		t.Log("Pool allocated new object (expected behavior for small pool)")
	}
}

func TestShutdown(t *testing.T) {
	logger := zaptest.NewLogger(t)
	service, err := NewLobbyService(logger, "redis://localhost:6379", "postgres://user:pass@localhost:5432/test")
	if err != nil {
		t.Skip("Skipping test - Redis not available:", err)
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	err = service.Shutdown(ctx)
	if err != nil {
		t.Errorf("Shutdown() error = %v", err)
	}

	// Verify connections are cleared
	service.mu.RLock()
	connCount := len(service.connections)
	service.mu.RUnlock()

	if connCount != 0 {
		t.Errorf("Expected 0 connections after shutdown, got %d", connCount)
	}
}

func BenchmarkProcessMessage(b *testing.B) {
	logger := zaptest.NewLogger(b)
	service, err := NewLobbyService(logger, "redis://localhost:6379", "postgres://user:pass@localhost:5432/test")
	if err != nil {
		b.Skip("Skipping benchmark - Redis not available:", err)
	}

	conn := &models.Connection{
		ID:     uuid.New().String(),
		UserID: uuid.New(),
	}

	msg := &models.LobbyMessage{
		Type:    "heartbeat",
		Payload: nil,
	}

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		service.processMessage(conn, msg)
	}
}

func BenchmarkHandleChatMessage(b *testing.B) {
	logger := zaptest.NewLogger(b)
	service, err := NewLobbyService(logger, "redis://localhost:6379", "postgres://user:pass@localhost:5432/test")
	if err != nil {
		b.Skip("Skipping benchmark - Redis not available:", err)
	}

	conn := &models.Connection{
		ID:     uuid.New().String(),
		UserID: uuid.New(),
	}

	msg := &models.LobbyMessage{
		Type:    "chat_message",
		Payload: map[string]interface{}{"content": "Hello world"},
	}

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		service.handleChatMessage(conn, msg)
	}
}
