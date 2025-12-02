// Issue: #81
package server

import (
	"context"
	"fmt"
	"time"
)

type Order struct {
	ID          string    `json:"id"`
	Title       string    `json:"title"`
	Description string    `json:"description"`
	OrderType   string    `json:"order_type"`
	Status      string    `json:"status"`
	CreatorID   string    `json:"creator_id"`
	ExecutorID  *string   `json:"executor_id,omitempty"`
	RewardEd    int       `json:"reward_ed"`
	CreatedAt   time.Time `json:"created_at"`
	UpdatedAt   time.Time `json:"updated_at"`
}

type OrderService struct {
	repo *OrderRepository
}

func NewOrderService(repo *OrderRepository) *OrderService {
	return &OrderService{
		repo: repo,
	}
}

// ListOrders retrieves orders with optional filtering
func (s *OrderService) ListOrders(ctx context.Context, orderType, status string) ([]*Order, error) {
	return s.repo.ListOrders(ctx, orderType, status)
}

// CreateOrder creates a new player order
func (s *OrderService) CreateOrder(ctx context.Context, title, description, orderType string, rewardEd int) (*Order, error) {
	// Generate order ID
	orderID := fmt.Sprintf("order-%d", time.Now().Unix())

	// TODO: Get creator ID from auth context
	creatorID := "player-001"

	order := &Order{
		ID:          orderID,
		Title:       title,
		Description: description,
		OrderType:   orderType,
		Status:      "pending",
		CreatorID:   creatorID,
		RewardEd:    rewardEd,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	if err := s.repo.CreateOrder(ctx, order); err != nil {
		return nil, fmt.Errorf("failed to create order: %w", err)
	}

	// TODO: Publish event to Event Bus
	// eventBus.Publish("social.orders.created", order)

	return order, nil
}

// GetOrder retrieves a single order by ID
func (s *OrderService) GetOrder(ctx context.Context, orderID string) (*Order, error) {
	return s.repo.GetOrder(ctx, orderID)
}

// AcceptOrder assigns an executor to the order
func (s *OrderService) AcceptOrder(ctx context.Context, orderID, executorID string) error {
	order, err := s.repo.GetOrder(ctx, orderID)
	if err != nil {
		return fmt.Errorf("order not found: %w", err)
	}

	if order.Status != "pending" {
		return fmt.Errorf("order is not pending")
	}

	order.ExecutorID = &executorID
	order.Status = "accepted"
	order.UpdatedAt = time.Now()

	if err := s.repo.UpdateOrder(ctx, order); err != nil {
		return fmt.Errorf("failed to accept order: %w", err)
	}

	// TODO: Publish event to Event Bus
	// eventBus.Publish("social.orders.accepted", order)

	return nil
}

// CompleteOrder marks an order as completed
func (s *OrderService) CompleteOrder(ctx context.Context, orderID string) error {
	order, err := s.repo.GetOrder(ctx, orderID)
	if err != nil {
		return fmt.Errorf("order not found: %w", err)
	}

	if order.Status != "accepted" && order.Status != "in_progress" {
		return fmt.Errorf("order cannot be completed")
	}

	order.Status = "completed"
	order.UpdatedAt = time.Now()

	if err := s.repo.UpdateOrder(ctx, order); err != nil {
		return fmt.Errorf("failed to complete order: %w", err)
	}

	// TODO: Publish event to Event Bus
	// TODO: Process reward payment
	// eventBus.Publish("social.orders.completed", order)

	return nil
}

// CancelOrder cancels an order
func (s *OrderService) CancelOrder(ctx context.Context, orderID string) error {
	order, err := s.repo.GetOrder(ctx, orderID)
	if err != nil {
		return fmt.Errorf("order not found: %w", err)
	}

	if order.Status == "completed" {
		return fmt.Errorf("cannot cancel completed order")
	}

	order.Status = "cancelled"
	order.UpdatedAt = time.Now()

	if err := s.repo.UpdateOrder(ctx, order); err != nil {
		return fmt.Errorf("failed to cancel order: %w", err)
	}

	// TODO: Publish event to Event Bus
	// eventBus.Publish("social.orders.cancelled", order)

	return nil
}

// StartOrder начинает выполнение заказа
func (s *OrderService) StartOrder(ctx context.Context, orderID string) error {
	order, err := s.repo.GetOrder(ctx, orderID)
	if err != nil {
		return fmt.Errorf("order not found: %w", err)
	}

	if order.Status != "accepted" {
		return fmt.Errorf("order must be accepted before starting")
	}

	order.Status = "in_progress"
	order.UpdatedAt = time.Now()

	if err := s.repo.UpdateOrder(ctx, order); err != nil {
		return fmt.Errorf("failed to start order: %w", err)
	}

	// TODO: Publish event to Event Bus
	// eventBus.Publish("social.orders.started", order)

	return nil
}

// ReviewOrder добавляет отзыв о заказе
func (s *OrderService) ReviewOrder(ctx context.Context, orderID string, rating int, comment string) error {
	order, err := s.repo.GetOrder(ctx, orderID)
	if err != nil {
		return fmt.Errorf("order not found: %w", err)
	}

	if order.Status != "completed" {
		return fmt.Errorf("order must be completed before reviewing")
	}

	// TODO: Store review in database
	// review := &Review{
	//     OrderID:  orderID,
	//     Rating:   rating,
	//     Comment:  comment,
	//     Created:  time.Now(),
	// }
	// s.repo.CreateReview(ctx, review)

	// TODO: Publish event to Event Bus
	// eventBus.Publish("social.orders.reviewed", review)

	return nil
}

