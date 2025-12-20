// Package server Issue: #1856
// OPTIMIZED: No chi dependency, standard http.ServeMux + middleware chain
// PERFORMANCE: OGEN routes (hot path) already maximum speed, removed chi overhead from health/metrics
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/guild-core-service-go/pkg/api"
	"github.com/google/uuid"
)

// HTTPServer represents HTTP server (no chi dependency)
type HTTPServer struct {
	addr   string
	server *http.Server
}

// NewHTTPServer creates new HTTP server WITHOUT chi
// PERFORMANCE: Standard mux for health/metrics, OGEN router for API (already max speed)
// SOLID: ТОЛЬКО настройка сервера и роутера. Middleware в middleware.go, Handlers в handlers.go
func NewHTTPServer(addr string, service *Service) *HTTPServer {
	// OGEN server (fast static router - hot path)
	handlers := NewHandlers(service)
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Standard mux (for health/metrics - cold path)
	mux := http.NewServeMux()

	// Middleware chain (no duplication, optimized)
	apiHandler := chainMiddleware(ogenServer,
		recoveryMiddleware,  // panic recovery
		requestIDMiddleware, // request ID
		LoggingMiddleware,   // structured logging
		MetricsMiddleware,   // metrics
	)

	// Mount OGEN (hot path - maximum speed, static router)
	mux.Handle("/api/v1/", apiHandler)

	// Health/metrics (cold path - simple mux, no chi overhead)
	mux.HandleFunc("/health", healthCheck)
	mux.HandleFunc("/metrics", metricsHandler)

	server := &http.Server{
		Addr:         addr,
		Handler:      mux,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return &HTTPServer{
		addr:   addr,
		server: server,
	}
}

// Start starts the HTTP server
func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// chainMiddleware applies middleware chain to handler
func chainMiddleware(handler http.Handler, middlewares ...func(http.Handler) http.Handler) http.Handler {
	for i := len(middlewares) - 1; i >= 0; i-- {
		handler = middlewares[i](handler)
	}
	return handler
}

// SecurityHandler implements security interface for ogen
type SecurityHandler struct{}

// HandleBearerAuth handles Bearer token authentication
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, _ string, _ api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT token validation
	// For now, just extract user ID from token
	userID, err := uuid.Parse("00000000-0000-0000-0000-000000000001") // Mock user ID
	if err != nil {
		return ctx, err
	}

	// Add user ID to context
	ctx = context.WithValue(ctx, "user_id", userID)
	return ctx, nil
}
