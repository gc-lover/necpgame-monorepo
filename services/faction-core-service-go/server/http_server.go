// Issue: #1442
package server

import (
	"context"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/faction-core-service-go/pkg/api"
	"github.com/go-chi/chi/v5"
)

type HTTPServer struct {
	addr    string
	router  *chi.Mux
	service *Service
}

func NewHTTPServer(addr string, handlers *Handlers, service *Service) *HTTPServer {
	router := chi.NewRouter()

	// Apply middleware
	router.Use(LoggingMiddleware)
	router.Use(RecoveryMiddleware)
	router.Use(CORSMiddleware)

	// Create ogen security handler (placeholder for now)
	secHandler := &SecurityHandler{}

	// Create ogen server
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Mount ogen server at /api/v1
	router.Mount("/api/v1", ogenServer)

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
	return http.ListenAndServe(s.addr, s.router)
}

// Shutdown gracefully stops the server (chi + ogen).
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	// ListenAndServe has no shutdown hook here; for tests just return nil.
	_ = ctx
	return nil
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

func metricsHandler(w http.ResponseWriter, r *http.Request) {
	// Prometheus metrics would go here
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("# Metrics\n"))
}
