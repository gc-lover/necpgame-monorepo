//go:build unit

package repository

import (
	"context"
	"database/sql"
	"testing"
	"time"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/assert"

	"necpgame/services/achievement-service-go/internal/models"
)

func TestAchievementRepository_GetByID_Success(t *testing.T) {
	// Setup
	db, mock, err := sqlmock.New()
	assert.NoError(t, err)
	defer db.Close()

	repo := NewAchievementRepository(db)

	achievementID := "test-achievement-123"
	expectedAchievement := &models.Achievement{
		ID:          achievementID,
		Name:        "Test Achievement",
		Description: "Test Description",
		Type:        models.AchievementTypeProgress,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	// Mock database expectations
	rows := sqlmock.NewRows([]string{
		"id", "name", "description", "type", "points", "is_active",
		"created_at", "updated_at", "version",
	}).AddRow(
		expectedAchievement.ID,
		expectedAchievement.Name,
		expectedAchievement.Description,
		string(expectedAchievement.Type),
		100, // points
		true, // is_active
		expectedAchievement.CreatedAt,
		expectedAchievement.UpdatedAt,
		1, // version
	)

	mock.ExpectQuery(`SELECT (.+) FROM achievements WHERE id = \$1`).
		WithArgs(achievementID).
		WillReturnRows(rows)

	// Execute
	ctx := context.Background()
	achievement, err := repo.GetByID(ctx, achievementID)

	// Assert
	assert.NoError(t, err)
	assert.NotNil(t, achievement)
	assert.Equal(t, achievementID, achievement.ID)
	assert.Equal(t, "Test Achievement", achievement.Name)
	assert.Equal(t, models.AchievementTypeProgress, achievement.Type)

	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestAchievementRepository_GetByID_NotFound(t *testing.T) {
	// Setup
	db, mock, err := sqlmock.New()
	assert.NoError(t, err)
	defer db.Close()

	repo := NewAchievementRepository(db)

	achievementID := "non-existent-id"

	// Mock database expectations - return empty result
	mock.ExpectQuery(`SELECT (.+) FROM achievements WHERE id = \$1`).
		WithArgs(achievementID).
		WillReturnRows(sqlmock.NewRows([]string{
			"id", "name", "description", "type", "points", "is_active",
			"created_at", "updated_at", "version",
		}))

	// Execute
	ctx := context.Background()
	achievement, err := repo.GetByID(ctx, achievementID)

	// Assert
	assert.Error(t, err)
	assert.Nil(t, achievement)
	assert.Equal(t, sql.ErrNoRows, err)

	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestAchievementRepository_Create_Success(t *testing.T) {
	// Setup
	db, mock, err := sqlmock.New()
	assert.NoError(t, err)
	defer db.Close()

	repo := NewAchievementRepository(db)

	achievement := &models.Achievement{
		ID:          "new-achievement-123",
		Name:        "New Achievement",
		Description: "New Description",
		Type:        models.AchievementTypeMilestone,
		Points:      50,
		IsActive:    true,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		Version:     1,
	}

	// Mock database expectations
	mock.ExpectExec(`INSERT INTO achievements (.+) VALUES (.+)`).
		WithArgs(
			achievement.ID,
			achievement.Name,
			achievement.Description,
			string(achievement.Type),
			achievement.Points,
			achievement.IsActive,
			achievement.CreatedAt,
			achievement.UpdatedAt,
			achievement.Version,
		).
		WillReturnResult(sqlmock.NewResult(1, 1))

	// Execute
	ctx := context.Background()
	err = repo.Create(ctx, achievement)

	// Assert
	assert.NoError(t, err)
	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestAchievementRepository_Update_Success(t *testing.T) {
	// Setup
	db, mock, err := sqlmock.New()
	assert.NoError(t, err)
	defer db.Close()

	repo := NewAchievementRepository(db)

	achievement := &models.Achievement{
		ID:          "existing-achievement-123",
		Name:        "Updated Achievement",
		Description: "Updated Description",
		Type:        models.AchievementTypeProgress,
		Points:      75,
		IsActive:    true,
		UpdatedAt:   time.Now(),
		Version:     2,
	}

	// Mock database expectations
	mock.ExpectExec(`UPDATE achievements SET (.+) WHERE id = \$1 AND version = \$2`).
		WithArgs(
			achievement.Name,
			achievement.Description,
			string(achievement.Type),
			achievement.Points,
			achievement.IsActive,
			achievement.UpdatedAt,
			achievement.Version,
			achievement.ID,
			achievement.Version-1, // optimistic locking
		).
		WillReturnResult(sqlmock.NewResult(0, 1))

	// Execute
	ctx := context.Background()
	err = repo.Update(ctx, achievement)

	// Assert
	assert.NoError(t, err)
	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestAchievementRepository_List_Success(t *testing.T) {
	// Setup
	db, mock, err := sqlmock.New()
	assert.NoError(t, err)
	defer db.Close()

	repo := NewAchievementRepository(db)

	limit := 10
	offset := 0

	// Mock database expectations
	rows := sqlmock.NewRows([]string{
		"id", "name", "description", "type", "points", "is_active",
		"created_at", "updated_at", "version",
	}).
		AddRow("ach-1", "Achievement 1", "Desc 1", "progress", 50, true, time.Now(), time.Now(), 1).
		AddRow("ach-2", "Achievement 2", "Desc 2", "milestone", 100, true, time.Now(), time.Now(), 1)

	mock.ExpectQuery(`SELECT (.+) FROM achievements ORDER BY created_at DESC LIMIT \$1 OFFSET \$2`).
		WithArgs(limit, offset).
		WillReturnRows(rows)

	// Execute
	ctx := context.Background()
	achievements, err := repo.List(ctx, limit, offset)

	// Assert
	assert.NoError(t, err)
	assert.Len(t, achievements, 2)
	assert.Equal(t, "ach-1", achievements[0].ID)
	assert.Equal(t, "ach-2", achievements[1].ID)
	assert.Equal(t, "Achievement 1", achievements[0].Name)
	assert.Equal(t, "Achievement 2", achievements[1].Name)

	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestAchievementRepository_Delete_Success(t *testing.T) {
	// Setup
	db, mock, err := sqlmock.New()
	assert.NoError(t, err)
	defer db.Close()

	repo := NewAchievementRepository(db)

	achievementID := "achievement-to-delete"

	// Mock database expectations
	mock.ExpectExec(`DELETE FROM achievements WHERE id = \$1`).
		WithArgs(achievementID).
		WillReturnResult(sqlmock.NewResult(0, 1))

	// Execute
	ctx := context.Background()
	err = repo.Delete(ctx, achievementID)

	// Assert
	assert.NoError(t, err)
	assert.NoError(t, mock.ExpectationsWereMet())
}

// Performance test
func BenchmarkAchievementRepository_GetByID(b *testing.B) {
	// Setup
	db, mock, err := sqlmock.New()
	if err != nil {
		b.Fatal(err)
	}
	defer db.Close()

	repo := NewAchievementRepository(db)

	achievementID := "benchmark-achievement"
	achievement := &models.Achievement{
		ID:          achievementID,
		Name:        "Benchmark Achievement",
		Description: "Benchmark Description",
		Type:        models.AchievementTypeProgress,
	}

	// Mock expectations
	rows := sqlmock.NewRows([]string{
		"id", "name", "description", "type", "points", "is_active",
		"created_at", "updated_at", "version",
	}).AddRow(
		achievement.ID,
		achievement.Name,
		achievement.Description,
		string(achievement.Type),
		100, true, time.Now(), time.Now(), 1,
	)

	// This will be called b.N times
	mock.ExpectQuery(`SELECT (.+) FROM achievements WHERE id = \$1`).
		WithArgs(achievementID).
		WillReturnRows(rows).
		WillReturnRows(rows) // Repeat for all benchmark iterations

	b.ResetTimer()
	ctx := context.Background()
	for i := 0; i < b.N; i++ {
		_, _ = repo.GetByID(ctx, achievementID)
	}
}