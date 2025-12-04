// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/faction-core-service-go/pkg/api"
)

// BenchmarkCreateFaction benchmarks CreateFaction handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCreateFaction(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.CreateFactionRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreateFaction(ctx, req)
	}
}

// BenchmarkGetFaction benchmarks GetFaction handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetFaction(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetFactionParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetFaction(ctx, params)
	}
}

// BenchmarkUpdateFaction benchmarks UpdateFaction handler
// Target: <100μs per operation, minimal allocs
func BenchmarkUpdateFaction(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.UpdateFactionRequest{
		// TODO: Fill request fields based on API spec
	}
	params := api.UpdateFactionParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.UpdateFaction(ctx, req, params)
	}
}

