// Issue: #1637 - HTTP Server for P2P Trade Service
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/trade-p2p-service-go/pkg/api"
)

type HTTPServer struct {
	addr    string
	router  *http.ServeMux
	server  *http.Server
	service *Service
}

func NewHTTPServer(addr string, handlers *Handlers, service *Service) *HTTPServer {
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
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  60 * time.Second,
		},
		service: service,
	}
}

func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Stop(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// Middleware functions are in middleware.go

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(200)
	w.Write([]byte(`{"status":"ok"}`))
}

func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(200)
	w.Write([]byte(`{"metrics":"not implemented"}`))
}
