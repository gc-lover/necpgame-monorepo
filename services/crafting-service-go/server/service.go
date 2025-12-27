package server

import (
	"context"
	"log"
	"net/http"
	"time"

	"crafting-service-go/api"

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

// CraftingHandler implements the generated OpenAPI interface
type CraftingHandler struct {
	service *CraftingService
}

// GetRecipesByCategory implements getRecipesByCategory operation
func (h *CraftingHandler) GetRecipesByCategory(ctx context.Context, params api.GetRecipesByCategoryParams) (api.GetRecipesByCategoryRes, error) {
	// BACKEND NOTE: Recipe retrieval with filtering by category, tier, and quality
	// TODO: Replace with actual database query when crafting_recipes table is created
	
	// Parse parameters
	limit := 20
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}
	offset := 0
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}
	
	// Mock recipes data - will be replaced with DB query
	recipes := []api.Recipe{}
	
	// Filter by category if provided
	categoryFilter := ""
	if params.Category.IsSet() {
		categoryFilter = string(params.Category.Value)
	}
	
	// Mock recipe data based on filters
	if categoryFilter == "" || categoryFilter == "weapons" {
		recipes = append(recipes, api.Recipe{
			ID:          uuid.MustParse("00000000-0000-0000-0000-000000000001"),
			Name:        "Cyberpunk Pistol",
			Description: api.NewOptString("A basic cyberpunk pistol"),
			Category:    api.RecipeCategoryWeapons,
			Tier:        1,
			Quality:     50,
			SuccessRate: 0.75,
			Duration:    300,
			Materials: []api.RecipeMaterial{
				{ItemID: uuid.MustParse("00000000-0000-0000-0000-000000000010"), Quantity: 5},
			},
			CreatedAt: api.NewOptDateTime(time.Now().Add(-24 * time.Hour)),
			UpdatedAt: api.NewOptDateTime(time.Now()),
		})
	}
	
	if categoryFilter == "" || categoryFilter == "armor" {
		recipes = append(recipes, api.Recipe{
			ID:          uuid.MustParse("00000000-0000-0000-0000-000000000002"),
			Name:        "Basic Armor Vest",
			Description: api.NewOptString("A basic protective vest"),
			Category:    api.RecipeCategoryArmor,
			Tier:        1,
			Quality:     40,
			SuccessRate: 0.70,
			Duration:    450,
			Materials: []api.RecipeMaterial{
				{ItemID: uuid.MustParse("00000000-0000-0000-0000-000000000011"), Quantity: 3},
			},
			CreatedAt: api.NewOptDateTime(time.Now().Add(-48 * time.Hour)),
			UpdatedAt: api.NewOptDateTime(time.Now()),
		})
	}
	
	// Apply tier filter
	if params.Tier.IsSet() {
		filtered := []api.Recipe{}
		for _, recipe := range recipes {
			if recipe.Tier == params.Tier.Value {
				filtered = append(filtered, recipe)
			}
		}
		recipes = filtered
	}
	
	// Apply quality filter
	if params.Quality.IsSet() {
		filtered := []api.Recipe{}
		for _, recipe := range recipes {
			if recipe.Quality >= params.Quality.Value {
				filtered = append(filtered, recipe)
			}
		}
		recipes = filtered
	}
	
	// Apply pagination
	total := len(recipes)
	start := offset
	end := offset + limit
	if start > total {
		start = total
	}
	if end > total {
		end = total
	}
	if start < end {
		recipes = recipes[start:end]
	} else {
		recipes = []api.Recipe{}
	}
	
	return &api.RecipeListResponse{
		Recipes: recipes,
		Total:   total,
	}, nil
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

	// Initialize OpenAPI handler
	handler := &CraftingHandler{
		service: s,
	}

	// Create OpenAPI server
	srv, err := api.NewServer(handler, nil)
	if err != nil {
		s.logger.Fatal("Failed to create OpenAPI server", err)
	}

	// Mount OpenAPI server
	r.Mount("/api/v1", srv)

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
