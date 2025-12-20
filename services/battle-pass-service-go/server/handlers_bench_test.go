// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/battle-pass-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkGetCurrentSeason benchmarks GetCurrentSeason handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetCurrentSeason(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetCurrentSeason(ctx)
	}
}

// BenchmarkGetPlayerProgress benchmarks GetPlayerProgress handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPlayerProgress(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetPlayerProgressParams{
		PlayerID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPlayerProgress(ctx, params)
	}
}

// BenchmarkClaimReward benchmarks ClaimReward handler
// Target: <100μs per operation, minimal allocs
func BenchmarkClaimReward(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.ClaimRewardReq{
		PlayerID: uuid.New(),
		Level:    1,
		Track:    api.RewardTrackFree,
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ClaimReward(ctx, req)
	}
}
