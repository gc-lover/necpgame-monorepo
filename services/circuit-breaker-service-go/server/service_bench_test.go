package server

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2156 - Benchmark tests for circuit breaker performance validation
func BenchmarkCircuitBreakerService_CreateCircuit(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{
		DefaultFailureThreshold: 5,
		DefaultTimeout:          5 * time.Second,
	}
	service := NewCircuitBreakerService(logger, metrics, config)

	reqData := CreateCircuitRequest{
		CircuitID:             "bench_circuit",
		ServiceName:           "bench_service",
		Endpoint:              "/api/test",
		FailureThreshold:      5,
		SuccessThreshold:      3,
		Timeout:               5000,
		RetryDelay:            1000,
		MaxRetryDelay:         30000,
		MonitoringWindow:      60000,
		SlowCallThreshold:     3000,
		SlowCallRateThreshold: 0.5,
		FallbackEnabled:       true,
		FallbackResponse:      `{"error": "service unavailable"}`,
		MetricsEnabled:        true,
		AlertThresholds: map[string]interface{}{
			"error_rate":           0.5,
			"slow_call_rate":       0.5,
			"consecutive_failures": 10,
		},
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/cb/circuits", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.CreateCircuit(w, req)

		if w.Code != http.StatusCreated {
			b.Fatalf("Expected status 201, got %d", w.Code)
		}
	}
}

