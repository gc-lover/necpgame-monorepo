// Issue: #140895110
package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/clan-war-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

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

func TestClanWarService_GetBattle_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	battleID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         uuid.New(),
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusScheduled,
		AttackerScore: 0,
		DefenderScore: 0,
		StartTime:     time.Now().Add(1 * time.Hour),
	}

	mockRepo.On("GetBattleByID", mock.Anything, battleID).Return(battle, nil)

	ctx := context.Background()
	result, err := service.GetBattle(ctx, battleID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, battleID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_ListBattles_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	warID := uuid.New()
	battles := []models.WarBattle{
		{
			ID:            uuid.New(),
			WarID:         warID,
			Type:          models.BattleTypeTerritory,
			Status:        models.BattleStatusScheduled,
			AttackerScore: 0,
			DefenderScore: 0,
			StartTime:     time.Now().Add(1 * time.Hour),
		},
	}

	mockRepo.On("ListBattles", mock.Anything, &warID, (*models.BattleStatus)(nil), 10, 0).Return(battles, 1, nil)

	ctx := context.Background()
	result, total, err := service.ListBattles(ctx, &warID, nil, 10, 0)

	require.NoError(t, err)
	assert.Len(t, result, 1)
	assert.Equal(t, 1, total)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_StartBattle_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	battleID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         uuid.New(),
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusScheduled,
		AttackerScore: 0,
		DefenderScore: 0,
		StartTime:     time.Now().Add(-1 * time.Hour),
	}

	mockRepo.On("GetBattleByID", mock.Anything, battleID).Return(battle, nil)
	mockRepo.On("UpdateBattle", mock.Anything, mock.AnythingOfType("*models.WarBattle")).Return(nil)

	ctx := context.Background()
	err := service.StartBattle(ctx, battleID)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_StartBattle_NotScheduled(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	battleID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         uuid.New(),
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusActive,
		AttackerScore: 0,
		DefenderScore: 0,
		StartTime:     time.Now().Add(-1 * time.Hour),
	}

	mockRepo.On("GetBattleByID", mock.Anything, battleID).Return(battle, nil)

	ctx := context.Background()
	err := service.StartBattle(ctx, battleID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "battle is not in scheduled status")
	mockRepo.AssertNotCalled(t, "UpdateBattle", mock.Anything, mock.Anything)
}

func TestClanWarService_StartBattle_StartTimeNotReached(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	battleID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         uuid.New(),
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusScheduled,
		AttackerScore: 0,
		DefenderScore: 0,
		StartTime:     time.Now().Add(1 * time.Hour),
	}

	mockRepo.On("GetBattleByID", mock.Anything, battleID).Return(battle, nil)

	ctx := context.Background()
	err := service.StartBattle(ctx, battleID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "battle start time has not been reached")
	mockRepo.AssertNotCalled(t, "UpdateBattle", mock.Anything, mock.Anything)
}

func TestClanWarService_UpdateBattleScore_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	battleID := uuid.New()
	warID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         warID,
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusActive,
		AttackerScore: 0,
		DefenderScore: 0,
		StartTime:     time.Now().Add(-1 * time.Hour),
	}

	war := &models.ClanWar{
		ID:              warID,
		AttackerGuildID: uuid.New(),
		DefenderGuildID: uuid.New(),
		Status:          models.WarStatusOngoing,
		Phase:           models.WarPhaseActive,
		AttackerScore:   0,
		DefenderScore:   0,
	}

	req := &models.UpdateBattleScoreRequest{
		BattleID:      battleID,
		AttackerScore: 10,
		DefenderScore: 5,
	}

	mockRepo.On("GetBattleByID", mock.Anything, battleID).Return(battle, nil)
	mockRepo.On("UpdateBattle", mock.Anything, mock.AnythingOfType("*models.WarBattle")).Return(nil)
	mockRepo.On("GetWarByID", mock.Anything, warID).Return(war, nil)
	mockRepo.On("UpdateWar", mock.Anything, mock.AnythingOfType("*models.ClanWar")).Return(nil)

	ctx := context.Background()
	err := service.UpdateBattleScore(ctx, req)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_UpdateBattleScore_NotActive(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	battleID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         uuid.New(),
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusScheduled,
		AttackerScore: 0,
		DefenderScore: 0,
		StartTime:     time.Now().Add(1 * time.Hour),
	}

	req := &models.UpdateBattleScoreRequest{
		BattleID:      battleID,
		AttackerScore: 10,
		DefenderScore: 5,
	}

	mockRepo.On("GetBattleByID", mock.Anything, battleID).Return(battle, nil)

	ctx := context.Background()
	err := service.UpdateBattleScore(ctx, req)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "battle is not in active status")
	mockRepo.AssertNotCalled(t, "UpdateBattle", mock.Anything, mock.Anything)
}

func TestClanWarService_CompleteBattle_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	battleID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         uuid.New(),
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusActive,
		AttackerScore: 10,
		DefenderScore: 5,
		StartTime:     time.Now().Add(-1 * time.Hour),
	}

	mockRepo.On("GetBattleByID", mock.Anything, battleID).Return(battle, nil)
	mockRepo.On("UpdateBattle", mock.Anything, mock.AnythingOfType("*models.WarBattle")).Return(nil)

	ctx := context.Background()
	err := service.CompleteBattle(ctx, battleID)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_CompleteBattle_NotActive(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	battleID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         uuid.New(),
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusScheduled,
		AttackerScore: 0,
		DefenderScore: 0,
		StartTime:     time.Now().Add(1 * time.Hour),
	}

	mockRepo.On("GetBattleByID", mock.Anything, battleID).Return(battle, nil)

	ctx := context.Background()
	err := service.CompleteBattle(ctx, battleID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "battle is not in active status")
	mockRepo.AssertNotCalled(t, "UpdateBattle", mock.Anything, mock.Anything)
}

