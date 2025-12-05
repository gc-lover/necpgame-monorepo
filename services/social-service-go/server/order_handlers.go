// Issue: #1509
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

// OrderHandlers handles order-related HTTP requests
type OrderHandlers struct {
	service OrderServiceInterface
	logger  *logrus.Logger
}

// NewOrderHandlers creates new order handlers
func NewOrderHandlers(service OrderServiceInterface, logger *logrus.Logger) *OrderHandlers {
	return &OrderHandlers{
		service: service,
		logger:  logger,
	}
}

// CreatePlayerOrder handles POST /social/orders/create
func (h *OrderHandlers) CreatePlayerOrder(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	userID := r.Context().Value("user_id")
	if userID == nil {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	customerID, err := uuid.Parse(userID.(string))
	if err != nil {
		http.Error(w, "Invalid user ID", http.StatusBadRequest)
		return
	}

	var req models.CreatePlayerOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if req.Title == "" || req.Description == "" {
		http.Error(w, "Title and description are required", http.StatusBadRequest)
		return
	}

	order, err := h.service.CreatePlayerOrder(ctx, customerID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create player order")
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(order)
}

// GetPlayerOrders handles GET /social/orders
func (h *OrderHandlers) GetPlayerOrders(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	var orderType *models.OrderType
	if ot := r.URL.Query().Get("order_type"); ot != "" {
		otEnum := models.OrderType(ot)
		orderType = &otEnum
	}

	var status *models.OrderStatus
	if s := r.URL.Query().Get("status"); s != "" {
		sEnum := models.OrderStatus(s)
		status = &sEnum
	}

	limit := 50
	if l := r.URL.Query().Get("limit"); l != "" {
		if parsed, err := strconv.Atoi(l); err == nil && parsed > 0 {
			limit = parsed
		}
	}

	offset := 0
	if o := r.URL.Query().Get("offset"); o != "" {
		if parsed, err := strconv.Atoi(o); err == nil && parsed >= 0 {
			offset = parsed
		}
	}

	response, err := h.service.GetPlayerOrders(ctx, orderType, status, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player orders")
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// GetPlayerOrder handles GET /social/orders/{orderId}
func (h *OrderHandlers) GetPlayerOrder(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	orderIDStr := chi.URLParam(r, "orderId")
	orderID, err := uuid.Parse(orderIDStr)
	if err != nil {
		http.Error(w, "Invalid order ID", http.StatusBadRequest)
		return
	}

	order, err := h.service.GetPlayerOrder(ctx, orderID)
	if err != nil {
		h.logger.WithError(err).WithField("order_id", orderID).Error("Failed to get player order")
		http.Error(w, "Order not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(order)
}

// AcceptPlayerOrder handles POST /social/orders/{orderId}/accept
func (h *OrderHandlers) AcceptPlayerOrder(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	userID := r.Context().Value("user_id")
	if userID == nil {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	executorID, err := uuid.Parse(userID.(string))
	if err != nil {
		http.Error(w, "Invalid user ID", http.StatusBadRequest)
		return
	}

	orderIDStr := chi.URLParam(r, "orderId")
	orderID, err := uuid.Parse(orderIDStr)
	if err != nil {
		http.Error(w, "Invalid order ID", http.StatusBadRequest)
		return
	}

	order, err := h.service.AcceptPlayerOrder(ctx, orderID, executorID)
	if err != nil {
		h.logger.WithError(err).WithField("order_id", orderID).Error("Failed to accept player order")
		http.Error(w, "Failed to accept order", http.StatusBadRequest)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(order)
}

// StartPlayerOrder handles POST /social/orders/{orderId}/start
func (h *OrderHandlers) StartPlayerOrder(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	orderIDStr := chi.URLParam(r, "orderId")
	orderID, err := uuid.Parse(orderIDStr)
	if err != nil {
		http.Error(w, "Invalid order ID", http.StatusBadRequest)
		return
	}

	order, err := h.service.StartPlayerOrder(ctx, orderID)
	if err != nil {
		h.logger.WithError(err).WithField("order_id", orderID).Error("Failed to start player order")
		http.Error(w, "Failed to start order", http.StatusBadRequest)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(order)
}

// CompletePlayerOrder handles POST /social/orders/{orderId}/complete
func (h *OrderHandlers) CompletePlayerOrder(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	orderIDStr := chi.URLParam(r, "orderId")
	orderID, err := uuid.Parse(orderIDStr)
	if err != nil {
		http.Error(w, "Invalid order ID", http.StatusBadRequest)
		return
	}

	var req models.CompletePlayerOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	order, err := h.service.CompletePlayerOrder(ctx, orderID, &req)
	if err != nil {
		h.logger.WithError(err).WithField("order_id", orderID).Error("Failed to complete player order")
		http.Error(w, "Failed to complete order", http.StatusBadRequest)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(order)
}

// CancelPlayerOrder handles POST /social/orders/{orderId}/cancel
func (h *OrderHandlers) CancelPlayerOrder(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	orderIDStr := chi.URLParam(r, "orderId")
	orderID, err := uuid.Parse(orderIDStr)
	if err != nil {
		http.Error(w, "Invalid order ID", http.StatusBadRequest)
		return
	}

	order, err := h.service.CancelPlayerOrder(ctx, orderID)
	if err != nil {
		h.logger.WithError(err).WithField("order_id", orderID).Error("Failed to cancel player order")
		http.Error(w, "Failed to cancel order", http.StatusBadRequest)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(order)
}

