package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/combat-implants-stats-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type StatsHandlers struct {
	logger *logrus.Logger
}

func NewStatsHandlers() *StatsHandlers {
	return &StatsHandlers{
		logger: GetLogger(),
	}
}

func (h *StatsHandlers) GetEnergyStatus(w http.ResponseWriter, r *http.Request, params api.GetEnergyStatusParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("character_id", params.CharacterId).Info("GetEnergyStatus request")

	current := float32(100.0)
	max := float32(100.0)
	consumption := float32(0.0)
	overheated := false
	coolingRate := float32(1.0)

	response := api.EnergyStatus{
		Current:     &current,
		Max:         &max,
		Consumption: &consumption,
		Overheated:  &overheated,
		CoolingRate: &coolingRate,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *StatsHandlers) GetHumanityStatus(w http.ResponseWriter, r *http.Request, params api.GetHumanityStatusParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("character_id", params.CharacterId).Info("GetHumanityStatus request")

	current := float32(100.0)
	max := float32(100.0)
	cyberpsychosisRisk := float32(0.0)
	implantCount := 0

	response := api.HumanityStatus{
		Current:           &current,
		Max:               &max,
		CyberpsychosisRisk: &cyberpsychosisRisk,
		ImplantCount:      &implantCount,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *StatsHandlers) CheckCompatibility(w http.ResponseWriter, r *http.Request, params api.CheckCompatibilityParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"character_id": params.CharacterId,
		"implant_id":   params.ImplantId,
	}).Info("CheckCompatibility request")

	compatible := true
	conflicts := []struct {
		ImplantId *openapi_types.UUID `json:"implant_id,omitempty"`
		Reason    *string             `json:"reason,omitempty"`
	}{}
	warnings := []string{}
	availableEnergy := float32(100.0)
	requiredEnergy := float32(10.0)
	sufficientEnergy := true
	availableHumanity := float32(100.0)
	requiredHumanity := float32(5.0)
	sufficientHumanity := true

	energyCheck := struct {
		Available  *float32 `json:"available,omitempty"`
		Required   *float32 `json:"required,omitempty"`
		Sufficient *bool    `json:"sufficient,omitempty"`
	}{
		Available:  &availableEnergy,
		Required:   &requiredEnergy,
		Sufficient: &sufficientEnergy,
	}

	humanityCheck := struct {
		Available  *float32 `json:"available,omitempty"`
		Required   *float32 `json:"required,omitempty"`
		Sufficient *bool    `json:"sufficient,omitempty"`
	}{
		Available:  &availableHumanity,
		Required:   &requiredHumanity,
		Sufficient: &sufficientHumanity,
	}

	response := api.CompatibilityResult{
		Compatible:  &compatible,
		Conflicts:   &conflicts,
		Warnings:    &warnings,
		EnergyCheck: &energyCheck,
		HumanityCheck: &humanityCheck,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *StatsHandlers) GetSetBonuses(w http.ResponseWriter, r *http.Request, params api.GetSetBonusesParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("character_id", params.CharacterId).Info("GetSetBonuses request")

	activeSets := []struct {
		Bonuses       *[]struct {
			Description *string  `json:"description,omitempty"`
			Name        *string  `json:"name,omitempty"`
			Value       *float32 `json:"value,omitempty"`
		} `json:"bonuses,omitempty"`
		Brand         *string `json:"brand,omitempty"`
		ImplantsCount *int    `json:"implants_count,omitempty"`
	}{}

	response := api.SetBonuses{
		ActiveSets: &activeSets,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *StatsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *StatsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	h.respondJSON(w, status, errorResponse)
}

