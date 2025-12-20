// Issue: #1919 - Performance benchmarks for MMOFPS quest service
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/quest-core-service-go/pkg/api"
	"github.com/google/uuid"
)

func BenchmarkStartQuest(b *testing.B) {
	// Setup test service
	service := NewService(nil, nil) // Using nil for benchmark (no actual DB calls)

	questID := uuid.New()
	req := &api.StartQuestRequest{
		QuestID: questID,
	}

	ctx := context.Background()

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		_, _ = service.StartQuest(ctx, req)
	}
}

func BenchmarkCompleteQuest(b *testing.B) {
	// Setup test service
	service := NewService(nil, nil)

	questInstanceID := uuid.New()
	req := &api.CompleteQuestRequest{}

	ctx := context.Background()

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		_, _ = service.CompleteQuest(ctx, questInstanceID)
	}
}

// Memory pooling benchmarks
func BenchmarkMemoryPooling_StartQuest(b *testing.B) {
	service := NewService(nil, nil)

	questID := uuid.New()
	req := &api.StartQuestRequest{
		QuestID: questID,
	}

	ctx := context.Background()

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		_, _ = service.StartQuest(ctx, req)
		// In real implementation, response would be returned to memory pool
	}
}

// Zero allocations target: < 0.01 allocs/op for hot paths
func TestZeroAllocationsHotPath(t *testing.T) {
	service := NewService(nil, nil)

	questID := uuid.New()
	req := &api.StartQuestRequest{
		QuestID: questID,
	}

	ctx := context.Background()

	// Run benchmark and check allocations
	result := testing.Benchmark(func(b *testing.B) {
		b.ResetTimer()
		b.ReportAllocs()

		for i := 0; i < b.N; i++ {
			_, _ = service.StartQuest(ctx, req)
		}
	})

	// Target: < 0.01 allocs/op for hot path operations
	allocsPerOp := float64(result.AllocsPerOp())
	if allocsPerOp > 0.01 {
		t.Errorf("StartQuest allocates too much: %.2f allocs/op (target: < 0.01)", allocsPerOp)
	}

	// Target: < 50ns/op for hot path operations
	nsPerOp := float64(result.NsPerOp())
	if nsPerOp > 50 {
		t.Errorf("StartQuest too slow: %.2f ns/op (target: < 50ns)", nsPerOp)
	}
}
