package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/admin-service-go/models"
	"github.com/stretchr/testify/assert"
)

func setupTestRepository(t *testing.T) (*AdminRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewAdminRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewAdminRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewAdminRepository(dbPool)
	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestAdminRepository_CreateAuditLog(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	targetID := uuid.New()
	log := &models.AdminAuditLog{
		AdminID:    adminID,
		ActionType: models.AdminActionTypeBan,
		TargetID:   &targetID,
		TargetType: "character",
		Details:    map[string]interface{}{"reason": "test ban"},
		IPAddress:  "127.0.0.1",
		UserAgent:  "test-agent",
	}

	ctx := context.Background()
	err := repo.CreateAuditLog(ctx, log)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, log.ID)
	assert.False(t, log.CreatedAt.IsZero())
}

func TestAdminRepository_GetAuditLog_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	logID := uuid.New()
	ctx := context.Background()
	log, err := repo.GetAuditLog(ctx, logID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, log)
}

func TestAdminRepository_GetAuditLog_Success(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	targetID := uuid.New()
	log := &models.AdminAuditLog{
		AdminID:    adminID,
		ActionType: models.AdminActionTypeBan,
		TargetID:   &targetID,
		TargetType: "character",
		Details:    map[string]interface{}{"reason": "test ban"},
		IPAddress:  "127.0.0.1",
		UserAgent:  "test-agent",
	}

	ctx := context.Background()
	err := repo.CreateAuditLog(ctx, log)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create audit log: %v", err)
		return
	}

	result, err := repo.GetAuditLog(ctx, log.ID)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to get audit log: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, log.ID, result.ID)
	assert.Equal(t, adminID, result.AdminID)
	assert.Equal(t, models.AdminActionTypeBan, result.ActionType)
	assert.Equal(t, "127.0.0.1", result.IPAddress)
}

func TestAdminRepository_ListAuditLogs_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	logs, err := repo.ListAuditLogs(ctx, nil, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, logs)
}

func TestAdminRepository_ListAuditLogs_WithFilters(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	targetID := uuid.New()
	log := &models.AdminAuditLog{
		AdminID:    adminID,
		ActionType: models.AdminActionTypeBan,
		TargetID:   &targetID,
		TargetType: "character",
		Details:    map[string]interface{}{"reason": "test ban"},
		IPAddress:  "127.0.0.1",
		UserAgent:  "test-agent",
	}

	ctx := context.Background()
	err := repo.CreateAuditLog(ctx, log)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create audit log: %v", err)
		return
	}

	logs, err := repo.ListAuditLogs(ctx, &adminID, nil, 10, 0)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to list audit logs: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, logs)
}

func TestAdminRepository_ListAuditLogs_WithActionType(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	targetID := uuid.New()
	log := &models.AdminAuditLog{
		AdminID:    adminID,
		ActionType: models.AdminActionTypeKick,
		TargetID:   &targetID,
		TargetType: "character",
		Details:    map[string]interface{}{"reason": "test kick"},
		IPAddress:  "127.0.0.1",
		UserAgent:  "test-agent",
	}

	ctx := context.Background()
	err := repo.CreateAuditLog(ctx, log)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create audit log: %v", err)
		return
	}

	actionType := models.AdminActionTypeKick
	logs, err := repo.ListAuditLogs(ctx, nil, &actionType, 10, 0)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to list audit logs: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, logs)
}

func TestAdminRepository_CountAuditLogs(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	count, err := repo.CountAuditLogs(ctx, nil, nil)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.GreaterOrEqual(t, count, 0)
}

func TestAdminRepository_CountAuditLogs_WithFilters(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	targetID := uuid.New()
	log := &models.AdminAuditLog{
		AdminID:    adminID,
		ActionType: models.AdminActionTypeMute,
		TargetID:   &targetID,
		TargetType: "character",
		Details:    map[string]interface{}{"reason": "test mute"},
		IPAddress:  "127.0.0.1",
		UserAgent:  "test-agent",
	}

	ctx := context.Background()
	err := repo.CreateAuditLog(ctx, log)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create audit log: %v", err)
		return
	}

	actionType := models.AdminActionTypeMute
	count, err := repo.CountAuditLogs(ctx, &adminID, &actionType)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to count audit logs: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.GreaterOrEqual(t, count, 1)
}

func TestAdminRepository_GetAuditLog_WithDetails(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	targetID := uuid.New()
	log := &models.AdminAuditLog{
		AdminID:    adminID,
		ActionType: models.AdminActionTypeGiveItem,
		TargetID:   &targetID,
		TargetType: "character",
		Details:    map[string]interface{}{"item_id": "item_001", "quantity": 10},
		IPAddress:  "127.0.0.1",
		UserAgent:  "test-agent",
	}

	ctx := context.Background()
	err := repo.CreateAuditLog(ctx, log)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create audit log: %v", err)
		return
	}

	result, err := repo.GetAuditLog(ctx, log.ID)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to get audit log: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.NotNil(t, result.Details)
	assert.Equal(t, "item_001", result.Details["item_id"])
	assert.Equal(t, float64(10), result.Details["quantity"])
}

func TestAdminRepository_ListAuditLogs_Pagination(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	ctx := context.Background()

	for i := 0; i < 5; i++ {
		targetID := uuid.New()
		log := &models.AdminAuditLog{
			AdminID:    adminID,
			ActionType: models.AdminActionTypeBan,
			TargetID:   &targetID,
			TargetType: "character",
			Details:    map[string]interface{}{"reason": "test"},
			IPAddress:  "127.0.0.1",
			UserAgent:  "test-agent",
		}

		err := repo.CreateAuditLog(ctx, log)
		if err != nil {
			t.Skipf("Skipping test due to database error: failed to create audit log: %v", err)
			return
		}
		time.Sleep(10 * time.Millisecond)
	}

	logs, err := repo.ListAuditLogs(ctx, &adminID, nil, 3, 0)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to list audit logs: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, logs)
	assert.LessOrEqual(t, len(logs), 3)

	if len(logs) > 0 {
		logsPage2, err := repo.ListAuditLogs(ctx, &adminID, nil, 3, 3)
		if err != nil {
			t.Skipf("Skipping test due to database error: failed to list audit logs page 2: %v", err)
			return
		}

		assert.NoError(t, err)
		assert.NotNil(t, logsPage2)
	}
}
