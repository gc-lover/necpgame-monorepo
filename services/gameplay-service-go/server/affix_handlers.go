package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/necpgame/gameplay-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type AffixHandlers struct {
	service AffixServiceInterface
	logger  *logrus.Logger
}

func NewAffixHandlers(service AffixServiceInterface) *AffixHandlers {
	return &AffixHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *AffixHandlers) GetActiveAffixes(w http.ResponseWriter, r *http.Request) {
	response, err := h.service.GetActiveAffixes(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get active affixes")
		h.respondError(w, http.StatusInternalServerError, "failed to get active affixes")
		return
	}

	apiResponse := convertActiveAffixesToAPI(response)
	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *AffixHandlers) GetAffix(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	affixID := uuid.UUID(id)

	affix, err := h.service.GetAffix(r.Context(), affixID)
	if err != nil {
		if err.Error() == "affix not found" {
			h.respondError(w, http.StatusNotFound, "affix not found")
			return
		}
		h.logger.WithError(err).Error("Failed to get affix")
		h.respondError(w, http.StatusInternalServerError, "failed to get affix")
		return
	}

	apiAffix := convertAffixToAPI(affix)
	h.respondJSON(w, http.StatusOK, apiAffix)
}

func (h *AffixHandlers) GetInstanceAffixes(w http.ResponseWriter, r *http.Request, instanceId openapi_types.UUID) {
	instanceID := uuid.UUID(instanceId)

	response, err := h.service.GetInstanceAffixes(r.Context(), instanceID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get instance affixes")
		h.respondError(w, http.StatusInternalServerError, "failed to get instance affixes")
		return
	}

	apiResponse := convertInstanceAffixesToAPI(response)
	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *AffixHandlers) GetAffixRotationHistory(w http.ResponseWriter, r *http.Request, params api.GetAffixRotationHistoryParams) {
	weeksBack := 4
	if params.WeeksBack != nil {
		weeksBack = *params.WeeksBack
	}

	limit := 20
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	response, err := h.service.GetRotationHistory(r.Context(), weeksBack, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get rotation history")
		h.respondError(w, http.StatusInternalServerError, "failed to get rotation history")
		return
	}

	apiResponse := convertRotationHistoryToAPI(response)
	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *AffixHandlers) TriggerAffixRotation(w http.ResponseWriter, r *http.Request) {
	var req api.TriggerAffixRotationJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	force := false
	if req.Force != nil {
		force = *req.Force
	}

	customAffixes := []uuid.UUID{}
	if req.CustomAffixes != nil {
		customAffixes = make([]uuid.UUID, len(*req.CustomAffixes))
		for i, id := range *req.CustomAffixes {
			customAffixes[i] = uuid.UUID(id)
		}
	}

	rotation, err := h.service.TriggerRotation(r.Context(), force, customAffixes)
	if err != nil {
		if err.Error() == "rotation already exists for this week" {
			h.respondError(w, http.StatusBadRequest, err.Error())
			return
		}
		if err.Error() == "custom_affixes must contain 8-10 affixes" {
			h.respondError(w, http.StatusBadRequest, err.Error())
			return
		}
		h.logger.WithError(err).Error("Failed to trigger rotation")
		h.respondError(w, http.StatusInternalServerError, "failed to trigger rotation")
		return
	}

	apiRotation := convertRotationToAPI(rotation)
	h.respondJSON(w, http.StatusOK, apiRotation)
}

func convertActiveAffixesToAPI(response *models.ActiveAffixesResponse) api.ActiveAffixesResponse {
	weekStart := response.WeekStart
	weekEnd := response.WeekEnd

	activeAffixes := make([]api.AffixSummary, len(response.ActiveAffixes))
	for i, a := range response.ActiveAffixes {
		activeAffixes[i] = convertAffixSummaryToAPI(&a)
	}

	var seasonalAffix *api.AffixSummary
	if response.SeasonalAffix != nil {
		sa := convertAffixSummaryToAPI(response.SeasonalAffix)
		seasonalAffix = &sa
	}

	return api.ActiveAffixesResponse{
		WeekStart:     &weekStart,
		WeekEnd:       &weekEnd,
		ActiveAffixes: &activeAffixes,
		SeasonalAffix: seasonalAffix,
	}
}

