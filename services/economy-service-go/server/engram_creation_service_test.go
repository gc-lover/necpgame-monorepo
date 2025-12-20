// Issue: #140894175
package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockEngramCreationRepository struct {
	mock.Mock
}

func (m *mockEngramCreationRepository) CreateCreationLog(ctx context.Context, creation *EngramCreation) error {
	args := m.Called(ctx, creation)
	return args.Error(0)
}

func (m *mockEngramCreationRepository) GetCreationLogByCreationID(ctx context.Context, creationID uuid.UUID) (*EngramCreation, error) {
	args := m.Called(ctx, creationID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*EngramCreation), args.Error(1)
}

func (m *mockEngramCreationRepository) GetCreationLogByEngramID(ctx context.Context, engramID uuid.UUID) (*EngramCreation, error) {
	args := m.Called(ctx, engramID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*EngramCreation), args.Error(1)
}

func (m *mockEngramCreationRepository) UpdateCreationStage(ctx context.Context, creationID uuid.UUID, stage string, dataLossPercent *float64, isComplete *bool) error {
	args := m.Called(ctx, creationID, stage, dataLossPercent, isComplete)
	return args.Error(0)
}

func (m *mockEngramCreationRepository) CompleteCreation(ctx context.Context, creationID uuid.UUID, engramID uuid.UUID) error {
	args := m.Called(ctx, creationID, engramID)
	return args.Error(0)
}

func setupTestEngramCreationService() (*EngramCreationService, *mockEngramCreationRepository, *redis.Client) {
	mockRepo := new(mockEngramCreationRepository)
	redisClient := redis.NewClient(&redis.Options{
		Addr:         "localhost:6379",
		DB:           1,
		DialTimeout:  1 * time.Second, // Fast timeout for tests
		ReadTimeout:  1 * time.Second,
		WriteTimeout: 1 * time.Second,
		PoolTimeout:  1 * time.Second,
	})

	service := NewEngramCreationService(mockRepo, redisClient)

	return service, mockRepo, redisClient
}

func TestEngramCreationService_GetCreationCost_Success(t *testing.T) {
	service, _, redisClient := setupTestEngramCreationService()
	defer redisClient.Close()

	ctx := context.Background()
	cost, err := service.GetCreationCost(ctx, 3)

	assert.NoError(t, err)
	assert.NotNil(t, cost)
	assert.Equal(t, 3, cost.ChipTier)
	assert.Greater(t, cost.CreationCostMin, 0.0)
	assert.Greater(t, cost.CreationCostMax, cost.CreationCostMin)
	assert.Greater(t, cost.MarketFluctuation, 0.0)
}

func TestEngramCreationService_GetCreationCost_InvalidTier(t *testing.T) {
	service, _, redisClient := setupTestEngramCreationService()
	defer redisClient.Close()

	ctx := context.Background()

	_, err := service.GetCreationCost(ctx, 0)
	assert.Error(t, err)
	assert.Equal(t, ErrInvalidChipTier, err)

	_, err = service.GetCreationCost(ctx, 6)
	assert.Error(t, err)
	assert.Equal(t, ErrInvalidChipTier, err)
}

func TestEngramCreationService_GetCreationCost_AllTiers(t *testing.T) {
	service, _, redisClient := setupTestEngramCreationService()
	defer redisClient.Close()

	ctx := context.Background()

	for tier := 1; tier <= 5; tier++ {
		cost, err := service.GetCreationCost(ctx, tier)
		assert.NoError(t, err)
		assert.NotNil(t, cost)
		assert.Equal(t, tier, cost.ChipTier)
	}
}

func TestEngramCreationService_ValidateCreation_Success(t *testing.T) {
	service, _, redisClient := setupTestEngramCreationService()
	defer redisClient.Close()

	ctx := context.Background()
	characterID := uuid.New()
	targetPersonID := uuidPtr(uuid.New())

	result, err := service.ValidateCreation(ctx, characterID, 3, targetPersonID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.IsValid)
	assert.NotNil(t, result.Requirements)
	assert.NotNil(t, result.EstimatedCost)
}

func TestEngramCreationService_ValidateCreation_InvalidTier(t *testing.T) {
	service, _, redisClient := setupTestEngramCreationService()
	defer redisClient.Close()

	ctx := context.Background()
	characterID := uuid.New()

	result, err := service.ValidateCreation(ctx, characterID, 0, nil)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.False(t, result.IsValid)
	assert.Contains(t, result.ValidationErrors, "invalid chip tier")
}

func TestEngramCreationService_CreateEngram_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestEngramCreationService()
	defer redisClient.Close()

	ctx := context.Background()
	characterID := uuid.New()
	chipTier := 3
	attitudeType := "friendly"
	customAttitudeSettings := map[string]interface{}{"trust": 0.8}
	targetPersonID := uuidPtr(uuid.New())

	mockRepo.On("CreateCreationLog", ctx, mock.AnythingOfType("*server.EngramCreation")).Return(nil)

	result, err := service.CreateEngram(ctx, characterID, chipTier, attitudeType, customAttitudeSettings, targetPersonID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.Success)
	assert.NotEqual(t, uuid.Nil, result.EngramID)
	assert.NotEqual(t, uuid.Nil, result.CreationID)
	assert.Equal(t, "completed", result.CreationStage)
	assert.NotNil(t, result.DataLossPercent)
	assert.Greater(t, result.CreationCost, 0.0)
	mockRepo.AssertExpectations(t)
}

func TestEngramCreationService_CreateEngram_InvalidTier(t *testing.T) {
	service, _, redisClient := setupTestEngramCreationService()
	defer redisClient.Close()

	ctx := context.Background()
	characterID := uuid.New()

	_, err := service.CreateEngram(ctx, characterID, 0, "friendly", nil, nil)

	assert.Error(t, err)
	assert.Equal(t, ErrInvalidChipTier, err)
}

func TestEngramCreationService_CreateEngram_RepositoryError(t *testing.T) {
	service, mockRepo, redisClient := setupTestEngramCreationService()
	defer redisClient.Close()

	ctx := context.Background()
	characterID := uuid.New()

	mockRepo.On("CreateCreationLog", ctx, mock.AnythingOfType("*server.EngramCreation")).Return(errors.New("database error"))

	result, err := service.CreateEngram(ctx, characterID, 3, "friendly", nil, nil)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestEngramCreationService_CreateEngram_AllTiers(t *testing.T) {
	service, mockRepo, redisClient := setupTestEngramCreationService()
	defer redisClient.Close()

	ctx := context.Background()
	characterID := uuid.New()

	for tier := 1; tier <= 5; tier++ {
		mockRepo.On("CreateCreationLog", ctx, mock.AnythingOfType("*server.EngramCreation")).Return(nil).Once()

		result, err := service.CreateEngram(ctx, characterID, tier, "neutral", nil, nil)

		assert.NoError(t, err)
		assert.NotNil(t, result)
		assert.True(t, result.Success)
	}
	mockRepo.AssertExpectations(t)
}

func TestEngramCreationService_CreateEngram_DataLossCalculation(t *testing.T) {
	service, mockRepo, redisClient := setupTestEngramCreationService()
	defer redisClient.Close()

	ctx := context.Background()
	characterID := uuid.New()

	mockRepo.On("CreateCreationLog", ctx, mock.AnythingOfType("*server.EngramCreation")).Return(nil)

	result, err := service.CreateEngram(ctx, characterID, 1, "neutral", nil, nil)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.NotNil(t, result.DataLossPercent)
	assert.GreaterOrEqual(t, *result.DataLossPercent, 5.0)
	mockRepo.AssertExpectations(t)
}
