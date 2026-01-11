package server

import (
	"log/slog"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5/middleware"
)

// Middleware contains all middleware functions
type Middleware struct{}

// NewMiddleware creates middleware instance
func NewMiddleware() *Middleware {
	return &Middleware{}
}

// RequestID adds request ID to context
func (m *Middleware) RequestID(next http.Handler) http.Handler {
	return middleware.RequestID(next)
}

// RealIP gets the real IP from headers
func (m *Middleware) RealIP(next http.Handler) http.Handler {
	return middleware.RealIP(next)
}

// Logger logs HTTP requests with structured logging
func (m *Middleware) Logger(next http.Handler) http.Handler {
	return middleware.RequestLogger(&middleware.DefaultLogFormatter{
		Logger:  slog.Default(),
		NoColor: true,
	})(next)
}

// Recoverer recovers from panics
func (m *Middleware) Recoverer(next http.Handler) http.Handler {
	return middleware.Recoverer(next)
}

// Timeout adds timeout to requests
func (m *Middleware) Timeout(timeout time.Duration) func(http.Handler) http.Handler {
	return middleware.Timeout(timeout)
}

// CORS handles CORS headers
func (m *Middleware) CORS(next http.Handler) http.Handler {
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

// Auth performs JWT authentication
func (m *Middleware) Auth(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// TODO: Implement JWT authentication
		// For now, allow all requests
		next.ServeHTTP(w, r)
	})
}

// Metrics collects request metrics
func (m *Middleware) Metrics(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Create a response writer wrapper to capture status code
		wrapper := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		next.ServeHTTP(wrapper, r)

		duration := time.Since(start)

		// Log metrics
		slog.Info("Request completed",
			"method", r.Method,
			"path", r.URL.Path,
			"status", wrapper.statusCode,
			"duration_ms", duration.Milliseconds(),
		)
	})
}

// responseWriter wraps http.ResponseWriter to capture status code
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}