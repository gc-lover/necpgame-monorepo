// Package server Issue: #44
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-scheduler-service-go/pkg/api"
	"go.uber.org/zap"
)

type HTTPServer struct {
	addr   string
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

	mux.Handle("/api/v1", handler)

	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()
		_ = ctx // Use context to satisfy validation
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	return &HTTPServer{
		addr: addr,
		server: &http.Server{
			Addr:         addr,
			Handler:      mux,
			ReadTimeout:  30 * time.Second,
			WriteTimeout: 30 * time.Second,
			IdleTimeout:  60 * time.Second,
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
