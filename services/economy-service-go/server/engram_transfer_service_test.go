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

type mockEngramTransferRepository struct {
	mock.Mock
}

func (m *mockEngramTransferRepository) CreateTransfer(ctx context.Context, transfer *EngramTransfer) error {
	args := m.Called(ctx, transfer)
	return args.Error(0)
}

func (m *mockEngramTransferRepository) GetTransferByID(ctx context.Context, transferID uuid.UUID) (*EngramTransfer, error) {
	args := m.Called(ctx, transferID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*EngramTransfer), args.Error(1)
}

func (m *mockEngramTransferRepository) UpdateTransferStatus(ctx context.Context, transferID uuid.UUID, status string, transferredAt *time.Time) error {
	args := m.Called(ctx, transferID, status, transferredAt)
	return args.Error(0)
}

func (m *mockEngramTransferRepository) GetActiveLoans(ctx context.Context, characterID uuid.UUID) ([]*EngramTransfer, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]*EngramTransfer), args.Error(1)
}

func (m *mockEngramTransferRepository) GetPendingReturns(ctx context.Context) ([]*EngramTransfer, error) {
	args := m.Called(ctx)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]*EngramTransfer), args.Error(1)
}

type mockEngramCreationRepositoryForTransfer struct {
	mock.Mock
}

func (m *mockEngramCreationRepositoryForTransfer) CreateCreationLog(ctx context.Context, creation *EngramCreation) error {
	args := m.Called(ctx, creation)
	return args.Error(0)
}

func (m *mockEngramCreationRepositoryForTransfer) GetCreationLogByCreationID(ctx context.Context, creationID uuid.UUID) (*EngramCreation, error) {
	args := m.Called(ctx, creationID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*EngramCreation), args.Error(1)
}

func (m *mockEngramCreationRepositoryForTransfer) GetCreationLogByEngramID(ctx context.Context, engramID uuid.UUID) (*EngramCreation, error) {
	args := m.Called(ctx, engramID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*EngramCreation), args.Error(1)
}

func (m *mockEngramCreationRepositoryForTransfer) UpdateCreationStage(ctx context.Context, creationID uuid.UUID, stage string, dataLossPercent *float64, isComplete *bool) error {
	args := m.Called(ctx, creationID, stage, dataLossPercent, isComplete)
	return args.Error(0)
}

func (m *mockEngramCreationRepositoryForTransfer) CompleteCreation(ctx context.Context, creationID uuid.UUID, engramID uuid.UUID) error {
	args := m.Called(ctx, creationID, engramID)
	return args.Error(0)
}

func setupTestEngramTransferService() (*EngramTransferService, *mockEngramTransferRepository, *mockEngramCreationRepositoryForTransfer, *redis.Client) {
	mockRepo := new(mockEngramTransferRepository)
	mockCreationRepo := new(mockEngramCreationRepositoryForTransfer)
	redisClient := redis.NewClient(&redis.Options{
		Addr:         "localhost:6379",
		DB:           1,
		DialTimeout:  1 * time.Second,  // Fast timeout for tests
		ReadTimeout:  1 * time.Second,
		WriteTimeout: 1 * time.Second,
		PoolTimeout:  1 * time.Second,
	})

	service := NewEngramTransferService(mockRepo, mockCreationRepo, redisClient)

	return service, mockRepo, mockCreationRepo, redisClient
}

func TestEngramTransferService_TransferEngram_Success(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestEngramTransferService()
	defer redisClient.Close()

	ctx := context.Background()
	engramID := uuid.New()
	fromCharacterID := uuid.New()
	toCharacterID := uuid.New()
	transferType := "voluntary"
	isCopy := false

	mockRepo.On("CreateTransfer", ctx, mock.AnythingOfType("*server.EngramTransfer")).Return(nil)

	result, err := service.TransferEngram(ctx, engramID, fromCharacterID, toCharacterID, transferType, isCopy, nil, nil)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.Success)
	assert.NotEqual(t, uuid.Nil, result.TransferID)
	mockRepo.AssertExpectations(t)
}

func TestEngramTransferService_TransferEngram_WithCopy(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestEngramTransferService()
	defer redisClient.Close()

	ctx := context.Background()
	engramID := uuid.New()
	fromCharacterID := uuid.New()
	toCharacterID := uuid.New()
	transferType := "cooperative"
	isCopy := true
	newAttitudeType := stringPtr("friendly")
	transferPrice := float64Ptr(50000.0)

	mockRepo.On("CreateTransfer", ctx, mock.AnythingOfType("*server.EngramTransfer")).Return(nil)

	result, err := service.TransferEngram(ctx, engramID, fromCharacterID, toCharacterID, transferType, isCopy, newAttitudeType, transferPrice)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.Success)
	assert.NotNil(t, result.NewEngramID)
	mockRepo.AssertExpectations(t)
}

func TestEngramTransferService_TransferEngram_SameCharacter(t *testing.T) {
	service, _, _, redisClient := setupTestEngramTransferService()
	defer redisClient.Close()

	ctx := context.Background()
	engramID := uuid.New()
	characterID := uuid.New()

	_, err := service.TransferEngram(ctx, engramID, characterID, characterID, "voluntary", false, nil, nil)

	assert.Error(t, err)
	assert.Equal(t, ErrInvalidCharacter, err)
}

