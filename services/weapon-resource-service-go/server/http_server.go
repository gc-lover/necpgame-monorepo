// Issue: #1574
package server

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/gc-lover/necpgame-monorepo/services/weapon-resource-service-go/pkg/api"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

// HTTPServer represents HTTP server
type HTTPServer struct {
	addr    string
	router  chi.Router
	service *Service
}

// NewHTTPServer creates HTTP server with DI
func NewHTTPServer(addr string, db *sql.DB) *HTTPServer {
	router := chi.NewRouter()

	// Create dependencies
	repo := NewRepository(db)
	service := NewService(repo)
	handlers := NewHandlers(service)

	// Apply middleware from middleware.go
	router.Use(LoggingMiddleware)
	router.Use(RecoveryMiddleware)
	router.Use(CORSMiddleware)

	// Register API handlers
	api.HandlerWithOptions(handlers, api.ChiServerOptions{
		BaseURL:    "/api/v1",
		BaseRouter: router,
	})

	// Health and metrics
	router.Get("/health", healthCheck)
	router.Handle("/metrics", promhttp.Handler())

	return &HTTPServer{
		addr:    addr,
		router:  router,
		service: service,
	}
}

// Start starts the HTTP server
func (s *HTTPServer) Start() error {
	fmt.Printf("Starting Weapon Resource Service on %s\n", s.addr)
	return http.ListenAndServe(s.addr, s.router)
}

// Shutdown gracefully shuts down the server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	// Graceful shutdown logic
	return nil
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

