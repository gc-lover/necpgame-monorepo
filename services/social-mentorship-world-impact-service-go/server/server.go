// HTTP Server for Social Mentorship World Impact Service
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
	"github.com/necp-game/social-mentorship-world-impact-service-go/pkg/mentorship-impact"
	"go.uber.org/zap"
)

// Server represents the HTTP server
type Server struct {
	router  *chi.Mux
	service *mentorshipimpact.Service
	logger  *zap.Logger
	server  *http.Server
}

// NewServer creates a new HTTP server
func NewServer(service *mentorshipimpact.Service, logger *zap.Logger) *Server {
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

	// Mentorship impact API
	s.router.Route("/mentorship-impact", func(r chi.Router) {
		r.Get("/world-impacts", s.getWorldImpactsFromMentorship)
		r.Get("/skill-development-analysis", s.getSkillDevelopmentAnalysis)
		r.Get("/social-network-analysis", s.getSocialNetworkAnalysis)
		r.Get("/knowledge-transfer-efficiency", s.getKnowledgeTransferEfficiency)
		r.Get("/community-development", s.getCommunityDevelopmentImpact)
		r.Post("/impact-prediction", s.predictMentorshipImpact)
		r.Post("/legacy-effects", s.calculateMentorshipLegacyEffects)
		r.Get("/{contract_id}/world-impact", s.getMentorshipContractWorldImpact)
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
		"service":   "social-mentorship-world-impact",
		"timestamp": time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// getMentorshipContractWorldImpact returns world impact for a specific mentorship contract
func (s *Server) getMentorshipContractWorldImpact(w http.ResponseWriter, r *http.Request) {
	contractIDStr := chi.URLParam(r, "contract_id")
	contractID, err := uuid.Parse(contractIDStr)
	if err != nil {
		s.logger.Error("Invalid contract ID", zap.String("contract_id", contractIDStr), zap.Error(err))
		http.Error(w, "Invalid contract ID", http.StatusBadRequest)
		return
	}

	impact, err := s.service.GetMentorshipContractWorldImpact(r.Context(), contractID)
	if err != nil {
		s.logger.Error("Failed to get mentorship contract world impact",
			zap.String("contract_id", contractID.String()),
			zap.Error(err))
		http.Error(w, "Failed to get impact", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(impact)
}

// getWorldImpactsFromMentorship returns overall world impacts from mentorship
func (s *Server) getWorldImpactsFromMentorship(w http.ResponseWriter, r *http.Request) {
	regionIDStr := r.URL.Query().Get("region_id")
	var regionID *uuid.UUID
	if regionIDStr != "" {
		if id, err := uuid.Parse(regionIDStr); err == nil {
			regionID = &id
		}
	}

	timePeriod := r.URL.Query().Get("time_period")
	if timePeriod == "" {
		timePeriod = "month"
	}

	impactType := r.URL.Query().Get("impact_type")

	impacts, err := s.service.GetWorldImpactsFromMentorship(r.Context(), regionID, timePeriod, impactType)
	if err != nil {
		s.logger.Error("Failed to get world impacts from mentorship", zap.Error(err))
		http.Error(w, "Failed to get impacts", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(impacts)
}

// predictMentorshipImpact predicts impact of mentorship
func (s *Server) predictMentorshipImpact(w http.ResponseWriter, r *http.Request) {
	var req mentorshipimpact.MentorshipImpactPredictionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.Error("Failed to decode request body", zap.Error(err))
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	prediction, err := s.service.PredictMentorshipImpact(r.Context(), req)
	if err != nil {
		s.logger.Error("Failed to predict mentorship impact", zap.Error(err))
		http.Error(w, "Failed to predict impact", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(prediction)
}

// getSkillDevelopmentAnalysis returns skill development analysis
func (s *Server) getSkillDevelopmentAnalysis(w http.ResponseWriter, r *http.Request) {
	mentorIDStr := r.URL.Query().Get("mentor_id")
	var mentorID *uuid.UUID
	if mentorIDStr != "" {
		if id, err := uuid.Parse(mentorIDStr); err == nil {
			mentorID = &id
		}
	}

	studentIDStr := r.URL.Query().Get("student_id")
	var studentID *uuid.UUID
	if studentIDStr != "" {
		if id, err := uuid.Parse(studentIDStr); err == nil {
			studentID = &id
		}
	}

	skillCategory := r.URL.Query().Get("skill_category")
	timePeriod := r.URL.Query().Get("time_period")
	if timePeriod == "" {
		timePeriod = "quarter"
	}

	analysis, err := s.service.GetSkillDevelopmentAnalysis(r.Context(), mentorID, studentID, skillCategory, timePeriod)
	if err != nil {
		s.logger.Error("Failed to get skill development analysis", zap.Error(err))
		http.Error(w, "Failed to get analysis", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(analysis)
}

// getSocialNetworkAnalysis returns social network analysis
func (s *Server) getSocialNetworkAnalysis(w http.ResponseWriter, r *http.Request) {
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

	networkDepthStr := r.URL.Query().Get("network_depth")
	networkDepth := 3
	if networkDepthStr != "" {
		// Parse network depth
	}

	includeInactive := r.URL.Query().Get("include_inactive") == "true"

	analysis, err := s.service.GetSocialNetworkAnalysis(r.Context(), regionID, networkDepth, includeInactive)
	if err != nil {
		s.logger.Error("Failed to get social network analysis",
			zap.String("region_id", regionID.String()),
			zap.Error(err))
		http.Error(w, "Failed to get analysis", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(analysis)
}

// getKnowledgeTransferEfficiency returns knowledge transfer efficiency
func (s *Server) getKnowledgeTransferEfficiency(w http.ResponseWriter, r *http.Request) {
	contractIDStr := r.URL.Query().Get("contract_id")
	var contractID *uuid.UUID
	if contractIDStr != "" {
		if id, err := uuid.Parse(contractIDStr); err == nil {
			contractID = &id
		}
	}

	mentorExpertiseLevel := r.URL.Query().Get("mentor_expertise_level")
	assessmentPeriod := r.URL.Query().Get("assessment_period")
	if assessmentPeriod == "" {
		assessmentPeriod = "final"
	}

	efficiency, err := s.service.GetKnowledgeTransferEfficiency(r.Context(), contractID, mentorExpertiseLevel, assessmentPeriod)
	if err != nil {
		s.logger.Error("Failed to get knowledge transfer efficiency", zap.Error(err))
		http.Error(w, "Failed to get efficiency", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(efficiency)
}

// getCommunityDevelopmentImpact returns community development impact
func (s *Server) getCommunityDevelopmentImpact(w http.ResponseWriter, r *http.Request) {
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

	developmentAspect := r.URL.Query().Get("development_aspect")
	timeFrame := r.URL.Query().Get("time_frame")
	if timeFrame == "" {
		timeFrame = "medium_term"
	}

	impact, err := s.service.GetCommunityDevelopmentImpact(r.Context(), regionID, developmentAspect, timeFrame)
	if err != nil {
		s.logger.Error("Failed to get community development impact",
			zap.String("region_id", regionID.String()),
			zap.Error(err))
		http.Error(w, "Failed to get impact", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(impact)
}

// calculateMentorshipLegacyEffects calculates legacy effects of mentorship
func (s *Server) calculateMentorshipLegacyEffects(w http.ResponseWriter, r *http.Request) {
	var req mentorshipimpact.LegacyEffectsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.Error("Failed to decode request body", zap.Error(err))
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	effects, err := s.service.CalculateMentorshipLegacyEffects(r.Context(), req)
	if err != nil {
		s.logger.Error("Failed to calculate mentorship legacy effects", zap.Error(err))
		http.Error(w, "Failed to calculate effects", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(effects)
}
