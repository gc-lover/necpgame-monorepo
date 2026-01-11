package server

import (
	"context"
	"crypto/tls"
	"time"

	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"necpgame/services/interactive-object-manager-service-go/pkg/api"
)

// HTTPServer handles HTTP server setup and lifecycle
type HTTPServer struct {
	server *echo.Echo
}

// NewHTTPServer creates a new HTTP server instance
func NewHTTPServer(addr string, handler api.ServerInterface) *HTTPServer {
	e := echo.New()

	// Performance: HTTP server timeouts
	e.Server.ReadTimeout = 30 * time.Second
	e.Server.WriteTimeout = 30 * time.Second
	e.Server.IdleTimeout = 120 * time.Second

	// Security: TLS configuration
	e.Server.TLSConfig = &tls.Config{
		MinVersion: tls.VersionTLS12,
	}

	// Middleware
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	e.Use(middleware.CORS())

	// Register API handlers
	api.RegisterHandlers(e, handler)

	return &HTTPServer{
		server: e,
	}
}

// Start starts the HTTP server
func (s *HTTPServer) Start(addr string) error {
	return s.server.Start(addr)
}

// StartTLS starts the HTTPS server
func (s *HTTPServer) StartTLS(addr, certFile, keyFile string) error {
	return s.server.StartTLS(addr, certFile, keyFile)
}

// Stop gracefully stops the HTTP server
func (s *HTTPServer) Stop(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}