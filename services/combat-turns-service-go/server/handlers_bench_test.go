// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-turns-service-go/pkg/api"
)

// BenchmarkGetCurrentTurn benchmarks GetCurrentTurn handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetCurrentTurn(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetCurrentTurnParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetCurrentTurn(ctx, params)
	}
}

// BenchmarkGetTurnOrder benchmarks GetTurnOrder handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetTurnOrder(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetTurnOrderParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetTurnOrder(ctx, params)
	}
}

// BenchmarkNextTurn benchmarks NextTurn handler
// Target: <100μs per operation, minimal allocs
func BenchmarkNextTurn(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.NextTurnParams{}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.NextTurn(ctx, params)
	}
}
