// Issue: Performance benchmarks
package server

import (
	"context"
	"github.com/gc-lover/necpgame-monorepo/services/combat-sandevistan-service-go/pkg/api"
	"testing"
)

// BenchmarkActivateSandevistan benchmarks ActivateSandevistan handler
// Target: <100μs per operation, minimal allocs
func BenchmarkActivateSandevistan(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ActivateSandevistan(ctx)
	}
}

// BenchmarkDeactivateSandevistan benchmarks DeactivateSandevistan handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDeactivateSandevistan(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DeactivateSandevistan(ctx)
	}
}

// BenchmarkGetSandevistanStatus benchmarks GetSandevistanStatus handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetSandevistanStatus(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetSandevistanStatusParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetSandevistanStatus(ctx, params)
	}
}

