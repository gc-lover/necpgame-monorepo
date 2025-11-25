package server

import (
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestHealthCheck(t *testing.T) {
	friendsService := NewMockFriendsService()
	server := NewHTTPServer(":8084", friendsService)
	
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
	friendsService := NewMockFriendsService()
	server := NewHTTPServer(":8084", friendsService)
	
	req := httptest.NewRequest("GET", "/metrics", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestCORSHeaders(t *testing.T) {
	friendsService := NewMockFriendsService()
	server := NewHTTPServer(":8084", friendsService)
	
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Header().Get("Access-Control-Allow-Origin") != "*" {
		t.Errorf("Expected CORS header to be set")
	}
}
