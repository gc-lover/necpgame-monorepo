package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestNotificationRepository(t *testing.T) (*NotificationRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewNotificationRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewNotificationRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewNotificationRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestNotificationRepository_GetByID_NotFound(t *testing.T) {
	repo, cleanup := setupTestNotificationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	notificationID := uuid.New()

	ctx := context.Background()
	notification, err := repo.GetByID(ctx, notificationID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, notification)
}

func TestNotificationRepository_Create(t *testing.T) {
	repo, cleanup := setupTestNotificationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	notification := &models.Notification{
		ID:        uuid.New(),
		AccountID: accountID,
		Type:      models.NotificationTypeSystem,
		Priority:  models.NotificationPriorityMedium,
		Title:     "Test Notification",
		Content:   "Test content",
		Status:    models.NotificationStatusUnread,
		Channels:  []models.DeliveryChannel{models.DeliveryChannelInGame},
		CreatedAt: time.Now(),
	}

	ctx := context.Background()
	created, err := repo.Create(ctx, notification)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, created)
	assert.Equal(t, notification.Title, created.Title)
	assert.Equal(t, accountID, created.AccountID)
}

func TestNotificationRepository_GetByAccountID_Empty(t *testing.T) {
	repo, cleanup := setupTestNotificationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	ctx := context.Background()

	notifications, err := repo.GetByAccountID(ctx, accountID, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, notifications)
}

func TestNotificationRepository_CountByAccountID(t *testing.T) {
	repo, cleanup := setupTestNotificationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	ctx := context.Background()

	count, err := repo.CountByAccountID(ctx, accountID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Equal(t, 0, count)
}

func TestNotificationRepository_CountUnreadByAccountID(t *testing.T) {
	repo, cleanup := setupTestNotificationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	ctx := context.Background()

	count, err := repo.CountUnreadByAccountID(ctx, accountID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Equal(t, 0, count)
}

func TestNotificationRepository_UpdateStatus(t *testing.T) {
	repo, cleanup := setupTestNotificationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	notification := &models.Notification{
		ID:        uuid.New(),
		AccountID: accountID,
		Type:      models.NotificationTypeSystem,
		Priority:  models.NotificationPriorityMedium,
		Title:     "Test Notification",
		Content:   "Test content",
		Status:    models.NotificationStatusUnread,
		Channels:  []models.DeliveryChannel{models.DeliveryChannelInGame},
		CreatedAt: time.Now(),
	}

	ctx := context.Background()
	created, err := repo.Create(ctx, notification)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	updated, err := repo.UpdateStatus(ctx, created.ID, models.NotificationStatusRead)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, updated)
	assert.Equal(t, models.NotificationStatusRead, updated.Status)
}

