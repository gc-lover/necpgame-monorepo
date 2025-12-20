// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/necpgame/character-engram-core-service-go/pkg/api"

	"github.com/google/uuid"
)

// BenchmarkGetEngramSlots benchmarks GetEngramSlots handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetEngramSlots(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetEngramSlotsParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetEngramSlots(ctx, params)
	}
}

// BenchmarkInstallEngram benchmarks InstallEngram handler
// Target: <100μs per operation, minimal allocs
func BenchmarkInstallEngram(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	req := &api.InstallEngramRequest{
		EngramID: uuid.New(),
	}
	params := api.InstallEngramParams{
		CharacterID: uuid.New(),
		SlotID:      1,
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.InstallEngram(ctx, req, params)
	}
}

// BenchmarkRemoveEngram benchmarks RemoveEngram handler
// Target: <100μs per operation, minimal allocs
func BenchmarkRemoveEngram(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.RemoveEngramParams{
		CharacterID: uuid.New(),
		SlotID:      1,
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.RemoveEngram(ctx, params)
	}
}
