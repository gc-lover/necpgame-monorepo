package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/support-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr          string
	router        *mux.Router
	ticketService *TicketService
	logger        *logrus.Logger
	server        *http.Server
	jwtValidator  *JwtValidator
	authEnabled   bool
}

func NewHTTPServer(addr string, ticketService *TicketService, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:         addr,
		router:       router,
		ticketService: ticketService,
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

	support := api.PathPrefix("/support").Subrouter()

	support.HandleFunc("/tickets", server.createTicket).Methods("POST")
	support.HandleFunc("/tickets", server.getTickets).Methods("GET")
	support.HandleFunc("/tickets/{id}", server.getTicket).Methods("GET")
	support.HandleFunc("/tickets/number/{number}", server.getTicketByNumber).Methods("GET")
	support.HandleFunc("/tickets/{id}", server.updateTicket).Methods("PUT")
	support.HandleFunc("/tickets/{id}/assign", server.assignTicket).Methods("POST")
	support.HandleFunc("/tickets/{id}/responses", server.addResponse).Methods("POST")
	support.HandleFunc("/tickets/{id}/rate", server.rateTicket).Methods("POST")
	support.HandleFunc("/tickets/{id}/detail", server.getTicketDetail).Methods("GET")

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

func (s *HTTPServer) createTicket(w http.ResponseWriter, r *http.Request) {
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

	var req models.CreateTicketRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Subject == "" || req.Description == "" {
		s.respondError(w, http.StatusBadRequest, "subject and description are required")
		return
	}

	ticket, err := s.ticketService.CreateTicket(r.Context(), playerID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create ticket")
		s.respondError(w, http.StatusInternalServerError, "failed to create ticket")
		return
	}

	s.respondJSON(w, http.StatusCreated, ticket)
}

func (s *HTTPServer) getTickets(w http.ResponseWriter, r *http.Request) {
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

	statusStr := r.URL.Query().Get("status")
	if statusStr != "" {
		status := models.TicketStatus(statusStr)
		response, err := s.ticketService.GetTicketsByStatus(r.Context(), status, limit, offset)
		if err != nil {
			s.logger.WithError(err).Error("Failed to get tickets by status")
			s.respondError(w, http.StatusInternalServerError, "failed to get tickets")
			return
		}
		s.respondJSON(w, http.StatusOK, response)
		return
	}

	response, err := s.ticketService.GetTicketsByPlayerID(r.Context(), playerID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get tickets")
		s.respondError(w, http.StatusInternalServerError, "failed to get tickets")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getTicket(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid ticket id")
		return
	}

	ticket, err := s.ticketService.GetTicket(r.Context(), id)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get ticket")
		s.respondError(w, http.StatusInternalServerError, "failed to get ticket")
		return
	}

	if ticket == nil {
		s.respondError(w, http.StatusNotFound, "ticket not found")
		return
	}

	s.respondJSON(w, http.StatusOK, ticket)
}

func (s *HTTPServer) getTicketByNumber(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	number := vars["number"]

	ticket, err := s.ticketService.GetTicketByNumber(r.Context(), number)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get ticket by number")
		s.respondError(w, http.StatusInternalServerError, "failed to get ticket")
		return
	}

	if ticket == nil {
		s.respondError(w, http.StatusNotFound, "ticket not found")
		return
	}

	s.respondJSON(w, http.StatusOK, ticket)
}

func (s *HTTPServer) updateTicket(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid ticket id")
		return
	}

	var req models.UpdateTicketRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	ticket, err := s.ticketService.UpdateTicket(r.Context(), id, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update ticket")
		s.respondError(w, http.StatusInternalServerError, "failed to update ticket")
		return
	}

	if ticket == nil {
		s.respondError(w, http.StatusNotFound, "ticket not found")
		return
	}

	s.respondJSON(w, http.StatusOK, ticket)
}

func (s *HTTPServer) assignTicket(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid ticket id")
		return
	}

	var req models.AssignTicketRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	ticket, err := s.ticketService.AssignTicket(r.Context(), id, req.AgentID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to assign ticket")
		s.respondError(w, http.StatusInternalServerError, "failed to assign ticket")
		return
	}

	if ticket == nil {
		s.respondError(w, http.StatusNotFound, "ticket not found")
		return
	}

	s.respondJSON(w, http.StatusOK, ticket)
}

func (s *HTTPServer) addResponse(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	ticketID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid ticket id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	authorID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	claims := r.Context().Value("claims").(*Claims)
	isAgent := false
	for _, role := range claims.RealmAccess.Roles {
		if role == "support_agent" || role == "admin" {
			isAgent = true
			break
		}
	}

	var req models.AddResponseRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Message == "" {
		s.respondError(w, http.StatusBadRequest, "message is required")
		return
	}

	response, err := s.ticketService.AddResponse(r.Context(), ticketID, authorID, isAgent, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to add response")
		s.respondError(w, http.StatusInternalServerError, "failed to add response")
		return
	}

	if response == nil {
		s.respondError(w, http.StatusNotFound, "ticket not found")
		return
	}

	s.respondJSON(w, http.StatusCreated, response)
}

func (s *HTTPServer) getTicketDetail(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid ticket id")
		return
	}

	response, err := s.ticketService.GetTicketDetail(r.Context(), id)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get ticket detail")
		s.respondError(w, http.StatusInternalServerError, "failed to get ticket detail")
		return
	}

	if response == nil {
		s.respondError(w, http.StatusNotFound, "ticket not found")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) rateTicket(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid ticket id")
		return
	}

	var req models.RateTicketRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	err = s.ticketService.RateTicket(r.Context(), id, req.Rating)
	if err != nil {
		s.logger.WithError(err).Error("Failed to rate ticket")
		s.respondError(w, http.StatusInternalServerError, "failed to rate ticket")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
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

