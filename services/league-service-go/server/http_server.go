// Issue: #44
package server

import (
	"context"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/league-service-go/pkg/api"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
)

type HTTPServer struct {
	addr    string
	router  chi.Router
	service *LeagueService
	server  *http.Server
}

func NewHTTPServer(addr string, service *LeagueService) *HTTPServer {
	router := chi.NewRouter()

	// Global middleware
	router.Use(middleware.RequestID)
	router.Use(middleware.RealIP)
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(LoggingMiddleware)
	router.Use(MetricsMiddleware)

	// Create handlers
	handlers := NewHandlers(service)

	// Register API routes
	api.HandlerWithOptions(handlers, api.ChiServerOptions{
		BaseURL:    "/api/v1",
		BaseRouter: router,
	})

	// Health check
	router.Get("/health", healthCheck)
	router.Get("/metrics", metricsHandler)

	return &HTTPServer{
		addr:    addr,
		router:  router,
		service: service,
	}
}

func (s *HTTPServer) Start() error {
	s.server = &http.Server{
		Addr:    s.addr,
		Handler: s.router,
	}
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

func metricsHandler(w http.ResponseWriter, r *http.Request) {
	// Prometheus metrics endpoint
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("# Metrics placeholder\n"))
}

