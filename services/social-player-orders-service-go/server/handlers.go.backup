// Issue: #81
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/social-player-orders-service-go/pkg/api"
	"github.com/oapi-codegen/runtime/types"
)

// OrderHandlers implements api.ServerInterface
type OrderHandlers struct {
	service *OrderService
}

// NewOrderHandlers creates handlers with DI
func NewOrderHandlers(service *OrderService) *OrderHandlers {
	return &OrderHandlers{
		service: service,
	}
}

// ListOrders implements api.ServerInterface
func (h *OrderHandlers) ListOrders(w http.ResponseWriter, r *http.Request, params api.ListOrdersParams) {
	ctx := r.Context()

	// Convert params to service format
	var orderType, status string
	if params.OrderType != nil {
		orderType = string(*params.OrderType)
	}
	if params.Status != nil {
		status = string(*params.Status)
	}

	// Get orders from service
	orders, err := h.service.ListOrders(ctx, orderType, status)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to list orders", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]interface{}{
		"data": orders,
		"pagination": map[string]interface{}{
			"page":       1,
			"page_size":  20,
			"total":      len(orders),
			"total_pages": 1,
		},
	})
}

// CreateOrder handles POST /social/orders/create
func (h *OrderHandlers) CreateOrder(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req struct {
		Title       string `json:"title"`
		Description string `json:"description"`
		OrderType   string `json:"order_type"`
		RewardEd    int    `json:"reward_ed"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	// Create order via service
	order, err := h.service.CreateOrder(ctx, req.Title, req.Description, req.OrderType, req.RewardEd)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to create order", err)
		return
	}

	respondJSON(w, http.StatusCreated, order)
}

// GetOrder implements api.ServerInterface
func (h *OrderHandlers) GetOrder(w http.ResponseWriter, r *http.Request, orderId types.UUID) {
	ctx := r.Context()

	order, err := h.service.GetOrder(ctx, orderId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, "Order not found", err)
		return
	}

	respondJSON(w, http.StatusOK, order)
}

// AcceptOrder implements api.ServerInterface
func (h *OrderHandlers) AcceptOrder(w http.ResponseWriter, r *http.Request, orderId types.UUID) {
	ctx := r.Context()

	// TODO: Get player ID from auth context
	playerID := "player-001"

	if err := h.service.AcceptOrder(ctx, orderId.String(), playerID); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to accept order", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Order accepted successfully",
	})
}

// StartOrder implements api.ServerInterface
func (h *OrderHandlers) StartOrder(w http.ResponseWriter, r *http.Request, orderId types.UUID) {
	ctx := r.Context()

	if err := h.service.StartOrder(ctx, orderId.String()); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to start order", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Order started successfully",
	})
}

// CompleteOrder implements api.ServerInterface
func (h *OrderHandlers) CompleteOrder(w http.ResponseWriter, r *http.Request, orderId types.UUID) {
	ctx := r.Context()

	if err := h.service.CompleteOrder(ctx, orderId.String()); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to complete order", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Order completed successfully",
	})
}

// CancelOrder implements api.ServerInterface
func (h *OrderHandlers) CancelOrder(w http.ResponseWriter, r *http.Request, orderId types.UUID) {
	ctx := r.Context()

	if err := h.service.CancelOrder(ctx, orderId.String()); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to cancel order", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Order cancelled successfully",
	})
}

// ReviewOrder implements api.ServerInterface
func (h *OrderHandlers) ReviewOrder(w http.ResponseWriter, r *http.Request, orderId types.UUID) {
	ctx := r.Context()

	var req struct {
		Rating  int    `json:"rating"`
		Comment string `json:"comment"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	if err := h.service.ReviewOrder(ctx, orderId.String(), req.Rating, req.Comment); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to review order", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Review submitted successfully",
	})
}

// Helper functions
func respondJSON(w http.ResponseWriter, statusCode int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, statusCode int, message string, err error) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	json.NewEncoder(w).Encode(map[string]interface{}{
		"error": map[string]interface{}{
			"code":    statusCode,
			"message": message,
			"details": err.Error(),
		},
	})
}