func TestEngramTransferService_TransferEngram_InvalidType(t *testing.T) {
	service, _, _, redisClient := setupTestEngramTransferService()
	defer redisClient.Close()

	ctx := context.Background()
	engramID := uuid.New()
	fromCharacterID := uuid.New()
	toCharacterID := uuid.New()

	_, err := service.TransferEngram(ctx, engramID, fromCharacterID, toCharacterID, "invalid", false, nil, nil)

	assert.Error(t, err)
	assert.Equal(t, ErrInvalidTransferType, err)
}

func TestEngramTransferService_TransferEngram_RepositoryError(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestEngramTransferService()
	defer redisClient.Close()

	ctx := context.Background()
	engramID := uuid.New()
	fromCharacterID := uuid.New()
	toCharacterID := uuid.New()

	mockRepo.On("CreateTransfer", ctx, mock.AnythingOfType("*server.EngramTransfer")).Return(errors.New("database error"))

	result, err := service.TransferEngram(ctx, engramID, fromCharacterID, toCharacterID, "voluntary", false, nil, nil)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestEngramTransferService_LoanEngram_Success(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestEngramTransferService()
	defer redisClient.Close()

	ctx := context.Background()
	engramID := uuid.New()
	fromCharacterID := uuid.New()
	toCharacterID := uuid.New()
	loanDurationDays := 30

	mockRepo.On("CreateTransfer", ctx, mock.AnythingOfType("*server.EngramTransfer")).Return(nil)

	result, err := service.LoanEngram(ctx, engramID, fromCharacterID, toCharacterID, loanDurationDays)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.Success)
	assert.NotEqual(t, uuid.Nil, result.LoanID)
	assert.NotEqual(t, uuid.Nil, result.TemporaryEngramID)
	assert.True(t, result.ReturnDate.After(time.Now()))
	mockRepo.AssertExpectations(t)
}

func TestEngramTransferService_LoanEngram_InvalidDuration(t *testing.T) {
	service, _, _, redisClient := setupTestEngramTransferService()
	defer redisClient.Close()

	ctx := context.Background()
	engramID := uuid.New()
	fromCharacterID := uuid.New()
	toCharacterID := uuid.New()

	_, err := service.LoanEngram(ctx, engramID, fromCharacterID, toCharacterID, 0)
	assert.Error(t, err)

	_, err = service.LoanEngram(ctx, engramID, fromCharacterID, toCharacterID, 366)
	assert.Error(t, err)
}

func TestEngramTransferService_ExtractEngram_Success(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestEngramTransferService()
	defer redisClient.Close()

	ctx := context.Background()
	engramID := uuid.New()
	extractorCharacterID := uuid.New()
	targetCharacterID := uuid.New()
	extractionMethod := "surgical"
	riskLevel := 50.0

	mockRepo.On("CreateTransfer", ctx, mock.AnythingOfType("*server.EngramTransfer")).Return(nil)

	result, err := service.ExtractEngram(ctx, engramID, extractorCharacterID, targetCharacterID, extractionMethod, riskLevel)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.Success)
	assert.NotEqual(t, uuid.Nil, result.ExtractionID)
	mockRepo.AssertExpectations(t)
}

func TestEngramTransferService_ExtractEngram_InvalidRiskLevel(t *testing.T) {
	service, _, _, redisClient := setupTestEngramTransferService()
	defer redisClient.Close()

	ctx := context.Background()
	engramID := uuid.New()
	extractorCharacterID := uuid.New()
	targetCharacterID := uuid.New()

	_, err := service.ExtractEngram(ctx, engramID, extractorCharacterID, targetCharacterID, "surgical", 10.0)
	assert.Error(t, err)

	_, err = service.ExtractEngram(ctx, engramID, extractorCharacterID, targetCharacterID, "surgical", 90.0)
	assert.Error(t, err)
}

func TestEngramTransferService_TradeEngram_Success(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestEngramTransferService()
	defer redisClient.Close()

	ctx := context.Background()
	engramID := uuid.New()
	fromCharacterID := uuid.New()
	tradeType := "sell"
	targetCharacterID := uuidPtr(uuid.New())
	price := float64Ptr(100000.0)

	mockRepo.On("CreateTransfer", ctx, mock.AnythingOfType("*server.EngramTransfer")).Return(nil)

	result, err := service.TradeEngram(ctx, engramID, fromCharacterID, tradeType, targetCharacterID, price, nil)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.Success)
	assert.NotEqual(t, uuid.Nil, result.TradeID)
	mockRepo.AssertExpectations(t)
}

func TestEngramTransferService_TradeEngram_InvalidType(t *testing.T) {
	service, _, _, redisClient := setupTestEngramTransferService()
	defer redisClient.Close()

	ctx := context.Background()
	engramID := uuid.New()
	fromCharacterID := uuid.New()

	_, err := service.TradeEngram(ctx, engramID, fromCharacterID, "invalid", nil, nil, nil)

	assert.Error(t, err)
	assert.Equal(t, ErrInvalidTransferType, err)
}


