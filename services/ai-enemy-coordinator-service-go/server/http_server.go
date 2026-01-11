package server

import (
	"context"
	"crypto/tls"
	"net/http"
	"time"

	"necpgame/services/ai-enemy-coordinator-service-go/pkg/api"
)

// HTTPServer handles HTTP server setup and lifecycle
type HTTPServer struct {
	server *http.Server
	api    *api.Server
}

// NewHTTPServer creates a new HTTP server instance
func NewHTTPServer(addr string, handler http.Handler) *HTTPServer {
	return &HTTPServer{
		server: &http.Server{
			Addr:         addr,
			Handler:      handler,
			ReadTimeout:  30 * time.Second,  // Performance: HTTP server timeouts
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

// StartTLS starts the HTTPS server
func (s *HTTPServer) StartTLS(certFile, keyFile string) error {
	return s.server.ListenAndServeTLS(certFile, keyFile)
}

// Stop gracefully stops the HTTP server
func (s *HTTPServer) Stop(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// SetTimeouts configures server timeouts for performance optimization
func (s *HTTPServer) SetTimeouts(readTimeout, writeTimeout, idleTimeout time.Duration) {
	s.server.ReadTimeout = readTimeout
	s.server.WriteTimeout = writeTimeout
	s.server.IdleTimeout = idleTimeout
}