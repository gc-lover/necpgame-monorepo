package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
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

type OrdersHandlers struct {
	service OrderServiceInterface
	logger  *logrus.Logger
}

func NewOrdersHandlers(service OrderServiceInterface) *OrdersHandlers {
	return &OrdersHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *OrdersHandlers) createPlayerOrder(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	customerID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.CreatePlayerOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Title == "" || req.Description == "" {
		h.respondError(w, http.StatusBadRequest, "title and description are required")
		return
	}

	order, err := h.service.CreatePlayerOrder(r.Context(), customerID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create player order")
		h.respondError(w, http.StatusInternalServerError, "failed to create player order")
		return
	}

	h.respondJSON(w, http.StatusCreated, order)
}

func (h *OrdersHandlers) getPlayerOrders(w http.ResponseWriter, r *http.Request) {
	var orderType *models.OrderType
	if orderTypeStr := r.URL.Query().Get("order_type"); orderTypeStr != "" {
		ot := models.OrderType(orderTypeStr)
		orderType = &ot
	}

	var status *models.OrderStatus
	if statusStr := r.URL.Query().Get("status"); statusStr != "" {
		s := models.OrderStatus(statusStr)
		status = &s
	}

	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := h.service.GetPlayerOrders(r.Context(), orderType, status, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player orders")
		h.respondError(w, http.StatusInternalServerError, "failed to get player orders")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *OrdersHandlers) getPlayerOrder(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["order_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid order ID")
		return
	}

	order, err := h.service.GetPlayerOrder(r.Context(), orderID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player order")
		h.respondError(w, http.StatusInternalServerError, "failed to get player order")
		return
	}

	if order == nil {
		h.respondError(w, http.StatusNotFound, "Order not found")
		return
	}

	h.respondJSON(w, http.StatusOK, order)
}

func (h *OrdersHandlers) acceptPlayerOrder(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	executorID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["order_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid order ID")
		return
	}

	order, err := h.service.AcceptPlayerOrder(r.Context(), orderID, executorID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to accept player order")
		h.respondError(w, http.StatusInternalServerError, "failed to accept player order")
		return
	}

	if order == nil {
		h.respondError(w, http.StatusNotFound, "Order not found")
		return
	}

	h.respondJSON(w, http.StatusOK, order)
}

func (h *OrdersHandlers) startPlayerOrder(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["order_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid order ID")
		return
	}

	order, err := h.service.StartPlayerOrder(r.Context(), orderID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to start player order")
		h.respondError(w, http.StatusInternalServerError, "failed to start player order")
		return
	}

	if order == nil {
		h.respondError(w, http.StatusNotFound, "Order not found")
		return
	}

	h.respondJSON(w, http.StatusOK, order)
}

func (h *OrdersHandlers) completePlayerOrder(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["order_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid order ID")
		return
	}

	var req models.CompletePlayerOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	order, err := h.service.CompletePlayerOrder(r.Context(), orderID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to complete player order")
		h.respondError(w, http.StatusInternalServerError, "failed to complete player order")
		return
	}

	if order == nil {
		h.respondError(w, http.StatusNotFound, "Order not found")
		return
	}

	h.respondJSON(w, http.StatusOK, order)
}

func (h *OrdersHandlers) cancelPlayerOrder(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["order_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid order ID")
		return
	}

	order, err := h.service.CancelPlayerOrder(r.Context(), orderID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to cancel player order")
		h.respondError(w, http.StatusInternalServerError, "failed to cancel player order")
		return
	}

	if order == nil {
		h.respondError(w, http.StatusNotFound, "Order not found")
		return
	}

	h.respondJSON(w, http.StatusOK, order)
}

func (h *OrdersHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	var buf bytes.Buffer
	if err := json.NewEncoder(&buf).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
		h.respondError(w, http.StatusInternalServerError, "Failed to encode JSON response")
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if _, err := w.Write(buf.Bytes()); err != nil {
		h.logger.WithError(err).Error("Failed to write JSON response")
	}
}

func (h *OrdersHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

