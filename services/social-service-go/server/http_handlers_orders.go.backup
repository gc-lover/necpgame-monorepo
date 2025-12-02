package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
)

func (s *HTTPServer) createPlayerOrder(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	customerID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.CreatePlayerOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	order, err := s.socialService.CreatePlayerOrder(r.Context(), customerID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create player order")
		s.respondError(w, http.StatusInternalServerError, "failed to create order")
		return
	}

	s.respondJSON(w, http.StatusCreated, order)
}

func (s *HTTPServer) getPlayerOrders(w http.ResponseWriter, r *http.Request) {
	var orderType *models.OrderType
	var status *models.OrderStatus

	if orderTypeStr := r.URL.Query().Get("order_type"); orderTypeStr != "" {
		ot := models.OrderType(orderTypeStr)
		orderType = &ot
	}

	if statusStr := r.URL.Query().Get("status"); statusStr != "" {
		st := models.OrderStatus(statusStr)
		status = &st
	}

	limit := 50
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil {
			offset = o
		}
	}

	response, err := s.socialService.GetPlayerOrders(r.Context(), orderType, status, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get player orders")
		s.respondError(w, http.StatusInternalServerError, "failed to get orders")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getPlayerOrder(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["orderId"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid order id")
		return
	}

	order, err := s.socialService.GetPlayerOrder(r.Context(), orderID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get player order")
		s.respondError(w, http.StatusInternalServerError, "failed to get order")
		return
	}

	if order == nil {
		s.respondError(w, http.StatusNotFound, "order not found")
		return
	}

	s.respondJSON(w, http.StatusOK, order)
}

func (s *HTTPServer) acceptPlayerOrder(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	executorID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["orderId"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid order id")
		return
	}

	order, err := s.socialService.AcceptPlayerOrder(r.Context(), orderID, executorID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to accept player order")
		s.respondError(w, http.StatusInternalServerError, "failed to accept order")
		return
	}

	s.respondJSON(w, http.StatusOK, order)
}

func (s *HTTPServer) startPlayerOrder(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["orderId"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid order id")
		return
	}

	order, err := s.socialService.StartPlayerOrder(r.Context(), orderID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to start player order")
		s.respondError(w, http.StatusInternalServerError, "failed to start order")
		return
	}

	s.respondJSON(w, http.StatusOK, order)
}

func (s *HTTPServer) completePlayerOrder(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["orderId"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid order id")
		return
	}

	var req models.CompletePlayerOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	order, err := s.socialService.CompletePlayerOrder(r.Context(), orderID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to complete player order")
		s.respondError(w, http.StatusInternalServerError, "failed to complete order")
		return
	}

	s.respondJSON(w, http.StatusOK, order)
}

func (s *HTTPServer) cancelPlayerOrder(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["orderId"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid order id")
		return
	}

	order, err := s.socialService.CancelPlayerOrder(r.Context(), orderID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to cancel player order")
		s.respondError(w, http.StatusInternalServerError, "failed to cancel order")
		return
	}

	s.respondJSON(w, http.StatusOK, order)
}

func (s *HTTPServer) reviewPlayerOrder(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	reviewerID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["orderId"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid order id")
		return
	}

	var req models.ReviewPlayerOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	review, err := s.socialService.ReviewPlayerOrder(r.Context(), orderID, reviewerID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to review player order")
		s.respondError(w, http.StatusInternalServerError, "failed to review order")
		return
	}

	s.respondJSON(w, http.StatusOK, review)
}

