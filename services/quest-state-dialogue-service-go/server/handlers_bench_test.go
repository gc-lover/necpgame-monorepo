// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/quest-state-dialogue-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkGetQuestState benchmarks GetQuestState handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetQuestState(b *testing.B) {
	handlers := &Handlers{}

	ctx := context.Background()
	params := api.GetQuestStateParams{
		QuestID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetQuestState(ctx, params)
	}
}

// BenchmarkUpdateQuestState benchmarks UpdateQuestState handler
// Target: <100μs per operation, minimal allocs
func BenchmarkUpdateQuestState(b *testing.B) {
	handlers := &Handlers{}

	ctx := context.Background()
	req := &api.UpdateStateRequest{
		// TODO: Fill request fields based on API spec
	}
	params := api.UpdateQuestStateParams{
		QuestID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.UpdateQuestState(ctx, req, params)
	}
}

// BenchmarkGetQuestDialogue benchmarks GetQuestDialogue handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetQuestDialogue(b *testing.B) {
	handlers := &Handlers{}

	ctx := context.Background()
	params := api.GetQuestDialogueParams{
		QuestID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetQuestDialogue(ctx, params)
	}
}
