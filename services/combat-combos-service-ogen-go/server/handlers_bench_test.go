// Issue: #1578 + #1590
// Benchmark comparison: ogen (TYPED) vs oapi-codegen (interface{})
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-ogen-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkOgenGetComboCatalog benchmarks ogen TYPED handler
// Expected: 3-5 allocs/op vs 16 with oapi-codegen
func BenchmarkOgenGetComboCatalog(b *testing.B) {
	// Setup
	repo, _ := NewRepository("postgres://test")
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetComboCatalogParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetComboCatalog(ctx, params)
	}
}

// BenchmarkOgenActivateCombo benchmarks TYPED request/response
func BenchmarkOgenActivateCombo(b *testing.B) {
	repo, _ := NewRepository("postgres://test")
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.ActivateComboRequest{
		CharacterID: mustUUID("12345678-1234-1234-1234-123456789abc"),
		ComboID:     mustUUID("87654321-4321-4321-4321-cba987654321"),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ActivateCombo(ctx, req)
	}
}

// Helper to parse UUID
func mustUUID(s string) uuid.UUID {
	u, _ := uuid.Parse(s)
	return u
}
