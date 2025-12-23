// Unit tests for MaintenanceWindowsService
// Issue: #316
// PERFORMANCE: Tests run without external dependencies

package server

import (
	"context"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/maintenance-windows-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

// MockRepository is a mock implementation for testing
type MockRepository struct {
	mock.Mock
}

func (m *MockRepository) CreateMaintenanceWindow(ctx context.Context, window *api.MaintenanceWindow) error {
	args := m.Called(ctx, window)
	return args.Error(0)
}

func (m *MockRepository) GetMaintenanceWindows(ctx context.Context, filter *MaintenanceWindowFilter, limit, offset int) ([]*api.MaintenanceWindow, int, error) {
	args := m.Called(ctx, filter, limit, offset)
	return args.Get(0).([]*api.MaintenanceWindow), args.Int(1), args.Error(2)
}

func (m *MockRepository) GetMaintenanceWindow(ctx context.Context, windowID uuid.UUID) (*api.MaintenanceWindow, error) {
	args := m.Called(ctx, windowID)
	return args.Get(0).(*api.MaintenanceWindow), args.Error(1)
}

func (m *MockRepository) UpdateMaintenanceWindow(ctx context.Context, window *api.MaintenanceWindow) error {
	args := m.Called(ctx, window)
	return args.Error(0)
}

func (m *MockRepository) DeleteMaintenanceWindow(ctx context.Context, windowID uuid.UUID) error {
	args := m.Called(ctx, windowID)
	return args.Error(0)
}

// MockCache is a mock implementation for testing
type MockCache struct {
	mock.Mock
}

func (m *MockCache) GetMaintenanceWindow(ctx context.Context, windowID uuid.UUID) (*api.MaintenanceWindow, bool) {
	args := m.Called(ctx, windowID)
	return args.Get(0).(*api.MaintenanceWindow), args.Bool(1)
}

func (m *MockCache) SetMaintenanceWindow(ctx context.Context, windowID uuid.UUID, window *api.MaintenanceWindow) {
	m.Called(ctx, windowID, window)
}

func (m *MockCache) DeleteMaintenanceWindow(ctx context.Context, windowID uuid.UUID) {
	m.Called(ctx, windowID)
}

func (m *MockCache) GetMaintenanceWindows(ctx context.Context, limit int) ([]*api.MaintenanceWindow, int, bool) {
	args := m.Called(ctx, limit)
	return args.Get(0).([]*api.MaintenanceWindow), args.Int(1), args.Bool(2)
}

func (m *MockCache) SetMaintenanceWindows(ctx context.Context, windows []*api.MaintenanceWindow, total, limit int) {
	m.Called(ctx, windows, total, limit)
}

func (m *MockCache) InvalidateAll(ctx context.Context) {
	m.Called(ctx)
}

// MockValidator is a mock implementation for testing
type MockValidator struct {
	mock.Mock
}

func (m *MockValidator) ValidateCreateRequest(ctx context.Context, req *api.CreateMaintenanceWindowRequest) error {
	args := m.Called(ctx, req)
	return args.Error(0)
}

func (m *MockValidator) ValidateUpdateRequest(ctx context.Context, req *api.UpdateMaintenanceWindowRequest) error {
	args := m.Called(ctx, req)
	return args.Error(0)
}

func (m *MockValidator) ValidateWindowID(ctx context.Context, windowID string) error {
	args := m.Called(ctx, windowID)
	return args.Error(0)
}

func (m *MockValidator) ValidatePagination(ctx context.Context, limit, offset *int) error {
	args := m.Called(ctx, limit, offset)
	return args.Error(0)
}

// MockMetrics is a mock implementation for testing
type MockMetrics struct {
	mock.Mock
}

func (m *MockMetrics) RecordRequest(operation string) {
	m.Called(operation)
}

func (m *MockMetrics) RecordError(operation string) {
	m.Called(operation)
}

func (m *MockMetrics) RecordCacheHit(operation string) {
	m.Called(operation)
}

func (m *MockMetrics) RecordCacheMiss(operation string) {
	m.Called(operation)
}

func (m *MockMetrics) RecordSuccess(operation string) {
	m.Called(operation)
}

func (m *MockMetrics) GetMetrics() map[string]interface{} {
	args := m.Called()
	return args.Get(0).(map[string]interface{})
}

func (m *MockMetrics) Reset() {
	m.Called()
}

func TestMaintenanceWindowsService_CreateMaintenanceWindow(t *testing.T) {
	mockRepo := &MockRepository{}
	mockCache := &MockCache{}
	mockValidator := &MockValidator{}
	mockMetrics := &MockMetrics{}

	service := &MaintenanceWindowsService{
		repo:      mockRepo,
		cache:     mockCache,
		validator: mockValidator,
		metrics:   mockMetrics,
	}

	ctx := context.Background()
	req := &api.CreateMaintenanceWindowRequest{
		Title:           "Test Maintenance",
		Description:     "Test description",
		MaintenanceType: api.MaintenanceWindowMaintenanceTypeScheduled,
		ScheduledStart:  api.NewOptDateTime(time.Now().Add(time.Hour)),
		ScheduledEnd:    api.NewOptDateTime(time.Now().Add(2 * time.Hour)),
		AffectedServices: []string{"api-gateway", "character-service"},
	}

	mockValidator.On("ValidateCreateRequest", ctx, req).Return(nil)
	mockMetrics.On("RecordRequest", "CreateMaintenanceWindow").Return()
	mockRepo.On("CreateMaintenanceWindow", ctx, mock.AnythingOfType("*api.MaintenanceWindow")).Return(nil)
	mockCache.On("SetMaintenanceWindow", ctx, mock.AnythingOfType("uuid.UUID"), mock.AnythingOfType("*api.MaintenanceWindow")).Return()
	mockMetrics.On("RecordSuccess", "CreateMaintenanceWindow").Return()

	window, err := service.CreateMaintenanceWindow(ctx, req)

	assert.NoError(t, err)
	assert.NotNil(t, window)
	assert.Equal(t, req.Title, window.Title)
	assert.Equal(t, api.MaintenanceWindowStatusPlanned, window.Status)

	mockValidator.AssertExpectations(t)
	mockMetrics.AssertExpectations(t)
	mockRepo.AssertExpectations(t)
	mockCache.AssertExpectations(t)
}

func TestMaintenanceWindowsService_GetMaintenanceWindow(t *testing.T) {
	mockRepo := &MockRepository{}
	mockCache := &MockCache{}
	mockValidator := &MockValidator{}
	mockMetrics := &MockMetrics{}

	service := &MaintenanceWindowsService{
		repo:      mockRepo,
		cache:     mockCache,
		validator: mockValidator,
		metrics:   mockMetrics,
	}

	ctx := context.Background()
	windowID := uuid.New()
	expectedWindow := &api.MaintenanceWindow{
		ID:              api.NewOptUUID(windowID),
		Title:           "Test Window",
		MaintenanceType: api.MaintenanceWindowMaintenanceTypeScheduled,
		Status:          api.MaintenanceWindowStatusPlanned,
	}

	mockMetrics.On("RecordRequest", "GetMaintenanceWindow").Return()
	mockCache.On("GetMaintenanceWindow", ctx, windowID).Return((*api.MaintenanceWindow)(nil), false) // Cache miss
	mockRepo.On("GetMaintenanceWindow", ctx, windowID).Return(expectedWindow, nil)
	mockCache.On("SetMaintenanceWindow", ctx, windowID, expectedWindow).Return()
	mockMetrics.On("RecordSuccess", "GetMaintenanceWindow").Return()

	window, err := service.GetMaintenanceWindow(ctx, windowID)

	assert.NoError(t, err)
	assert.NotNil(t, window)
	assert.Equal(t, expectedWindow.Title, window.Title)

	mockMetrics.AssertExpectations(t)
	mockCache.AssertExpectations(t)
	mockRepo.AssertExpectations(t)
}

func TestMaintenanceWindowsService_CancelMaintenanceWindow(t *testing.T) {
	mockRepo := &MockRepository{}
	mockCache := &MockCache{}
	mockValidator := &MockValidator{}
	mockMetrics := &MockMetrics{}

	service := &MaintenanceWindowsService{
		repo:      mockRepo,
		cache:     mockCache,
		validator: mockValidator,
		metrics:   mockMetrics,
	}

	ctx := context.Background()
	windowID := uuid.New()

	mockMetrics.On("RecordRequest", "CancelMaintenanceWindow").Return()
	mockValidator.On("ValidateUpdateRequest", ctx, mock.AnythingOfType("*api.UpdateMaintenanceWindowRequest")).Return(nil)
	mockRepo.On("GetMaintenanceWindow", ctx, windowID).Return(&api.MaintenanceWindow{
		ID:              api.NewOptUUID(windowID),
		Title:           "Test Window",
		MaintenanceType: api.MaintenanceWindowMaintenanceTypeScheduled,
		Status:          api.MaintenanceWindowStatusPlanned,
	}, nil)
	mockRepo.On("UpdateMaintenanceWindow", ctx, mock.AnythingOfType("*api.MaintenanceWindow")).Return(nil)
	mockCache.On("DeleteMaintenanceWindow", ctx, windowID).Return()
	mockMetrics.On("RecordSuccess", "CancelMaintenanceWindow").Return()

	err := service.CancelMaintenanceWindow(ctx, windowID)

	assert.NoError(t, err)

	mockMetrics.AssertExpectations(t)
	mockValidator.AssertExpectations(t)
	mockRepo.AssertExpectations(t)
	mockCache.AssertExpectations(t)
}
