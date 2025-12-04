// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkClaimAchievementReward benchmarks ClaimAchievementReward handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkClaimAchievementReward(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ClaimAchievementReward(ctx)
	}
}

// BenchmarkGetAchievementDetails benchmarks GetAchievementDetails handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetAchievementDetails(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetAchievementDetailsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetAchievementDetails(ctx, params)
	}
}

// BenchmarkGetAchievements benchmarks GetAchievements handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetAchievements(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetAchievementsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetAchievements(ctx, params)
	}
}

