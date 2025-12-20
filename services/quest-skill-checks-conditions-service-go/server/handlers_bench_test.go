// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/quest-skill-checks-conditions-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkCheckQuestConditions benchmarks CheckQuestConditions handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCheckQuestConditions(b *testing.B) {
	handlers := &Handlers{}

	ctx := context.Background()
	params := api.CheckQuestConditionsParams{
		QuestID: uuid.New(),
	}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CheckQuestConditions(ctx, params)
	}
}

// BenchmarkGetQuestRequirements benchmarks GetQuestRequirements handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetQuestRequirements(b *testing.B) {
	handlers := &Handlers{}

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
	handlers := &Handlers{}

	ctx := context.Background()
	req := &api.SkillCheckRequest{}
	params := api.PerformSkillCheckParams{
		QuestID: uuid.New(),
	}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.PerformSkillCheck(ctx, req, params)
	}
}
