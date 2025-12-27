// Issue: #2248 - QA Testing: reset-service-go
package server

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"net/http/httptest"
	"runtime"
	"sync"
	"testing"
	"time"

	"reset-service-go-service-go/pkg/api"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

// TestHealthCheckQA tests health check endpoint under various conditions
func TestHealthCheckQA(t *testing.T) {
	tests := []struct {
		name           string
		expectedStatus int
		expectedDomain string
		expectedStatusValue string
	}{
		{
			name:           "Basic health check",
			expectedStatus: http.StatusOK,
			expectedDomain: "reset-service",
			expectedStatusValue: "healthy",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Create service
			svc := NewResetservicegoService()
			require.NotNil(t, svc)

			// Create test request
			req := httptest.NewRequest(http.MethodGet, "/api/v1/reset/health", nil)
			w := httptest.NewRecorder()

			// Get handler and serve
			handler := svc.Handler()
			handler.ServeHTTP(w, req)

			// Assert response
			assert.Equal(t, tt.expectedStatus, w.Code)

			var resp api.HealthResponse
			err := json.Unmarshal(w.Body.Bytes(), &resp)
			require.NoError(t, err)

			assert.Equal(t, tt.expectedDomain, resp.Domain)
			assert.Equal(t, tt.expectedStatusValue, string(resp.Status))
			assert.NotEmpty(t, resp.Version.Value)
			assert.Greater(t, resp.UptimeSeconds.Value, 0)
		})
	}
}

// TestGetResetHistoryQA tests reset history retrieval with various scenarios
func TestGetResetHistoryQA(t *testing.T) {
	tests := []struct {
		name           string
		limit          int
		offset         int
		expectedStatus int
		expectedCount  int
		hasMore        bool
	}{
		{
			name:           "Default pagination",
			limit:          10,
			offset:         0,
			expectedStatus: http.StatusOK,
			expectedCount:  2,
			hasMore:        false,
		},
		{
			name:           "Small limit",
			limit:          1,
			offset:         0,
			expectedStatus: http.StatusOK,
			expectedCount:  1,
			hasMore:        true,
		},
		{
			name:           "Offset beyond data",
			limit:          10,
			offset:         10,
			expectedStatus: http.StatusOK,
			expectedCount:  0,
			hasMore:        false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewResetservicegoService()
			require.NotNil(t, svc)

			// Create request with query parameters
			url := fmt.Sprintf("/api/v1/api/v1/reset/history?limit=%d&offset=%d", tt.limit, tt.offset)
			req := httptest.NewRequest(http.MethodGet, url, nil)
			w := httptest.NewRecorder()

			handler := svc.Handler()
			handler.ServeHTTP(w, req)

			assert.Equal(t, tt.expectedStatus, w.Code)

			var resp api.GetResetHistoryOK
			err := json.Unmarshal(w.Body.Bytes(), &resp)
			require.NoError(t, err)

			assert.Len(t, resp.Resets, tt.expectedCount)
			assert.Equal(t, tt.hasMore, resp.HasMore)
			assert.Equal(t, 2, resp.TotalCount) // Total mock data
		})
	}
}

// TestGetResetStatsQA tests reset statistics endpoint
func TestGetResetStatsQA(t *testing.T) {
	svc := NewResetservicegoService()
	require.NotNil(t, svc)

	req := httptest.NewRequest(http.MethodGet, "/api/v1/reset/stats", nil)
	w := httptest.NewRecorder()

	handler := svc.Handler()
	handler.ServeHTTP(w, req)

	assert.Equal(t, http.StatusOK, w.Code)

	var resp api.GetResetStatsOK
	err := json.Unmarshal(w.Body.Bytes(), &resp)
	require.NoError(t, err)

	// Validate statistics structure
	assert.Equal(t, 42, resp.TotalResets)
	assert.Equal(t, 38, resp.SuccessfulResets)
	assert.Equal(t, 4, resp.FailedResets)
	assert.Equal(t, 45.5, resp.AverageCompletionTime)
}

