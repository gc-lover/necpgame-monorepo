// Issue: #140894950
package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/stretchr/testify/assert"
)

func TestAchievementService_GetPlayerAchievements_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	playerID := uuid.New()
	playerAchievements := []models.PlayerAchievement{
		{
			ID:            uuid.New(),
			PlayerID:      playerID,
			AchievementID: uuid.New(),
			Status:        models.AchievementStatusUnlocked,
			Progress:      100,
			ProgressMax:   100,
			CreatedAt:     time.Now(),
			UpdatedAt:     time.Now(),
		},
	}

	nearCompletion := []models.PlayerAchievement{}
	recentUnlocks := []models.PlayerAchievement{}

	ctx := context.Background()
	mockRepo.On("GetPlayerAchievements", ctx, playerID, (*models.AchievementCategory)(nil), 10, 0).Return(playerAchievements, nil)
	mockRepo.On("CountPlayerAchievements", ctx, playerID).Return(1, 1, nil)
	mockRepo.On("GetNearCompletion", ctx, playerID, 0.8).Return(nearCompletion, nil)
	mockRepo.On("GetRecentUnlocks", ctx, playerID, 10).Return(recentUnlocks, nil)

	response, err := service.GetPlayerAchievements(ctx, playerID, nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 1, response.Total)
	assert.Equal(t, 1, response.Unlocked)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_GetPlayerAchievements_WithCategory(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	playerID := uuid.New()
	category := models.CategoryCombat
	playerAchievements := []models.PlayerAchievement{
		{
			ID:            uuid.New(),
			PlayerID:      playerID,
			AchievementID: uuid.New(),
			Status:        models.AchievementStatusUnlocked,
			Progress:      100,
			ProgressMax:   100,
			CreatedAt:     time.Now(),
			UpdatedAt:     time.Now(),
		},
	}

	nearCompletion := []models.PlayerAchievement{}
	recentUnlocks := []models.PlayerAchievement{}

	ctx := context.Background()
	mockRepo.On("GetPlayerAchievements", ctx, playerID, &category, 10, 0).Return(playerAchievements, nil)
	mockRepo.On("CountPlayerAchievements", ctx, playerID).Return(1, 1, nil)
	mockRepo.On("GetNearCompletion", ctx, playerID, 0.8).Return(nearCompletion, nil)
	mockRepo.On("GetRecentUnlocks", ctx, playerID, 10).Return(recentUnlocks, nil)

	response, err := service.GetPlayerAchievements(ctx, playerID, &category, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 1, response.Total)
	assert.Equal(t, 1, response.Unlocked)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_GetPlayerAchievements_RepositoryError(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	playerID := uuid.New()
	ctx := context.Background()
	mockRepo.On("GetPlayerAchievements", ctx, playerID, (*models.AchievementCategory)(nil), 10, 0).Return(nil, errors.New("database error"))

	response, err := service.GetPlayerAchievements(ctx, playerID, nil, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, response)
	mockRepo.AssertExpectations(t)
}

