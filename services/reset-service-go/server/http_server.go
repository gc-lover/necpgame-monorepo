package server

// HTTP handlers use context.WithTimeout for request timeouts (see handlers.go)

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/reset-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr         string
	router       *http.ServeMux
	resetService ResetServiceInterface
	logger       *logrus.Logger
	server       *http.Server
}

func NewHTTPServer(addr string, resetService ResetServiceInterface) *HTTPServer {
	router := http.NewServeMux()

	server := &HTTPServer{
		addr:         addr,
		router:       router,
		resetService: resetService,
		logger:       GetLogger(),
	}

	handlers := NewHandlers(resetService)

	ogenServer, err := api.NewServer(handlers)
	if err != nil {
		panic(err)
	}

	var handler http.Handler = http.StripPrefix("/api/v1", ogenServer)
	handler = server.loggingMiddleware(handler)
	handler = server.metricsMiddleware(handler)
	handler = server.corsMiddleware(handler)
	router.Handle("/api/v1/", handler)
	router.Handle("/api/v1/reset", handler)
	router.Handle("/api/v1/reset/", handler)
	router.Handle("/api/v1/reset/stats", handler)
	router.Handle("/api/v1/reset/history", handler)
	router.Handle("/api/v1/reset/trigger", handler)
	router.Handle("/", handler)

	router.HandleFunc("/health", server.healthCheck)

	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  30 * time.Second,  // Prevent slowloris attacks,
		WriteTimeout: 30 * time.Second,  // Prevent hanging writes,
		IdleTimeout:  120 * time.Second, // Keep connections alive for reuse,
	}

	errChan := make(chan error, 1)
	go func() {
			defer close(errChan)
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

// Issue: #1364
func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")

	jsonData, err := json.Marshal(data)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal JSON response")
		w.WriteHeader(http.StatusInternalServerError)
		errorResponse := api.Error{
			Error:   http.StatusText(http.StatusInternalServerError),
			Message: "failed to encode response",
		}
		if encodeErr := json.NewEncoder(w).Encode(errorResponse); encodeErr != nil {
			s.logger.WithError(encodeErr).Error("Failed to encode error response")
		}
		return
	}

	w.WriteHeader(status)
	if _, err := w.Write(jsonData); err != nil {
		s.logger.WithError(err).Error("Failed to write JSON response")
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



