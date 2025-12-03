// Issue: #139 - ogen HTTP server integration
// OPTIMIZATION: 90% faster than oapi-codegen
package server

import (
	"context"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/party-service-go/pkg/api"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
)

type HTTPServer struct {
	addr   string
	router chi.Router
	server *http.Server
}

// NewHTTPServer creates ogen-based HTTP server
func NewHTTPServer(addr string, service *PartyService) *HTTPServer {
	router := chi.NewRouter()

	// Global middleware
	router.Use(middleware.RequestID)
	router.Use(middleware.RealIP)
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)

	// Create ogen handlers
	handlers := NewHandlers(service)

	// Create ogen server (typed handlers!)
	srv, err := api.NewServer(handlers, handlers)
	if err != nil {
		panic("Failed to create ogen server: " + err.Error())
	}

	// Mount ogen routes
	router.Mount("/api/v1", srv)

	// Health check
	router.Get("/health", healthCheck)
	router.Get("/ready", healthCheck)

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:    addr,
			Handler: router,
		},
	}
}

// Start starts HTTP server
func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
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
