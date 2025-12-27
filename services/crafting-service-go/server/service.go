package server

import (
	"log"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
)

// CraftingService implements the crafting system business logic
type CraftingService struct {
	logger *log.Logger
}

// NewCraftingService creates a new crafting service instance
func NewCraftingService() *CraftingService {
	return &CraftingService{
		logger: log.New(log.Writer(), "[crafting-service] ", log.LstdFlags),
	}
}

// Handler returns the HTTP handler for the crafting service
func (s *CraftingService) Handler() http.Handler {
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

	// Performance and security middleware
	r.Use(middleware.Logger)
	r.Use(middleware.Timeout(30 * time.Second)) // Context timeouts for performance

	// Health check endpoint
	r.Get("/health", s.healthCheck)

	// API routes (to be generated from OpenAPI)
	// TODO: Mount Ogen-generated routes

	return r
}

// healthCheck handles health check requests
func (s *CraftingService) healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{
		"status": "healthy",
		"service": "crafting-service-go",
		"version": "2.0.0",
		"timestamp": "` + time.Now().Format(time.RFC3339) + `"
	}`))
}
