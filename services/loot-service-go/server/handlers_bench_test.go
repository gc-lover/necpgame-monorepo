// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	api "github.com/gc-lover/necpgame-monorepo/services/loot-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// BenchmarkDistributeLoot benchmarks DistributeLoot handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDistributeLoot(b *testing.B) {
	logger := logrus.New()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	req := &api.DistributeLootRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DistributeLoot(ctx, req)
	}
}

// BenchmarkGenerateLoot benchmarks GenerateLoot handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGenerateLoot(b *testing.B) {
	logger := logrus.New()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	req := &api.GenerateLootRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GenerateLoot(ctx, req)
	}
}

// BenchmarkGetPlayerLootHistory benchmarks GetPlayerLootHistory handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPlayerLootHistory(b *testing.B) {
	logger := logrus.New()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	params := api.GetPlayerLootHistoryParams{
		PlayerID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPlayerLootHistory(ctx, params)
	}
}

