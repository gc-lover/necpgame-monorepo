// Issue: #1510
package server

import (
	"context"
	"log"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-acrobatics-wall-run-service-go/pkg/api"
)

type HTTPServer struct {
	addr   string
	router *http.ServeMux
	server *http.Server
}

func NewHTTPServer(addr string, handlers api.Handler) *HTTPServer {
	router := http.NewServeMux()

	// ogen server integration
	secHandler := &SecurityHandler{}
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
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  10 * time.Second,
			WriteTimeout: 10 * time.Second,
			IdleTimeout:  30 * time.Second,
		},
	}
}

func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Stop(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

func metricsHandler(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement Prometheus metrics handler
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("Metrics not implemented yet"))
}
