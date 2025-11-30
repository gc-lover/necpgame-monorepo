// Issue: #140893532
package server

import (
	"context"
	"errors"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/character-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

func TestCharacterService_GetAccount_DatabaseError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	expectedErr := errors.New("database error")

	mockRepo.On("GetAccountByID", mock.Anything, accountID).Return(nil, expectedErr)

	ctx := context.Background()
	account, err := service.GetAccount(ctx, accountID)

	assert.Error(t, err)
	assert.Equal(t, expectedErr, err)
	assert.Nil(t, account)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_CreateCharacter_DatabaseError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	req := &models.CreateCharacterRequest{
		AccountID: accountID,
		Name:      "Test Character",
		Level:     intPtr(1),
	}
	expectedErr := errors.New("database error")

	mockRepo.On("CreateCharacter", mock.Anything, req).Return(nil, expectedErr)

	ctx := context.Background()
	char, err := service.CreateCharacter(ctx, req)

	assert.Error(t, err)
	assert.Equal(t, expectedErr, err)
	assert.Nil(t, char)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_GetCharactersByAccountID_DatabaseError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	expectedErr := errors.New("database error")

	mockRepo.On("GetCharactersByAccountID", mock.Anything, accountID).Return(nil, expectedErr)

	ctx := context.Background()
	characters, err := service.GetCharactersByAccountID(ctx, accountID)

	assert.Error(t, err)
	assert.Equal(t, expectedErr, err)
	assert.Nil(t, characters)
	mockRepo.AssertExpectations(t)
}

