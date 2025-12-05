// Issue: #1509 - Order handlers tests
package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

// TestOrderHandlers_CreatePlayerOrder tests order creation
func TestOrderHandlers_CreatePlayerOrder(t *testing.T) {
	mockService := new(mockOrderService)
	handlers := NewOrderHandlers(mockService, GetLogger())

	userID := uuid.New()
	deadline := time.Now().Add(24 * time.Hour)
	req := models.CreatePlayerOrderRequest{
		OrderType:   models.OrderTypeCombat,
		Title:       "Test Order",
		Description: "Test Description",
		Reward:      map[string]interface{}{"currency": 1000},
		Requirements: map[string]interface{}{"level": 10},
		Deadline:    &deadline,
	}

	expectedOrder := &models.PlayerOrder{
		ID:           uuid.New(),
		CustomerID:  userID,
		OrderType:   req.OrderType,
		Title:       req.Title,
		Description: req.Description,
		Status:      models.OrderStatusOpen,
		Reward:      req.Reward,
		Requirements: req.Requirements,
		Deadline:    &deadline,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.On("CreatePlayerOrder", mock.Anything, userID, mock.MatchedBy(func(r *models.CreatePlayerOrderRequest) bool {
		return r.OrderType == req.OrderType && r.Title == req.Title && r.Description == req.Description
	})).Return(expectedOrder, nil)

	body, _ := json.Marshal(req)
	httpReq := httptest.NewRequest("POST", "/api/v1/social/orders/create", bytes.NewReader(body))
	// Use same key as handlers (string "user_id" for compatibility)
	httpReq = httpReq.WithContext(context.WithValue(httpReq.Context(), "user_id", userID.String()))
	w := httptest.NewRecorder()

	handlers.CreatePlayerOrder(w, httpReq)

	assert.Equal(t, http.StatusCreated, w.Code)
	mockService.AssertExpectations(t)
}

// TestOrderHandlers_GetPlayerOrders tests order listing
func TestOrderHandlers_GetPlayerOrders(t *testing.T) {
	mockService := new(mockOrderService)
	handlers := NewOrderHandlers(mockService, GetLogger())

	expectedResponse := &models.PlayerOrdersResponse{
		Orders: []models.PlayerOrder{
			{
				ID:          uuid.New(),
				OrderType:  models.OrderTypeCombat,
				Title:      "Test Order",
				Status:     models.OrderStatusOpen,
				CreatedAt:  time.Now(),
				UpdatedAt:  time.Now(),
			},
		},
		Total: 1,
	}

	mockService.On("GetPlayerOrders", mock.Anything, (*models.OrderType)(nil), (*models.OrderStatus)(nil), 50, 0).Return(expectedResponse, nil)

	httpReq := httptest.NewRequest("GET", "/api/v1/social/orders", nil)
	w := httptest.NewRecorder()

	handlers.GetPlayerOrders(w, httpReq)

	assert.Equal(t, http.StatusOK, w.Code)
	mockService.AssertExpectations(t)
}

// TestOrderHandlers_GetPlayerOrder tests single order retrieval
func TestOrderHandlers_GetPlayerOrder(t *testing.T) {
	mockService := new(mockOrderService)
	handlers := NewOrderHandlers(mockService, GetLogger())

	orderID := uuid.New()
	expectedOrder := &models.PlayerOrder{
		ID:          orderID,
		OrderType:   models.OrderTypeCombat,
		Title:       "Test Order",
		Status:      models.OrderStatusOpen,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.On("GetPlayerOrder", mock.Anything, orderID).Return(expectedOrder, nil)

	// Use chi router to properly extract URL params
	router := chi.NewRouter()
	router.Get("/{orderId}", handlers.GetPlayerOrder)

	httpReq := httptest.NewRequest("GET", "/"+orderID.String(), nil)
	w := httptest.NewRecorder()

	router.ServeHTTP(w, httpReq)

	assert.Equal(t, http.StatusOK, w.Code)
	mockService.AssertExpectations(t)
}

// mockOrderService is a mock for OrderServiceInterface
type mockOrderService struct {
	mock.Mock
}

func (m *mockOrderService) CreatePlayerOrder(ctx context.Context, customerID uuid.UUID, req *models.CreatePlayerOrderRequest) (*models.PlayerOrder, error) {
	args := m.Called(ctx, customerID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerOrder), args.Error(1)
}

func (m *mockOrderService) GetPlayerOrders(ctx context.Context, orderType *models.OrderType, status *models.OrderStatus, limit, offset int) (*models.PlayerOrdersResponse, error) {
	args := m.Called(ctx, orderType, status, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerOrdersResponse), args.Error(1)
}

func (m *mockOrderService) GetPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	args := m.Called(ctx, orderID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerOrder), args.Error(1)
}

func (m *mockOrderService) AcceptPlayerOrder(ctx context.Context, orderID, executorID uuid.UUID) (*models.PlayerOrder, error) {
	args := m.Called(ctx, orderID, executorID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerOrder), args.Error(1)
}

func (m *mockOrderService) StartPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	args := m.Called(ctx, orderID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerOrder), args.Error(1)
}

func (m *mockOrderService) CompletePlayerOrder(ctx context.Context, orderID uuid.UUID, req *models.CompletePlayerOrderRequest) (*models.PlayerOrder, error) {
	args := m.Called(ctx, orderID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerOrder), args.Error(1)
}

func (m *mockOrderService) CancelPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	args := m.Called(ctx, orderID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerOrder), args.Error(1)
}

