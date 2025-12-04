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

func (m *benchmarkClanWarService) DeclareWar(ctx context.Context, req *models.DeclareWarRequest) (*models.ClanWar, error) {
	return nil, nil
}

func (m *benchmarkClanWarService) GetWar(ctx context.Context, warID uuid.UUID) (*models.ClanWar, error) {
	return nil, nil
}

func (m *benchmarkClanWarService) ListWars(ctx context.Context, guildID *uuid.UUID, status *models.WarStatus, limit, offset int) ([]models.ClanWar, int, error) {
	return nil, 0, nil
}

func (m *benchmarkClanWarService) StartWar(ctx context.Context, warID uuid.UUID) error {
	return nil
}

func (m *benchmarkClanWarService) CompleteWar(ctx context.Context, warID uuid.UUID) error {
	return nil
}

func (m *benchmarkClanWarService) CreateBattle(ctx context.Context, req *models.CreateBattleRequest) (*models.WarBattle, error) {
	return nil, nil
}

func (m *benchmarkClanWarService) GetBattle(ctx context.Context, battleID uuid.UUID) (*models.WarBattle, error) {
	return nil, nil
}

func (m *benchmarkClanWarService) ListBattles(ctx context.Context, warID *uuid.UUID, status *models.BattleStatus, limit, offset int) ([]models.WarBattle, int, error) {
	return nil, 0, nil
}

func (m *benchmarkClanWarService) StartBattle(ctx context.Context, battleID uuid.UUID) error {
	return nil
}

func (m *benchmarkClanWarService) UpdateBattleScore(ctx context.Context, req *models.UpdateBattleScoreRequest) error {
	return nil
}

func (m *benchmarkClanWarService) CompleteBattle(ctx context.Context, battleID uuid.UUID) error {
	return nil
}

func (m *benchmarkClanWarService) GetTerritory(ctx context.Context, territoryID uuid.UUID) (*models.Territory, error) {
	return nil, nil
}

func (m *benchmarkClanWarService) ListTerritories(ctx context.Context, ownerGuildID *uuid.UUID, limit, offset int) ([]models.Territory, int, error) {
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

