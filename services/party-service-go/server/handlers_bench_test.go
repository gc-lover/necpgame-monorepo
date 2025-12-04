// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"
)

// BenchmarkCreateParty benchmarks CreateParty handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCreateParty(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.CreatePartyRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreateParty(ctx, req)
	}
}

// BenchmarkGetParty benchmarks GetParty handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetParty(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetPartyParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetParty(ctx, params)
	}
}

// BenchmarkDisbandParty benchmarks DisbandParty handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDisbandParty(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DisbandParty(ctx)
	}
}

