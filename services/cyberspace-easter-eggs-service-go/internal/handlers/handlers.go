// Issue: #2262 - Cyberspace Easter Eggs Backend Integration
// HTTP handlers for Easter Eggs Service - Enterprise-grade API implementation

package handlers

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"go.uber.org/zap"

	"cyberspace-easter-eggs-service-go/internal/metrics"
	"cyberspace-easter-eggs-service-go/internal/service"
	"cyberspace-easter-eggs-service-go/pkg/models"
)

// EasterEggsHandlers handles HTTP requests
type EasterEggsHandlers struct {
	service  service.EasterEggsServiceInterface
	logger   *zap.SugaredLogger
	metrics  *metrics.Collector
}

// NewEasterEggsHandlers creates new handlers instance
func NewEasterEggsHandlers(svc service.EasterEggsServiceInterface, logger *zap.SugaredLogger, metrics *metrics.Collector) *EasterEggsHandlers {
	return &EasterEggsHandlers{
		service: svc,
		logger:  logger,
		metrics: metrics,
	}
}

// HealthCheck handles health check requests
func (h *EasterEggsHandlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	if err := h.service.HealthCheck(ctx); err != nil {
		h.metrics.ObserveRequestDuration("HealthCheck", start)
		h.logger.Errorf("Health check failed: %v", err)
		h.respondWithError(w, http.StatusServiceUnavailable, "Service unhealthy")
		return
	}

	h.metrics.ObserveRequestDuration("HealthCheck", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"status":    "healthy",
		"service":   "cyberspace-easter-eggs-service",
		"timestamp": time.Now().UTC().Format(time.RFC3339),
	})
}

// GetEasterEggs handles GET /api/v1/easter-eggs
func (h *EasterEggsHandlers) GetEasterEggs(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	// Parse query parameters
	limit := 50 // default
	offset := 0

	if l := r.URL.Query().Get("limit"); l != "" {
		if parsed, err := strconv.Atoi(l); err == nil && parsed > 0 && parsed <= 100 {
			limit = parsed
		}
	}

	if o := r.URL.Query().Get("offset"); o != "" {
		if parsed, err := strconv.Atoi(o); err == nil && parsed >= 0 {
			offset = parsed
		}
	}

	eggs, err := h.service.GetEasterEggsByCategory(ctx, "", limit, offset) // Get all
	if err != nil {
		h.metrics.ObserveRequestDuration("GetEasterEggs", start)
		h.logger.Errorf("Failed to get easter eggs: %v", err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to retrieve easter eggs")
		return
	}

	h.metrics.ObserveRequestDuration("GetEasterEggs", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"easter_eggs": eggs,
		"pagination": map[string]interface{}{
			"limit":  limit,
			"offset": offset,
			"count":  len(eggs),
		},
	})
}

// GetEasterEgg handles GET /api/v1/easter-eggs/{id}
func (h *EasterEggsHandlers) GetEasterEgg(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	id := chi.URLParam(r, "id")
	if id == "" {
		h.respondWithError(w, http.StatusBadRequest, "Easter egg ID is required")
		return
	}

	egg, err := h.service.GetEasterEgg(ctx, id)
	if err != nil {
		h.metrics.ObserveRequestDuration("GetEasterEgg", start)
		h.logger.Errorf("Failed to get easter egg %s: %v", id, err)
		h.respondWithError(w, http.StatusNotFound, "Easter egg not found")
		return
	}

	h.metrics.ObserveRequestDuration("GetEasterEgg", start)
	h.respondWithJSON(w, http.StatusOK, egg)
}

// GetEasterEggsByCategory handles GET /api/v1/easter-eggs/category/{category}
func (h *EasterEggsHandlers) GetEasterEggsByCategory(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	category := chi.URLParam(r, "category")
	limit, offset := h.parsePaginationParams(r)

	eggs, err := h.service.GetEasterEggsByCategory(ctx, category, limit, offset)
	if err != nil {
		h.metrics.ObserveRequestDuration("GetEasterEggsByCategory", start)
		h.logger.Errorf("Failed to get easter eggs by category %s: %v", category, err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to retrieve easter eggs")
		return
	}

	h.metrics.ObserveRequestDuration("GetEasterEggsByCategory", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"category":     category,
		"easter_eggs":  eggs,
		"pagination": map[string]interface{}{
			"limit":  limit,
			"offset": offset,
			"count":  len(eggs),
		},
	})
}

