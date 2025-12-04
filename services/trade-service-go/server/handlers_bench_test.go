// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkCreateTradeSession benchmarks CreateTradeSession handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCreateTradeSession(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.CreateTradeSessionRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreateTradeSession(ctx, req)
	}
}

// BenchmarkGetTradeSession benchmarks GetTradeSession handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetTradeSession(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetTradeSessionParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetTradeSession(ctx, params)
	}
}

// BenchmarkCancelTradeSession benchmarks CancelTradeSession handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCancelTradeSession(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CancelTradeSession(ctx)
	}
}

