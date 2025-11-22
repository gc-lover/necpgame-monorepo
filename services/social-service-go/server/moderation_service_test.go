package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockModerationRepository struct {
	mock.Mock
}

func (m *mockModerationRepository) CreateBan(ctx context.Context, ban *models.ChatBan) error {
	args := m.Called(ctx, ban)
	return args.Error(0)
}

func (m *mockModerationRepository) GetActiveBan(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID) (*models.ChatBan, error) {
	args := m.Called(ctx, characterID, channelID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ChatBan), args.Error(1)
}

func (m *mockModerationRepository) GetBans(ctx context.Context, characterID *uuid.UUID, limit, offset int) ([]models.ChatBan, int, error) {
	args := m.Called(ctx, characterID, limit, offset)
	return args.Get(0).([]models.ChatBan), args.Get(1).(int), args.Error(2)
}

func (m *mockModerationRepository) DeactivateBan(ctx context.Context, banID uuid.UUID) error {
	args := m.Called(ctx, banID)
	return args.Error(0)
}

func (m *mockModerationRepository) CreateReport(ctx context.Context, report *models.ChatReport) error {
	args := m.Called(ctx, report)
	return args.Error(0)
}

func (m *mockModerationRepository) GetReports(ctx context.Context, status *string, limit, offset int) ([]models.ChatReport, int, error) {
	args := m.Called(ctx, status, limit, offset)
	return args.Get(0).([]models.ChatReport), args.Get(1).(int), args.Error(2)
}

func (m *mockModerationRepository) UpdateReportStatus(ctx context.Context, reportID uuid.UUID, status string, adminID *uuid.UUID) error {
	args := m.Called(ctx, reportID, status, adminID)
	return args.Error(0)
}

type mockEventBus struct {
	mock.Mock
}

func (m *mockEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	args := m.Called(ctx, eventType, payload)
	return args.Error(0)
}

func setupTestModerationService(t *testing.T) (*ModerationService, *mockModerationRepository, *mockEventBus, *redis.Client, func()) {
	mockRepo := new(mockModerationRepository)
	mockEventBus := new(mockEventBus)
	redisClient := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		DB:   1,
	})
	redisClient.FlushDB(context.Background())

	service := &ModerationService{
		repo:     mockRepo,
		cache:    redisClient,
		logger:   GetLogger(),
		eventBus: mockEventBus,
		profanityWords: []string{"spam", "hack"},
		urlWhitelist:   []string{"necp.game"},
		autoBanEnabled: true,
		spamBanDuration: 1,
		severeViolationBanDuration: 24,
	}

	cleanup := func() {
		redisClient.Close()
	}

	return service, mockRepo, mockEventBus, redisClient, cleanup
}

func TestModerationService_AutoBanIfSpam_Success(t *testing.T) {
	service, mockRepo, mockEventBus, redisClient, cleanup := setupTestModerationService(t)
	defer cleanup()

	characterID := uuid.New()
	channelID := uuid.New()

	for i := 0; i < 11; i++ {
		key := "spam:character:" + characterID.String()
		redisClient.Incr(context.Background(), key)
	}
	redisClient.Expire(context.Background(), "spam:character:"+characterID.String(), 1*time.Minute)

	mockRepo.On("CreateBan", context.Background(), mock.AnythingOfType("*models.ChatBan")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "chat:ban:auto:spam", mock.Anything).Return(nil)

	ban, err := service.AutoBanIfSpam(context.Background(), characterID, &channelID)

	assert.NoError(t, err)
	assert.NotNil(t, ban)
	assert.Equal(t, characterID, ban.CharacterID)
	assert.Contains(t, ban.Reason, "spam detected")
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestModerationService_AutoBanIfSpam_NotEnoughMessages(t *testing.T) {
	service, _, _, redisClient, cleanup := setupTestModerationService(t)
	defer cleanup()

	characterID := uuid.New()
	channelID := uuid.New()

	for i := 0; i < 5; i++ {
		key := "spam:character:" + characterID.String()
		redisClient.Incr(context.Background(), key)
	}

	ban, err := service.AutoBanIfSpam(context.Background(), characterID, &channelID)

	assert.NoError(t, err)
	assert.Nil(t, ban)
}

func TestModerationService_AutoBanIfSevereViolation_Success(t *testing.T) {
	service, mockRepo, mockEventBus, _, cleanup := setupTestModerationService(t)
	defer cleanup()

	characterID := uuid.New()
	channelID := uuid.New()
	violationCount := 3

	mockRepo.On("CreateBan", context.Background(), mock.AnythingOfType("*models.ChatBan")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "chat:ban:auto:severe", mock.Anything).Return(nil)

	ban, err := service.AutoBanIfSevereViolation(context.Background(), characterID, &channelID, violationCount)

	assert.NoError(t, err)
	assert.NotNil(t, ban)
	assert.Equal(t, characterID, ban.CharacterID)
	assert.Contains(t, ban.Reason, "severe violations")
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestModerationService_AutoBanIfSevereViolation_NotEnoughViolations(t *testing.T) {
	service, _, _, _, cleanup := setupTestModerationService(t)
	defer cleanup()

	characterID := uuid.New()
	channelID := uuid.New()
	violationCount := 2

	ban, err := service.AutoBanIfSevereViolation(context.Background(), characterID, &channelID, violationCount)

	assert.NoError(t, err)
	assert.Nil(t, ban)
}

func TestModerationService_CreateBan_PublishesEvent(t *testing.T) {
	service, mockRepo, mockEventBus, _, cleanup := setupTestModerationService(t)
	defer cleanup()

	adminID := uuid.New()
	characterID := uuid.New()
	req := &models.CreateBanRequest{
		CharacterID: characterID,
		Reason:      "Test ban",
	}

	mockRepo.On("CreateBan", context.Background(), mock.AnythingOfType("*models.ChatBan")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "chat:ban:created", mock.Anything).Return(nil)

	ban, err := service.CreateBan(context.Background(), adminID, req)

	assert.NoError(t, err)
	assert.NotNil(t, ban)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestModerationService_RemoveBan_PublishesEvent(t *testing.T) {
	service, mockRepo, mockEventBus, _, cleanup := setupTestModerationService(t)
	defer cleanup()

	banID := uuid.New()

	mockRepo.On("DeactivateBan", context.Background(), banID).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "chat:ban:removed", mock.Anything).Return(nil)

	err := service.RemoveBan(context.Background(), banID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestModerationService_CreateReport_PublishesEvent(t *testing.T) {
	service, mockRepo, mockEventBus, _, cleanup := setupTestModerationService(t)
	defer cleanup()

	reporterID := uuid.New()
	req := &models.CreateReportRequest{
		ReportedID: uuid.New(),
		Reason:     "Test report",
	}

	mockRepo.On("CreateReport", context.Background(), mock.AnythingOfType("*models.ChatReport")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "chat:report:created", mock.Anything).Return(nil)

	report, err := service.CreateReport(context.Background(), reporterID, req)

	assert.NoError(t, err)
	assert.NotNil(t, report)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

