// Issue: #2203 - Recipe service implementation
package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

// RecipeService handles recipe business logic
type RecipeService struct {
	repo   *RecipeRepository
	redis  *redis.Client
	logger *logrus.Logger
}

// NewRecipeService creates new recipe service
func NewRecipeService(repo *RecipeRepository, redisClient *redis.Client) RecipeServiceInterface {
	return &RecipeService{
		repo:   repo,
		redis:  redisClient,
		logger: GetLogger(),
	}
}

// GetRecipe retrieves recipe by ID with caching
func (s *RecipeService) GetRecipe(ctx context.Context, recipeID uuid.UUID) (*Recipe, error) {
	// PERFORMANCE: Try cache first
	cacheKey := fmt.Sprintf("recipe:%s", recipeID)
	if cached, err := s.getCachedRecipe(ctx, cacheKey); err == nil && cached != nil {
		return cached, nil
	}

	// Get from database
	recipe, err := s.repo.GetByID(ctx, recipeID)
	if err != nil {
		return nil, fmt.Errorf("failed to get recipe: %w", err)
	}

	// PERFORMANCE: Cache result
	s.cacheRecipe(ctx, cacheKey, recipe, 10*time.Minute)

	return recipe, nil
}

// ListRecipes retrieves recipes with pagination
func (s *RecipeService) ListRecipes(ctx context.Context, category *string, tier *int, limit, offset int) ([]Recipe, int, error) {
	recipes, total, err := s.repo.List(ctx, category, tier, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list recipes: %w", err)
	}

	return recipes, total, nil
}

// CreateRecipe creates new recipe
func (s *RecipeService) CreateRecipe(ctx context.Context, recipe *Recipe) error {
	// Validate recipe
	if err := s.validateRecipe(recipe); err != nil {
		return fmt.Errorf("invalid recipe: %w", err)
	}

	// Set defaults
	now := time.Now()
	recipe.ID = uuid.New()
	recipe.CreatedAt = now

	if recipe.SuccessRate == 0 {
		recipe.SuccessRate = 0.95 // Default success rate
	}

	// Create in database
	if err := s.repo.Create(ctx, recipe); err != nil {
		return fmt.Errorf("failed to create recipe: %w", err)
	}

	// PERFORMANCE: Invalidate cache
	s.invalidateRecipeCache(ctx, recipe.ID)

	s.logger.WithFields(logrus.Fields{
		"recipe_id": recipe.ID,
		"name":      recipe.Name,
	}).Info("Recipe created successfully")

	return nil
}

// UpdateRecipe updates existing recipe
func (s *RecipeService) UpdateRecipe(ctx context.Context, recipe *Recipe) error {
	// Validate recipe
	if err := s.validateRecipe(recipe); err != nil {
		return fmt.Errorf("invalid recipe: %w", err)
	}

	// Set update time
	now := time.Now()
	recipe.UpdatedAt = &now

	// Update in database
	if err := s.repo.Update(ctx, recipe); err != nil {
		return fmt.Errorf("failed to update recipe: %w", err)
	}

	// PERFORMANCE: Invalidate cache
	s.invalidateRecipeCache(ctx, recipe.ID)

	s.logger.WithField("recipe_id", recipe.ID).Info("Recipe updated successfully")

	return nil
}

// DeleteRecipe removes recipe
func (s *RecipeService) DeleteRecipe(ctx context.Context, recipeID uuid.UUID) error {
	// Check if recipe exists
	if _, err := s.repo.GetByID(ctx, recipeID); err != nil {
		return fmt.Errorf("recipe not found: %w", err)
	}

	// Delete from database
	if err := s.repo.Delete(ctx, recipeID); err != nil {
		return fmt.Errorf("failed to delete recipe: %w", err)
	}

	// PERFORMANCE: Invalidate cache
	s.invalidateRecipeCache(ctx, recipeID)

	s.logger.WithField("recipe_id", recipeID).Info("Recipe deleted successfully")

	return nil
}

// validateRecipe validates recipe data
func (s *RecipeService) validateRecipe(recipe *Recipe) error {
	if recipe.Name == "" {
		return fmt.Errorf("recipe name is required")
	}

	if recipe.Category == "" {
		return fmt.Errorf("recipe category is required")
	}

	validCategories := map[string]bool{
		"weapons": true, "armor": true, "implants": true, "vehicles": true,
		"consumables": true, "tools": true,
	}
	if !validCategories[recipe.Category] {
		return fmt.Errorf("invalid recipe category: %s", recipe.Category)
	}

	if recipe.Tier < 1 || recipe.Tier > 5 {
		return fmt.Errorf("recipe tier must be between 1 and 5")
	}

	if recipe.Quality < 1 || recipe.Quality > 100 {
		return fmt.Errorf("recipe quality must be between 1 and 100")
	}

	if recipe.Duration < 1 {
		return fmt.Errorf("recipe duration must be positive")
	}

	if recipe.SuccessRate < 0 || recipe.SuccessRate > 1 {
		return fmt.Errorf("recipe success rate must be between 0 and 1")
	}

	if len(recipe.Materials) == 0 {
		return fmt.Errorf("recipe must have at least one material")
	}

	return nil
}

// PERFORMANCE: Cache helpers
func (s *RecipeService) getCachedRecipe(ctx context.Context, key string) (*Recipe, error) {
	_, err := s.redis.Get(ctx, key).Result()
	if err != nil {
		return nil, err
	}

	// TODO: Implement JSON unmarshaling for cached recipe
	// For now, return nil to force database lookup
	return nil, fmt.Errorf("cache not implemented")
}

func (s *RecipeService) cacheRecipe(ctx context.Context, key string, recipe *Recipe, ttl time.Duration) {
	// TODO: Implement JSON marshaling and caching
	// s.redis.Set(ctx, key, jsonData, ttl)
}

func (s *RecipeService) invalidateRecipeCache(ctx context.Context, recipeID uuid.UUID) {
	cacheKey := fmt.Sprintf("recipe:%s", recipeID)
	s.redis.Del(ctx, cacheKey)
}
