// Issue: #150 - Matchmaking HTTP Server (ogen-based)
// SOLID: ТОЛЬКО настройка сервера. Middleware в middleware.go, Handlers в handlers.go
package server

import (
	"context"
	"log"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	
	api "github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
)

// HTTPServer represents HTTP server
type HTTPServer struct {
	addr   string
	server *http.Server
	router chi.Router
}

// NewHTTPServer creates new HTTP server with ogen integration
// SOLID: ТОЛЬКО настройка сервера и роутера
func NewHTTPServer(addr string, service *Service) *HTTPServer {
	router := chi.NewRouter()

	// Built-in middleware
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.RequestID)
	router.Use(middleware.Timeout(60 * time.Second)) // Global timeout

	// Custom middleware
	router.Use(LoggingMiddleware)
	router.Use(MetricsMiddleware)

	// Handlers (реализация api.Handler из handlers.go)
	handlers := NewHandlers(service)

	// Security handler (JWT validation)
	secHandler := NewSecurityHandler()

	// Integration with ogen (creates its own router)
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		log.Fatalf("Failed to create ogen server: %v", err)
	}

	// Mount ogen server under /api/v1
	router.Mount("/api/v1", ogenServer)

	// Health and metrics
	router.Get("/health", healthCheck)
	router.Get("/metrics", metricsHandler)
	router.Get("/ready", readyCheck)

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:           addr,
			Handler:        router,
			ReadTimeout:    15 * time.Second,
			WriteTimeout:   15 * time.Second,
			IdleTimeout:    60 * time.Second,
			MaxHeaderBytes: 1 << 20, // 1 MB
		},
	}
}

// Start starts HTTP server
func (s *HTTPServer) Start() error {
	log.Printf("Matchmaking Service listening on %s", s.addr)
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down HTTP server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// Health check handler
func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy"}`))
}

// Ready check handler
func readyCheck(w http.ResponseWriter, r *http.Request) {
	// TODO: Check DB and Redis connectivity
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ready"}`))
}

// Metrics handler (Prometheus format)
func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusOK)
	// TODO: Collect real metrics
	w.Write([]byte("# HELP matchmaking_requests_total Total requests\n"))
	w.Write([]byte("# TYPE matchmaking_requests_total counter\n"))
	w.Write([]byte("matchmaking_requests_total 0\n"))
}

