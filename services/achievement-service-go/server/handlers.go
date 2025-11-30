package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/necpgame/achievement-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type AchievementHandlers struct {
	service AchievementServiceInterface
	logger  *logrus.Logger
}

func NewAchievementHandlers(service AchievementServiceInterface) *AchievementHandlers {
	return &AchievementHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *AchievementHandlers) GetAchievements(w http.ResponseWriter, r *http.Request, params api.GetAchievementsParams) {
	ctx := r.Context()

	var category *models.AchievementCategory
	if params.Category != nil {
		cat := models.AchievementCategory(*params.Category)
		category = &cat
	}

	limit := 50
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	response, err := h.service.ListAchievements(ctx, category, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list achievements")
		h.respondError(w, http.StatusInternalServerError, "failed to list achievements")
		return
	}

	apiAchievements := make([]api.Achievement, len(response.Achievements))
	for i, ach := range response.Achievements {
		apiAchievements[i] = toAPIAchievement(&ach)
	}

	h.respondJSON(w, http.StatusOK, apiAchievements)
}

func (h *AchievementHandlers) GetAchievement(w http.ResponseWriter, r *http.Request, achievementId openapi_types.UUID) {
	ctx := r.Context()
	achievementID := uuid.UUID(achievementId)

	achievement, err := h.service.GetAchievement(ctx, achievementID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get achievement")
		if err.Error() == "achievement not found" {
			h.respondError(w, http.StatusNotFound, "achievement not found")
		} else {
			h.respondError(w, http.StatusInternalServerError, "failed to get achievement")
		}
		return
	}

	if achievement == nil {
		h.respondError(w, http.StatusNotFound, "achievement not found")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIAchievement(achievement))
}

// Issue: #141886468
func (h *AchievementHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *AchievementHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

func toAPIAchievement(ach *models.Achievement) api.Achievement {
	if ach == nil {
		return api.Achievement{}
	}

	apiID := openapi_types.UUID(ach.ID)
	apiCategory := api.AchievementCategory(ach.Category)
	apiRarity := api.AchievementRarity(ach.Rarity)
	apiType := api.AchievementType(ach.Type)

	var apiSeasonID *openapi_types.UUID
	if ach.SeasonID != nil {
		id := openapi_types.UUID(*ach.SeasonID)
		apiSeasonID = &id
	}

	return api.Achievement{
		Id:          &apiID,
		Code:        &ach.Code,
		Title:       &ach.Title,
		Description: &ach.Description,
		Category:    &apiCategory,
		Rarity:      &apiRarity,
		Type:        &apiType,
		Points:      &ach.Points,
		Conditions:  &ach.Conditions,
		Rewards:     &ach.Rewards,
		IsHidden:    &ach.IsHidden,
		IsSeasonal:  &ach.IsSeasonal,
		SeasonId:    apiSeasonID,
		CreatedAt:   &ach.CreatedAt,
		UpdatedAt:   &ach.UpdatedAt,
	}
}
