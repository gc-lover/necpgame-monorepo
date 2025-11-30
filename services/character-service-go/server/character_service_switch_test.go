// Issue: #140893532
package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/character-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

func TestCharacterService_ValidateCharacter_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	char := &models.Character{
		ID:        characterID,
		AccountID: uuid.New(),
		Name:      "Test Character",
		Level:     1,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockRepo.On("GetCharacterByID", mock.Anything, characterID).Return(char, nil)

	ctx := context.Background()
	valid, err := service.ValidateCharacter(ctx, characterID)

	require.NoError(t, err)
	assert.True(t, valid)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_ValidateCharacter_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()

	mockRepo.On("GetCharacterByID", mock.Anything, characterID).Return(nil, nil)

	ctx := context.Background()
	valid, err := service.ValidateCharacter(ctx, characterID)

	require.NoError(t, err)
	assert.False(t, valid)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_SwitchCharacter_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	characterID1 := uuid.New()
	characterID2 := uuid.New()
	characters := []models.Character{
		{
			ID:        characterID1,
			AccountID: accountID,
			Name:      "Character 1",
			Level:     1,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
		{
			ID:        characterID2,
			AccountID: accountID,
			Name:      "Character 2",
			Level:     5,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
	}

	mockRepo.On("GetCharactersByAccountID", mock.Anything, accountID).Return(characters, nil)

	ctx := context.Background()
	result, err := service.SwitchCharacter(ctx, accountID, characterID2)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.Success)
	assert.NotNil(t, result.CurrentCharacter)
	assert.Equal(t, characterID2, result.CurrentCharacter.ID)
	assert.NotNil(t, result.PreviousCharacterID)
	assert.Equal(t, characterID1, *result.PreviousCharacterID)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_SwitchCharacter_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	characterID := uuid.New()
	characters := []models.Character{
		{
			ID:        uuid.New(),
			AccountID: accountID,
			Name:      "Character 1",
			Level:     1,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
	}

	mockRepo.On("GetCharactersByAccountID", mock.Anything, accountID).Return(characters, nil)

	ctx := context.Background()
	result, err := service.SwitchCharacter(ctx, accountID, characterID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.False(t, result.Success)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_SwitchCharacter_EmptyList(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	characterID := uuid.New()

	mockRepo.On("GetCharactersByAccountID", mock.Anything, accountID).Return([]models.Character{}, nil)

	ctx := context.Background()
	result, err := service.SwitchCharacter(ctx, accountID, characterID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.False(t, result.Success)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_SwitchCharacter_SingleCharacter(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	characterID := uuid.New()
	characters := []models.Character{
		{
			ID:        characterID,
			AccountID: accountID,
			Name:      "Character 1",
			Level:     1,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
	}

	mockRepo.On("GetCharactersByAccountID", mock.Anything, accountID).Return(characters, nil)

	ctx := context.Background()
	result, err := service.SwitchCharacter(ctx, accountID, characterID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.Success)
	assert.NotNil(t, result.CurrentCharacter)
	assert.Equal(t, characterID, result.CurrentCharacter.ID)
	assert.Nil(t, result.PreviousCharacterID)
	mockRepo.AssertExpectations(t)
}

