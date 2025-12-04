// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"
)

// BenchmarkCalculateDamage benchmarks CalculateDamage handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCalculateDamage(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CalculateDamage(ctx)
	}
}

// BenchmarkApplyEffects benchmarks ApplyEffects handler
// Target: <100μs per operation, minimal allocs
func BenchmarkApplyEffects(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ApplyEffects(ctx)
	}
}

// BenchmarkRemoveEffect benchmarks RemoveEffect handler
// Target: <100μs per operation, minimal allocs
func BenchmarkRemoveEffect(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.RemoveEffect(ctx)
	}
}

