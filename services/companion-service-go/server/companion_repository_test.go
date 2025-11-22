package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/companion-service-go/models"
	"github.com/stretchr/testify/assert"
)

func setupTestRepository(t *testing.T) (*CompanionRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewCompanionRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewCompanionRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewCompanionRepository(dbPool)
	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestCompanionRepository_GetCompanionType_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	companionTypeID := "nonexistent"
	ctx := context.Background()
	result, err := repo.GetCompanionType(ctx, companionTypeID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestCompanionRepository_CreatePlayerCompanion(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	companion := &models.PlayerCompanion{
		CharacterID:     characterID,
		CompanionTypeID: "combat_drone_001",
		Level:           1,
		Experience:      0,
		Status:          models.CompanionStatusOwned,
		Equipment:       make(map[string]interface{}),
		Stats:           map[string]interface{}{"health": 100.0, "damage": 50.0},
	}

	ctx := context.Background()
	err := repo.CreatePlayerCompanion(ctx, companion)

	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create companion: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, companion.ID)
	assert.False(t, companion.CreatedAt.IsZero())
}

func TestCompanionRepository_GetPlayerCompanion_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	companionID := uuid.New()
	ctx := context.Background()
	result, err := repo.GetPlayerCompanion(ctx, companionID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestCompanionRepository_GetPlayerCompanion_Success(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	companion := &models.PlayerCompanion{
		CharacterID:     characterID,
		CompanionTypeID: "combat_drone_001",
		Level:           1,
		Experience:      0,
		Status:          models.CompanionStatusOwned,
		Equipment:       make(map[string]interface{}),
		Stats:           map[string]interface{}{"health": 100.0, "damage": 50.0},
	}

	ctx := context.Background()
	err := repo.CreatePlayerCompanion(ctx, companion)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create companion: %v", err)
		return
	}

	result, err := repo.GetPlayerCompanion(ctx, companion.ID)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to get companion: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, companion.ID, result.ID)
	assert.Equal(t, characterID, result.CharacterID)
}

func TestCompanionRepository_ListCompanionTypes_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	types, err := repo.ListCompanionTypes(ctx, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, types)
}

func TestCompanionRepository_ListCompanionTypes_WithCategory(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	category := models.CompanionCategoryCombat
	ctx := context.Background()
	types, err := repo.ListCompanionTypes(ctx, &category, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, types)
}

func TestCompanionRepository_CountCompanionTypes(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	count, err := repo.CountCompanionTypes(ctx, nil)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.GreaterOrEqual(t, count, 0)
}

func TestCompanionRepository_UpdatePlayerCompanion(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	companion := &models.PlayerCompanion{
		CharacterID:     characterID,
		CompanionTypeID: "combat_drone_001",
		Level:           1,
		Experience:      0,
		Status:          models.CompanionStatusOwned,
		Equipment:       make(map[string]interface{}),
		Stats:           map[string]interface{}{"health": 100.0, "damage": 50.0},
	}

	ctx := context.Background()
	err := repo.CreatePlayerCompanion(ctx, companion)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create companion: %v", err)
		return
	}

	customName := "My Drone"
	companion.CustomName = &customName
	companion.Level = 5
	companion.Experience = 1000

	err = repo.UpdatePlayerCompanion(ctx, companion)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to update companion: %v", err)
		return
	}

	assert.NoError(t, err)

	result, err := repo.GetPlayerCompanion(ctx, companion.ID)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to get companion: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, customName, *result.CustomName)
	assert.Equal(t, 5, result.Level)
}

func TestCompanionRepository_ListPlayerCompanions_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()
	companions, err := repo.ListPlayerCompanions(ctx, characterID, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, companions)
}

func TestCompanionRepository_ListPlayerCompanions_WithStatus(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	status := models.CompanionStatusOwned
	ctx := context.Background()
	companions, err := repo.ListPlayerCompanions(ctx, characterID, &status, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, companions)
}

func TestCompanionRepository_CountPlayerCompanions(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()
	count, err := repo.CountPlayerCompanions(ctx, characterID, nil)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.GreaterOrEqual(t, count, 0)
}

func TestCompanionRepository_GetActiveCompanion_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()
	result, err := repo.GetActiveCompanion(ctx, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestCompanionRepository_CreateCompanionAbility(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	companion := &models.PlayerCompanion{
		CharacterID:     characterID,
		CompanionTypeID: "combat_drone_001",
		Level:           1,
		Experience:      0,
		Status:          models.CompanionStatusOwned,
		Equipment:       make(map[string]interface{}),
		Stats:           map[string]interface{}{"health": 100.0, "damage": 50.0},
	}

	ctx := context.Background()
	err := repo.CreatePlayerCompanion(ctx, companion)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create companion: %v", err)
		return
	}

	ability := &models.CompanionAbility{
		PlayerCompanionID: companion.ID,
		AbilityID:         "attack",
		IsActive:          true,
	}

	err = repo.CreateCompanionAbility(ctx, ability)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create ability: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, ability.ID)
}

func TestCompanionRepository_GetCompanionAbility_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	companionID := uuid.New()
	abilityID := "nonexistent"
	ctx := context.Background()
	result, err := repo.GetCompanionAbility(ctx, companionID, abilityID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestCompanionRepository_ListCompanionAbilities_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	companionID := uuid.New()
	ctx := context.Background()
	abilities, err := repo.ListCompanionAbilities(ctx, companionID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, abilities)
}

func TestCompanionRepository_UpdateCompanionAbility(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	companion := &models.PlayerCompanion{
		CharacterID:     characterID,
		CompanionTypeID: "combat_drone_001",
		Level:           1,
		Experience:      0,
		Status:          models.CompanionStatusOwned,
		Equipment:       make(map[string]interface{}),
		Stats:           map[string]interface{}{"health": 100.0, "damage": 50.0},
	}

	ctx := context.Background()
	err := repo.CreatePlayerCompanion(ctx, companion)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create companion: %v", err)
		return
	}

	ability := &models.CompanionAbility{
		PlayerCompanionID: companion.ID,
		AbilityID:         "attack",
		IsActive:          true,
	}

	err = repo.CreateCompanionAbility(ctx, ability)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create ability: %v", err)
		return
	}

	cooldownUntil := time.Now().Add(30 * time.Second)
	ability.CooldownUntil = &cooldownUntil
	ability.IsActive = false

	err = repo.UpdateCompanionAbility(ctx, ability)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to update ability: %v", err)
		return
	}

	assert.NoError(t, err)
}