// GetEasterEggsByDifficulty handles GET /api/v1/easter-eggs/difficulty/{difficulty}
func (h *EasterEggsHandlers) GetEasterEggsByDifficulty(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	difficulty := chi.URLParam(r, "difficulty")
	limit, offset := h.parsePaginationParams(r)

	eggs, err := h.service.GetEasterEggsByDifficulty(ctx, difficulty, limit, offset)
	if err != nil {
		h.metrics.ObserveRequestDuration("GetEasterEggsByDifficulty", start)
		h.logger.Errorf("Failed to get easter eggs by difficulty %s: %v", difficulty, err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to retrieve easter eggs")
		return
	}

	h.metrics.ObserveRequestDuration("GetEasterEggsByDifficulty", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"difficulty":   difficulty,
		"easter_eggs":  eggs,
		"pagination": map[string]interface{}{
			"limit":  limit,
			"offset": offset,
			"count":  len(eggs),
		},
	})
}

// GetPlayerProgress handles GET /api/v1/players/{playerId}/progress
func (h *EasterEggsHandlers) GetPlayerProgress(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	playerID := chi.URLParam(r, "playerId")
	easterEggID := r.URL.Query().Get("easterEggId")

	if playerID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Player ID is required")
		return
	}

	if easterEggID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Easter egg ID is required")
		return
	}

	progress, err := h.service.GetPlayerProgress(ctx, playerID, easterEggID)
	if err != nil {
		h.metrics.ObserveRequestDuration("GetPlayerProgress", start)
		h.logger.Errorf("Failed to get player progress for %s:%s: %v", playerID, easterEggID, err)
		h.respondWithError(w, http.StatusNotFound, "Player progress not found")
		return
	}

	h.metrics.ObserveRequestDuration("GetPlayerProgress", start)
	h.respondWithJSON(w, http.StatusOK, progress)
}

// DiscoverEasterEgg handles POST /api/v1/players/{playerId}/easter-eggs/{eggId}/discover
func (h *EasterEggsHandlers) DiscoverEasterEgg(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	playerID := chi.URLParam(r, "playerId")
	easterEggID := chi.URLParam(r, "eggId")

	if playerID == "" || easterEggID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Player ID and Easter egg ID are required")
		return
	}

	var attemptData map[string]interface{}
	if err := json.NewDecoder(r.Body).Decode(&attemptData); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	progress, err := h.service.DiscoverEasterEgg(ctx, playerID, easterEggID, attemptData)
	if err != nil {
		h.metrics.ObserveRequestDuration("DiscoverEasterEgg", start)
		h.logger.Errorf("Failed to discover easter egg %s for player %s: %v", easterEggID, playerID, err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to process discovery")
		return
	}

	h.metrics.ObserveRequestDuration("DiscoverEasterEgg", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"message":  "Easter egg discovered successfully",
		"progress": progress,
	})
}

// GetPlayerProfile handles GET /api/v1/players/{playerId}/profile
func (h *EasterEggsHandlers) GetPlayerProfile(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	playerID := chi.URLParam(r, "playerId")
	if playerID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Player ID is required")
		return
	}

	profile, err := h.service.GetPlayerProfile(ctx, playerID)
	if err != nil {
		h.metrics.ObserveRequestDuration("GetPlayerProfile", start)
		h.logger.Errorf("Failed to get player profile for %s: %v", playerID, err)
		h.respondWithError(w, http.StatusNotFound, "Player profile not found")
		return
	}

	h.metrics.ObserveRequestDuration("GetPlayerProfile", start)
	h.respondWithJSON(w, http.StatusOK, profile)
}

// GetHintsForEasterEgg handles GET /api/v1/easter-eggs/{id}/hints
func (h *EasterEggsHandlers) GetHintsForEasterEgg(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	id := chi.URLParam(r, "id")
	maxLevel := 3 // default

	if ml := r.URL.Query().Get("maxLevel"); ml != "" {
		if parsed, err := strconv.Atoi(ml); err == nil && parsed > 0 && parsed <= 3 {
			maxLevel = parsed
		}
	}

	hints, err := h.service.GetHintsForEasterEgg(ctx, id, maxLevel)
	if err != nil {
		h.metrics.ObserveRequestDuration("GetHintsForEasterEgg", start)
		h.logger.Errorf("Failed to get hints for easter egg %s: %v", id, err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to retrieve hints")
		return
	}

	h.metrics.ObserveRequestDuration("GetHintsForEasterEgg", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"easter_egg_id": id,
		"hints":         hints,
	})
}

