package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/battle-pass-service-go/models"
	"github.com/necpgame/battle-pass-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type BattlePassHandlers struct {
	service BattlePassServiceInterface
	logger  *logrus.Logger
}

func NewBattlePassHandlers(service BattlePassServiceInterface) *BattlePassHandlers {
	return &BattlePassHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *BattlePassHandlers) GetCurrentBattlePass(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	season, err := h.service.GetCurrentSeason(ctx)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get current season")
		h.respondError(w, http.StatusInternalServerError, "failed to get current season")
		return
	}

	if season == nil {
		h.respondError(w, http.StatusNotFound, "no active season")
		return
	}

	apiSeason := toAPIBattlePassSeason(season)
	h.respondJSON(w, http.StatusOK, apiSeason)
}

func (h *BattlePassHandlers) GetPlayerProgress(w http.ResponseWriter, r *http.Request, params api.GetPlayerProgressParams) {
	ctx := r.Context()

	characterID := uuid.UUID(params.CharacterId)
	var seasonID uuid.UUID

	if params.SeasonId != nil {
		seasonID = uuid.UUID(*params.SeasonId)
	} else {
		season, err := h.service.GetCurrentSeason(ctx)
		if err != nil {
			h.logger.WithError(err).Error("Failed to get current season")
			h.respondError(w, http.StatusInternalServerError, "failed to get current season")
			return
		}
		if season == nil {
			h.respondError(w, http.StatusNotFound, "no active season")
			return
		}
		seasonID = season.ID
	}

	progress, err := h.service.GetProgress(ctx, characterID, seasonID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get progress")
		h.respondError(w, http.StatusInternalServerError, "failed to get progress")
		return
	}

	apiProgress := toAPIPlayerBattlePassProgress(progress)
	h.respondJSON(w, http.StatusOK, apiProgress)
}

func (h *BattlePassHandlers) PurchasePremium(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req api.PurchasePremiumRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.CharacterId)
	progress, err := h.service.PurchasePremium(ctx, characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to purchase premium")
		h.respondError(w, http.StatusInternalServerError, "failed to purchase premium")
		return
	}

	apiProgress := toAPIPlayerBattlePassProgress(progress)
	h.respondJSON(w, http.StatusOK, apiProgress)
}

func (h *BattlePassHandlers) GetLevelRequirements(w http.ResponseWriter, r *http.Request, level int) {
	ctx := r.Context()

	if level < 1 || level > 100 {
		h.respondError(w, http.StatusBadRequest, "invalid level")
		return
	}

	requirements, err := h.service.GetLevelRequirements(ctx, level)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get level requirements")
		h.respondError(w, http.StatusInternalServerError, "failed to get level requirements")
		return
	}

	apiRequirements := toAPILevelRequirements(requirements)
	h.respondJSON(w, http.StatusOK, apiRequirements)
}

func (h *BattlePassHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *BattlePassHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

func toAPIBattlePassSeason(season *models.BattlePassSeason) *api.BattlePassSeason {
	if season == nil {
		return nil
	}

	apiID := openapi_types.UUID(season.ID)
	return &api.BattlePassSeason{
		Id:       &apiID,
		Name:     stringPtr(season.Name),
		StartDate: &season.StartDate,
		EndDate:   &season.EndDate,
		MaxLevel:  intPtr(season.MaxLevel),
		IsActive:  boolPtr(season.IsActive),
	}
}

func toAPIPlayerBattlePassProgress(progress *models.PlayerBattlePassProgress) *api.PlayerBattlePassProgress {
	if progress == nil {
		return nil
	}

	apiCharID := openapi_types.UUID(progress.CharacterID)
	apiSeasonID := openapi_types.UUID(progress.SeasonID)

	result := &api.PlayerBattlePassProgress{
		CharacterId:   &apiCharID,
		SeasonId:      &apiSeasonID,
		Level:         intPtr(progress.Level),
		Xp:            intPtr(progress.XP),
		XpToNextLevel: intPtr(progress.XPToNextLevel),
		HasPremium:    boolPtr(progress.HasPremium),
	}

	if progress.PremiumPurchasedAt != nil {
		result.PremiumPurchasedAt = progress.PremiumPurchasedAt
	}

	return result
}

func toAPILevelRequirements(reqs *models.LevelRequirements) *api.LevelRequirements {
	if reqs == nil {
		return nil
	}

	return &api.LevelRequirements{
		Level:        intPtr(reqs.Level),
		XpRequired:   intPtr(reqs.XPRequired),
		CumulativeXp: intPtr(reqs.CumulativeXP),
	}
}

func stringPtr(s string) *string {
	return &s
}

func boolPtr(b bool) *bool {
	return &b
}

func intPtr(i int) *int {
	return &i
}



