package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
)

func (s *HTTPServer) createEngram(w http.ResponseWriter, r *http.Request) {
	if s.engramCreationService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram creation service not initialized")
		return
	}

	var req struct {
		CharacterID            uuid.UUID              `json:"character_id"`
		ChipTier               int                    `json:"chip_tier"`
		AttitudeType           string                 `json:"attitude_type"`
		CustomAttitudeSettings map[string]interface{} `json:"custom_attitude_settings,omitempty"`
		TargetPersonID         *uuid.UUID             `json:"target_person_id,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.CharacterID == uuid.Nil {
		s.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	if req.ChipTier < 1 || req.ChipTier > 5 {
		s.respondError(w, http.StatusBadRequest, "chip_tier must be 1-5")
		return
	}

	if req.AttitudeType == "" {
		s.respondError(w, http.StatusBadRequest, "attitude_type is required")
		return
	}

	result, err := s.engramCreationService.CreateEngram(r.Context(), req.CharacterID, req.ChipTier, req.AttitudeType, req.CustomAttitudeSettings, req.TargetPersonID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create engram")
		s.respondError(w, http.StatusInternalServerError, "failed to create engram")
		return
	}

	response := map[string]interface{}{
		"engram_id":         result.EngramID.String(),
		"creation_id":       result.CreationID.String(),
		"success":           result.Success,
		"creation_stage":    result.CreationStage,
		"data_loss_percent": result.DataLossPercent,
		"is_complete":       result.IsComplete,
		"creation_cost":     result.CreationCost,
		"created_at":        result.CreatedAt.Format("2006-01-02T15:04:05Z07:00"),
	}

	s.respondJSON(w, http.StatusCreated, response)
}

func (s *HTTPServer) getEngramCreationCost(w http.ResponseWriter, r *http.Request) {
	if s.engramCreationService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram creation service not initialized")
		return
	}

	vars := mux.Vars(r)
	chipTierStr := vars["chip_tier"]
	if chipTierStr == "" {
		s.respondError(w, http.StatusBadRequest, "chip_tier is required")
		return
	}

	chipTier, err := strconv.Atoi(chipTierStr)
	if err != nil || chipTier < 1 || chipTier > 5 {
		s.respondError(w, http.StatusBadRequest, "invalid chip_tier (must be 1-5)")
		return
	}

	cost, err := s.engramCreationService.GetCreationCost(r.Context(), chipTier)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get creation cost")
		s.respondError(w, http.StatusInternalServerError, "failed to get creation cost")
		return
	}

	response := map[string]interface{}{
		"chip_tier":               cost.ChipTier,
		"creation_cost_min":       cost.CreationCostMin,
		"creation_cost_max":       cost.CreationCostMax,
		"purchase_cost_multiplier": cost.PurchaseCostMultiplier,
		"market_fluctuation":      cost.MarketFluctuation,
	}

	if cost.HistoricalMultiplier != nil {
		response["historical_multiplier"] = *cost.HistoricalMultiplier
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) validateEngramCreation(w http.ResponseWriter, r *http.Request) {
	if s.engramCreationService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram creation service not initialized")
		return
	}

	var req struct {
		CharacterID    uuid.UUID  `json:"character_id"`
		ChipTier       int        `json:"chip_tier"`
		TargetPersonID *uuid.UUID `json:"target_person_id,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.CharacterID == uuid.Nil {
		s.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	if req.ChipTier < 1 || req.ChipTier > 5 {
		s.respondError(w, http.StatusBadRequest, "chip_tier must be 1-5")
		return
	}

	result, err := s.engramCreationService.ValidateCreation(r.Context(), req.CharacterID, req.ChipTier, req.TargetPersonID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to validate creation")
		s.respondError(w, http.StatusInternalServerError, "failed to validate creation")
		return
	}

	response := map[string]interface{}{
		"is_valid":          result.IsValid,
		"validation_errors": result.ValidationErrors,
	}

	if result.Requirements != nil {
		response["requirements"] = map[string]interface{}{
			"technology_available": result.Requirements.TechnologyAvailable,
			"equipment_available":  result.Requirements.EquipmentAvailable,
			"reputation_met":       result.Requirements.ReputationMet,
			"skills_met":           result.Requirements.SkillsMet,
			"funds_available":      result.Requirements.FundsAvailable,
		}
	}

	if result.EstimatedCost != nil {
		response["estimated_cost"] = *result.EstimatedCost
	}

	s.respondJSON(w, http.StatusOK, response)
}

