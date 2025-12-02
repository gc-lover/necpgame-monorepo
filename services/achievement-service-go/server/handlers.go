// Issue: #138
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame/services/achievement-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type Handlers struct {
	service Service
}

func NewHandlers(service Service) *Handlers {
	return &Handlers{service: service}
}

func (h *Handlers) ListAchievements(w http.ResponseWriter, r *http.Request, params api.ListAchievementsParams) {
	response, err := h.service.ListAchievements(r.Context(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetAchievement(w http.ResponseWriter, r *http.Request, achievementId openapi_types.UUID) {
	response, err := h.service.GetAchievement(r.Context(), achievementId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, "Achievement not found")
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetPlayerAchievements(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetPlayerAchievementsParams) {
	response, err := h.service.GetPlayerAchievements(r.Context(), playerId.String(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) ClaimAchievement(w http.ResponseWriter, r *http.Request, achievementId openapi_types.UUID) {
	response, err := h.service.ClaimAchievement(r.Context(), achievementId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) UpdateProgress(w http.ResponseWriter, r *http.Request) {
	var req api.UpdateProgressRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.UpdateProgress(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetAchievementStats(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	response, err := h.service.GetAchievementStats(r.Context(), playerId.String())
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func respondJSON(w http.ResponseWriter, code int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, code int, message string) {
	respondJSON(w, code, map[string]string{"error": message})
}
