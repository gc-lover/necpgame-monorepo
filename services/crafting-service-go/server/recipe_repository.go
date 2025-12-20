// Package server Issue: #2203 - Recipe repository implementation
package server

import (
	"context"
	"encoding/json"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

// RecipeRepository handles recipe database operations
type RecipeRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

// NewRecipeRepository creates new recipe repository
func NewRecipeRepository(db *pgxpool.Pool) *RecipeRepository {
	return &RecipeRepository{
		db:     db,
		logger: GetLogger(),
	}
}

// GetByID retrieves recipe by ID
func (r *RecipeRepository) GetByID(ctx context.Context, id uuid.UUID) (*Recipe, error) {
	query := `
		SELECT id, name, description, category, tier, quality, duration, success_rate,
			   materials, requirements, created_at, updated_at
		FROM crafting_recipes
		WHERE id = $1
	`

	var recipe Recipe
	var materialsJSON, requirementsJSON []byte

	err := r.db.QueryRow(ctx, query, id).Scan(
		&recipe.ID, &recipe.Name, &recipe.Description, &recipe.Category,
		&recipe.Tier, &recipe.Quality, &recipe.Duration, &recipe.SuccessRate,
		&materialsJSON, &requirementsJSON, &recipe.CreatedAt, &recipe.UpdatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to get recipe: %w", err)
	}

	// Unmarshal JSON fields
	if err := json.Unmarshal(materialsJSON, &recipe.Materials); err != nil {
		r.logger.WithError(err).Warn("Failed to unmarshal recipe materials")
	}
	if err := json.Unmarshal(requirementsJSON, &recipe.Requirements); err != nil {
		r.logger.WithError(err).Warn("Failed to unmarshal recipe requirements")
	}

	return &recipe, nil
}

// List retrieves recipes with pagination and filtering
func (r *RecipeRepository) List(ctx context.Context, category *string, tier *int, limit, offset int) ([]Recipe, int, error) {
	// Build query with optional filters
	baseQuery := `
		SELECT id, name, description, category, tier, quality, duration, success_rate,
			   materials, requirements, created_at, updated_at
		FROM crafting_recipes
		WHERE 1=1
	`
	var args []interface{}

	if category != nil {
		baseQuery += fmt.Sprintf(" AND category = $%d", len(args)+1)
		args = append(args, *category)
	}

	if tier != nil {
		baseQuery += fmt.Sprintf(" AND tier = $%d", len(args)+1)
		args = append(args, *tier)
	}

	query := baseQuery + fmt.Sprintf(" ORDER BY created_at DESC LIMIT $%d OFFSET $%d", len(args)+1, len(args)+2)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list recipes: %w", err)
	}
	defer rows.Close()

	var recipes []Recipe
	for rows.Next() {
		var recipe Recipe
		var materialsJSON, requirementsJSON []byte

		err := rows.Scan(
			&recipe.ID, &recipe.Name, &recipe.Description, &recipe.Category,
			&recipe.Tier, &recipe.Quality, &recipe.Duration, &recipe.SuccessRate,
			&materialsJSON, &requirementsJSON, &recipe.CreatedAt, &recipe.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan recipe: %w", err)
		}

		// Unmarshal JSON fields (optional for list)
		json.Unmarshal(materialsJSON, &recipe.Materials)
		json.Unmarshal(requirementsJSON, &recipe.Requirements)

		recipes = append(recipes, recipe)
	}

	// Get total count
	countQuery := "SELECT COUNT(*) FROM crafting_recipes WHERE 1=1"
	var countArgs []interface{}

	if category != nil {
		countQuery += " AND category = $1"
		countArgs = append(countArgs, *category)
	}

	if tier != nil {
		countQuery += " AND tier = $2"
		countArgs = append(countArgs, *tier)
	}

	var total int
	err = r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	return recipes, total, nil
}

// Create inserts new recipe
func (r *RecipeRepository) Create(ctx context.Context, recipe *Recipe) error {
	materialsJSON, _ := json.Marshal(recipe.Materials)
	requirementsJSON, _ := json.Marshal(recipe.Requirements)

	query := `
		INSERT INTO crafting_recipes (
			id, name, description, category, tier, quality, duration, success_rate,
			materials, requirements, created_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	_, err := r.db.Exec(ctx, query,
		recipe.ID, recipe.Name, recipe.Description, recipe.Category,
		recipe.Tier, recipe.Quality, recipe.Duration, recipe.SuccessRate,
		materialsJSON, requirementsJSON, recipe.CreatedAt,
	)
	if err != nil {
		return fmt.Errorf("failed to create recipe: %w", err)
	}

	return nil
}

// Update modifies existing recipe
func (r *RecipeRepository) Update(ctx context.Context, recipe *Recipe) error {
	materialsJSON, _ := json.Marshal(recipe.Materials)
	requirementsJSON, _ := json.Marshal(recipe.Requirements)

	query := `
		UPDATE crafting_recipes SET
			name = $2, description = $3, category = $4, tier = $5, quality = $6,
			duration = $7, success_rate = $8, materials = $9, requirements = $10,
			updated_at = $11
		WHERE id = $1
	`

	_, err := r.db.Exec(ctx, query,
		recipe.ID, recipe.Name, recipe.Description, recipe.Category,
		recipe.Tier, recipe.Quality, recipe.Duration, recipe.SuccessRate,
		materialsJSON, requirementsJSON, recipe.UpdatedAt,
	)
	if err != nil {
		return fmt.Errorf("failed to update recipe: %w", err)
	}

	return nil
}

// Delete removes recipe
func (r *RecipeRepository) Delete(ctx context.Context, id uuid.UUID) error {
	query := "DELETE FROM crafting_recipes WHERE id = $1"

	_, err := r.db.Exec(ctx, query, id)
	if err != nil {
		return fmt.Errorf("failed to delete recipe: %w", err)
	}

	return nil
}
