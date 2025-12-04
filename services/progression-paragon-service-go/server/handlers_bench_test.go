// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/progression-paragon-service-go/pkg/api"
)

// mockParagonService implements ParagonServiceInterface
type mockParagonService struct{}

func (m *mockParagonService) GetParagonLevels(ctx context.Context, characterID uuid.UUID) (*ParagonLevels, error) {
	return &ParagonLevels{}, nil
}

func (m *mockParagonService) DistributeParagonPoints(ctx context.Context, characterID uuid.UUID, allocations []ParagonAllocation) (*ParagonLevels, error) {
	return &ParagonLevels{}, nil
}

func (m *mockParagonService) GetParagonStats(ctx context.Context, characterID uuid.UUID) (*ParagonStats, error) {
	return &ParagonStats{}, nil
}

func (m *mockParagonService) AddParagonExperience(ctx context.Context, characterID uuid.UUID, amount int64) error {
	return nil
}

// BenchmarkGetParagonLevels benchmarks GetParagonLevels handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetParagonLevels(b *testing.B) {
	mockService := &mockParagonService{}
	handlers := NewParagonHandlers(mockService)

	ctx := context.Background()
	params := api.GetParagonLevelsParams{
		CharacterID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetParagonLevels(ctx, params)
	}
}
