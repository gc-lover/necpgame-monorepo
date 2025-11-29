package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/character-engram-core-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type EngramHandlers struct{}

func NewEngramHandlers() *EngramHandlers {
	return &EngramHandlers{}
}

func (h *EngramHandlers) GetEngramSlots(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	zeroFloat := float32(0)
	slots := []api.EngramSlot{
		{
			CharacterId:     characterId,
			SlotId:          1,
			EngramId:        nil,
			InfluenceLevel:  &zeroFloat,
			IsActive:        true,
			CreatedAt:       nil,
			InstalledAt:     nil,
			UpdatedAt:       nil,
			UsagePoints:     nil,
		},
		{
			CharacterId:     characterId,
			SlotId:          2,
			EngramId:        nil,
			InfluenceLevel:  &zeroFloat,
			IsActive:        true,
			CreatedAt:       nil,
			InstalledAt:     nil,
			UpdatedAt:       nil,
			UsagePoints:     nil,
		},
	}

	response := api.EngramSlotsResponse{
		Slots: slots,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *EngramHandlers) InstallEngram(w http.ResponseWriter, r *http.Request, characterId api.CharacterId, slotId int) {
	var req api.InstallEngramRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	now := time.Now()
	zeroFloat := float32(0)
	response := api.EngramSlot{
		CharacterId:     characterId,
		EngramId:        &req.EngramId,
		SlotId:          slotId,
		InstalledAt:     &now,
		InfluenceLevel:  &zeroFloat,
		IsActive:        true,
		CreatedAt:       &now,
		UpdatedAt:       &now,
		UsagePoints:     nil,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *EngramHandlers) RemoveEngram(w http.ResponseWriter, r *http.Request, characterId api.CharacterId, slotId int, params api.RemoveEngramParams) {
	now := time.Now()
	cooldownUntil := now.Add(24 * time.Hour)
	penalties := []string{"temporary_stat_reduction"}

	response := api.RemoveEngramResponse{
		CooldownUntil: &cooldownUntil,
		Penalties:     &penalties,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *EngramHandlers) GetActiveEngrams(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	engramId := openapi_types.UUID{}
	cat := api.ActiveEngramInfluenceLevelCategoryMedium
	installedAt := time.Now().Add(-24 * time.Hour)
	usagePoints := 100
	engrams := []api.ActiveEngram{
		{
			EngramId:              engramId,
			SlotId:                1,
			InfluenceLevel:        50.0,
			InfluenceLevelCategory: &cat,
			InstalledAt:           &installedAt,
			UsagePoints:           &usagePoints,
		},
	}

	respondJSON(w, http.StatusOK, engrams)
}

func (h *EngramHandlers) GetEngramInfluence(w http.ResponseWriter, r *http.Request, characterId api.CharacterId, engramId openapi_types.UUID) {
	influence := api.EngramInfluence{
		EngramId:              engramId,
		SlotId:                func() *int {
			v := 1
			return &v
		}(),
		InfluenceLevel:        50.0,
		InfluenceLevelCategory: func() *api.EngramInfluenceInfluenceLevelCategory {
			cat := api.EngramInfluenceInfluenceLevelCategoryMedium
			return &cat
		}(),
		UsagePoints:     100,
		GrowthRate:       func() *float32 {
			v := float32(1.0)
			return &v
		}(),
		BlockerReduction: nil,
	}

	respondJSON(w, http.StatusOK, influence)
}

func (h *EngramHandlers) UpdateEngramInfluence(w http.ResponseWriter, r *http.Request, characterId api.CharacterId, engramId openapi_types.UUID) {
	var req api.UpdateEngramInfluenceJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	influence := api.EngramInfluence{
		EngramId:              engramId,
		SlotId:                func() *int {
			v := 1
			return &v
		}(),
		InfluenceLevel:        60.0,
		InfluenceLevelCategory: func() *api.EngramInfluenceInfluenceLevelCategory {
			cat := api.EngramInfluenceInfluenceLevelCategoryHigh
			return &cat
		}(),
		UsagePoints:     150,
		GrowthRate:       func() *float32 {
			v := float32(1.2)
			return &v
		}(),
		BlockerReduction: nil,
	}

	respondJSON(w, http.StatusOK, influence)
}

func (h *EngramHandlers) GetEngramInfluenceLevels(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	engramId := openapi_types.UUID{}
	usagePoints := 100
	dominancePercentage := float32(25.0)
	levels := []api.EngramInfluenceLevel{
		{
			EngramId:              engramId,
			SlotId:                1,
			InfluenceLevel:        50.0,
			InfluenceLevelCategory: api.Medium,
			UsagePoints:           &usagePoints,
			DominancePercentage:   &dominancePercentage,
		},
	}

	respondJSON(w, http.StatusOK, levels)
}

