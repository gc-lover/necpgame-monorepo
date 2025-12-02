// Issue: #142
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/gc-lover/necpgame/services/loot-service-go/pkg/api"
)

type HTTPServer struct {
	addr    string
	router  *chi.Mux
	service Service
}

func NewHTTPServer(addr string, service Service) *HTTPServer {
	router := chi.NewRouter()
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.Timeout(60 * time.Second))

	handlers := NewHandlers(service)
	api.HandlerWithOptions(handlers, api.ChiServerOptions{
		BaseURL:    "/api/v1",
		BaseRouter: router,
	})

	router.Get("/health", healthCheck)
	router.Get("/metrics", metricsHandler)

	return &HTTPServer{addr: addr, router: router, service: service}
}

func (s *HTTPServer) Start() error {
	return http.ListenAndServe(s.addr, s.router)
}

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

