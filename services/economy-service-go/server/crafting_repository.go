// Package server Issue: #140890170 - Crafting mechanics implementation
package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/jackc/pgx/v5/pgxpool"
)

// CraftingRepository управляет данными крафта в базе данных
type CraftingRepository struct {
	db *pgxpool.Pool
}

// NewCraftingRepository создает новый репозиторий крафта
func NewCraftingRepository(db *pgxpool.Pool) *CraftingRepository {
	return &CraftingRepository{db: db}
}

// GetRecipeByID получает рецепт по ID с материалами и требованиями
func (r *CraftingRepository) GetRecipeByID(ctx context.Context, recipeID string) (*models.CraftingRecipe, error) {
	query := `
		SELECT id, name, description, tier, category, quality, duration, success_rate, created_at, updated_at
		FROM crafting_recipes
		WHERE id = $1
	`

	var recipe models.CraftingRecipe
	err := r.db.QueryRow(ctx, query, recipeID).Scan(
		&recipe.ID, &recipe.Name, &recipe.Description, &recipe.Tier, &recipe.Category,
		&recipe.Quality, &recipe.Duration, &recipe.SuccessRate, &recipe.CreatedAt, &recipe.UpdatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("recipe not found: %s", recipeID)
		}
		return nil, fmt.Errorf("failed to get recipe: %w", err)
	}

	// Загружаем материалы
	materials, err := r.getRecipeMaterials(ctx, recipeID)
	if err != nil {
		return nil, fmt.Errorf("failed to get recipe materials: %w", err)
	}
	recipe.Materials = materials

	// Загружаем требования
	requirements, err := r.getRecipeRequirements(ctx, recipeID)
	if err != nil {
		return nil, fmt.Errorf("failed to get recipe requirements: %w", err)
	}
	recipe.Requirements = *requirements

	// Загружаем результат
	output, err := r.getRecipeOutput(ctx, recipeID)
	if err != nil {
		return nil, fmt.Errorf("failed to get recipe output: %w", err)
	}
	recipe.Output = *output

	return &recipe, nil
}

