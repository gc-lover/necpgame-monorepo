package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
)

func (s *HTTPServer) getEngramChipTiers(w http.ResponseWriter, r *http.Request) {
	if s.engramChipsService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram chips service not initialized")
		return
	}

	var leagueYear *int
	if yearStr := r.URL.Query().Get("league_year"); yearStr != "" {
		year, err := strconv.Atoi(yearStr)
		if err == nil && year >= 2020 && year <= 2093 {
			leagueYear = &year
		}
	}

	tiers, err := s.engramChipsService.GetChipTiers(r.Context(), leagueYear)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chip tiers")
		s.respondError(w, http.StatusInternalServerError, "failed to get chip tiers")
		return
	}

	response := make([]map[string]interface{}, 0, len(tiers))
	for _, tier := range tiers {
		response = append(response, convertTierToAPI(tier))
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getEngramChipTier(w http.ResponseWriter, r *http.Request) {
	if s.engramChipsService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram chips service not initialized")
		return
	}

	vars := mux.Vars(r)
	chipIDStr := vars["chip_id"]

	chipID, err := uuid.Parse(chipIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid chip id")
		return
	}

	tier, err := s.engramChipsService.GetChipTier(r.Context(), chipID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chip tier")
		if err == ErrChipNotFound {
			s.respondError(w, http.StatusNotFound, "chip not found")
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to get chip tier")
		return
	}

	s.respondJSON(w, http.StatusOK, convertTierToAPI(tier))
}

func (s *HTTPServer) getEngramChipDecay(w http.ResponseWriter, r *http.Request) {
	if s.engramChipsService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram chips service not initialized")
		return
	}

	vars := mux.Vars(r)
	chipIDStr := vars["chip_id"]

	chipID, err := uuid.Parse(chipIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid chip id")
		return
	}

	decay, err := s.engramChipsService.GetChipDecay(r.Context(), chipID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chip decay")
		if err == ErrChipNotFound {
			s.respondError(w, http.StatusNotFound, "chip not found")
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to get chip decay")
		return
	}

	response := convertDecayToAPI(decay)
	s.respondJSON(w, http.StatusOK, response)
}

func convertTierToAPI(tier *EngramChipTierInfo) map[string]interface{} {
	result := map[string]interface{}{
		"tier":                 tier.Tier,
		"tier_name":            tier.TierName,
		"stability_level":      tier.StabilityLevel,
		"lifespan_years":       tier.LifespanYears,
		"corruption_risk":      tier.CorruptionRisk,
		"corruption_risk_percent": tier.CorruptionRiskPercent,
		"protection_level":     tier.ProtectionLevel,
		"creation_cost_min":    tier.CreationCostMin,
		"creation_cost_max":    tier.CreationCostMax,
		"available_from_year":  tier.AvailableFromYear,
		"is_available":         tier.IsAvailable,
	}

	if tier.LifespanRange != nil {
		result["lifespan_range"] = map[string]interface{}{
			"min": tier.LifespanRange.Min,
			"max": tier.LifespanRange.Max,
		}
	}

	return result
}

func convertDecayToAPI(decay *EngramChipDecayInfo) map[string]interface{} {
	result := map[string]interface{}{
		"chip_id":       decay.ChipID.String(),
		"decay_percent": decay.DecayPercent,
		"decay_risk":    decay.DecayRisk,
		"storage_conditions": map[string]interface{}{
			"temperature":            decay.StorageConditions.Temperature,
			"humidity":               decay.StorageConditions.Humidity,
			"electromagnetic_shield": decay.StorageConditions.ElectromagneticShield,
			"storage_time_outside":   decay.StorageConditions.StorageTimeOutside,
		},
		"decay_effects": decay.DecayEffects,
	}

	if decay.TimeUntilCritical != nil {
		result["time_until_critical"] = *decay.TimeUntilCritical
	}

	return result
}

