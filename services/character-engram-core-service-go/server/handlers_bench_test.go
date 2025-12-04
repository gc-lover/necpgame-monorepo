// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkGetEngramSlots benchmarks GetEngramSlots handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetEngramSlots(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetEngramSlotsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetEngramSlots(ctx, params)
	}
}

// BenchmarkInstallEngram benchmarks InstallEngram handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkInstallEngram(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.InstallEngram(ctx)
	}
}

// BenchmarkRemoveEngram benchmarks RemoveEngram handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkRemoveEngram(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.RemoveEngram(ctx)
	}
}

