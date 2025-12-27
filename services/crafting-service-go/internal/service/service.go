// Issue: #2229
package service

import (
	"context"
	"fmt"
	"time"

	"go.uber.org/zap"

	"crafting-service-go/internal/repository"
	"crafting-service-go/internal/metrics"
)

// CraftingService handles crafting business logic
type CraftingService struct {
	repo     *repository.CraftingRepository
	metrics  *metrics.Collector
	logger   *zap.SugaredLogger
}

// NewCraftingService creates a new crafting service
func NewCraftingService(repo *repository.CraftingRepository, metrics *metrics.Collector, logger *zap.SugaredLogger) *CraftingService {
	return &CraftingService{
		repo:    repo,
		metrics: metrics,
		logger:  logger,
	}
}

// GetRecipesByCategory retrieves recipes by category
func (s *CraftingService) GetRecipesByCategory(ctx context.Context, category string, tier *int, quality *string, limit int, offset int) ([]*repository.Recipe, error) {
	recipes, err := s.repo.GetRecipesByCategory(ctx, category, tier, quality, limit, offset)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get recipes by category: %w", err)
	}

	return recipes, nil
}

// GetRecipe retrieves a single recipe
func (s *CraftingService) GetRecipe(ctx context.Context, recipeID string) (*repository.Recipe, error) {
	recipe, err := s.repo.GetRecipe(ctx, recipeID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get recipe: %w", err)
	}

	return recipe, nil
}

// CreateRecipe creates a new recipe
func (s *CraftingService) CreateRecipe(ctx context.Context, name, description, category string, tier int, quality string, materials map[string]int, result map[string]interface{}, skillReq, timeReq int) (*repository.Recipe, error) {
	recipeID := fmt.Sprintf("recipe_%d", time.Now().UnixNano())

	recipe := &repository.Recipe{
		ID:          recipeID,
		Name:        name,
		Description: description,
		Category:    category,
		Tier:        tier,
		Quality:     quality,
		Materials:   materials,
		Result:      result,
		SkillReq:    skillReq,
		TimeReq:     timeReq,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	if err := s.repo.CreateRecipe(ctx, recipe); err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to create recipe: %w", err)
	}

	s.metrics.IncrementRecipesCreated()
	s.logger.Infof("Created recipe: %s", recipeID)

	return recipe, nil
}

// CreateCraftingOrder creates a new crafting order
func (s *CraftingService) CreateCraftingOrder(ctx context.Context, playerID, recipeID, stationID string) (*repository.CraftingOrder, error) {
	orderID := fmt.Sprintf("order_%d", time.Now().UnixNano())

	order := &repository.CraftingOrder{
		ID:        orderID,
		PlayerID:  playerID,
		RecipeID:  recipeID,
		StationID: stationID,
		Status:    "queued",
		Progress:  0.0,
		StartTime: time.Now(),
		CreatedAt: time.Now(),
	}

	if err := s.repo.CreateCraftingOrder(ctx, order); err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to create crafting order: %w", err)
	}

	s.metrics.IncrementOrdersCreated()
	s.logger.Infof("Created crafting order: %s", orderID)

	return order, nil
}

// GetCraftingOrders retrieves crafting orders for a player
func (s *CraftingService) GetCraftingOrders(ctx context.Context, playerID string, limit int, offset int) ([]*repository.CraftingOrder, error) {
	orders, err := s.repo.GetCraftingOrders(ctx, playerID, limit, offset)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get crafting orders: %w", err)
	}

	return orders, nil
}

// GetCraftingStations retrieves available crafting stations
func (s *CraftingService) GetCraftingStations(ctx context.Context, stationType *string, limit int, offset int) ([]*repository.CraftingStation, error) {
	stations, err := s.repo.GetCraftingStations(ctx, stationType, limit, offset)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get crafting stations: %w", err)
	}

	return stations, nil
}

// BookCraftingStation books a crafting station
func (s *CraftingService) BookCraftingStation(ctx context.Context, stationID, playerID string) error {
	if err := s.repo.BookCraftingStation(ctx, stationID, playerID); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to book crafting station: %w", err)
	}

	s.metrics.IncrementStationsBooked()
	s.logger.Infof("Booked crafting station: %s for player: %s", stationID, playerID)

	return nil
}

// UpdateCraftingOrder updates an order status
func (s *CraftingService) UpdateCraftingOrder(ctx context.Context, orderID, status string, progress float64, quality string) error {
	// Implementation would update the order in database
	s.logger.Infof("Updated crafting order: %s status: %s progress: %.2f", orderID, status, progress)
	return nil
}

// CancelCraftingOrder cancels an order
func (s *CraftingService) CancelCraftingOrder(ctx context.Context, orderID string) error {
	// Implementation would cancel the order in database
	s.metrics.IncrementOrdersCancelled()
	s.logger.Infof("Cancelled crafting order: %s", orderID)
	return nil
}
