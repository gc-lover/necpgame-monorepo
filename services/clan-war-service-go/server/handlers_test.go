package server

import (
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/go-chi/jwtauth/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap/zaptest"
)

func TestHandlers_HealthCheck(t *testing.T) {
	// Create a test logger
	logger := zaptest.NewLogger(t)

	// Create mock database connection (we'll use nil for health check test)
	var db *pgxpool.Pool

	// Create mock JWT auth
	tokenAuth := jwtauth.New("HS256", []byte("test-secret"), nil)

	// Create server instance
	srv := NewServer(db, logger, tokenAuth)

	// Create handlers
	handlers := NewHandlers(srv)

	// Create a test request
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()

	// Call the health check handler
	handlers.HealthCheck(w, req)

	// Check the response
	if w.Code != http.StatusOK {
		t.Errorf("Expected status code %d, got %d", http.StatusOK, w.Code)
	}

	expectedBody := `{"status":"healthy"}`
	if w.Body.String() != expectedBody {
		t.Errorf("Expected body %q, got %q", expectedBody, w.Body.String())
	}
}

func TestHandlers_ReadinessCheck(t *testing.T) {
	// Create a test logger
	logger := zaptest.NewLogger(t)

	// Create mock database connection
	var db *pgxpool.Pool

	// Create mock JWT auth
	tokenAuth := jwtauth.New("HS256", []byte("test-secret"), nil)

	// Create server instance
	srv := NewServer(db, logger, tokenAuth)

	// Create handlers
	handlers := NewHandlers(srv)

	// Create a test request
	req := httptest.NewRequest("GET", "/ready", nil)
	w := httptest.NewRecorder()

	// Call the readiness check handler
	handlers.ReadinessCheck(w, req)

	// Check the response
	if w.Code != http.StatusOK {
		t.Errorf("Expected status code %d, got %d", http.StatusOK, w.Code)
	}

	expectedBody := `{"status":"ready"}`
	if w.Body.String() != expectedBody {
		t.Errorf("Expected body %q, got %q", expectedBody, w.Body.String())
	}
}

func TestHandlers_Metrics(t *testing.T) {
	// Create a test logger
	logger := zaptest.NewLogger(t)

	// Create mock database connection
	var db *pgxpool.Pool

	// Create mock JWT auth
	tokenAuth := jwtauth.New("HS256", []byte("test-secret"), nil)

	// Create server instance
	srv := NewServer(db, logger, tokenAuth)

	// Create handlers
	handlers := NewHandlers(srv)

	// Create a test request
	req := httptest.NewRequest("GET", "/metrics", nil)
	w := httptest.NewRecorder()

	// Call the metrics handler
	handlers.Metrics(w, req)

	// Check the response
	if w.Code != http.StatusOK {
		t.Errorf("Expected status code %d, got %d", http.StatusOK, w.Code)
	}

	contentType := w.Header().Get("Content-Type")
	if contentType != "text/plain" {
		t.Errorf("Expected Content-Type %q, got %q", "text/plain", contentType)
	}

	body := w.Body.String()
	if !contains(body, "# Clan War Service Metrics") {
		t.Errorf("Expected metrics header in body, got %q", body)
	}
	if !contains(body, "clan_war_service_up 1") {
		t.Errorf("Expected up metric in body, got %q", body)
	}
}

// Helper function to check if string contains substring
func contains(s, substr string) bool {
	return len(s) >= len(substr) && (s == substr || len(s) > len(substr) && (s[:len(substr)] == substr || s[len(s)-len(substr):] == substr || containsAt(s, substr)))
}

func containsAt(s, substr string) bool {
	for i := 0; i <= len(s)-len(substr); i++ {
		if s[i:i+len(substr)] == substr {
			return true
		}
	}
	return false
}

// Issue: #1846
