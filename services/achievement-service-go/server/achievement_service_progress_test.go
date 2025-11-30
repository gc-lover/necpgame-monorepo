// Issue: #140894950
package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

func TestAchievementService_TrackProgress_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	playerID := uuid.New()
	achievementID := uuid.New()
	achievement := &models.Achievement{
		ID:          achievementID,
		Code:        "test_achievement",
		Type:        models.AchievementTypeProgressive,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Points:      10,
		Conditions:  map[string]interface{}{"target": float64(100)},
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	mockRepo.On("GetByID", ctx, achievementID).Return(achievement, nil)
	mockRepo.On("GetPlayerAchievement", ctx, playerID, achievementID).Return(nil, nil)
	mockRepo.On("CreatePlayerAchievement", ctx, mock.AnythingOfType("*models.PlayerAchievement")).Return(nil)

	err := service.TrackProgress(ctx, playerID, achievementID, 50, map[string]interface{}{})

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_TrackProgress_UpdateExisting(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	playerID := uuid.New()
	achievementID := uuid.New()
	achievement := &models.Achievement{
		ID:          achievementID,
		Code:        "test_achievement",
		Type:        models.AchievementTypeProgressive,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Points:      10,
		Conditions:  map[string]interface{}{"target": float64(100)},
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	pa := &models.PlayerAchievement{
		ID:            uuid.New(),
		PlayerID:      playerID,
		AchievementID: achievementID,
		Status:        models.AchievementStatusProgress,
		Progress:      50,
		ProgressMax:   100,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	ctx := context.Background()
	mockRepo.On("GetByID", ctx, achievementID).Return(achievement, nil)
	mockRepo.On("GetPlayerAchievement", ctx, playerID, achievementID).Return(pa, nil)
	mockRepo.On("UpdatePlayerAchievement", ctx, mock.AnythingOfType("*models.PlayerAchievement")).Return(nil)

	err := service.TrackProgress(ctx, playerID, achievementID, 75, map[string]interface{}{})

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_TrackProgress_Unlock(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	playerID := uuid.New()
	achievementID := uuid.New()
	achievement := &models.Achievement{
		ID:          achievementID,
		Code:        "test_achievement",
		Type:        models.AchievementTypeProgressive,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Points:      10,
		Conditions:  map[string]interface{}{"target": float64(100)},
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	pa := &models.PlayerAchievement{
		ID:            uuid.New(),
		PlayerID:      playerID,
		AchievementID: achievementID,
		Status:        models.AchievementStatusProgress,
		Progress:      90,
		ProgressMax:   100,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	ctx := context.Background()
	mockRepo.On("GetByID", ctx, achievementID).Return(achievement, nil)
	mockRepo.On("GetPlayerAchievement", ctx, playerID, achievementID).Return(pa, nil)
	mockRepo.On("UpdatePlayerAchievement", ctx, mock.AnythingOfType("*models.PlayerAchievement")).Return(nil)

	err := service.TrackProgress(ctx, playerID, achievementID, 100, map[string]interface{}{})

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_TrackProgress_AchievementNotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	playerID := uuid.New()
	achievementID := uuid.New()
	ctx := context.Background()
	mockRepo.On("GetByID", ctx, achievementID).Return(nil, nil)

	err := service.TrackProgress(ctx, playerID, achievementID, 50, map[string]interface{}{})

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_UnlockAchievement_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	playerID := uuid.New()
	achievementID := uuid.New()
	achievement := &models.Achievement{
		ID:          achievementID,
		Code:        "test_achievement",
		Type:        models.AchievementTypeOneTime,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Points:      10,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	mockRepo.On("GetByID", ctx, achievementID).Return(achievement, nil)
	mockRepo.On("GetPlayerAchievement", ctx, playerID, achievementID).Return(nil, nil)
	mockRepo.On("CreatePlayerAchievement", ctx, mock.AnythingOfType("*models.PlayerAchievement")).Return(nil)

	err := service.UnlockAchievement(ctx, playerID, achievementID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_UnlockAchievement_UpdateExisting(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	playerID := uuid.New()
	achievementID := uuid.New()
	achievement := &models.Achievement{
		ID:          achievementID,
		Code:        "test_achievement",
		Type:        models.AchievementTypeOneTime,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Points:      10,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	pa := &models.PlayerAchievement{
		ID:            uuid.New(),
		PlayerID:      playerID,
		AchievementID: achievementID,
		Status:        models.AchievementStatusProgress,
		Progress:      50,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	ctx := context.Background()
	mockRepo.On("GetByID", ctx, achievementID).Return(achievement, nil)
	mockRepo.On("GetPlayerAchievement", ctx, playerID, achievementID).Return(pa, nil)
	mockRepo.On("UpdatePlayerAchievement", ctx, mock.AnythingOfType("*models.PlayerAchievement")).Return(nil)

	err := service.UnlockAchievement(ctx, playerID, achievementID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

