// Issue: #140894066
package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/admin-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

func TestAdminService_BanPlayer_EventBusError(t *testing.T) {
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
	mockEventBus.On("PublishEvent", ctx, "admin:player-banned", mock.AnythingOfType("map[string]interface {}")).Return(errors.New("event bus error"))

	response, err := service.BanPlayer(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_KickPlayer_EventBusError(t *testing.T) {
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
	mockEventBus.On("PublishEvent", ctx, "admin:player-kicked", mock.AnythingOfType("map[string]interface {}")).Return(errors.New("event bus error"))

	response, err := service.KickPlayer(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_MutePlayer_EventBusError(t *testing.T) {
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
	mockEventBus.On("PublishEvent", ctx, "admin:player-muted", mock.AnythingOfType("map[string]interface {}")).Return(errors.New("event bus error"))

	response, err := service.MutePlayer(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_GiveItem_EventBusError(t *testing.T) {
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
	mockEventBus.On("PublishEvent", ctx, "admin:item-given", mock.AnythingOfType("map[string]interface {}")).Return(errors.New("event bus error"))

	response, err := service.GiveItem(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_RemoveItem_RepositoryError(t *testing.T) {
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

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(errors.New("database error"))

	response, err := service.RemoveItem(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.Error(t, err)
	assert.Nil(t, response)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_SetCurrency_EventBusError(t *testing.T) {
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
	mockEventBus.On("PublishEvent", ctx, "admin:currency-set", mock.AnythingOfType("map[string]interface {}")).Return(errors.New("event bus error"))

	response, err := service.SetCurrency(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_AddCurrency_EventBusError(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	req := &models.AddCurrencyRequest{
		CharacterID: characterID,
		CurrencyType: "gold",
		Amount:      500,
		Reason:      "Test add currency",
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:currency-added", mock.AnythingOfType("map[string]interface {}")).Return(errors.New("event bus error"))

	response, err := service.AddCurrency(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_SetWorldFlag_EventBusError(t *testing.T) {
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
	mockEventBus.On("PublishEvent", ctx, "admin:world-flag-set", mock.AnythingOfType("map[string]interface {}")).Return(errors.New("event bus error"))

	response, err := service.SetWorldFlag(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_CreateEvent_EventBusError(t *testing.T) {
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
	mockEventBus.On("PublishEvent", ctx, "admin:event-created", mock.AnythingOfType("map[string]interface {}")).Return(errors.New("event bus error"))

	response, err := service.CreateEvent(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_GetAuditLogs_CountError(t *testing.T) {
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
	mockRepo.On("CountAuditLogs", ctx, &adminID, (*models.AdminActionType)(nil)).Return(0, errors.New("database error"))

	response, err := service.GetAuditLogs(ctx, &adminID, nil, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, response)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_GetAuditLog_RepositoryError(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	logID := uuid.New()
	ctx := context.Background()

	mockRepo.On("GetAuditLog", ctx, logID).Return(nil, errors.New("database error"))

	result, err := service.GetAuditLog(ctx, logID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

