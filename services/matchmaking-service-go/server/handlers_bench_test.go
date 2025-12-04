// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkEnterQueue benchmarks EnterQueue handler
// Target: <100μs per operation, minimal allocs
func BenchmarkEnterQueue(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.EnterQueue(ctx)
	}
}

// BenchmarkGetQueueStatus benchmarks GetQueueStatus handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetQueueStatus(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetQueueStatusParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetQueueStatus(ctx, params)
	}
}

// BenchmarkLeaveQueue benchmarks LeaveQueue handler
// Target: <100μs per operation, minimal allocs
func BenchmarkLeaveQueue(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.LeaveQueue(ctx)
	}
}

