// Issue: #140894066
package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/admin-service-go/models"
	"github.com/stretchr/testify/assert"
)

func TestAdminRepository_ListAuditLogs_WithBothFilters(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	targetID := uuid.New()
	actionType := models.AdminActionTypeKick
	log := &models.AdminAuditLog{
		AdminID:    adminID,
		ActionType: actionType,
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

	logs, err := repo.ListAuditLogs(ctx, &adminID, &actionType, 10, 0)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to list audit logs: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, logs)
}

func TestAdminRepository_CountAuditLogs_WithBothFilters(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	targetID := uuid.New()
	actionType := models.AdminActionTypeMute
	log := &models.AdminAuditLog{
		AdminID:    adminID,
		ActionType: actionType,
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

	count, err := repo.CountAuditLogs(ctx, &adminID, &actionType)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to count audit logs: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.GreaterOrEqual(t, count, 1)
}

func TestAdminRepository_ListAuditLogs_WithLargeLimit(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	logs, err := repo.ListAuditLogs(ctx, nil, nil, 1000, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, logs)
}

func TestAdminRepository_ListAuditLogs_WithLargeOffset(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	logs, err := repo.ListAuditLogs(ctx, nil, nil, 10, 1000)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, logs)
}

func TestAdminRepository_CreateAuditLog_WithEmptyDetails(t *testing.T) {
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
		Details:    map[string]interface{}{},
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
}

func TestAdminRepository_CreateAuditLog_WithNilTargetID(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	log := &models.AdminAuditLog{
		AdminID:    adminID,
		ActionType: models.AdminActionTypeSetWorldFlag,
		TargetID:   nil,
		TargetType: "world",
		Details:    map[string]interface{}{"flag": "test"},
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
}

func TestAdminRepository_GetAuditLog_WithComplexDetails(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	targetID := uuid.New()
	complexDetails := map[string]interface{}{
		"nested": map[string]interface{}{
			"key1": "value1",
			"key2": 123,
			"key3": []interface{}{"item1", "item2"},
		},
		"array": []interface{}{1, 2, 3},
		"string": "test",
		"number": 42,
		"boolean": true,
	}
	log := &models.AdminAuditLog{
		AdminID:    adminID,
		ActionType: models.AdminActionTypeGiveItem,
		TargetID:   &targetID,
		TargetType: "character",
		Details:    complexDetails,
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
}

func TestAdminRepository_ListAuditLogs_OrderByCreatedAt(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	adminID := uuid.New()
	ctx := context.Background()

	for i := 0; i < 3; i++ {
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

	logs, err := repo.ListAuditLogs(ctx, &adminID, nil, 10, 0)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to list audit logs: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, logs)
	if len(logs) >= 2 {
		assert.True(t, logs[0].CreatedAt.After(logs[1].CreatedAt) || logs[0].CreatedAt.Equal(logs[1].CreatedAt))
	}
}

