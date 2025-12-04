// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-weapon-special-mechanics-service-go/pkg/api"
)

// BenchmarkApplySpecialMechanics benchmarks ApplySpecialMechanics handler
// Target: <100μs per operation, minimal allocs
func BenchmarkApplySpecialMechanics(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.ApplySpecialMechanicsRequest{}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ApplySpecialMechanics(ctx, req)
	}
}

// BenchmarkCalculateChainDamage benchmarks CalculateChainDamage handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCalculateChainDamage(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.CalculateChainDamageRequest{}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CalculateChainDamage(ctx, req)
	}
}

// BenchmarkCreatePersistentEffect benchmarks CreatePersistentEffect handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCreatePersistentEffect(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.CreatePersistentEffectRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreatePersistentEffect(ctx, req)
	}
}

