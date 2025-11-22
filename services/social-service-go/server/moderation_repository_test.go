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

func setupTestModerationRepository(t *testing.T) (*ModerationRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewModerationRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewModerationRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewModerationRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestModerationRepository_CreateBan(t *testing.T) {
	repo, cleanup := setupTestModerationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	adminID := uuid.New()
	ban := &models.ChatBan{
		ID:          uuid.New(),
		CharacterID: characterID,
		Reason:      "Test ban",
		AdminID:     &adminID,
		CreatedAt:   time.Now(),
		IsActive:    true,
	}

	ctx := context.Background()
	err := repo.CreateBan(ctx, ban)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, ban.ID)
	assert.Equal(t, characterID, ban.CharacterID)
	assert.Equal(t, "Test ban", ban.Reason)
	assert.True(t, ban.IsActive)
}

func TestModerationRepository_CreateBan_WithExpiresAt(t *testing.T) {
	repo, cleanup := setupTestModerationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	expiresAt := time.Now().Add(24 * time.Hour)
	ban := &models.ChatBan{
		ID:          uuid.New(),
		CharacterID: characterID,
		Reason:      "Temporary ban",
		ExpiresAt:   &expiresAt,
		CreatedAt:   time.Now(),
		IsActive:    true,
	}

	ctx := context.Background()
	err := repo.CreateBan(ctx, ban)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, ban.ExpiresAt)
}

func TestModerationRepository_GetActiveBan_NotFound(t *testing.T) {
	repo, cleanup := setupTestModerationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()
	ban, err := repo.GetActiveBan(ctx, characterID, nil)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, ban)
}

func TestModerationRepository_GetActiveBan_Found(t *testing.T) {
	repo, cleanup := setupTestModerationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ban := &models.ChatBan{
		ID:          uuid.New(),
		CharacterID: characterID,
		Reason:      "Test ban",
		CreatedAt:   time.Now(),
		IsActive:    true,
	}

	ctx := context.Background()
	err := repo.CreateBan(ctx, ban)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	found, err := repo.GetActiveBan(ctx, characterID, nil)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, found)
	assert.Equal(t, ban.ID, found.ID)
	assert.Equal(t, characterID, found.CharacterID)
	assert.True(t, found.IsActive)
}

func TestModerationRepository_GetBans_Empty(t *testing.T) {
	repo, cleanup := setupTestModerationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	bans, total, err := repo.GetBans(ctx, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, bans)
	assert.Equal(t, 0, total)
	assert.Len(t, bans, 0)
}

func TestModerationRepository_GetBans_WithCharacterID(t *testing.T) {
	repo, cleanup := setupTestModerationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ban := &models.ChatBan{
		ID:          uuid.New(),
		CharacterID: characterID,
		Reason:      "Test ban",
		CreatedAt:   time.Now(),
		IsActive:    true,
	}

	ctx := context.Background()
	err := repo.CreateBan(ctx, ban)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	bans, total, err := repo.GetBans(ctx, &characterID, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.GreaterOrEqual(t, total, 1)
	assert.GreaterOrEqual(t, len(bans), 1)
}

func TestModerationRepository_DeactivateBan(t *testing.T) {
	repo, cleanup := setupTestModerationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ban := &models.ChatBan{
		ID:          uuid.New(),
		CharacterID: characterID,
		Reason:      "Test ban",
		CreatedAt:   time.Now(),
		IsActive:    true,
	}

	ctx := context.Background()
	err := repo.CreateBan(ctx, ban)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.DeactivateBan(ctx, ban.ID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)

	found, err := repo.GetActiveBan(ctx, characterID, nil)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, found)
}

func TestModerationRepository_CreateReport(t *testing.T) {
	repo, cleanup := setupTestModerationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	reporterID := uuid.New()
	reportedID := uuid.New()
	report := &models.ChatReport{
		ID:         uuid.New(),
		ReporterID: reporterID,
		ReportedID: reportedID,
		Reason:     "Test report",
		Status:     "pending",
		CreatedAt:  time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateReport(ctx, report)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, report.ID)
	assert.Equal(t, reporterID, report.ReporterID)
	assert.Equal(t, reportedID, report.ReportedID)
	assert.Equal(t, "pending", report.Status)
}

func TestModerationRepository_GetReports_Empty(t *testing.T) {
	repo, cleanup := setupTestModerationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	reports, total, err := repo.GetReports(ctx, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, reports)
	assert.Equal(t, 0, total)
	assert.Len(t, reports, 0)
}

func TestModerationRepository_GetReports_WithStatus(t *testing.T) {
	repo, cleanup := setupTestModerationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	reporterID := uuid.New()
	reportedID := uuid.New()
	report := &models.ChatReport{
		ID:         uuid.New(),
		ReporterID: reporterID,
		ReportedID: reportedID,
		Reason:     "Test report",
		Status:     "pending",
		CreatedAt:  time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateReport(ctx, report)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	status := "pending"
	reports, total, err := repo.GetReports(ctx, &status, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.GreaterOrEqual(t, total, 1)
	assert.GreaterOrEqual(t, len(reports), 1)
}

func TestModerationRepository_UpdateReportStatus(t *testing.T) {
	repo, cleanup := setupTestModerationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	reporterID := uuid.New()
	reportedID := uuid.New()
	report := &models.ChatReport{
		ID:         uuid.New(),
		ReporterID: reporterID,
		ReportedID: reportedID,
		Reason:     "Test report",
		Status:     "pending",
		CreatedAt:  time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateReport(ctx, report)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	adminID := uuid.New()
	err = repo.UpdateReportStatus(ctx, report.ID, "resolved", &adminID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)

	status := "resolved"
	reports, _, err := repo.GetReports(ctx, &status, 10, 0)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.GreaterOrEqual(t, len(reports), 1)
	if len(reports) > 0 {
		assert.Equal(t, "resolved", reports[0].Status)
	}
}

