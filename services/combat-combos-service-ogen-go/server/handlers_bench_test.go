// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-ogen-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkGetComboCatalog benchmarks GetComboCatalog handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetComboCatalog(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetComboCatalogParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetComboCatalog(ctx, params)
	}
}

// BenchmarkGetComboDetails benchmarks GetComboDetails handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetComboDetails(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetComboDetailsParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetComboDetails(ctx, params)
	}
}

// BenchmarkActivateCombo benchmarks ActivateCombo handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkActivateCombo(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.ActivateComboRequest{
		CharacterID: uuid.New(),
		ComboID:     uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ActivateCombo(ctx, req)
	}
}
