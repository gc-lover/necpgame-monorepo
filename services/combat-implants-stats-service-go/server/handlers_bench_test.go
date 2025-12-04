// Issue: Performance benchmarks
package server

import (
	"context"
	"github.com/gc-lover/necpgame-monorepo/services/combat-implants-stats-service-go/pkg/api"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkGetEnergyStatus benchmarks GetEnergyStatus handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetEnergyStatus(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetEnergyStatusParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetEnergyStatus(ctx, params)
	}
}

// BenchmarkGetHumanityStatus benchmarks GetHumanityStatus handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetHumanityStatus(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetHumanityStatusParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetHumanityStatus(ctx, params)
	}
}

// BenchmarkCheckCompatibility benchmarks CheckCompatibility handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCheckCompatibility(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CheckCompatibility(ctx)
	}
}

