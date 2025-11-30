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

func TestClanWarService_StartWar_StartTimeNotReached(t *testing.T) {
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
		StartTime:       time.Now().Add(1 * time.Hour),
	}

	mockRepo.On("GetWarByID", mock.Anything, warID).Return(war, nil)

	ctx := context.Background()
	err := service.StartWar(ctx, warID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "war start time has not been reached")
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

func TestClanWarService_CompleteWar_NotOngoing(t *testing.T) {
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
		AttackerScore:   100,
		DefenderScore:   50,
	}

	mockRepo.On("GetWarByID", mock.Anything, warID).Return(war, nil)

	ctx := context.Background()
	err := service.CompleteWar(ctx, warID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "war is not in ongoing status")
	mockRepo.AssertNotCalled(t, "UpdateWar", mock.Anything, mock.Anything)
}

