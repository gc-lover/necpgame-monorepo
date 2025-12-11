// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	api "github.com/gc-lover/necpgame-monorepo/services/loot-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

type mockLootService struct{}

func (m *mockLootService) DistributeLoot(ctx context.Context, req *api.DistributeLootRequest) (*api.DistributeLootResponse, error) {
	return &api.DistributeLootResponse{}, nil
}

func (m *mockLootService) GenerateLoot(ctx context.Context, req *api.GenerateLootRequest) (*api.GenerateLootResponse, error) {
	return &api.GenerateLootResponse{}, nil
}

func (m *mockLootService) GetPlayerLootHistory(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]api.LootHistoryEntry, int, error) {
	return nil, 0, nil
}

func (m *mockLootService) GetRollStatus(ctx context.Context, rollID uuid.UUID) (*api.RollStatusResponse, error) {
	return &api.RollStatusResponse{}, nil
}

func (m *mockLootService) GetWorldDrops(ctx context.Context, limit, offset int) ([]api.WorldDrop, error) {
	return nil, nil
}

func (m *mockLootService) PassRoll(ctx context.Context, rollID uuid.UUID) error {
	return nil
}

func (m *mockLootService) PickupWorldDrop(ctx context.Context, dropID uuid.UUID) (*api.PickupDropResponse, error) {
	return &api.PickupDropResponse{}, nil
}

func (m *mockLootService) RollForItem(ctx context.Context, req *api.RollRequest) (*api.RollResponse, error) {
	return &api.RollResponse{}, nil
}

// BenchmarkDistributeLoot benchmarks DistributeLoot handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDistributeLoot(b *testing.B) {
	logger := logrus.New()
	handlers := NewHandlers(logger, &mockLootService{})

	ctx := context.Background()
	req := &api.DistributeLootRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DistributeLoot(ctx, req)
	}
}

// BenchmarkGenerateLoot benchmarks GenerateLoot handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGenerateLoot(b *testing.B) {
	logger := logrus.New()
	handlers := NewHandlers(logger, &mockLootService{})

	ctx := context.Background()
	req := &api.GenerateLootRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GenerateLoot(ctx, req)
	}
}

// BenchmarkGetPlayerLootHistory benchmarks GetPlayerLootHistory handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPlayerLootHistory(b *testing.B) {
	logger := logrus.New()
	handlers := NewHandlers(logger, &mockLootService{})

	ctx := context.Background()
	params := api.GetPlayerLootHistoryParams{
		PlayerID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPlayerLootHistory(ctx, params)
	}
}

