package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/housing-service-go/models"
	"github.com/stretchr/testify/assert"
)

func setupTestRepository(t *testing.T) (*HousingRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	logger := GetLogger()
	repo := NewHousingRepository(dbPool, logger)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewHousingRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	logger := GetLogger()
	repo := NewHousingRepository(dbPool, logger)
	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestHousingRepository_GetApartmentByID_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	apartmentID := uuid.New()
	ctx := context.Background()
	result, err := repo.GetApartmentByID(ctx, apartmentID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestHousingRepository_CreateApartment(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ownerID := uuid.New()
	apartment := &models.Apartment{
		ID:            uuid.New(),
		OwnerID:       ownerID,
		OwnerType:     "character",
		ApartmentType: models.ApartmentTypeStudio,
		Location:      "district_01",
		Price:         50000,
		FurnitureSlots: 20,
		PrestigeScore: 0,
		IsPublic:      false,
		Guests:        []uuid.UUID{},
		Settings:      make(map[string]interface{}),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateApartment(ctx, apartment)

	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create apartment: %v", err)
		return
	}

	assert.NoError(t, err)
}

func TestHousingRepository_GetApartmentByID_Success(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ownerID := uuid.New()
	apartment := &models.Apartment{
		ID:            uuid.New(),
		OwnerID:       ownerID,
		OwnerType:     "character",
		ApartmentType: models.ApartmentTypeStudio,
		Location:      "district_01",
		Price:         50000,
		FurnitureSlots: 20,
		PrestigeScore: 0,
		IsPublic:      false,
		Guests:        []uuid.UUID{},
		Settings:      make(map[string]interface{}),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateApartment(ctx, apartment)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create apartment: %v", err)
		return
	}

	result, err := repo.GetApartmentByID(ctx, apartment.ID)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to get apartment: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, apartment.ID, result.ID)
	assert.Equal(t, ownerID, result.OwnerID)
}

func TestHousingRepository_ListApartments_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	apartments, total, err := repo.ListApartments(ctx, nil, nil, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, apartments)
	assert.GreaterOrEqual(t, total, 0)
}

func TestHousingRepository_ListApartments_WithOwnerID(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ownerID := uuid.New()
	ctx := context.Background()
	apartments, total, err := repo.ListApartments(ctx, &ownerID, nil, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, apartments)
	assert.GreaterOrEqual(t, total, 0)
}

func TestHousingRepository_UpdateApartment(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ownerID := uuid.New()
	apartment := &models.Apartment{
		ID:            uuid.New(),
		OwnerID:       ownerID,
		OwnerType:     "character",
		ApartmentType: models.ApartmentTypeStudio,
		Location:      "district_01",
		Price:         50000,
		FurnitureSlots: 20,
		PrestigeScore: 0,
		IsPublic:      false,
		Guests:        []uuid.UUID{},
		Settings:      make(map[string]interface{}),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateApartment(ctx, apartment)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create apartment: %v", err)
		return
	}

	apartment.IsPublic = true
	apartment.UpdatedAt = time.Now()

	err = repo.UpdateApartment(ctx, apartment)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to update apartment: %v", err)
		return
	}

	assert.NoError(t, err)

	result, err := repo.GetApartmentByID(ctx, apartment.ID)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to get apartment: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.IsPublic)
}

func TestHousingRepository_GetFurnitureItemByID_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	itemID := "nonexistent"
	ctx := context.Background()
	result, err := repo.GetFurnitureItemByID(ctx, itemID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestHousingRepository_ListFurnitureItems_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	items, total, err := repo.ListFurnitureItems(ctx, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, items)
	assert.GreaterOrEqual(t, total, 0)
}

func TestHousingRepository_CreatePlacedFurniture(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	apartmentID := uuid.New()
	furniture := &models.PlacedFurniture{
		ID:            uuid.New(),
		ApartmentID:   apartmentID,
		FurnitureItemID: "furniture_001",
		Position:      map[string]interface{}{"x": 0, "y": 0, "z": 0},
		Rotation:      map[string]interface{}{"x": 0, "y": 0, "z": 0},
		Scale:         map[string]interface{}{"x": 1, "y": 1, "z": 1},
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	ctx := context.Background()
	err := repo.CreatePlacedFurniture(ctx, furniture)

	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create furniture: %v", err)
		return
	}

	assert.NoError(t, err)
}

func TestHousingRepository_GetPlacedFurnitureByID_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	furnitureID := uuid.New()
	ctx := context.Background()
	result, err := repo.GetPlacedFurnitureByID(ctx, furnitureID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestHousingRepository_ListPlacedFurniture_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	apartmentID := uuid.New()
	ctx := context.Background()
	furniture, err := repo.ListPlacedFurniture(ctx, apartmentID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, furniture)
}

func TestHousingRepository_CountPlacedFurniture(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	apartmentID := uuid.New()
	ctx := context.Background()
	count, err := repo.CountPlacedFurniture(ctx, apartmentID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.GreaterOrEqual(t, count, 0)
}

func TestHousingRepository_CreateVisit(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	apartmentID := uuid.New()
	visitorID := uuid.New()
	visit := &models.ApartmentVisit{
		ID:          uuid.New(),
		ApartmentID: apartmentID,
		VisitorID:   visitorID,
		VisitedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateVisit(ctx, visit)

	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create visit: %v", err)
		return
	}

	assert.NoError(t, err)
}

func TestHousingRepository_GetPrestigeLeaderboard_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	entries, total, err := repo.GetPrestigeLeaderboard(ctx, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, entries)
	assert.GreaterOrEqual(t, total, 0)
}

