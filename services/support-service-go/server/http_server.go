package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/support-service-go/models"
	supportapi "github.com/necpgame/support-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type TicketServiceInterface interface {
	CreateTicket(ctx context.Context, playerID uuid.UUID, req *models.CreateTicketRequest) (*models.SupportTicket, error)
	GetTicket(ctx context.Context, id uuid.UUID) (*models.SupportTicket, error)
	GetTicketByNumber(ctx context.Context, number string) (*models.SupportTicket, error)
	GetTicketsByPlayerID(ctx context.Context, playerID uuid.UUID, limit, offset int) (*models.TicketListResponse, error)
	GetTicketsByAgentID(ctx context.Context, agentID uuid.UUID, limit, offset int) (*models.TicketListResponse, error)
	GetTicketsByStatus(ctx context.Context, status models.TicketStatus, limit, offset int) (*models.TicketListResponse, error)
	UpdateTicket(ctx context.Context, id uuid.UUID, req *models.UpdateTicketRequest) (*models.SupportTicket, error)
	AssignTicket(ctx context.Context, id uuid.UUID, agentID uuid.UUID) (*models.SupportTicket, error)
	AddResponse(ctx context.Context, ticketID uuid.UUID, authorID uuid.UUID, isAgent bool, req *models.AddResponseRequest) (*models.TicketResponse, error)
	GetTicketDetail(ctx context.Context, id uuid.UUID) (*models.TicketDetailResponse, error)
	RateTicket(ctx context.Context, id uuid.UUID, rating int) error
}

type HTTPServer struct {
	addr          string
	router        *mux.Router
	ticketService TicketServiceInterface
	slaService    SLAServiceInterface
	logger        *logrus.Logger
	server        *http.Server
	jwtValidator  *JwtValidator
	authEnabled   bool
}

func NewHTTPServer(addr string, ticketService TicketServiceInterface, slaService SLAServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:          addr,
		router:        router,
		ticketService: ticketService,
		slaService:    slaService,
		logger:        GetLogger(),
		jwtValidator:  jwtValidator,
		authEnabled:   authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	supportHandlers := NewSupportHandlers(ticketService)
	support := api.PathPrefix("/support").Subrouter()
	supportapi.HandlerFromMux(supportHandlers, support)

	slaHandlers := NewSLAHandlers(slaService)
	support.HandleFunc("/tickets/{ticket_id}/sla", slaHandlers.getTicketSLA).Methods("GET")
	support.HandleFunc("/sla/violations", slaHandlers.getSLAViolations).Methods("GET")

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
			s.respondErrorJSON(w, http.StatusUnauthorized, "authorization header required")
			return
		}

		claims, err := s.jwtValidator.Verify(r.Context(), authHeader)
		if err != nil {
			s.logger.WithError(err).Warn("JWT validation failed")
			s.respondErrorJSON(w, http.StatusUnauthorized, "invalid or expired token")
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

func (s *HTTPServer) respondErrorJSON(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}
