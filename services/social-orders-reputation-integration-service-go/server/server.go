// HTTP Server for Social Orders Reputation Integration Service
// Issue: #140894823

package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/google/uuid"
	"github.com/necp-game/social-orders-reputation-integration-service-go/pkg/orders-reputation"
	"go.uber.org/zap"
)

// Server represents the HTTP server
type Server struct {
	router  *chi.Mux
	service *ordersreputation.Service
	logger  *zap.Logger
	server  *http.Server
}

// NewServer creates a new HTTP server
func NewServer(service *ordersreputation.Service, logger *zap.Logger) *Server {
	s := &Server{
		router:  chi.NewRouter(),
		service: service,
		logger:  logger,
	}

	s.setupRoutes()
	return s
}

// setupRoutes configures the HTTP routes
func (s *Server) setupRoutes() {
	s.router.Use(middleware.Logger)
	s.router.Use(middleware.Recoverer)
	s.router.Use(middleware.Timeout(30 * time.Second))

	// Health check
	s.router.Get("/health", s.healthCheck)

	// Orders reputation API
	s.router.Route("/orders-reputation", func(r chi.Router) {
		r.Get("/orders/{order_id}/cost", s.calculateOrderReputationCost)
		r.Get("/orders/{order_id}/requirements", s.checkOrderReputationRequirements)
		r.Post("/orders/{order_id}/complete", s.applyOrderCompletionReputation)
		r.Get("/contractors/ranking", s.getContractorsReputationRanking)
		r.Get("/bonuses", s.getReputationBonuses)
	})
}

