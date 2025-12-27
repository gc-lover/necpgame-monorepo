package server

import (
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/go-chi/jwtauth/v5"
)

// Handlers contains HTTP handlers for the service
type Handlers struct {
	server *Server
}

// NewHandlers creates new handlers instance
func NewHandlers(srv *Server) *Handlers {
	return &Handlers{server: srv}
}

// HealthCheck handles health check requests
func (h *Handlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy"}`))
}

// ReadinessCheck handles readiness check requests
func (h *Handlers) ReadinessCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ready"}`))
}

// Metrics handles metrics endpoint
func (h *Handlers) Metrics(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("# Gameplay Service Metrics\ngameplay_service_up 1\n"))
}

// CreateRouter creates the main router with middleware
func (h *Handlers) CreateRouter() *chi.Mux {
	r := chi.NewRouter()

	// Middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// CORS
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure for production
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Health check (no auth required)
	r.Get("/health", h.HealthCheck)
	r.Get("/ready", h.ReadinessCheck)

	// Metrics endpoint (no auth required)
	r.Get("/metrics", h.Metrics)

	// pprof endpoints for profiling
	r.Mount("/debug", middleware.Profiler())

	// API routes with authentication
	r.Route("/api/v1", func(r chi.Router) {
		r.Use(jwtauth.Verifier(h.server.tokenAuth))
		r.Use(jwtauth.Authenticator(h.server.tokenAuth))

		// Gameplay routes - TODO: Implement when OpenAPI spec is fully expanded
		// r.Post("/combat/sessions", h.CreateCombatSession)
		// r.Post("/combat/abilities/activate", h.ActivateAbility)
		// r.Get("/combat/implants/{implant_id}/stats", h.GetImplantStats)
		// etc.
	})

	return r
}

// Issue: #104
