// Issue: Performance benchmarks
package server

import (
	"context"
	"// Issue: #164/pkg/api"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkValidateProgression benchmarks ValidateProgression handler
// Target: <100μs per operation, minimal allocs
func BenchmarkValidateProgression(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ValidateProgression(ctx)
	}
}

// BenchmarkGetCharacterProgression benchmarks GetCharacterProgression handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetCharacterProgression(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetCharacterProgressionParams{
		CharacterID: uuid.New(),
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
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DistributeAttributePoints(ctx)
	}
}

