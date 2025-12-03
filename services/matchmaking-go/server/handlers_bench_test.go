// Issue: #150 - Performance Benchmarks
package server

import (
	"context"
	"sync"
	"testing"

	"github.com/google/uuid"

	api "github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
)

// BenchmarkEnterQueue tests EnterQueue performance
// Target: <100μs per operation, 0 allocs (memory pooling!)
func BenchmarkEnterQueue(b *testing.B) {
	// Setup mock service
	service := &Service{
		queueResponsePool: newQueuePool(),
	}
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.EnterQueueRequest{
		ActivityType: api.EnterQueueRequestActivityTypePvp5v5,
	}

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.EnterQueue(ctx, req)
	}
}

// BenchmarkGetQueueStatus tests GetQueueStatus performance
// Target: <50μs per operation (Redis cache hit)
func BenchmarkGetQueueStatus(b *testing.B) {
	service := &Service{
		statusResponsePool: newStatusPool(),
	}
	handlers := NewHandlers(service)

	ctx := context.Background()
	queueID := uuid.New()

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetQueueStatus(ctx, api.GetQueueStatusParams{QueueId: queueID})
	}
}

// BenchmarkSkillBucketMatcher tests O(1) matching
// Target: <1μs per operation (vs 1000μs for O(n) naive approach)
func BenchmarkSkillBucketMatcher(b *testing.B) {
	matcher := NewSkillBucketMatcher()

	// Populate with 10k players
	for i := 0; i < 10000; i++ {
		entry := &QueueEntry{
			ID:           uuid.New(),
			PlayerID:     uuid.New(),
			ActivityType: "pvp_5v5",
			Rating:       1500 + (i % 1000), // Spread ratings
		}
		matcher.AddToQueue(entry)
	}

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		_ = matcher.GetQueueSize("pvp_5v5", 1750)
	}
}

// Helper functions for benchmarks
func newQueuePool() sync.Pool {
	return sync.Pool{
		New: func() interface{} {
			return &api.QueueResponse{}
		},
	}
}

func newStatusPool() sync.Pool {
	return sync.Pool{
		New: func() interface{} {
			return &api.QueueStatusResponse{}
		},
	}
}
