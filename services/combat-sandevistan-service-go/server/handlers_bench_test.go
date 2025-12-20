// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sandevistan-service-go/pkg/api"
)

// BenchmarkActivateSandevistan benchmarks ActivateSandevistan handler
// Target: <100μs per operation, minimal allocs
func BenchmarkActivateSandevistan(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	params := api.ActivateSandevistanParams{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ActivateSandevistan(ctx, params)
	}
}

// BenchmarkDeactivateSandevistan benchmarks DeactivateSandevistan handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDeactivateSandevistan(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	params := api.DeactivateSandevistanParams{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DeactivateSandevistan(ctx, params)
	}
}

// BenchmarkGetSandevistanStatus benchmarks GetSandevistanStatus handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetSandevistanStatus(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetSandevistanStatusParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetSandevistanStatus(ctx, params)
	}
}
