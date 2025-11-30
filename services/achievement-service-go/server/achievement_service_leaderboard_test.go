// Issue: #140893464
package server

import (
	"context"
	"encoding/json"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/stretchr/testify/assert"
)

func TestAchievementService_GetLeaderboard_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	entries := []models.LeaderboardEntry{
		{
			Rank:     1,
			PlayerID: uuid.New(),
			Points:   100,
			Unlocked: 10,
		},
		{
			Rank:     2,
			PlayerID: uuid.New(),
			Points:   50,
			Unlocked: 5,
		},
	}

	ctx := context.Background()
	redisClient.Del(ctx, "leaderboard:*")
	mockRepo.On("GetLeaderboard", ctx, "all", 10).Return(entries, nil)

	response, err := service.GetLeaderboard(ctx, "all", 10)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 2, response.Total)
	assert.Len(t, response.Entries, 2)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_GetLeaderboard_Cache(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	entries := []models.LeaderboardEntry{
		{
			Rank:     1,
			PlayerID: uuid.New(),
			Points:   100,
			Unlocked: 10,
		},
	}

	response := &models.LeaderboardResponse{
		Entries: entries,
		Total:   1,
		Period:  "all",
	}

	responseJSON, _ := json.Marshal(response)
	cacheKey := "leaderboard:all:limit:10"
	ctx := context.Background()
	redisClient.Set(ctx, cacheKey, responseJSON, 1*time.Minute)

	cachedResponse, err := service.GetLeaderboard(ctx, "all", 10)

	assert.NoError(t, err)
	assert.NotNil(t, cachedResponse)
	assert.Equal(t, 1, cachedResponse.Total)
	mockRepo.AssertNotCalled(t, "GetLeaderboard", ctx, "all", 10)
}

func TestAchievementService_GetLeaderboard_Daily(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	entries := []models.LeaderboardEntry{
		{
			Rank:     1,
			PlayerID: uuid.New(),
			Points:   100,
			Unlocked: 10,
		},
	}

	ctx := context.Background()
	redisClient.Del(ctx, "leaderboard:*")
	mockRepo.On("GetLeaderboard", ctx, "daily", 10).Return(entries, nil)

	response, err := service.GetLeaderboard(ctx, "daily", 10)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 1, response.Total)
	assert.Equal(t, "daily", response.Period)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_GetLeaderboard_Weekly(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	entries := []models.LeaderboardEntry{
		{
			Rank:     1,
			PlayerID: uuid.New(),
			Points:   100,
			Unlocked: 10,
		},
	}

	ctx := context.Background()
	redisClient.Del(ctx, "leaderboard:*")
	mockRepo.On("GetLeaderboard", ctx, "weekly", 10).Return(entries, nil)

	response, err := service.GetLeaderboard(ctx, "weekly", 10)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 1, response.Total)
	assert.Equal(t, "weekly", response.Period)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_GetAchievementStats_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	achievementID := uuid.New()
	stats := &models.AchievementStatsResponse{
		AchievementID: achievementID,
		TotalUnlocks:  100,
		UnlockPercent: 50.0,
	}

	ctx := context.Background()
	mockRepo.On("GetAchievementStats", ctx, achievementID).Return(stats, nil)

	result, err := service.GetAchievementStats(ctx, achievementID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, achievementID, result.AchievementID)
	assert.Equal(t, 100, result.TotalUnlocks)
	mockRepo.AssertExpectations(t)
}

