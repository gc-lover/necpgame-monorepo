// Issue: #2236
package server

import (
	"testing"
	"time"
)

// TestTradingCoreService_HealthCheck skipped - requires database/redis setup
// Integration tests should be run separately with proper environment

func TestRateLimiter_Allow(t *testing.T) {
	limiter := NewRateLimiter(2, time.Minute)

	key := "test-user"

	// First request should be allowed
	if !limiter.Allow(key) {
		t.Error("First request should be allowed")
	}

	// Second request should be allowed
	if !limiter.Allow(key) {
		t.Error("Second request should be allowed")
	}

	// Third request should be denied
	if limiter.Allow(key) {
		t.Error("Third request should be denied")
	}
}

func TestMetricsCollector_RecordDuration(t *testing.T) {
	metrics := NewMetricsCollector()

	duration := 100 * time.Millisecond
	metrics.RecordDuration("test_operation", duration)

	// Check that metrics were recorded
	allMetrics := metrics.GetMetrics()

	if count, exists := allMetrics["requests:test_operation"]; !exists || count.(int64) != 1 {
		t.Errorf("Expected 1 request recorded, got %v", count)
	}

	if latency, exists := allMetrics["latency_avg:test_operation"]; !exists || latency.(int64) != 100 {
		t.Errorf("Expected 100ms average latency, got %v", latency)
	}
}

func TestMetricsCollector_RecordSuccess(t *testing.T) {
	metrics := NewMetricsCollector()

	metrics.RecordSuccess("execute_trade")
	metrics.RecordSuccess("execute_trade")
	metrics.RecordSuccess("create_listing")

	allMetrics := metrics.GetMetrics()

	if trades, exists := allMetrics["trades_executed"]; !exists || trades.(int64) != 2 {
		t.Errorf("Expected 2 trades executed, got %v", trades)
	}

	if listings, exists := allMetrics["listings_created"]; !exists || listings.(int64) != 1 {
		t.Errorf("Expected 1 listing created, got %v", listings)
	}
}

// BenchmarkHealthCheck skipped - requires database/redis setup
// Integration benchmarks should be run separately with proper environment

func BenchmarkRateLimiter(b *testing.B) {
	limiter := NewRateLimiter(1000, time.Minute)

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		limiter.Allow("benchmark-user")
	}
}
