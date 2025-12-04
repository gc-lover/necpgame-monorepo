// Issue: #1595
package server

import (
	"context"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/gc-lover/necpgame-monorepo/services/combat-ai-service-go/pkg/api"
)

type HTTPServer struct {
	addr   string
	server *http.Server
	router chi.Router
}

func NewHTTPServer(addr string, service *Service) *HTTPServer {
	router := chi.NewRouter()
	router.Use(middleware.Logger, middleware.Recoverer, middleware.RequestID)
	router.Use(LoggingMiddleware, MetricsMiddleware)

	handlers := NewHandlers(service)
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}
	
	router.Mount("/api/v1", ogenServer)
	router.Get("/health", healthCheck)
	router.Get("/metrics", metricsHandler)

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{Addr: addr, Handler: router},
	}
}

func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("# HELP combat_ai_service metrics\n"))
}

