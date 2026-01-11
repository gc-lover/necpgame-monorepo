package server

import (
	"context"
	"crypto/tls"
	"net/http"
	"time"
)

// HTTPServer handles HTTP server setup and lifecycle
type HTTPServer struct {
	server *http.Server
}

// NewHTTPServer creates a new HTTP server instance
func NewHTTPServer(addr string, handler http.Handler) *HTTPServer {
	return &HTTPServer{
		server: &http.Server{
			Addr:         addr,
			Handler:      handler,
			ReadTimeout:  30 * time.Second, // Performance: HTTP server timeouts
			WriteTimeout: 30 * time.Second, // Performance: HTTP server timeouts
			IdleTimeout:  120 * time.Second, // Performance: Connection reuse
			TLSConfig: &tls.Config{
				MinVersion: tls.VersionTLS12, // Security: TLS 1.2 minimum
			},
		},
	}
}

// Start starts the HTTP server
func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

// Stop gracefully stops the HTTP server
func (s *HTTPServer) Stop(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// AiBehaviorEngineServer wraps the handler for API server
type AiBehaviorEngineServer struct {
	handler *Handler
}

// NewAiBehaviorEngineServer creates a new API server instance
func NewAiBehaviorEngineServer(service Service) *AiBehaviorEngineServer {
	return &AiBehaviorEngineServer{
		handler: NewHandler(service),
	}
}