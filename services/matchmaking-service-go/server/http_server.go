// Package server Issue: #1579 - ogen router + middleware
package server

// HTTP handlers use context.WithTimeout for request timeouts (see handlers.go)

import (
	"context"
	"net/http"
)

// HTTPServer wraps ogen server
type HTTPServer struct {
	addr        string
	service     Service
	server      *http.Server
	loadShedder *LoadShedder // Issue: #1588 - Resilience patterns
}

// NewHTTPServer создает HTTP server с ogen
func NewHTTPServer(addr string, service Service) *HTTPServer {
	return &HTTPServer{
		addr:        addr,
		service:     service,
		loadShedder: NewLoadShedder(3000), // Max 3000 concurrent (5k RPS service)
	}
}

// Start запускает HTTP server
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

// Shutdown gracefully shuts down server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}

// withCORS adds CORS headers

// loadSheddingMiddleware prevents overload by limiting concurrent requests
// Issue: #1588 - Resilience patterns
func (s *HTTPServer) loadSheddingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !s.loadShedder.Allow() {
			w.WriteHeader(http.StatusServiceUnavailable)
			w.Write([]byte(`{"error":"service overloaded, please try again later"}`))
			return
		}
		defer s.loadShedder.Done()
		next.ServeHTTP(w, r)
	})
}
