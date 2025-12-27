package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/announcement-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"go.uber.org/zap"
	"go.uber.org/zap/zaptest"
)

// MockRepository is a mock implementation of Repository
type MockRepository struct {
	mock.Mock
}

func (m *MockRepository) GetAnnouncements(ctx context.Context, announcementType, priority *string, limit, offset int) ([]*api.Announcement, int, error) {
	args := m.Called(ctx, announcementType, priority, limit, offset)
	return args.Get(0).([]*api.Announcement), args.Int(1), args.Error(2)
}

func (m *MockRepository) GetAnnouncement(ctx context.Context, id uuid.UUID) (*api.Announcement, error) {
	args := m.Called(ctx, id)
	return args.Get(0).(*api.Announcement), args.Error(1)
}

func (m *MockRepository) CreateAnnouncement(ctx context.Context, announcement *api.Announcement) error {
	args := m.Called(ctx, announcement)
	return args.Error(0)
}

func (m *MockRepository) UpdateAnnouncement(ctx context.Context, announcement *api.Announcement) error {
	args := m.Called(ctx, announcement)
	return args.Error(0)
}

func (m *MockRepository) DeleteAnnouncement(ctx context.Context, id uuid.UUID) error {
	args := m.Called(ctx, id)
	return args.Error(0)
}

// MockCache is a mock implementation of Cache
type MockCache struct {
	mock.Mock
}

func (m *MockCache) Set(ctx context.Context, key string, value interface{}, ttl time.Duration) {
	m.Called(ctx, key, value, ttl)
}

func (m *MockCache) Get(ctx context.Context, key string) (interface{}, bool) {
	args := m.Called(ctx, key)
	return args.Get(0), args.Bool(1)
}

func (m *MockCache) Delete(ctx context.Context, key string) {
	m.Called(ctx, key)
}

func (m *MockCache) GetAnnouncement(ctx context.Context, id string) (*api.Announcement, bool) {
	args := m.Called(ctx, id)
	return args.Get(0).(*api.Announcement), args.Bool(1)
}

func (m *MockCache) SetAnnouncement(ctx context.Context, id string, announcement *api.Announcement, ttl time.Duration) {
	m.Called(ctx, id, announcement, ttl)
}

func (m *MockCache) GetAnnouncementsList(ctx context.Context, key string) ([]*api.Announcement, bool) {
	args := m.Called(ctx, key)
	return args.Get(0).([]*api.Announcement), args.Bool(1)
}

func (m *MockCache) SetAnnouncementsList(ctx context.Context, key string, announcements []*api.Announcement, ttl time.Duration) {
	m.Called(ctx, key, announcements, ttl)
}

// MockValidator is a mock implementation of Validator
type MockValidator struct {
	mock.Mock
}

func (m *MockValidator) ValidateCreateAnnouncementRequest(ctx context.Context, req *api.CreateAnnouncementRequest) error {
	args := m.Called(ctx, req)
	return args.Error(0)
}

func (m *MockValidator) ValidateUpdateAnnouncementRequest(ctx context.Context, req *api.UpdateAnnouncementRequest) error {
	args := m.Called(ctx, req)
	return args.Error(0)
}

func (m *MockValidator) ValidateUUID(ctx context.Context, id string) error {
	args := m.Called(ctx, id)
	return args.Error(0)
}

// MockMetrics is a mock implementation of Metrics
type MockMetrics struct {
	mock.Mock
}

func (m *MockMetrics) RecordRequest(operation string) {
	m.Called(operation)
}

func (m *MockMetrics) RecordError(operation string) {
	m.Called(operation)
}

func (m *MockMetrics) GetMetrics() api.MetricsResponse {
	args := m.Called()
	return args.Get(0).(api.MetricsResponse)
}

