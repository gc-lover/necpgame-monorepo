package server

import (
	"context"
	"encoding/json"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/economy-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

type mockTradeRepository struct {
	mock.Mock
}

func (m *mockTradeRepository) Create(ctx context.Context, session *models.TradeSession) error {
	args := m.Called(ctx, session)
	return args.Error(0)
}

func (m *mockTradeRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.TradeSession, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.TradeSession), args.Error(1)
}

func (m *mockTradeRepository) GetActiveByCharacter(ctx context.Context, characterID uuid.UUID) ([]models.TradeSession, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.TradeSession), args.Error(1)
}

func (m *mockTradeRepository) Update(ctx context.Context, session *models.TradeSession) error {
	args := m.Called(ctx, session)
	return args.Error(0)
}

func (m *mockTradeRepository) UpdateStatus(ctx context.Context, id uuid.UUID, status models.TradeStatus) error {
	args := m.Called(ctx, id, status)
	return args.Error(0)
}

func (m *mockTradeRepository) CreateHistory(ctx context.Context, history *models.TradeHistory) error {
	args := m.Called(ctx, history)
	return args.Error(0)
}

func (m *mockTradeRepository) GetHistoryByCharacter(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.TradeHistory, error) {
	args := m.Called(ctx, characterID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.TradeHistory), args.Error(1)
}

func (m *mockTradeRepository) CountHistoryByCharacter(ctx context.Context, characterID uuid.UUID) (int, error) {
	args := m.Called(ctx, characterID)
	return args.Int(0), args.Error(1)
}

func (m *mockTradeRepository) CleanupExpired(ctx context.Context) error {
	args := m.Called(ctx)
	return args.Error(0)
}

func setupTestService(t *testing.T) (*TradeService, *mockTradeRepository, func()) {
	redisOpts, err := redis.ParseURL("redis://localhost:6379")
	if err != nil {
		t.Skipf("Skipping test due to Redis connection: %v", err)
		return nil, nil, nil
	}
	redisClient := redis.NewClient(redisOpts)

	mockRepo := new(mockTradeRepository)
	service := &TradeService{
		repo:   mockRepo,
		cache:  redisClient,
		logger: GetLogger(),
	}

	cleanup := func() {
		redisClient.Close()
	}

	return service, mockRepo, cleanup
}

func TestTradeService_CreateTrade_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	initiatorID := uuid.New()
	recipientID := uuid.New()
	req := &models.CreateTradeRequest{
		RecipientID: recipientID,
	}

	mockRepo.On("GetActiveByCharacter", mock.Anything, initiatorID).Return([]models.TradeSession{}, nil)
	mockRepo.On("GetActiveByCharacter", mock.Anything, recipientID).Return([]models.TradeSession{}, nil)
	mockRepo.On("Create", mock.Anything, mock.AnythingOfType("*models.TradeSession")).Return(nil)

	ctx := context.Background()
	session, err := service.CreateTrade(ctx, initiatorID, req)

	require.NoError(t, err)
	assert.NotNil(t, session)
	assert.Equal(t, initiatorID, session.InitiatorID)
	assert.Equal(t, recipientID, session.RecipientID)
	assert.Equal(t, models.TradeStatusPending, session.Status)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CreateTrade_ActiveTradeExists(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	initiatorID := uuid.New()
	recipientID := uuid.New()
	req := &models.CreateTradeRequest{
		RecipientID: recipientID,
	}

	activeSession := models.TradeSession{
		ID:     uuid.New(),
		Status: models.TradeStatusActive,
	}

	mockRepo.On("GetActiveByCharacter", mock.Anything, initiatorID).Return([]models.TradeSession{activeSession}, nil)

	ctx := context.Background()
	session, err := service.CreateTrade(ctx, initiatorID, req)

	require.NoError(t, err)
	assert.Nil(t, session)
	mockRepo.AssertNotCalled(t, "Create", mock.Anything, mock.Anything)
}

