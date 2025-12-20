// Issue: #140890170 - Crafting mechanics implementation
package server

import (
	"context"
	"testing"
	"time"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestCraftingService_CalculateCraftingCost(t *testing.T) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := NewCraftingRepository(&pgxpool.Pool{})
	redisClient := redis.NewClient(&redis.Options{})
	service := NewCraftingService(repo, redisClient)

	// Mock recipe data
	mock.ExpectQuery(`SELECT (.+) FROM crafting_recipes WHERE id = \$1`).
		WithArgs("test-recipe").
		WillReturnRows(sqlmock.NewRows([]string{
			"id", "name", "description", "tier", "category", "quality",
			"duration", "success_rate", "created_at", "updated_at",
		}).AddRow(
			"test-recipe", "Test Recipe", "Test description", 1, "weapons", 5,
			time.Minute * 5, 0.9, time.Now(), time.Now(),
		))

	// Mock materials
	mock.ExpectQuery(`SELECT (.+) FROM recipe_materials WHERE recipe_id = \$1`).
		WillReturnRows(sqlmock.NewRows([]string{
			"resource_id", "quantity", "quality", "is_optional",
		}).AddRow("iron", 2, 3, false))

	// Mock requirements
	mock.ExpectQuery(`SELECT (.+) FROM recipe_requirements WHERE recipe_id = \$1`).
		WillReturnRows(sqlmock.NewRows([]string{
			"skill_level", "station_type", "special_tools", "prerequisites",
		}).AddRow(1, "forge", []string{"hammer"}, []string{}))

	// Mock output
	mock.ExpectQuery(`SELECT (.+) FROM recipe_outputs WHERE recipe_id = \$1`).
		WillReturnRows(sqlmock.NewRows([]string{
			"item_id", "quantity", "quality",
		}).AddRow("sword", 1, 5))

	ctx := context.Background()
	costs, err := service.CalculateCraftingCost(ctx, "test-recipe")

	require.NoError(t, err)
	assert.Contains(t, costs, "materials")
	assert.Contains(t, costs, "time")
	assert.Contains(t, costs, "station")
	assert.Contains(t, costs, "risk")
	assert.Contains(t, costs, "total")
	assert.Greater(t, costs["total"], 0.0)
}

func TestCraftingService_StartCrafting(t *testing.T) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := NewCraftingRepository(&pgxpool.Pool{})
	redisClient := redis.NewClient(&redis.Options{})
	service := NewCraftingService(repo, redisClient)

	playerID := uuid.New().String()
	recipeID := "test-recipe"
	stationID := "test-station"

	// Mock recipe lookup
	mock.ExpectQuery(`SELECT (.+) FROM crafting_recipes WHERE id = \$1`).
		WithArgs(recipeID).
		WillReturnRows(sqlmock.NewRows([]string{
			"id", "name", "description", "tier", "category", "quality",
			"duration", "success_rate", "created_at", "updated_at",
		}).AddRow(
			recipeID, "Test Recipe", "Test description", 1, "weapons", 5,
			time.Minute * 5, 0.9, time.Now(), time.Now(),
		))

	// Mock materials query
	mock.ExpectQuery(`SELECT (.+) FROM recipe_materials WHERE recipe_id = \$1`).
		WillReturnRows(sqlmock.NewRows([]string{
			"resource_id", "quantity", "quality", "is_optional",
		}).AddRow("iron", 2, 3, false))

	// Mock requirements query
	mock.ExpectQuery(`SELECT (.+) FROM recipe_requirements WHERE recipe_id = \$1`).
		WillReturnRows(sqlmock.NewRows([]string{
			"skill_level", "station_type", "special_tools", "prerequisites",
		}).AddRow(1, "forge", []string{"hammer"}, []string{}))

	// Mock output query
	mock.ExpectQuery(`SELECT (.+) FROM recipe_outputs WHERE recipe_id = \$1`).
		WillReturnRows(sqlmock.NewRows([]string{
			"item_id", "quantity", "quality",
		}).AddRow("sword", 1, 5))

	// Mock station lookup
	mock.ExpectQuery(`SELECT (.+) FROM crafting_stations WHERE id = \$1`).
		WithArgs(stationID).
		WillReturnRows(sqlmock.NewRows([]string{
			"id", "name", "type", "location", "owner_id", "tier", "efficiency",
			"is_active", "created_at", "updated_at", "total_orders",
			"successful_orders", "failed_orders", "average_quality", "last_used_at",
		}).AddRow(
			stationID, "Test Forge", "forge", "workshop", "owner-123", 1, 1.0,
			true, time.Now(), time.Now(), 10, 8, 2, 4.5, nil,
		))

	// Mock order creation
	mock.ExpectExec(`INSERT INTO crafting_orders (.+) VALUES (.+)`).
		WillReturnResult(sqlmock.NewResult(1, 1))

	// Mock material usage insertion
	mock.ExpectExec(`INSERT INTO used_materials (.+) VALUES (.+)`).
		WillReturnResult(sqlmock.NewResult(1, 1))

	ctx := context.Background()
	materials := []models.UsedMaterial{
		{ResourceID: "iron", Quantity: 2, Quality: 3},
	}

	order, err := service.StartCrafting(ctx, playerID, recipeID, stationID, materials)

	require.NoError(t, err)
	assert.Equal(t, playerID, order.PlayerID)
	assert.Equal(t, recipeID, order.RecipeID)
	assert.Equal(t, stationID, order.StationID)
	assert.Equal(t, "pending", order.Status)
	assert.NotEmpty(t, order.ID)
	assert.Len(t, order.UsedMaterials, 1)
}

func TestCraftingService_GetPlayerOrders(t *testing.T) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := NewCraftingRepository(&pgxpool.Pool{})
	redisClient := redis.NewClient(&redis.Options{})
	service := NewCraftingService(repo, redisClient)

	playerID := uuid.New().String()

	// Mock orders query
	mock.ExpectQuery(`SELECT (.+) FROM crafting_orders WHERE player_id = \$1 (.+)`).
		WithArgs(playerID, 10, 0).
		WillReturnRows(sqlmock.NewRows([]string{
			"id", "player_id", "recipe_id", "station_id", "status", "quality",
			"created_at", "started_at", "completed_at",
		}).AddRow(
			"order-1", playerID, "recipe-1", "station-1", "completed", 8,
			time.Now(), time.Now(), time.Now(),
		))

	ctx := context.Background()
	orders, err := service.GetPlayerOrders(ctx, playerID, 10, 0)

	require.NoError(t, err)
	assert.Len(t, orders, 1)
	assert.Equal(t, "order-1", orders[0].ID)
	assert.Equal(t, playerID, orders[0].PlayerID)
	assert.Equal(t, "completed", orders[0].Status)
}