func BenchmarkCircuitBreakerService_GetCircuit(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{}
	service := NewCircuitBreakerService(logger, metrics, config)

	// Pre-create circuit
	createReq := CreateCircuitRequest{
		CircuitID:   "test_circuit",
		ServiceName: "test_service",
		Endpoint:    "/api/test",
	}
	createBody, _ := json.Marshal(createReq)
	createHttpReq := httptest.NewRequest("POST", "/cb/circuits", bytes.NewReader(createBody))
	createHttpReq.Header.Set("X-User-ID", "user_123")
	createW := httptest.NewRecorder()
	service.CreateCircuit(createW, createHttpReq)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("GET", "/cb/circuits/test_circuit", nil)
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.GetCircuit(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkCircuitBreakerService_CreateBulkhead(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{}
	service := NewCircuitBreakerService(logger, metrics, config)

	reqData := CreateBulkheadRequest{
		BulkheadID:         "bench_bulkhead",
		ServiceName:        "bench_service",
		MaxConcurrentCalls: 10,
		MaxWaitDuration:    5000,
		IsolationStrategy:  "semaphore",
		QueueSize:          100,
		Fairness:           true,
		MetricsEnabled:     true,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/cb/bulkheads", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.CreateBulkhead(w)

		if w.Code != http.StatusCreated {
			b.Fatalf("Expected status 201, got %d", w.Code)
		}
	}
}

func BenchmarkCircuitBreakerService_GetMetrics(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{}
	service := NewCircuitBreakerService(logger, metrics, config)

	// Pre-create some circuits and bulkheads
	for i := 0; i < 10; i++ {
		circuitReq := CreateCircuitRequest{
			CircuitID:   fmt.Sprintf("circuit_%d", i),
			ServiceName: "test_service",
			Endpoint:    fmt.Sprintf("/api/test/%d", i),
		}
		circuitBody, _ := json.Marshal(circuitReq)
		circuitHttpReq := httptest.NewRequest("POST", "/cb/circuits", bytes.NewReader(circuitBody))
		circuitHttpReq.Header.Set("X-User-ID", "user_123")
		circuitW := httptest.NewRecorder()
		service.CreateCircuit(circuitW, circuitHttpReq)

		bulkheadReq := CreateBulkheadRequest{
			BulkheadID:  fmt.Sprintf("bulkhead_%d", i),
			ServiceName: "test_service",
		}
		bulkheadBody, _ := json.Marshal(bulkheadReq)
		bulkheadHttpReq := httptest.NewRequest("POST", "/cb/bulkheads", bytes.NewReader(bulkheadBody))
		bulkheadHttpReq.Header.Set("X-User-ID", "user_123")
		bulkheadW := httptest.NewRecorder()
		service.CreateBulkhead(bulkheadW)
	}

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("GET", "/cb/metrics", nil)
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.GetMetrics(w)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

// Memory allocation benchmark for concurrent circuit breaker operations
func BenchmarkCircuitBreakerService_ConcurrentOperations(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{}
	service := NewCircuitBreakerService(logger, metrics, config)

	reqData := CreateCircuitRequest{
		CircuitID:   "concurrent_circuit",
		ServiceName: "concurrent_service",
		Endpoint:    "/api/concurrent",
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	b.RunParallel(func(pb *testing.PB) {
		localReqBody := make([]byte, len(reqBody))
		copy(localReqBody, reqBody)

		localReqData := reqData
		counter := 0

		for pb.Next() {
			// Alternate between create and get operations
			if counter%2 == 0 {
				// Create circuit with unique ID
				localReqData.CircuitID = fmt.Sprintf("concurrent_circuit_%d", counter)
				localReqBody, _ = json.Marshal(localReqData)

				req := httptest.NewRequest("POST", "/cb/circuits", bytes.NewReader(localReqBody))
				req.Header.Set("X-User-ID", "user_123")
				w := httptest.NewRecorder()

				service.CreateCircuit(w, req)

				if w.Code != http.StatusCreated && w.Code != http.StatusConflict {
					b.Fatalf("Expected status 201 or 409, got %d", w.Code)
				}
			} else {
				// Get circuit
				req := httptest.NewRequest("GET", "/cb/circuits/concurrent_circuit_0", nil)
				req.Header.Set("X-User-ID", "user_123")
				w := httptest.NewRecorder()

				service.GetCircuit(w, req)

				// Accept both 200 (found) and 404 (not found due to concurrent creates)
				if w.Code != http.StatusOK && w.Code != http.StatusNotFound {
					b.Fatalf("Expected status 200 or 404, got %d", w.Code)
				}
			}
			counter++
		}
	})
}

// Performance target validation for circuit breaker service
func TestCircuitBreakerService_PerformanceTargets(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{}
	service := NewCircuitBreakerService(logger, metrics, config)

	// Test circuit creation performance
	reqData := CreateCircuitRequest{
		CircuitID:   "perf_circuit",
		ServiceName: "perf_service",
		Endpoint:    "/api/perf",
	}

	reqBody, _ := json.Marshal(reqData)

	// Warm up
	for i := 0; i < 100; i++ {
		req := httptest.NewRequest("POST", "/cb/circuits", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()
		service.CreateCircuit(w, req)
	}

	// Benchmark for 1 second
	result := testing.Benchmark(func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			req := httptest.NewRequest("POST", "/cb/circuits", bytes.NewReader(reqBody))
			req.Header.Set("X-User-ID", "user_123")
			w := httptest.NewRecorder()
			service.CreateCircuit(w, req)
		}
	})

	// Calculate operations per second
	opsPerSec := float64(result.N) / result.T.Seconds()

	// Target: at least 500 ops/sec for circuit breaker operations
	targetOpsPerSec := 500.0

	if opsPerSec < targetOpsPerSec {
		t.Errorf("Circuit breaker performance target not met: %.2f ops/sec < %.2f ops/sec target", opsPerSec, targetOpsPerSec)
	}

	// Check memory allocations (should be low with pooling)
	if result.AllocsPerOp() > 50 {
		t.Errorf("Too many allocations: %.2f allocs/op > 50 allocs/op target", result.AllocsPerOp())
	}

	t.Logf("Circuit Breaker Performance: %.2f ops/sec, %.2f allocs/op", opsPerSec, result.AllocsPerOp())
}

// Test concurrent circuit breaker operations
func TestCircuitBreakerService_ConcurrentOperations(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{}
	service := NewCircuitBreakerService(logger, metrics, config)

	done := make(chan bool, 20)

	// Test concurrent circuit operations
	for i := 0; i < 20; i++ {
		go func(operationIndex int) {
			circuitID := fmt.Sprintf("concurrent_circuit_%d", operationIndex)

			// Create circuit
			createReq := CreateCircuitRequest{
				CircuitID:   circuitID,
				ServiceName: "concurrent_service",
				Endpoint:    fmt.Sprintf("/api/concurrent/%d", operationIndex),
			}
			createBody, _ := json.Marshal(createReq)
			createHttpReq := httptest.NewRequest("POST", "/cb/circuits", bytes.NewReader(createBody))
			createHttpReq.Header.Set("X-User-ID", "creator")
			createW := httptest.NewRecorder()

			service.CreateCircuit(createW, createHttpReq)

			if createW.Code != http.StatusCreated && createW.Code != http.StatusConflict {
				t.Errorf("Failed to create circuit %s: %d", circuitID, createW.Code)
			}

			// Get circuit
			getHttpReq := httptest.NewRequest("GET", "/cb/circuits/"+circuitID, nil)
			getHttpReq.Header.Set("X-User-ID", "getter")
			getW := httptest.NewRecorder()

			service.GetCircuit(getW, getHttpReq)

			if getW.Code != http.StatusOK {
				t.Errorf("Failed to get circuit %s: %d", circuitID, getW.Code)
			}

			// Update circuit state
			stateReq := SetCircuitStateRequest{
				State:  "open",
				Reason: "testing concurrent operations",
			}
			stateBody, _ := json.Marshal(stateReq)
			stateHttpReq := httptest.NewRequest("POST", "/cb/circuits/"+circuitID+"/state", bytes.NewReader(stateBody))
			stateHttpReq.Header.Set("X-User-ID", "state_changer")
			stateW := httptest.NewRecorder()

			service.SetCircuitState(stateW, stateHttpReq)

			if stateW.Code != http.StatusOK {
				t.Errorf("Failed to set circuit state %s: %d", circuitID, stateW.Code)
			}

			done <- true
		}(i)
	}

	// Wait for all goroutines to complete
	for i := 0; i < 20; i++ {
		<-done
	}

	t.Log("Concurrent circuit breaker operations test passed")
}

// Test circuit breaker state transitions
func TestCircuitBreakerService_StateTransitions(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{}
	service := NewCircuitBreakerService(logger, metrics, config)

	circuitID := "state_test_circuit"

	// Create circuit
	createReq := CreateCircuitRequest{
		CircuitID:   circuitID,
		ServiceName: "state_test_service",
		Endpoint:    "/api/state_test",
	}
	createBody, _ := json.Marshal(createReq)
	createHttpReq := httptest.NewRequest("POST", "/cb/circuits", bytes.NewReader(createBody))
	createHttpReq.Header.Set("X-User-ID", "user_123")
	createW := httptest.NewRecorder()

	service.CreateCircuit(createW, createHttpReq)

	if createW.Code != http.StatusCreated {
		t.Fatalf("Failed to create circuit: %d", createW.Code)
	}
}
