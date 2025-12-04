// Issue: #1578
// OPTIMIZED: No chi dependency, standard http.ServeMux + middleware chain
// PERFORMANCE: OGEN routes (hot path) already maximum speed, removed chi overhead from health/metrics
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-ogen-go/pkg/api"
	"github.com/google/uuid"
)

// HTTPServer represents HTTP server (no chi dependency)
type HTTPServer struct {
	addr   string
	server *http.Server
}

// NewHTTPServer creates new HTTP server WITHOUT chi
// PERFORMANCE: Standard mux for health/metrics, OGEN router for API (already max speed)
// SOLID: ТОЛЬКО настройка сервера и роутера. Middleware в middleware.go, Handlers в handlers.go
func NewHTTPServer(addr string, service *Service) *HTTPServer {
	// OGEN server (fast static router - hot path)
	handlers := NewHandlers(service)
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Standard mux (for health/metrics - cold path)
	mux := http.NewServeMux()

	// Middleware chain (no duplication, optimized)
	apiHandler := chainMiddleware(ogenServer,
		recoveryMiddleware,  // panic recovery
		requestIDMiddleware,  // request ID
		LoggingMiddleware,    // structured logging
		MetricsMiddleware,    // metrics
	)

	// Mount OGEN (hot path - maximum speed, static router)
	mux.Handle("/api/v1/", apiHandler)

	// Health/metrics (cold path - simple mux, no chi overhead)
	mux.HandleFunc("/health", healthCheck)
	mux.HandleFunc("/metrics", metricsHandler)

	return &HTTPServer{
		addr: addr,
		server: &http.Server{
			Addr:         addr,
			Handler:      mux,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  60 * time.Second,
		},
	}
}

// chainMiddleware chains middleware functions (simple and fast)
func chainMiddleware(h http.Handler, mws ...func(http.Handler) http.Handler) http.Handler {
	for i := len(mws) - 1; i >= 0; i-- {
		h = mws[i](h)
	}
	return h
}

// recoveryMiddleware recovers from panics
func recoveryMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if err := recover(); err != nil {
				http.Error(w, "Internal Server Error", http.StatusInternalServerError)
			}
		}()
		next.ServeHTTP(w, r)
	})
}

// requestIDMiddleware adds request ID to headers
func requestIDMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		requestID := r.Header.Get("X-Request-ID")
		if requestID == "" {
			requestID = uuid.New().String()
		}
		w.Header().Set("X-Request-ID", requestID)
		next.ServeHTTP(w, r)
	})
}

// Start starts HTTP server
func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down HTTP server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// Health check handler
func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

// Metrics handler (stub)
func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("# HELP combat_combos_service metrics\n"))
}
