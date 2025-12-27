package server

import (
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/cors"
	"go.uber.org/zap"

	"combat-abilities-service-go/pkg/cache"
	"combat-abilities-service-go/pkg/handlers"
	"combat-abilities-service-go/pkg/repository"
)

// Server wraps the HTTP server with handlers
type Server struct {
	handler http.Handler
	logger  *zap.Logger
}

// NewServer creates a new HTTP server instance with enterprise-grade configuration
func NewServer(logger *zap.Logger) *Server {
	// Initialize dependencies
	repo := repository.NewRepository()
	cache := cache.NewCache()

	// Initialize service layer
	service := handlers.NewService(repo, cache)

	// Initialize handlers
	combatHandler := handlers.NewCombatAbilitiesHandler(service, logger)

	// Setup router with middleware
	r := chi.NewRouter()

	// CORS middleware for cross-origin requests
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // In production, specify allowed origins
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300, // Maximum value not ignored by any of major browsers
	}))

	// Mount handlers
	r.Mount("/api/v1", combatHandler.Routes())

	return &Server{
		handler: r,
		logger:  logger,
	}
}

// Handler returns the HTTP handler
func (s *Server) Handler() http.Handler {
	return s.handler
}
