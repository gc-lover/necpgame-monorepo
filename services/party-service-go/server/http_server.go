// Issue: #139
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/gc-lover/necpgame/services/party-service-go/pkg/api"
)

// HTTPServer HTTP сервер
type HTTPServer struct {
	addr    string
	router  *chi.Mux
	service Service
}

// NewHTTPServer создает новый HTTP сервер
func NewHTTPServer(addr string, service Service) *HTTPServer {
	router := chi.NewRouter()

	// Middleware
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.Timeout(60 * time.Second))

	// Handlers
	handlers := NewHandlers(service)

	// Mount API
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

// Start запускает HTTP сервер
func (s *HTTPServer) Start() error {
	return http.ListenAndServe(s.addr, s.router)
}

// Shutdown graceful shutdown
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return nil
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}

func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
}

