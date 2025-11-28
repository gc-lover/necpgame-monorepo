package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/jackc/pgx/v5"
	"github.com/necpgame/economy-service-go/models"
)

type CurrencyExchangeServiceInterface interface {
	GetExchangeRates(ctx context.Context) (*models.ExchangeRatesResponse, error)
	GetExchangeRate(ctx context.Context, pair models.CurrencyPair) (*models.ExchangeRate, error)
	CalculateQuote(ctx context.Context, req *models.QuoteRequest) (*models.Quote, error)
	InstantExchange(ctx context.Context, playerID uuid.UUID, req *models.InstantExchangeRequest) (*models.ExchangeOrder, error)
	CreateLimitOrder(ctx context.Context, playerID uuid.UUID, req *models.LimitOrderRequest) (*models.ExchangeOrder, error)
	GetOrder(ctx context.Context, orderID uuid.UUID) (*models.ExchangeOrder, error)
	ListOrders(ctx context.Context, playerID uuid.UUID, status *models.OrderStatus, limit, offset int) (*models.OrderListResponse, error)
	DeleteOrder(ctx context.Context, orderID uuid.UUID) error
	ListTrades(ctx context.Context, playerID uuid.UUID, limit, offset int) (*models.CurrencyExchangeTradeListResponse, error)
}

func (s *HTTPServer) getExchangeRates(w http.ResponseWriter, r *http.Request) {
	if s.currencyExchangeService == nil {
		s.respondError(w, http.StatusNotImplemented, "currency exchange service not initialized")
		return
	}

	response, err := s.currencyExchangeService.GetExchangeRates(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get exchange rates")
		s.respondError(w, http.StatusInternalServerError, "failed to get exchange rates")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getExchangeRate(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	pair := models.CurrencyPair(vars["pair"])

	if s.currencyExchangeService == nil {
		s.respondError(w, http.StatusNotImplemented, "currency exchange service not initialized")
		return
	}

	rate, err := s.currencyExchangeService.GetExchangeRate(r.Context(), pair)
	if err != nil {
		if err == pgx.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "exchange rate not found")
		} else {
			s.logger.WithError(err).Error("Failed to get exchange rate")
			s.respondError(w, http.StatusInternalServerError, "failed to get exchange rate")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, rate)
}

func (s *HTTPServer) calculateQuote(w http.ResponseWriter, r *http.Request) {
	var req models.QuoteRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if s.currencyExchangeService == nil {
		s.respondError(w, http.StatusNotImplemented, "currency exchange service not initialized")
		return
	}

	quote, err := s.currencyExchangeService.CalculateQuote(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to calculate quote")
		s.respondError(w, http.StatusInternalServerError, "failed to calculate quote")
		return
	}

	s.respondJSON(w, http.StatusOK, quote)
}

func (s *HTTPServer) instantExchange(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.InstantExchangeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if s.currencyExchangeService == nil {
		s.respondError(w, http.StatusNotImplemented, "currency exchange service not initialized")
		return
	}

	order, err := s.currencyExchangeService.InstantExchange(r.Context(), playerID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to execute instant exchange")
		s.respondError(w, http.StatusInternalServerError, "failed to execute instant exchange")
		return
	}

	s.respondJSON(w, http.StatusCreated, order)
}

func (s *HTTPServer) createLimitOrder(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.LimitOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if s.currencyExchangeService == nil {
		s.respondError(w, http.StatusNotImplemented, "currency exchange service not initialized")
		return
	}

	order, err := s.currencyExchangeService.CreateLimitOrder(r.Context(), playerID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create limit order")
		s.respondError(w, http.StatusInternalServerError, "failed to create limit order")
		return
	}

	s.respondJSON(w, http.StatusCreated, order)
}

func (s *HTTPServer) getOrder(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["order_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid order_id")
		return
	}

	if s.currencyExchangeService == nil {
		s.respondError(w, http.StatusNotImplemented, "currency exchange service not initialized")
		return
	}

	order, err := s.currencyExchangeService.GetOrder(r.Context(), orderID)
	if err != nil {
		if err == pgx.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "order not found")
		} else {
			s.logger.WithError(err).Error("Failed to get order")
			s.respondError(w, http.StatusInternalServerError, "failed to get order")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, order)
}

func (s *HTTPServer) getOrders(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var status *models.OrderStatus
	if statusStr := r.URL.Query().Get("status"); statusStr != "" {
		s := models.OrderStatus(statusStr)
		status = &s
	}

	limit := 50
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	if s.currencyExchangeService == nil {
		s.respondError(w, http.StatusNotImplemented, "currency exchange service not initialized")
		return
	}

	response, err := s.currencyExchangeService.ListOrders(r.Context(), playerID, status, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list orders")
		s.respondError(w, http.StatusInternalServerError, "failed to list orders")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) deleteOrder(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	orderID, err := uuid.Parse(vars["order_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid order_id")
		return
	}

	if s.currencyExchangeService == nil {
		s.respondError(w, http.StatusNotImplemented, "currency exchange service not initialized")
		return
	}

	err = s.currencyExchangeService.DeleteOrder(r.Context(), orderID)
	if err != nil {
		if err == pgx.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "order not found")
		} else if err.Error() == "cannot delete filled order" {
			s.respondError(w, http.StatusConflict, err.Error())
		} else {
			s.logger.WithError(err).Error("Failed to delete order")
			s.respondError(w, http.StatusInternalServerError, "failed to delete order")
		}
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

func (s *HTTPServer) getTrades(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	limit := 50
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	if s.currencyExchangeService == nil {
		s.respondError(w, http.StatusNotImplemented, "currency exchange service not initialized")
		return
	}

	response, err := s.currencyExchangeService.ListTrades(r.Context(), playerID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get trades")
		s.respondError(w, http.StatusInternalServerError, "failed to get trades")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

