// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-service-go/pkg/api"
)

// mockMatchmakingService implements Service interface for benchmarks
type mockMatchmakingService struct{}

func (m *mockMatchmakingService) EnterQueue(_ context.Context, _ *api.EnterQueueRequest) (*api.QueueResponse, error) {
	return nil, nil
}

func (m *mockMatchmakingService) GetQueueStatus(_ context.Context, _ string) (*api.QueueStatusResponse, error) {
	return nil, nil
}

func (m *mockMatchmakingService) LeaveQueue(_ context.Context, _ string) (*api.LeaveQueueResponse, error) {
	return nil, nil
}

func (m *mockMatchmakingService) GetPlayerRating(_ context.Context, _ string) (*api.PlayerRatingResponse, error) {
	return nil, nil
}

func (m *mockMatchmakingService) GetLeaderboard(_ context.Context, _ string, _ api.GetLeaderboardParams) (*api.LeaderboardResponse, error) {
	return nil, nil
}

func (m *mockMatchmakingService) AcceptMatch(_ context.Context, _ string) error {
	return nil
}

func (m *mockMatchmakingService) DeclineMatch(_ context.Context, _ string) error {
	return nil
}

// BenchmarkEnterQueue benchmarks EnterQueue handler
// Target: <100μs per operation, minimal allocs
func BenchmarkEnterQueue(b *testing.B) {
	mockService := &mockMatchmakingService{}
	handlers := NewHandlers(mockService)

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
	mockService := &mockMatchmakingService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	params := api.GetQueueStatusParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetQueueStatus(ctx, params)
	}
}

// BenchmarkLeaveQueue benchmarks LeaveQueue handler
// Target: <100μs per operation, minimal allocs
func BenchmarkLeaveQueue(b *testing.B) {
	mockService := &mockMatchmakingService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	params := api.LeaveQueueParams{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.LeaveQueue(ctx, params)
	}
}
