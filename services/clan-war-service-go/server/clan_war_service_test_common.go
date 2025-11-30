// Issue: #140895110
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/clan-war-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/mock"
)

type mockClanWarRepository struct {
	mock.Mock
}

func (m *mockClanWarRepository) CreateWar(ctx context.Context, war *models.ClanWar) error {
	args := m.Called(ctx, war)
	return args.Error(0)
}

func (m *mockClanWarRepository) GetWarByID(ctx context.Context, warID uuid.UUID) (*models.ClanWar, error) {
	args := m.Called(ctx, warID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ClanWar), args.Error(1)
}

func (m *mockClanWarRepository) ListWars(ctx context.Context, guildID *uuid.UUID, status *models.WarStatus, limit, offset int) ([]models.ClanWar, int, error) {
	args := m.Called(ctx, guildID, status, limit, offset)
	if args.Get(0) == nil {
		return nil, 0, args.Error(2)
	}
	return args.Get(0).([]models.ClanWar), args.Int(1), args.Error(2)
}

func (m *mockClanWarRepository) UpdateWar(ctx context.Context, war *models.ClanWar) error {
	args := m.Called(ctx, war)
	return args.Error(0)
}

func (m *mockClanWarRepository) CreateBattle(ctx context.Context, battle *models.WarBattle) error {
	args := m.Called(ctx, battle)
	return args.Error(0)
}

func (m *mockClanWarRepository) GetBattleByID(ctx context.Context, battleID uuid.UUID) (*models.WarBattle, error) {
	args := m.Called(ctx, battleID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.WarBattle), args.Error(1)
}

func (m *mockClanWarRepository) ListBattles(ctx context.Context, warID *uuid.UUID, status *models.BattleStatus, limit, offset int) ([]models.WarBattle, int, error) {
	args := m.Called(ctx, warID, status, limit, offset)
	if args.Get(0) == nil {
		return nil, 0, args.Error(2)
	}
	return args.Get(0).([]models.WarBattle), args.Int(1), args.Error(2)
}

func (m *mockClanWarRepository) UpdateBattle(ctx context.Context, battle *models.WarBattle) error {
	args := m.Called(ctx, battle)
	return args.Error(0)
}

func (m *mockClanWarRepository) GetTerritoryByID(ctx context.Context, territoryID uuid.UUID) (*models.Territory, error) {
	args := m.Called(ctx, territoryID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Territory), args.Error(1)
}

func (m *mockClanWarRepository) ListTerritories(ctx context.Context, ownerGuildID *uuid.UUID, limit, offset int) ([]models.Territory, int, error) {
	args := m.Called(ctx, ownerGuildID, limit, offset)
	if args.Get(0) == nil {
		return nil, 0, args.Error(2)
	}
	return args.Get(0).([]models.Territory), args.Int(1), args.Error(2)
}

func (m *mockClanWarRepository) UpdateTerritoryOwner(ctx context.Context, territoryID, ownerGuildID uuid.UUID) error {
	args := m.Called(ctx, territoryID, ownerGuildID)
	return args.Error(0)
}

func setupTestService(t *testing.T) (*ClanWarService, *mockClanWarRepository, func()) {
	redisOpts, err := redis.ParseURL("redis://localhost:6379")
	if err != nil {
		t.Skipf("Skipping test due to Redis connection: %v", err)
		return nil, nil, nil
	}
	redisClient := redis.NewClient(redisOpts)

	mockRepo := new(mockClanWarRepository)
	service := &ClanWarService{
		repo:   mockRepo,
		redis:  redisClient,
		logger: GetLogger(),
	}

	cleanup := func() {
		redisClient.Close()
	}

	return service, mockRepo, cleanup
}

