package handlers

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/google/uuid"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"services/achievement-system-service-go/internal/service"
	"services/achievement-system-service-go/pkg/models"
)

// Handler handles HTTP requests for the Achievement System
type Handler struct {
	service   *service.Service
	logger    *zap.Logger
	validator *Validator
}

// Validator handles request validation
type Validator struct{}

// NewValidator creates a new validator
func NewValidator() *Validator {
	return &Validator{}
}

// NewHandler creates a new handler instance
func NewHandler(svc *service.Service, logger *zap.Logger) *Handler {
	return &Handler{
		service:   svc,
		logger:    logger,
		validator: NewValidator(),
	}
}

// SetupRoutes configures all routes for the achievement system
func (h *Handler) SetupRoutes(r *chi.Mux) {
	// Middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// Health check
	r.Get("/health", h.HealthCheck)

	// Metrics
	r.Handle("/metrics", promhttp.Handler())

	// API routes
	r.Route("/api/v1", func(r chi.Router) {
		r.Route("/achievements", func(r chi.Router) {
			r.Get("/", h.ListAchievements)
			r.Post("/", h.CreateAchievement)
			r.Get("/{achievementID}", h.GetAchievement)
			r.Put("/{achievementID}", h.UpdateAchievement)
			r.Delete("/{achievementID}", h.DeleteAchievement)
		})

		r.Route("/players/{playerID}", func(r chi.Router) {
			r.Get("/achievements", h.GetPlayerAchievements)
			r.Get("/profile", h.GetPlayerProfile)
			r.Post("/achievements/{achievementID}/unlock", h.UnlockAchievement)
			r.Post("/achievements/{achievementID}/progress", h.UpdateAchievementProgress)
		})

		r.Route("/events", func(r chi.Router) {
			r.Post("/", h.ProcessEvent)
		})

		// Admin routes
		r.Route("/admin", func(r chi.Router) {
			r.Post("/import", h.ImportAchievements)
		})
	})
}

// HealthCheck returns service health status
func (h *Handler) HealthCheck(w http.ResponseWriter, r *http.Request) {
	h.respondJSON(w, http.StatusOK, map[string]string{
		"status": "healthy",
		"time":   time.Now().Format(time.RFC3339),
	})
}

// Achievement handlers

// ListAchievements lists all achievements with pagination
func (h *Handler) ListAchievements(w http.ResponseWriter, r *http.Request) {
	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	limit := 50 // default
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0 // default
	if offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	achievements, err := h.service.ListAchievements(r.Context(), limit, offset)
	if err != nil {
		h.logger.Error("Failed to list achievements", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to list achievements")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"achievements": achievements,
		"limit":        limit,
		"offset":       offset,
	})
}

// CreateAchievement creates a new achievement
func (h *Handler) CreateAchievement(w http.ResponseWriter, r *http.Request) {
	var req models.Achievement
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
		return
	}

	if err := h.validator.ValidateAchievement(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	achievement, err := h.service.CreateAchievement(r.Context(), &req)
	if err != nil {
		h.logger.Error("Failed to create achievement", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to create achievement")
		return
	}

	h.respondJSON(w, http.StatusCreated, achievement)
}

// GetAchievement retrieves a specific achievement
func (h *Handler) GetAchievement(w http.ResponseWriter, r *http.Request) {
	achievementID, err := uuid.Parse(chi.URLParam(r, "achievementID"))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid achievement ID")
		return
	}

	achievement, err := h.service.GetAchievement(r.Context(), achievementID)
	if err != nil {
		h.logger.Error("Failed to get achievement", zap.Error(err), zap.String("achievement_id", achievementID.String()))
		h.respondError(w, http.StatusNotFound, "Achievement not found")
		return
	}

	h.respondJSON(w, http.StatusOK, achievement)
}

// UpdateAchievement updates an existing achievement
func (h *Handler) UpdateAchievement(w http.ResponseWriter, r *http.Request) {
	achievementID, err := uuid.Parse(chi.URLParam(r, "achievementID"))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid achievement ID")
		return
	}

	var req models.Achievement
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
		return
	}

	req.ID = achievementID
	if err := h.validator.ValidateAchievement(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	achievement, err := h.service.UpdateAchievement(r.Context(), &req)
	if err != nil {
		h.logger.Error("Failed to update achievement", zap.Error(err), zap.String("achievement_id", achievementID.String()))
		h.respondError(w, http.StatusInternalServerError, "Failed to update achievement")
		return
	}

	h.respondJSON(w, http.StatusOK, achievement)
}

// DeleteAchievement deletes an achievement
func (h *Handler) DeleteAchievement(w http.ResponseWriter, r *http.Request) {
	achievementID, err := uuid.Parse(chi.URLParam(r, "achievementID"))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid achievement ID")
		return
	}

	if err := h.service.DeleteAchievement(r.Context(), achievementID); err != nil {
		h.logger.Error("Failed to delete achievement", zap.Error(err), zap.String("achievement_id", achievementID.String()))
		h.respondError(w, http.StatusInternalServerError, "Failed to delete achievement")
		return
	}

	h.respondJSON(w, http.StatusNoContent, nil)
}

// Player handlers

// GetPlayerAchievements retrieves all achievements for a player
func (h *Handler) GetPlayerAchievements(w http.ResponseWriter, r *http.Request) {
	playerID, err := uuid.Parse(chi.URLParam(r, "playerID"))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid player ID")
		return
	}

	achievements, err := h.service.GetPlayerAchievements(r.Context(), playerID)
	if err != nil {
		h.logger.Error("Failed to get player achievements", zap.Error(err), zap.String("player_id", playerID.String()))
		h.respondError(w, http.StatusInternalServerError, "Failed to get player achievements")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"player_id":    playerID,
		"achievements": achievements,
	})
}

