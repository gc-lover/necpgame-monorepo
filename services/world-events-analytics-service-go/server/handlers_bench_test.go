// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-analytics-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkGetWorldEventMetrics benchmarks GetWorldEventMetrics handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetWorldEventMetrics(b *testing.B) {
	logger := GetLogger()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	params := api.GetWorldEventMetricsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetWorldEventMetrics(ctx, params)
	}
}

// BenchmarkGetWorldEventEngagement benchmarks GetWorldEventEngagement handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetWorldEventEngagement(b *testing.B) {
	logger := GetLogger()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	params := api.GetWorldEventEngagementParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetWorldEventEngagement(ctx, params)
	}
}

// BenchmarkGetWorldEventImpact benchmarks GetWorldEventImpact handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetWorldEventImpact(b *testing.B) {
	logger := GetLogger()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	params := api.GetWorldEventImpactParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetWorldEventImpact(ctx, params)
	}
}

