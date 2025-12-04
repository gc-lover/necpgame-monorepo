// Issue: ogen migration
package server

import (
	"context"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr            string
	router          chi.Router
	feedbackService FeedbackServiceInterface
	logger          *logrus.Logger
	server          *http.Server
	jwtValidator    *JwtValidator
	authEnabled     bool
}

func NewHTTPServer(addr string, feedbackService FeedbackServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := chi.NewRouter()

	// Built-in middleware
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.RequestID)

	// Custom middleware
	router.Use(loggingMiddleware)
	router.Use(metricsMiddleware)
	router.Use(corsMiddleware)

	// Handlers (реализация api.Handler из handlers.go)
	handlers := NewHandlers(feedbackService)

	// Security handler
	secHandler := NewSecurityHandler(jwtValidator, authEnabled)

	// Integration with ogen
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Mount ogen server under /api/v1/feedback
	router.Mount("/api/v1/feedback", ogenServer)

	// Health check
	router.Get("/health", healthCheck)

	return &HTTPServer{
		addr:            addr,
		router:          router,
		feedbackService: feedbackService,
		logger:          GetLogger(),
		jwtValidator:    jwtValidator,
		authEnabled:     authEnabled,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  60 * time.Second,
		},
	}
}

func (s *HTTPServer) Start() error {
	s.logger.WithField("addr", s.addr).Info("Starting HTTP server")
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Stop(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy"}`))
}

func loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start)
		logger := GetLogger()
		logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"status":      recorder.statusCode,
			"duration":    duration,
			"remote_addr": r.RemoteAddr,
		}).Info("HTTP request")
	})
}

func metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start).Seconds()
		RecordRequest(r.Method, r.URL.Path, strconv.Itoa(recorder.statusCode))
		RecordRequestDuration(r.Method, r.URL.Path, duration)
	})
}

func corsMiddleware(next http.Handler) http.Handler {
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
