// Issue: #44
package server

import (
	"context"
	"net/http"

	"github.com/go-chi/chi/v5"
	chiMiddleware "github.com/go-chi/chi/v5/middleware"
	"github.com/gc-lover/necpgame-monorepo/services/world-events-core-service-go/pkg/api"
	"go.uber.org/zap"
)

type HTTPServer struct {
	addr   string
	router chi.Router
	server *http.Server
	logger *zap.Logger
}

func NewHTTPServer(addr string, service Service, logger *zap.Logger) *HTTPServer {
	router := chi.NewRouter()

	router.Use(chiMiddleware.Logger)
	router.Use(chiMiddleware.Recoverer)
	router.Use(chiMiddleware.RequestID)
	router.Use(CORSMiddleware())
	router.Use(LoggingMiddleware(logger))
	router.Use(MetricsMiddleware())

	handlers := NewHandlers(service, logger)
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		logger.Fatal("Failed to create ogen server", zap.Error(err))
	}

	router.Mount("/api/v1", ogenServer)

	router.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

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

func (s *HTTPServer) Start() error {
	s.logger.Info("Starting HTTP server", zap.String("addr", s.addr))
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down HTTP server")
	return s.server.Shutdown(ctx)
}
