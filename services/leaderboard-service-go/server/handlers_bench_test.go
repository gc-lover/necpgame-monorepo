// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/leaderboard-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkGetGlobalLeaderboard benchmarks GetGlobalLeaderboard handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetGlobalLeaderboard(b *testing.B) {
	logger := GetLogger()
	service := NewLeaderboardService(logger)
	handlers := NewHandlers(logger, service)

	ctx := context.Background()
	params := api.GetGlobalLeaderboardParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetGlobalLeaderboard(ctx, params)
	}
}

// BenchmarkGetFactionLeaderboard benchmarks GetFactionLeaderboard handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetFactionLeaderboard(b *testing.B) {
	logger := GetLogger()
	service := NewLeaderboardService(logger)
	handlers := NewHandlers(logger, service)

	ctx := context.Background()
	params := api.GetFactionLeaderboardParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetFactionLeaderboard(ctx, params)
	}
}

// BenchmarkGetPlayerRank benchmarks GetPlayerRank handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPlayerRank(b *testing.B) {
	logger := GetLogger()
	service := NewLeaderboardService(logger)
	handlers := NewHandlers(logger, service)

	ctx := context.Background()
	params := api.GetPlayerRankParams{
		PlayerID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPlayerRank(ctx, params)
	}
}
