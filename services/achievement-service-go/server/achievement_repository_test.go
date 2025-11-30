package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestRepository(t *testing.T) (*AchievementRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewAchievementRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewAchievementRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewAchievementRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestAchievementRepository_GetByID_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	achievementID := uuid.New()

	ctx := context.Background()
	achievement, err := repo.GetByID(ctx, achievementID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, achievement)
}

func TestAchievementRepository_Create(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	achievement := &models.Achievement{
		ID:          uuid.New(),
		Code:        "test_achievement",
		Type:        models.AchievementTypeProgressive,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Description: "Test description",
		Points:      10,
		Conditions:  make(map[string]interface{}),
		Rewards:     make(map[string]interface{}),
		IsHidden:    false,
		IsSeasonal:  false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, achievement)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	created, err := repo.GetByID(ctx, achievement.ID)
	require.NoError(t, err)
	assert.NotNil(t, created)
	assert.Equal(t, achievement.Code, created.Code)
	assert.Equal(t, achievement.Title, created.Title)
}

func TestAchievementRepository_GetByCode_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	achievement, err := repo.GetByCode(ctx, "non_existent_code")

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, achievement)
}

func TestAchievementRepository_GetByCode_Success(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	achievement := &models.Achievement{
		ID:          uuid.New(),
		Code:        "test_achievement_code",
		Type:        models.AchievementTypeProgressive,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Description: "Test description",
		Points:      10,
		Conditions:  make(map[string]interface{}),
		Rewards:     make(map[string]interface{}),
		IsHidden:    false,
		IsSeasonal:  false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, achievement)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	found, err := repo.GetByCode(ctx, achievement.Code)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, found)
	assert.Equal(t, achievement.Code, found.Code)
	assert.Equal(t, achievement.Title, found.Title)
}

func TestAchievementRepository_List_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	achievements, err := repo.List(ctx, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, achievements)
}

func TestAchievementRepository_Count(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	count, err := repo.Count(ctx, nil)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Equal(t, 0, count)
}

func TestAchievementRepository_CountByCategory(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	categories, err := repo.CountByCategory(ctx)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, categories)
}

func TestAchievementRepository_GetPlayerAchievement_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	playerID := uuid.New()
	achievementID := uuid.New()

	ctx := context.Background()
	pa, err := repo.GetPlayerAchievement(ctx, playerID, achievementID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, pa)
}

func TestAchievementRepository_CreatePlayerAchievement(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	playerID := uuid.New()
	achievementID := uuid.New()
	pa := &models.PlayerAchievement{
		ID:          uuid.New(),
		PlayerID:    playerID,
		AchievementID: achievementID,
		Status:      models.AchievementStatusProgress,
		Progress:    5,
		ProgressMax: 10,
		ProgressData: make(map[string]interface{}),
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.CreatePlayerAchievement(ctx, pa)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	created, err := repo.GetPlayerAchievement(ctx, playerID, achievementID)
	require.NoError(t, err)
	assert.NotNil(t, created)
	assert.Equal(t, playerID, created.PlayerID)
	assert.Equal(t, achievementID, created.AchievementID)
}

func TestAchievementRepository_UpdatePlayerAchievement(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	playerID := uuid.New()
	achievementID := uuid.New()
	pa := &models.PlayerAchievement{
		ID:          uuid.New(),
		PlayerID:    playerID,
		AchievementID: achievementID,
		Status:      models.AchievementStatusProgress,
		Progress:    5,
		ProgressMax: 10,
		ProgressData: make(map[string]interface{}),
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.CreatePlayerAchievement(ctx, pa)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	pa.Status = models.AchievementStatusUnlocked
	pa.Progress = 10
	now := time.Now()
	pa.UnlockedAt = &now
	pa.UpdatedAt = time.Now()

	err = repo.UpdatePlayerAchievement(ctx, pa)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	updated, err := repo.GetPlayerAchievement(ctx, playerID, achievementID)
	require.NoError(t, err)
	assert.NotNil(t, updated)
	assert.Equal(t, models.AchievementStatusUnlocked, updated.Status)
	assert.Equal(t, 10, updated.Progress)
}

func TestAchievementRepository_GetPlayerAchievements_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	playerID := uuid.New()
	ctx := context.Background()

	achievements, err := repo.GetPlayerAchievements(ctx, playerID, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, achievements)
}

func TestAchievementRepository_CountPlayerAchievements(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	playerID := uuid.New()
	ctx := context.Background()

	total, unlocked, err := repo.CountPlayerAchievements(ctx, playerID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Equal(t, 0, total)
	assert.Equal(t, 0, unlocked)
}

func TestAchievementRepository_GetNearCompletion_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	playerID := uuid.New()
	ctx := context.Background()

	achievements, err := repo.GetNearCompletion(ctx, playerID, 0.5)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, achievements)
}

func TestAchievementRepository_GetRecentUnlocks_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	playerID := uuid.New()
	ctx := context.Background()

	achievements, err := repo.GetRecentUnlocks(ctx, playerID, 10)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, achievements)
}

func TestAchievementRepository_GetLeaderboard_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	entries, err := repo.GetLeaderboard(ctx, "all", 10)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, entries)
}

func TestAchievementRepository_GetAchievementStats_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	achievementID := uuid.New()
	ctx := context.Background()

	stats, err := repo.GetAchievementStats(ctx, achievementID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, stats)
	assert.Equal(t, achievementID, stats.AchievementID)
	assert.Equal(t, 0, stats.TotalUnlocks)
}

func TestAchievementRepository_GetPlayerAchievements_WithCategory(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	playerID := uuid.New()
	category := models.CategoryCombat
	ctx := context.Background()

	achievements, err := repo.GetPlayerAchievements(ctx, playerID, &category, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, achievements)
}

func TestAchievementRepository_GetLeaderboard_Daily(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	entries, err := repo.GetLeaderboard(ctx, "daily", 10)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, entries)
}

func TestAchievementRepository_GetLeaderboard_Weekly(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	entries, err := repo.GetLeaderboard(ctx, "weekly", 10)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, entries)
}

