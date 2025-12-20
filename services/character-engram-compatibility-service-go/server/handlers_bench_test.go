// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/necpgame/character-engram-compatibility-service-go/pkg/api"

	"github.com/google/uuid"
)

// BenchmarkGetEngramCompatibility benchmarks GetEngramCompatibility handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetEngramCompatibility(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetEngramCompatibilityParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetEngramCompatibility(ctx, params)
	}
}

// BenchmarkCheckEngramCompatibility benchmarks CheckEngramCompatibility handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCheckEngramCompatibility(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	req := &api.CheckCompatibilityRequest{
		EngramIds: []uuid.UUID{uuid.New()},
	}
	params := api.CheckEngramCompatibilityParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CheckEngramCompatibility(ctx, req, params)
	}
}

// BenchmarkGetEngramConflicts benchmarks GetEngramConflicts handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetEngramConflicts(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetEngramConflictsParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetEngramConflicts(ctx, params)
	}
}
