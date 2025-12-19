// Issue: #1595
package server

// HTTP handlers use context.WithTimeout for request timeouts (see handlers.go)

import (
	"context"
	"log"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/chat-service-go/pkg/api"
)

// HTTPServer represents HTTP server
type HTTPServer struct {
	addr   string
	server *http.Server
	router *http.ServeMux
}

// NewHTTPServer creates new HTTP server
// SOLID: ТОЛЬКО настройка сервера и роутера. Middleware в middleware.go, Handlers в handlers.go
func NewHTTPServer(addr string, service *Service, config *Config, logger *log.Logger) *HTTPServer {
	router := http.NewServeMux()

	// Handlers (реализация api.Handler из handlers.go)
	handlers := NewHandlers(service)

	// Integration with ogen (creates its own Chi router)
	secHandler := NewSecurityHandler(config, logger)
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Mount ogen server under /api/v1
	var handler http.Handler = ogenServer
	handler = LoggingMiddleware(handler)
	handler = MetricsMiddleware(handler)
	handler = TimeoutMiddleware(30 * time.Second)(handler) // PERFORMANCE: Request timeout
	router.Handle("/api/v1/", handler)

	// Health check
	router.HandleFunc("/health", healthCheck)
	router.HandleFunc("/metrics", metricsHandler)

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:    addr,
			Handler: router,
			ReadTimeout:  30 * time.Second,  // Prevent slowloris attacks
			WriteTimeout: 30 * time.Second,  // Prevent hanging writes
			IdleTimeout:  120 * time.Second, // Keep connections alive for reuse
		},
	}
}

// Start starts HTTP server
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

// Shutdown gracefully shuts down HTTP server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// Health check handler
func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

// Metrics handler (stub)
func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("# HELP combat_actions_service metrics\n"))
}



