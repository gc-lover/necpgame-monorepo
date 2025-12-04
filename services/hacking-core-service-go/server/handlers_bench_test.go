// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"
)

// BenchmarkInitiateHack benchmarks InitiateHack handler
// Target: <100μs per operation, minimal allocs
func BenchmarkInitiateHack(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.InitiateHack(ctx)
	}
}

// BenchmarkCancelHack benchmarks CancelHack handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCancelHack(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CancelHack(ctx)
	}
}

// BenchmarkExecuteHack benchmarks ExecuteHack handler
// Target: <100μs per operation, minimal allocs
func BenchmarkExecuteHack(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ExecuteHack(ctx)
	}
}

