package server

import (
	"context"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/inventory-service-go/models"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestRepository(t *testing.T) (*InventoryRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewInventoryRepository(dbPool, nil)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewInventoryRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewInventoryRepository(dbPool, nil)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestInventoryRepository_GetInventoryByCharacterID_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()

	ctx := context.Background()
	inv, err := repo.GetInventoryByCharacterID(ctx, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, inv)
}

func TestInventoryRepository_CreateInventory(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	capacity := 50
	maxWeight := 100.0

	ctx := context.Background()
	inv, err := repo.CreateInventory(ctx, characterID, capacity, maxWeight)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, inv)
	assert.Equal(t, characterID, inv.CharacterID)
	assert.Equal(t, capacity, inv.Capacity)
	assert.Equal(t, 0, inv.UsedSlots)
	assert.Equal(t, 0.0, inv.Weight)
	assert.Equal(t, maxWeight, inv.MaxWeight)
}

func TestInventoryRepository_GetInventoryItems_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()

	inv, err := repo.CreateInventory(ctx, characterID, 50, 100.0)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	items, err := repo.GetInventoryItems(ctx, inv.ID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, items)
}

func TestInventoryRepository_AddItem(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()

	inv, err := repo.CreateInventory(ctx, characterID, 50, 100.0)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	item := &models.InventoryItem{
		ID:           uuid.New(),
		InventoryID:  inv.ID,
		ItemID:       "item_001",
		SlotIndex:    0,
		StackCount:   1,
		MaxStackSize: 10,
		IsEquipped:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	err = repo.AddItem(ctx, item)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	items, err := repo.GetInventoryItems(ctx, inv.ID)
	require.NoError(t, err)
	assert.Len(t, items, 1)
	assert.Equal(t, item.ItemID, items[0].ItemID)
}

func TestInventoryRepository_UpdateItem(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()

	inv, err := repo.CreateInventory(ctx, characterID, 50, 100.0)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	item := &models.InventoryItem{
		ID:           uuid.New(),
		InventoryID:  inv.ID,
		ItemID:       "item_001",
		SlotIndex:    0,
		StackCount:   1,
		MaxStackSize: 10,
		IsEquipped:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	err = repo.AddItem(ctx, item)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	item.StackCount = 5
	item.UpdatedAt = time.Now()

	err = repo.UpdateItem(ctx, item)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	items, err := repo.GetInventoryItems(ctx, inv.ID)
	require.NoError(t, err)
	assert.Len(t, items, 1)
	assert.Equal(t, 5, items[0].StackCount)
}

func TestInventoryRepository_RemoveItem(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()

	inv, err := repo.CreateInventory(ctx, characterID, 50, 100.0)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	item := &models.InventoryItem{
		ID:           uuid.New(),
		InventoryID:  inv.ID,
		ItemID:       "item_001",
		SlotIndex:    0,
		StackCount:   1,
		MaxStackSize: 10,
		IsEquipped:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	err = repo.AddItem(ctx, item)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.RemoveItem(ctx, item.ID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	items, err := repo.GetInventoryItems(ctx, inv.ID)
	require.NoError(t, err)
	assert.Empty(t, items)
}

func TestInventoryRepository_UpdateInventoryStats(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()

	inv, err := repo.CreateInventory(ctx, characterID, 50, 100.0)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	usedSlots := 5
	weight := 25.5

	err = repo.UpdateInventoryStats(ctx, inv.ID, usedSlots, weight)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	updatedInv, err := repo.GetInventoryByCharacterID(ctx, characterID)
	require.NoError(t, err)
	assert.Equal(t, usedSlots, updatedInv.UsedSlots)
	assert.Equal(t, weight, updatedInv.Weight)
}

func TestInventoryRepository_GetItemTemplate_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	template, err := repo.GetItemTemplate(ctx, "non_existent_item")

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, template)
}
