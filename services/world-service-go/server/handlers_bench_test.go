// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkListContinents benchmarks ListContinents handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkListContinents(b *testing.B) {
	logger := GetLogger()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	params := api.ListContinentsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ListContinents(ctx, params)
	}
}

// BenchmarkCreateContinent benchmarks CreateContinent handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkCreateContinent(b *testing.B) {
	logger := GetLogger()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	req := &api.CreateContinentRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreateContinent(ctx, req)
	}
}

