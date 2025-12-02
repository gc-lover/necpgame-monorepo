// Issue: #138
package server

import (
	"encoding/json"
	"net/http"

	openapi_types "github.com/oapi-codegen/runtime/types"

	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/pkg/api"
)

type Handlers struct {
	service *Service
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// GetAchievements implements GET /api/v1/world/achievements
func (h *Handlers) GetAchievements(w http.ResponseWriter, r *http.Request, params api.GetAchievementsParams) {
	achievements, err := h.service.GetAchievements(r.Context(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	response := map[string]interface{}{
		"achievements": achievements,
	}
	respondJSON(w, http.StatusOK, response)
}

// GetAchievementDetails implements GET /api/v1/world/achievements/{achievementId}
func (h *Handlers) GetAchievementDetails(w http.ResponseWriter, r *http.Request, achievementId openapi_types.UUID) {
	details, err := h.service.GetAchievementDetails(r.Context(), achievementId.String())
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Achievement not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, details)
}

// GetPlayerProgress implements GET /api/v1/players/{playerId}/achievements
func (h *Handlers) GetPlayerProgress(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetPlayerProgressParams) {
	progress, err := h.service.GetPlayerProgress(r.Context(), playerId.String(), params)
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Player not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, progress)
}

// ClaimAchievementReward implements POST /api/v1/players/{playerId}/achievements/{achievementId}/claim
func (h *Handlers) ClaimAchievementReward(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, achievementId openapi_types.UUID) {
	result, err := h.service.ClaimReward(r.Context(), playerId.String(), achievementId.String())
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Achievement not found or not unlocked")
			return
		}
		if err == ErrAlreadyClaimed {
			respondError(w, http.StatusBadRequest, "Reward already claimed")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, result)
}

// GetPlayerTitles implements GET /api/v1/players/{playerId}/titles
func (h *Handlers) GetPlayerTitles(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	titles, err := h.service.GetPlayerTitles(r.Context(), playerId.String())
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Player not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, titles)
}

// SetActiveTitle implements PUT /api/v1/players/{playerId}/titles/active
func (h *Handlers) SetActiveTitle(w http.ResponseWriter, r *http.Request, playerId string) {
	var req struct {
		TitleID string `json:"title_id"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	title, err := h.service.SetActiveTitle(r.Context(), playerId, req.TitleID)
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Title not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, title)
}

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, status int, message string) {
	respondJSON(w, status, map[string]string{"error": message})
}