// RecordDiscoveryAttempt handles POST /api/v1/easter-eggs/{id}/attempt
func (h *EasterEggsHandlers) RecordDiscoveryAttempt(w http.ResponseWriter, r *http.Request) {
	_ = r.Context() // Not used in this function
	start := time.Now()

	id := chi.URLParam(r, "id")
	var attempt models.EasterEggDiscoveryAttempt

	if err := json.NewDecoder(r.Body).Decode(&attempt); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	attempt.EasterEggID = id
	attempt.AttemptedAt = time.Now()

	// Note: This would need to be implemented in the repository
	// For now, just log the attempt
	h.logger.Infof("Discovery attempt recorded for easter egg %s", id)

	h.metrics.ObserveRequestDuration("RecordDiscoveryAttempt", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"message": "Discovery attempt recorded",
		"attempt": attempt,
	})
}

// GetEasterEggStatistics handles GET /api/v1/easter-eggs/{id}/stats
func (h *EasterEggsHandlers) GetEasterEggStatistics(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	id := chi.URLParam(r, "id")

	stats, err := h.service.GetEasterEggStatistics(ctx, id)
	if err != nil {
		h.metrics.ObserveRequestDuration("GetEasterEggStatistics", start)
		h.logger.Errorf("Failed to get statistics for easter egg %s: %v", id, err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to retrieve statistics")
		return
	}

	h.metrics.ObserveRequestDuration("GetEasterEggStatistics", start)
	h.respondWithJSON(w, http.StatusOK, stats)
}

// GetCategoryStatistics handles GET /api/v1/stats/categories
func (h *EasterEggsHandlers) GetCategoryStatistics(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	stats, err := h.service.GetCategoryStatistics(ctx)
	if err != nil {
		h.metrics.ObserveRequestDuration("GetCategoryStatistics", start)
		h.logger.Errorf("Failed to get category statistics: %v", err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to retrieve category statistics")
		return
	}

	h.metrics.ObserveRequestDuration("GetCategoryStatistics", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"categories": stats,
	})
}

// GetActiveChallenges handles GET /api/v1/challenges/active
func (h *EasterEggsHandlers) GetActiveChallenges(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	challenges, err := h.service.GetActiveChallenges(ctx)
	if err != nil {
		h.metrics.ObserveRequestDuration("GetActiveChallenges", start)
		h.logger.Errorf("Failed to get active challenges: %v", err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to retrieve challenges")
		return
	}

	h.metrics.ObserveRequestDuration("GetActiveChallenges", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"challenges": challenges,
	})
}

// GetPlayerChallengeProgress handles GET /api/v1/players/{playerId}/challenges/{challengeId}/progress
func (h *EasterEggsHandlers) GetPlayerChallengeProgress(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	playerID := chi.URLParam(r, "playerId")
	challengeID := chi.URLParam(r, "challengeId")

	progress, err := h.service.GetPlayerChallengeProgress(ctx, playerID, challengeID)
	if err != nil {
		h.metrics.ObserveRequestDuration("GetPlayerChallengeProgress", start)
		h.logger.Errorf("Failed to get challenge progress for %s:%s: %v", playerID, challengeID, err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to retrieve challenge progress")
		return
	}

	h.metrics.ObserveRequestDuration("GetPlayerChallengeProgress", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"player_id":    playerID,
		"challenge_id": challengeID,
		"progress":     progress,
	})
}

// Administrative endpoints (would require proper authentication in production)

// CreateEasterEgg handles POST /api/v1/admin/easter-eggs
func (h *EasterEggsHandlers) CreateEasterEgg(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	var egg models.EasterEgg
	if err := json.NewDecoder(r.Body).Decode(&egg); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if err := h.service.CreateEasterEgg(ctx, &egg); err != nil {
		h.metrics.ObserveRequestDuration("CreateEasterEgg", start)
		h.logger.Errorf("Failed to create easter egg: %v", err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to create easter egg")
		return
	}

	h.metrics.ObserveRequestDuration("CreateEasterEgg", start)
	h.respondWithJSON(w, http.StatusCreated, map[string]interface{}{
		"message":     "Easter egg created successfully",
		"easter_egg":  egg,
	})
}

// UpdateEasterEgg handles PUT /api/v1/admin/easter-eggs/{id}
func (h *EasterEggsHandlers) UpdateEasterEgg(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	id := chi.URLParam(r, "id")
	var egg models.EasterEgg

	if err := json.NewDecoder(r.Body).Decode(&egg); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	egg.ID = id
	if err := h.service.UpdateEasterEgg(ctx, &egg); err != nil {
		h.metrics.ObserveRequestDuration("UpdateEasterEgg", start)
		h.logger.Errorf("Failed to update easter egg %s: %v", id, err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to update easter egg")
		return
	}

	h.metrics.ObserveRequestDuration("UpdateEasterEgg", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"message":    "Easter egg updated successfully",
		"easter_egg": egg,
	})
}

// DeleteEasterEgg handles DELETE /api/v1/admin/easter-eggs/{id}
func (h *EasterEggsHandlers) DeleteEasterEgg(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	id := chi.URLParam(r, "id")

	if err := h.service.DeleteEasterEgg(ctx, id); err != nil {
		h.metrics.ObserveRequestDuration("DeleteEasterEgg", start)
		h.logger.Errorf("Failed to delete easter egg %s: %v", id, err)
		h.respondWithError(w, http.StatusInternalServerError, "Failed to delete easter egg")
		return
	}

	h.metrics.ObserveRequestDuration("DeleteEasterEgg", start)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"message": "Easter egg deleted successfully",
		"id":      id,
	})
}

