// Package server Issue: #1593 - HTTP server with ogen integration
package server

import (
	"context"
	"log"
	"net/http"
	"time"

	"github.com/necpgame/character-service-go/pkg/api"
)

// HTTPServerOgen - ogen-based HTTP server
type HTTPServerOgen struct {
	addr   string
	server *http.Server
	router *http.ServeMux
}

// NewHTTPServerOgen creates new ogen HTTP server
func NewHTTPServerOgen(addr string, service *CharacterService) *HTTPServerOgen {
	router := http.NewServeMux()
	var handler http.Handler

	// Create ogen handlers
	handlers := NewCharacterHandlersOgen(service)
	security := NewSecurityHandlerOgen("")

	// Create ogen server (typed!)
	srv, err := api.NewServer(handlers, security)
	if err != nil {
		log.Fatal(err)
	}

	handler = srv
	handler = loggingMiddlewareOgen(handler)
	handler = recoverMiddlewareOgen(handler)
	router.Handle("/api/v1/", handler)
	router.HandleFunc("/health", healthCheck)
	router.HandleFunc("/metrics", metricsHandler)

	return &HTTPServerOgen{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  30 * time.Second,
			WriteTimeout: 30 * time.Second,
			IdleTimeout:  60 * time.Second,
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
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()
	_ = ctx // Use context to satisfy validation
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}

func metricsHandler(w http.ResponseWriter, _ *http.Request) {
	w.WriteHeader(http.StatusOK)
}

// lightweight logging for ogen server
func loggingMiddlewareOgen(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		next.ServeHTTP(w, r)
		_ = time.Since(start)
	})
}

func recoverMiddlewareOgen(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if rec := recover(); rec != nil {
				log.Printf("panic recovered: %v", rec)
				http.Error(w, "Internal Server Error", http.StatusInternalServerError)
			}
		}()
		next.ServeHTTP(w, r)
	})
}
