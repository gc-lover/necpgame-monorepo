// Issue: #1928 - HTTP server for Meta Mechanics Service
// PERFORMANCE: Graceful shutdown, connection limits
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
)

// HTTPServer wraps http.Server with graceful shutdown
type HTTPServer struct {
	server *http.Server
}

// NewHTTPServer creates configured HTTP server
func NewHTTPServer(addr string, service *Service) *HTTPServer {
	r := chi.NewRouter()

	// Middleware
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// Health check
	r.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"healthy"}`))
	})

	// Meta Mechanics routes
	r.Route("/api/v1", func(r chi.Router) {
		r.Get("/leagues", service.GetLeagues)
		r.Post("/leagues", service.CreateLeague)
		r.Post("/leagues/{league_id}/join", service.JoinLeague)
		r.Get("/leagues/{league_id}/rankings", service.GetLeagueRankings)
		r.Get("/prestige", service.GetPrestige)
		r.Post("/prestige", service.PrestigeReset)
		r.Get("/meta-events", service.GetMetaEvents)
		r.Post("/meta-events", service.CreateMetaEvent)
		r.Get("/rankings/global", service.GetGlobalRankings)
	})

	return &HTTPServer{
		server: &http.Server{
			Addr:         addr,
			Handler:      r,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  60 * time.Second,
		},
	}
}

// Start begins serving HTTP requests
func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

// Shutdown gracefully stops the server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}
