// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/loot-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

type mockLootService struct{}

func (m *mockLootService) DistributeLoot(_ context.Context, _ *api.DistributeLootRequest) (*api.DistributeLootResponse, error) {
	return &api.DistributeLootResponse{}, nil
}

func (m *mockLootService) GenerateLoot(_ context.Context, _ *api.GenerateLootRequest) (*api.GenerateLootResponse, error) {
	return &api.GenerateLootResponse{}, nil
}

func (m *mockLootService) GetPlayerLootHistory(_ context.Context, _ uuid.UUID, _, _ int) ([]api.LootHistoryEntry, int, error) {
	return nil, 0, nil
}

func (m *mockLootService) GetRollStatus(_ context.Context, _ uuid.UUID) (*api.RollStatusResponse, error) {
	return &api.RollStatusResponse{}, nil
}

func (m *mockLootService) GetWorldDrops(_ context.Context, _, _ int) ([]api.WorldDrop, error) {
	return nil, nil
}

func (m *mockLootService) PassRoll(_ context.Context, _ uuid.UUID) error {
	return nil
}

func (m *mockLootService) PickupWorldDrop(_ context.Context, _ uuid.UUID) (*api.PickupDropResponse, error) {
	return &api.PickupDropResponse{}, nil
}

func (m *mockLootService) RollForItem(_ context.Context, _ *api.RollRequest) (*api.RollResponse, error) {
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
