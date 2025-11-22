package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/economy-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr          string
	router        *mux.Router
	tradeService  *TradeService
	logger        *logrus.Logger
	server        *http.Server
	jwtValidator  *JwtValidator
	authEnabled   bool
}

func NewHTTPServer(addr string, tradeService *TradeService, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:         addr,
		router:       router,
		tradeService: tradeService,
		logger:       GetLogger(),
		jwtValidator: jwtValidator,
		authEnabled:  authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	economy := api.PathPrefix("/economy").Subrouter()

	economy.HandleFunc("/trade", server.createTrade).Methods("POST")
	economy.HandleFunc("/trade", server.getActiveTrades).Methods("GET")
	economy.HandleFunc("/trade/{id}", server.getTrade).Methods("GET")
	economy.HandleFunc("/trade/{id}/offer", server.updateOffer).Methods("PUT")
	economy.HandleFunc("/trade/{id}/confirm", server.confirmTrade).Methods("POST")
	economy.HandleFunc("/trade/{id}/complete", server.completeTrade).Methods("POST")
	economy.HandleFunc("/trade/{id}/cancel", server.cancelTrade).Methods("POST")
	economy.HandleFunc("/trade/history", server.getTradeHistory).Methods("GET")

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

func (s *HTTPServer) createTrade(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	initiatorID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.CreateTradeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.RecipientID == uuid.Nil {
		s.respondError(w, http.StatusBadRequest, "recipient_id is required")
		return
	}

	session, err := s.tradeService.CreateTrade(r.Context(), initiatorID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create trade")
		s.respondError(w, http.StatusInternalServerError, "failed to create trade")
		return
	}

	if session == nil {
		s.respondError(w, http.StatusConflict, "active trade already exists")
		return
	}

	s.respondJSON(w, http.StatusCreated, session)
}

func (s *HTTPServer) getActiveTrades(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	characterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	sessions, err := s.tradeService.GetActiveTrades(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get active trades")
		s.respondError(w, http.StatusInternalServerError, "failed to get active trades")
		return
	}

	response := models.TradeListResponse{
		Trades: sessions,
		Total:  len(sessions),
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getTrade(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid trade id")
		return
	}

	session, err := s.tradeService.GetTrade(r.Context(), id)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get trade")
		s.respondError(w, http.StatusInternalServerError, "failed to get trade")
		return
	}

	if session == nil {
		s.respondError(w, http.StatusNotFound, "trade not found")
		return
	}

	s.respondJSON(w, http.StatusOK, session)
}

func (s *HTTPServer) updateOffer(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	sessionID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid trade id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	characterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.UpdateTradeOfferRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	session, err := s.tradeService.UpdateOffer(r.Context(), sessionID, characterID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update offer")
		s.respondError(w, http.StatusInternalServerError, "failed to update offer")
		return
	}

	if session == nil {
		s.respondError(w, http.StatusForbidden, "cannot update offer")
		return
	}

	s.respondJSON(w, http.StatusOK, session)
}

func (s *HTTPServer) confirmTrade(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	sessionID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid trade id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	characterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	session, err := s.tradeService.ConfirmTrade(r.Context(), sessionID, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to confirm trade")
		s.respondError(w, http.StatusInternalServerError, "failed to confirm trade")
		return
	}

	if session == nil {
		s.respondError(w, http.StatusForbidden, "cannot confirm trade")
		return
	}

	s.respondJSON(w, http.StatusOK, session)
}

func (s *HTTPServer) completeTrade(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	sessionID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid trade id")
		return
	}

	err = s.tradeService.CompleteTrade(r.Context(), sessionID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to complete trade")
		s.respondError(w, http.StatusInternalServerError, "failed to complete trade")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) cancelTrade(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	sessionID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid trade id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	characterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	err = s.tradeService.CancelTrade(r.Context(), sessionID, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to cancel trade")
		s.respondError(w, http.StatusInternalServerError, "failed to cancel trade")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) getTradeHistory(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	characterID, err := uuid.Parse(userID.(string))
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

	response, err := s.tradeService.GetTradeHistory(r.Context(), characterID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get trade history")
		s.respondError(w, http.StatusInternalServerError, "failed to get trade history")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
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

