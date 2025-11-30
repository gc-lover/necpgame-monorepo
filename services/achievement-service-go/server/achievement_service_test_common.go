// Issue: #140893464
package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/redis/go-redis/v9"
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

	eventBus := NewRedisEventBus(redisClient)

	service := &AchievementService{
		repo:     mockRepo,
		cache:    redisClient,
		logger:   GetLogger(),
		eventBus: eventBus,
	}

	return service, mockRepo, redisClient
}

