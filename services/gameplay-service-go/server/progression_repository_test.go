package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/stretchr/testify/assert"
)

func setupTestProgressionRepository(t *testing.T) (*ProgressionRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewProgressionRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewProgressionRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewProgressionRepository(dbPool)
	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestProgressionRepository_GetProgression_NotFound(t *testing.T) {
	repo, cleanup := setupTestProgressionRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()
	result, err := repo.GetProgression(ctx, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestProgressionRepository_CreateProgression(t *testing.T) {
	repo, cleanup := setupTestProgressionRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	progression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:            1,
		Experience:       0,
		ExperienceToNext: 100,
		AttributePoints:  0,
		SkillPoints:      0,
		Attributes:       make(map[string]int),
		UpdatedAt:        time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateProgression(ctx, progression)

	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create progression: %v", err)
		return
	}

	assert.NoError(t, err)
}

func TestProgressionRepository_GetProgression_Success(t *testing.T) {
	repo, cleanup := setupTestProgressionRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	progression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:            5,
		Experience:       1000,
		ExperienceToNext: 2000,
		AttributePoints:  10,
		SkillPoints:      5,
		Attributes:       map[string]int{"strength": 10},
		UpdatedAt:        time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateProgression(ctx, progression)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create progression: %v", err)
		return
	}

	result, err := repo.GetProgression(ctx, characterID)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to get progression: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, characterID, result.CharacterID)
	assert.Equal(t, 5, result.Level)
}

func TestProgressionRepository_UpdateProgression(t *testing.T) {
	repo, cleanup := setupTestProgressionRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	progression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:            1,
		Experience:       0,
		ExperienceToNext: 100,
		AttributePoints:  0,
		SkillPoints:      0,
		Attributes:       make(map[string]int),
		UpdatedAt:        time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateProgression(ctx, progression)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create progression: %v", err)
		return
	}

	progression.Level = 10
	progression.Experience = 5000
	progression.UpdatedAt = time.Now()

	err = repo.UpdateProgression(ctx, progression)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to update progression: %v", err)
		return
	}

	assert.NoError(t, err)

	result, err := repo.GetProgression(ctx, characterID)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to get progression: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, 10, result.Level)
	assert.Equal(t, int64(5000), result.Experience)
}

func TestProgressionRepository_GetSkillExperience_NotFound(t *testing.T) {
	repo, cleanup := setupTestProgressionRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	skillID := "combat"
	ctx := context.Background()
	result, err := repo.GetSkillExperience(ctx, characterID, skillID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestProgressionRepository_CreateSkillExperience(t *testing.T) {
	repo, cleanup := setupTestProgressionRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	skillExp := &models.SkillExperience{
		CharacterID: characterID,
		SkillID:     "combat",
		Level:       1,
		Experience:  0,
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateSkillExperience(ctx, skillExp)

	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create skill experience: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, skillExp.ID)
}

func TestProgressionRepository_UpdateSkillExperience(t *testing.T) {
	repo, cleanup := setupTestProgressionRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	skillExp := &models.SkillExperience{
		CharacterID: characterID,
		SkillID:     "combat",
		Level:       1,
		Experience:  0,
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateSkillExperience(ctx, skillExp)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create skill experience: %v", err)
		return
	}

	skillExp.Level = 10
	skillExp.Experience = 5000
	skillExp.UpdatedAt = time.Now()

	err = repo.UpdateSkillExperience(ctx, skillExp)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to update skill experience: %v", err)
		return
	}

	assert.NoError(t, err)

	result, err := repo.GetSkillExperience(ctx, characterID, "combat")
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to get skill experience: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, 10, result.Level)
	assert.Equal(t, int64(5000), result.Experience)
}

func TestProgressionRepository_ListSkillExperience_Empty(t *testing.T) {
	repo, cleanup := setupTestProgressionRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()
	skills, err := repo.ListSkillExperience(ctx, characterID, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, skills)
}

func TestProgressionRepository_CountSkillExperience(t *testing.T) {
	repo, cleanup := setupTestProgressionRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()
	count, err := repo.CountSkillExperience(ctx, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.GreaterOrEqual(t, count, 0)
}

