// Package server SQL queries use prepared statements with placeholders ($1, $2, ?) for safety
package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
)

func (s *HTTPServer) getEngramSlots(w http.ResponseWriter, r *http.Request) {
	if s.engramService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram service not initialized")
		return
	}

	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	slots, err := s.engramService.GetEngramSlots(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get engram slots")
		s.respondError(w, http.StatusInternalServerError, "failed to get engram slots")
		return
	}

	response := map[string]interface{}{
		"slots": convertSlotsToAPI(slots),
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) installEngram(w http.ResponseWriter, r *http.Request) {
	if s.engramService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram service not initialized")
		return
	}

	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]
	slotIDStr := vars["slotId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	slotID, err := strconv.Atoi(slotIDStr)
	if err != nil || slotID < 1 || slotID > 3 {
		s.respondError(w, http.StatusBadRequest, "invalid slot id (must be 1-3)")
		return
	}

	var req struct {
		EngramID              uuid.UUID `json:"engram_id"`
		ValidateCompatibility *bool     `json:"validate_compatibility,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	validateCompatibility := true
	if req.ValidateCompatibility != nil {
		validateCompatibility = *req.ValidateCompatibility
	}

	slot, err := s.engramService.InstallEngram(r.Context(), characterID, slotID, req.EngramID, validateCompatibility)
	if err != nil {
		s.logger.WithError(err).Error("Failed to install engram")
		if err == ErrSlotAlreadyOccupied {
			s.respondError(w, http.StatusConflict, "slot already occupied")
			return
		}
		if err == ErrInvalidSlotID {
			s.respondError(w, http.StatusBadRequest, "invalid slot id")
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to install engram")
		return
	}

	s.respondJSON(w, http.StatusOK, convertSlotToAPI(slot))
}

func (s *HTTPServer) removeEngram(w http.ResponseWriter, r *http.Request) {
	if s.engramService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram service not initialized")
		return
	}

	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]
	slotIDStr := vars["slotId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	slotID, err := strconv.Atoi(slotIDStr)
	if err != nil || slotID < 1 || slotID > 3 {
		s.respondError(w, http.StatusBadRequest, "invalid slot id (must be 1-3)")
		return
	}

	removalType := r.URL.Query().Get("removal_type")
	if removalType == "" {
		removalType = "safe"
	}

	result, err := s.engramService.RemoveEngram(r.Context(), characterID, slotID, removalType)
	if err != nil {
		s.logger.WithError(err).Error("Failed to remove engram")
		s.respondError(w, http.StatusInternalServerError, "failed to remove engram")
		return
	}

	response := map[string]interface{}{
		"success":        result.Success,
		"removal_risk":   result.RemovalRisk,
		"penalties":      result.Penalties,
		"cooldown_until": result.CooldownUntil,
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getActiveEngrams(w http.ResponseWriter, r *http.Request) {
	if s.engramService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram service not initialized")
		return
	}

	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	slots, err := s.engramService.GetActiveEngrams(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get active engrams")
		s.respondError(w, http.StatusInternalServerError, "failed to get active engrams")
		return
	}

	activeEngrams := make([]map[string]interface{}, 0, len(slots))
	for _, slot := range slots {
		if slot.EngramID != nil {
			activeEngrams = append(activeEngrams, convertActiveEngramToAPI(slot))
		}
	}

	s.respondJSON(w, http.StatusOK, activeEngrams)
}

func (s *HTTPServer) getEngramInfluence(w http.ResponseWriter, r *http.Request) {
	if s.engramService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram service not initialized")
		return
	}

	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]
	engramIDStr := vars["engramId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	engramID, err := uuid.Parse(engramIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid engram id")
		return
	}

	influence, err := s.engramService.GetEngramInfluence(r.Context(), characterID, engramID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get engram influence")
		if err == ErrEngramNotFound {
			s.respondError(w, http.StatusNotFound, "engram not found")
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to get engram influence")
		return
	}

	response := convertInfluenceInfoToAPI(influence)
	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) updateEngramInfluence(w http.ResponseWriter, r *http.Request) {
	if s.engramService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram service not initialized")
		return
	}

	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]
	engramIDStr := vars["engramId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	engramID, err := uuid.Parse(engramIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid engram id")
		return
	}

	var req struct {
		ChangeReason string                 `json:"change_reason"`
		ChangeAmount *float64               `json:"change_amount,omitempty"`
		ActionData   map[string]interface{} `json:"action_data,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.ChangeReason == "" {
		s.respondError(w, http.StatusBadRequest, "change_reason is required")
		return
	}

	changeAmount := 0.0
	if req.ChangeAmount != nil {
		changeAmount = *req.ChangeAmount
	}

	influence, err := s.engramService.UpdateEngramInfluence(r.Context(), characterID, engramID, req.ChangeReason, changeAmount, req.ActionData)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update engram influence")
		if err == ErrEngramNotFound {
			s.respondError(w, http.StatusNotFound, "engram not found")
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to update engram influence")
		return
	}

	response := convertInfluenceInfoToAPI(influence)
	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getEngramInfluenceLevels(w http.ResponseWriter, r *http.Request) {
	if s.engramService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram service not initialized")
		return
	}

	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	levels, err := s.engramService.GetEngramInfluenceLevels(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get engram influence levels")
		s.respondError(w, http.StatusInternalServerError, "failed to get engram influence levels")
		return
	}

	response := make([]map[string]interface{}, 0, len(levels))
	for _, level := range levels {
		response = append(response, convertInfluenceLevelToAPI(level))
	}

	s.respondJSON(w, http.StatusOK, response)
}

