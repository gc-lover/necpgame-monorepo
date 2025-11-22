package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/movement-service-go/models"
	"github.com/sirupsen/logrus"
)

type MovementServiceInterface interface {
	GetPosition(ctx context.Context, characterID uuid.UUID) (*models.CharacterPosition, error)
	SavePosition(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) (*models.CharacterPosition, error)
	GetPositionHistory(ctx context.Context, characterID uuid.UUID, limit int) ([]models.PositionHistory, error)
}

type HTTPServer struct {
	addr            string
	router          *mux.Router
	movementService MovementServiceInterface
	logger          *logrus.Logger
	server          *http.Server
}

func NewHTTPServer(addr string, movementService MovementServiceInterface) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:            addr,
		router:          router,
		movementService: movementService,
		logger:          GetLogger(),
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1").Subrouter()

	api.HandleFunc("/movement/{characterId}/position", server.getPosition).Methods("GET")
	api.HandleFunc("/movement/{characterId}/position", server.savePosition).Methods("POST")
	api.HandleFunc("/movement/{characterId}/history", server.getPositionHistory).Methods("GET")

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

func (s *HTTPServer) getPosition(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	position, err := s.movementService.GetPosition(r.Context(), characterID)
	if err != nil {
		if err.Error() == "position not found" {
			s.respondError(w, http.StatusNotFound, "position not found")
			return
		}
		s.logger.WithError(err).Error("Failed to get position")
		s.respondError(w, http.StatusInternalServerError, "failed to get position")
		return
	}

	if position == nil {
		s.respondError(w, http.StatusNotFound, "position not found")
		return
	}

	s.respondJSON(w, http.StatusOK, position)
}

func (s *HTTPServer) savePosition(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	var req models.SavePositionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	position, err := s.movementService.SavePosition(r.Context(), characterID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to save position")
		s.respondError(w, http.StatusInternalServerError, "failed to save position")
		return
	}

	s.respondJSON(w, http.StatusOK, position)
}

func (s *HTTPServer) getPositionHistory(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	limitStr := r.URL.Query().Get("limit")
	limit := 50
	if limitStr != "" {
		var err error
		limit, err = strconv.Atoi(limitStr)
		if err != nil || limit <= 0 || limit > 100 {
			limit = 50
		}
	}

	history, err := s.movementService.GetPositionHistory(r.Context(), characterID, limit)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get position history")
		s.respondError(w, http.StatusInternalServerError, "failed to get position history")
		return
	}

	s.respondJSON(w, http.StatusOK, history)
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

		next.ServeHTTP(w, r)

		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": duration.Milliseconds(),
			"status":      http.StatusOK,
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
