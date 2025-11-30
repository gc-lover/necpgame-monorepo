// Issue: #140895110
package server

import (
	"context"
	"errors"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/clan-war-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

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

