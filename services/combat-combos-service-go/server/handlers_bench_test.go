// Issue: #1578
package server

import (
	"context"
	"net/http/httptest"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-go/pkg/api"
)

// BenchmarkRespondJSONWithoutPooling benchmarks response without pooling (baseline)
func BenchmarkRespondJSONWithoutPooling(b *testing.B) {
	data := map[string]string{
		"status":  "success",
		"message": "test message for benchmarking",
		"id":      "12345678-1234-1234-1234-123456789abc",
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		w := httptest.NewRecorder()
		// Baseline: no pool, no buffer, direct encode
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(200)
		// This is how oapi-codegen does it WITHOUT optimization
		respondJSON(w, 200, data)  // Now uses REAL production code!
	}
}

// BenchmarkOapiHandlerReal benchmarks REAL oapi-codegen handler with pooling
func BenchmarkOapiHandlerReal(b *testing.B) {
	// Setup real service
	repo, _ := NewRepository("postgres://test")
	service := NewService(repo)
	handlers := NewHandlers(service)

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		// Real HTTP handler call with REAL respondJSON!
		w := httptest.NewRecorder()
		r := httptest.NewRequest("GET", "/api/v1/gameplay/combat/combos/catalog", nil)
		
		handlers.GetComboCatalog(w, r, api.GetComboCatalogParams{})
	}
}

// BenchmarkContextTimeout benchmarks context timeout overhead
func BenchmarkContextTimeout(b *testing.B) {
	parentCtx := context.Background()

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		ctx, cancel := context.WithTimeout(parentCtx, DBTimeout)
		_ = ctx
		cancel()
	}
}
