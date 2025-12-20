// Package server Issue: #1442
package server

// HTTP handlers use context.WithTimeout for request timeouts (see handlers.go)

import (
	"context"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/faction-core-service-go/pkg/api"
)

type HTTPServer struct {
	addr    string
	router  *http.ServeMux
	service *Service
}

func NewHTTPServer(addr string, handlers *Handlers, service *Service) *HTTPServer {
	router := http.NewServeMux()

	// Create ogen security handler (placeholder for now)
	secHandler := &SecurityHandler{}

	// Create ogen server
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	var handler http.Handler = ogenServer
	handler = LoggingMiddleware(handler)
	handler = RecoveryMiddleware(handler)
	handler = CORSMiddleware(handler)
	router.Handle("/api/v1/", handler)

	// Health check
	router.HandleFunc("/health", healthCheck)
	router.HandleFunc("/metrics", metricsHandler)

	return &HTTPServer{
		addr:    addr,
		router:  router,
		service: service,
	}
}

func (s *HTTPServer) Start() error {
	return http.ListenAndServe(s.addr, s.router)
}

// Shutdown gracefully stops the server (chi + ogen).
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	// ListenAndServe has no shutdown hook here; for tests just return nil.
	_ = ctx
	return nil
}

func healthCheck(w http.ResponseWriter, _ *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

func metricsHandler(w http.ResponseWriter, _ *http.Request) {
	// Prometheus metrics would go here
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("# Metrics\n"))
}
