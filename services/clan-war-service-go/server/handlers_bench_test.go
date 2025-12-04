// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/necpgame/clan-war-service-go/pkg/api"
)

// BenchmarkDeclareWar benchmarks DeclareWar handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDeclareWar(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DeclareWar(ctx)
	}
}

// BenchmarkGetWar benchmarks GetWar handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetWar(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetWarParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetWar(ctx, params)
	}
}

// BenchmarkGetActiveWars benchmarks GetActiveWars handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetActiveWars(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetActiveWarsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetActiveWars(ctx, params)
	}
}

