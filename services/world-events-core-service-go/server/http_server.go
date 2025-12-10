// Issue: #44
package server

import (
	"context"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-core-service-go/pkg/api"
	"go.uber.org/zap"
)

type HTTPServer struct {
	addr   string
	router *http.ServeMux
	server *http.Server
	logger *zap.Logger
}

func NewHTTPServer(addr string, service Service, logger *zap.Logger) *HTTPServer {
	mux := http.NewServeMux()

	handlers := NewHandlers(service, logger)
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		logger.Fatal("Failed to create ogen server", zap.Error(err))
	}

	var handler http.Handler = ogenServer
	handler = CORSMiddleware()(handler)
	handler = LoggingMiddleware(logger)(handler)
	handler = MetricsMiddleware()(handler)

	mux.Handle("/api/v1", handler)

	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	mux.HandleFunc("/metrics", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("# HELP world_events_core_service World Events Core Service metrics\n"))
	})

	return &HTTPServer{
		addr:   addr,
		router: mux,
		server: &http.Server{
			Addr:    addr,
			Handler: mux,
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
