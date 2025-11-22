package server

import (
	"context"
	"errors"
	"net/http"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/admin-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockAdminRepository struct {
	mock.Mock
}

func (m *mockAdminRepository) CreateAuditLog(ctx context.Context, log *models.AdminAuditLog) error {
	args := m.Called(ctx, log)
	return args.Error(0)
}

func (m *mockAdminRepository) GetAuditLog(ctx context.Context, logID uuid.UUID) (*models.AdminAuditLog, error) {
	args := m.Called(ctx, logID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.AdminAuditLog), args.Error(1)
}

func (m *mockAdminRepository) ListAuditLogs(ctx context.Context, adminID *uuid.UUID, actionType *models.AdminActionType, limit, offset int) ([]models.AdminAuditLog, error) {
	args := m.Called(ctx, adminID, actionType, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.AdminAuditLog), args.Error(1)
}

func (m *mockAdminRepository) CountAuditLogs(ctx context.Context, adminID *uuid.UUID, actionType *models.AdminActionType) (int, error) {
	args := m.Called(ctx, adminID, actionType)
	return args.Int(0), args.Error(1)
}

type mockEventBus struct {
	mock.Mock
}

func (m *mockEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	args := m.Called(ctx, eventType, payload)
	return args.Error(0)
}

func setupTestService() (*AdminService, *mockAdminRepository, *mockEventBus, *redis.Client) {
	mockRepo := new(mockAdminRepository)
	mockEventBus := new(mockEventBus)
	redisClient := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		DB:   1,
	})

	service := &AdminService{
		repo:       mockRepo,
		cache:      redisClient,
		logger:     GetLogger(),
		eventBus:   mockEventBus,
		httpClient: &http.Client{Timeout: 10 * time.Second},
	}

	return service, mockRepo, mockEventBus, redisClient
}

