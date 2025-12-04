// Issue: Performance benchmarks
package server

import (
	"context"
	"// Issue: #1595/pkg/api"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkGetCurrentTurn benchmarks GetCurrentTurn handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetCurrentTurn(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetCurrentTurnParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetCurrentTurn(ctx, params)
	}
}

// BenchmarkGetTurnOrder benchmarks GetTurnOrder handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetTurnOrder(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetTurnOrderParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetTurnOrder(ctx, params)
	}
}

// BenchmarkNextTurn benchmarks NextTurn handler
// Target: <100μs per operation, minimal allocs
func BenchmarkNextTurn(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.NextTurn(ctx)
	}
}

