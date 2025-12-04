// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	api "github.com/gc-lover/necpgame-monorepo/services/quest-core-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkStartQuest benchmarks StartQuest handler
// Target: <100μs per operation, minimal allocs
func BenchmarkStartQuest(b *testing.B) {
	handlers := NewHandlers(nil)

	ctx := context.Background()
	req := &api.StartQuestRequest{
		QuestID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.StartQuest(ctx, req)
	}
}

// BenchmarkGetQuest benchmarks GetQuest handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetQuest(b *testing.B) {
	handlers := NewHandlers(nil)

	ctx := context.Background()
	params := api.GetQuestParams{
		QuestID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetQuest(ctx, params)
	}
}

// BenchmarkGetPlayerQuests benchmarks GetPlayerQuests handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPlayerQuests(b *testing.B) {
	handlers := NewHandlers(nil)

	ctx := context.Background()
	params := api.GetPlayerQuestsParams{
		PlayerID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPlayerQuests(ctx, params)
	}
}

