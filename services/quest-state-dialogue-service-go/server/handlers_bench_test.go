// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkGetQuestState benchmarks GetQuestState handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetQuestState(b *testing.B) {
	handlers := NewHandlers()

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
// Target: <100Ојs per operation, minimal allocs
func BenchmarkUpdateQuestState(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	req := &api.UpdateQuestStateRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.UpdateQuestState(ctx, req)
	}
}

// BenchmarkGetQuestDialogue benchmarks GetQuestDialogue handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetQuestDialogue(b *testing.B) {
	handlers := NewHandlers()

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

