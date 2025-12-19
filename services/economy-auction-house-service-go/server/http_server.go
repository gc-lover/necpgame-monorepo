// Issue: #61 - Player Market HTTP Server
package server

import (
	"context"
		"time"
	"net/http"
)

// HTTPServer wraps HTTP server
type HTTPServer struct {
	addr   string
	server *http.Server
}

// NewHTTPServer creates HTTP server
func NewHTTPServer(addr string, service *PlayerMarketService) *HTTPServer {
	return &HTTPServer{
		addr: addr,
		server: &http.Server{
			Addr:    addr,
			Handler: http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
				w.WriteHeader(http.StatusOK)
				w.Write([]byte("OK"))
			}),
		},
	}
}

// Start starts server
func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}