// GetRecipesByCategory получает рецепты по категории с пагинацией
func (r *CraftingRepository) GetRecipesByCategory(ctx context.Context, category string, limit, offset int) ([]*models.CraftingRecipe, error) {
	query := `
		SELECT id, name, description, tier, category, quality, duration, success_rate, created_at, updated_at
		FROM crafting_recipes
		WHERE category = $1
		ORDER BY tier, name
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.Query(ctx, query, category, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get recipes by category: %w", err)
	}
	defer rows.Close()

	var recipes []*models.CraftingRecipe
	for rows.Next() {
		var recipe models.CraftingRecipe
		err := rows.Scan(
			&recipe.ID, &recipe.Name, &recipe.Description, &recipe.Tier, &recipe.Category,
			&recipe.Quality, &recipe.Duration, &recipe.SuccessRate, &recipe.CreatedAt, &recipe.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan recipe: %w", err)
		}

		// Загружаем связанные данные для каждого рецепта
		materials, err := r.getRecipeMaterials(ctx, recipe.ID)
		if err != nil {
			return nil, fmt.Errorf("failed to get materials for recipe %s: %w", recipe.ID, err)
		}
		recipe.Materials = materials

		requirements, err := r.getRecipeRequirements(ctx, recipe.ID)
		if err != nil {
			return nil, fmt.Errorf("failed to get requirements for recipe %s: %w", recipe.ID, err)
		}
		recipe.Requirements = *requirements

		output, err := r.getRecipeOutput(ctx, recipe.ID)
		if err != nil {
			return nil, fmt.Errorf("failed to get output for recipe %s: %w", recipe.ID, err)
		}
		recipe.Output = *output

		recipes = append(recipes, &recipe)
	}

	return recipes, nil
}

// CreateOrder создает новый заказ на крафт
func (r *CraftingRepository) CreateOrder(ctx context.Context, order *models.CraftingOrder) error {
	query := `
		INSERT INTO crafting_orders (id, player_id, recipe_id, station_id, status, quality, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`

	_, err := r.db.Exec(ctx, query,
		order.ID, order.PlayerID, order.RecipeID, order.StationID,
		order.Status, order.Quality, order.CreatedAt,
	)
	if err != nil {
		return fmt.Errorf("failed to create crafting order: %w", err)
	}

	// Сохраняем использованные материалы
	if len(order.UsedMaterials) > 0 {
		err = r.saveUsedMaterials(ctx, order.ID, order.UsedMaterials)
		if err != nil {
			return fmt.Errorf("failed to save used materials: %w", err)
		}
	}

	return nil
}

// UpdateOrderStatus обновляет статус заказа
func (r *CraftingRepository) UpdateOrderStatus(ctx context.Context, orderID, status string, completedAt *time.Time) error {
	query := `
		UPDATE crafting_orders
		SET status = $1, completed_at = $2, updated_at = NOW()
		WHERE id = $3
	`

	_, err := r.db.Exec(ctx, query, status, completedAt, orderID)
	if err != nil {
		return fmt.Errorf("failed to update order status: %w", err)
	}

	return nil
}

// GetOrderByID получает заказ по ID
func (r *CraftingRepository) GetOrderByID(ctx context.Context, orderID string) (*models.CraftingOrder, error) {
	query := `
		SELECT id, player_id, recipe_id, station_id, status, quality, created_at, started_at, completed_at
		FROM crafting_orders
		WHERE id = $1
	`

	var order models.CraftingOrder
	var startedAt, completedAt sql.NullTime

	err := r.db.QueryRow(ctx, query, orderID).Scan(
		&order.ID, &order.PlayerID, &order.RecipeID, &order.StationID,
		&order.Status, &order.Quality, &order.CreatedAt, &startedAt, &completedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("order not found: %s", orderID)
		}
		return nil, fmt.Errorf("failed to get order: %w", err)
	}

	if startedAt.Valid {
		order.StartedAt = &startedAt.Time
	}
	if completedAt.Valid {
		order.CompletedAt = &completedAt.Time
	}

	// Загружаем использованные материалы
	usedMaterials, err := r.getUsedMaterials(ctx, orderID)
	if err != nil {
		return nil, fmt.Errorf("failed to get used materials: %w", err)
	}
	order.UsedMaterials = usedMaterials

	// Загружаем результат
	result, err := r.getCraftingResult(ctx, orderID)
	if err != nil && err.Error() != "result not found" {
		return nil, fmt.Errorf("failed to get crafting result: %w", err)
	}
	order.Result = result

	return &order, nil
}

// GetPlayerOrders получает заказы игрока с пагинацией
func (r *CraftingRepository) GetPlayerOrders(ctx context.Context, playerID string, limit, offset int) ([]*models.CraftingOrder, error) {
	query := `
		SELECT id, player_id, recipe_id, station_id, status, quality, created_at, started_at, completed_at
		FROM crafting_orders
		WHERE player_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.Query(ctx, query, playerID, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get player orders: %w", err)
	}
	defer rows.Close()

	var orders []*models.CraftingOrder
	for rows.Next() {
		var order models.CraftingOrder
		var startedAt, completedAt sql.NullTime

		err := rows.Scan(
			&order.ID, &order.PlayerID, &order.RecipeID, &order.StationID,
			&order.Status, &order.Quality, &order.CreatedAt, &startedAt, &completedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan order: %w", err)
		}

		if startedAt.Valid {
			order.StartedAt = &startedAt.Time
		}
		if completedAt.Valid {
			order.CompletedAt = &completedAt.Time
		}

		orders = append(orders, &order)
	}

	return orders, nil
}

// GetStationByID получает станцию по ID
func (r *CraftingRepository) GetStationByID(ctx context.Context, stationID string) (*models.CraftingStation, error) {
	query := `
		SELECT id, name, type, location, owner_id, tier, efficiency, is_active, created_at, updated_at,
			   total_orders, successful_orders, failed_orders, average_quality, last_used_at
		FROM crafting_stations
		WHERE id = $1
	`

	var station models.CraftingStation
	var lastUsedAt sql.NullTime

	err := r.db.QueryRow(ctx, query, stationID).Scan(
		&station.ID, &station.Name, &station.Type, &station.Location, &station.OwnerID,
		&station.Tier, &station.Efficiency, &station.IsActive, &station.CreatedAt, &station.UpdatedAt,
		&station.UsageStats.TotalOrders, &station.UsageStats.SuccessfulOrders,
		&station.UsageStats.FailedOrders, &station.UsageStats.AverageQuality, &lastUsedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("station not found: %s", stationID)
		}
		return nil, fmt.Errorf("failed to get station: %w", err)
	}

	if lastUsedAt.Valid {
		station.UsageStats.LastUsedAt = &lastUsedAt.Time
	}

	return &station, nil
}

// UpdateStationStats обновляет статистику станции
func (r *CraftingRepository) UpdateStationStats(ctx context.Context, stationID string, success bool, quality float64) error {
	query := `
		UPDATE crafting_stations
		SET total_orders = total_orders + 1,
		    successful_orders = CASE WHEN $2 THEN successful_orders + 1 ELSE successful_orders END,
		    failed_orders = CASE WHEN NOT $2 THEN failed_orders + 1 ELSE failed_orders END,
		    average_quality = (average_quality * total_orders + $3) / (total_orders + 1),
		    last_used_at = NOW(),
		    updated_at = NOW()
		WHERE id = $1
	`

	_, err := r.db.Exec(ctx, query, stationID, success, quality)
	if err != nil {
		return fmt.Errorf("failed to update station stats: %w", err)
	}

	return nil
}

// CreateContract создает новый контракт на крафт
func (r *CraftingRepository) CreateContract(ctx context.Context, contract *models.CraftingContract) error {
	query := `
		INSERT INTO crafting_contracts (id, title, description, client_id, crafter_id, recipe_id,
		                               status, currency, amount, bonus, deadline, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
	`

	_, err := r.db.Exec(ctx, query,
		contract.ID, contract.Title, contract.Description, contract.ClientID, contract.CrafterID,
		contract.RecipeID, contract.Status, contract.Reward.Currency, contract.Reward.Amount,
		contract.Reward.Bonus, contract.Deadline, contract.CreatedAt,
	)
	if err != nil {
		return fmt.Errorf("failed to create contract: %w", err)
	}

	return nil
}

