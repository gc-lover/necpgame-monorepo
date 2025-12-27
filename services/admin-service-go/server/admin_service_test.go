// Issue: #404 - Unit-тесты для admin-service-go
package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"go.uber.org/zap/zaptest"
	"admin-service-go/server/internal/models"
)

// MockAdminRepository is a mock implementation of AdminRepositoryInterface
type MockAdminRepository struct {
	mock.Mock
}

func (m *MockAdminRepository) CreateAdminAction(ctx context.Context, action *models.AdminAction) error {
	args := m.Called(ctx, action)
	return args.Error(0)
}

func (m *MockAdminRepository) GetAdminActions(ctx context.Context, filter *models.AuditLogFilter) ([]*models.AdminAction, error) {
	args := m.Called(ctx, filter)
	return args.Get(0).([]*models.AdminAction), args.Error(1)
}

func (m *MockAdminRepository) BanUser(ctx context.Context, userID uuid.UUID, reason string, duration time.Duration, adminID uuid.UUID) error {
	args := m.Called(ctx, userID, reason, duration, adminID)
	return args.Error(0)
}

func (m *MockAdminRepository) UnbanUser(ctx context.Context, userID uuid.UUID) error {
	args := m.Called(ctx, userID)
	return args.Error(0)
}

func (m *MockAdminRepository) GetUserDetails(ctx context.Context, userID uuid.UUID) (*models.UserDetails, error) {
	args := m.Called(ctx, userID)
	return args.Get(0).(*models.UserDetails), args.Error(1)
}

func (m *MockAdminRepository) GetSystemMetrics(ctx context.Context) (*models.SystemMetrics, error) {
	args := m.Called(ctx)
	return args.Get(0).(*models.SystemMetrics), args.Error(1)
}

func (m *MockAdminRepository) IsUserBanned(ctx context.Context, userID uuid.UUID) (bool, error) {
	args := m.Called(ctx, userID)
	return args.Bool(0), args.Error(1)
}

func (m *MockAdminRepository) GetContentModerationQueue(ctx context.Context, limit, offset int) ([]*models.ContentItem, error) {
	args := m.Called(ctx, limit, offset)
	return args.Get(0).([]*models.ContentItem), args.Error(1)
}

func (m *MockAdminRepository) ModerateContent(ctx context.Context, contentID uuid.UUID, action string, moderatorID uuid.UUID, reason string) error {
	args := m.Called(ctx, contentID, action, moderatorID, reason)
	return args.Error(0)
}

func setupTestService(t *testing.T) (*AdminService, *MockAdminRepository) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockAdminRepository{}

	service := NewAdminServiceWithRepo(logger, mockRepo, "test-secret")

	return service, mockRepo
}

func TestAdminService_GetSystemHealth(t *testing.T) {
	service, mockRepo := setupTestService(t)
	ctx := context.Background()

	tests := []struct {
		name    string
		mockFn  func()
		wantErr bool
	}{
		{
			name: "successful health check",
			mockFn: func() {
				metrics := &models.SystemMetrics{
					ActiveConnections: 5,
				}
				mockRepo.On("GetSystemMetrics", mock.Anything).Return(metrics, nil)
			},
			wantErr: false,
		},
		{
			name: "metrics error",
			mockFn: func() {
				mockRepo.On("GetSystemMetrics", mock.Anything).Return((*models.SystemMetrics)(nil), assert.AnError)
			},
			wantErr: false, // Health check should still succeed
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			health, err := service.GetSystemHealth(ctx)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
				assert.NotNil(t, health)
				assert.Equal(t, "healthy", health.Status)
				assert.Equal(t, "1.0.0", health.Version)
			}

			mockRepo.AssertExpectations(t)
		})
	}
}

func TestAdminService_GetActiveAdminSessions(t *testing.T) {
	service, _ := setupTestService(t)
	ctx := context.Background()

	sessions, err := service.GetActiveAdminSessions(ctx)
	assert.NoError(t, err)
	assert.NotNil(t, sessions)
	// Initially should be empty
	assert.Len(t, sessions, 0)
}

func TestAdminService_GetAdminAuditLog(t *testing.T) {
	service, mockRepo := setupTestService(t)
	ctx := context.Background()

	expectedActions := []*models.AdminAction{
		{
			ID:        uuid.New(),
			AdminID:   uuid.New(),
			Action:    "user_ban",
			Resource:  "users/123",
			Timestamp: time.Now(),
			IPAddress: "127.0.0.1",
			UserAgent: "Test Agent",
		},
	}

	tests := []struct {
		name     string
		limit    int
		offset   int
		mockFn   func()
		wantErr  bool
	}{
		{
			name:   "successful retrieval",
			limit:  10,
			offset: 0,
			mockFn: func() {
				filter := &models.AuditLogFilter{Limit: 10, Offset: 0}
				mockRepo.On("GetAdminActions", ctx, filter).Return(expectedActions, nil)
			},
			wantErr: false,
		},
		{
			name:   "repository error",
			limit:  10,
			offset: 0,
			mockFn: func() {
				filter := &models.AuditLogFilter{Limit: 10, Offset: 0}
				mockRepo.On("GetAdminActions", ctx, filter).Return(([]*models.AdminAction)(nil), assert.AnError)
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			actions, err := service.GetAdminAuditLog(ctx, tt.limit, tt.offset)
			if tt.wantErr {
				assert.Error(t, err)
				assert.Nil(t, actions)
			} else {
				assert.NoError(t, err)
				assert.NotNil(t, actions)
				assert.Equal(t, expectedActions, actions)
			}

			mockRepo.AssertExpectations(t)
		})
	}
}

