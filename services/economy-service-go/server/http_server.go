package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type TradeServiceInterface interface {
	CreateTrade(ctx context.Context, initiatorID uuid.UUID, req *models.CreateTradeRequest) (*models.TradeSession, error)
	GetTrade(ctx context.Context, id uuid.UUID) (*models.TradeSession, error)
	GetActiveTrades(ctx context.Context, characterID uuid.UUID) ([]models.TradeSession, error)
	UpdateOffer(ctx context.Context, sessionID, characterID uuid.UUID, req *models.UpdateTradeOfferRequest) (*models.TradeSession, error)
	ConfirmTrade(ctx context.Context, sessionID, characterID uuid.UUID) (*models.TradeSession, error)
	CompleteTrade(ctx context.Context, sessionID uuid.UUID) error
	CancelTrade(ctx context.Context, sessionID, characterID uuid.UUID) error
	GetTradeHistory(ctx context.Context, characterID uuid.UUID, limit, offset int) (*models.TradeHistoryListResponse, error)
}

type HTTPServer struct {
	addr                string
	router              *mux.Router
	tradeService        TradeServiceInterface
	engramCreationService EngramCreationServiceInterface
	engramTransferService EngramTransferServiceInterface
	weaponCombinationsService WeaponCombinationsServiceInterface
	logger              *logrus.Logger
	server              *http.Server
	jwtValidator        *JwtValidator
	authEnabled         bool
}

func NewHTTPServer(addr string, tradeService TradeServiceInterface, jwtValidator *JwtValidator, authEnabled bool, engramCreationService EngramCreationServiceInterface, engramTransferService EngramTransferServiceInterface, weaponCombinationsService WeaponCombinationsServiceInterface) *HTTPServer {
	router := mux.NewRouter()
	
	server := &HTTPServer{
		addr:                addr,
		router:              router,
		tradeService:        tradeService,
		engramCreationService: engramCreationService,
		engramTransferService: engramTransferService,
		weaponCombinationsService: weaponCombinationsService,
		logger:              GetLogger(),
		jwtValidator:        jwtValidator,
		authEnabled:         authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	// Legacy endpoints (must be registered BEFORE ogen server to avoid conflicts)
	// Legacy trade endpoints (not in ogen API yet)
	// IMPORTANT: More specific routes must be registered FIRST
	router.HandleFunc("/api/v1/economy/trade/history", server.getTradeHistory).Methods("GET")
	router.HandleFunc("/api/v1/economy/trade/{trade_id}/offer", server.updateOffer).Methods("PUT")
	router.HandleFunc("/api/v1/economy/trade/{trade_id}/confirm", server.confirmTrade).Methods("POST")
	router.HandleFunc("/api/v1/economy/trade/{trade_id}/complete", server.completeTrade).Methods("POST")
	router.HandleFunc("/api/v1/economy/trade/{trade_id}/cancel", server.cancelTrade).Methods("POST")
	router.HandleFunc("/api/v1/economy/trade/{trade_id}", server.getTrade).Methods("GET")
	router.HandleFunc("/api/v1/economy/trade", server.getActiveTrades).Methods("GET")
	router.HandleFunc("/api/v1/economy/trade", server.createTradeLegacy).Methods("POST")

	// ogen server
	handlers := NewEconomyHandlers(server.tradeService)
	secHandler := NewSecurityHandler(server.jwtValidator)

	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		server.logger.WithError(err).Fatal("Failed to create ogen server")
	}

	router.PathPrefix("/api/v1").Handler(ogenServer)
	if server.engramCreationService != nil {
		router.HandleFunc("/api/v1/economy/engrams/create", server.createEngram).Methods("POST")
		router.HandleFunc("/api/v1/economy/engrams/create/cost/{chip_tier}", server.getEngramCreationCost).Methods("GET")
		router.HandleFunc("/api/v1/economy/engrams/create/validate", server.validateEngramCreation).Methods("POST")
	}

	if server.engramTransferService != nil {
		router.HandleFunc("/api/v1/economy/engrams/{engram_id}/transfer", server.transferEngram).Methods("POST")
		router.HandleFunc("/api/v1/economy/engrams/{engram_id}/loan", server.loanEngram).Methods("POST")
		router.HandleFunc("/api/v1/economy/engrams/{engram_id}/extract", server.extractEngram).Methods("POST")
		router.HandleFunc("/api/v1/economy/engrams/{engram_id}/trade", server.tradeEngram).Methods("POST")
	}

	// TODO: Weapon combinations API - not in ogen spec yet
	// if server.weaponCombinationsService != nil {
	// 	weaponCombinationsHandlers := NewWeaponCombinationsHandlers(server.weaponCombinationsService)
	// 	weaponCombinationsAPI := economy.PathPrefix("/weapons").Subrouter()
	// 	weaponcombinationsapi.HandlerFromMux(weaponCombinationsHandlers, weaponCombinationsAPI)
	// }

	router.HandleFunc("/health", server.healthCheck).Methods("GET")

	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	errChan := make(chan error, 1)
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	select {
	case err := <-errChan:
		return err
	case <-ctx.Done():
		return s.Shutdown(context.Background())
	}
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

// Issue: #141886468, #141887873
func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		s.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": duration.Milliseconds(),
			"status":      recorder.statusCode,
		}).Info("HTTP request")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start).Seconds()
		RecordRequest(r.Method, r.URL.Path, http.StatusText(recorder.statusCode))
		RecordRequestDuration(r.Method, r.URL.Path, duration)
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

