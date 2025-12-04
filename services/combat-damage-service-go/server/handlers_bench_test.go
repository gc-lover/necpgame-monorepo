// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-damage-service-go/pkg/api"
)

// BenchmarkCalculateDamage benchmarks CalculateDamage handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCalculateDamage(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.DamageCalculationRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CalculateDamage(ctx, req)
	}
}

// BenchmarkApplyEffects benchmarks ApplyEffects handler
// Target: <100μs per operation, minimal allocs
func BenchmarkApplyEffects(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.ApplyEffectsRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ApplyEffects(ctx, req)
	}
}

// BenchmarkRemoveEffect benchmarks RemoveEffect handler
// Target: <100μs per operation, minimal allocs
func BenchmarkRemoveEffect(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	params := api.RemoveEffectParams{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.RemoveEffect(ctx, params)
	}
}