func TestTradeService_GetTrade_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	tradeID := uuid.New()
	session := &models.TradeSession{
		ID:     tradeID,
		Status: models.TradeStatusPending,
	}

	mockRepo.On("GetByID", mock.Anything, tradeID).Return(session, nil)

	ctx := context.Background()
	result, err := service.GetTrade(ctx, tradeID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, tradeID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_GetActiveTrades_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	sessions := []models.TradeSession{
		{
			ID:     uuid.New(),
			Status: models.TradeStatusActive,
		},
	}

	mockRepo.On("GetActiveByCharacter", mock.Anything, characterID).Return(sessions, nil)

	ctx := context.Background()
	result, err := service.GetActiveTrades(ctx, characterID)

	require.NoError(t, err)
	assert.Len(t, result, 1)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_GetActiveTrades_Cache(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	sessions := []models.TradeSession{
		{
			ID:     uuid.New(),
			Status: models.TradeStatusActive,
		},
	}

	sessionsJSON, _ := json.Marshal(sessions)

	ctx := context.Background()
	service.cache.Set(ctx, "trades:active:"+characterID.String(), sessionsJSON, 30*time.Second)

	cached, err := service.GetActiveTrades(ctx, characterID)

	require.NoError(t, err)
	assert.Len(t, cached, 1)
	mockRepo.AssertNotCalled(t, "GetActiveByCharacter", mock.Anything, characterID)
}

func TestTradeService_UpdateOffer_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	sessionID := uuid.New()
	characterID := uuid.New()
	session := &models.TradeSession{
		ID:              sessionID,
		InitiatorID:     characterID,
		RecipientID:     uuid.New(),
		Status:          models.TradeStatusPending,
		InitiatorOffer:  models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		RecipientOffer:  models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
	}

	req := &models.UpdateTradeOfferRequest{
		Items: []map[string]interface{}{{"item_id": "test_item"}},
	}

	mockRepo.On("GetByID", mock.Anything, sessionID).Return(session, nil)
	mockRepo.On("Update", mock.Anything, mock.AnythingOfType("*models.TradeSession")).Return(nil)

	ctx := context.Background()
	updated, err := service.UpdateOffer(ctx, sessionID, characterID, req)

	require.NoError(t, err)
	assert.NotNil(t, updated)
	assert.Equal(t, models.TradeStatusActive, updated.Status)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_ConfirmTrade_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	sessionID := uuid.New()
	characterID := uuid.New()
	session := &models.TradeSession{
		ID:                sessionID,
		InitiatorID:       characterID,
		RecipientID:       uuid.New(),
		Status:            models.TradeStatusActive,
		InitiatorConfirmed: false,
		RecipientConfirmed: false,
		InitiatorOffer:    models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		RecipientOffer:    models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
	}

	mockRepo.On("GetByID", mock.Anything, sessionID).Return(session, nil)
	mockRepo.On("Update", mock.Anything, mock.AnythingOfType("*models.TradeSession")).Return(nil)

	ctx := context.Background()
	confirmed, err := service.ConfirmTrade(ctx, sessionID, characterID)

	require.NoError(t, err)
	assert.NotNil(t, confirmed)
	assert.True(t, confirmed.InitiatorConfirmed)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CompleteTrade_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	sessionID := uuid.New()
	session := &models.TradeSession{
		ID:                sessionID,
		InitiatorID:       uuid.New(),
		RecipientID:       uuid.New(),
		Status:            models.TradeStatusConfirmed,
		InitiatorConfirmed: true,
		RecipientConfirmed: true,
		InitiatorOffer:    models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		RecipientOffer:    models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
	}

	mockRepo.On("GetByID", mock.Anything, sessionID).Return(session, nil)
	mockRepo.On("Update", mock.Anything, mock.AnythingOfType("*models.TradeSession")).Return(nil)
	mockRepo.On("CreateHistory", mock.Anything, mock.AnythingOfType("*models.TradeHistory")).Return(nil)

	ctx := context.Background()
	err := service.CompleteTrade(ctx, sessionID)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CancelTrade_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	sessionID := uuid.New()
	characterID := uuid.New()
	session := &models.TradeSession{
		ID:     sessionID,
		InitiatorID: characterID,
		RecipientID: uuid.New(),
		Status: models.TradeStatusActive,
	}

	mockRepo.On("GetByID", mock.Anything, sessionID).Return(session, nil)
	mockRepo.On("UpdateStatus", mock.Anything, sessionID, models.TradeStatusCancelled).Return(nil)

	ctx := context.Background()
	err := service.CancelTrade(ctx, sessionID, characterID)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_GetTradeHistory_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	history := []models.TradeHistory{
		{
			ID:     uuid.New(),
			Status: models.TradeStatusCompleted,
		},
	}

	mockRepo.On("GetHistoryByCharacter", mock.Anything, characterID, 10, 0).Return(history, nil)
	mockRepo.On("CountHistoryByCharacter", mock.Anything, characterID).Return(1, nil)

	ctx := context.Background()
	result, err := service.GetTradeHistory(ctx, characterID, 10, 0)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.History, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CreateTrade_DatabaseError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	initiatorID := uuid.New()
	recipientID := uuid.New()
	req := &models.CreateTradeRequest{
		RecipientID: recipientID,
	}
	expectedErr := errors.New("database error")

	mockRepo.On("GetActiveByCharacter", mock.Anything, initiatorID).Return(nil, expectedErr)

	ctx := context.Background()
	session, err := service.CreateTrade(ctx, initiatorID, req)

	assert.Error(t, err)
	assert.Equal(t, expectedErr, err)
	assert.Nil(t, session)
	mockRepo.AssertExpectations(t)
}