// Start starts the HTTP server
func (s *Server) Start(addr string) error {
	s.server = &http.Server{
		Addr:    addr,
		Handler: s.router,
	}

	s.logger.Info("Starting HTTP server", zap.String("addr", addr))
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *Server) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// healthCheck handles health check requests
func (s *Server) healthCheck(w http.ResponseWriter, r *http.Request) {
	response := map[string]interface{}{
		"status":    "healthy",
		"service":   "social-orders-reputation-integration",
		"timestamp": time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// calculateOrderReputationCost calculates order cost with reputation modifiers
func (s *Server) calculateOrderReputationCost(w http.ResponseWriter, r *http.Request) {
	orderIDStr := chi.URLParam(r, "order_id")
	orderID, err := uuid.Parse(orderIDStr)
	if err != nil {
		s.logger.Error("Invalid order ID", zap.String("order_id", orderIDStr), zap.Error(err))
		http.Error(w, "Invalid order ID", http.StatusBadRequest)
		return
	}

	clientIDStr := r.URL.Query().Get("client_id")
	if clientIDStr == "" {
		http.Error(w, "client_id is required", http.StatusBadRequest)
		return
	}
	clientID, err := uuid.Parse(clientIDStr)
	if err != nil {
		s.logger.Error("Invalid client ID", zap.String("client_id", clientIDStr), zap.Error(err))
		http.Error(w, "Invalid client ID", http.StatusBadRequest)
		return
	}

	contractorIDStr := r.URL.Query().Get("contractor_id")
	var contractorID *uuid.UUID
	if contractorIDStr != "" {
		if id, err := uuid.Parse(contractorIDStr); err == nil {
			contractorID = &id
		}
	}

	cost, err := s.service.CalculateOrderReputationCost(r.Context(), orderID, clientID, contractorID)
	if err != nil {
		s.logger.Error("Failed to calculate order reputation cost",
			zap.String("order_id", orderID.String()),
			zap.String("client_id", clientID.String()),
			zap.Error(err))
		http.Error(w, "Failed to calculate cost", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(cost)
}

// checkOrderReputationRequirements checks if character meets reputation requirements
func (s *Server) checkOrderReputationRequirements(w http.ResponseWriter, r *http.Request) {
	orderIDStr := chi.URLParam(r, "order_id")
	orderID, err := uuid.Parse(orderIDStr)
	if err != nil {
		s.logger.Error("Invalid order ID", zap.String("order_id", orderIDStr), zap.Error(err))
		http.Error(w, "Invalid order ID", http.StatusBadRequest)
		return
	}

	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		http.Error(w, "character_id is required", http.StatusBadRequest)
		return
	}
	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.logger.Error("Invalid character ID", zap.String("character_id", characterIDStr), zap.Error(err))
		http.Error(w, "Invalid character ID", http.StatusBadRequest)
		return
	}

	roleStr := r.URL.Query().Get("role")
	if roleStr == "" {
		http.Error(w, "role is required", http.StatusBadRequest)
		return
	}

	var role ordersreputation.CharacterRole
	switch roleStr {
	case "client":
		role = ordersreputation.RoleClient
	case "contractor":
		role = ordersreputation.RoleContractor
	default:
		http.Error(w, "Invalid role. Must be 'client' or 'contractor'", http.StatusBadRequest)
		return
	}

	check, err := s.service.CheckOrderReputationRequirements(r.Context(), orderID, characterID, role)
	if err != nil {
		s.logger.Error("Failed to check reputation requirements",
			zap.String("order_id", orderID.String()),
			zap.String("character_id", characterID.String()),
			zap.Error(err))
		http.Error(w, "Failed to check requirements", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(check)
}

// applyOrderCompletionReputation applies reputation changes after order completion
func (s *Server) applyOrderCompletionReputation(w http.ResponseWriter, r *http.Request) {
	orderIDStr := chi.URLParam(r, "order_id")
	orderID, err := uuid.Parse(orderIDStr)
	if err != nil {
		s.logger.Error("Invalid order ID", zap.String("order_id", orderIDStr), zap.Error(err))
		http.Error(w, "Invalid order ID", http.StatusBadRequest)
		return
	}

	var req ordersreputation.OrderCompletionReputationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.Error("Failed to decode request body", zap.Error(err))
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	response, err := s.service.ApplyOrderCompletionReputation(r.Context(), orderID, req)
	if err != nil {
		s.logger.Error("Failed to apply reputation changes",
			zap.String("order_id", orderID.String()),
			zap.Error(err))
		http.Error(w, "Failed to apply reputation changes", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// getContractorsReputationRanking returns contractors ranking by reputation
func (s *Server) getContractorsReputationRanking(w http.ResponseWriter, r *http.Request) {
	factionIDStr := r.URL.Query().Get("faction_id")
	var factionID *uuid.UUID
	if factionIDStr != "" {
		if id, err := uuid.Parse(factionIDStr); err == nil {
			factionID = &id
		}
	}

	orderTypeStr := r.URL.Query().Get("order_type")
	var orderType *string
	if orderTypeStr != "" {
		orderType = &orderTypeStr
	}

	limitStr := r.URL.Query().Get("limit")
	limit := 50 // default
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offsetStr := r.URL.Query().Get("offset")
	offset := 0 // default
	if offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	ranking, err := s.service.GetContractorsReputationRanking(r.Context(), factionID, orderType, limit, offset)
	if err != nil {
		s.logger.Error("Failed to get contractors ranking", zap.Error(err))
		http.Error(w, "Failed to get ranking", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(ranking)
}

// getReputationBonuses returns reputation bonuses for orders
func (s *Server) getReputationBonuses(w http.ResponseWriter, r *http.Request) {
	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		http.Error(w, "character_id is required", http.StatusBadRequest)
		return
	}
	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.logger.Error("Invalid character ID", zap.String("character_id", characterIDStr), zap.Error(err))
		http.Error(w, "Invalid character ID", http.StatusBadRequest)
		return
	}

	factionIDStr := r.URL.Query().Get("faction_id")
	var factionID *uuid.UUID
	if factionIDStr != "" {
		if id, err := uuid.Parse(factionIDStr); err == nil {
			factionID = &id
		}
	}

	bonuses, err := s.service.GetReputationBonuses(r.Context(), characterID, factionID)
	if err != nil {
		s.logger.Error("Failed to get reputation bonuses",
			zap.String("character_id", characterID.String()),
			zap.Error(err))
		http.Error(w, "Failed to get bonuses", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(bonuses)
}