// GetContractsByStatus получает контракты по статусу
func (r *CraftingRepository) GetContractsByStatus(ctx context.Context, status string, limit, offset int) ([]*models.CraftingContract, error) {
	query := `
		SELECT id, title, description, client_id, crafter_id, recipe_id, status,
		       currency, amount, bonus, deadline, created_at, updated_at
		FROM crafting_contracts
		WHERE status = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.Query(ctx, query, status, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get contracts: %w", err)
	}
	defer rows.Close()

	var contracts []*models.CraftingContract
	for rows.Next() {
		var contract models.CraftingContract
		var deadline sql.NullTime

		err := rows.Scan(
			&contract.ID, &contract.Title, &contract.Description, &contract.ClientID,
			&contract.CrafterID, &contract.RecipeID, &contract.Status, &contract.Reward.Currency,
			&contract.Reward.Amount, &contract.Reward.Bonus, &deadline, &contract.CreatedAt, &contract.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan contract: %w", err)
		}

		if deadline.Valid {
			contract.Deadline = &deadline.Time
		}

		contracts = append(contracts, &contract)
	}

	return contracts, nil
}

// Вспомогательные методы для загрузки связанных данных

func (r *CraftingRepository) getRecipeMaterials(ctx context.Context, recipeID string) ([]models.RecipeMaterial, error) {
	query := `
		SELECT resource_id, quantity, quality, is_optional
		FROM recipe_materials
		WHERE recipe_id = $1
		ORDER BY resource_id
	`

	rows, err := r.db.Query(ctx, query, recipeID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var materials []models.RecipeMaterial
	for rows.Next() {
		var material models.RecipeMaterial
		err := rows.Scan(&material.ResourceID, &material.Quantity, &material.Quality, &material.IsOptional)
		if err != nil {
			return nil, err
		}
		materials = append(materials, material)
	}

	return materials, nil
}

func (r *CraftingRepository) getRecipeRequirements(ctx context.Context, recipeID string) (*models.RecipeRequirements, error) {
	query := `
		SELECT skill_level, station_type, special_tools, prerequisites
		FROM recipe_requirements
		WHERE recipe_id = $1
	`

	var req models.RecipeRequirements
	var specialTools, prerequisites []string

	err := r.db.QueryRow(ctx, query, recipeID).Scan(
		&req.SkillLevel, &req.StationType, &specialTools, &prerequisites,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			// Возвращаем пустые требования по умолчанию
			return &models.RecipeRequirements{}, nil
		}
		return nil, err
	}

	req.SpecialTools = specialTools
	req.Prerequisites = prerequisites

	return &req, nil
}

func (r *CraftingRepository) getRecipeOutput(ctx context.Context, recipeID string) (*models.RecipeOutput, error) {
	query := `
		SELECT item_id, quantity, quality
		FROM recipe_outputs
		WHERE recipe_id = $1
	`

	var output models.RecipeOutput
	err := r.db.QueryRow(ctx, query, recipeID).Scan(&output.ItemID, &output.Quantity, &output.Quality)
	if err != nil {
		return nil, err
	}

	return &output, nil
}

func (r *CraftingRepository) saveUsedMaterials(ctx context.Context, orderID string, materials []models.UsedMaterial) error {
	query := `
		INSERT INTO used_materials (order_id, resource_id, quantity, quality)
		VALUES ($1, $2, $3, $4)
	`

	for _, material := range materials {
		_, err := r.db.Exec(ctx, query, orderID, material.ResourceID, material.Quantity, material.Quality)
		if err != nil {
			return err
		}
	}

	return nil
}

func (r *CraftingRepository) getUsedMaterials(ctx context.Context, orderID string) ([]models.UsedMaterial, error) {
	query := `
		SELECT resource_id, quantity, quality
		FROM used_materials
		WHERE order_id = $1
		ORDER BY resource_id
	`

	rows, err := r.db.Query(ctx, query, orderID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var materials []models.UsedMaterial
	for rows.Next() {
		var material models.UsedMaterial
		err := rows.Scan(&material.ResourceID, &material.Quantity, &material.Quality)
		if err != nil {
			return nil, err
		}
		materials = append(materials, material)
	}

	return materials, nil
}

func (r *CraftingRepository) getCraftingResult(ctx context.Context, orderID string) (*models.CraftingResult, error) {
	query := `
		SELECT item_id, quantity, quality, success
		FROM crafting_results
		WHERE order_id = $1
	`

	var result models.CraftingResult
	err := r.db.QueryRow(ctx, query, orderID).Scan(
		&result.ItemID, &result.Quantity, &result.Quality, &result.Success,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("result not found")
		}
		return nil, err
	}

	return &result, nil
}
