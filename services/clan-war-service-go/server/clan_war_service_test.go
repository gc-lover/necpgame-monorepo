package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/clan-war-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
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

func TestClanWarService_DeclareWar_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	attackerGuildID := uuid.New()
	defenderGuildID := uuid.New()
	req := &models.DeclareWarRequest{
		AttackerGuildID: attackerGuildID,
		DefenderGuildID: defenderGuildID,
		Allies:          []uuid.UUID{},
	}

	mockRepo.On("CreateWar", mock.Anything, mock.AnythingOfType("*models.ClanWar")).Return(nil)

	ctx := context.Background()
	war, err := service.DeclareWar(ctx, req)

	require.NoError(t, err)
	assert.NotNil(t, war)
	assert.Equal(t, attackerGuildID, war.AttackerGuildID)
	assert.Equal(t, defenderGuildID, war.DefenderGuildID)
	assert.Equal(t, models.WarStatusDeclared, war.Status)
	assert.Equal(t, models.WarPhasePreparation, war.Phase)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_DeclareWar_SameGuild(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	guildID := uuid.New()
	req := &models.DeclareWarRequest{
		AttackerGuildID: guildID,
		DefenderGuildID: guildID,
		Allies:          []uuid.UUID{},
	}

	ctx := context.Background()
	war, err := service.DeclareWar(ctx, req)

	assert.Error(t, err)
	assert.Nil(t, war)
	assert.Contains(t, err.Error(), "attacker and defender cannot be the same guild")
	mockRepo.AssertNotCalled(t, "CreateWar", mock.Anything, mock.Anything)
}

func TestClanWarService_GetWar_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	warID := uuid.New()
	war := &models.ClanWar{
		ID:              warID,
		AttackerGuildID: uuid.New(),
		DefenderGuildID: uuid.New(),
		Status:          models.WarStatusDeclared,
		Phase:           models.WarPhasePreparation,
	}

	mockRepo.On("GetWarByID", mock.Anything, warID).Return(war, nil)

	ctx := context.Background()
	result, err := service.GetWar(ctx, warID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, warID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_ListWars_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	guildID := uuid.New()
	wars := []models.ClanWar{
		{
			ID:              uuid.New(),
			AttackerGuildID: guildID,
			Status:          models.WarStatusDeclared,
		},
	}

	mockRepo.On("ListWars", mock.Anything, &guildID, (*models.WarStatus)(nil), 10, 0).Return(wars, 1, nil)

	ctx := context.Background()
	result, total, err := service.ListWars(ctx, &guildID, nil, 10, 0)

	require.NoError(t, err)
	assert.Len(t, result, 1)
	assert.Equal(t, 1, total)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_StartWar_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	warID := uuid.New()
	war := &models.ClanWar{
		ID:              warID,
		AttackerGuildID: uuid.New(),
		DefenderGuildID: uuid.New(),
		Status:          models.WarStatusDeclared,
		Phase:           models.WarPhasePreparation,
		StartTime:       time.Now().Add(-1 * time.Hour),
	}

	mockRepo.On("GetWarByID", mock.Anything, warID).Return(war, nil)
	mockRepo.On("UpdateWar", mock.Anything, mock.AnythingOfType("*models.ClanWar")).Return(nil)

	ctx := context.Background()
	err := service.StartWar(ctx, warID)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_StartWar_NotDeclared(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	warID := uuid.New()
	war := &models.ClanWar{
		ID:              warID,
		AttackerGuildID: uuid.New(),
		DefenderGuildID: uuid.New(),
		Status:          models.WarStatusOngoing,
		Phase:           models.WarPhaseActive,
		StartTime:       time.Now().Add(-1 * time.Hour),
	}

	mockRepo.On("GetWarByID", mock.Anything, warID).Return(war, nil)

	ctx := context.Background()
	err := service.StartWar(ctx, warID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "war is not in declared status")
	mockRepo.AssertNotCalled(t, "UpdateWar", mock.Anything, mock.Anything)
}

func TestClanWarService_CompleteWar_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	warID := uuid.New()
	attackerGuildID := uuid.New()
	war := &models.ClanWar{
		ID:              warID,
		AttackerGuildID: attackerGuildID,
		DefenderGuildID: uuid.New(),
		Status:          models.WarStatusOngoing,
		Phase:           models.WarPhaseActive,
		AttackerScore:   100,
		DefenderScore:   50,
	}

	mockRepo.On("GetWarByID", mock.Anything, warID).Return(war, nil)
	mockRepo.On("UpdateWar", mock.Anything, mock.AnythingOfType("*models.ClanWar")).Return(nil)

	ctx := context.Background()
	err := service.CompleteWar(ctx, warID)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_CreateBattle_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	warID := uuid.New()
	war := &models.ClanWar{
		ID:              warID,
		AttackerGuildID: uuid.New(),
		DefenderGuildID: uuid.New(),
		Status:          models.WarStatusOngoing,
		Phase:           models.WarPhaseActive,
	}

	req := &models.CreateBattleRequest{
		WarID:     warID,
		Type:      models.BattleTypeTerritory,
		StartTime: time.Now().Add(1 * time.Hour),
	}

	mockRepo.On("GetWarByID", mock.Anything, warID).Return(war, nil)
	mockRepo.On("CreateBattle", mock.Anything, mock.AnythingOfType("*models.WarBattle")).Return(nil)

	ctx := context.Background()
	battle, err := service.CreateBattle(ctx, req)

	require.NoError(t, err)
	assert.NotNil(t, battle)
	assert.Equal(t, warID, battle.WarID)
	assert.Equal(t, models.BattleTypeTerritory, battle.Type)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_CreateBattle_WarNotOngoing(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	warID := uuid.New()
	war := &models.ClanWar{
		ID:              warID,
		AttackerGuildID: uuid.New(),
		DefenderGuildID: uuid.New(),
		Status:          models.WarStatusDeclared,
		Phase:           models.WarPhasePreparation,
	}

	req := &models.CreateBattleRequest{
		WarID:     warID,
		Type:      models.BattleTypeTerritory,
		StartTime: time.Now().Add(1 * time.Hour),
	}

	mockRepo.On("GetWarByID", mock.Anything, warID).Return(war, nil)

	ctx := context.Background()
	battle, err := service.CreateBattle(ctx, req)

	assert.Error(t, err)
	assert.Nil(t, battle)
	assert.Contains(t, err.Error(), "war is not in ongoing status")
	mockRepo.AssertNotCalled(t, "CreateBattle", mock.Anything, mock.Anything)
}

func TestClanWarService_DeclareWar_DatabaseError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	attackerGuildID := uuid.New()
	defenderGuildID := uuid.New()
	req := &models.DeclareWarRequest{
		AttackerGuildID: attackerGuildID,
		DefenderGuildID: defenderGuildID,
		Allies:          []uuid.UUID{},
	}
	expectedErr := errors.New("database error")

	mockRepo.On("CreateWar", mock.Anything, mock.AnythingOfType("*models.ClanWar")).Return(expectedErr)

	ctx := context.Background()
	war, err := service.DeclareWar(ctx, req)

	assert.Error(t, err)
	assert.Nil(t, war)
	mockRepo.AssertExpectations(t)
}

