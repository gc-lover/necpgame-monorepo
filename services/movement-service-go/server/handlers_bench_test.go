// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkGetPosition benchmarks GetPosition handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPosition(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetPositionParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPosition(ctx, params)
	}
}

// BenchmarkSavePosition benchmarks SavePosition handler
// Target: <100μs per operation, minimal allocs
func BenchmarkSavePosition(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.SavePosition(ctx)
	}
}

// BenchmarkGetPositionHistory benchmarks GetPositionHistory handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPositionHistory(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetPositionHistoryParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPositionHistory(ctx, params)
	}
}

