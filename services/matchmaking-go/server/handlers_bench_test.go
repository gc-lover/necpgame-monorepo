// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
)

// BenchmarkEnterQueue benchmarks EnterQueue handler
// Target: <100μs per operation, minimal allocs
func BenchmarkEnterQueue(b *testing.B) {
	repo, _ := NewRepository("")
	cache := NewCacheManager("")
	service := NewService(repo, cache)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.EnterQueueRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.EnterQueue(ctx, req)
	}
}

// BenchmarkGetQueueStatus benchmarks GetQueueStatus handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetQueueStatus(b *testing.B) {
	repo, _ := NewRepository("")
	cache := NewCacheManager("")
	service := NewService(repo, cache)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetQueueStatusParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetQueueStatus(ctx, params)
	}
}

// BenchmarkLeaveQueue benchmarks LeaveQueue handler
// Target: <100μs per operation, minimal allocs
func BenchmarkLeaveQueue(b *testing.B) {
	repo, _ := NewRepository("")
	cache := NewCacheManager("")
	service := NewService(repo, cache)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	params := api.LeaveQueueParams{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.LeaveQueue(ctx, params)
	}
}

