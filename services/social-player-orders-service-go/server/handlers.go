// Issue: #81
package server

import (
	"encoding/json"
	"net/http"

	"github.com/go-chi/chi/v5"
)

type OrderHandlers struct {
	service *OrderService
}

func NewOrderHandlers(service *OrderService) *OrderHandlers {
	return &OrderHandlers{
		service: service,
	}
}

// ListOrders handles GET /social/orders
func (h *OrderHandlers) ListOrders(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Parse query parameters
	orderType := r.URL.Query().Get("orderType")
	status := r.URL.Query().Get("status")

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

// GetOrder handles GET /social/orders/{orderId}
func (h *OrderHandlers) GetOrder(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	orderID := chi.URLParam(r, "orderId")

	order, err := h.service.GetOrder(ctx, orderID)
	if err != nil {
		respondError(w, http.StatusNotFound, "Order not found", err)
		return
	}

	respondJSON(w, http.StatusOK, order)
}

// AcceptOrder handles POST /social/orders/{orderId}/accept
func (h *OrderHandlers) AcceptOrder(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	orderID := chi.URLParam(r, "orderId")

	// TODO: Get player ID from auth context
	playerID := "player-001"

	if err := h.service.AcceptOrder(ctx, orderID, playerID); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to accept order", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Order accepted successfully",
	})
}

// CompleteOrder handles POST /social/orders/{orderId}/complete
func (h *OrderHandlers) CompleteOrder(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	orderID := chi.URLParam(r, "orderId")

	if err := h.service.CompleteOrder(ctx, orderID); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to complete order", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Order completed successfully",
	})
}

// CancelOrder handles POST /social/orders/{orderId}/cancel
func (h *OrderHandlers) CancelOrder(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	orderID := chi.URLParam(r, "orderId")

	if err := h.service.CancelOrder(ctx, orderID); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to cancel order", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Order cancelled successfully",
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

