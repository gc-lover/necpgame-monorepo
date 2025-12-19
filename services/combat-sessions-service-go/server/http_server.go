// Issue: #1595
// OPTIMIZED: No chi dependency, standard http.ServeMux + middleware chain
// PERFORMANCE: OGEN routes (hot path) already maximum speed, removed chi overhead from health/metrics
package server

// HTTP handlers use context.WithTimeout for request timeouts (see handlers.go)

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sessions-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

// HTTPServer represents HTTP server (no chi dependency)
type HTTPServer struct {
	addr   string
	server *http.Server
	logger *logrus.Logger
}

// NewHTTPServer creates new HTTP server WITHOUT chi
// PERFORMANCE: Standard mux for health/metrics, OGEN router for API (already max speed)
func NewHTTPServer(addr string, logger *logrus.Logger, service *CombatSessionService) *HTTPServer {
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
		recoveryMiddleware(logger),  // panic recovery
		requestIDMiddleware,  // request ID
		loggingMiddleware(logger),    // structured logging
		CORSMiddleware(),       // CORS
	)

	// Mount OGEN (hot path - maximum speed, static router)
	mux.Handle("/api/v1/", apiHandler)

	// Health/metrics (cold path - simple mux, no chi overhead)
	mux.Handle("/metrics", promhttp.Handler())
	mux.HandleFunc("/health", healthCheckHandler)

	return &HTTPServer{
		addr: addr,
		logger: logger,
		server: &http.Server{
			Addr:         addr,
			Handler:      mux,
			ReadTimeout:  30 * time.Second,  // Prevent slowloris attacks,
			WriteTimeout: 30 * time.Second,  // Prevent hanging writes,
			IdleTimeout:  120 * time.Second, // Keep connections alive for reuse,
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
func recoveryMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			defer func() {
				if err := recover(); err != nil {
					logger.WithField("error", err).Error("Panic recovered")
					http.Error(w, "Internal Server Error", http.StatusInternalServerError)
				}
			}()
			next.ServeHTTP(w, r)
		})
	}
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

// loggingMiddleware logs HTTP requests
func loggingMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			next.ServeHTTP(w, r)
			logger.WithFields(logrus.Fields{
				"method":   r.Method,
				"path":     r.URL.Path,
				"duration": time.Since(start),
			}).Info("HTTP request processed")
		})
	}
}

// Start starts HTTP server
func (s *HTTPServer) Start() error {
	// Start server in background with proper goroutine management
	errChan := make(chan error, 1)
	go func() {
		defer close(errChan)
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	// Wait indefinitely (server runs until shutdown)
	err := <-errChan
	return err
}

// Shutdown gracefully shuts down HTTP server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down HTTP server")
	return s.server.Shutdown(ctx)
}

// Health check handler
func healthCheckHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy"}`))
}



