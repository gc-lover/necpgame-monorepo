// Issue: #141886468
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
)

type EngramCyberpsychosisHandlers struct {
	cyberpsychosisService EngramCyberpsychosisServiceInterface
	logger                *logrus.Logger
}

func NewEngramCyberpsychosisHandlers(cyberpsychosisService EngramCyberpsychosisServiceInterface) *EngramCyberpsychosisHandlers {
	return &EngramCyberpsychosisHandlers{
		cyberpsychosisService: cyberpsychosisService,
		logger:                GetLogger(),
	}
}

func (h *EngramCyberpsychosisHandlers) GetEngramCyberpsychosisRisk(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid character ID")
		return
	}

	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()
	result, err := h.cyberpsychosisService.GetCyberpsychosisRisk(ctx, characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get cyberpsychosis risk")
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve cyberpsychosis risk")
		return
	}

	response := map[string]interface{}{
		"character_id":      result.CharacterID.String(),
		"base_risk":         result.BaseRisk,
		"engram_risk":       result.EngramRisk,
		"total_risk":        result.TotalRisk,
		"blocker_reduction": result.BlockerReduction,
		"risk_factors":      result.RiskFactors,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *EngramCyberpsychosisHandlers) UpdateEngramCyberpsychosisRisk(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid character ID")
		return
	}

	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()
	result, err := h.cyberpsychosisService.UpdateCyberpsychosisRisk(ctx, characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update cyberpsychosis risk")
		h.respondError(w, http.StatusInternalServerError, "Failed to update cyberpsychosis risk")
		return
	}

	response := map[string]interface{}{
		"character_id":      result.CharacterID.String(),
		"base_risk":         result.BaseRisk,
		"engram_risk":       result.EngramRisk,
		"total_risk":        result.TotalRisk,
		"blocker_reduction": result.BlockerReduction,
		"risk_factors":      result.RiskFactors,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *EngramCyberpsychosisHandlers) GetEngramBlockers(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid character ID")
		return
	}

	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()
	blockers, err := h.cyberpsychosisService.GetBlockers(ctx, characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get engram blockers")
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve engram blockers")
		return
	}

	var result []map[string]interface{}
	for _, blocker := range blockers {
		blockerMap := map[string]interface{}{
			"blocker_id":          blocker.BlockerID.String(),
			"character_id":        blocker.CharacterID.String(),
			"tier":                blocker.Tier,
			"risk_reduction":      blocker.RiskReduction,
			"influence_reduction": blocker.InfluenceReduction,
			"duration_days":       blocker.DurationDays,
			"installed_at":        blocker.InstalledAt.Format("2006-01-02T15:04:05Z07:00"),
			"expires_at":          blocker.ExpiresAt.Format("2006-01-02T15:04:05Z07:00"),
			"is_active":           blocker.IsActive,
		}

		if blocker.Buffs != nil {
			blockerMap["buffs"] = blocker.Buffs
		}
		if blocker.Debuffs != nil {
			blockerMap["debuffs"] = blocker.Debuffs
		}

		result = append(result, blockerMap)
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *EngramCyberpsychosisHandlers) InstallEngramBlocker(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid character ID")
		return
	}

	var req struct {
		BlockerTier int `json:"blocker_tier"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if req.BlockerTier < 1 || req.BlockerTier > 5 {
		h.respondError(w, http.StatusBadRequest, "blocker_tier must be 1-5")
		return
	}

	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()
	result, err := h.cyberpsychosisService.InstallBlocker(ctx, characterID, req.BlockerTier)
	if err != nil {
		h.logger.WithError(err).Error("Failed to install blocker")
		h.respondError(w, http.StatusInternalServerError, "Failed to install blocker")
		return
	}

	response := map[string]interface{}{
		"blocker_id":          result.BlockerID.String(),
		"character_id":        result.CharacterID.String(),
		"tier":                result.Tier,
		"risk_reduction":      result.RiskReduction,
		"influence_reduction": result.InfluenceReduction,
		"duration_days":       result.DurationDays,
		"installed_at":        result.InstalledAt.Format("2006-01-02T15:04:05Z07:00"),
		"expires_at":          result.ExpiresAt.Format("2006-01-02T15:04:05Z07:00"),
		"is_active":           result.IsActive,
	}

	if result.Buffs != nil {
		response["buffs"] = result.Buffs
	}
	if result.Debuffs != nil {
		response["debuffs"] = result.Debuffs
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *EngramCyberpsychosisHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *EngramCyberpsychosisHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}
