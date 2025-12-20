package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/crafting-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// HTTPServer represents the HTTP server
type HTTPServer struct {
	addr          string
	server        *http.Server
	logger        *logrus.Logger
	jwtValidator  *JwtValidator
	authEnabled   bool
}

// NewHTTPServer creates new HTTP server
func NewHTTPServer(addr string, handler *CraftingHandler) *HTTPServer {
	logger := GetLogger()

	// Create ogen server
	apiServer, err := api.NewServer(handler, nil)
	if err != nil {
		logger.WithError(err).Fatal("Failed to create API server")
	}

	var httpHandler http.Handler = apiServer

	// Add middleware
	httpHandler = NewAuthMiddleware(handler.jwtValidator, handler.authEnabled, logger)(httpHandler)
	httpHandler = NewLoggingMiddleware(logger)(httpHandler)
	httpHandler = NewTimeoutMiddleware(30*time.Second)(httpHandler) // PERFORMANCE: Context timeouts

	mux := http.NewServeMux()
	mux.Handle("/", httpHandler)

	// PERFORMANCE: Optimized server configuration
	httpServer := &http.Server{
		Addr:         addr,
		Handler:      mux,
		ReadTimeout:  30 * time.Second, // PERFORMANCE: Prevent slow loris attacks
		WriteTimeout: 30 * time.Second, // PERFORMANCE: Prevent hanging connections
		IdleTimeout:  120 * time.Second, // PERFORMANCE: Reuse connections
	}

	return &HTTPServer{
		addr:         addr,
		server:       httpServer,
		logger:       logger,
		jwtValidator: handler.jwtValidator,
		authEnabled:  handler.authEnabled,
	}
}

// Start starts the HTTP server
func (s *HTTPServer) Start(ctx context.Context) error {
	s.logger.WithField("addr", s.addr).Info("Starting HTTP server")

	// Graceful shutdown handling
	go func() {
		<-ctx.Done()
		s.logger.Info("Shutting down HTTP server...")
		shutdownCtx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
		defer cancel()
		s.server.Shutdown(shutdownCtx)
	}()

	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}