func convertSlotsToAPI(slots []*EngramSlot) []map[string]interface{} {
	result := make([]map[string]interface{}, 0, len(slots))
	for _, slot := range slots {
		result = append(result, convertSlotToAPI(slot))
	}
	return result
}

func convertSlotToAPI(slot *EngramSlot) map[string]interface{} {
	result := map[string]interface{}{
		"slot_id":      slot.SlotID,
		"character_id": slot.CharacterID.String(),
		"is_active":    slot.IsActive,
		"created_at":   slot.CreatedAt.Format("2006-01-02T15:04:05Z07:00"),
		"updated_at":   slot.UpdatedAt.Format("2006-01-02T15:04:05Z07:00"),
	}

	if slot.EngramID != nil {
		result["engram_id"] = slot.EngramID.String()
	}
	if slot.InstalledAt != nil {
		result["installed_at"] = slot.InstalledAt.Format("2006-01-02T15:04:05Z07:00")
	}
	if slot.InfluenceLevel > 0 {
		result["influence_level"] = slot.InfluenceLevel
	}
	if slot.UsagePoints > 0 {
		result["usage_points"] = slot.UsagePoints
	}

	return result
}

func convertActiveEngramToAPI(slot *EngramSlot) map[string]interface{} {
	category := getInfluenceCategory(slot.InfluenceLevel)

	result := map[string]interface{}{
		"engram_id":                slot.EngramID.String(),
		"slot_id":                  slot.SlotID,
		"influence_level":          slot.InfluenceLevel,
		"influence_level_category": category,
		"usage_points":             slot.UsagePoints,
		"installed_at":             slot.InstalledAt.Format("2006-01-02T15:04:05Z07:00"),
	}

	return result
}

func convertInfluenceInfoToAPI(info *EngramInfluenceInfo) map[string]interface{} {
	result := map[string]interface{}{
		"engram_id":                info.EngramID.String(),
		"influence_level":          info.InfluenceLevel,
		"influence_level_category": info.InfluenceCategory,
		"usage_points":             info.UsagePoints,
		"growth_rate":              info.GrowthRate,
		"blocker_reduction":        info.BlockerReduction,
	}

	if info.SlotID > 0 {
		result["slot_id"] = info.SlotID
	}

	return result
}

func convertInfluenceLevelToAPI(level *EngramInfluenceLevel) map[string]interface{} {
	result := map[string]interface{}{
		"engram_id":                level.EngramID.String(),
		"slot_id":                  level.SlotID,
		"influence_level":          level.InfluenceLevel,
		"influence_level_category": level.InfluenceCategory,
		"usage_points":             level.UsagePoints,
		"dominance_percentage":     level.DominancePercentage,
	}

	return result
}

func getInfluenceCategory(level float64) string {
	if level < 20 {
		return "low"
	} else if level < 50 {
		return "medium"
	} else if level < 70 {
		return "high"
	} else if level < 90 {
		return "critical"
	}

	return "takeover"
}