func convertAffixToAPI(affix *models.Affix) api.Affix {
	id := openapi_types.UUID(affix.ID)
	name := affix.Name
	category := api.AffixCategory(affix.Category)
	description := affix.Description
	rewardModifier := float32(affix.RewardModifier)
	difficultyModifier := float32(affix.DifficultyModifier)
	createdAt := affix.CreatedAt

	var mechanics *api.Affix_Mechanics
	if affix.Mechanics != nil {
		mechanics = &api.Affix_Mechanics{}
		if trigger, ok := affix.Mechanics["trigger"].(string); ok {
			mechanics.Trigger = &trigger
		}
		if effectType, ok := affix.Mechanics["effect_type"].(string); ok {
			mechanics.EffectType = &effectType
		}
		if radius, ok := affix.Mechanics["radius"].(float64); ok {
			r := float32(radius)
			mechanics.Radius = &r
		}
		if damagePercent, ok := affix.Mechanics["damage_percent"].(float64); ok {
			dp := int(damagePercent)
			mechanics.DamagePercent = &dp
		}
		if damageType, ok := affix.Mechanics["damage_type"].(string); ok {
			mechanics.DamageType = &damageType
		}
	}

	var visualEffects *api.Affix_VisualEffects
	if affix.VisualEffects != nil {
		visualEffects = &api.Affix_VisualEffects{}
		if explosionParticle, ok := affix.VisualEffects["explosion_particle"].(string); ok {
			visualEffects.ExplosionParticle = &explosionParticle
		}
		if soundEffect, ok := affix.VisualEffects["sound_effect"].(string); ok {
			visualEffects.SoundEffect = &soundEffect
		}
		if screenShake, ok := affix.VisualEffects["screen_shake"].(bool); ok {
			visualEffects.ScreenShake = &screenShake
		}
	}

	return api.Affix{
		Id:                &id,
		Name:              &name,
		Category:          &category,
		Description:       &description,
		Mechanics:         mechanics,
		VisualEffects:     visualEffects,
		RewardModifier:    &rewardModifier,
		DifficultyModifier: &difficultyModifier,
		CreatedAt:         &createdAt,
	}
}

func convertAffixSummaryToAPI(summary *models.AffixSummary) api.AffixSummary {
	id := openapi_types.UUID(summary.ID)
	name := summary.Name
	category := api.AffixSummaryCategory(summary.Category)
	description := summary.Description
	rewardModifier := float32(summary.RewardModifier)
	difficultyModifier := float32(summary.DifficultyModifier)

	return api.AffixSummary{
		Id:                 &id,
		Name:               &name,
		Category:           &category,
		Description:        &description,
		RewardModifier:     &rewardModifier,
		DifficultyModifier: &difficultyModifier,
	}
}

func convertInstanceAffixesToAPI(response *models.InstanceAffixesResponse) api.InstanceAffixesResponse {
	instanceID := openapi_types.UUID(response.InstanceID)
	appliedAt := response.AppliedAt

	affixes := make([]api.AffixSummary, len(response.Affixes))
	for i, a := range response.Affixes {
		affixes[i] = convertAffixSummaryToAPI(&a)
	}

	totalRewardModifier := float32(response.TotalRewardModifier)
	totalDifficultyModifier := float32(response.TotalDifficultyModifier)

	return api.InstanceAffixesResponse{
		InstanceId:              &instanceID,
		AppliedAt:                &appliedAt,
		Affixes:                  &affixes,
		TotalRewardModifier:      &totalRewardModifier,
		TotalDifficultyModifier:  &totalDifficultyModifier,
	}
}

func convertRotationToAPI(rotation *models.AffixRotation) api.AffixRotation {
	id := openapi_types.UUID(rotation.ID)
	weekStart := rotation.WeekStart
	weekEnd := rotation.WeekEnd
	createdAt := rotation.CreatedAt

	activeAffixes := make([]api.AffixSummary, len(rotation.ActiveAffixes))
	for i, a := range rotation.ActiveAffixes {
		activeAffixes[i] = convertAffixSummaryToAPI(&a)
	}

	var seasonalAffix *api.AffixSummary
	if rotation.SeasonalAffix != nil {
		sa := convertAffixSummaryToAPI(rotation.SeasonalAffix)
		seasonalAffix = &sa
	}

	return api.AffixRotation{
		Id:            &id,
		WeekStart:     &weekStart,
		WeekEnd:       &weekEnd,
		ActiveAffixes: &activeAffixes,
		SeasonalAffix: seasonalAffix,
		CreatedAt:     &createdAt,
	}
}

func convertRotationHistoryToAPI(response *models.AffixRotationHistoryResponse) api.AffixRotationHistoryResponse {
	items := make([]api.AffixRotation, len(response.Items))
	for i, r := range response.Items {
		items[i] = convertRotationToAPI(&r)
	}

	total := response.Total
	limit := response.Limit
	offset := response.Offset
	hasMore := offset+limit < total

	return api.AffixRotationHistoryResponse{
		Items:   items,
		Total:   total,
		Limit:   &limit,
		Offset:  &offset,
		HasMore: &hasMore,
	}
}

func (h *AffixHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *AffixHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

