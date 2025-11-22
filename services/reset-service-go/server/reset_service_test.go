package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/reset-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/robfig/cron/v3"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockResetRepository struct {
	mock.Mock
}

func (m *mockResetRepository) Create(ctx context.Context, record *models.ResetRecord) error {
	args := m.Called(ctx, record)
	return args.Error(0)
}

func (m *mockResetRepository) Update(ctx context.Context, record *models.ResetRecord) error {
	args := m.Called(ctx, record)
	return args.Error(0)
}

func (m *mockResetRepository) GetLastReset(ctx context.Context, resetType models.ResetType) (*models.ResetRecord, error) {
	args := m.Called(ctx, resetType)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ResetRecord), args.Error(1)
}

func (m *mockResetRepository) List(ctx context.Context, resetType *models.ResetType, limit, offset int) ([]models.ResetRecord, error) {
	args := m.Called(ctx, resetType, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.ResetRecord), args.Error(1)
}

func (m *mockResetRepository) Count(ctx context.Context, resetType *models.ResetType) (int, error) {
	args := m.Called(ctx, resetType)
	return args.Int(0), args.Error(1)
}

type mockEventBus struct {
	mock.Mock
}

func (m *mockEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	args := m.Called(ctx, eventType, payload)
	return args.Error(0)
}

func setupTestResetService(t *testing.T) (*ResetService, *mockResetRepository, *mockEventBus, func()) {
	mockRepo := new(mockResetRepository)
	mockEventBus := new(mockEventBus)

	redisClient := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		DB:   1,
	})
	redisClient.FlushDB(context.Background())

	cronScheduler := cron.New(cron.WithSeconds())

	service := &ResetService{
		repo:     mockRepo,
		cache:    redisClient,
		cron:     cronScheduler,
		logger:   GetLogger(),
		eventBus: mockEventBus,
	}

	cleanup := func() {
		redisClient.Close()
		cronScheduler.Stop()
	}

	return service, mockRepo, mockEventBus, cleanup
}

func TestResetService_ExecuteDailyReset_Success(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestResetService(t)
	defer cleanup()

	mockRepo.On("Create", context.Background(), mock.AnythingOfType("*models.ResetRecord")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "reset.daily.completed", mock.Anything).Return(nil)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.ResetRecord")).Return(nil)

	err := service.ExecuteDailyReset(context.Background())

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestResetService_ExecuteDailyReset_EventPublishError(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestResetService(t)
	defer cleanup()

	mockRepo.On("Create", context.Background(), mock.AnythingOfType("*models.ResetRecord")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "reset.daily.completed", mock.Anything).Return(assert.AnError)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.ResetRecord")).Return(nil)

	err := service.ExecuteDailyReset(context.Background())

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestResetService_ExecuteWeeklyReset_Success(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestResetService(t)
	defer cleanup()

	mockRepo.On("Create", context.Background(), mock.AnythingOfType("*models.ResetRecord")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "reset.weekly.completed", mock.Anything).Return(nil)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.ResetRecord")).Return(nil)

	err := service.ExecuteWeeklyReset(context.Background())

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestResetService_TriggerReset_Daily(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestResetService(t)
	defer cleanup()

	mockRepo.On("Create", context.Background(), mock.AnythingOfType("*models.ResetRecord")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "reset.daily.completed", mock.Anything).Return(nil)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.ResetRecord")).Return(nil)

	err := service.TriggerReset(context.Background(), models.ResetTypeDaily)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestResetService_TriggerReset_Weekly(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestResetService(t)
	defer cleanup()

	mockRepo.On("Create", context.Background(), mock.AnythingOfType("*models.ResetRecord")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "reset.weekly.completed", mock.Anything).Return(nil)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.ResetRecord")).Return(nil)

	err := service.TriggerReset(context.Background(), models.ResetTypeWeekly)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestResetService_GetResetStats_Success(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestResetService(t)
	defer cleanup()

	now := time.Now()
	lastDaily := &models.ResetRecord{
		ID:          uuid.New(),
		Type:        models.ResetTypeDaily,
		Status:      models.ResetStatusCompleted,
		StartedAt:   now.Add(-24 * time.Hour),
		CompletedAt: &now,
		Metadata:    make(map[string]interface{}),
	}
	lastWeekly := &models.ResetRecord{
		ID:          uuid.New(),
		Type:        models.ResetTypeWeekly,
		Status:      models.ResetStatusCompleted,
		StartedAt:   now.Add(-7 * 24 * time.Hour),
		CompletedAt: &now,
		Metadata:    make(map[string]interface{}),
	}

	mockRepo.On("GetLastReset", context.Background(), models.ResetTypeDaily).Return(lastDaily, nil)
	mockRepo.On("GetLastReset", context.Background(), models.ResetTypeWeekly).Return(lastWeekly, nil)

	result, err := service.GetResetStats(context.Background())

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.NotNil(t, result.LastDailyReset)
	assert.NotNil(t, result.LastWeeklyReset)
	mockRepo.AssertExpectations(t)
}

func TestResetService_GetResetHistory_Success(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestResetService(t)
	defer cleanup()

	records := []models.ResetRecord{
		{
			ID:        uuid.New(),
			Type:      models.ResetTypeDaily,
			Status:    models.ResetStatusCompleted,
			StartedAt: time.Now(),
			Metadata:  make(map[string]interface{}),
		},
	}

	mockRepo.On("List", context.Background(), (*models.ResetType)(nil), 10, 0).Return(records, nil)
	mockRepo.On("Count", context.Background(), (*models.ResetType)(nil)).Return(1, nil)

	result, err := service.GetResetHistory(context.Background(), nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Resets, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestResetService_GetResetHistory_WithType(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestResetService(t)
	defer cleanup()

	resetType := models.ResetTypeDaily
	records := []models.ResetRecord{
		{
			ID:        uuid.New(),
			Type:      models.ResetTypeDaily,
			Status:    models.ResetStatusCompleted,
			StartedAt: time.Now(),
			Metadata:  make(map[string]interface{}),
		},
	}

	mockRepo.On("List", context.Background(), &resetType, 10, 0).Return(records, nil)
	mockRepo.On("Count", context.Background(), &resetType).Return(1, nil)

	result, err := service.GetResetHistory(context.Background(), &resetType, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Resets, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

