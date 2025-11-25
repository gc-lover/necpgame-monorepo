package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/feedback-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr            string
	router          *mux.Router
	feedbackService FeedbackServiceInterface
	logger          *logrus.Logger
	server          *http.Server
	jwtValidator    *JwtValidator
	authEnabled     bool
}

func NewHTTPServer(addr string, feedbackService FeedbackServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:            addr,
		router:          router,
		feedbackService: feedbackService,
		logger:          GetLogger(),
		jwtValidator:    jwtValidator,
		authEnabled:     authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1/feedback").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	api.HandleFunc("/submit", server.submitFeedback).Methods("POST")
	api.HandleFunc("/{id}", server.getFeedback).Methods("GET")
	api.HandleFunc("/player/{player_id}", server.getPlayerFeedback).Methods("GET")
	api.HandleFunc("/{id}/update-status", server.updateStatus).Methods("POST")
	api.HandleFunc("/board", server.getBoard).Methods("GET")
	api.HandleFunc("/{id}/vote", server.vote).Methods("POST")
	api.HandleFunc("/{id}/vote", server.unvote).Methods("DELETE")
	api.HandleFunc("/stats", server.getStats).Methods("GET")

	router.HandleFunc("/health", server.healthCheck).Methods("GET")

	return server
}

func (s *HTTPServer) Start() error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	s.logger.WithField("addr", s.addr).Info("Starting HTTP server")
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Stop(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func (s *HTTPServer) submitFeedback(w http.ResponseWriter, r *http.Request) {
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

	var req models.SubmitFeedbackRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.feedbackService.SubmitFeedback(r.Context(), playerID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to submit feedback")
		s.respondError(w, http.StatusInternalServerError, "failed to submit feedback")
		return
	}

	s.respondJSON(w, http.StatusCreated, response)
}

func (s *HTTPServer) getFeedback(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	id, err := uuid.Parse(vars["id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid feedback id")
		return
	}

	feedback, err := s.feedbackService.GetFeedback(r.Context(), id)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get feedback")
		s.respondError(w, http.StatusInternalServerError, "failed to get feedback")
		return
	}

	if feedback == nil {
		s.respondError(w, http.StatusNotFound, "feedback not found")
		return
	}

	s.respondJSON(w, http.StatusOK, feedback)
}

func (s *HTTPServer) getPlayerFeedback(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID, err := uuid.Parse(vars["player_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player id")
		return
	}

	var status *models.FeedbackStatus
	var feedbackType *models.FeedbackType

	if statusStr := r.URL.Query().Get("status"); statusStr != "" {
		s := models.FeedbackStatus(statusStr)
		status = &s
	}

	if typeStr := r.URL.Query().Get("type"); typeStr != "" {
		t := models.FeedbackType(typeStr)
		feedbackType = &t
	}

	limit := 20
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil {
			offset = o
		}
	}

	response, err := s.feedbackService.GetPlayerFeedback(r.Context(), playerID, status, feedbackType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get player feedback")
		s.respondError(w, http.StatusInternalServerError, "failed to get player feedback")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) updateStatus(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	id, err := uuid.Parse(vars["id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid feedback id")
		return
	}

	var req models.UpdateStatusRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	feedback, err := s.feedbackService.UpdateStatus(r.Context(), id, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update status")
		s.respondError(w, http.StatusInternalServerError, "failed to update status")
		return
	}

	s.respondJSON(w, http.StatusOK, feedback)
}

func (s *HTTPServer) getBoard(w http.ResponseWriter, r *http.Request) {
	var category *models.FeedbackCategory
	var status *models.FeedbackStatus
	var search *string

	if categoryStr := r.URL.Query().Get("category"); categoryStr != "" {
		c := models.FeedbackCategory(categoryStr)
		category = &c
	}

	if statusStr := r.URL.Query().Get("status"); statusStr != "" {
		s := models.FeedbackStatus(statusStr)
		status = &s
	}

	if searchStr := r.URL.Query().Get("search"); searchStr != "" {
		search = &searchStr
	}

	sort := r.URL.Query().Get("sort")
	if sort == "" {
		sort = "created"
	}

	limit := 20
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil {
			offset = o
		}
	}

	response, err := s.feedbackService.GetBoard(r.Context(), category, status, search, sort, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get board")
		s.respondError(w, http.StatusInternalServerError, "failed to get board")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) vote(w http.ResponseWriter, r *http.Request) {
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

	vars := mux.Vars(r)
	feedbackID, err := uuid.Parse(vars["id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid feedback id")
		return
	}

	response, err := s.feedbackService.Vote(r.Context(), feedbackID, playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to vote")
		s.respondError(w, http.StatusInternalServerError, "failed to vote")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) unvote(w http.ResponseWriter, r *http.Request) {
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

	vars := mux.Vars(r)
	feedbackID, err := uuid.Parse(vars["id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid feedback id")
		return
	}

	response, err := s.feedbackService.Unvote(r.Context(), feedbackID, playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to unvote")
		s.respondError(w, http.StatusInternalServerError, "failed to unvote")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getStats(w http.ResponseWriter, r *http.Request) {
	stats, err := s.feedbackService.GetStats(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get stats")
		s.respondError(w, http.StatusInternalServerError, "failed to get stats")
		return
	}

	s.respondJSON(w, http.StatusOK, stats)
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
			"method":     r.Method,
			"path":       r.URL.Path,
			"status":     recorder.statusCode,
			"duration":   duration,
			"remote_addr": r.RemoteAddr,
		}).Info("HTTP request")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start).Seconds()
		RecordRequest(r.Method, r.URL.Path, strconv.Itoa(recorder.statusCode))
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
		if !s.authEnabled {
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
			s.logger.WithError(err).Warn("JWT verification failed")
			s.respondError(w, http.StatusUnauthorized, "invalid token")
			return
		}

		ctx := context.WithValue(r.Context(), "user_id", claims.Subject)
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




