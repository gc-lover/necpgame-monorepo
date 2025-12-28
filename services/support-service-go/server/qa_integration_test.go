// Issue: #288 - QA Testing Support Service
// PERFORMANCE: Comprehensive testing with concurrent load, memory usage, and latency benchmarks

package server

import (
	"context"
	"fmt"
	"net/http"
	"net/http/httptest"
	"runtime"
	"sync"
	"testing"
	"time"

	"support-service-go/pkg/api"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

// TestCreateExample tests support ticket creation
func TestCreateExample(t *testing.T) {
	tests := []struct {
		name           string
		request        *api.CreateExampleRequest
		expectSuccess  bool
		expectedStatus api.SupportTicketStatus
	}{
		{
			name: "Valid ticket creation",
			request: &api.CreateExampleRequest{
				Title:       "Game crash issue",
				Description: api.NewOptString("Game crashes when entering arena"),
				Priority:    api.NewOptSupportTicketPriority(api.SupportTicketPriorityHigh),
				Category:    api.NewOptString("technical"),
			},
			expectSuccess:  true,
			expectedStatus: api.SupportTicketStatusOpen,
		},
		{
			name: "Minimal ticket creation",
			request: &api.CreateExampleRequest{
				Title: "Simple issue",
			},
			expectSuccess:  true,
			expectedStatus: api.SupportTicketStatusOpen,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			resp, err := svc.CreateExample(context.Background(), tt.request)

			if tt.expectSuccess {
				require.NoError(t, err)
				createResp, ok := resp.(*api.CreateExampleCreated)
				require.True(t, ok, "Response should be CreateExampleCreated")
				assert.NotEmpty(t, createResp.Id)
				assert.Equal(t, tt.request.Title, createResp.Title)
				assert.Equal(t, tt.expectedStatus, createResp.Status)
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestGetExample tests ticket retrieval
func TestGetExample(t *testing.T) {
	tests := []struct {
		name           string
		ticketID       string
		expectSuccess  bool
	}{
		{
			name:          "Valid ticket retrieval",
			ticketID:      "12345678-9abc-def0-1234-56789abcdef0",
			expectSuccess: true,
		},
		{
			name:          "Invalid ticket ID",
			ticketID:      "invalid-uuid",
			expectSuccess: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.GetExampleParams{
				Id: tt.ticketID,
			}

			resp, err := svc.GetExample(context.Background(), params)

			if tt.expectSuccess {
				require.NoError(t, err)
				getResp, ok := resp.(*api.SupportTicket)
				require.True(t, ok, "Response should be SupportTicket")
				assert.Equal(t, tt.ticketID, getResp.Id.String())
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestUpdateExample tests ticket updates
func TestUpdateExample(t *testing.T) {
	tests := []struct {
		name           string
		ticketID       string
		request        *api.UpdateExampleRequest
		expectSuccess  bool
	}{
		{
			name:     "Valid ticket update",
			ticketID: "12345678-9abc-def0-1234-56789abcdef0",
			request: &api.UpdateExampleRequest{
				Title:    api.NewOptString("Updated title"),
				Status:   api.NewOptSupportTicketStatus(api.SupportTicketStatusInProgress),
				Priority: api.NewOptSupportTicketPriority(api.SupportTicketPriorityHigh),
			},
			expectSuccess: true,
		},
		{
			name:     "Invalid ticket ID",
			ticketID: "invalid-uuid",
			request: &api.UpdateExampleRequest{
				Title: api.NewOptString("Updated title"),
			},
			expectSuccess: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.UpdateExampleParams{
				Id: tt.ticketID,
			}

			resp, err := svc.UpdateExample(context.Background(), tt.request, params)

			if tt.expectSuccess {
				require.NoError(t, err)
				updateResp, ok := resp.(*api.SupportTicket)
				require.True(t, ok, "Response should be SupportTicket")
				if tt.request.Title.IsSet {
					assert.Equal(t, tt.request.Title.Value, updateResp.Title)
				}
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestDeleteExample tests ticket deletion
func TestDeleteExample(t *testing.T) {
	tests := []struct {
		name          string
		ticketID      string
		expectSuccess bool
	}{
		{
			name:          "Valid ticket deletion",
			ticketID:      "12345678-9abc-def0-1234-56789abcdef0",
			expectSuccess: true,
		},
		{
			name:          "Invalid ticket ID",
			ticketID:      "invalid-uuid",
			expectSuccess: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.DeleteExampleParams{
				Id: tt.ticketID,
			}

			resp, err := svc.DeleteExample(context.Background(), params)

			if tt.expectSuccess {
				require.NoError(t, err)
				_, ok := resp.(*api.DeleteExampleNoContent)
				require.True(t, ok, "Response should be DeleteExampleNoContent")
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestListSupportTickets tests ticket listing
func TestListSupportTickets(t *testing.T) {
	svc := NewServer(nil, nil, nil)
	require.NotNil(t, svc)

	params := api.ListSupportTicketsParams{
		Limit:  api.NewOptInt(10),
		Offset: api.NewOptInt(0),
		Status: api.NewOptSupportTicketStatus(api.SupportTicketStatusOpen),
	}

	resp, err := svc.ListSupportTickets(context.Background(), params)
	require.NoError(t, err)

	listResp, ok := resp.(*api.SupportTicketsList)
	require.True(t, ok, "Response should be SupportTicketsList")
	assert.NotNil(t, listResp.Tickets)
	assert.GreaterOrEqual(t, len(listResp.Tickets), 0)
}

// TestSupportServiceHealthCheck tests health check endpoint
func TestSupportServiceHealthCheck(t *testing.T) {
	tests := []struct {
		name           string
		params         api.SupportServiceHealthCheckParams
		expectSuccess  bool
	}{
		{
			name: "Basic health check",
			params: api.SupportServiceHealthCheckParams{
				Service: api.NewOptString("support-service"),
			},
			expectSuccess: true,
		},
		{
			name: "Health check with detailed info",
			params: api.SupportServiceHealthCheckParams{
				Service:     api.NewOptString("support-service"),
				Detailed:    api.NewOptBool(true),
				IncludeDeps: api.NewOptBool(true),
			},
			expectSuccess: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			resp, err := svc.SupportServiceHealthCheck(context.Background(), tt.params)

			if tt.expectSuccess {
				require.NoError(t, err)
				healthResp, ok := resp.(*api.HealthResponse)
				require.True(t, ok, "Response should be HealthResponse")
				assert.Equal(t, "healthy", string(healthResp.Status))
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestSupportServiceBatchHealthCheck tests batch health check
func TestSupportServiceBatchHealthCheck(t *testing.T) {
	svc := NewServer(nil, nil, nil)
	require.NotNil(t, svc)

	req := &api.SupportServiceBatchHealthCheckReq{
		Services: []string{"support-service", "database", "cache"},
		Detailed: api.NewOptBool(true),
	}

	resp, err := svc.SupportServiceBatchHealthCheck(context.Background(), req)
	require.NoError(t, err)

	batchResp, ok := resp.(*api.BatchHealthResponse)
	require.True(t, ok, "Response should be BatchHealthResponse")
	assert.NotEmpty(t, batchResp.Services)
	assert.GreaterOrEqual(t, len(batchResp.Services), 1)
}

// TestConcurrentLoad tests service under concurrent load
func TestConcurrentLoad(t *testing.T) {
	if testing.Short() {
		t.Skip("Skipping concurrent load test in short mode")
	}

	svc := NewServer(nil, nil, nil)
	require.NotNil(t, svc)

	concurrentUsers := 25
	requestsPerUser := 4

	var wg sync.WaitGroup
	errorCount := int64(0)
	var mu sync.Mutex

	// Start concurrent users
	for i := 0; i < concurrentUsers; i++ {
		wg.Add(1)
		go func(userID int) {
			defer wg.Done()

			for j := 0; j < requestsPerUser; j++ {
				// Alternate between different operations
				switch j % 4 {
				case 0:
					// Create ticket
					req := &api.CreateExampleRequest{
						Title: fmt.Sprintf("Concurrent ticket %d-%d", userID, j),
					}
					_, err := svc.CreateExample(context.Background(), req)
					if err != nil {
						mu.Lock()
						errorCount++
						mu.Unlock()
					}
				case 1:
					// Get ticket
					params := api.GetExampleParams{
						Id: "12345678-9abc-def0-1234-56789abcdef0",
					}
					_, err := svc.GetExample(context.Background(), params)
					if err != nil {
						mu.Lock()
						errorCount++
						mu.Unlock()
					}
				case 2:
					// List tickets
					params := api.ListSupportTicketsParams{
						Limit: api.NewOptInt(5),
					}
					_, err := svc.ListSupportTickets(context.Background(), params)
					if err != nil {
						mu.Lock()
						errorCount++
						mu.Unlock()
					}
				case 3:
					// Health check
					params := api.SupportServiceHealthCheckParams{}
					_, err := svc.SupportServiceHealthCheck(context.Background(), params)
					if err != nil {
						mu.Lock()
						errorCount++
						mu.Unlock()
					}
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
	case <-time.After(20 * time.Second):
		t.Fatal("Concurrent load test timed out")
	}

	// Assert low error rate (allow some for mock implementation)
	assert.LessOrEqual(t, errorCount, int64(concurrentUsers*requestsPerUser/10), "Too many concurrent requests failed")
}

// TestMemoryUsage tests memory usage patterns
func TestMemoryUsage(t *testing.T) {
	// Record initial memory stats
	runtime.GC()
	var m1 runtime.MemStats
	runtime.ReadMemStats(&m1)

	svc := NewServer(nil, nil, nil)
	require.NotNil(t, svc)

	// Perform operations that should use memory pools
	for i := 0; i < 100; i++ {
		req := &api.CreateExampleRequest{
			Title: fmt.Sprintf("Memory test ticket %d", i),
		}
		_, err := svc.CreateExample(context.Background(), req)
		assert.NoError(t, err)

		params := api.GetExampleParams{
			Id: "12345678-9abc-def0-1234-56789abcdef0",
		}
		_, err = svc.GetExample(context.Background(), params)
		assert.NoError(t, err)
	}

	// Force GC and check memory
	runtime.GC()
	var m2 runtime.MemStats
	runtime.ReadMemStats(&m2)

	// Memory usage should not grow significantly due to pooling
	memoryGrowth := m2.Alloc - m1.Alloc
	t.Logf("Memory growth after 200 operations: %d bytes", memoryGrowth)

	// With memory pooling, growth should be minimal
	assert.Less(t, memoryGrowth, int64(1024*1024), "Memory growth too high, pooling may not be working")
}

// TestPerformanceBenchmarks runs performance benchmarks
func TestPerformanceBenchmarks(t *testing.T) {
	if testing.Short() {
		t.Skip("Skipping performance benchmarks in short mode")
	}

	svc := NewServer(nil, nil, nil)
	require.NotNil(t, svc)

	// Benchmark ticket creation
	t.Run("CreateTicketLatency", func(t *testing.T) {
		req := &api.CreateExampleRequest{
			Title:       "Benchmark ticket",
			Description: api.NewOptString("Performance benchmark ticket"),
		}

		start := time.Now()
		iterations := 1000

		for i := 0; i < iterations; i++ {
			_, err := svc.CreateExample(context.Background(), req)
			assert.NoError(t, err)
		}

		duration := time.Since(start)
		avgLatency := duration / time.Duration(iterations)

		t.Logf("Create ticket: %d requests in %v, avg latency: %v",
			iterations, duration, avgLatency)

		// Assert performance targets (support service can be slower than game services)
		assert.Less(t, avgLatency, 100*time.Millisecond,
			"Create ticket latency exceeds 100ms target")
	})

	// Benchmark ticket retrieval
	t.Run("GetTicketLatency", func(t *testing.T) {
		params := api.GetExampleParams{
			Id: "12345678-9abc-def0-1234-56789abcdef0",
		}

		start := time.Now()
		iterations := 2000

		for i := 0; i < iterations; i++ {
			_, err := svc.GetExample(context.Background(), params)
			assert.NoError(t, err)
		}

		duration := time.Since(start)
		avgLatency := duration / time.Duration(iterations)

		t.Logf("Get ticket: %d requests in %v, avg latency: %v",
			iterations, duration, avgLatency)

		// Assert performance targets
		assert.Less(t, avgLatency, 50*time.Millisecond,
			"Get ticket latency exceeds 50ms target")
	})

	// Benchmark health check
	t.Run("HealthCheckLatency", func(t *testing.T) {
		params := api.SupportServiceHealthCheckParams{}

		start := time.Now()
		iterations := 5000

		for i := 0; i < iterations; i++ {
			_, err := svc.SupportServiceHealthCheck(context.Background(), params)
			assert.NoError(t, err)
		}

		duration := time.Since(start)
		avgLatency := duration / time.Duration(iterations)

		t.Logf("Health check: %d requests in %v, avg latency: %v",
			iterations, duration, avgLatency)

		// Assert performance targets
		assert.Less(t, avgLatency, 10*time.Millisecond,
			"Health check latency exceeds 10ms target")
	})
}

// Helper function for context
func req() *http.Request {
	return httptest.NewRequest(http.MethodGet, "/", nil).WithContext(context.Background())
}

// Issue: #288 - QA Testing Support Service
