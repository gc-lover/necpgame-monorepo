// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/pkg/api"
	"github.com/google/uuid"
)

type benchService struct{}

func (benchService) GetPosition(_ context.Context, characterID uuid.UUID) (*models.CharacterPosition, error) {
	return &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID,
		UpdatedAt:   time.Now(),
		CreatedAt:   time.Now(),
	}, nil
}

func (benchService) SavePosition(_ context.Context, characterID uuid.UUID, _ *models.SavePositionRequest) (*models.CharacterPosition, error) {
	return &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID,
		UpdatedAt:   time.Now(),
		CreatedAt:   time.Now(),
	}, nil
}

func (benchService) GetPositionHistory(_ context.Context, characterID uuid.UUID, _ int) ([]models.PositionHistory, error) {
	return []models.PositionHistory{
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			CreatedAt:   time.Now(),
		},
	}, nil
}

// BenchmarkGetPosition benchmarks GetPosition handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPosition(b *testing.B) {
	handlers := NewHandlers(benchService{})
	ctx := context.Background()
	params := api.GetPositionParams{CharacterId: uuid.New()}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPosition(ctx, params)
	}
}

// BenchmarkSavePosition benchmarks SavePosition handler
// Target: <100μs per operation, minimal allocs
func BenchmarkSavePosition(b *testing.B) {
	handlers := NewHandlers(benchService{})
	ctx := context.Background()
	req := &api.SavePositionRequest{
		PositionX: 1,
		PositionY: 2,
		PositionZ: 3,
		Yaw:       4,
	}
	params := api.SavePositionParams{CharacterId: uuid.New()}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.SavePosition(ctx, req, params)
	}
}

// BenchmarkGetPositionHistory benchmarks GetPositionHistory handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPositionHistory(b *testing.B) {
	handlers := NewHandlers(benchService{})
	ctx := context.Background()
	params := api.GetPositionHistoryParams{
		CharacterId: uuid.New(),
		Limit:       api.OptInt{Value: 5, Set: true},
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPositionHistory(ctx, params)
	}
}
