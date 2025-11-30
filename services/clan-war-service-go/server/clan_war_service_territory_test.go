// Issue: #140895110
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/clan-war-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

func TestClanWarService_GetTerritory_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	territoryID := uuid.New()
	territory := &models.Territory{
		ID:              territoryID,
		Name:            "Test Territory",
		Region:          "Test Region",
		Resources:       make(map[string]interface{}),
		DefenseLevel:    1,
		SiegeDifficulty: 1,
	}

	mockRepo.On("GetTerritoryByID", mock.Anything, territoryID).Return(territory, nil)

	ctx := context.Background()
	result, err := service.GetTerritory(ctx, territoryID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, territoryID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_ListTerritories_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	ownerGuildID := uuid.New()
	territories := []models.Territory{
		{
			ID:              uuid.New(),
			Name:            "Test Territory",
			Region:          "Test Region",
			OwnerGuildID:    &ownerGuildID,
			Resources:       make(map[string]interface{}),
			DefenseLevel:    1,
			SiegeDifficulty: 1,
		},
	}

	mockRepo.On("ListTerritories", mock.Anything, &ownerGuildID, 10, 0).Return(territories, 1, nil)

	ctx := context.Background()
	result, total, err := service.ListTerritories(ctx, &ownerGuildID, 10, 0)

	require.NoError(t, err)
	assert.Len(t, result, 1)
	assert.Equal(t, 1, total)
	mockRepo.AssertExpectations(t)
}

