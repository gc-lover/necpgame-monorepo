// Issue: #2203 - Order service implementation
package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

// OrderService handles order business logic
type OrderService struct {
	repo         *OrderRepository
	recipeRepo   *RecipeRepository
	stationRepo  *StationRepository
	redis        *redis.Client
	logger       *logrus.Logger
}

// NewOrderService creates new order service
func NewOrderService(repo *OrderRepository, recipeRepo *RecipeRepository, stationRepo *StationRepository, redisClient *redis.Client) OrderServiceInterface {
	return &OrderService{
		repo:        repo,
		recipeRepo:  recipeRepo,
		stationRepo: stationRepo,
		redis:       redisClient,
		logger:      GetLogger(),
	}
}

// GetOrder retrieves order by ID
func (s *OrderService) GetOrder(ctx context.Context, orderID uuid.UUID) (*Order, error) {
	order, err := s.repo.GetByID(ctx, orderID)
	if err != nil {
		return nil, fmt.Errorf("failed to get order: %w", err)
	}

	return order, nil
}

// ListOrders retrieves orders with pagination
func (s *OrderService) ListOrders(ctx context.Context, playerID *uuid.UUID, status *string, limit, offset int) ([]Order, int, error) {
	orders, total, err := s.repo.List(ctx, playerID, status, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list orders: %w", err)
	}

	return orders, total, nil
}

// CreateOrder creates new crafting order
func (s *OrderService) CreateOrder(ctx context.Context, playerID uuid.UUID, recipeID uuid.UUID, stationID *uuid.UUID, qualityModifier float64) (*Order, error) {
	// Validate recipe exists
	_, err := s.recipeRepo.GetByID(ctx, recipeID)
	if err != nil {
		return nil, fmt.Errorf("recipe not found: %w", err)
	}

	// Validate station if provided
	if stationID != nil {
		available, err := s.stationRepo.GetByID(ctx, *stationID)
		if err != nil {
			return nil, fmt.Errorf("station not found: %w", err)
		}
		if !available.IsAvailable {
			return nil, fmt.Errorf("station is not available")
		}
	}

	// Validate quality modifier
	if qualityModifier < 0.1 || qualityModifier > 2.0 {
		return nil, fmt.Errorf("quality modifier must be between 0.1 and 2.0")
	}

	// Create order
	now := time.Now()
	order := &Order{
		ID:              uuid.New(),
		PlayerID:        playerID,
		RecipeID:        recipeID,
		StationID:       stationID,
		Status:          "pending",
		QualityModifier: qualityModifier,
		StationBonus:    0.0, // Will be set when assigned to station
		Progress:        0.0,
		CreatedAt:       now,
	}

	// Calculate initial station bonus if station is assigned
	if stationID != nil {
		if station, err := s.stationRepo.GetByID(ctx, *stationID); err == nil {
			order.StationBonus = station.Efficiency - 1.0 // Efficiency above 1.0 is bonus
		}
	}

	// Save to database
	if err := s.repo.Create(ctx, order); err != nil {
		return nil, fmt.Errorf("failed to create order: %w", err)
	}

	// Queue for async processing if station is available
	if stationID != nil {
		s.queueOrderForProcessing(ctx, order.ID)
	}

	s.logger.WithFields(logrus.Fields{
		"order_id":   order.ID,
		"player_id":  playerID,
		"recipe_id":  recipeID,
		"station_id": stationID,
	}).Info("Order created successfully")

	return order, nil
}

// UpdateOrder updates existing order
func (s *OrderService) UpdateOrder(ctx context.Context, order *Order) error {
	now := time.Now()
	order.UpdatedAt = &now

	if err := s.repo.Update(ctx, order); err != nil {
		return fmt.Errorf("failed to update order: %w", err)
	}

	return nil
}

// CancelOrder cancels pending or active order
func (s *OrderService) CancelOrder(ctx context.Context, orderID uuid.UUID) error {
	order, err := s.repo.GetByID(ctx, orderID)
	if err != nil {
		return fmt.Errorf("order not found: %w", err)
	}

	if order.Status == "completed" || order.Status == "failed" {
		return fmt.Errorf("cannot cancel order with status: %s", order.Status)
	}

	order.Status = "cancelled"
	now := time.Now()
	order.UpdatedAt = &now

	if err := s.repo.Update(ctx, order); err != nil {
		return fmt.Errorf("failed to cancel order: %w", err)
	}

	// TODO: Return materials to player inventory

	s.logger.WithField("order_id", orderID).Info("Order cancelled successfully")

	return nil
}

// StartOrder starts order processing
func (s *OrderService) StartOrder(ctx context.Context, orderID uuid.UUID) error {
	order, err := s.repo.GetByID(ctx, orderID)
	if err != nil {
		return fmt.Errorf("order not found: %w", err)
	}

	if order.Status != "pending" {
		return fmt.Errorf("can only start pending orders")
	}

	now := time.Now()
	order.Status = "active"
	order.StartedAt = &now
	order.UpdatedAt = &now

	if err := s.repo.Update(ctx, order); err != nil {
		return fmt.Errorf("failed to start order: %w", err)
	}

	s.logger.WithField("order_id", orderID).Info("Order started successfully")

	return nil
}

// CompleteOrder marks order as completed
func (s *OrderService) CompleteOrder(ctx context.Context, orderID uuid.UUID) error {
	order, err := s.repo.GetByID(ctx, orderID)
	if err != nil {
		return fmt.Errorf("order not found: %w", err)
	}

	if order.Status != "active" {
		return fmt.Errorf("can only complete active orders")
	}

	now := time.Now()
	order.Status = "completed"
	order.Progress = 1.0
	order.CompletedAt = &now
	order.UpdatedAt = &now

	if err := s.repo.Update(ctx, order); err != nil {
		return fmt.Errorf("failed to complete order: %w", err)
	}

	// TODO: Add crafted item to player inventory

	s.logger.WithField("order_id", orderID).Info("Order completed successfully")

	return nil
}

// PERFORMANCE: Queue order for async processing
func (s *OrderService) queueOrderForProcessing(ctx context.Context, orderID uuid.UUID) {
	// TODO: Implement Redis queue for async order processing
	// This would allow background processing of crafting orders
	queueKey := "crafting:queue:orders"
	s.redis.RPush(ctx, queueKey, orderID.String())
}
