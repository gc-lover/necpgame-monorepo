// Issue: #44
package server

import (
	"context"
	"net/http"

	"github.com/go-chi/chi/v5"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-scheduler-service-go/pkg/api"
)

type HTTPServer struct {
	addr   string
	server *http.Server
	logger *zap.Logger
}

func NewHTTPServer(addr string, handlers *Handlers, logger *zap.Logger) *HTTPServer {
	router := chi.NewRouter()

	router.Use(CORSMiddleware())
	router.Use(LoggingMiddleware(logger))

	api.HandlerWithOptions(handlers, api.ChiServerOptions{
		BaseURL:    "/api/v1",
		BaseRouter: router,
	})

	router.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	return &HTTPServer{
		addr: addr,
		server: &http.Server{
			Addr:    addr,
			Handler: router,
		},
		logger: logger,
	}
}

func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}








