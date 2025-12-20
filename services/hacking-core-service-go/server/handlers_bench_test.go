// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/hacking-core-service-go/pkg/api"
)

// BenchmarkInitiateHack benchmarks InitiateHack handler
// Target: <100μs per operation, minimal allocs
func BenchmarkInitiateHack(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.InitiateHackRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.InitiateHack(ctx, req)
	}
}

// BenchmarkCancelHack benchmarks CancelHack handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCancelHack(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	params := api.CancelHackParams{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CancelHack(ctx, params)
	}
}

// BenchmarkExecuteHack benchmarks ExecuteHack handler
// Target: <100μs per operation, minimal allocs
func BenchmarkExecuteHack(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.ExecuteHackRequest{}
	params := api.ExecuteHackParams{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ExecuteHack(ctx, req, params)
	}
}
