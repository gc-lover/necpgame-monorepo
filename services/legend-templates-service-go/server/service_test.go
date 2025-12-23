// Issue: #2241
package server

import (
	"context"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/legend-templates-service-go/pkg/api"
)

func TestLegendTemplatesService_HealthCheck(t *testing.T) {
	// Create mock service (simplified for testing)
	service := &LegendTemplatesService{
		metrics: NewMetricsCollector(),
	}

	ctx := context.Background()
	response, err := service.HealthCheck(ctx)

	if err != nil {
		t.Fatalf("HealthCheck failed: %v", err)
	}

	if response.Service != "legend-templates-service" {
		t.Errorf("Expected service name 'legend-templates-service', got %s", response.Service)
	}

	if response.Status != "healthy" {
		t.Errorf("Expected healthy status, got %s", response.Status)
	}
}

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

	if count, exists := allMetrics["requests:test_operation"]; !exists || count != 1 {
		t.Errorf("Expected 1 request recorded, got %v", count)
	}

	if latency, exists := allMetrics["latency_avg:test_operation"]; !exists || latency != 100 {
		t.Errorf("Expected 100ms average latency, got %v", latency)
	}
}

func TestMetricsCollector_RecordSuccess(t *testing.T) {
	metrics := NewMetricsCollector()

	metrics.RecordSuccess("generate_legend")
	metrics.RecordSuccess("generate_legend")
	metrics.RecordSuccess("create_template")

	allMetrics := metrics.GetMetrics()

	if legends, exists := allMetrics["legends_generated"]; !exists || legends != 2 {
		t.Errorf("Expected 2 legends generated, got %v", legends)
	}

	if templates, exists := allMetrics["templates_created"]; !exists || templates != 1 {
		t.Errorf("Expected 1 template created, got %v", templates)
	}
}

func BenchmarkHealthCheck(b *testing.B) {
	service := &LegendTemplatesService{
		metrics: NewMetricsCollector(),
	}

	ctx := context.Background()

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_, err := service.HealthCheck(ctx)
		if err != nil {
			b.Fatalf("HealthCheck failed: %v", err)
		}
	}
}

func BenchmarkRateLimiter(b *testing.B) {
	limiter := NewRateLimiter(1000, time.Minute)

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		limiter.Allow("benchmark-user")
	}
}
