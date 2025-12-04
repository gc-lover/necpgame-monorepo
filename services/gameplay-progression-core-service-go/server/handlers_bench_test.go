// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-progression-core-service-go/pkg/api"
)

// BenchmarkValidateProgression benchmarks ValidateProgression handler
// Target: <100μs per operation, minimal allocs
func BenchmarkValidateProgression(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	req := &api.ValidateProgressionRequest{}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ValidateProgression(ctx, req)
	}
}

// BenchmarkGetCharacterProgression benchmarks GetCharacterProgression handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetCharacterProgression(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetCharacterProgressionParams{
		CharacterId: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetCharacterProgression(ctx, params)
	}
}

// BenchmarkDistributeAttributePoints benchmarks DistributeAttributePoints handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDistributeAttributePoints(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	req := &api.DistributeAttributePointsRequest{}
	params := api.DistributeAttributePointsParams{
		CharacterId: uuid.New(),
	}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DistributeAttributePoints(ctx, req, params)
	}
}

