// Issue: #141888033
package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
)

func (s *SocialService) CreatePlayerOrder(ctx context.Context, customerID uuid.UUID, req *models.CreatePlayerOrderRequest) (*models.PlayerOrder, error) {
	order := &models.PlayerOrder{
		ID:          uuid.New(),
		CustomerID:  customerID,
		OrderType:   req.OrderType,
		Title:       req.Title,
		Description: req.Description,
		Status:      models.OrderStatusOpen,
		Reward:      req.Reward,
		Requirements: req.Requirements,
		Deadline:    req.Deadline,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	if err := s.orderRepo.Create(ctx, order); err != nil {
		return nil, err
	}

	return s.orderRepo.GetByID(ctx, order.ID)
}

func (s *SocialService) GetPlayerOrders(ctx context.Context, orderType *models.OrderType, status *models.OrderStatus, limit, offset int) (*models.PlayerOrdersResponse, error) {
	orders, err := s.orderRepo.List(ctx, orderType, status, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.orderRepo.Count(ctx, orderType, status)
	if err != nil {
		return nil, err
	}

	return &models.PlayerOrdersResponse{
		Orders: orders,
		Total:  total,
	}, nil
}

func (s *SocialService) GetPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	return s.orderRepo.GetByID(ctx, orderID)
}

func (s *SocialService) AcceptPlayerOrder(ctx context.Context, orderID, executorID uuid.UUID) (*models.PlayerOrder, error) {
	if err := s.orderRepo.AcceptOrder(ctx, orderID, executorID); err != nil {
		return nil, err
	}

	return s.orderRepo.GetByID(ctx, orderID)
}

func (s *SocialService) StartPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	if err := s.orderRepo.StartOrder(ctx, orderID); err != nil {
		return nil, err
	}

	return s.orderRepo.GetByID(ctx, orderID)
}

func (s *SocialService) CompletePlayerOrder(ctx context.Context, orderID uuid.UUID, req *models.CompletePlayerOrderRequest) (*models.PlayerOrder, error) {
	if !req.Success {
		return nil, errors.New("order completion requires success=true")
	}

	if err := s.orderRepo.CompleteOrder(ctx, orderID); err != nil {
		return nil, err
	}

	return s.orderRepo.GetByID(ctx, orderID)
}

func (s *SocialService) CancelPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	if err := s.orderRepo.CancelOrder(ctx, orderID); err != nil {
		return nil, err
	}

	return s.orderRepo.GetByID(ctx, orderID)
}

func (s *SocialService) ReviewPlayerOrder(ctx context.Context, orderID, reviewerID uuid.UUID, req *models.ReviewPlayerOrderRequest) (*models.PlayerOrderReview, error) {
	order, err := s.orderRepo.GetByID(ctx, orderID)
	if err != nil {
		return nil, err
	}
	if order == nil {
		return nil, errors.New("order not found")
	}

	if order.Status != models.OrderStatusCompleted {
		return nil, errors.New("can only review completed orders")
	}

	review := &models.PlayerOrderReview{
		ID:         uuid.New(),
		OrderID:    orderID,
		ReviewerID: reviewerID,
		ExecutorID: req.ExecutorID,
		Rating:     req.Rating,
		Comment:    req.Comment,
		CreatedAt:  time.Now(),
	}

	if err := s.orderRepo.CreateReview(ctx, review); err != nil {
		return nil, err
	}

	return review, nil
}

