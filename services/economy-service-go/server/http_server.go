package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/economy-service-go/models"
	tradeapi "github.com/necpgame/economy-service-go/pkg/api"
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
	tradeHandlers       *TradeHandlers
	engramCreationService EngramCreationServiceInterface
	engramTransferService EngramTransferServiceInterface
	logger              *logrus.Logger
	server              *http.Server
	jwtValidator        *JwtValidator
	authEnabled         bool
}

func NewHTTPServer(addr string, tradeService TradeServiceInterface, jwtValidator *JwtValidator, authEnabled bool, engramCreationService EngramCreationServiceInterface, engramTransferService EngramTransferServiceInterface) *HTTPServer {
	router := mux.NewRouter()
	tradeHandlers := NewTradeHandlers(tradeService)
	
	server := &HTTPServer{
		addr:                addr,
		router:              router,
		tradeService:        tradeService,
		tradeHandlers:       tradeHandlers,
		engramCreationService: engramCreationService,
		engramTransferService: engramTransferService,
		logger:              GetLogger(),
		jwtValidator:        jwtValidator,
		authEnabled:         authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	apiRouter := router.PathPrefix("/api/v1").Subrouter()

	if authEnabled {
		apiRouter.Use(server.authMiddleware)
	}

	economy := apiRouter.PathPrefix("/economy").Subrouter()

	tradeapi.HandlerFromMux(tradeHandlers, economy)

	if server.engramCreationService != nil {
		economy.HandleFunc("/engrams/create", server.createEngram).Methods("POST")
		economy.HandleFunc("/engrams/create/cost", server.getEngramCreationCost).Methods("GET")
		economy.HandleFunc("/engrams/create/validate", server.validateEngramCreation).Methods("POST")
	}

	if server.engramTransferService != nil {
		economy.HandleFunc("/engrams/{engram_id}/transfer", server.transferEngram).Methods("POST")
		economy.HandleFunc("/engrams/{engram_id}/loan", server.loanEngram).Methods("POST")
		economy.HandleFunc("/engrams/{engram_id}/extract", server.extractEngram).Methods("POST")
		economy.HandleFunc("/engrams/{engram_id}/trade", server.tradeEngram).Methods("POST")
	}

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

