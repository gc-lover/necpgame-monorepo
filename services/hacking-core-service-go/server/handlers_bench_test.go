// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkInitiateHack benchmarks InitiateHack handler
// Target: <100Ојs per operation, minimal allocs
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
// Target: <100Ојs per operation, minimal allocs
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
// Target: <100Ојs per operation, minimal allocs
func BenchmarkExecuteHack(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ExecuteHack(ctx)
	}
}