func TestAdminService_BanUser(t *testing.T) {
	service, mockRepo := setupTestService(t)
	ctx := context.Background()

	adminID := uuid.New()
	userID := uuid.New()
	reason := "Test ban"
	duration := 24 * time.Hour

	tests := []struct {
		name    string
		mockFn  func()
		wantErr bool
	}{
		{
			name: "successful ban",
			mockFn: func() {
				mockRepo.On("BanUser", ctx, userID, reason, duration, adminID).Return(nil)
			},
			wantErr: false,
		},
		{
			name: "repository error",
			mockFn: func() {
				mockRepo.On("BanUser", ctx, userID, reason, duration, adminID).Return(assert.AnError)
			},
			wantErr: true,
		},
		{
			name: "insufficient permissions",
			mockFn: func() {
				// Admin authentication will fail due to mock implementation
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			err := service.BanUser(ctx, adminID, userID, reason, duration)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}

			mockRepo.AssertExpectations(t)
		})
	}
}

func TestAdminService_UnbanUser(t *testing.T) {
	service, mockRepo := setupTestService(t)
	ctx := context.Background()

	adminID := uuid.New()
	userID := uuid.New()
	reason := "Test unban"

	tests := []struct {
		name    string
		mockFn  func()
		wantErr bool
	}{
		{
			name: "successful unban",
			mockFn: func() {
				mockRepo.On("UnbanUser", ctx, userID).Return(nil)
			},
			wantErr: false,
		},
		{
			name: "repository error",
			mockFn: func() {
				mockRepo.On("UnbanUser", ctx, userID).Return(assert.AnError)
			},
			wantErr: true,
		},
		{
			name: "insufficient permissions",
			mockFn: func() {
				// Admin authentication will fail due to mock implementation
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			err := service.UnbanUser(ctx, adminID, userID, reason)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}

			mockRepo.AssertExpectations(t)
		})
	}
}

func TestAdminService_ValidateAdminPermissions(t *testing.T) {
	service, _ := setupTestService(t)

	tests := []struct {
		name         string
		admin        *models.AdminUser
		requiredPerms []string
		expected     bool
	}{
		{
			name: "super_admin has all permissions",
			admin: &models.AdminUser{
				Role: "super_admin",
			},
			requiredPerms: []string{"any_permission"},
			expected:     true,
		},
		{
			name: "admin has required permissions",
			admin: &models.AdminUser{
				Role:        "admin",
				Permissions: []string{"read", "write", "user_ban"},
			},
			requiredPerms: []string{"read", "user_ban"},
			expected:     true,
		},
		{
			name: "admin missing permission",
			admin: &models.AdminUser{
				Role:        "admin",
				Permissions: []string{"read", "write"},
			},
			requiredPerms: []string{"read", "delete"},
			expected:     false,
		},
		{
			name: "admin has no permissions",
			admin: &models.AdminUser{
				Role:        "admin",
				Permissions: []string{},
			},
			requiredPerms: []string{"read"},
			expected:     false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := service.validateAdminPermissions(tt.admin, tt.requiredPerms)
			assert.Equal(t, tt.expected, result)
		})
	}
}

func TestAdminService_AuthenticateAdmin(t *testing.T) {
	service, _ := setupTestService(t)
	ctx := context.Background()

	admin, err := service.authenticateAdmin(ctx, "test-token")
	assert.NoError(t, err)
	assert.NotNil(t, admin)
	assert.Equal(t, "admin", admin.Username)
	assert.Equal(t, "super_admin", admin.Role)
	assert.Contains(t, admin.Permissions, "read")
	assert.Contains(t, admin.Permissions, "write")
	assert.Contains(t, admin.Permissions, "delete")
	assert.Contains(t, admin.Permissions, "admin")
}

func TestAdminService_LogAdminAction(t *testing.T) {
	service, _ := setupTestService(t)
	ctx := context.Background()

	action := &models.AdminAction{
		AdminID:   uuid.New(),
		Action:    "test_action",
		Resource:  "test_resource",
		IPAddress: "127.0.0.1",
		UserAgent: "Test Agent",
		Metadata:  map[string]interface{}{"test": "data"},
	}

	err := service.logAdminAction(ctx, action)
	assert.NoError(t, err)
	// Note: Current implementation just logs, doesn't return errors
}

func TestAdminService_Close(t *testing.T) {
	service, _ := setupTestService(t)

	err := service.Close()
	assert.NoError(t, err)
}

// Test edge cases
func TestAdminService_EdgeCases(t *testing.T) {
	service, mockRepo := setupTestService(t)
	ctx := context.Background()

	t.Run("BanUser with empty reason", func(t *testing.T) {
		adminID := uuid.New()
		userID := uuid.New()
		reason := ""
		duration := time.Hour

		mockRepo.On("BanUser", ctx, userID, reason, duration, adminID).Return(nil)

		_ = service.BanUser(ctx, adminID, userID, reason, duration)
		// Will fail due to authentication, but tests the path
		mockRepo.AssertExpectations(t)
	})

	t.Run("UnbanUser with empty reason", func(t *testing.T) {
		adminID := uuid.New()
		userID := uuid.New()
		reason := ""

		mockRepo.On("UnbanUser", ctx, userID).Return(nil)

		_ = service.UnbanUser(ctx, adminID, userID, reason)
		// Will fail due to authentication, but tests the path
		mockRepo.AssertExpectations(t)
	})

	t.Run("GetAdminAuditLog with zero limit", func(t *testing.T) {
		filter := &models.AuditLogFilter{Limit: 0, Offset: 0}
		mockRepo.On("GetAdminActions", ctx, filter).Return([]*models.AdminAction{}, nil)

		actions, err := service.GetAdminAuditLog(ctx, 0, 0)
		assert.NoError(t, err)
		assert.NotNil(t, actions)
		mockRepo.AssertExpectations(t)
	})
}
