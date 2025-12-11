// Issue: #141886730, ogen migration
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"
	"strings"

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
	router        *http.ServeMux
	ticketService TicketServiceInterface
	slaService    SLAServiceInterface
	logger        *logrus.Logger
	server        *http.Server
	jwtValidator  *JwtValidator
	authEnabled   bool
}

func NewHTTPServer(addr string, ticketService TicketServiceInterface, slaService SLAServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := http.NewServeMux()
	server := &HTTPServer{
		addr:          addr,
		router:        router,
		ticketService: ticketService,
		slaService:    slaService,
		logger:        GetLogger(),
		jwtValidator:  jwtValidator,
		authEnabled:   authEnabled,
	}

	// Issue: #1489 - Initialize ogen handlers with SLA service
	ogenHandlers := NewHandlers(ticketService, slaService)
	ogenSecurity := NewSecurityHandler(jwtValidator, authEnabled)

	// Create ogen server (routes under /support/...)
	ogenServer, err := supportapi.NewServer(ogenHandlers, ogenSecurity, supportapi.WithPathPrefix("/api/v1"))
	if err != nil {
		server.logger.WithError(err).Fatal("Failed to create ogen server")
	}

	var handler http.Handler = ogenServer
	// Custom endpoints not present in OpenAPI (assign, responses, rate, detail)
	handler = server.extraSupportRoutes(handler)
	handler = server.loggingMiddleware(handler)
	handler = server.metricsMiddleware(handler)
	handler = server.corsMiddleware(handler)
	handler = recoveryMiddleware(handler, server.logger)
	// Preserve user_id from context in tests when auth disabled
	if !authEnabled {
		handler = preserveUserIDMiddleware(handler)
	}

	router.Handle("/", handler)

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

func recoveryMiddleware(next http.Handler, logger *logrus.Logger) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if rec := recover(); rec != nil {
				logger.WithField("panic", rec).Error("recovered from panic")
				http.Error(w, "internal server error", http.StatusInternalServerError)
			}
		}()
		next.ServeHTTP(w, r)
	})
}

func preserveUserIDMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if userID := r.Context().Value("user_id"); userID != nil {
			ctx := context.WithValue(r.Context(), "user_id", userID)
			r = r.WithContext(ctx)
		}
		next.ServeHTTP(w, r)
	})
}

// extraSupportRoutes handles endpoints not covered by the generated OpenAPI server.
func (s *HTTPServer) extraSupportRoutes(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !strings.HasPrefix(r.URL.Path, "/api/v1/support/tickets/") {
			next.ServeHTTP(w, r)
			return
		}

		switch {
		case strings.HasSuffix(r.URL.Path, "/assign") && r.Method == http.MethodPost:
			s.handleAssignTicket(w, r)
		case strings.HasSuffix(r.URL.Path, "/responses") && r.Method == http.MethodPost:
			s.handleAddResponse(w, r)
		case strings.HasSuffix(r.URL.Path, "/rate") && r.Method == http.MethodPost:
			s.handleRateTicket(w, r)
		case strings.HasSuffix(r.URL.Path, "/detail") && r.Method == http.MethodGet:
			s.handleTicketDetail(w, r)
		default:
			next.ServeHTTP(w, r)
		}
	})
}

func (s *HTTPServer) handleAssignTicket(w http.ResponseWriter, r *http.Request) {
	ticketIDStr := pathBaseTrimSuffix(r.URL.Path, "/assign")
	ticketID, err := uuid.Parse(ticketIDStr)
	if err != nil {
		http.Error(w, "invalid ticket id", http.StatusBadRequest)
		return
	}

	var req models.AssignTicketRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid request body", http.StatusBadRequest)
		return
	}
	if req.AgentID == uuid.Nil {
		http.Error(w, "agent_id is required", http.StatusBadRequest)
		return
	}

	ticket, err := s.ticketService.AssignTicket(r.Context(), ticketID, req.AgentID)
	if err != nil {
		http.Error(w, "failed to assign", http.StatusInternalServerError)
		return
	}
	if ticket == nil {
		http.NotFound(w, r)
		return
	}
	s.respondJSON(w, http.StatusOK, ticket)
}

func (s *HTTPServer) handleAddResponse(w http.ResponseWriter, r *http.Request) {
	ticketIDStr := pathBaseTrimSuffix(r.URL.Path, "/responses")
	ticketID, err := uuid.Parse(ticketIDStr)
	if err != nil {
		http.Error(w, "invalid ticket id", http.StatusBadRequest)
		return
	}

	var req models.AddResponseRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid request body", http.StatusBadRequest)
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		http.Error(w, "unauthorized", http.StatusUnauthorized)
		return
	}
	authorID, err := uuid.Parse(userID.(string))
	if err != nil {
		http.Error(w, "invalid user id", http.StatusBadRequest)
		return
	}

	response, err := s.ticketService.AddResponse(r.Context(), ticketID, authorID, false, &req)
	if err != nil {
		http.Error(w, "failed to add response", http.StatusInternalServerError)
		return
	}
	if response == nil {
		http.NotFound(w, r)
		return
	}
	s.respondJSON(w, http.StatusCreated, response)
}

func (s *HTTPServer) handleRateTicket(w http.ResponseWriter, r *http.Request) {
	ticketIDStr := pathBaseTrimSuffix(r.URL.Path, "/rate")
	ticketID, err := uuid.Parse(ticketIDStr)
	if err != nil {
		http.Error(w, "invalid ticket id", http.StatusBadRequest)
		return
	}

	var body struct {
		Rating int `json:"rating"`
	}
	if err := json.NewDecoder(r.Body).Decode(&body); err != nil {
		http.Error(w, "invalid request body", http.StatusBadRequest)
		return
	}
	if body.Rating < 1 || body.Rating > 5 {
		http.Error(w, "invalid rating", http.StatusBadRequest)
		return
	}

	if err := s.ticketService.RateTicket(r.Context(), ticketID, body.Rating); err != nil {
		http.Error(w, "failed to rate", http.StatusInternalServerError)
		return
	}
	w.WriteHeader(http.StatusOK)
}

func (s *HTTPServer) handleTicketDetail(w http.ResponseWriter, r *http.Request) {
	ticketIDStr := pathBaseTrimSuffix(r.URL.Path, "/detail")
	ticketID, err := uuid.Parse(ticketIDStr)
	if err != nil {
		http.Error(w, "invalid ticket id", http.StatusBadRequest)
		return
	}

	detail, err := s.ticketService.GetTicketDetail(r.Context(), ticketID)
	if err != nil {
		http.Error(w, "failed to fetch detail", http.StatusInternalServerError)
		return
	}
	if detail == nil {
		http.NotFound(w, r)
		return
	}
	s.respondJSON(w, http.StatusOK, detail)
}

func pathBaseTrimSuffix(path string, suffix string) string {
	trimmed := strings.TrimSuffix(path, suffix)
	trimmed = strings.Trim(trimmed, "/")
	parts := strings.Split(trimmed, "/")
	if len(parts) == 0 {
		return ""
	}
	return parts[len(parts)-1]
}
