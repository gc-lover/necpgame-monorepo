// Issue: #44
package server

import (
	"context"
	"net/http"

	"github.com/go-chi/chi/v5"
	api "github.com/gc-lover/necpgame-monorepo/services/world-events-analytics-service-go/pkg/api"
	"go.uber.org/zap"
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









