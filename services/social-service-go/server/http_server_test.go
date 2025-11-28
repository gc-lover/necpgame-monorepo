package server

import (
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestHealthCheck(t *testing.T) {
	config := &ServerConfig{
		Addr:           ":8084",
		FriendsService: NewMockFriendsService(),
		GuildsService:  &MockGuildsService{},
		ChatService:    &MockChatService{},
		MailService:    &MockMailService{},
		NotificationsService: &MockNotificationsService{},
	}
	server := NewHTTPServer(config)
	
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
	
	expected := `{"status":"ok"}`
	if w.Body.String() != expected {
		t.Errorf("Expected body %s, got %s", expected, w.Body.String())
	}
}

func TestMetricsEndpoint(t *testing.T) {
	config := &ServerConfig{
		Addr:           ":8084",
		FriendsService: NewMockFriendsService(),
		GuildsService:  &MockGuildsService{},
		ChatService:    &MockChatService{},
		MailService:    &MockMailService{},
		NotificationsService: &MockNotificationsService{},
	}
	server := NewHTTPServer(config)
	
	req := httptest.NewRequest("GET", "/metrics", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestCORSHeaders(t *testing.T) {
	config := &ServerConfig{
		Addr:           ":8084",
		FriendsService: NewMockFriendsService(),
		GuildsService:  &MockGuildsService{},
		ChatService:    &MockChatService{},
		MailService:    &MockMailService{},
		NotificationsService: &MockNotificationsService{},
	}
	server := NewHTTPServer(config)
	
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Header().Get("Access-Control-Allow-Origin") != "*" {
		t.Errorf("Expected CORS header to be set")
	}
}
