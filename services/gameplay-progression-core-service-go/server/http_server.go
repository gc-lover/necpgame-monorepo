// Issue: #164
package server

import (
	"context"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/necpgame/gameplay-progression-core-service-go/pkg/api"
)

type HTTPServer struct {
	addr   string
	router chi.Router
	server *http.Server
}

func NewHTTPServer(addr string) *HTTPServer {
	router := chi.NewRouter()

	// Middleware
	router.Use(loggingMiddleware)
	router.Use(metricsMiddleware)
	router.Use(corsMiddleware)

	// Health check
	router.Get("/health", healthCheck)

	// Handlers
	handlers := NewHandlers()

	// Интеграция с oapi-codegen (chi)
	api.HandlerWithOptions(handlers, api.ChiServerOptions{
		BaseURL:    "/api/v1",
		BaseRouter: router,
	})

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:    addr,
			Handler: router,
		},
	}
}

func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

