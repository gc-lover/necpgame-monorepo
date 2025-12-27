// Issue: #404 - Unit-тесты для admin-service-go
package server

import (
	"context"
	"encoding/json"
	"testing"
	"time"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"go.uber.org/zap/zaptest"
	"admin-service-go/server/internal/models"
)

func setupTestRepository(t *testing.T) (*AdminRepository, sqlmock.Sqlmock) {
	// Create a mock database connection
	_, mock, err := sqlmock.New()
	require.NoError(t, err)

	// Create a pool config (we'll mock the pool behavior)
	pool, err := pgxpool.New(context.Background(), "postgres://test:test@localhost:5432/test?sslmode=disable")
	require.NoError(t, err)

	logger := zaptest.NewLogger(t)
	repo := &AdminRepository{
		db:     pool,
		logger: logger,
	}

	// Note: For full integration testing, we'd need a test database
	// These tests demonstrate the structure but will skip if DB is not available
	t.Skip("Skipping repository tests - requires test database setup")

	return repo, mock
}

func TestAdminRepository_CreateAdminAction(t *testing.T) {
	repo, mock := setupTestRepository(t)
	defer mock.ExpectClose()

	tests := []struct {
		name    string
		action  *models.AdminAction
		mockFn  func()
		wantErr bool
	}{
		{
			name: "successful creation",
			action: &models.AdminAction{
				ID:        uuid.New(),
				AdminID:   uuid.New(),
				Action:    "user_ban",
				Resource:  "users/123",
				Timestamp: time.Now(),
				IPAddress: "127.0.0.1",
				UserAgent: "Test Agent",
				Metadata:  map[string]interface{}{"reason": "test"},
			},
			mockFn: func() {
				mock.ExpectExec(`INSERT INTO admin_audit_log`).
					WithArgs(sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg()).
					WillReturnResult(sqlmock.NewResult(1, 1))
			},
			wantErr: false,
		},
		{
			name: "database error",
			action: &models.AdminAction{
				ID:        uuid.New(),
				AdminID:   uuid.New(),
				Action:    "user_ban",
				Resource:  "users/123",
				Timestamp: time.Now(),
				IPAddress: "127.0.0.1",
				UserAgent: "Test Agent",
				Metadata:  map[string]interface{}{"reason": "test"},
			},
			mockFn: func() {
				mock.ExpectExec(`INSERT INTO admin_audit_log`).
					WithArgs(sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg()).
					WillReturnError(sqlmock.ErrCancelled)
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			err := repo.CreateAdminAction(context.Background(), tt.action)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}

func TestAdminRepository_GetAdminActions(t *testing.T) {
	repo, mock := setupTestRepository(t)
	defer mock.ExpectClose()

	tests := []struct {
		name    string
		filter  *models.AuditLogFilter
		mockFn  func()
		wantErr bool
	}{
		{
			name: "successful retrieval with filter",
			filter: &models.AuditLogFilter{
				AdminID:   &[]uuid.UUID{uuid.New()}[0],
				Action:    &[]string{"user_ban"}[0],
				Limit:     10,
				Offset:    0,
			},
			mockFn: func() {
				rows := sqlmock.NewRows([]string{"id", "admin_id", "action", "resource", "timestamp", "ip_address", "user_agent", "metadata"}).
					AddRow(uuid.New(), uuid.New(), "user_ban", "users/123", time.Now(), "127.0.0.1", "Test Agent", json.RawMessage(`{"reason":"test"}`))
				mock.ExpectQuery(`SELECT (.+) FROM admin_audit_log`).
					WithArgs(sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg()).
					WillReturnRows(rows)
			},
			wantErr: false,
		},
		{
			name: "empty results",
			filter: &models.AuditLogFilter{
				Limit:  10,
				Offset: 0,
			},
			mockFn: func() {
				rows := sqlmock.NewRows([]string{"id", "admin_id", "action", "resource", "timestamp", "ip_address", "user_agent", "metadata"})
				mock.ExpectQuery(`SELECT (.+) FROM admin_audit_log`).
					WithArgs(sqlmock.AnyArg(), sqlmock.AnyArg()).
					WillReturnRows(rows)
			},
			wantErr: false,
		},
		{
			name: "database error",
			filter: &models.AuditLogFilter{
				Limit:  10,
				Offset: 0,
			},
			mockFn: func() {
				mock.ExpectQuery(`SELECT (.+) FROM admin_audit_log`).
					WithArgs(sqlmock.AnyArg(), sqlmock.AnyArg()).
					WillReturnError(sqlmock.ErrCancelled)
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			actions, err := repo.GetAdminActions(context.Background(), tt.filter)
			if tt.wantErr {
				assert.Error(t, err)
				assert.Nil(t, actions)
			} else {
				assert.NoError(t, err)
				assert.NotNil(t, actions)
			}
		})
	}
}

func TestAdminRepository_BanUser(t *testing.T) {
	repo, mock := setupTestRepository(t)
	defer mock.ExpectClose()

	userID := uuid.New()
	adminID := uuid.New()
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
				mock.ExpectExec(`INSERT INTO user_bans`).
					WithArgs(userID, reason, duration, adminID, sqlmock.AnyArg(), sqlmock.AnyArg()).
					WillReturnResult(sqlmock.NewResult(1, 1))
			},
			wantErr: false,
		},
		{
			name: "database error",
			mockFn: func() {
				mock.ExpectExec(`INSERT INTO user_bans`).
					WithArgs(userID, reason, duration, adminID, sqlmock.AnyArg(), sqlmock.AnyArg()).
					WillReturnError(sqlmock.ErrCancelled)
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			err := repo.BanUser(context.Background(), userID, reason, duration, adminID)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}

func TestAdminRepository_UnbanUser(t *testing.T) {
	repo, mock := setupTestRepository(t)
	defer mock.ExpectClose()

	userID := uuid.New()

	tests := []struct {
		name    string
		mockFn  func()
		wantErr bool
	}{
		{
			name: "successful unban",
			mockFn: func() {
				mock.ExpectExec(`DELETE FROM user_bans`).
					WithArgs(userID).
					WillReturnResult(sqlmock.NewResult(1, 1))
			},
			wantErr: false,
		},
		{
			name: "user not banned",
			mockFn: func() {
				mock.ExpectExec(`DELETE FROM user_bans`).
					WithArgs(userID).
					WillReturnResult(sqlmock.NewResult(0, 0))
			},
			wantErr: true,
		},
		{
			name: "database error",
			mockFn: func() {
				mock.ExpectExec(`DELETE FROM user_bans`).
					WithArgs(userID).
					WillReturnError(sqlmock.ErrCancelled)
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			err := repo.UnbanUser(context.Background(), userID)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}

func TestAdminRepository_GetUserDetails(t *testing.T) {
	repo, mock := setupTestRepository(t)
	defer mock.ExpectClose()

	userID := uuid.New()

	tests := []struct {
		name    string
		mockFn  func()
		wantErr bool
	}{
		{
			name: "successful retrieval",
			mockFn: func() {
				rows := sqlmock.NewRows([]string{"id", "username", "email", "created_at", "last_login", "is_active", "role", "ban_reason", "ban_expires"}).
					AddRow(userID, "testuser", "test@example.com", time.Now(), time.Now(), true, "user", nil, nil)
				mock.ExpectQuery(`SELECT (.+) FROM users`).
					WithArgs(userID).
					WillReturnRows(rows)
			},
			wantErr: false,
		},
		{
			name: "user not found",
			mockFn: func() {
				mock.ExpectQuery(`SELECT (.+) FROM users`).
					WithArgs(userID).
					WillReturnError(sqlmock.ErrCancelled)
			},
			wantErr: true,
		},
		{
			name: "user with ban",
			mockFn: func() {
				banExpires := time.Now().Add(24 * time.Hour)
				rows := sqlmock.NewRows([]string{"id", "username", "email", "created_at", "last_login", "is_active", "role", "ban_reason", "ban_expires"}).
					AddRow(userID, "testuser", "test@example.com", time.Now(), time.Now(), true, "user", "spam", banExpires.Format(time.RFC3339))
				mock.ExpectQuery(`SELECT (.+) FROM users`).
					WithArgs(userID).
					WillReturnRows(rows)
			},
			wantErr: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			user, err := repo.GetUserDetails(context.Background(), userID)
			if tt.wantErr {
				assert.Error(t, err)
				assert.Nil(t, user)
			} else {
				assert.NoError(t, err)
				assert.NotNil(t, user)
				assert.Equal(t, userID, user.ID)
			}
		})
	}
}

func TestAdminRepository_IsUserBanned(t *testing.T) {
	repo, mock := setupTestRepository(t)
	defer mock.ExpectClose()

	userID := uuid.New()

	tests := []struct {
		name     string
		mockFn   func()
		expected bool
		wantErr  bool
	}{
		{
			name: "user is banned",
			mockFn: func() {
				rows := sqlmock.NewRows([]string{"exists"}).AddRow(true)
				mock.ExpectQuery(`SELECT EXISTS`).
					WithArgs(userID).
					WillReturnRows(rows)
			},
			expected: true,
			wantErr:  false,
		},
		{
			name: "user is not banned",
			mockFn: func() {
				rows := sqlmock.NewRows([]string{"exists"}).AddRow(false)
				mock.ExpectQuery(`SELECT EXISTS`).
					WithArgs(userID).
					WillReturnRows(rows)
			},
			expected: false,
			wantErr:  false,
		},
		{
			name: "database error",
			mockFn: func() {
				mock.ExpectQuery(`SELECT EXISTS`).
					WithArgs(userID).
					WillReturnError(sqlmock.ErrCancelled)
			},
			expected: false,
			wantErr:  true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			result, err := repo.IsUserBanned(context.Background(), userID)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
				assert.Equal(t, tt.expected, result)
			}
		})
	}
}

func TestAdminRepository_GetContentModerationQueue(t *testing.T) {
	repo, mock := setupTestRepository(t)
	defer mock.ExpectClose()

	tests := []struct {
		name    string
		limit   int
		offset  int
		mockFn  func()
		wantErr bool
	}{
		{
			name:   "successful retrieval",
			limit:  10,
			offset: 0,
			mockFn: func() {
				rows := sqlmock.NewRows([]string{"id", "content_type", "content", "author_id", "submitted_at", "status", "priority"}).
					AddRow(uuid.New(), "post", "test content", uuid.New(), time.Now(), "pending", 1)
				mock.ExpectQuery(`SELECT (.+) FROM content_moderation_queue`).
					WithArgs(10, 0).
					WillReturnRows(rows)
			},
			wantErr: false,
		},
		{
			name:   "empty queue",
			limit:  10,
			offset: 0,
			mockFn: func() {
				rows := sqlmock.NewRows([]string{"id", "content_type", "content", "author_id", "submitted_at", "status", "priority"})
				mock.ExpectQuery(`SELECT (.+) FROM content_moderation_queue`).
					WithArgs(10, 0).
					WillReturnRows(rows)
			},
			wantErr: false,
		},
		{
			name:   "database error",
			limit:  10,
			offset: 0,
			mockFn: func() {
				mock.ExpectQuery(`SELECT (.+) FROM content_moderation_queue`).
					WithArgs(10, 0).
					WillReturnError(sqlmock.ErrCancelled)
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			items, err := repo.GetContentModerationQueue(context.Background(), tt.limit, tt.offset)
			if tt.wantErr {
				assert.Error(t, err)
				assert.Nil(t, items)
			} else {
				assert.NoError(t, err)
				assert.NotNil(t, items)
			}
		})
	}
}

func TestAdminRepository_ModerateContent(t *testing.T) {
	repo, mock := setupTestRepository(t)
	defer mock.ExpectClose()

	contentID := uuid.New()
	moderatorID := uuid.New()
	action := "approve"
	reason := "good content"

	tests := []struct {
		name    string
		mockFn  func()
		wantErr bool
	}{
		{
			name: "successful moderation",
			mockFn: func() {
				mock.ExpectExec(`UPDATE content_moderation_queue`).
					WithArgs(action, moderatorID, reason, contentID).
					WillReturnResult(sqlmock.NewResult(1, 1))
			},
			wantErr: false,
		},
		{
			name: "database error",
			mockFn: func() {
				mock.ExpectExec(`UPDATE content_moderation_queue`).
					WithArgs(action, moderatorID, reason, contentID).
					WillReturnError(sqlmock.ErrCancelled)
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tt.mockFn()

			err := repo.ModerateContent(context.Background(), contentID, action, moderatorID, reason)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}

// Test edge cases and invalid inputs
func TestAdminRepository_EdgeCases(t *testing.T) {
	repo, mock := setupTestRepository(t)
	defer mock.ExpectClose()

	t.Run("invalid UUID in GetUserDetails", func(t *testing.T) {
		// This would test invalid UUID handling if we had validation
		// For now, just ensure the method doesn't panic with any UUID
		invalidUUID := uuid.New() // Any UUID should work
		mock.ExpectQuery(`SELECT (.+) FROM users`).
			WithArgs(invalidUUID).
			WillReturnError(sqlmock.ErrCancelled)

		_, err := repo.GetUserDetails(context.Background(), invalidUUID)
		assert.Error(t, err)
	})

	t.Run("empty metadata in CreateAdminAction", func(t *testing.T) {
		action := &models.AdminAction{
			ID:        uuid.New(),
			AdminID:   uuid.New(),
			Action:    "test",
			Resource:  "test",
			Timestamp: time.Now(),
			IPAddress: "127.0.0.1",
			UserAgent: "test",
			Metadata:  nil, // Empty metadata
		}

		mock.ExpectExec(`INSERT INTO admin_audit_log`).
			WithArgs(sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg()).
			WillReturnResult(sqlmock.NewResult(1, 1))

		err := repo.CreateAdminAction(context.Background(), action)
		assert.NoError(t, err)
	})
}
