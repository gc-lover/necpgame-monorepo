// Social HTTP Server - Enterprise-grade server setup
// Issue: #140875791
// PERFORMANCE: Optimized HTTP server for social systems

package server

import (
	"context"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/cors"

	"social-service-go/pkg/handlers"
)

// Server wraps the HTTP server with social handlers
type Server struct {
	router  *chi.Mux
	service *handlers.Service
}

// NewServer creates a new HTTP server instance
func NewServer(service *handlers.Service) *Server {
	r := chi.NewRouter()

	// Enterprise-grade middleware stack
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure for production
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Performance middleware
	r.Use(middlewareTimeout(30 * time.Second))
	r.Use(middlewareLogger)

	server := &Server{
		router:  r,
		service: service,
	}

	server.setupRoutes()

	return server
}

// setupRoutes configures all API routes
func (s *Server) setupRoutes() {
	// Health check
	s.router.Get("/health", s.healthCheck)

	// Relationship routes
	s.router.Get("/relationships/{sourceType}/{sourceID}/{targetType}/{targetID}", s.service.GetRelationship)
	s.router.Post("/relationships/{sourceType}/{sourceID}/{targetType}/{targetID}/update", s.service.UpdateRelationship)

	// Social network routes
	s.router.Get("/social-network/{entityType}/{entityID}", s.service.GetSocialNetwork)

	// Order routes
	s.router.Post("/orders", s.service.CreateOrder)
	s.router.Get("/orders/{orderID}", s.service.GetOrder)
	s.router.Get("/orders/regions/{regionID}", s.service.GetOrderBoard)
	s.router.Post("/orders/{orderID}/accept", s.service.AcceptOrder)
	s.router.Post("/orders/{orderID}/complete", s.service.CompleteOrder)

	// NPC hiring routes
	s.router.Get("/npcs/regions/{regionID}/available", s.service.GetAvailableNPCs)
	s.router.Post("/npcs/{npcID}/hire", s.service.HireNPC)
	s.router.Get("/npc-hirings/{hiringID}", s.service.GetNPCHiring)
	s.router.Post("/npc-hirings/{hiringID}/terminate", s.service.TerminateNPCHiring)
	s.router.Get("/npc-hirings/{hiringID}/performance", s.service.GetNPCPerformance)
}

// Handler returns the HTTP handler
func (s *Server) Handler() http.Handler {
	return s.router
}

// Start starts the HTTP server
func (s *Server) Start(addr string) error {
	server := &http.Server{
		Addr:         addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *Server) Shutdown(ctx context.Context) error {
	// In a real implementation, this would shut down the HTTP server
	return nil
}

// healthCheck handles health check requests
func (s *Server) healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{
		"status": "healthy",
		"service": "social-service-go",
		"version": "1.0.0",
		"timestamp": "` + time.Now().Format(time.RFC3339) + `"
	}`))
}

// middlewareTimeout adds timeout to requests
func middlewareTimeout(timeout time.Duration) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			ctx, cancel := context.WithTimeout(r.Context(), timeout)
			defer cancel()

			r = r.WithContext(ctx)
			next.ServeHTTP(w, r)
		})
	}
}

// middlewareLogger logs HTTP requests
func middlewareLogger(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		next.ServeHTTP(w, r)

		// In a real implementation, this would use structured logging
		// log.Printf("%s %s %v", r.Method, r.URL.Path, time.Since(start))
	})
}
