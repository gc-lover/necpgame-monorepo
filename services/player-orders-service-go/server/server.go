// HTTP Server for Player Orders World Impact Service
// Issue: #140894810

package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/necp-game/player-orders-service-go/pkg/world-impact"
	"go.uber.org/zap"
)

// Server represents the HTTP server
type Server struct {
	router  *chi.Mux
	service *worldimpact.Service
	logger  *zap.Logger
	server  *http.Server
}

// NewServer creates a new HTTP server
func NewServer(service *worldimpact.Service, logger *zap.Logger) *Server {
	s := &Server{
		router:  chi.NewRouter(),
		service: service,
		logger:  logger,
	}

	s.setupRoutes()
	return s
}

// setupRoutes configures the HTTP routes
func (s *Server) setupRoutes() {
	s.router.Use(middleware.Logger)
	s.router.Use(middleware.Recoverer)
	s.router.Use(middleware.Timeout(30 * time.Second))

	// Health check
	s.router.Get("/health", s.healthCheck)

	// World impact API
	s.router.Route("/world/player-orders", func(r chi.Router) {
		r.Get("/effects", s.getWorldEffects)
		r.Post("/effects/recalculate", s.recalculateEffects)
		r.Get("/events", s.getActiveEvents)
	})

	// Economy API
	s.router.Route("/economy/player-orders", func(r chi.Router) {
		r.Get("/index", s.getEconomicIndex)
		r.Get("/index/{regionID}", s.getEconomicIndexByRegion)
	})

	// Social API
	s.router.Route("/social/player-orders", func(r chi.Router) {
		r.Get("/news", s.getSocialImpact)
		r.Get("/news/{regionID}", s.getSocialImpactByRegion)
	})
}

// Start starts the HTTP server
func (s *Server) Start(addr string) error {
	s.server = &http.Server{
		Addr:    addr,
		Handler: s.router,
	}

	s.logger.Info("Starting HTTP server", zap.String("addr", addr))
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *Server) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// healthCheck handles health check requests
func (s *Server) healthCheck(w http.ResponseWriter, r *http.Request) {
	response := map[string]interface{}{
		"status":    "healthy",
		"service":   "player-orders-world-impact",
		"timestamp": time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// getWorldEffects returns world effects for all regions
func (s *Server) getWorldEffects(w http.ResponseWriter, r *http.Request) {
	response := map[string]interface{}{
		"economic_indexes":   s.service.GetAllEconomicIndexes(),
		"social_impacts":     s.service.GetAllSocialImpacts(),
		"political_impacts":  s.service.GetAllPoliticalImpacts(),
		"city_developments":  s.service.GetAllCityDevelopments(),
		"timestamp":          time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// recalculateEffects triggers recalculation of all effects
func (s *Server) recalculateEffects(w http.ResponseWriter, r *http.Request) {
	go s.service.RecalculateImpacts(r.Context())

	response := map[string]interface{}{
		"status":    "recalculation_started",
		"message":   "World impact recalculation has been initiated",
		"timestamp": time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// getActiveEvents returns all active world events
func (s *Server) getActiveEvents(w http.ResponseWriter, r *http.Request) {
	events := s.service.GetActiveEvents()

	response := map[string]interface{}{
		"events":    events,
		"count":     len(events),
		"timestamp": time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// getEconomicIndex returns economic index for all regions
func (s *Server) getEconomicIndex(w http.ResponseWriter, r *http.Request) {
	indexes := s.service.GetAllEconomicIndexes()

	response := map[string]interface{}{
		"economic_indexes": indexes,
		"count":            len(indexes),
		"timestamp":        time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// getEconomicIndexByRegion returns economic index for specific region
func (s *Server) getEconomicIndexByRegion(w http.ResponseWriter, r *http.Request) {
	regionID := chi.URLParam(r, "regionID")

	index, exists := s.service.GetEconomicIndex(regionID)
	if !exists {
		http.Error(w, "Region not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(index)
}

// getSocialImpact returns social impact for all regions
func (s *Server) getSocialImpact(w http.ResponseWriter, r *http.Request) {
	impacts := s.service.GetAllSocialImpacts()

	response := map[string]interface{}{
		"social_impacts": impacts,
		"count":          len(impacts),
		"timestamp":      time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// getSocialImpactByRegion returns social impact for specific region
func (s *Server) getSocialImpactByRegion(w http.ResponseWriter, r *http.Request) {
	regionID := chi.URLParam(r, "regionID")

	impact, exists := s.service.GetSocialImpact(regionID)
	if !exists {
		http.Error(w, "Region not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(impact)
}
