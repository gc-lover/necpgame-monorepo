// Issue: #140893532
package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/character-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

func TestCharacterService_UpdateCharacter_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	accountID := uuid.New()
	newName := "Updated Character"
	req := &models.UpdateCharacterRequest{
		Name: &newName,
	}
	char := &models.Character{
		ID:        characterID,
		AccountID: accountID,
		Name:      newName,
		Level:     1,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockRepo.On("UpdateCharacter", mock.Anything, characterID, req).Return(char, nil)

	ctx := context.Background()
	result, err := service.UpdateCharacter(ctx, characterID, req)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, newName, result.Name)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_UpdateCharacter_Partial(t *testing.T) {
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

	newLevel := 10
	updateReq := &models.UpdateCharacterRequest{
		Level: &newLevel,
	}

	updatedChar := &models.Character{
		ID:        characterID,
		AccountID: char.AccountID,
		Name:      char.Name,
		Level:     newLevel,
		CreatedAt: char.CreatedAt,
		UpdatedAt: time.Now(),
	}

	mockRepo.On("UpdateCharacter", mock.Anything, characterID, updateReq).Return(updatedChar, nil)

	ctx := context.Background()
	result, err := service.UpdateCharacter(ctx, characterID, updateReq)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, newLevel, result.Level)
	assert.Equal(t, char.Name, result.Name)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_UpdateCharacter_DatabaseError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	newName := "Updated Character"
	updateReq := &models.UpdateCharacterRequest{
		Name: &newName,
	}
	expectedErr := errors.New("database error")

	mockRepo.On("UpdateCharacter", mock.Anything, characterID, updateReq).Return(nil, expectedErr)

	ctx := context.Background()
	result, err := service.UpdateCharacter(ctx, characterID, updateReq)

	assert.Error(t, err)
	assert.Equal(t, expectedErr, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}
