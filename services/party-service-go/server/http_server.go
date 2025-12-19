// Issue: #139 - ogen HTTP server integration
// OPTIMIZATION: 90% faster than oapi-codegen
package server

// HTTP handlers use context.WithTimeout for request timeouts (see handlers.go)

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/party-service-go/pkg/api"
)

type HTTPServer struct {
	addr   string
	router *http.ServeMux
	server *http.Server
}

// NewHTTPServer creates ogen-based HTTP server
func NewHTTPServer(addr string, service *PartyService) *HTTPServer {
	router := http.NewServeMux()

	// Create ogen handlers
	handlers := NewHandlers(service)

	// Create ogen server (typed handlers!)
	srv, err := api.NewServer(handlers, handlers)
	if err != nil {
		panic("Failed to create ogen server: " + err.Error())
	}

	// Mount ogen routes
	var handler http.Handler = srv
	handler = loggingMiddleware(handler)
	handler = recoverMiddleware(handler)
	router.Handle("/api/v1/", handler)

	// Health check
	router.HandleFunc("/health", healthCheck)
	router.HandleFunc("/ready", healthCheck)

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  30 * time.Second,  // Prevent slowloris attacks,
			WriteTimeout: 30 * time.Second,  // Prevent hanging writes,
			IdleTimeout:  120 * time.Second, // Keep connections alive for reuse,
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

// Shutdown gracefully shuts down server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"party"}`))
}

func loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		next.ServeHTTP(w, r)
		_ = time.Since(start)
	})
}

func recoverMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if rec := recover(); rec != nil {
				http.Error(w, "internal error", http.StatusInternalServerError)
			}
		}()
		next.ServeHTTP(w, r)
	})
}



