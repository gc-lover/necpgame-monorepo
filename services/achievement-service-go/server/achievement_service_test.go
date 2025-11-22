package server

import (
	"context"
	"encoding/json"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockAchievementRepository struct {
	mock.Mock
}

func (m *mockAchievementRepository) Create(ctx context.Context, achievement *models.Achievement) error {
	args := m.Called(ctx, achievement)
	return args.Error(0)
}

func (m *mockAchievementRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Achievement, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Achievement), args.Error(1)
}

func (m *mockAchievementRepository) GetByCode(ctx context.Context, code string) (*models.Achievement, error) {
	args := m.Called(ctx, code)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Achievement), args.Error(1)
}

func (m *mockAchievementRepository) List(ctx context.Context, category *models.AchievementCategory, limit, offset int) ([]models.Achievement, error) {
	args := m.Called(ctx, category, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.Achievement), args.Error(1)
}

func (m *mockAchievementRepository) Count(ctx context.Context, category *models.AchievementCategory) (int, error) {
	args := m.Called(ctx, category)
	return args.Int(0), args.Error(1)
}

func (m *mockAchievementRepository) CountByCategory(ctx context.Context) (map[string]int, error) {
	args := m.Called(ctx)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(map[string]int), args.Error(1)
}

func (m *mockAchievementRepository) GetPlayerAchievement(ctx context.Context, playerID, achievementID uuid.UUID) (*models.PlayerAchievement, error) {
	args := m.Called(ctx, playerID, achievementID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerAchievement), args.Error(1)
}

func (m *mockAchievementRepository) CreatePlayerAchievement(ctx context.Context, pa *models.PlayerAchievement) error {
	args := m.Called(ctx, pa)
	return args.Error(0)
}

func (m *mockAchievementRepository) UpdatePlayerAchievement(ctx context.Context, pa *models.PlayerAchievement) error {
	args := m.Called(ctx, pa)
	return args.Error(0)
}

func (m *mockAchievementRepository) GetPlayerAchievements(ctx context.Context, playerID uuid.UUID, category *models.AchievementCategory, limit, offset int) ([]models.PlayerAchievement, error) {
	args := m.Called(ctx, playerID, category, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.PlayerAchievement), args.Error(1)
}

func (m *mockAchievementRepository) CountPlayerAchievements(ctx context.Context, playerID uuid.UUID) (int, int, error) {
	args := m.Called(ctx, playerID)
	return args.Int(0), args.Int(1), args.Error(2)
}

func (m *mockAchievementRepository) GetNearCompletion(ctx context.Context, playerID uuid.UUID, threshold float64) ([]models.PlayerAchievement, error) {
	args := m.Called(ctx, playerID, threshold)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.PlayerAchievement), args.Error(1)
}

func (m *mockAchievementRepository) GetRecentUnlocks(ctx context.Context, playerID uuid.UUID, limit int) ([]models.PlayerAchievement, error) {
	args := m.Called(ctx, playerID, limit)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.PlayerAchievement), args.Error(1)
}

func (m *mockAchievementRepository) GetLeaderboard(ctx context.Context, period string, limit int) ([]models.LeaderboardEntry, error) {
	args := m.Called(ctx, period, limit)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.LeaderboardEntry), args.Error(1)
}

func (m *mockAchievementRepository) GetAchievementStats(ctx context.Context, achievementID uuid.UUID) (*models.AchievementStatsResponse, error) {
	args := m.Called(ctx, achievementID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.AchievementStatsResponse), args.Error(1)
}

func setupTestService() (*AchievementService, *mockAchievementRepository, *redis.Client) {
	mockRepo := new(mockAchievementRepository)
	redisClient := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		DB:   1,
	})

	service := &AchievementService{
		repo:   mockRepo,
		cache:  redisClient,
		logger: GetLogger(),
	}

	return service, mockRepo, redisClient
}

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
		Period:   "all",
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

func TestAchievementService_GetAchievementStats_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	achievementID := uuid.New()
	stats := &models.AchievementStatsResponse{
		AchievementID: achievementID,
		TotalUnlocks:  100,
		UnlockPercent:  50.0,
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