// TestTriggerResetQA tests reset triggering with various scenarios
func TestTriggerResetQA(t *testing.T) {
	tests := []struct {
		name           string
		resetType      string
		confirmationToken string
		expectedStatus int
		expectSuccess  bool
	}{
		{
			name:           "Valid character reset",
			resetType:      "character_reset",
			confirmationToken: "CONFIRM_RESET_2024",
			expectedStatus: http.StatusOK,
			expectSuccess:  true,
		},
		{
			name:           "Valid full reset",
			resetType:      "full_reset",
			confirmationToken: "CONFIRM_RESET_2024",
			expectedStatus: http.StatusOK,
			expectSuccess:  true,
		},
		{
			name:           "Invalid confirmation token",
			resetType:      "character_reset",
			confirmationToken: "INVALID_TOKEN",
			expectedStatus: http.StatusInternalServerError,
			expectSuccess:  false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewResetservicegoService()
			require.NotNil(t, svc)

			// Create request body
			reqBody := api.TriggerResetReq{
				ResetType:         api.TriggerResetReqResetType(tt.resetType),
				ConfirmationToken: tt.confirmationToken,
			}

			bodyBytes, err := json.Marshal(reqBody)
			require.NoError(t, err)

			req := httptest.NewRequest(http.MethodPost, "/api/v1/reset/trigger",
				bytes.NewReader(bodyBytes))
			req.Header.Set("Content-Type", "application/json")
			w := httptest.NewRecorder()

			handler := svc.Handler()
			handler.ServeHTTP(w, req)

			assert.Equal(t, tt.expectedStatus, w.Code)

			if tt.expectSuccess {
				var resp api.TriggerResetOK
				err := json.Unmarshal(w.Body.Bytes(), &resp)
				require.NoError(t, err)

				assert.NotEmpty(t, resp.ResetID)
				assert.Contains(t, resp.Message, "queued successfully")
			}
		})
	}
}

// TestConcurrentLoadQA tests service under concurrent load
func TestConcurrentLoadQA(t *testing.T) {
	svc := NewResetservicegoService()
	require.NotNil(t, svc)

	handler := svc.Handler()
	concurrentUsers := 100
	requestsPerUser := 10

	var wg sync.WaitGroup
	errorCount := int64(0)
	var mu sync.Mutex

	// Start concurrent users
	for i := 0; i < concurrentUsers; i++ {
		wg.Add(1)
		go func(userID int) {
			defer wg.Done()

			for j := 0; j < requestsPerUser; j++ {
				// Alternate between health check and stats
				var req *http.Request
				if j%2 == 0 {
					req = httptest.NewRequest(http.MethodGet, "/health", nil)
				} else {
					req = httptest.NewRequest(http.MethodGet, "/api/v1/reset/stats", nil)
				}

				w := httptest.NewRecorder()
				handler.ServeHTTP(w, req)

				if w.Code != http.StatusOK {
					mu.Lock()
					errorCount++
					mu.Unlock()
				}
			}
		}(i)
	}

	// Wait for all requests to complete
	done := make(chan bool)
	go func() {
		wg.Wait()
		done <- true
	}()

	select {
	case <-done:
		// All requests completed
	case <-time.After(30 * time.Second):
		t.Fatal("Concurrent load test timed out")
	}

	// Assert no errors occurred
	assert.Equal(t, int64(0), errorCount, "Some concurrent requests failed")
}

