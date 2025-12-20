// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/clan-war-service-go/models"
	"github.com/necpgame/clan-war-service-go/pkg/api"
)

// benchmarkClanWarService implements ClanWarServiceInterface for benchmarks
type benchmarkClanWarService struct{}

func (m *benchmarkClanWarService) DeclareWar(_ context.Context, _ *models.DeclareWarRequest) (*models.ClanWar, error) {
	return nil, nil
}

func (m *benchmarkClanWarService) GetWar(_ context.Context, _ uuid.UUID) (*models.ClanWar, error) {
	return nil, nil
}

func (m *benchmarkClanWarService) ListWars(_ context.Context, _ *uuid.UUID, _ *models.WarStatus, _, _ int) ([]models.ClanWar, int, error) {
	return nil, 0, nil
}

func (m *benchmarkClanWarService) StartWar(_ context.Context, _ uuid.UUID) error {
	return nil
}

func (m *benchmarkClanWarService) CompleteWar(_ context.Context, _ uuid.UUID) error {
	return nil
}

func (m *benchmarkClanWarService) CreateBattle(_ context.Context, _ *models.CreateBattleRequest) (*models.WarBattle, error) {
	return nil, nil
}

func (m *benchmarkClanWarService) GetBattle(_ context.Context, _ uuid.UUID) (*models.WarBattle, error) {
	return nil, nil
}

func (m *benchmarkClanWarService) ListBattles(_ context.Context, _ *uuid.UUID, _ *models.BattleStatus, _, _ int) ([]models.WarBattle, int, error) {
	return nil, 0, nil
}

func (m *benchmarkClanWarService) StartBattle(_ context.Context, _ uuid.UUID) error {
	return nil
}

func (m *benchmarkClanWarService) UpdateBattleScore(_ context.Context, _ *models.UpdateBattleScoreRequest) error {
	return nil
}

func (m *benchmarkClanWarService) CompleteBattle(_ context.Context, _ uuid.UUID) error {
	return nil
}

func (m *benchmarkClanWarService) GetTerritory(_ context.Context, _ uuid.UUID) (*models.Territory, error) {
	return nil, nil
}

func (m *benchmarkClanWarService) ListTerritories(_ context.Context, _ *uuid.UUID, _, _ int) ([]models.Territory, int, error) {
	return nil, 0, nil
}

// BenchmarkDeclareWar benchmarks DeclareWar handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDeclareWar(b *testing.B) {
	mockService := &benchmarkClanWarService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	req := &api.DeclareWarRequest{
		AttackerClanID: uuid.New(),
		DefenderClanID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DeclareWar(ctx, req)
	}
}

// BenchmarkGetWar benchmarks GetWar handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetWar(b *testing.B) {
	mockService := &benchmarkClanWarService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	params := api.GetWarParams{
		WarID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetWar(ctx, params)
	}
}

// BenchmarkGetActiveWars benchmarks GetActiveWars handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetActiveWars(b *testing.B) {
	mockService := &benchmarkClanWarService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	params := api.GetActiveWarsParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetActiveWars(ctx, params)
	}
}
