// Issue: Performance benchmarks
package server

import (
	"context"
	"// Issue: #1595 - ogen migration/pkg/api"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkApplySpecialMechanics benchmarks ApplySpecialMechanics handler
// Target: <100μs per operation, minimal allocs
func BenchmarkApplySpecialMechanics(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ApplySpecialMechanics(ctx)
	}
}

// BenchmarkCalculateChainDamage benchmarks CalculateChainDamage handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCalculateChainDamage(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CalculateChainDamage(ctx)
	}
}

// BenchmarkCreatePersistentEffect benchmarks CreatePersistentEffect handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCreatePersistentEffect(b *testing.B) {
	service := NewService(nil)
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