// ImportEasterEggs handles bulk import of easter eggs from YAML data
func (h *EasterEggsHandlers) ImportEasterEggs(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer h.metrics.ObserveRequestDuration("ImportEasterEggs", start)

	// Parse request body containing YAML path or data
	var req struct {
		YAMLPath string `json:"yaml_path,omitempty"`
		YAMLData string `json:"yaml_data,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Errorf("Failed to decode import request: %v", err)
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	var yamlPath string
	if req.YAMLPath != "" {
		yamlPath = req.YAMLPath
	} else if req.YAMLData != "" {
		// For inline YAML data, we'd need to write it to a temp file
		// For now, we'll expect a file path
		h.respondWithError(w, http.StatusBadRequest, "YAML data import not implemented, use yaml_path")
		return
	} else {
		h.respondWithError(w, http.StatusBadRequest, "Either yaml_path or yaml_data must be provided")
		return
	}

	// Call service import method
	result, err := h.service.ImportEasterEggsFromYAML(r.Context(), yamlPath)
	if err != nil {
		h.logger.Errorf("Failed to import easter eggs from %s: %v", yamlPath, err)
		h.respondWithError(w, http.StatusInternalServerError, fmt.Sprintf("Import failed: %v", err))
		return
	}

	h.logger.Infof("Successfully imported %d easter eggs, %d errors",
		result.SuccessfullyAdded, len(result.Errors))

	h.respondWithJSON(w, http.StatusOK, result)
}

// Helper methods

func (h *EasterEggsHandlers) parsePaginationParams(r *http.Request) (limit, offset int) {
	limit = 50  // default
	offset = 0

	if l := r.URL.Query().Get("limit"); l != "" {
		if parsed, err := strconv.Atoi(l); err == nil && parsed > 0 && parsed <= 100 {
			limit = parsed
		}
	}

	if o := r.URL.Query().Get("offset"); o != "" {
		if parsed, err := strconv.Atoi(o); err == nil && parsed >= 0 {
			offset = parsed
		}
	}

	return limit, offset
}

func (h *EasterEggsHandlers) respondWithJSON(w http.ResponseWriter, status int, payload interface{}) {
	response, err := json.Marshal(payload)
	if err != nil {
		h.logger.Errorf("Failed to marshal JSON response: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	w.Write(response)
}

func (h *EasterEggsHandlers) respondWithError(w http.ResponseWriter, status int, message string) {
	h.respondWithJSON(w, status, map[string]interface{}{
		"error":   true,
		"message": message,
		"timestamp": time.Now().UTC().Format(time.RFC3339),
	})
}
