// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-implants-core-service-go/pkg/api"

	"github.com/google/uuid"
)

// BenchmarkGetImplantCatalog benchmarks GetImplantCatalog handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetImplantCatalog(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetImplantCatalogParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetImplantCatalog(ctx, params)
	}
}

// BenchmarkGetImplantById benchmarks GetImplantById handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetImplantById(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetImplantByIdParams{
		ImplantID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetImplantById(ctx, params)
	}
}

// BenchmarkGetCharacterImplants benchmarks GetCharacterImplants handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetCharacterImplants(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetCharacterImplantsParams{
		CharacterID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetCharacterImplants(ctx, params)
	}
}
