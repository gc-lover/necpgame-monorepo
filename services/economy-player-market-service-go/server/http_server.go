// Package server Issue: #61 - Player Market HTTP Server
package server

import (
	"context"
	"net/http"
)

// HTTPServer wraps HTTP server
type HTTPServer struct {
	addr   string
	server *http.Server
}

// NewHTTPServer creates HTTP server

// Start starts server
func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}
