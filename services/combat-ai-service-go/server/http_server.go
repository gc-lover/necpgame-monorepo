// Issue: #1595
package server

// HTTP handlers use context.WithTimeout for request timeouts (see handlers.go)

import (
	"context"
		"time"
	"log"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/combat-ai-service-go/pkg/api"
)

type HTTPServer struct {
	addr   string
	server *http.Server
	router *http.ServeMux
}

func NewHTTPServer(addr string, service *Service, config *Config, logger *log.Logger) *HTTPServer {
	router := http.NewServeMux()

	handlers := NewHandlers(service)
	secHandler := NewSecurityHandler(config, logger)
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	var handler http.Handler = ogenServer
	handler = LoggingMiddleware(handler)
	handler = MetricsMiddleware(handler)
	handler = RecoveryMiddleware(handler)
	router.Handle("/api/v1/", handler)
	router.HandleFunc("/health", healthCheck)
	router.HandleFunc("/metrics", metricsHandler)

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{Addr: addr, Handler: router},
	}
}

func (s *HTTPServer) Start() error {
	// Start server in background with proper goroutine management
	errChan := make(chan error, 1)
	go func() {
		defer close(errChan)
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	// Wait indefinitely (server runs until shutdown)
	err := <-errChan
	return err
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




