// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"
)

// BenchmarkGetProjectileForms benchmarks GetProjectileForms handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetProjectileForms(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetProjectileFormsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetProjectileForms(ctx, params)
	}
}

// BenchmarkGetProjectileForm benchmarks GetProjectileForm handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetProjectileForm(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetProjectileFormParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetProjectileForm(ctx, params)
	}
}

// BenchmarkSpawnProjectile benchmarks SpawnProjectile handler
// Target: <100μs per operation, minimal allocs
func BenchmarkSpawnProjectile(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.SpawnProjectile(ctx)
	}
}

