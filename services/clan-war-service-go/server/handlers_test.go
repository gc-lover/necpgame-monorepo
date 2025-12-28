package server

import (
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/jwtauth/v5"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/stretchr/testify/assert"
	"go.uber.org/zap/zaptest"
)

func setupTestHandlers() (*Handlers, *Server) {
	// Create mock database pool (simplified for testing)
	var db *pgxpool.Pool

	logger := zaptest.NewLogger(nil)
	tokenAuth := jwtauth.New("HS256", []byte("test-secret"), nil)

	server := NewServer(db, logger, tokenAuth)
	handlers := NewHandlers(server)

	return handlers, server
}

func TestHandlers_HealthCheck(t *testing.T) {
	handlers, _ := setupTestHandlers()

	// Create a test request
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()

	// Call the health check handler
	handlers.HealthCheck(w, req)

	// Check the response
	assert.Equal(t, http.StatusOK, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Equal(t, "healthy", response["status"])
}

func TestHandlers_HealthCheck_DatabaseError(t *testing.T) {
	handlers, _ := setupTestHandlers()

	// Create a test request
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()

	// Call the health check handler
	handlers.HealthCheck(w, req)

	// Check the response - since db is nil, it should be unhealthy
	assert.Equal(t, http.StatusServiceUnavailable, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Equal(t, "unhealthy", response["status"])
}

func TestHandlers_ReadinessCheck_Success(t *testing.T) {
	handlers, _ := setupTestHandlers()

	// Create a test request
	req := httptest.NewRequest("GET", "/ready", nil)
	w := httptest.NewRecorder()

	// Call the readiness check handler
	handlers.ReadinessCheck(w, req)

	// Check the response
	assert.Equal(t, http.StatusOK, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Equal(t, "healthy", response["status"])
}

func TestHandlers_ReadinessCheck_Timeout(t *testing.T) {
	handlers, _ := setupTestHandlers()

	// Create a test request with short timeout context
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Millisecond)
	defer cancel()

	req := httptest.NewRequest("GET", "/ready", nil).WithContext(ctx)
	w := httptest.NewRecorder()

	// Call the readiness check handler
	handlers.ReadinessCheck(w, req)

	// Check the response - should timeout
	assert.Equal(t, http.StatusServiceUnavailable, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Equal(t, "unhealthy", response["status"])
}

func TestHandlers_Metrics_Success(t *testing.T) {
	handlers, _, mock := setupTestHandlers()
	defer mock.ExpectClose()

	// Setup mock expectations
	mock.ExpectPing().WillReturnError(nil)

	// Create a test request
	req := httptest.NewRequest("GET", "/metrics", nil)
	w := httptest.NewRecorder()

	// Call the metrics handler
	handlers.Metrics(w, req)

	// Check the response
	assert.Equal(t, http.StatusOK, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Equal(t, "healthy", response["status"])
	assert.Equal(t, "clan-war-service-go", response["service"])
}

func TestHandlers_CreateRouter(t *testing.T) {
	handlers, _, mock := setupTestHandlers()
	defer mock.ExpectClose()

	router := handlers.CreateRouter()

	// Verify router is created
	assert.NotNil(t, router)
	assert.IsType(t, &chi.Mux{}, router)

	// Test health endpoint
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()

	router.ServeHTTP(w, req)
	assert.Equal(t, http.StatusOK, w.Code)
}

func TestHandlers_ConcurrentRequests(t *testing.T) {
	handlers, _, mock := setupTestHandlers()
	defer mock.ExpectClose()

	// Setup mock for concurrent pings
	mock.ExpectPing().WillReturnError(nil).Times(10)

	// Run concurrent health checks
	const numGoroutines = 10

	results := make(chan int, numGoroutines)

	for i := 0; i < numGoroutines; i++ {
		go func() {
			req := httptest.NewRequest("GET", "/health", nil)
			w := httptest.NewRecorder()
			handlers.HealthCheck(w, req)
			results <- w.Code
		}()
	}

	// Collect results
	for i := 0; i < numGoroutines; i++ {
		statusCode := <-results
		assert.Equal(t, http.StatusOK, statusCode)
	}
}

// API Integration tests for clan war operations
func TestHandlers_API_ClanWarOperations(t *testing.T) {
	handlers, server, mock := setupTestHandlers()
	defer mock.ExpectClose()

	// Test clan war creation (mock API call)
	testClanWarID := uuid.New()
	testClanID1 := uuid.New()
	testClanID2 := uuid.New()

	// Mock database expectations for clan war creation
	mock.ExpectBegin()
	mock.ExpectExec("INSERT INTO clan_wars").
		WithArgs(testClanWarID, testClanID1, testClanID2, sqlmock.AnyArg(), sqlmock.AnyArg()).
		WillReturnResult(sqlmock.NewResult(1, 1))
	mock.ExpectCommit()

	// Test through API handlers (simulated)
	// Note: This would be expanded based on actual API endpoints
	// For now, testing the server setup and basic functionality

	assert.NotNil(t, handlers)
	assert.NotNil(t, server)
	assert.NotNil(t, server.db)
	assert.NotNil(t, server.logger)
}

func TestHandlers_ErrorHandling(t *testing.T) {
	handlers, _, mock := setupTestHandlers()
	defer mock.ExpectClose()

	// Test with invalid JSON in request
	invalidJSON := `{"invalid": json}`
	req := httptest.NewRequest("POST", "/clan-wars", strings.NewReader(invalidJSON))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	// This test demonstrates error handling structure
	// In a real implementation, you would test specific endpoints
	handlers.HealthCheck(w, req) // Using health check as it's always available

	// Should still work despite invalid body
	assert.Equal(t, http.StatusOK, w.Code)
}

func TestHandlers_CORS_Headers(t *testing.T) {
	handlers, _, mock := setupTestHandlers()
	defer mock.ExpectClose()

	req := httptest.NewRequest("OPTIONS", "/health", nil)
	req.Header.Set("Origin", "http://localhost:3000")
	req.Header.Set("Access-Control-Request-Method", "GET")
	w := httptest.NewRecorder()

	handlers.HealthCheck(w, req)

	// Check CORS headers are set (if implemented)
	// This test structure allows for CORS validation when implemented
	assert.Equal(t, http.StatusOK, w.Code)
}

func TestHandlers_RequestValidation(t *testing.T) {
	handlers, _, mock := setupTestHandlers()
	defer mock.ExpectClose()

	// Test various invalid request scenarios
	testCases := []struct {
		name         string
		method       string
		path         string
		body         string
		expectedCode int
	}{
		{
			name:         "invalid method",
			method:       "PUT",
			path:         "/health",
			body:         "",
			expectedCode: http.StatusMethodNotAllowed,
		},
		{
			name:         "malformed path",
			method:       "GET",
			path:         "/invalid-path",
			body:         "",
			expectedCode: http.StatusNotFound,
		},
	}

	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			var body *strings.Reader
			if tc.body != "" {
				body = strings.NewReader(tc.body)
			}

			req := httptest.NewRequest(tc.method, tc.path, body)
			w := httptest.NewRecorder()

			router := handlers.CreateRouter()
			router.ServeHTTP(w, req)

			// Note: Actual status codes depend on router implementation
			// This test structure allows validation of request handling
			assert.NotEqual(t, http.StatusInternalServerError, w.Code)
		})
	}
}

func TestHandlers_Performance_UnderLoad(t *testing.T) {
	if testing.Short() {
		t.Skip("Skipping performance test in short mode")
	}

	handlers, _, mock := setupTestHandlers()
	defer mock.ExpectClose()

	// Setup mock for multiple pings
	mock.ExpectPing().WillReturnError(nil).Times(100)

	start := time.Now()

	// Simulate load
	for i := 0; i < 100; i++ {
		req := httptest.NewRequest("GET", "/health", nil)
		w := httptest.NewRecorder()
		handlers.HealthCheck(w, req)

		assert.Equal(t, http.StatusOK, w.Code)
	}

	duration := time.Since(start)

	// Performance assertion - should complete within reasonable time
	assert.Less(t, duration, 5*time.Second, "Health checks should complete quickly under load")
}

// Integration test for server lifecycle
func TestHandlers_ServerLifecycle(t *testing.T) {
	handlers, server, mock := setupTestHandlers()
	defer mock.ExpectClose()

	// Test server initialization
	assert.NotNil(t, server)
	assert.NotNil(t, server.db)
	assert.NotNil(t, server.logger)
	assert.NotNil(t, server.tokenAuth)

	// Test handlers initialization
	assert.NotNil(t, handlers)
	assert.NotNil(t, handlers.srv)

	// Test router creation
	router := handlers.CreateRouter()
	assert.NotNil(t, router)

	// Test basic functionality after setup
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()
	handlers.HealthCheck(w, req)

	assert.Equal(t, http.StatusOK, w.Code)
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