// GetPlayerProfile retrieves a player's achievement profile
func (h *Handler) GetPlayerProfile(w http.ResponseWriter, r *http.Request) {
	playerID, err := uuid.Parse(chi.URLParam(r, "playerID"))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid player ID")
		return
	}

	profile, err := h.service.GetPlayerProfile(r.Context(), playerID)
	if err != nil {
		h.logger.Error("Failed to get player profile", zap.Error(err), zap.String("player_id", playerID.String()))
		h.respondError(w, http.StatusInternalServerError, "Failed to get player profile")
		return
	}

	h.respondJSON(w, http.StatusOK, profile)
}

// UnlockAchievement unlocks an achievement for a player
func (h *Handler) UnlockAchievement(w http.ResponseWriter, r *http.Request) {
	playerID, err := uuid.Parse(chi.URLParam(r, "playerID"))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid player ID")
		return
	}

	achievementID, err := uuid.Parse(chi.URLParam(r, "achievementID"))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid achievement ID")
		return
	}

	achievement, err := h.service.UnlockAchievement(r.Context(), playerID, achievementID)
	if err != nil {
		h.logger.Error("Failed to unlock achievement", zap.Error(err),
			zap.String("player_id", playerID.String()), zap.String("achievement_id", achievementID.String()))
		h.respondError(w, http.StatusInternalServerError, "Failed to unlock achievement")
		return
	}

	h.respondJSON(w, http.StatusOK, achievement)
}

// UpdateAchievementProgress updates progress towards an achievement
func (h *Handler) UpdateAchievementProgress(w http.ResponseWriter, r *http.Request) {
	playerID, err := uuid.Parse(chi.URLParam(r, "playerID"))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid player ID")
		return
	}

	achievementID, err := uuid.Parse(chi.URLParam(r, "achievementID"))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid achievement ID")
		return
	}

	var req struct {
		Progress int `json:"progress"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
		return
	}

	if req.Progress < 0 {
		h.respondError(w, http.StatusBadRequest, "Progress cannot be negative")
		return
	}

	if err := h.service.UpdateAchievementProgress(r.Context(), playerID, achievementID, req.Progress); err != nil {
		h.logger.Error("Failed to update achievement progress", zap.Error(err),
			zap.String("player_id", playerID.String()), zap.String("achievement_id", achievementID.String()))
		h.respondError(w, http.StatusInternalServerError, "Failed to update progress")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "progress updated"})
}

// ProcessEvent processes achievement-related events
func (h *Handler) ProcessEvent(w http.ResponseWriter, r *http.Request) {
	var event models.AchievementEvent
	if err := json.NewDecoder(r.Body).Decode(&event); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
		return
	}

	if event.PlayerID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "Player ID is required")
		return
	}

	if event.Type == "" {
		h.respondError(w, http.StatusBadRequest, "Event type is required")
		return
	}

	event.Timestamp = time.Now()

	if err := h.service.ProcessAchievementEvent(r.Context(), &event); err != nil {
		h.logger.Error("Failed to process achievement event", zap.Error(err), zap.String("event_type", event.Type))
		h.respondError(w, http.StatusInternalServerError, "Failed to process event")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "event processed"})
}

    // Admin handlers

    // ImportAchievements imports achievements from YAML data
    func (h *Handler) ImportAchievements(w http.ResponseWriter, r *http.Request) {
    	var req models.AchievementImportRequest
    	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
    		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
    		return
    	}

    	// Validate achievements
    	validationErrors := []string{}
    	for i, achievement := range req.Achievements {
    		if err := h.validator.ValidateAchievement(achievement); err != nil {
    			validationErrors = append(validationErrors, fmt.Sprintf("achievement %d: %s", i+1, err.Error()))
    		}
    	}

    	if len(validationErrors) > 0 {
    		h.respondJSON(w, http.StatusBadRequest, map[string]interface{}{
    			"error": "Validation failed",
    			"details": validationErrors,
    		})
    		return
    	}

    	response := &models.AchievementImportResponse{
    		Total: len(req.Achievements),
    	}

    	if req.DryRun {
    		// Just validate, don't import
    		response.Validated = true
    		h.respondJSON(w, http.StatusOK, response)
    		return
    	}

    	// Perform actual import
    	if err := h.service.ImportAchievements(r.Context(), req.Achievements); err != nil {
    		h.logger.Error("Failed to import achievements", zap.Error(err))
    		h.respondError(w, http.StatusInternalServerError, "Failed to import achievements")
    		return
    	}

    	// For now, assume all were imported (we'll improve error tracking later)
    	response.Imported = len(req.Achievements)

    	h.respondJSON(w, http.StatusOK, response)
    }

// Helper methods

func (h *Handler) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *Handler) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

// Validation methods

func (v *Validator) ValidateAchievement(achievement *models.Achievement) error {
	if achievement.Name == "" {
		return &ValidationError{Field: "name", Message: "name is required"}
	}
	if len(achievement.Name) > 100 {
		return &ValidationError{Field: "name", Message: "name too long"}
	}
	if achievement.Description == "" {
		return &ValidationError{Field: "description", Message: "description is required"}
	}
	if achievement.Points < 0 {
		return &ValidationError{Field: "points", Message: "points cannot be negative"}
	}
	return nil
}

// ValidationError represents a validation error
type ValidationError struct {
	Field   string
	Message string
}

func (e *ValidationError) Error() string {
	return e.Message
}
