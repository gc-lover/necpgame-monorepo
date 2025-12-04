// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sessions-service-go/pkg/api"
)

// BenchmarkListCombatSessions benchmarks ListCombatSessions handler
// Target: <100μs per operation, minimal allocs
func BenchmarkListCombatSessions(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.ListCombatSessionsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ListCombatSessions(ctx, params)
	}
}

// BenchmarkCreateCombatSession benchmarks CreateCombatSession handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCreateCombatSession(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	req := &api.CreateSessionRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreateCombatSession(ctx, req)
	}
}

// BenchmarkGetCombatSession benchmarks GetCombatSession handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetCombatSession(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetCombatSessionParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetCombatSession(ctx, params)
	}
}

