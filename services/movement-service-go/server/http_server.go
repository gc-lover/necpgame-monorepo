package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"github.com/necpgame/movement-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr            string
	router          *mux.Router
	movementService *MovementService
	logger          *logrus.Logger
	server          *http.Server
}

func NewHTTPServer(addr string, movementService *MovementService) *HTTPServer {
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

	apiRouter := router.PathPrefix("/api/v1").Subrouter()

	handlers := NewHandlers(movementService)
	api.HandlerWithOptions(handlers, api.GorillaServerOptions{
		BaseRouter: apiRouter.PathPrefix("/movement").Subrouter(),
		ErrorHandlerFunc: func(w http.ResponseWriter, r *http.Request, err error) {
			w.WriteHeader(http.StatusBadRequest)
			json.NewEncoder(w).Encode(map[string]string{"error": err.Error()})
		},
	})

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

func HandlerFromMux(si api.ServerInterface, r *mux.Router) {
	wrapper := &api.ServerInterfaceWrapper{
		Handler: si,
	}

	r.HandleFunc("/movement/{characterId}/history", wrapper.GetPositionHistory).Methods("GET")
	r.HandleFunc("/movement/{characterId}/position", wrapper.GetPosition).Methods("GET")
	r.HandleFunc("/movement/{characterId}/position", wrapper.SavePosition).Methods("POST")
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