func TestAdminService_LogAction_Success(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)

	err := service.LogAction(ctx, adminID, models.AdminActionTypeBan, &characterID, "character", map[string]interface{}{"reason": "test"}, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_LogAction_RepositoryError(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(errors.New("database error"))

	err := service.LogAction(ctx, adminID, models.AdminActionTypeBan, &characterID, "character", map[string]interface{}{}, "127.0.0.1", "test-agent")

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_BanPlayer_Success(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	req := &models.BanPlayerRequest{
		CharacterID: characterID,
		Reason:      "Test ban",
		Permanent:   false,
		Duration:    int64Ptr(3600),
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:player-banned", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	response, err := service.BanPlayer(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "Player banned successfully", response.Message)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_BanPlayer_Permanent(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	req := &models.BanPlayerRequest{
		CharacterID: characterID,
		Reason:      "Test permanent ban",
		Permanent:   true,
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:player-banned", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	response, err := service.BanPlayer(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_BanPlayer_RepositoryError(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	req := &models.BanPlayerRequest{
		CharacterID: characterID,
		Reason:      "Test ban",
		Permanent:   false,
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(errors.New("database error"))

	response, err := service.BanPlayer(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.Error(t, err)
	assert.Nil(t, response)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_KickPlayer_Success(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	req := &models.KickPlayerRequest{
		CharacterID: characterID,
		Reason:      "Test kick",
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:player-kicked", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	response, err := service.KickPlayer(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "Player kicked successfully", response.Message)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_MutePlayer_Success(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	req := &models.MutePlayerRequest{
		CharacterID: characterID,
		Reason:      "Test mute",
		Duration:    3600,
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:player-muted", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	response, err := service.MutePlayer(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "Player muted successfully", response.Message)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_GiveItem_Success(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	req := &models.GiveItemRequest{
		CharacterID: characterID,
		ItemID:      "item_001",
		Quantity:    10,
		Reason:      "Test give item",
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:item-given", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	response, err := service.GiveItem(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "Item given successfully", response.Message)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_RemoveItem_Success(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	req := &models.RemoveItemRequest{
		CharacterID: characterID,
		ItemID:      "item_001",
		Quantity:    5,
		Reason:      "Test remove item",
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)

	response, err := service.RemoveItem(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "Item removed successfully", response.Message)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_SetCurrency_Success(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	req := &models.SetCurrencyRequest{
		CharacterID: characterID,
		CurrencyType: "gold",
		Amount:      1000,
		Reason:      "Test set currency",
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:currency-set", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	response, err := service.SetCurrency(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "Currency set successfully", response.Message)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_AddCurrency_Success(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.UUID{}
	ctx := context.Background()

	req := &models.AddCurrencyRequest{
		CharacterID: characterID,
		CurrencyType: "gold",
		Amount:      500,
		Reason:      "Test add currency",
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:currency-added", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	response, err := service.AddCurrency(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "Currency added successfully", response.Message)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_SetWorldFlag_Success(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	ctx := context.Background()

	region := "us-east"
	req := &models.SetWorldFlagRequest{
		FlagName:  "test_flag",
		FlagValue: map[string]interface{}{"value": "test"},
		Region:    &region,
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:world-flag-set", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	response, err := service.SetWorldFlag(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "World flag set successfully", response.Message)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_CreateEvent_Success(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	ctx := context.Background()

	endTime := time.Now().Add(24 * time.Hour)
	req := &models.CreateEventRequest{
		EventName:    "Test Event",
		EventType:    "seasonal",
		Description:  "Test description",
		StartTime:    time.Now(),
		EndTime:      &endTime,
		Settings:     map[string]interface{}{"difficulty": "normal"},
		Announcement: true,
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:event-created", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	response, err := service.CreateEvent(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "Event created successfully", response.Message)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_SearchPlayers_Success(t *testing.T) {
	service, _, _, redisClient := setupTestService()
	defer redisClient.Close()

	ctx := context.Background()
	req := &models.SearchPlayersRequest{
		Query:    "test",
		SearchBy: "name",
		Limit:    10,
		Offset:   0,
	}

	response, err := service.SearchPlayers(ctx, req)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 0, response.Total)
	assert.Empty(t, response.Players)
}

func TestAdminService_GetAnalytics_Success(t *testing.T) {
	service, _, _, redisClient := setupTestService()
	defer redisClient.Close()

	ctx := context.Background()

	response, err := service.GetAnalytics(ctx)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 0, response.OnlinePlayers)
	assert.NotNil(t, response.EconomyMetrics)
	assert.NotNil(t, response.CombatMetrics)
	assert.NotNil(t, response.PerformanceMetrics)
}

func TestAdminService_GetAuditLogs_Success(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	ctx := context.Background()

	logs := []models.AdminAuditLog{
		{
			ID:         uuid.New(),
			AdminID:    adminID,
			ActionType: models.AdminActionTypeBan,
			Details:    map[string]interface{}{"reason": "test"},
			CreatedAt:  time.Now(),
		},
	}

	mockRepo.On("ListAuditLogs", ctx, &adminID, (*models.AdminActionType)(nil), 10, 0).Return(logs, nil)
	mockRepo.On("CountAuditLogs", ctx, &adminID, (*models.AdminActionType)(nil)).Return(1, nil)

	response, err := service.GetAuditLogs(ctx, &adminID, nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 1, response.Total)
	assert.Len(t, response.Logs, 1)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_GetAuditLogs_WithActionType(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	actionType := models.AdminActionTypeBan
	ctx := context.Background()

	logs := []models.AdminAuditLog{}

	mockRepo.On("ListAuditLogs", ctx, &adminID, &actionType, 10, 0).Return(logs, nil)
	mockRepo.On("CountAuditLogs", ctx, &adminID, &actionType).Return(0, nil)

	response, err := service.GetAuditLogs(ctx, &adminID, &actionType, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 0, response.Total)
	assert.Empty(t, response.Logs)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_GetAuditLog_Success(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	logID := uuid.New()
	ctx := context.Background()

	log := &models.AdminAuditLog{
		ID:         logID,
		AdminID:    uuid.New(),
		ActionType: models.AdminActionTypeBan,
		Details:    map[string]interface{}{"reason": "test"},
		CreatedAt:  time.Now(),
	}

	mockRepo.On("GetAuditLog", ctx, logID).Return(log, nil)

	result, err := service.GetAuditLog(ctx, logID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, logID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_GetAuditLog_NotFound(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	logID := uuid.New()
	ctx := context.Background()

	mockRepo.On("GetAuditLog", ctx, logID).Return(nil, nil)

	result, err := service.GetAuditLog(ctx, logID)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_GetAuditLogs_RepositoryError(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	ctx := context.Background()

	mockRepo.On("ListAuditLogs", ctx, &adminID, (*models.AdminActionType)(nil), 10, 0).Return(nil, errors.New("database error"))

	response, err := service.GetAuditLogs(ctx, &adminID, nil, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, response)
	mockRepo.AssertExpectations(t)
}


