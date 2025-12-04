// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"
)

// BenchmarkGetResetStats benchmarks GetResetStats handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetResetStats(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetResetStatsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetResetStats(ctx, params)
	}
}

// BenchmarkGetResetHistory benchmarks GetResetHistory handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetResetHistory(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetResetHistoryParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetResetHistory(ctx, params)
	}
}

// BenchmarkTriggerReset benchmarks TriggerReset handler
// Target: <100μs per operation, minimal allocs
func BenchmarkTriggerReset(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.TriggerReset(ctx)
	}
}