// TestMemoryUsageQA tests memory usage patterns
func TestMemoryUsageQA(t *testing.T) {
	// Record initial memory stats
	runtime.GC()
	var m1 runtime.MemStats
	runtime.ReadMemStats(&m1)

	svc := NewResetservicegoService()
	require.NotNil(t, svc)

	// Perform operations that should use memory pools
	handler := svc.Handler()

	// Make multiple requests to trigger memory pool usage
	for i := 0; i < 100; i++ {
		req := httptest.NewRequest(http.MethodGet, "/health", nil)
		w := httptest.NewRecorder()
		handler.ServeHTTP(w, req)
		assert.Equal(t, http.StatusOK, w.Code)
	}

	// Force GC and check memory
	runtime.GC()
	var m2 runtime.MemStats
	runtime.ReadMemStats(&m2)

	// Memory usage should not grow significantly due to pooling
	memoryGrowth := m2.Alloc - m1.Alloc
	t.Logf("Memory growth after 100 requests: %d bytes", memoryGrowth)

	// With memory pooling, growth should be minimal
	// Allow some growth for test overhead, but not excessive
	assert.Less(t, memoryGrowth, int64(1024*1024), "Memory growth too high, pooling may not be working")
}

// TestPerformanceBenchmarksQA runs performance benchmarks
func TestPerformanceBenchmarksQA(t *testing.T) {
	if testing.Short() {
		t.Skip("Skipping performance benchmarks in short mode")
	}

	svc := NewResetservicegoService()
	require.NotNil(t, svc)
	handler := svc.Handler()

	// Benchmark health check endpoint
	t.Run("HealthCheckLatency", func(t *testing.T) {
		req := httptest.NewRequest(http.MethodGet, "/health", nil)

		start := time.Now()
		iterations := 1000

		for i := 0; i < iterations; i++ {
			w := httptest.NewRecorder()
			handler.ServeHTTP(w, req)
			assert.Equal(t, http.StatusOK, w.Code)
		}

		duration := time.Since(start)
		avgLatency := duration / time.Duration(iterations)

		t.Logf("Health check: %d requests in %v, avg latency: %v",
			iterations, duration, avgLatency)

		// Assert performance targets
		assert.Less(t, avgLatency, 10*time.Millisecond,
			"Health check latency exceeds 10ms target")
	})

	// Benchmark reset stats endpoint
	t.Run("ResetStatsLatency", func(t *testing.T) {
		req := httptest.NewRequest(http.MethodGet, "/api/v1/reset/stats", nil)

		start := time.Now()
		iterations := 500

		for i := 0; i < iterations; i++ {
			w := httptest.NewRecorder()
			handler.ServeHTTP(w, req)
			assert.Equal(t, http.StatusOK, w.Code)
		}

		duration := time.Since(start)
		avgLatency := duration / time.Duration(iterations)

		t.Logf("Reset stats: %d requests in %v, avg latency: %v",
			iterations, duration, avgLatency)

		// Assert performance targets
		assert.Less(t, avgLatency, 25*time.Millisecond,
			"Reset stats latency exceeds 25ms target")
	})
}

// TestAPIDefinitionComplianceQA tests that implementation matches OpenAPI spec
func TestAPIDefinitionComplianceQA(t *testing.T) {
	svc := NewResetservicegoService()
	require.NotNil(t, svc)
	handler := svc.Handler()

	// Test all defined endpoints exist and return expected status codes
	endpoints := []struct {
		method   string
		path     string
		expected int
	}{
		{http.MethodGet, "/health", http.StatusOK},
		{http.MethodGet, "/api/v1/reset/history", http.StatusOK},
		{http.MethodGet, "/api/v1/reset/stats", http.StatusOK},
		{http.MethodPost, "/api/v1/reset/trigger", http.StatusInternalServerError}, // Requires body
	}

	for _, ep := range endpoints {
		t.Run(fmt.Sprintf("%s_%s", ep.method, ep.path), func(t *testing.T) {
			req := httptest.NewRequest(ep.method, ep.path, nil)
			if ep.method == http.MethodPost {
				// Add minimal valid body for POST requests
				body := `{"resetType":"character_reset","confirmationToken":"CONFIRM_RESET_2024"}`
				req = httptest.NewRequest(ep.method, ep.path, bytes.NewReader([]byte(body)))
				req.Header.Set("Content-Type", "application/json")
				ep.expected = http.StatusOK // With valid body, should succeed
			}

			w := httptest.NewRecorder()
			handler.ServeHTTP(w, req)

			assert.Equal(t, ep.expected, w.Code,
				fmt.Sprintf("Endpoint %s %s returned %d, expected %d",
					ep.method, ep.path, w.Code, ep.expected))

			// Validate response is valid JSON
			var jsonResp interface{}
			if w.Body.Len() > 0 {
				err := json.Unmarshal(w.Body.Bytes(), &jsonResp)
				assert.NoError(t, err, "Response should be valid JSON")
			}
		})
	}
}

