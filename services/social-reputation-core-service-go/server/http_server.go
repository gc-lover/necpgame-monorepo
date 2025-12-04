// Issue: #1595
package server

import (
	"context"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/gc-lover/necpgame-monorepo/services/social reputation core/pkg/api"
)

// HTTPServer represents HTTP server
type HTTPServer struct {
	addr   string
	server *http.Server
	router chi.Router
}

// NewHTTPServer creates new HTTP server
// SOLID: ТОЛЬКО настройка сервера и роутера. Middleware в middleware.go, Handlers в handlers.go
func NewHTTPServer(addr string, service *Service) *HTTPServer {
	router := chi.NewRouter()

	// Built-in middleware
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.RequestID)

	// Custom middleware (из middleware.go)
	router.Use(LoggingMiddleware)
	router.Use(MetricsMiddleware)

	// Handlers (реализация api.Handler из handlers.go)
	handlers := NewHandlers(service)

	// Integration with ogen (creates its own Chi router)
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}
	
	// Mount ogen server under /api/v1
	router.Mount("/api/v1", ogenServer)

	// Health check
	router.Get("/health", healthCheck)
	router.Get("/metrics", metricsHandler)

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:    addr,
			Handler: router,
		},
	}
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
	w.Write([]byte("# HELP combat_actions_service metrics\n"))
}

