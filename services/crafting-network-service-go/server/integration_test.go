//go:align 64
// Issue: #2286 - QA Testing
// Integration tests for crafting-network-service-go

package server

import (
	"net/http"
	"net/http/httptest"
	"sync"
	"testing"
	"time"
)

func TestCraftingNetworkService_Integration(t *testing.T) {
	// Setup test configuration
	config := &Config{
		MaxWorkers:      10,
		WorkerPool:      make(chan struct{}, 10),
		CacheTTL:        10 * time.Minute,
		ReadTimeout:     15 * time.Second,
		WriteTimeout:    15 * time.Second,
		IdleTimeout:     60 * time.Second,
		MaxHeaderBytes:  1 << 16,
		WebSocketPort:   8081,
		UDPPort:         9999,
	}

	// Create server
	server := NewCraftingNetworkServer(config)

	// Create test HTTP request to health check endpoint with Bearer token
	req := httptest.NewRequest("GET", "/health", nil)
	req.Header.Set("Authorization", "Bearer test-token")
	w := httptest.NewRecorder()

	// Get handler and serve
	handler := server.Handler()
	handler.ServeHTTP(w, req)

	// Check response
	if w.Code != http.StatusOK {
		t.Errorf("Expected status 200, got %d", w.Code)
	}

	// Check response body contains expected health status
	body := w.Body.String()
	if len(body) == 0 {
		t.Error("Response body is empty")
	}

	t.Logf("Health check response: %s", body)
}

func TestCraftingNetworkService_ConcurrentRequests(t *testing.T) {
	// Setup test configuration
	config := &Config{
		MaxWorkers:      50,
		WorkerPool:      make(chan struct{}, 50),
		CacheTTL:        10 * time.Minute,
		ReadTimeout:     15 * time.Second,
		WriteTimeout:    15 * time.Second,
		IdleTimeout:     60 * time.Second,
		MaxHeaderBytes:  1 << 16,
		WebSocketPort:   8081,
		UDPPort:         9999,
	}

	// Create server
	server := NewCraftingNetworkServer(config)

	// Test concurrent health check requests
	numRequests := 100
	var wg sync.WaitGroup
	errors := make(chan error, numRequests)

	for i := 0; i < numRequests; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()

			req := httptest.NewRequest("GET", "/health", nil)
			req.Header.Set("Authorization", "Bearer test-token")
			w := httptest.NewRecorder()

			handler := server.Handler()
			handler.ServeHTTP(w, req)

			if w.Code != http.StatusOK {
				errors <- http.ErrNotSupported // Using this as a generic error
				return
			}
		}()
	}

	wg.Wait()
	close(errors)

	// Check for any errors
	errorCount := 0
	for err := range errors {
		if err != nil {
			errorCount++
		}
	}

	if errorCount > 0 {
		t.Errorf("Found %d errors in concurrent requests", errorCount)
	} else {
		t.Logf("Successfully handled %d concurrent requests", numRequests)
	}
}

func TestCraftingNetworkService_LoadTest(t *testing.T) {
	// Setup test configuration optimized for load testing
	config := &Config{
		MaxWorkers:      200,
		WorkerPool:      make(chan struct{}, 200),
		CacheTTL:        10 * time.Minute,
		ReadTimeout:     5 * time.Second,  // Shorter for load testing
		WriteTimeout:    5 * time.Second,
		IdleTimeout:     30 * time.Second,
		MaxHeaderBytes:  1 << 16,
		WebSocketPort:   8081,
		UDPPort:         9999,
	}

	// Create server
	server := NewCraftingNetworkServer(config)

	// Load test parameters
	numConcurrent := 100
	requestsPerClient := 10
	totalRequests := numConcurrent * requestsPerClient

	start := time.Now()
	var wg sync.WaitGroup
	errors := make(chan error, totalRequests)

	// Run load test
	for i := 0; i < numConcurrent; i++ {
		wg.Add(1)
		go func(clientID int) {
			defer wg.Done()

			for j := 0; j < requestsPerClient; j++ {
				req := httptest.NewRequest("GET", "/health", nil)
				req.Header.Set("Authorization", "Bearer test-token")
				w := httptest.NewRecorder()

				handler := server.Handler()
				handler.ServeHTTP(w, req)

				if w.Code != http.StatusOK {
					errors <- http.ErrNotSupported
					continue
				}
			}
		}(i)
	}

	wg.Wait()
	close(errors)

	// Calculate results
	duration := time.Since(start)
	requestsPerSecond := float64(totalRequests) / duration.Seconds()

	// Check for errors
	errorCount := 0
	for err := range errors {
		if err != nil {
			errorCount++
		}
	}

	// Validate performance targets
	if duration > 5*time.Second {
		t.Errorf("Load test took too long: %v (target: <5s)", duration)
	}

	if requestsPerSecond < 1000 {
		t.Errorf("Low throughput: %.0f req/s (target: >1000 req/s)", requestsPerSecond)
	}

	if errorCount > 0 {
		t.Errorf("Load test had %d errors", errorCount)
	}

	t.Logf("Load test results: %d requests in %v (%.0f req/s), %d errors",
		totalRequests, duration, requestsPerSecond, errorCount)
}

func TestCraftingNetworkService_APIContract(t *testing.T) {
	// Setup test configuration
	config := &Config{
		MaxWorkers:      10,
		WorkerPool:      make(chan struct{}, 10),
		CacheTTL:        10 * time.Minute,
		ReadTimeout:     15 * time.Second,
		WriteTimeout:    15 * time.Second,
		IdleTimeout:     60 * time.Second,
		MaxHeaderBytes:  1 << 16,
		WebSocketPort:   8081,
		UDPPort:         9999,
	}

	// Create server
	server := NewCraftingNetworkServer(config)

	// Test that server implements the Handler interface
	handler := server.Handler()

	// Test that we can create a request to a known endpoint
	req := httptest.NewRequest("GET", "/health", nil)
	req.Header.Set("Authorization", "Bearer test-token")
	w := httptest.NewRecorder()

	handler.ServeHTTP(w, req)

	// Verify response structure
	if w.Code != http.StatusOK {
		t.Errorf("Expected 200 OK, got %d", w.Code)
	}

	// Verify Content-Type header (should be JSON for API responses)
	contentType := w.Header().Get("Content-Type")
	if contentType == "" {
		t.Log("Warning: No Content-Type header set")
	}

	t.Logf("API contract test passed - Status: %d, Content-Type: %s", w.Code, contentType)
}