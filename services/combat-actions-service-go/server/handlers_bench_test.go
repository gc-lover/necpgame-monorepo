// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-actions-service-go/pkg/api"
)

// BenchmarkApplyEffects benchmarks ApplyEffects handler
// Target: <100μs per operation, minimal allocs
func BenchmarkApplyEffects(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.ApplyEffectsRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ApplyEffects(ctx, req)
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

	req := &api.CalculateDamageRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CalculateDamage(ctx, req)
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

	req := &api.DefendRequest{}
	params := api.DefendInCombatParams{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DefendInCombat(ctx, req, params)
	}
}

