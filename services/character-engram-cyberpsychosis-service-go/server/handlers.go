package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/character-engram-cyberpsychosis-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type EngramCyberpsychosisHandlers struct{}

func NewEngramCyberpsychosisHandlers() *EngramCyberpsychosisHandlers {
	return &EngramCyberpsychosisHandlers{}
}

func (h *EngramCyberpsychosisHandlers) GetEngramCyberpsychosisRisk(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	baseRisk := float32(20.0)
	engramRisk := float32(30.0)
	totalRisk := float32(50.0)
	blockerReduction := float32(10.0)

	riskFactors := []api.RiskFactor{
		{
			FactorType: api.BasePerEngram,
			RiskAmount: 10.0,
			EngramId:   nil,
		},
		{
			FactorType: api.AggressiveEngram,
			RiskAmount: 5.0,
			EngramId:   nil,
		},
	}

	response := api.CyberpsychosisRisk{
		CharacterId:     characterId,
		BaseRisk:        baseRisk,
		EngramRisk:      engramRisk,
		TotalRisk:       totalRisk,
		RiskFactors:     &riskFactors,
		BlockerReduction: &blockerReduction,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *EngramCyberpsychosisHandlers) UpdateEngramCyberpsychosisRisk(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	baseRisk := float32(20.0)
	engramRisk := float32(30.0)
	totalRisk := float32(50.0)
	blockerReduction := float32(10.0)

	riskFactors := []api.RiskFactor{
		{
			FactorType: api.BasePerEngram,
			RiskAmount: 10.0,
			EngramId:   nil,
		},
	}

	response := api.CyberpsychosisRisk{
		CharacterId:      characterId,
		BaseRisk:         baseRisk,
		EngramRisk:       engramRisk,
		TotalRisk:        totalRisk,
		RiskFactors:      &riskFactors,
		BlockerReduction: &blockerReduction,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *EngramCyberpsychosisHandlers) GetEngramBlockers(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	blockerId := openapi_types.UUID{}
	now := time.Now()
	expiresAt := now.Add(60 * 24 * time.Hour)

	mentalResistance := float32(10.0)
	concentration := float32(5.0)
	energyRecovery := float32(-10.0)

	buffs := &struct {
		AllSkills        *float32 `json:"all_skills,omitempty"`
		Concentration   *float32 `json:"concentration,omitempty"`
		HackResistance  *float32 `json:"hack_resistance,omitempty"`
		MentalResistance *float32 `json:"mental_resistance,omitempty"`
	}{
		AllSkills:        nil,
		Concentration:   &concentration,
		HackResistance:  nil,
		MentalResistance: &mentalResistance,
	}

	debuffs := &struct {
		EnergyRecovery  *float32 `json:"energy_recovery,omitempty"`
		PhysicalStrength *float32 `json:"physical_strength,omitempty"`
	}{
		EnergyRecovery:  &energyRecovery,
		PhysicalStrength: nil,
	}

	blockers := []api.EngramBlocker{
		{
			BlockerId:         blockerId,
			Buffs:             buffs,
			CharacterId:      characterId,
			Debuffs:           debuffs,
			DurationDays:      func() *int { v := 60; return &v }(),
			ExpiresAt:         expiresAt,
			InfluenceReduction: func() *float32 { v := float32(20.0); return &v }(),
			InstalledAt:       now,
			IsActive:          func() *bool { v := true; return &v }(),
			RiskReduction:     func() *float32 { v := float32(10.0); return &v }(),
			Tier:              2,
		},
	}

	respondJSON(w, http.StatusOK, blockers)
}

func (h *EngramCyberpsychosisHandlers) InstallEngramBlocker(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	var req api.InstallBlockerRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	blockerId := openapi_types.UUID{}
	now := time.Now()
	var expiresAt time.Time
	var durationDays int

	switch req.BlockerTier {
	case 1:
		expiresAt = now.Add(30 * 24 * time.Hour)
		durationDays = 30
	case 2:
		expiresAt = now.Add(60 * 24 * time.Hour)
		durationDays = 60
	case 3:
		expiresAt = now.Add(90 * 24 * time.Hour)
		durationDays = 90
	case 4:
		expiresAt = now.Add(180 * 24 * time.Hour)
		durationDays = 180
	case 5:
		expiresAt = now.Add(365 * 24 * time.Hour)
		durationDays = 365
	default:
		expiresAt = now.Add(60 * 24 * time.Hour)
		durationDays = 60
	}

	riskReduction := float32(req.BlockerTier * 5)
	influenceReduction := float32(req.BlockerTier * 10)

	mentalResistance := float32(float32(req.BlockerTier) * 5.0)
	concentration := float32(float32(req.BlockerTier) * 2.5)
	energyRecovery := float32(-float32(req.BlockerTier) * 5.0)

	buffs := &struct {
		AllSkills        *float32 `json:"all_skills,omitempty"`
		Concentration   *float32 `json:"concentration,omitempty"`
		HackResistance  *float32 `json:"hack_resistance,omitempty"`
		MentalResistance *float32 `json:"mental_resistance,omitempty"`
	}{
		AllSkills:        nil,
		Concentration:   &concentration,
		HackResistance:  nil,
		MentalResistance: &mentalResistance,
	}

	debuffs := &struct {
		EnergyRecovery  *float32 `json:"energy_recovery,omitempty"`
		PhysicalStrength *float32 `json:"physical_strength,omitempty"`
	}{
		EnergyRecovery:  &energyRecovery,
		PhysicalStrength: nil,
	}

	response := api.EngramBlocker{
		BlockerId:         blockerId,
		Buffs:             buffs,
		CharacterId:      characterId,
		Debuffs:           debuffs,
		DurationDays:      func() *int { v := durationDays; return &v }(),
		ExpiresAt:         expiresAt,
		InfluenceReduction: &influenceReduction,
		InstalledAt:       now,
		IsActive:          func() *bool { v := true; return &v }(),
		RiskReduction:     &riskReduction,
		Tier:              req.BlockerTier,
	}

	respondJSON(w, http.StatusOK, response)
}

