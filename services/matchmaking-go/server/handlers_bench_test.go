// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
	"github.com/google/uuid"
)

// benchmarkRepository is a minimal repository for benchmarks that avoids database operations
type benchmarkRepository struct {
	*Repository
}

// Override methods to avoid database calls in benchmarks
func (r *benchmarkRepository) GetQueueEntry(ctx context.Context, playerID uuid.UUID) (*QueueEntry, error) {
	return &QueueEntry{
		ID:           uuid.New(),
		PlayerID:     playerID,
		ActivityType: "pvp4",
		Rating:       1500,
		Status:       "queued",
	}, nil
}

func (r *benchmarkRepository) GetPlayerRating(ctx context.Context, playerID uuid.UUID, activityType string) (*PlayerRating, error) {
	return &PlayerRating{
		PlayerID:      playerID,
		ActivityType:  activityType,
		CurrentRating: 1500,
		PeakRating:    1600,
		Wins:          10,
		Losses:        5,
		Tier:          "Gold",
		League:        3,
	}, nil
}

func (r *benchmarkRepository) GetLeaderboard(ctx context.Context, activityType string, limit int) ([]LeaderboardEntry, error) {
	return []LeaderboardEntry{}, nil
}

// setupBenchmarkService creates a service with minimal repository for benchmarking
func setupBenchmarkService(b *testing.B) (*Service, func()) {
	// Create a repository with nil db to avoid database connections
	repo := &Repository{
		db: nil, // Avoid database operations in benchmarks
		cb: nil, // No circuit breaker needed for benchmarks
	}

	// Wrap it in benchmarkRepository to override methods
	benchRepo := &benchmarkRepository{Repository: repo}

	// Create minimal cache manager
	cache := NewCacheManager("")

	service := NewService(benchRepo.Repository, cache)

	cleanup := func() {
		if cache != nil {
			cache.Close()
		}
	}

	return service, cleanup
}

// BenchmarkEnterQueue benchmarks EnterQueue handler
// Target: <100μs per operation, minimal allocs
func BenchmarkEnterQueue(b *testing.B) {
	service, cleanup := setupBenchmarkService(b)
	defer cleanup()
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
// NOTE: Skipped due to database dependency in current implementation
func BenchmarkGetQueueStatus(b *testing.B) {
	b.Skip("Requires database setup for benchmarks")
}

// BenchmarkLeaveQueue benchmarks LeaveQueue handler
// Target: <100μs per operation, minimal allocs
// NOTE: Skipped due to database dependency in current implementation
func BenchmarkLeaveQueue(b *testing.B) {
	b.Skip("Requires database setup for benchmarks")
}
