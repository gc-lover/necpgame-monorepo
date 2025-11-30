// Issue: #140894066
package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/admin-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

func TestAdminService_SetWorldFlag_WithoutRegion(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	ctx := context.Background()

	req := &models.SetWorldFlagRequest{
		FlagName:  "test_flag",
		FlagValue: map[string]interface{}{"value": "test"},
		Region:    nil,
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:world-flag-set", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	response, err := service.SetWorldFlag(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_CreateEvent_WithoutEndTime(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	ctx := context.Background()

	req := &models.CreateEventRequest{
		EventName:    "Test Event",
		EventType:    "seasonal",
		Description:  "Test description",
		StartTime:    time.Now(),
		EndTime:      nil,
		Settings:     map[string]interface{}{"difficulty": "normal"},
		Announcement: false,
	}

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)
	mockEventBus.On("PublishEvent", ctx, "admin:event-created", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	response, err := service.CreateEvent(ctx, adminID, req, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestAdminService_BanPlayer_WithoutDuration(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	req := &models.BanPlayerRequest{
		CharacterID: characterID,
		Reason:      "Test ban",
		Permanent:   false,
		Duration:    nil,
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

func TestAdminService_GetAuditLogs_WithoutFilters(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	ctx := context.Background()

	logs := []models.AdminAuditLog{
		{
			ID:         uuid.New(),
			AdminID:    uuid.New(),
			ActionType: models.AdminActionTypeBan,
			Details:    map[string]interface{}{"reason": "test"},
			CreatedAt:  time.Now(),
		},
	}

	mockRepo.On("ListAuditLogs", ctx, (*uuid.UUID)(nil), (*models.AdminActionType)(nil), 10, 0).Return(logs, nil)
	mockRepo.On("CountAuditLogs", ctx, (*uuid.UUID)(nil), (*models.AdminActionType)(nil)).Return(1, nil)

	response, err := service.GetAuditLogs(ctx, nil, nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 1, response.Total)
	assert.Len(t, response.Logs, 1)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_GetAuditLogs_WithPagination(t *testing.T) {
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

	mockRepo.On("ListAuditLogs", ctx, &adminID, (*models.AdminActionType)(nil), 5, 10).Return(logs, nil)
	mockRepo.On("CountAuditLogs", ctx, &adminID, (*models.AdminActionType)(nil)).Return(15, nil)

	response, err := service.GetAuditLogs(ctx, &adminID, nil, 5, 10)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 15, response.Total)
	assert.Len(t, response.Logs, 1)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_LogAction_WithNilTargetID(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	ctx := context.Background()

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)

	err := service.LogAction(ctx, adminID, models.AdminActionTypeSetWorldFlag, nil, "world", map[string]interface{}{"flag": "test"}, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_LogAction_WithEmptyDetails(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	characterID := uuid.New()
	ctx := context.Background()

	mockRepo.On("CreateAuditLog", ctx, mock.AnythingOfType("*models.AdminAuditLog")).Return(nil)

	err := service.LogAction(ctx, adminID, models.AdminActionTypeBan, &characterID, "character", map[string]interface{}{}, "127.0.0.1", "test-agent")

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAdminService_GetAuditLogs_EmptyResult(t *testing.T) {
	service, mockRepo, _, redisClient := setupTestService()
	defer redisClient.Close()

	adminID := uuid.New()
	ctx := context.Background()

	logs := []models.AdminAuditLog{}

	mockRepo.On("ListAuditLogs", ctx, &adminID, (*models.AdminActionType)(nil), 10, 0).Return(logs, nil)
	mockRepo.On("CountAuditLogs", ctx, &adminID, (*models.AdminActionType)(nil)).Return(0, nil)

	response, err := service.GetAuditLogs(ctx, &adminID, nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, 0, response.Total)
	assert.Empty(t, response.Logs)
	mockRepo.AssertExpectations(t)
}

