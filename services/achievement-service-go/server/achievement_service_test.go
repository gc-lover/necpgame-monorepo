// Issue: #140893464
package server

import (
	"context"
	"encoding/json"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

func TestAchievementService_CreateAchievement_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	achievement := &models.Achievement{
		Code:        "test_achievement",
		Type:        models.AchievementTypeOneTime,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Description: "Test description",
		Points:      10,
		Conditions:  map[string]interface{}{"target": 100},
		Rewards:     map[string]interface{}{"exp": 100},
		IsHidden:    false,
		IsSeasonal:  false,
	}

	ctx := context.Background()
	mockRepo.On("Create", ctx, mock.AnythingOfType("*models.Achievement")).Return(nil)

	err := service.CreateAchievement(ctx, achievement)

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, achievement.ID)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_CreateAchievement_RepositoryError(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	achievement := &models.Achievement{
		Code:        "test_achievement",
		Type:        models.AchievementTypeOneTime,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Description: "Test description",
		Points:      10,
	}

	ctx := context.Background()
	mockRepo.On("Create", ctx, mock.AnythingOfType("*models.Achievement")).Return(errors.New("database error"))

	err := service.CreateAchievement(ctx, achievement)

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_GetAchievement_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	achievementID := uuid.New()
	achievement := &models.Achievement{
		ID:          achievementID,
		Code:        "test_achievement",
		Type:        models.AchievementTypeOneTime,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Description: "Test description",
		Points:      10,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	mockRepo.On("GetByID", ctx, achievementID).Return(achievement, nil)

	result, err := service.GetAchievement(ctx, achievementID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, achievementID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_GetAchievement_NotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	achievementID := uuid.New()
	ctx := context.Background()
	mockRepo.On("GetByID", ctx, achievementID).Return(nil, nil)

	result, err := service.GetAchievement(ctx, achievementID)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_GetAchievement_Cache(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	achievementID := uuid.New()
	achievement := &models.Achievement{
		ID:          achievementID,
		Code:        "test_achievement",
		Type:        models.AchievementTypeOneTime,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Description: "Test description",
		Points:      10,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	achievementJSON, _ := json.Marshal(achievement)
	cacheKey := "achievement:" + achievementID.String()
	ctx := context.Background()
	redisClient.Set(ctx, cacheKey, achievementJSON, 10*time.Minute)

	cachedResult, err := service.GetAchievement(ctx, achievementID)

	assert.NoError(t, err)
	assert.NotNil(t, cachedResult)
	assert.Equal(t, achievementID, cachedResult.ID)
	mockRepo.AssertNotCalled(t, "GetByID", ctx, achievementID)
}

func TestAchievementService_GetAchievement_RepositoryError(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	achievementID := uuid.New()
	ctx := context.Background()
	mockRepo.On("GetByID", ctx, achievementID).Return(nil, errors.New("database error"))

	result, err := service.GetAchievement(ctx, achievementID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_GetAchievementByCode_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	achievement := &models.Achievement{
		ID:          uuid.New(),
		Code:        "test_achievement",
		Type:        models.AchievementTypeOneTime,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Description: "Test description",
		Points:      10,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	mockRepo.On("GetByCode", ctx, "test_achievement").Return(achievement, nil)

	result, err := service.GetAchievementByCode(ctx, "test_achievement")

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, "test_achievement", result.Code)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_ListAchievements_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	achievements := []models.Achievement{
		{
			ID:          uuid.New(),
			Code:        "test_achievement_1",
			Type:        models.AchievementTypeOneTime,
			Category:    models.CategoryCombat,
			Rarity:      models.RarityCommon,
			Title:       "Test Achievement 1",
			Points:      10,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
		{
			ID:          uuid.New(),
			Code:        "test_achievement_2",
			Type:        models.AchievementTypeOneTime,
			Category:    models.CategoryCombat,
			Rarity:      models.RarityCommon,
			Title:       "Test Achievement 2",
			Points:      20,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
	}

	categories := map[string]int{
		"combat": 2,
	}

	ctx := context.Background()
	mockRepo.On("List", ctx, (*models.AchievementCategory)(nil), 10, 0).Return(achievements, nil)
	mockRepo.On("Count", ctx, (*models.AchievementCategory)(nil)).Return(2, nil)
	mockRepo.On("CountByCategory", ctx).Return(categories, nil)

	response, err := service.ListAchievements(ctx, nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 2, response.Total)
	assert.Len(t, response.Achievements, 2)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_ListAchievements_WithCategory(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	category := models.CategoryCombat
	achievements := []models.Achievement{
		{
			ID:          uuid.New(),
			Code:        "test_achievement_1",
			Type:        models.AchievementTypeOneTime,
			Category:    category,
			Rarity:      models.RarityCommon,
			Title:       "Test Achievement 1",
			Points:      10,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
	}

	categories := map[string]int{
		"combat": 1,
	}

	ctx := context.Background()
	mockRepo.On("List", ctx, &category, 10, 0).Return(achievements, nil)
	mockRepo.On("Count", ctx, &category).Return(1, nil)
	mockRepo.On("CountByCategory", ctx).Return(categories, nil)

	response, err := service.ListAchievements(ctx, &category, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 1, response.Total)
	assert.Len(t, response.Achievements, 1)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_ListAchievements_ListError(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	ctx := context.Background()
	redisClient.FlushDB(ctx)
	mockRepo.On("List", ctx, (*models.AchievementCategory)(nil), 10, 0).Return(nil, errors.New("database error"))

	response, err := service.ListAchievements(ctx, nil, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, response)
	mockRepo.AssertExpectations(t)
}
