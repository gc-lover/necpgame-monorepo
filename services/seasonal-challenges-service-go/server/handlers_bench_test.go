// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/necpgame/seasonal-challenges-service-go/pkg/api"
)

// BenchmarkGetCurrentSeason benchmarks GetCurrentSeason handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetCurrentSeason(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetCurrentSeason(ctx)
	}
}

// BenchmarkGetSeasonChallenges benchmarks GetSeasonChallenges handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetSeasonChallenges(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetSeasonChallengesParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetSeasonChallenges(ctx, params)
	}
}

// BenchmarkGetSeasonRewards benchmarks GetSeasonRewards handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetSeasonRewards(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetSeasonRewardsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetSeasonRewards(ctx, params)
	}
}

