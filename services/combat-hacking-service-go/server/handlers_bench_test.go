// Issue: Performance benchmarks
package server

import (
	"context"
	"github.com/gc-lover/necpgame-monorepo/services/combat-hacking-service-go/pkg/api"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkHackTarget benchmarks HackTarget handler
// Target: <100μs per operation, minimal allocs
func BenchmarkHackTarget(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.HackTargetParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.HackTarget(ctx, params)
	}
}

// BenchmarkActivateCountermeasures benchmarks ActivateCountermeasures handler
// Target: <100μs per operation, minimal allocs
func BenchmarkActivateCountermeasures(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ActivateCountermeasures(ctx)
	}
}

// BenchmarkGetDemons benchmarks GetDemons handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetDemons(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetDemonsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetDemons(ctx, params)
	}
}

