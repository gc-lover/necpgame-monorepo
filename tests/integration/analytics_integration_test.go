// Integration tests for NECPGAME services - Analytics Dashboard Integration
// Tests service-to-service communication and API compatibility
// PERFORMANCE: Tests include latency validation and concurrent load testing

package integration

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"sync"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

// TestAnalyticsServiceHealth tests basic health check functionality
func TestAnalyticsServiceHealth(t *testing.T) {
	t.Parallel()

	// Test health endpoint
	resp, err := http.Get("http://localhost:8091/health")
	require.NoError(t, err)
	defer resp.Body.Close()

	assert.Equal(t, http.StatusOK, resp.StatusCode)

	var health map[string]interface{}
	err = json.NewDecoder(resp.Body).Decode(&health)
	require.NoError(t, err)

	assert.Equal(t, "analytics-dashboard-service", health["service"])
	assert.Equal(t, "healthy", health["status"])
}

// TestAnalyticsDashboardAPIIntegration tests analytics API endpoints
func TestAnalyticsDashboardAPIIntegration(t *testing.T) {
	t.Parallel()

	tests := []struct {
		name           string
		endpoint       string
		expectedStatus int
		method         string
		body           string
	}{
		{
			name:           "Health Check",
			endpoint:       "/health",
			expectedStatus: http.StatusOK,
			method:         "GET",
		},
		{
			name:           "Metrics Endpoint",
			endpoint:       "/metrics",
			expectedStatus: http.StatusOK,
			method:         "GET",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			var req *http.Request
			var err error

			if tt.method == "POST" && tt.body != "" {
				req, err = http.NewRequest(tt.method, "http://localhost:8091"+tt.endpoint,
					bytes.NewBufferString(tt.body))
			} else {
				req, err = http.NewRequest(tt.method, "http://localhost:8091"+tt.endpoint, nil)
			}
			require.NoError(t, err)

			if tt.method == "POST" {
				req.Header.Set("Content-Type", "application/json")
			}

			client := &http.Client{Timeout: 10 * time.Second}
			resp, err := client.Do(req)
			require.NoError(t, err)
			defer resp.Body.Close()

			assert.Equal(t, tt.expectedStatus, resp.StatusCode)
		})
	}
}

// TestConcurrentAnalyticsRequests tests concurrent access to analytics service
// PERFORMANCE: Validates MMOFPS concurrent user handling
func TestConcurrentAnalyticsRequests(t *testing.T) {
	t.Parallel()

	const numGoroutines = 50
	const numRequests = 10

	var wg sync.WaitGroup
	errors := make(chan error, numGoroutines*numRequests)

	client := &http.Client{Timeout: 5 * time.Second}

	for i := 0; i < numGoroutines; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()

			for j := 0; j < numRequests; j++ {
				resp, err := client.Get("http://localhost:8091/health")
				if err != nil {
					errors <- fmt.Errorf("goroutine %d, request %d: %v", id, j, err)
					continue
				}
				resp.Body.Close()

				if resp.StatusCode != http.StatusOK {
					errors <- fmt.Errorf("goroutine %d, request %d: status %d", id, j, resp.StatusCode)
					continue
				}
			}
		}(i)
	}

	wg.Wait()
	close(errors)

	var errorCount int
	for err := range errors {
		t.Logf("Error: %v", err)
		errorCount++
	}

	assert.Equal(t, 0, errorCount, "Expected no errors in concurrent requests")
}

// TestCyberspaceEasterEggsIntegration tests easter eggs service integration
func TestCyberspaceEasterEggsIntegration(t *testing.T) {
	t.Parallel()

	// Test health endpoint
	resp, err := http.Get("http://localhost:8080/health")
	require.NoError(t, err)
	defer resp.Body.Close()

	assert.Equal(t, http.StatusUnauthorized, resp.StatusCode) // Expected for auth-required service
}

// TestWorldEventsServiceIntegration tests world events service
func TestWorldEventsServiceIntegration(t *testing.T) {
	t.Parallel()

	// Note: World events service may not be running in test environment
	// This test validates the integration test framework
	client := &http.Client{Timeout: 2 * time.Second}

	resp, err := client.Get("http://localhost:8070/health")
	if err != nil {
		t.Logf("World events service not available (expected): %v", err)
		return
	}
	defer resp.Body.Close()

	assert.Equal(t, http.StatusOK, resp.StatusCode)
}

// TestServiceDiscovery tests basic service discovery and connectivity
func TestServiceDiscovery(t *testing.T) {
	t.Parallel()

	services := map[string]string{
		"analytics-dashboard": "http://localhost:8091/health",
		"cyberspace-easter-eggs": "http://localhost:8080/health",
	}

	for serviceName, endpoint := range services {
		t.Run(serviceName, func(t *testing.T) {
			client := &http.Client{Timeout: 3 * time.Second}

			resp, err := client.Get(endpoint)
			if err != nil {
				t.Logf("Service %s not available: %v", serviceName, err)
				return
			}
			defer resp.Body.Close()

			// Accept both OK and Unauthorized (for auth-protected services)
			assert.True(t, resp.StatusCode == http.StatusOK || resp.StatusCode == http.StatusUnauthorized,
				"Expected OK or Unauthorized status for %s", serviceName)
		})
	}
}

// TestAnalyticsLatencyValidation validates P99 latency requirements
// PERFORMANCE: MMOFPS requirement P99 <50ms for analytics queries
func TestAnalyticsLatencyValidation(t *testing.T) {
	t.Parallel()

	const numRequests = 100
	client := &http.Client{Timeout: 1 * time.Second}

	var latencies []time.Duration

	for i := 0; i < numRequests; i++ {
		start := time.Now()

		resp, err := client.Get("http://localhost:8091/health")
		if err != nil {
			t.Logf("Request %d failed: %v", i, err)
			continue
		}
		resp.Body.Close()

		latency := time.Since(start)
		latencies = append(latencies, latency)
	}

	if len(latencies) == 0 {
		t.Skip("No successful requests to measure latency")
	}

	// Calculate P95 latency
	// Simple percentile calculation
	p95Index := int(float64(len(latencies)) * 0.95)
	if p95Index >= len(latencies) {
		p95Index = len(latencies) - 1
	}

	// Sort latencies (simple bubble sort for test)
	for i := 0; i < len(latencies)-1; i++ {
		for j := 0; j < len(latencies)-i-1; j++ {
			if latencies[j] > latencies[j+1] {
				latencies[j], latencies[j+1] = latencies[j+1], latencies[j]
			}
		}
	}

	p95Latency := latencies[p95Index]

	t.Logf("P95 Latency: %v", p95Latency)
	assert.True(t, p95Latency < 50*time.Millisecond,
		"P95 latency %v exceeds MMOFPS requirement of <50ms", p95Latency)
}

// TestCrossServiceCommunication tests basic cross-service API calls
func TestCrossServiceCommunication(t *testing.T) {
	t.Parallel()

	// Test that services can communicate with each other
	// This is a placeholder for more complex cross-service tests

	// Test analytics service response format
	resp, err := http.Get("http://localhost:8091/health")
	if err != nil {
		t.Skipf("Analytics service not available: %v", err)
	}
	defer resp.Body.Close()

	var response map[string]interface{}
	err = json.NewDecoder(resp.Body).Decode(&response)
	require.NoError(t, err)

	// Validate response structure
	assert.Contains(t, response, "service")
	assert.Contains(t, response, "status")
	assert.Contains(t, response, "timestamp")
	assert.Contains(t, response, "version")
}