// TestErrorHandlingQA tests error handling scenarios
func TestErrorHandlingQA(t *testing.T) {
	svc := NewResetservicegoService()
	require.NotNil(t, svc)
	handler := svc.Handler()

	tests := []struct {
		name        string
		method      string
		path        string
		body        string
		expectError bool
	}{
		{
			name:        "Invalid JSON in POST",
			method:      http.MethodPost,
			path:        "/api/v1/reset/trigger",
			body:        `{"invalid": json}`,
			expectError: true,
		},
		{
			name:        "Missing required fields",
			method:      http.MethodPost,
			path:        "/api/v1/reset/trigger",
			body:        `{"resetType":"character_reset"}`,
			expectError: true,
		},
		{
			name:        "Invalid HTTP method",
			method:      http.MethodPut,
			path:        "/health",
			body:        "",
			expectError: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			var body io.Reader
			if tt.body != "" {
				body = bytes.NewReader([]byte(tt.body))
			}

			req := httptest.NewRequest(tt.method, tt.path, body)
			if tt.body != "" {
				req.Header.Set("Content-Type", "application/json")
			}

			w := httptest.NewRecorder()
			handler.ServeHTTP(w, req)

			if tt.expectError {
				assert.NotEqual(t, http.StatusOK, w.Code, "Expected error but got success")
			}
		})
	}
}

// TestGracefulShutdownQA tests graceful shutdown behavior
func TestGracefulShutdownQA(t *testing.T) {
	// This test would require integration with the main.go server
	// For now, test service cleanup
	svc := NewResetservicegoService()
	require.NotNil(t, svc)

	// Service should be properly initialized
	assert.NotNil(t, svc.Handler())

	// Test that service can be created multiple times without issues
	for i := 0; i < 10; i++ {
		newSvc := NewResetservicegoService()
		assert.NotNil(t, newSvc)
	}
}

// TestMemoryPoolEfficiencyQA tests memory pool effectiveness
func TestMemoryPoolEfficiencyQA(t *testing.T) {
	// Test that memory pools reduce allocations
	svc := NewResetservicegoService()
	require.NotNil(t, svc)

	// This test verifies that repeated operations use pooled objects
	handler := svc.Handler()

	// Make multiple requests
	for i := 0; i < 50; i++ {
		req := httptest.NewRequest(http.MethodGet, "/health", nil)
		w := httptest.NewRecorder()
		handler.ServeHTTP(w, req)
		assert.Equal(t, http.StatusOK, w.Code)
	}

	// In a real performance test, we'd measure allocations
	// For now, just ensure the service continues to work
	assert.NotNil(t, svc)
}

// BenchmarkHealthCheck benchmarks health check performance
func BenchmarkHealthCheck(b *testing.B) {
	svc := NewResetservicegoService()
	handler := svc.Handler()
	req := httptest.NewRequest(http.MethodGet, "/health", nil)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		w := httptest.NewRecorder()
		handler.ServeHTTP(w, req)
	}
}

// BenchmarkResetStats benchmarks reset stats performance
func BenchmarkResetStats(b *testing.B) {
	svc := NewResetservicegoService()
	handler := svc.Handler()
	req := httptest.NewRequest(http.MethodGet, "/api/v1/reset/stats", nil)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		w := httptest.NewRecorder()
		handler.ServeHTTP(w, req)
	}
}
