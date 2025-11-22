package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr          string
	router        *mux.Router
	socialService *SocialService
	logger        *logrus.Logger
	server        *http.Server
	jwtValidator  *JwtValidator
	authEnabled   bool
}

func NewHTTPServer(addr string, socialService *SocialService, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:          addr,
		router:        router,
		socialService: socialService,
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

	social := api.PathPrefix("/social").Subrouter()

	social.HandleFunc("/notifications", server.createNotification).Methods("POST")
	social.HandleFunc("/notifications", server.getNotifications).Methods("GET")
	social.HandleFunc("/notifications/{id}", server.getNotification).Methods("GET")
	social.HandleFunc("/notifications/{id}/status", server.updateNotificationStatus).Methods("PUT")

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

func (s *HTTPServer) createNotification(w http.ResponseWriter, r *http.Request) {
	var req models.CreateNotificationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.AccountID == uuid.Nil {
		s.respondError(w, http.StatusBadRequest, "account_id is required")
		return
	}

	if req.Title == "" {
		s.respondError(w, http.StatusBadRequest, "title is required")
		return
	}

	notification, err := s.socialService.CreateNotification(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create notification")
		s.respondError(w, http.StatusInternalServerError, "failed to create notification")
		return
	}

	s.respondJSON(w, http.StatusCreated, notification)
}

func (s *HTTPServer) getNotifications(w http.ResponseWriter, r *http.Request) {
	accountIDStr := r.URL.Query().Get("account_id")
	if accountIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "account_id query parameter is required")
		return
	}

	accountID, err := uuid.Parse(accountIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid account id")
		return
	}

	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsedLimit, err := strconv.Atoi(limitStr); err == nil && parsedLimit > 0 && parsedLimit <= 100 {
			limit = parsedLimit
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsedOffset, err := strconv.Atoi(offsetStr); err == nil && parsedOffset >= 0 {
			offset = parsedOffset
		}
	}

	response, err := s.socialService.GetNotifications(r.Context(), accountID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get notifications")
		s.respondError(w, http.StatusInternalServerError, "failed to get notifications")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getNotification(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	notificationIDStr := vars["id"]

	notificationID, err := uuid.Parse(notificationIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid notification id")
		return
	}

	notification, err := s.socialService.GetNotification(r.Context(), notificationID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get notification")
		s.respondError(w, http.StatusInternalServerError, "failed to get notification")
		return
	}

	if notification == nil {
		s.respondError(w, http.StatusNotFound, "notification not found")
		return
	}

	s.respondJSON(w, http.StatusOK, notification)
}

func (s *HTTPServer) updateNotificationStatus(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	notificationIDStr := vars["id"]

	notificationID, err := uuid.Parse(notificationIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid notification id")
		return
	}

	var req models.UpdateNotificationStatusRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	notification, err := s.socialService.UpdateNotificationStatus(r.Context(), notificationID, req.Status)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update notification status")
		s.respondError(w, http.StatusInternalServerError, "failed to update notification status")
		return
	}

	if notification == nil {
		s.respondError(w, http.StatusNotFound, "notification not found")
		return
	}

	s.respondJSON(w, http.StatusOK, notification)
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
		ctx = context.WithValue(ctx, "user_id", claims.Subject)
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
