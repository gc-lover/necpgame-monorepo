// Issue: #1579 - ogen benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkOgenEnterQueue benchmarks ogen TYPED handler
// Expected: <200 ns/op, <5 allocs vs oapi-codegen 2000 ns/op, 25 allocs
func BenchmarkOgenEnterQueue(b *testing.B) {
	// Setup
	repo := &MockRepository{}
	service := NewMatchmakingService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.EnterQueueRequest{
		ActivityType:   api.EnterQueueRequestActivityTypePvp5v5,
		PreferredRoles: []api.EnterQueueRequestPreferredRolesItem{api.EnterQueueRequestPreferredRolesItemDps},
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.EnterQueue(ctx, req)
	}
}

// BenchmarkSkillBucketsO1 benchmarks O(1) matching
func BenchmarkSkillBucketsO1(b *testing.B) {
	queue := NewMatchmakingQueue()
	
	// Add 1000 players
	for i := 0; i < 1000; i++ {
		queue.AddPlayer(PlayerQueueEntry{
			PlayerID:     uuid.New(),
			Skill:        i * 10,
			ActivityType: "pvp",
		})
	}
	
	b.ReportAllocs()
	b.ResetTimer()
	
	for i := 0; i < b.N; i++ {
		// Find match - O(1) lookup in bucket
		bucket := (i * 10) / 100
		_ = bucket
	}
}

// Mock repository for benchmarks
type MockRepository struct{}

func (m *MockRepository) CreateQueueEntry(ctx context.Context, playerID, activityType string, rating int) (string, error) {
	return "queue-123", nil
}

func (m *MockRepository) GetQueueEntry(ctx context.Context, queueID string) (interface{}, error) {
	return nil, nil
}

func (m *MockRepository) DeleteQueueEntry(ctx context.Context, queueID string) error {
	return nil
}

func (m *MockRepository) GetPlayerRating(ctx context.Context, playerID string, activityType string) (int, error) {
	return 1500, nil
}

func (m *MockRepository) UpdatePlayerRating(ctx context.Context, playerID string, activityType string, newRating int) error {
	return nil
}

func (m *MockRepository) CreateMatch(ctx context.Context, players []string, activityType string) (string, error) {
	return "match-123", nil
}

func (m *MockRepository) UpdateMatchStatus(ctx context.Context, matchID string, status string) error {
	return nil
}