func (s *HTTPServer) authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !s.authEnabled || s.jwtValidator == nil {
			next.ServeHTTP(w, r)
			return
		}

		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			s.respondError(w, http.StatusUnauthorized, "authorization header required")
			return
		}

		claims, err := s.jwtValidator.Verify(r.Context(), authHeader)
		if err != nil {
			s.logger.WithError(err).Warn("JWT validation failed")
			s.respondError(w, http.StatusUnauthorized, "invalid or expired token")
			return
		}

		ctx := context.WithValue(r.Context(), "claims", claims)
		userID := claims.Subject
		if userID == "" {
			userID = claims.RegisteredClaims.Subject
		}
		ctx = context.WithValue(ctx, "user_id", userID)
		ctx = context.WithValue(ctx, "username", claims.PreferredUsername)

		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}

// getActiveTrades is a legacy endpoint for getting active trades
func (s *HTTPServer) getActiveTrades(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	
	// Get user ID from context (set by authMiddleware or createRequestWithUserID)
	userIDStr, ok := ctx.Value("user_id").(string)
	if !ok {
		s.respondError(w, http.StatusUnauthorized, "user ID not found in context")
		return
	}
	
	characterID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user ID")
		return
	}
	
	trades, err := s.tradeService.GetActiveTrades(ctx, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get active trades")
		s.respondError(w, http.StatusInternalServerError, "failed to get active trades")
		return
	}
	
	s.respondJSON(w, http.StatusOK, trades)
}

// createTradeLegacy is a legacy endpoint for creating trades
func (s *HTTPServer) createTradeLegacy(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()
	
	// Get user ID from context
	userIDStr, ok := ctx.Value("user_id").(string)
	if !ok {
		s.respondError(w, http.StatusUnauthorized, "user ID not found in context")
		return
	}
	
	initiatorID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user ID")
		return
	}
	
	var reqBody models.CreateTradeRequest
	if err := json.NewDecoder(r.Body).Decode(&reqBody); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	
	session, err := s.tradeService.CreateTrade(ctx, initiatorID, &reqBody)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create trade")
		s.respondError(w, http.StatusInternalServerError, "failed to create trade")
		return
	}
	
	s.respondJSON(w, http.StatusCreated, session)
}

// getTrade is a legacy endpoint for getting a trade by ID
func (s *HTTPServer) getTrade(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	vars := mux.Vars(r)
	tradeIDStr, ok := vars["trade_id"]
	if !ok {
		s.respondError(w, http.StatusBadRequest, "trade_id is required")
		return
	}

	tradeID, err := uuid.Parse(tradeIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid trade_id")
		return
	}

	session, err := s.tradeService.GetTrade(ctx, tradeID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get trade")
		s.respondError(w, http.StatusNotFound, "trade not found")
		return
	}

	s.respondJSON(w, http.StatusOK, session)
}

