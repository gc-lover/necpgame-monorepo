// HTTP Server for Social Orders Creation Service
// Issue: #140894825

package server

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/google/uuid"
	"github.com/necp-game/social-orders-creation-service-go/pkg/orders-creation"
	"go.uber.org/zap"
)

// Server represents the HTTP server
type Server struct {
	router  *chi.Mux
	service *orderscreation.Service
	logger  *zap.Logger
	server  *http.Server
}

// NewServer creates a new HTTP server
func NewServer(service *orderscreation.Service, logger *zap.Logger) *Server {
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

	// Orders creation API
	s.router.Route("/orders-creation", func(r chi.Router) {
		r.Post("/draft", s.createOrderDraft)
		r.Post("/validate", s.validateOrderParameters)
		r.Post("/optimize", s.optimizeOrderParameters)
		r.Post("/contractors/suggest", s.suggestContractors)
		r.Post("/create-with-validation", s.createOrderWithValidation)
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
		"service":   "social-orders-creation",
		"timestamp": time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// createOrderDraft creates an order draft with validation and suggestions
func (s *Server) createOrderDraft(w http.ResponseWriter, r *http.Request) {
	var req orderscreation.CreateOrderDraftRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.Error("Failed to decode request body", zap.Error(err))
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// TODO: Extract client ID from JWT token
	clientID := uuid.New() // placeholder

	draft, err := s.service.CreateOrderDraft(r.Context(), clientID, req)
	if err != nil {
		s.logger.Error("Failed to create order draft",
			zap.String("client_id", clientID.String()),
			zap.Error(err))
		http.Error(w, "Failed to create draft", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(draft)
}

// validateOrderParameters validates order parameters
func (s *Server) validateOrderParameters(w http.ResponseWriter, r *http.Request) {
	var req orderscreation.ValidateOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.Error("Failed to decode request body", zap.Error(err))
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// TODO: Extract client ID from JWT token
	clientID := uuid.New() // placeholder

	validation, err := s.service.ValidateOrderParameters(r.Context(), clientID, req)
	if err != nil {
		s.logger.Error("Failed to validate order parameters",
			zap.String("client_id", clientID.String()),
			zap.Error(err))
		http.Error(w, "Failed to validate parameters", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(validation)
}

// optimizeOrderParameters provides optimization suggestions
func (s *Server) optimizeOrderParameters(w http.ResponseWriter, r *http.Request) {
	var req orderscreation.OptimizeOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.Error("Failed to decode request body", zap.Error(err))
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// TODO: Extract client ID from JWT token
	clientID := uuid.New() // placeholder

	optimization, err := s.service.OptimizeOrderParameters(r.Context(), clientID, req)
	if err != nil {
		s.logger.Error("Failed to optimize order parameters",
			zap.String("client_id", clientID.String()),
			zap.Error(err))
		http.Error(w, "Failed to optimize parameters", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(optimization)
}

// suggestContractors suggests suitable contractors for an order
func (s *Server) suggestContractors(w http.ResponseWriter, r *http.Request) {
	var req orderscreation.SuggestContractorsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.Error("Failed to decode request body", zap.Error(err))
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	maxSuggestionsStr := r.URL.Query().Get("max_suggestions")
	maxSuggestions := 5
	if maxSuggestionsStr != "" {
		if ms, err := strconv.Atoi(maxSuggestionsStr); err == nil && ms > 0 && ms <= 20 {
			maxSuggestions = ms
		}
	}

	suggestions, err := s.service.SuggestContractors(r.Context(), req, maxSuggestions)
	if err != nil {
		s.logger.Error("Failed to suggest contractors", zap.Error(err))
		http.Error(w, "Failed to suggest contractors", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(suggestions)
}

// createOrderWithValidation creates an order with full validation and optimization
func (s *Server) createOrderWithValidation(w http.ResponseWriter, r *http.Request) {
	var req orderscreation.CreateOrderWithValidationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.Error("Failed to decode request body", zap.Error(err))
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// TODO: Extract client ID from JWT token
	clientID := uuid.New() // placeholder

	result, err := s.service.CreateOrderWithValidation(r.Context(), clientID, req)
	if err != nil {
		s.logger.Error("Failed to create order with validation",
			zap.String("client_id", clientID.String()),
			zap.Error(err))
		http.Error(w, "Failed to create order", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(result)
}
