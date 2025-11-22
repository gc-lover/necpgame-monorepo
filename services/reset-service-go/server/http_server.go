package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/gorilla/mux"
	"github.com/necpgame/reset-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr         string
	router       *mux.Router
	resetService *ResetService
	logger       *logrus.Logger
	server       *http.Server
}

func NewHTTPServer(addr string, resetService *ResetService) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:         addr,
		router:       router,
		resetService: resetService,
		logger:       GetLogger(),
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1").Subrouter()

	api.HandleFunc("/reset/stats", server.getResetStats).Methods("GET")
	api.HandleFunc("/reset/history", server.getResetHistory).Methods("GET")
	api.HandleFunc("/reset/trigger", server.triggerReset).Methods("POST")

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

func (s *HTTPServer) getResetStats(w http.ResponseWriter, r *http.Request) {
	stats, err := s.resetService.GetResetStats(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get reset stats")
		s.respondError(w, http.StatusInternalServerError, "failed to get reset stats")
		return
	}

	s.respondJSON(w, http.StatusOK, stats)
}

func (s *HTTPServer) getResetHistory(w http.ResponseWriter, r *http.Request) {
	var resetType *models.ResetType
	if typeStr := r.URL.Query().Get("type"); typeStr != "" {
		rt := models.ResetType(typeStr)
		if rt == models.ResetTypeDaily || rt == models.ResetTypeWeekly {
			resetType = &rt
		}
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

	response, err := s.resetService.GetResetHistory(r.Context(), resetType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get reset history")
		s.respondError(w, http.StatusInternalServerError, "failed to get reset history")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) triggerReset(w http.ResponseWriter, r *http.Request) {
	var req models.TriggerResetRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Type != models.ResetTypeDaily && req.Type != models.ResetTypeWeekly {
		s.respondError(w, http.StatusBadRequest, "invalid reset type")
		return
	}

	err := s.resetService.TriggerReset(r.Context(), req.Type)
	if err != nil {
		s.logger.WithError(err).Error("Failed to trigger reset")
		s.respondError(w, http.StatusInternalServerError, "failed to trigger reset")
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

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}

