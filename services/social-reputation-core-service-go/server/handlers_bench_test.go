// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkGetReputation benchmarks GetReputation handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetReputation(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetReputationParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetReputation(ctx, params)
	}
}

// BenchmarkGetFactionReputation benchmarks GetFactionReputation handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetFactionReputation(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetFactionReputationParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetFactionReputation(ctx, params)
	}
}

// BenchmarkGetFactionRelations benchmarks GetFactionRelations handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetFactionRelations(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetFactionRelationsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetFactionRelations(ctx, params)
	}
}