func TestAnnouncementService_GetAnnouncement(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := new(MockRepository)
	mockCache := new(MockCache)
	mockValidator := new(MockValidator)
	mockMetrics := new(MockMetrics)

	service := &AnnouncementService{
		repo:      mockRepo,
		cache:     mockCache,
		validator: mockValidator,
		metrics:   mockMetrics,
		logger:    logger,
	}

	ctx := context.Background()
	id := uuid.New()
	expectedAnnouncement := &api.Announcement{
		ID:               api.NewOptUUID(id),
		Title:            "Test Announcement",
		Content:          "Test content",
		AnnouncementType: "GAME_NEWS",
		Priority:         api.NewOptAnnouncementPriority(api.AnnouncementPriorityNORMAL),
		DisplayStyle:     api.NewOptAnnouncementDisplayStyle(api.AnnouncementDisplayStyleNEWS_FEED),
		Status:           api.NewOptAnnouncementStatus(api.AnnouncementStatusDRAFT),
		CreatedAt:        api.NewOptDateTime(time.Now()),
		UpdatedAt:        api.NewOptDateTime(time.Now()),
	}

	mockMetrics.On("RecordRequest", "GetAnnouncement").Return()
	mockCache.On("Get", ctx, "announcement:"+id.String()).Return(nil, false)
	mockRepo.On("GetAnnouncement", ctx, id).Return(expectedAnnouncement, nil)

	announcement, err := service.GetAnnouncement(ctx, id)

	assert.NoError(t, err)
	assert.NotNil(t, announcement)
	assert.Equal(t, expectedAnnouncement.Title, announcement.Title)
	mockMetrics.AssertCalled(t, "GetAnnouncement")
	mockRepo.AssertCalled(t, "GetAnnouncement", ctx, id)
}

func TestAnnouncementService_CreateAnnouncement(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := new(MockRepository)
	mockCache := new(MockCache)
	mockValidator := new(MockValidator)
	mockMetrics := new(MockMetrics)

	service := &AnnouncementService{
		repo:      mockRepo,
		cache:     mockCache,
		validator: mockValidator,
		metrics:   mockMetrics,
		logger:    logger,
	}

	ctx := context.Background()
	req := &api.CreateAnnouncementRequest{
		Title:            "New Announcement",
		Content:          "Announcement content",
		AnnouncementType: "GAME_NEWS",
		Priority:         api.NewOptAnnouncementPriority(api.AnnouncementPriorityNORMAL),
		DisplayStyle:     api.NewOptAnnouncementDisplayStyle(api.AnnouncementDisplayStyleNEWS_FEED),
	}

	mockMetrics.On("RecordRequest", "CreateAnnouncement").Return()
	mockValidator.On("ValidateCreateAnnouncementRequest", ctx, req).Return(nil)
	mockRepo.On("CreateAnnouncement", ctx, mock.AnythingOfType("*api.Announcement")).Return(nil)

	announcement, err := service.CreateAnnouncement(ctx, req)

	assert.NoError(t, err)
	assert.NotNil(t, announcement)
	assert.Equal(t, req.Title, announcement.Title)
	mockMetrics.AssertCalled(t, "RecordRequest", "CreateAnnouncement")
	mockValidator.AssertCalled(t, "ValidateCreateAnnouncementRequest", ctx, req)
	mockRepo.AssertCalled(t, "CreateAnnouncement", ctx, mock.AnythingOfType("*api.Announcement"))
}

func TestAnnouncementService_CreateAnnouncement_ValidationError(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := new(MockRepository)
	mockCache := new(MockCache)
	mockValidator := new(MockValidator)
	mockMetrics := new(MockMetrics)

	service := &AnnouncementService{
		repo:      mockRepo,
		cache:     mockCache,
		validator: mockValidator,
		metrics:   mockMetrics,
		logger:    logger,
	}

	ctx := context.Background()
	req := &api.CreateAnnouncementRequest{
		Title:            "", // Empty title should cause validation error
		Content:          "Announcement content",
		AnnouncementType: "GAME_NEWS",
	}

	mockMetrics.On("RecordRequest", "CreateAnnouncement").Return()
	mockValidator.On("ValidateCreateAnnouncementRequest", ctx, req).Return(errors.New("title cannot be empty"))
	mockMetrics.On("RecordError", "CreateAnnouncement").Return()

	_, err := service.CreateAnnouncement(ctx, req)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "validation failed")
	mockMetrics.AssertCalled(t, "RecordRequest", "CreateAnnouncement")
	mockMetrics.AssertCalled(t, "RecordError", "CreateAnnouncement")
	mockValidator.AssertCalled(t, "ValidateCreateAnnouncementRequest", ctx, req)
	mockRepo.AssertNotCalled(t, "CreateAnnouncement")
}



