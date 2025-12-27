// Issue: #2229
package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/lib/pq"
	"go.uber.org/zap"
)

// Recipe represents a crafting recipe
type Recipe struct {
	ID          string                 `json:"id" db:"id"`
	Name        string                 `json:"name" db:"name"`
	Description string                 `json:"description" db:"description"`
	Category    string                 `json:"category" db:"category"`
	Tier        int                    `json:"tier" db:"tier"`
	Quality     string                 `json:"quality" db:"quality"`
	Materials   map[string]int         `json:"materials" db:"materials"`
	Result      map[string]interface{} `json:"result" db:"result"`
	SkillReq    int                    `json:"skill_req" db:"skill_req"`
	TimeReq     int                    `json:"time_req" db:"time_req"`
	CreatedAt   time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time              `json:"updated_at" db:"updated_at"`
}

// CraftingOrder represents a crafting order
type CraftingOrder struct {
	ID          string    `json:"id" db:"id"`
	PlayerID    string    `json:"player_id" db:"player_id"`
	RecipeID    string    `json:"recipe_id" db:"recipe_id"`
	StationID   string    `json:"station_id" db:"station_id"`
	Status      string    `json:"status" db:"status"`
	Progress    float64   `json:"progress" db:"progress"`
	StartTime   time.Time `json:"start_time" db:"start_time"`
	EndTime     *time.Time `json:"end_time" db:"end_time"`
	Quality     string    `json:"quality" db:"quality"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
}

// CraftingStation represents a crafting station
type CraftingStation struct {
	ID       string `json:"id" db:"id"`
	Type     string `json:"type" db:"type"`
	Location string `json:"location" db:"location"`
	Status   string `json:"status" db:"status"`
	BookedBy *string `json:"booked_by" db:"booked_by"`
}

// CraftingRepository handles database operations
type CraftingRepository struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.SugaredLogger
}

// NewConnection creates a new database connection
func NewConnection(databaseURL string) (*sql.DB, error) {
	db, err := sql.Open("postgres", databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to open database: %w", err)
	}

	// Test connection
	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	// Configure connection pool
	db.SetMaxOpenConns(50)
	db.SetMaxIdleConns(10)
	db.SetConnMaxLifetime(time.Hour)

	return db, nil
}

// NewRedisClient creates a new Redis client
func NewRedisClient(redisURL string) (*redis.Client, error) {
	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	client := redis.NewClient(opt)

	// Test connection
	ctx := context.Background()
	if err := client.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to ping Redis: %w", err)
	}

	return client, nil
}

// NewCraftingRepository creates a new crafting repository
func NewCraftingRepository(db *sql.DB, redis *redis.Client, logger *zap.SugaredLogger) *CraftingRepository {
	return &CraftingRepository{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// GetRecipesByCategory retrieves recipes by category
func (r *CraftingRepository) GetRecipesByCategory(ctx context.Context, category string, tier *int, quality *string, limit int, offset int) ([]*Recipe, error) {
	query := `
		SELECT id, name, description, category, tier, quality, materials, result, skill_req, time_req, created_at, updated_at
		FROM economy.crafting_recipes
		WHERE ($1 = '' OR category = $1)
		AND ($2::int IS NULL OR tier = $2)
		AND ($3 = '' OR quality = $3)
		ORDER BY tier ASC, name ASC
		LIMIT $4 OFFSET $5
	`

	rows, err := r.db.QueryContext(ctx, query, category, tier, quality, limit, offset)
	if err != nil {
		r.logger.Errorf("Failed to get recipes by category: %v", err)
		return nil, fmt.Errorf("failed to get recipes: %w", err)
	}
	defer rows.Close()

	var recipes []*Recipe
	for rows.Next() {
		var recipe Recipe
		var materialsJSON, resultJSON []byte

		err := rows.Scan(
			&recipe.ID, &recipe.Name, &recipe.Description, &recipe.Category,
			&recipe.Tier, &recipe.Quality, &materialsJSON, &resultJSON,
			&recipe.SkillReq, &recipe.TimeReq, &recipe.CreatedAt, &recipe.UpdatedAt,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan recipe: %v", err)
			continue
		}

		// Parse JSON fields
		json.Unmarshal(materialsJSON, &recipe.Materials)
		json.Unmarshal(resultJSON, &recipe.Result)

		recipes = append(recipes, &recipe)
	}

	return recipes, nil
}

// GetRecipe retrieves a single recipe by ID
func (r *CraftingRepository) GetRecipe(ctx context.Context, recipeID string) (*Recipe, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("crafting:recipe:%s", recipeID)
	cached, err := r.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var recipe Recipe
		if err := json.Unmarshal([]byte(cached), &recipe); err == nil {
			return &recipe, nil
		}
	}

	// Fallback to database
	query := `
		SELECT id, name, description, category, tier, quality, materials, result, skill_req, time_req, created_at, updated_at
		FROM economy.crafting_recipes
		WHERE id = $1
	`

	var recipe Recipe
	var materialsJSON, resultJSON []byte

	err = r.db.QueryRowContext(ctx, query, recipeID).Scan(
		&recipe.ID, &recipe.Name, &recipe.Description, &recipe.Category,
		&recipe.Tier, &recipe.Quality, &materialsJSON, &resultJSON,
		&recipe.SkillReq, &recipe.TimeReq, &recipe.CreatedAt, &recipe.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("recipe not found")
		}
		r.logger.Errorf("Failed to get recipe: %v", err)
		return nil, fmt.Errorf("failed to get recipe: %w", err)
	}

	// Parse JSON fields
	json.Unmarshal(materialsJSON, &recipe.Materials)
	json.Unmarshal(resultJSON, &recipe.Result)

	// Cache result
	recipeJSON, _ := json.Marshal(recipe)
	r.redis.Set(ctx, cacheKey, recipeJSON, 30*time.Minute)

	return &recipe, nil
}

// CreateRecipe creates a new recipe
func (r *CraftingRepository) CreateRecipe(ctx context.Context, recipe *Recipe) error {
	materialsJSON, _ := json.Marshal(recipe.Materials)
	resultJSON, _ := json.Marshal(recipe.Result)

	query := `
		INSERT INTO economy.crafting_recipes (id, name, description, category, tier, quality, materials, result, skill_req, time_req, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
	`

	_, err := r.db.ExecContext(ctx, query,
		recipe.ID, recipe.Name, recipe.Description, recipe.Category,
		recipe.Tier, recipe.Quality, materialsJSON, resultJSON,
		recipe.SkillReq, recipe.TimeReq, recipe.CreatedAt, recipe.UpdatedAt)

	if err != nil {
		r.logger.Errorf("Failed to create recipe: %v", err)
		return fmt.Errorf("failed to create recipe: %w", err)
	}

	return nil
}

// CreateCraftingOrder creates a new crafting order
func (r *CraftingRepository) CreateCraftingOrder(ctx context.Context, order *CraftingOrder) error {
	query := `
		INSERT INTO economy.crafting_orders (id, player_id, recipe_id, station_id, status, progress, start_time, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.ExecContext(ctx, query,
		order.ID, order.PlayerID, order.RecipeID, order.StationID,
		order.Status, order.Progress, order.StartTime, order.CreatedAt)

	if err != nil {
		r.logger.Errorf("Failed to create crafting order: %v", err)
		return fmt.Errorf("failed to create crafting order: %w", err)
	}

	return nil
}

// GetCraftingOrders retrieves crafting orders for a player
func (r *CraftingRepository) GetCraftingOrders(ctx context.Context, playerID string, limit int, offset int) ([]*CraftingOrder, error) {
	query := `
		SELECT id, player_id, recipe_id, station_id, status, progress, start_time, end_time, quality, created_at
		FROM economy.crafting_orders
		WHERE player_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.QueryContext(ctx, query, playerID, limit, offset)
	if err != nil {
		r.logger.Errorf("Failed to get crafting orders: %v", err)
		return nil, fmt.Errorf("failed to get crafting orders: %w", err)
	}
	defer rows.Close()

	var orders []*CraftingOrder
	for rows.Next() {
		var order CraftingOrder
		var endTime pq.NullTime

		err := rows.Scan(
			&order.ID, &order.PlayerID, &order.RecipeID, &order.StationID,
			&order.Status, &order.Progress, &order.StartTime, &endTime,
			&order.Quality, &order.CreatedAt,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan crafting order: %v", err)
			continue
		}

		if endTime.Valid {
			order.EndTime = &endTime.Time
		}

		orders = append(orders, &order)
	}

	return orders, nil
}

// GetCraftingStations retrieves available crafting stations
func (r *CraftingRepository) GetCraftingStations(ctx context.Context, stationType *string, limit int, offset int) ([]*CraftingStation, error) {
	query := `
		SELECT id, type, location, status, booked_by
		FROM economy.crafting_stations
		WHERE ($1 = '' OR type = $1)
		ORDER BY type ASC, location ASC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.QueryContext(ctx, query, stationType, limit, offset)
	if err != nil {
		r.logger.Errorf("Failed to get crafting stations: %v", err)
		return nil, fmt.Errorf("failed to get crafting stations: %w", err)
	}
	defer rows.Close()

	var stations []*CraftingStation
	for rows.Next() {
		var station CraftingStation
		var bookedBy sql.NullString

		err := rows.Scan(
			&station.ID, &station.Type, &station.Location,
			&station.Status, &bookedBy,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan crafting station: %v", err)
			continue
		}

		if bookedBy.Valid {
			station.BookedBy = &bookedBy.String
		}

		stations = append(stations, &station)
	}

	return stations, nil
}

// BookCraftingStation books a crafting station for a player
func (r *CraftingRepository) BookCraftingStation(ctx context.Context, stationID, playerID string) error {
	query := `
		UPDATE economy.crafting_stations
		SET status = 'booked', booked_by = $2
		WHERE id = $1 AND status = 'available'
	`

	result, err := r.db.ExecContext(ctx, query, stationID, playerID)
	if err != nil {
		r.logger.Errorf("Failed to book crafting station: %v", err)
		return fmt.Errorf("failed to book crafting station: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("station not available or not found")
	}

	return nil
}
