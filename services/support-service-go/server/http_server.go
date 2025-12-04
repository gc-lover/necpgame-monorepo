// Issue: #141886730, ogen migration
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/support-service-go/models"
	supportapi "github.com/gc-lover/necpgame-monorepo/services/support-service-go/pkg/api"
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
	router        *chi.Mux
	ticketService TicketServiceInterface
	slaService    SLAServiceInterface
	logger        *logrus.Logger
	server        *http.Server
	jwtValidator  *JwtValidator
	authEnabled   bool
}

func NewHTTPServer(addr string, ticketService TicketServiceInterface, slaService SLAServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := chi.NewRouter()
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
	router.Use(middleware.Recoverer)

	// Initialize ogen handlers and security handler
	ogenHandlers := NewHandlers(ticketService)
	ogenSecurity := NewSecurityHandler(jwtValidator, authEnabled)

	// Create ogen server
	ogenServer, err := supportapi.NewServer(ogenHandlers, ogenSecurity)
	if err != nil {
		server.logger.WithError(err).Fatal("Failed to create ogen server")
	}

	router.Mount("/api/v1/support", ogenServer)

	// SLA handlers (still using direct handlers for now)
	slaHandlers := NewSLAHandlers(slaService)
	router.Route("/api/v1/support", func(r chi.Router) {
		r.Get("/tickets/{ticket_id}/sla", slaHandlers.getTicketSLA)
		r.Get("/sla/violations", slaHandlers.getSLAViolations)
	})

	router.HandleFunc("/health", server.healthCheck)

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
	if err := json.NewEncoder(w).Encode(data); err != nil {
		s.logger.WithError(err).Error("Failed to encode JSON response")
	}
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

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}
