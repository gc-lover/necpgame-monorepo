// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"
)

// BenchmarkApplyEffects benchmarks ApplyEffects handler
// Target: <100μs per operation, minimal allocs
func BenchmarkApplyEffects(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ApplyEffects(ctx)
	}
}

// BenchmarkCalculateDamage benchmarks CalculateDamage handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCalculateDamage(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CalculateDamage(ctx)
	}
}

// BenchmarkDefendInCombat benchmarks DefendInCombat handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDefendInCombat(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DefendInCombat(ctx)
	}
}

