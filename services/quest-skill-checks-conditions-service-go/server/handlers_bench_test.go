// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkCheckQuestConditions benchmarks CheckQuestConditions handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCheckQuestConditions(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CheckQuestConditions(ctx)
	}
}

// BenchmarkGetQuestRequirements benchmarks GetQuestRequirements handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetQuestRequirements(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetQuestRequirementsParams{
		QuestID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetQuestRequirements(ctx, params)
	}
}

// BenchmarkPerformSkillCheck benchmarks PerformSkillCheck handler
// Target: <100μs per operation, minimal allocs
func BenchmarkPerformSkillCheck(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.PerformSkillCheck(ctx)
	}
}

