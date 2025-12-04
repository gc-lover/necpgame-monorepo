// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/quest-rewards-events-service-go/pkg/api"
)

// BenchmarkGetQuestRewards benchmarks GetQuestRewards handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetQuestRewards(b *testing.B) {
	handlers := NewQuestRewardsEventsHandlers()

	ctx := context.Background()
	params := api.GetQuestRewardsParams{
		QuestID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetQuestRewards(ctx, params)
	}
}
