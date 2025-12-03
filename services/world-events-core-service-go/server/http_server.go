// Issue: #44
package server

import (
	"context"
	"net/http"

	"github.com/go-chi/chi/v5"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-core-service-go/pkg/api"
)

// HTTPServer - HTTP сервер
type HTTPServer struct {
	addr   string
	router chi.Router
	server *http.Server
	logger *zap.Logger
}

// NewHTTPServer создает новый HTTP сервер
func NewHTTPServer(addr string, handlers *Handlers, logger *zap.Logger) *HTTPServer {
	router := chi.NewRouter()

	// Middleware
	router.Use(CORSMiddleware())
	router.Use(LoggingMiddleware(logger))
	router.Use(MetricsMiddleware())

	// Register handlers using oapi-codegen generated router
	api.HandlerWithOptions(handlers, api.ChiServerOptions{
		BaseURL:    "/api/v1",
		BaseRouter: router,
	})

	// Health check
	router.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	// Metrics endpoint
	router.Get("/metrics", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("# HELP world_events_core_service World Events Core Service metrics\n"))
	})

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:    addr,
			Handler: router,
		},
		logger: logger,
	}
}

// Start запускает HTTP сервер
func (s *HTTPServer) Start() error {
	s.logger.Info("Starting HTTP server", zap.String("addr", s.addr))
	return s.server.ListenAndServe()
}

// Shutdown останавливает HTTP сервер
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down HTTP server")
	return s.server.Shutdown(ctx)
}




