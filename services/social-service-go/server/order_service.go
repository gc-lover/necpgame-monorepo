// Package server Issue: #1509
package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type OrderServiceInterface interface {
	CreatePlayerOrder(ctx context.Context, customerID uuid.UUID, req *models.CreatePlayerOrderRequest) (*models.PlayerOrder, error)
	GetPlayerOrders(ctx context.Context, orderType *models.OrderType, status *models.OrderStatus, limit, offset int) (*models.PlayerOrdersResponse, error)
	GetPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error)
	AcceptPlayerOrder(ctx context.Context, orderID, executorID uuid.UUID) (*models.PlayerOrder, error)
	StartPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error)
	CompletePlayerOrder(ctx context.Context, orderID uuid.UUID, req *models.CompletePlayerOrderRequest) (*models.PlayerOrder, error)
	CancelPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error)
}

type OrderService struct {
	repo   OrderRepositoryInterface
	logger *logrus.Logger
}

func NewOrderService(db *pgxpool.Pool, logger *logrus.Logger) *OrderService {
	return &OrderService{
		repo:   NewOrderRepository(db, logger),
		logger: logger,
	}
}

func (s *OrderService) CreatePlayerOrder(ctx context.Context, customerID uuid.UUID, req *models.CreatePlayerOrderRequest) (*models.PlayerOrder, error) {
	order := &models.PlayerOrder{
		ID:           uuid.New(),
		CustomerID:   customerID,
		OrderType:    req.OrderType,
		Title:        req.Title,
		Description:  req.Description,
		Status:       models.OrderStatusOpen,
		Reward:       req.Reward,
		Requirements: req.Requirements,
		Deadline:     req.Deadline,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	if err := s.repo.Create(ctx, order); err != nil {
		return nil, err
	}

	return s.repo.GetByID(ctx, order.ID)
}

func (s *OrderService) GetPlayerOrders(ctx context.Context, orderType *models.OrderType, status *models.OrderStatus, limit, offset int) (*models.PlayerOrdersResponse, error) {
	orders, err := s.repo.List(ctx, orderType, status, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.Count(ctx, orderType, status)
	if err != nil {
		return nil, err
	}

	return &models.PlayerOrdersResponse{
		Orders: orders,
		Total:  total,
	}, nil
}

func (s *OrderService) GetPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	return s.repo.GetByID(ctx, orderID)
}

func (s *OrderService) AcceptPlayerOrder(ctx context.Context, orderID, executorID uuid.UUID) (*models.PlayerOrder, error) {
	if err := s.repo.AcceptOrder(ctx, orderID, executorID); err != nil {
		return nil, err
	}

	return s.repo.GetByID(ctx, orderID)
}

func (s *OrderService) StartPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	if err := s.repo.StartOrder(ctx, orderID); err != nil {
		return nil, err
	}

	return s.repo.GetByID(ctx, orderID)
}

func (s *OrderService) CompletePlayerOrder(ctx context.Context, orderID uuid.UUID, req *models.CompletePlayerOrderRequest) (*models.PlayerOrder, error) {
	if !req.Success {
		return nil, errors.New("order completion requires success=true")
	}

	if err := s.repo.CompleteOrder(ctx, orderID); err != nil {
		return nil, err
	}

	return s.repo.GetByID(ctx, orderID)
}

func (s *OrderService) CancelPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	if err := s.repo.CancelOrder(ctx, orderID); err != nil {
		return nil, err
	}

	return s.repo.GetByID(ctx, orderID)
}
