// Issue: #130

package server

import (
	"context"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/combat-sessions-service-go/pkg/api"
)

// HTTPServer represents HTTP server
type HTTPServer struct {
	addr    string
	router  chi.Router
	server  *http.Server
	service *CombatSessionService
}

// NewHTTPServer creates new HTTP server
func NewHTTPServer(addr string, service *CombatSessionService) *HTTPServer {
	router := chi.NewRouter()

	// Middleware
	router.Use(middleware.RequestID)
	router.Use(middleware.RealIP)
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(CORSMiddleware())
	router.Use(AuthMiddleware())
	router.Use(MetricsMiddleware())

	// Create handlers
	handlers := NewCombatSessionHandlers(service)

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

// Shutdown gracefully shuts down server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// Health check handler
func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy"}`))
}

// Metrics handler (placeholder)
func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("# Prometheus metrics\n"))
}

