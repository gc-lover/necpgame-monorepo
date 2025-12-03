// Issue: #1593 - HTTP server with ogen integration
package server

import (
	"context"
	"log"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/necpgame/character-service-go/pkg/api"
)

// HTTPServerOgen - ogen-based HTTP server
type HTTPServerOgen struct {
	addr   string
	server *http.Server
	router *chi.Mux
}

// NewHTTPServerOgen creates new ogen HTTP server
func NewHTTPServerOgen(addr string, service *CharacterService) *HTTPServerOgen {
	router := chi.NewRouter()
	
	// Middleware
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.RequestID)
	
	// Create ogen handlers
	handlers := NewCharacterHandlersOgen(service)
	security := NewSecurityHandlerOgen("")
	
	// Create ogen server (typed!)
	srv, err := api.NewServer(handlers, security)
	if err != nil {
		log.Fatal(err)
	}
	
	// Mount ogen server
	router.Mount("/api/v1", srv)
	
	// Health check
	router.Get("/health", healthCheck)
	router.Get("/metrics", metricsHandler)
	
	return &HTTPServerOgen{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  10 * time.Second,
			WriteTimeout: 10 * time.Second,
		},
	}
}

// Start starts the HTTP server
func (s *HTTPServerOgen) Start() error {
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *HTTPServerOgen) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}

func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
}
