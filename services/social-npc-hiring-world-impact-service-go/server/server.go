// HTTP Server for Social NPC Hiring World Impact Service
// Issue: #140894831

package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/google/uuid"
	"github.com/necp-game/social-npc-hiring-world-impact-service-go/pkg/npc-hiring-impact"
	"go.uber.org/zap"
)

// Server represents the HTTP server
type Server struct {
	router  *chi.Mux
	service *npchiringimpact.Service
	logger  *zap.Logger
	server  *http.Server
}

// NewServer creates a new HTTP server
func NewServer(service *npchiringimpact.Service, logger *zap.Logger) *Server {
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

	// NPC hiring impact API
	s.router.Route("/npc-hiring-impact", func(r chi.Router) {
		r.Get("/world-impacts", s.getWorldImpactsFromNPCHiring)
		r.Get("/economic-impact", s.getNPCHiringEconomicImpact)
		r.Get("/social-changes", s.getNPCHiringSocialChanges)
		r.Get("/loyalty-effects", s.getNPCLoyaltyEffects)
		r.Post("/impact-prediction", s.predictNPCHiringImpact)
		r.Post("/political-consequences", s.calculatePoliticalConsequences)
		r.Get("/{hire_id}/world-impact", s.getNPCHireWorldImpact)
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
		"service":   "social-npc-hiring-world-impact",
		"timestamp": time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// getNPCHireWorldImpact returns world impact for a specific NPC hire
func (s *Server) getNPCHireWorldImpact(w http.ResponseWriter, r *http.Request) {
	hireIDStr := chi.URLParam(r, "hire_id")
	hireID, err := uuid.Parse(hireIDStr)
	if err != nil {
		s.logger.Error("Invalid hire ID", zap.String("hire_id", hireIDStr), zap.Error(err))
		http.Error(w, "Invalid hire ID", http.StatusBadRequest)
		return
	}

	impact, err := s.service.GetNPCHireWorldImpact(r.Context(), hireID)
	if err != nil {
		s.logger.Error("Failed to get NPC hire world impact",
			zap.String("hire_id", hireID.String()),
			zap.Error(err))
		http.Error(w, "Failed to get impact", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(impact)
}

// getWorldImpactsFromNPCHiring returns overall world impacts from NPC hiring
func (s *Server) getWorldImpactsFromNPCHiring(w http.ResponseWriter, r *http.Request) {
	regionIDStr := r.URL.Query().Get("region_id")
	var regionID *uuid.UUID
	if regionIDStr != "" {
		if id, err := uuid.Parse(regionIDStr); err == nil {
			regionID = &id
		}
	}

	timePeriod := r.URL.Query().Get("time_period")
	if timePeriod == "" {
		timePeriod = "day"
	}

	impacts, err := s.service.GetWorldImpactsFromNPCHiring(r.Context(), regionID, timePeriod)
	if err != nil {
		s.logger.Error("Failed to get world impacts from NPC hiring", zap.Error(err))
		http.Error(w, "Failed to get impacts", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(impacts)
}

// predictNPCHiringImpact predicts impact of NPC hiring
func (s *Server) predictNPCHiringImpact(w http.ResponseWriter, r *http.Request) {
	var req npchiringimpact.NPCHiringPredictionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.Error("Failed to decode request body", zap.Error(err))
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	prediction, err := s.service.PredictNPCHiringImpact(r.Context(), req)
	if err != nil {
		s.logger.Error("Failed to predict NPC hiring impact", zap.Error(err))
		http.Error(w, "Failed to predict impact", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(prediction)
}

// getNPCLoyaltyEffects returns loyalty effects on the world
func (s *Server) getNPCLoyaltyEffects(w http.ResponseWriter, r *http.Request) {
	npcIDStr := r.URL.Query().Get("npc_id")
	var npcID *uuid.UUID
	if npcIDStr != "" {
		if id, err := uuid.Parse(npcIDStr); err == nil {
			npcID = &id
		}
	}

	loyaltyLevelStr := r.URL.Query().Get("loyalty_level")
	var loyaltyLevel *int
	if loyaltyLevelStr != "" {
		// Parse loyalty level
	}

	regionIDStr := r.URL.Query().Get("region_id")
	var regionID *uuid.UUID
	if regionIDStr != "" {
		if id, err := uuid.Parse(regionIDStr); err == nil {
			regionID = &id
		}
	}

	effects, err := s.service.GetNPCLoyaltyEffects(r.Context(), npcID, loyaltyLevel, regionID)
	if err != nil {
		s.logger.Error("Failed to get NPC loyalty effects", zap.Error(err))
		http.Error(w, "Failed to get effects", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(effects)
}

// getNPCHiringEconomicImpact returns economic impact analysis
func (s *Server) getNPCHiringEconomicImpact(w http.ResponseWriter, r *http.Request) {
	regionIDStr := r.URL.Query().Get("region_id")
	if regionIDStr == "" {
		http.Error(w, "region_id is required", http.StatusBadRequest)
		return
	}
	regionID, err := uuid.Parse(regionIDStr)
	if err != nil {
		s.logger.Error("Invalid region ID", zap.String("region_id", regionIDStr), zap.Error(err))
		http.Error(w, "Invalid region ID", http.StatusBadRequest)
		return
	}

	npcType := r.URL.Query().Get("npc_type")
	timeRange := r.URL.Query().Get("time_range")
	if timeRange == "" {
		timeRange = "last_week"
	}

	impact, err := s.service.GetNPCHiringEconomicImpact(r.Context(), regionID, npcType, timeRange)
	if err != nil {
		s.logger.Error("Failed to get economic impact",
			zap.String("region_id", regionID.String()),
			zap.Error(err))
		http.Error(w, "Failed to get impact", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(impact)
}

// getNPCHiringSocialChanges returns social changes analysis
func (s *Server) getNPCHiringSocialChanges(w http.ResponseWriter, r *http.Request) {
	regionIDStr := r.URL.Query().Get("region_id")
	if regionIDStr == "" {
		http.Error(w, "region_id is required", http.StatusBadRequest)
		return
	}
	regionID, err := uuid.Parse(regionIDStr)
	if err != nil {
		s.logger.Error("Invalid region ID", zap.String("region_id", regionIDStr), zap.Error(err))
		http.Error(w, "Invalid region ID", http.StatusBadRequest)
		return
	}

	changeType := r.URL.Query().Get("change_type")

	changes, err := s.service.GetNPCHiringSocialChanges(r.Context(), regionID, changeType)
	if err != nil {
		s.logger.Error("Failed to get social changes",
			zap.String("region_id", regionID.String()),
			zap.Error(err))
		http.Error(w, "Failed to get changes", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(changes)
}

// calculatePoliticalConsequences calculates political consequences of NPC hiring
func (s *Server) calculatePoliticalConsequences(w http.ResponseWriter, r *http.Request) {
	var req npchiringimpact.PoliticalConsequencesRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.Error("Failed to decode request body", zap.Error(err))
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	consequences, err := s.service.CalculatePoliticalConsequences(r.Context(), req)
	if err != nil {
		s.logger.Error("Failed to calculate political consequences", zap.Error(err))
		http.Error(w, "Failed to calculate consequences", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(consequences)
}
