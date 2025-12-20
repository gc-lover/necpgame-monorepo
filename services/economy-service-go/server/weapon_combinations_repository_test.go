// Issue: #140894175
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/stretchr/testify/assert"
)

func setupTestWeaponCombinationsRepository(t *testing.T) (*WeaponCombinationsRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewWeaponCombinationsRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewWeaponCombinationsRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewWeaponCombinationsRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestWeaponCombinationsRepository_SaveWeaponCombination(t *testing.T) {
	repo, cleanup := setupTestWeaponCombinationsRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	weaponID := uuid.New()
	combinationData := map[string]interface{}{
		"base_type": "pistol",
		"rarity":    "legendary",
		"damage":    150,
	}

	ctx := context.Background()
	err := repo.SaveWeaponCombination(ctx, weaponID, combinationData)

	assert.NoError(t, err)
}

func TestWeaponCombinationsRepository_GetWeaponCombination(t *testing.T) {
	repo, cleanup := setupTestWeaponCombinationsRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	weaponID := uuid.New()
	ctx := context.Background()

	combination, err := repo.GetWeaponCombination(ctx, weaponID)

	assert.NoError(t, err)
	assert.NotNil(t, combination)
}

func TestWeaponCombinationsRepository_SaveWeaponModifier(t *testing.T) {
	repo, cleanup := setupTestWeaponCombinationsRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	weaponID := uuid.New()
	modifierID := uuid.New()
	modifierData := map[string]interface{}{
		"type":         "scope",
		"damage_bonus": 10,
		"accuracy":     5,
	}

	ctx := context.Background()
	err := repo.SaveWeaponModifier(ctx, weaponID, modifierID, modifierData)

	assert.NoError(t, err)
}

func TestWeaponCombinationsRepository_GetWeaponModifiers(t *testing.T) {
	repo, cleanup := setupTestWeaponCombinationsRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()

	modifiers, err := repo.GetWeaponModifiers(ctx)

	assert.NoError(t, err)
	assert.NotNil(t, modifiers)
	assert.IsType(t, []map[string]interface{}{}, modifiers)
}
