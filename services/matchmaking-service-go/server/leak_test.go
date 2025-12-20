// Issue: #1585 - Goroutine leak detection (CRITICAL - Hot path 5k+ RPS!)
// matchmaking-service is HIGH RISK for leaks (queue workers, skill buckets, concurrent matching)
package server

import (
	"context"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-service-go/pkg/api"
	"github.com/google/uuid"
	"go.uber.org/goleak"
)

// mockRepository implements Repository interface for tests
type mockRepository struct{}

func (m *mockRepository) CreateQueueEntry(_ context.Context, _, _ string, _ int) (string, error) {
	return "", nil
}

func (m *mockRepository) GetQueueEntry(_ context.Context, _ string) (interface{}, error) {
	return nil, nil
}

func (m *mockRepository) DeleteQueueEntry(_ context.Context, _ string) error {
	return nil
}

func (m *mockRepository) GetPlayerRating(_ context.Context, _ string, _ string) (int, error) {
	return 1500, nil
}

func (m *mockRepository) UpdatePlayerRating(_ context.Context, _ string, _ string, _ int) error {
	return nil
}

func (m *mockRepository) CreateMatch(_ context.Context, _ []string, _ string) (string, error) {
	return "", nil
}

func (m *mockRepository) UpdateMatchStatus(_ context.Context, _ string, _ string) error {
	return nil
}

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for hot path service - 5k+ RPS, queue workers, skill buckets
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestMatchmakingServiceNoLeaks verifies matchmaking service doesn't leak
func TestMatchmakingServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// Create mock repository
	mockRepo := &mockRepository{}
	service := NewMatchmakingService(mockRepo)

	// Test queue operations
	queue := NewMatchmakingQueue()
	entry := PlayerQueueEntry{
		PlayerID:     uuid.New(),
		Skill:        1500,
		JoinedAt:     time.Now(),
		ActivityType: "pvp_5v5",
	}
	queue.AddPlayer(entry)

	// Test service operations (no DB connection, so no leaks)
	ctx := context.Background()
	_, _ = service.EnterQueue(ctx, &api.EnterQueueRequest{
		ActivityType: api.EnterQueueRequestActivityTypePvp5v5,
	})

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from queue operations, test FAILS
}

// TestSkillBucketsNoLeaks verifies skill buckets don't leak goroutines
func TestSkillBucketsNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	queue := NewMatchmakingQueue()

	// Add 100 players to different skill buckets
	for i := 0; i < 100; i++ {
		entry := PlayerQueueEntry{
			PlayerID:     uuid.New(),
			Skill:        i * 10,
			JoinedAt:     time.Now(),
			ActivityType: "pvp_5v5",
		}
		queue.AddPlayer(entry)
	}

	time.Sleep(200 * time.Millisecond)

	// Skill bucket operations should not leak goroutines
}

// TestConcurrentQueueOperationsNoLeaks verifies concurrent operations don't leak
func TestConcurrentQueueOperationsNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	repo := &mockRepository{}
	service := NewMatchmakingService(repo)
	ctx := context.Background()

	// Simulate concurrent queue operations (100 goroutines)
	done := make(chan struct{}, 100)
	for i := 0; i < 100; i++ {
		go func() {
			_, _ = service.EnterQueue(ctx, &api.EnterQueueRequest{
				ActivityType: api.EnterQueueRequestActivityTypePvp5v5,
			})
			done <- struct{}{}
		}()
	}

	// Wait for all
	for i := 0; i < 100; i++ {
		<-done
	}

	time.Sleep(100 * time.Millisecond)

	// No leaked goroutines from concurrent operations
}

// NOTE: matchmaking-service is HIGH RISK for leaks:
// - 5k+ RPS (hot path)
// - Queue workers (background goroutines)
// - Skill buckets (O(1) matching)
// - Concurrent match finding
//
// MUST implement proper cleanup:
// - Context cancellation for all queue workers
// - Ticker.Stop() for all time.Ticker
// - Worker pool limits (bounded concurrency)
// - Channel close for all event channels
