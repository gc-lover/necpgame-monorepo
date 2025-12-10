// Issue: #1595
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr   string
	router *http.ServeMux
	logger *logrus.Logger
	server *http.Server
}

func NewHTTPServer(addr string, service *Service) *HTTPServer {
	logger := GetLogger()
	mux := http.NewServeMux()

	// Handlers (реализация api.Handler из handlers.go)
	handlers := NewHandlers(service)

	// Integration with ogen
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Mount ogen server under /api/v1
	var handler http.Handler = ogenServer
	handler = LoggingMiddleware(handler)
	handler = MetricsMiddleware(handler)
	handler = http.TimeoutHandler(handler, 60*time.Second, "request timed out")

	mux.Handle("/api/v1", handler)
	mux.HandleFunc("/health", healthCheck)

	server := &HTTPServer{
		addr:   addr,
		router: mux,
		logger: logger,
		server: &http.Server{
			Addr:         addr,
			Handler:      mux,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  60 * time.Second,
		},
	}

	return server
}

func (s *HTTPServer) Start() error {
	s.logger.WithField("addr", s.addr).Info("Combat Combos Service starting")
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down Combat Combos Service")
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}
