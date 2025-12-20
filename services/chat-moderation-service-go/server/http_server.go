// Issue: #1911
// OPTIMIZED: Standard http.ServeMux for performance
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/chat-moderation-service-go/pkg/api"
)

// HTTPServer represents HTTP server
// OPTIMIZATION: Struct alignment - pointer first (8 bytes), then string (16 bytes)
type HTTPServer struct {
	server *http.Server // 8 bytes
	addr   string       // 16 bytes
}

// NewHTTPServer creates new HTTP server
func NewHTTPServer(addr string, service *Service) *HTTPServer {
	// OGEN server (fast static router)
	handlers := NewHandlers(service)
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Standard mux for health/metrics
	mux := http.NewServeMux()

	// Middleware chain
	apiHandler := chainMiddleware(ogenServer,
		RecoveryMiddleware,
		RequestIDMiddleware,
		LoggingMiddleware,
		MetricsMiddleware,
	)

	// Mount OGEN (hot path)
	mux.Handle("/api/v1/", apiHandler)

	// Health/metrics (cold path)
	mux.HandleFunc("/health", healthCheck)
	mux.HandleFunc("/metrics", metricsHandler)

	return &HTTPServer{
		addr: addr,
		server: &http.Server{
			Addr:         addr,
			Handler:      mux,
			ReadTimeout:  30 * time.Second,
			WriteTimeout: 30 * time.Second,
			IdleTimeout:  120 * time.Second,
		},
	}
}

// chainMiddleware chains middleware functions
func chainMiddleware(h http.Handler, mws ...func(http.Handler) http.Handler) http.Handler {
	for i := len(mws) - 1; i >= 0; i-- {
		h = mws[i](h)
	}
	return h
}

// Start starts HTTP server
func (s *HTTPServer) Start() error {
	GetLogger().WithField("addr", s.addr).Info("Starting Chat Moderation HTTP server")
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
	w.Write([]byte("# HELP chat_moderation_service metrics\n"))
}