// updateOffer is a legacy endpoint for updating a trade offer
func (s *HTTPServer) updateOffer(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	vars := mux.Vars(r)
	tradeIDStr, ok := vars["trade_id"]
	if !ok {
		s.respondError(w, http.StatusBadRequest, "trade_id is required")
		return
	}

	tradeID, err := uuid.Parse(tradeIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid trade_id")
		return
	}

	userIDStr, ok := r.Context().Value("user_id").(string)
	if !ok {
		s.respondError(w, http.StatusUnauthorized, "user ID not found in context")
		return
	}

	characterID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user ID")
		return
	}

	var reqBody models.UpdateTradeOfferRequest
	if err := json.NewDecoder(r.Body).Decode(&reqBody); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	session, err := s.tradeService.UpdateOffer(ctx, tradeID, characterID, &reqBody)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update offer")
		s.respondError(w, http.StatusInternalServerError, "failed to update offer")
		return
	}

	s.respondJSON(w, http.StatusOK, session)
}

// confirmTrade is a legacy endpoint for confirming a trade
func (s *HTTPServer) confirmTrade(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	vars := mux.Vars(r)
	tradeIDStr, ok := vars["trade_id"]
	if !ok {
		s.respondError(w, http.StatusBadRequest, "trade_id is required")
		return
	}

	tradeID, err := uuid.Parse(tradeIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid trade_id")
		return
	}

	userIDStr, ok := r.Context().Value("user_id").(string)
	if !ok {
		s.respondError(w, http.StatusUnauthorized, "user ID not found in context")
		return
	}

	characterID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user ID")
		return
	}

	session, err := s.tradeService.ConfirmTrade(ctx, tradeID, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to confirm trade")
		s.respondError(w, http.StatusInternalServerError, "failed to confirm trade")
		return
	}

	s.respondJSON(w, http.StatusOK, session)
}

// completeTrade is a legacy endpoint for completing a trade
func (s *HTTPServer) completeTrade(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	vars := mux.Vars(r)
	tradeIDStr, ok := vars["trade_id"]
	if !ok {
		s.respondError(w, http.StatusBadRequest, "trade_id is required")
		return
	}

	tradeID, err := uuid.Parse(tradeIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid trade_id")
		return
	}

	err = s.tradeService.CompleteTrade(ctx, tradeID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to complete trade")
		s.respondError(w, http.StatusInternalServerError, "failed to complete trade")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "completed"})
}

// cancelTrade is a legacy endpoint for canceling a trade
func (s *HTTPServer) cancelTrade(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	vars := mux.Vars(r)
	tradeIDStr, ok := vars["trade_id"]
	if !ok {
		s.respondError(w, http.StatusBadRequest, "trade_id is required")
		return
	}

	tradeID, err := uuid.Parse(tradeIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid trade_id")
		return
	}

	userIDStr, ok := r.Context().Value("user_id").(string)
	if !ok {
		s.respondError(w, http.StatusUnauthorized, "user ID not found in context")
		return
	}

	characterID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user ID")
		return
	}

	err = s.tradeService.CancelTrade(ctx, tradeID, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to cancel trade")
		s.respondError(w, http.StatusInternalServerError, "failed to cancel trade")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "cancelled"})
}

// getTradeHistory is a legacy endpoint for getting trade history
func (s *HTTPServer) getTradeHistory(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	userIDStr, ok := r.Context().Value("user_id").(string)
	if !ok {
		s.respondError(w, http.StatusUnauthorized, "user ID not found in context")
		return
	}

	characterID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user ID")
		return
	}

	limit := 10
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil {
			limit = parsed
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsed, err := strconv.Atoi(offsetStr); err == nil {
			offset = parsed
		}
	}

	history, err := s.tradeService.GetTradeHistory(ctx, characterID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get trade history")
		s.respondError(w, http.StatusInternalServerError, "failed to get trade history")
		return
	}

	s.respondJSON(w, http.StatusOK, history)
}

