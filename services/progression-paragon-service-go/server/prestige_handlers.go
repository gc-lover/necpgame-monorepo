package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	prestigeapi "github.com/necpgame/progression-paragon-service-go/pkg/api/prestige"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type PrestigeHandlers struct {
	service PrestigeServiceInterface
	logger  *logrus.Logger
}

func NewPrestigeHandlers(service PrestigeServiceInterface) *PrestigeHandlers {
	return &PrestigeHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *PrestigeHandlers) GetPrestigeInfo(w http.ResponseWriter, r *http.Request, params prestigeapi.GetPrestigeInfoParams) {
	characterID := uuid.UUID(params.CharacterId)

	info, err := h.service.GetPrestigeInfo(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get prestige info")
		h.respondError(w, http.StatusInternalServerError, "failed to get prestige info")
		return
	}

	if info == nil {
		h.respondError(w, http.StatusNotFound, "prestige info not found")
		return
	}

	apiInfo := convertPrestigeInfoToAPI(info)
	h.respondJSON(w, http.StatusOK, apiInfo)
}

func (h *PrestigeHandlers) ResetPrestige(w http.ResponseWriter, r *http.Request, params prestigeapi.ResetPrestigeParams) {
	characterID := uuid.UUID(params.CharacterId)

	var req prestigeapi.ResetPrestigeJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if !req.Confirm {
		h.respondError(w, http.StatusBadRequest, "prestige reset requires confirmation")
		return
	}

	info, err := h.service.ResetPrestige(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to reset prestige")
		if err.Error() == "maximum prestige level reached" {
			h.respondError(w, http.StatusForbidden, "maximum prestige level reached")
			return
		}
		if err.Error() == "prestige requirements not met" {
			h.respondError(w, http.StatusForbidden, "prestige requirements not met")
			return
		}
		h.respondError(w, http.StatusInternalServerError, "failed to reset prestige")
		return
	}

	apiInfo := convertPrestigeInfoToAPI(info)
	h.respondJSON(w, http.StatusOK, apiInfo)
}

func (h *PrestigeHandlers) GetPrestigeBonuses(w http.ResponseWriter, r *http.Request, params prestigeapi.GetPrestigeBonusesParams) {
	characterID := uuid.UUID(params.CharacterId)

	bonuses, err := h.service.GetPrestigeBonuses(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get prestige bonuses")
		h.respondError(w, http.StatusInternalServerError, "failed to get prestige bonuses")
		return
	}

	if bonuses == nil {
		h.respondError(w, http.StatusNotFound, "prestige bonuses not found")
		return
	}

	apiBonuses := convertPrestigeBonusesToAPI(bonuses)
	h.respondJSON(w, http.StatusOK, apiBonuses)
}

func convertPrestigeInfoToAPI(info *PrestigeInfo) prestigeapi.PrestigeInfo {
	characterID := openapi_types.UUID(info.CharacterID)
	prestigeLevel := info.PrestigeLevel
	resetCount := info.ResetCount
	bonusesApplied := make(map[string]float32)
	for k, v := range info.BonusesApplied {
		bonusesApplied[k] = float32(v)
	}
	
	var nextRequirements *struct {
		CompletedContent *[]string `json:"completed_content,omitempty"`
		MinLevel         *int      `json:"min_level,omitempty"`
		MinParagonLevel  *int      `json:"min_paragon_level,omitempty"`
	}
	if info.NextPrestigeRequirements != nil {
		completedContent := info.NextPrestigeRequirements.CompletedContent
		minLevel := info.NextPrestigeRequirements.MinLevel
		minParagonLevel := info.NextPrestigeRequirements.MinParagonLevel
		nextRequirements = &struct {
			CompletedContent *[]string `json:"completed_content,omitempty"`
			MinLevel         *int      `json:"min_level,omitempty"`
			MinParagonLevel  *int      `json:"min_paragon_level,omitempty"`
		}{
			CompletedContent: &completedContent,
			MinLevel:         &minLevel,
			MinParagonLevel:  &minParagonLevel,
		}
	}

	return prestigeapi.PrestigeInfo{
		CharacterId:            &characterID,
		PrestigeLevel:          &prestigeLevel,
		ResetCount:             &resetCount,
		BonusesApplied:         &bonusesApplied,
		NextPrestigeRequirements: nextRequirements,
		LastResetAt:            info.LastResetAt,
		UpdatedAt:             &info.UpdatedAt,
	}
}

func convertPrestigeBonusesToAPI(bonuses *PrestigeBonuses) prestigeapi.PrestigeBonuses {
	characterID := openapi_types.UUID(bonuses.CharacterID)
	prestigeLevel := bonuses.PrestigeLevel
	maxPrestigeLevel := bonuses.MaxPrestigeLevel
	
	availableBonuses := make([]struct {
		Description *string  `json:"description,omitempty"`
		Type        *string  `json:"type,omitempty"`
		Value       *float32 `json:"value,omitempty"`
	}, len(bonuses.AvailableBonuses))
	
	for i, b := range bonuses.AvailableBonuses {
		bonusType := b.Type
		value := float32(b.Value)
		description := b.Description
		availableBonuses[i] = struct {
			Description *string  `json:"description,omitempty"`
			Type        *string  `json:"type,omitempty"`
			Value       *float32 `json:"value,omitempty"`
		}{
			Type:        &bonusType,
			Value:       &value,
			Description: &description,
		}
	}

	return prestigeapi.PrestigeBonuses{
		CharacterId:      &characterID,
		PrestigeLevel:    &prestigeLevel,
		AvailableBonuses: &availableBonuses,
		MaxPrestigeLevel: &maxPrestigeLevel,
	}
}

func (h *PrestigeHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *PrestigeHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := map[string]string{
		"error":   http.StatusText(status),
		"message": message,
	}
	h.respondJSON(w, status, errorResponse)
}

