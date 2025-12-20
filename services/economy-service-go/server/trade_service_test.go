package server

import (
	"context"
	"encoding/json"
	"errors"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
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

func setupTestService() (*TradeService, *mockTradeRepository, *redis.Client) {
	mockRepo := new(mockTradeRepository)
	redisClient := redis.NewClient(&redis.Options{
		Addr:         "localhost:6379",
		DB:           1,
		DialTimeout:  1 * time.Second, // Fast timeout for tests
		ReadTimeout:  1 * time.Second,
		WriteTimeout: 1 * time.Second,
		PoolTimeout:  1 * time.Second,
	})

	service := &TradeService{
		repo:   mockRepo,
		cache:  redisClient,
		logger: GetLogger(),
	}

	return service, mockRepo, redisClient
}

func TestTradeService_CreateTrade_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	initiatorID := uuid.New()
	recipientID := uuid.New()
	zoneID := uuid.New()
	ctx := context.Background()

	req := &models.CreateTradeRequest{
		RecipientID: recipientID,
		ZoneID:      &zoneID,
	}

	mockRepo.On("GetActiveByCharacter", ctx, initiatorID).Return([]models.TradeSession{}, nil)
	mockRepo.On("GetActiveByCharacter", ctx, recipientID).Return([]models.TradeSession{}, nil)
	mockRepo.On("Create", ctx, mock.AnythingOfType("*models.TradeSession")).Return(nil)

	session, err := service.CreateTrade(ctx, initiatorID, req)

	assert.NoError(t, err)
	assert.NotNil(t, session)
	assert.Equal(t, initiatorID, session.InitiatorID)
	assert.Equal(t, recipientID, session.RecipientID)
	assert.Equal(t, models.TradeStatusPending, session.Status)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CreateTrade_InitiatorHasActiveTrade(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	req := &models.CreateTradeRequest{
		RecipientID: recipientID,
	}

	activeTrade := []models.TradeSession{
		{
			ID:          uuid.New(),
			InitiatorID: initiatorID,
			Status:      models.TradeStatusActive,
		},
	}

	mockRepo.On("GetActiveByCharacter", ctx, initiatorID).Return(activeTrade, nil)

	session, err := service.CreateTrade(ctx, initiatorID, req)

	assert.NoError(t, err)
	assert.Nil(t, session)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CreateTrade_RecipientHasActiveTrade(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	req := &models.CreateTradeRequest{
		RecipientID: recipientID,
	}

	mockRepo.On("GetActiveByCharacter", ctx, initiatorID).Return([]models.TradeSession{}, nil)

	activeTrade := []models.TradeSession{
		{
			ID:          uuid.New(),
			RecipientID: recipientID,
			Status:      models.TradeStatusActive,
		},
	}

	mockRepo.On("GetActiveByCharacter", ctx, recipientID).Return(activeTrade, nil)

	session, err := service.CreateTrade(ctx, initiatorID, req)

	assert.NoError(t, err)
	assert.Nil(t, session)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CreateTrade_RepositoryError(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	req := &models.CreateTradeRequest{
		RecipientID: recipientID,
	}

	mockRepo.On("GetActiveByCharacter", ctx, initiatorID).Return([]models.TradeSession{}, nil)
	mockRepo.On("GetActiveByCharacter", ctx, recipientID).Return([]models.TradeSession{}, nil)
	mockRepo.On("Create", ctx, mock.AnythingOfType("*models.TradeSession")).Return(errors.New("database error"))

	session, err := service.CreateTrade(ctx, initiatorID, req)

	assert.Error(t, err)
	assert.Nil(t, session)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_GetTrade_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	session := &models.TradeSession{
		ID:          sessionID,
		InitiatorID: initiatorID,
		RecipientID: recipientID,
		Status:      models.TradeStatusPending,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		ExpiresAt:   time.Now().Add(5 * time.Minute),
	}

	mockRepo.On("GetByID", ctx, sessionID).Return(session, nil)

	result, err := service.GetTrade(ctx, sessionID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, sessionID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_GetTrade_NotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	ctx := context.Background()

	mockRepo.On("GetByID", ctx, sessionID).Return(nil, nil)

	result, err := service.GetTrade(ctx, sessionID)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_GetActiveTrades_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	ctx := context.Background()

	sessions := []models.TradeSession{
		{
			ID:          uuid.New(),
			InitiatorID: characterID,
			Status:      models.TradeStatusActive,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
			ExpiresAt:   time.Now().Add(5 * time.Minute),
		},
	}

	// GetActiveTrades tries cache first, which may timeout if Redis unavailable
	// Use context with timeout to prevent hanging
	ctxWithTimeout, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	mockRepo.On("GetActiveByCharacter", ctxWithTimeout, characterID).Return(sessions, nil)

	result, err := service.GetActiveTrades(ctxWithTimeout, characterID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result, 1)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_GetActiveTrades_Cache(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	ctx := context.Background()

	sessions := []models.TradeSession{
		{
			ID:          uuid.New(),
			InitiatorID: characterID,
			Status:      models.TradeStatusActive,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
			ExpiresAt:   time.Now().Add(5 * time.Minute),
		},
	}

	sessionsJSON, _ := json.Marshal(sessions)
	cacheKey := "trades:active:" + characterID.String()
	// Use mock Redis or skip if Redis not available
	// Check Redis connection first with timeout
	pingCtx, pingCancel := context.WithTimeout(ctx, 1*time.Second)
	defer pingCancel()
	if err := redisClient.Ping(pingCtx).Err(); err != nil {
		// Redis not available - skip test
		t.Skipf("Skipping test due to Redis not available: %v", err)
		return
	}

	// Set cache with timeout
	setCtx, setCancel := context.WithTimeout(ctx, 1*time.Second)
	defer setCancel()
	err := redisClient.Set(setCtx, cacheKey, sessionsJSON, 30*time.Second).Err()
	if err != nil {
		// Redis error - skip test
		t.Skipf("Skipping test due to Redis error: %v", err)
		return
	}

	cachedResult, err := service.GetActiveTrades(ctx, characterID)

	assert.NoError(t, err)
	assert.NotNil(t, cachedResult)
	assert.Len(t, cachedResult, 1)
	// If Redis worked, repo should not be called; if Redis failed, repo was called
	mockRepo.AssertExpectations(t)
}

func TestTradeService_UpdateOffer_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	session := &models.TradeSession{
		ID:                 sessionID,
		InitiatorID:        initiatorID,
		RecipientID:        recipientID,
		Status:             models.TradeStatusPending,
		InitiatorOffer:     models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		RecipientOffer:     models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		InitiatorConfirmed: false,
		RecipientConfirmed: false,
		CreatedAt:          time.Now(),
		UpdatedAt:          time.Now(),
		ExpiresAt:          time.Now().Add(5 * time.Minute),
	}

	req := &models.UpdateTradeOfferRequest{
		Items:    []map[string]interface{}{{"item_id": "item_001", "quantity": 1}},
		Currency: map[string]int{"gold": 100},
	}

	mockRepo.On("GetByID", ctx, sessionID).Return(session, nil)
	mockRepo.On("Update", ctx, mock.AnythingOfType("*models.TradeSession")).Return(nil)

	result, err := service.UpdateOffer(ctx, sessionID, initiatorID, req)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, models.TradeStatusActive, result.Status)
	assert.Len(t, result.InitiatorOffer.Items, 1)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_UpdateOffer_Recipient(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	session := &models.TradeSession{
		ID:                 sessionID,
		InitiatorID:        initiatorID,
		RecipientID:        recipientID,
		Status:             models.TradeStatusPending,
		InitiatorOffer:     models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		RecipientOffer:     models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		InitiatorConfirmed: false,
		RecipientConfirmed: false,
		CreatedAt:          time.Now(),
		UpdatedAt:          time.Now(),
		ExpiresAt:          time.Now().Add(5 * time.Minute),
	}

	req := &models.UpdateTradeOfferRequest{
		Items:    []map[string]interface{}{{"item_id": "item_002", "quantity": 2}},
		Currency: map[string]int{"gold": 200},
	}

	mockRepo.On("GetByID", ctx, sessionID).Return(session, nil)
	mockRepo.On("Update", ctx, mock.AnythingOfType("*models.TradeSession")).Return(nil)

	result, err := service.UpdateOffer(ctx, sessionID, recipientID, req)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, models.TradeStatusActive, result.Status)
	assert.Len(t, result.RecipientOffer.Items, 1)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_UpdateOffer_NotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	req := &models.UpdateTradeOfferRequest{
		Items: []map[string]interface{}{{"item_id": "item_001", "quantity": 1}},
	}

	mockRepo.On("GetByID", ctx, sessionID).Return(nil, nil)

	result, err := service.UpdateOffer(ctx, sessionID, characterID, req)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_UpdateOffer_InvalidStatus(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	session := &models.TradeSession{
		ID:             sessionID,
		InitiatorID:    initiatorID,
		RecipientID:    recipientID,
		Status:         models.TradeStatusCompleted,
		InitiatorOffer: models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		RecipientOffer: models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		CreatedAt:      time.Now(),
		UpdatedAt:      time.Now(),
		ExpiresAt:      time.Now().Add(5 * time.Minute),
	}

	req := &models.UpdateTradeOfferRequest{
		Items: []map[string]interface{}{{"item_id": "item_001", "quantity": 1}},
	}

	mockRepo.On("GetByID", ctx, sessionID).Return(session, nil)

	result, err := service.UpdateOffer(ctx, sessionID, initiatorID, req)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_ConfirmTrade_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	session := &models.TradeSession{
		ID:                 sessionID,
		InitiatorID:        initiatorID,
		RecipientID:        recipientID,
		Status:             models.TradeStatusActive,
		InitiatorConfirmed: false,
		RecipientConfirmed: false,
		CreatedAt:          time.Now(),
		UpdatedAt:          time.Now(),
		ExpiresAt:          time.Now().Add(5 * time.Minute),
	}

	mockRepo.On("GetByID", ctx, sessionID).Return(session, nil)
	mockRepo.On("Update", ctx, mock.AnythingOfType("*models.TradeSession")).Return(nil)

	result, err := service.ConfirmTrade(ctx, sessionID, initiatorID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.InitiatorConfirmed)
	assert.False(t, result.RecipientConfirmed)
	assert.Equal(t, models.TradeStatusActive, result.Status)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_ConfirmTrade_BothConfirmed(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	session := &models.TradeSession{
		ID:                 sessionID,
		InitiatorID:        initiatorID,
		RecipientID:        recipientID,
		Status:             models.TradeStatusActive,
		InitiatorConfirmed: true,
		RecipientConfirmed: false,
		CreatedAt:          time.Now(),
		UpdatedAt:          time.Now(),
		ExpiresAt:          time.Now().Add(5 * time.Minute),
	}

	mockRepo.On("GetByID", ctx, sessionID).Return(session, nil)
	mockRepo.On("Update", ctx, mock.AnythingOfType("*models.TradeSession")).Return(nil)

	result, err := service.ConfirmTrade(ctx, sessionID, recipientID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.InitiatorConfirmed)
	assert.True(t, result.RecipientConfirmed)
	assert.Equal(t, models.TradeStatusConfirmed, result.Status)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_ConfirmTrade_NotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	mockRepo.On("GetByID", ctx, sessionID).Return(nil, nil)

	result, err := service.ConfirmTrade(ctx, sessionID, characterID)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CompleteTrade_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	session := &models.TradeSession{
		ID:                 sessionID,
		InitiatorID:        initiatorID,
		RecipientID:        recipientID,
		Status:             models.TradeStatusConfirmed,
		InitiatorOffer:     models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		RecipientOffer:     models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		InitiatorConfirmed: true,
		RecipientConfirmed: true,
		CreatedAt:          time.Now(),
		UpdatedAt:          time.Now(),
		ExpiresAt:          time.Now().Add(5 * time.Minute),
	}

	mockRepo.On("GetByID", ctx, sessionID).Return(session, nil)
	mockRepo.On("Update", ctx, mock.AnythingOfType("*models.TradeSession")).Return(nil)
	mockRepo.On("CreateHistory", ctx, mock.AnythingOfType("*models.TradeHistory")).Return(nil)

	err := service.CompleteTrade(ctx, sessionID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CompleteTrade_NotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	ctx := context.Background()

	mockRepo.On("GetByID", ctx, sessionID).Return(nil, nil)

	err := service.CompleteTrade(ctx, sessionID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CompleteTrade_InvalidStatus(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	session := &models.TradeSession{
		ID:          sessionID,
		InitiatorID: initiatorID,
		RecipientID: recipientID,
		Status:      models.TradeStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		ExpiresAt:   time.Now().Add(5 * time.Minute),
	}

	mockRepo.On("GetByID", ctx, sessionID).Return(session, nil)

	err := service.CompleteTrade(ctx, sessionID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CancelTrade_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	session := &models.TradeSession{
		ID:          sessionID,
		InitiatorID: initiatorID,
		RecipientID: recipientID,
		Status:      models.TradeStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		ExpiresAt:   time.Now().Add(5 * time.Minute),
	}

	mockRepo.On("GetByID", ctx, sessionID).Return(session, nil)
	mockRepo.On("UpdateStatus", ctx, sessionID, models.TradeStatusCancelled).Return(nil)

	err := service.CancelTrade(ctx, sessionID, initiatorID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CancelTrade_NotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	mockRepo.On("GetByID", ctx, sessionID).Return(nil, nil)

	err := service.CancelTrade(ctx, sessionID, characterID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_CancelTrade_AlreadyCompleted(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	sessionID := uuid.New()
	initiatorID := uuid.New()
	recipientID := uuid.New()
	ctx := context.Background()

	session := &models.TradeSession{
		ID:          sessionID,
		InitiatorID: initiatorID,
		RecipientID: recipientID,
		Status:      models.TradeStatusCompleted,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		ExpiresAt:   time.Now().Add(5 * time.Minute),
	}

	mockRepo.On("GetByID", ctx, sessionID).Return(session, nil)

	err := service.CancelTrade(ctx, sessionID, initiatorID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_GetTradeHistory_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	ctx := context.Background()

	history := []models.TradeHistory{
		{
			ID:          uuid.New(),
			InitiatorID: characterID,
			RecipientID: uuid.New(),
			Status:      models.TradeStatusCompleted,
			CreatedAt:   time.Now(),
			CompletedAt: time.Now(),
		},
	}

	mockRepo.On("GetHistoryByCharacter", ctx, characterID, 10, 0).Return(history, nil)
	mockRepo.On("CountHistoryByCharacter", ctx, characterID).Return(1, nil)

	response, err := service.GetTradeHistory(ctx, characterID, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 1, response.Total)
	assert.Len(t, response.History, 1)
	mockRepo.AssertExpectations(t)
}

func TestTradeService_GetTradeHistory_Cache(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	ctx := context.Background()

	history := []models.TradeHistory{
		{
			ID:          uuid.New(),
			InitiatorID: characterID,
			RecipientID: uuid.New(),
			Status:      models.TradeStatusCompleted,
			CreatedAt:   time.Now(),
			CompletedAt: time.Now(),
		},
	}

	response := &models.TradeHistoryListResponse{
		History: history,
		Total:   1,
	}

	responseJSON, _ := json.Marshal(response)
	cacheKey := "trade_history:" + characterID.String() + ":limit:10:offset:0"

	// Check Redis connection first with timeout
	pingCtx, pingCancel := context.WithTimeout(ctx, 1*time.Second)
	defer pingCancel()
	if err := redisClient.Ping(pingCtx).Err(); err != nil {
		// Redis not available - skip test
		t.Skipf("Skipping test due to Redis not available: %v", err)
		return
	}

	// Flush and set cache with timeout
	flushCtx, flushCancel := context.WithTimeout(ctx, 1*time.Second)
	defer flushCancel()
	redisClient.FlushDB(flushCtx)

	setCtx, setCancel := context.WithTimeout(ctx, 1*time.Second)
	defer setCancel()
	if err := redisClient.Set(setCtx, cacheKey, responseJSON, 5*time.Minute).Err(); err != nil {
		t.Skipf("Skipping test due to Redis error: %v", err)
		return
	}

	cachedResponse, err := service.GetTradeHistory(ctx, characterID, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, cachedResponse)
	assert.Equal(t, 1, cachedResponse.Total)
	mockRepo.AssertNotCalled(t, "GetHistoryByCharacter", ctx, characterID, 10, 0)
}

func TestTradeService_GetTradeHistory_RepositoryError(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	ctx := context.Background()

	mockRepo.On("GetHistoryByCharacter", ctx, characterID, 10, 0).Return(nil, errors.New("database error"))

	response, err := service.GetTradeHistory(ctx, characterID, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, response)
	mockRepo.AssertExpectations(t)
}
